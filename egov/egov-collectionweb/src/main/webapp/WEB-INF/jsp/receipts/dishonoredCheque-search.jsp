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

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="dishonorcheque.title" /></title>
<sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true" />
<script type="text/javascript"
	src="/EGF/resources/javascript/helper.js?rnd=${app_release_no}"></script>
<script type="text/javascript">
	function getAccountNumbers(branchId) {
		populateaccountNumber({
			bankBranchId : branchId
		});
	}

	function resetValues()
	{
		jQuery("select").val(-1);
		jQuery(":text").val("");
	}

	function validateFormAndSubmit() {
		var accountType = dom.get("instrumentMode").value;
		var accountDDNo = dom.get("chequeNumber").value;
		var accountDDDate = dom.get("chequeDate").value;
		dom.get("errorDiv").innerHTML = '';
		if (accountType == 0) {
			dom.get("errorDiv").style.display = '';
			dom.get("errorDiv").innerHTML = 'Please enter Instrument mode.';
			return false;
		} else if (accountDDNo == "") {
			dom.get("errorDiv").style.display = '';
			dom.get("errorDiv").innerHTML = 'Please enter Cheque/DD Number.';
			return false;
		} else if (accountDDDate == "") {
			dom.get("errorDiv").style.display = '';
			dom.get("errorDiv").innerHTML = 'Please enter Cheque/DD Date.';
			return false;
		} else {
			dom.get("errorDiv").style.display = 'none';
			dom.get("errorDiv").innerHTML = '';
		}
		document.dishonorForm.action = '/collection/receipts/dishonoredCheque-list.action';
		document.dishonorForm.submit();
	}

	function checkSelectedChqs(obj) {

	}
	function processDishonorCheque() {
		doLoadingMask();
		var value = new Array();
		var voucherHeaderIds = new Array();
		var receiptHeaderIds = new Array();
		var dishonorcheck = document.getElementsByName("selectedCheque");
		var receiptHdrId = document.getElementsByName("receiptHeaderId");
		var instrmntHdrId = document.getElementsByName("instrumentHeaderId");
		var voucherHdrId = document.getElementsByName("voucherHeaderId");
		var referenceNo = document.getElementsByName("referenceNum");
		var len = dishonorcheck.length;
		var i = 0, x = 0;
		var flag = "";
		var receiptId = "";
		var instrmndId = "";
		var voucherId = "";

		if (len > 0) {
			for (i = 0; i < len; i++) {
				if (dishonorcheck[i].checked) {
					receiptId = receiptHdrId[i].value;
					instrmndId = instrmntHdrId[i].value;
					voucherId = voucherHdrId[i].value;
					break;
				}
			}
			if (receiptId != "") {
				value[x++] = instrmndId;
				voucherHeaderIds[x-1] = voucherId;
				receiptHeaderIds[x-1] = receiptId;
				for (var j = i + 1; j < len; j++) {
					if (dishonorcheck[j].checked) {
						if (receiptId == receiptHdrId[j].value) {
							value[x++] = instrmntHdrId[j].value;
							voucherHeaderIds[x++] = voucherHdrId[j].value;
							flag = true;
							if (flag == "false" && flag != "") {
								bootbox
										.alert("Please Choose only cheques from the same Receipt No.");
								undoLoadingMask();
								return false;
							}
						} else if (receiptId != receiptHdrId[j].value) {
							flag = false;
							break;
						}

					}
				}

				if (flag == "false" && flag != "") {
					bootbox
							.alert("Please Choose only cheques from the same Receipt No.");
					undoLoadingMask();
					return false;
				}

			}
		}
		if (value.length == 0) {
			bootbox.alert("Please Choose Cheques to Dishonor.");
			undoLoadingMask();
			return false;
		}
		var referenceNum = "";
		var refFlag = false;
		for (i = 0; i < len; i++) {
			refFlag = false;
			if (dishonorcheck[i].checked) {
				referenceNum = referenceNo[i].value;
				refFlag = true;
			}
			if (refFlag == true && (referenceNum == null || referenceNum == "")) {
				bootbox.alert("Please enter	Reference No for selected Cheque/DD Number.");
				undoLoadingMask();
				return false;
			}
			console.log(referenceNum);
		}
		if ((flag == "true" || flag == "") && value.length > 0) {
			bootbox.confirm("Are you sure you want to process ?", function(result) {
				  if(result)
					  {
					  console.log("referenceNum" + referenceNum);  
						  document.dishonorForm.action = '/collection/receipts/dishonoredCheque-process.action?instHeaderIds='
							+ value+'&voucherHeaderIds='+voucherHeaderIds+'&referenceNo='+referenceNum + '&receiptHeaderIds=' + receiptHeaderIds;
						  document.dishonorForm.submit();
					  }
				  else
					  {
					  undoLoadingMask();
					  }
				}); 
		}
		return false;
	}

	function openVoucher(vid){
		var url = "/EGF/voucher/preApprovedVoucher-loadvoucherview.action?vhid="+ vid;
		window.open(url,'','width=900, height=700');
	}

	jQuery(document).ready(function(){
	    jQuery('.check').change(function() {
	        if(jQuery(this).is(":checked")) {
	        	jQuery('.check').prop('checked', false);
	        	jQuery(this).prop('checked', true);
	        }
	    });
	    var accNum = '<s:property value="accountNumber"/>';
	    jQuery.when(getAccountNumbers(dom.get('bankBranchId').value)).then(function(){
	        console.log("All done");
	    });
	    setTimeout(function(){
	    	 jQuery('#accountNumber').val(accNum);
	    }, 1000);
	});
</script>
</head>
<body>
	<s:form name="dishonorForm" action="dishonoredCheque" theme="simple"
		validate="true">
		<div style="color: green">
			<s:actionmessage />
		</div>
		<div style="color: red">
			<s:actionerror />
		</div>
		<div style="color: red">
			<div class="errorstyle" style="display: none" id="errorDiv"></div>
		</div>
		<div class="formmainbox">
			<div class="formheading">
				<div class="subheadnew">
					<s:text name="dishonorcheque.title" />
				</div>
			</div>
			<br />
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="greybox"></td>
					<td class="greybox"><s:text name="dishonorcheque.bankbranch" />:</td>
					<td class="greybox"><s:select name="bankBranchId"
							id="bankBranchId" list="dropdownData.bankBranchList"
							headerKey="-1" headerValue="---Choose---"
							listKey="bank.id + '-' + id"
							listValue="bank.name + ' ' + branchname"
							onchange="getAccountNumbers(this.value);" value="%{bankBranchId}" />
						<egov:ajaxdropdown id="accountNumber" fields="['Text','Value']"
							dropdownId='accountNumber'
							url='receipts/dishonoredCheque-getAccountNumbers.action'
							selectedValue="%{bank.id + '-' + id}" /></td>
					<td class="greybox"><s:text
							name="dishonorcheque.accountnumber" />:</td>
					<td class="greybox"><s:select name="accountNumber"
							id="accountNumber" list="dropdownData.accountNumberList"
							headerKey="-1" headerValue="---Choose---" listKey="id"
							listValue='name' value="%{accountNumber}" /></td>
				</tr>

				<tr>
					<td class="bluebox"></td>
					<td class="bluebox"><s:text
							name="dishonorcheque.instrumentmode" />:<span class="mandatory1">*</span></td>
					<td class="bluebox"><s:select headerValue="--Select--"
							headerKey="-1" list="instrumentModesMap" listKey="key"
							id="instrumentMode" listValue="value" label="instrumentMode"
							name="instrumentMode" /></td>
				</tr>

				<tr>
					<td class="greybox"></td>
					<td class="greybox"><s:text
							name="dishonorcheque.cheque.dd.number" />:<span
						class="mandatory1">*</span></td>
					<td class="greybox"><s:textfield name="chequeNumber"
							style="width: 200px;" id="chequeNumber" value="%{chequeNumber}"
							maxlength="6" cssClass="patternvalidation" data-pattern="number" /></td>

					<td class="greybox"><s:text
							name="dishonorcheque.cheque.dd.date" />:<span class="mandatory1">*</span></td>
					<s:date name="chequeDate" var="chqDate" format="dd/MM/yyyy" />
					<td class="greybox"><s:textfield id="chequeDate"
							name="chequeDate" value="%{chqDate}" placeholder="DD/MM/YYYY"
							cssClass="form-control datepicker"
							data-inputmask="'mask': 'd/m/y'" /></td>
				</tr>
			</table>
		</div>
		<br />
		<div class="buttonbottom">
			<table align="center">
				<tr class="buttonbottom" id="buttondiv" style="align: middle">
					<td><input type="button" class="buttonsubmit" value="VIEW"
						id="searchButton" onclick="return validateFormAndSubmit();" />&nbsp;</td>
					<td><input type="button" class="button" value="RESET"
						id="resetbutton" name="clear" onclick="resetValues();">&nbsp;</td>
					<td><input type="button" class="button" value="CLOSE"
						id="closeButton" name="closeButton" onclick="window.close();" /></td>
				</tr>
			</table>
		</div>

		<s:if test="%{searchResult.fullListSize != 0}">
			<table align="center">
				<tr>
					<td colspan="4">
						<div class="tbl2-container" id="tbl-container">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td><display:table name="searchResult"
											id="currentRowObject" uid="currentRowObject" pagesize="30"
											class="tablebottom" style="width:100%;" cellpadding="0"
											cellspacing="0" requestURI="">

											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="Sl.No"
												style="width:8%;text-align:left">
												<s:property
													value="#attr.currentRowObject_rowNum + (page-1)*pageSize" />
												<s:hidden name="receiptHeaderId" id="receiptHeaderId"
													value="%{#attr.currentRowObject.receiptHeaderid}" />
												<s:hidden name="instrumentHeaderId" id="instrumentHeaderId"
													value="%{#attr.currentRowObject.instrumentHeaderid}" />
												<s:hidden name="voucherHeaderId" id="voucherHeaderId"
													value="%{#attr.currentRowObject.voucherHeaderId}" />
											</display:column>

											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="Voucher Number"
												style="width:30%;text-align:left">
												<a href="#"
													onclick="openVoucher(<s:property value='%{#attr.currentRowObject.voucherHeaderId}'/>);"><s:property
														value="%{#attr.currentRowObject.voucherNumber}" /> </a>
											</display:column>

											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="Receipt Number"
												style="width:30%;text-align:left" property="receiptNumber" />

											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="Receipt Date"
												style="width:15%;text-align:left" property="receiptDate" />

											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="DD/Chq Number"
												style="width:15%;text-align:left"
												property="instrumentNumber" />

											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="DD/Chq Date"
												style="width:15%;text-align:center"
												property="instrumentDate" />

											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="DD/Chq Amount"
												style="width:25%;text-align:right"
												property="instrumentAmount" />

											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="Bank"
												style="width:30%;text-align:left" property="bankName" />

											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="Bank Account Number"
												style="width:20%;text-align:left" property="accountNumber" />

											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="Status"
												style="width:20%;text-align:center" property="description" />
											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="Reference No"
												style="width:20%;text-align:center">
												<s:textfield name="referenceNum" style="width: 200px;"
													id="referenceNum" cssClass="form-control" />
											</display:column>

											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="Select"
												style="width:3%;text-align:center">
												<s:checkbox class="check" id="selectedCheque"
													name="selectedCheque" />
											</display:column>
										</display:table></td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
			</table>
			<div class="buttonbottom">
				<td><input type="submit" class="button" value="Process"
					id="process" name="button"
					onclick="return processDishonorCheque();" /></td>
			</div>
		</s:if>
		<s:elseif test="%{searchResult.fullListSize == 0}">
			<tr>
				<td align="center"><font color="red">No record Found.</font></td>

			</tr>
		</s:elseif>
	</s:form>
</body>
</html>