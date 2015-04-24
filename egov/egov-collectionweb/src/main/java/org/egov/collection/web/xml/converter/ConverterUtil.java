package org.egov.erpcollection.web.xml.converter;

import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public final class ConverterUtil {
	private ConverterUtil(){
		
	}
	public static void createNode(HierarchicalStreamWriter writer, String nodeName, String nodeValue) {
		
		String newNodeName="";
		
		writer.startNode(nodeName);
		
		if(nodeValue!=null){
			newNodeName=nodeValue;
		}
		
        writer.setValue(newNodeName);
        writer.endNode();
	}
}
