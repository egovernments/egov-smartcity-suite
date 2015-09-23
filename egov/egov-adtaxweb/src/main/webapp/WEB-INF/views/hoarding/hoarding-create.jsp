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
<div class="row">
	<div class="col-md-12">
		<c:if test="${not empty message}">
			<div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
		</c:if>
		<form:form id="hoardingform" method="post" class="form-horizontal form-groups-bordered" 
		modelAttribute="hoarding" commandName="hoarding" enctype="multipart/form-data">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<ul class="nav nav-tabs" id="settingstab">
					<li class="active"><a data-toggle="tab"
						href="#hoardingdetails" data-tabidx="0" aria-expanded="false"><spring:message code="lbl.hoarding.details"/></a></li>
					<li class=""><a data-toggle="tab" href="#hoardingattachments"
						data-tabidx="1" aria-expanded="false"><spring:message code="lbl.hoarding.enclosure"/></a></li>
				</ul>
			</div>
			<div class="panel-body custom-form">
				<div class="tab-content">
					<div class="tab-pane fade active in" id="hoardingdetails">
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.application.no"/>
								<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" cssClass="form-control" path="applicationNumber" id="applicationNumber" required="required"/>
                               		<form:errors path="applicationNumber" cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right">
								<spring:message code="lbl.application.date"/>
								<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" cssClass="form-control datepicker" path="applicationDate" id="applicationDate" required="required"/>
                               		<form:errors path="applicationDate" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.hoarding.permission.no"/>
								<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" cssClass="form-control" path="permissionNumber" id="permissionNumber" required="required"/>
                               		<form:errors path="permissionNumber" cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right">
								<spring:message code="lbl.hoarding.no"/>
								<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" cssClass="form-control" path="hoardingNumber" id="hoardingNumber" required="required"/>
                               		<form:errors path="hoardingNumber" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.hoarding.name"/>
								<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" cssClass="form-control" path="hoardingName" id="hoardingName" required="required"/>
                               		<form:errors path="hoardingName" cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right">
								<spring:message code="lbl.hoarding.type"/>
								<span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin dynamic-span capitalize">
									<form:radiobuttons path="type" element="span"/>
									<form:errors path="type" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.hoarding.agency"/>
								<span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<input type="text" class="form-control typeahead" autocomplete="off" required="required" value="${hoarding.agency.name}">
									<form:hidden path="agency" id="agencyId" value="${hoarding.agency.id}" />
									<form:errors path="agency" cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right">
								<spring:message code="lbl.hoarding.adv"/>
								<span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:textarea path="advertiser" cols="5" rows="2" class="form-control"  required="required" minlength="5" maxlength="125"/>
                               		<form:errors path="advertiser" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.hoarding.ad.particulars"/>
								<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<form:textarea path="advertisementParticular" cols="5" rows="2" class="form-control"  required="required" minlength="5" maxlength="512"/>
									<form:errors path="advertisementParticular" cssClass="error-msg" />
								</div>
									<label class="col-sm-2 control-label text-right">
									<spring:message code="lbl.hoarding.prop.type"/>
									<span class="mandatory"></span>
									</label>
									<div class="col-sm-3 add-margin">
										<form:select path="propertyType" id="propertyType" cssClass="form-control" cssErrorClass="form-control error" required="required">
											<form:option value=""><spring:message code="lbl.select" /></form:option>
											<form:options items="${hoardingPropertyType}"/>
										</form:select>
									<form:errors path="propertyType" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.hoarding.prop.assesmt.no"/>
								</label>
								<div class="col-sm-3 add-margin">
									<div class="input-group">
										<form:input type="text" cssClass="form-control" path="propertyNumber" id="propertyNumber" /><span
											class="input-group-addon"> <i
											class="fa fa-search specific"></i></span>
                               			<form:errors path="propertyNumber" cssClass="error-msg" />
									</div>
								</div>
								<label class="col-sm-2 control-label text-right">
								<spring:message code="lbl.owner.detail"/>
								<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<form:textarea path="ownerDetail" cols="5" rows="2" class="form-control"  required="required" minlength="5" maxlength="125"/>
                               		<form:errors path="ownerDetail" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.hoarding.status"/>
								<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<form:select path="status" id="status" cssClass="form-control" cssErrorClass="form-control error" required="required">
										<form:option value=""><spring:message code="lbl.select" /></form:option>
										<form:options items="${hoardingStatus}"/>
									</form:select>
									<form:errors path="status" cssClass="error-msg" />
								</div>
								<div class="col-sm-5 add-margin">&nbsp;</div>
							</div>
							<div class="panel-heading custom_form_panel_heading">
								<div class="panel-title">
								<spring:message code="lbl.hoarding.struct"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.category"/>
								<span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:select path="category" id="category" cssClass="form-control" cssErrorClass="form-control error" required="required">
										<form:option value=""><spring:message code="lbl.select" /></form:option>
										<form:options items="${hoardingCategories}" itemLabel="name" itemValue="id"/>
									</form:select>
									<form:errors path="category" cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right">
								<spring:message code="lbl.subcategory"/>
								<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<form:select path="subCategory" id="subCategory" cssClass="form-control" cssErrorClass="form-control error" required="required">
										<form:option value=""><spring:message code="lbl.select"/></form:option>
									</form:select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.hoarding.measurement"/>
								<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" cssClass="form-control" path="measurement" id="measurement" required="required"/>
                               		<form:errors path="measurement" cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right">
								<spring:message code="lbl.uom"/>
								<span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:select path="unitOfMeasure" id="unitOfMeasure" cssClass="form-control" cssErrorClass="form-control error" required="required">
										<form:option value=""><spring:message code="lbl.select" /></form:option>
										<form:options items="${uom}" itemLabel="description" itemValue="id"/>
									</form:select>
									<form:errors path="unitOfMeasure" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.length"/>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" cssClass="form-control" path="length" id="length"/>
                               		<form:errors path="length" cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right">
								<spring:message code="lbl.width"/>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" cssClass="form-control" path="width" id="width"/>
                               		<form:errors path="width" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.breadth"/>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" cssClass="form-control" path="breadth" id="breadth"/>
                               		<form:errors path="breadth" cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right">
								<spring:message code="lbl.hoarding.total.height"/>
								<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" cssClass="form-control" path="totalHeight" id="totalHeight" required="required"/>
                               		<form:errors path="totalHeight" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.rev.zone"/>
								</label>
								<div class="col-sm-3 add-margin">
									<form:select path="revenueBoundary.parent" id="revenueBoundaryParent" cssClass="form-control" cssErrorClass="form-control error">
										<form:option value=""><spring:message code="lbl.select" /></form:option>
										<form:options items="${revenueZones}" itemLabel="name" itemValue="id"/>
									</form:select>
									<form:errors path="revenueBoundary.parent" cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right">
								<spring:message code="lbl.rev.ward"/>
								</label>
								<div class="col-sm-3 add-margin">
									<form:select path="revenueBoundary" id="revenueBoundary" cssClass="form-control" cssErrorClass="form-control error">
										<form:option value=""><spring:message code="lbl.select"/></form:option>
									</form:select>
									<form:errors path="revenueBoundary" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.locality"/>
								</label>
								<div class="col-sm-3 add-margin">
									<select class="form-control"><option>select
											from below</option></select>
								</div>
								<label class="col-sm-2 control-label text-right">
								<spring:message code="lbl.address"/>
								<span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:textarea path="address" cols="5" rows="2" class="form-control"  required="required" minlength="5" maxlength="512"/>
                               		<form:errors path="address" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.zone"/>
								<span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:select path="adminBoundry.parent" id="adminBoundryParent" cssClass="form-control" cssErrorClass="form-control error" required="required">
										<form:option value=""><spring:message code="lbl.select" /></form:option>
										<form:options items="${zones}" itemLabel="name" itemValue="id"/>
									</form:select>
									<form:errors path="adminBoundry.parent" cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right">
								<spring:message code="lbl.ward"/>
								<span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:select path="adminBoundry" id="adminBoundry" cssClass="form-control" cssErrorClass="form-control error" required="required">
										<form:option value=""><spring:message code="lbl.select"/></form:option>
									</form:select>
									<form:errors path="adminBoundry" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.hoarding.class"/>
								<span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:select path="rateClass" id="rateClass" cssClass="form-control" cssErrorClass="form-control error" required="required">
										<form:option value=""><spring:message code="lbl.select" /></form:option>
										<form:options items="${rateClasses}" itemLabel="description" itemValue="id"/>
									</form:select>
									<form:errors path="rateClass" cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right">
								<spring:message code="lbl.rev.inspector"/>
								</label>
								<div class="col-sm-3 add-margin">
									<form:select path="revenueInspector" id="revenueInspector" cssClass="form-control" cssErrorClass="form-control error">
										<form:option value=""><spring:message code="lbl.select" /></form:option>
										<form:options items="${revenueInspectors}" itemLabel="name" itemValue="id"/>
									</form:select>
									<form:errors path="revenueInspector" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.hoarding.duration"/>
								</label>
								<div class="col-sm-3 add-margin">
									<form:select path="advertisementDuration" id="advertisementDuration" cssClass="form-control" cssErrorClass="form-control error">
										<form:option value=""><spring:message code="lbl.select" /></form:option>
										<form:options items="${advertisementDuration}"/>
									</form:select>
									<form:errors path="advertisementDuration" cssClass="error-msg" />
								</div>
							</div>
							<div class="panel-heading custom_form_panel_heading">
								<div class="panel-title">
								<spring:message code="lbl.fee.details"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.tax"/>
								<span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" cssClass="form-control" path="taxAmount" id="taxAmount" required="required"/>
                               		<form:errors path="taxAmount" cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right">
								<spring:message code="lbl.hoarding.enc.fee"/>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" cssClass="form-control" path="encroachmentFee" id="encroachmentFee"/>
                               		<form:errors path="encroachmentFee" cssClass="error-msg" />
								</div>
							</div>
					</div>
					<div class="tab-pane fade" id="hoardingattachments">
						<c:choose>
							<c:when test="${not empty hoardingDocumentTypes}">
							<form:hidden path="latitude" id="latitude"/> 
							<form:hidden path="longitude" id="longitude" /> 
							<div class="col-sm-12 view-content header-color hidden-xs">
								<div class="col-sm-1 table-div-column"><spring:message code="lbl.srl.no"/></div>
								<div class="col-sm-5 table-div-column"><spring:message code="lbl.documentname"/></div>
								<div class="col-sm-3 table-div-column"><spring:message code="lbl.enclosed"/></div>
								<div class="col-sm-3 table-div-column"><spring:message code="lbl.attachdocument"/></div>
							</div>
							
							<c:forEach var="docs" items="${hoardingDocumentTypes}" varStatus="status">	
								<div class="form-group">
									<div class="col-sm-1 text-center">${status.index+1}</div>
									<div class="col-sm-5 text-center">${docs.mandatory ? "<span
									class='mandatory'></span>" : ""}${docs.name}
									</div>
									<div class="col-sm-3 text-center">
										<input type="checkbox" ${hoarding.documents[status.index].enclosed ? "checked='checked'" : ""} 
										name="documents[${status.index}].enclosed" ${docs.mandatory ? "required='required'" : ""}>
									</div>
									<div class="col-sm-3 text-center">
										<input type="file" name="documents[${status.index}].attachments" class="form-control" >
										<form:errors path="documents[${status.index}].attachments" cssClass="add-margin error-msg" />
										<form:hidden path="documents[${status.index}].doctype" value="${docs.id}" /> 
									</div>
								</div>
							</c:forEach> 
							</c:when>
						</c:choose>
					</div>
				</div>
			</div>

		</div>
		<div class="text-center">
			<button type="submit" class="btn btn-primary"><spring:message code="lbl.submit"/></button>
			<button type="reset" class="btn btn-default"><spring:message code="lbl.reset"/></button>
		    <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
		</div>
	</form:form>
	</div>
</div>
<script>
//this is to reset the sub combobox upon field error
var subcategory = '${hoarding.subCategory.id}';
var adminBoundry = '${hoarding.adminBoundry.id}';
var revenueBoundary = '${hoarding.revenueBoundary.id}';
</script>
<script src="<c:url value='/resources/global/js/jquery/plugins/exif.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/app/js/hoarding.js'/>"></script>
