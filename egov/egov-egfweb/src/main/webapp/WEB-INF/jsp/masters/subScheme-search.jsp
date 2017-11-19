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
<title><s:if test="showMode == 'edit'">
		<s:text name="subscheme.modify.title" />
	</s:if> <s:else>
		<s:text name="masters.subscheme.searchview.title" />
	</s:else></title>
</head>
<body>
	<jsp:include page="../budget/budgetHeader.jsp" />
	<s:actionmessage theme="simple" />

	<div class="formmainbox">

		<div class="subheadnew">
			<s:if test="showMode == 'edit'">
				<s:text name="subscheme.modify.title" />
			</s:if>
			<s:else>
				<s:text name="masters.subscheme.searchview.title" />
			</s:else>
		</div>

		<s:form name="subSchemeForm" action="subScheme" theme="simple">

			<s:hidden name="showMode" />

			<table align="center" width="99.5%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="greybox">&nbsp</td>
					<td class="greybox">&nbsp</td>
					<td class="greybox">&nbsp</td>
					<td class="greybox">&nbsp</td>
					<td class="greybox">&nbsp</td>
				</tr>
				<tr>
					<td></td>
					<td class="bluebox"><s:text
							name="masters.subscheme.search.fund" /><span class="mandatory1">
							*</span></td>
					<td class="bluebox"><s:select list="dropdownData.fundList"
							id="fundId" listKey="id" listValue="name" name="fundId"
							headerKey="0" headerValue="---- Choose ----"
							onchange="loadScheme(this)"></s:select> <egov:ajaxdropdown
							id="schemeId" dropdownId="schemeId" fields="['Text','Value']"
							url="voucher/common-ajaxLoadSchemes.action" selectedValue="%{id}" />
					</td>
					<td class="bluebox"><s:text
							name="masters.subscheme.search.scheme" /></td>

					<td class="bluebox"><s:select name="schemeId" id="schemeId"
							list="dropdownData.schemeList" headerKey="-1"
							headerValue="---- Choose ----" listKey="id" listValue="name"
							onchange="loadSubScheme(this)" /></td>
					<egov:ajaxdropdown id="subSchemeId" dropdownId="subSchemeId"
						fields="['Text','Value']"
						url="voucher/common-ajaxLoadSubSchemes.action"
						selectedValue="%{id}" />
				</tr>
				<tr>
					<td></td>
					<td class="greybox"><s:text name="masters.subscheme.search" />
					</td>
					<td class="greybox"><s:select name="subScheme.id"
							id="subSchemeId" list="dropdownData.subSchemeList" headerKey="-1"
							headerValue="---- Choose ----" listKey="id" listValue="name" />
					</td>
					<td class="greybox">&nbsp</td>
					<td class="greybox">&nbsp</td>
				</tr>
				<tr>
					<td class="greybox">&nbsp</td>
					<td class="greybox">&nbsp</td>
					<td class="greybox">&nbsp</td>
					<td class="greybox">&nbsp</td>
					<td class="greybox">&nbsp</td>
				</tr>
			</table>
	</div>

	<div class="buttonbottom">
		<input type="submit" class="buttonsubmit" value="Search"
			id="saveButton" name="button" onclick="return submitForm();" /> <input
			type="button" id="Close" value="Close"
			onclick="javascript:window.close()" class="button" />
	</div>

	<s:if test="%{subSchemeList.size!=0}">
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0" class="setborder" style="border-collapse: inherit;">
			<tr>
				<th class="bluebgheadtd"><s:text
						name="masters.subscheme.search.serial" /></th>
				<th class="bluebgheadtd"><s:text
						name="masters.subscheme.search.code" /></th>
				<th class="bluebgheadtd"><s:text
						name="masters.subscheme.search.name" /></th>
				<th class="bluebgheadtd"><s:text
						name="masters.subscheme.search.schemename" /></th>
				<th class="bluebgheadtd"><s:text
						name="masters.subscheme.search.fundname" /></th>
				<th class="bluebgheadtd"><s:text
						name="masters.subscheme.search.estimateamount" /></th>
				<th class="bluebgheadtd"><s:text
						name="masters.subscheme.search.isactive" /></th>

			</tr>

			<c:set var="trclass" value="greybox" />
			<s:iterator var="sub" value="subSchemeList" status="s">
				<tr>
					<td class="<c:out value='${trclass}'/>"><s:property
							value="#s.index+1" /></td>
					<td class="<c:out value='${trclass}'/>"><a href="#"
						onclick="urlLoad('<s:property value="%{id}" />','<s:property value="%{showMode}" />');"
						id="sourceLink" /> <s:label value="%{code}" /> </a></td>
					<td class="<c:out value='${trclass}'/>"><s:property
							value="name" /></td>
					<td class="<c:out value='${trclass}'/>"><s:property
							value="scheme.name" /></td>
					<td class="<c:out value='${trclass}'/>"><s:property
							value="scheme.fund.name" /></td>
					<td class="<c:out value='${trclass}'/>"><s:property
							value="initialEstimateAmount" /></td>
					<td class="<c:out value="${trclass}"/>"><s:if
							test="%{isactive==true}">Yes</s:if> <s:else>No</s:else></td>
				</tr>
				<c:choose>
					<c:when test="${trclass=='greybox'}">
						<c:set var="trclass" value="bluebox" />
					</c:when>
					<c:when test="${trclass=='bluebox'}">
						<c:set var="trclass" value="greybox" />
					</c:when>
				</c:choose>
			</s:iterator>

		</table>
	</s:if>


	<s:if test="%{subSchemeList.size==0}">
		<div id="msgdiv" style="display: block">
			<table align="center" class="tablebottom" width="80%">
				<tr>
					<th class="bluebgheadtd" colspan="7">No Records Found
					</td>
				</tr>
			</table>
		</div>

	</s:if>

	</s:form>





	<s:actionerror />
	<s:fielderror />


	<script>
		
		function loadScheme(fund)
		{
		populateschemeId({fundId:fund.value})
		}
		
		function loadSubScheme(scheme)
		{
		populatesubSchemeId({schemeId:scheme.value})
		}
		
		function urlLoad(subSchemeId,showMode)
		{
		if(showMode=='edit')
			url="../masters/subScheme-beforeEdit.action?id="+subSchemeId+"&showMode=edit";
		else
			url="../masters/subScheme-viewSubScheme.action?id="+subSchemeId;
		window.open(url,'subSchemeView','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
		}
				

	    function submitForm(){
		    if(document.getElementById("fundId").value==0)
			    {
		    	bootbox.alert("Please select Fund");
				return false;
			    }
	    	document.subSchemeForm.action='/EGF/masters/subScheme-search.action';
	    	document.subSchemeForm.submit();
	    	return true;
	    }     
		</script>
</body>
</html>
