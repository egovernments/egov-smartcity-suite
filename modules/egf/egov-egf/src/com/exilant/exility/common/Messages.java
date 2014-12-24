package com.exilant.exility.common;

import java.net.URL;
import java.util.*;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;
/**
 * @author raghu.bhandi
 *
 * Static Class that returns a Message object for a given code, and optional paramaters 
 * At this time, it provides only english text. but can be extended to 
 * return eror message in different langauegs
 */
public class Messages{
	
	private static final Logger LOGGER = Logger.getLogger(Messages.class);
	public static final int UNDEFINED = 0; // Code not defined for this installation
	public static final int IGNORE = 1;
	public static final int INFO = 2;
	public static final int WARNING = 3;
	public static final int ERROR = 4;
/*
 * Singleton pattern is used instad of making the class static.
 * instance carries one instance that the static methods use.
 * There are no public instance methods, not even the constructor.
 *  
 */
 
	private static Messages instance; // stores the singleton instance of this class

	// array contiaining text for sevirity. This is  only in English. To be made 'multi-lingual' as and when required
	private String[] sevirityText = {"UNDEFINED", "", "Information : ", "WARNING : ", "ERROR : ", "UNDEFINED"};
	
	protected Message message;
	public  HashMap messages; //contais all the msesages defined for this application. It is stored once.

/*
 * Not to be instantiated by public
 */
	
	private Messages(){
		// load the messages from xml file
		this.messages = new HashMap();
		XMLLoader xl = new XMLLoader();
		URL url = EGovConfig.class.getClassLoader().getResource("config/resource/messages.xml");
		//LOGGER.debug("url in Messages=================="+url);
		xl.load(url.toString(), this);
	}
/*
 * getMessage is defined with different number of parameters for convinience
 * All of them in turn call getMessage(String code, String[] parameters
 */
	public static Message getMessage(String code){
		String[] p ={};
		return getMessage(code, p);
	}
	
	public static Message getMessage(String code, String p1){
		String[] p ={p1};
		return getMessage(code, p);
	}
	
	public static Message getMessage(String code, String p1, String p2){
		String[] p ={p1,p2};
		return getMessage(code, p);
	}
	
	public static Message getMessage(String code, String p1, String p2, String p3){
		String[] p ={p1,p2,p3};
		return getMessage(code, p);
	}
	
	public static Message getMessage(String code, String p1, String p2, String p3, String p4){
		String[] p ={p1,p2,p3,p4};
		return getMessage(code, p);
	}
	
	public static Message getMessage(String code, String[] parameters){
		if (instance == null) instance = new Messages();
		return instance.get(code, parameters);
		
	}
/*
 * Helper method for all variations of getMessgae()
 */
	private Message get(String code, String[] params){
		Message messageToReturn = new Message();
		messageToReturn.id = code;
		
		// get the message for the code from the hashmap
		Object o = this.messages.get(code);

		try{
			Message m = (Message)o; //null pointer, or non-message will throw exception 
			messageToReturn.sevirity  = m.sevirity; 
			StringBuffer sbf = new StringBuffer(this.sevirityText[m.sevirity]);
			sbf.append(m.text);

			if (params != null){ //replace @1, @2 etc in the message with the paramater supplied
				int len = params.length;
				int j = 0;
				for (int i=0; i<len; i++){
					j = sbf.indexOf("@" + (i+1));
					if (j>0)sbf.replace(j,j+2, params[i]);
				}
			}
			messageToReturn.text = sbf.toString();
			
		}catch(Exception e){
			messageToReturn.sevirity = Messages.UNDEFINED;
			messageToReturn.text = code + " is not a valid message code"; // what about language?? To have this by langauge as well..
		}
		
		return messageToReturn;
	}
	
	public static void main (String[] args){
		LOGGER.debug(Messages.getMessage("exilNoServiceName" ,"p11111111", "p2", "p3"));		
	}

}
