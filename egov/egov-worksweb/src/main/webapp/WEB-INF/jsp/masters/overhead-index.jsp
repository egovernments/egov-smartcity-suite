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
<title>Overhead List</title>
<body >
 
		<s:if test="%{hasErrors()}">
        <div class="alert alert-danger">
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
    <s:if test="%{hasActionMessages()}">
        <div class="alert alert-success">
        	<s:actionmessage theme="simple"/>
        </div>
    </s:if>
    
    <div class="new-page-header">
		Overhead List
	</div>
	
	<div id="overheadTable" class="panel panel-primary" data-collapsed="0" style="text-align:left">
				<div class="panel-heading">
					<div class="panel-title">
					    Overhead Rate
					</div>
				</div>
				<div class="panel-body no-margin-bottom">
					<div class="form-group">
						<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table table-bordered table-hover">
						  <thead>
			              <tr>
			                <!--  <td width="10%" class="tablesubheadwk">ID</td>  -->
			                <th width="17%" class="tablesubheadwk">Name</th>
			                <th width="20%" class="tablesubheadwk">Account</th>
			                <th width="27%" class="tablesubheadwk">Description</th>
			                <th width="12%" class="tablesubheadwk">Expenditure Type</th>
			                <th width="14%" class="tablesubheadwk">Edit</th>
			                </tr>
			               </thead>
			               <tbody>
			                <s:iterator id="overheadIterator" value="overheadList" status="row_status">
							<tr>	
								<!-- <td width="10%"><s:property value="%{id}" /> </td> -->
								<td width="17%"><s:property value="%{name}" /> </td>	   
								<td width="20%"><s:property value="%{account.name}" /></td>
								<td width="27%"><s:property value="%{description}" /></td>
								<td width="12%"><s:property value="%{expenditureType.value}" /></td>
								<td width="14%">
			                      <a class="btn btn-default" href='${pageContext.request.contextPath}/masters/overhead!edit.action?id=<s:property value='%{id}'/>'>Edit</a>
			                    </td>
						
							</tr>
							</s:iterator>
							</tbody>
			            </table>
					</div>
				</div>
	</div>
	
	
	<div class="row">
		<div class="col-xs-12 text-center buttonholdersearch">
			<input type="button" class="btn btn-default" value="Close" id="closeButton" name="button"
				onclick="window.close();" />
		</div>
	</div>
	

</body>

</html>

			
