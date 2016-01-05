<!--  #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------  -->
<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="billInfoFetch.title" /></title>
<sx:head />
<script type="text/javascript">
	<s:if test="%{hasActionMessages()}">
		undoLoadingMask();
	</s:if>
</script>
</head>
<script type="text/javascript" src="/EGF/resources/javascript/helper.js"></script>
<body>
	<s:actionmessage />
	<s:actionerror />
	<s:form name="billInfoFetchForm" id="billInfoFetchForm"
		action="eBBillInfoFetch" theme="css_xhtml" validate="true">
		<s:token />
		<div class="formmainbox"></div>
		<div class="formheading" />
		<div class="subheadnew">
			<s:text name="billInfoFetch.title" />
		</div>
		<br />
		<table align="center" width="100%" cellspacing="0">
			<tr>
				<td colspan="2" width="50%" style="text-align: right;"><s:text
						name="billingCycle" /><span class="mandatory">*</span> :</td>
				<td colspan="2" width="50%"><s:select name="billingCycle"
						id="oddOrEvenBilling" list="dropdownData.billingCycles"
						headerKey="" headerValue="%{getText('default.select')}" /> <s:hidden
						name="type" /></td>

			</tr>
		</table>

		<div class="buttonbottom" align="center">
			<table border="0px" cellpadding="0" cellspacing="10"
				class="buttonbottom">
				<tr align="center">
					<td style="padding: 0px"><s:submit method="create"
							cssClass="buttonsubmit" value="Schedule"
							onclick="return validate();" /></td>
					<td style="padding: 0px"><input type="button" value="Close"
						onclick="javascript:window.close();" class="button" /></td>
				</tr>
			</table>
		</div>
		</div>
	</s:form>
	<script>
		paintAlternateColorForRows();
		
		function validate() {			
			if (!validateForm_billInfoFetchForm()) { 
				undoLoadingMask();
				return false;
			} 
			
			document.getElementById("billInfoFetchForm").action = "eBBillInfoFetch!create.action";
			return true;
		}
	</script>
</body>
</html>
