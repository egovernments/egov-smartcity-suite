/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
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
