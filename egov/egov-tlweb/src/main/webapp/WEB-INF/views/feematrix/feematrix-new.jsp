<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2016>  eGovernments Foundation
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
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<script src="<cdn:url  value='/resources/js/app/feematrix.js?rnd=${app_release_no}'/>"></script>

<div class="row">
    <div class="col-md-12">
      <div class="panel panel-primary" data-collapsed="0"> 
        <div class="panel-heading">
          <div class="panel-title">FeeMatrix</div>
        </div>
        <div class="panel-body">
          <form:form role="form" action="feematrix/create" modelAttribute="feeMatrix" id="feematrix-new" name="feematrix-new"
            cssClass="form-horizontal form-groups-bordered" enctype="multipart/form-data">
            <div class="form-group">
              <label class="col-sm-3 control-label text-right"><spring:message code="lbl.natureofbusiness" /> <span
                class="mandatory"></span> </label>
              <div class="col-sm-3 add-margin">
                <form:select path="natureOfBusiness" id="natureOfBusiness" cssClass="form-control"   required="required"
                  cssErrorClass="form-control error" >
                  <form:option value="">
                    <spring:message code="lbl.select" />
                  </form:option>
                  <form:options items="${natureOfBusinesss}" itemValue="id" itemLabel="name" />
                </form:select>
              </div>
               <label class="col-sm-2 control-label text-right"><spring:message code="lbl.licenseapptype" /> <span
	              class="mandatory"></span> </label>
	            <div class="col-sm-3 add-margin">
	              <form:select path="licenseAppType" id="licenseAppType" cssClass="form-control"   required="required"
	                cssErrorClass="form-control error">
	                <form:option value="">
	                  <spring:message code="lbl.select" />
	                </form:option>
	                <form:options items="${licenseAppTypes}" itemValue="id" itemLabel="name" />
	              </form:select>
	            </div>
            </div>
                  
                  <div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.licensecategory" /> <span class="mandatory"></span> </label>
						<div class="col-sm-3 add-margin">
							<form:select path="licenseCategory" id="licenseCategory"
								cssClass="form-control" required="required"
								cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${licenseCategorys}" itemValue="id"
									itemLabel="name" />
							</form:select>
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.subcategory" /> <span class="mandatory"></span> </label>
						<div class="col-sm-3 add-margin">
							<form:select path="subCategory" id="subCategory"
										 cssClass="form-control" required="required"
										 cssErrorClass="form-control error">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
							</form:select>
							<label id="subCategory-error" class="error display-hide" for="subCategory">Required</label>
						</div>
					</div>

					<div class="form-group">
	            <label class="col-sm-3 control-label text-right"><spring:message code="lbl.feetype" /> <span
	                class="mandatory"></span> </label>
	              <div class="col-sm-3 add-margin">
	                <form:select path="feeType" id="feeType" cssClass="form-control"   required="required"
	                  cssErrorClass="form-control error">
	                  <form:option value="">
	                    <spring:message code="lbl.select" />
	                  </form:option>
	                  <form:options items="${feeTypes}" itemValue="id" itemLabel="name" />
	                </form:select>
	              </div>
	              <label class="col-sm-2 control-label text-right"><spring:message code="lbl.unitofmeasurement" /> <span
	              class="mandatory"></span> </label>
		            <div class="col-sm-3 add-margin">
		              <form:select path="unitOfMeasurement" id="unitOfMeasurement" cssClass="form-control"   required="required"
		                cssErrorClass="form-control error">
		                <form:option value="">
		                  <spring:message code="lbl.select" />
		                </form:option>
		                <form:options items="${unitOfMeasurements}" itemValue="id" itemLabel="name" />
		              </form:select>
		            </div>
            </div>
            
            <div class="form-group">
              <label class="col-sm-3 control-label text-right"><spring:message code="lbl.rateType" /> </label>
		            <div class="col-sm-3 add-margin">
		            	<form:input id="rateType" path="" class="form-control text-left" maxlength="32" readonly="true"/>
		            </div>
            
              <label class="col-sm-2 control-label text-right"><spring:message code="lbl.financialyear" /> <span
	                class="mandatory"></span> </label>
	                <div class="col-sm-3 add-margin">
	                <form:select path="financialYear" id="financialYear" cssClass="form-control"   required="required"
	                  cssErrorClass="form-control error">
	                  <form:option value="">
	                    <spring:message code="lbl.select" />
	                  </form:option>
	                  <form:options items="${financialYears}" itemValue="id" itemLabel="finYearRange" />
	                </form:select>
	              </div>
            </div>
            
            <div class="form-group text-center">
                   <button type="button" class="btn btn-primary" id="search">Submit</button>
                  <button type="button" class="btn btn-default" data-dismiss="modal" onclick="window.close();"> Close</button>
            </div>
            <c:if test="${hideTemporary}">
            <script>
            $("#natureOfBusiness option:contains('Permanent')").attr('selected', 'selected');
            $('#natureOfBusiness').attr("disabled", true); 
            </script>
            </c:if>
            
            <c:if test="${hideRenew}">
            <script>
            $("#licenseAppType option:contains('New')").attr('selected', 'selected');
            $('#licenseAppType').attr("disabled", true); 
            </script>
            </c:if>
            <div id="resultdiv">
            </div>
          </form:form>
        </div>
      </div>
    </div>
</div>