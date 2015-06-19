<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
		<spring:message code="lbl.connection.details" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.connectiontype" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:select name="connectionType" path=""
			data-first-option="false" cssClass="form-control">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${connectionTypes}" />
		</form:select>
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.usagetype" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:select name="usageType" path="" data-first-option="false"
			cssClass="form-control">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${usageTypes}" itemValue="id"
				itemLabel="name" />
		</form:select>
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.category" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:select name="category" path="" data-first-option="false"
			cssClass="form-control">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${connectionCategories}" itemValue="id"
				itemLabel="name" />
		</form:select>
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.watersourcetype" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:select name="waterSource" path="" data-first-option="false"
			cssClass="form-control">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${waterSourceTypes}" itemValue="id"
				itemLabel="waterSourceType" />
		</form:select>
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.hscpipesize.inches" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:select name="pipeSize" path="" data-first-option="false"
			cssClass="form-control">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${pipeSizes}" itemValue="id"
				itemLabel="code" />
		</form:select>							
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.sumpcapacity.litres" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control">
	</div>
</div>
					