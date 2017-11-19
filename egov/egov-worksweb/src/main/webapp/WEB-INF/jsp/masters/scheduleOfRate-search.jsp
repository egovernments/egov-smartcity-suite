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
<script src="<egov:url path='/resources/js/works.js?${app_release_no}'/>"></script> 
<html>
<title><s:text name="sor.list" /></title>
<style>
body
{
  font-size: 14px;
  font-family:regular;
}
</style>

<body>
<s:hidden name="id" id="id" />
<div class="new-page-header">
	<s:text name="search.sorRate.header" />
</div>

<s:if test="%{hasErrors()}">
	<div class="alert alert-danger">
		<s:actionerror/>
   		<s:fielderror/>
    </div>
 </s:if>
  	
<s:if test="%{hasActionMessages()}">
	<div id="msgsDiv" class="messagestyle">
    	<s:actionmessage theme="simple"/>
	</div>
</s:if>
<div id="sor.sorError" class="alert alert-danger" style="display: none;"></div>
<s:form name="searchSORForm" id="searchSORForm" action="/masters/scheduleOfRate-searchSorDetails.action" theme="simple" cssClass="form-horizontal form-groups-bordered" >			
<div class="panel panel-primary" data-collapsed="0" style="text-align:left">
	<div class="panel-heading">
		<div class="panel-title">
		    <s:text name='title.search.criteria' />
		</div>
	</div>
	<div class="panel-body">
	    <s:if test="%{scheduleCategoryList.size != 0}">
		   <div class="form-group">
				<label class="col-sm-2 control-label text-right">
				    <s:text name="master.sor.category" /><span class="mandatory"></span>
				</label>
				<div class="col-sm-3 add-margin">
					<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="scheduleCategoryId" id="scheduleCategory" cssClass="form-control" list="dropdownData.scheduleCategoryList" listKey="id" listValue="code"/>
				</div>
			</div>
		</s:if>
		
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <s:text name="master.sor.code" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:textfield name="code" id="code" cssClass="form-control"/>
			</div>
			<label class="col-sm-2 control-label text-right">
			    <s:text name="master.sor.description" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:textarea name="description" cols="45"  rows="3" id="description" cssClass="form-control" maxlength = "4000"/>
			</div>
		</div>
	</div>
</div>

<input type="hidden" value="<s:text name='master.sor.select.error' />" id='selectMessage'>
<s:hidden name="id" id="id" />

<div class="row">
	<div class="col-xs-12 text-center buttonholdersearch">
		<input type="submit" class="btn btn-primary" value="Search" id="searchButton" name="button" onClick="validateSOR()" /> &nbsp;
		<input type="button" class="btn btn-default" value="Close" id="closeButton" name="button" onclick="window.close();" />
	</div>
</div>
<s:if test="%{searchResult.fullListSize != 0 && displData=='yes'}">
	<div class="row report-section">
		<div class="col-md-12 table-header text-left">
		  <s:text name="title.search.result" />
		</div>
		
		<div class="col-md-12 report-table-container">
			<s:text id="select"	name="%{getText('column.title.select')}"></s:text>
			<s:text id="slNo" name="%{getText('column.title.SLNo')}"></s:text>
			<s:text id="sorCode" name="%{getText('master.sor.code')}"></s:text>
			<s:text id="sorDescription" name="%{getText('master.sor.description')}"></s:text>
			<s:text id="sorRate" name="%{getText('master.sor.rate')}"></s:text>
			<s:text id="unitOfMeasure" name="%{getText('master.sor.uom')}"></s:text>
			<s:text id="startDate" name="%{getText('master.sor.startDate')}"></s:text>
			<s:text id="endDate" name="%{getText('master.sor.endDate')}"></s:text>
		
			<display:table name="searchResult" pagesize="30" uid="currentRow" cellpadding="0" cellspacing="0" requestURI=""	class="table table-hover">
				<display:column headerClass="pagetableth" class="pagetabletd" title="${select}"
				style="width:2%;" titleKey="column.title.select">
				<input name="radio" type="radio" id="radio" value="<s:property value='%{#attr.currentRow.id}'/>" onClick="setSorId('<s:property value='%{#attr.currentRow.id}'/>');" />
				</display:column>
				
				<display:column headerClass="pagetableth" class="pagetabletd" title="${slNo}"style="width:4%;text-align:right" >
					<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize" />
				</display:column>
				
				<display:column headerClass="pagetableth" class="pagetabletd" title="${sorCode}" style="width:5%;text-align:left" property="code">
				</display:column>
				
				<display:column headerClass="pagetableth" class="pagetabletd" title="${sorDescription}" style="width:51%;text-align:left" property="description">
				</display:column>
				
				<display:column headerClass="pagetableth" class="pagetabletd"  title="${unitOfMeasure}" style="width:9%;text-align:left" property="uom.uom">
				</display:column>

				<display:column headerClass="pagetableth" class="pagetabletd"  title="${sorRate}" style="width:10%;text-align:left">
					<s:property value="#attr.currentRow.sorRates.get(#attr.currentRow.sorRates.size-1).rate" />
				</display:column>
									
				<display:column headerClass="pagetableth" class="pagetabletd" title="${startDate}" style="width:10%;text-align:left" >
					<s:date name="#attr.currentRow.sorRates.get(#attr.currentRow.sorRates.size-1).validity.startDate" format="dd/MM/yyyy"/>
				</display:column>
				
				<display:column headerClass="pagetableth" class="pagetabletd" title="${endDate}" style="width:10%;text-align:left" >
					<s:date name="#attr.currentRow.sorRates.get(#attr.currentRow.sorRates.size-1).validity.endDate" format="dd/MM/yyyy"/>
				</display:column>
																	
			</display:table>
		</div>
	</div>
</s:if>
<s:elseif test="%{searchResult.fullListSize == 0 && displData=='noData'}">
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
		<input type="submit" name="VIEW" Class="btn btn-primary" value="View" id="VIEW" onclick=" return viewScheduleData();" /> 
		<input type="submit" name="MODIFY" Class="btn btn-primary" value="Modify" id="MODIFY" onclick="modifyScheduleData();" /> 
		<input type="submit" name="closeButton"	id="closeButton" value="Close" Class="btn btn-default" onclick="window.close();" /> &nbsp;&nbsp;
	</div>
</s:if>
</body>
</html>
