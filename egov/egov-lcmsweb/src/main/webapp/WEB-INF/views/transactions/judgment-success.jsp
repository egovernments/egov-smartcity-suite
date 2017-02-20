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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<form:form method="post" action=""
	class="form-horizontal form-groups-bordered" modelAttribute="judgment"
	id="judgmentform">
	<c:if test="${not empty message}">
				<div role="alert">${message}</div>
			</c:if>
	<div class="row">
		<div class="col-md-12">
			<input type="hidden" name="mode" value="${mode}" />
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">Judgment Details</div>
				</div>
				<div class="panel-body ">
					<div class="row add-border">
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.orderdate" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content">
							<fmt:formatDate value="${judgment.orderDate}" var="OD"
								pattern="dd/MM/yyyy" />
							<c:out value="${OD}" />
						</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.senttodepton" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<fmt:formatDate pattern="dd/MM/yyyy" var="senttodept"
								value="${judgment.sentToDeptOn}" />
							<c:out value="${senttodept}" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.judgmenttype" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${judgment.judgmentType.name}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.implementbydate" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<fmt:formatDate pattern="dd/MM/yyyy"
								value="${judgment.implementByDate}" var="implby" />
							<c:out value="${implby}" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.costawarded" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${judgment.costAwarded}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.compensationawarded" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${judgment.compensationAwarded}</div>
					</div>

					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.judgmentdetails" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${judgment.judgmentDetails}</div>

					</div>
					<c:choose>
						<c:when test="${judgment.judgmentType.name == 'Enquiry'}">
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.enquirydetails" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${judgment.enquiryDetails}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.enquirydate" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<fmt:formatDate pattern="MM/dd/yyyyy"
								value="${judgment.enquiryDate}" var="enuiryDate" />
							<c:out value="${enuiryDate}" />
						</div>
					</div>
						</c:when>
					</c:choose>
					<c:choose>
						<c:when test="${judgment.judgmentType.name == 'Ex-parte Order'}">
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.setasidepetitiondate" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<fmt:formatDate pattern="MM/dd/yyyyy"
								value="${judgment.setasidePetitionDate}" var="petitiondate" />
							<c:out value="${petitiondate}" />
						</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.saphearingdate" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<fmt:formatDate pattern="MM/dd/yyyyy"
								value="${judgment.sapHearingDate}" var="sapdate" />
							<c:out value="${sapdate}" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.sapaccepted" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${judgment.sapAccepted}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.setasidepetitiondetails" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${judgment.setasidePetitionDetails}</div>
					</div>
					</c:when>
					</c:choose>
				</div>
			</div>
		</div>
		</div>
		 <jsp:include page="judgmentdocuments-view.jsp"/> 
		<div class="row text-center">
			<div class="add-margin">
				<a href="javascript:void(0)" class="btn btn-default"
					onclick="self.close()">Close</a>
			</div>
		</div>
		
</form:form>