package bitwise.engine.service.requests;

public enum RequestStatus {
	Created,
	Cancelled,
	EnqueuedServe,
	Serving,
	ServingInterrupted,
	ServingException,
	Served,
	EnqueuedEpilogue,
	Epilogue,
	Epilogued,
	EpilogueInterrupted,
	EpilogueException,
}
