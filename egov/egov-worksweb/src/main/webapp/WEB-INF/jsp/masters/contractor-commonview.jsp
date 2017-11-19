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
<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
			</div>
			<div class="panel-body">
				<div class="row add-border">
					<div class="col-xs-3 add-margin" id = "code">
						<s:text name="contractor.code" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "code-value">
						<s:property value="%{code}" />
					</div>
					<div class="col-xs-3 add-margin" id = "name">
						<s:text name="contractor.name" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "name-value">
						<s:property value="%{name}" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin" id = "correspondenceAddress">
						<s:text name="contractor.correspondenceAddress" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "correspondenceAddress-value">
						<s:property value="%{correspondenceAddress}"
							/>
					</div>
					<div class="col-xs-3 add-margin" id = "paymentAddress">
						<s:text name="contractor.paymentAddress" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "paymentAddress-value">
						<s:property value="%{paymentAddress}" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin" id = "contactPerson">
						<s:text name="contractor.contactPerson" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "contactPerson-value">
						<s:property value="%{contactPerson}" />
					</div>
					<div class="col-xs-3 add-margin" id = "email">
						<s:text name="contractor.email" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "email-value">
						<s:property value="%{email}" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin" id = "narration">
						<s:text name="contractor.narration" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "narration-value">
						<s:property value="%{narration}" />
					</div>
					<div class="col-xs-3 add-margin" id = "mobileNumber">
						<s:text name="depositworks.applicant.mobile" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "mobileNumber-value">
						<s:property value="%{mobileNumber}" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin" id = "panNo"> 
						<s:text name="contractor.panNo" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "panNo-value">
						<s:property value="%{panNumber}" />
					</div>
					<div class="col-xs-3 add-margin" id = "tinNo">
						<s:text name="contractor.tinNo" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "tinNo-value">
						<s:property value="%{tinNumber}" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin" id = "bank">
						<s:text name="contractor.bank" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "bank-value">
						<s:property value="%{bank.name}" />
					</div>
					<div class="col-xs-3 add-margin" id = "ifscCode">
						<s:text name="contractor.ifscCode" />
					</div>
					<div class="col-xs-3 add-margin view-content" id ="ifscCode-value">
						<s:property value="%{ifscCode}" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin" id = "bankAccount">
						<s:text name="contractor.bankAccount" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "bankAccount-value">
						<s:property value="%{bankAccount}" />
					</div>
					<div class="col-xs-3 add-margin" id = "pwdApprovalCode">
						<s:text name="contractor.pwdApprovalCode" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "pwdApprovalCode-value">
						<s:property value="%{pwdApprovalCode}" />
					</div>
				</div>
				<div class="row">
					<div class="col-xs-3 add-margin" id = "exemptionFrom">
						<s:text name="contractor.exemptionFrom" />
					</div>
					<div class="col-xs-3 add-margin view-content" id="exemptionForm-value">
						<s:property value="%{exemptionForm}" />
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<s:text name="contractor.contDetails" />
		</div>
	</div>
	<div class="panel-body">
		<table class="table table-bordered">
			<thead>
				<tr>
					<th><s:text name="column.title.SLNo" /></th>
					<th id = "department"><s:text name="contractor.department" /></th>
					<th id = "registrationNo"><s:text name="contractor.registrationNo" /></th>
					<th id = "category"><s:text name="contractor.category" /></th>
					<th id = "grade"><s:text name="contractor.grade" /></th>
					<th id = "status"><s:text name="contractor.status" /></th>
					<th id = "fromDate"><s:text name="contractor.fromDate" /></th>
					<th id = "toDate"><s:text name="contractor.toDate" /></th>
				</tr>
			</thead>
			<tbody>
			<s:iterator value="model.contractorDetails" status="row_status">
				<tr>
					<td><s:property value="#row_status.count" /></td>
					<td id = "department-value"><s:property value="%{department.name}" /></td>
					<td id = "registrationNumber-value"><s:property value="%{registrationNumber}" /></td>
					<td id = "category-value"><s:property value="%{category}" /></td>
					<td id = "grade-value"><s:property value="%{grade.grade}" /></td>
					<td id = "status-value"><s:property value="%{status.description}" /></td>
					<td id = "startDate-value"><s:date name="validity.startDate" format="dd/MM/yyyy" /></td>
					<td id = "toDate-value"><s:date name="validity.endDate" format="dd/MM/yyyy" /></td>
				</tr>
			</s:iterator>
			</tbody>
		</table>
	</div>
</div>