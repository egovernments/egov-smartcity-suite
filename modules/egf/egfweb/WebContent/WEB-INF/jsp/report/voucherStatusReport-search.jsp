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
    <title><s:text name="voucher.title" /></title>
    
</head>
	<body>  
		<s:form action="voucherStatusReport" name="voucherStatusReport" theme="simple" >
		<!--	<jsp:include page="../budget/budgetHeader.jsp">
	        		<jsp:param name="heading" value="Voucher Search" />
				</jsp:include>		-->
			<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
			
				<div class="formmainbox">
					<div class="subheadnew">
						<s:text name="voucher.title" />
					</div>
				</div>
			<table align="center" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<jsp:include page="../voucher/voucher-filter.jsp"/>
					                               
				</tr>                                                                          
				<tr>
				<td class="greybox" ><s:text name="voucher.type"/> </td>
				<td class="greybox"><s:select name="type" id="type" list="dropdownData.typeList" headerKey="-1" headerValue="----Choose----" onchange="loadVoucherNames(this)" /></td>
				<egov:ajaxdropdown fields="['Text','Value']" url="voucher/common!ajaxLoadVoucherNames.action" dropdownId="name" id="name"/>
				<td class="greybox" ><s:text name="voucher.name"/></td>
				<td class="greybox"><s:select name="name" id="name" list="%{nameMap}" headerKey="-1" headerValue="----Choose----" /></td>
				</tr>  
				<tr>
					<td class="bluebox" ><s:text name="voucher.fromdate"/> </td>
					<td class="bluebox"><s:textfield name="fromDate" id="fromDate" maxlength="20" onkeyup="DateFormat(this,this.value,event,false,'3')" value="%{fromDate}"/><a href="javascript:show_calendar('forms[0].fromDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a></td>
					<td class="bluebox"><s:text name="voucher.todate"/> </td>
					<td class="bluebox"><s:textfield name="toDate" id="toDate" maxlength="20" onkeyup="DateFormat(this,this.value,event,false,'3')" value="%{toDate}"/><a href="javascript:show_calendar('forms[0].toDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
				</tr>
				<tr>
					<td class="greybox">
						<s:text name="voucher.status" />
					</td>
					<td class="greybox">
						<s:select  name="status" id="status" list="%{statusMap}" headerKey="-1"
							headerValue="----Choose----" value="%{status}" />
					</td>
					<td class="greybox">
					</td>
					<td class="greybox">
					</td>
				</tr>
			</table>
			<div  class="buttonbottom">
				<s:submit method="search" value="Search"  cssClass="buttonsubmit" />
				<s:submit method="beforeSearch" value="Cancel"  cssClass="button" />
				<input type="submit" value="Close" onclick="javascript:window.close()" class="button"/>
				
			</div>
			<div id="listid" style="display:none">
					<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
			        <tr>  
			            <th class="bluebgheadtd"><s:text name="voucher.serialno" /></th> 
			            <th class="bluebgheadtd"><s:text name="voucher.department" /></th>  
			            <th class="bluebgheadtd"><s:text name="voucher.number" /></th>
			            <th class="bluebgheadtd"><s:text name="voucher.type" /></th>
			            <th class="bluebgheadtd"><s:text name="voucher.name" /></th>
			            <th class="bluebgheadtd"><s:text name="voucher.date" /></th>  
			            <th class="bluebgheadtd"><s:text name="voucher.source" /></th>
			            <th class="bluebgheadtd"><s:text name="voucher.amount" /></th>
			            <th class="bluebgheadtd"><s:text name="voucher.status" /></th>                      
			        </tr>  
			        <c:set var="trclass" value="greybox"/>
			        
				    <s:iterator var="p" value="voucherList" status="s">
				    <tr>  
				    	<td class="<c:out value="${trclass}"/>">  
				            <s:property value="#s.index+1" />
				        </td>
				        <td align="left"  class="<c:out value="${trclass}"/>">  
				            <s:property value="%{deptName}" /> 
				        </td>
						<td align="left"  class="<c:out value="${trclass}"/>">  
				            <s:property value="%{vouchernumber}" /> 
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
				            <s:property value="%{source}" />
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
				</table>
				<div class="buttonbottom">
					<s:submit method="generatePdf" value="Save As Pdf" cssClass="buttonsubmit" id="generatePdf" />
					<s:submit method="generateXls" value="Save As Xls" cssClass="buttonsubmit" id="generateXls" />
				</div>
			</div>
			<div id="msgdiv" style="display:none">
				<table align="center" class="tablebottom" width="80%">
					<tr><th class="bluebgheadtd" colspan="7"><s:text name="voucher.norecords" /></td></tr>
				</table>
			</div>
			
		</s:form>  
		
		<script>
		function loadVoucherNames(type)
		{
		populatename({type:type.options[type.selectedIndex].value})	
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
