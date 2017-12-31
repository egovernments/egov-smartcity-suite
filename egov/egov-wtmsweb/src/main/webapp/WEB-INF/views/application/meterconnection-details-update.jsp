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
 
 <%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
 <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
 <div class="modal fade meterdtl-update" data-backdrop="static">
 	<div class="modal-dialog">
 		<div class="modal-content">
 			<div class="modal-header alert-info">
 				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 div class="modal-title"><strong><spring:message code='lbl.update.meter.details'/></strong></h4>
 			</div>
 			<div class="modal-body">
 				<div id="meterDetailsUpdateForm">
 					<form id="meterDetailsUpdateForm" class="form-horizontal form-groups-bordered">
 						<div class="alert alert-danger display-err-msg" style="color:#a94442;"></div>
 						<div class="alert alert-info display-success-msg" style="color:#3c763d; background-color:#dff0d8; border-color:#d6e9c6;"></div>
 						<br/>
 						<div class="form-group">
 							<div class="col-xs-3">
 								<label class="control-label"><spring:message code="lbl.application.no"/></label>
 							</div>
 							<div class="col-xs-3 add-margin applnNumber view-content" valign="center">
 							</div>	
 						</div>
 						<div class="form-group">
 								<div class="col-sm-3 control-label text-right"><spring:message code="lbl.metermake" /><span class="mandatory"></span></div>
								<div class="col-sm-3 add-margin">
									<select id="meterMake" name="meterMake" class="form-control" required="required">
										<option value="">
											<spring:message code="lbl.select" />
										</option>
									</select>
								</div>
								<div class="col-sm-3 control-label text-right"><spring:message code="lbl.executiondate" /><span class="mandatory"></span></div>
								<div class="col-sm-3 add-margin">
									<input id="executionDate" name="executionDate" class="form-control datepicker today" data-date-end-date="0d" required="required" />
								</div>
						</div>
						<div class="form-group">
 								<div class="col-sm-3 control-label text-right">
									<spring:message code="lbl.initialreading"/><span class="mandatory"></span>
								</div>
								<div class="col-sm-3 add-margin">
									<input class="form-control patternvalidation" id="initialReading" name="initialReading" data-pattern="decimalvalue" maxlength="12" required="required" />
								</div>
								<div class="col-sm-3 control-label text-right">
									<spring:message code="lbl.meter.serial.no"/><span class="mandatory"></span>
								</div>
								<div class="col-sm-3 add-margin">
									<input class="form-control patternvalidation" id="meterSerialNumber" name="meterSerialNumber" data-pattern="alphanumeric" maxlength="12" required="required" />
								</div>
 						</div>
		 				<div class="form-group text-center">
		 					<div class="col-md-12 add-margin">
		 						<button type="button" class="btn btn-primary" id="save"><spring:message code='lbl.save.button'/></button>
		 						<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code='lbl.close'/></button>
		 					</div>
		 				</div>
		 			</form>
 				</div>
 			</div>
 		</div>
 	</div>
</div>
 
<script src="<cdn:url value='/resources/js/app/executeWaterTapApplication.js?rnd=${app_release_no}'/>"></script>
 
 
 
 
  