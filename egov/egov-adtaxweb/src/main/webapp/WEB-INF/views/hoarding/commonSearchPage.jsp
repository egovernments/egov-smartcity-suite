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
		<div class="panel-body">
	               <div class="form-group">
                        <label class="col-sm-3 control-label text-right"><spring:message code="lbl.agency.name"/></label>
                        <div class="col-sm-3 add-margin">
                            <input type="text" id="agencyTypeAhead"  class="form-control typeahead" autocomplete="off">
							<form:hidden path="agency" id="agencyId" value="${hoardingSearch.agency}" />
                        </div>
                        <label class="col-sm-2 control-label text-right"><spring:message code="lbl.hoarding.no"/></label>
                        <div class="col-sm-3 add-margin">
                            <form:input type="text" class="form-control" id="advertisementNumber" path="advertisementNumber"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label text-right"><spring:message code="lbl.application.no"/></label>
                        <div class="col-sm-3 add-margin">
                            <form:input type="text" class="form-control" id="applicationNumber" path="applicationNumber"/>
                        </div>
                        <label class="col-sm-2 control-label text-right"><spring:message code="lbl.hoarding.permission.no"/></label>
                        <div class="col-sm-3 add-margin">
                            <form:input type="text" class="form-control" id="permissionNumber" path="permissionNumber"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label text-right"><spring:message code="lbl.application.frm.date"/></label>
                        <div class="col-sm-3 add-margin">
                            <form:input type="text" class="form-control datepicker" id="applicationFromDate" path="applicationFromDate"/>
                        </div>
                        <label class="col-sm-2 control-label text-right"><spring:message code="lbl.application.to.date"/></label>
                        <div class="col-sm-3 add-margin">
                            <form:input type="text" class="form-control datepicker" id="applicationToDate" path="applicationToDate"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label text-right"><spring:message code="lbl.hoarding.category"/></label>
                        <div class="col-sm-3 add-margin">
                        	 <form:select path="category" id="categories" cssClass="form-control" 
							cssErrorClass="form-control error">
								<form:option value=""><spring:message code="lbl.select" /></form:option>
									<c:forEach items="${hoardingcategories}" var="hoardingcategory">
                       				<option value="${hoardingcategory.id}"> ${hoardingcategory.name}</option>
                       			</c:forEach>
							</form:select>
						 <form:errors path="category" cssClass="error-msg"/>
                        
                        </div>
                        <label class="col-sm-2 control-label text-right"><spring:message code="lbl.hoarding.subcategory"/></label>
                        <div class="col-sm-3 add-margin">
		                   	<form:select path="subCategory" id="subcategories" cssClass="form-control" 
							cssErrorClass="form-control error">
								<form:option value=""><spring:message code="lbl.select" /></form:option>
								</form:select>
							<form:errors path="subCategory" cssClass="error-msg"/>
                        </div>
                    </div>
                    <div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message code="lbl.locality"/></label>
					    <div class="col-sm-3 add-margin">
					    	<form:select path="adminBoundryParent" id="zoneList" cssClass="form-control" 
							cssErrorClass="form-control error">
								<form:option value=""><spring:message code="lbl.select" /></form:option>
								<c:forEach items="${localities}" var="zone">
					   				<option value="${zone.id}"> ${zone.name}</option>
					   			</c:forEach>
							</form:select>
					  		
					    </div>
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.ward"/></label>
					    <div class="col-sm-3 add-margin">
							
							<form:select path="adminBoundry" id="wardlist" cssClass="form-control" 
							cssErrorClass="form-control error">
								<form:option value=""><spring:message code="lbl.select" /></form:option>
							</form:select>
							
						</div>
					</div> 
                    <div class="form-group">
                       <label class="col-sm-3 control-label text-right"><spring:message code="lbl.ri.no"/></label>
                        <div class="col-sm-3 add-margin">
                        	<form:select path="revenueInspector" id="revenueinspector" cssClass="form-control" 
							cssErrorClass="form-control error" >
								<form:option value=""><spring:message code="lbl.select" /></form:option>
								<form:options items="${revenueinspectors}" itemLabel="name" itemValue="id" />
							</form:select>
							<form:errors path="revenueInspector" cssClass="error-msg"/>
                        </div>
                         <label class="col-sm-2 control-label text-right"><spring:message code="lbl.owner.Details"/></label>
                        <div class="col-sm-3 add-margin">
                      
                            <form:input type="text" class="form-control" id="ownerDetail" path="ownerDetail"/>
							<form:errors path="ownerDetail" cssClass="error-msg"/>
                        </div>	
                    </div>
                    </div>