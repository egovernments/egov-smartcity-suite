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

<%@ include file="/includes/taglibs.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><s:text name="bakayaFeristReport.title"/></title>
	<script type="text/javascript">
	
		function populateWard() {
			populatereportWards({
				zoneId : document.getElementById("reportZones").value
			});			
		}
		
	</script>
</head>
<body>
	<div align="left">
  		<s:actionerror/>
  	</div>

  	<s:form name="BakayaFeristReportForm" action="bakayaFeristReport" theme="simple">
		<div class="formmainbox">
  			<div class="formheading"></div>
			<div class="headingbg"><s:text name="bakayaFeristReport.title"/></div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td style="text-align: center">
						<s:text name="Zone" /><span class="mandatory"> * </span> : &nbsp; &nbsp; &nbsp; &nbsp;
					</td>
					<td style="text-align: center"> 
						<s:select id="reportZones" headerKey="-1"
							headerValue="%{getText('default.select')}" name="zoneId"
							value="%{zoneId}" list="ZoneBndryMap" cssClass="selectnew"
							onchange="return populateWard();" style="width: 100px"/>
					</td>					
				</tr>
				<tr>
					<td style="text-align: center">
						<s:text name="Ward" /><span class="mandatory"> * </span> : &nbsp; &nbsp; &nbsp; &nbsp;
					</td>
					<td style="text-align: center">
						<egov:ajaxdropdown id="wardAjaxDropdown" fields="['Text','Value']"
							dropdownId="reportWards" url="common/ajaxCommon!wardByZone.action" />					
						<s:select id="reportWards" headerKey="-1"
							headerValue="%{getText('default.select')}"
							list="dropdownData.Wards" cssClass="selectnew" name="wardId"
							value="%{wardId}" onchange="return populatePartNumbers(this.id);" style="width: 100px"/> 
					</td>
				</tr>		
				<tr>
					<%@ include file="../common/partnumber.jsp" %>	
				</tr> 
				<tr>
					<td>
					</td>					
				</tr>
			</table>
			<div class="buttonbottom" align="center">
					<s:submit name="btnSubmitReport" id="btnSubmitReport" method="report" cssClass="buttonsubmit" />
					<input type="button" name="ptReportClose" id="ptReportClose" value="Close" class="button" onclick="return confirmClose();">
			</div>			
		</div>
	</s:form>
	<script type="text/javascript">
  		paintAlternateColorForRows();
  	</script>
</body>
</html>
