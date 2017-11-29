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


<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

<%@ page language="java"%>
<html>
<head>
<title><s:text name="bank.modify.new" /></title>
<link rel="stylesheet" type="text/css"
	href="/EGF/resources/css/jquery-ui/css/smoothness/jquery-ui-1.8.4.custom.css" />
<link rel="stylesheet" type="text/css" media="screen"
	href="../resources/css/jquery/ui.jqgrid.css" />
<script>
    var fundJson = "<s:property value='getFundsJSON()'/>";
    var accTypeJson = <s:property value='getAccountTypesJSON()' escapeHtml='false'/>;
    var bankAccTypeJson = "<s:property value='getBankAccountTypesJSON()'/>";
    var autoBankGLCode =  "<s:property value='isAutoBankAccountGLCodeEnabled()'/>";
	</script>
<style>
.EditTable td {
	text-align: left;
}

.center {
	width: 800px;
	margin-left: auto;
	margin-right: auto;
}
</style>
</head>
<body>

	<div class="formmainbox">
		<div class="subheadnew">
			<s:text name="bank.view.new" />
		</div>
		<jsp:include page="bank-form.jsp"></jsp:include>
	</div>
	<div class="buttonbottom">
		<input type="button" id="Close" value="Close"
			onclick="javascript:window.close()" class="button" />
	</div>
	<script src="../resources/javascript/jquery/grid.locale-en.js"
		type="text/javascript"></script>
	<script src="../resources/javascript/jquery/jquery.jqGrid.min.js"
		type="text/javascript"></script>
	<script>
	existingCode = '${model.code}';
    existingName = '${model.name}';
	viewGrid();
    </script>
</body>
</html>