<%@ page errorPage="/error/error.jsp" language="java" import="org.egov.EGOVRuntimeException,org.egov.infstr.commons.Module,java.util.Enumeration,java.util.HashMap,java.util.Iterator,java.util.List,java.util.Map" %>
<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%String values = "";
	List list=null;
	String xmlConfigName=null;
	String qryLevel=null;
	String qryName=null;
	String target="";
	Map mapNamedParams=new HashMap();
	
	Map map=request.getParameterMap();
	String queryParamName=null;
	Enumeration enumParamNames=request.getParameterNames();
	while(enumParamNames.hasMoreElements()) {
		queryParamName=(String)enumParamNames.nextElement();
		if(queryParamName.equalsIgnoreCase("xmlConfigName"))
		{
		xmlConfigName=request.getParameter(queryParamName);
		}
		else if(queryParamName.equalsIgnoreCase("qryLevel"))
		{
		qryLevel=request.getParameter(queryParamName);
		}
		else if(queryParamName.equalsIgnoreCase("qryName"))
		{
		qryName=request.getParameter(queryParamName);
		}
		else if(queryParamName.equalsIgnoreCase("text"))
		{
		mapNamedParams.put(queryParamName,request.getParameter(queryParamName).toUpperCase()+"%");	
		}
		else
		mapNamedParams.put(queryParamName,request.getParameter(queryParamName));
	}
	
	AjaxQueryUtil ajaxQueryUtil = new AjaxQueryUtil();
	list=ajaxQueryUtil.executeAjaxQuery(xmlConfigName,qryLevel,qryName,mapNamedParams);
	
	if(list!=null && !list.isEmpty()) {
		Iterator itr=list.iterator();
		StringBuffer result=new StringBuffer();
		StringBuffer id=new StringBuffer();
		StringBuffer name=new StringBuffer();
		StringBuffer narration=new StringBuffer();
		int i = 0;
		
		while(itr.hasNext()){
		 	Object[] rs=(Object[])itr.next();
			if(i > 0) {
				id.append("+");
				id.append(rs[0]);
				name.append("+");
				name.append(rs[1]);
				narration.append("+");
				narration.append(rs[2]);
			} else {
				id.append(rs[0]);
				name.append(rs[1]);
				narration.append(rs[2]);
			}
			i++;	
		}
		result.append(id);
		result.append("^");
		result.append(name);
		result.append("^");
		result.append(narration);
		result.append("^");
		values=result.toString();
	}
	response.setHeader("Cache-Control", "no-cache");
	response.setContentType("text/plain");
	out.print(values);%>