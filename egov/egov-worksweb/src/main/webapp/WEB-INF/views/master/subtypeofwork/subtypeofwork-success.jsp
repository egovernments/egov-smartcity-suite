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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>
<div class="page-container" id="page-container">
		<div class="new-page-header" id="successMessage">
			<c:if test="${ mode != 'view' && mode != 'edit'}">
				${createSuccess }
			</c:if>
			<c:if test = "${ mode == 'edit'}">
				${modifySuccess }
			</c:if>
		</div>
			<jsp:include page="subtypeofwork-commonview.jsp" />
		<div class="col-sm-12 text-center">
			<div class="row">		
				<c:if test="${ mode != 'view' && mode != 'edit' }">		
					<input type="submit" name="create" Class="btn btn-primary" value="Create New Sub Type of Work" id="CREATE" name="button" onclick="createNewSubTypeOfWork();" />
				</c:if>
				<input type="submit" name="closeButton" id="closeButton" value="Close" Class="btn btn-default" onclick="window.close();" />
			</div>
		</div>
		
</div>
<script
	src="<cdn:url value='/resources/js/master/subtypeofwork.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url value='/resources/js/common/commondisablebackspace.js?rnd=${app_release_no}'/>"></script>