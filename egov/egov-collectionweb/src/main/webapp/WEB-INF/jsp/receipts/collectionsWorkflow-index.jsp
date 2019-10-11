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
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<head>
<title>Collections Submission/Approval</title>
<script>
jQuery(document).ready(function() {
     jQuery(" form ").submit(function( event ) {
    	 doLoadingMask();
    });
     doLoadingMask();
 });

jQuery(window).load(function () {
	undoLoadingMask();
});



var isSubmitAction = ${isSubmitAction};
var isApproveAction = ${isApproveAction};
var totalAmount = ${totalAmount};
var cashAmount = ${cashAmount};
var chequeAmount = ${chequeAmount};
var ddAmount = ${ddAmount};
var cardAmount = ${cardAmount};
var listApproveAction = "collectionsWorkflow-listApprove.action"

// Enable/disable buttons based on user selections
function enableButtons() {
	if (isChecked(document.getElementsByName('receiptIds'))) {
		if(isSubmitAction) {
			document.collectionsWorkflowForm.submitPageCollections.disabled = true;
			document.collectionsWorkflowForm.submitCollections.disabled = true;
			document.collectionsWorkflowForm.submitCollections.className="buttonsubmit";
		} else {
			document.collectionsWorkflowForm.approveCollections.disabled = false;
			document.collectionsWorkflowForm.rejectCollections.disabled = false;
			document.collectionsWorkflowForm.approveCollections.className= "buttonsubmit";
			document.collectionsWorkflowForm.rejectCollections.className= "buttonsubmit";

		}
	} else {
		if(isSubmitAction) {
			document.collectionsWorkflowForm.submitCollections.disabled = true;
			document.collectionsWorkflowForm.submitCollections.className="button";
		} else {
			document.collectionsWorkflowForm.approveCollections.disabled = true;
			document.collectionsWorkflowForm.rejectCollections.disabled = true;
			document.collectionsWorkflowForm.approveCollections.className= "button";
			document.collectionsWorkflowForm.rejectCollections.className= "button";

		}
	}
}

// Adds commas to given numeric string
function addCommas(nStr)
{
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
	}
	return x1 + x2;
}

//Displays the summary of amounts
function refreshSummary() {
	/* //document.collectionsWorkflowForm.totalAmount.value = addCommas(totalAmount.toFixed(2));
	document.collectionsWorkflowForm.cashAmount.value = addCommas(cashAmount.toFixed(2));
	document.collectionsWorkflowForm.chequeAmount.value = addCommas(chequeAmount.toFixed(2));
	document.collectionsWorkflowForm.ddAmount.value = addCommas(ddAmount.toFixed(2));
	document.collectionsWorkflowForm.cardAmount.value = addCommas(cardAmount.toFixed(2)); */
}

// Handle the event when user selects/deselects a receipt to be submitted/approved/rejected
function handleReceiptSelectionEvent(receiptAmount, instrumentType, isSelected) {
	// Enable/disable buttons based on number of receipts selected
	enableButtons();
	var delta = 0;

	if(isSelected == true) {
		delta = receiptAmount;
	} else {
		delta = receiptAmount * -1;
	}

	// Increment/decrement all amounts
	totalAmount = totalAmount + delta;

	if(instrumentType == "cash") {
		cashAmount = cashAmount + delta;
	}
	
	if(instrumentType == "cheque") {
		chequeAmount = chequeAmount + delta;
	}

	if(instrumentType == "dd") {
		ddAmount = ddAmount + delta;
	}

	if(instrumentType == "card") {
		cardAmount = cardAmount + delta;
	}

	// Set the total amount of selected receipts
	refreshSummary();
}

// Check if at least one receipt is selected
function isChecked(chk) {
	if (chk.length == undefined) {
 		if (chk.checked == true) {
  			return true;
 		} else {
 	 		return false;
 		}
 	} else {
 		for (i = 0; i < chk.length; i++)
		{
			if (chk[i].checked == true ) {
				return true;
			}
		}
		return false;
 	}
}

// Changes selection of all receipts to given value (checked/unchecked)
function changeSelectionOfAllReceipts(checked) {
	chk = document.getElementsByName('receiptIds');
	if (chk.length == undefined) {
		chk.checked = checked;
 	} else {
 		for (j = 0; j < chk.length; j++)
		{
 			chk[j].checked = checked;
		}
 	}
}

//DeSelect all receipts
function deSelectAll() {
	// DeSelect all checkboxes
	changeSelectionOfAllReceipts(false);

 	// Set all amounts to zero
	totalAmount = 0;
	cashAmount = 0;
	chequeAmount = 0;
	ddAmount = 0;
	cardAmount = 0;

	// Refresh the summary section
	refreshSummary();

	// Enable/disable buttons
	enableButtons();
}

// Select all receipts
function selectAll() {
	// Select all checkboxes
	changeSelectionOfAllReceipts(true);

 	// Set all amounts to original values
	totalAmount = ${totalAmount};
	cashAmount = ${cashAmount};
	chequeAmount = ${chequeAmount};
	ddAmount = ${ddAmount};
	cardAmount = ${cardAmount};

	// Refresh the summary section
	refreshSummary();

	// Enable/disable buttons
	enableButtons();
}

function changeCounterId(newCounterId) {
	if(newCounterId != ${counterId}) {
		document.collectionsWorkflowForm.action=listApproveAction;
		document.collectionsWorkflowForm.submit();
	}
}

function changeUserName(newUserName) {
	if(newUserName != "${userName}") {
		document.collectionsWorkflowForm.action=listApproveAction;
		document.collectionsWorkflowForm.submit();
	}
}

function changeServiceCode(newServiceCode) {
	if(newServiceCode != "${serviceCode}") {
		document.collectionsWorkflowForm.action=listApproveAction;
		document.collectionsWorkflowForm.submit();
	}
}

function setCheckboxStatuses(isSelected) {
	if(isSelected == true) {
		selectAll();
	} else {
		deSelectAll();
	}
}

function readOnlyCheckBox() {
   return false;
}

function onChangePaymentMode(obj)
{
    document.collectionsWorkflowForm.action="collectionsWorkflow-listWorkflow.action";
	document.collectionsWorkflowForm.submit();
}
</script>
</head>
<body onload="javascript:refreshSummary()">
	<div class="formmainbox">

		<div id="loadingMask"
			style="display: none; overflow: hidden; text-align: center">
			<img src="/collection/resources/images/bar_loader.gif" /> <span
				style="color: red">Please wait....</span>
		</div>

		<s:form theme="simple" name="collectionsWorkflowForm"
			onsubmit="return checkonsubmit(event);">
			 <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<div class="subheadnew">
				<s:if test="%{isSubmitAction == true}">
					<s:text name="collectionsWorkflow.submitTitle" />
				</s:if>
				<s:else>
					<s:text name="collectionsWorkflow.approveTitle" />
				</s:else>
			</div>
			<br />
			<s:if test="%{hasErrors()}">
				<div id="actionErrorMessages" class="errorstyle">
					<s:actionerror />
					<s:fielderror />
				</div>
			</s:if>
			<s:if test="%{hasActionMessages()}">
				<div id="actionMessages" class="messagestyle">
					<s:actionmessage theme="simple" />
				</div>
			</s:if>
			<tr>
				<div align="right">
					<td class="blueborderfortd"><s:text
							name="collectionsWorkflow.payment.mode" /></td>
					<td class="bluebox"><s:select headerKey="ALL"
							headerValue="%{getText('collectionsWorkflow.payment.mode.all')}"
							list="paymentModesMap" id="paymentMode" label="paymentMode"
							name="paymentMode" value="%{paymentMode}"
							onchange="onChangePaymentMode(this);" /></td>
				</div>
				<s:hidden name="inboxItemDetails" id="inboxItemDetails"
					value="%{inboxItemDetails}" />

			</tr>
			<br />
			<br />
			<%-- <s:if test="%{!receiptHeaders.isEmpty() && !hasErrors()}"> --%>
			<%-- CHANGES --%>
			<%-- The current requirement is to show validation message on the same page, so we cannot hide the contents --%>
			<s:if test="%{!receiptHeaders.isEmpty()}">
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="tablebottom">

					<display:table name="receiptHeaders" uid="currentRow" pagesize="30"
						style="border:1px;empty-cells:show;border-collapse:collapse;"
						cellpadding="0" cellspacing="0" export="false" requestURI="">
						<s:hidden name="receiptDate" id="receiptDate"
							value="%{receiptdate}" />
						<s:if test="%{allowPartialSelection == true}">
							<!--  Partial selection allowed. Enable the checkboxes -->
							<display:column headerClass="bluebgheadtd"
								class="blueborderfortd"
								title="Select?<input type='checkbox' name='selectAllReceipts' value='on' onClick='setCheckboxStatuses(this.checked)' checked/>"
								style="width:5%; text-align: center">
								<input name="receiptIds" type="checkbox" id="receiptIds"
									value="${currentRow.id}"
									onClick="handleReceiptSelectionEvent(${currentRow.totalAmount}, '${currentRow.instrumentType}', this.checked)"
									checked />
							</display:column>
						</s:if>
						<s:else>
							<!--  Partial selection allowed. Disable the checkboxes -->
							<display:column headerClass="bluebgheadtd"
								class="blueborderfortd"
								title="Select?<input type='checkbox' name='selectAllReceipts' value='on' onClick='return readOnlyCheckBox()' checked/>"
								style="width:5%; text-align: center">
								<input name="receiptIds" type="checkbox" id="receiptIds"
									value="${currentRow.id}" onClick="return readOnlyCheckBox()"
									checked />
							</display:column>
						</s:else>
						<display:column headerClass="bluebgheadtd" class="blueborderfortd"
							property="receiptnumber" title="Receipt No."
							style="width:10%; text-align: center" />

						<display:column headerClass="bluebgheadtd" class="blueborderfortd"
							property="receiptdate" title="Receipt Date"
							format="{0,date,dd/MM/yyyy}"
							style="width:10%; text-align: center" />


						<display:column headerClass="bluebgheadtd" class="blueborderfortd"
							property="service.name" title="Service"
							style="width:10%; text-align: center;" />

						<display:column headerClass="bluebgheadtd" class="blueborderfortd"
							title="Bill Number" style="width:10% text-align: right;">&nbsp;${currentRow.referencenumber}</display:column>

						<display:column headerClass="bluebgheadtd" class="blueborderfortd"
							property="totalAmount" title="Receipt Amount"
							format="{0, number, #,##0.00}"
							style="width:10%; text-align: right" />

						<display:column headerClass="bluebgheadtd" class="blueborderfortd"
							property="instrumentsAsString" title="Instrument(s)"
							style="width:15%; text-align: right;" />

						<display:column headerClass="bluebgheadtd" class="blueborderfortd"
							property="payeeName" title="Payee Name"
							style="width:15%; text-align: center;" />

						<display:column headerClass="bluebgheadtd" class="blueborderfortd"
							title="Bill Description" style="width:15%;">&nbsp;${currentRow.referenceDesc}</display:column>

					</display:table>

					<br />

					<table width="50%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tablebottom input-nobackground">
						<tr>
							<th class="bluebgheadtd"><b><s:text
										name="%{getText('collectionsWorkflow.summary.instrumentType')}" /></b></th>
							<th class="bluebgheadtd"><b><s:text
										name="%{getText('collectionsWorkflow.summary.amount')}" /></b></th>
						</tr>
						<tr>
							<td class="blueborderfortd"><b><s:text
										name="%{getText('collectionsWorkflow.summary.cash')}" /></b></td>
							<td class="blueborderfortd"><s:textfield id="cashAmount"
									name="cashAmount"
									style="border-width:0px; text-align: right; width: 90%"
									disabled="true" /></td>
						</tr>
						<tr>
							<td class="blueborderfortd"><b><s:text
										name="%{getText('collectionsWorkflow.summary.cheque')}" /></b></td>
							<td class="blueborderfortd"><s:textfield name="chequeAmount"
									style="border-width:0px; text-align: right; width: 90%"
									disabled="true" /></td>
						</tr>
						<tr>
							<td class="blueborderfortd"><b><s:text
										name="%{getText('collectionsWorkflow.summary.dd')}" /></b></td>
							<td class="blueborderfortd"><s:textfield name="ddAmount"
									style="border-width:0px; text-align: right; width: 90%"
									disabled="true" /></td>
						</tr>
						<tr>
							<td class="blueborderfortd"><b><s:text
										name="%{getText('collectionsWorkflow.summary.card')}" /></b></td>
							<td class="blueborderfortd"><s:textfield name="cardAmount"
									style="border-width:0px; text-align: right; width: 90%"
									disabled="true" /></td>
						</tr>
						<tr>
							<td class="blueborderfortd"><b><s:text
										name="%{getText('collectionsWorkflow.summary.bank')}" /></b></td>
							<td class="blueborderfortd"><s:textfield name="bankAmount"
									style="border-width:0px; text-align: right; width: 90%"
									disabled="true" /></td>
						</tr>
						<tr>
							<td class="blueborderfortd"
								style="background-color: #F5F5F5; text-align: center;"><b><s:text
										name="%{getText('collectionsWorkflow.summary.total')}" /></b></td>
							<td class="blueborderfortd" style="background-color: #F5F5F5;"><s:textfield
									name="totalAmount" disabled="true"
									style="border-width: 0px;font-weight: bold; background-color: #F5F5F5; text-align: right; width: 90%" /></td>
						</tr>


					</table>

					<br>
					<br>
					<br>
					
					<!-- start workflow changes 1 -->
					<s:if test="%{isSubmitAction == true}">
					<div class="panel panel-primary" style="width: 80%; margin: 0 auto;" data-collapsed="0" >
					<div class="panel-heading">
				<div class="panel-title">
					<span class="bold"> <s:text
                                                name="title.approval.information" />
                                        </span>
				</div>					
			</div>
					<div class="panel-body" style="margin: 0 auto;">
					<div class="row show-row"  id="approverDetailHeading">
				<div class="show-row form-group" >
					<label class="col-sm-2 control-label text-right" for="approverDepartmentId">
                                                <s:text name="wf.approver.department" /><span class="mandatory"></span>
                                            </label>
					<div class="col-sm-3 add-margin">
						
                                            <s:select id="approverDepartmentId"
                                                name="approverDepartmentId" cssClass="form-control"
                                                list="departmentList" listKey="id" listValue="name"
                                                headerKey="-1" headerValue="%{getText('wf.select')}"
                                                onchange="return onSelectDepartment(event);"
                                            />
					</div>
					<label class="col-sm-2 control-label text-right" for="approverDesignationId">
                                                <s:text name="wf.approver.designation" /><span class="mandatory"></span>
                                            </label>
					<div class="col-sm-3 add-margin" style="margin-right: 100px;">
						
                                            <s:select id="approverDesignationId"
                                                name="approverDesignationId" cssClass="form-control"
                                                list="designationList" listKey="id" listValue="name"
                                                headerKey="-1" headerValue="%{getText('wf.select')}"
                                                onchange="return onSelectDesignation(event);"
                                            />				
					</div>
				</div>
				<div class="show-row form-group">
					<label class="col-sm-2 control-label text-right" for="approverIdPositionId">
                        <s:text name="wf.approver" /><span class="mandatory"></span>
                    </label>
					<div class="col-sm-3 add-margin">

                        <s:select id="approverIdPositionId" name="approverIdPositionId"
                            cssClass="form-control"
                            list="employeeList" listKey="id" listValue="name"
                            headerKey="-1" headerValue="%{getText('wf.select')}"
                            onchange="return onSelectApprover(event);"
                        />
					</div> 
				</div>
				</div>
				<div class="row">
					<label class="col-sm-2 control-label text-right" style="margin-left: 40px;"><s:text name="Remarks" /></label>
					<div class="col-sm-8 add-margin">
						<s:textarea id="remarks" name="remarks" maxlength="1024" cols="40" cssClass="form-control"></s:textarea>
					</div>
				</div>
				<br/>
				</div>
				</div>
				</s:if>
				<!-- end workflow changes 1 -->
					
					
					<br />
					<br />
					<br />
					<div class="buttonbottom">
						<!--  If action is submit, show only submit button -->
						<s:if test="%{isSubmitAction == true}">
							<s:submit type="submit" cssClass="buttonsubmit btn"
								id="submitPageCollections" name="submitPageCollections"
								value="Submit Page Collections" disabled="false"
								onclick="doLoadingMask('#loadingMask');document.collectionsWorkflowForm.action='collectionsWorkflow-submitCollections.action'" />
							<s:submit type="submit" cssClass="buttonsubmit btn"
								id="submitCollections" name="submitCollections"
								value="Submit All Collections" disabled="false"
								onclick="doLoadingMask('#loadingMask');document.collectionsWorkflowForm.action='collectionsWorkflow-submitAllCollections.action'" />
						</s:if>
						<!-- else show only approve and reject buttons -->
						<s:else>
							<s:submit type="submit" cssClass="buttonsubmit"
								id="approveCollections" name="approveCollections"
								value="Approve Page Collections" disabled="false"
								onclick="doLoadingMask('#loadingMask');document.collectionsWorkflowForm.action='collectionsWorkflow-approveCollections.action'" />
				&nbsp;
				<s:submit type="submit" cssClass="buttonsubmit"
								id="approveCollections" name="approveCollections"
								value="Approve All Collections" disabled="false"
								onclick="doLoadingMask('#loadingMask');document.collectionsWorkflowForm.action='collectionsWorkflow-approveAllCollections.action'" />
				&nbsp;
				<s:submit type="submit" cssClass="buttonsubmit"
								id="rejectCollections" name="rejectCollections"
								value="Reject Collections" disabled="false"
								onclick="doLoadingMask('#loadingMask');document.collectionsWorkflowForm.action='collectionsWorkflow-rejectCollections.action'" />
						</s:else>
						&nbsp;<input type="button" class="button" id="buttonClose"
							value="<s:text name='common.buttons.close'/>"
							onclick="window.close()" />
					</div>
				</table>
			</s:if>

			<s:if test='%{receiptHeaders.isEmpty()}'>
				<table width="90%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="tablebottom">
					<tr>
						<div>&nbsp;</div>
						<div class="subheadnew">
							<s:text name="collectionsWorkflow.noReceipts" />
						</div>
					</tr>
				</table>
			</s:if>
		</s:form>
	</div>
	<script
		src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
	<!-- start workflow changes 2 -->
	<script>

    	var $approverDepartmentElement, $approverDesignationElement, $approverEmployeeElement, $btnSubmitPageElement, $btnSubmitAllElement;


    	jQuery(document).ready(function(){
    		$approverDepartmentElement = document.collectionsWorkflowForm.approverDepartmentId;
    		$approverDesignationElement = document.collectionsWorkflowForm.approverDesignationId;
    		$approverEmployeeElement = document.collectionsWorkflowForm.approverIdPositionId;
    		$btnSubmitPageElement = document.collectionsWorkflowForm.submitPageCollections;
    		$btnSubmitAllElement = document.collectionsWorkflowForm.submitCollections;


    		$approverDesignationElement.disabled = true;
    		$approverEmployeeElement.disabled = true;
    		$btnSubmitPageElement.disabled = true;
    		$btnSubmitAllElement.disabled = true;

            jQuery('.text-left').html('<b style="color: red">*</b>' + jQuery('.text-left').text().replace('*', ''));
    	});

    	function clearSelect($sel) {
    		var opts = $sel.options;
    		for (var i = opts.length - 1; i >= 0; i--) {
    			opts.remove(i);
    		}
    		opts.add(new Option('<s:text name="wf.select" />', "-1"));
    	}


    	function checkonsubmit(event) {
    		var dpt = $approverDepartmentElement.value;
    		var dsg = $approverDesignationElement.value;
    		var emp = $approverEmployeeElement.value;
    		var unselectedfields = [];

    		if(dpt == "-1") {
    			unselectedfields.push('<s:text name="wf.approver.department" />');
    		}
    		if(dsg == "-1") {
    			unselectedfields.push('<s:text name="wf.approver.designation" />');
    		}
    		if(emp == "-1") {
    			unselectedfields.push('<s:text name="wf.approver" />');
    		}
    		if(unselectedfields.length > 0) {
    			event.preventDefault();
    			var fields = unselectedfields.join(', ');
    			bootbox.alert("Please select " + unselectedfields);
    			return false;
    		}
    		return true;
    	}

    	function onSelectDepartment() {
    		var $select = $approverDepartmentElement;
    		var val = $select.value;
    		$approverDesignationElement.disabled = false;
    		$approverEmployeeElement.disabled = true;
    		clearSelect($approverEmployeeElement);
            $approverDesignationElement.selectedIndex = 0;
    	}
    	function onSelectDesignation() {
    		var $select = $approverDesignationElement;
    		$approverEmployeeElement.disabled = false;
    		clearSelect($approverEmployeeElement);
    		jQuery.getJSON('/collection/receipts/workflowApproverAjax.action', {
                queryField: 'employee',
                approverDepartmentId: $approverDepartmentElement.value,
                approverDesignationId: $approverDesignationElement.value
            }).done(function(optionsMap) {
                console.log('optionsMap: employee', optionsMap);
                var emptyMap = true;
                for(var key in optionsMap) {
                    $approverEmployeeElement.options.add(new Option(optionsMap[key], key+''));
                    emptyMap = false;
                }
                $approverEmployeeElement.selectedIndex = 0;
                if (emptyMap) {
                    $approverEmployeeElement.options[0].text = '<s:text name="wf.no.employee" />'
                }
            }).fail(function(error) {
                console.log('error', e);
                $approverEmployeeElement.options[0].text = '<s:text name="wf.error.employee" />';
            });
    	}
    	function onSelectApprover() {
    		var $select = $approverEmployeeElement;
    		var val = $select.value;
    		if(val != "-1") {
    			$btnSubmitPageElement.disabled = false;
    			$btnSubmitAllElement.disabled = false;
    		} else {
    			$btnSubmitPageElement.disabled = true;
    			$btnSubmitAllElement.disabled = true;
    		}
    	}
    </script>
    <!-- end workflow changes 2 -->

<style>

.form-inline {
    text-align: center;
}
.form-inline .center-block {
    display: inline-block;
    text-align: initial;
}
.form-group.mandatory label {
    position: relative;
}
.form-group.mandatory label::before {
    content: ': ';
    color: black;
    display: inline-block;
    position: absolute;
    right: 0;
}
.form-group.mandatory label::after {
    content: ': ';
    color: black;
    display: inline-block;
}

.row > .form-group {
    position: relative;
    margin-left: 50px;
    margin-top: 12px;
}

.left-pd {
	padding-left: 50px;
}

.no-float {
	float: none;
}
</style>
</body>
</html>
