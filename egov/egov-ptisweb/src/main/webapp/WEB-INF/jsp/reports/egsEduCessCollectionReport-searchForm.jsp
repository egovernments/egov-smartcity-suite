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

<%@ include file="/includes/taglibs.jsp"%>
<html>
	<title><s:text name="egsEduCessCollReport"/></title>
	<body>
	<div align="left">
  		<s:actionerror/>
  	</div>
	<div class="errorstyle" id="property_error_area" style="display:none;"></div>
	<div class="formmainbox">
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<s:form action="/reports/egsEduCessCollectionReport!generateReport.action" name="egsEduCessCollectionReportForm" theme="simple">
				<div class="formheading"></div>
				<tr>
					<td width="100%" colspan="3" class="headingbg">
						<div class="headingbg">
							<s:text name="egsEduCessCollReport" />
						</div>
					</td>
				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox">
						<table align="center">
							<td class="bluebox"><s:text name="day" /></td>
							<td class="bluebox">
									<s:date name="day" format="dd/MM/yyyy" var="d"/>
									<s:textfield name="day" id="day" maxlength="10" size="10" value="%{d}"/>
									<a href="javascript:show_calendar('egsEduCessCollectionReportForm.day',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;
									<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>
							</td>
						</table>
					</td>
					<td class="bluebox">&nbsp;</td>
				</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox">
						<table align="center">
							<td class="greybox"><s:text name="month" /></td>
							<td class="greybox"><s:select list="monthsMap" listKey="key" listValue="value"
								headerKey="-1" headerValue="--Choose--" id="month" name="month"
								value="%{month}" />
							</td>
						</table>
					</td>
					<td class="greybox">&nbsp;</td>
				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox">
						<table align="center">
							<td  class="bluebox"><s:text name="year" /></td>
							<td  class="bluebox"><s:select list="dropdownData.yearsList" listKey="description"
								listValue="description" headerKey="-1" headerValue="--Choose--"
								id="year" name="year" value="%{year}" />
							</td>
						</table>
					</td>
					<td class="bluebox">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="3">
						<div class="buttonbottom" align="center">
							<s:submit name="search" value="Get Report" cssClass="buttonSubmit"/>
							<input type="button" name="close" value="Close" class="button" onclick="return confirmClose();"/>
						</div>
					</td>
				</tr>
			</s:form>
		</table>
	</div>
	<s:if test="%{!recordsExist}">
		<div>
			<s:text name="noRecsFound"/>
		</div>
	</s:if>
	</body>
</html>
