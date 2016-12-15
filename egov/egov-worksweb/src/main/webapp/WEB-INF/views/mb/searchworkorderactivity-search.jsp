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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>
		<form name="SearchRequest" role="form" action="" id="SearchRequest" class="form-horizontal form-groups-bordered">
			<input type="hidden" id="errorSelect" value="<spring:message code='msg.select.workorderactivity' />">
			<div class="row">
				<div class="col-md-12">
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading">
							<div class="panel-title" style="text-align:center;"><spring:message code="title.searchview.workorderactivity" /></div>
						</div>
						<input type="hidden" name="workOrderEstimateId" id="workOrderEstimateId" value="${workOrderEstimateId }"/>
						<input type="hidden" name="id" id="mbHeaderId" value="${mbHeaderId }"/>
						<div class="panel-body">
							<div class="col-md-12 text-left">
								<spring:message code="lbl.workordernumber" />: ${workOrderNo }
							</div><br /><br />
							<div class="form-group">
								<label class="col-sm-2 control-label text-right"><spring:message code="lbl.item.code" /></label>
								<div class="col-sm-3 add-margin">
									<input name="itemCode" id="itemCode" class="form-control"/>
								</div>
								<label class="col-sm-2 control-label text-right"><spring:message code="lbl.description.item" /></label>
								<div class="col-sm-3 add-margin">
									<input name="description" id="description" class="form-control"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label text-right"><spring:message code="lbl.sor.nonsor.type" /></label>
								<div class="col-sm-3 add-margin">
									<select name="sorType" data-first-option="false" id="sorType" class="form-control">
										<option value="">
											<spring:message code="lbl.select" />
										</option>
										<option value="SOR">SOR</option>
 										<option value="Non SOR">Non SOR</option>
									</select>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12 text-center">
					<button type='button' class='btn btn-primary' id="btnsearch">
						<spring:message code='lbl.search' />
					</button>
					<a href='javascript:void(0)' class='btn btn-default'
				onclick='self.close()'><spring:message code='lbl.close' /></a>
				</div>
			</div>
		</form>  
	<jsp:include page="searchworkorderactivity-searchresult.jsp"/>
<script>
	$('#btnsearch').click(function(e) {
		if ($('form').valid()) {
		} else {
			e.preventDefault();
		}
	});
</script>
<script src="<cdn:url value='/resources/js/mb/searchworkorderactivityhelper.js?rnd=${app_release_no}'/>"></script>