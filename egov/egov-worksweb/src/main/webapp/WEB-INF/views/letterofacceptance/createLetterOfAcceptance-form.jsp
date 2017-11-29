<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>		
		<form:form id="createLetterOfAcceptanceForm" class="form-horizontal form-groups-bordered" modelAttribute="workOrder" role="form" action="loa-save" method="post" enctype="multipart/form-data">
			<div class="row">
				<div class="col-md-12"> 
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading">
							<div class="panel-title"><spring:message code="title.loa.create" /></div> 
						</div>
						<div>
							<spring:hasBindErrors name="workOrder">
				        		<div class="alert alert-danger col-md-10 col-md-offset-1">
						      			<form:errors path="*" cssClass="error-msg add-margin" /><br/>
						      	</div>
				        	</spring:hasBindErrors>
				        </div>
						<div class="panel-body">
							<jsp:include page="estimateDetails-view.jsp"/>
							<jsp:include page="letterOfAcceptance-details.jsp"/>
						</div>
					</div>
					
					<jsp:include page="uploadDocuments.jsp"/>
				</div>
			</div>	
			<div class="row">
				<div class="col-sm-12 text-center">
					<form:button type="submit" name="submit" id="save" class="btn btn-primary" value="Save" ><spring:message code="lbl.save"/></form:button>
					<form:button type="button" class="btn btn-default" id="button2" onclick="window.close();"><spring:message code="lbl.close"/></form:button>
				</div>
			</div>
		</form:form>  
<script src="<cdn:url value='/resources/js/letterofacceptance.js?rnd=${app_release_no}'/>"></script>
