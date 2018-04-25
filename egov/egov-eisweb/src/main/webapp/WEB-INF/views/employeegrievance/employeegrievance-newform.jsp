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

<div class="main-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">Employee Grievance</div>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.employeecode" />  </label>
						<div class="col-sm-3 add-margin">
							<form:input path="employee.code"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="50" required="required"  readOnly="true"/>
							<form:errors path="employee.code" cssClass="error-msg" />
						</div>
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.employeename" />  </label>
						<div class="col-sm-3 add-margin">
							<form:input path="employee.name"
								class="form-control text-left patternvalidation"
								data-pattern="alphanumeric" maxlength="50" required="required" readOnly="true"/>
							<form:errors path="employee.name" cssClass="error-msg" />
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.employeegrievancetype" /> <span class="mandatory"></span>
						</label>
						<div class="col-sm-3 add-margin">
							<form:select path="employeeGrievanceType"
								id="employeeGrievanceType" cssClass="form-control"
								required="required" cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${employeeGrievanceTypes}" itemValue="id"
									itemLabel="name" />
							</form:select>
							<form:errors path="employeeGrievanceType" cssClass="error-msg" />
						</div>
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.description" /><span class="mandatory"></span> </label>
						<div class="col-sm-3 add-margin">
							<form:textarea path="details"
								data-pattern="alphanumeric" class="form-control" maxlength="1000" required="required"/>
							<form:errors path="details" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.grievancedocs" /> </label>
						<div class="col-sm-3 add-margin">
							<div>
								<table width="100%">
									<tbody>
                        <tr>
                            <td valign="top">
                                <table id="uploadertbl" width="100%"><tbody>
                                <tr id="row1">
                                    <td>
                                        <input type="file" name="file" id="file1" onchange="isValidFile(this.id)" class="padding-10">
                                    </td>
                                </tr>
                                </tbody></table>
                            </td>
                        </tr>
                        <tr>
                            <td align="center">
                                <button id="attachNewFileBtn" type="button" class="btn btn-primary" onclick="addFileInputField()"><spring:message code="lbl.addfile" /></button>
                            </td>
                        </tr>
                        </tbody>
								</table>
							</div>
							<form:errors path="grievanceDocs" cssClass="error-msg" />
						</div>
						<input type="hidden" name="employeeGrievance"
							value="${employeeGrievance.id}" />
						<input type="hidden" name="employee" id="employee" value="${employeeGrievance.employee.id}" />
							