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

<div class="panel-heading" style="text-align: left">
	<div class="panel-title">
		<s:text name="ownerDtls" />
	</div>
</div>
<div class="panel-body">
	<table class="table table-bordered" id="ownerInfoTbl">
		<thead>
			<tr>
				<th class="text-center"><s:text name="adharno" /></th>
				<th class="text-center"><s:text name="MobileNumber" /> <span
					class="mandatory1">*</span> (without +91)</th>
				<th class="text-center"><s:text name="OwnerName" /><span
					class="mandatory1">*</span></th>
				<th class="text-center"><s:text name="gender" /><span
					class="mandatory1">*</span></th>
				<th class="text-center"><s:text name="EmailAddress" /></th>
				<th class="text-center"><s:text name="GuardianRelation" /><span
					class="mandatory1">*</span></th>
				<th class="text-center"><s:text name="Guardian" /><span
					class="mandatory1">*</span></th>
<!-- 				<th class="text-center" id="addDelOwners"><s:text name="Add/Delete" /></th> -->
				<th class="text-center" id="addDelOwners"><s:text name="Delete" /></th>
			</tr>
		</thead>
		<tbody>
			<s:if test="%{amalgamationOwnersProxy.size != 0}">
				<s:iterator value="(amalgamationOwnersProxy.size).{#this}"
					status="ownerStatus">
					<tr id="ownerDetailsRow">
					<s:hidden
							name="amalgamationOwnersProxy[%{#ownerStatus.index}].upicNo"
							id="amalgamationOwnersProxy[%{#ownerStatus.index}].upicNo"
							value="%{amalgamationOwnersProxy[#ownerStatus.index].upicNo}"></s:hidden>
						<s:hidden
							name="amalgamationOwnersProxy[%{#ownerStatus.index}].owner.type"
							id="amalgamationOwnersProxy[%{#ownerStatus.index}].owner.type"
							value="%{amalgamationOwnersProxy[#ownerStatus.index].owner.type}"></s:hidden>
						<s:hidden
							name="amalgamationOwnersProxy[%{#ownerStatus.index}].ownerOfParent"
							id="amalgamationOwnersProxy[%{#ownerStatus.index}].ownerOfParent"
							value="%{amalgamationOwnersProxy[#ownerStatus.index].ownerOfParent}" cssClass="ownerofparent"></s:hidden>
						<td><s:textfield
								name="amalgamationOwnersProxy[%{#ownerStatus.index}].owner.aadhaarNumber"
								id="aadharNo" size="12" maxlength="12" data-optional="1"
								data-errormsg="Aadhar no is mandatory!"
								value="%{amalgamationOwnersProxy[#ownerStatus.index].owner.aadhaarNumber}"
								data-idx="%{#ownerStatus.index}"
								cssClass="form-control  txtaadhar"></s:textfield></td>
						<td><s:textfield
								name="amalgamationOwnersProxy[%{#ownerStatus.index}].owner.mobileNumber"
								maxlength="10" size="20" id="mobileNumber"
								value="%{amalgamationOwnersProxy[#ownerStatus.index].owner.mobileNumber}"
								data-pattern="number" data-idx="%{#ownerStatus.index}"
								data-optional="0" data-errormsg="Mobile no is mandatory!"
								cssClass="form-control patternvalidation mobileno" /></td>
						<td><s:textfield
								name="amalgamationOwnersProxy[%{#ownerStatus.index}].owner.name"
								maxlength="74" size="20" id="ownerName"
								value="%{amalgamationOwnersProxy[#ownerStatus.index].owner.name}"
								cssClass="form-control patternvalidation ownername"
								data-pattern="alphabetwithspecialcharacters" data-optional="0"
								data-errormsg="Owner name is mandatory!" /></td>
						<td><s:select id="gender"
								name="amalgamationOwnersProxy[%{#ownerStatus.index}].owner.gender"
								value="%{amalgamationOwnersProxy[#ownerStatus.index].owner.gender}"
								headerValue="Choose" headerKey=""
								list="@org.egov.infra.persistence.entity.enums.Gender@values()"
								data-optional="0" data-errormsg="Gender is mandatory!">
							</s:select></td>
						<td><s:textfield
								name="amalgamationOwnersProxy[%{#ownerStatus.index}].owner.emailId"
								maxlength="32" size="20" id="emailId"
								value="%{amalgamationOwnersProxy[#ownerStatus.index].owner.emailId}"
								cssClass="form-control email"
								onblur="trim(this,this.value);validateEmail(this);" /></td>
						<td><s:select id="guardianRelation"
								name="amalgamationOwnersProxy[%{#ownerStatus.index}].owner.guardianRelation"
								value="%{amalgamationOwnersProxy[#ownerStatus.index].owner.guardianRelation}"
								headerValue="Choose" list="guardianRelations"
								data-optional="0"
								data-errormsg="Guardian relation is mandatory!" /></td>
						<td><s:textfield
								name="amalgamationOwnersProxy[%{#ownerStatus.index}].owner.guardian"
								maxlength="32" size="20" id="guardian"
								value="%{amalgamationOwnersProxy[#ownerStatus.index].owner.guardian}"
								cssClass="form-control guardianname"
								onblur="trim(this,this.value);checkSpecialCharForName(this);"
								data-optional="0" data-errormsg="Guardian name is mandatory!" /></td>
 						<td class="text-center">
                          <!--  <span id="addOwnerBtn"
							name="addOwnerBtn" class="btn-sm btn-default" alt="addOwnerBtn"
							onclick="addOwners();"> <i class="fa fa-plus"></i>
						</span> &nbsp; -->
                        <span id="delete_owner_row" name="removeOwnerBtn" 
							class="btn-sm btn-default deleteowner" alt="removeOwnerBtn"> <i
								class="fa fa-trash"></i>
						</span></td>

					</tr>
				</s:iterator>
			</s:if>
		</tbody>
	</table>
</div>