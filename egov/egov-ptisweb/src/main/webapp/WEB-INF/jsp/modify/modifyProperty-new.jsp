<!-------------------------------------------------------------------------------
	eGov suite of products aim to improve the internal efficiency,transparency, 
    accountability and the service delivery of the government  organizations.
 
    Copyright (C) <2015>  eGovernments Foundation
 
    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org
 
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.
 
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .
 
    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:
 
 	1) All versions of this program, verbatim or modified must carry this 
 	   Legal Notice.
 
 	2) Any misrepresentation of the origin of the material is prohibited. It 
 	   is required that all modified versions of this material be marked in 
 	   reasonable ways as different from the original version.
 
 	3) This license does not grant any rights to any user of the program 
 	   with regards to rights under trademark law for use of the trade names 
 	   or trademarks of eGovernments Foundation.
 
   	In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
------------------------------------------------------------------------------->
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title><s:text name='ModProp.title' /></title>
<sx:head />

<link
	href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>"
	rel="stylesheet" type="text/css" />
<script
	src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>

<script type="text/javascript">
	jQuery.noConflict();
	jQuery("#loadingMask").remove();
	jQuery(function($) {
		try {
			jQuery(".datepicker").datepicker({
				format : "dd/mm/yyyy"
			});
		} catch (e) {
			console.warn("No Date Picker " + e);
		}

		jQuery('.datepicker').on('changeDate', function(ev) {
			jQuery(this).datepicker('hide');
		});
	});

	function onSubmit(action) {
		document.forms[0].action = action;
		document.forms[0].submit;
		return true;
	}
</script>
</head>

<body>
	<div align="left" class="errortext">
		<s:actionerror />
	</div>
	<!-- Area for error display -->
	<div class="errorstyle" id="property_error_area" style="display: none;"></div>
	<s:form name="ModifyPropertyForm" action="modifyProperty"
		theme="simple" validate="true">
		<s:push value="model">
			<s:token />
			<div class="formmainbox">
				<div class="headingbg" id="modPropHdr">
					<s:text name="ModProp.title" />
				</div>
				<%@ include file="modifyPropertyForm.jsp"%>
				<s:hidden name="modelId" id="modelId" value="%{modelId}" />
				<s:hidden id="indexNumber" name="indexNumber" value="%{indexNumber}" />
				<s:hidden id="modifyRsn" name="modifyRsn" value="%{modifyRsn}" />
				<s:hidden id="ownerName" name="ownerName" value="%{ownerName}" />
				<s:hidden id="propAddress" name="propAddress" value="%{propAddress}" />

				<%@ include file="../workflow/property-workflow.jsp"%>
				<div class="buttonbottom" align="center">
					<s:submit value="Forward" name="Forward" id="Modify:Forward"
						method="forwardModify" cssClass="buttonsubmit"
						onclick="return onSubmit('modifyProperty-forward.action');" />
					<input type="button" name="button2" id="button2" value="Close"
						class="button" onclick="window.close();" />
				</div>
			</div>

		</s:push>
	</s:form>
</body>
</html>
