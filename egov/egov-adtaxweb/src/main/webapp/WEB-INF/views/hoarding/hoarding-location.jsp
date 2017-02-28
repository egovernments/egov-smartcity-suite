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
		<div class="panel-heading custom_form_panel_heading">
			<div class="panel-title">
			<spring:message code="lbl.hoarding.locality"/>
			</div>
		</div>		
		<div class="form-group">
			<label class="col-sm-3 control-label text-right">
			<spring:message code="lbl.hoarding.prop.assesmt.no"/>
				</label>
			
			<div class="col-sm-3 add-margin">
					<form:input type="text" cssClass="form-control patternvalidation" data-pattern="alphanumerichyphenbackslash" maxlength="15" path="advertisement.propertyNumber" id="propertyNumber" />
                          			<form:errors path="advertisement.propertyNumber" cssClass="error-msg" />
			</div>
			<label class="col-sm-2 control-label text-right">
			<spring:message code="lbl.locality"/>
				<span class="mandatory"></span></label>
		
			<div class="col-sm-3 add-margin">
			<form:select path="advertisement.locality" id="locality" cssClass="form-control" cssErrorClass="form-control error" required="required">
					<form:option value=""><spring:message code="lbl.select" /></form:option>
					<form:options items="${localities}" itemLabel="name" itemValue="id"/>
				</form:select>
				<form:errors path="advertisement.locality" cssClass="error-msg" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label text-right">
			<spring:message code="lbl.rev.ward"/>
			<span class="mandatory"></span></label>
			<div class="col-sm-3 add-margin">
				<form:select path="advertisement.ward" id="ward" cssClass="form-control" cssErrorClass="form-control error" required="required">
					<form:option value=""><spring:message code="lbl.select" /></form:option>
					</form:select>
				<form:errors path="advertisement.ward" cssClass="error-msg" />
			</div>
			<label class="col-sm-2 control-label text-right">
			<spring:message code="lbl.block"/>
			<span class="mandatory"></span></label>
			<div class="col-sm-3 add-margin">
				<form:select path="advertisement.block" id="block" cssClass="form-control" cssErrorClass="form-control error" required="required">
					<form:option value=""><spring:message code="lbl.select"/></form:option>
				</form:select>
				<form:errors path="advertisement.block" cssClass="error-msg" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label text-right">
			<spring:message code="lbl.street"/>
			</label>
			<div class="col-sm-3 add-margin">
				<form:select path="advertisement.street" id="street" cssClass="form-control" cssErrorClass="form-control error" >
					<form:option value=""><spring:message code="lbl.select" /></form:option>
					</form:select>
				<form:errors path="advertisement.street" cssClass="error-msg" />
			</div>
			<label class="col-sm-2 control-label text-right">
				<spring:message code="lbl.election.ward"/>
				<span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<form:select path="advertisement.electionWard" id="electionWard" cssClass="form-control" cssErrorClass="form-control error" required="required">
					<form:option value=""><spring:message code="lbl.select"/></form:option> 
					<form:options items="${revenueWards}" itemLabel="name" itemValue="id"/> 
				</form:select>
				<form:errors path="advertisement.electionWard" cssClass="error-msg" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label text-right">
			<spring:message code="lbl.address"/>
			<span class="mandatory"></span></label>
			<div class="col-sm-3 add-margin">
				<form:textarea path="advertisement.address" id="address" cols="5" rows="2" class="form-control patternvalidation" data-pattern="alphanumericwithspace" required="required" minlength="5" maxlength="256"/>
                <form:errors path="advertisement.address" cssClass="error-msg" />
			</div>
			<div class="col-sm-5 add-margin">&nbsp;</div>
		</div>
