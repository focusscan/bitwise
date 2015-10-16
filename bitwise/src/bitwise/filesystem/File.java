package bitwise.filesystem;

import java.nio.file.Files;
import java.nio.file.Path;

import bitwise.engine.Thing;
import bitwise.filesystem.filesystemservice.FileSystemServiceCertificate;

public class File extends Thing<FileID> {
	private final Path path;
	
	public File(FileSystemServiceCertificate fsCert, Path in_path) {
		super(new FileID());
		if (null == fsCert)
			throw new IllegalArgumentException("FileSystemServiceCertificate");
		if (!Files.isRegularFile(in_path))
			throw new IllegalArgumentException("in_path");
		path = in_path;
	}
	
	public Path getJavaPath() {
		return path;
	}
	
	@Override
	public String toString() {
		return path.toString();
	}
}
