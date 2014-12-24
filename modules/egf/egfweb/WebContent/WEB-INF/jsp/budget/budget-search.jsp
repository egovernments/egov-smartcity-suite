<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<html>  
<head>  
    <title><s:text name="budget.search"/></title>
</head>
	<script>
		function check()
		{
			if(document.getElementById('financialYear').value==-1)
			{
				document.getElementById('errormsg').style.display='block';
				return false;
			}
			else
				return true;
		}
	</script>  
	<body>  
		<s:form action="budget" theme="simple" >
			<jsp:include page="budgetHeader.jsp">
        		<jsp:param name="heading" value="Budget Definition Search" />
			</jsp:include>
			<div class="formmainbox"><div class="subheadnew"><s:text name="budget.definition.search"/></div>
			<span id="errormsg" style="display:none" class="mandatory"><s:text name="budget.financialyear.mandatory"/></span>
			<table align="center" width="100%">
				<tr>
					<td class="bluebox" width="10%">&nbsp;</td>
					<td class="bluebox" width="30%"><s:text name="budget.financialYear"/> <span class="mandatory">*</span></td>
					<td class="bluebox"><s:select label="Financial Year" name="financialYear" id="financialYear" list="dropdownData.financialYearList" listKey="id" listValue="finYearRange" headerKey="-1" headerValue="----Choose----" value="%{budget.financialYear.id}"/></td>
				</tr>
				<br/>
			</table>
			<div align="left" class="mandatory">* <s:text name="mandatory.fields"/></div>
			<div class="buttonbottom">
				<s:submit method="list" value="Search" onclick="return check()" cssClass="buttonsubmit" />  
				<input type="submit" value="Close" onclick="javascript:window.close()" class="button"/>
			</div>
			
			<div id="listid" style="display:block">
					<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
			        <tr>  
			            <th class="bluebgheadtdnew"><s:text name="name"/></th>  
			            <th class="bluebgheadtdnew"><s:text name="budget.primarybudget"/></th>
			            <th class="bluebgheadtdnew"><s:text name="isactive"/></th>  
			            <th class="bluebgheadtdnew"><s:text name="budget.bere"/></th>
			            <th class="bluebgheadtdnew"><s:text name="budget.parent"/></th>
			            <th class="bluebgheadtdnew"><s:text name="financialyear"/></th>  
			            <th class="bluebgheadtdnew"><s:text name="status"/></th>
			        </tr>  
				    <s:iterator var="p" value="budgetList" status="1">  
				    <tr>  
						<td class="blueborderfortdnew">  
				            <a href="#" onclick="javascript:window.open('budget!edit.action?id=<s:property value='%{id}'/>','Search','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700')"><s:property value="%{name}" /> </a> 
				        </td>
				        <td class="blueborderfortdnew">  
				            <s:if test="isPrimaryBudget ==true"><s:text name="yes"/></s:if><s:else><s:text name="no"/></s:else>
				        </td>
				        <td class="blueborderfortdnew">  
				            <s:if test="isActiveBudget ==true"><s:text name="yes"/></s:if><s:else><s:text name="no"/></s:else>
				        </td>
				        <td class="blueborderfortdnew">  
				            <s:property value="%{isbere}" />  
				        </td>
				        <td class="blueborderfortdnew">  
				           <a href="budget!edit.action?id=<s:property value='%{parent.id}'/>"><s:property value="%{parent.name}" /> </a>  
				        </td>
				        <td class="blueborderfortdnew">  
				            <s:property value="%{financialYear.finYearRange}" />  
				        </td>
				        <td class="blueborderfortdnew">  
				            <s:property value="%{state.value}" />  
				        </td>
				    </tr>  
				    </s:iterator>
				    <s:hidden name="targetvalue" value="%{target}" id="targetvalue"/>  
				</table>  
			</div>
			<br/>
			<div id="msgdiv" style="display:none">
				<table align="center" width="100%">
					<tr><td colspan="7"><s:text name="no.data.found"/></td></tr>
				</table>
			</div>
			<br/>
			<br/>
		</s:form>  
		<script>
		if(dom.get('targetvalue').value=='NONE')
		{
			dom.get('listid').style.display='none';
		}	
		else if(dom.get('targetvalue').value=='EMPTY')
		{
			dom.get('listid').style.display='none';
			dom.get('msgdiv').style.display='block';
		}
		</script>
	</body>  
</html>
