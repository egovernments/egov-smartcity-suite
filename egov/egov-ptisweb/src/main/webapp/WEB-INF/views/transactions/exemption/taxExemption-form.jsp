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

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<style>
body
{
  font-family:regular !important;
  font-size:14px;
}
</style>
<body onload="loadOnStartUp(${isExempted})">

<c:if test="${not empty errorMsg}">
 	<div class="panel-heading">
				<div class="add-margin error-msg" style="text-align:center;">
					<strong><c:out value="${errorMsg}"/></strong>
				</div>
	</div>
</c:if>
<div class="row">
	<div class="col-md-12">
		<form:form class="form-horizontal form-groups-bordered" method="post"
			name="taxExemptionForm" id="taxExemptionForm" action=""
			modelAttribute="property" onsubmit="validate()">
			<div class="panel panel-primary" data-collapsed="0"
				style="text-align: left">
				<jsp:include page="../../common/commonPropertyDetailsView.jsp"></jsp:include>
			    <jsp:include page="../../common/ownerDetailsView.jsp"></jsp:include>
			    <form:hidden path="" name="mode" id="mode" value="${mode}"/>
			    <form:hidden path="" name="propertyByEmployee" id="propertyByEmployee" value="${propertyByEmployee}" />
			    <form:hidden path="" name="applicationSource" value="${applicationSource}"/>
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.exemption.heading" />
					</div>
				</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-xs-3">
							<spring:message code="lbl.exemption.reason" /><span class="mandatory"></span>
						</div>
						<div class="col-sm-3 add-margin control-label text-right">
							<form:select path="taxExemptedReason" id="taxExemptedReason" name="taxExemptedReason"
								 cssClass="form-control"
								>
								<form:option value="">
									<spring:message code="lbl.option.none" />
								</form:option>
								<form:options items="${taxExemptionReasons}" itemValue="id" 
									itemLabel="name" />
							</form:select>
						</div>
					</div>
				</div>
			</div>
			<c:if test="${propertyByEmployee == true}">
			<jsp:include page="../../common/commonWorkflowMatrix.jsp" />
			</c:if>
			<jsp:include page="../../common/commonWorkflowMatrix-button.jsp" />
		</form:form>
	</div>
</div>
</body>
<script language="javascript">

function loadOnStartUp(exempted) {
	
	var isAlert = ${isAlert};
	var isExempted = exempted;
	if(isExempted && isAlert){
		bootbox.dialog({
		    title: 'Property Tax Exemption',
		    closeButton: false,
		    onEscape: false,
		    message: 'The property is exempted from tax, are you sure you want to remove the exemption?',
		    buttons: {
		        'cancel': {
		            label: 'No',
		            className: 'btn-danger',
		            callback: function(){
		            	
		                      window.close();
		            }
		        },
		        'confirm': {
		            label: 'Yes',
		            className: 'btn-success'
		        }
		    }
		    
		});
	}else if(isExempted==false){
		
		jQuery('#taxExemptedReason').attr('required','required');
	}
	
	
}
</script>