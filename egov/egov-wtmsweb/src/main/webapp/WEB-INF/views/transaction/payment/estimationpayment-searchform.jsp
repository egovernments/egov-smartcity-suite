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

<%@ page contentType="text/html" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<div class="row">
    <div class="col-md-12">
        <div class="panel panel-primary" data-collapsed="0">
            <div class="panel-heading">
                <div class="panel-title">
                    <strong><spring:message code='title.watertaxSearch'/>
                    </strong>
                </div>
            </div>
            <div class="panel-body">
                <form:form role="form" class="form-horizontal form-groups-bordered"
                           id="waterSearchRequestForm" modelAttribute="waterConnectionDetails" method="get">
                    <div class="form-group">
                        <label class="col-sm-3 control-label text-right"><spring:message
                                code='lbl1.consumer.number'/></label>
                        <div class="col-sm-3 add-margin">
                            <form:input type="text" path="connection.consumerCode"
                                        class="form-control patternvalidation" data-pattern="number" maxlength="15"
                                        id="consumerCode"/>
                            <form:errors path="connection.consumerCode" cssClass="add-margin error-msg"/>
                        </div>
                        <label class="col-sm-2 control-label text-right"><spring:message
                                code='lbl.application.no'/></label>
                        <div class="col-sm-3 add-margin">
                            <form:input type="text" path="applicationNumber"
                                        class="form-control patternvalidation" data-pattern="alphanumericwithhyphen" maxlength="15"
                                        id="applicationNo"/>
                            <form:errors path="applicationNumber" cssClass="add-margin error-msg"/>
                        </div>
                    </div>
                    <br/>
                    <div class="form-group">
                        <div style="text-align:center">
                            <input type="button" id="search" class="btn btn-default" onclick="return formsubmit();"
                                   value="<spring:message code="lbl.search"/>"
                                   name="<spring:message code="lbl.search"/>"/>
                            <button type="button" id="btnclose" class="btn btn-default" onclick="window.close();">
                                <spring:message code="lbl.close"/>
                            </button>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>
<script>
    function formsubmit() {
        if ( $("#consumerCode").val() &&  $("#applicationNo").val()) {
                bootbox.alert("Please select any one of Consumer Code or Application Number.");
                return false;
        }
        else if($("#consumerCode").val().length == 0 &&  $("#applicationNo").val().length ==0)
        {
            bootbox.alert("Please select atleast one field.");
            return false;
        }
        window.open("/wtms/estimationcharges/verification?consumerNumber=" +  $("#consumerCode").val() + "&applicationNumber=" + $("#applicationNo").val(),"_self");
    }

</script>