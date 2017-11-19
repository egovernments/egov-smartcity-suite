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

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/includes/taglibs.jsp" %> 
<!DOCTYPE html>
<html>
<head>
<title>Upload Successful</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">  
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">   
<link rel="stylesheet" href="../resources/css/jquerymobile/jquery.mobile-1.3.1.min.css" />
<script src="../resources/js/jquery-1.7.2.min.js"></script>
<script src="../resources/js/jquerymobile/jquery.mobile-1.3.1.min.js"></script>
<style>
@media screen and (min-width: 20em) {
	   .my-custom-class th.ui-table-priority-1,
	   .my-custom-class td.ui-table-priority-1 {
	     display: table-cell;
	   }
	}
	/* Show priority 2 at 480px (30em x 16px) */
	@media screen and (min-width: 30em) {
	   .my-custom-class  th.ui-table-priority-2,
	   .my-custom-class td.ui-table-priority-2 {
	     display: table-cell;
	   }
	}
</style>
</head>
<body >
	<s:form action="uploadEstimatePhotos!search.action" enctype="multipart/form-data" theme="simple" name="tablet" id="tablet" data-ajax="false">
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:if test="%{hasActionMessages()}">
			<div class="errorstyle">
				<s:actionmessage />
			</div>
		</s:if>
		<div id="uploadfiles" data-role="page" data-add-back-btn="false" class="pageclass">
			<div data-theme="b" data-role="header" data-position="fixed">
				<h5>Upload Successful</h5>
			</div>
			<fieldset class="ui-grid-a"  align="center" >
				<div>
					<img src="../css/jquerymobile/images/greetick.png"  style="height: 24px;width: 24px" />&nbsp;&nbsp;&nbsp;<s:property value="successMessage" />
				</div>
				<div class="ui-block-a">
					<button type="submit" data-mini="true" id="submit" data-theme="b">Search</button>
				</div>
				<div class="ui-block-b">
					<button type="button" data-mini="true" id="cancel" onclick="window.close();" data-theme="d">Close</button>
				</div>
			</fieldset>
		</div>
	</s:form>
</body>
</html>
