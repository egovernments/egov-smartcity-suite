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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<div class="row">
    <div class="col-md-12">
        <form:form  mothod ="post" class="form-horizontal form-groups-bordered" id="featureForm">
            <div class="panel panel-primary" data-collapsed="0">
                <div class="panel-heading">
                    <div class="panel-title">
                        <strong><spring:message code="title.accesscontrol.grantrevoke"/></strong>
                    </div>
                </div>

                <div class="panel-body">
                        <c:if test="${not empty features}">
                        	<div class="row">
	                            <div class="col-sm-5 text-right">
	                                <spring:message code="lbl.roleName"/> :
	                            </div>
	                            <div class="col-sm-7 view-content">
	                                    ${role.name}
	                            </div>
	                            <input type="hidden" id="role" value="${role.id}">
                            </div><br>
                            <div class="col-md-3 col-md-offset-9 col-xs-3 col-xs-offset-9">
								<div class="input-group">
								  <input type="text" class="form-control input-sm search-table" id="search-table">
								  <span class="input-group-addon"><i class="fa fa-search" aria-hidden="true"></i></span>
								</div>
							</div>
                            <div class="col-sm-12 add-margin">
                                <table class="table table-bordered paginated">
                                    <thead>
                                        <tr>
                                            <th><spring:message code="lbl.feature.name"/></th>
                                            <th><spring:message code="lbl.description"/></th>
                                            <th><spring:message code="lbl.accessible"/></th>
                                        </tr>
                                    </thead>
                                    <c:forEach items="${features}" var="feature">
                                    <tr>
                                        <td>${feature.name}</td>
                                        <td>${feature.description}</td>
                                        <c:set var="checked" value=""/>
                                        <c:if test="${feature.hasRole(role)}">
                                            <c:set var="checked" value="checked"/>
                                        </c:if>
                                        <td>
                                            <input class="feature-map" data-feature="${feature.id}" type="checkbox" ${checked}> &nbsp;
                                            <span id="${feature.id}"></span>
                                        </td>
                                    </tr>
                                    </c:forEach>
                                </table>
                            </div>
                        </c:if>
                        <c:if test="${not empty roles}">
                        	<div class="row">
	                            <div class="col-sm-5 text-right">
	                                <spring:message code="lbl.feature.name"/> :
	                            </div>
	                            <div class="col-sm-7 view-content">
	                                ${feature.name}
	                            </div>
	                            <input type="hidden" id="feature" value="${feature.id}">
                            </div><br>
							<div class="col-md-3 col-md-offset-9 col-xs-3 col-xs-offset-9">
								<div class="input-group">
								  <input type="text" class="form-control input-sm search-table" id="search-table">
								  <span class="input-group-addon"><i class="fa fa-search" aria-hidden="true"></i></span>
								</div>
							</div>
                            <div class="col-sm-12 add-margin">
                                <table class="table table-bordered paginated">
                                    <thead>
                                    <tr>
                                        <th style="width:30%;"><spring:message code="lbl.roleName"/></th>
                                        <th style="width:55%;"><spring:message code="lbl.description"/></th>
                                        <th style="width:15%;"><spring:message code="lbl.accessible"/></th>
                                    </tr>
                                    </thead>
                                    <c:forEach items="${roles}" var="role">
                                        <tr>
                                            <td>${role.name}</td>
                                            <td>${role.description}</td>
                                            <c:set var="checked" value=""/>
                                            <c:if test="${feature.hasRole(role)}">
                                                <c:set var="checked" value="checked"/>
                                            </c:if>
                                            <td>
                                                <input class="role-map" data-role="${role.id}" type="checkbox" ${checked}> &nbsp;
                                                <span id="${role.id}"></span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>
                        </c:if>
                </div>
            </div>
            <div class="row">
                <div class="text-center">
                    <button type="button" class="btn btn-default" data-dismiss="modal" onclick="window.close();"><spring:message code="lbl.close"/></button>
                </div>
            </div>
        </form:form>
    </div>
</div>
<script src="<cdn:url cdn='${applicationScope.cdn}' value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js'/>"></script>
<script src="<cdn:url cdn='${applicationScope.cdn}' value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js'/>"></script>
<script>
	$(document).ready(function(){
		
		$(".feature-map").change(function() {
	        var feature = $(this).data("feature");
	        if ($(this).is(":checked")) {
	            $.ajax({
	                type: "POST",
	                url: "grant/"+feature+"/"+$("#role").val(),
	                cache: false,
	                beforeSend: function() {
	                	$("#"+feature).html('').append('Processing...').show();
	                }
	            }).done(function(data) {
	            	$("#"+feature).html('').append('<i class="fa fa-check-circle access-size success-msg" aria-hidden="true"></i>').show();
	            });
	        } else {
	            $.ajax({
	                type: "POST",
	                url: "revoke/"+feature+"/"+$("#role").val(),
	                cache: false,
	                beforeSend: function() {
	                	$("#"+feature).html('').append('Processing...').show();
	                }
	            }).done(function(data) {
	            	$("#"+feature).html('').append('<i class="fa fa-times-circle access-size error-msg" aria-hidden="true"></i>').show();
	            });
	        }
	    });
	    
	    $(".role-map").change(function() {
	        var role = $(this).data("role");
	        if ($(this).is(":checked")) {
	            $.ajax({
	                type: "POST",
	                url: "grant/"+$("#feature").val()+"/"+role,
	                cache: false,
	                beforeSend: function() {
	                	$("#"+role).html('').append('Processing...').show();
	                }
	            }).done(function(data) {
	            	$("#"+role).html('').append('<i class="fa fa-check-circle access-size success-msg" aria-hidden="true"></i>').show();
	            });
	        } else {
	            $.ajax({
	                type: "POST",
	                url: "revoke/"+$("#feature").val()+"/"+role,
	                cache: false,
	                beforeSend: function() {
	                	$("#"+role).html('').append('Processing...').show();
	                }
	            }).done(function(data) {
	            	$("#"+role).html('').append('<i class="fa fa-times-circle access-size error-msg" aria-hidden="true"></i>').show();
	            });
	        }
	    });

	    tableContainer = $('.paginated')
	    tableContainer.dataTable({
	    		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
	    });

	    $('.search-table').keyup(function(){
			tableContainer.fnFilter(this.value);
		});
	    
	});
    
</script>
<style>
.access-size{
	font-size:18px;
}
</style>