<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>


<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<html>
<head>
<title>Create budget proposal (BE)</title>
<SCRIPT type="text/javascript">
    function onLoadTask(){
    	showMessage = '<s:property value="showMessage"/>';
    	if(showMessage == 'true' && '<s:property value="actionMessage"/>' != ''){
    		bootbox.alert('<s:property value="actionMessage"/>');
    		document.forms[0].action = "${pageContext.request.contextPath}/budget/budgetDetail.action";
			document.forms[0].submit();
    	}
    }
    </SCRIPT>
</head>
<body onload="onLoadTask()">
	<input type="hidden" id="bere" value="be" />
	<jsp:include page="budgetHeader.jsp" />
	<%@ include file='budgetDetailSetUp.jsp'%>
	<script>
			function validate(){
				anticipatory = false;
				estimate = false;
				for(i=0;i<budgetDetailsTable.getRecordSet().getLength();i++){
					if(isNaN(document.getElementById('budgetDetailList['+i+'].anticipatoryAmount').value))
						anticipatory = true;
				}				
				for(i=0;i<budgetDetailsTable.getRecordSet().getLength();i++){
					if(isNaN(document.getElementById('budgetDetailList['+i+'].originalAmount').value))
						estimate = true;
				}				
				if(estimate && anticipatory){
					bootbox.alert('Estimate amount and Anticipatory amount must be a number');
					return false;
				}else if(estimate){
					bootbox.alert('Estimate amount must be a number');
					return false;
				}else if(anticipatory){
					bootbox.alert('Anticipatory amount must be a number');
					return false;
				}
				document.budgetDetailForm.submit();
				return;
			}
		</script>
	<div class="formmainbox">
		<div class="subheadnew">Create budget proposal (BE)</div>
	</div>
	<s:actionmessage theme="simple" />
	<s:actionerror />
	<s:fielderror />
	<s:form name="budgetDetailForm" action="budgetDetail" theme="simple">
		<s:token />
		<%@ include file='budgetDetail-form.jsp'%>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			id="budgetDetailFormTable">
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td width="10%" class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="budgetdetail.budget.asOnDate" /></td>
				<td class="bluebox"><input type="text" id="asOnDate"
					name="asOnDate" style="width: 100px"
					value='<s:date name="asOnDate" format="dd/MM/yyyy"/>' /><a
					href="javascript:show_calendar('budgetDetailForm.asOnDate');"
					style="text-decoration: none">&nbsp;<img
						src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)</td>
				<td class="bluebox"><s:submit method="loadActuals"
						value="Get Actuals" cssClass="buttonsubmit" /></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		<br />
		<div class="yui-skin-sam"
			style="width: 100%; overflow-x: auto; overflow-y: hidden;">
			<div id="budgetDetailTable"></div>
		</div>
		<script>
			makeBudgetDetailTable();
			document.getElementById('budgetDetailTable').getElementsByTagName('table')[0].width = "100%";
			addGridRows();
			hideColumns();
			updateAllGridValues()
			<s:if test="%{getActionErrors().size()>0 || getFieldErrors().size()>0}">
				setValues();
			</s:if>
		</script>
		<br />
		<br />
		<div class="buttonbottom" style="padding-bottom: 10px;">
			<s:hidden name="budget" id="hidden_budget" />
			<input type="submit" value="Save" id="budgetDetail__create"
				name="method:create" onClick="javascript: return validate();"
				class="buttonsubmit" />
			<s:submit value="Close" onclick="javascript: self.close()"
				cssClass="buttonsubmit" />
		</div>
		<div id="savedDataGrid">
			<s:if test="%{savedbudgetDetailList.size()>0}">
				<%@ include file='budgetDetailList.jsp'%>
			</s:if>
		</div>
		<script>
				document.getElementById('hidden_budget').value = '<s:property value="budgetDetail.budget.id"/>'
		</script>
	</s:form>
</body>
</html>
