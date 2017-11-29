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

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<div class="row">
	<div class="col-md-12">
		<form:form role="form" action="" method="post"
			modelAttribute="bpaApplication" id="documentscrutinyform"
			cssClass="form-horizontal form-groups-bordered"
			enctype="multipart/form-data">

			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-body custom-form ">
					<jsp:include page="viewapplication-details.jsp"></jsp:include>
				</div>
				<div class="panel-heading custom_form_panel_heading">
					<div class="panel-title">
						<spring:message code="lbl.document.scrutiny" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label text-right"><spring:message code="lbl.plotsurvey.number" /><span class="mandatory"></label>
					<div class="col-sm-3 add-margin">
						<form:hidden path="documentScrutiny[0].application" 
							id="scrutinyapplicationid" value="${bpaApplication.id}" />
						<form:hidden path="documentScrutiny[0].verifiedBy"
							id="verifiedById" value="${loginUser.id}" />

						<form:input class="form-control patternvalidation" maxlength="20" data-pattern="alphanumeric"  required="required"
							id="plotsurveynumber" path="documentScrutiny[0].plotsurveynumber" value="${bpaApplication.siteDetail[0].plotsurveynumber}"/>
						<form:errors path="documentScrutiny[0].plotsurveynumber"
							cssClass="add-margin error-msg" />
					</div>

					<label class="col-sm-2 control-label text-right"><spring:message code="lbl.subdivision.number" /></label>
					<div class="col-sm-2 add-margin">
						<form:input class="form-control patternvalidation" maxlength="12" data-pattern="alphanumeric" 
							id="subdivisionNumber"
							path="documentScrutiny[0].subdivisionNumber"  value="${bpaApplication.siteDetail[0].subdivisionNumber}"/>
						<form:errors path="documentScrutiny[0].subdivisionNumber"
							cssClass="add-margin error-msg" />
					</div>
				</div>

				<div class="form-group">
					

					<label class="col-sm-3 control-label text-right"><spring:message code="lbl.dimensionofplot" /></label>
					<div class="col-sm-3 add-margin">
						<form:radiobutton
							path="documentScrutiny[0].isBoundaryDrawingSubmitted"
							value="true" />
						<spring:message code="lbl.yes" />
						<form:radiobutton
							path="documentScrutiny[0].isBoundaryDrawingSubmitted"
							value="false"  checked="checked" />
						<spring:message code="lbl.no" />
						<form:errors path="documentScrutiny[0].isBoundaryDrawingSubmitted"
							cssClass="add-margin error-msg" />
					</div>
					<label class="col-sm-2 control-label text-right"><spring:message code="lbl.extent.of.land" /><span class="mandatory"> </label>
					<div class="col-sm-2 add-margin">
						<form:input class="form-control patternvalidation" maxlength="10" data-pattern="number" required="required"
							id="extentinsqmts" path="documentScrutiny[0].extentinsqmts"  value="${bpaApplication.siteDetail[0].extentinsqmts}"/>
						<form:errors path="documentScrutiny[0].extentinsqmts"
							cssClass="add-margin error-msg" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label text-right"><spring:message code="lbl.detailof.neigbour" /></label>
					<div class="col-sm-3 add-margin">
						<form:radiobutton
							path="documentScrutiny[0].neighoutOwnerDtlSubmitted"
							value="true" checked="checked" />
						<spring:message code="lbl.yes" />
						<form:radiobutton
							path="documentScrutiny[0].neighoutOwnerDtlSubmitted"
							value="false" />
						<spring:message code="lbl.no" />
						<form:errors path="documentScrutiny[0].neighoutOwnerDtlSubmitted"
							cssClass="add-margin error-msg" />
					</div>

					<label class="col-sm-2 control-label text-right"><spring:message code="lbl.nature.of.ownership" /></label>
					<div class="col-sm-2 add-margin">
						<form:input class="form-control patternvalidation" maxlength="120" data-pattern="alphanumeric" 
							id="natureofOwnership"
							path="documentScrutiny[0].natureofOwnership"  value="${bpaApplication.siteDetail[0].natureofOwnership}"/>
						<form:errors path="documentScrutiny[0].natureofOwnership"
							cssClass="add-margin error-msg" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label text-right"><spring:message code="lbl.righttomake.construction" /></label>
					<div class="col-sm-3 add-margin">
						<form:radiobutton
							path="documentScrutiny[0].rightToMakeConstruction"
							value="true" checked="checked" />
						<spring:message code="lbl.yes" />
						<form:radiobutton
							path="documentScrutiny[0].rightToMakeConstruction"
							value="false" />
						<spring:message code="lbl.no" />
						<form:errors path="documentScrutiny[0].rightToMakeConstruction"
							cssClass="add-margin error-msg" />
					</div>

					<label class="col-sm-2 control-label text-right"><spring:message code="lbl.deednumber" /></label>
					<div class="col-sm-2 add-margin">
						<form:input class="form-control patternvalidation" maxlength="60" data-pattern="alphanumeric" 
							id="deedNumber"
							path="documentScrutiny[0].deedNumber" />
						<form:errors path="documentScrutiny[0].deedNumber"
							cssClass="add-margin error-msg" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label text-right"><spring:message code="lbl.registraroffice" /><span class="mandatory"></label>
					<div class="col-sm-3 add-margin">
						<form:input class="form-control patternvalidation" maxlength="120" data-pattern="alphanumeric"  required="required"
							id="registrarOffice"
							path="documentScrutiny[0].registrarOffice" value="${bpaApplication.siteDetail[0].registrarOffice}"/>
						<form:errors path="documentScrutiny[0].registrarOffice"
							cssClass="add-margin error-msg" />
					</div>

					<label class="col-sm-2 control-label text-right"><spring:message code="lbl.typeofland" /></label>
					<div class="col-sm-2 add-margin">
						<form:input class="form-control patternvalidation" maxlength="120" data-pattern="alphanumeric" 
							id="typeofLand"
							path="documentScrutiny[0].typeofLand" />
						<form:errors path="documentScrutiny[0].typeofLand"
							cssClass="add-margin error-msg" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label text-right"><spring:message code="lbl.taluk" /></label>
					<div class="col-sm-3 add-margin">
						<form:input class="form-control patternvalidation" maxlength="120" data-pattern="alphanumeric" 
							id="taluk"
							path="documentScrutiny[0].taluk" value="${bpaApplication.siteDetail[0].taluk}"/>
						<form:errors path="documentScrutiny[0].taluk"
							cssClass="add-margin error-msg" />
					</div>

					<label class="col-sm-2 control-label text-right"><spring:message code="lbl.district" /></label>
					<div class="col-sm-2 add-margin">
						<form:input class="form-control patternvalidation" maxlength="120" data-pattern="alphanumeric" 
							id="district"
							path="documentScrutiny[0].district" value="${bpaApplication.siteDetail[0].district}"/>
						<form:errors path="documentScrutiny[0].district"
							cssClass="add-margin error-msg" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label text-right"><spring:message code="lbl.alldocument.attached" /></label>
					<div class="col-sm-3 add-margin">
						<form:radiobutton
							path="documentScrutiny[0].whetheralldocAttached"
							value="true" checked="checked" />
						<spring:message code="lbl.yes" />
						<form:radiobutton
							path="documentScrutiny[0].whetheralldocAttached"
							value="false" />
						<spring:message code="lbl.no" />
						<form:errors path="documentScrutiny[0].whetheralldocAttached"
							cssClass="add-margin error-msg" />
					</div>

					<label class="col-sm-2 control-label text-right"><spring:message code="lbl.revenue.village" /></label>
					<div class="col-sm-2 add-margin">
						<form:select path="documentScrutiny[0].village" id="village"
							 cssClass="form-control"
							cssErrorClass="form-control error">
							<form:option value="">
								<spring:message code="lbl.select" />
							</form:option>
							 <form:options items="${villageNames}" itemValue="code" itemLabel="name"/>
						</form:select>
						<form:errors path="documentScrutiny[0].village" cssClass="error-msg" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label text-right"><spring:message code="lbl.allpage.attached" /></label>
					<div class="col-sm-3 add-margin">
						<form:radiobutton
							path="documentScrutiny[0].whetherallPageOfdocAttached"
							value="true" checked="checked" />
						<spring:message code="lbl.yes" />
						<form:radiobutton
							path="documentScrutiny[0].whetherallPageOfdocAttached"
							value="false" />
						<spring:message code="lbl.no" />
						<form:errors path="documentScrutiny[0].whetherallPageOfdocAttached"
							cssClass="add-margin error-msg" />
					</div>

					<label class="col-sm-2 control-label text-right"><spring:message code="lbl.various.doc.matching" /></label>
					<div class="col-sm-2 add-margin">
							<form:radiobutton
							path="documentScrutiny[0].whetherdocumentMatch"
							value="true" checked="checked" />
						<spring:message code="lbl.yes" />
						<form:radiobutton
							path="documentScrutiny[0].whetherdocumentMatch"
							value="false" />
						<spring:message code="lbl.no" />
						<form:errors path="documentScrutiny[0].whetherdocumentMatch"
							cssClass="add-margin error-msg" />
					</div>
				</div>

			</div>
			<jsp:include page="../common/commonWorkflowMatrix.jsp" />
			<div class="buttonbottom" align="center">
				<jsp:include page="../common/commonWorkflowMatrix-button.jsp" />
			</div>
			<!-- 			
			<div class="buttonbottom" align="center">
				<table>
					<tr>
						<input type="button" name="button2" id="button2" value="Close"
							class="btn btn-primary" onclick="window.close();" />
						</td>
					</tr>
				</table>
			</div> -->
		</form:form>
	</div>
</div>

<script
	src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
