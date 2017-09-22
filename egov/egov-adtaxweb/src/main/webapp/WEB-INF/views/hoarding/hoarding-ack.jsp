<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2017>  eGovernments Foundation
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

<div class="row">
	<div class="col-md-12">
		<form:form id="advertisementSuccessform" method="post"
			class="form-horizontal form-groups-bordered"
			modelAttribute="advertisementPermitDetail"
			commandName="advertisementPermitDetail" enctype="multipart/form-data">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title text-center" align="center">
						<c:out value="${message}" />
					</div>
				</div>
			</div>
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-body custom-form">
					<div class="tab-content">
						<div class="tab-pane fade active in" id="hoardingdetails">
							<div class="form-group">
								<label class="col-sm-3 add-margin text-right"> <spring:message
										code="lbl.application.no" />

								</label>
								<div class="col-sm-3 add-margin view-content view-content">
									${advertisementPermitDetail.applicationNumber}</div>
								<label class="col-sm-2 control-label text-right"> <spring:message
										code="lbl.application.date" />

								</label>
								<div class="col-sm-3 add-margin view-content">
									${advertisementPermitDetail.applicationDate}</div>
							</div>

							<div class="form-group">

								<label class="col-sm-3 control-label text-right"> <spring:message
										code="lbl.hoarding.type" />
								</label>
								<div
									class="col-sm-3 add-margin view-content dynamic-span capitalize">
									${advertisementPermitDetail.advertisement.type}</div>
								<label class="col-sm-2 control-label text-right"> <spring:message
										code="lbl.hoarding.agency" />
								</label>
								<div class="col-sm-3 add-margin view-content">
									${advertisementPermitDetail.agency.name}</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right"> <spring:message
										code="lbl.hoarding.ad.particulars" />
								</label>
								<div class="col-sm-3 add-margin view-content">
									${advertisementPermitDetail.advertisementParticular}</div>
								<label class="col-sm-2 control-label text-right"> <spring:message
										code="lbl.hoarding.prop.type" />
								</label>
								<div class="col-sm-3 add-margin view-content">
									${advertisementPermitDetail.advertisement.propertyType}</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right"> <spring:message
										code="lbl.hoarding.status" />
								</label>
								<div class="col-sm-3 add-margin view-content">
									${advertisementPermitDetail.advertisement.status}</div>
								<label class="col-sm-2 control-label text-right"> <spring:message
										code="lbl.owner.detail" />

								</label>
								<div class="col-sm-3 add-margin view-content">
									${advertisementPermitDetail.ownerDetail}</div>
							</div>

							<div class="form-group">
								<label class="col-sm-3 control-label text-right"> <spring:message
										code="lbl.address" />
								</label>
								<div class="col-sm-3 add-margin view-content">
									<c:out
										value="${advertisementPermitDetail.advertisement.address}"
										default="N/A" />
								</div>
								<div class="col-sm-5 add-margin">&nbsp;</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form:form>
	</div>
	<div class="row text-center">
		<div class="add-margin">
			<input type="button" value="Print" name="PrintAck" id="PrintAck"
				class="button"
				onclick="window.open('/adtax/hoarding/printack/${advertisementPermitDetail.id}','_blank');" > 
				<a href="javascript:void(0)" class="btn btn-default"
				onclick="self.close()"><spring:message code="lbl.close" /></a>
		</div>
	</div>
</div>
