<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/contra.js"></script>

<html>  
<head>  
    <title>Payment Search</title>
</head>
	<body>  
		<s:form action="paymentReversal" theme="simple" >
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="Voucher Search" />
			</jsp:include>
			<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
			<div class="formmainbox"><div class="subheadnew">Payment Search</div>
			<table align="center" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="greybox" ><s:text name="voucher.mode.of.payment"/> </td>
					<td class="greybox"><s:select name="name" id="voucherName" list="dropdownData.voucherNameList" headerKey="-1" headerValue="----Choose----"/></td>
					<td class="greybox" ><s:text name="voucher.number"/> </td>
					<td class="greybox"><s:textfield name="voucherNumber" id="voucherNumber" maxlength="25" value="%{voucherNumber}"/></td>
				</tr>
				<jsp:include page="../voucher/voucher-filter.jsp"/>
				<tr>
					<td class="bluebox" ><s:text name="voucher.fromdate"/><span class="mandatory">*</span> </td>
					<td class="bluebox"><input type="text"  id="fromDate" name="fromDate" style="width:100px" value='<s:date name="fromDate" format="dd/MM/yyyy"/>' onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('forms[0].fromDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
					<td class="bluebox"><s:text name="voucher.todate"/><span class="mandatory">*</span> </td>
					<td class="bluebox"><input type="text"  id="toDate" name="toDate" style="width:100px" value='<s:date name="toDate" format="dd/MM/yyyy"/>' onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('forms[0].toDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
				</tr>
				
				<tr>
		  			<td class="greybox" width="30%"><s:text name="payin.bank"/></td>
		  			<td class="greybox"><s:select name="bankBranch" id="bankId" list="dropdownData.bankList" listKey="bankBranchId" listValue="bankBranchName" headerKey="-1" headerValue="----Choose----" onChange="populateAccNum(this);"  /></td>
 					<egov:ajaxdropdown id="accountNumber" fields="['Text','Value']" dropdownId="accountNumber" url="voucher/common!ajaxLoadAccNum.action" />
					<td class="greybox"><s:text name="payin.accountNum"/></td>
					<td class="greybox"><s:select  name="bankAccount" id="accountNumber" list="dropdownData.accNumList" listKey="id" listValue="accountnumber" headerKey="-1" headerValue="----Choose----" /></td>
				</tr>
				
			</table>
			<div  class="buttonbottom">
				<s:submit method="searchVouchersForReversal" value="Search" cssClass="buttonsubmit" onclick="return validate();"/>
				<input type="submit" value="Close" onclick="javascript:window.close()" class="button"/>
			</div>
			<br/>
			<div id="listid" style="display:none">
					<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
			        <tr>  
			            <th class="bluebgheadtd">Sl.No.</th>  
			            <th class="bluebgheadtd">Voucher Number</th>
			            <th class="bluebgheadtd">Voucher Type</th>
			            <th class="bluebgheadtd">Voucher Name</th>  
			            <th class="bluebgheadtd">Voucher Date</th>  
			            <th class="bluebgheadtd">Fund Name</th>
			            <th class="bluebgheadtd">Amount</th>
			            <th class="bluebgheadtd">Status</th>  
			        </tr>  
			        <c:set var="trclass" value="greybox"/>
			        
				    <s:iterator var="p" value="paymentHeaderList" status="s">  
				    <tr>  
				    	<td class="<c:out value="${trclass}"/>">  
				            <s:property value="#s.index+1" />
				        </td>
						<td align="left"  class="<c:out value="${trclass}"/>">  
						
						<s:if test="%{voucherheader.name=='Direct Bank Payment'}">
						  <a href="#" onclick="openVoucher(<s:property value='%{voucherheader.id}'/>,'directBankPayment!beforeReverse.action?voucherHeader.id' );"><s:property value="%{voucherheader.voucherNumber}" /> </a>	
							</s:if>
					        <s:else>
					       <a href="#" onclick="openVoucher(<s:property value='%{id}'/>,'paymentReversal!reverse.action?paymentHeader.id' );"><s:property value="%{voucherheader.voucherNumber}" /> </a>
					        </s:else>
				             
				        </td>
				        <td align="left"  class="<c:out value="${trclass}"/>">  
				            <s:property value="%{voucherheader.type}" />
				        </td>
				        <td  class="<c:out value="${trclass}"/>">  
				            <s:property value="%{voucherheader.name}"/>  
				        </td>
				        <td  class="<c:out value="${trclass}"/>">  
				            <s:date name="%{voucherheader.voucherDate}" format="dd/MM/yyyy"/>  
				        </td>
				        <td align="left"  class="<c:out value="${trclass}"/>">  
				            <s:property value="%{voucherheader.fundId.name}" />
				        </td>
				        <td style="text-align:right"  class="<c:out value="${trclass}"/>">  
				           <s:text name="format.number" ><s:param value="%{voucherheader.totalAmount}"/></s:text>
				        </td>
				        <td  class="<c:out value="${trclass}"/>">  
				        	<s:if test="%{voucherheader.isConfirmed == 0}">UnConfirmed</s:if>
				        	<s:else>Confirmed</s:else>
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
		function openVoucher(pid,url){
			var url =  url+'='+ pid;
			window.open(url,'Search','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
		}
 		   <s:if test="%{paymentHeaderList.size==0 and message!=''}">
				dom.get('msgdiv').style.display='block';
			</s:if>
			<s:if test="%{paymentHeaderList.size!=0}">
				dom.get('msgdiv').style.display='none';
				dom.get('listid').style.display='block';
			</s:if>	
	function validate(){
		var fromDate = document.getElementById('fromDate').value;
		var toDate = document.getElementById('toDate').value;
		if(fromDate == ''){
			alert('Select From Date');
			return false;
		}
		if(toDate == ''){
			alert('Select To Date');
			return false;
		}
		return true;
	}
		</script>
	</body>  
</html>