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


<html>
<head>
<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/contraBTBHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>
</head>
<body onload="onLoadTask_edit()">
	<s:form action="contraBTB" theme="simple" name="cbtbform">
		<s:push value="model">
			<jsp:include page="../budget/budgetHeader.jsp">
				<jsp:param value="Bank to Bank Transfer" name="heading" />
			</jsp:include>
			<div class="formmainbox">
				<div class="formheading" />
				<div class="subheadnew">Modify Bank to Bank Transfer</div>
				<div id="listid" style="display: block">
					<br />
				</div>
			</div>
			</div>
			<div align="center">
				<font style='color: red;'>
					<p class="error-block" id="lblError"></p>
				</font> <span class="mandatory">
					<div id="Errors">
						<s:actionerror />
						<s:fielderror />
					</div> <s:actionmessage />
				</span>
				<table border="0" width="100%" cellspacing="0" cellpadding="0">
					<tr>
						<td class="bluebox" width="10%"></td>
						<s:if test="%{shouldShowHeaderField('vouchernumber')}">
							<td class="bluebox" width="22%"><s:text
									name="voucher.number" /><span class="mandatory">*</span></td>
							<td class="bluebox" width="22%">
								<table width="100%">
									<tr>
										<td style="width: 25%"><input type="text"
											name="voucherNumberPrefix" id="voucherNumberPrefix"
											readonly="true" style="width: 100%" /></td>
										<td width="75%"><s:textfield name="voucherNumber"
												id="voucherNumber" /></td>
									</tr>
								</table>
							</td>

						</s:if>
						<s:else>
							<td class="bluebox" width="22%"><s:text
									name="voucher.number" /><span class="mandatory">*</span></td>
							<td class="bluebox" width="22%">
								<table width="100%">
									<tr>
										<td style="width: 25%"><input type="text"
											name="voucherNumberPrefix" id="voucherNumberPrefix"
											readonly="true" style="width: 100%" /></td>
										<td width="75%"><s:textfield name="voucherNumber"
												id="voucherNumber" readonly="true" /></td>
									</tr>
								</table>
							</td>
							<s:hidden name="voucherNumber" id="voucherNumber" />
						</s:else>
						<s:hidden name="id" />
						<td class="bluebox" width="18%%"><s:text name="voucher.date" /><span
							class="mandatory">*</span></td>
						<td class="bluebox" width="34%"><input type="text"
							name="voucherDate" id="voucherDate"
							value='<s:date name="voucherDate" format="dd/MM/yyyy"/>'
							onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
							href="javascript:show_calendar('cbtbform.voucherDate');"
							style="text-decoration: none">&nbsp;<img tabIndex="-1"
								src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></A>(dd/mm/yyyy)</td>
					</tr>
					<%@include file="contraBTB-form.jsp"%>
				</table>
				<div class="subheadsmallnew" /></div>
				<div class="mandatory" align="left">* Mandatory Fields</div>
			</div>
			</div>
			<%@include file="../voucher/modifyButtons.jsp"%>
			<s:hidden id="bankBalanceMandatory" name="bankBalanceMandatory"
				value="%{isBankBalanceMandatory()}" />
			<s:hidden id="startDateForBalanceCheckStr"
				name="startDateForBalanceCheckStr"
				value="%{startDateForBalanceCheckStr}" />
		</s:push>
	</s:form>
	<SCRIPT type="text/javascript">
	
			function onLoadTask_edit() {
			   var tempVoucherNumber='<s:property value="voucherHeader.voucherNumber"/>';
			   var prefixLength='<s:property value="voucherNumberPrefixLength"/>';
			   var srcFund='<s:property value="contraBean.fromFundId"/>'
			   var desFund='<s:property value="contraBean.toFundId"/>'
				if(srcFund==desFund){
					document.getElementById("interFundRow").style.visibility="hidden";
				}
				else
					document.getElementById("interFundRow").style.visibility="visible";
			   document.getElementById('voucherNumberPrefix').value=tempVoucherNumber.substring(0,prefixLength);
			   document.getElementById('voucherNumber').value=tempVoucherNumber.substring(prefixLength,tempVoucherNumber.length);
				if('other'=='<s:property value="contraBean.modeOfCollection"/>')
				{
				document.getElementById("mdcNumber").innerHTML = '<s:text name="contra.refNumber" />';
				document.getElementById("mdcDate").innerHTML = '<s:text name="contra.refDate" />';
				}else
				{
				 <s:if test="egovCommon.isShowChequeNumber()">
				 document.getElementById("chequeGrid").style.visibility="visible";
				 </s:if>
				 <s:else>
				 document.getElementById("chequeGrid").style.visibility="hidden";
				 </s:else>
				 
				}
				
				var button = '<s:property value="button"/>';
				 if (button != null && button != "") {

					if (document.getElementById("Errors").innerHTML == '') {
					bootbox.alert('<s:text name="contra.transaction.succcess"/>');
					   if (button == "Save_Close") {
							window.close();
						} else if (button == "Save_View") {
							var vhId = '<s:property value="vhId"/>';
							document.forms[0].action = "${pageContext.request.contextPath}/voucher/preApprovedVoucher!loadvoucherview.action?vhid="
									+ vhId;
							document.forms[0].submit();
						} else if (button == "Save_New") {
							document.forms[0].button.value = '';
							document.forms[0].action = "contraBTB!newform.action";
							document.forms[0].submit();
						}
					}
					 
				}

			}
			
function toggleChequeAndRefNumber(obj) {

		if (obj.value == "other") {
			document.getElementById("chequeGrid").style.visibility="visible";
			document.getElementById("mdcNumber").innerHTML = '<s:text name="contra.refNumber" />';
			document.getElementById("mdcDate").innerHTML = '<s:text name="contra.refDate" />';
					
		} else {
		var x='<s:property value="egovCommon.isShowChequeNumber()"/>';
		<s:if test="egovCommon.isShowChequeNumber()">
		 document.getElementById("chequeGrid").style.visibility="visible";
		 </s:if>
		 <s:else>
		 document.getElementById("chequeGrid").style.visibility="hidden";
		 </s:else>
			document.getElementById("mdcNumber").innerHTML = '<s:text name="contra.chequeNumber" />';
			document.getElementById("mdcDate").innerHTML = '<s:text name="contra.chequeDate" />';
			
		}
	}
	
	if('<s:text name="%{isBankBalanceMandatory()}"/>'=='')
		document.getElementById('lblError').innerHTML = "bank_balance_mandatory parameter is not defined";
	
					
</SCRIPT>
</body>
</html>
