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

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<script src="<cdn:url value='/resources/js/app/positionsearch.js' context='/eis'/>"></script>
	<script>

		$(document).ready(function(){
		 <c:if test="${mode == 'saved'}">
	  		$('#position_dept').attr('disabled','true');	
			$('#position_desig').attr('disabled','true');	
			$('#position_name').attr('disabled','true');
			</c:if>
 		});
	
	</script>	
<div class="row">
	<div class="col-md-12">
			<c:if test="${not empty warning}">
				<div class="panel-heading">
								<div class="panel-title view-content">
                	<div >${warning}</div>
                	</div>
				</div>
           		</c:if>
		<form:form id="addPosition" method="post" class="form-horizontal form-groups-bordered"	modelAttribute="position">
		<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading">
						<div class="panel-title">
							<strong><spring:message code="lbl.createPosition" /></strong>
						</div>
					</div>
					<div class="panel-body custom-form">
						<div class="form-group">
							<label class="col-sm-3 control-label"> <spring:message
									code="lbl.department" /><span class="mandatory"></span>
							</label>

							<div class="col-sm-6 add-margin">
								<form:select path="deptDesig.department" id="position_dept" required="required" 
									cssClass="form-control" cssErrorClass="form-control error">
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
									<form:options items="${departments}" itemValue="id"
										itemLabel="name" />
								</form:select>
								<form:errors path="deptDesig.department" cssClass="error-msg" />
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label"> <spring:message
									code="lbl.designation" /><span class="mandatory"></span>
							</label>

							<div class="col-sm-6 add-margin">
								<form:select path="deptDesig.designation" id="position_desig" required="required" 
									cssClass="form-control" cssErrorClass="form-control error">
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
									<form:options items="${designations}" itemValue="id"
										itemLabel="name" />
								</form:select>		
								<form:errors path="deptDesig.designation" cssClass="error-msg" />
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-3 control-label"> <spring:message
									code="lbl.position" /><span class="mandatory"></span>
							</label>
								<div class="col-sm-6 add-margin">
								
								<form:input path="name" id="position_name" required="required" 
									cssClass="form-control is_valid_letters_space_hyphen_underscore"
									cssErrorClass="form-control error"  autocomplete="off"/>
								<form:errors path="name" cssClass="error-msg" />
							</div>
							

						</div>

						<div class="form-group">

							<label for="field-1" class="col-sm-3 control-label"><spring:message
									code="lbl.outsourcePost" /><span class="mandatory"></span>
							</label>

							<div class="col-sm-1 col-xs-12 add-margin">
								<form:radiobutton path="isPostOutsourced" id="position_loc_yes" value="yes" />
								<label>Yes</label>
							</div>
							<div class="col-sm-1 col-xs-12 add-margin">
								<form:radiobutton path="isPostOutsourced" id="position_loc_yno" value="no" checked="true"/>
								<label>No</label>
							</div>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="text-center">
					<c:if test="${mode != 'saved'}">
						<button type="submit" class="btn btn-primary"><spring:message code="lbl.position.submit"/></button>
						</c:if>
						<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close" /></a>
					</div>
				</div>
	</form:form>
	</div>
</div>