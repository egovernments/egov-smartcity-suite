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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/typeahead.css'/>">
<div class="row" id="page-content">

	<div class="col-md-12">
				 <c:if test="${not empty message}">
                    <div id="message" class="success"><spring:message code="${message}"/></div>
                </c:if>
		<form:form  mothod ="post" class="form-horizontal form-groups-bordered" modelAttribute="appConfig" id="appConfigForm" >
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<strong><spring:message code="title.viewpconfig"/></strong>
					</div>
				</div> 
			<div class="panel-body custom-form11">
					<div class="form-group">
								<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.AppconfigKeyName"/><span class="mandatory"></span></label>
								
								<div class="col-sm-6">
							<form:input path="keyName" id="keyName" type="text" class="form-control low-width" placeholder="" autocomplete="off" readonly="true" />
                            <form:errors path="keyName" cssClass="add-margin error-msg"/>
						</div>
								
							</div>
							
							<div class="form-group">
								
								<label class="col-sm-3 control-label"><spring:message code="lbl.description"/><span class="mandatory"></span></label>
								<div class="col-sm-6">
							<form:input path="description" id="description" type="text" class="form-control low-width" readonly="true" placeholder="" autocomplete="off" />
                            <form:errors path="description" cssClass="add-margin error-msg"/>
						</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.module"/><span class="mandatory"></span></label>
							<div class="col-sm-6">
							<form:input path="module.name" id="module" type="text" class="form-control low-width" placeholder="" autocomplete="off"  readonly="true"/>
					        <form:errors path="module" cssClass="add-margin error-msg"/>
							<div class="error-msg eithererror all-errors display-hide"></div>
			</div>
					</div>
					</div>
					
					<div class="col-md-12">
					 <table  class="table table-bordered"   id="floorDetails" >
      				 	<tr>
							<th><spring:message code="lbl.date"/></th>
							<th><spring:message code="lbl.values"/></th>
						</tr>
										<c:choose>
											<c:when test="${!appConfig.appDataValues.isEmpty()}">
												<c:forEach items="${appConfig.appDataValues}" var="var1" varStatus="counter">
												<tr id="Floorinfo">
													<td class="blueborderfortd">	
											
							<fmt:formatDate value="${var1.effectiveFrom}" var="historyDate"
											pattern="dd/MM/yyyy" />
							 <input type="text" class="form-control" value="${historyDate}" 
											 name="appDataValues[${counter.index}].effectiveFrom"  
											 id="appDataValues[${counter.index}].effectiveFrom"  readonly="true"/>
											</td>
											<td class="blueborderfortd" >	
											
											 <input type="text" class="form-control low-width"  value="${var1.value}" 
											 name="appDataValues[${counter.index}].value" id="appDataValues[${counter.index}].value"
											 readonly="true"/>
										
										 <input type="hidden"
														id="appDataValues[${counter.index}].id" value="${var1.id}" /></td>
										
										
														
													</tr>
												</c:forEach>
											</c:when>

										</c:choose>
					  			</table>
							</div>
					
					<div class="col-md-12 text-center">
						<div class="add-margin">
							 <button type="button" class="btn btn-default" data-dismiss="modal" onclick="self.close()" ><spring:message code="lbl.close"/></button>
                          </div>
					</div>
					</div>
					</div>
					</form:form>
				</div>
		</div>	
<script src="<c:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>	
<script src="<c:url value='/resources/global/js/jquery/plugins/exif.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>"/>
<script src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script src="<c:url value='/commonjs/ajaxCommonFunctions.js?rnd=${app_release_no}' context='/egi'/>"></script>


<script src="<c:url value='/resources/js/app/appconfig.js?rnd=${app_release_no}' context='/egi'/>"></script>
			<script>
			var cmdaindex=0;
	var moduleid = '${appConfig.module.id}';
	if(moduleid !== ''){
		$("#moduleid").val(moduleid);
	}

	 
	
	
</script>	
