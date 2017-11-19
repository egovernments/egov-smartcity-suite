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

<span class="mandatory"> <font
	style='color: red; font-weight: bold'> <s:actionerror /> <s:fielderror />
		<s:actionmessage /></font>
</span>
<s:if test="%{dishonoredChequeDisplayList.size!=0}">
	<display:table name="dishonoredChequeDisplayList" id="currentRowObject"
		uid="currentRowObject" class="tablebottom" style="width:100%;"
		cellpadding="0" cellspacing="0" export="true"
		requestURI="dishonoredChequeReport!ajaxSearch.action">
		<display:caption>
			<div class="headingsmallbgnew" align="center"
				style="text-align: center; width: 98%;">
				<b><s:property value="%{heading}" /></b>
			</div>
		</display:caption>
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Sl.No" style="width:4%;text-align:center">
			<s:property value="%{#attr.currentRowObject_rowNum}" />
		</display:column>
		<display:column media="pdf" headerClass="bluebgheadtd"
			class="blueborderfortd" title="Voucher Number"
			style="width:8%;text-align:center" property="voucherNumber" />
		<display:column media="excel" headerClass="bluebgheadtd"
			class="blueborderfortd" title="Voucher Number"
			style="width:8%;text-align:center" property="voucherNumber" />
		<display:column media="html" headerClass="bluebgheadtd"
			class="blueborderfortd" title="Voucher Number"
			style="width:8%;text-align:center">
			<a href="#"
				onclick="return viewVoucher('<s:property value="#attr.currentRowObject.voucherHeaderId"/>')">
				<s:property value="#attr.currentRowObject.voucherNumber" />
			</a>
		</display:column>
		<s:if test="%{dishonoredChequeReport.mode == 2}">
			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="Cheque Number" style="width:6%;text-align:center"
				property="chequeNumber" />
			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="Cheque Date" style="width:6%;text-align:center"
				property="chequeDate" />
		</s:if>
		<s:else>
			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="DD Number" style="width:8%;text-align:center"
				property="chequeNumber" />
			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="DD Date" style="width:6%;text-align:center"
				property="chequeDate" />
		</s:else>
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Payee Name" style="width:6%;text-align:center"
			property="payeeName" />
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Deposited in" style="width:8%;text-align:center"
			property="bankName" />
		<s:if test="%{dishonoredChequeReport.mode == 2}">
			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="Cheque Dishonored on" style="width:6%;text-align:center"
				property="recChequeDate" />
		</s:if>
		<s:else>
			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="DD Dishonored on" style="width:6%;text-align:center"
				property="recChequeDate" />
		</s:else>
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Bank Ref Number" style="width:6%;text-align:center"
			property="bankRefNumber" />
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Bank Charge Amount(Rs.)" style="width:8%;text-align:right"
			property="bankChargeAmt" />
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Cheque Amount (Rs.)" style="width:8%;text-align:right"
			property="amount" />
		<s:if test="%{dishonoredChequeReport.mode == 2}">
			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="Cheque Status" style="width:8%;text-align:center"
				property="status" />
		</s:if>
		<s:else>
			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="DD Status" style="width:8%;text-align:center"
				property="status" />
		</s:else>
		<display:caption media="pdf">
			<div align="left" style="text-align: left;">
				<b> <s:property value="%{heading}" /></b>
			</div>
		</display:caption>
		<display:caption media="excel">
				   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						  Dishonored Cheque Report   
				</display:caption>
		<display:setProperty name="export.pdf" value="true" />
		<display:setProperty name="export.pdf.filename"
			value="DishonoredReport.pdf" />
		<display:setProperty name="export.excel" value="true" />
		<display:setProperty name="export.excel.filename"
			value="DishonoredReport.xls" />
		<display:setProperty name="export.csv" value="false" />
		<display:setProperty name="export.xml" value="false" />
	</display:table>

</s:if>
<s:else>
	<table align="center" class="tablebottom" width="80%">
		<tr>
			<th class="bluebgheadtd" colspan="7">No Records Found
			</td>
		</tr>
	</table>
</s:else>
