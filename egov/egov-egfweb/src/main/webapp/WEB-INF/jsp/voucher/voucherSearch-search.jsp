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
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>

<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>
<link href="<egov:url path='/resources/css/displaytagFormatted.css'/>" rel="stylesheet" type="text/css" />

<html>  
<head>  
    <title>Voucher Search</title>
	<base target="_self" />
	<script type="text/javascript">
	function disableTypeForEditMode()
	{
		var showMode='<s:property value="showMode" />';
		if(showMode=='edit' )
			document.getElementById("type").disabled=true;
	}
	</script>
</head>
	<body onload="disableTypeForEditMode()">  
		<s:form action="voucherSearch" theme="simple" >
			<jsp:include page="../budget/budgetHeader.jsp">
	        		<jsp:param name="heading" value="Voucher Search" />
				</jsp:include>
			<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
			<s:if test="%{showMode=='nonbillPayment'}">
			<div class="formmainbox"><div class="subheadnew">Non Bill Payment Search</div>
			</s:if>
			<s:else>
			<div class="formmainbox"><div class="subheadnew">Voucher Search</div>
			</s:else>
			<table align="center" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="greybox" ><s:text name="voucher.number"/> </td>
					<td class="greybox"><s:textfield name="voucherNumber" id="voucherNumber" maxlength="25" value="%{voucherNumber}"/></td>
					<td class="greybox" ></td>
					<td class="greybox" ></td>
				</tr>
				<tr>
				<td class="bluebox" ><s:text name="voucher.type"/> </td>
				<td class="bluebox"><s:select name="type" id="type" list="dropdownData.typeList" headerKey="-1" headerValue="----Choose----" onchange="loadVoucherNames(this.value)" /></td>
				<td class="bluebox" ><s:text name="voucher.name"/></td>
				<td class="bluebox"><s:select name="name" id="name" list="%{nameList}" headerKey="-1" headerValue="----Choose----" /></td>
				</tr>
				<tr>
					<td class="greybox" ><s:text name="voucher.fromdate"/><span class="mandatory">*</span> </td>
					<s:date name="fromDate" format="dd/MM/yyyy" var="tempFromDate"/>
					<td class="greybox"><s:textfield name="fromDate" id="fromDate" maxlength="20" value="%{tempFromDate}"/><a href="javascript:show_calendar('forms[0].fromDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a><br/>(dd/mm/yyyy)</td>
					<td class="greybox"><s:text name="voucher.todate"/><span class="mandatory">*</span> </td>
					<td class="greybox"><s:textfield name="toDate" id="toDate" maxlength="20" value="%{toDate}"/><a href="javascript:show_calendar('forms[0].toDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
				</tr>
				<jsp:include page="voucher-filter.jsp"/>
				<tr>
				<td class="greybox" ><s:text name="voucher.source"/> </td>
				<td class="greybox"><s:select name="moduleId" id="moduleId" list="sourceMap"  headerKey="-1" headerValue="----Choose----"  /></td>
				</tr>
				<s:hidden name="mode" value="%{mode}" id="mode"/>  
			</table>
			<div  class="buttonbottom">
				<s:submit method="search" value="Search" onclick="return validate()" cssClass="buttonsubmit" />
				<input type="button" value="Close" onclick="javascript:window.close()" class="button" />
			</div>
			<br/>
			 <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<s:if test="%{pagedResults!=null}">
			 <tr>
				<td width="100%">
          			 	  <display:table name="pagedResults" uid="currentRowObject" cellpadding="0" cellspacing="0" 
          			 	  requestURI="" class="its"  style=" border-left: 1px solid #C5C5C5; border-top: 1px solid #C5C5C5;border-right: 1px solid #C5C5C5;border-bottom: 1px solid #C5C5C5;">	
							 <display:column  title=" Sl No" style="text-align:center;"  >
 						 	 <s:property value="%{#attr.currentRowObject_rowNum+ (page-1)*pageSize}"/></display:column>
          			 	   	<display:column    title="Voucher Number"  style="text-align:center;">
 						 	
          			 	   <a href="#" onclick="openVoucher('<s:property value='%{#attr.currentRowObject.id}'/>','<s:property value="%{#attr.currentRowObject.vouchernumber}" />','<s:date name="%{#attr.currentRowObject.voucherdate}" format="dd/MM/yyyy"/>');"><s:property value="%{#attr.currentRowObject.vouchernumber}" /> </display:column>
 						 			
 						 	</a>
 						 	 
          			 	    <display:column   title="Voucher Type" style="text-align:center;">
          			 	    <s:property value="%{#attr.currentRowObject.type}" /> </display:column>
          			 	    <display:column    title="Voucher Name" style="text-align:center;" >
          			 	    <s:property value="%{#attr.currentRowObject.name}" /> </display:column>
          			 	    <display:column      title="Voucher Date" style="text-align:center;" >
          			 	    <s:date name="%{#attr.currentRowObject.voucherdate}" format="dd/MM/yyyy"/>
          			 	    </display:column>
          			 	    <display:column    title="Fund Name" style="text-align:center;">
          			 	    <s:property value="%{#attr.currentRowObject.fundname}" /> </display:column>
          			 	    <display:column    title="Department Name" style="text-align:center;">
          			 	    <s:property value="%{#attr.currentRowObject.deptName}" /> </display:column>
          			 	    <display:column      title="Total Amount" style="text-align:right;" >
          			 	    <s:property value="%{#attr.currentRowObject.amount}" /> </display:column>
          			 	    <display:column      title="Status" style="text-align:center;">
          			 	    <s:property value="%{#attr.currentRowObject.status}" /> </display:column>
          			 	     <display:column    title="Source" style="text-align:center;">
          			 	    <s:property value="%{#attr.currentRowObject.source}" /> </display:column>
          			 	   
          			 	  </display:table>
          			 	  </td>
          		<tr>
          		
				</tr>
			</s:if>
			<s:elseif test="%{voucherList.size!=0 || voucherList!=null}">
			<div id="listid" style="display:none">
					<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
			        <tr>  
			            <th class="bluebgheadtd">Sl.No.</th>  
			            <th class="bluebgheadtd">Voucher Number</th>
			            <th class="bluebgheadtd">Type</th>
			            <th class="bluebgheadtd">Name</th>
			            <th class="bluebgheadtd">Voucher Date</th>  
			            <th class="bluebgheadtd">Fund Name</th>
			             <th class="bluebgheadtd">Department Name</th>
			            <th class="bluebgheadtd">Amount(Rs)</th>
			            <th class="bluebgheadtd">Status</th>
						<th class="bluebgheadtd">Source</th>  
			        </tr>  
			        <c:set var="trclass" value="greybox"/>
			        
				    <s:iterator var="p" value="voucherList" status="s">  
				    <tr>  
				    	<td class="<c:out value="${trclass}"/>">  
				            <s:property value="#s.index+1" />
				        </td>
						<td align="left"  class="<c:out value="${trclass}"/>">  
				            <a href="#" onclick="openVoucher(<s:property value='%{id}'/>,'<s:text name="%{name}.%{showMode}" />','<s:property value="%{vouchernumber}" /> ' ,'<s:date name="%{voucherdate}" format="dd/MM/yyyy"/>');"><s:property value="%{vouchernumber}" /> </a> 
				        </td>
				        <td align="left"  class="<c:out value="${trclass}"/>">  
				            <s:property value="%{type}" />
				        </td>
				         <td align="left"  class="<c:out value="${trclass}"/>">  
				            <s:property value="%{name}" />
				        </td>
				        <td  class="<c:out value="${trclass}"/>">  
				            <s:date name="%{voucherdate}" format="dd/MM/yyyy"/>  
				        </td>
				        <td align="left"  class="<c:out value="${trclass}"/>">  
				            <s:property value="%{fundname}" />
				        </td>
				        <td align="left"  class="<c:out value="${trclass}"/>">  
				            <s:property value="%{deptName}" />
				        </td>
				        <td style="text-align:right"  class="<c:out value="${trclass}"/>">  
				            <s:text name="format.number" ><s:param value="%{amount}"/></s:text>
				        </td>
				        <td  class="<c:out value="${trclass}"/>">  
				      	    <s:text name="%{status}" />
				        </td>
				        <td  class="<c:out value="${trclass}"/>">  
				      	    <s:text name="%{source}" />
				        </td>
				        <c:choose>
					        <c:when test="${trclass=='greybox'}"><c:set var="trclass" value="bluebox"/></c:when>
					        <c:when test="${trclass=='bluebox'}"><c:set var="trclass" value="greybox"/></c:when>
				        </c:choose>
				    </tr>  
				    </s:iterator>
				    <s:hidden name="targetvalue" value="%{target}" id="targetvalue"/>  
				</table>  
			</div>
			</s:elseif>
			</table>
			<br/>
			<div id="msgdiv" style="display:none">
				<table align="center" class="tablebottom" width="80%">
					<tr><th class="bluebgheadtd" colspan="7">No Records Found</td></tr>
				</table>
			</div>
			<br/>
			<br/>
			<s:hidden name="showMode"  id="showMode"/>
		</s:form>  
		
		<script>
		
		function loadVoucherNames(selected)
		{
			var s="";  
			if(selected==-1)
				{
				document.getElementById('name').options.length=0;
				document.getElementById('name').options[0]= new Option('--------Choose--------','0');
				}
		<s:iterator value="voucherTypes" var="obj">
		  s='<s:property value="#obj"/>';
		 if(selected==s)
		 {
		document.getElementById('name').options.length=0;
		document.getElementById('name').options[0]= new Option('--------Choose--------','0');

		 <s:iterator value="voucherNames[#obj]" status="stat" var="names">
		 document.getElementById('name').options[<s:property value="#stat.index+1"/>]= new Option('<s:property value="#names"/>','<s:property value="#names"/>');
		 </s:iterator>   
		 }
		 </s:iterator>
			  
			
		}
		function openVoucher(vid,url,voucherNumber,voucherDate){
		
		var showMode = document.getElementById('showMode').value ;
		
		if(showMode=='nonbillPayment')
		{
		url="../payment/directBankPayment!nonBillPayment.action?showMode="+showMode+"&voucherHeader.id="+vid;
		window.open(url,'Search','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
		}
		else if(showMode == 'sourceLink' ){
			window.returnValue = voucherNumber+"$"+voucherDate+"$"+vid;
	        window.close();
	        return;
		}
		else if(showMode == '' ){
			var url = 'preApprovedVoucher!loadvoucherview.action?vhid='+ vid;
		}
		else{

			var url =  url+'='+ vid+'&showMode='+showMode;
		}
			window.open(url,'Search','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
		}
		
		function validate()
		{
		document.getElementById('type').disabled=false;
		}
		
			<s:if test="%{voucherList.size==0}">
				dom.get('msgdiv').style.display='block';
				dom.get('listid').style.display='none';
			</s:if>
			<s:if test="%{pagedResults!=null}">
				dom.get('msgdiv').style.display='none';
				dom.get('listid').style.display='none';
			</s:if>
			<s:if test="%{voucherList.size!=0}">
				dom.get('msgdiv').style.display='none';
				dom.get('listid').style.display='block';
			</s:if>
			
		var showMode = document.getElementById('showMode').value ;
		if(showMode=='nonbillPayment')
		{
			if(document.getElementById('type'))
			{
			document.getElementById('type').disabled=true;
			}
			document.title="Non Bill Payment Search";
		}
		</script>
	</body>  
</html>
