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

<span class="mandatory1"> <font
	style='color: red; font-weight: bold'> <s:actionerror /> <s:fielderror />
		<s:actionmessage /></font>
</span>
<s:if test="%{journalBookDisplayList.size!=0}">
	<display:table name="journalBookDisplayList" id="currentRowObject"
		uid="currentRowObject" class="tablebottom" style="width:100%;"
		cellpadding="0" cellspacing="0" export="true"
		requestURI="journalBookReport-ajaxSearch.action">
		<display:caption>
			<table width="100%">
				<tr>
					<th class="bluebgheadtd" width="100%" colspan="8"><strong
						style="font-size: 15px;"><s:property value="%{heading}" />
					</strong></th>
				</tr>
			</table>
		</display:caption>
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Voucher Date" style="width:4%;text-align:center"
			property="voucherdate" />
		<display:column media="pdf" headerClass="bluebgheadtd"
			class="blueborderfortd" title="Voucher Number"
			style="width:5%;text-align:center" property="vouchernumber" />
		<display:column media="excel" headerClass="bluebgheadtd"
			class="blueborderfortd" title="Voucher Number"
			style="width:4%;text-align:center" property="vouchernumber" />
		<display:column media="html" headerClass="bluebgheadtd"
			class="blueborderfortd" title="Voucher Number"
			style="width:4%;text-align:center">
			<a href="#"
				onclick="return viewVoucher('<s:property value="#attr.currentRowObject.vhId"/>')">
				<s:property value="#attr.currentRowObject.vouchernumber" />
			</a>
		</display:column>
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Voucher Name" style="width:6%;text-align:center"
			property="voucherName" />
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Account Code" style="width:4%;text-align:center"
			property="code" />
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Account Name" style="width:9%;text-align:center"
			property="accName" />
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Narration" style="width:7%;text-align:center"
			property="narration" />
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Debit Amount(Rs.)" style="width:3%;text-align:right"
			property="debitamount" />
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Credit Amount(Rs.)" style="width:3%;text-align:right"
			property="creditamount" />
		<display:caption media="pdf">
			<div align="left" style="text-align: left;">
				<b> <s:property value="%{heading}" /> (<s:property
						value="%{journalBookReport.isConfirmedCount}" /> in <s:property
						value="%{journalBookReport.totalCount}" /> are unconfirmed)
				</b>
			</div>
		</display:caption>
		<display:caption media="excel">
				   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						  Journal Book Report  
				</display:caption>
		<display:setProperty name="export.pdf" value="true" />
		<display:setProperty name="export.pdf.filename"
			value="Journal Book Report.pdf" />
		<display:setProperty name="export.excel" value="true" />
		<display:setProperty name="export.excel.filename"
			value="Journal Book Report.xls" />
		<display:setProperty name="export.csv" value="false" />
		<display:setProperty name="export.xml" value="false" />
	</display:table>

</s:if>
<s:else>No records found</s:else>
