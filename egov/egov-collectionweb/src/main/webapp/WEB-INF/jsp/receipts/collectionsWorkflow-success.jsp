
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
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Collections Workflow - Success</title>
<script type="text/javascript">
function refreshInbox() {
        var x=opener.top.opener;
        if(x==null){
            x=opener.top;
        }
        x.document.getElementById('inboxframe').contentWindow.egovInbox.from = 'Inbox';
	    x.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
}
</script>
</head>
<body onLoad="refreshInbox();">

<s:form theme="simple" name="collectionsWorkflowForm">

	<div class="subheadnew"><s:if test="%{isSubmitAction == true}">
		<s:text name="collectionsWorkflow.submitSuccess" /> <s:property value="%{approverName}" />
	</s:if> <s:elseif test="%{isApproveAction == true}">
		<s:text name="collectionsWorkflow.approveSuccess" />
	</s:elseif> <s:else>
		<s:text name="collectionsWorkflow.rejectSuccess" /> <s:property value="%{approverName}" />
	</s:else></div>
	<br />
	<s:hidden name="receiptDate" value="%{receiptDate}"/>

	<div class="buttonbottom">
	<input name="buttonClose" type="button" class="buttonsubmit"
		id="buttonClose" value="Close" onclick="window.close()" />

	<s:if test="%{isSubmitAction == true}">	&nbsp;
		
	<input type="button" class="buttonsubmit" id="buttonCashReport"
			value="<s:text name='collectionsWorkflow.submit.report.cash'/>"
			onclick="window.open('${pageContext.request.contextPath}/receipts/collectionsWorkflow-submissionReportCash.action?receiptDate=<s:property value="%{receiptDate}" />', '_blank', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');"/> &nbsp;
	<input type="button" class="buttonsubmit" id="buttonCashReport"
			value="<s:text name='collectionsWorkflow.submit.report.cheque'/>"
			onclick="window.open('${pageContext.request.contextPath}/receipts/collectionsWorkflow-submissionReportCheque.action?receiptDate=<s:property value="%{receiptDate}" />', '_blank', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');"/> &nbsp;
	</s:if>
	</div>
</s:form>
</body>
</html>
