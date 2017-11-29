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

<%@ include file="/includes/taglibs.jsp"%>
<html>
<title><s:text name="contractor.master.title" /></title>
<body>
    
	
	
	<div class="row">
						<div class="col-md-12">
							<div class="panel panel-primary" data-collapsed="0">
								
								<div class="panel-heading">
									<div class="panel-title text-center no-float">
										<s:if test="%{hasActionMessages()}">
											<div id="msgsDiv">
												<s:actionmessage theme="simple" />
											</div>
										</s:if>
									</div>
									
								</div>
								<div class="panel-body">
									<div class="row add-border">
										<div class="col-xs-3 add-margin">
											Contractor Code
										</div>
										<div class="col-xs-3 add-margin view-content">
											123123
										</div>
										<div class="col-xs-3 add-margin">
											Name
										</div>
										<div class="col-xs-3 add-margin view-content">
											XYZ
										</div>
									</div>
									<div class="row add-border">
										<div class="col-xs-3 add-margin">
											Correspondence Address
										</div>
										<div class="col-xs-3 add-margin view-content">
											Address1
										</div>
										<div class="col-xs-3 add-margin">
											Payment Address 
										</div>
										<div class="col-xs-3 add-margin view-content">
											Address2
										</div>
									</div>
									<div class="row add-border">
										<div class="col-xs-3 add-margin">
											Contact Person  
										</div>
										<div class="col-xs-3 add-margin view-content">
											XYZ
										</div>
										<div class="col-xs-3 add-margin">
											Email 
										</div>
										<div class="col-xs-3 add-margin view-content">
											XYZ@XYZ.COM
										</div>
									</div>
									<div class="row add-border">
										<div class="col-xs-3 add-margin">
											Narration
										</div>
										<div class="col-xs-9 add-margin view-content">
											-
										</div>
									</div>
									<div class="row add-border">
										<div class="col-xs-3 add-margin">
											PAN No 
										</div>
										<div class="col-xs-3 add-margin view-content">
											BKEPD123SDW
										</div>
									
										<div class="col-xs-3 add-margin">
											TIN No	
										</div>
										<div class="col-xs-3 add-margin view-content">
											123SDe
										</div>
									</div>
									<div class="row add-border">
										<div class="col-xs-3 add-margin">
											Bank
										</div>
										<div class="col-xs-3 add-margin view-content">
											XYZ Bank
										</div>
										<div class="col-xs-3 add-margin">
											IFSC Code
										</div>
										<div class="col-xs-3 add-margin view-content">
											IND12331
										</div>
									</div>
									<div class="row add-border">
										<div class="col-xs-3 add-margin">
											Bank Account Number
										</div>
										<div class="col-xs-3 add-margin view-content">
											45906123123
										</div>
										<div class="col-xs-3 add-margin">
											PWD Approval Code
										</div>
										<div class="col-xs-3 add-margin view-content">
											-
										</div>
									</div>
									<div class="row">
										<div class="col-xs-3 add-margin">
											Exempted From
										</div>
										<div class="col-xs-3 add-margin view-content">
											12/12/2015
										</div>
									</div>
								</div>
							</div>					
						</div>
				</div>

				<div class="row">
						<div class="col-md-12">
							<div class="panel panel-primary" data-collapsed="0">
								
								<div class="panel-heading">
									<div class="panel-title">
										Contractor Details
									</div>
									
								</div>
								<div class="panel-body">
									<table class="table table-bordered">
										<thead>
											<tr>
												<th>S. No.</th>
												<th>Department</th>
												<th>Registration Number</th>
												<th>Contractor Class</th>
												<th>Status</th>
												<th>From Date</th>
												<th>To Date</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>1</td>
												<td>XYZ</td>
												<td>4560</td>
												<td>-</td>
												<td>1</td>
												<td>23/01/2014</td>
												<td>23/01/2014</td>
											</tr>
											<tr>
												<td>1</td>
												<td>XYZ</td>
												<td>4560</td>
												<td>-</td>
												<td>1</td>
												<td>23/01/2014</td>
												<td>23/01/2014</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>					
						</div>
				</div>

				

				<div class="row text-center">
					<div class="add-margin">
						<input type="submit" name="VIEW" Class="btn btn-primary" value="View"
							id="VIEW" onclick="viewData();" /> <input type="submit"
							name="create" Class="btn btn-primary" value="Create" id="CREATE"
							name="button" onclick="createNew();" /> <input type="submit"
							name="closeButton" id="closeButton" value="Close"
							Class="btn btn-default" onclick="window.close();" />
					</div>
				</div>
<script type="text/javascript">

function createNew() {
	window.location = '${pageContext.request.contextPath}/masters/contractor-newform.action';
}
 			
function viewData() {
	var id = '<s:property value="%{model.id}"/>';
	window.location = '${pageContext.request.contextPath}/masters/contractor-edit.action?mode=view&id='+id;
}
				
</script>
		
</body>
</html>