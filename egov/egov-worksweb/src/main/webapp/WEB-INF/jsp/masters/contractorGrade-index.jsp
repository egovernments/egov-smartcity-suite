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
<script src="<cdn:url value='/resources/js/contractorgrade.js?rnd=${app_release_no}'/>"></script> 
<html>
<title><s:text name="contractor.grade.list" /></title>
<body>

<s:if test="%{hasErrors()}">
	<div class="alert alert-danger">
		<s:actionerror />
		<s:fielderror />
	</div>
</s:if>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">

			<div class="panel-heading">
				<div class="panel-title text-center no-float">
					<s:if test="%{hasActionMessages()}">
						<s:actionmessage theme="simple" />
					</s:if>
					<s:else>
						<s:text name="contractor.grade.master" />
					</s:else>
				</div>

			</div>
			<div class="panel-body">
				<s:iterator value="contractorGradeList">
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<s:text name="contractor.grade.master.grade" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<s:property value="grade" />
						</div>
						<div class="col-xs-3 add-margin">
							<s:text name="contractor.grade.master.description" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<s:property value="description" />
						</div>

					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<s:text name="contractor.grade.master.minamount" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<s:text name="contractor.format.number">
								<s:param name="rate" value='%{minAmount}' />
							</s:text>
						</div>
						<div class="col-xs-3 add-margin">
							<s:text name="contractor.grade.master.maxamount" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<s:text name="contractor.format.number">
								<s:param name="rate" value='%{maxAmount}' />
							</s:text>
						</div>
					</div>
				</s:iterator>
			</div>
		</div>
	</div>
</div>

	<s:hidden name="id" id="id" />
	
<div class="row text-center">
	<div class="add-margin">
		<input type="submit" name="MODIFY" Class="btn btn-primary" value="MODIFY" id="MODIFY" onclick="modifyGradeData();" /> &nbsp;
		<input type="submit" name="VIEW" Class="btn btn-primary" value="VIEW" id="MODIFY" onclick="viewGradeData();" /> &nbsp;
		<input type="submit" name="closeButton" id="closeButton" value="Close" class="btn btn-default" onclick="window.close();" />
	</div>
</div>
</body>
</html>


