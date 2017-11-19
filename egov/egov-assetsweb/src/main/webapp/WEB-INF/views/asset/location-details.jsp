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
		<div class="panel-heading">
			<div class="panel-title">Location Details</div>
		</div>
		<div class="panel-body">
			<div class="form-group">
				<label class="col-sm-3 control-label text-right"><spring:message
						code="lbl.location" /> <span class="mandatory"></span> </label>
				<div class="col-sm-3 add-margin">
					<form:select path="locationDetails.location" id="locationId" cssClass="form-control"
						required="required" cssErrorClass="form-control error">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${locations}" itemValue="id"
									itemLabel="name" />
					</form:select>
					<form:errors path="locationDetails.location" cssClass="error-msg" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label text-right"><spring:message
						code="lbl.electionwardnumber" /> </label>
				<div class="col-sm-3 add-margin">
					<form:select path="locationDetails.electionWard" id="electionwardId" cssClass="form-control"
						cssErrorClass="form-control error">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${electionWards}" itemValue="id"
									itemLabel="name" />
					</form:select>
					<form:errors path="locationDetails.electionWard" cssClass="error-msg" />
				</div>
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.revenuewardnumber" /></label>
				<div class="col-sm-3 add-margin">
					<form:select path="locationDetails.revenueWard" id="revenuewardId" cssClass="form-control"
						cssErrorClass="form-control error">
						<form:option value="${locationDetails.revenueWard}">
							<spring:message code="lbl.select" />
						</form:option>
					</form:select>
					<form:errors path="locationDetails.revenueWard" cssClass="error-msg" />
				</div>
			</div>
			<div class="form-group">
			<label class="col-sm-3 control-label text-right"><spring:message
						code="lbl.zonenumber" /> </label>
				<div class="col-sm-3 add-margin">
					<form:select path="locationDetails.zone" id="zoneId" cssClass="form-control"
						cssErrorClass="form-control error">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${zones}" itemValue="id"
									itemLabel="name" />
					</form:select>
					<form:errors path="locationDetails.zone" cssClass="error-msg" />
				</div>
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.blocknumber" /> </label>
				<div class="col-sm-3 add-margin">
					<form:select path="locationDetails.block" id="blockId" cssClass="form-control"
						cssErrorClass="form-control error">
						<form:option value="${locationDetails.block}">
							<spring:message code="lbl.select" />
						</form:option>
					</form:select>
					<form:errors path="locationDetails.block" cssClass="error-msg" />
				</div>
				
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label text-right"><spring:message
						code="lbl.street" /></label>
				<div class="col-sm-3 add-margin">
					<form:select path="locationDetails.street" id="streetId" cssClass="form-control"
						cssErrorClass="form-control error">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
					</form:select>
					<form:errors path="locationDetails.street" cssClass="error-msg" />
				</div>
				<label class="col-sm-2 control-label text-right"><spring:message
						code="lbl.doornumber" /></label>
				<div class="col-sm-3 add-margin">
					<form:input path="locationDetails.doorNumber"
						class="form-control text-left patternvalidation"
						data-pattern="alphanumeric" maxlength="50" />
					<form:errors path="locationDetails.doorNumber" cssClass="error-msg" />
				</div>
			</div>
			<div class="form-group">
			
				<label class="col-sm-3 control-label text-right"><spring:message
						code="lbl.pincode" /></label>
				<div class="col-sm-3 add-margin">
					<form:input path="locationDetails.pinCode"
						class="form-control text-left patternvalidation"
						data-pattern="number" maxlength="50" />
					<form:errors path="locationDetails.pinCode" cssClass="error-msg" />
				</div>
			</div>
		</div>
	</div>
</div>