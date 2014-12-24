<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page language="java"%>
<html>  
<head>  
    <title><s:text name="bill.view"/></title>
    <link href="common/css/budget.css" rel="stylesheet" type="text/css" />
	<link href="common/css/commonegov.css" rel="stylesheet" type="text/css" />	
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/payment.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/voucherHelper.js"></script>
	<script type="text/javascript">
		var path="${pageContext.request.contextPath}";
	</script>
	<style type="text/css">

		@media print {
		input#button1 {
		display: none;
		}
		}
		
		@media print {
		input#button2 {
		display: none;
		}
		}
		
		@media print {
		div.commontopyellowbg {
		display: none;
		}
		}
		@media print {
		div.commontopbluebg {
		display: none;
		}
		}
	</style>
</head> 
	<body >  
		<s:form action="billView" theme="simple" >  
			<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
			
			<div class="formmainbox"><div class="subheadnew"><s:property value="expendituretype"/>&nbsp<s:text name="bill.view"/></div></div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">		
				<tr>
				    <td width="18%" class="bluebox"><s:text name="billDate"/></td>
				    <td width="23%" class="bluebox">
				    	<s:date name="billdate" format="dd/MM/yyyy"/>
				    </td>
				    <td width="17%" class="bluebox"><s:text name="bill.number"/></td>
				    <td width="33%" class="bluebox"><s:property value="billnumber"/></td>
				</tr>
				<tr>
					<td class="greybox"><s:text name="bill.search.fund"/></td>
					<td class="greybox"><s:property value="egBillregistermis.fund.name"/></td>  
					<td class="greybox"><s:text name="bill.search.dept"/></td>
					<td class="greybox"><s:property value="egBillregistermis.egDepartment.deptName"/></td>
				</tr>
				<tr>
					<td class="bluebox"> <s:text name="bill.search..functionary"/></td>
					<td class="bluebox"><s:property value="egBillregistermis.functionaryid.name"/></td>
					<td class="bluebox">&nbsp</td>
					<td class="bluebox">&nbsp</td>
				</tr>
				<tr>
					<td class="greybox"> <s:text name="bill.narration"/></td>
					<td colspan="3" class="greybox"><s:property value="narration"/></td>
				</tr>
		  </table>
		  <br />
			<div align="center">
				<table border="1" width="100%" cellspacing="0">
					<tr>
						<th colspan="5"><div class="subheadsmallnew"><s:text name="bill.accountdetails"/></div></th>
					</tr>
					<tr>
						<th class="bluebgheadtd" width="17%"><s:text name="bill.function"/></th>
						<th class="bluebgheadtd" width="17%"><s:text name="bill.accountcode"/></th>
						<th class="bluebgheadtd" width="19%"><s:text name="bill.accounthead"/></th>
						<th class="bluebgheadtd" width="17%"><s:text name="bill.dtamt"/></th>
						<th class="bluebgheadtd" width="16%"><s:text name="bill.cramt"/></th>
					</tr>
					<s:iterator var="p" value="%{billDetailsList}" status="s"> 
						<tr>
							<td width="17%"  class="bluebox"><s:property value="function"/></td>
							<td width="17%"  class="bluebox"><s:property value="glcode"/></td>
							<td width="19%"  class="bluebox"><s:property value="accountHead"/></td>
							<td width="17%"  class="bluebox" style="text-align:right"><s:text name="format.number" ><s:param value="%{debitAmount}"/></s:text></td>
							<td width="16%"  class="bluebox" style="text-align:right"><s:text name="format.number" ><s:param value="%{creditAmount}"/></s:text></td>
							<c:set var="db" value="${db+debitAmount}"/>
							<c:set var="cr" value="${cr+creditAmount}"/>
						</tr>
					</s:iterator>
					<tr>
						<td class="greybox" style="text-align:right" colspan="3"/>Total</td>
						<td class="greybox" style="text-align:right"><fmt:formatNumber value="${db}" pattern="#0.00" /></td>
						<td class="greybox" style="text-align:right"><fmt:formatNumber value="${cr}" pattern="#0.00" /></td>
					</tr>
				</table>
			</div>
			<br />
			<s:if test="%{subledgerList.size()>0}">
				<div align="center">
					<table border="1" width="100%" cellspacing="0">
						<tr>
							<th colspan="5"><div class="subheadsmallnew"><s:text name="bill.subledgerdetails"/></div></th>
						</tr>
					 
						<tr>
							<th class="bluebgheadtd" width="17%"><s:text name="bill.function"/></th>
							<th class="bluebgheadtd" width="17%"><s:text name="bill.accountcode"/></th>
							<th class="bluebgheadtd" width="17%"><s:text name="bill.detailtype"/></th>
							<th class="bluebgheadtd" width="16%"><s:text name="bill.detailkey"/></th>
							<th class="bluebgheadtd" width="16%"><s:text name="bill.amount"/></th>
						</tr>
						<s:iterator var="p" value="%{subledgerList}" status="s"> 
							<tr>
								<td width="17%"  class="bluebox"><s:property value="function"/></td>
								<td width="17%"  class="bluebox"><s:property value="glcode"/></td>
								<td width="19%"  class="bluebox"><s:property value="detailcode"/></td>
								<td width="19%"  class="bluebox"><s:property value="detailkey"/></td>
								<td width="16%"  class="bluebox" style="text-align:right"><s:text name="format.number" ><s:param value="%{amount}"/></s:text></td>
							</tr>
						</s:iterator>
					</table>
				</div>
			</s:if>
			<div class="buttonbottom">
				<input name="button" type="button" class="buttonsubmit" id="button1" value="Print" onclick="window.print()"/>&nbsp;
				<input type="submit" id="button2" value="Close" onclick="javascript:window.close()" class="button"/>
			</div>
		</s:form>  
	</body>  
</html>
