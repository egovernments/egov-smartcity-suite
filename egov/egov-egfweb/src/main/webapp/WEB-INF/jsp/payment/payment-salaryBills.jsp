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
<link href="/EGF/resources/css/budget.css?rnd=${app_release_no}"
	rel="stylesheet" type="text/css" />
<link href="/EGF/resources/css/commonegovnew.css?rnd=${app_release_no}"
	rel="stylesheet" type="text/css" />
<link rel="stylesheet"
	href="/EGF/resources/css/tabber.css?rnd=${app_release_no}"
	TYPE="text/css">
<script type="text/javascript"
	src="/EGF/resources/javascript/tabber.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/tabber2.js?rnd=${app_release_no}"></script>
</head>
<script>

function validateTab(indexx)
{
	if(indexx==0)
	{
		document.getElementById('buttondiv').style.display='none';
		document.getElementById('paginationdiv').style.display='none';
	}
	else
	{
		document.getElementById('buttondiv').style.display='block';
		document.getElementById('paginationdiv').style.display='block';
	}
	return true;
}

var temp = window.setInterval(load,1);
function load()
{
	try{document.getElementById('tabber1').onclick(); window.clearInterval(temp);}catch(e){}
}

function checkMiscAttributes(obj)
{
	if(obj.checked)
	{
		/*var id = obj.id.substring(10,obj.id.length);
		var mis = '';
		if(document.getElementById('fundName'+id))
			mis = document.getElementById('fundName'+id).value;
		if(document.getElementById('deptName'+id))
			mis = mis+'#'+document.getElementById('deptName'+id).value;
		if(document.getElementById('functionaryName'+id))
			mis = mis+'#'+document.getElementById('functionaryName'+id).value;
		if(document.getElementById('fundsourceName'+id))
			mis = mis+'#'+document.getElementById('fundsourceName'+id).value;
		if(document.getElementById('schemeName'+id))
			mis = mis+'#'+document.getElementById('schemeName'+id).value;
		if(document.getElementById('subschemeName'+id))
			mis = mis+'#'+document.getElementById('subschemeName'+id).value;
		if(document.getElementById('fieldName'+id))
			mis = mis+'#'+document.getElementById('fieldName'+id).value;
		*/	
		var id = obj.id.substring(10,obj.id.length);
		var prefix = obj.name.substring(0, obj.name.indexOf("["));
		var fundName = prefix+"["+id+"].fundName";
		var deptName = prefix+"["+id+"].deptName";
		var functionaryName = prefix+"["+id+"].functionaryName";
		var functionName = prefix+"["+id+"].functionName";
		var fundsourceName = prefix+"["+id+"].fundsourceName";
		var schemeName = prefix+"["+id+"].schemeName";
		var subschemeName = prefix+"["+id+"].subschemeName";
		var fieldName = prefix+"["+id+"].fieldName";
		var schemeName = prefix+"["+id+"].schemeName";
		var mis = '';
		if(document.getElementsByName(fundName) && document.getElementsByName(fundName).item(0) != null )
			mis = ( document.getElementsByName(fundName).item(0)).value;
		if(document.getElementsByName(deptName) && document.getElementsByName(deptName).item(0) != null )
			mis = mis+'#'+( document.getElementsByName(deptName).item(0)).value;
		if(document.getElementsByName(functionName) && document.getElementsByName(functionName).item(0) != null )
			mis = ( document.getElementsByName(functionName).item(0)).value;
		if(document.getElementsByName(functionaryName) && document.getElementsByName(functionaryName).item(0) != null)
			mis = mis+'#'+( document.getElementsByName(functionaryName).item(0)).value;	
		if(document.getElementsByName(fundsourceName) && document.getElementsByName(fundsourceName).item(0) != null)
			mis = mis+'#'+( document.getElementsByName(fundsourceName).item(0)).value;	
		if(document.getElementsByName(schemeName) && document.getElementsByName(schemeName).item(0) != null)
			mis = mis+'#'+( document.getElementsByName(schemeName).item(0)).value;
		if(document.getElementsByName(subschemeName) && document.getElementsByName(subschemeName).item(0) != null)
			mis = mis+'#'+( document.getElementsByName(subschemeName).item(0)).value;
		if(document.getElementsByName(fieldName) && document.getElementsByName(fieldName).item(0) != null)
			mis = mis+'#'+( document.getElementsByName(fieldName).item(0)).value;

		if(document.getElementById('miscattributes').value=='')
			document.getElementById('miscattributes').value = mis;
		
		if(mis!=document.getElementById('miscattributes').value)
		{
			bootbox.alert('Selected bills do not have same attributes. Please select bills with same attributes');
			obj.checked =false;
			return;
			
		}
		document.getElementById('miscount').value=parseInt(document.getElementById('miscount').value)+1;		
	}
	else
	{
		document.getElementById('miscount').value=parseInt(document.getElementById('miscount').value)-1;
		if(document.getElementById('miscount').value==0)
			document.getElementById('miscattributes').value='';
	}
}
function check()
{
	if(document.getElementById('miscount').value==0)
	{
		bootbox.alert('Please select a bill before making the payment');
		return false;
	}
	if(document.getElementById('vouchermis.departmentid'))
		document.getElementById('vouchermis.departmentid').disabled=false;
	return true;
}
function loadBank(obj){}
function search()
{
	if(document.getElementById('vouchermis.departmentid'))
		document.getElementById('vouchermis.departmentid').disabled=false;
	return true;
}

function selectAllSalary(element){
	var length = 0;
	<s:if test="%{salaryList!=null}">
		length = <s:property value="%{salaryList.size()}"/>;
	</s:if>
	if(element.checked == true)
		checkAll('salaryList',length);
	else
		uncheckAll('salaryList',length);
}

function checkAll(field,length){
	for (i = 0; i < length; i++){
		document.getElementsByName(field+'['+i+'].isSelected')[0].checked = true;
		document.getElementById('miscount').value=parseInt(document.getElementById('miscount').value)+1;
	}
}

function uncheckAll(field,length){
	for (i = 0; i < length; i++){
		document.getElementsByName(field+'['+i+'].isSelected')[0].checked = false;
		document.getElementById('miscount').value=parseInt(document.getElementById('miscount').value)-1;
	}
}
function selectAllSalary(element){
	var length = 0;
	<s:if test="%{salaryList!=null}">
		length = <s:property value="%{salaryList.size()}"/>;
	</s:if>
	
	if(element.checked == true)	{
		var salcnt=checksalaryForSameMisAttribs('salaryList',length);
		if(salcnt!=0){
		 bootbox.alert("Selected Bills doesnot have same attributes");
		  document.getElementById('salSelectAll').checked =false; }
		else
		checkAll('salaryList',length);
		
	}
	else
		uncheckAll('salaryList',length);
}

function checksalaryForSameMisAttribs(obj,len)
{
		var fund1=document.getElementsByName(obj+"[0].fundName");
		var dept1=document.getElementsByName(obj+"[0].deptName");
		var function1=document.getElementsByName(obj+"[0].functionName");
		var scheme1=document.getElementsByName(obj+"[0].schemeName");
		var subscheme1=document.getElementsByName(obj+"[0].subschemeName");
		var fundsource1=document.getElementsByName(obj+"[0].fundsourceName");
		var field1=document.getElementsByName(obj+"[0].fieldName");
		var functionaryName1=document.getElementsByName(obj+"[0].functionaryName");
		var salcount=0;
		for(i=0;i<len;i++)
		{
			 <s:if test="%{!isFieldMandatory('fund')}">
		   if((document.getElementsByName(obj+"["+i+"].fundName").item(0)).value!=null){
		   if(fund1[0].value != null && fund1[0].value !=(document.getElementsByName(obj+"["+i+"].fundName").item(0)).value) {
		   	document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false;
		  	salcount++; break;}}
		  	</s:if>
		   
		   <s:if test="%{shouldShowHeaderField('department')}"> 
		   if((document.getElementsByName(obj+"["+i+"].deptName").item(0)).value!=null){  	
		   if(dept1[0].value != null &&  dept1[0].value !=(document.getElementsByName(obj+"["+i+"].deptName").item(0)).value) {
		   document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false;
		   salcount++; break;}}
		   </s:if>

		   <s:if test="%{shouldShowHeaderField('function')}"> 
		   if((document.getElementsByName(obj+"["+i+"].functionName").item(0)).value!=null){  	
		   if(function1[0].value != null &&  function1[0].value !=(document.getElementsByName(obj+"["+i+"].functionName").item(0)).value) {
		   document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false;
		   concount++; break;}}
		   </s:if>
		    
		     <s:if test="%{shouldShowHeaderField('functionary')}">
		    if(document.getElementsByName(obj+"["+i+"].functionaryName")!=null){
		    if(functionaryName1[0].value != null && functionaryName1[0].value != (document.getElementsByName(obj+"["+i+"].functionaryName").item(0)).value) {
		    document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false; 
		    salcount++; break;}}
		    </s:if>
		   
		   <s:if test="%{shouldShowHeaderField('fundsource')}"> 
		   if((document.getElementsByName(obj+"["+i+"].fundsourceName").item(0)).value!=null){
		   if(fundsource1[0].value != null &&  fundsource1[0].value !=(document.getElementsByName(obj+"["+i+"].fundsourceName").item(0)).value) {
		   document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false; 
		   salcount++; break;}}
		   </s:if>		   
		   
		   <s:if test="%{shouldShowHeaderField('scheme')}">
		   if((document.getElementsByName(obj+"["+i+"].schemeName").item(0)).value!=null){
		   if(scheme1[0].value != null  && scheme1[0].value !=( document.getElementsByName(obj+"["+i+"].schemeName").item(0)).value) {
		    document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false;
		   salcount++; break;}}
		   </s:if>
		 
		   <s:if test="%{shouldShowHeaderField('subscheme')}">
		   if((document.getElementsByName(obj+"["+i+"].subschemeName").item(0)).value!=null){
		   if(subscheme1[0].value !=  null && subscheme1[0].value!=(document.getElementsByName(obj+"["+i+"].subschemeName").item(0)).value) {
		   document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false;  
		   salcount++; break;}}
		   </s:if>		 
		   
		    <s:if test="%{shouldShowHeaderField('field')}">
		    if(document.getElementsByName(obj+"["+i+"].fieldName")!=null){
		    if(field1[0].value != null && field1[0].value != (document.getElementsByName(obj+"["+i+"].fieldName").item(0)).value) {
		    document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false; 
		    salcount++; break;}}
		    </s:if>	  		  
		   }
		   return salcount;
}
</script>

</head>
<body>
	<s:form action="payment" theme="simple">
		<s:token />
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="Salary Bill Payment Search" />
		</jsp:include>
		<span class="mandatory"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
		</span>

		<div class="formmainbox">
			<div class="subheadnew">Salary Bill Payment</div>
			<div id="budgetSearchGrid"
				style="display: block; width: 100%; border-top: 1px solid #ccc;">
				<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td>
							<div align="left">
								<br />
								<table border="0" cellspacing="0" cellpadding="0" width="100%">
									<tr>
										<td>
											<div class="tabber">
												<div class="tabbertab" id="searchtab">
													<h2>Search Bill</h2>
													<span>
														<table width="100%" border="0" cellspacing="0"
															cellpadding="0">
															<tr>
																<td align="center" colspan="6" class="serachbillhead">Salary
																	Bill Payment Search</td>
															</tr>
															<tr>
																<td class="bluebox" width="30%"><s:text
																		name="payment.billnumber" /></td>
																<td class="bluebox"><s:textfield name="billNumber"
																		id="billNumber" maxlength="25" value="%{billNumber}" /></td>
															</tr>
															<tr>
																<td class="greybox" width="30%"><s:text
																		name="payment.billdatefrom" /></td>
																<td class="greybox"><s:textfield id="fromDate"
																		name="fromDate" value="%{fromDate}"
																		data-date-end-date="0d"
																		onkeyup="DateFormat(this,this.value,event,false,'3')"
																		placeholder="DD/MM/YYYY"
																		class="form-control datepicker"
																		data-inputmask="'mask': 'd/m/y'" /></td>
																<td class="greybox" width="30%"><s:text
																		name="payment.billdateto" /></td>
																<td class="greybox"><s:textfield id="toDate"
																		name="toDate" value="%{toDate}"
																		data-date-end-date="0d"
																		onkeyup="DateFormat(this,this.value,event,false,'3')"
																		placeholder="DD/MM/YYYY"
																		class="form-control datepicker"
																		data-inputmask="'mask': 'd/m/y'" /></td>
															</tr>
															<tr>
																<td class="bluebox" width="30%"><s:text
																		name="payment.expendituretype" /></td>
																<td class="bluebox"><s:select name="expType"
																		id="expType" list="#{'Salary':'Salary'}"
																		value="%{expType}" /></td>
															</tr>
															<jsp:include page="../voucher/vouchertrans-filter.jsp" />
															<tr>
																<td align="center" colspan="6">
																	<div class="buttonbottom">
																		<s:submit method="search" value="Search"
																			cssClass="buttonsubmit" onclick="return search()" />
																		<input type="button" value="Close"
																			onclick="javascript:window.close()" class="button" />
																		<s:hidden name="miscount" id="miscount" />
																		<input type="hidden" name="miscattributes"
																			id="miscattributes" value="" />
																	</div>
																</td>
															</tr>
														</table>
													</span>
												</div>
												<div class="tabbertab" id="suppliertab">
													<h2>Salary Bill</h2>
													<span>
														<table align="center" border="0" cellpadding="0"
															cellspacing="0" class="newtable">
															<tr>
																<td colspan="6"><div class="subheadsmallnew">Salary
																		Bill Payment Search</div></td>
															</tr>
															<tr>
																<td colspan="6">
																	<div style="float: left; width: 100%;">
																		<table align="center" border="0" cellpadding="0"
																			cellspacing="0" width="100%">
																			<tr>
																				<th class="bluebgheadtdnew">Select<input
																					type="checkbox" name="salSelectAll"
																					id="salSelectAll" onclick="selectAllSalary(this)" />
																					</checkbox></th>
																				<jsp:include page="billdetails-header.jsp" />
																				<s:iterator var="p" value="salaryList" status="s">
																					<tr>
																						<td class="blueborderfortdnew"><s:hidden
																								name="salaryList[%{#s.index}].csBillId"
																								id="csBillId%{#s.index}" value="%{csBillId}" />
																							<s:checkbox
																								name="salaryList[%{#s.index}].isSelected"
																								id="isSelected%{#s.index}"
																								onclick="checkMiscAttributes(this)"></s:checkbox></td>
																						<td class="blueborderfortdnew"><s:property
																								value="#s.index+1" /></td>
																						<td align="left" class="blueborderfortdnew"><s:hidden
																								name="salaryList[%{#s.index}].expType"
																								id="expType%{#s.index}" value="%{expType}" /> <s:hidden
																								name="salaryList[%{#s.index}].billNumber"
																								id="billNumber%{#s.index}" value="%{billNumber}" />
																							<s:property value="%{billNumber}" /></td>
																						<td class="blueborderfortdnew"><s:hidden
																								name="salaryList[%{#s.index}].billDate"
																								id="billDate%{#s.index}" value="%{billDate}" />
																							<s:date name="%{billDate}" format="dd/MM/yyyy" /></td>

																						<td align="left" class="blueborderfortdnew"><s:hidden
																								name="salaryList[%{#s.index}].billVoucherNumber"
																								id="billVoucherNumber%{#s.index}"
																								value="%{billVoucherNumber}" /> <s:property
																								value="%{billVoucherNumber}" /></td>
																						<td style="text-align: left"
																							class="blueborderfortdnew"><s:hidden
																								name="salaryList[%{#s.index}].billVoucherDate"
																								id="billVoucherDate%{#s.index}"
																								value="%{billVoucherDate}" /> <s:date
																								name="%{billVoucherDate}" format="dd/MM/yyyy" /></td>

																						<td align="left" class="blueborderfortdnew"><s:hidden
																								name="salaryList[%{#s.index}].payTo"
																								id="payTo%{#s.index}" value="%{payTo}" /> <s:property
																								value="%{payTo}" /></td>
																						<td style="text-align: right"
																							class="blueborderfortdnew"><s:hidden
																								name="salaryList[%{#s.index}].netAmt"
																								id="netAmt%{#s.index}" value="%{netAmt}" /> <s:text
																								name="payment.format.number">
																								<s:param value="%{netAmt}" />
																							</s:text></td>
																						<td style="text-align: right"
																							class="blueborderfortdnew"><s:hidden
																								name="salaryList[%{#s.index}].earlierPaymentAmt"
																								id="earlierPaymentAmt%{#s.index}"
																								value="%{earlierPaymentAmt}" /> <s:text
																								name="payment.format.number">
																								<s:param value="%{earlierPaymentAmt}" />
																							</s:text></td>
																						<td style="text-align: right"
																							class="blueborderfortdnew"><s:hidden
																								name="salaryList[%{#s.index}].payableAmt"
																								id="payableAmt%{#s.index}" value="%{payableAmt}" />
																							<s:hidden
																								name="salaryList[%{#s.index}].paymentAmt"
																								id="paymentAmt%{#s.index}" value="%{paymentAmt}" />
																							<s:text name="payment.format.number">
																								<s:param value="%{payableAmt}" />
																							</s:text></td>
																						<s:if test="%{!isFieldMandatory('fund')}">
																							<td class="blueborderfortdnew"
																								id="fund<s:property value="#s.index"/>"><s:hidden
																									name="salaryList[%{#s.index}].fundName"
																									id="fundName%{#s.index}" value="%{fundName}" />
																								<s:property value="%{fundName}" /></td>
																						</s:if>
																						<s:if
																							test="%{shouldShowHeaderField('department')}">
																							<td class="blueborderfortdnew"
																								id="dept<s:property value="#s.index"/>"><s:hidden
																									name="salaryList[%{#s.index}].deptName"
																									id="deptName%{#s.index}" value="%{deptName}" />
																								<s:property value="%{deptName}" /></td>
																						</s:if>
																						<s:if test="%{shouldShowHeaderField('function')}">
																							<td class="blueborderfortdnew"
																								id="function<s:property value="#s.index"/>"><s:hidden
																									name="salaryList[%{#s.index}].functionName"
																									id="functionName%{#s.index}"
																									value="%{functionName}" /> <s:property
																									value="%{functionName}" /></td>
																						</s:if>
																						<s:if
																							test="%{shouldShowHeaderField('functionary')}">
																							<td class="blueborderfortdnew"
																								id="functionary<s:property value="#s.index"/>"><s:hidden
																									name="salaryList[%{#s.index}].functionaryName"
																									id="functionaryName%{#s.index}"
																									value="%{functionaryName}" /> <s:property
																									value="%{functionaryName}" /></td>
																						</s:if>
																						<s:if
																							test="%{shouldShowHeaderField('fundsource')}">
																							<td class="blueborderfortdnew"
																								id="fundsource<s:property value="#s.index"/>"><s:hidden
																									name="salaryList[%{#s.index}].fundsourceName"
																									id="fundsourceName%{#s.index}"
																									value="%{fundsourceName}" /> <s:property
																									value="%{fundsourceName}" /></td>
																						</s:if>
																						<s:if test="%{shouldShowHeaderField('scheme')}">
																							<td class="blueborderfortdnew"
																								id="scheme<s:property value="#s.index"/>"><s:hidden
																									name="salaryList[%{#s.index}].schemeName"
																									id="schemeName%{#s.index}"
																									value="%{schemeName}" /> <s:property
																									value="%{schemeName}" /></td>
																						</s:if>
																						<s:if test="%{shouldShowHeaderField('subscheme')}">
																							<td class="blueborderfortdnew"
																								id="subscheme<s:property value="#s.index"/>"><s:hidden
																									name="salaryList[%{#s.index}].subschemeName"
																									id="subschemeName%{#s.index}"
																									value="%{subschemeName}" /> <s:property
																									value="%{subschemeName}" /></td>
																						</s:if>
																						<s:if test="%{shouldShowHeaderField('field')}">
																							<td class="blueborderfortdnew"
																								id="field<s:property value="#s.index"/>"><s:hidden
																									name="salaryList[%{#s.index}].fieldName"
																									id="fieldName%{#s.index}" value="%{fieldName}" />
																								<s:property value="%{fieldName}" /></td>
																						</s:if>
																					</tr>
																				</s:iterator>
																		</table>
																		<s:if test="salaryList == null || salaryList.size==0">
																			<div class="subheadsmallnew">No Records Found</div>
																		</s:if>
																	</div>
																</td>
															</tr>
														</table>
													</span>
												</div>
											</div> <!-- tabber div -->
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div id="paginationdiv" align="center" style="padding-top: 10px;">
			<!-- <a href="#"><<</a> <a href="#">1</a> <a href="#">2</a> <a href="#">3</a> <a href="#">>></a>&nbsp;&nbsp;&nbsp;
		<select name="select">
			<option>Show 50</option>
			<option>Show 100</option>
			<option>Show All</option>
		</select> -->
		</div>
		<div id="buttondiv" align="center" style="display: visible">
			<table align="center" width="100%">
				<tr>
					<td class="modeofpayment"><strong><s:text
								name="payment.mode" /><span class="mandatory">*</span></strong> <input
						name="paymentMode" id="paymentModecash" checked="checked"
						value="cash" type="radio"><label for="paymentModecash"><s:text
								name="cash.consolidated.cheque" /></label> <input name="paymentMode"
						id="paymentModecash" value="rtgs" type="radio"><label
						for="paymentModeRTGS"><s:text name="rtgs" /></label></td>
				</tr>
				<tr>
					<td class="buttonbottomnew" align="center"><s:submit
							method="save" value="Generate Payment" cssClass="buttonsubmit"
							onclick="return check()" /></td>
				</tr>
			</table>
		</div>
		<s:if test="%{!validateUser('createpayment')}">
			<script>
			document.getElementById('searchBtn').disabled=true;
			document.getElementById('errorSpan').innerHTML='<s:text name="payment.invalid.user"/>';
			if(document.getElementById('vouchermis.departmentid'))
			{
				var d = document.getElementById('vouchermis.departmentid');
				d.options[d.selectedIndex].text='----Choose----';
				d.options[d.selectedIndex].text.value=-1;
			}
		</script>
		</s:if>
		<s:if test="%{validateUser('deptcheck')}">
			<script>
				if(document.getElementById('vouchermis.departmentid'))
				{
					document.getElementById('vouchermis.departmentid').disabled=true;
				}
			</script>
		</s:if>
	</s:form>
</body>
</html>
