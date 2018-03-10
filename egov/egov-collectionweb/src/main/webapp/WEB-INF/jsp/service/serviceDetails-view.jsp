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
<html>
<head>
 <meta http-equiv="Pragma" content="no-cache"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/finAccountsTable.js?rnd=${app_release_no}"></script>
<script type="text/javascript">
	function onBodyLoad() {
		if (document.getElementById('voucherCreation').checked == false) {
			document.getElementById("voucherApprovedDetails").style.display = "none";
		} else {
			document.getElementById("voucherApprovedDetails").style.display = "";
		}
		var selectedStatus = document.getElementById("serviceType").value;
		if (selectedStatus == 'P') {
			document.getElementById("urlDetails").style.display = "";
		} else {
			document.getElementById("urlDetails").style.display = "none";
		}
	}
	function disableAll()  
	{
		var frmIndex=0;
		for(var i=0;i<document.forms[frmIndex].length;i++)
		{
			for(var i=0;i<document.forms[0].length;i++)
			{
				if(document.forms[0].elements[i].value != 'Close'){
					document.forms[frmIndex].elements[i].disabled =true;   
				}						
			}	
		}  
	}
</script>
<title> <s:text name="service.master.search.header"></s:text> </title>

</head>  

<body onload="onBodyLoad();loadDropDownCodes();loadGridOnValidationFail();disableAll();">
<s:form theme="simple" name="serviceDetailsForm" action="serviceDetails" method="post">
<s:token />
<s:push value="model">
	
	<input type="hidden" id="isviewmode" value="true"/>
	
	<jsp:include page="serviceDetails-form.jsp"/>
	<div class="buttonbottom">
			
			
			<label>
				<input type="button" id="closeButton" value="Close" onclick="javascript:window.close()" class="button"/>
			</label>			
		</div>
</s:push>
</s:form>
</body>
</html>
