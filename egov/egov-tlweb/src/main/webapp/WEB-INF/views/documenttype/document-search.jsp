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
  
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<form:form role="form" id="searchdocumentform" action="search" modelAttribute="documenttype" commandName="documenttype"  cssClass="form-horizontal form-groups-bordered">
   
   <div class="row">
      <div class="col-md-12">
        <div class="panel panel-primary" data-collapsed="0">
          <div class="panel-heading">
            <div class="panel-title"><spring:message code="title.document.search"/></div>
          </div>
          <div class="panel-body custom-form">
            <div class="form-group">
              <label class="col-sm-3 control-label text-right"><spring:message code="lbl.name" /> </label>
              <div class="col-sm-3 add-margin">
               <form:input path="name" id="name" class="form-control text-left patternvalidation" maxLength="100"/>
              
              </div>
              <label class="col-sm-2 control-label text-right"><spring:message code="lbl.licenseAppType" /> </label>
              <div class="col-sm-3 add-margin">
                <form:select path="applicationType" id="applicationType_dropdown" cssClass="form-control"
                cssErrorClass="form-control error">
                <form:option value=""><spring:message code="lbl.select" /></form:option> 
                <form:options items="${applicationType}" />
                </form:select>
              </div>
            </div>
           </div>
        </div>
        <div class="form-group">
          <div class="text-center">
            <button type='button' class='btn btn-primary' id="searchbtn">
              <spring:message code='lbl.search' />
            </button>
            <a href='javascript:void(0)' class='btn btn-default' onclick='self.close()'><spring:message
                code='lbl.close' /></a>
          </div>
        </div>
      </div>
    </div>
</form:form>
<div class="row display-hide report-section">
  <div class="col-md-12 table-header text-left"><spring:message code="title.document.view"/></div>
  <div class="col-md-12 form-group report-table-container">
    <table class="table table-bordered table-hover multiheadertbl" id="document-Table">
      <thead>
        <tr>
          <th><spring:message code="lbl.name" /></th>
          <th><spring:message code="lbl.licenseAppType" /></th>
          <th><spring:message code="lbl.mandatory" /></th>
          <th><spring:message code="lbl.action" /></th>
        </tr>
      </thead>
    </table>
  </div>
</div>


<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript"
  src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript"
  src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript"
  src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript"
  src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"
  type="text/javascript"></script>
<script type="text/javascript" src="<cdn:url value='/resources/js/app/document-type-search.js?rnd=${app_release_no}'/>"></script>
