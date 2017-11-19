/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

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
