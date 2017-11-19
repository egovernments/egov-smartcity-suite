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
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252" />
<title>Create Voucher From Bill</title>

</head>

<body onload="onloadtask();">

	<s:form action="billVoucher" theme="simple" name="billVoucher">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="Journal voucher search" />
		</jsp:include>
		<div class="formmainbox">
			<div class="formheading">
				<div class="subheadnew">Create Voucher From Bill</div>
			</div>
			<div id="listid" style="display: block">
				<br />
				<div align="left">
					<font style='color: red; font-weight: bold'>
						<p class="error-block" id="lblError"></p>
					</font> <span class="mandatory1"> <font
						style='color: red; font-weight: bold'> <s:actionerror /> <s:fielderror />
							<s:actionmessage />
					</font>
					</span>
					<table border="0" width="100%">
						<tr>
							<td class="bluebox">Bill Type<span class="bluebox"><span
									class="mandatory1">*</span></span></td>
							<td class="bluebox"><s:select name="expType" id="expType"
									list="dropdownData.expTypeList" headerKey="-1"
									headerValue="----Choose----" /></td>
							<td class="bluebox" id="deptLabel"><s:text
									name="voucher.department" /></td>
							<td class="bluebox"><s:select name="vouchermis.departmentid"
									id="departmentid" list="dropdownData.departmentList"
									listKey="id" listValue="name" headerKey="-1"
									headerValue="----Choose----"
									value="voucherHeader.vouchermis.departmentid.id" /></td>

						</tr>

						<tr>
							<td class="greybox">From Date</td>
							<td class="greybox"><s:textfield id="voucherDateFrom"
									name="voucherTypeBean.voucherDateFrom" data-date-end-date="0d"
									onkeyup="DateFormat(this,this.value,event,false,'3')"
									placeholder="DD/MM/YYYY" class="form-control datepicker"
									data-inputmask="'mask': 'd/m/y'" /></td>
							<td class="greybox">To Date</td>
							<td class="greybox"><s:textfield id="voucherDateTo"
									name="voucherTypeBean.voucherDateTo" data-date-end-date="0d"
									onkeyup="DateFormat(this,this.value,event,false,'3')"
									placeholder="DD/MM/YYYY" class="form-control datepicker"
									data-inputmask="'mask': 'd/m/y'" />
						</tr>
						<tr>
							<td class="bluebox"><s:text name="bill.Number" /></td>
							<td class="bluebox"><s:textfield name="billNumber"
									id="billNumber" maxlength="50" value="%{billNumber}" /></td>
							<td class="bluebox"></td>
							<td class="bluebox"></td>
						</tr>

					</table>
				</div>
				<br> <br>
			</div>
		</div>
		<div align="center">
			<div class="buttonbottom">
				<table align="center">
					<tr>

						<td><s:submit value="Search" onclick="return validate()"
								cssClass="buttonsubmit" />&nbsp;</td>
						<td><input type="button" value="Reset" class="button"
							onclick="return resetForm();" />&nbsp;</td>
						<td><input type="button" value="Close"
							onclick="javascript:window.close()" class="button" />&nbsp;</td>
					</tr>
				</table>
			</div>
		</div>
		<s:if
			test="%{preApprovedVoucherList.size!=0 && preApprovedVoucherList!=null}">
			<div id="listid" style="display: block">
				<table width="100%" align="center" class="tablebottom">
					<tr>
						<th class="bluebgheadtd">Sl. No.</th>
						<th class="bluebgheadtd">Bill Number</th>
						<th class="bluebgheadtd">Bill Date</th>
						<th class="bluebgheadtd">Bill Amount</th>
						<th class="bluebgheadtd">Passed Amount</th>
						<th class="bluebgheadtd">Expenditure Type</th>
						<th class="bluebgheadtd">Department</th>
					</tr>
					<s:iterator var="p" value="preApprovedVoucherList" status="s">
						<tr class="setborder">
							<td class="bluebox setborder"><s:property value="#s.index+1" />
							</td>
							<td class="bluebox setborder" style="text-align: center"><a
								href="preApprovedVoucher-voucher.action?billid=<s:property value='%{id}'/>"><s:property
										value="%{billnumber}" /> </a></td>
							<td class="bluebox setborder" style="text-align: center"><s:date
									name="%{billdate}" format="dd/MM/yyyy" /></td>
							<td class="bluebox setborder" style="text-align: right"><s:text
									name="format.number">
									<s:param value="%{billamount}" />
								</s:text></td>
							<td class="bluebox setborder" style="text-align: right"><s:text
									name="format.number">
									<s:param value="%{passedamount}" />
								</s:text></td>
							<td class="bluebox setborder" style="text-align: center"><s:property
									value="%{expendituretype}" /></td>
							<td class="bluebox setborder" style="text-align: center"><s:property
									value="%{egBillregistermis.egDepartment.name}" /></td>
						</tr>
					</s:iterator>
				</table>
			</div>
		</s:if>

	</s:form>
	<script type="text/javascript">
function onloadtask(){
<s:iterator value="getActionErrors()" >
  document.getElementById("search").style.display="none";
   document.getElementById("Reset").style.display="none";
 </s:iterator>
<s:if test="%{isFieldMandatory('department')}"> 
	// document.getElementById("departmentid").disabled=true;
	
</s:if>

}


function validate()
{
	
	var expType=document.getElementById('expType').value;
	
	

	if(expType == "-1"){
		bootbox.alert("Please select Bill Type");
		return false;
		}
	
	
	 document.billVoucher.action='${pageContext.request.contextPath}/voucher/billVoucher-lists.action';
	 document.billVoucher.submit();

   
document.getElementById('expType').disabled=false;
return true;
}


	

	function resetForm()
	{

		document.getElementById("expType").value=-1;
		document.getElementById("departmentid").value=-1;
		document.getElementById("voucherDateFrom").value="";
		document.getElementById("voucherDateTo").value="";
		document.getElementById("billNumber").value="";
		
	
	}
</script>
</body>

</html>