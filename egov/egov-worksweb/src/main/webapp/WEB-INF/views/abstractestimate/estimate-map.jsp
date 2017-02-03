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
  <%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>
  <div class="modal fade" id="modal-6">
	   <div class="modal-dialog">
		  <div class="modal-content">
			 <div class="modal-body">
				<div class="row">
					<div class="col-md-12">
						<div class="panel panel-primary" data-collapsed="0"><!-- to apply shadow add class "panel-shadow" -->
							<!-- panel head -->
						 <div class="panel-heading">
								<div class="panel-title"><spring:message code="lbl.worksmanagement"/></div>
							</div> 
						
							<!-- panel body -->
							<div class="panel-body no-padding">
								<script type="text/javascript" src="https://maps.google.com/maps/api/js?key=AIzaSyB4Rn4dQ1hivzYaXNvpxxUB5i3x2j4ytic&amp;libraries=places"></script>
								<script type="text/javascript" src="<cdn:url value='/resources/global/js/geolocation/geolocationmarker-compiled.js?rnd=${app_release_no}' context='/egi'/>"></script>
								<div id="normal" class="img-prop"></div>
								<input id="pac-input" class="controls " type="text" placeholder="Enter a location">
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="modal-footer">
				<button type="button" class="btn btn-info btn-save-location" data-dismiss="modal"><spring:message code="lbl.capture"/></button>
				<button type="button" class="btn btn-info clear" data-dismiss="modal"><spring:message code="lbl.clear"/></button>
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="lbl.close"/></button>
			</div>
		</div>
	</div>
</div>