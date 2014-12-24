package org.egov;


public class InvalidAccountHeadException extends Exception {


	public InvalidAccountHeadException()
	{		
		super();
	}
	
	public InvalidAccountHeadException(String msg)
	{		
		super(msg);
	}

	public InvalidAccountHeadException(String msg, Throwable th)
	{		
		super(msg,th);
	}
}
