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
<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<%@ page language="java"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>

<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js"></script>
<script type="text/javascript" src="/EGF/commonjs/ajaxCommonFunctions.js"></script>
	
		<script type="text/javascript" src="/EGF/exility/PageManager.js"></script>
		<script type="text/javascript" src="/EGF/exility/PageValidator.js"></script>
		<script type="text/javascript" src="/EGF/exility/data.js"></script>
		<script type="text/javascript" src="/EGF/exility/ExilityParameters.js"></script>
		<script type="text/javascript" src="/EGF/resources/javascript/calender.js"></script>
		<script type="text/javascript" src="/EGF/resources/javascript/calendar.js" ></script>
		<script type="text/javascript" src="/EGF/javascript/dateValidation.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Create Voucher</title>

</head>

<body onload="onloadtask();">

<s:form action="billVoucher" theme="simple" name="billVoucher" >
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="Journal voucher search" />
			</jsp:include>
		<div class="formmainbox"><div class="formheading"/><div class="subheadnew">Create Voucher</div>
		<div id="listid" style="display:block">
		<br/>
<div align="center">
<font  style='color: red ;font-weight:bold'> 
<p class="error-block" id="lblError" ></p>
</font>
<span class="mandatory">

<font  style='color: red ; font-weight:bold '> 
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage /> </font>
			</span>
	<table border="0" width="100%">
	<tr><td class="bluebox">Bill Type<span class="bluebox"><span class="mandatory">*</span></span></td><td class="bluebox"><s:select name="expType" id="expType" list="dropdownData.expTypeList"  headerKey="-1" headerValue="----Choose----"   /></td>
	<td class="bluebox" id="deptLabel"><s:text name="voucher.department"/></td>
	<td class="bluebox"><s:select name="vouchermis.departmentid" id="departmentid" list="dropdownData.departmentList" listKey="id" listValue="deptName" headerKey="-1" headerValue="----Choose----" value="voucherHeader.vouchermis.departmentid.id"/></td>
	
</tr>
			
			<tr>
				<td class="greybox">From Date</td>
				<td class="greybox"><s:textfield name="voucherTypeBean.voucherDateFrom" id="voucherDateFrom" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
				<a href="javascript:show_calendar('billVoucher.voucherDateFrom');" style="text-decoration:none">&nbsp;<img tabIndex=-1 src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
				<td class="greybox">To Date</td>
				<td class="greybox"><s:textfield name="voucherTypeBean.voucherDateTo" id="voucherDateTo" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
				<a href="javascript:show_calendar('billVoucher.voucherDateTo');" style="text-decoration:none">&nbsp;<img tabIndex=-1 src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
			</tr>
			<tr>
					<td class="bluebox" ><s:text name="bill.Number"/> </td>
					<td class="bluebox"><s:textfield name="billNumber" id="billNumber"  maxlength="50" value="%{billNumber}"/>
					</td>
			</tr>
		
	</table>
</div>
	<br><br>
     <div align="center">
		
		<table border="0" width="50%">
		    <tr></tr>
			<tr>
				<td align="center">	
					 <s:submit cssClass="buttonsubmit" value="Search" id="search" name="search"  method="lists" onclick="return validateSearch()"/></td>
					<td align="center"> <s:reset id="Reset" value="Cancel" cssClass="buttonsubmit"/></td>
					<td align="center"> <input type="button" value="Close" onclick="javascript:window.close()" class="button" />
				</td>
			</tr>
		</table>
	</div>
	<div id="listid" style="display:block">
					<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
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
				    <tr>  
				    	<td>  
				            <s:property value="#s.index+1" />  
				        </td>
						<td>  
				            <a href="preApprovedVoucher!voucher.action?billid=<s:property value='%{id}'/>"><s:property value="%{billnumber}" /> </a> 
				        </td>
				        <td>  
				            <s:date name="%{billdate}" format="dd/MM/yyyy"/>  
				        </td>
				        <td style="text-align:right">  
				        	<s:text name="format.number" ><s:param value="%{billamount}"/></s:text>
				        </td>
				        <td  style="text-align:right">  
				            <s:text name="format.number" ><s:param value="%{passedamount}"/></s:text>
				        </td>
				        <td>  
				            <s:property value="%{expendituretype}" />  
				        </td>
				         <td>  
				            <s:property value="%{egBillregistermis.egDepartment.deptName}" />  
				        </td>
				    </tr>  
				    </s:iterator>
				</table>  
			</div>
</s:form>
<script>
function onloadtask(){
<s:iterator value="getActionErrors()" >
  document.getElementById("search").style.display="none";
   document.getElementById("Reset").style.display="none";
 </s:iterator>
<s:if test="%{isFieldMandatory('department')}"> 
	// document.getElementById("departmentid").disabled=true;
	
</s:if>

}
	function validateSearch(){
	document.getElementById('lblError').innerHTML ="";
	if((document.getElementById("expType").value) == -1 ){
		document.getElementById('lblError').innerHTML ="Please select a bill type";
		return false;
	}
	document.getElementById("departmentid").disabled=false;
return true;;
}
</script>
</body>

</html>
