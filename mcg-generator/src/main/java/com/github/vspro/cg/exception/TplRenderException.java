package com.github.vspro.cg.exception;

public class TplRenderException extends RuntimeException {

	public TplRenderException(String message) {
		super(message);
	}

	public TplRenderException(String message, Throwable cause) {
		super(message, cause);
	}
}
