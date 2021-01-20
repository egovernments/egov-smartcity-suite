<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2018  eGovernments Foundation
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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="row">
    <div class="col-md-12">
        <form:form role="form" class="form-horizontal form-groups-bordered" modelAttribute="tradeLicense"
                   id="licenseview">
            <c:choose>
                <c:when test="${not empty message}">
                    <div class="text-center">
                        <div class="alert alert-danger align-center" role="alert"><spring:message
                                code="${message}"/></div>
                        <button type="button" id="close" class="btn btn-default" onclick="window.close();">
                            <spring:message code="lbl.close"/>
                        </button>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="panel panel-primary" data-collapsed="0">
                        <div class="panel-heading">
                            <div class="panel-title" style="text-align: center">
                                <spring:message code="page.title.viewtrade"/>
                            </div>
                        </div>
                        <ul class="nav nav-tabs" id="settingstab">
                            <li class="active"><a data-toggle="tab" href="#tradedetails" data-tabidx="0"
                                                  aria-expanded="true"><spring:message code="license.tradedetail"/></a>
                            </li>
                            <li class=""><a data-toggle="tab" href="#tradeattachments" id="getdocuments" data-tabidx="1"
                                            aria-expanded="false"><spring:message code="license.support.docs"/></a></li>
                        </ul>
                        <input type="hidden" id="licenseId" value="${tradeLicense.id}"/>
                        <div class="panel-body custom-form">
                            <div class="tab-content">
                                <div class="tab-pane fade active in" id="tradedetails">
                                    <div class="panel-body">
                                        <div class="row">
                                            </br>
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.applicationnumber'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.applicationNumber}
                                            </div>
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='dateofapplication.lbl'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                <fmt:formatDate value="${tradeLicense.applicationDate}"
                                                                pattern="dd/MM/yyyy" var="applicationDate"/>
                                                    ${applicationDate}
                                            </div>
                                        </div>
                                    </div>
                                    <div class="panel-heading  custom_form_panel_heading subheadnew">
                                        <div class="panel-title">
                                            <spring:message code='license.title.applicantdetails'/>
                                        </div>
                                    </div>
                                    <div class="panel-body">
                                        <div class="row add-border">
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='licensee.applicantname'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.licensee.applicantName}
                                            </div>
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='licensee.fatherorspousename'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.licensee.fatherOrSpouseName}
                                            </div>
                                        </div>
                                        <div class="row add-border">
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='search.licensee.mobileNo'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.licensee.mobilePhoneNumber}
                                            </div>
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='lbl.emailid'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.licensee.emailId}
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='licensee.address'/>
                                            </div>
                                            <div class="col-xs-6 add-margin view-content">
                                                    ${tradeLicense.licensee.address}
                                            </div>
                                        </div>
                                    </div>
                                    <div class="panel-heading  custom_form_panel_heading subheadnew">
                                        <div class="panel-title">
                                            <spring:message code='license.location.lbl'/>
                                        </div>
                                    </div>
                                    <div class="panel-body">
                                        <div class="row add-border">
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.propertyNo.lbl'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.assessmentNo}
                                            </div>
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='lbl.locality'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.boundary.name}
                                            </div>
                                        </div>
                                        <div class="row add-border">
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='baseregister.ward'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.parentBoundary.name}
                                            </div>
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='lbl.admin.ward'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.adminWard.name}
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.ownerShipType.lbl'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.ownershipType.displayName}
                                            </div>
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.address'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.address}
                                            </div>
                                        </div>
                                    </div>
                                    <div class="panel-heading  custom_form_panel_heading subheadnew">
                                        <div class="panel-title">
                                            <spring:message code='license.tradedetail'/>
                                        </div>
                                    </div>
                                    <div class="panel-body">

                                        <div class="row add-border">
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.licensenumber'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.licenseNumber}
                                            </div>
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.oldlicensenum'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.oldLicenseNumber}
                                            </div>
                                        </div>
                                        <div class="row add-border">
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='search.license.establishmentname'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.nameOfEstablishment}
                                            </div>
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.tradeType.lbl'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.natureOfBusiness.name}
                                            </div>
                                        </div>
                                        <div class="row add-border">
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.category.lbl'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.category.name}
                                            </div>
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.subCategory.lbl'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.tradeName.name}
                                            </div>
                                        </div>
                                        <div class="row add-border">
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.uom.lbl'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.tradeName.licenseSubCategoryDetails[0].uom.name}
                                            </div>
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.premises.lbl'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.tradeArea_weight}
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.remarks'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.remarks}
                                            </div>
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.startdate'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                <fmt:formatDate value="${tradeLicense.commencementDate}"
                                                                pattern="dd/MM/yyyy" var="commencementDateFrmttd"/>
                                                    ${commencementDateFrmttd}
                                            </div>
                                        </div>
                                    </div>
                                    <div class="panel-heading  custom_form_panel_heading subheadnew">
                                        <div class="panel-title">
                                            <spring:message code='license.labourdetail'/>
                                        </div>
                                    </div>
                                    <div class="panel-body">
                                        <div class="row add-border">
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.labourclassification'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.classificationType.name}
                                            </div>
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.labouremployers'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.employersType.name}
                                            </div>
                                        </div>
                                        <div class="row add-border">
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.labourmandal'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.mandalName}
                                            </div>
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.labourdoorno'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.doorNo}
                                            </div>
                                        </div>
                                        <div class="row add-border">
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.directworkers.male'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.directWorkerMale}
                                            </div>
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.directworkers.female'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.directWorkerFemale}
                                            </div>
                                        </div>
                                        <div class="row add-border">
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.contractworkers.male'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.contractWorkerMale}
                                            </div>
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.contractworkers.female'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.contractWorkerFemale}
                                            </div>
                                        </div>
                                        <div class="row add-border">
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.dailywages.male'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.dailyWagesMale}
                                            </div>
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.dailywages.female'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.dailyWagesFemale}
                                            </div>
                                        </div>
                                        <div class="row add-border">
                                            <div class="col-xs-3 add-margin">
                                                <spring:message code='license.totalworkers'/>
                                            </div>
                                            <div class="col-xs-3 add-margin view-content">
                                                    ${tradeLicense.totalWorkers}
                                            </div>
                                        </div>
                                        
                                    </div>
                                    <c:if test="${tradeLicense.agreementDate!=null}">
                                        <div class="panel-heading  custom_form_panel_heading subheadnew">
                                            <div class="panel-title">
                                                <spring:message code='license.AgreementDetails.lbl'/>
                                            </div>
                                        </div>
                                        <div class="panel-body">
                                            <div class="row add-border">
                                                <div class="col-xs-3 add-margin">
                                                    <spring:message code='license.agreementDate.lbl'/>
                                                </div>
                                                <div class="col-xs-3 add-margin view-content">
                                                    <fmt:formatDate value="${tradeLicense.agreementDate}"
                                                                    pattern="dd/MM/yyyy" var="agreementDateFrmttd"/>
                                                        ${agreementDateFrmttd}
                                                </div>
                                                <div class="col-xs-3 add-margin">
                                                    <spring:message code='license.agreementDocNo.lbl'/>
                                                </div>
                                                <div class="col-xs-3 add-margin view-content">
                                                        ${tradeLicense.agreementDocNo}
                                                </div>
                                            </div>
                                        </div>
                                    </c:if>
                                </div>
                                <div class="tab-pane fade" id="tradeattachments">
                                    <br/><br/>
                                    <div class="col-md-12 col-sm-12 col-xs-12">
                                        <ul class="nav nav-tabs" id="settingstab1">
                                            <li class="active"><a data-toggle="tab" href="#newDocs"
                                                                  data-tabidx="0" aria-expanded="true">
                                                <spring:message code="lbl.new"/></a></li>
                                            <li class=""><a data-toggle="tab" href="#renewDocs"
                                                            data-tabidx="1" aria-expanded="false">
                                                <spring:message code="lbl.renew"/></a></li>
                                            <li class=""><a data-toggle="tab" href="#closureDocs"
                                                            data-tabidx="1" aria-expanded="false">
                                                <spring:message code="lbl.closure"/></a></li>
                                        </ul>
                                        <div class="tab-content">
                                            <div class="tab-pane fade active in" id="newDocs"><br/>
                                                <div id="newTbl">
                                                </div>
                                            </div>
                                            <div class="tab-pane fade" id="renewDocs"><br/>
                                                <div id="renewTbl">
                                                </div>
                                            </div>
                                            <div class="tab-pane fade" id="closureDocs"><br/>
                                                <div id="closureTbl">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <c:set value="${outstandingFee}" var="feeInfo"></c:set>
                    <c:if test="${feeInfo.size()> 0}">
                        <div class="panel panel-primary" data-collapsed="0">
                            <div class="panel-heading  custom_form_panel_heading subheadnew">
                                <div class="panel-title">
                                    <spring:message code='license.title.feedetail'/>
                                </div>
                            </div>
                            <table class="table table-bordered" style="width: 97%; margin: 0 auto;">
                                <thead>
                                <tr>
                                    <th><spring:message code='lbl.feetype'/></th>
                                    <th><spring:message code='lbl.current'/></th>
                                    <th><spring:message code='license.fee.arrears'/></th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${feeInfo}" var="fee" varStatus="status">
                                    <tr>
                                        <td>${fee.key}</td>
                                        <td>${fee.value['current']}</td>
                                        <td>${fee.value['arrear']}</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                                <tfoot>
                                <tr>
                                    <td colspan="3">
                                        <div style="text-align:left">
                                            <a name="viewdcb" class="btn btn-secondary align-right" id="viewdcb"
                                               onclick="window.open('/tl/dcb/view/'+ '${tradeLicense.uid}', '_blank', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');">
                                                <spring:message code='lbl.show.dcb' htmlEscape="false"/></a>
                                        </div>
                                    </td>
                                </tr>
                                </tfoot>
                            </table>
                        </div>
                    </c:if>
                    <br/>
                    <c:if test="${not empty licenseHistory}">
                        <div class="panel panel-primary" data-collapsed="0">
                            <div class="panel-heading">
                                <div class="panel-title">
                                        ${tradeLicense.state.natureOfTask}
                                    <spring:message code='lbl.licensehistory'/>
                                </div>
                            </div>
                            <div class="panel-body">
                                <div class="row add-border">
                                    <table class="table table-bordered"
                                           style="width:97%;margin:0 auto;">
                                        <thead>
                                        <tr>
                                            <th class="bluebgheadtd"><spring:message code="lbl.wf.date"/></th>
                                            <th class="bluebgheadtd"><spring:message code="lbl.wf.updatedby"/></th>
                                            <th class="bluebgheadtd"><spring:message code="lbl.wf.currentowner"/></th>
                                            <th class="bluebgheadtd"><spring:message code="lbl.wf.status"/></th>
                                            <th class="bluebgheadtd"><spring:message code="lbl.remarks"/></th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${licenseHistory}" varStatus="stat"
                                                   var="history">
                                            <tr>
                                                <fmt:formatDate value="${history.date}" var="historyDate"
                                                                pattern="dd/MM/yyyy HH:mm a"/>
                                                <td class="blueborderfortd" style="text-align: left">
                                                        ${historyDate}</td>
                                                <td class="blueborderfortd" style="text-align: left">
                                                        ${history.updatedBy}</td>
                                                <td class="blueborderfortd" style="text-align: left">
                                                        ${history.user}</td>
                                                <td class="blueborderfortd" style="text-align: left">
                                                        ${history.status}</td>
                                                <td class="blueborderfortd" style="text-align: left">
                                                        ${history.comments}</td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <div class="row">
                        <div class="text-center">
                            <button type="button" id="btnprint" class="btn btn-default printbtn"
                                    onclick="window.print()">
                                <spring:message code="lbl.print"/>
                            </button>
                            <button type="button" id="btnclose" class="btn btn-default" onclick="window.close();">
                                <spring:message code="lbl.close"/>
                            </button>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </form:form>
    </div>
</div>
<style>
    @media print {
        .btn {
            display: none;
        }
    }

    #newTbl > table {
        margin-left: 0;
    }

    #renewTbl > table {
        margin-left: 0;
    }

    #closureTbl > table {
        margin-left: 0;
    }
</style>
<link rel="stylesheet"
      href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet"
      href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript"
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript"
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script src="<cdn:url  value='/resources/js/app/support-documents.js?rnd=${app_release_no}'/>"></script>
