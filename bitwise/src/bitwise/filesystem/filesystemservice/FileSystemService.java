package bitwise.filesystem.filesystemservice;

import java.nio.file.Path;

import bitwise.engine.service.BaseRequest;
import bitwise.engine.service.BaseService;
import bitwise.engine.supervisor.SupervisorCertificate;
import bitwise.filesystem.Directory;

public final class FileSystemService extends BaseService<FileSystemServiceHandle> {
	private final FileSystemServiceCertificate cert = new FileSystemServiceCertificate();
	private final FileSystemServiceHandle serviceHandle;
	private final Directory workpath;
	private FileSystemTask fileSystemTask = null;
	
	public FileSystemService(SupervisorCertificate supervisorCert, Path in_workpath) {
		super();
		if (null == supervisorCert)
			throw new IllegalArgumentException("SupervisorCertificate");
		serviceHandle = new FileSystemServiceHandle(this);
		workpath = new Directory(cert, in_workpath);
	}
	
	public Directory getWorkpath() {
		return workpath;
	}
	
	@Override
	public FileSystemServiceHandle getServiceHandle() {
		return serviceHandle;
	}

	@Override
	protected boolean onStartService() {
		fileSystemTask = new FileSystemTask(this, cert, workpath);
		this.addServiceTask(fileSystemTask);
		return true;
	}

	@Override
	protected void onStopService() {
		
	}

	@Override
	protected void onRequestComplete(BaseRequest<?, ?> in) {
	}
}
