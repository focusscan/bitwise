package bitwise.filesystem.filesystemservice;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;

import bitwise.log.Log;
import bitwise.engine.service.BaseServiceTask;
import bitwise.filesystem.Directory;
import bitwise.filesystem.File;

public class FileSystemTask extends BaseServiceTask<FileSystemService> {
	private final FileSystemServiceCertificate cert;
	private final Directory workpath;
	private final HashMap<WatchKey, Directory> directories = new HashMap<>();
	
	public FileSystemTask(FileSystemService in_service, FileSystemServiceCertificate in_cert, Directory in_workpath) {
		super(in_service);
		cert = in_cert;
		workpath = in_workpath;
	}

	private void initializeDirectory(WatchService watcher, Directory in) throws IOException {
		// Enumerate the directory's contents
		DirectoryStream<Path> stream = Files.newDirectoryStream(in.getPath());
		for (Path preChild : stream) {
			Path child = in.getPath().resolve(preChild);
			if (Files.isDirectory(child)) {
				Directory newDir = new Directory(cert, child);
				in.addSubDirectory(cert, newDir);
				initializeDirectory(watcher, newDir);
			}
			else if (Files.isReadable(child)) {
				in.addFile(cert, new File(cert, child));
			}
		}
		
		// Track its changes
		WatchKey workpathKey = in.getPath().register(watcher,
				StandardWatchEventKinds.ENTRY_CREATE,
				StandardWatchEventKinds.ENTRY_DELETE);
		directories.put(workpathKey, in);
	}
	
	@Override
	protected void taskMain() throws InterruptedException {
		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();
			initializeDirectory(watcher, workpath);
			
			while (!isCancelled()) {
				WatchKey key = watcher.take();
				Log.log(getService(), "Got key %s", key);
				Directory dir = directories.get(key);
				if (null != dir) {
					Log.log(getService(), "Key %s is for directory %s", key, dir);
					
					for (WatchEvent<?> preEvent : key.pollEvents()) {
						if (preEvent.kind() == StandardWatchEventKinds.OVERFLOW)
							continue;
						
						@SuppressWarnings("unchecked")
						WatchEvent<Path> event = (WatchEvent<Path>) preEvent;
						
						if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
							Path path = dir.getPath().resolve(event.context());
							Log.log(getService(), "Create path %s", path);
							if (Files.isDirectory(path)) {
								Directory newDir = new Directory(cert, path);
								dir.addSubDirectory(cert, newDir);
								initializeDirectory(watcher, newDir);
							}
							else if (Files.isRegularFile(path)) {
								dir.addFile(cert, new File(cert, path));
							}
						}
						else if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
							Path path = dir.getPath().resolve(event.context());
							Log.log(getService(), "Delete path %s", path);
							dir.removeChild(cert, path);
						}
					}
				}
				
				if (!key.reset()) {
					directories.remove(key);
				}
			}
			
			watcher.close();
		} catch (IOException e) {
			Log.logException(getService(), e);
		}
	}
}
