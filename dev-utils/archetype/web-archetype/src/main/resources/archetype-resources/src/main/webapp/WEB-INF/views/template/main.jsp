#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>
	<head>
	    <meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	    <meta name="description" content="eGov System" />
	    <meta name="author" content="eGovernments Foundation" />
	
	    <title><tiles:insertAttribute name="title"/></title>
		<link rel="icon" href="<c:url value='/resources/global/images/favicon.png" sizes="32x32' context='/egi'/>">
	    <link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/bootstrap.css' context='/egi'/>">
		<link rel="stylesheet" href="<c:url value='/resources/global/css/egov/custom.css?rnd=${symbol_dollar}{app_release_no}' context='/egi'/>">
		<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/font-awesome/css/font-awesome.min.css' context='/egi'/>">
		<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/typeahead.css' context='/egi'/>">
		<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap-tour/bootstrap-tour.css' context='/egi'/>">
	
		<script src="<c:url value='/resources/global/js/jquery/jquery.js' context='/egi'/>"></script>
		<script src="<c:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
		<script src="<c:url value='/resources/global/js/bootstrap/bootbox.min.js' context='/egi'/>"></script>
		<script src="<c:url value='/resources/global/js/bootstrap-tour/bootstrap-tour.js' context='/egi'/>"></script>
		<script src="<c:url value='/resources/global/js/egov/patternvalidation.js?rnd=${symbol_dollar}{app_release_no}' context='/egi'/>"></script>
		<script src="<c:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
		<script src="<c:url value='/resources/global/js/egov/custom.js?rnd=${symbol_dollar}{app_release_no}' context='/egi'/>"></script>	
	    
		
		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
			<script src="/egi/resources/global/js/ie8/html5shiv.min.js"></script>
			<script src="/egi/resources/global/js/ie8/respond.min.js"></script>
		<![endif]-->
	
	</head>
    <body class="page-body" oncontextmenu="return false;">
        <div class="page-container">
            <tiles:insertAttribute name="header"/>
                <div class="main-content">
                    <tiles:insertAttribute name="body"/>
                </div>
				<tiles:insertAttribute name="footer"/>
        </div>
        <input type="hidden" value="${symbol_dollar}{sessionScope.citylat}" id="getcitylat">
        <input type="hidden" value="${symbol_dollar}{sessionScope.citylng}" id="getcitylng">
        <div class="modal fade loader-class" data-backdrop="static">
			<div class="modal-dialog">
					<div class="modal-body">
						<div class="row spinner-margin text-center">
							<div class="col-md-12 ">
								<div class="spinner">
									<div class="rect1"></div>
									<div class="rect2"></div>
									<div class="rect3"></div>
									<div class="rect4"></div>
									<div class="rect5"></div>
								</div>
							</div>
							
							<div class="col-md-12 spinner-text">
								Processing your request. Please wait..
							</div>
						</div>
					</div>
			</div>
		</div>
		<div class="modal fade demo-class" data-backdrop="static" style="opacity:0.01">
			<div class="modal-dialog">
					<div class="modal-body">
						
					</div>
			</div>
		</div>
    </body>
</html>
