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
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>
<form:form modelAttribute="contractorMB" name="contractorMB" role="form" method="post" id="contractorMB" class="form-horizontal form-groups-bordered" accept-charset="utf-8" enctype="multipart/form-data">
	<input id="errorMandatory" type="hidden" value="<spring:message code="error.mandatory.fields" />" />
	
	<div class="new-page-header"><spring:message code="lbl.viewcontractormb" /></div> 
	
	<div>
	       <spring:hasBindErrors name="contractorMB">
			    <div class="col-md-10 col-md-offset-1">
					<form:errors path="*" cssClass="error-msg add-margin" /><br/>
			   </div>
          </spring:hasBindErrors>
	</div>
	<%@ include file="contractormb-header.jsp"%>
	<%@ include file="contractormb-viewdetails.jsp"%>
	<%@ include file="additional-items-view.jsp"%>
	<jsp:include page="../common/uploaddocuments.jsp"/>
	<div class="form-group">
		<label class="col-sm-2 control-label text-right">
		    <spring:message code="lbl.remarks" />
		</label>
		<div class="col-sm-3 add-margin view-content">
			<c:out value="${contractorMB.remarks }" default="N/A"></c:out>
		</div>
	</div>
	<div class="buttonbottom" align="center">
		<table>
			<tr>
				<td id="actionButtons">
					<input type="button" name="button2" id="button2" value="Close" class="btn btn-default" onclick="window.close();" />
				</td>
			</tr>
		</table>
	</div>
</form:form>

<script type="text/javascript"
	src="<cdn:url value='/resources/js/contractorportal/contractormb.js?rnd=${app_release_no}'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/contractorportal/contractormbmsheet.js?rnd=${app_release_no}'/>"></script>