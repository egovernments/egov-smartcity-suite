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
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title><s:text name='license.search' /></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
</head>
<script>
var applicationNoType='<s:property value="%{@org.egov.tl.utils.Constants@SEARCH_BY_APPNO}"/>'; 
var licenseNoType='<s:property value="%{@org.egov.tl.utils.Constants@SEARCH_BY_LICENSENO}"/>'; 
var oldLicenseNoType='<s:property value="%{@org.egov.tl.utils.Constants@SEARCH_BY_OLDLICENSENO}"/>'; 
var tradeTitleType='<s:property value="%{@org.egov.tl.utils.Constants@SEARCH_BY_TRADETITLE}"/>'; 
var ownerNameType='<s:property value="%{@org.egov.tl.utils.Constants@SEARCH_BY_TRADEOWNERNAME}"/>'; 
var propertyNoType='<s:property value="%{@org.egov.tl.utils.Constants@SEARCH_BY_PROPERTYASSESSMENTNO}"/>'; 
var mobileNoType='<s:property value="%{@org.egov.tl.utils.Constants@SEARCH_BY_MOBILENO}"/>'; 
var wf_approved_status='<s:property value="%{@org.egov.tl.utils.Constants@LICENSE_WF_APPROVED_STATUS}"/>'; 
var wf_certificateGenerate_status='<s:property value="%{@org.egov.tl.utils.Constants@LICENSE_WF_CERTIFICATEGENERATED_STATUS}"/>'; 
</script> 
<body>
	<div id="tradeSearchError" class="errorstyle" style="display:none;"></div>
	<div class="row">
		<div class="col-md-12">
			<div class="panel-body">
				<s:form name="searchTradeForm" action="searchTrade" theme="simple"
					cssClass="form-horizontal form-groups-bordered">
					<s:hidden id="licenseId" name="licenseId" value="%{licenseId}"/> 
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading">
							<div class="panel-title text-left"><s:text name='license.search' /></div>
						</div>
						
						<div class="panel-body custom-form">
							<div class="form-group">
							    <label class="col-sm-3 control-label text-right"><s:text name='license.applicationnumber' /></label>
							    <div class="col-sm-3 add-margin">
							       <s:textfield name="applicationNumber" id="applicationNumber" value="%{applicationNumber}"  cssClass="form-control" />
							    </div>
							    <label class="col-sm-2 control-label text-right"><s:text name='search.license.cancelled' /></label>
								<div class="col-sm-3 add-margin">
									<s:checkbox id="isCancelled" name="isCancelled" />
								</div>
							</div>
							
							<div class="form-group">
							    <label class="col-sm-3 control-label text-right"><s:text name="search.licensee.no" /></label>
							    <div class="col-sm-3 add-margin">
							       <s:textfield name="licenseNumber" id="licenseNumber" value="%{licenseNumber}" cssClass="form-control" />
							    </div>
							    <label class="col-sm-2 control-label text-right"><s:text name='license.oldlicensenum' /></label>
							    <div class="col-sm-3 add-margin">
							      	<s:textfield name="oldLicenseNumber" id="oldLicenseNumber" value="%{oldLicenseNumber}" cssClass="form-control"/>
							    </div>
							</div>
							
							<div class="form-group">
							    <label class="col-sm-3 control-label text-right"><s:text name="search.license.category" /></label>
							    <div class="col-sm-3 add-margin">
							        <s:select name="category" id="category" list="dropdownData.categoryList"
										listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('default.select')}" value="%{category.id}" class="form-control" onChange="setupAjaxSubCategory(this);" />
										<egov:ajaxdropdown id="populateSubCategory" fields="['Text','Value']" dropdownId='subCategory' url='domain/commonTradeLicenseAjax-populateSubCategory.action' />
							    </div>
							    <label class="col-sm-2 control-label text-right"><s:text name='search.license.subCategory' /></label>
							    <div class="col-sm-3 add-margin">
							      	  <s:select name="tradeName" id="subCategory" list="dropdownData.subCategoryList"
										listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('default.select')}" value="%{tradeName.id}" class="form-control"/>
							    </div>
							</div>
							
							<div class="form-group">
							    <label class="col-sm-3 control-label text-right"><s:text name="search.license.establishmentname" /></label>
							    <div class="col-sm-3 add-margin">
							       <s:textfield name="tradeTitle" id="tradeTitle" value="%{tradeTitle}" cssClass="form-control" />
							    </div>
							    <label class="col-sm-2 control-label text-right"><s:text name='licensee.applicantname' /></label>
							    <div class="col-sm-3 add-margin">
							      	<s:textfield name="tradeOwnerName" id="tradeOwnerName"  value="%{tradeOwnerName}"  cssClass="form-control"/>
							    </div>
							</div>
							
							
							<div class="form-group">
							    <label class="col-sm-3 control-label text-right"><s:text name="search.license.propertyNo" /></label>
							    <div class="col-sm-3 add-margin">
							       <s:textfield name="propertyAssessmentNo" id="propertyAssessmentNo" value="%{propertyAssessmentNo}" cssClass="form-control" />
							    </div>
							    <label class="col-sm-2 control-label text-right"><s:text name='search.licensee.mobileNo' /></label>
							    <div class="col-sm-3 add-margin">
							      	<s:textfield name="mobileNo" id="mobileNo" value="%{mobileNo}" onKeyPress="return numbersonly(this, event)" onBlur="checkMinLength(this,10)" maxlength="10" cssClass="form-control patternvalidation" data-pattern="number"/>
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
				<div class="col-md-12 table-header text-left">Search Result</div>
				<div class="col-md-12 form-group report-table-container">
					<table class="table table-bordered table-hover multiheadertbl" id="tblSearchTrade">
						<thead>
							<tr>
								<th>Application Number</th>
							    <th>TL Number</th>
								<th>Old TL Number</th>
								<th>Category</th>
								<th>Sub Category</th>
								<th>Tittle of Trade</th>
								<th>Trade Owner</th>
								<th>Mobile Number</th>
								<th>Property Assessment Number</th>
								<th>Actions</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
<link rel="stylesheet" href="<cdn:url  value='/resources/global/css/bootstrap/typeahead.css' context='/egi'/>">
<script type="text/javascript" src="<cdn:url  value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url  value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url  value='/resources/js/app/searchTradeLicense.js?rnd=${app_release_no}'/>"></script>
	 <style>
		.ui-autocomplete { 
		    /* these sets the height and width */
		    max-height:200px; 
		    max-width: 236px; 
		
		    /* these make it scroll for anything outside */
		    overflow-y:scroll;
		}
	</style>
</body>
</html>