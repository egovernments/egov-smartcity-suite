<!--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  -->
<html>
<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>

<head>
<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>" />
<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" />
<script type="text/javascript"
  src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.columnFilter.js' context='/egi'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"
  type="text/javascript"></script>
<script type="text/javascript" src="<c:url value='/resources/app/js/reconciliationHelper.js?rnd=${app_release_no}'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/jquery.js' context='/egi'/>"></script>
    <script src="<c:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
    <script src="<c:url value='/resources/global/js/bootstrap/bootbox.min.js' context='/egi'/>"></script>
    <script src="<c:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
    <script src="<c:url value='/resources/global/js/egov/custom.js' context='/egi'/>"></script> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">


<title><s:text name="bankreconciliation" /></title>
<script type="text/javascript">


	function validate() {
		if (document.getElementById("bankId").value == "") {
			bootbox.alert("Select Bank");
			return false;
		}
		if (document.getElementById("branchId").value == "") {
			bootbox.alert("Select Branch");
			return false;
		}
		if (document.getElementById("accountId").value == "") {
			bootbox.alert("Select Account");
			return false;
		}
		var toDateStr=document.getElementById("toDate").value;
		
		var reconDateStr =document.getElementById("reconciliationDate").value;
		if (reconDateStr == "") {
			bootbox.alert("Select <s:text name='reconciliationdate'/>");
			return false;
		}
		if (document.getElementById("fromDate").value == "") {
			bootbox.alert("Select <s:text name='fromdate'/>");
			return false;
		}
		if (toDateStr == "") {
			bootbox.alert("Select <s:text name='todate'/>");
			return false;
		}
		
		if(toDateStr!=null && reconDateStr!=null)
		{
		
		var toDateParts=	toDateStr.split("/");
		if(toDateParts.length!=3)
		{
		bootbox.alert("Enter date is 'DD/MM/YYYY' format only");
		return false;
		}
		var toDate=new Date(toDateParts[1]+"/"+toDateParts[0]+"/"+toDateParts[2]);
		var reconDateParts=	reconDateStr.split("/");
		
		if(reconDateParts.length!=3)
		{
		bootbox.alert("Enter date is 'DD/MM/YYYY' format only");
		return false;
		}
		var reconDate=new Date(reconDateParts[1]+"/"+reconDateParts[0]+"/"+reconDateParts[2]);
		//bootbox.alert(reconDate.toString('MM-dd-yyyy'));
		if(reconDate<toDate)
		{
		bootbox.alert("<s:text name='reconciliationdate'/> must be higher or equal to <s:text name='todate'/>");
		return false;
		}
		}
	    callAjaxSearch();
		//return true;
	}
	function populatebranch(obj) {
		var bid = document.getElementById("bankId").value;
		populatebranchId( {
			bankId : bid
		})
	}

	function populateaccount(obj) {
		var bid = document.getElementById("branchId").value;
		populateaccountId( {
			branchId : bid
		})
	}
</script>
</head>
<body>
	<s:form action="autoReconciliation" theme="simple" name="mrform" id="mrform">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param value="Auto Bank Reconciliation" name="heading" />
		</jsp:include>
		<div class="formmainbox">
			<div class="formheading"></div>
			<div class="subheadnew">
				<s:text name="manualbankreconciliation" />
			</div>
	
		<div align="center">
			<font style='color: red;'>
				<p class="error-block" id="lblError"></p>
			</font>
		</div>
		<span class="mandatory1">
			<div id="Errors">
				<s:actionerror />
				<s:fielderror />
			</div> <s:actionmessage />
		</span>
		<center>
			<table border="0" width="100%" cellspacing="0" cellpadding="0">
				<tr>
					<td class="greybox"></td>
					<td class="greybox"><s:text name="bank" /> <span
						class="greybox"><span class="mandatory1">*</span></span></td>
					<egov:ajaxdropdown id="branchId" fields="['Text','Value']"
						dropdownId="branchId"
						url="/voucher/common-ajaxLoadBankBranchesByBank.action" />
					<td class="greybox"><s:select name="reconcileBean.bankId" id="bankId"
							list="dropdownData.bankList" listKey="id" listValue="name"
							headerKey="" headerValue="----Choose----"
							onchange="populatebranch(this);" value="%{bankId}" /></td>
					<td class="greybox"><s:text name="bankbranch" /> <span
						class="greybox"><span class="mandatory1">*</span></span></td>
					<egov:ajaxdropdown id="accountId" fields="['Text','Value']"
						dropdownId="accountId"
						url="/voucher/common-ajaxLoadBankAccountsByBranch.action" />
					<td class="greybox"><s:select name="reconcileBean.branchId" id="branchId"
							list="dropdownData.branchList" listKey="id" listValue="name"
							headerKey="" headerValue="----Choose----"
							onchange="populateaccount(this);" /></td>
				</tr>
				<tr>
					<td class="bluebox"></td>
					<td class="bluebox"><s:text name="bankaccount" /> <span
						class="bluebox"><span class="mandatory1">*</span></span></td>
					<td class="bluebox"><s:select name="reconcileBean.accountId" id="accountId"
							list="dropdownData.accountList" listKey="id"
							listValue="accountnumber" headerKey=""
							headerValue="----Choose----" /></td>
					<td class="bluebox"><s:text name="reconciliationdate" /> <span
						class="bluebox"><span class="mandatory1">*</span></span></td>
					<td class="bluebox"><s:textfield name="reconcileBean.reconciliationDate"
							id="reconciliationDate"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							value="%{asOnDate}" /> <a
						href="javascript:show_calendar('mrform.reconciliationDate');"
						style="text-decoration: none">&nbsp;<img tabIndex="-1"
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></A>

					</td>
				</tr>
				<tr>
					<td class="greybox"></td>
					<td class="greybox"><s:text name="fromdate" /> <span
						class="greybox"><span class="mandatory1">*</span></span></td>
					<td class="greybox"><s:textfield name="reconcileBean.fromDate" id="fromDate"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							value="%{fromDate}" /> <a
						href="javascript:show_calendar('mrform.fromDate');"
						style="text-decoration: none">&nbsp;<img tabIndex="-1"
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></A>

					</td>
					<td class="greybox"><s:text name="todate" /> <span
						class="greybox"><span class="mandatory1">*</span></span></td>
					<td class="greybox"><s:textfield name="reconcileBean.toDate" id="toDate"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							value="%{toDate}" /> <a
						href="javascript:show_calendar('mrform.toDate');"
						style="text-decoration: none">&nbsp;<img tabIndex="-1"
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></A>

					</td>
				</tr>

			</table>

			<div class="buttonbottom" id="buttondiv">
				<table>
					<tr>
						<td><input  type="button" class="buttonsubmit"
								value="Search" name="Search" method="search"
								onclick="return validate();" /></td>
						<td><input type="button" value="Close"
							onclick="javascript:window.close()" class="buttonsubmit" /></td>
					</tr>
				</table>
			</div>
      <div id="resultDiv"> </div>
      
      <div class="buttonbottom" id="reconcileDiv" style="display: none">
        <table>
          <tr>
            <td><input  type="button" class="buttonsubmit"
                value="Reconcile" name="Reconcile" method="reconcile"
                onclick="return validateReconcile();" /></td>
            <td><input type="button" value="Close"
              onclick="javascript:window.close()" class="buttonsubmit" /></td>
          </tr>
        </table>
      </div>
		</center>
      </div>
	</s:form>
</body>
</html>

