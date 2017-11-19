<%@ page import="org.egov.utils.Constants" %>
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
	src="${pageContext.request.contextPath}/resources/javascript/contraBTBHelper.js"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>
</head>
<body onload="onLoadTask_reverse">
	<s:form action="contraBTB" theme="simple" name="cbtbform">
		<s:push value="model">
			<jsp:include page="../budget/budgetHeader.jsp">
				<jsp:param value="Bank to Bank Transfer" name="heading" />
			</jsp:include>
			<div class="formmainbox">
				<div class="formheading" />
				<div class="subheadnew">Create Bank to Bank Transfer</div>
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
						<td width="10%" class="bluebox"><s:if
								test="%{shouldShowHeaderField('vouchernumber')}">
								<td class="bluebox" width="22%"><s:text
										name="voucher.number" /><span class="mandatory">*</span></td>
								<td class="bluebox" width="22%"><s:textfield
										name="voucherNumber" id="voucherNumber" /></td>
							</s:if> <s:hidden name="id" />
						<td class="bluebox" width="18%"><s:text name="voucher.date" /><span
							class="mandatory">*</span></td>
						<td class="bluebox" width="34%"><input type:text
							name="voucherDate"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							value='<s:date name="voucherDate" format="dd/MM/yyyy"/>' /> <a
							href="javascript:show_calendar('cbtbform.voucherDate');"
							style="text-decoration: none">&nbsp;<img tabIndex="-1"
								src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></A>(dd/mm/yyyy)</td>
					</tr>
					<%@include file="contraBTB-form.jsp"%>
					<tr>
						<td class="bluebox"></td>
						<s:if test="%{shouldShowHeaderField('vouchernumber')}">

							<td class="bluebox"><s:text name="reversalVoucherNumber" /><span
								class="mandatory">*</span></td>
							<td class="bluebox"><s:textfield
									name="reversalVoucherNumber" id="reversalVoucherNumber" /></td>
						</s:if>
						<td class="bluebox"><s:text name="reversalVoucherDate" /><span
							class="mandatory">*</span></td>
						<td class="bluebox"><s:textfield name="reversalVoucherDate"
								id="reversalVoucherDate"
								onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
							href="javascript:show_calendar('cbtbform.reversalVoucherDate');"
							style="text-decoration: none">&nbsp;<img tabIndex="-1"
								src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></A>(dd/mm/yyyy)</td>
					</tr>
				</table>
			</div>
			<%@include file="../voucher/ReverseButtons.jsp"%>
			<input type="hidden" id="voucherTypeBean.voucherName"
				name="voucherTypeBean.voucherName" value="BankToBank" />
			<input type="hidden" id="voucherTypeBean.voucherType"
				name="voucherTypeBean.voucherType" value="Contra" />
			<input type="hidden" id="voucherTypeBean.voucherNumType"
				name="voucherTypeBean.voucherNumType" value="Contra" />
			<input type="hidden" id="voucherTypeBean.cgnType"
				name="voucherTypeBean.cgnType" value="BTB" />
		</s:push>
	</s:form>
	<SCRIPT type="text/javascript">
 function onLoadTask_reverse() {
		       
				var button = '<s:property value="button"/>';
				bootbox.alert(button);
				if (button != null && button != "") {
				if (document.getElementById("Errors").innerHTML == '') {
				bootbox.alert('<s:text name="contra.reverse.transaction.success"/>');
						if (button == "Reverse_Close") {
							window.close();
						} else if (button == "Reverse_View") {
							var vhId = '<s:property value="vhId"/>';
							document.forms[0].action = "${pageContext.request.contextPath}/voucher/preApprovedVoucher!loadvoucherview.action?vhid="
									+ vhId;
							document.forms[0].submit();
						} else if (button == "Reverse_New") {
							document.forms[0].button.value = '';
							document.forms[0].action = "contraBTB!newform.action";
							document.forms[0].submit();
						}
					}
				}
				else
				{
				bootbox.alert('disabling');
				disableControls(0,true);
				}
				document.getElementById('button').disabled=false;
				document.getElementById('Save_View').disabled=false;
				document.getElementById('Save_Close').disabled=false;
				document.getElementById('Close').disabled=false;
				//document.getElementById('reversalVouhernumber').disabled=false;
				document.getElementById('reversalVoucherDate').disabled=false;
				var revVoucherNumberObj=document.getElementById('reversalVoucherNumber');
				if(revVoucherNumberObj!=null && revVoucherNumberObj!=undefined)
				{
				revVoucherNumberObj.disabled=false;
				}
				 
			
			}

</SCRIPT>
</body>
</html>
