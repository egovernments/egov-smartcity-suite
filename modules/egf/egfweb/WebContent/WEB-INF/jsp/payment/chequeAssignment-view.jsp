<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>  
<head>  
	<link rel="stylesheet" type="text/css" href="/EGF/cssnew/ccMenu.css"/>
    <title>Cheque Assignment View</title>
</head>
	<body>  
		<s:form action="chequeAssignment" theme="simple" >
			<jsp:include page="../budget/budgetHeader.jsp">
				<jsp:param name="heading" value="Cheque Assignment View" />
			</jsp:include>
 			<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
			<div class="formmainbox"><div class="subheadnew"><s:text name="chq.assignment.heading.view"/></div>
			<table align="center" width="100%" cellpadding="0" cellspacing="0">
				<tr>  
					<s:if test="%{paymentMode=='cheque'}">
				    	<th class="bluebgheadtdnew"><s:text name="chq.assignment.partycode"/></th>
				    </s:if>
				    <s:else>
				    	<th class="bluebgheadtdnew"><s:text name="chq.assignment.paymentvoucherno"/></th>
				    </s:else>
				    <th class="bluebgheadtdnew"><s:text name="chq.assignment.instrument.no"/></th>  
				    <th class="bluebgheadtdnew"><s:text name="chq.assignment.instrument.amount"/></th>
				    <th class="bluebgheadtdnew"><s:text name="chq.assignment.instrument.date"/></th>
				    <th class="bluebgheadtdnew"><s:text name="chq.assignment.instrument.status"/></th>
				</tr>
				<s:if test="%{paymentMode=='cheque'}">
					<s:iterator var="p" value="instHeaderList" status="s">  
		        	<tr>  
			  	  		<td style="text-align:center" class="blueborderfortdnew"><s:property value="%{payTo}" /></td>
					  	<td style="text-align:center" class="blueborderfortdnew"><s:property value="%{instrumentNumber}" /></td>
	      			  	<td style="text-align:right" class="blueborderfortdnew"><s:text name="format.number" ><s:param value="%{instrumentAmount}"/></s:text></td>
	      			  	<td style="text-align:center" class="blueborderfortdnew"><s:date name="%{instrumentDate}" format="dd/MM/yyyy"/></td>
	      			  	<td style="text-align:center" class="blueborderfortdnew"><s:property value="%{statusId.description}"/></td>
					</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<s:iterator var="p" value="instVoucherList" status="s">  
		        	<tr>  
				    	<td style="text-align:center" class="blueborderfortdnew"><s:property value="%{voucherHeaderId.voucherNumber}" /></td>
					  	<td style="text-align:center" class="blueborderfortdnew"><s:property value="%{instrumentHeaderId.instrumentNumber}" /></td>
	      			  	<td style="text-align:right" class="blueborderfortdnew"><s:property value="%{instrumentHeaderId.instrumentAmount}"/></td>
	      			  	<td style="text-align:center" class="blueborderfortdnew"><s:date name="%{instrumentHeaderId.instrumentDate}" format="dd/MM/yyyy"/></td>
	      			  	<td style="text-align:center" class="blueborderfortdnew"><s:property value="%{instrumentHeaderId.statusId.description}"/></td>
					</tr>
					</s:iterator>
				</s:else>
			</table>
			<br/>
				<div  class="buttonbottom">
				<input type="button" value="Close" onclick="javascript:window.close()" class="button"/>
			</div>
		</div>
		</s:form>
	</body>  
</html>