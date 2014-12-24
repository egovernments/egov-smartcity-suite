package com.exilant.exility.pagemanager;
/*
 * PageMaps is the Class that programmers use in their JSP to get 
 * an instance of a PageMap for the page/service they want to handle.
 * 
 * It is a static class. getPageMap(mapName) returns an instance of PageMap for the page
 * 
 * PageMaps caches in all the maps that is loaded. Even if a map is not found, 
 *  it caches that fact, so that it need not try to load it again.
 * 
 * getpageMap(mapName, refresh) would force a refresh. This may be required during testing stage  
 */
import java.util.HashMap;
import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;

import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.XMLLoader;

/**
 * @author raghu.bhandi, Exilant Consulting
 *
 */
public class PageMaps {
	private static final Logger LOGGER = Logger.getLogger(PageMaps.class);
	private static HashMap pageMaps;
	
	public static PageMap getPageMap(String pageName){
		if (pageMaps == null) pageMaps = new HashMap();
		PageMap pm;
		Object obj = null;
		
		obj = pageMaps.get(pageName);
		if (obj == null){
			pm = new PageMap();
			XMLLoader xl = new XMLLoader();
			String fileName = "config/resource/page/" +pageName+".xml"; 
			URL url = EGovConfig.class.getClassLoader().getResource("config/resource/page/" +pageName+".xml");
			//LOGGER.debug("url in pagemaps=================="+url);  
			if(url!=null)
				fileName = url.getFile();
			File file = new File(fileName);
			LOGGER.debug("file name in pagemaps === " + fileName);			
			if (file.isFile()){
				xl.load(fileName, pm);
				pageMaps.put(pageName, pm);
				LOGGER.debug("added toi pagemaps === ");				
			}
		}else pm = (PageMap)obj;
		
		return pm;
	}
	
	private PageMaps() {
		super();
	}
	
	public static void main(String[] args) {
		
		DataCollection dc = new DataCollection();
		dc.addValue("field1", "field1Value");
		dc.addValue("field2", "field2Value");
		dc.addValue("field3", "field3Value");
		dc.addValue("field4", "field4Value");
		dc.addValue("ServerNameForField2", "SomeValue");
		dc.addValue("entity_field1", 12345);
		dc.addValue("entity_field2", true);
		dc.addValue("entity_field3", "entity_field3Value");
		dc.addValue("entity_field4", "entity_field4Value");
		String[][] grid1 = {{"column1", "column2", "column3", "column4"}
			,{"row1Value1", "row1Value2", "row1Value3", "row1Value4"}
			,{"row2Value1", "row2Value2", "row2Value3", "row2Value4"}
			,{"row3Value1", "row3Value2", "row3Value3", "row3Value4"}
			,{"row4Value1", "row4Value2", "row4Value3", "row4Value4"}
		};
		
		String[][] grid2 = {{"row1Value1", "row1Value2", "row1Value3", "row1Value4"}
			,{"row2Value1", "row2Value2", "row2Value3", "row2Value4"}
		};
		dc.addGrid("grid1", grid1);
		dc.addGrid("grid2", grid2);
		PageMap pm = PageMaps.getPageMap("supplierDataIn");
		LOGGER.debug(pm.toJavaScript(dc));
		pm = PageMaps.getPageMap("supplierDataOut");
		LOGGER.debug(pm.toJavaScript(dc));
		pm = PageMaps.getPageMap("junk");
		LOGGER.debug(pm.toJavaScript(dc));
		
	}
}
