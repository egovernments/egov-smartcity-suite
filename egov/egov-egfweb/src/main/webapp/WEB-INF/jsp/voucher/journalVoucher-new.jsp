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
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/calendar.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/dateValidation.js?rnd=${app_release_no}"></script>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<script type="text/javascript"
	src="/EGF/resources/javascript/autocomplete-debug.js"></script>
<title>Journal voucher Create</title>
</head>

<body
	onload="loadDropDownCodes();loadDropDownCodesFunction();onloadtask()">

	<s:form action="journalVoucher" theme="simple" name="jvcreateform">
		<s:token />
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="Journal voucher Create" />
		</jsp:include>

		<span class="mandatory1"> <font
			style='color: red; font-weight: bold'> <s:actionerror /> <s:fielderror />
				<s:actionmessage /></font>
		</span>
		<div class="formmainbox">
			<div class="subheadnew">Journal Voucher</div>
			<div id="listid" style="display: block">
				<div align="center">
					<font style='color: red; font-weight: bold'>
						<p class="error-block" id="lblError"></p>
					</font>

					<table border="0" width="100%">
						<tr>
							<td style="width: 5%"></td>
							<s:if test="%{shouldShowHeaderField('vouchernumber')}">
								<td class="greybox"><s:text name="voucher.number" /><span
									class="mandatory1">*</span></td>
								<td class="greybox"><s:textfield name="voucherNumber"
										id="voucherNumber" maxlength="30" /></td>
							</s:if>
							<td class="greybox"><s:text name="voucher.date" /><span
								class="mandatory1">*</span></td>

							<td class="bluebox"><s:date name="voucherDate"
									var="voucherDateId" format="dd/MM/yyyy" /> <s:textfield
									id="voucherDate" name="voucherDate" value="%{voucherDateId}"
									data-date-end-date="0d"
									onkeyup="DateFormat(this,this.value,event,false,'3')"
									placeholder="DD/MM/YYYY" cssClass="form-control datepicker"
									data-inputmask="'mask': 'd/m/y'" /></td>
							<s:else>
								<td class="greybox">&nbsp;</td>
								<td class="greybox">&nbsp;</td>
							</s:else>
						</tr>
						<jsp:include page="loadYIDataTable.jsp" />

						<jsp:include page="voucherSubType.jsp" />
						<jsp:include page="vouchertrans-filter.jsp" />


						<tr>
							<td style="width: 5%"></td>
							<td class="greybox"><s:text name="voucher.narration" /></td>
							<td class="greybox" colspan="3"><s:textarea id="narration"
									name="description" cols="100" rows="3"
									onblur="checkVoucherNarrationLen(this)" /></td>
						</tr>
						</tr>
					</table>
				</div>
				<br />
				<div id="labelAD" align="center">
					<table width="80%" border=0 id="labelid">
						<th>Account Details</th>
					</table>
				</div>
				<div class="yui-skin-sam" align="center">
					<div id="billDetailTable"></div>
				</div>
				<script>
		
		makeVoucherDetailTable();
		document.getElementById('billDetailTable').getElementsByTagName('table')[0].width="80%"
	 </script>
				<br />
				<div id="labelSL" align="center">
					<table width="80%" border=0 id="labelid">
						<th>Sub-Ledger Details</th>
					</table>
				</div>

				<div class="yui-skin-sam" align="center">
					<div id="subLedgerTable"></div>
				</div>
				<script>
			
			makeSubLedgerTable();
			
			document.getElementById('subLedgerTable').getElementsByTagName('table')[0].width="80%"
		</script>
				<br />
				<div class="subheadsmallnew" /></div>
				<s:hidden id="voucherDate" name="voucherDate" />
				<s:hidden id="cutOffDate" name="cutOffDate" />
				<%@ include file='../workflow/commonWorkflowMatrix.jsp'%>
				<%@ include file='../workflow/commonWorkflowMatrix-button.jsp'%>
				<br />
			</div>
		</div>

		<div id="codescontainer"></div>
		<s:hidden name="actionName" id="actionName" />
		<input type="hidden" id="voucherTypeBean.voucherName"
			name="voucherTypeBean.voucherName"
			value="${voucherTypeBean.voucherName}" />
		<input type="hidden" id="voucherTypeBean.voucherType"
			name="voucherTypeBean.voucherType" value="Journal Voucher" />
		<input type="hidden" id="voucherTypeBean.voucherNumType"
			name="voucherTypeBean.voucherNumType"
			value="${voucherTypeBean.voucherNumType}" />
		<input type="hidden" id="voucherTypeBean.cgnType"
			name="voucherTypeBean.cgnType" value="JVG" />
		<input type="hidden" id="buttonValue" name="buttonValue" />

	</s:form>

	<s:hidden name="targetvalue" value="%{target}" id="targetvalue" />
	<s:hidden name="functionValue" id="functionValue" />
	<s:hidden name="functionId" id="functionId" />
	<script type="text/javascript">
		
			if(dom.get('targetvalue').value=='success')
			{
				document.getElementById('voucherDate').value=""; 
				if(document.getElementById('voucherNumber')){
					document.getElementById('voucherNumber').value="";
				}
				document.getElementById('narration').value="";
				if(document.getElementById('fundId')){
					document.getElementById('fundId').value=-1;
				}
				if(document.getElementById('vouchermis.function')){   
					document.getElementById('vouchermis.function').value=-1;
				}
				if(document.getElementById('vouchermis.departmentid')){
					document.getElementById('vouchermis.departmentid').value=-1;
				}   
				if(document.getElementById('schemeid')){
					document.getElementById('schemeid').value=-1;
				}
				if(document.getElementById('subschemeid')){
					document.getElementById('subschemeid').value=-1;
				}
				if(document.getElementById('vouchermis.functionary')){
					document.getElementById('vouchermis.functionary').value=-1;
				}
				if(document.getElementById('fundsourceId')){
					document.getElementById('fundsourceId').value=-1;
				}
				if(document.getElementById('vouchermis.divisionid')){
					document.getElementById('vouchermis.divisionid').value=-1;
				}
			}	
			function validateApproverUser(name,value){
				//bootbox.alert("action name"+name);     
				document.getElementById("actionName").value= name;
				//bootbox.alert("button value"+value);  
				<s:if test='%{! wfitemstate.equalsIgnoreCase("END")}'>
					if(!validateUser(name,value)){ return false; }
				</s:if>
				return true;
			}
	function onSubmit()
	{
		if(validateJV()){
			document.jvcreateform.action='/EGF/voucher/journalVoucher-create.action';
	    	return true;
				
		}else{
			return false;
		}
	}

	function validateCutOff()
	{
	var cutOffDatePart=document.getElementById("cutOffDate").value.split("/");
    var voucherDatePart=document.getElementById("voucherDate").value.split("/");
    var cutOffDate = new Date(cutOffDatePart[1] + "/" + cutOffDatePart[0] + "/"
			+ cutOffDatePart[2]);
    var voucherDate = new Date(voucherDatePart[1] + "/" + voucherDatePart[0] + "/"
			+ voucherDatePart[2]);
	if(voucherDate<=cutOffDate)
	{
		return true;
	}
	else{
		var msg1='<s:text name="wf.vouchercutoffdate.message"/>';
		var msg2='<s:text name="wf.cutoffdate.msg"/>';
		bootbox.alert(msg1+" "+document.getElementById("cutOffDate").value+" "+msg2);
			return false;
		}
	}
	
	function validateJV()
	{
	   //document.getElementById("buttonValue").value=btnval;
		document.getElementById('lblError').innerHTML ="";
		var cDate = new Date();
		
		var currDate = cDate.getDate()+"/"+(parseInt(cDate.getMonth())+1)+"/"+cDate.getYear();
		var vhDate=document.getElementById('voucherDate').value;
		var vhType=document.getElementById('vType').value;

		console.log(vhType);
		
		if(vhType =='-1' )	{
			document.getElementById('lblError').innerHTML = "Please select voucher sub type ";
			document.getElementById('voucherDate').focus();
			return false;
		}
		if(vhDate == '' )	{
			document.getElementById('lblError').innerHTML = "Please enter the Voucher date ";
			document.getElementById('voucherDate').focus();
			return false;
		}

		var voucherdate = vhDate.substring(0, 2);
	    var vouchermonth = vhDate.substring(3, 5);
	    var voucheryear = vhDate.substring(6, 10);
	    var voucherDate = new Date(voucheryear, vouchermonth - 1, voucherdate);
	    var today = new Date();
	    if (voucherDate > today) {
	        bootbox.alert("Voucher date is greater than today's date ");
	        return false
	    }
		var vVoucherSubType = document.getElementById('vType').value;
		if(vVoucherSubType != 'JVGeneral' && vVoucherSubType != '-1' )	{
			if(document.getElementById('voucherTypeBean.partyName').value == '' ) {
				document.getElementById('lblError').innerHTML = "Please enter a Party Name ";
				document.getElementById('voucherTypeBean.partyName').focus();
				return false;
			}
		}
		var billDate = document.getElementById("billDate").value;
	    var date = billDate.substring(0, 2);
	    var month = billDate.substring(3, 5);
	    var year = billDate.substring(6, 10);
	    var myBillDate = new Date(year, month - 1, date);

	    if (myBillDate > today) {
	        bootbox.alert("Bill date is greater than today's date ");
	        return false
	    }
	    var partyBillDate = document.getElementById("partyBillDate").value;
	    var partydate = partyBillDate.substring(0, 2);
	    var partymonth = partyBillDate.substring(3, 5);
	    var partyyear = partyBillDate.substring(6, 10);
	    var myPartyBillDate = new Date(partyyear, partymonth - 1, partydate);

	    if (myPartyBillDate > today) {
	        bootbox.alert("Party bill date is greater than today's date ");
	        return false
	    }
		
	// Javascript validation of the MIS Manadate attributes.
		<s:if test="%{isFieldMandatory('vouchernumber')}"> 
			 if(null != document.getElementById('voucherNumber') && document.getElementById('voucherNumber').value.trim().length == 0 ){

				document.getElementById('lblError').innerHTML = "Please enter the Voucher number";
				return false;
			 }
		 </s:if>
		 <s:if test="%{isFieldMandatory('voucherdate')}"> 
				 if(null != document.getElementById('voucherDate') && document.getElementById('voucherDate').value.trim().length == 0){

					document.getElementById('lblError').innerHTML = "Please enter the Voucher date";
					return false;
				 }
			 </s:if>    
		 	<s:if test="%{isFieldMandatory('fund')}"> 
				 if(null != document.getElementById('fundId') && document.getElementById('fundId').value == -1){
					document.getElementById('lblError').innerHTML = "Please select a Fund";
					return false;
				 }    
			 </s:if>   
			 <s:if test="%{isFieldMandatory('function')}">                        
			 if(null != document.getElementById('vouchermis.function') && document.getElementById('vouchermis.function').value == -1){

				document.getElementById('lblError').innerHTML = "Please select a Function";
				return false;
			 }
		 </s:if>
			<s:if test="%{isFieldMandatory('department')}"> 
				 if(null!= document.getElementById('vouchermis.departmentid') && document.getElementById('vouchermis.departmentid').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a Department";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('scheme')}"> 
				 if(null!=document.getElementById('schemeid') &&  document.getElementById('schemeid').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a Scheme";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('subscheme')}"> 
				 if(null!= document.getElementById('subschemeid') && document.getElementById('subschemeid').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a Subscheme";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('functionary')}"> 
				 if(null!=document.getElementById('vouchermis.functionary') &&  document.getElementById('vouchermis.functionary').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a Functionary";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('fundsource')}"> 
				 if(null !=document.getElementById('fundsourceId') &&  document.getElementById('fundsourceId').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a Fundsource";
					return false;
				}
			</s:if>
			<s:if test="%{isFieldMandatory('field')}"> 
				 if(null!= document.getElementById('vouchermis.divisionid') && document.getElementById('vouchermis.divisionid').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a Field";
					return false;
				 }
			</s:if>

			if(null!= document.getElementById('vouchermis.divisionid') && document.getElementById('vouchermis.divisionid').value == -1){

				document.getElementById('lblError').innerHTML = "Please select a Field";
				return false;
			 }	
			
			//result =validateApproverUser(name,value);
			
	return true;
}function loadBank(fund){
	}
function onloadtask(){
//autocompleteEntities1By20();

	var currentTime = new Date()
	var month = currentTime.getMonth() + 1
	var day = currentTime.getDate()
	var year = currentTime.getFullYear()
	if(document.getElementById('voucherDate').value  =="")  
		document.getElementById('voucherDate').value = day + "/" + month + "/" + year ;
	var VTypeFromBean = '<s:property value="voucherTypeBean.voucherSubType"/>';
	if(VTypeFromBean == "") 
		VTypeFromBean = '-1';
	document.getElementById('vType').value = VTypeFromBean;
	if('<s:property value="voucherTypeBean.voucherSubType"/>' == 'JVGeneral' || '<s:property value="voucherTypeBean.voucherSubType"/>'== ""){
		document.getElementById('voucherTypeBean.partyBillNum').readOnly=true;
		document.getElementById('voucherTypeBean.partyName').readOnly=true;
		document.getElementById('partyBillDate').readOnly=true;
		document.getElementById('voucherTypeBean.billNum').readOnly=true;
		document.getElementById('billDate').readOnly=true;
	}
	//document.getElementById('vouchermis.function').style.display="none";
	//document.getElementById('functionnametext').style.display="none";
	var message = '<s:property value="message"/>';
	if(message != null && message != '')
		showMessage(message);
	<s:if test="%{voucherTypeBean.voucherNumType == null}">
		document.getElementById('voucherTypeBean.voucherNumType').value ="Journal";
	</s:if>
	<s:if test="%{voucherTypeBean.voucherName == null}">
		document.getElementById('voucherTypeBean.voucherName').value ="JVGeneral";
	</s:if>
	<s:if test="%{voucherTypeBean.voucherSubType == null}">
		document.getElementById('voucherTypeBean.voucherSubType').value = "JVGeneral";
	</s:if>
	if(message == null || message == '')
		populateslDropDown(); // to load the subledger detils when page loads, required when validation fails.
	if(document.getElementById('approverDepartment'))
		document.getElementById('approverDepartment').value = "-1";
  }
function showMessage(message){
	var buttonValue = '<s:property value="buttonValue"/>';
	for(var i=0;i<document.forms[0].length;i++)
	{
		if( document.forms[0].elements[i].id!='Close')
		document.forms[0].elements[i].disabled =true;
	} 
	//bootbox.alert(message);
	bootbox.alert(message, function() {
		var voucherHeaderId = '<s:property value="voucherHeader.id"/>';
		document.forms[0].action = "/EGF/voucher/preApprovedVoucher-loadvoucherview.action?vhid="+voucherHeaderId;
		document.forms[0].submit(); 
	});
	     
	
}

function printJV()
{		
		var voucherHeaderId = '<s:property value="voucherHeader.id"/>';
		window.location="${pageContext.request.contextPath}/voucher/journalVoucherPrint-print.action?id="+voucherHeaderId;		
		//document.forms[0].submit();
}

</script>
</body>

</html>
