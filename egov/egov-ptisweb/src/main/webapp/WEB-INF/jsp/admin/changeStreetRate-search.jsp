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

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title><s:text name="ChBaseRateArea.title"></s:text> </title>
	<script type="text/javascript">

		function populateWard() {
			populatewardId( {
				zoneId : document.getElementById("zoneId").value
			});
		}
		function populateArea() {
			populateareaId( {
				wardId : document.getElementById("wardId").value
			});
		}			

	</script>
</head>  
<body>
  <div align="left">
  	<s:actionerror/>
  </div>
  
  <s:form name="ChBaseRateAreaForm" action="changeStreetRate-search" theme="simple" method="post">
  <div class="formmainbox">
  
	<div class="headingbg"><s:text name="ChBaseRateAreaHeader"/></div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td class="greybox" width="100%" colspan="5">
				<center>
					<table border="0" cellspacing="0" cellpadding="0" width="300px">
						<tr>
							<td class="greybox" width="10px">
								<s:text name="Zone" />
								<span class="mandatory1">*</span> :
							</td>
							<td class="greybox" width="20%">
								<s:select headerKey="-1" headerValue="%{getText('default.select')}"
									name="zoneId" id="zoneId" listKey="id" listValue="name"
									list="dropdownData.Zone" cssClass="selectnew"
									 value="%{zoneId}" cssStyle="width:150px" />
							</td>
						</tr>
					</table>
				</center>
			</td>
		</tr>
		
		<tr>
			<td class="bluebox" width="100%" colspan="5">
				<center>
					<table border="0" cellspacing="0" cellpadding="0" width="300px">
						<tr>
							<td class="bluebox" width="10px">
								<s:text name="Ward" />
								<span class="mandatory1">*</span> :
							</td>
							<td class="bluebox" width="20%">
								<s:select name="wardId" id="wardId" list="dropdownData.wardList"
									listKey="id" listValue="name" headerKey="-1"
									headerValue="%{getText('default.select')}"
									onchange="return populateArea();" value="%{wardId}" cssStyle="width:150px" />
							</td>
						</tr>
					</table>
				</center>
			</td>
		</tr>
		<tr>
			<td class="greybox" width="100%" colspan="5">
				<center>
					<table border="0" cellspacing="0" cellpadding="0" width="300px">
						<tr>
							<egov:ajaxdropdown id="areaId" fields="['Text','Value']"  dropdownId="areaId" url="common/ajaxCommon-streetByWard.action" />
							<td class="greybox" width="10px"><s:text name="Street" /><span class="mandatory1">*</span> : </td>
							<td class="greybox" width="20%">
								<s:select name="areaId" id="areaId" list="dropdownData.streetList"
							    listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('default.select')}" value="%{areaId}" cssStyle="width:150px" />
							</td>
						</tr>
					</table>
				</center>
			</td>
		</tr>		
	  	<tr>
	  	<td class="bluebox" colspan="5">
	  	<div class="mandatory1" align="left">
	  		<font size="2"><s:text name="mandtryFlds"/></font>
	  	</div>
	  	</td>
	  </tr>
	  </table>
	            	
	</div>
	 <div class="buttonsearch" align="center">
		   	<s:submit cssClass="buttonsubmit" value="Search" method="search"/>
		   	<input type="button" name="button2" id="button2" value="Close" class="button" onclick="return confirmClose();"/>
	  </div>
	</s:form>
</body>
</html>


