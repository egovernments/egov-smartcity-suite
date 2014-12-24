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
   <STYLE type="text/css">
	.tabbertab {
		border:1px solid #CCCCCC;
		height:450px;
		margin-bottom:8px;
		overflow:scroll;
	}
   </STYLE>
</head>  
	<body>  
		<script>
			var budgetDetailsTable = null;
		</script>
		<jsp:include page="budgetHeader.jsp"/>
		<s:form action="budgetSearch" theme="simple" >
		<div class="formmainbox"><div class="subheadnew"><s:text name="budget.search"/></div>
			<%@ include file='budgetSearch-form.jsp'%>
			<div class="buttonbottom" style="padding-bottom:10px;">
				<s:submit method="groupedBudgets" value="Search" cssClass="buttonsubmit"/>  
				<s:reset value="Cancel" cssClass="button"/>
				<s:submit value="Close" onclick="javascript:window.close()" cssClass="button"/>
			</div>
		</s:form> 
		
		<s:if test="%{!budgetList.isEmpty()}">
			<div id="detail">
				<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%" class="tablebottom" style="border-right:0px solid #C5C5C5;">
					<tr>
						<td colspan="9">
							<div class="subheadsmallnew"><strong>Budget</strong></div>
						</td>
					</tr>
					<tr>
						<th class="bluebgheadtd" width="10%"><s:text name="budget.budgetname"/></th>
						<th class="bluebgheadtd" width="11%"><s:text name="budget.parent"/></th>
						<th class="bluebgheadtd" width="10%"><s:text name="budget.description"/></th>
					</tr>
					<s:iterator value="budgetList" status="stat">
						<tr> 
							<td class="blueborderfortd"> <a href='<s:url action="budgetSearch" method="groupedBudgetDetailList">
							<s:param name="budget.id" value="%{id}" />
							<s:param name="skipPrepare" value="true"/>
							</s:url>'><s:property value="name"/></a> &nbsp;</td>
							<td class="blueborderfortd"> <s:property value="parent.name"/>&nbsp;</td>
							<td class="blueborderfortd"> <s:property value="description"/>&nbsp;</td>
						</tr>
					</s:iterator>				
				</table>
			</div>
		</s:if>
		<s:elseif test="%{!errorMessage || budgetDetail.budget==null}"></s:elseif>
		<s:else>
			<div class="error">
				<s:text name="budget.no.details.found"/>
			</div>
		</s:else>
	</body>  
</html>
