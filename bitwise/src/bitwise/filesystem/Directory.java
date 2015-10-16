package bitwise.filesystem;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

import bitwise.engine.Thing;
import bitwise.filesystem.filesystemservice.FileSystemServiceCertificate;
import bitwise.log.Log;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Directory extends Thing<DirectoryID> {
	private final Path path;
	private final ObservableList<Directory> subDirectories = FXCollections.observableArrayList();
	private final ObservableList<File> files = FXCollections.observableArrayList();
	
	public Directory(FileSystemServiceCertificate fsCert, Path in_path) {
		super(new DirectoryID());
		if (null == fsCert)
			throw new IllegalArgumentException("FileSystemServiceCertificate");
		if (!Files.isDirectory(in_path))
			throw new IllegalArgumentException("in_path");
		path = in_path;
	}
	
	public Path getJavaPath() {
		return path;
	}
	
	public ObservableList<Directory> getSubDirectories() {
		return subDirectories;
	}
	
	public ObservableList<File> getFiles() {
		return files;
	}
	
	public void addSubDirectory(FileSystemServiceCertificate fsCert, Directory in) {
		if (null == fsCert)
			throw new IllegalArgumentException("FileSystemServiceCertificate");
		Log.log(this, "%s adding subdir %s", this, in.toString());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				subDirectories.add(in);
			}
		});
	}
	
	public void removeSubDirectory(FileSystemServiceCertificate fsCert, Path in) {
		if (null == fsCert)
			throw new IllegalArgumentException("FileSystemServiceCertificate");
		Log.log(this, "%s removing subdir %s", this, in.toString());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				subDirectories.removeIf(new Predicate<Directory>() {
					@Override
					public boolean test(Directory t) {
						return t.getJavaPath().equals(in);
					}
				});
			}
		});
	}
	
	public void addFile(FileSystemServiceCertificate fsCert, File in) {
		if (null == fsCert)
			throw new IllegalArgumentException("FileSystemServiceCertificate");
		Log.log(this, "%s adding file %s", this, in.toString());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				files.add(in);
			}
		});
	}
	
	public void removeFile(FileSystemServiceCertificate fsCert, Path in) {
		if (null == fsCert)
			throw new IllegalArgumentException("FileSystemServiceCertificate");
		Log.log(this, "%s removing file %s", this, in.toString());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				files.removeIf(new Predicate<File>() {
					@Override
					public boolean test(File t) {
						return t.getJavaPath().equals(in);
					}
				});
			}
		});
	}
	
	@Override
	public String toString() {
		return path.toString();
	}
}
