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
		<s:text name="lbl.amalgamated.properties" />
	</div>
</div>
<div class="panel-body">
	<table class="table table-bordered" id="amalgamatedPropertiesTbl">
		<thead>
			<tr>
				<th class="text-center"><s:text name="prop.Id" /></th>
				<th class="text-center"><s:text name="OwnerName" /></th>
				<th class="text-center"><s:text name="MobileNumber" /></th>
				<th class="text-center"><s:text name="PropertyAddress" /></th>
				<th class="text-center" id="addDelAmalgamatedProps"><s:text
						name="Add/Delete" /></th>
			</tr>
		</thead>
		<tbody>
			<s:if test="%{basicProp.amalgamationsProxy.size() == 0}">
				<tr id="amalgamatedPropRow">
					<td><s:textfield
							name="basicProp.amalgamationsProxy[0].assessmentNo"
							id="assessmentNo" cssClass="form-control amlgpropassessmentno"
							value="%{basicProp.amalgamationsProxy[0].assessmentNo}"
							cssStyle="width:100%" data-idx="0" /></td>
					<td><s:textfield
							name="basicProp.amalgamationsProxy[0].ownerName" id="ownerName"
							cssClass="form-control amlgpropownername"
							value="%{basicProp.amalgamationsProxy[0].ownerName}"
							cssStyle="width:100%" /></td>
					<td><s:textfield
							name="basicProp.amalgamationsProxy[0].mobileNo"
							id="amlgPropMobileNo" cssClass="form-control amlgpropmobileno"
							value="%{basicProp.amalgamationsProxy[0].mobileNo}"
							cssStyle="width:100%" /></td>
					<td><s:textfield
							name="basicProp.amalgamationsProxy[0].propertyAddress"
							id="amlgPropPropertyAddress"
							cssClass="form-control amlgpropaddress"
							value="%{basicProp.amalgamationsProxy[0].propertyAddress}"
							cssStyle="width:100%" /></td>
					<td class="text-center"><a href="javascript:void(0);"
						class="btn-sm btn-default" onclick="addAmalgamatedProperties();"><span
							style="cursor: pointer;"><i class="fa fa-plus"></i></span></a> <a
						href="javascript:void(0);" class="btn-sm btn-default"
						id="delete_row"><span style="cursor: pointer;"> <i
								class="fa fa-trash"></i>
						</span></a></td>
				</tr>
			</s:if>
			<s:else>
				<s:iterator value="(basicProp.amalgamationsProxy.size).{#this}"
					status="item">
					<tr id="amalgamatedPropRow">
					<s:if test="%{modelId != null}">
						<td><s:textfield
								name="basicProp.amalgamationsProxy[%{#item.index}].assessmentNo"
								id="assessmentNo" cssClass="form-control amlgpropassessmentno"
								value="%{basicProp.amalgamationsProxy[#item.index].assessmentNo}"
								cssStyle="width:100%" data-idx="%{#item.index}" readOnly="true" disabled="disabled"/></td>
								</s:if>
								<s:else>
							<td><s:textfield
								name="basicProp.amalgamationsProxy[%{#item.index}].assessmentNo"
								id="assessmentNo" cssClass="form-control amlgpropassessmentno"
								value="%{basicProp.amalgamationsProxy[#item.index].assessmentNo}"
								cssStyle="width:100%" data-idx="%{#item.index}" /></td>
								</s:else>	
						<td><s:textfield
								name="basicProp.amalgamationsProxy[%{#item.index}].ownerName"
								id="ownerName" cssClass="form-control amlgpropownername"
								value="%{basicProp.amalgamationsProxy[#item.index].ownerName}"
								cssStyle="width:100%" /></td>
						<td><s:textfield
								name="basicProp.amalgamationsProxy[%{#item.index}].mobileNo"
								id="amlgPropMobileNo" cssClass="form-control amlgpropmobileno"
								value="%{basicProp.amalgamationsProxy[#item.index].mobileNo}"
								cssStyle="width:100%" /></td>
						<td><s:textfield
								name="basicProp.amalgamationsProxy[%{#item.index}].propertyAddress"
								id="amlgPropPropertyAddress"
								cssClass="form-control amlgpropaddress"
								value="%{basicProp.amalgamationsProxy[#item.index].propertyAddress}"
								cssStyle="width:100%" /></td>
						<td class="text-center"><a href="javascript:void(0);"
							class="btn-sm btn-default" onclick="addAmalgamatedProperties();"><span
								style="cursor: pointer;"><i class="fa fa-plus"></i></span></a> <a
							href="javascript:void(0);" class="btn-sm btn-default"
							id="delete_row"><span style="cursor: pointer;"> <i
									class="fa fa-trash"></i>
							</span></a></td>
					</tr>

				</s:iterator>
			</s:else>
		</tbody>
	</table>
</div>