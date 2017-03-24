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

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<div class="form-group">
	<label class="col-sm-2 control-label text-right"> <spring:message
			code="lbl.fromDate" />:<span class="mandatory"></span>
	</label>
	<div class="col-sm-3 add-margin">
		<input type="text" name="fromDate"
			class="form-control datepicker" 
			id="fromDate" data-inputmask="'mask': 'd/m/y'"
			onblur="onchnageofDate()" required="required"/>
	</div>
	<label class="col-sm-2 control-label text-right"> <spring:message
			code="lbl.toDate" />:<span class="mandatory"></label>
	<div class="col-sm-3 add-margin">
		<input type="text" name="toDate"
			class="form-control datepicker today" 
			id="toDate" data-inputmask="'mask': 'd/m/y'" required="required" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.officerincharge" />:</label>
	<div class="col-sm-3 add-margin">
			  <input id="positionName" type="text" class="form-control typeahead" placeholder="" autocomplete="off" />
               <input type="hidden" name="officerIncharge" id="officerIncharge"/>
	</div>
	<div class="col-sm-3 add-margin"></div>
</div>

<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" />
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>