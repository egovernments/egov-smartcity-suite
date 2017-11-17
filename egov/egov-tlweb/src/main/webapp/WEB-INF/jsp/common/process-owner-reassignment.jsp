<%--
  ~ eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~ accountability and the service delivery of the government  organizations.
  ~
  ~  Copyright (C) <2017>  eGovernments Foundation
  ~
  ~  The updated version of eGov suite of products as by eGovernments Foundation
  ~  is available at http://www.egovernments.org
  ~
  ~  This program is free software: you can redistribute it and/or modify
  ~  it under the terms of the GNU General Public License as published by
  ~  the Free Software Foundation, either version 3 of the License, or
  ~  any later version.
  ~
  ~  This program is distributed in the hope that it will be useful,
  ~  but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~  GNU General Public License for more details.
  ~
  ~  You should have received a copy of the GNU General Public License
  ~  along with this program. If not, see http://www.gnu.org/licenses/ or
  ~  http://www.gnu.org/licenses/gpl.html .
  ~
  ~  In addition to the terms of the GPL license to be adhered to in using this
  ~  program, the following additional terms are to be complied with:
  ~
  ~      1) All versions of this program, verbatim or modified must carry this
  ~         Legal Notice.
  ~ 	Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~         derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~ 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~ 	For any further queries on attribution, including queries on brand guidelines,
  ~         please contact contact@egovernments.org
  ~
  ~      2) Any misrepresentation of the origin of the material is prohibited. It
  ~         is required that all modified versions of this material be marked in
  ~         reasonable ways as different from the original version.
  ~
  ~      3) This license does not grant any rights to any user of the program
  ~         with regards to rights under trademark law for use of the trade names
  ~         or trademarks of eGovernments Foundation.
  ~
  ~  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<%@ include file="/includes/taglibs.jsp" %>

<div class="modal fade reassign-screen display-hide" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header  alert-info">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 div class="modal-title"><strong><s:text name='lbl.reassign.title'/></strong></h4>
            </div>
            <div class="modal-body">
                <form id="reassign-form" class="form-horizontal form-groups-bordered">
                    <div class="form-group">
                        <div class="col-md-3">
                            <label class="control-label"><s:text name='lbl.reassign.to'/><span class="mandatory"></span></label>
                        </div>
                        <div class="col-md-8 add-margin">
                            <select id="approvalPosition" name="approvalPosition" required="required" class="form-control">
                                <option value=""><s:text name='default.select'/></option>
                            </select><br/>
                            <div id="reassignment_error" class="error-msg" style="display:none;" align="center"></div>
                        </div>
                    </div>
                    <div class="form-group text-center">
                        <div class="col-md-12 add-margin">
                            <button type="button" class="btn btn-primary" id="ReassignSubmit">
                                Reassign
                            </button>
                            <button type="button" class="btn btn-default" data-dismiss="modal"><s:text name='lbl.close'/></button>
                        </div>
                    </div>
                </form>
                <div id="success-div" style="display: none">
                    <div><b>
                        <span class="control-label" id="successmsg"/></b>
                    </div>
                    <br/>
                    <div class="text-center">
                        <button type="button" class="btn btn-default" data-dismiss="modal" id="reassignclose"><s:text name='lbl.close'/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="<cdn:url  value='/resources/js/app/process-owner-reassignment.js?rnd=${app_release_no}'/>"></script>