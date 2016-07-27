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
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="main-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">Council Preamble</div>
				</div>
				<div class="panel-body custom">
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.department" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${councilPreamble.department.name}</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.amount" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${councilPreamble.sanctionAmount}</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.gistofpreamble" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${councilPreamble.gistOfPreamble}</div>
												<%-- <div class="col-xs-3 add-margin"><spring:message code="lbl.photo" />
</div><div class="col-sm-3 add-margin view-content">
<c:if test="${not empty councilMember.photo}">
										<div class="col-md-3 col-xs-6 add-margin view-content">
											<img class='imagem_artigo' src='data:image/jpg;Base64, ${imageFile}' alt="IMG DESC">
											<img width="100" height="70" src='/council/councilmember/image?${councilMember.id}' /> 
										</div>	
										</c:if>
</div> --%>

						<div class="row add-border">
							<div class="col-md-2 col-xs-6 add-margin">
								<spring:message code="lbl.upload" />
							</div>
							<div class="col-md-3 col-xs-12 add-margin down-file view-content"
								id="links">
								<c:choose>
									<c:when test="${councilPreamble.filestoreid != null}">
										<%-- <c:if test="${(councilMember.photo.contentType == 'image/jpg') || (councilMember.photo.contentType == 'image/jpeg')|| (councilMember.photo.contentType == 'image/gif')|| 
										(councilMember.photo.contentType == 'image/png')}">
										<a href="/council/councilmember/downloadfile/${councilMember.photo.fileStoreId}"
												data-gallery> <img class="img-width add-margin"
												src="/council/councilmember/downloadfile/${councilMember.photo.fileStoreId}" alt="councilMember.photo.fileName"/></a>
										</c:if> --%>
										<a
											href="/council/councilmember/downloadfile/${councilPreamble.filestoreid.fileStoreId}"
											data-gallery target="_blank"> <img
											class="img-width add-margin"
											style="max-width: 50%; max-height: 50%;"
											src="/council/councilmember/downloadfile/${councilPreamble.filestoreid.fileStoreId}"
											alt="${councilPreamble.filestoreid.fileName}" /></a>

									</c:when>
									<c:otherwise>
										<spring:message code="msg.no.attach.found" />
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="row text-center">
		<div class="add-margin">
			<a href="javascript:void(0)" class="btn btn-default"
				onclick="self.close()">Close</a>
		</div>
	</div>