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

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html>
<head>
<title><s:text name='transferProperty' /></title>
<link rel="stylesheet"
	href="<c:url value='/resources/global/css/font-icons/font-awesome/css/font-awesome.min.css' context='/egi'/>">

<link
	href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>"
	rel="stylesheet" type="text/css" />
<script
	src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>

<html>
<head>
 
<script type="text/javascript">
function submit(obj) {
	var type = obj.id;
  	var assessmentNo = '<s:property value = "%{assessmentNo}"/>';
	jQuery('<form>.').attr({
		method: 'post',
		action: '/ptis/property/transfer/new.action',
		target: '_self'
	}).append(jQuery('<input>').attr({
	    type: 'hidden',
	    id: 'type',
	    name: 'type',
	    value: type
	})).append(jQuery('<input>').attr({
	    type: 'hidden',
	    id: 'assessmentNo',
	    name: 'assessmentNo',
	    value: assessmentNo
	})).appendTo( document.body ).submit();
} 
  
</script>
</head>
<body>
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-body">
      	<div class="radio">
		  <label class="radio-inline"><input type="radio" name="optradio" id="REGISTERED TRANSFER" onchange="submit(this);"><s:text name="label.mutationtype.registrion"/></label>
		</div>
		<div class="radio">
		  <label class="radio-inline"><input type="radio" name="optradio" id="FULL TRANSFER" onchange="submit(this);"><s:text name="label.mutationtype.full"/></label>
		</div>
		<div class="radio disabled">
		  <label class="radio-inline"><input type="radio" name="optradio" id="PARTIAL TRANSFER" onchange="submit(this);" disabled><s:text name="label.mutationtype.partial"/></label>
		</div>
      </div>
    </div>
  </div>
</body>
</html>
