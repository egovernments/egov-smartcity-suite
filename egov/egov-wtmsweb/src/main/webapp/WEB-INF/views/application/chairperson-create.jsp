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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
 <form:form method ="post" action="" class="form-horizontal form-groups-bordered"  id="chairPersonDetailsform"
			cssClass="form-horizontal form-groups-bordered" modelAttribute="chairPerson"
			enctype="multipart/form-data">
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading">
						</div>
						<div class="panel-body custom-form">
							<div class="form-group">
								<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.chairpersonname" />
								<span class="mandatory"/></label>
								<div class="col-sm-3 add-margin">
									<form:input class="form-control" path="name" title="space is not allowed as the first letter" 
									pattern="^[^-\s][a-zA-Z0-9_\s-]+$" maxlength="32" id ="name" required="required"/>
								</div>
								
								<label class="col-sm-2 control-label text-right"><spring:message code="lbl.isActive" /></label>
								<div class="col-sm-3 add-margin">
									<form:checkbox path="active" id="isActive" checked="checked"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.effective.fromdate" />:<span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:input  path="fromDate" class="form-control datepicker" 
										id="fromDate" data-inputmask="'mask': 'd/m/y'" required="required" />
									<form:errors path="fromDate" cssClass="add-margin error-msg" />
								</div>
								
								<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.effective.todate" /><span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:input  path="toDate" class="form-control datepicker" id="toDate" 
										data-inputmask="'mask': 'd/m/y'" required="required" />
									<form:errors path="toDate" cssClass="add-margin error-msg" />
								</div>						
							</div>
							<div class="form-group text-center" >
								<input type="submit" value="Save" class="btn btn-primary" id="buttonid"></button>
								<button type="reset" class="btn btn-default"><spring:message code="lbl.reset" /></button>
								<a onclick="self.close()" class="btn btn-default" href="javascript:void(0)"><spring:message code="lbl.close"/></a>
							</div>
						</div>
					</div>
 					
		</form:form>			
				<script src="<cdn:url value='/resources/js/app/chairperson-details.js?rnd=${app_release_no}'/>"></script>
	            <script src="<cdn:url value='/resources/js/app/helper.js?rnd=${app_release_no}'/>"></script>
	            