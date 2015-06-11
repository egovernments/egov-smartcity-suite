<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ include file="/includes/taglibs.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><s:text name="defaultersListReport.title"/></title>
	<script type="text/javascript">
	
		function populateWard() {
			populatereportWards({
				zoneId : document.getElementById("reportZones").value
			});			
		}
		
		function populatePartNumbers() {
			populatepartNumbers({
				wardId : document.getElementById("reportWards").value
			});			
		}
	
	</script>
</head>
<body>
	<div align="left">
  		<s:actionerror/>
  	</div>

  	<s:form name="DefaultersListForm" action="defaultersReport" theme="simple">
		<div class="formmainbox">
			<div class="headingbg"><s:text name="defaultersListReport.title"/></div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="greybox" width="30%">
							&nbsp; &nbsp;
					</td>
					<td class="greybox" width="20%" style="text-align: left">
						<s:text name="Zone" /><span class="mandatory1"> * </span> :
					</td>  
					<td class="greybox" width="25%" style="text-align: left">
						
						<s:select id="reportZones" headerKey="-1"
							headerValue="%{getText('default.select')}" name="zoneId"
							value="%{zoneId}" list="ZoneBndryMap" cssClass="selectnew"
							onchange="return populateWard();" style="width: 100px"/>
					</td>
					<td class="greybox" width="25%">
							&nbsp; &nbsp;
					</td>					
				</tr>
				<tr>
					<td class="bluebox" width="20%">
							&nbsp; &nbsp;
					</td>					
					<td class="bluebox" width="25%" style="text-align: left">
						<s:text name="Ward" /><span class="mandatory1"> * </span> : 
					</td>
					<td class="bluebox" width="25%" style="text-align: left">
						<egov:ajaxdropdown id="wardAjaxDropdown" fields="['Text','Value']"
							dropdownId="reportWards" url="common/ajaxCommon!wardByZone.action" />
						<s:select id="reportWards" headerKey="-1"
							headerValue="%{getText('default.select')}"
							list="dropdownData.Wards" cssClass="selectnew" name="wardId" listKey="id" listValue="name"
							value="%{wardId}" onchange="return populatePartNumbers();" style="width: 100px"/> 
					</td>
					<td class="bluebox" width="25%">
							&nbsp; &nbsp;
					</td>
				</tr>		
				<tr>
					<td class="greybox" width="20%">
							&nbsp; &nbsp;
					</td>					
					<td class="greybox" width="25%" style="text-align: left">
						<s:text name="block" /> : 
					</td>
					<td class="greybox" width="25%" style="text-align: left">
						<egov:ajaxdropdown id="partNoAjaxDropdown" fields="['Text','Value']"
							dropdownId="partNumbers" url="common/ajaxCommon!partNumbersByWard.action" />
						<s:select id="partNumbers" headerKey="-1"
							headerValue="%{getText('default.select')}"
							list="dropdownData.partNumbers" cssClass="selectnew" name="partNo"
							value="%{partNo}" style="width: 100px"/> 
					</td>
					<td class="greybox" width="25%">
							&nbsp; &nbsp;
					</td>
				</tr>			
				<tr>
					<td class="bluebox" width="20%">
							&nbsp; &nbsp;
					</td>					
					<td class="bluebox" width="25%" style="text-align: left">
						<s:text name="outstandingAmount" /><span class="mandatory1"> * </span> : 
					</td>
					<td class="bluebox" width="25%" style="text-align: left">
						<s:select id="outstandingAmount" headerKey="-1"
							headerValue="%{getText('default.select')}" name="amountRange"
							value="%{amountRange}"
							list="@org.egov.ptis.actions.common.CommonServices@outstandingAmountRanges"
							cssClass="selectnew" style="width: 100px" />
					</td>					
					<td class="bluebox" width="25%">
							&nbsp; &nbsp;
					</td>
				</tr>
				<tr>
					<td class="greybox" colspan="4" width="100%">
					<span class="mandatory1"><s:text name="defaulter.report.infomsg"/></span>
					</td>					
				</tr>
			</table>
			<div class="buttonbottom" align="center">
					<s:submit name="btnSubmitReport" id="btnSubmitReport" method="generateReport" cssClass="buttonsubmit" />
					&nbsp; &nbsp;
					<input type="button" name="ptReportClose" id="ptReportClose" value="Close" class="button" onclick="return confirmClose();">
			</div>			
		</div>
	</s:form>
	<s:if test="%{resultPage}">
		<logic:empty name="defaulters">
			<div class="headingsmallbgnew" style="text-align:center;width:100%;"><s:text name="searchresult.norecord"/></div>
		</logic:empty>
	</s:if>
</body>
</html>
