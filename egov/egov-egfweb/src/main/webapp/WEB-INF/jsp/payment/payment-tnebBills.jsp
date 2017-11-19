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
<link href="/EGF/resources/css/budget.css?rnd=${app_release_no}" rel="stylesheet"
	type="text/css" />
<link href="/EGF/resources/css/commonegovnew.css?rnd=${app_release_no}" rel="stylesheet"
	type="text/css" />
<link rel="stylesheet" href="/EGF/resources/css/tabber.css?rnd=${app_release_no}"
	TYPE="text/css">
<script type="text/javascript" src="/EGF/resources/javascript/tabber.js?rnd=${app_release_no}"></script>
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
function changeTNEBSelectListCount(obj)
{
	if(obj.checked){
		document.getElementById('miscount').value=parseInt(document.getElementById('miscount').value)+1;		
	}
	else
	{
		document.getElementById('miscount').value=parseInt(document.getElementById('miscount').value)-1;
		if(document.getElementById('miscount').value==0)
			document.getElementById('miscattributes').value='';
	}
}
function search()
{
	if(document.getElementById('vouchermis.departmentid'))
		document.getElementById('vouchermis.departmentid').disabled=false;
	var month = document.getElementById('month').value;
	var year = document.getElementById('year').value;
	var region = document.getElementById('region').value;
	if(region == ""){
		 bootbox.alert("Please select Region");
		 return false;
	}
	if(month!=""){
		if(year == ""){
			 bootbox.alert("Please select Year");
		  return false;
		}
	}
	if(year!=""){
		if(month == ""){
			 bootbox.alert("Please select Month");
		  return false;
		}
	}
	return true;
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
function selectAllTNEB(element){
	var length = 0;
	<s:if test="%{contingentList!=null}">
		length = <s:property value="%{contingentList.size()}"/>;
	</s:if>
	
	if(element.checked == true)	{
		checkAll('contingentList',length);
	}
	else
		uncheckAll('contingentList',length);
}

</script>

</head>
<body>
	<s:form action="payment" theme="simple">
		<s:token />
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="TNEB Bill Payment Search" />
		</jsp:include>
		<span class="mandatory"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
		</span>
		<s:hidden name="billSubType" id="billSubType" value="%{billSubType}" />
		<s:hidden name="expType" id="expType" value="%{expType}" />
		<s:hidden name="bank_branch" id="bank_branch" />
		<s:hidden name="bank_account" id="bank_account" />
		<s:hidden name="bankaccount" id="bankaccount" />
		<s:hidden name="bankbranch" id="bankbranch" />
		<s:hidden name="voucherHeader.fundId.id" id="voucherHeader.fundId.id"
			value="%{voucherHeader.fundId.id}" />
		<s:hidden name="voucherHeader.vouchermis.function.id"
			id="voucherHeader.vouchermis.function.id"
			value="%{voucherHeader.vouchermis.function.id}" />
		<s:hidden name="voucherHeader.vouchermis.departmentid.id"
			id="voucherHeader.vouchermis.departmentid.id"
			value="%{voucherHeader.vouchermis.departmentid.id}" />
		<div class="formmainbox">
			<div class="subheadnew">TNEB Bill Payment</div>
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
													<h2>Search TNEB Bill</h2>
													<span>
														<table width="100%" border="0" cellspacing="0"
															cellpadding="0">
															<tr>
																<td align="center" colspan="4" class="serachbillhead"
																	width="10%">TNEB Bill Payment Search</td>
															</tr>
															<tr>
																<td class="bluebox"><s:text
																		name="payment.billnumber" /></td>
																<td class="bluebox"><s:textfield name="billNumber"
																		id="billNumber" maxlength="25" value="%{billNumber}" /></td>
																<td class="bluebox">
																<td class="bluebox">
															</tr>
															<tr>
																<td class="greybox"><s:text
																		name="tnebpayment.monthyear" /></td>
																<td class="greybox"><s:select name="month"
																		id="month" list="%{monthMap}" headerKey=""
																		headerValue="----Choose----" />
																	<s:select name="year" id="year"
																		list="dropdownData.financialYearsList" listKey="id"
																		listValue="finYearRange" headerKey=""
																		headerValue="----Choose----" /></td>
																<td class="greybox"><s:text
																		name="payment.tneb.bill.region" /><span
																	class="mandatory">*</span></td>
																<td class="greybox" id="regionRowId2"><s:select
																		name="region" id="region"
																		list="dropdownData.regionsList" headerKey=""
																		headerValue="----Choose----" /></td>

																</td>
															</tr>
															<tr>
																<td class="bluebox"><s:text
																		name="payment.expendituretype" /></td>
																<td class="bluebox"><s:property value="%{expType}" /></td>
																<td class="bluebox"><s:text name="voucher.fund" /></td>
																<td class="bluebox"><s:property
																		value="%{voucherHeader.fundId.name}" /></td>
															</tr>
															<tr>
																<td class="greybox"><s:text
																		name="voucher.department" /></td>
																<td class="greybox"><s:property
																		value="%{voucherHeader.vouchermis.departmentid.deptName}" /></td>
																<td class="greybox"><s:text name="voucher.function" /></td>
																<td class="greybox"><s:property
																		value="%{voucherHeader.vouchermis.function.name}" /></td>

															</tr>
															<tr>
																<td align="center" colspan="4">
																	<div class="buttonbottom">
																		<s:submit method="tnebBills" value="Search"
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
													<h2>TNEB Bill</h2>
													<span>
														<table align="center" border="0" cellpadding="0"
															cellspacing="0" class="newtable">
															<tr>
																<td colspan="6"><div class="subheadsmallnew">TNEB
																		Bill Payment Search</div></td>
															</tr>
															<tr>
																<td colspan="6">
																	<div style="float: left; width: 100%;">
																		<table align="center" border="0" cellpadding="0"
																			cellspacing="0" width="100%">
																			<tr>
																				<th class="bluebgheadtdnew">Select<input
																					type="checkbox" name="tnebSelectAll"
																					id="tnebSelectAll" onclick="selectAllTNEB(this)" />
																				</checkbox></th>
																				<jsp:include page="tnebBillDetails-header.jsp" />
																				<s:iterator var="p" value="contingentList"
																					status="s">
																					<tr>
																						<td class="blueborderfortdnew"><s:hidden
																								name="contingentList[%{#s.index}].csBillId"
																								id="csBillId%{#s.index}" value="%{csBillId}" />
																							<s:checkbox
																								name="contingentList[%{#s.index}].isSelected"
																								id="isSelected%{#s.index}"
																								onclick="changeTNEBSelectListCount(this)"></s:checkbox></td>
																						<td class="blueborderfortdnew"><s:property
																								value="#s.index+1" /></td>
																						<td align="left" class="blueborderfortdnew"><s:hidden
																								name="contingentList[%{#s.index}].expType"
																								id="expType%{#s.index}" value="%{expType}" />
																							<s:hidden
																								name="contingentList[%{#s.index}].billNumber"
																								id="billNumber%{#s.index}" value="%{billNumber}" />
																							<s:property value="%{billNumber}" /></td>
																						<td class="blueborderfortdnew"><s:hidden
																								name="contingentList[%{#s.index}].billDate"
																								id="billDate%{#s.index}" value="%{billDate}" />
																							<s:date name="%{billDate}" format="dd/MM/yyyy" /></td>

																						<td align="left" class="blueborderfortdnew"><s:hidden
																								name="contingentList[%{#s.index}].billVoucherNumber"
																								id="billVoucherNumber%{#s.index}"
																								value="%{billVoucherNumber}" />
																							<s:property value="%{billVoucherNumber}" /></td>
																						<td style="text-align: left"
																							class="blueborderfortdnew"><s:hidden
																								name="contingentList[%{#s.index}].billVoucherDate"
																								id="billVoucherDate%{#s.index}"
																								value="%{billVoucherDate}" />
																							<s:date name="%{billVoucherDate}"
																								format="dd/MM/yyyy" /></td>

																						<td align="left" class="blueborderfortdnew"><s:hidden
																								name="contingentList[%{#s.index}].payTo"
																								id="payTo%{#s.index}" value="%{payTo}" />
																							<s:property value="%{payTo}" /></td>
																						<td style="text-align: right"
																							class="blueborderfortdnew"><s:hidden
																								name="contingentList[%{#s.index}].netAmt"
																								id="netAmt%{#s.index}" value="%{netAmt}" />
																							<s:text name="payment.format.number">
																								<s:param value="%{netAmt}" />
																							</s:text></td>
																						<td style="text-align: right"
																							class="blueborderfortdnew"><s:hidden
																								name="contingentList[%{#s.index}].earlierPaymentAmt"
																								id="earlierPaymentAmt%{#s.index}"
																								value="%{earlierPaymentAmt}" />
																							<s:text name="payment.format.number">
																								<s:param value="%{earlierPaymentAmt}" />
																							</s:text></td>
																						<td style="text-align: right"
																							class="blueborderfortdnew"><s:hidden
																								name="contingentList[%{#s.index}].payableAmt"
																								id="payableAmt%{#s.index}" value="%{payableAmt}" />
																							<s:hidden
																								name="contingentList[%{#s.index}].paymentAmt"
																								id="paymentAmt%{#s.index}" value="%{paymentAmt}" />
																							<s:text name="payment.format.number">
																								<s:param value="%{payableAmt}" />
																							</s:text></td>
																						<s:if test="%{!isFieldMandatory('fund')}">
																							<td class="blueborderfortdnew"
																								id="fund<s:property value="#s.index"/>"><s:hidden
																									name="contingentList[%{#s.index}].fundName"
																									id="fundName%{#s.index}" value="%{fundName}" />
																								<s:property value="%{fundName}" /></td>
																						</s:if>
																						<s:if
																							test="%{shouldShowHeaderField('department')}">
																							<td class="blueborderfortdnew"
																								id="dept<s:property value="#s.index"/>"><s:hidden
																									name="contingentList[%{#s.index}].deptName"
																									id="deptName%{#s.index}" value="%{deptName}" />
																								<s:property value="%{deptName}" /></td>
																						</s:if>
																						<s:if test="%{shouldShowHeaderField('function')}">
																							<td class="blueborderfortdnew"
																								id="function<s:property value="#s.index"/>"><s:hidden
																									name="contingentList[%{#s.index}].functionName"
																									id="functionName%{#s.index}"
																									value="%{functionName}" />
																								<s:property value="%{functionName}" /></td>
																						</s:if>
																						<td align="left" class="blueborderfortdnew"><s:hidden
																								name="contingentList[%{#s.index}].region"
																								id="payTo%{#s.index}" value="%{region}" />
																							<s:property value="%{region}" /></td>
																					</tr>
																				</s:iterator>
																		</table>
																		<s:if
																			test="contingentList == null || contingentList.size==0">
																			<div class="subheadsmallnew">No Records Found</div>
																		</s:if>
																	</div>
																</td>
															</tr>
														</table>
													</span>
												</div>
											</div>
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div id="buttondiv" align="center" style="display: visible">
			<table align="center" width="100%">
				<tr>
					<td class="modeofpayment"><strong><s:text
								name="payment.mode" /><span class="mandatory">*</span></strong> <input
						name="paymentMode" id="paymentModecash" checked="checked"
						value="rtgs" type="radio"><label for="paymentModeRTGS"><s:text
								name="rtgs" /></label></td>
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
