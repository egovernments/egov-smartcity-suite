package com.exilant.exility.common;

import com.exilant.exility.common.Message;
import java.util.ArrayList;
/**
 * @author raghu.bhandi
 * Accumulates a list of Error Objects, as they are added
 * This is designed specifically to collect several messages,
 *  possibly from different routines. It can be 'passed around' by
 *  objects to cllect all messages before passing it to user interface.
 *
 * Programmers; Avoid using this directly. Use DataCollection instead which wraps
 *   this object and provides convinent methods
 */

public class MessageList {
	protected ArrayList messages;
	protected int highestSevirity = Messages.IGNORE; 

	public MessageList() {
		this.messages = new ArrayList();
	}
	
	/****************
	 * Adds an error code to mesaage list
	 * @param code error code
	 * @return sevirity of the messaage that is added
	 * 		Messages.XXXX static ints are available for meaningful name for sevirity
	 */

	public int add(String code){
		return this.addHelper(code, null);	
	}
	
/******************
 * Adds an error code with additional information about the error
 * @param code
 * @param params
 * @return
 ***********************/
	public int add(String code, String[] params){
		return this.addHelper(code, params);	
	}
/*
 * several add() methods are provided for users to call with different number
 *  of additional inormation. Up to 4 are suppoerted directly, betond which
 *    one is forced to use an array
 */
	public int add(String code, String param1){
		String[] p = {param1};
		return this.addHelper(code, p);	
	}

	public int add(String code, String param1, String param2){
		String[] p = {param1, param2};
		return this.addHelper(code, p);	
	}

	public int add(String code, String param1, String param2, String param3){
		String[] p = {param1, param2, param3};
		return this.addHelper(code, p);	
	}

	public int add(String code, String param1, String param2, String param3, String param4){
		String[] p = {param1, param2, param3, param4};
		return this.addHelper(code, p);	
	}
/******************************
 * Returns the highest sevirity of all messages that are added so far
 * @return
 */
	public int getSevirity(){
		return this.highestSevirity;
	}
	
	public int size(){
		return this.messages.size();
	}
	
	public String getMessage(int id){
		if (id >= this.messages.size())return "";
		return this.messages.get(id).toString();
	}
/********************
 * toString() overridden to return a text that reflect the contents
 */
	public String toString(){
		StringBuffer sbf = new StringBuffer();
		int l = messages.size();
		for(int i=0; i<l; i++){
			if (i > 0) sbf.append("\n");
			sbf.append(i+1);
			sbf.append(". ");
			sbf.append(((Message)messages.get(i)).toString());
		}
		return sbf.toString();
	}
	
	
	private int addHelper(String code, String[] parameters){
		Message m = Messages.getMessage(code,parameters); 
		if (m.sevirity != Messages.IGNORE){ // if none, this is not a message at all
			messages.add(m);
			if (m.sevirity > highestSevirity )highestSevirity = m.sevirity;
		}
		return m.sevirity;
	}
	/*
	 * Test routine
	 *
	 */
/*
	public static void main (String[] args){
		MessageList ml = new MessageList();
		ml.add("exilNoServiceName", "pp1", "pp2", "pp3");
		if(LOGGER.isDebugEnabled())     LOGGER.debug(" sevirity of ML " + ml.getSevirity());
		if(LOGGER.isDebugEnabled())     LOGGER.debug(ml);				
	}
*/
}
