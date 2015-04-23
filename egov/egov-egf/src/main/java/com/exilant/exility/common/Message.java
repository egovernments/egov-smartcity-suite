package com.exilant.exility.common;

/**
 * @author raghu.bhandi
 * Holds code, sevirity and text for a message.
 * This is more of a data structure rather than an object.
 * Setters not provided, as this is not to be used directly
 * Programmers are advised to use MessageList instead of this basic message
 */
public class Message {
	/*
	 * these constants are repeated in Messages as well. 
	 */

	protected String id;
	protected String text;
	protected int sevirity;
		
	protected Message(){ //will be usd by Messages only. No one else should
			//be calling new()
	}

	public String toString(){
		return text;
	}
	/**
	 * @return
	 */
	public String getCode() {
		return id;
	}

	/**
	 * @return
	 */
	public int getSevirity() {
		return sevirity;
	}

	/**
	 * @return
	 */
	public String getText() {
		return text;
	}

}
