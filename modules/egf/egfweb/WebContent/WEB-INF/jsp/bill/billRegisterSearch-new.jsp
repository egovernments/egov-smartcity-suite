<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<html>  
<head>  
    <title><s:text name="bill.search.heading"></s:text></title>
</head>
	<body>  
		<s:form action="billRegisterSearch" theme="simple" >
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="Voucher Search" />
			</jsp:include>
<font  style='color: red ; font-weight:bold '> 
<p class="error-block" id="lblError" ></p></font>
			<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
			<div class="subheadnew"><s:text name="bill.search.heading"></s:text></div>
			<table align="center" width="100%" cellpadding="0" cellspacing="0">
				<tr>
				<td class="bluebox" ><s:text name="bill.search.expType"/> <span class="mandatory">*</span></td>
				<td class="bluebox"><s:select name="expType" id="expType" list="dropdownData.expType" headerKey="-1" headerValue="----Choose----" value="%{expType}"/></td>
				</tr>
				<tr>
					<td class="greybox" ><s:text name="bill.search.dateFrom"/> <span class="mandatory">*</span></td>
					<td class="greybox"><s:textfield name="billDateFrom" id="billDateFrom" maxlength="20" value="%{billDateFrom}" /><a href="javascript:show_calendar('forms[0].billDateFrom');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a><br/>(dd/mm/yyyy)</td>
					<td class="greybox"><s:text name="bill.search.dateTo"/> <span class="mandatory">*</span></td>
					<td class="greybox"><s:textfield name="billDateTo" id="billDateTo" maxlength="20" value="%{billDateTo}"  /><a href="javascript:show_calendar('forms[0].billDateTo');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
				</tr>
				<jsp:include page="billSearchCommon-filter.jsp"/>
				
			</table>
	<div class="subheadsmallnew" id="savebuttondiv1"/></div>
	<div class="mandatory" align="left" id="mandatorymarkdiv">* Mandatory Fields</div>
			<div  class="buttonbottom">
				<s:submit method="search" value="Search" cssClass="buttonsubmit" onclick="return validate()"/>
				<input type="submit" value="Close" onclick="javascript:window.close()" class="buttonsubmit"/>
			</div>
			<br/>
			
			<div id="listid" style="display:block">
					<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
			        <tr>  
			        	<th class="bluebgheadtd">Sl. No.</th>
			            <th class="bluebgheadtd">Expenditure Type</th>  
			            <th class="bluebgheadtd">Bill Type</th>  
			            <th class="bluebgheadtd">Bill Number</th>  
			            <th class="bluebgheadtd">Bill Date</th>
			            <th class="bluebgheadtd">Bill Amount</th>  
			            <th class="bluebgheadtd">Passed Amount</th>
			            <th class="bluebgheadtd">Bill Status</th>  
			        </tr>  
				    <s:iterator var="p" value="billList" status="s">  
					
				    <tr>
					 
				    	<td>  
				            <s:property value="#s.index+1" />  
				        </td>
				        <td>  
				            <s:property value="%{expendituretype}" />  
				        </td>
				        <td>  
				            <s:property value="%{billtype}" />  
				        </td>
						<td>  
				            <a href="<s:property value='%{sourcepath}' />"><s:property value="%{billnumber}" /> </a> 
				        </td>
				        <td>  
				            <s:date name="%{billdate}" format="dd/MM/yyyy"/>  
				        </td>
				        <td style="text-align:right">  
				            <s:text name="bill.format.number" ><s:param value="%{billamount}"/></s:text>
				        </td>
				        <td  style="text-align:right">  
				           <s:text name="bill.format.number" ><s:param value="%{passedamount}"/></s:text>
				        </td>
				        
				         <td  style="text-align:center">  
				            <s:property value="%{billstatus}" />  
				        </td>
				    </tr>  
				    </s:iterator>
				</table>  
			</div>
		
		</s:form>  
		<script>
	function validate(){
	
		document.getElementById('lblError').innerHTML ="";
		if(document.getElementById('expType').value == -1){
			document.getElementById('lblError').innerHTML = "Please select expenditure type";
			return false;
		}
		if(document.getElementById('billDateFrom').value.trim().length == 0){
			document.getElementById('lblError').innerHTML = "Please bill from date";
			return false;
		}
		if(document.getElementById('billDateTo').value.trim().length == 0){
			document.getElementById('lblError').innerHTML = "Please bill to date";
			return false;
		}
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


String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}

</script>
		
	</body>  

</html>