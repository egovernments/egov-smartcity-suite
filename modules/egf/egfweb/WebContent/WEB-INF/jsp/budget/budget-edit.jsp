<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<html>  
<head>  
    <title><s:text name="budget.modify"/></title>
   
  	</head>
  	<script>
  		function setTarget(){
  			document.getElementById('target').value='edit';
  			return true;
  		}
  		
  	</script>
	<body>  
		<s:form action="budget" theme="simple" > 
		<s:token/>
			<jsp:include page="budgetHeader.jsp"/>
	<div class="formmainbox"><div class="subheadnew"><s:text name="budget.modify"/></div>
			<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
				<%@ include file='budget-form.jsp'%>
				<td><div align="left" class="mandatory">* <s:text name="mandatory.fields"/></div></td>
				<s:hidden  name="model.id" id="tempId"/>
				<s:hidden  name="model.state.id" value="%{model.state.id}"/>
				<div class="buttonbottom">
					<s:submit method="save" value="Save " cssClass="buttonsubmit" onclick="return setTarget()" />  </label>
					<s:reset name="button" type="submit" cssClass="button" id="button" value="Cancel"/>
					<label><input type="submit" value="Close" onclick="javascript:window.close()" class="button"/></label>
				</div>
			</div>
		</s:form> 
		<s:hidden name="target" value="%{target}" id="target"/>
		<script>
		if(dom.get('target').value=='NotAllowToEdit')
		{
			dom.get('name').readOnly=true;
			dom.get('budget_isbereBE').readOnly=true;
			dom.get('budget_isbereRE').readOnly=true;
			dom.get('financialYear').readOnly=true;
			dom.get('parentId').readOnly=true;
			dom.get('isPrimaryBudget').readOnly=true;
		}
		
  		handleReferenceBudgets();
		
		</script>  
	</body>  
	
</html>
