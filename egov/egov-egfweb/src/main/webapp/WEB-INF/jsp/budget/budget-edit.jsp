<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>


<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<html>
<head>
<title><s:text name="budget.modify" /></title>

</head>
<script>
  		function setTarget(){
  			document.getElementById('target').value='edit';
  			return true;
  		}
  		
  	</script>
<body>
	<s:form action="budget" theme="simple">
		<s:token />
		<jsp:include page="budgetHeader.jsp" />
		<div class="formmainbox">
			<div class="subheadnew">
				<s:text name="budget.modify" />
			</div>
			<span class="mandatory"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
			</span>
			<%@ include file='budget-form.jsp'%>
			<td><div align="left" class="mandatory">
					*
					<s:text name="mandatory.fields" />
				</div></td>
			<s:hidden name="model.id" id="tempId" />
			<s:hidden name="model.state.id" value="%{model.state.id}" />
			<div class="buttonbottom">
				<s:submit method="save" value="Save " cssClass="buttonsubmit"
					onclick="return setTarget()" />
				</label>
				<s:reset name="button" type="submit" cssClass="button" id="button"
					value="Cancel" />
				<label><input type="submit" value="Close"
					onclick="javascript:window.close()" class="button" /></label>
			</div>
		</div>
	</s:form>
	<s:hidden name="target" value="%{target}" id="target" />
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
