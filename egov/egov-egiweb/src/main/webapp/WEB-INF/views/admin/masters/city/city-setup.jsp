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

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<div class="row" id="page-content">
    <div class="col-md-12">
        <c:if test="${not empty message}">
            <div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
        </c:if>
        <form:form method="post" class="form-horizontal form-groups-bordered" modelAttribute="city"
                   id="cityForm" enctype="multipart/form-data">
            <div class="panel panel-primary" data-collapsed="0">
                <div class="panel-heading">
                    <ul class="nav nav-tabs" id="settingstab">
                        <li class="active"><a data-toggle="tab" href="#citysetup"
                                              data-tabidx=0><spring:message code="tab.city.info"/></a></li>
                        <li><a data-toggle="tab" href="#preferences" data-tabidx=1><spring:message
                                code="tab.corp.info"/></a></li>
                        <li><a data-toggle="tab" href="#configuration" data-tabidx=2><spring:message
                                code="tab.city.config"/></a></li>
                    </ul>
                </div>

                <div class="panel-body custom-form">
                    <div class="tab-content">
                        <div class="tab-pane fade in active" id="citysetup">
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.city.name"/><span class="mandatory"></span></label>
                                <c:set var="helptext">
                                    <spring:message code="help.city.name" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <form:input path="name" id="name" type="text"
                                                data-toggle="popover" data-trigger="focus" data-placement="right"
                                                data-content="${helptext}"
                                                class="form-control is_valid_alphabet" placeholder="" autocomplete="off"
                                                required="required" maxlength="256"/>
                                    <form:errors path="name" cssClass="add-margin error-msg"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.city.local.name"/></label>
                                <c:set var="helptext">
                                    <spring:message code="help.city.local.name" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <form:input path="localName" id="localName" type="text"
                                                data-toggle="popover" data-trigger="focus" data-placement="right"
                                                data-content="${helptext}" maxlength="256"
                                                class="form-control is_valid_alphabet" placeholder="" autocomplete="off"/>
                                    <form:errors path="localName" cssClass="add-margin error-msg"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.city.code"/><span class="mandatory"></span></label>
                                <c:set var="helptext">
                                    <spring:message code="help.city.code" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <form:input path="code" id="code" type="text"
                                                data-toggle="popover" data-trigger="focus" data-placement="right"
                                                data-content="${helptext}" maxlength="4"
                                                class="form-control patternvalidation" data-pattern="masterCode" placeholder="" autocomplete="off"
                                                required="required"/>
                                    <form:errors path="code" cssClass="add-margin error-msg"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.city.lgd.code"/><span class="mandatory"></span></label>
                                <c:set var="helptext">
                                    <spring:message code="help.city.ldg.code" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <form:input path="lgdCode" id="lgdCode" type="text"
                                                data-toggle="popover" data-trigger="focus" data-placement="right"
                                                data-content="${helptext}" maxlength="10"
                                                class="form-control patternvalidation" data-pattern="masterCode" placeholder="" autocomplete="off"
                                                required="required"/>
                                    <form:errors path="lgdCode" cssClass="add-margin error-msg"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.city.domain.url"/><span class="mandatory"></span></label>
                                <c:set var="helptext">
                                    <spring:message code="help.city.domain.url" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <form:input path="domainURL" id="domainURL" type="text"
                                                data-toggle="popover" data-trigger="focus" data-placement="right"
                                                data-content="${helptext}" maxlength="128"
                                                class="form-control" placeholder="" autocomplete="off"
                                                required="required"/>
                                    <form:errors path="domainURL" cssClass="add-margin error-msg"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.city.district.name"/><span class="mandatory"></span></label>
                                <c:set var="helptext">
                                    <spring:message code="help.city.district.name" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <form:input path="districtName" id="districtName" type="text"
                                                data-toggle="popover" data-trigger="focus" data-placement="right"
                                                data-content="${helptext}" maxlength="50"
                                                class="form-control is_valid_alphabet" placeholder="" autocomplete="off"
                                                required="required"/>
                                    <form:errors path="districtName"
                                                 cssClass="add-margin error-msg"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.city.district.code"/><span class="mandatory"></span></label>
                                <c:set var="helptext">
                                    <spring:message code="help.city.district.code" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <form:input path="districtCode" id="districtCode" type="text"
                                                data-toggle="popover" data-trigger="focus" data-placement="right"
                                                data-content="${helptext}" maxlength="10"
                                                class="form-control patternvalidation" data-pattern="masterCode" placeholder="" autocomplete="off"
                                                required="required"/>
                                    <form:errors path="districtCode"
                                                 cssClass="add-margin error-msg"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.city.district.lgd.code"/><span class="mandatory"></span></label>
                                <c:set var="helptext">
                                    <spring:message code="help.city.district.ldg.code" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <form:input path="districtLGDCode" id="districtLGDCode" type="text"
                                                data-toggle="popover" data-trigger="focus" data-placement="right"
                                                data-content="${helptext}" maxlength="10"
                                                class="form-control patternvalidation" data-pattern="masterCode" placeholder="" autocomplete="off"
                                                required="required"/>
                                    <form:errors path="districtLGDCode" cssClass="add-margin error-msg"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.city.region.name"/></label>
                                <c:set var="helptext">
                                    <spring:message code="help.city.region.name" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <form:input path="regionName" id="regionName" type="text"
                                                data-toggle="popover" data-trigger="focus" data-placement="right"
                                                data-content="${helptext}" maxlength="50"
                                                class="form-control is_valid_alphabet" placeholder="" autocomplete="off"/>
                                    <form:errors path="regionName" cssClass="add-margin error-msg"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.city.grade"/></label>
                                <c:set var="helptext">
                                    <spring:message code="help.city.grade" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <form:input path="grade" id="grade" type="text"
                                                data-toggle="popover" data-trigger="focus" data-placement="right"
                                                data-content="${helptext}" maxlength="50"
                                                class="form-control" placeholder="" autocomplete="off"/>
                                    <form:errors path="grade" cssClass="add-margin error-msg"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.city.active"/><span class="mandatory"></span> </label>

                                <div class="col-sm-1 col-xs-12 add-margin">
                                    <form:radiobutton path="active" id="active_yes" value="yes"
                                                      checked="true"/>
                                    <label><spring:message code="lbl.yes"/></label>
                                </div>
                                <div class="col-sm-1 col-xs-12 add-margin">
                                    <form:radiobutton path="active" id="active_no" value="no"/>
                                    <label><spring:message code="lbl.no"/></label>
                                </div>
                            </div>
                        </div>

                        <div id="preferences" class="tab-pane fade">
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message code="lbl.city.logo"/></label>
                                <c:set var="helptext">
                                    <spring:message code="help.corp.logo" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <img id="imglogo" height="60" width="60"
                                         src="/egi/downloadfile?fileStoreId=${city.preferences.municipalityLogo.fileStoreId}&moduleName=ADMIN">

                                    <input type="file" name="logo" id="logo" accept="image/*" data-accepts="${allowedFileExt}"
                                           data-size="${maxFileSize}"
                                           class="form-control" placeholder="" autocomplete="off" data-toggle="popover"
                                           data-trigger="focus" data-placement="right" data-content="${helptext}"/>
                                    <form:errors path="preferences.municipalityLogo" cssClass="add-margin error-msg"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.corp.name"/><span class="mandatory"></span></label>
                                <c:set var="helptext">
                                    <spring:message code="help.corp.name" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <form:input path="preferences.municipalityName" id="municipalityName"
                                                type="text" data-toggle="popover" data-trigger="focus" data-placement="right"
                                                data-content="${helptext}" cssClass="form-control is_valid_alphabet" placeholder=""
                                                autocomplete="off" required="required" maxlength="50"/>
                                    <form:errors path="preferences.municipalityName"
                                                 cssClass="add-margin error-msg"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.corp.address"/></label>
                                <c:set var="helptext">
                                    <spring:message code="help.corp.address" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <form:textarea path="preferences.municipalityAddress"
                                                   id="municipalityAddress" type="text" data-toggle="popover" data-trigger="focus" data-placement="right"
                                                   data-content="${helptext}" cssClass="form-control"
                                                   placeholder="" autocomplete="off" rows="5" maxlength="200"/>
                                    <form:errors path="preferences.municipalityAddress"
                                                 cssClass="add-margin error-msg"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.corp.contact.no"/></label>
                                <c:set var="helptext">
                                    <spring:message code="help.corp.contact.no" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <form:input path="preferences.municipalityContactNo"
                                                id="municipalityContactNo" type="text" data-toggle="popover" data-trigger="focus" data-placement="right"
                                                data-content="${helptext}" cssClass="form-control"
                                                placeholder="" autocomplete="off" maxlength="20"/>
                                    <form:errors path="preferences.municipalityContactNo"
                                                 cssClass="add-margin error-msg"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.corp.contact.email"/></label>
                                <c:set var="helptext">
                                    <spring:message code="help.corp.contact.email" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <form:input path="preferences.municipalityContactEmail"
                                                id="municipalityContactEmail" type="text"
                                                data-toggle="popover" data-trigger="focus" data-placement="right"
                                                data-content="${helptext}" maxlength="50"
                                                cssClass="form-control" placeholder="" autocomplete="off"/>
                                    <form:errors path="preferences.municipalityContactEmail"
                                                 cssClass="add-margin error-msg"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.corp.callcenter.no"/></label>
                                <c:set var="helptext">
                                    <spring:message code="help.corp.callcenter.no" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <form:input path="preferences.municipalityCallCenterNo"
                                                id="municipalityCallCenterNo" type="text"
                                                data-toggle="popover" data-trigger="focus" data-placement="right"
                                                data-content="${helptext}" maxlength="20"
                                                cssClass="form-control" placeholder="" autocomplete="off"/>
                                    <form:errors path="preferences.municipalityCallCenterNo"
                                                 cssClass="add-margin error-msg"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.corp.gis.location.link"/></label>
                                <c:set var="helptext">
                                    <spring:message code="help.corp.gis.location.link" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <form:input path="preferences.municipalityGisLocation"
                                                id="municipalityOfficeGisLocation" type="text"
                                                data-toggle="popover" data-trigger="focus" data-placement="right"
                                                data-content="${helptext}" maxlength="100"
                                                cssClass="form-control" placeholder="" autocomplete="off"/>
                                    <form:errors path="preferences.municipalityGisLocation" cssClass="add-margin error-msg"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.corp.fb.link"/></label>
                                <c:set var="helptext">
                                    <spring:message code="help.corp.fb.link" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <form:input path="preferences.municipalityFacebookLink" id="facebookLink"
                                                type="text" cssClass="form-control" placeholder=""
                                                data-toggle="popover" data-trigger="focus" data-placement="right"
                                                data-content="${helptext}" maxlength="100" autocomplete="off"/>
                                    <form:errors path="preferences.municipalityFacebookLink" cssClass="add-margin error-msg"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.corp.twitter.link"/></label>
                                <c:set var="helptext">
                                    <spring:message code="help.corp.twitter.link" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <form:input path="preferences.municipalityTwitterLink" id="twitterLink"
                                                type="text" cssClass="form-control" placeholder=""
                                                data-toggle="popover" data-trigger="focus" data-placement="right"
                                                data-content="${helptext}" maxlength="100" autocomplete="off"/>
                                    <form:errors path="preferences.municipalityTwitterLink" cssClass="add-margin error-msg"/>
                                </div>
                            </div>
                        </div>
                        <div id="configuration" class="tab-pane fade">
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.city.lat" htmlEscape="false"/></label>
                                <c:set var="helptext">
                                    <spring:message code="help.city.coord.lat" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <form:input path="latitude" id="latitude" type="text"
                                                data-toggle="popover" data-trigger="focus" data-placement="right"
                                                data-content="${helptext}"
                                                cssClass="form-control" placeholder="" autocomplete="off"/>
                                    <form:errors path="latitude" cssClass="add-margin error-msg"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.city.lng" htmlEscape="false"/></label>
                                <c:set var="helptext">
                                    <spring:message code="help.city.coord.lng" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <form:input path="longitude" id="longitude" type="text"
                                                data-toggle="popover" data-trigger="focus" data-placement="right"
                                                data-content="${helptext}"
                                                cssClass="form-control" placeholder="" autocomplete="off"/>
                                    <form:errors path="longitude" cssClass="add-margin error-msg"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.city.recaptcha.pri"/><span class="mandatory"></span></label>
                                <c:set var="helptext">
                                    <spring:message code="help.city.recaptcha.pvt.key" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <input style="display:none" type="password"/>
                                    <form:input path="preferences.recaptchaPK" id="recaptchaPK" type="password"
                                                data-toggle="popover" data-trigger="focus" data-placement="right"
                                                data-content="${helptext}" maxlength="64"
                                                class="form-control readonly-pwd" placeholder="" autocomplete="off"
                                                required="required" onfocus="this.removeAttribute('readonly');" readonly="true"/>
                                    <form:errors path="preferences.recaptchaPK" cssClass="add-margin error-msg"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.city.recaptcha.pub"/><span class="mandatory"></span></label>
                                <c:set var="helptext">
                                    <spring:message code="help.city.recaptcha.pub.key" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <input style="display:none" type="password"/>
                                    <form:input path="preferences.recaptchaPub" id="recaptchaPub" type="password"
                                                data-toggle="popover" data-trigger="focus" data-placement="right"
                                                data-content="${helptext}" maxlength="64"
                                                class="form-control readonly-pwd" placeholder="" autocomplete="off"
                                                required="required" onfocus="this.removeAttribute('readonly');" readonly="true"/>

                                    <form:errors path="preferences.recaptchaPub" cssClass="add-margin error-msg"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.city.google.api.key"/><span class="mandatory"></span></label>
                                <c:set var="helptext">
                                    <spring:message code="help.city.google.api.key" htmlEscape="false"/>
                                </c:set>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <input style="display:none" type="password"/>
                                    <form:input path="preferences.googleApiKey" id="googleApiKey" type="password"
                                                data-toggle="popover" data-trigger="focus" data-placement="right"
                                                data-content="${helptext}" maxlength="50"
                                                class="form-control readonly-pwd" placeholder="" autocomplete="off"
                                                required="required" onfocus="this.removeAttribute('readonly');" readonly="true"/>
                                    <form:errors path="preferences.googleApiKey" cssClass="add-margin error-msg"/>
                                </div>

                            </div>
                        </div>
                    </div>
                    <div>
                        <div class="col-sm-12" style="padding-top: 7px">
                            <label class="col-sm-12 error-msg"><spring:message
                                    code="imp.notice"/></label>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="text-center">
                    <button type="submit" class="btn btn-primary" id="submitform">
                        <spring:message code="lbl.submit"/>
                    </button>
                    <button type="button" class="btn btn-default"
                            onclick="window.location.reload()">
                        <spring:message code="lbl.reset"/>
                    </button>
                    <button type="button" class="btn btn-default" data-dismiss="modal"
                            onclick="window.close();">
                        <spring:message code="lbl.close"/>
                    </button>
                </div>
            </div>
        </form:form>
    </div>
</div>
<style type="text/css">
    .popover {
        min-width: 200px;
    }
</style>
<script src="<cdn:url value='/resources/js/app/citysetup.js?rnd=${app_release_no}'/>" type="text/javascript"></script>
