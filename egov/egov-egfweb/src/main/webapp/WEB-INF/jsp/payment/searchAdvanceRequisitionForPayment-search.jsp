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


<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<script type="text/javascript"
	src="<egov:url prefix='/egi'  path='/commonyui/yui2.7/autocomplete/autocomplete-min.js'/>"></script>
<link rel="stylesheet" type="text/css"
	href="<egov:url prefix='/egi'  path='/commonyui/yui2.7/assets/skins/sam/autocomplete.css'/>" />
<html>
<head>
<title><s:text name='arf.search.title' /></title>
</head>

<script type="text/javascript">
function validateAndSubmit(){
	   if(dom.get('fromDate').value == "" && dom.get('toDate').value == ""
		   && dom.get('departmentId').value == -1 && dom.get('arfNumberSearch').value == "") {
	   		dom.get("searchAdvanceRequisition_error").innerHTML='<s:text name="arf.validation.select.one.search.creteria"/>'
			dom.get("searchAdvanceRequisition_error").style.display='';
        	return false;
	  }
	   else {
			dom.get("searchAdvanceRequisition_error").innerHTML='';
			dom.get("searchAdvanceRequisition_error").style.display="none";
		}
}

var arfNumberSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};

</script>

<body>
	<div class="error-block" id="searchAdvanceRequisition_error"
		style="display: none; color: red;"></div>
	<s:if test="%{hasErrors()}">
		<div class="error-block" style="color: red; align: left">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:form name="searchAdvanceRequisitionForPayment"
		id="searchAdvanceRequisitionForPayment" theme="simple"
		onsubmit="return validateAndSubmit(); ">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param value="Search Advance Requisition" name="heading" />
		</jsp:include>
		<div class="formmainbox">
			<div class="formheading" />
			<div class="subheadnew">
				<s:text name="arf.search.title" />
			</div>
		</div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td width="10%" class="bluebox"></td>
							<td class="bluebox"><s:text name="arf.fromdate" /></td>
							<td class="bluebox"><s:date name="fromDate"
									var="fromDateFormat" format="dd/MM/yyyy" /> <s:textfield
									name="fromDate" id="fromDate" cssClass="selectwk"
									value="%{fromDateFormat}" onfocus="javascript:vDateType='3';"
									onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
								href="javascript:show_calendar('forms[0].fromDate',null,null,'DD/MM/YYYY');"
								onmouseover="window.status='Date Picker';return true;"
								onmouseout="window.status='';return true;"> <img
									src="/egi/resources/erp2/images/calendaricon.gif"
									alt="Calendar" width="16" height="16" border="0"
									align="absmiddle" />
							</a></td>
							<td class="bluebox"><s:text name="arf.todate" /></td>
							<td class="bluebox"><s:date name="toDate" var="toDateFormat"
									format="dd/MM/yyyy" /> <s:textfield name="toDate" id="toDate"
									value="%{toDateFormat}" cssClass="selectwk"
									onfocus="javascript:vDateType='3';"
									onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
								href="javascript:show_calendar('forms[0].toDate',null,null,'DD/MM/YYYY');"
								onmouseover="window.status='Date Picker';return true;"
								onmouseout="window.status='';return true;"> <img
									src="/egi/resources/erp2/images/calendaricon.gif"
									alt="Calendar" width="16" height="16" border="0"
									align="absmiddle" />
							</a></td>
						</tr>

						<tr>
							<td width="10%" class="greybox"></td>
							<td class="greybox"><s:text name="arf.department" /></td>
							<td class="greybox"><s:select headerKey="-1"
									headerValue="%{getText('default.dropdown.select')}"
									name="departmentId" id="departmentId" cssClass="selectwk"
									list="dropdownData.departmentList" listKey="id"
									listValue='deptName' value="%{departmentId}" /></td>
							<td class="greybox"><s:text name="arf.arfnumber" /></td>
							<td class="greybox">
								<div class="yui-skin-sam">
									<div id="arfNumberSearch_autocomplete">
										<div>
											<s:textfield id="arfNumberSearch" name="arfNumber"
												value="%{arfNumber}" cssClass="selectwk" />
										</div>
										<span id="arfNumberSearchResults"></span>
									</div>
								</div> <egov:autocomplete name="arfNumberSearch" width="20"
									field="arfNumberSearch"
									url="../voucher/common!searchARFNumbers.action?"
									queryQuestionMark="false" results="arfNumberSearchResults"
									handler="arfNumberSearchSelectionHandler" queryLength="3" />
							</td>
						</tr>
						</tr>
						<tr>

						</tr>
						<tr>
							<td colspan="5" class="shadowwk"></td>
						</tr>

						<tr>
							<td colspan="5">
								<div class="buttonbottom" align="center">
									<s:submit cssClass="buttonsubmit" value="SEARCH"
										id="saveButton" name="button"
										onclick="return validateAndSubmit();" method="searchList" />
									<input type="button" class="buttonsubmit" value="CLOSE"
										id="closeButton" name="button" onclick="window.close();" />
							</td>
						</tr>

					</table>
				</td>
			</tr>
		</table>
		</div>
		<!-- end of formmainbox -->
		<%@ include file='searchAdvanceRequisitionForPayment-result.jsp'%>
	</s:form>
</body>
</html>
