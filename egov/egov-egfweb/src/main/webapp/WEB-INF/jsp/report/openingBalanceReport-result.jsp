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
<%@ page language="java"%>

<%-- <span class="mandatory"> <font
	style='color: red; font-weight: bold'> <s:actionerror /> <s:fielderror />
		<s:actionmessage /></font>
</span> --%>
<s:if test="%{openingBalanceDisplayList.size!=0}">
	<display:table name="openingBalanceDisplayList" id="currentRowObject"
		uid="currentRowObject" class="tablebottom" style="width:100%;"
		cellpadding="0" cellspacing="0" export="true"
		requestURI="openingBalanceReport-ajaxSearch.action">
		<display:caption>
			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<th class="bluebgheadtd" width="100%" colspan="5"><strong
						style="font-size: 15px;"> <s:property value="%{heading}" /></strong></th>
				</tr>
			</table>
		</display:caption>
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Fund" style="width:6%;text-align:center" property="fund" />
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="GL Code" style="width:6%;text-align:center" property="accCode" />
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Department" style="width:3%;text-align:center"
			property="deptcode" />
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Function" style="width:3%;text-align:center"
			property="functioncode" />
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Account Head" style="width:8%;text-align:left"
			property="accName" />
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Description" style="width:5%;text-align:left"
			property="description" />
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Debit (Rs.)" style="width:6%;text-align:right"
			property="debit" />
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Credit (Rs.)" style="width:6%;text-align:right"
			property="credit" />
		<display:caption media="pdf">
			<div align="left" style="text-align: left;">
				<b><s:property value="%{heading}" /></b>
			</div>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  Opening Balance Report  
		</display:caption>
		<display:caption media="excel">
				   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						  Opening Balance Report  
				</display:caption>
		<display:setProperty name="export.pdf" value="true" />
		<display:setProperty name="export.pdf.filename"
			value="Opening Balance Report.pdf" />
		<display:setProperty name="export.excel" value="true" />
		<display:setProperty name="export.excel.filename"
			value="Opening Balance Report.xls" />
		<display:setProperty name="export.csv" value="false" />
		<display:setProperty name="export.xml" value="false" />
	</display:table>

</s:if>
