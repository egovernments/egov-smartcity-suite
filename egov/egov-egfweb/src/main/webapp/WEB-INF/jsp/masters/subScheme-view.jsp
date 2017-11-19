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


<%@ include file="/includes/taglibs.jsp"%>

<%@ page language="java"%>
<html>
<head>
<title><s:if test="%{showMode=='edit'}">
		<s:text name="subscheme.modify" />
	</s:if> <s:else>
		<s:text name="masters.subscheme.searchview.title" />
	</s:else></title>
<script type="text/javascript">
		function validate(){
			if(document.getElementById('name').value == null || document.getElementById('name').value==''){
				bootbox.alert("Please enter Name");
				return false;
			}
			if(document.getElementById('code').value == null || document.getElementById('code').value==''){
				bootbox.alert("Please enter Code");
				return false;
			}
			if(document.getElementById('validfrom').value == null || document.getElementById('validfrom').value==''){
				bootbox.alert("Please enter Valid From Date");
				return false;
			}
			if(document.getElementById('validto').value == null || document.getElementById('validto').value==''){
				bootbox.alert("Please enter Valid To Date");
				return false;
			}
			if(isNaN(document.getElementById('initialEstimateAmount').value)){
				bootbox.alert("Please enter valid Initial Eastimate Amount");
				return false;
			}
			document.getElementById("scheme").disabled  = false;
			document.subSchemeForm.action='${pageContext.request.contextPath}/masters/subScheme-create.action';
	    	document.subSchemeForm.submit();
			return true;
		}
		
	function disableControls(isDisable) {
		for ( var i = 0; i < document.subSchemeForm.length; i++)
			document.subSchemeForm.elements[i].disabled = isDisable;
		document.getElementById('Close').disabled = false;
		
		var id; 
		var obj;
		
		// Calendar ids are of the form calendar1,calendar2 etc.
			for (i = 0; i <= 5; i++) {
				id = "calendar" + i;
				obj=document.getElementById(id);
				if(isDisable)
					obj.removeAttribute('href');
			}

	}	
</script>

</head>
<body name="subSchemeView">

	<jsp:include page="../budget/budgetHeader.jsp" />
	<div style="color: green;"><s:actionmessage theme="simple" /></div>
	<div class="formmainbox">
		<div class="subheadnew">
			<s:if test="%{showMode=='new'}">
			<s:text name="subScheme.add" />
				
			</s:if>
			<s:elseif  test="%{showMode=='view'}">
				<s:text name="masters.subscheme.searchview.title" />
			</s:elseif>
			<s:else><s:text name="subscheme.modify" /></s:else>
			
		</div>

		<s:actionerror />
		<s:fielderror />
		<s:form name="subSchemeForm" action="subScheme" theme="simple">
			<s:hidden name="showMode" />
			<s:hidden name="id" />
			<s:push value="model">
				<%@include file="subScheme-form.jsp"%>
	</div>

	<div class="buttonbottom" style="padding-bottom: 10px;">


		<input type="button" id="Close" value="Close"
			onclick="javascript:window.close()" class="button" />
	</div>
	<script>
			<s:if test="%{isactive==true}">
			    document.getElementById("isActive").checked="checked";
			</s:if>
			<s:if test="%{showMode=='edit'}">
			document.getElementById("scheme").disabled  = true;
			 	<s:if test="%{clearValues==true}">
			 		bootbox.alert('<s:text name="subscheme.saved.successfully" />');
			 		window.close();
			 	</s:if>	
			</s:if>
			<s:else>
				disableControls(true);
			</s:else>
		</script>
	<s:hidden name="createdBy" value="%{createdBy.id}" />
	<s:hidden name="createdDate" value="%{createdDate}" />
	</s:push>
	<s:token />
	</s:form>
	<script>

	</script>
</body>
</html>
