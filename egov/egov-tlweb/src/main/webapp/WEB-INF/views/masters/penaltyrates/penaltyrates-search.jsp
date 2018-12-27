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
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<div class="row form-horizontal form-groups-bordered">
    <div class="col-md-12">
        <div class="panel panel-primary" data-collapsed="0">
            <div class="panel-body">
                <div class="panel-heading">
                    <div class="col-md-12 panel-title text-left">
                        &nbsp;
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-5 control-label text-right">
                        <spring:message code="lbl.licenseAppType"/> <span class="mandatory"></span>
                    </label>
                    <div class="col-sm-3 add-margin">
                        <select name="licenseAppType" id="licenseAppType" class="form-control" required="required">
                            <option value="">
                                <spring:message code="lbl.select"/>
                            </option>
                            <c:forEach items="${licenseAppTypes}" var="licenseAppType">
                                <option value="${licenseAppType.id}">${licenseAppType.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-group text-center">
                    <label class="col-sm-5 control-label text-right">
                        &nbsp;
                    </label>
                    <div class="col-sm-3 add-margin">
                        <button type="button" class="btn btn-primary" id="search-btn"><spring:message code="lbl.search"/></button>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <button type="button" class="btn btn-default" data-dismiss="modal" onclick="window.close();">
                            <spring:message code="lbl.close"/>
                        </button>
                    </div>
                </div>
                <div id="rates" style="display: none">
                    <div class="col-md-12 panel-heading">
                        <div class="panel-title text-left">
                            <spring:message code="lbl.penaltyrate.details"/>
                        </div>
                    </div>
                    <div class="col-md-12">
                        <table class="table table-bordered table-hover" id="resultTable">
                            <thead>
                            <tr>
                                <th class="text-center"><spring:message code="lbl.from"/></th>
                                <th class="text-center"><spring:message code="lbl.to"/></th>
                                <th class="text-center" colspan="3"><spring:message code="lbl.penaltyrate"/></th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                    <div class="col-sm-12 add-margin text-center">
                        <button type="button" class="btn btn-primary" id="edit-btn"><spring:message code="lbl.modify"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script src="<cdn:url  value='/resources/js/app/license-penalty-rates.js?rnd=${app_release_no}'/>"></script>