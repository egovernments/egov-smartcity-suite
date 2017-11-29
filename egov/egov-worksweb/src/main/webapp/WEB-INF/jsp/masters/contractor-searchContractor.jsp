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

<%@ include file="/includes/taglibs.jsp" %>
<script src="<cdn:url value='/resources/js/works.js?rnd=${app_release_no}'/>"></script> 
<html>
<title><s:text name="contractor.search.title" /></title>

<body >
<s:if test="%{hasErrors()}">
	 <div class="errorstyle">
   		<s:actionerror/>
   		<s:fielderror/>
  	</div>
</s:if>
<s:if test="%{hasActionMessages()}">
	<div id="msgsDiv" class="messagestyle">
		<s:actionmessage theme="simple"/>
	</div>
</s:if>
    	
<div class="new-page-header">
	<s:text name="contractor.search.title" />
</div>
    	
<div id="contractorError" class="alert alert-danger" style="display: none;"></div>
<s:form action="/masters/contractor-viewResult.action" theme="simple" name="contractor" cssClass="form-horizontal form-groups-bordered">
					
<div class="panel panel-primary" data-collapsed="0"
	style="text-align: left">
	<div class="panel-heading">
		<div class="panel-title"><s:text name='title.search.criteria' /></div>
	</div>
	
	<div class="panel-body">
		<div class="form-group">
			<label class="col-sm-2 control-label text-right"> 
			   <s:text name="contractor.name" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:textfield name="contractorName" id="contractorName" cssClass="form-control"/>
			</div>
			<label class="col-sm-2 control-label text-right"> 
					<s:text name="contractor.code" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:textfield name="contractorCode" id="contractorCode" cssClass="form-control"/>
			</div>
		</div>
				
		<div class="form-group">
			<label class="col-sm-2 control-label text-right"> 
			   <s:text name="contractor.department" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:select id="department" name="departmentId" cssClass="form-control" list="%{dropdownData.departmentList}" listKey="id" listValue="name" headerKey="" headerValue="--- Select ---" />
			</div>
			<label class="col-sm-2 control-label text-right"> 
				<s:text name="contractor.status" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:select id="status" name="statusId" cssClass="form-control" list="%{dropdownData.statusList}" listKey="id" listValue="description" headerKey="" headerValue="--- Select ---" />
			</div>
		</div>
				
		<div class="form-group">
			<label class="col-sm-2 control-label text-right"> 
			   <s:text name="contractor.grade" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:select id="grade" name="gradeId" cssClass="form-control" list="%{dropdownData.gradeList}" listKey="id" listValue="grade" headerKey="" headerValue="--- Select ---" />
			</div>
		</div>
	</div>
</div>
			<input type="hidden" value="<s:text name='contractor.select.error' />" id='selectMessage'>
			<s:hidden name="id" id="id" />
<div class="row">
	<div class="col-xs-12 text-center buttonholdersearch">
		<input type="submit" class="btn btn-primary" value="Search" id="searchButton" name="button" onClick="return validate()" />&nbsp;
		<input type="button" class="btn btn-default" value="Close" id="closeButton" name="button" onclick="window.close();" />
	</div>
</div>
		
	<s:text var="select"	name="%{getText('column.title.select')}"></s:text>
 	<s:text	var="slNo" name="%{getText('column.title.SLNo')}"></s:text>
	<s:text var="code" name="%{getText('contractor.code')}"></s:text>
	<s:text var="name" name="%{getText('contractot.contractorname')}"></s:text>
	<s:text var="class" name="%{getText('contractor.grade')}"></s:text>
	<s:text var="status"	name="%{getText('contractor.status')}" />
		<s:if test="%{searchResult.fullListSize != 0}">
		<div class="row report-section">
			<div class="col-md-12 table-header text-left">
				<s:text name="title.search.result" />
			</div>
				
				<div class="col-md-12 report-table-container">
					<display:table name="searchResult" pagesize="30" uid="currentRow" cellpadding="0" cellspacing="0" requestURI="" class="table table-hover">
					
					<display:column headerClass="pagetableth" class="pagetabletd" title="${select}" style="width:2%;" titleKey="column.title.select">
						<input name="radio" type="radio" id="radio" value="<s:property value='%{#attr.currentRow.id}'/>" onClick="setContractorId('<s:property value='%{#attr.currentRow.id}'/>');" />
						
					</display:column>

					<display:column headerClass="pagetableth"  class="pagetabletd" title="${slNo}"  style="width:4%;text-align:right">
						<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize" />
					</display:column>

					<display:column headerClass="pagetableth" class="pagetabletd" title="${name}"
						style="width:15%;text-align:left" property="name" >
					</display:column>
					
					<display:column headerClass="pagetableth" class="pagetabletd" title="${code}"
						style="width:15%;text-align:left" property="code" >
					</display:column>

					<display:column headerClass="pagetableth" class="pagetabletd" title="${class}" style="width:15%;text-align:left">
						<s:property value="#attr.currentRow.contractorDetails[0].grade.grade" />
					</display:column>

					<display:column headerClass="pagetableth" class="pagetabletd" title="${status}" style="width:15%;text-align:left">
						<s:property value="#attr.currentRow.contractorDetails[0].status.description" />
					</display:column>
							
					</display:table>
			</div>
		</div>
		</s:if>
		<s:elseif test="%{searchResult.fullListSize == 0}">
			<div class="row report-section">
				<div class="col-md-12 table-header text-left">
				  <s:text name="title.search.result" />
				</div>
				
				<div class="col-md-12 text-center report-table-container">
				   <div class="alert alert-warning no-margin"><s:text name="label.no.records.found"/></div>
				</div>
			</div>
		</s:elseif>
		
			
</s:form>
<s:if test="%{searchResult.fullListSize != 0}">
	<div align="center">
		<input type="submit" name="VIEW" Class="btn btn-primary" value="View" id="VIEW" onclick="return viewContractorDataOnSearch();" /> 
		<input type="submit" name="MODIFY" Class="btn btn-primary" value="Modify" id="MODIFY" onclick="return modifyContractorDataOnSearch();" /> 
		<input type="submit" name="closeButton" id="closeButton" value="Close" Class="btn btn-default" onclick="window.close();" /> &nbsp;&nbsp;
	</div>
</s:if>
</body>
</html>