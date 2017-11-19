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

<html>
<head>
	<title><s:text name="activeDemandReport.title"/></title>
	<script type="text/javascript">
		jQuery.noConflict();
		jQuery(function() {
			jQuery("#asOnDate").datepicker(
				{
					maxDate: new Date(),
					dateFormat: 'dd/mm/yy'
				}
			);
		});

		function generateWardReport() {
		}
	</script>
</head>
<body>
	<div align="left">
  		<s:actionerror/>
  	</div>
  	<s:form name="ActiveDemandReportForm" action="activeDemandReport" theme="simple">
		<div class="formmainbox">
  			<div class="formheading"></div>
			<div class="headingbg">
				<s:text name="title.nmc" /><br>
	  			<s:text name="title.pt" /><br><br>
				<s:date name="asOnDate" format="dd MMMMM yyyy" var="asOnDateString" />
				<s:if test="#asOnDateString == null">
					<s:text name="activeDemandReport.title"/>
				</s:if> 				
				<s:else>
					<s:text name="activeDemandReport.title"/> as on <s:property value="%{asOnDateString}" />
				</s:else>
			</div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="bluebox" width="5%">
						&nbsp;
					</td>
					<td class="bluebox">
						<s:text name="PropertyType" /><span class="mandatory"> * </span> : 
					</td>
					<td class="bluebox">
						<s:select list="dropdownData.propTypes" name="propertyTypes" value="%{propertyTypes}"
							headerKey="-1" listKey="id" listValue="type"
						    id="propType" multiple="true"/>
					</td>
					<td class="bluebox"> 
						<s:text name="asOnDate" /> : 
					</td>
					<td class="bluebox">
						<s:textfield id="asOnDate" name="asOnDate" value="%{asOnDate}" readonly="true"/>
					</td>					
				</tr>
				<tr>
					<td class="greybox" width="5%">
						&nbsp;
					</td>
					<td class="greybox" colspan="2">
						<s:text name="include.obj.properties" /> 
						<s:checkbox name="objPropsIncluded" value="%{objPropsIncluded}" id="objPropsIncluded"/>
					</td>
					<td class="greybox" colspan="3">
						&nbsp;
					</td>
				</tr>		
			</table>
			<div class="buttonbottom" align="center">
					<s:submit name="btnSubmitReport" id="btnSubmitReport" method="search" cssClass="buttonsubmit" />
					<input type="button" name="ptReportClose" id="ptReportClose" value="Close" class="button" onclick="return confirmClose();">
			</div>			
		</div>
		<s:if test="%{resultPage}">
			<%@ include file="activeDemandReportResults.jsp"%> 
		</s:if>
	</s:form>
</body>
</html>
