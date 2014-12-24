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
				<s:hidden name="mode" value="%{mode}" id="mode"/>  
			</table>
			<div  class="buttonbottom">
				<s:submit method="search" value="Search" onclick="return validate()" cssClass="buttonsubmit" />
				<input type="submit" value="Close" onclick="javascript:window.close()" class="button"/>
			</div>
			<br/>
			<div id="listid" style="display:none">
					<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
			        <tr>  
			            <th class="bluebgheadtd">Sl.No.</th>  
			            <th class="bluebgheadtd">Voucher Number</th>
			            <th class="bluebgheadtd">Type</th>
			            <th class="bluebgheadtd">Name</th>
			            <th class="bluebgheadtd">Voucher Date</th>  
			            <th class="bluebgheadtd">Fund Name</th>
			            <th class="bluebgheadtd">Amount(Rs)</th>
			            <th class="bluebgheadtd">Status</th>  
			        </tr>  
			        <c:set var="trclass" value="greybox"/>
			        
				    <s:iterator var="p" value="voucherList" status="s">  
				    <tr>  
				    	<td class="<c:out value="${trclass}"/>">  
				            <s:property value="#s.index+1" />
				        </td>
						<td align="left"  class="<c:out value="${trclass}"/>">  
				            <a href="#" onclick="openVoucher(<s:property value='%{id}'/>,'<s:text name="%{name}.%{showMode}" />' );"><s:property value="%{vouchernumber}" /> </a> 
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
				        <td style="text-align:right"  class="<c:out value="${trclass}"/>">  
				            <s:text name="format.number" ><s:param value="%{amount}"/></s:text>
				        </td>
				        <td  class="<c:out value="${trclass}"/>">  
				      	    <s:text name="%{status}" />
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
		
		function loadVoucherNames(type)
		{
		populatename({type:type.options[type.selectedIndex].value})	
		}
		function openVoucher(vid,url){
		var showMode = document.getElementById('showMode').value ;
		if(showMode=='nonbillPayment')
		{
		url="../payment/directBankPayment!nonBillPayment.action?showMode="+showMode+"&voucherHeader.id="+vid;
		window.open(url,'Search','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
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