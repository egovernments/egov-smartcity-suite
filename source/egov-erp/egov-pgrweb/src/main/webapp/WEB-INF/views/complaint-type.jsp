<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<script src="<c:url value='/resources/pgr/js/complaint-type.js'/>"></script>

<div class="row" id="page-content">
    <div class="col-md-12">
        <div class="panel" data-collapsed="0">
            <div class="panel-body">
                <form:form action="javascript:void(0);" id="addcomplaint" method="post"
                      class="form-horizontal form-groups-bordered" modelAttribute="complaintType">

                    <div class="form-group">
                        <label class="col-sm-3 control-label">Complaint Type</label>

                        <div class="col-sm-6 add-margin">
                            <input type="text" class="form-control" id="comp_type_name" placeholder=""
                                   name="comp_type_name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">Department</label>

                        <div class="col-sm-6 add-margin">
                            <form:select path="department"
                                         id="comp_type_dept" class="form-control">
                                <form:option value=""> Select </form:option>
                                <form:options items="${departments}" itemValue="deptCode" itemLabel="deptName"/>
                            </form:select>
                        </div>

                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">Number of days to resolve</label>
                        <div class="col-sm-6 add-margin">
                            <input type="text" class="form-control" id="comp_type_nod" placeholder="" maxlength="2"
                                   name="comp_type_nod">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">Is location required</label>

                        <div class="col-sm-6">
                            <input type="radio" id="comp_type_loc_yes" name="comp_type_loc" value="yes">
                            <label>Yes</label>
                            <input type="radio" id="comp_type_loc_yno" name="comp_type_loc" value="no">
                            <label>No</label>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="text-center">
                            <button type="submit" class="btn btn-success">Submit</button>
                            <button class="btn btn-default" type="reset">Reset</button>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>