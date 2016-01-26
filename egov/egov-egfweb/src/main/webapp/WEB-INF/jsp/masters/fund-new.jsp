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
<title><s:text name="fund.create" /></title>
<script type="text/javascript">

     function openSearch(obj, val)
     {
     	var a = new Array(3);
     	var str = "../HTML/Search.html?purposeId="+val;
     	var sRtn= showModalDialog(str,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");

     	if(sRtn != '')
     	{
     		a = sRtn.split("`~`");

     			document.getElementById('chartofaccountsByPayglcodeid.id').value = a[2];
     			document.getElementById('chartofaccountsByPayglcodeid.name').value = a[1];
     	}
     }
						
	function onLoadTask() {
		var close = '<s:property value="close"/>';
		var success = '<s:property value="success"/>';
							
		if (success == 'yes') {
			bootbox.alert("Fund Created Successfully");
		} else if((success == 'no')){
			bootbox.alert("Fund Could Not be Created");
		}
							
		if (close == 'true') {
			window.close();
		}
	}
						
	function validate(){
		if(document.getElementById('code').value == null || document.getElementById('code').value==''){
			bootbox.alert("Please enter Fund Code");
			return false;
		}
		if(document.getElementById('name').value == null || document.getElementById('name').value==''){
			bootbox.alert("Please enter Fund Name");
			return false;
		}
		return true;
	}
						
	function setClose() {
		var close = document.getElementById('close');    
		close.value = true;
		return true;
	}
</script>
</head>
<body onload="onLoadTask();">
	<div class="formmainbox">
		<div class="subheadnew">
			<s:text name="fund.create" />
		</div>
		<div style="color: red">
			<s:actionmessage theme="simple" />
			<s:actionerror />
			<s:fielderror />
		</div>
		<s:form name="fundForm" action="fund" theme="simple">
			<%@include file="fund-form.jsp"%>
			<div class="buttonbottom">
				<s:submit name="create" value="Save & New" method="create"
					cssClass="buttonsubmit" onclick="javascript: return validate();" />
				<s:submit name="create" value="Save & Close" method="create"
					cssClass="buttonsubmit" onclick="validate();setClose();" />
				<s:hidden name="close" id="close" />
				<input type="button" id="Close" value="Close"
					onclick="javascript:window.close()" class="button" /> <input
					type="hidden" name="tableName" id="tableName" value="fund" />
			</div>
	</div>
	<s:token />
	</s:form>
	</div>
	<script type="text/javascript">
		<s:if test="%{clearValues == true}">
			document.getElementById('code').value = "";
			document.getElementById('name').value = "";
			document.getElementById('fund.fund.id').value = "";
			document.getElementById('identifier').value = "";
			document.getElementById('chartofaccountsByPayglcodeid.name').value = "";
			document.forms[0].isactive.checked=false;
		</s:if>
	</script>
</body>
</html>
