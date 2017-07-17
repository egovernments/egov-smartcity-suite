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
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html>
<head>
<script type="text/javascript">
	function populateWard() {
		populatewardId({
			zoneId : document.getElementById("zoneId").value
		});
	}

	function onSubmit() {

		if (jQuery("#assessmentNum").val() == ''
				&& jQuery("#oldMuncipalNum").val() == ''
				&& jQuery("#ownerName").val() == ''
				&& jQuery("#doorNo").val() == '') {
			bootbox.alert("Any one value is mandatory");
			return false;
		} else {
			document.assessmentform.action = '${pageContext.request.contextPath}/citizen/search/search-srchByAssessmentAndOwnerDetail.action';
			document.assessmentform.submit();
			return true;
		}		
	}
	
</script>
<title><s:text name="citizen.search.welcome"></s:text></title>
</head>
<body>
	<div class="formmainbox">
		<s:if test="%{hasErrors()}">
			<div align="left">
				<s:actionerror />
			</div>
		</s:if>
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<s:form name="assessmentform" id="assessmentform" theme="simple">
				<tr>
					<td width="100%" colspan="4" class="headingbg">
						<div class="headingbg" style="text-align: left">
							<s:text name="citizen.search.welcome" />
						</div>
					</td>
				</tr>

				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="citizen.prop.id" /> :</td>

					<td class="bluebox"><s:textfield name="assessmentNum"
							id="assessmentNum" value="%{assessmentNum}"
							cssClass="form-control patternvalidation" data-pattern="number"
							maxlength="10" /></td>
					<td class="bluebox">&nbsp;</td>
				</tr>
			
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="Old.assessmentno" /> :</td>

					<td class="bluebox"><s:textfield name="oldMuncipalNum"
							id="oldMuncipalNum" value="%{oldMuncipalNum}" maxlength="20" /></td>
					<td class="bluebox">&nbsp;</td>
				</tr>
							
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="citizen.prop.owner" /> :</td>
					<td class="bluebox"><s:textfield name="ownerName"
							id="ownerName" value="%{ownerName}" maxlength="30" /></td>
					<td class="bluebox">&nbsp;</td>
				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="citizen.prop.doorno" /> :</td>
					<td class="bluebox"><s:textfield name="doorNo" id="doorNo"
							value="%{doorNo}" maxlength="10" /></td>
					<td class="bluebox">&nbsp;</td>
				</tr>

				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox" colspan="2">
						<div class="greybox" style="text-align: center">

							<s:submit name="search" value="Search" cssClass="buttonsubmit"
								onclick="return onSubmit();"></s:submit>

						</div>
					</td>
					
				</tr>

			</s:form>
		</table>


		<div align="left"><span style="font-size: 16px; color: red"> 
			<s:text name="citizen.note" /></span>
		</div>

		</center>
	</div>
	<script
		src="<cdn:url value='/resources/global/js/egov/patternvalidation.js?rnd=${app_release_no}' context='/egi'/>"></script>
</body>
</html>
