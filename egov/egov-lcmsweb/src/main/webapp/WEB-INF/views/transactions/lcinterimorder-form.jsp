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
<div class="main-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<c:if test="${mode == 'create'}">
					<div class="panel-heading">
						<div class="panel-title">Interim Order</div>
					</div>
				</c:if>
				<c:if test="${mode == 'edit'}">
					<div class="panel-heading">
						<div class="panel-title">Edit Interim Order</div>
					</div>
				</c:if>
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.interimorder" /> :<span class="mandatory"></span> </label>
						<div class="col-sm-3 add-margin">
							<form:select path="interimOrder" id="interimOrder"
								cssClass="form-control" required="required"
								cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${interimOrders}" itemValue="id"
									itemLabel="interimOrderType" />
							</form:select>
							<form:errors path="interimOrder" cssClass="error-msg" />
						</div>

						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.iodate" /> :<span class="mandatory"></span> </label>
						<div class="col-sm-3 add-margin">
							<form:input path="ioDate" class="form-control datepicker"
								data-date-end-date="0d" data-inputmask="'mask': 'd/m/y'"
								required="required" />
							<form:errors path="ioDate" cssClass="error-msg" />
						</div>

					</div>
					<div class="form-group">

						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.mpnumber" /> :<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">       
							<form:input path="mpNumber"  
								class="form-control text-left patternvalidation"
								data-pattern="alphanumerichyphenbackslash" maxlength="50"  required="required" />      
							<form:errors path="mpNumber" cssClass="error-msg" />
						</div>
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.notes" /> :<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:textarea path="notes"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumericwithspecialcharacterswithspace"
								maxlength="1024" required="required" />
							<form:errors path="notes" cssClass="error-msg" />
						</div>
					</div>

					<div class="form-group">

						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.actionitem" />:</span></label>
						<div class="col-sm-3 add-margin">
							<form:textarea path="actionItem"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumericwithspecialcharacterswithspace"
								maxlength="500" />
							<form:errors path="actionItem" cssClass="error-msg" />
						</div>


						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.actiontaken" />:</label>
						<div class="col-sm-3 add-margin">
							<form:textarea path="actionTaken"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumericwithspecialcharacterswithspace"
								maxlength="500" />
							<form:errors path="actionTaken" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">

						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.duedate" />:</span></label>
						<div class="col-sm-3 add-margin">
							<form:input path="dueDate" class="form-control datepicker" />
							<form:errors path="dueDate" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label for="field-1" class="col-sm-3 control-label"><spring:message
								code="lbl.officerresponsible" />:</label>

						<div class="col-sm-3 add-margin">
							<form:hidden path="employee" id="employee" value=""
								cssClass="selectwk" />
							<form:input id="employeeInput" path=""
								class="form-control" type="text" placeholder="Employee Name" />
							<input type="hidden" name="employeeName" id="employeeName"
								value="${legalCaseInterimOrder.employee.name}" /> <input
								type="hidden" name="department" id="department"
								value="${legalCaseInterimOrder.employee.assignments[0].department.name}" />
							<input type="hidden" name="designation" id="designation"
								value="${legalCaseInterimOrder.employee.assignments[0].designation.name}" />
							<form:errors path="employee" cssClass="add-margin error-msg" />
						</div>

						<!-- <div class="form-group">
							<label class="col-sm-3 control-label text-right"><font
								size="2"><spring:message code="lbl.complianceorder" />:</font></label>
							<div class="col-sm-3 add-margin">
								<input type="file" id="file"
									name="lcInterimOrderDocuments[0].files"
									class="file-ellipsis upload-file">
								<form:errors path="lcInterimOrderDocuments[0].files"
									cssClass="add-margin error-msg" />
							</div>
						</div> -->
						<div class="form-group" id="staydetails" style="display: none">
							<label class="col-sm-3 control-label text-right"><spring:message
									code="lbl.sendtostandingcounsel" /> :</label>
							<div class="col-sm-3 add-margin">
								<form:input path="sendtoStandingCounsel"
									class="form-control datepicker" data-date-end-date="0d"
									data-inputmask="'mask': 'd/m/y'" />
								<form:errors path="sendtoStandingCounsel" cssClass="error-msg" />
							</div>

							<label class="col-sm-3 control-label text-right"><spring:message
									code="lbl.petitionfiledon" />: </label>
							<div class="col-sm-3 add-margin">
								<form:input path="petitionFiledOn"
									class="form-control datepicker" data-date-end-date="0d"
									data-inputmask="'mask': 'd/m/y'" />
								<form:errors path="petitionFiledOn" cssClass="error-msg" />
							</div>
						</div>
						<!--  <div class="form-group" id="reportdetails1"  style="display:none">
						<label class="col-sm-3 control-label text-right"><spring:message
							code="lbl.reportfilingdue" /> :</label>
					<div class="col-sm-3 add-margin">
						<form:input path="reportFilingDue" class="form-control datepicker"
							data-date-end-date="0d" data-inputmask="'mask': 'd/m/y'" />
						<form:errors path="reportFilingDue" cssClass="error-msg" />
					</div>
					
					<label class="col-sm-3 control-label text-right"><spring:message
							code="lbl.reportfromhod" />: </label>
					<div class="col-sm-3 add-margin">
						<form:input path="reportFromHod" class="form-control datepicker"
							data-date-end-date="0d" data-inputmask="'mask': 'd/m/y'" />
						<form:errors path="reportFromHod" cssClass="error-msg" />
					</div>
				</div> 
					 <div class="form-group" id="reportdetails2"  style="display:none">
					<label class="col-sm-3 control-label text-right"><spring:message
							code="lbl.reportsendtostandingcounsel" /> :</label>
					<div class="col-sm-3 add-margin">
						<form:input path="reportSendtoStandingCounsel"
							class="form-control datepicker" data-date-end-date="0d"
							data-inputmask="'mask': 'd/m/y'" />
						<form:errors path="reportSendtoStandingCounsel"
							cssClass="error-msg" />
					</div>
					<label class="col-sm-3 control-label text-right"><spring:message
							code="lbl.reportfilingdate" />: </label>
					<div class="col-sm-3 add-margin">
						<form:input path="reportFilingDate"
							class="form-control datepicker" data-date-end-date="0d"
							data-inputmask="'mask': 'd/m/y'" />
						<form:errors path="reportFilingDate" cssClass="error-msg" />
					</div>
				</div> 
			 -->
			 </div>
			 <div id="lcInterimOrderDocuments"></div>
						<input type="hidden" name="legalCaseInterimOrder"
							value="${legalCaseInterimOrder.id}" /> <input type="hidden"
							name="mode" value="${mode}" />