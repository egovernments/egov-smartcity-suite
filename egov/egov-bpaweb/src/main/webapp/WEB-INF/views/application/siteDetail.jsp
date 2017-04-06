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

<%@page import="org.python.modules.jarray"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">Site Details</div>
</div>


<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">Boundary Details</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"> Zone <span class="mandatory"></span>
								</label>
<div class="col-sm-3 add-margin">
		<form:select path="zoneId" data-first-option="false" id="zone"
			cssClass="form-control" required="required">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${zones}"  itemValue="id"
						itemLabel="name" />
		</form:select>
		<form:errors path="zoneId" cssClass="add-margin error-msg" />
	</div>
								
	<label class="col-sm-2 control-label text-right"> Ward <span class="mandatory"></span>
								</label>
							<div class="col-sm-2 add-margin">
		<form:select path="wardId" data-first-option="false" id="ward"
			cssClass="form-control" required="required">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${wards}"  itemValue="id"
						itemLabel="name" />
		</form:select>
		<form:errors path="wardId" cssClass="add-margin error-msg" />
	</div>
								</div>
						<div class="form-group">
								<label class="col-sm-3 control-label text-right"><spring:message
										code="lbl.block" /></label>
								<div class="col-sm-3 add-margin">
									<form:select name="block" id="block" path="siteDetail[0].locationBoundary"
										cssClass="form-control" cssErrorClass="form-control error">
										<form:option value="">
									--select--
								</form:option>
									</form:select>
								</div>
								
								<label for="field-1" class="col-sm-2 control-label"><spring:message
										code="lbl.locality" /></label>
								<div class="col-sm-2 add-margin">
									<form:select name="localitys" id="localitys" path="siteDetail[0].locationBoundary"
										cssClass="form-control" cssErrorClass="form-control error">
										<form:option value="">
									--select--
								</form:option>
										<form:options items="${localitys}" itemValue="id"
											itemLabel="name" />

									</form:select>
							
							</div>
						</div>
								</div>
								
								<div class="form-group">
								<label class="col-sm-3 control-label text-right"> Elelction Ward
								</label>
								<div class="col-sm-3 add-margin">
		<form:select path="siteDetail[0].electionBoundary" data-first-option="false" id="zone"
			cssClass="form-control" >
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${electionwards}"  itemValue="id"
						itemLabel="name" />
		</form:select>
		<form:errors path="siteDetail[0].electionBoundary" cssClass="add-margin error-msg" />
	</div>
	</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right">Door Number </label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation" maxlength="50"
			id="plotdoornumber" path="siteDetail[0].plotdoornumber" />
		<form:errors path="siteDetail[0].plotdoornumber"
			cssClass="add-margin error-msg" />
	</div>

	<label class="col-sm-2 control-label text-right">Plot Land Mark</label>
	<div class="col-sm-2 add-margin">
		<form:input class="form-control patternvalidation" maxlength="50"
			id="plotlandmark" path="siteDetail[0].plotlandmark" />
		<form:errors path="siteDetail[0].plotlandmark"
			cssClass="add-margin error-msg" />
	</div>

</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right">Plot Number </label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="number" maxlength="50" id="plotnumber"
			path="siteDetail[0].plotnumber" />
		<form:errors path="siteDetail[0].plotnumber"
			cssClass="add-margin error-msg" />
	</div>

	<label class="col-sm-2 control-label text-right">Plot
		SurveyNumber</label>
	<div class="col-sm-2 add-margin">
		<form:input class="form-control patternvalidation" maxlength="50"
			id="plotsurveynumber" path="siteDetail[0].plotsurveynumber" />
		<form:errors path="siteDetail[0].plotsurveynumber"
			cssClass="add-margin error-msg" />
	</div>

</div>



<div class="form-group">
	<label class="col-sm-3 control-label text-right">SurveyNumberType
	</label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="string" maxlength="50" id="surveynumberType"
			path="siteDetail[0].surveynumberType" />
		<form:errors path="siteDetail[0].surveynumberType"
			cssClass="add-margin error-msg" />
	</div>

	<label class="col-sm-2 control-label text-right">Old
		SurveyNumber</label>
	<div class="col-sm-2 add-margin">
		<form:input class="form-control patternvalidation" maxlength="50"
			id="oldSurveyNumber" path="siteDetail[0].oldSurveyNumber" />
		<form:errors path="siteDetail[0].oldSurveyNumber"
			cssClass="add-margin error-msg" />
	</div>

</div>


<div class="form-group">
	<label class="col-sm-3 control-label text-right">Street
		Address1 </label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="string" id="streetaddress1"
			path="siteDetail[0].streetaddress1" />
		<form:errors path="siteDetail[0].streetaddress1"
			cssClass="add-margin error-msg" />
	</div>

	<label class="col-sm-2 control-label text-right">Street
		Address2 </label>
	<div class="col-sm-2 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="string" id="streetaddress2"
			path="siteDetail[0].streetaddress2" />
		<form:errors path="siteDetail[0].streetaddress2"
			cssClass="add-margin error-msg" />
	</div>

</div>



<div class="form-group">
	<label class="col-sm-3 control-label text-right">Area </label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="string" id="area" path="siteDetail[0].area" />
		<form:errors path="siteDetail[0].area" cssClass="add-margin error-msg" />
	</div>

	<label class="col-sm-2 control-label text-right">CityTown</label>
	<div class="col-sm-2 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="string" id="citytown" path="siteDetail[0].citytown" />
		<form:errors path="siteDetail[0].citytown"
			cssClass="add-margin error-msg" />
	</div>

</div>


<div class="form-group">
	<label class="col-sm-3 control-label text-right">District </label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="string" id="district" path="siteDetail[0].district" />
		<form:errors path="siteDetail[0].district"
			cssClass="add-margin error-msg" />
	</div>

	<label class="col-sm-2 control-label text-right">Taluk</label>
	<div class="col-sm-2 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="string" id="taluk" path="siteDetail[0].taluk" />
		<form:errors path="siteDetail[0].taluk"
			cssClass="add-margin error-msg" />
	</div>

</div>


<div class="form-group">
	<label class="col-sm-3 control-label text-right">State </label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="string" id="state" path="siteDetail[0].state" />
		<form:errors path="siteDetail[0].state"
			cssClass="add-margin error-msg" />
	</div>

	<label class="col-sm-2 control-label text-right">Site Pincode</label>
	<div class="col-sm-2 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="string" id="sitePincode"
			path="siteDetail[0].sitePincode" />
		<form:errors path="siteDetail[0].sitePincode"
			cssClass="add-margin error-msg" />
	</div>

</div>



<div class="form-group">
	<label class="col-sm-3 control-label text-right">Nature Of
		Ownership </label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="string" id="natureofOwnership"
			path="siteDetail[0].natureofOwnership" />
		<form:errors path="siteDetail[0].natureofOwnership"
			cssClass="add-margin error-msg" />
	</div>

	<label class="col-sm-2 control-label text-right">Extent inSqmt</label>
	<div class="col-sm-2 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="number" id="extentinsqmts"
			path="siteDetail[0].extentinsqmts" />
		<form:errors path="siteDetail[0].extentinsqmts"
			cssClass="add-margin error-msg" />
	</div>

</div>





<div class="form-group">
	<label class="col-sm-3 control-label text-right">Registrar
		Office</label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="string" id="registrarOffice"
			path="siteDetail[0].registrarOffice" />
		<form:errors path="siteDetail[0].registrarOffice"
			cssClass="add-margin error-msg" />
	</div>

	<label class="col-sm-2 control-label text-right">Nearest
		Building Number</label>
	<div class="col-sm-2 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="number" id="nearestbuildingnumber"
			path="siteDetail[0].nearestbuildingnumber" />
		<form:errors path="siteDetail[0].nearestbuildingnumber"
			cssClass="add-margin error-msg" />
	</div>

</div>



<div class="form-group">
	<label class="col-sm-3 control-label text-right">Subdivision
		Number</label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="string" id="subdivisionNumber"
			path="siteDetail[0].subdivisionNumber" />
		<form:errors path="siteDetail[0].subdivisionNumber"
			cssClass="add-margin error-msg" />
	</div>

	<label class="col-sm-2 control-label text-right">Approved
		LayoutDetail</label>
	<div class="col-sm-2 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="number" id="approvedLayoutDetail"
			path="siteDetail[0].approvedLayoutDetail" />
		<form:errors path="siteDetail[0].approvedLayoutDetail"
			cssClass="add-margin error-msg" />
	</div>

</div>


<div class="form-group">

<label class="col-sm-3 control-label text-right">SetBackFront(SBF)</label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="number" id="setBackFront"
			path="siteDetail[0].setBackFront" />
		<form:errors path="siteDetail[0].setBackFront"
			cssClass="add-margin error-msg" />
	</div>
	<label class="col-sm-2 control-label text-right">SetBackRear(SBR)</label>
	<div class="col-sm-2 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="number" id="setBackRear"
			path="siteDetail[0].setBackRear" />
		<form:errors path="siteDetail[0].setBackRear"
			cssClass="add-margin error-msg" />
	</div>

	

</div>
<div class="form-group">

	<label class="col-sm-3 control-label text-right">SetBackSide1(SBS2)</label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="number" id="setBackSide2"
			path="siteDetail[0].setBackSide2" />
		<form:errors path="siteDetail[0].setBackSide2"
			cssClass="add-margin error-msg" />
	</div>
<label class="col-sm-2 control-label text-right">SetBackSide1(SBS1)</label>
	<div class="col-sm-2 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="number" id="setBackSide1"
			path="siteDetail[0].setBackSide1" />
		<form:errors path="siteDetail[0].setBackSide1"
			cssClass="add-margin error-msg" />
	</div>
<div class="form-group">

	<div class="form-group" id="statusdiv">
		<label class="col-sm-3 control-label text-right">Encroachment
			IssuesPresent</label>
		<div class="col-sm-3 add-margin">
			<form:checkbox id="encroachmentIssuesPresent"
				path="siteDetail[0].encroachmentIssuesPresent"
				value="encroachmentIssuesPresent" />
			<form:errors path="siteDetail[0].encroachmentIssuesPresent" />
		</div>
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right">Encroachment
		Remarks</label>
	<div class="col-sm-3 add-margin">
		<form:textarea class="form-control patternvalidation"
			data-pattern="string" maxlength="128" id="encroachmentRemarks"
			path="siteDetail[0].encroachmentRemarks"  />
		<form:errors path="siteDetail[0].encroachmentRemarks"
			cssClass="add-margin error-msg" />
	</div>

</div>