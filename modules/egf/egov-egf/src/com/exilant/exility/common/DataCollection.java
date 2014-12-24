package com.exilant.exility.common;

import java.util.*;
//import java.lang.reflect.Array;

/**
 * @author raghu.bhandi
 * Class designed to be a common carrier of generic data across objects across layers
 * This is also inteded to be carried across to the client tier
 * Recommendation is that we use a naming convention entity_attribute as the name,
 * 	so that they are unique acros all entities within an enterprise.
 * 
 * Allows storage of primitive data type, including String, and single/two dimensional array of these.
 * It internally has a MessageList object, but provides its own methods rather than exposing the MessageList Object
 *  
 * USAGE:
 * 
 */

public class DataCollection {
	/*
	 * all primitive data stored as name/value pair in a hashmap
	 */
	public HashMap values;
	
	//for all single dimension arraya
	protected HashMap valueLists;
	
	// two dimension arrays
	protected HashMap grids;
	
	//three dimensional arrays :-) NO NO, No.. three dimensional arrays are not allaowed at Exilant
	// list of messages (error message, warnings etc..
	protected MessageList messageList;

	/**
	 * 
	 */
	public DataCollection() {
		super();
		this.values  	= new HashMap();
		this.valueLists	= new HashMap();
		this.grids		= new HashMap();
		this.messageList= new MessageList();
	}
/**********
 * 
 * @param name 
 * @param value Should be a prmitive data type or String. Other types are not added
 * @return value is returned is added, else null is returned
 */	
	public void addValue(String name, String value){
		addValueHelper(name,value);
	}

	public void addValue(String name, int value){
		addValueHelper(name,Integer.valueOf(value));
	}

	public void addValue(String name, long value){
		addValueHelper(name,Long.valueOf(value));
	}

	public void addValue(String name, float value){
		addValueHelper(name,new Float(value));
	}

	public void addValue(String name, double value){
		addValueHelper(name,new Double(value));
	}

	public void addValue(String name, boolean value){
		addValueHelper(name,Boolean.valueOf(value));
	}
	
	private void addValueHelper(String name, Object value){
		if (null == this.values)this.values = new HashMap();
		this.values.put(name, value);
	}

	public String getValue(String name){
		Object obj = this.values.get(name); 
		if (null == obj) return "";
		return obj.toString();
	}

	public float getFloat(String name){
		Object obj = this.values.get(name); 
		if (obj instanceof Number) return ((Number) obj).floatValue();
		try{
			return Float.parseFloat(obj.toString());
		}catch (Exception e1){
				return 0;
		}
	}
	
	public double getDouble(String name){
		Object obj = this.values.get(name); 
		if (obj instanceof Number) return ((Number) obj).doubleValue();
		try{
			return Double.parseDouble(obj.toString());
		}catch (Exception e1){
				return 0;
		}
	}
	
	
	public int getInt(String name){
		Object obj = this.values.get(name); 
		if (obj instanceof Number) return ((Number) obj).intValue();
		try{
			return Integer.parseInt(obj.toString());
		}catch (Exception e1){
				return 0;
		}
	}
	
	public long getLong(String name){
		Object obj = this.values.get(name); 
		if (obj instanceof Number) return ((Number) obj).longValue();
		try{
			return Long.parseLong(obj.toString());
		}catch (Exception e1){
			return 0;
		}
	}
	
	public boolean getBoolean(String name){
		Object obj = this.values.get(name); 
		if (obj instanceof Boolean) return ((Boolean) obj).booleanValue();
		if( obj.toString().equalsIgnoreCase("true") 
				|| obj.toString().equalsIgnoreCase("yes") 
				|| obj.toString().equals("1") )return true;
		return false;
	}
	
	public void addValueList(String name, String[] anArray){
		this.valueLists.put(name,anArray);
	}

	public String[] getValueList(String name){
		try{
			return (String[])this.valueLists.get(name);
		}catch(Exception e){}
		String[] arr = new String[0]; 
		return arr;
	}
	
	public void addGrid(String name, String[][] grid){
		this.grids.put(name,grid);
	}

	public String[][] getGrid(String name){
		Object obj = this.grids.get(name);
		try {
			return (String[][])obj;
		} catch (Exception e) {}
		String[][] arr = new String[0][0];
		return arr;
	}
	
	public boolean hasName(String name){
		return this.values.containsKey(name);
	}

	public boolean hasList(String name){
		return this.valueLists.containsKey(name);
	}

	public boolean hasgrid(String name){
		return this.grids.containsKey(name);
	}

/************
 * 
 */	
	public int addMessage(String code){
		return this.messageList.add(code);
	}

	public int addMessage(String code, String p1){
		return this.messageList.add(code, p1);
	}

	public int addMessage(String code, String p1, String p2){
		return this.messageList.add(code, p1, p2);
	}

	public int addMessage(String code, String p1, String p2, String p3){
		return this.messageList.add(code, p1, p2, p3);
	}

	public int addMessage(String code, String p1, String p2, String p3, String p4){
		return this.messageList.add(code, p1, p2, p3, p4);
	}

	public int addMessage(String code, String[] parms){
		return this.messageList.add(code, parms);
	}

	public int getSevirity(){
		return messageList.getSevirity();
	}
	
	public String getMessageText(){
		return messageList.toString();
	}
	
	public MessageList getMessageList(){
		return messageList;
	}
	
	public Iterator getFieldNames(){
		return this.values.keySet().iterator();	
	}
	
	public Iterator getListNames(){
		return this.valueLists.keySet().iterator();	
		
	}

	public Iterator getGridNames(){
		return this.grids.keySet().iterator();	
	}
	
	public String toString(){
		return XMLGenerator.getInstance().toXML(this, "DataCollection", "");
	}
/*
	public static void main(String[] args) {
		DataCollection dc = new DataCollection();
		dc.addValue("abcd" , "ABCD");
		dc.addValue("abcd" , "MNOP");
		dc.addValue("anInt" , 12);
		dc.addValue("aFloat" , 12.12345);
		String[] a = {"1","2","3","4","5","6","7"};
		String[] b = {"a","b","c"};
		String[][] c = {{"a","b","c"},{"w","x","y","z"}};
		String[][] q = {a,{"10","11","12","23","24"}};
		dc.addValueList("an int Array",a );
		dc.addValueList("a String Array",b );
		dc.addGrid("a grid",c );
		dc.addGrid("a String grid",c );
		dc.addGrid("an int grid",q );
		dc.addMessage("a" ,"a", "b", "c", "d");
		dc.addMessage("b" , b);
		dc.addMessage("c");
		dc.addMessage("d");
		dc.addMessage("e");
		dc.addMessage("f");
		LOGGER.debug("values has " + dc.values.size() + " entries"); 
		LOGGER.debug("ValueList has " + dc.valueLists.size() + " entries"); 
		LOGGER.debug("Grid has " + dc.grids.size() + " entries"); 
		LOGGER.debug("value of abcd is " +dc.getValue("abcd"));
		LOGGER.debug("anInt = " +dc.getInt("anInt"));
		LOGGER.debug("aFloat = " +dc.getFloat("aFloat"));
		LOGGER.debug("JUNK = " +dc.getValue("JUNK"));
		LOGGER.debug("an invalid value = " +dc.getValue("an invalid value"));
		
		LOGGER.debug("value of an int Array " +dc.getValueList("an int Array"));
		LOGGER.debug("value of a String Array " +dc.getValueList("a String Array"));
		LOGGER.debug("value of abcd as avluelist " +dc.getValueList("abcd"));
		LOGGER.debug("value of a grid is " +dc.getGrid("a grid"));
		LOGGER.debug("value of unkown grid is " +dc.getGrid("unkknown grid"));
		LOGGER.debug("Message sevirity is" + dc.getSevirity());
		LOGGER.debug("Message List is " +dc.getMessageText());
		LOGGER.debug("And taking the message lis I got : ");
		LOGGER.debug(dc.getMessageList());
		LOGGER.debug(" By the way, let me try XML Generator");
		LOGGER.debug(XMLGenerator.getInstance().toXML(dc, "MyDataCollection", ""));

		}
*/	
}
