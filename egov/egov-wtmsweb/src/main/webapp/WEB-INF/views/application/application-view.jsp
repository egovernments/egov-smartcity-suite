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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>


<form:form  id="waterConnectionSuccess" method ="get" class="form-horizontal form-groups-bordered" modelAttribute="waterConnectionDetails" >				
<div class="page-container" id="page-container">
	<input type="hidden" id="mode" name="hidden" value="${mode}"/>
	<input type="hidden" id="applicationTypeCode"  value="<c:out value="${waterConnectionDetails.applicationType.code}" />" />
	<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message  code="lbl.basicdetails"/>
				</div>
			</div>
			<input type="hidden" id="waterTaxDueforParent" value="${waterTaxDueforParent}" name="waterTaxDueforParent"/>
				<input type="hidden"  id="citizenRole" value="${citizenRole}" />
				<input type="hidden"  id="cscUserRole" value="${cscUserRole}" />
				<input type="hidden"  id="checkOperator" value="${checkOperator}" />
				<input type="hidden"  id="ulbUserRole" value="${ulbUserRole}" />
                <input type="hidden"  id="citizenPortal" value="${citizenPortal}" />
			<input id="applicationCode" type="hidden" value="<c:out value="${waterConnectionDetails.applicationNumber}" />" />  
			<input id="consumerCode" type="hidden" value="<c:out value="${waterConnectionDetails.connection.consumerCode}" />" /> 						
				<jsp:include page="commonappdetails-view.jsp"></jsp:include>
		</div>
	<jsp:include page="connectiondetails-view.jsp"></jsp:include>
</div>			
<div class="row text-center">
	<div class="add-margin">
	

	<c:choose>
	<c:when test="${waterConnectionDetails.legacy}">
	<%-- <input type="button" class="btn btn-primary" id="viewWorkOrder" value="Edit" onclick="showEdit('<c:out value="${waterConnectionDetails.connection.consumerCode}" />')"/> --%>
	</c:when>
	<c:otherwise>
        <c:if test="${!citizenPortal }">
            <c:if test="${waterConnectionDetails.status.code == 'ESTIMATIONNOTICEGENERATED' && waterConnectionDetails.status.code != 'VERIFIED'  && (checkOperator ) }">
                <button type="submit" class="btn btn-primary" id="payBtn"><spring:message code="lbl.collect.fees"/></button>
            </c:if>
            <c:if test="${waterConnectionDetails.status.code == 'ESTIMATIONNOTICEGENERATED' && ( citizenRole && !checkOperator) }">
                <button type="submit" class="btn btn-primary" id="payBtn"><spring:message code="lbl.pay.tax"/></button>
            </c:if>
            <c:if test="${waterConnectionDetails.status.code == 'SANCTIONED' && waterConnectionDetails.connectionType == 'METERED' && checkOperator && waterTaxDueforParent > 0}">
                <button type="submit" class="btn btn-primary" id="payBtn"><spring:message code="lbl.pay.tax"/></button>
            </c:if>
            <c:if test="${waterConnectionDetails.status.code == 'SANCTIONED' && waterConnectionDetails.connectionType != 'METERED' && checkOperator }">
                <button type="submit" class="btn btn-primary" id="payBtn"><spring:message code="lbl.pay.tax"/></button>
            </c:if>
            <c:if test="${waterConnectionDetails.status.code != 'ESTIMATIONNOTICEGENERATED' && waterTaxDueforParent > 0 && (citizenRole && !checkOperator) }">
                <button type="submit" class="btn btn-primary" id="payBtn"><spring:message code="lbl.pay.online"/></button>
            </c:if>
            <c:if test="${waterConnectionDetails.status.code != 'CREATED' && waterConnectionDetails.status.code != 'VERIFIED'
	&& waterConnectionDetails.status.code != 'CANCELLED' && waterConnectionDetails.fieldInspectionDetails != null && !legacy && !citizenRole && waterConnectionDetails.applicationType.code !='CLOSINGCONNECTION'}">
                <button type="submit" class="btn btn-primary" id="viewEstimationNotice"><spring:message code="lbl.printestimationnotice"/></button>
            </c:if>
            <c:if test="${!legacy &&  !citizenRole && (waterConnectionDetails.status.code == 'SANCTIONED' || waterConnectionDetails.status.code == 'WORKORDERGENERATED')}">
                <button type="submit" class="btn btn-primary" id="viewWorkOrder"><spring:message code="lb.printworkorder"/></button>
            </c:if>

            <c:if test="${ !citizenRole && ( waterConnectionDetails.applicationType.code == 'CLOSINGCONNECTION'  && waterConnectionDetails.connectionStatus == 'CLOSED' && waterConnectionDetails.status.code == 'CLOSERSANCTIONED')}">
                <button type="submit" class="btn btn-primary" id="viewClosureAck"><spring:message code="lbl.printclosureAck"/></button>
            </c:if>
            <c:if test="${ !citizenRole && ( waterConnectionDetails.applicationType.code == 'RECONNECTION'  && waterConnectionDetails.connectionStatus == 'ACTIVE' && waterConnectionDetails.status.code == 'RECONNECTIONSANCTIONED')}">
                <button type="submit" class="btn btn-primary" id="viewRecOonnAck"><spring:message code="lbl.printReconnAck"/></button>
            </c:if>

            <c:if test="${ ((ulbUserRole != null && ulbUserRole!='') ||  (cscUserRole!=null && cscUserRole!='')) && waterConnectionDetails.connectionStatus == 'ACTIVE' && waterConnectionDetails.applicationType.code == 'NEWCONNECTION' && waterConnectionDetails.closeConnectionType== null && waterConnectionDetails.connectionType !='METERED'}">
                <button type="submit" class="btn btn-primary" id="addConnection"><spring:message code="lbl.addconnection"/></button>
                <button type="submit" class="btn btn-primary" id="changeConnection"><spring:message code="lbl.changeconnection"/></button>
                <button type="submit" class="btn btn-primary" id="closureConnection"><spring:message code="lbl.closureconnection"/></button>
                <button type="submit" class="btn btn-primary" id="dcbscreen-view"><spring:message code="lbl.dcbscreen.view"/></button>
                <%-- <c:if test="${waterTaxDueforParent > 0}">
                <button type="submit" class="btn btn-primary" id="payBtn"><spring:message code="lbl.pay.tax"/></button>
                </c:if> --%>
            </c:if>
            <c:if test="${ ((ulbUserRole != null && ulbUserRole!='') ||  (cscUserRole!=null && cscUserRole!='')) && waterConnectionDetails.connectionStatus == 'ACTIVE' && waterConnectionDetails.applicationType.code == 'ADDNLCONNECTION'&& waterConnectionDetails.closeConnectionType== null && waterConnectionDetails.connectionType !='METERED'}">
                <button type="submit" class="btn btn-primary" id="changeConnection"><spring:message code="lbl.changeconnection"/></button>
                <button type="submit" class="btn btn-primary" id="closureConnection"><spring:message code="lbl.closureconnection"/></button>
                <button type="submit" class="btn btn-primary" id="dcbscreen-view"><spring:message code="lbl.dcbscreen.view"/></button>

            </c:if>

            <c:if test="${ ((ulbUserRole != null && ulbUserRole!='') ||  (cscUserRole!=null && cscUserRole!='')) && waterConnectionDetails.applicationType.code == 'NEWCONNECTION' && waterConnectionDetails.connectionStatus == 'ACTIVE' && waterConnectionDetails.connectionType == 'METERED' && waterConnectionDetails.closeConnectionType== null }">
                <button type="submit" class="btn btn-primary" id="changeConnection"><spring:message code="lbl.changeconnection"/></button>
                <button type="submit" class="btn btn-primary" id="closureConnection"><spring:message code="lbl.closureconnection"/></button>
                <button type="submit" class="btn btn-primary" id="dcbscreen-view"><spring:message code="lbl.dcbscreen.view"/></button>
                <%-- <button type="submit" class="btn btn-primary" id="payBtn"><spring:message code="lbl.pay.tax"/></button> --%>
            </c:if>

            <c:if test="${ ((ulbUserRole != null && ulbUserRole!='') ||  (cscUserRole!=null && cscUserRole!='')) && waterConnectionDetails.applicationType.code == 'ADDNLCONNECTION'  && waterConnectionDetails.connectionStatus == 'ACTIVE' && waterConnectionDetails.connectionType == 'METERED' && waterConnectionDetails.closeConnectionType== null }">
                <button type="submit" class="btn btn-primary" id="changeConnection"><spring:message code="lbl.changeconnection"/></button>
                <button type="submit" class="btn btn-primary" id="closureConnection"><spring:message code="lbl.closureconnection"/></button>
                <button type="submit" class="btn btn-primary" id="dcbscreen-view"><spring:message code="lbl.dcbscreen.view"/></button>
                <%-- <button type="submit" class="btn btn-primary" id="payBtn"><spring:message code="lbl.pay.tax"/></button> --%>
            </c:if>
            <c:if test="${ ((ulbUserRole != null && ulbUserRole!='') ||  (cscUserRole!=null && cscUserRole!='')) && waterConnectionDetails.connectionStatus == 'ACTIVE' && waterConnectionDetails.applicationType.code == 'CHANGEOFUSE' && waterConnectionDetails.closeConnectionType== null && waterConnectionDetails.connectionType !='METERED'}">
                <button type="submit" class="btn btn-primary" id="changeConnection"><spring:message code="lbl.changeconnection"/></button>
                <button type="submit" class="btn btn-primary" id="closureConnection"><spring:message code="lbl.closureconnection"/></button>
                <button type="submit" class="btn btn-primary" id="dcbscreen-view"><spring:message code="lbl.dcbscreen.view"/></button>
                <%-- <c:if test="${waterTaxDueforParent > 0}">
                <button type="submit" class="btn btn-primary" id="payBtn"><spring:message code="lbl.pay.tax"/></button>
                </c:if> --%>
            </c:if>

            <c:if test="${ ((ulbUserRole != null && ulbUserRole!='') ||  (cscUserRole!=null && cscUserRole!='')) && waterConnectionDetails.applicationType.code == 'CHANGEOFUSE' && waterConnectionDetails.connectionStatus == 'ACTIVE' && waterConnectionDetails.connectionType == 'METERED' && waterConnectionDetails.closeConnectionType== null }">
                <button type="submit" class="btn btn-primary" id="changeConnection"><spring:message code="lbl.changeconnection"/></button>
                <button type="submit" class="btn btn-primary" id="closureConnection"><spring:message code="lbl.closureconnection"/></button>
                <button type="submit" class="btn btn-primary" id="dcbscreen-view"><spring:message code="lbl.dcbscreen.view"/></button>
                <%-- <button type="submit" class="btn btn-primary" id="payBtn"><spring:message code="lbl.pay.tax"/></button> --%>
            </c:if>
            <c:if test="${ ((ulbUserRole != null && ulbUserRole!='') ||  (cscUserRole!=null && cscUserRole!=''))  && waterConnectionDetails.applicationType.code == 'CLOSINGCONNECTION'  && waterConnectionDetails.connectionStatus == 'CLOSED' && waterConnectionDetails.closeConnectionType== 'T' && waterConnectionDetails.status.code == 'CLOSERSANCTIONED' }">
                <button type="submit" class="btn btn-primary" id="re-connection"><spring:message code="lbl.re-connection"/></button>
            </c:if>
        </c:if>
	</c:otherwise>
	</c:choose>
		<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close" /></a>
	</div>
</div>
</form:form>
<script>

function showEdit(obj)
{
window.location="/wtms/application/newConnection-editExisting/"+obj;
}
</script>

<script src="<cdn:url value='/resources/js/app/applicationsuccess.js?rnd=${app_release_no}'/>"></script>