<!-- -------------------------------------------------------------------------------
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
<%@ include file="/includes/taglibs.jsp" %> 

<html>
<title><s:text name="sor.list" /></title>
<style>
body
{
  font-size: 14px;
  font-family:regular;
}
</style>

<script type="text/javascript">
     
	function validate(){	
		document.forms[0].action='<%=request.getContextPath()%>/masters/scheduleOfRate-searchSorDetails.action';
		document.forms[0].submit();
	}
	</script>
	<body>
		<div class="new-page-header">
			Search Schedule Of Rate
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
    	
    	
			
		<s:form name="searchSORForm" id="searchSORForm" action="/masters/scheduleOfRate-searchSorDetails.action" theme="simple" cssClass="form-horizontal form-groups-bordered">			
			
			
			
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
									<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}"
									name="scheduleCategoryId" id="scheduleCategory" cssClass="form-control"
									list="dropdownData.scheduleCategoryList" listKey="id"
									listValue="code"/>
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
							<s:textfield name="description" id="description" cssClass="form-control"/>
						</div>
					</div>
					
				</div>
			</div>

			<div class="row">
				<div class="col-xs-12 text-center buttonholdersearch">
					<input type="submit" class="btn btn-primary" value="Search" id="searchButton" name="button" 
						 onClick="validate()" /> &nbsp;
					<input type="button" class="btn btn-default" value="Close" id="closeButton" name="button"
						onclick="window.close();" />
				</div>
			</div>
			<s:if test="%{searchResult.fullListSize != 0 && displData=='yes'}">
			<div class="row report-section">
				<div class="col-md-12 table-header text-left">
				  <s:text name="title.search.result" />
				</div>
				
				<div class="col-md-12 report-table-container">
					<display:table name="searchResult" pagesize="30" uid="currentRow"
						cellpadding="0" cellspacing="0" requestURI=""
						class="table table-hover">
						
						<display:column headerClass="pagetableth" class="pagetabletd" 
							title="Sl.No" titleKey="column.title.SLNo"
							style="width:4%;text-align:right" >
							<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize" />
						</display:column>
						
						<display:column headerClass="pagetableth" class="pagetabletd" 
							title="Category Type" titleKey="master.sor.category"
							style="width:10%;text-align:left" property="scheduleCategory.code">
						</display:column>
						
						<display:column headerClass="pagetableth" class="pagetabletd" 
							title="SOR Code" titleKey="master.sor.code"
							style="width:10%;text-align:left" property="code">
						</display:column>
						
						<display:column headerClass="pagetableth" class="pagetabletd" 
							title="SOR Decsription" titleKey="master.sor.description"
							style="width:51%;text-align:left" property="description">
						</display:column>
						
						<display:column headerClass="pagetableth" class="pagetabletd" 
							title="Unit of Measure" titleKey="master.sor.uom"
							style="width:9%;text-align:left" property="uom.uom">
						</display:column>
																		
						<display:column headerClass="pagetableth" class="pagetabletd" 
							title="Action" style="width:13%;text-align:left" >
									<egov-authz:authorize actionName="WorksSOREditAutho">                   		
										<a class="btn btn-default" href="${pageContext.request.contextPath}/masters/scheduleOfRate-edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=edit">
											<s:text name="sor.edit" /></a>
									</egov-authz:authorize>
									<egov-authz:authorize actionName="WorksSORViewAutho">
										<a class="btn btn-default" href="${pageContext.request.contextPath}/masters/scheduleOfRate-edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=view">
											<s:text name="sor.view" />
										</a>
								 </egov-authz:authorize>
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
	</body>
</html>
