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
    <title>Voucher Search</title>
</head>
	<body>  
		<s:form action="approvePreapprovedVouchers" theme="simple" >
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="Approve pre-approval Voucher Search" />
			</jsp:include>
			<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
			<div class="formmainbox"><div class="formheading"></div>
			<table align="center" width="80%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="greybox" ><s:text name="voucher.number"/> </td>
					<td class="greybox"><s:textfield name="voucherNumber" id="voucherNumber" maxlength="25" value="%{voucherNumber}"/></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
				<td class="bluebox" ><s:text name="voucher.type"/> </td>
					<td class="bluebox"><s:select name="type" id="type" list="dropdownData.typeList" headerKey="-1" headerValue="----Choose----" onchange="loadVoucherNames(this)" /></td>
				<egov:ajaxdropdown fields="['Text','Value']" url="voucher/common!ajaxLoadVoucherNames.action" dropdownId="name" id="name"/>
				<td class="bluebox" ><s:text name="voucher.name"/></td>
				<td class="bluebox"><s:select name="name" id="name" list="%{nameList}" headerKey="-1" headerValue="----Choose----" /></td>
				</tr>
				<tr>
					<td class="greybox" ><s:text name="voucher.fromdate"/> </td>
					<td class="greybox"><s:textfield name="fromDate" id="fromDate" maxlength="20" value="%{fromDate}"/><a href="javascript:show_calendar('forms[0].fromDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a><br/>(dd/mm/yyyy)</td>
					<td class="greybox"><s:text name="voucher.todate"/> </td>
					<td class="greybox"><s:textfield name="toDate" id="toDate" maxlength="20" value="%{toDate}"/><a href="javascript:show_calendar('forms[0].toDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
				</tr>
				<jsp:include page="voucher-filter.jsp"/>
				<tr>
					<td align="right"></td>  
					<td><s:submit method="search" value="Search" cssClass="buttonsubmit" /></td>
					<td><input type="submit" value="Close" onclick="javascript:window.close()" class="button"/></td>
				</tr>
				<s:hidden name="mode" value="%{mode}" id="mode"/>  
			</table>
			<br/>
			<div id="listid" style="display:none">
					<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
			        <tr>  
			            <th class="bluebgheadtd">Sl.No.</th>  
			            <th class="bluebgheadtd">Voucher Number</th>
			            <th class="bluebgheadtd">Type</th>
			            <th class="bluebgheadtd">Voucher Date</th>  
			            <th class="bluebgheadtd">Fund Name</th>
			            <th class="bluebgheadtd">Amount</th>
			            <th class="bluebgheadtd">Approve</th>  
			        </tr>  
			        <c:set var="trclass" value="greybox"/>
			        
				    <s:iterator var="p" value="voucherList" status="s">  
				    <tr>  
				    	<td class="<c:out value="${trclass}"/>">  
				            <s:property value="#s.index+1" />
				        </td>
						<td align="left"  class="<c:out value="${trclass}"/>">  
				            <a href="#" onclick="javascript:window.open('preApprovedVoucher!loadvoucherview.action?vhid=<s:property value='%{id}'/>','Search','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700')"><s:property value="%{vouchernumber}" /> </a> 
				        </td>
				        <td align="left"  class="<c:out value="${trclass}"/>">  
				            <s:property value="%{type}" />
				        </td>
				        <td  class="<c:out value="${trclass}"/>">  
				            <s:date name="%{voucherdate}" format="dd/MM/yyyy"/>  
				        </td>
				        <td align="left"  class="<c:out value="${trclass}"/>">  
				            <s:property value="%{fundname}" />
				        </td>
				        <td style="text-align:right"  class="<c:out value="${trclass}"/>">  
				            <s:property value="%{amount}" />
				        </td>
				        <td  class="<c:out value="${trclass}"/>">  
				            <s:checkbox name="approveList" fieldValue="%{id}" />
				           </td>
				        <c:choose>
					        <c:when test="${trclass=='greybox'}"><c:set var="trclass" value="bluebox"/></c:when>
					        <c:when test="${trclass=='bluebox'}"><c:set var="trclass" value="greybox"/></c:when>
				        </c:choose>
				    </tr>  
				    </s:iterator>
				    <s:hidden name="targetvalue" value="%{target}" id="targetvalue"/>  
				</table>  
				<table align="center" >
				<tr align="center">
				<td><s:submit method="approve" value="Approve" cssClass="buttonsubmit" /></td>
					<td><input type="button" value="Select All" onclick="javascript:selectAll()" class="button"/></td>
					<td><input type="button" value="Deselect All" onclick="javascript:deselectAll()" class="button"/></td>
				</td>
				</tr>
				</table>
			</div>
			<br/>
			<div id="msgdiv" style="display:none">
				<table align="center" class="tablebottom" width="80%">
					<tr><td class="bluebgheadtd" colspan="7">No Records Found</td></tr>   
				</table>
			</div>
			<br/>                      
			<br/>
		</s:form>  
		<script>
		
		function loadVoucherNames(type)
		{
		populatename({type:type.options[type.selectedIndex].value})	
		}
		
	   function selectAll()
	   {
	    var list=document.getElementsByName("approveList");
	    for (var i=0;i<list.length;i++)
	    {
	     list[i].checked="checked";
	    }
	    
	   }
	   	
	 function deselectAll()
	   {
	    var list=document.getElementsByName("approveList");
	    for (var i=0;i<list.length;i++)
	    {
	     list[i].checked="";
	    }
	    
	   }
	   	
			
		
		<s:if test="%{voucherList.size==0}">
				dom.get('msgdiv').style.display='block';
			</s:if>
			<s:if test="%{voucherList.size!=0}">
				dom.get('msgdiv').style.display='none';
				dom.get('listid').style.display='block';
			</s:if>	
	
		</script>
	</body>  
</html>
