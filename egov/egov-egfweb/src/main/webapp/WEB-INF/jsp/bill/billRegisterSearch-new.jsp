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
<title><s:text name="bill.search.heading"></s:text></title>
</head>
<body onload="changeMandatoryField()">
	<s:form name="billRegisterForm" action="billRegisterSearch"
		theme="simple">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="Voucher Search" />
		</jsp:include>
		<font style='color: red; font-weight: bold'>
			<p class="error-block" id="lblError"></p>
		</font>
		<span class="mandatory1"> <s:actionerror /> <s:fielderror />
			<s:actionmessage />
		</span>
		<div class="formmainbox">
			<div class="subheadnew">
				<s:text name="bill.search.heading"></s:text>
			</div>
			<table align="center" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="bill.search.expType" /> <span
						class="mandatory1">*</span></td>
					<td class="bluebox"><s:select name="expType" id="expType"
							list="dropdownData.expType" headerKey="-1"
							headerValue="----Choose----" value="%{expType}" /></td>
					<td class="greybox"><s:text name="bill.search.billnumber" /></td>
					<td class="greybox"><s:textfield name="billnumber"
							id="billnumber" maxlength="25" value="%{billnumber}"
							onblur="changeMandatoryField()" /></td>
				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="greybox"><s:text name="bill.search.dateFrom" /> <span
						class="mandatory1" id="fromDateMandatory">*</span></td>

					<td class="greybox"><s:date name="billDateFrom"
							var="billDateFrom" format="dd/MM/yyyy" /> <s:textfield
							id="billDateFrom" name="billDateFrom" value="%{billDateFrom}"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							placeholder="DD/MM/YYYY" cssClass="form-control datepicker"
							data-inputmask="'mask': 'd/m/y'" /></td>

					<td class="greybox"><s:text name="bill.search.dateTo" /> <span
						class="mandatory1" id="toDateMandatory">*</span></td>

					<td class="greybox"><s:date name="billDateTo" var="billDateTo"
							format="dd/MM/yyyy" /> <s:textfield id="billDateTo"
							name="billDateTo" value="%{billDateTo}"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							placeholder="DD/MM/YYYY" cssClass="form-control datepicker"
							data-inputmask="'mask': 'd/m/y'" /></td>


				</tr>
				<jsp:include page="billSearchCommon-filter.jsp" />
			</table>
		</div>
		<div align="center" class="buttonbottom">
			<input type="submit" class="buttonsubmit" value="Search" id="Search"
				name="button" onclick="return validateFormAndSubmit();" /> <input
				type="button" id="Close" value="Close"
				onclick="javascript:window.close()" class="button" />
		</div>
		<br />
		<s:if test="%{billList.size!=0 || billList!=null}">
			<div id="listid" style="display: block">
				<table width="100%" align="center" cellpadding="0" cellspacing="0"
					class="setborder" style="border-collapse: inherit;">
					<tr>
						<th class="bluebgheadtd">Sl. No.</th>
						<th class="bluebgheadtd">Expenditure Type</th>
						<th class="bluebgheadtd">Bill Type</th>
						<th class="bluebgheadtd">Bill Number</th>
						<th class="bluebgheadtd">Bill Date</th>
						<th class="bluebgheadtd">Bill Amount</th>
						<th class="bluebgheadtd">Passed Amount</th>
						<th class="bluebgheadtd">Bill Status</th>
						<th class="bluebgheadtd">Owner Name</th>
					</tr>

					<s:iterator var="p" value="billList" status="s">

						<tr>

							<td style="text-align: center"
								class="text-center bluebox setborder"><s:property
									value="#s.index+1" /></td>
							<td style="text-align: center"
								class="text-center bluebox setborder"><s:property
									value="%{expendituretype}" /></td>
							<td style="text-align: center"
								class="text-center bluebox setborder"><s:property
									value="%{billtype}" /></td>
							<td style="text-align: center"
								class="text-center bluebox setborder"><a href="#"
								onclick="openBill('<s:property value='%{sourcepath}' />')"><s:property
										value="%{billnumber}" /></a></td>
							<td style="text-align: center"
								class="text-center bluebox setborder"><s:date
									name="%{billdate}" format="dd/MM/yyyy" /></td>
							<td class="bluebox setborder" style="text-align: right"><s:text
									name="bill.format.number">
									<s:param value="%{billamount}" />
								</s:text></td>
							<td class="bluebox setborder" style="text-align: right"><s:text
									name="bill.format.number">
									<s:param value="%{passedamount}" />
								</s:text></td>

							<td style="text-align: center"
								class="text-center bluebox setborder"><s:property
									value="%{billstatus}" /></td>
							<td style="text-align: center"
								class="text-center bluebox setborder "><s:property
									value="%{ownerName}" /></td>
						</tr>
					</s:iterator>
				</table>
			</div>
		</s:if>
		<div id="msgdiv" style="display: none">
			<table align="center" class="tablebottom" width="80%">
				<tr>
					<th class="bluebgheadtd" colspan="7">No Records Found
					</td>
				</tr>
			</table>
		</div>
		<div id="loading" class="loading"
			style="width: 700; height: 700; display: none" align="center">
			<blink style="color: red">Searching processing, Please
				wait...</blink>
		</div>



	</s:form>
	<script>
	 function validateFormAndSubmit(){
		 if(jQuery('#billnumber').val()!="")
			 {
				 if(jQuery('#expType').val()==-1)
					 {
						 jQuery('#lblError').html("Please select expenditure type");
						 return false
					 }
			 }
		 else {
				 if(!validate())
					 return false;
		 	  }
	       	document.billRegisterForm.action='${pageContext.request.contextPath}/bill/billRegisterSearch-search.action';
		 	document.billRegisterForm.submit();
		 	return true;
		 }
	function validate(){
	
		document.getElementById('lblError').innerHTML ="";
		if(document.getElementById('expType').value == -1){
			document.getElementById('lblError').innerHTML = "Please select expenditure type";
			return false;
		}
		if(document.getElementById('billDateFrom').value.trim().length == 0){
			document.getElementById('lblError').innerHTML = "Please select bill from date";
			return false;
		}
		if(document.getElementById('billDateTo').value.trim().length == 0){
			document.getElementById('lblError').innerHTML = "Please select bill to date";
			return false;
			
		}

		var fromDate=document.getElementById('billDateFrom').value;
		var toDate=document.getElementById('billDateTo').value;
		if(!DateValidation(fromDate,toDate))
			return false;
		
		 <s:if test="%{isFieldMandatory('fund')}"> 
				 if(null != document.getElementById('fundId') && document.getElementById('fundId').value == -1){

					document.getElementById('lblError').innerHTML = "Please Select a fund";
					return false;
				 }
			 </s:if>
			<s:if test="%{isFieldMandatory('department')}"> 
				 if(null!= document.getElementById('vouchermis.departmentid') && document.getElementById('vouchermis.departmentid').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a department";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('scheme')}"> 
				 if(null!=document.getElementById('schemeid') &&  document.getElementById('schemeid').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a scheme";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('subscheme')}"> 
				 if(null!= document.getElementById('subschemeid') && document.getElementById('subschemeid').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a subscheme";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('functionary')}"> 
				 if(null!=document.getElementById('vouchermis.functionary') &&  document.getElementById('vouchermis.functionary').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a functionary";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('fundsource')}"> 
				 if(null !=document.getElementById('fundsourceId') &&  document.getElementById('fundsourceId').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a fundsource";
					return false;
				}
			</s:if>
			<s:if test="%{isFieldMandatory('field')}"> 
				 if(null!= document.getElementById('vouchermis.divisionid') && document.getElementById('vouchermis.divisionid').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a field";
					return false;
				 }
			</s:if>
			
			
		return true;
	}
function openBill(url){
		
			window.open(url,'','width=900, height=700,scrollbars=1');
			
		}
function doAfterSubmit(){
		document.getElementById('loading').style.display ='block';
		dom.get('msgdiv').style.display='none';
		dom.get('listid').style.display='none';
	}
 
			
String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}

	<s:if test="%{billList.size<=0}">
				dom.get('msgdiv').style.display='block';
				dom.get('listid').style.display='none';
			</s:if>
	<s:if test="%{billList.size!=0}">
				dom.get('msgdiv').style.display='none';
				document.getElementById('loading').style.display ='none';
				dom.get('listid').style.display='block';
	</s:if>	
	
function changeMandatoryField()
	{
		if(jQuery('#billnumber').val()!="")
			{
				jQuery("#fromDateMandatory").html("");
				jQuery("#toDateMandatory").html("");
				jQuery("#fundDateMandatory").html("");
			}
		else
			{
				jQuery("#fromDateMandatory").html("*");
				jQuery("#toDateMandatory").html("*");
				jQuery("#fundDateMandatory").html("*");
			}
		
	}
</script>

</body>

</html>