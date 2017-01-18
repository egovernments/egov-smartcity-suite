<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
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

<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<script src="<cdn:url  value='/resources/app/js/demand-generate.js?rnd=${app_release_no}'/>"></script>
<div class="row">
    <div class="col-md-12">
        <div class="panel panel-primary" data-collapsed="0">
            <div class="panel-heading">
                <div class="panel-title">Demand Generation</div>
            </div>
            <div class="panel-body">
                <form:form role="form" action="create"  id="generatedemand" name="generatedemand" cssClass="form-horizontal form-groups-bordered"  method="post">
                    <div class="form-group">
                        <label class="col-sm-4 control-label text-right"><spring:message code="lbl.financialyear" /> </label>
                        <div class="col-sm-4 add-margin">
                            <select name="installmentYear" id="installmentYear" class="form-control" required="required">
                                <option value="" ><spring:message code="lbl.select"/></option>
                                <c:forEach items="${financialYearList}" var="finYear">
                                    <option value="${finYear.finYearRange}">${finYear.finYearRange}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="text-center">
                            <button type="submit" class='btn btn-primary' id="submit">
                                <spring:message code='lbl.generate.demand' />
                            </button>
                            <button type="button" class="btn btn-default" data-dismiss="modal" onclick="window.close();">
                                <spring:message code='lbl.close' /></button>
                        </div>
                    </div>
                    <c:if test="${demandGenerationLog != null && not empty demandGenerationLog.details}">
                        <div class="alert alert-info" role="alert"><spring:message code="${message}"/></div>
                        <div class="text-center add-margin">
                            <button type="submit" class='btn btn-primary' id="regenbtn" name="regenerate">Regenerate</button>
                        </div>
                        <script>
                            jQuery("#installmentYear").val('${demandGenerationLog.installmentYear}');
                        </script>
                        <div class="col-md-12 text-center add-margin">
                            <ul class="pagination pagination-xs pager" id="myPager"></ul>
                        </div>
                        <table class="table table-bordered" style="width:97%;margin:0 auto;">
                            <thead>
                            <tr>
                                <th>License Number</th>
                                <th>Status</th>
                                <th>Details</th>
                            </tr>
                            </thead>
                            <tbody id="dgdtl">
                            <c:forEach items="${demandGenerationLog.details}" var="detail">
                                <tr>
                                    <td><a href="#" onclick="window.open('/tl/viewtradelicense/viewTradeLicense-view.action?id='+${detail.license.id}, '',
                                            'scrollbars=yes,width=1000,height=700,status=yes'); return false;">${detail.license.licenseNumber}</a></td>
                                    <td>${detail.status}</td>
                                    <td><spring:message code="${detail.detail}" text="${detail.detail}"/> </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:if>
                </form:form>
            </div>
        </div>
    </div>
</div>