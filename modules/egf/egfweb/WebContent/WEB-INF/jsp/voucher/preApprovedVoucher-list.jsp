<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<html>  
<head>  
    <title>Generate PJV</title>
</head>
	<body>  
		<s:form action="preApprovedVoucher" theme="simple" >
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="Generate PJV" />
			</jsp:include>
			<span class="mandatory">
				<s:actionerror/>  
			</span>
			<div class="formmainbox"><div class="subheadnew">Generate PJV</div>
			<br/>
			<div align="left">
				<s:if test="%{shouldShowHeaderField('department')}"><strong><s:text name="voucher.department"/> : </strong>
				<s:property value="%{getCurrentDepartment().deptName}"/></s:if>
			</div>
			<br/>
			<div id="listid" style="display:block">
					<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
			        <tr>  
			        	<th class="bluebgheadtd">Sl. No.</th>
			            <th class="bluebgheadtd">Bill Number</th>  
			            <th class="bluebgheadtd">Bill Date</th>
			            <th class="bluebgheadtd">Bill Amount</th>  
			            <th class="bluebgheadtd">Passed Amount</th>
			            <th class="bluebgheadtd">Expenditure Type</th>  
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
				            <s:property value="%{billamount}" />  
				        </td>
				        <td  style="text-align:right">  
				            <s:property value="%{passedamount}" />  
				        </td>
				        <td>  
				            <s:property value="%{expendituretype}" />  
				        </td>
				    </tr>  
				    </s:iterator>
				</table>  
			</div>
			<div  class="buttonbottom" id="buttondiv">
				<input type="submit" value="Close" onclick="javascript:window.close()" class="button"/></td>
			</div>
		</s:form>  
	</body>  
</html>
