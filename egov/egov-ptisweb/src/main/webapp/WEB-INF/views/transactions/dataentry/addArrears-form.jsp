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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<style>
body
{
  font-family:regular !important;
  font-size:14px;
}
</style>
<script type="text/javascript" src="<cdn:url value='/resources/javascript/validations.js'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/javascript/dateValidation.js'/>"></script>

<form:form id="addArrearsForm" method="post"
	class="form-horizontal form-groups-bordered" modelAttribute="arrearsInfo">
	<div class="page-container" id="page-container">
        	<div class="main-content">
				<div class="row">
					<div class="col-md-12">
						<div class="panel panel-primary" data-collapsed="0">
							<div class="panel-heading" style="text-align: left">
								<div class="panel-title"><spring:message code="lbl.addArrears.title" /></div>
							</div>
							<div class="panel-body custom-form">
								<div class="mandatory" align="right">
									<c:out value="${arrearsMessage}"></c:out>
								</div>
								<br />
								<div class="form-group">
									<c:if test="${propertyType == 'VAC_LAND'}">
										<label class="col-sm-3 control-label text-right">
											<spring:message code="lbl.addArrears.vacantLandTax" /> <span class="mandatory"></span>
										</label>
										<div class="col-sm-3 add-margin">
											<form:input path="vacantLandTax" id="vacantLandTax" type="text" class="form-control low-width" placeholder="" autocomplete="off" required="required" />
											<form:errors path="vacantLandTax" cssClass="add-margin error-msg"/>
										</div>
									</c:if>
									<c:if test="${propertyType != 'VAC_LAND'}">
										<label class="col-sm-3 control-label text-right">
											<spring:message code="lbl.addArrears.generalTax" /><span class="mandatory"></span>
										</label>
										<div class="col-sm-3 add-margin">
											<form:input path="generalTax" id="generalTax" type="text" class="form-control low-width" placeholder="" autocomplete="off" required="required"/>
											<form:errors path="generalTax" cssClass="add-margin error-msg"/>
										</div>
									</c:if>
                                    <label class="col-sm-3 control-label text-right">
                                    	<spring:message code="lbl.addArrears.libraryCess" /> <span class="mandatory"></span>
									</label>
									<div class="col-sm-3 add-margin">
										<form:input path="libraryCess" id="libraryCess" type="text" class="form-control low-width" placeholder="" autocomplete="off" required="required"/>
										<form:errors path="libraryCess" cssClass="add-margin error-msg"/>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label text-right">
										<spring:message code="lbl.addArrears.eduCess" /> <span class="mandatory"></span>
									</label>
									<div class="col-sm-3 add-margin">
										<form:input path="educationCess" id="educationCess" type="text" class="form-control low-width" placeholder="" autocomplete="off" required="required"/>
										<form:errors path="educationCess" cssClass="add-margin error-msg"/>
									</div>
                                    <label class="col-sm-3 control-label text-right">
                                    	<spring:message code="lbl.addArrears.unauthorizedPenalty" />  
									</label>
									<div class="col-sm-3 add-margin">
										<form:input path="unauthorizedPenalty" id="unauthorizedPenalty" type="text" class="form-control low-width" placeholder="" autocomplete="off" />
										<form:errors path="unauthorizedPenalty" cssClass="add-margin error-msg"/>
									</div>
								</div>
								<div class="form-group">
                                    <label class="col-sm-3 control-label text-right">
                                    	<spring:message code="lbl.addArrears.latePayment.Penalty" /> 
									</label>
									<div class="col-sm-3 add-margin">
										<form:input path="latePaymentPenalty" id="latePaymentPenalty" type="text" class="form-control low-width" placeholder="" autocomplete="off" />
										<form:errors path="latePaymentPenalty" cssClass="add-margin error-msg"/>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="text-center">
						<button type="submit" class="btn btn-primary"><spring:message code="lbl.submit"/></button>
						<button type="button" class="btn btn-default" data-dismiss="modal" onclick="self.close()"><spring:message code="lbl.close"/></button>
					</div>
				</div>
			</div> <!-- end of main-content -->
		</div>
</form:form>

