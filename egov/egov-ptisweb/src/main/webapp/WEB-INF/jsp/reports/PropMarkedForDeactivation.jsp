<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>					
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Report - List Of Properties Marked For Deactivation</title>
 	<link href="<cdn:url value='/resources/css/propertytax.css'/>" rel="stylesheet" type="text/css" />
	<link href="<cdn:url value='/resources/css/commonegov.css'/>" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<cdn:url value='/resources/erp2/js/ajaxCommonFunctions.js'/>"></script>
 

 <script type="text/javascript">
function checkBeforeSubmit()
 {
 	var zoneNo = document.propMarkedDeactiveForm.zoneNumber.value;
 	var WardNo = document.propMarkedDeactiveForm.divNumber.value;
 	if(zoneNo==null || zoneNo=="" || zoneNo=="0")
   		{
   		bootbox.alert("Please Select a Zone Number");
   		return false;
   		}
   	if(WardNo==null || WardNo=="" || WardNo=="0")
   		{
   		bootbox.alert("Please Select a Ward Number");
   		return false;
   		}
 }
 </script>
  </head>
  
<html:form action="/reports/BeforeListOfPropMarkedForDeactivationAction.do">
  <body>
<div class="formmainbox"><div class="formheading"></div>
  <div class="headingbg"><bean:message key="listOfPropMarkDeactive" ></bean:message></div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">

  <tr>
    <td class="greybox" width="10%">&nbsp;</td>
    <td class="greybox" width="8%"><bean:message key="zonenum"/>:<span class="mandatory">*</span></td>
    <td class="greybox">
    <html:select property="zoneNumber" styleId = "zoneNumber" onchange = "loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'eg_boundary', 'ID_BNDRY', 'name', 'parent=#1 order by name', 'zoneNumber', 'divNumber');" styleClass="selectnew">
    	<html:option value="0">--Choose--</html:option>
    	<html:options collection="ZoneBoundary" property="id" labelProperty="name" />	          
	</html:select>
    </td>
    <td class="greybox" width="8%"><bean:message key="divNum"/>:<span class="mandatory">*</span></td>
    <td class="greybox"><label>
      <select class="selectnew" name="divNumber" id="divNumber">
		<option  value = "0"><bean:message key="choose" /></option>  	          
      </select>
      </label></td>
  </tr>

</table>
</div>

<font size="1"><div align="left" class="mandatory"><bean:message key="mandtryFlds"/></div></font>
 
<div class="buttonbottom" align="center">
<input type="button" name="button2" id="button2" value="Close" class="button" onclick="window.close();"/>
 <input type="submit" name="button3" id="button3" value="List" class="buttonsubmit" onclick = "return checkBeforeSubmit();"/>
 </div>
 
 
</body>
</html:form>
</html>
