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
<title>Budget Definition</title>
</head>
<body>
	<s:form action="budget" theme="simple">
		<s:token />
		<jsp:include page="budgetHeader.jsp">
			<jsp:param name="heading" value="Budget Definition Create" />
		</jsp:include>
		<span class="error-msg"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
		</span>
		<div class="formmainbox">
			<div class="subheadnew">Budget Definition Create</div>
			<%@ include file='budget-form.jsp'%>
			<div class="buttonbottom">
				<label></label> <label><s:submit method="create"
						value="Save " cssClass="buttonsubmit" /> </label>
				<s:reset name="button" type="submit" cssClass="button" id="button"
					value="Cancel" />
				<label><input type="submit" value="Close"
					onclick="javascript:window.close()" class="button" /></label>
			</div>
		</div>

	</s:form>

	<s:hidden name="targetvalue" value="%{target}" id="targetvalue" />
	<script>
			if(dom.get('targetvalue').value=='SUCCESS')
			{
				document.forms[0].name.value="";
				document.forms[0].description.value="";
				document.forms[0].financialYear.value=-1;
				document.forms[0].parentId.value=-1;
				document.forms[0].isActiveBudget.checked=false;
				document.forms[0].isPrimaryBudget.checked=false;
				document.forms[0].budget_isbereBE.checked=true;
			}	
		</script>
</body>
</html>
