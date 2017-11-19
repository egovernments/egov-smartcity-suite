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


<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ include file="/includes/taglibs.jsp"%>

<link href="<egov:url path='/resources/css/displaytagFormatted.css'/>"
	rel="stylesheet" type="text/css" />
<html>
<head>
<title><s:text name="report.autoremittancescheduler.title" /></title>

</head>
<body>

	<div class="formmainbox">
		<div class="formheading"></div>
		<div class="subheadnew">
			<s:text name='report.autoremittancescheduler.title' />
		</div>

		<s:form action="autoRemittanceSchedulerReport" theme="simple"
			name="autoRemittanceSchedulerReport">
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="bluebox"><s:text name="report.remittancecoa" /></td>
					<td class="bluebox"><s:select name="recoveryId"
							id="recoveryId" list="recoveryMap" listKey="key"
							value='%{recoveryId}' listValue="value" headerKey=""
							headerValue="----Choose----" /></td>

					<td class="bluebox"><s:text name="report.schedulertype" /></td>
					<td class="bluebox"><s:select name="schedulerType"
							value='%{schedulerType}' id="schedulerType"
							list="dropdownData.schedulerTypeList" headerKey="" /></td>

				</tr>
				<tr>
					<td class="greybox"><s:text name="report.rundatefrom" />:</td>
					<s:date name="runDateFrom" var="fromDateFormat" format="dd/MM/yyyy" />
					<td class="greybox"><s:textfield name="runDateFrom"
							id="runDateFrom" cssStyle="width:100px" value='%{fromDateFormat}'
							onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
						href="javascript:show_calendar('autoRemittanceSchedulerReport.runDateFrom');"
						style="text-decoration: none">&nbsp;<img
							src="/egi/resources/erp2/images/calendaricon.gif" border="0"
							alt="" /></a>(dd/mm/yyyy)<br /></td>
					<td class="greybox"><s:text name="report.rundateto" />:</td>
					<s:date name="runDateTo" var="toDateFormat" format="dd/MM/yyyy" />
					<td class="greybox"><s:textfield name="runDateTo"
							id="runDateTo" cssStyle="width:100px" value='%{toDateFormat}'
							onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
						href="javascript:show_calendar('autoRemittanceSchedulerReport.runDateTo');"
						style="text-decoration: none">&nbsp;<img
							src="/egi/resources/erp2/images/calendaricon.gif" border="0"
							alt="" /></a>(dd/mm/yyyy)<br /></td>
				</tr>
				<!-- <tr>
					<td class="bluebox" width="10%"><s:text
							name="report.nextrundate" /></td>
					<td class="bluebox"><s:checkbox name="nextRunDate"
							id="nextRunDate" /></td>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox">&nbsp;</td>
				</tr> -->
				<tr>
					<td colspan="4">
						<div class="buttonbottom">
							<s:submit cssClass="buttonsubmit" value="SEARCH"
								id="searchButton" name="button" method="searchList" />
							<input type="button" class="button" value="CLOSE"
								id="closeButton" name="button" onclick="window.close();" />
						</div>
					</td>
				</tr>
			</table>
			<div>
				<s:if test="%{searchResult.fullListSize != 0}">
					<display:table name="searchResult" uid="currentRowObject"
						cellpadding="0" cellspacing="0" requestURI="" class="its"
						style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
						<display:column title="Sl. No." titleKey='label.slno'
							headerClass="pagetableth" class="pagetabletd"
							style="width:3%;text-align:right">
							<s:property
								value="%{#attr.currentRowObject_rowNum+ (page-1)*pageSize}" />
						</display:column>
						<display:column headerClass="pagetableth" class="pagetabletd"
							title="Recovery CoA" titleKey="report.schedulertype"
							style="width:20%;text-align:center">
							<s:property value="#attr.currentRowObject.recoveryCoa" />
						</display:column>

						<display:column headerClass="pagetableth" class="pagetabletd"
							title="Scheduler Type" titleKey="report.schedulertype"
							style="width:4%;text-align:center">
							<s:property value="#attr.currentRowObject.scheduleType" />
						</display:column>

						<display:column headerClass="pagetableth" class="pagetabletd"
							title="Run Date" titleKey="report.run.date"
							style="width:5%;text-align:left">
							<s:date name="#attr.currentRowObject.runDate" format="dd/MM/yyyy" />
						</display:column>

						<display:column headerClass="pagetableth" class="pagetabletd"
							title="Status" titleKey="report.schedulertype"
							style="width:4%;text-align:center">
							<s:property value="#attr.currentRowObject.status" />
						</display:column>

						<display:column headerClass="pagetableth" class="pagetabletd"
							title="Remarks" titleKey="report.schedulertype"
							style="width:60%;text-align:center">
							<s:property value="#attr.currentRowObject.remarks" />
						</display:column>

						<display:column headerClass="pagetableth" class="pagetabletd"
							title="Number of Payments" titleKey="report.schedulertype"
							style="width:3%;text-align:center">
							<s:property value="#attr.currentRowObject.numberOfPayments" />
						</display:column>
					</display:table>
				</s:if>
				<s:elseif test="%{searchResult.fullListSize == 0}">
					<div>
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td align="center"><font color="red"><s:text
											name="label.no.records.found" /></font></td>
							</tr>
						</table>
					</div>
				</s:elseif>
			</div>
		</s:form>
	</div>
</body>

</html>
