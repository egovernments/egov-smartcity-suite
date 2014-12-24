package com.exilant.exility.common;

import java.util.*; 

import org.apache.log4j.Logger;

/**
 * @author raghu.bhandi
 *
 * Example to demonstrate the use of DefaultDefinition
 */
public class TestXMLLoading /*extends DefaultObject */{
	private static final Logger LOGGER = Logger.getLogger(TestXMLLoading.class);
// fields inherited from DefaultObject
//	protected String type;
//	protected ArrayList children;
//	protected HashMap attributes;

	//String type;
	String id ;
	String type;
	protected float grossMargin;
	protected String img;
	protected boolean booleanValue;
	// a child of same type. 
	// in my first version, I was initializing this field with new TestXMLLoading()
	// guess what, that resulted in an infinite loop !!
	// XMLLoader is smart enough to initialize it when required. 
	// just declare and leave it to XMLLoader
	protected TestXMLLoading parameter; // = new TestXMLLoading(); 
	public TestXMLLoading node; //type declararion for nodes
	public ArrayList nodes;
	protected TestXMLLoading child;
	protected HashMap childs;
	protected TestXMLLoading[] childInArrays;
	protected HashMap attributes; 
	

	public TestXMLLoading() {
		super();
	}


	
	public static void main(String[] args) {
		TestXMLLoading de = new TestXMLLoading();
		XMLLoader xl = new XMLLoader();
		xl.load("resource/testXMLLoading.xml", de);
		//XMLGenerator xg =  XMLGenerator.getInstance();
		LOGGER.debug("Dumping using XML Generator");
		
	}
}
