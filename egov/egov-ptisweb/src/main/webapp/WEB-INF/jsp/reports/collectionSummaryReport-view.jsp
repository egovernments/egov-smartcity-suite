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

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>
	<s:property value="%{mode}"/>
		<s:if test="%{mode=='zoneWise'}">
			<s:text name='zoneWiseCollectionReport.search' />
		</s:if>
		 <s:elseif test="%{mode=='wardWise'}">
		 	<s:text name='wardWiseCollectionReport.search' />
		</s:elseif>
         <s:elseif test="%{mode=='blockWise'}">	
         	<s:text name='blockWiseCollectionReport.search' />
		</s:elseif>
		<s:elseif test="%{mode=='localityWise'}">
			<s:text name='localityWiseCollectionReport.search' />
		</s:elseif>
		<s:elseif test="%{mode=='usageWise'}">
			<s:text name='usageWiseCollectionReport.search' />
		</s:elseif>
		
	</title>
	<script type="text/javascript">
		function populateBlock() {
			jQuery.ajax({
				url: "/egi/boundary/ajaxBoundary-blockByWard.action",
				type: "GET",
				data: {
					wardId : jQuery('#wardId').val()
				},
				cache: false,
				dataType: "json",
				success: function (response) {
					jQuery('#blockId').html("");
					jQuery.each(response, function (j, block) {
						jQuery('#blockId').append("<option value='"+block.blockId+"'>"+block.blockName+"</option>");
					});
					<s:if test="%{blockId != null}">
						jQuery('#blockId').val('<s:property value="%{blockId}"/>');
					</s:if>
				}, 
				error: function (response) {
					jQuery('#blockId').html("");
					bootbox.alert("No block details mapped for ward")
				}
			});
		}
	</script>
</head>
<body>
	<div id="colSummaryError" class="errorstyle" style="display:none;"></div> 
	<div class="row">
		<div class="col-md-12">
			<div class="panel-body">
			<s:if test="%{hasErrors()}">
				<div align="left">
					<s:actionerror />
					<s:fielderror/>
				</div>			
			</s:if>
				<s:form name="boundaryWiseCollectionForm" action="collectionSummaryReport" theme="simple"
					cssClass="form-horizontal form-groups-bordered">
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading">
							<div class="panel-title text-left">
									<s:if test="%{mode=='zoneWise'}">
										<s:text name='zoneWiseCollectionReport.search' />
									</s:if>
									 <s:elseif test="%{mode=='wardWise'}">
									 	<s:text name='wardWiseCollectionReport.search' />
									</s:elseif>
							         <s:elseif test="%{mode=='blockWise'}">	
							         	<s:text name='blockWiseCollectionReport.search' />
									</s:elseif>
									<s:elseif test="%{mode=='localityWise'}">
										<s:text name='localityWiseCollectionReport.search' />
									</s:elseif>
									<s:elseif test="%{mode=='usageWise'}">
										<s:text name='usageWiseCollectionReport.search' />
									</s:elseif>
							</div>
						</div>
						<div class="panel-body custom-form">
						
						<s:hidden id="finYearStartDate" name="finYearStartDate" value="%{finYearStartDate}"/> 
						<s:hidden id="mode" name="mode" value="%{mode}"/> 
			            
			            <s:if test="%{mode=='zoneWise'}">
			            	<div class="form-group">
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
											name="collectionsummary.zone" /> :</label>
								<div class="col-sm-3 add-margin">
										<s:select headerKey="-1"
											headerValue="%{getText('default.all')}" name="boundaryId"
											id="boundaryId" listKey="key" listValue="value"
											list="zoneBndryMap" cssClass="form-control" value="%{boundaryId}" 
											/>
								</div>			
							</div>
			            </s:if>
			            <s:elseif test="%{mode=='wardWise'}">
			           	 	<div class="form-group">
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
											name="collectionsummary.ward" /> :</label>
								<div class="col-sm-3 add-margin">
										<s:select headerKey="-1"
											headerValue="%{getText('default.all')}" name="boundaryId"
											id="boundaryId" listKey="key" listValue="value"
											list="wardBndryMap" cssClass="form-control" value="%{boundaryId}" 
											/>
								</div>			
							</div>
			            </s:elseif>
			             <s:elseif test="%{mode=='blockWise'}">
			           	 	<div class="form-group">
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
											name="collectionsummary.block" /> :</label>
								<div class="col-sm-3 add-margin">
										<s:select headerKey="-1"
											headerValue="%{getText('default.all')}" name="boundaryId"
											id="boundaryId" listKey="key" listValue="value"
											list="blockBndryMap" cssClass="form-control" value="%{boundaryId}" 
											/>
								</div>			
							</div>
			            </s:elseif>
			            <s:elseif test="%{mode=='localityWise'}">
			           	 	<div class="form-group">
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
											name="collectionsummary.locality" /> :</label>
								<div class="col-sm-3 add-margin">
										<s:select headerKey="-1"
											headerValue="%{getText('default.all')}" name="boundaryId"
											id="boundaryId" listKey="key" listValue="value"
											list="localityBndryMap" cssClass="form-control" value="%{boundaryId}" 
											/>
								</div>			
							</div>
			            </s:elseif>
			            <s:elseif test="%{mode=='usageWise'}"> 
							<div class="form-group">
								<label for="field-1" class="col-sm-2 control-label text-right">Property Usages :</label>
								<div class="col-sm-3 add-margin">
										<s:select headerKey="-1"
											headerValue="%{getText('default.all')}" name="propTypeCategoryId"
											id="propTypeCategoryId" listKey="id" listValue="usageName"
											list="propUsageList" cssClass="form-control" value="%{propTypeCategoryId}" 
											/>
								</div>			
							
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
										name="Zone" /> :</label>
								<div class="col-sm-3 add-margin">
									<s:select headerKey="-1"
										headerValue="%{getText('default.all')}" name="zoneId"
										id="zoneId" listKey="id" listValue="name"
										list="dropdownData.zoneList" cssClass="form-control" value="%{zoneId}"/>
								</div>
							</div>
							
							<div class="form-group">
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
										name="Ward" /> :</label>
								<div class="col-sm-3 add-margin">
									<s:select headerKey="-1"
										headerValue="%{getText('default.all')}" name="wardId"
										id="wardId" listKey="key" listValue="value"
										list="wardBndryMap" cssClass="form-control" value="%{wardId}" onchange="populateBlock()"/>
								</div>
							
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
										name="block" /> :</label>
								<div class="col-sm-3 add-margin">
									<s:select headerKey="-1"
										headerValue="%{getText('default.select')}" name="blockId"
										id="blockId" listKey="key" listValue="value"
										list="dropdownData.blockList" cssClass="form-control" value="%{blockId}" 
										/>
								</div>
							</div>
						</s:elseif>	
							
							<div class="form-group">
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
										name="titleTransferRegReport.fromdate" /><span class="mandatory1" id="prntMandatory">*</span> :</label>
								 <s:date name="fromDate" var="cdFormat" format="dd/MM/yyyy" />		
								<div class="col-sm-3 add-margin">
									<s:textfield id="fromDate"	name="fromDate" value="%{fromDate}"
										onkeyup="DateFormat(this,this.value,event,false,'3')"
										placeholder="DD/MM/YYYY"
										cssClass="form-control datepicker"
										data-inputmask="'mask': 'd/m/y'"
										onblur="validateDateFormat(this);" />
								</div>
								
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
										name="titleTransferRegReport.todate" /><span class="mandatory1" id="prntMandatory">*</span> :</label>
								 <s:date name="toDate" var="cdFormat1" format="dd/MM/yyyy" />		
								<div class="col-sm-3 add-margin">
									<s:textfield id="toDate" name="toDate" value="%{toDate}"
										onkeyup="DateFormat(this,this.value,event,false,'3')"
										placeholder="DD/MM/YYYY"
										cssClass="form-control datepicker"
										data-inputmask="'mask': 'd/m/y'"
										onblur="validateDateFormat(this);" />
								</div>
							</div>
							
							<div class="form-group">
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
										name="collectionsummary.collectionmode" /> :</label>
								<div class="col-sm-3 add-margin">
										<s:select headerKey="-1"
											headerValue="%{getText('default.all')}" name="collMode"
											id="collMode" listKey="key" listValue="value"
											list="collectionModesMap" cssClass="form-control" value="%{collMode}" 
											/>
								</div>	
								
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
										name="collectionsummary.transactionmode" /> :</label>
								<div class="col-sm-3 add-margin">
										<s:select headerKey="-1"
											headerValue="%{getText('default.all')}" name="transMode"
											id="transMode" listKey="type" listValue="type"
											list="dropdownData.instrumentTypeList" cssClass="form-control" value="%{transMode}" 
											/>
								</div>		
							</div>
						</div>
					</div>
				</s:form>

				<div class="row">
					<div class="text-center">
						<button type="button" id="btnsearch" class="btn btn-primary">
							Search</button>
						<button type="button" id="btnclose" class="btn btn-default" onclick="window.close();">
							Close</button>
					</div>
				</div>
			</div>

			<div class="row display-hide report-section">
				<div class="col-md-12 table-header text-left">
				<input type="hidden" value="${sessionScope.citymunicipalityname}, ${sessionScope.districtName} District" id="pdfTitle"/>
					<s:if test="%{mode=='zoneWise'}">
						<div class="col-md-12 table-header text-left" id="reportTitle">Zone Wise Collection Report Details</div>
					</s:if>
					 <s:elseif test="%{mode=='wardWise'}">
					 	<div class="col-md-12 table-header text-left" id="reportTitle">Ward Wise Collection Report Details</div>
					</s:elseif>
			         <s:elseif test="%{mode=='blockWise'}">	
			         	<div class="col-md-12 table-header text-left" id="reportTitle">Block Wise Collection Report Details</div>
					</s:elseif>
					<s:elseif test="%{mode=='localityWise'}">
					<div class="col-md-12 table-header text-left" id="reportTitle">Locality Wise Collection Report Details</div>
					</s:elseif>
					<s:elseif test="%{mode=='usageWise'}">  
						<div class="col-md-12 table-header text-left" id="reportTitle">Usage Wise Collection Report Details</div>  
					</s:elseif>
				</div>
				<div class="col-md-12 form-group report-table-container">
					<table class="table table-bordered table-hover multiheadertbl" id="tblCollectionSummary">
						<thead>
							<tr> 
								<s:if test="%{mode=='zoneWise'}">
									<th>Zone</th> 
								</s:if>
								 <s:elseif test="%{mode=='wardWise'}">
								 	<th>Ward</th> 
								</s:elseif>
						         <s:elseif test="%{mode=='blockWise'}">	
						         	<th>Block</th> 
								</s:elseif>
								<s:elseif test="%{mode=='localityWise'}">
									<th>Locality</th> 
								</s:elseif> 
								<s:elseif test="%{mode=='usageWise'}">
									<th>Property Type</th> 
								</s:elseif>
								<th>Arrear Tax Amount</th>
								<th>Arrear LibraryCess Amount</th>
								<th>Arrear Total</th>
								<th>Current Tax Amount</th>
								<th>Current LibraryCess Amount</th>
								<th>Current Total</th>
								<th>Penalty</th>
								<th>Arrear Penalty Amount</th>
								<th>Penalty Total</th>
								<th>Grand Total</th>
							</tr>
						</thead>
						<tfoot id="report-footer">
							<tr>
								<td>Total</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
						</tfoot> 
					</table>
				</div>
			</div>
		</div>
	</div>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.columnFilter.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"
	type="text/javascript"></script>
<script type="text/javascript" src="<cdn:url value='/resources/javascript/collectionSummaryReport.js?rnd=${app_release_no}'/>"></script>
</html>