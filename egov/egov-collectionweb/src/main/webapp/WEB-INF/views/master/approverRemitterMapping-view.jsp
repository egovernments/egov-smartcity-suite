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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<script src="<cdn:url value='/resources/global/js/bootstrap/bootbox.min.js' context='/egi'/>"></script>

<style>
td.bluebox {
    padding: 10px;
}
table {
    max-width: 960px;
    margin: 0 auto;
    border: 0;
}

table.result-table {
    width: 100%;
    max-width: initial;
    border: 1;
}
.mandatorycoll {
    font-weight: normal;
    color: #cc0000;
    font-size: 11px;
}
</style>

<script>

    var selectedRowIdx = -1;

    function resetValues() {
        jQuery("select").val("");
    }

    function onSubmit(event) {
        //
    }

    function onModify(event) {
        if(selectedRowIdx < 0) {
            event.preventDefault();
            bootbox.alert("Please select one record by clicking on the radio button of search result.");
        }
    }

    function onSelectChange(idx) {
        console.log(idx);
        selectedRowIdx = idx;
    }
</script>

<form:form modelAttribute="mapspec" method="post" role="form">

    <div class="formmainbox">
        <div class="subheadnew">
            <c:if test='${mode == "MODIFY"}'>
                <spring:message code="form.title.approverRemitterMapping.modify" />
            </c:if>
            <c:if test='${mode != "MODIFY"}'>
                <spring:message code="form.title.approverRemitterMapping.view" />
            </c:if>
            
        </div>
        <table width="100%">
            <tr>
                <td class="bluebox">&nbsp;</td>
                <td class="bluebox"><spring:message code="lbl.approver" /></td>
                <td class="bluebox">
                    <form:select path="approverId" value="${approverId}">
                        <form:option value="">
                            <spring:message code="lbl.select.option" />
                        </form:option>
                        <optgroup label='<spring:message code="lbl.mapped.approver" />'>
                            <form:options items="${activeApproverList}" itemValue="id" itemLabel="name" />
                        </optgroup>
                        <optgroup label='<spring:message code="lbl.free.approver" />'>
                            <form:options items="${unmappedApproverList}" itemValue="id" itemLabel="name" />
                        </optgroup>
                    </form:select>
                </td>
                <td class="bluebox"><spring:message code="lbl.remitter" /></td>
                <td class="bluebox">
                    <form:select path="remitterId" value="${remitterId}">
                        <form:option value="">
                            <spring:message code="lbl.select.option" />
                        </form:option>
                        <form:options items="${remitterList}" itemValue="id" itemLabel="name" />
                    </form:select>
                </td>
            </tr>
            <tr>
                <td class="bluebox">&nbsp;</td>
                <td class="bluebox"><spring:message code="lbl.status" /> </td>
                <td class="bluebox" colspan="3">
                    <form:select path="isActive" value="${isActive}">
                        <form:option value="">
                            <spring:message code="lbl.select.all" />
                        </form:option>
                        <form:option value="true">
                            <spring:message code="lbl.active" />
                        </form:option>
                        <form:option value="false">
                            <spring:message code="lbl.inactive" />
                        </form:option>
                    </form:select>
                </td>
            </tr>
        </table>
        <br />
    </div> <%-- .formmainbox --%>

    <div class="buttonbottom">
        <input type="submit" class="buttonsubmit"
            onclick="return onSubmit(event);"
            value='<spring:message code="lbl.search" />' />
        <input type="button" class="button" value='<spring:message code="lbl.reset" />'
            id="resetbutton" name="clear" onclick="resetValues();">
        <input name="close" type="button" class="button"
            onclick="window.close()" value='<spring:message code="lbl.close" />' />
    </div>
</form:form>

<%-- Search Result --%>
<form:form
    action="edit" method="get" role="form">
    <div>
        <c:if test='${pageContext.request.method == "POST"}'>
        <table class="result-table">
            <thead>
            <tr>
                <c:if test='${mode == "MODIFY"}'>
                    <th class="bluebgheadtd"> </th>
                </c:if>
                <th class="bluebgheadtd" style="text-align: left;">
                    <spring:message code="lbl.sno" />
                </th>
                <th class="bluebgheadtd" style="text-align: left;">
                    <spring:message code="lbl.approver" />
                </th>
                <th class="bluebgheadtd" style="text-align: left;">
                    <spring:message code="lbl.remitter" />
                </th>
                <th class="bluebgheadtd" style="text-align: left;">
                    <spring:message code="lbl.status" />
                </th>
            </tr>
            </thead>
            <tbody>
                <c:if test='${empty maplist}'>
                    <tr>
                        <c:if test='${mode == "MODIFY"}'>
                            <td colspan='5'>
                        </c:if>
                        <c:if test='${mode != "MODIFY"}'>
                            <td colspan='4'>
                        </c:if>
                            <div class="subheadnew text-center">
                                <spring:message code="lbl.searchresult.norecord" />
                            </div>
                        </td>
                    </tr>
                </c:if>
                <c:forEach items="${maplist}" var="mapping" varStatus="iStatus">
                    <tr>
                        <c:if test='${mode == "MODIFY"}'>
                            <td>
                                <input type="radio" name="selectedId" value="${mapping.id}" onchange="onSelectChange(${iStatus.index})"/>
                            </td>
                        </c:if>
                        <td><c:out value="${iStatus.count}" /></td>
                        <td>
                            <c:out value="${mapping.approver.name}" />
                        </td>
                        <td>
                            <c:out value="${mapping.remitter.name}" />
                        </td>
                        <td>
                            <c:if test="${mapping.isActive}" >
                                <spring:message code="lbl.active" />
                            </c:if>
                            <c:if test="${!mapping.isActive}" >
                                <spring:message code="lbl.inactive" />
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        </c:if>

        <c:if test='${mode == "MODIFY" && pageContext.request.method == "POST"}'>
            <div class="buttonbottom">
                <input type="submit" class="buttonsubmit"
                    onclick="return onModify(event);"
                    value='<spring:message code="lbl.modify" />' />
                <input name="close" type="button" class="button"
                    onclick="window.close()" value='<spring:message code="lbl.close" />' />
            </div>
        </c:if>
    </div>
</form:form>