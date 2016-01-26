<!--  #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------  -->
<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%@ include file='billInfoFetchTitle.jsp'%>
</title>
<sx:head />
<script type="text/javascript">
	<s:if test="%{hasActionMessages()}">
		undoLoadingMask();
	</s:if>
</script>
</head>
<script type="text/javascript" src="/EGF/resources/javascript/helper.js"></script>
<body>
	<s:actionmessage />
	<s:actionerror />
	<s:form name="billInfoFetchForm" id="billInfoFetchForm" theme="simple">
		<s:token />
		<div class="formmainbox"></div>
		<div class="formheading" />
		<div class="subheadnew">
			<%@ include file='billInfoFetchTitle.jsp'%>
		</div>
		<br />
		<table align="center" width="100%" cellspacing="0">
			<tr>
				<td width="25%" class="greybox"
					style="text-align: right; font-weight: bold;"><s:text
						name="monthAndYear" /> :</td>
				<td width="25%" class="greybox" style="text-align: left;">
					&nbsp;&nbsp;&nbsp;&nbsp;<s:property value="monthAndYear"
						default="NA" /> <s:hidden name="monthAndYear"
						value="%{monthAndYear}" />
				</td>
				<td width="25%" class="greybox"
					style="text-align: right; font-weight: bold;"><s:text
						name="region" />:</td>
				<td width="25%" class="greybox" style="text-align: left;">
					&nbsp;&nbsp;&nbsp;&nbsp;<s:property value="region" default="NA" />
					<s:hidden name="region" value="%{region}" />
				</td>
			</tr>
			<tr>
				<td width="25%" class="bluebox"
					style="text-align: right; font-weight: bold;"><s:text
						name="targetArea" /> :</td>
				<td width="25%" class="bluebox" style="text-align: left;">
					&nbsp;&nbsp;&nbsp;&nbsp;<s:property value="targetArea" default="NA" />
					<s:hidden name="targetArea" value="%{targetArea}" />
				</td>
				<td width="25%" class="bluebox" style="text-align: right;"></td>
				<td width="25%" class="bluebox"></td>
			</tr>
		</table>
		<br />
		<div class="subheadnew">
			<s:text name="billDetails" />
		</div>
		<br />
		<s:set value="0" var="index" />
		<s:hidden name="model.id" value="%{model.id}" />

		<table align="center" width="100%" cellspacing="0" class="tablebottom">
			<tr>
				<th class="bluebgheadtd" style='width: 2%; text-align: center;'><s:text
						name="slNo" /></th>
				<th class="bluebgheadtd" style='width: 5%; text-align: center;'><s:text
						name="consumerNo" /></th>
				<th class="bluebgheadtd" style='width: 5%; text-align: center;'><s:text
						name="accountNo" /></th>
				<th class="bluebgheadtd" style='width: 15%; text-align: center;'><s:text
						name="ward" /></th>
				<th class="bluebgheadtd" style='width: 5%; text-align: center;'><s:text
						name="prevBillAmount" /></th>
				<th class="bluebgheadtd" style='width: 5%; text-align: center;'><s:text
						name="currBillAmount" /></th>
				<th class="bluebgheadtd" style='width: 5%; text-align: center;'><s:text
						name="variance" /></th>
				<th class="bluebgheadtd" style='width: 15%; text-align: center;'><s:text
						name="remarks" /></th>
				<th class="bluebgheadtd" style='width: 10%; text-align: center;'>
					<s:text name="select" /><br /> <input type="checkbox"
					id="selectAll" onclick="toggleCheckboxes();" style="padding: 0px;">
					Select All
				</th>
			</tr>

			<s:iterator value="ebDetailsList" status="ebDetails">
				<tr>
					<s:set
						value="%{ebDetailsList[#ebDetails.index].isHighlight == true ? 'red' : 'black'}"
						var="rowColor" />
					<td class="blueborderfortd"
						style='width:2%;text-align:center;color:<s:property value="%{#rowColor}" />;'>
						<s:property value="%{#ebDetails.index + 1}" /> <s:hidden
							name="ebDetailsList[%{#ebDetails.index}].id"
							value="%{ebDetailsList[#ebDetails.index].id}" /> <s:hidden
							name="ebDetailsList[%{#ebDetails.index}].isHighlight"
							value="%{ebDetailsList[#ebDetails.index].isHighlight}" />
					</td>
					<td class="blueborderfortd"
						style='width:15%;text-align:center;color:<s:property value="%{#rowColor}" />;'>
						<s:property
							value="%{ebDetailsList[#ebDetails.index].ebConsumer.code}"
							default="NA" /> <s:hidden
							name="ebDetailsList[%{#ebDetails.index}].ebConsumer.id"
							value="%{ebDetailsList[#ebDetails.index].ebConsumer.id}" /> <s:hidden
							id='consumer%{#ebDetails.index}'
							name="ebDetailsList[%{#ebDetails.index}].ebConsumer.code"
							value="%{ebDetailsList[#ebDetails.index].ebConsumer.code}" />
					</td>
					<td class="blueborderfortd"
						style='width:15%;text-align:center;color:<s:property value="%{#rowColor}" />;'>
						<s:property
							value="%{ebDetailsList[#ebDetails.index].ebConsumer.name}"
							default="NA" /> <s:hidden id='account%{#ebDetails.index}'
							name="ebDetailsList[%{#ebDetails.index}].ebConsumer.name"
							value="%{ebDetailsList[#ebDetails.index].ebConsumer.name}" />
					</td>
					<td class="blueborderfortd"
						style='width:25%;text-align:center;color:<s:property value="%{#rowColor}" />;'>
						<s:property
							value="%{ebDetailsList[#ebDetails.index].ebConsumer.ward.name}" />
						<s:hidden
							name="ebDetailsList[%{#ebDetails.index}].ebConsumer.ward.name"
							value="%{ebDetailsList[#ebDetails.index].ebConsumer.ward.name}" />
					</td>
					<td class="blueborderfortd"
						style='width:5%;text-align:right;color:<s:property value="%{#rowColor}" />;'>
						<s:property
							value="%{ebDetailsList[#ebDetails.index].prevBillAmount}"
							default="NA" /> <s:hidden
							name="ebDetailsList[%{#ebDetails.index}].prevBillAmount"
							value="%{ebDetailsList[#ebDetails.index].prevBillAmount}" />
					</td>
					<td class="blueborderfortd"
						style='width:5%;text-align:right;color:<s:property value="%{#rowColor}" />;'>
						<s:property value="%{ebDetailsList[#ebDetails.index].billAmount}" />
						<s:hidden name="ebDetailsList[%{#ebDetails.index}].billAmount"
							value="%{ebDetailsList[#ebDetails.index].billAmount}" />
					</td>
					<td class="blueborderfortd"
						style='width:5%;text-align:right;color:<s:property value="%{#rowColor}" />;'>
						<s:if test="%{ebDetailsList[#ebDetails.index].variance == 0}">
							NA
						</s:if> <s:else>
							<s:property value="%{ebDetailsList[#ebDetails.index].variance}" />%
						</s:else> <s:hidden id='variance%{#ebDetails.index}'
							name="ebDetailsList[%{#ebDetails.index}].variance"
							value="%{ebDetailsList[#ebDetails.index].variance}" />
					</td>
					<td class="blueborderfortd"
						style='width:15%;text-align:center;color:<s:property value="%{#rowColor}" />;'>
						<s:textarea id="comments%{#ebDetails.index}"
							name="ebDetailsList[%{#ebDetails.index}].remarks" rows="1"
							cols="20" title="Comments" style="height:20px;"
							cssStyle="padding:0px;" />
					</td>
					<td class="blueborderfortd"
						style='width:10%;text-align:center;color:<s:property value="%{#rowColor}" />;'>
						<s:checkbox id="checkbox%{#ebDetails.index}"
							name="ebDetailsList[%{#ebDetails.index}].isProcess"
							value="%{#ebDetailsList[#ebDetails.index].isProcess}"
							onclick="toggleCheck(this.id);" />
					</td>
				</tr>
			</s:iterator>
		</table>

		<s:if
			test="%{mode == @org.egov.utils.FinancialConstants@STRUTS_RESULT_PAGE_NEW}">
			<div class="buttonbottom" align="center">
				<table border="0px" cellpadding="0" cellspacing="10"
					class="buttonbottom">
					<tr align="center">
						<td style="padding: 0px"><s:submit method="create"
								cssClass="buttonsubmit" value="Schedule"
								onclick="return validate();" /></td>
						<td style="padding: 0px"><input type="button" value="Close"
							onclick="javascript:window.close();" class="button" /></td>
					</tr>
				</table>
			</div>
		</s:if>
		<s:elseif
			test="%{mode == @org.egov.utils.FinancialConstants@STRUTS_RESULT_PAGE_VIEW}">
			<div class="subheadsmallnew"></div>
			<s:hidden name="nextLevel" id="nextLevel" value="%{getNextAction()}"></s:hidden>
			<s:hidden name="actionName" id="actionName"></s:hidden>
			<div>
				<s:if test="%{getNextAction() == 'END'}">
					<div>
						<table align="center" width="100%" cellspacing="0">
							<tr>
								<td width="25%" class="greybox" style="text-align: center;"
									colspan="2"><span style="font-weight: bold;">Date :
								</span> <s:date format="dd/MM/yyyy" name="%{todaysDate}" /></td>
								<td width="25%" class="greybox" style="text-align: center;">
									<span style="font-weight: bold;"><s:text
											name="expenseCode" /> :</span> <s:property
										value="%{@org.egov.eb.utils.EBConstants@BILL_DEBIT_GLCODE_ELECTRICITY}" />
								</td>
								<td width="25%" class="greybox" style="text-align: center;">
									<span style="font-weight: bold;"><s:text
											name="netPayableCode" /> : </span> <s:property
										value="%{@org.egov.eb.utils.EBConstants@BILL_CREDIT_GLCODE}" />
								</td>
							</tr>
						</table>
					</div>
				</s:if>
				<s:if test="%{nextLevel!='END'}">
					<%@ include file="/WEB-INF/jsp/payment/commonWorkflowMatrix.jsp"%>
				</s:if>
			</div>
			<div class="buttonbottom" align="center">

				<table id="ebDetailTable" align="center" border="0" cellpadding="0"
					cellspacing="0" width="100%">
					<tr>
						<s:iterator value="%{getValidActions()}" var="name">
							<s:if test="%{name!=''}">
								<s:submit type="submit" cssClass="buttonsubmit" value="%{name}"
									id="%{name}" name="%{name}" method="save"
									onclick="return populateActionName('%{name}');" />
							</s:if>
						</s:iterator>
						<input type="button" value="Close"
							onclick="javascript:window.close();" class="button" />
					</tr>

					</div>
					</s:elseif>
					</s:form>
					<script type="text/javascript">
		
		var size = parseInt('<s:property value="%{ebDetailsList.size}" />');
	
		function validateCommentsField() {
			var variance;
			var comments;
			var isProcess;
			for (var i = 0; i < size; i++) {
				variance = jQuery('#variance'+i).val();
				isProcess = jQuery('#checkbox'+i).val();
				if (isProcess == 'true' && (variance != 0 && (variance < -20 || variance > 20))) {
					comments = jQuery('#comments'+i).val();
					if (comments.trim().length == 0) {
						bootbox.alert("Please enter Remarks for Consumer: " + jQuery('#consumer'+i).val() + " and for Account: " + jQuery('#account'+i).val());
						return false;
					}
				}
			}
			return true;
		}
		
		function validateCommentsForReject() {
			var comments;
			for (var i = 0; i < size; i++) {
				comments = jQuery('#comments'+i).val();
				if (comments.trim().length == 0 && document.getElementById("checkbox"+i+"").checked) {
					bootbox.alert("Please enter Remarks for Consumer: " + jQuery('#consumer'+i).val() + " and for Account: " + jQuery('#account'+i).val());
					return false;
				}
			}
			return true;
		}
		function populateApprover() {
			getUsersByDesignationAndDept();
		}
		function populateActionName(name) {
			document.getElementById('actionName').value = name;
		}
		function loadDesignationFromMatrix() {
			var e = dom.get('approverDepartment');
			var dept = e.options[e.selectedIndex].text;
	  		var currentState = dom.get('currentState').value;
	  		var amountRule="";
			var pendingAction=document.getElementById('pendingActions').value;
			loadDesignationByDeptAndType('EBDetails',dept,currentState,amountRule,"",pendingAction); 
		}
		function populateActionName(name) {
			document.getElementById('actionName').value = name;
			<s:if test="%{getNextAction()!='END'}">
			var value=document.getElementById("approverDepartment").value;
			
			if((name=="Forward" || name=="forward") && value=="-1")
			{
				bootbox.alert("Please select the Approver Department");
				document.getElementById("approverDepartment").focus();
				return false;
			}
			var value=document.getElementById("approverDesignation").value;
			if((name=="Forward" || name=="forward") && value=="-1")
			{
				bootbox.alert("Please select the approver designation");
				document.getElementById("approverDesignation").focus();
				return false;
			}
		    if((name=="Forward" || name=="forward") && document.getElementById('approverPositionId').value=="-1")
		    {
		    	bootbox.alert("Please Select the Approver");
				document.getElementById("approverPositionId").focus();
				return false;
		    }
	    </s:if>
	    var size = parseInt('<s:property value="%{ebDetailsList.size}" />');
	    var anyRowSelection = "N"; 
	    
	    if(size > 0){		
	  		for(var i = 0 ; i < size ; i++){
	  			if(document.getElementById("checkbox"+i+"").checked){			
	  				anyRowSelection = "Y";
	  				break;
	  			}	  			
	  		}
	  	}  
	  	if(anyRowSelection == "N"){
	  		bootbox.alert("Select at least one row to approve/forward/reject");
	  		return false;
	  	}
	  	
	  	if((name=="Forward" || name=="forward")){
			return validateCommentsField();
			}
		if((name=="Reject" || name=="Cancel" || name=="cancel" || name=="reject")){
			return validateCommentsForReject();
			}
	  
		}
		
	</script>
</body>
</html>
