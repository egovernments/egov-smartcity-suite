<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6 lt8"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7 lt8"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8 lt8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> 
<html lang="en" class="no-js"> 
<!--<![endif]-->
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>${sessionScope.citymunicipalityname} Smart City Dashboard</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
       
       	<link rel="icon" href="<c:url value='/resources/global/images/favicon.png" sizes="32x32' context='/egi'/>">
	    <link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/bootstrap.css' context='/egi'/>">
	    <link rel="stylesheet" href="resources/css/global.css">
	    <link rel="stylesheet" href="resources/css/dashboard.css">
		<!-- link rel="stylesheet" href="resources/bootstrap/v3.3.1/css/bootstrap-theme.min.css">
		<link rel="stylesheet" href="resources/fontawesome/v4.2.0/css/font-awesome.min.css"-->
		<link rel="stylesheet" href="resources/css/grayscale.css">
		<!-- link rel="stylesheet" href="resources/css/plugins/social-buttons.css" -->
		<link rel="stylesheet" href="resources/css/home.css">
		
		<script src="<c:url value='/resources/global/js/jquery/jquery.js' context='/egi'/>"></script>
		<script src="<c:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
		<script src="<c:url value='/resources/global/js/bootstrap/bootbox.min.js' context='/egi'/>"></script>
		<script>
			$(window).load(function() {
				$("#cover").delay(1000).slideUp(300);
				$("#preloader-container").delay(1000).slideUp(300);
			});
		</script>		
    	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    	<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    	<!--[if lt IE 9]>
        <script src="resources/js/html5shiv.js"></script>
        <script src="resources/js/respond.min.js"></script>
    	<![endif]-->    		
    </head>
    <body id="page-top" data-spy="scroll" />
    	<div class="cover" id="cover"></div>
    	<div class="preloader-container" id="preloader-container">
    		<div class="preloader-logo">eGov<img src="resources/images/mask.gif">Dashboard</div>
    	</div>
		<div class="page-container">
            <tiles:insertAttribute name="header"/>
                <div class="main-content">
                    <tiles:insertAttribute name="body"/>
                </div>
			<tiles:insertAttribute name="footer"/>
        </div>
	</body>
	<!--script src="resources/jquery-ui/v1.11.2/jquery-ui.min.js"></script>
	< script src="resources/bootstrap/v3.3.1/js/bootstrap.min.js"></script-->
	<script src="resources/js/highchart/highstock.js"></script>
	<script src='resources/js/date.js' type="text/javascript"></script>
	<script src='resources/js/home.js' type="text/javascript"></script>
</html>
