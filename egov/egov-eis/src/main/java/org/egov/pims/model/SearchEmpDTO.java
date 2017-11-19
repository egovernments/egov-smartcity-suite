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
package org.egov.pims.model;

import org.egov.infra.admin.master.entity.Department;
import org.egov.pims.commons.Designation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SearchEmpDTO implements java.io.Serializable
{
	String code = null;
	String name = null;
	Integer iD = null;
	Integer desID = null;
	Integer prdiD = null;
	Integer depID = null;
	java.sql.Date fromDate = null;
	java.sql.Date toDate = null;
	
		public SearchEmpDTO(Integer desID,Integer depID,
    		String code,
    		String name,Integer iD)

	{
		this.code=code;
		this.desID=desID;
		this.depID=depID;
		this.name=name;
		this.iD=iD;
	}
		public SearchEmpDTO(Integer desID,Integer depID,
	    		String code,
	    		String name,Integer iD,Integer prdiD)

		{
			this(desID,depID,code,name,iD);
			this.prdiD= prdiD;
		}
		public SearchEmpDTO(Integer desID,Integer depID,
	    		String code,
	    		String name,Integer iD,java.sql.Date fromDate,java.sql.Date toDate)

		{
			this(desID,depID,code,name,iD);
			this.fromDate=fromDate;
			this.toDate=toDate;
		}
	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	public Integer getID()
	{
		return iD;
	}
	/**
	 * @param name The name to set.
	 */
	public void setID(Integer iD)
	{
		this.iD = iD;
	}
	/**
	 * @return Returns the depID.
	 */
	public Integer getDepID() {
		return depID;
	}
	/**
	 * @param depID The depID to set.
	 */
	public void setDepID(Integer depID) {
		this.depID = depID;
	}
	/**
	 * @return Returns the desID.
	 */
	public Integer getDesID() {
		return desID;
	}
	/**
	 * @param desID The desID to set.
	 */
	public void setDesID(Integer desID) {
		this.desID = desID;
	}
	
	public String getDepartment()
	{
		
		List deptMasterList=Collections.emptyList();//EgovMasterDataCaching.getInstance().get("egEmp-department");
		Map deptmap = getDepartmentMap(deptMasterList);
		String dept = (String)deptmap.get(getDepID());
		
		return dept;

	}
	public String getDesignation()
	{
		String desig = "N/A";
		List designationMasterList=Collections.emptyList();//EgovMasterDataCaching.getInstance().get("egEmp-DesignationMaster");
		Map mapOfDesignation = getDsig(designationMasterList);
		if(mapOfDesignation.get(getDesID())!=null)
			desig = (String)mapOfDesignation.get(getDesID());
	return desig;
	}
	public java.sql.Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(java.sql.Date fromDate) {
		this.fromDate = fromDate;
	}
	public java.sql.Date getToDate() {
		return toDate;
	}
	public void setToDate(java.sql.Date toDate) {
		this.toDate = toDate;
	}
	public Map getDepartmentMap(List list)
	{
		Map depMap = new HashMap();
		for(Iterator iter = list.iterator();iter.hasNext();)
		{
			Department department = (Department)iter.next();
			depMap.put(department.getId(), department.getName());
		}
		return depMap;
	}
	private Map getDsig(List list)
	{
		Map desMap = new HashMap();
		for(Iterator iter = list.iterator();iter.hasNext();)
		{
			Designation desig = (Designation)iter.next();
			desMap.put(desig.getId(), desig.getName());
		}
		return desMap;
		
	}
	public Integer getPrdiD() {
		return prdiD;
	}
	public void setPrdiD(Integer prdiD) {
		this.prdiD = prdiD;
	}
}
