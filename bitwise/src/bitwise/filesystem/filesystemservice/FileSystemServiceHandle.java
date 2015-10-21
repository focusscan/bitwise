package bitwise.filesystem.filesystemservice;

import bitwise.engine.service.BaseServiceHandle;
import bitwise.filesystem.Directory;

public final class FileSystemServiceHandle extends BaseServiceHandle<FileSystemRequest<?>, FileSystemService> {
	protected FileSystemServiceHandle(FileSystemService in_service) {
		super(in_service);
	}
	
	public Directory getWorkpath() {
		return getService().getWorkpath();
	}
}
