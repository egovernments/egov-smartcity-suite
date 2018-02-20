<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
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

<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<style>
    .progress {
        width: 300px;
        position: relative;
    }

    .progress-bar-title {
        position: absolute;
        text-align: center;
        line-height: 20px; /* line-height should be equal to bar height */
        overflow: hidden;
        color: #000;
        right: 0;
        left: 0;
        top: 0;
    }
</style>
<div class="row">
    <div class="col-md-12">
        <div class="panel panel-primary" data-collapsed="0">
            <div class="panel-heading">
                <div class="panel-title"><spring:message code="title.demand.generation"/></div>
            </div>

            <div class="panel-body">
                <form:form role="form" id="generatedemand" name="generatedemand"
                           cssClass="form-horizontal form-groups-bordered" method="post">
                    <div class="form-group">
                        <label class="col-sm-6 control-label"><spring:message
                                code="lbl.financialyear"/> </label>
                        <div class="col-sm-4">
                            <input type="hidden" value="${installmentYear}" id="installmentYear">
                            <label class="col-sm-4 control-label text-left">${installmentYear}</label>
                        </div>
                    </div>
                    <div class="form-group add-margin">
                        <div class="col-sm-12 text-center">
                            <button type="button" class='btn btn-primary' id="genDmdBtn">
                                <spring:message code='lbl.generate.demand'/>
                            </button>&nbsp;&nbsp;&nbsp;
                            <button type="button" class="btn btn-default" data-dismiss="modal"
                                    onclick="window.close();">
                                <spring:message code='lbl.close'/></button>
                        </div>
                    </div>

                    <div class="progress-div text-center form-group display-hide">

                        <div class="alert alert-info" role="alert">
                            <spring:message code="lbl.generate.demand.wait"/>
                            <div class="progress center-block" style="width:300px">
                                <div class="progress-bar progress-bar-striped active" role="progressbar"
                                     aria-valuenow="0" aria-valuemin="0" aria-valuemax="${licenseIds.size()}">
                                    <div class="progress-bar-title"></div>
                                </div>
                            </div>
                            <spring:message code="lbl.generate.demand.info"/>
                        </div>
                    </div>
                    <div class="col-md-12form-group report-table-container display-hide">

                        <table class="table table-bordered datatable dt-responsive table-hover multiheadertbl"
                               id="tbldemandgenerate">
                        </table>
                    </div>

                </form:form>
            </div>
            <div class="panel-footer">
                <div class="alert alert-warning">
                    <spring:message code="msg.demand.gen.footer" />
                </div>
            </div>
        </div>
    </div>
</div>
<link rel="stylesheet"
      href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet"
      href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript"
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript"
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/dataTables.buttons.min.js' context='/egi'/>"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/buttons.bootstrap.min.js' context='/egi'/>"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/buttons.flash.min.js' context='/egi'/>"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/jszip.min.js' context='/egi'/>"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/pdfmake.min.js' context='/egi'/>"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/vfs_fonts.js' context='/egi'/>"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/buttons.html5.min.js' context='/egi'/>"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/buttons.print.min.js' context='/egi'/>"></script>
<script>
    var licenseIds = ${licenseIds};
    var logDetails = '${demandGenerationLogDetails}';
</script>
<script src="<cdn:url  value='/resources/js/app/demand-generation-bulk.js?rnd=${app_release_no}'/>"></script>