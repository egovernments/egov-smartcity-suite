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
	src="${pageContext.request.contextPath}/resources/javascript/contra.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>

</head>
<body onload="onloadTask();">
	<s:form action="contraBTC" theme="simple" name="cbtcform">
		<s:push value="model">
			<jsp:include page="../budget/budgetHeader.jsp">
				<jsp:param value="Bank to Cash Transfer" name="heading" />
			</jsp:include>
			<div class="formmainbox">
				<div class="formheading" />
				<div class="subheadnew">Reverse Cash Withdrawal</div>
				<div id="listid" style="display: block">
					<br />
				</div>
				<div align="center">
					<font style='color: red;'><p class="error-block"
							id="lblError"></p></font> <span class="mandatory"> <s:actionerror
							id="actionerror" /> <s:fielderror id="fielderror" /> <s:actionmessage
							id="actionmessage" />
					</span>
				</div>
				<%@include file="contraBTC-form.jsp"%>
				<tr>
					<s:if test="%{shouldShowHeaderField('vouchernumber')}">
						<td class="bluebox"><s:text name="reversalVoucherNumber" /><span
							class="mandatory">*</span></td>
						<td class="bluebox"><s:textfield name="reverseVoucherNumber"
								id="reversalVoucherNumber" /></td>
					</s:if>
					<td class="bluebox"><s:text name="reversalVoucherDate" /><span
						class="mandatory">*</span></td>
					<td class="bluebox"><s:textfield name="reverseVoucherDate"
							id="reversalVoucherDate"
							onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
						href="javascript:show_calendar('cbtcform.reversalVoucherDate');"
						style="text-decoration: none">&nbsp;<img tabIndex="-1"
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></A>(dd/mm/yyyy)</td>
				</tr>
		</s:push>
		<br />
		<br />
		<input type="hidden" name="voucherHeader.id"
			value='<s:property value="voucherHeader.id"/>' id="voucherHeaderId" />
		<div id="buttons">
			<s:submit type="submit" cssClass="buttonsubmit"
				value="Reverse and View" method="saveReverse" id="reverse"
				onClick="return validate();" />
			<s:submit type="submit" cssClass="buttonsubmit"
				value="Reverse and Close" method="saveReverse" id="reverse"
				onClick="return validate();" />
			<s:submit value="Close" onclick="javascript: self.close()"
				id="button2" cssClass="button" />
		</div>
		<div id="resultGrid"></div>
		</div>
	</s:form>
	<SCRIPT type="text/javascript">

function onloadTask(){
	disable(0);
	document.getElementById('reversalVoucherDate').readOnly = false;
	document.getElementById('voucherHeaderId').readOnly = false;
	if(document.getElementById('reversalVoucherNumber') != undefined){
		document.getElementById('reversalVoucherNumber').readOnly = false;
	}
	document.getElementById('reverse').readOnly = false;
	var element = document.getElementById('accountNumber');
	if(element != undefined){
		populateAvailableBalance(element)
		if(element.value != -1){
			populateNarration(element)
		}
	}
}


function disable(frmIndex){
	for(var i=0;i<document.forms[frmIndex].length;i++){
		if(document.forms[frmIndex].elements[i].value != "Close" && document.forms[frmIndex].elements[i].value != "Print"){
			document.forms[frmIndex].elements[i].readOnly = true;
		}
	}
}


function validate(){
	document.getElementById('lblError').innerHTML = "";
		
	if(document.getElementById('reversalVoucherDate').value == ''){
		document.getElementById('lblError').innerHTML = "Please enter Reversal Voucher Date";
		return false;
	}
	if(document.getElementById('reversalVoucherNumber') != undefined && document.getElementById('reversalVoucherNumber').value == ''){
		document.getElementById('lblError').innerHTML = "Please enter Reversal Voucher Number";
		return false;
	}
}
</SCRIPT>
</body>
</html>
