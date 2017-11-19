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
<title><s:text name="party.search" /></title>
</head>
<body>
	<div class="formmainbox">
		<div class="subheadnew">
			<s:text name="party.search" />
		</div>

		<s:form name="partyForm" action="partyType" theme="simple">
			<s:hidden name="showMode" />
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox" width="20%"><strong><s:text
								name="party.code" /></strong></td>
					<td class="bluebox"><s:textfield id="code" name="code" /></td>
					<td class="bluebox" width="20%"><strong><s:text
								name="party.parent" /></strong></td>
					<td class="bluebox"><s:select
							list="dropdownData.partyTypeList" id="partyType.egPartytype.id"
							listKey="id" listValue="code" name="partyType.egPartytype.id"
							headerKey="" headerValue="---- Choose ----">
						</s:select></td>
				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox" width="20%"><strong><s:text
								name="party.desc" /></strong></td>
					<td class="bluebox"><s:textarea name="description"
							id="description" rows="3" cols="60" /></td>
					<td class="bluebox" width="20%">&nbsp;</td>
					<td class="bluebox">&nbsp;</td>
				</tr>
			</table>

			<div class="buttonbottom">
				<s:submit method="search" value="Search" cssClass="buttonsubmit" />
				<input type="submit" value="Close"
					onclick="javascript:window.close()" class="button" />
			</div>
	</div>
	<s:if test="%{partySearchList.size!=0}">
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0" class="tablebottom">

			<tr>

				<th class="bluebgheadtd" style="width: 2%; text-align: center"
					align="center">Sl No.</th>
				<th class="bluebgheadtd" style="width: 4%; text-align: center"
					align="center">Code</th>
				<th class="bluebgheadtd" style="width: 8%; text-align: center"
					align="center">Description</th>
				<th class="bluebgheadtd" style="width: 4%; text-align: center"
					align="center">Parent Code</th>
			</tr>
			<c:set var="trclass" value="greybox" />
			<s:iterator var="pa" value="partySearchList" status="p">
				<tr>

					<td class="<c:out value="${trclass}"/>" style="text-align: center"
						align="center"><s:property value="#p.index+1" /></td>
					<td class="<c:out value="${trclass}"/>" style="text-align: center"
						align="center"><a href="#"
						onclick="urlLoad('<s:property value="%{id}" />','<s:property value="%{showMode}" />');"
						id="sourceLink" /> <s:label value="%{code}" /> </a></td>
					<td class="<c:out value="${trclass}"/>" style="text-align: center"
						align="center"><s:property value="description" /></td>

					<td class="<c:out value="${trclass}"/>" style="text-align: center"
						align="center"><s:property value="egPartytype.code" /></td>
					<c:choose>
						<c:when test="${trclass=='greybox'}">
							<c:set var="trclass" value="bluebox" />
						</c:when>
						<c:when test="${trclass=='bluebox'}">
							<c:set var="trclass" value="greybox" />
						</c:when>
					</c:choose>
				</tr>
			</s:iterator>

		</table>
	</s:if>
	<s:if test="%{partySearchList.size==0}">
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
	<script type="text/javascript">
	function urlLoad(id,showMode) {
		if(showMode=='edit')
			 url = "../masters/partyType!beforeModify.action?id="+id+"&showMode=edit";
		else
			 url = "../masters/partyType!beforeModify.action?id="+id+"&showMode=view"; 
		window.open(url,'PartyTypeView','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
	}
	</script>
</body>
</html>
