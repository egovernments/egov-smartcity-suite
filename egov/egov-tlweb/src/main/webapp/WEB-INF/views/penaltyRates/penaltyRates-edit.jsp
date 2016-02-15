<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
  
<form:form role="form" action="/tl/penaltyRates/update" modelAttribute="penaltyRates" commandName="penaltyRates" id="penaltyform"  cssClass="form-horizontal form-groups-bordered">
	<div class="row">
    	<div class="col-md-12">
      		<div class="panel panel-primary" data-collapsed="0">
		        <div class="panel-heading">
		          <div class="panel-title"><spring:message code="title.penaltyRate"/></div>
		        </div>
        		<div class="panel-body">
        		<c:if test="${not empty message}">
	        		<div class="alert alert-danger">
	            		<strong><spring:message code='${message}'/></strong>
	          		</div>
	          	</c:if>
            		<div class="form-group">
            			<label class="col-sm-3 control-label text-right"><spring:message code="lbl.licenseAppType" />
            				<span class="mandatory"></span> </label>
            			<div class="col-sm-3 add-margin">
			              <form:select path="licenseAppType" id="licenseAppType" cssClass="form-control"
			                cssErrorClass="form-control error" readonly="readonly" required="required" >
			                <form:option value="">
			                  <spring:message code="lbl.select" />
			                </form:option>
			                <form:options items="${licenseAppTypes}" itemValue="id" itemLabel="name" />
			              </form:select>
            			</div>
          			</div>
		          	<div class="form-group">
		            	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.from" /> </label>
			            <div class="col-sm-3 add-margin">
			              <form:input path="fromRange" id="fromRange" class="form-control daysvalidate" size="10" pattern="-?\d*" required="required" />
			            </div>
		            	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.to" /> </label>
			            <div class="col-sm-3 add-margin">
			              <form:input path="toRange" id="toRange" class="form-control text-right daysvalidate" size="10" pattern="-?\d*" required="required"/>
			            </div>
		          	</div>
		          	 <input type="hidden" name="penaltyRates" value="${penaltyRates.id}" />
          			<div class="form-group nonfindata">
            			<label class="col-sm-3 control-label text-right"><spring:message code="lbl.rate" /> </label>
			            <div class="col-sm-3 add-margin">
			              <form:input path="rate" id="rate" class="form-control text-right patternvalidation" required="required"  data-pattern="decimalValue" />
			              <form:errors path="rate" cssClass="error-msg" />
			            </div>
          			</div>
        		</div>
  			</div>
 		</div>
  	</div>
   	<div class="form-group">
	    <div class="text-center">
	      <button type='submit' class='btn btn-primary' id="buttonSubmit">
	        <spring:message code='lbl.update' />
	      </button>
	      <a href='javascript:void(0)' class='btn btn-default' onclick='self.close()'><spring:message code='lbl.close' /></a>
	    </div>
  	</div>
</form:form>
<script type="text/javascript" src="<c:url value='/resources/app/js/penaltyRates.js'/>"></script>