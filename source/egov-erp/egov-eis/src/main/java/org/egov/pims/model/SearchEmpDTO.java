package org.egov.pims.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infra.admin.master.entity.Department;
import org.egov.pims.commons.DesignationMaster;

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
		
		ArrayList deptMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-department");
		Map deptmap = getDepartmentMap(deptMasterList);
		String dept = (String)deptmap.get(getDepID());
		
		return dept;

	}
	public String getDesignation()
	{
		String desig = "N/A";
		ArrayList designationMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-DesignationMaster");
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
	public Map getDepartmentMap(ArrayList list)
	{
		Map depMap = new HashMap();
		for(Iterator iter = list.iterator();iter.hasNext();)
		{
			Department department = (Department)iter.next();
			depMap.put(department.getId(), department.getName());
		}
		return depMap;
	}
	private Map getDsig(ArrayList list)
	{
		Map desMap = new HashMap();
		for(Iterator iter = list.iterator();iter.hasNext();)
		{
			DesignationMaster desig = (DesignationMaster)iter.next();
			desMap.put(desig.getDesignationId(), desig.getDesignationName());
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
