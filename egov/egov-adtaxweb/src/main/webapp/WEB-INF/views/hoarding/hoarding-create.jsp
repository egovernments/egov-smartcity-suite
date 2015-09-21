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
		<form:form id="hoardingform" method="post" class="form-horizontal form-groups-bordered" modelAttribute="hoarding" commandName="hoarding">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<ul class="nav nav-tabs" id="settingstab">
					<li class="active"><a data-toggle="tab"
						href="#hoardingdetails" data-tabidx="0" aria-expanded="false">Hoarding
							Details</a></li>
					<li class=""><a data-toggle="tab" href="#hoardingattachments"
						data-tabidx="1" aria-expanded="false">Enlosed Documents</a></li>
				</ul>
			</div>
			<div class="panel-body custom-form">
				<div class="tab-content">
					<div class="tab-pane fade active in" id="hoardingdetails">
						<form role="form" class="form-horizontal form-groups-bordered"
							action="">
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">Application
									No<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" cssClass="form-control" path="applicationNumber" id="applicationNumber" required="required"/>
                               		<form:errors path="applicationNumber" cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right">Application
									Date<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" cssClass="form-control datepicker" path="applicationDate" id="applicationDate" required="required"/>
                               		<form:errors path="applicationDate" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">Permission
									No<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" cssClass="form-control" path="permissionNumber" id="permissionNumber" required="required"/>
                               		<form:errors path="permissionNumber" cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right">Hoarding
									No<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" cssClass="form-control" path="hoardingNumber" id="hoardingNumber" required="required"/>
                               		<form:errors path="hoardingNumber" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">Hoarding
									Name<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" cssClass="form-control" path="hoardingName" id="hoardingName" required="required"/>
                               		<form:errors path="hoardingName" cssClass="error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right">Hoarding Type<span
									class="mandatory"></span></label>
								<div class="col-sm-3 add-margin dynamic-span capitalize">
									<form:radiobuttons path="type" element="span"/>
									<form:errors path="type" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">Agency<span
									class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<input type="text" class="form-control typeahead" autocomplete="off" required="required" value="${hoarding.agency.name}">
									<form:hidden path="agency" id="agencyId" value="0" />
									<form:errors path="agency" cssClass="error-msg" />
								</div>
								<label class="col-sm-3 control-label text-right">Advertiser<span
									class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<form:input type="text" cssClass="form-control" path="advertiser" id="advertiser" required="required"/>
                               		<form:errors path="advertiser" cssClass="error-msg" />
								</div>
							</div>
							<div class="form-group">
								
								<label class="col-sm-2 control-label text-right">Advertisement
									Particulars<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<input type="text" class="form-control" required>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">Property
									Type<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<select class="form-control"><option>select
											from below</option></select>
								</div>
								<label class="col-sm-2 control-label text-right">Property
									Assessment No</label>
								<div class="col-sm-3 add-margin">
									<div class="input-group">
										<input type="text" class="form-control" /> <span
											class="input-group-addon"> <i
											class="fa fa-search specific"></i>
										</span>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">Owner
									Details<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<input type="text" class="form-control" required>
								</div>
								<label class="col-sm-2 control-label text-right">Hoarding
									Status<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<input type="text" class="form-control" required>
								</div>
							</div>
							<div class="panel-heading custom_form_panel_heading">
								<div class="panel-title">Structure</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">Category<span
									class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<select class="form-control"><option>select
											from below</option></select>
								</div>
								<label class="col-sm-2 control-label text-right">Sub
									Category<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<select class="form-control"><option>select
											from below</option></select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">Measurement
									(No/SqMt/Sqft/Vol)<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<input type="text" class="form-control" required>
								</div>
								<label class="col-sm-2 control-label text-right">UOM<span
									class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<select class="form-control"><option>select
											from below</option></select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">Length</label>
								<div class="col-sm-3 add-margin">
									<input type="text" class="form-control">
								</div>
								<label class="col-sm-2 control-label text-right">Width</label>
								<div class="col-sm-3 add-margin">
									<input type="text" class="form-control">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">Breadth</label>
								<div class="col-sm-3 add-margin">
									<input type="text" class="form-control">
								</div>
								<label class="col-sm-2 control-label text-right">Total
									Height of Structure<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<input type="text" class="form-control">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">Revenue
									Zone<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<select class="form-control"><option>select
											from below</option></select>
								</div>
								<label class="col-sm-2 control-label text-right">Revenue
									Ward<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<select class="form-control"><option>select
											from below</option></select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">Locality<span
									class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<select class="form-control"><option>select
											from below</option></select>
								</div>
								<label class="col-sm-2 control-label text-right">Address<span
									class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<textarea class="form-control" cols="" rows=""></textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">Zone<span
									class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<select class="form-control"><option>select
											from below</option></select>
								</div>
								<label class="col-sm-2 control-label text-right">Ward<span
									class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<select class="form-control"><option>select
											from below</option></select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">Class<span
									class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<input type="text" class="form-control" required>
								</div>
								<label class="col-sm-2 control-label text-right">TPBO/RI
									No</label>
								<div class="col-sm-3 add-margin">
									<input type="text" class="form-control">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">Duration
									of Advertisement<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<select class="form-control"><option>select
											from below</option></select>
								</div>
							</div>
							<div class="panel-heading custom_form_panel_heading">
								<div class="panel-title">Fee Details</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">Tax<span
									class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<input type="text" class="form-control" required>
								</div>
								<label class="col-sm-2 control-label text-right">Encroachment
									Fees<span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<input type="text" class="form-control" required>
								</div>
							</div>
							<div class="panel-heading custom_form_panel_heading">
								<div class="panel-title">Attach Documents</div>
							</div>
							<div class="form-group">
								<div class="col-sm-3 add-margin">
									<input type="file" class="form-control" required>
								</div>
								<div class="col-sm-3 add-margin" style="cursor: pointer;">
									<div class="rounded-circle add-icon-attachment add-attachment">
										<i class="fa fa-plus"></i>
									</div>
								</div>
							</div>
						</form>
					</div>
					<div class="tab-pane fade" id="hoardingattachments">
						<form role="form" class="form-horizontal form-groups-bordered"
							action="">
							<div class="col-sm-12 view-content header-color hidden-xs">
								<div class="col-sm-1 table-div-column">S.no</div>
								<div class="col-sm-5 table-div-column">Document Name</div>
								<div class="col-sm-3 table-div-column">Check List</div>
								<div class="col-sm-3 table-div-column">Attach Document</div>
							</div>
							<div class="form-group">
								<div class="col-sm-1 text-center">1</div>
								<div class="col-sm-5 text-center">Document 1</div>
								<div class="col-sm-3 text-center">
									<input type="checkbox" required>
								</div>
								<div class="col-sm-3 text-center">
									<input type="file" class="form-control" required>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-1 text-center">2</div>
								<div class="col-sm-5 text-center">Document 2</div>
								<div class="col-sm-3 text-center">
									<input type="checkbox" required>
								</div>
								<div class="col-sm-3 text-center">
									<input type="file" class="form-control" required>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-1 text-center">3</div>
								<div class="col-sm-5 text-center">Document 3</div>
								<div class="col-sm-3 text-center">
									<input type="checkbox" required>
								</div>
								<div class="col-sm-3 text-center">
									<input type="file" class="form-control" required>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-1 text-center">4</div>
								<div class="col-sm-5 text-center">Document 4</div>
								<div class="col-sm-3 text-center">
									<input type="checkbox" required>
								</div>
								<div class="col-sm-3 text-center">
									<input type="file" class="form-control" required>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-1 text-center">5</div>
								<div class="col-sm-5 text-center">Document 5</div>
								<div class="col-sm-3 text-center">
									<input type="checkbox" required>
								</div>
								<div class="col-sm-3 text-center">
									<input type="file" class="form-control" required>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>

		</div>

		<div class="text-center">
			<button type="submit" class="btn btn-primary">Submit</button>
			<button type="button" class="btn btn-default"
				onclick="window.location.reload()">Reset</button>
			<button type="button" class="btn btn-default" data-dismiss="modal"
				onclick="window.close();">Close</button>
		</div>
	</form:form>
	</div>
</div>
<script src="<c:url value='/resources/app/js/hoarding.js'/>"></script>