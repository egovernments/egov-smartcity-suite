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
/*
 * Created on Mar 29, 2008
 * @author Iliyaraja.S
 */

package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

public class Egw_Works_Deductions
{
	private static final Logger LOGGER=Logger.getLogger(Egw_Works_Deductions.class);

	private String id =null;
	private String worksdetailid =null;
	private String glcodeid =null;
	private String amount = null;
	private String perc = null;
	private String dedType="";
	private String tdsId = null;
	private String lastmodifieddate = "";

	private String created ="";



	EGovernCommon cm = new EGovernCommon();

	private String updateQuery="UPDATE Egw_Works_Deductions SET";
	private boolean isId=false, isField=false;

	public void setId(String aId){ id = aId;isId=true;  }
	public int getId() {return Integer.valueOf(id).intValue(); }

	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
		updateQuery = updateQuery + " amount = "+ amount + ","; isField = true;
	}

	public String getDedType() {
		return dedType;
	}
	public void setDedType(String dedType) {
		this.dedType = dedType;
		updateQuery = updateQuery + " dedType = '"+ dedType + "',"; isField = true;
	}
	public String getGlcodeid() {
		return glcodeid;
	}
	public void setGlcodeid(String glcodeid) {
		this.glcodeid = glcodeid;
		updateQuery = updateQuery + " glcodeid = "+ glcodeid + ","; isField = true;
	}

	public String getLastmodifieddate() {
		return lastmodifieddate;
	}
	public void setLastmodifieddate(String lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
		updateQuery = updateQuery + " lastmodifieddate = '" + lastmodifieddate + "',"; isField = true;
		}

	public String getPerc() {
		return perc;
	}
	public void setPerc(String perc) {
		this.perc = perc;
		updateQuery = updateQuery + " perc = "+ perc + ","; isField = true;
	}
	public String getTdsId() {
		return tdsId;
	}
	public void setTdsId(String tdsId) {
		this.tdsId = tdsId;
		updateQuery = updateQuery + " tdsId = "+ tdsId + ","; isField = true;
	}
	public String getWorksdetailid() {
		return worksdetailid;
	}
	public void setWorksdetailid(String worksdetailid) {
		this.worksdetailid = worksdetailid;
		updateQuery = updateQuery + " worksdetailid = "+ worksdetailid + ",";
		isId=true;
		isField = true;
	}


public void insert(Connection connection) throws SQLException,TaskFailedException
	{
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("Egw_Works_Deductions")) );
		created = cm.getCurrentDate();
		try{
			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			String currentdate = formatter.format(sdf.parse( created ));
			setLastmodifieddate(currentdate);
		}catch(Exception e){throw new TaskFailedException(e.getMessage());}



		String insertQuery = "INSERT INTO Egw_Works_Deductions (id, worksdetailid, glcodeid, amount, perc, " +
					"dedType, tdsId, lastmodifieddate) " +
					"VALUES (" + id + ", " + worksdetailid + ", " + glcodeid + ", " + amount + ", "	+ perc + ","
					+"'" + dedType + "', " + tdsId + ","+"'"+lastmodifieddate+"')";
        if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		Statement statement = connection.createStatement();
		statement.executeUpdate(insertQuery);
		statement.close();

	} //insert

	public void update (Connection connection) throws SQLException,TaskFailedException
	{
		created = cm.getCurrentDate();
		String currentdate="";
		try{
			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			currentdate = formatter.format(sdf.parse( created ));
		}
		catch(Exception e){throw new TaskFailedException(e.getMessage());}
		setLastmodifieddate(currentdate);

		if(isId && isField)
		{
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE worksdetailid = " + worksdetailid;
            if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
			Statement statement = connection.createStatement();
			statement.executeUpdate(updateQuery);
			statement.close();
			updateQuery="UPDATE Egw_Works_Deductions SET";
		}
	} // update

} // class





