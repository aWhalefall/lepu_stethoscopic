package com.core.lib.core;

public interface AsyncRequest {
	void RequestComplete(Object object, Object data);

	void RequestError(final Object object, int errorId, String errorMessage);
}
