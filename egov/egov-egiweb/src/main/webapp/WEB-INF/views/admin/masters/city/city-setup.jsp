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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<div class="row" id="page-content">
	<div class="col-md-12">
				 <c:if test="${not empty message}">
                    <div class="alert alert-success" role="alert">${message}</div>
                </c:if>
				<form:form action="update" method ="post" class="form-horizontal form-groups-bordered" modelAttribute="city" id="cityForm" enctype="multipart/form-data">
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading">
							<div class="panel-title">
								<strong><spring:message code="title.city.setup"/></strong>
							</div>
						</div> 

						<div class="panel-body custom-form">
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.city.name"/><span class="mandatory"></span></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<form:input path="name" id="name" type="text" class="form-control low-width" placeholder="" autocomplete="off" required="required"/>
		                            <form:errors path="name" cssClass="add-margin error-msg"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.city.local.name"/></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<form:input path="localName" id="localName" type="text" class="form-control low-width" placeholder="" autocomplete="off"/>
		                            <form:errors path="localName" cssClass="add-margin error-msg"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.city.code"/><span class="mandatory"></span></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<form:input path="code" id="code" type="text" class="form-control low-width" placeholder="" autocomplete="off" required="required"/>
		                            <form:errors path="code" cssClass="add-margin error-msg"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.city.domain.url"/><span class="mandatory"></span></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<form:input path="domainURL" id="domainURL" type="text" class="form-control low-width" placeholder="" autocomplete="off" required="required"/>
		                            <form:errors path="domainURL" cssClass="add-margin error-msg"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.city.district.name"/><span class="mandatory"></span></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<form:input path="districtName" id="districtName" type="text" class="form-control low-width" placeholder="" autocomplete="off" required="required"/>
		                            <form:errors path="districtName" cssClass="add-margin error-msg"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.city.district.code"/><span class="mandatory"></span></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<form:input path="districtCode" id="districtCode" type="text" class="form-control low-width" placeholder="" autocomplete="off" required="required"/>
		                            <form:errors path="districtCode" cssClass="add-margin error-msg"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.city.logo"/></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<img src="/egi/downloadfile?fileStoreId=${city.preferences.logo.fileStoreId}&moduleName=${sessionScope.cityCode}">
									<input type="file" name="logo" id="logo"  class="form-control low-width" placeholder="" autocomplete="off"/>
		                            <form:errors path="preferences.logo" cssClass="add-margin error-msg"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.city.lat"/></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<form:input path="latitude" id="latitude" type="text" cssClass="form-control low-width" placeholder="" autocomplete="off" />
									<form:errors path="latitude" cssClass="add-margin error-msg"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.city.lng"/></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<form:input path="longitude" id="longitude" type="text" cssClass="form-control low-width" placeholder="" autocomplete="off" />
									<form:errors path="longitude" cssClass="add-margin error-msg"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.city.gis.kml"/></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<input type="file" name="gisKML" id="gisKML"  class="form-control low-width" placeholder="" autocomplete="off"/>
		                            <a target="_blank" href="/egi/downloadfile?fileStoreId=${city.preferences.gisKML.fileStoreId}&moduleName=${sessionScope.cityCode}">${city.preferences.gisKML.fileName}</a>
		                            <form:errors path="preferences.gisKML" cssClass="add-margin error-msg"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.city.gis.shape"/></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<input type="file" name="gisShape" id="gisShape"  class="form-control low-width" placeholder="" autocomplete="off"/>
									<a target="_blank" href="/egi/downloadfile?fileStoreId=${city.preferences.gisShape.fileStoreId}&moduleName=${sessionScope.cityCode}">${city.preferences.gisShape.fileName}</a>
		                            <form:errors path="preferences.gisShape" cssClass="add-margin error-msg"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.city.recaptcha.pri"/><span class="mandatory"></span></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<form:input path="recaptchaPK" id="recaptchaPK" type="text" class="form-control low-width" placeholder="" autocomplete="off" required="required"/>
		                            <form:errors path="recaptchaPK" cssClass="add-margin error-msg"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.city.recaptcha.pub"/><span class="mandatory"></span></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<form:input path="recaptchaPub" id="recaptchaPub" type="text" class="form-control low-width" placeholder="" autocomplete="off" required="required"/>
		                            <form:errors path="recaptchaPub" cssClass="add-margin error-msg"/>
								</div>
							</div>
							<div class="form-group">
								<label for="active" class="col-sm-3 control-label"><spring:message code="lbl.city.active" /><span class="mandatory"></span>	</label>
	
								<div class="col-sm-1 col-xs-12 add-margin">
									<form:radiobutton path="active" id="active_yes" value="yes" checked="true"/>
									<label>Yes</label>
								</div>
								<div class="col-sm-1 col-xs-12 add-margin">
									<form:radiobutton path="active" id="active_no" value="no"/>
									<label>No</label>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="text-center">
							<button type="submit" class="btn btn-primary"><spring:message code="lbl.submit"/></button>
					        <button type="button" class="btn btn-default" onclick="window.location.reload()"><spring:message code="lbl.reset"/></button>
					        <button type="button" class="btn btn-default" data-dismiss="modal" onclick="window.close();"><spring:message code="lbl.close"/></button>
						</div>
					</div>
				</form:form>
			</div>
        </div>
