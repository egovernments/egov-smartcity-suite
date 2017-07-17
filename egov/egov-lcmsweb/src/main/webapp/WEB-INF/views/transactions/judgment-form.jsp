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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div class="main-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">

				<c:if test="${mode =='create'}">
					<div class="panel-heading">
						<div class="panel-title">Judgment</div>
					</div>
				</c:if>

				<c:if test="${mode =='edit'}">
					<div class="panel-heading">
						<div class="panel-title">Edit Judgment</div>
					</div>

				</c:if>
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.orderdate" />: <span class="mandatory"></span> </label>
						<div class="col-sm-3 add-margin">
							<form:input path="orderDate" class="form-control datepicker"
								title="Please enter a valid date" data-date-end-date="-1d"
								pattern="\d{1,2}/\d{1,2}/\d{4}" data-inputmask="'mask': 'd/m/y'"
								required="required" />
							<form:errors path="orderDate" cssClass="error-msg" />
						</div>
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.senttodepton" /> :<span class="mandatory"></span> </label>
						<div class="col-sm-3 add-margin">
							<form:input path="sentToDeptOn" class="form-control datepicker"
								data-date-end-date="0d" data-inputmask="'mask': 'd/m/y'"
								required="required" />
							<form:errors path="sentToDeptOn" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.judgmentype" /> :<span class="mandatory"></span> </label>
						<div class="col-sm-3 add-margin">
							<form:select path="judgmentType" id="judgmentType"
								name="judgmentType" cssClass="form-control" required="required"
								cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${judgmentTypes}" itemValue="id"
									itemLabel="name" />
							</form:select>
							<form:errors path="judgmentType" cssClass="error-msg" />
						</div>
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.implementbydate" /> :</label>
						<div class="col-sm-3 add-margin">
							<form:input path="implementByDate"
								class="form-control datepicker" data-date-end-date="0d" data-inputmask="'mask': 'd/m/y'"/>
							<form:errors path="implementByDate" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.costawarded" /> :</label>
						<div class="col-sm-3 add-margin">
							<form:input path="costAwarded"
								class="form-control text-left patternvalidation"
								data-pattern="number" />
							<form:errors path="costAwarded" cssClass="error-msg" />
						</div>
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.compensationawarded" />: </label>
						<div class="col-sm-3 add-margin">
							<form:input path="compensationAwarded"
								class="form-control text-left patternvalidation"
								data-pattern="number" />
							<form:errors path="compensationAwarded" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.judgmentdetails" />: <span class="mandatory"></span>
						</label>
						<div class="col-sm-3 add-margin">
							<form:textarea class="form-control text-left patternvalidation"
								path="judgmentDetails" id="judgmentDetails"
								name="judgmentDetails"
								data-pattern="alphanumericwithspecialcharacterswithspace"
								maxlength="1024" required="required" />
							<form:errors path="judgmentDetails" cssClass="error-msg" />
						</div>
					</div>
					<%-- <div class="form-group">
						<label class="col-sm-3 control-label text-right"><font
							size="2"><spring:message code="lbl.mesg.document" />:</font></label>
						<div class="col-sm-3 add-margin">
							<input type="file" id="file" name="judgmentDocuments[0].files"
								class="file-ellipsis upload-file">
							<form:errors path="judgmentDocuments[0].files"
								cssClass="add-margin error-msg" />
						</div>
					</div> --%>

					<div class="form-group" id="enquirydetails" style="display: none">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.enquirydetails" /> :</label>
						<div class="col-sm-3 add-margin">
							<form:textarea path="enquiryDetails"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumericwithspecialcharacterswithspace"
								maxlength="1024" />
							<form:errors path="enquiryDetails" cssClass="error-msg" />
						</div>
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.enquirydate" />: </label>
						<div class="col-sm-3 add-margin">
							<form:input path="enquiryDate" class="form-control datepicker"
								data-date-end-date="0d" data-inputmask="'mask': 'd/m/y'" />
							<form:errors path="enquiryDate" cssClass="error-msg" />
						</div>
					</div>

					<div class="form-group" id="exparteorder1" style="display: none">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.setasidepetitiondate" /> :</label>
						<div class="col-sm-3 add-margin">
							<form:input path="setasidePetitionDate"
								class="form-control datepicker" data-date-end-date="0d"
								data-inputmask="'mask': 'd/m/y'" />
							<form:errors path="setasidePetitionDate" cssClass="error-msg" />
						</div>
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.saphearingdate" />: </label>
						<div class="col-sm-3 add-margin">
							<form:input path="sapHearingDate" class="form-control datepicker"
								data-date-end-date="0d" data-inputmask="'mask': 'd/m/y'" />
							<form:errors path="sapHearingDate" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group" id="exparteorder2" style="display: none">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.sapaccepted" /> :</label>
						<div class="col-sm-3 add-margin">
							<form:checkbox path="sapAccepted" />
							<form:errors path="sapAccepted" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group" id="exparteorder3" style="display: none">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.setasidepetitiondetails" />: </label>
						<div class="col-sm-3 add-margin">
							<form:textarea path="setasidePetitionDetails"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumericwithspecialcharacterswithspace"
								maxlength="1024" />
							<form:errors path="setasidePetitionDetails" cssClass="error-msg" />
						</div>
					</div>

					<div id="judgmentDocuments"></div>

					<input type="hidden" name="judgment" value="${judgment.id}" /> <input
						type="hidden" name="mode" value="${mode}" />