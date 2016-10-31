package org.egov.assets.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

import org.apache.commons.lang.RandomStringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;

public class AssetCategoryType {
	
	
	
	private String acquisitionMode;
	private String waterComponents;
	private String landSurveyNumber;
	private BigDecimal area;
	private String description;

	
	
	public static void main(String[] args) {
		String[] aMode={"Purchase","Tender","Construction","Donation","Transfer from other department","Acquired"};
		String[] wComp={"Infrastructural","Treatment","Storage reservoir","Pipeline"};
		AssetCategoryType ac=null;
		PrintWriter writer=null;
		String jsonStr;
		try {
			 writer = new PrintWriter("/home/mani/jsonbsql.sql");
		} catch (FileNotFoundException e) {


		}
		for(int i=0;i<1000;i++)
		{
			ac=new AssetCategoryType();
			ac.setAcquisitionMode(aMode[i%6]);
			ac.setWaterComponents(wComp[i%4]);
			ac.setLandSurveyNumber(RandomStringUtils.randomAlphanumeric(20).toUpperCase());
			ac.setArea(BigDecimal.valueOf(Math.random()));
			ac.setDescription(""+i);
			jsonStr=ac.serialize(ac);
			writer.write("insert into Foo ( id,category_type) values(nextval('seq_foo'),'"+jsonStr+"');\n");
		}
		

	} 
	
	public String serialize(AssetCategoryType obj)
	{
		ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        String jsonResponse="";
		try {
			jsonResponse = objectMapper.writeValueAsString(obj);
		} catch (IOException e) {


		}
        return jsonResponse;
	}
	

	public String getAcquisitionMode() {
		return acquisitionMode;
	}



	public void setAcquisitionMode(String acquisitionMode) {
		this.acquisitionMode = acquisitionMode;
	}



	public String getWaterComponents() {
		return waterComponents;
	}



	public void setWaterComponents(String waterComponents) {
		this.waterComponents = waterComponents;
	}



	public String getLandSurveyNumber() {
		return landSurveyNumber;
	}



	public void setLandSurveyNumber(String landSurveyNumber) {
		this.landSurveyNumber = landSurveyNumber;
	}



	public BigDecimal getArea() {
		return area;
	}



	public void setArea(BigDecimal area) {
		this.area = area;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}

}
