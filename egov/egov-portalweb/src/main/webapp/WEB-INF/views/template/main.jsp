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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <spring:eval expression="@environment.getProperty('analytics.enabled')" scope="application" var="analyticsEnabled"/>
    <c:if test="${analyticsEnabled}">
        <spring:eval expression="@environment.getProperty('analytics.config')" scope="application"/>
    </c:if>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="description" content="eGov ERP System"/>
    <meta name="author" content="eGovernments Foundation"/>

    <title><tiles:insertAttribute name="title"/></title>

    <link rel="stylesheet" href="<cdn:url value='/resources/global/css/bootstrap/bootstrap.css' context='/egi'/>">
    <link rel="stylesheet" href="<cdn:url value='/resources/global/css/font-icons/font-awesome/css/font-awesome.min.css?rnd=${app_release_no}' context='/egi'/>">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link href="<cdn:url value='/resources/css/citizen.css?rnd=${app_release_no}'/>" rel="stylesheet">
    <link href="<cdn:url value='/resources/css/scrollbar.css'/>" rel="stylesheet">
    <link rel="icon" href="<cdn:url value='/resources/global/images/favicon.png' context='/egi'/>" sizes="32x32">

    <script src="<cdn:url value='/resources/global/js/jquery/jquery.js' context='/egi'/>"></script>
    <script src="<cdn:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
    <script src="<cdn:url value='/resources/global/js/bootstrap/bootbox.min.js' context='/egi'/>"></script>
    <script src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery.matchHeight/0.7.0/jquery.matchHeight-min.js"></script>
    <script src="<cdn:url value='/resources/js/citizen.js?rnd=${app_release_no}'/>"></script>

    <%-- <script src="<cdn:url value='/resources/global/js/egov/custom.js?rnd=${app_release_no}' context='/egi'/>"></script> --%>
    <%-- <script src="<cdn:url value='/resources/js/app/homepagecitizen.js?rnd=${app_release_no}' context='/egi'/>"></script> --%>


    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="<cdn:url value='/resources/global/js/ie8/html5shiv.min.js' context='/egi'/>"></script>
    <script src="<cdn:url value='/resources/global/js/ie8/respond.min.js' context='/egi'/>"></script>
    <![endif]-->
    <script>
        var googleapikey = '${sessionScope.googleApiKey}';
        var citylat = ${sessionScope.citylat};
        var citylng = ${sessionScope.citylng};
    </script>
</head>
<body>
<spring:htmlEscape defaultHtmlEscape="true" />
<tiles:insertAttribute name="body"/>

<div class="modal fade change-password" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">

            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Change Password</h4>
            </div>

            <div class="modal-body">
                <form id="passwordForm" class="form-horizontal form-groups-bordered">
                    <div class="form-group">
                        <div class="col-md-4">
                            <label class="control-label">Old Password</label>
                        </div>
                        <div class="col-md-8 add-margin">
                            <input type="password" class="form-control" id="old-pass">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-4">
                            <label class="control-label">New Password</label>
                        </div>
                        <div class="col-md-8 add-margin">
                            <input type="password" class="form-control checkpassword" id="new-pass">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-4">
                            <label class="control-label">Re-type Password</label>
                        </div>
                        <div class="col-md-8 add-margin">
                            <input type="password" class="form-control checkpassword" id="retype-pass">
                            <div class="password-error error-msg display-hide">Password is incorrect</div>
                        </div>
                    </div>
                    <div class="form-group text-right">
                        <div class="col-md-12 add-margin">
                            <button type="button" class="btn btn-primary" id="btnChangePwd">Change Password</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>