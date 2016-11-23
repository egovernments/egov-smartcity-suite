<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~      accountability and the service delivery of the government  organizations.
  ~
  ~       Copyright (C) 2016  eGovernments Foundation
  ~
  ~       The updated version of eGov suite of products as by eGovernments Foundation
  ~       is available at http://www.egovernments.org
  ~
  ~       This program is free software: you can redistribute it and/or modify
  ~       it under the terms of the GNU General Public License as published by
  ~       the Free Software Foundation, either version 3 of the License, or
  ~       any later version.
  ~
  ~       This program is distributed in the hope that it will be useful,
  ~       but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~       GNU General Public License for more details.
  ~
  ~       You should have received a copy of the GNU General Public License
  ~       along with this program. If not, see http://www.gnu.org/licenses/ or
  ~       http://www.gnu.org/licenses/gpl.html .
  ~
  ~       In addition to the terms of the GPL license to be adhered to in using this
  ~       program, the following additional terms are to be complied with:
  ~
  ~           1) All versions of this program, verbatim or modified must carry this
  ~              Legal Notice.
  ~
  ~           2) Any misrepresentation of the origin of the material is prohibited. It
  ~              is required that all modified versions of this material be marked in
  ~              reasonable ways as different from the original version.
  ~
  ~           3) This license does not grant any rights to any user of the program
  ~              with regards to rights under trademark law for use of the trade names
  ~              or trademarks of eGovernments Foundation.
  ~
  ~     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="panel panel-primary" data-collapsed="0">
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.shsc.number" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input path="connection.shscNumber" id="shscNumber"
			class="form-control text-left patternvalidation" data-pattern="number" maxlength="10" required="required" />
		<form:errors path="connection.shscNumber" 
			cssClass="add-margin error-msg" />
	</div>
	
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.executiondate" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input path="connection.executionDate" title="Please enter a valid date" class="form-control" pattern="\d{1,2}/\d{1,2}/\d{4}" 
			data-date-end-date="-1d"  id="executionDate"
			data-inputmask="'mask': 'd/m/y'" required="required" />
		<form:errors path="connection.executionDate" cssClass="add-margin error-msg" />
	</div>
</div>
<div class="panel-body">
	<table class="table table-striped table-bordered" id="legacyDemandDetails"  style="display: none;">	
			<thead>
			      <tr>
			        <th class="text-center">Installment</th>
			        <th class="text-center">Tax</th>
					<th class="text-right">Demand<span class="mandatory"></span></th>
					<th class="text-right">Collection</th>
			      </tr>
		    </thead>
		<tbody> 
		</tbody>
		</table>
</div>
</div>	
