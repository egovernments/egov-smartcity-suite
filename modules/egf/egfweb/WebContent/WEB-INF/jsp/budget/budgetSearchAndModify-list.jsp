<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<html>  
<head>  
    <title><s:text name="budget.search.modify"/></title>
</head>  
	<body>
		<script>
			var budgetDetailsTable = null;
			if(opener != null && opener.top != null){
				opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
			}
		</script>
		<jsp:include page="budgetHeader.jsp"/>
		<div class="formmainbox"><div class="subheadnew"><s:text name="budget.search.modify"/></div>
		<s:form action="budgetSearchAndModify" theme="simple" >
		 	<s:actionmessage/>
			<%@ include file='budgetSearch-form.jsp'%>
			<div class="buttonbottom" style="padding-bottom:10px;">
				<s:hidden name="mode"/>
				<s:submit method="list" value="Search" cssClass="buttonsubmit" onclick="document.getElementById('budgetDetail_budget').disabled = false;"/>  
				<s:reset value="Cancel" cssClass="button"/>
				<s:submit value="Close" onclick="javascript:window.close()" cssClass="button"/>
			</div>
		</s:form> 
		
		<s:if test="%{!budgetList.isEmpty()}">
			<div id="detail" width="100%" >
				<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%" class="tablebottom" style="border-right:0px solid #C5C5C5;">
					<tr>
						<td colspan="9">
							<div class="subheadsmallnew"><strong><s:text name="budgetdetail.budget"/></strong></div>
						</td>
					</tr>
					<tr>
						<th class="bluebgheadtd" width="10%"><s:text name="budget.budgetname"/></th>
						<th class="bluebgheadtd" width="11%"><s:text name="budget.financialYear"/></th>
						<th class="bluebgheadtd" width="10%"><s:text name="budget.description"/></th>
					</tr>
			
					<s:iterator value="budgetList" status="stat">
						<tr> 

							<td class="blueborderfortd"> <a href='<s:url action="budgetSearchAndModify" method="modifyList"><s:param name="budget.id" value="%{id}" /><s:param name="mode" value="%{mode}" />
							</s:url>'><s:property value="name"/></a> &nbsp;</td>
							<td class="blueborderfortd"> <s:property value="financialYear.finYearRange"/>&nbsp;</td>
							<td class="blueborderfortd"> <s:property value="description"/>&nbsp;</td>
						</tr>
					</s:iterator>				
				</table>
			</div>
		</s:if>
		<s:if test='%{message != ""}'>
			<div class="error">
				<s:property value="message"/>
			</div>
		</s:if>
		<s:if test="%{disableBudget}">
				<s:hidden name="budget" id="hidden_budget"/>
		</s:if>	
		 <script>
			disable = <s:property value="disableBudget"/>;
			if(disable){
				document.getElementById('budgetDetail_budget').disabled = true;
				document.getElementById('hidden_budget').value = '<s:property value="budgetDetail.budget.id"/>';
			}
		</script>
	</body>  
</html>
