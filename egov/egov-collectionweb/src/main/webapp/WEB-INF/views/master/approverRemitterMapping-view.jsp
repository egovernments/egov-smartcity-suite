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

<form:form modelAttribute="approverRemitterMappingSpec" method="post" role="form">

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
                            <form:options items="${activeMappedApproverList}" itemValue="id" itemLabel="name" />
                        </optgroup>
                        <optgroup label='<spring:message code="lbl.free.approver" />'>
                            <form:options items="${freeApproverList}" itemValue="id" itemLabel="name" />
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
<form:form modelAttribute="modifyRequest"
    action="edit" method="post" role="form">
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
                <th class="bluebgheadtd">
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
                <c:if test='${empty approverRemitterMappingList}'>
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
                <c:forEach items="${approverRemitterMappingList}" var="mapping" varStatus="iStatus">
                    <tr>
                        <c:if test='${mode == "MODIFY"}'>
                            <td>
                                <form:radiobutton path="selectedId" value="${mapping.id}" onchange="onSelectChange(${iStatus.index})"/>
                                <form:hidden path="selectedRow" value="${iStatus.count}" />
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
