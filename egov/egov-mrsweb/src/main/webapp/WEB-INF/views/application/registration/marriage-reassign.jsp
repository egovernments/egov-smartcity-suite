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

<!--eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
accountability and the service delivery of the government  organizations.

 Copyright (C) <2017>  eGovernments Foundation

 The updated version of eGov suite of products as by eGovernments Foundation
 is available at http://www.egovernments.org

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see http://www.gnu.org/licenses/ or
 http://www.gnu.org/licenses/gpl.html .

 In addition to the terms of the GPL license to be adhered to in using this
 program, the following additional terms are to be complied with:

     1) All versions of this program, verbatim or modified must carry this
        Legal Notice.
        Further, all user interfaces, including but not limited to citizen facing interfaces, 
        Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any 
        derived works should carry eGovernments Foundation logo on the top right corner.

        For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
        For any further queries on attribution, including queries on brand guidelines, 
        please contact contact@egovernments.org

     2) Any misrepresentation of the origin of the material is prohibited. It
        is required that all modified versions of this material be marked in
        reasonable ways as different from the original version.

     3) This license does not grant any rights to any user of the program
        with regards to rights under trademark law for use of the trade names
        or trademarks of eGovernments Foundation.

 In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.-->

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>


<div class="text-right" style="color: #ff0000">
	<strong>${message}</strong>
</div>

<div class="panel-heading">
	<div class="panel-title">
		<spring:message code="lbl.reassign.title" />
	</div>

</div>
<div class="panel-body">


	<form:form name="reassign" id="reassign" modelAttribute="reassign">
		<form:hidden path="applicationId" id="applicationId"
			name="applicationId" value="${applicationId}" />
		<form:hidden path="stateType" id="stateType" name="stateType"
			value="${stateType}" />
		<div class="row show-row" id="approverDetailHeading">
			<div class="show-row form-group">
				<label class="col-sm-3 control-label text-right"><spring:message
						code="lbl.employee.select" /><span class="mandatory"></span></label>
				<div class="col-sm-3 add-margin">
					<form:select path="" data-first-option="false"
						id="approvalPosition" name="approvalPosition"
						cssClass="form-control" cssErrorClass="form-control error"
						required="required">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${assignments}" />
					</form:select>
				</div>
			</div>
		</div>
		<div class="buttonbottom" align="center">
			<form:button disabled="false" id="ReassignSubmit"
				align="center" class="btn btn-primary" value="ReassignSubmit">
				<c:out value="Reassign" />
			</form:button>
			<a href="javascript:void(0)" class="btn btn-default"
				onclick="window.opener.closeChildWindow();"><spring:message
					code="lbl.close" /></a>
		</div>
	</form:form>
</div>

<script
	src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/js/app/reassign.js?rnd=${app_release_no}'/> "></script>