package bitwise.filesystem.filesystemservice;

import bitwise.engine.service.BaseRequest;
import bitwise.engine.service.BaseRequester;

public abstract class FileSystemRequest<R extends BaseRequester> extends BaseRequest<FileSystemService, R> {
	protected FileSystemRequest(FileSystemService in_service, R in_requester) {
		super(in_service, in_requester);
	}
}
