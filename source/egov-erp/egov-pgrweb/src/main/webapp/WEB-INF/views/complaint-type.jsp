<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<script src="<c:url value='/resources/pgr/js/complaint-type.js'/>"></script>

<div class="row" id="page-content">
  <div class="col-md-12">

      <div class="panel" data-collapsed="0">

          <div class="panel-body">

              <form action="javascript:void(0);" id="addcomplaint" method="post" class="form-horizontal form-groups-bordered">

                  <div class="form-group">
                      <label for="comp_type_name" class="col-sm-3 control-label">Complaint Type</label>
                      <div class="col-sm-6 add-margin">
                          <input type="text" class="form-control" id="comp_type_name" placeholder="" name="comp_type_name" >
                      </div>
                  </div>

                  <div class="form-group">
                      <label for="comp_type_dept" class="col-sm-3 control-label">Department</label>

                      <div class="col-sm-6 add-margin">
                          <select name="comp_type_dept" id="comp_type_dept" class="form-control">
                              <option value="">Select</option>
                              <option value="1">AED</option>
                              <option value="2">Mayor</option>
                          </select>
                      </div>

                  </div>

                  <div class="form-group">
                      <label for="comp_type_nod" class="col-sm-3 control-label">Number of days to resolve</label>
                      <div class="col-sm-6 add-margin">
                        <input type="text" class="form-control" id="comp_type_nod" placeholder="" maxlength="2" name="comp_type_nod" >
                      </div>
                  </div>

                  <div class="form-group">
                      <label class="col-sm-3 control-label">Is location required</label>

                      <div class="col-sm-6">
                          <input type="radio" id="comp_type_loc_yes" name="comp_type_loc" value="yes">
                          <label for="comp_type_loc_yes">Yes</label>

                          <input type="radio" id="comp_type_loc_yno" name="comp_type_loc" value="no" >
                          <label for="comp_type_loc_yno">No</label>
                      </div>

                  </div>

                  <div class="form-group">
                      <div class="text-center">
                          <button type="submit" class="btn btn-success">Submit</button>
                          <button class="btn btn-default" type="reset">Reset</button>
                      </div>
                  </div>

              </form>

          </div>

      </div>

  </div>
</div>