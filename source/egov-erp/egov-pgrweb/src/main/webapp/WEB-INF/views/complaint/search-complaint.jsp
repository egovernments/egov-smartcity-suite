<%@ page contentType="text/html" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

<div class="row">
    <div class="col-md-12">

        <div class="panel panel-primary" data-collapsed="0">

            <div class="panel-heading">
                <div class="panel-title">
                    <strong><spring:message code='title.searchComplaints'/> </strong>
                </div>

            </div>

            <div class="panel-body">

                <form role="form" class="form-horizontal form-groups-bordered" id="searchComplaintForm">

                    <div class="form-group">

                        <div class="col-md-6 add-margin">
                            <input type="text" name="searchText" class="form-control" id="ct-search"
                                   placeholder="<spring:message code='lbl.complaint.search.searchText' /> "/>
                        </div>

                        <div class="col-md-6 add-margin">
                            <input type="text" name="" class="form-control" id="ct-location" placeholder="<spring:message code='lbl.location'/> "/>
                        </div>

                    </div>

                    <div class="form-group">

                        <div class="col-md-3 add-margin">
                            <label><spring:message code='lbl.when'/> </label>
                        </div>
                        <div class="col-md-3 add-margin">
                            <select name="dateselect" id="when_date" class="form-control" data-first-option="false">
                                <option value=""><spring:message code='lbl.select'/> </option>
                                <option value="lastyear"><spring:message code='lbl.complaint.search.lfyear'/></option>
                                <option value="thisyear"><spring:message code='lbl.complaint.search.cfyear'/></option>
                                <option value="lastquarter"><spring:message code='lbl.complaint.search.lqtr'/></option>
                                <option value="thisquarter"><spring:message code='lbl.complaint.search.cqtr'/></option>
                                <option value="lastmonth"><spring:message code='lbl.complaint.search.lmnth'/></option>
                                <option value="thismonth"><spring:message code='lbl.complaint.search.cmnth'/></option>
                                <option value="lastweek"><spring:message code='lbl.complaint.search.lweek'/></option>
                                <option value="thisweek"><spring:message code='lbl.complaint.search.cweek'/></option>
                                <option value="today"><spring:message code='lbl.today'/></option>
                            </select>
                        </div>
                        <div class="col-md-3 add-margin">
                            <input type="text" class="form-control datepicker checkdate" id="start_date"
                                   data-inputmask="'mask': 'd/m/y'" placeholder="<spring:message code='lbl.fromDate'/>"/>
                        </div>
                        <div class="col-md-3 add-margin">
                            <input type="text" class="form-control datepicker checkdate" id="end_date"
                                   data-inputmask="'mask': 'd/m/y'" placeholder="<spring:message code='lbl.toDate'/>"/>
                        </div>

                    </div>

                    <div class="form-group">
                        <div class="col-md-3 col-xs-12 add-margin">
                            <a href="javascript:void(0);" id="toggle-searchcomp" class="btn btn-secondary"><spring:message code='lbl.more'/>..</a>
                        </div>
                    </div>

                    <div class="form-group show-searchcomp-more display-hide">

                        <div class="col-md-4 add-margin">
                            <input type="text" name="complaintNumber" class="form-control" id="ct-ctno"
                                   placeholder="<spring:message code='lbl.complaint.number'/>"/>
                        </div>
                        <div class="col-md-4 add-margin">
                            <input type="text" name="" class="form-control" id="ct-name" placeholder="<spring:message code='lbl.name'/>"/>
                        </div>
                        <div class="col-md-4 add-margin">
                            <input type="text" name="" class="form-control" id="ct-mobno" placeholder="<spring:message code='lbl.phoneNumber'/>"/>
                        </div>

                    </div>

                    <div class="form-group show-searchcomp-more display-hide">

                        <div class="col-md-4 add-margin">
                            <input type="text" name="" class="form-control" id="ct-email" placeholder="<spring:message code='lbl.email'/>"/>
                        </div>
                        <div class="col-md-4 add-margin">
                            <input type="text" name="" class="form-control" id="ct-type" placeholder="<spring:message code='lbl.complaintType'/>"/>
                        </div>
                        <div class="col-md-4 add-margin">
                            <input type="text" name="" class="form-control" id="" placeholder="<spring:message code='lbl.complaint.registrationNumber'/>"/>
                        </div>
                    </div>

                    <div class="form-group show-searchcomp-more display-hide">

                        <div class="col-md-4 add-margin">
                            <select id="ct-sel-cgroup" class="form-control" data-first-option="false">
                                <option>Complaint Group</option>
                                <option value="1">GROUP1</option>
                                <option value="2">GROUP2</option>
                            </select>
                        </div>
                        <div class="col-md-4 add-margin">
                            <select id="ct-sel-cthrough" class="form-control" data-first-option="false">
                                <option><spring:message code='lbl.complaint.through'/></option>
                                <option value="1">All</option>
                                <option value="2">Paper Form</option>
                                <option value="3">Internet</option>
                                <option value="4">Phone</option>
                                <option value="5">SMS</option>
                            </select>
                        </div>

                        <div class="col-md-4 add-margin">
                            <select name="test" id="ct-sel-status" class="form-control" data-first-option="false">
                                <option><spring:message code='lbl.status'/></option>
                                <option value="1">FORWARDED</option>
                                <option value="2">CLOSED</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group show-searchcomp-more display-hide">

                        <div class="col-md-4 add-margin">
                            <select name="test" id="ct-sel-recenter" class="form-control" data-first-option="false">
                                <option><spring:message code='lbl.receivingcenter'/></option>
                                <option value="1">Complaint Cell</option>
                                <option value="2">Mayor</option>
                                <option value="3">Zone office</option>
                            </select>
                        </div>


                        <div class="col-md-4 add-margin">
                            <select name="test" id="ct-sel-dept" class="form-control" data-first-option="false">
                                <option><spring:message code='lbl.department'/></option>
                                <option value="1">NP-PERUNGUDI</option>
                                <option value="2">NP-ADAYAR</option>
                            </select>
                        </div>

                    </div>


                    <div class="form-group">
                        <div class="text-center">
                            <a href="javascript:void(0);" id="searchComplaints" class="btn btn-primary"><spring:message code='lbl.search'/></a>
                            <a href="javascript:void(0);" class="btn btn-default"
                               onclick="jQuery('#criteria-modal').modal('show', {backdrop: 'static'});"><spring:message code='lbl.search.save'/></a>
                            <a href="javascript:void(0);" class="btn btn-default"
                               onclick="jQuery('#saved-criteria-modal').modal('show', {backdrop: 'static'});"><spring:message code='lbl.search.manage'/></a>
                        </div>
                    </div>


                </form>


            </div>


        </div>

    </div>
</div>

<div>
    <strong class="head-font">The search result is</strong>
    <table class="table table-bordered datatable dt-responsive" id="complaintSearchResults">

    </table>

</div>

<link rel="stylesheet"
      href="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<script src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.columnFilter.js' context='/egi'/>"></script>

<script src="<c:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>

<script src="<c:url value='/resources/js/app/search-complaint.js'/>"></script>