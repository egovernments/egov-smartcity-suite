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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<!DOCTYPE html>
<html class="no-js">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="description" content="eGov Urban Portal"/>
    <meta name="author" content=""/>

    <title>Search Position</title>
    <link rel="stylesheet" href="<cdn:url value='/resources/global/css/bootstrap/bootstrap.css' context='/egi'/>">
    <link rel="stylesheet" href="<cdn:url value='/resources/global/css/font-icons/font-awesome/css/font-awesome.min.css' context='/egi'/>">
    <link rel="stylesheet"
          href="<cdn:url value='/resources/global/css/egov/custom.css?rnd=${app_release_no}' context='/egi'/>">

    <script src="<cdn:url value='/resources/global/js/jquery/jquery.js' context='/egi'/>"></script>
    <script src="<cdn:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>


    <script src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>

    <link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
    <link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
    <script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
    <script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
    <script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"></script>

    <script src="<cdn:url value='/resources/global/js/bootstrap/bootbox.min.js' context='/egi'/>"></script>
    <script src="<cdn:url value='/resources/global/js/egov/custom.js?rnd=${app_release_no}' context='/egi'/>"></script>
    <script src="<cdn:url value='/resources/js/app/positionsearch.js?rnd=${app_release_no}' context='/eis'/>"></script>
</head>
<body class="page-body">

<div class="page-container">
    <header class="navbar navbar-fixed-top"><!-- set fixed position by adding class "navbar-fixed-top" -->

        <nav class="navbar navbar-default navbar-custom navbar-fixed-top">
            <div class="container-fluid">
                <div class="navbar-header col-md-10 col-xs-10">
                    <a class="navbar-brand" href="javascript:void(0);">
                        <img src="<c:url value='/downloadfile/logo' context='/egi'/>" height="60">
                        <div>

                            <span class="title2">Search Position</span>
                        </div>
                    </a>
                </div>

                <div class="nav-right-menu col-md-2 col-xs-2">
                    <ul class="hr-menu text-right">
                        <li class="ico-menu">
                            <a href="javascript:void(0);">
                                <img src="<cdn:url value='/resources/global/images/logo@2x.png' context='/egi'/>" title="Powered by eGovernments" height="20px">
                            </a>
                        </li>

                    </ul>
                </div>

            </div>
        </nav>

    </header>
    <div class="main-content">
        <div class="row">
            <div class="col-md-12">
                <c:if test="${not empty warning}">
                    <div class="panel-heading">
                        <div class="panel-title view-content">
                            <div>${warning}</div>
                        </div>
                    </div>
                </c:if>
                <form:form id="" method="post" class="form-horizontal form-groups-bordered" modelAttribute="deptDesig">
                    <div class="panel panel-primary" data-collapsed="0">
                        <div class="panel-heading">
                            <div class="panel-title">
                                <strong><spring:message code="lbl.searchposition"/></strong>
                            </div>
                        </div>
                        <div class="panel-body custom-form">
                            <div class="form-group">
                                <label class="col-sm-3 control-label"> <spring:message
                                        code="lbl.department"/>
                                </label>

                                <div class="col-sm-6 add-margin">
                                    <form:select path="department" id="position_dept"
                                                 required="required" cssClass="form-control"
                                                 cssErrorClass="form-control error">
                                        <form:option value="">
                                            <spring:message code="lbl.select"/>
                                        </form:option>
                                        <form:options items="${departments}" itemValue="id"
                                                      itemLabel="name"/>
                                    </form:select>
                                    <form:errors path="department" cssClass="error-msg"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label"> <spring:message
                                        code="lbl.designation"/>
                                </label>

                                <div class="col-sm-6 add-margin">
                                    <form:select path="designation" id="position_desig"
                                                 required="required" cssClass="form-control"
                                                 cssErrorClass="form-control error">
                                        <form:option value="">
                                            <spring:message code="lbl.select"/>
                                        </form:option>
                                        <form:options items="${designations}" itemValue="id"
                                                      itemLabel="name"/>
                                    </form:select>
                                    <form:errors path="designation" cssClass="error-msg"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="text-center">
                            <button type="button" id="positionSearch" class="btn btn-primary">
                                <spring:message code="button.submit.position.search"/>
                            </button>
                            <a href="javascript:void(0)" class="btn btn-default"
                               onclick="self.close()"><spring:message code="lbl.close"/></a>
                        </div>
                    </div>
                    <c:if test="${mode != 'error' && mode != 'new'}"> </c:if>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="panel panel-primary" data-collapsed="0">
                                <div class="panel-heading">
                                </div>
                                <div class="panel-body">
                                    <div class="row">
                                        <div class="col-md-3 col-xs-6 add-margin">
                                            <spring:message code="lbl.outsourcedposts"/>
                                        </div>
                                        <div class="col-md-3 col-xs-6 add-margin view-content">
                                            <input type="text" class="form-control" maxlength="4" size="4" id='outSourcedPost' disabled>
                                        </div>
                                        <div class="col-md-3 col-xs-6 add-margin">
                                            <spring:message code="lbl.sanctionedposts"/>
                                        </div>
                                        <div class="col-md-3 col-xs-6 add-margin view-content">
                                            <input type="text" class="form-control" maxlength="4" size="4" id='sanctionedPost' disabled>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 col-xs-6 table-header">List of Positions</div>
                        <!-- <div class="col-md-6 col-xs-6 add-margin text-right">
                            <span class="inline-elem">Search</span> <span class="inline-elem"><input
                                type="text" id="searchposition" class="form-control input-sm"></span>
                        </div> -->
                        <div class="col-md-12">
                            <table class="table table-bordered datatable" id="position-table">

                            </table>
                        </div>
                    </div>

                </form:form>
            </div>
        </div>
    </div>
    <footer class="main">

        Powered by <a href="http://eGovernments.org" target="_blank">eGovernments Foundation</a>

    </footer>
</div>

<div class="modal fade position-modal" id="position-modal" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">

            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Edit Position</h4>
            </div>

            <div class="modal-body">
                <form id="position-form" class="form-horizontal form-groups-bordered">
                    <div class="row">
                        <div class="col-md-4">
                            <label class="control-label">Department</label>
                        </div>
                        <div class="col-md-8">

                            <div class="form-group">
                                <input type="text" class="form-control" id='departmentname-edit' readonly="readonly">

                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-4">
                            <label class="control-label">Designation</label>
                        </div>
                        <div class="col-md-8">

                            <div class="form-group">
                                <input type="text" class="form-control" placeholder="" id='designationname-edit' readonly="readonly">
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-4">
                            <label class="control-label">Position</label>
                        </div>
                        <div class="col-md-8">

                            <div class="form-group">
                                <input type="text" class="form-control is_valid_letters_space_hyphen_underscore" placeholder="" id='positionname-edit' required="required">

                                <input type="hidden" class="form-control" placeholder="" id='position-id'>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-4">
                            <label class="control-label">Outsourced Posts</label>
                        </div>
                        <div class="col-md-8">

                            <div class="col-sm-6 col-xs-6 add-margin">
                                <input type="radio" id="outSourced-Yes" name="posts" value="Yes">
                                <label>Yes</label>
                            </div>

                            <div class="col-sm-6 col-xs-6 add-margin">
                                <input type="radio" id="outSourced-No" name="posts" value="No">
                                <label>No</label>
                            </div>

                        </div>
                    </div>

            </div>

            <div class="modal-footer">
                <button type="submit" class="btn btn-primary add-fav">Save Position</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
            </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>																						
	
		
					