package com.exilant.exility.common;

/**
 * @author raghu.bhandi, Exilant Consultiing
 *
 */
public class TaskFailedException extends Exception {
	public TaskFailedException() {
		super();
	}

	public TaskFailedException(String arg0) {
		super(arg0);
	}

	public TaskFailedException(Throwable arg0) {
		super(arg0);
	}

	public TaskFailedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
