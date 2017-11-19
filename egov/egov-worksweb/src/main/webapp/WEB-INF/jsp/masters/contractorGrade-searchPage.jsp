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

<html>
<title><s:text name="contractor.grade.list" /></title>

<script type="text/javascript">
     
	function validate(){	
		
		document.forms[0].action='<%=request.getContextPath()%>/masters/contractorGrade-searchGradeDetails.action?mode=search';
		
		document.forms[0].submit();
		
	}
	</script>
<body >
       <div class="new-page-header">
			<s:text name='contractor.grade.header.search' />
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
    	
		 <s:form name="searchGradeForm" id="searchGradeForm" cssClass="form-horizontal form-groups-bordered" action="/masters/contractorGrade-searchGradeDetails.action" theme="simple">			
			
			  <div class="panel panel-primary" data-collapsed="0"	style="text-align: left">
						<div class="panel-heading">
							<div class="panel-title"><s:text name='title.search.criteria' /></div>
						</div>
						
						<div class="panel-body">
						
							<div class="form-group">
								<label class="col-sm-2 control-label text-right"> 
								   <s:text name="contractor.grade.master.grade" />
								</label>
								<div class="col-sm-3 add-margin">
									<s:textfield name="grade" id="grade" maxlength="20" cssClass="form-control"/>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label text-right"> 
								   <s:text name="contractor.grade.master.minamount" />
								</label>
								<div class="col-sm-3 add-margin">
								  <s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}"
										name="minAmountString" id="minAmount" cssClass="form-control"
										list="minAmountList" value="%{minAmountString}" />
								</div>
								<label class="col-sm-2 control-label text-right"> 
									<s:text name="contractor.grade.master.maxamount" />
								</label>
								<div class="col-sm-3 add-margin">
									<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}"
										name="maxAmountString" id="maxAmount" cssClass="form-control"
										list="maxAmountList" value="%{maxAmountString}" />
								</div>
							</div>
							
					</div>
			</div>
			
			<div class="row">
					<div class="col-xs-12 text-center buttonholdersearch">
						<input type="submit" class="btn btn-primary" value="Search" id="searchButton" onClick="validate()" />&nbsp;
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
						title="Contractor Class" titleKey="contractor.grade.master.grade"
						style="width:20%;text-align:left" property="grade"/>
																		
					<display:column headerClass="pagetableth" class="pagetabletd" 
						title="Description" titleKey="contractor.grade.master.description"
						style="width:40%;text-align:left" property="description"/>
																		
					<display:column headerClass="pagetableth" class="pagetabletd" 
						title="Minimum Amount " titleKey="contractor.grade.master.minamount"
						style="width:10%;text-align:right" >
						<s:text name="contractor.format.number" >
				   	<s:param name="rate" value='%{#attr.currentRow.minAmount}' /></s:text>
					</display:column>
					
					<display:column headerClass="pagetableth" class="pagetabletd" 
						title="Maximum Amount" titleKey="contractor.grade.master.maxamount"
						style="width:10%;text-align:right"  >
						<s:text name="contractor.format.number" >
				   	<s:param name="rate" value='%{#attr.currentRow.maxAmount}' /></s:text>
					</display:column>
					
					<display:column headerClass="pagetableth" class="pagetabletd" 
						title="View" style="width:13%;text-align:left" >
						
									<a class="btn btn-default" href="${pageContext.request.contextPath}/masters/contractorGrade-edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=view">
										<s:text name="sor.view" />
									</a>
							
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
