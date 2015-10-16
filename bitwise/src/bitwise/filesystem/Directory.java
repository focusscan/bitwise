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

public class Directory extends Thing<DirectoryID> implements Node {
	private final Path path;
	private final ObservableList<Node> children = FXCollections.observableArrayList();
	
	public Directory(FileSystemServiceCertificate fsCert, Path in_path) {
		super(new DirectoryID());
		if (null == fsCert)
			throw new IllegalArgumentException("FileSystemServiceCertificate");
		if (!Files.isDirectory(in_path))
			throw new IllegalArgumentException("in_path");
		path = in_path;
	}
	
	@Override
	public Path getPath() {
		return path;
	}
	
	@Override
	public Directory asDirectory() {
		return this;
	}
	
	@Override
	public File asFile() {
		return null;
	}
	
	public ObservableList<Node> getChildren() {
		return children;
	}
	
	public void addSubDirectory(FileSystemServiceCertificate fsCert, Directory in) {
		if (null == fsCert)
			throw new IllegalArgumentException("FileSystemServiceCertificate");
		Log.log(this, "%s adding subdir %s", this, in.toString());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				children.add(in);
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
				children.add(in);
			}
		});
	}
	
	public void removeChild(FileSystemServiceCertificate fsCert, Path in) {
		if (null == fsCert)
			throw new IllegalArgumentException("FileSystemServiceCertificate");
		Log.log(this, "%s removing subdir %s", this, in.toString());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				children.removeIf(new Predicate<Node>() {
					@Override
					public boolean test(Node t) {
						return t.getPath().equals(in);
					}
				});
			}
		});
	}
}
