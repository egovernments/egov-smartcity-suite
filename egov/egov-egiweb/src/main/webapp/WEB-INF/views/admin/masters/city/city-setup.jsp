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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="row" id="page-content">
	<div class="col-md-12">
		<c:if test="${not empty message}">
			<div class="alert alert-success" role="alert">${message}</div>
		</c:if>
		<form:form action="update" method="post"
			class="form-horizontal form-groups-bordered" modelAttribute="city"
			id="cityForm" enctype="multipart/form-data">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<ul class="nav nav-tabs" id="settingstab">
						<li class="active"><a data-toggle="tab" href="#citysetup"
							data-tabidx=0><spring:message code="tab.city.info" /></a></li>
						<li><a data-toggle="tab" href="#preferences" data-tabidx=1><spring:message
									code="tab.corp.info" /></a></li>
						<li><a data-toggle="tab" href="#configuration" data-tabidx=2><spring:message
									code="tab.city.config" /></a></li>
					</ul>
				</div>

				<div class="panel-body custom-form">
					<div class="tab-content">
						<div class="tab-pane fade in active" id="citysetup">
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.city.name" /><span class="mandatory"></span></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<div class="input-group">
										<form:input path="name" id="name" type="text"
											class="form-control" placeholder="" autocomplete="off"
											required="required" />
										<span class="input-group-btn">
											<button class="btn btn-default btnpopover" type="button"
												data-toggle="popover" data-placement="right" title=""
												data-content="<spring:message code="help.city.name"/>">
												<i class="entypo-help"></i>
											</button>
										</span>
									</div>
									<form:errors path="name" cssClass="add-margin error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.city.local.name" /></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<div class="input-group">
										<form:input path="localName" id="localName" type="text"
											class="form-control" placeholder="" autocomplete="off" />
										<span class="input-group-btn">
											<button class="btn btn-default btnpopover" type="button"
												data-toggle="popover" data-placement="right" title=""
												data-content="<spring:message code="help.city.local.name" />">
												<i class="entypo-help"></i>
											</button>
										</span>
									</div>
									<form:errors path="localName" cssClass="add-margin error-msg" />
								</div>

							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.city.code" /><span class="mandatory"></span></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<div class="input-group">
										<form:input path="code" id="code" type="text"
											class="form-control" placeholder="" autocomplete="off"
											required="required" />
										<span class="input-group-btn">
											<button class="btn btn-default btnpopover" type="button"
												data-toggle="popover" data-placement="right" title=""
												data-content="<spring:message code="help.city.code" />">
												<i class="entypo-help"></i>
											</button>
										</span>
									</div>
									<form:errors path="code" cssClass="add-margin error-msg" />
								</div>

							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.city.domain.url" /><span class="mandatory"></span></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<div class="input-group">
										<form:input path="domainURL" id="domainURL" type="text"
											class="form-control" placeholder="" autocomplete="off"
											required="required" />
										<span class="input-group-btn">
											<button class="btn btn-default btnpopover" type="button"
												data-toggle="popover" data-placement="right" title=""
												data-content="<spring:message code="help.city.domain.url" />">
												<i class="entypo-help"></i>
											</button>
										</span>
									</div>
									<form:errors path="domainURL" cssClass="add-margin error-msg" />
								</div>

							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.city.district.name" /><span class="mandatory"></span></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<div class="input-group">
										<form:input path="districtName" id="districtName" type="text"
											class="form-control" placeholder="" autocomplete="off"
											required="required" />
										<span class="input-group-btn">
											<button class="btn btn-default btnpopover" type="button"
												data-toggle="popover" data-placement="right" title=""
												data-content="<spring:message code="help.city.district.name" />">
												<i class="entypo-help"></i>
											</button>
										</span>
									</div>
									<form:errors path="districtName"
										cssClass="add-margin error-msg" />
								</div>

							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.city.district.code" /><span class="mandatory"></span></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<div class="input-group">
										<form:input path="districtCode" id="districtCode" type="text"
											class="form-control" placeholder="" autocomplete="off"
											required="required" />
										<span class="input-group-btn">
											<button class="btn btn-default btnpopover" type="button"
												data-toggle="popover" data-placement="right" title=""
												data-content="<spring:message code="help.city.district.code" />">
												<i class="entypo-help"></i>
											</button>
										</span>
									</div>
									<form:errors path="districtCode"
										cssClass="add-margin error-msg" />
								</div>

							</div>
							<div class="form-group">
								<label for="active" class="col-sm-3 control-label"><spring:message
										code="lbl.city.active" /><span class="mandatory"></span> </label>

								<div class="col-sm-1 col-xs-12 add-margin">
									<form:radiobutton path="active" id="active_yes" value="yes"
										checked="true" />
									<label>Yes</label>
								</div>
								<div class="col-sm-1 col-xs-12 add-margin">
									<form:radiobutton path="active" id="active_no" value="no" />
									<label>No</label>
								</div>
							</div>
						</div>

						<div id="preferences" class="tab-pane fade">
							<div class="form-group">
								<label class="col-sm-3 control-label">Logo</label>
								<div class="col-sm-6" style="padding-top: 7px">
									<img id="imglogo" height="60" width="60"
										src="/egi/downloadfile?fileStoreId=${city.preferences.logo.fileStoreId}&moduleName=${sessionScope.cityCode}">

									<div class="input-group">
										<input type="file" name="logo" id="logo"
											data-accept="jpg,jpeg,png,gif,PNG,JPG,JPEG"
											data-errormsg="Please select valid image file!"
											class="form-control" placeholder="" autocomplete="off" /> <span
											class="input-group-btn">
											<button class="btn btn-default btnpopover" type="button"
												data-toggle="popover" data-placement="right" title=""
												data-content="<spring:message code="help.corp.logo" />">
												<i class="entypo-help"></i>
											</button>
										</span>
									</div>
									<form:errors path="preferences.logo"
										cssClass="add-margin error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.corp.name" /></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<div class="input-group">
										<form:input path="preferences.corporationName" id="latitude"
											type="text" cssClass="form-control" placeholder=""
											autocomplete="off" required="required" />
										<span class="input-group-btn">
											<button class="btn btn-default btnpopover" type="button"
												data-toggle="popover" data-placement="right" title=""
												data-content="<spring:message code="help.corp.name" />">
												<i class="entypo-help"></i>
											</button>
										</span>
									</div>
									<form:errors path="preferences.corporationName"
										cssClass="add-margin error-msg" />
								</div>

							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.corp.address" /></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<div class="icon-inputgroup input-group">
										<form:textarea path="preferences.corporationAddress"
											id="corporationAddress" type="text" cssClass="form-control"
											placeholder="" autocomplete="off" rows="5" />
										<span class="input-group-btn">
											<button class="btn btn-default btnpopover" type="button"
												data-toggle="popover" data-placement="right" title=""
												data-content="<spring:message code="help.corp.address" />">
												<i class="entypo-help"></i>
											</button>
										</span>
									</div>
									<form:errors path="preferences.corporationAddress"
										cssClass="add-margin error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.corp.contact.no" /></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<div class="input-group">
										<form:input path="preferences.corporationContactNo"
											id="corporationContactNo" type="text" cssClass="form-control"
											placeholder="" autocomplete="off" />
										<span class="input-group-btn">
											<button class="btn btn-default btnpopover" type="button"
												data-toggle="popover" data-placement="right" title=""
												data-content="<spring:message code="help.corp.contact.no" />">
												<i class="entypo-help"></i>
											</button>
										</span>
									</div>
									<form:errors path="preferences.corporationContactNo"
										cssClass="add-margin error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.corp.contact.email" /></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<div class="input-group">
										<form:input path="preferences.corporationContactEmail"
											id="corporationContactEmail" type="text"
											cssClass="form-control" placeholder="" autocomplete="off" />
										<span class="input-group-btn">
											<button class="btn btn-default btnpopover" type="button"
												data-toggle="popover" data-placement="right" title=""
												data-content="<spring:message code="help.corp.contact.email" />">
												<i class="entypo-help"></i>
											</button>
										</span>
									</div>
									<form:errors path="preferences.corporationContactEmail"
										cssClass="add-margin error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.corp.callcenter.no" /></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<div class="input-group">
										<form:input path="preferences.corporationCallCenterNo"
											id="corporationCallCenterNo" type="text"
											cssClass="form-control" placeholder="" autocomplete="off" />
										<span class="input-group-btn">
											<button class="btn btn-default btnpopover" type="button"
												data-toggle="popover" data-placement="right" title=""
												data-content="<spring:message code="help.corp.callcenter.no" />">
												<i class="entypo-help"></i>
											</button>
										</span>
									</div>
									<form:errors path="preferences.corporationCallCenterNo"
										cssClass="add-margin error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.corp.gis.location.link" /></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<div class="input-group">
										<form:input path="preferences.corporationOfficeGisLocation"
											id="corporationOfficeGisLocation" type="text"
											cssClass="form-control" placeholder="" autocomplete="off" />
										<span class="input-group-btn">
											<button class="btn btn-default btnpopover" type="button"
												data-toggle="popover" data-placement="right" title=""
												data-content="<spring:message code="help.corp.gis.location.link" />">
												<i class="entypo-help"></i>
											</button>
										</span>
									</div>
									<form:errors path="preferences.corporationOfficeGisLocation"
										cssClass="add-margin error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.corp.fb.link" /></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<div class="input-group">
										<form:input path="preferences.facebookLink" id="facebookLink"
											type="text" cssClass="form-control" placeholder=""
											autocomplete="off" />
										<span class="input-group-btn">
											<button class="btn btn-default btnpopover" type="button"
												data-toggle="popover" data-placement="right" title=""
												data-content="<spring:message code="help.corp.fb.link" />">
												<i class="entypo-help"></i>
											</button>
										</span>
									</div>
									<form:errors path="preferences.facebookLink"
										cssClass="add-margin error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.corp.twitter.link" /></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<div class="input-group">
										<form:input path="preferences.twitterLink" id="twitterLink"
											type="text" cssClass="form-control" placeholder=""
											autocomplete="off" />
										<span class="input-group-btn">
											<button class="btn btn-default btnpopover" type="button"
												data-toggle="popover" data-placement="right" title=""
												data-content="<spring:message code="help.corp.twitter.link" />">
												<i class="entypo-help"></i>
											</button>
										</span>
									</div>
									<form:errors path="preferences.twitterLink"
										cssClass="add-margin error-msg" />
								</div>
							</div>
						</div>
						<div id="configuration" class="tab-pane fade">
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.city.lat" /></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<div class="input-group">
										<form:input path="latitude" id="latitude" type="text"
											cssClass="form-control" placeholder="" autocomplete="off" />
										<span class="input-group-btn">
											<button class="btn btn-default btnpopover" type="button"
												data-toggle="popover" data-placement="right" title=""
												data-content="<spring:message code="help.city.coord.lat" />">
												<i class="entypo-help"></i>
											</button>
										</span>
									</div>
									<form:errors path="latitude" cssClass="add-margin error-msg" />
								</div>

							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.city.lng" /></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<div class="input-group">
										<form:input path="longitude" id="longitude" type="text"
											cssClass="form-control" placeholder="" autocomplete="off" />
										<span class="input-group-btn">
											<button class="btn btn-default btnpopover" type="button"
												data-toggle="popover" data-placement="right" title=""
												data-content="<spring:message code="help.city.coord.lng" />">
												<i class="entypo-help"></i>
											</button>
										</span>
									</div>
									<form:errors path="longitude" cssClass="add-margin error-msg" />
								</div>

							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.city.gis.kml" /></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<a target="_blank"
										href="/egi/downloadfile?fileStoreId=${city.preferences.gisKML.fileStoreId}&moduleName=${sessionScope.cityCode}">${city.preferences.gisKML.fileName}</a>
									
									<div class="input-group">
										<input type="file" name="gisKML" id="gisKML" data-accept="kml"
											data-errormsg="Please select valid Gis KML file!"
											class="form-control" placeholder="" autocomplete="off" />
									    <span class="input-group-btn">
											<button class="btn btn-default btnpopover" type="button"
												data-toggle="popover" data-placement="right" title=""
												data-content="<spring:message code="help.city.giskml" />">
												<i class="entypo-help"></i>
											</button>
										</span>
									</div>
									
									<form:errors path="preferences.gisKML"
										cssClass="add-margin error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.city.gis.shape" /></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<a target="_blank"
										href="/egi/downloadfile?fileStoreId=${city.preferences.gisShape.fileStoreId}&moduleName=${sessionScope.cityCode}">${city.preferences.gisShape.fileName}</a>
									<div class="input-group">
										<input type="file" name="gisShape" id="gisShape"
										data-accept="shp"
										data-errormsg="Please select valid Gis Shape file!"
										class="form-control" placeholder="" autocomplete="off" />
									    <span class="input-group-btn">
											<button class="btn btn-default btnpopover" type="button"
												data-toggle="popover" data-placement="right" title=""
												data-content="<spring:message code="help.city.gisshape" />">
												<i class="entypo-help"></i>
											</button>
										</span>
									</div>
									<form:errors path="preferences.gisShape"
										cssClass="add-margin error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.city.recaptcha.pri" /><span class="mandatory"></span></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<div class="input-group">
										<form:input path="recaptchaPK" id="recaptchaPK" type="text"
											class="form-control" placeholder="" autocomplete="off"
											required="required" />
										<span class="input-group-btn">
											<button class="btn btn-default btnpopover" type="button"
												data-toggle="popover" data-placement="right" title=""
												data-content="<spring:message code="help.city.recaptcha.pvt.key" />">
												<i class="entypo-help"></i>
											</button>
										</span>
									</div>
									<form:errors path="recaptchaPK" cssClass="add-margin error-msg" />
								</div>

							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.city.recaptcha.pub" /><span class="mandatory"></span></label>
								<div class="col-sm-6" style="padding-top: 7px">
									<div class="input-group">
										<form:input path="recaptchaPub" id="recaptchaPub" type="text"
											class="form-control" placeholder="" autocomplete="off"
											required="required" />
										<span class="input-group-btn">
											<button class="btn btn-default btnpopover" type="button"
												data-toggle="popover" data-placement="right" title=""
												data-content="<spring:message code="help.city.recaptcha.pub.key" />">
												<i class="entypo-help"></i>
											</button>
										</span>
									</div>

									<form:errors path="recaptchaPub"
										cssClass="add-margin error-msg" />
								</div>

							</div>
						</div>
					</div>
					<div>
						<div class="col-sm-12" style="padding-top: 7px">
							<label class="col-sm-12 error-msg"><spring:message
									code="imp.notice" /></label>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="text-center">
					<button type="submit" class="btn btn-primary">
						<spring:message code="lbl.submit" />
					</button>
					<button type="button" class="btn btn-default"
						onclick="window.location.reload()">
						<spring:message code="lbl.reset" />
					</button>
					<button type="button" class="btn btn-default" data-dismiss="modal"
						onclick="window.close();">
						<spring:message code="lbl.close" />
					</button>
				</div>
			</div>
		</form:form>
	</div>
</div>
<script src="../../resources/js/app/citysetup.js"></script>