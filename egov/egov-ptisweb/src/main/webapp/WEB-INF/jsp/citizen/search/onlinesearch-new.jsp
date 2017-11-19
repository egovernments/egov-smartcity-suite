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
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html>
<head>
<sx:head />
<script type="text/javascript">
	function onSubmit() {

		if (jQuery("#assessmentNum").val() == '') {
			bootbox.alert("Assessment number is mandatory");
			return false;
		} else {
			document.assessmentform.action = '${pageContext.request.contextPath}/citizen/search/search-searchByAssessment.action';
			document.assessmentform.submit();
			return true;
		}
	}
</script>
<title><s:text name="citizenPaytax.title"></s:text></title>
</head>
<body>
	<div class="formmainbox">
		<s:if test="%{hasErrors()}">
			<div class="errorstyle" id="property_error_area">
			
				<div class="errortext"><span style="font-size: 12px; color: red;"> 
				<s:actionerror />
				<s:fielderror />
				</div>
			</div>
		</s:if>
		
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<s:form name="assessmentform" id="assessmentform" theme="simple">
				<tr>
					<td width="100%" colspan="4" class="headingbg">
						<div class="headingbg" style="text-align: left">
							<s:text name="citizen.paytax.welcome" />
						</div>
					</td>
				</tr>

				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="citizen.prop.id" /><span
						class="mandatory1">*</span> :</td>

					<td class="bluebox"><s:textfield name="assessmentNum"
							id="assessmentNum" value="%{assessmentNum}"
							cssClass="form-control patternvalidation" data-pattern="number"
							maxlength="10" /></td>
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

		</center>
	</div>
	<script
		src="<cdn:url value='/resources/global/js/egov/patternvalidation.js?rnd=${app_release_no}' context='/egi'/>"></script>
</body>
</html>
