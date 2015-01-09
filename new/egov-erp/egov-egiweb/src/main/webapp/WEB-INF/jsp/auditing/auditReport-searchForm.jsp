<%@page import="org.egov.infstr.auditing.model.AuditEvent"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html>
	<head>
		<title>Audit Search</title>
		<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/css/egov.css'/>" />
		<script type="text/javascript">
			function onBodyLoad() {
				populateEntities();
			}

			function populateEntities() {
				populateentityId({
					moduleName : document.getElementById("moduleName").value
				});
			}
			
			function checkBeforeSubmit() {
				var fromDate = document.getElementById("fDate").value;
				var toDate = document.getElementById("tDate").value;
				var moduleName = document.getElementById("moduleName").value;
				var userName = document.getElementById("userName").value;

				if ((moduleName == null || moduleName == "" || moduleName == "-1")
						&& (userName == null || userName == "") 
						&& (fromDate == null || fromDate == "" || fromDate == 'DD/MM/YYYY')
						&& (toDate == null || toDate == "" || toDate == 'DD/MM/YYYY')) {
					alert('Please Select atleast anyone of the Search criteria');
					return false;
				} else if (fromDate != null && fromDate != "") {
					if (toDate != null && toDate != "") {
							if(validateFromAndToDate(fromDate,toDate)) {
								return true;
							} else {
								return false;
							}
					} else {
						alert('To Date is mandatory when From Date is entered');
						return false;
					}
				} else if (toDate != null && toDate != "") {
					alert('From Date is mandatory when To Date is entered');
					return false;
				} else {
					return true;
				}
			}
		</script>
		<style type="text/css">
			table.its thead th {position:relative;display:table-cell;z-index: 0;}
		</style>
		<script type="text/javascript" src="../commonjs/ajaxCommonFunctions.js"></script>
		<script type="text/javascript" src="../javascript/jquery/helper.js"></script>
	</head>
	<body onload="onBodyLoad();">
		<div align="left">
  			<s:actionerror/>
  		</div>
		<s:form action="auditReport!search.action" theme="simple">
			<table align="center" class="tableStyle" width="99%">
				<tr>
					<td colspan='4'>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="tableheader" valign="middle" align="center" colspan="2" height="26">
						Audit Search
					</td>
				</tr>
			</table>
			<table width="99%" align="center" class="tableStyle">
				<tr>
					<td colspan='7'>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="labelcell" width="5%">&nbsp;</td>
					<td class="labelcell">Module : </td>
					<td class="labelcell">
						<s:select list="moduleMap" value="%{defaultModule}" listKey="key" listValue="value" 
							id="moduleName" name="moduleName" onchange="return populateEntities();"/> 
					</td>
					<td class="labelcell">&nbsp;</td>
					<td class="labelcell">Entity : </td>
					<egovtags:ajaxdropdown id="entityId" fields="['Text','Value']" 
						dropdownId="entityId" url="auditing/auditReport!populateEntities.action" />
					<td class="labelcell">
						<s:select name="entityName" id="entityId" listKey="key" listValue="value"
						list="dropdownData.entityList" headerKey="-1" headerValue="--Choose--"
						value="%{entityName}" />
					</td>
					<td class="labelcell">&nbsp;</td>
				</tr>
				<tr>
					<td class="labelcell" width="5%">&nbsp;</td>
					<td class="labelcell">From Date (dd/MM/yyyy) :</td>
					<td class="labelcell">
						<s:date name="fromDate" var="fDate" format="dd/MM/yyyy"/>
						<s:textfield  name="fromDate" id="fDate" maxlength="10" 
							onkeyup="DateFormat(this,this.value,event,false,'3')" 
       						onblur="validateDateFormat(this);" value="%{fDate}"/>
       				</td>
       				<td class="labelcell" width="5%">&nbsp;</td>
       				<td class="labelcell">To Date (dd/MM/yyyy) :</td>
					<td class="labelcell">
						<s:date name="toDate" var="tDate" format="dd/MM/yyyy"/>
						<s:textfield  name="toDate" id="tDate" maxlength="10" 
							onkeyup="DateFormat(this,this.value,event,false,'3')"
        					onblur="validateDateFormat(this);" value="%{tDate}"/>
					</td>
					<td class="labelcell">&nbsp;</td>
				</tr>
				<tr>
					<td class="labelcell" width="5%">&nbsp;</td>
					<td class="labelcell">
						UserName : 
					</td>
					<td class="labelcell">
						<div id="userName_autocomplete" class="autoComContainer" style="width: 100%">
							<div>
								<s:textfield id="userName" name="userName" />
							</div>
							<span id="userNameResults"></span>
						</div> <egovtags:autocomplete name="userName" width="30" maxResults="8"
							field="userName"
							url="${pageContext.request.contextPath}/auditing/auditReport!ajaxUserNames.action"
							queryQuestionMark="true" results="userNameResults"
							handler="userNameSelectionHandler"
							forceSelectionHandler="userNameSelectionEnforceHandler" /> <span
						class='warning' id="improperuserNameSelectionWarning"></span>
					</td>
					<td class="labelcell" colspan="4">&nbsp;</td>				
				</tr>
				<tr>
					<td colspan='7'>
						&nbsp;
					</td>
				</tr>
			</table>
			<table align="center" width="99%">
				<tr>
					<td class="button2" align="center">
						<s:submit  cssClass="buttonsubmit" id="button32" name="Search" value="Search" onclick="return checkBeforeSubmit();" method="search" />
						<input type="button" cssClass="buttonsubmit" name="button2" id="button2" value="Close" onclick="window.close();"/>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
			</table>
			<s:if test="target=='searchResult'">
				<div class="bold">Search Criteria::<s:property value="%{searchValue}"/></div>
				<div class="bold">Total Results:<s:property value="%{searchResult.fullListSize}"/></div>
			</s:if>
			<logic:notEmpty name="searchResult">
				<display:table style="color:black;thead:block;width:99%" name="searchResult" export="true" 
					uid="currentRowObject" class="its" id="its" cellspacing="0" cellpadding="0" requestURI="">
					<display:caption style="text-align:center;">
						<div class="tablesubcaption1">Audit Report</div>
					</display:caption>
					<display:column property="bizId" title="BUSINESS ID" style="border-bottom:1px solid black;text-align:center"/>
					<display:column title="EVENT DATE" style="border-bottom:1px solid black;text-align:center">
						<fmt:formatDate value="${its.eventDate}" pattern="dd/MM/yyyy hh:mm:ss a"/>
					</display:column>
					<display:column property="auditModule.moduleName" title="MODULE NAME" style="border-bottom:1px solid black;text-align:center"/>
					<display:column property="auditEntity.entityName" title="ENTITY NAME" style="border-bottom:1px solid black;text-align:center"/>
					<display:column property="userName" title="USER NAME" style="border-bottom:1px solid black;text-align:center"/>
					<display:column property="action" title="ACTION" style="border-bottom:1px solid black;text-align:center"/>
					<display:column property="details1" title="DETAILS1" style="border-bottom:1px solid black;text-align:left "/>
					<display:column title="DETAILS2" style="border-bottom:1px solid black;border-right:1px solid black;text-align:left">
						<%=
							(((AuditEvent)its).getDetails2() == null) ? "&nbsp;" : ((AuditEvent)its).getDetails2()
						%>
					</display:column>
				</display:table>
			</logic:notEmpty>
			<logic:empty name="searchResult">
				<s:if test="target=='searchResult'">
						<div class="bold">No Records found..</div>
				</s:if>
			</logic:empty>
								
		</s:form>
	</body>
</html>