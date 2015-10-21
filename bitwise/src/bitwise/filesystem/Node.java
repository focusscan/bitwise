package bitwise.filesystem;

import java.nio.file.Path;

public interface Node {
	public Path getPath();
	public Directory asDirectory();
	public File asFile();
}
