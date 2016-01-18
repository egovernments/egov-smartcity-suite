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
		<form:form id="hoardingformview" action="" class="form-horizontal form-groups-bordered" modelAttribute="hoarding" 
		commandName="hoarding" >
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<ul class="nav nav-tabs" id="settingstab">
					<li class="active"><a data-toggle="tab"
						href="#hoardingdetails" data-tabidx="0" aria-expanded="false"><spring:message code="lbl.hoarding.details"/></a></li>
					
				</ul>
			</div>
			<div class="panel-body custom-form">
				<div class="tab-content">
					<div class="tab-pane fade active in" id="hoardingdetails">
							
							<div class="form-group">
								<label class="col-sm-3 control-label text-right">
									<spring:message code="lbl.hoarding.no"/>
							
								</label>
								<div class="col-sm-3 add-margin view-content">
									${hoarding.advertisementNumber}
								</div>
								
							</div>
			
													
							<div class="panel-heading custom_form_panel_heading">
								<div class="panel-title">
								<spring:message code="lbl.hoardingReport.Result"/>
								</div>
							</div>	
							<div  id="dcbreport">
							 	<div class="col-sm-12 view-content header-color hidden-xs">
									<div class="col-sm-1 table-div-column"><spring:message code="lbl.srl.no"/></div>
									<div class="col-sm-2 table-div-column"><spring:message code="lbl.hoardingReport.year"/></div>
										<div class="col-sm-2 table-div-column"><spring:message code="lbl.hoardingReport.taxReason"/></div>
								 <div class="col-sm-2 table-div-column"><spring:message code="lbl.hoardingReport.demandAmount"/></div>
									<div class="col-sm-1 table-div-column"><spring:message code="lbl.hoardingReport.collectedAmt"/></div>
									<div class="col-sm-2 table-div-column"><spring:message code="lbl.hoardingReport.agency"/></div>
									<div class="col-sm-2 table-div-column"><spring:message code="lbl.hoardingReport.receipts"/></div>
									
									
									
									
									
								</div>
								<c:forEach var="dcb" items="${dcbResult}" varStatus="status">	
									<div class="form-group">
										<div class="col-sm-1 text-center">${status.index+1}</div>
										<div class="col-sm-2 text-center">${dcb.installmentYearDescription}</div>
										<div class="col-sm-2 text-center">${dcb.demandReason}</div>

										<div class="col-sm-2 text-center">${dcb.demandAmount}</div>
										<div class="col-sm-1 text-center">${dcb.collectedAmount}</div>

										<div class="col-sm-2 text-center">${dcb.payeeName}</div>
										<div class="col-sm-2 text-center">${dcb.receiptNumber}</div>

									</div>
								</c:forEach> 
							</div>	
				</div>
			</div>

		</div>
		<div class="text-center">
		    <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
		</div>
	</form:form>
	</div>
</div>
