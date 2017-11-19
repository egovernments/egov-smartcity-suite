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

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">

    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="description" content="eGov Urban Portal"/>
    <meta name="author" content=""/>

    <title>eGov Urban Portal</title>

    <link rel="icon" href="<cdn:url  value='/resources/global/images/favicon.png' context='/egi'/>" sizes="32x32">
    <link rel="stylesheet" href="<cdn:url  value='/resources/global/css/bootstrap/bootstrap.css' context='/egi'/>">
    <link rel="stylesheet" href="<cdn:url  value='/resources/global/css/font-icons/font-awesome/css/font-awesome.min.css' context='/egi'/>">
    <link rel="stylesheet" href="<cdn:url  value='/resources/global/css/egov/custom.css?rnd=${app_release_no}' context='/egi'/>">

    <script src="<cdn:url  value='/resources/global/js/jquery/jquery.js' context='/egi'/>"></script>
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="<cdn:url  value='/resources/global/js/ie8/html5shiv.min.js' context='/egi'/>"></script>
    <script src="<cdn:url  value='/resources/global/js/ie8/respond.min.js' context='/egi'/>"></script>
    <![endif]-->


</head>
<body class="page-body">
<div class="page-container">
    <tiles:insertAttribute name="header"/>
    <div class="main-content">
        <div class="page-error-404">
            <div class="error-text">
                <tiles:insertAttribute name="body"/>
            </div>
            <div class="row display-hide" id="close">
                <button type="button" class="btn btn-primary" data-dismiss="modal"
                        onclick="window.close();">Close
                </button>
            </div>
        </div>
    </div>
    <tiles:insertAttribute name="footer"/>
</div>
<script>
    if (window.opener && window.opener !== window)
        $("#close").show();
</script>
</body>
</html>
