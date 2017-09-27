<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~      accountability and the service delivery of the government  organizations.
  ~
  ~       Copyright (C) 2016  eGovernments Foundation
  ~
  ~       The updated version of eGov suite of products as by eGovernments Foundation
  ~       is available at http://www.egovernments.org
  ~
  ~       This program is free software: you can redistribute it and/or modify
  ~       it under the terms of the GNU General Public License as published by
  ~       the Free Software Foundation, either version 3 of the License, or
  ~       any later version.
  ~
  ~       This program is distributed in the hope that it will be useful,
  ~       but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~       GNU General Public License for more details.
  ~
  ~       You should have received a copy of the GNU General Public License
  ~       along with this program. If not, see http://www.gnu.org/licenses/ or
  ~       http://www.gnu.org/licenses/gpl.html .
  ~
  ~       In addition to the terms of the GPL license to be adhered to in using this
  ~       program, the following additional terms are to be complied with:
  ~
  ~           1) All versions of this program, verbatim or modified must carry this
  ~              Legal Notice.
  ~
  ~           2) Any misrepresentation of the origin of the material is prohibited. It
  ~              is required that all modified versions of this material be marked in
  ~              reasonable ways as different from the original version.
  ~
  ~           3) This license does not grant any rights to any user of the program
  ~              with regards to rights under trademark law for use of the trade names
  ~              or trademarks of eGovernments Foundation.
  ~
  ~     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6 lt8"> <![endif]-->
<!--[if IE 7 ]> <html lang="en" class="no-js ie7 lt8"> <![endif]-->
<!--[if IE 8 ]> <html lang="en" class="no-js ie8 lt8"> <![endif]-->
<!--[if IE 9 ]> <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>${sessionScope.citymunicipalityname} Smart City Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="icon" href="<cdn:url value='/resources/global/images/favicon.png' context='/egi'/>" sizes="32x32">
    <link rel="stylesheet" href="<cdn:url value='/resources/global/css/bootstrap/bootstrap.css' context='/egi'/>">
    <link rel="stylesheet" href="<cdn:url value='/resources/css/global.css?rnd=${app_release_no}'/>">
    <link rel="stylesheet" href="<cdn:url value='/resources/css/dashboard.css?rnd=${app_release_no}'/>">
    <link rel="stylesheet" href="<cdn:url value='/resources/css/grayscale.css?rnd=${app_release_no}'/>">
    <link rel="stylesheet" href="<cdn:url value='/resources/css/home.css?rnd=${app_release_no}'/>">

    <script src="<cdn:url value='/resources/global/js/jquery/jquery.js' context='/egi'/>"></script>
    <script src="<cdn:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
    <script src="<cdn:url value='/resources/global/js/bootstrap/bootbox.min.js' context='/egi'/>"></script>
    <script type="text/javascript" src="<cdn:url  value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
    <script src="<cdn:url  value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>
    <script src="<cdn:url value='/resources/global/js/egov/custom.js?rnd=${app_release_no}' context='/egi'/>"></script>

    <script>
        $(window).load(function () {
            $("#cover").delay(1000).slideUp(300);
            $("#preloader-container").delay(1000).slideUp(300);
        });
        var googleapikey = '${sessionScope.googleApiKey}';
        var citylat = ${sessionScope.citylat};
        var citylng = ${sessionScope.citylng};
    </script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="<cdn:url value='/resources/global/js/ie8/html5shiv.min.js' context='/egi'/>"></script>
    <script src="<cdn:url value='/resources/global/js/ie8/respond.min.js' context='/egi'/>"></script>
    <![endif]-->
</head>
<body id="page-top" data-spy="scroll"/>
<div id="loadingMask"></div>
<div id="loading">
    <div class="loading-indicator"><i class="fa fa-spinner fa-pulse fa-2x"></i></div>
</div>
<div class="page-container">
    <tiles:insertAttribute name="header"/>
    <div class="main-content">
        <tiles:insertAttribute name="body"/>
    </div>
    <tiles:insertAttribute name="footer"/>
</div>
</body>
<script src="<cdn:url value='/resources/js/highchart/highstock.js'/>"></script>
<script src="<cdn:url value='/resources/js/date.js'/>" type="text/javascript"></script>
<script src="<cdn:url value='/resources/js/home.js'/>" type="text/javascript"></script>
<script type="text/javascript">
    $(window).load(function () {
        document.getElementById("loading").style.display = "none";
        document.getElementById("loadingMask").style.display = "none";
    });
</script>
</html>
