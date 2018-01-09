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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%> 
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">				
			<div class="panel-heading slide-history-menu">
				<div class="panel-title">
					<spring:message code="lbl.fieldinspection.details" />
				</div>				
				<div class="history-icon">
					<i class="fa fa-angle-down fa-2x" id="toggle-his-icon"></i>
				</div>	
			</div>
			
			<div class="panel-body history-slide">
				<div class="form-group">
					<div class="row">
						<label class="col-sm-3 control-label"><spring:message  code="lbl.inspectiondate"/></label> 
						<div class="col-sm-3 add-margin">
							<input class="form-control datepicker today" data-date-end-date="0d" data-inputmask="'mask': 'd/m/y'" id="inspectionDate" name="inspectionDate" required="required"/> 
						</div>
				
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.attachdocument"/></label>
						<div class="col-sm-3 add-margin">
							<input type="file" id="fileStoreId" name="files" class="file-ellipsis upload-file"> 
							<div class="add-margin error-msg" ><font size="2">
								<spring:message code="lbl.mesg.document"/>	
								</font></div>
						</div>		
					</div>
					<c:if test="${sewerageApplicationDetails.fieldInspections[0].fileStore != null}">
						<div class="row">
						<div class="col-sm-3 add-margin"></div>
						<div class="col-sm-3 add-margin"></div>
						
						<label class="col-sm-3 control-label"><spring:message  code="lbl.fileattached"/></label> 
							<div class="col-sm-3 add-margin">
								<a href="/stms/transactions/downloadFile?applicationNumber=${sewerageApplicationDetails.applicationNumber}">${sewerageApplicationDetails.fieldInspections[0].fileStore.fileName }</a> 
							</div>
						</div>
					</c:if>
			   </div>
			
				<div class="panel-heading">
					<div class="panel-title">
							<spring:message code="lbl.pipe.details" />
					</div>	
				</div>
				
				<%-- data-existing-row="${sewerageApplicationDetails.fieldInspections[0].fieldInspectionDetailsForUpdate.size()}" --%>
				<table class="table table-striped table-bordered" id="inspectionDetails" >					<thead>
					      <tr>
							<th class="text-center"><spring:message code="lbl.slno" /></th>
							<th class="text-center"><spring:message code="lbl.noofpipes" /><span class="mandatory"></span></th>
							<th class="text-center"><spring:message code="lbl.pipesize.inches" /><span class="mandatory"></span></th>
							<th class="text-center"><spring:message code="lbl.pipelength" /><span class="mandatory"></span></th>
							<th class="text-center"><spring:message code="lbl.screwsize.inches" /><span class="mandatory"></span></th>
							<th class="text-center"><spring:message code="lbl.noOfScrews" /><span class="mandatory"></span></th>
							<th class="text-center"><spring:message code="lbl.property.distance" /><span class="mandatory"></span></th>
							<th class="text-center"><spring:message code="lbl.roaddigging" /></th>
							<th class="text-center"><spring:message code="lbl.roadlength" /></th>
							<th class="text-center"><spring:message code="lbl.roadowner" /></th>
							<th class="text-center"><spring:message code="lbl.actions" /></th>
					      </tr>
				         </thead> 
					<tbody>
					<c:choose>
						<c:when test="${!sewerageApplicationDetails.fieldInspections.isEmpty() &&
						 !sewerageApplicationDetails.fieldInspections[0].fieldInspectionDetailsForUpdate.isEmpty()}">
							<c:forEach items="${sewerageApplicationDetails.fieldInspections[0].fieldInspectionDetailsForUpdate}" var="var1"
								varStatus="counter"> 
								<input type="hidden" value="${var1.id}" id="table_fieldInspections${counter.index}" name="fieldInspectionsDetails[${counter.index}].id" />
							      <tr class="data-fetched">
									<td class="text-center"><span class="serialNo" id="slNoInsp">${counter.index+1}</span></td>
									<td><form:input type="text" class="form-control table-input patternvalidation quantity" data-pattern="decimalvalue" path="fieldInspections[0].fieldInspectionDetailsForUpdate[${counter.index}].noOfPipes" id="fieldInspectionDetailsForUpdate${counter.index}noOfPipes" maxlength="8" value="${var1.noOfPipes}" /></td>
									<td>
										<form:select path="fieldInspections[0].fieldInspectionDetailsForUpdate[${counter.index}].pipeSize" data-first-option="false" id="fieldInspectionDetailsForUpdate${counter.index}pipeSize"
											cssClass="form-control" >
											<form:option value="">
												<spring:message code="lbl.select" />
											</form:option>
											<form:options items="${pipeSize}"  />
										</form:select>		 
									</td>						
									<td><form:input type="text" class="form-control table-input patternvalidation quantity" data-pattern="decimalvalue" path="fieldInspections[0].fieldInspectionDetailsForUpdate[${counter.index}].pipeLength" id="fieldInspectionDetailsForUpdate${counter.index}pipeLength" maxlength="8" value="${var1.pipeLength}" /></td>
									<td>
									<form:select path="fieldInspections[0].fieldInspectionDetailsForUpdate[${counter.index}].screwSize" data-first-option="false" id="fieldInspectionDetailsForUpdate${counter.index}screwSize"
											cssClass="form-control">
											<form:option value="">
												<spring:message code="lbl.select" />
											</form:option>
											<form:options items="${pipeSize}"  />
											<%-- <c:forEach items="${pipeSize}" var="entry">
										        <option value="${entry.key}">${entry.value}</option> 
										    </c:forEach> --%>
										</form:select>	 
									</td>
									<td><form:input type="text" class="form-control table-input patternvalidation quantity" data-pattern="decimalvalue" path="fieldInspections[0].fieldInspectionDetailsForUpdate[${counter.index}].noOfScrews" id="fieldInspectionDetailsForUpdate${counter.index}noOfScrews" maxlength="8" value="${var1.noOfScrews}" /></td>
									<td><form:input type="text" class="form-control table-input patternvalidation quantity" data-pattern="decimalvalue" path="fieldInspections[0].fieldInspectionDetailsForUpdate[${counter.index}].distance" id="fieldInspectionDetailsForUpdate${counter.index}distance" maxlength="8" value="${var1.distance}" /></td>
									<td class="text-center"><form:checkbox path="fieldInspections[0].fieldInspectionDetailsForUpdate[${counter.index}].roadDigging" id="fieldInspectionDetailsForUpdate${counter.index}roadDigging" maxlength="8" value="${var1.roadDigging}"  onchange="enableDisableRoadInfo(this);" /></td>
									<td><form:input type="text" disabled="true" class="form-control table-input  patternvalidation quantity roadLength" data-pattern="decimalvalue" path="fieldInspections[0].fieldInspectionDetailsForUpdate[${counter.index}].roadLength" id="fieldInspectionDetailsForUpdate${counter.index}roadLength" maxlength="8" value="${var1.roadLength}" /></td>
							      	<td>
									<form:select path="fieldInspections[0].fieldInspectionDetailsForUpdate[${counter.index}].roadOwner" disabled="true" data-first-option="false" id="fieldInspectionDetailsForUpdate${counter.index}roadOwner"
											cssClass="form-control roadOwner">
											<form:option value="">
												<spring:message code="lbl.select" />
											</form:option>
											<form:options items="${roadOwner}"  />
											<%-- <c:forEach items="${roadOwner}" var="entry">
										        <option value="${entry}">${entry}</option>  
										    </c:forEach>  --%>
										</form:select>	 
									</td>
									
									<c:if test="${counter.index==0}">
										<td class="text-center"><a href="javascript:void(0);" class="btn-sm btn-default" id="addInspctRowId"><i class="fa fa-plus"></i></a></td>
									</c:if>
									<c:if test="${counter.index!=0}">
										<td class="text-center"><a href="javascript:void(0);" class="btn-sm btn-danger" id="delete_insp_row" data-record-id="${var1.id}"><i class="fa fa-trash"></i></a></td>
									</c:if>
							      </tr>
				     		 </c:forEach>
						</c:when> 
						<c:otherwise> 
							      <tr class="data-fetchedFromDB">
									<td><span class="serialNo" id="slNoInsp1">1</span></td>
									<td><form:input type="text" class="form-control table-input patternvalidation quantity" data-pattern="decimalvalue" path="fieldInspections[0].fieldInspectionDetailsForUpdate[0].noOfPipes" id="fieldInspectionDetailsForUpdate0noOfPipes" maxlength="8" /></td>
									<td>
									<form:select path="fieldInspections[0].fieldInspectionDetailsForUpdate[0].pipeSize" data-first-option="false" id="fieldInspectionDetailsForUpdate0pipeSize"
											cssClass="form-control">
											<form:option value="">
												<spring:message code="lbl.select" />
											</form:option>
											<form:options items="${pipeSize}"  />
											<%-- <c:forEach items="${pipeSize}" var="entry">
										        <option value="${entry.key}">${entry.key}</option> 
										    </c:forEach> --%>
										</form:select>	 
									</td>
									<td><form:input type="text" class="form-control table-input patternvalidation quantity" data-pattern="decimalvalue" path="fieldInspections[0].fieldInspectionDetailsForUpdate[0].pipeLength" id="fieldInspectionDetailsForUpdate0pipeLength" maxlength="8" /></td>
									<td>
									<form:select path="fieldInspections[0].fieldInspectionDetailsForUpdate[0].screwSize" data-first-option="false" id="fieldInspectionDetailsForUpdate0screwSize"
											cssClass="form-control" >
											<form:option value="">
												<spring:message code="lbl.select" />
											</form:option>
											<form:options items="${pipeSize}"  />
											<%-- <c:forEach items="${pipeSize}" var="entry">
										        <option value="${entry.key}">${entry.value}</option> 
										    </c:forEach> --%>
										</form:select>	 
									</td>
									<td><form:input type="text" class="form-control table-input patternvalidation quantity" data-pattern="decimalvalue" path="fieldInspections[0].fieldInspectionDetailsForUpdate[0].noOfScrews" id="fieldInspectionDetailsForUpdate0noOfScrews" maxlength="8" /></td>
									<td><form:input type="text" class="form-control table-input patternvalidation quantity" data-pattern="decimalvalue" path="fieldInspections[0].fieldInspectionDetailsForUpdate[0].distance" id="fieldInspectionDetailsForUpdate0distance" maxlength="8" /></td>
									<td class="text-center"><form:checkbox path="fieldInspections[0].fieldInspectionDetailsForUpdate[0].roadDigging" id="fieldInspectionDetailsForUpdate0roadDigging" onchange="enableDisableRoadInfo(this);"/></td> 
									<td><form:input type="text" disabled="true" class="form-control table-input patternvalidation quantity roadLength" data-pattern="decimalvalue" path="fieldInspections[0].fieldInspectionDetailsForUpdate[0].roadLength" id="fieldInspectionDetailsForUpdate0roadLength" maxlength="8" /></td>
							      	<td>
									<form:select path="fieldInspections[0].fieldInspectionDetailsForUpdate[0].roadOwner" disabled="true" data-first-option="false" id="fieldInspectionDetailsForUpdate0roadOwner"
											cssClass="form-control roadOwner">
											<form:option value="">
												<spring:message code="lbl.select" /> 
											</form:option>
											<form:options items="${roadOwner}" />
											<%-- <c:forEach items="${roadOwner}" var="entry">
										        <option value="${entry}">${entry}</option> 
										    </c:forEach> --%>
										</form:select>	 
									</td>
									<td class="text-center"><a href="javascript:void(0)" class="btn-sm btn-default" id="addInspctRowId"><i class="fa fa-plus"></i></a></td>
							      </tr>
							</c:otherwise> 
						</c:choose>
					</tbody> 
				</table>
			
				<div class="panel-heading">
					<div class="panel-title">
							<spring:message code="lbl.estimation.details" />
					</div>	
				</div>
				<table class="table table-striped table-bordered" id="estimateDetails"  data-existing-rowValue="${sewerageApplicationDetails.estimationDetailsForUpdate.size()}">
					<thead>
					      <tr>
							<th class="text-center"><spring:message code="lbl.slno" /></th>
							<th class="text-center"><spring:message code="lbl.material" /><span class="mandatory"></span></th>
							<th class="text-center"><spring:message code="lbl.quantity" /></th>
							<th class="text-center"><spring:message code="lbl.uom" /></th>
							<th class="text-center"><spring:message code="lbl.rate" /></th>
							<th class="text-center"><spring:message code="lbl.amount" /></th>
							<th class="text-center"><spring:message code="lbl.actions" /></th>
					      </tr>
				         </thead>
					<tbody>
						<c:choose>
							<c:when test="${!sewerageApplicationDetails.estimationDetailsForUpdate.isEmpty()}">
								<c:forEach items="${sewerageApplicationDetails.estimationDetailsForUpdate}" var="var1"
									varStatus="counter">
							      <tr id="estimationRow">
									<td class="text-center"><span class="serialNumber" id="slNo${counter.index}">${counter.index+1}</span></td>
									<td class="text-center"><form:textarea class="form-control table-input" path="estimationDetailsForUpdate[${counter.index}].itemDescription" id="estimationDetailsForUpdate${counter.index}itemDescription" maxlength="256" value="${var1.itemDescription}"></form:textarea></td>
									<td class="text-right"><form:input type="text" class="form-control table-input text-right patternvalidation quantity" data-pattern="decimalvalue" path="estimationDetailsForUpdate[${counter.index}].quantity" id="estimationDetailsForUpdate${counter.index}quantity" maxlength="8" onblur="calculateTotalAmount();" value="${var1.quantity}"/></td>
									<td class="text-right">
											<form:select path="estimationDetailsForUpdate[${counter.index}].unitOfMeasurement" data-first-option="false" id="estimationDetailsForUpdate${counter.index}unitOfMeasurement"
												cssClass="form-control" >
												<form:option value="">
													<spring:message code="lbl.select" /> 
												</form:option>
												<form:options items="${uomList}" itemLabel="uom" itemValue="id" />
												<%-- <c:forEach items="${uomList}" var="entry">
											        <option value="${entry.id}">${entry.uom}</option> 
											    </c:forEach> --%>
										</form:select>	
									<td class="text-right"><form:input type="text" class="form-control table-input text-right patternvalidation unitrate" data-pattern="decimalvalue" path="estimationDetailsForUpdate[${counter.index}].unitRate" id="estimationDetailsForUpdate${counter.index}unitRate" maxlength="8" onblur="calculateTotalAmount();" value="${var1.unitRate}" /></td>
									<td class="text-right"><form:input type="text" class="form-control table-input text-right patternvalidation quantity" data-pattern="decimalvalue" path="estimationDetailsForUpdate[${counter.index}].amount" id="estimationDetailsForUpdate${counter.index}amount" maxlength="8" onblur="calculateGrandTotal();" value="${var1.amount}" /></td>
									<c:if test="${counter.index==0}">
										<td class="text-center"><a href="javascript:void(0)" class="btn-sm btn-default" id="addRowId"><i class="fa fa-plus"></i></a></td>
									</c:if>
									<c:if test="${counter.index!=0}">
										<td class="text-center"><a href="javascript:void(0)" class="btn-sm btn-danger" id="delete_row" data-record-id="${var1.id}"><i class="fa fa-trash"></i></a></td>
									</c:if>
							      </tr>
					       		</c:forEach>
							</c:when> 
							<c:otherwise>
							      <tr class="">
									<td class="text-center"><span class="serialNumber" id="slNo1">1</span></td>
									<td class="text-center"><form:textarea class="form-control table-input" path="estimationDetailsForUpdate[0].itemDescription" id="estimationDetailsForUpdate0itemDescription" maxlength="256"></form:textarea></td>
									<td class="text-right"><form:input type="text" class="form-control table-input text-right patternvalidation quantity" data-pattern="decimalvalue" path="estimationDetailsForUpdate[0].quantity" id="estimationDetailsForUpdate0quantity" maxlength="8" onblur="calculateTotalAmount();" value="0"/></td>
									<td class="text-right">
										<form:select path="estimationDetailsForUpdate[0].unitOfMeasurement" data-first-option="false" id="estimationDetailsForUpdate0unitOfMeasurement"
												cssClass="form-control">
												<form:option value="">
													<spring:message code="lbl.select" /> 
												</form:option>
												<form:options items="${uomList}" itemLabel="uom" itemValue="id" />
												<%-- <c:forEach items="${uomList}" var="entry">
											        <option value="${entry.id}">${entry.uom}</option> 
											    </c:forEach> --%>
										</form:select>	
									<td class="text-right"><form:input type="text" class="form-control table-input text-right patternvalidation unitrate" data-pattern="decimalvalue" path="estimationDetailsForUpdate[0].unitRate" id="estimationDetailsForUpdate0unitRate" maxlength="8" onblur="calculateTotalAmount();" value="0.00" /></td>
									<td class="text-right"><form:input type="text" class="form-control table-input text-right patternvalidation quantity" data-pattern="decimalvalue" path="estimationDetailsForUpdate[0].amount" id="estimationDetailsForUpdate0amount" maxlength="8" onblur="calculateGrandTotal();" value="0.00" /></td>
									<td class="text-center"><a href="javascript:void(0)" class="btn-sm btn-default" id="addRowId"><i class="fa fa-plus"></i></a></td>
							      </tr>
							</c:otherwise> 
						</c:choose>
					      <tr class="">
							<td class="text-center"></td>
							<td class="text-center"></td>
							<td class="text-right"></td>
							<td class="text-right"></td>
							<td class="text-right"><spring:message code="lbl.grandtotal" /></td>
							<td class="text-right"><input type="text" class="form-control text-right" id="grandTotal" disabled="true"></td>
							<td class="text-center"></td>
					      </tr>
					</tbody>
				</table>
				
			</div>				
		</div>
	</div>
</div>
<script src="<cdn:url  value='/resources/js/transactions/estimatedetails.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url  value='/resources/js/transactions/documentsupload.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url  value='/resources/javascript/helper.js?rnd=${app_release_no}'/>"></script>
<script	src="<cdn:url  value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"
	type="text/javascript"></script>