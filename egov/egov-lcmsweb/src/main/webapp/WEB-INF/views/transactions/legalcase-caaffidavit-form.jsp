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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<div class="main-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-body">
					<div class="panel-heading ">
						<div class="panel-title" style="font-weight: bold">Pwr
							Details</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.datesubpwr" />:</label>
						<div class="col-sm-3 add-margin">
							<form:input name="pwrList[0].pwrDueDate"
								path="pwrList[0].pwrDueDate" class="form-control datepicker"
								title="Please enter a valid date"
								pattern="\d{1,2}/\d{1,2}/\d{4}" data-date-end-date="-1d"
								id="pwrDueDate" data-inputmask="'mask': 'd/m/y'"
							 />
							<form:errors path="pwrList[0].pwrDueDate"
								cssClass="add-margin error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"> <spring:message
								code="lbl.dateofapprovalpwr" />:
						</label>
						<div class="col-sm-3 add-margin">
							<form:input name="pwrList[0].pwrApprovalDate"
								path="pwrList[0].pwrApprovalDate"
								class="form-control datepicker"
								title="Please enter a valid date"
								pattern="\d{1,2}/\d{1,2}/\d{4}" id="pwrApprovalDate"
								data-inputmask="'mask': 'd/m/y'" />
							<form:errors path="pwrList[0].pwrApprovalDate"
								cssClass="add-margin error-msg" />
						</div>
					</div>
					<div class="panel-heading ">
						<div class="panel-title" style="font-weight: bold">Counter
							Affidavit Details</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.dateofsubmissionca" />:</label>
						<div class="col-sm-3 add-margin">
							<form:input name="counterAffidavits[0].counterAffidavitDueDate"
								path="counterAffidavits[0].counterAffidavitDueDate"
								class="form-control datepicker"
								title="Please enter a valid date"
								pattern="\d{1,2}/\d{1,2}/\d{4}" data-date-end-date="-1d"
								id="counterAffidavitDueDate"
								data-inputmask="'mask': 'd/m/y'"/>
							<form:errors path="counterAffidavits[0].counterAffidavitDueDate"
								cssClass="add-margin error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.dateofapprovalca" />:</label>
						<div class="col-sm-3 add-margin">
							<form:input
								name="counterAffidavits[0].counterAffidavitApprovalDate"
								path="counterAffidavits[0].counterAffidavitApprovalDate"
								class="form-control datepicker"
								title="Please enter a valid date"
								pattern="\d{1,2}/\d{1,2}/\d{4}"
								id="counterAffidavitApprovalDate"
								data-inputmask="'mask': 'd/m/y'"  />
							<form:errors
								path="counterAffidavits[0].counterAffidavitApprovalDate"
								cssClass="add-margin error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.eofficecomputernumber" />:</label>
						<div class="col-sm-3 add-margin">
							<form:input path="counterAffidavits[0].eOfficeComputerNumber"
								class="form-control  patternvalidation"
								data-pattern="alphanumerichyphenbackslash"  id="eoffice" maxlength="16"/>
							<form:errors path="counterAffidavits[0].eOfficeComputerNumber"
								cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.cafillingdate" />:</label>
						<div class="col-sm-3 add-margin">
							<form:input name="pwrList[0].caFilingDate"
								path="pwrList[0].caFilingDate" class="form-control datepicker"
								title="Please enter a valid date"
								pattern="\d{1,2}/\d{1,2}/\d{4}" data-date-end-date="-1d"
								id="caFilingDate" data-inputmask="'mask': 'd/m/y'"/>
							<form:errors path="pwrList[0].caFilingDate"
								cssClass="add-margin error-msg" />
						</div>
					</div>

					<div>
					 </div>
			 <div id="pwrDocuments"></div>