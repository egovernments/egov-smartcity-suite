/*#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/
	function disableUserInfo()
	{
		if(document.getElementById('userChk').checked)
		{
			document.getElementById("userName").disabled=false;
			document.getElementById("addUser").disabled=false;
			document.getElementById("removeUser").disabled=false;
		}
		else{		
			document.getElementById("userName").disabled=true;
			document.getElementById("addUser").disabled=true;
			document.getElementById("removeUser").disabled=true;
		}
	}
	
	function addToUserList()
	{
	var user = document.getElementById("user");
	var length = document.getElementById("user").length+1;
		if(chkDuplication(user,document.forms[0].userId.value))
		{
				user.options[length-1] = new Option(document.forms[0].userName.value, document.forms[0].userId.value);
				user.options[length-1].selected=true;
		}	
		else
		{
		alert("Duplicate User")
		}
		
		document.getElementById("userId").value = "";
		document.getElementById("userName").value = "";
	}
	
	function chkDuplication(dropdown,dropdownValue)
	{
		for (i = 0; i < dropdown.length; i++) {
			if(dropdown[i].value==dropdownValue)
			{
			 return false;
			}
		}
		return true;
	}

var userNameSelectionHandler = function(sType, arguments)
    { 
        var oData = arguments[2];
	 	var userName = oData[0];
	 	var userId = oData[1];
	 	document.getElementById("userId").value = userId;
	 	document.getElementById("userName").value = userName;
 	}

var userNameSelectionEnforceHandler = function(sType, arguments) {
      		warn('improperUserNameSelection');
  	}
    
  	function removeFromUserList()
  	{
  		var userList=document.forms[0].userIdList;
	  	for (i = 0; i < userList.length; i++) {
	  		
		  	if (userList.options[i].selected) {
				userList.options[i]=null;
				i=i-1;
			}
		}

 
   	}
  //role related functions 	
   	function disableRoleInfo()
   	{
	   	if(document.getElementById('roleChk').checked)
		{
			document.getElementById("roleName").disabled=false;
			document.getElementById("addRole").disabled=false;
			document.getElementById("removeRole").disabled=false;
		}
		else{		
			document.getElementById("roleName").disabled=true;
			document.getElementById("addRole").disabled=true;
			document.getElementById("removeRole").disabled=true;
		}
   	}
   	
	 function addToRoleList()
	{
		var role = document.getElementById("role");
		var length = document.getElementById("role").length+1;
			if(chkDuplication(role,document.forms[0].roleId.value))
			{
					role.options[length-1] = new Option(document.forms[0].roleName.value, document.forms[0].roleId.value);
					role.options[length-1].selected=true;
			}	
			else
			{
			alert("Duplicate Role")
			}
			
			document.getElementById("roleId").value = "";
			document.getElementById("roleName").value = "";
	}
 
var roleNameSelectionHandler = function(sType, arguments)
    { 
        var oData = arguments[2];
	 	var roleName = oData[0];
	 	var roleId = oData[1];
	 	document.getElementById("roleId").value = roleId;
	 	document.getElementById("roleName").value = roleName;
 	}

var roelNameSelectionEnforceHandler = function(sType, arguments) {
      		warn('improperRoleNameSelection');
  	}
    
  	function removeFromRoleList()
  	{
  		var roleList=document.forms[0].roleIdList;
	  	for (i = 0; i < roleList.length; i++) {
	  		
		  	if (roleList.options[i].selected) {
				roleList.options[i]=null;
				i=i-1;
			}
		}

 
   	}
  	
  	//employee related functions
  	function disableEmpInfo()
   	{
	   	if(document.getElementById('empChk').checked)
		{
			document.getElementById("empName").disabled=false;
			document.getElementById("addEmp").disabled=false;
			document.getElementById("removeEmp").disabled=false;
		}
		else{		
			document.getElementById("empName").disabled=true;
			document.getElementById("addEmp").disabled=true;
			document.getElementById("removeEmp").disabled=true;
		}
   	}
   	
	function addToEmpList()
	{
		var emp = document.getElementById("emp");
		var length = document.getElementById("emp").length+1;
			if(chkDuplication(emp,document.forms[0].empId.value))
			{
					emp.options[length-1] = new Option(document.forms[0].empName.value, document.forms[0].empId.value);
					emp.options[length-1].selected=true;
			}	
			else
			{
				alert("Duplicate Employee");
			}
			
			document.getElementById("empId").value = "";
			document.getElementById("empName").value = "";
	}
var empNameSelectionHandler = function(sType, arguments)
    { 
  
        var oData = arguments[2];
	 	var empDetails = oData[0];
	 	var empName = empDetails.split('#')[0];
	 	var empId = oData[1];
	 	document.getElementById("empId").value = empId;
	 	document.getElementById("empName").value = empName;
 	}
var empNameSelectionEnforceHandler = function(sType, arguments) {
      		warn('improperempNameSelection');
  	}

function removeFromEmpList()
	{
		var empList=document.forms[0].empIdList;
  	for (i = 0; i < empList.length; i++) {
  		
	  	if (empList.options[i].selected) {
	  		empList.options[i]=null;
			i=i-1;
		}
	}


	}
  	//group related functions
    function disableGroupInfo()
   	{
	   	if(document.getElementById('groupChk').checked)
		{
			document.getElementById("groupSelect").disabled=false;
			document.getElementById("addGroup").disabled=false;
			document.getElementById("removeGroup").disabled=false;
		}
		else{		
			document.getElementById("groupSelect").disabled=true;
			document.getElementById("addGroup").disabled=true;
			document.getElementById("removeGroup").disabled=true;
		}
   	}

  	function removeFromGroupList()
  	{
  		var groupIdList=document.forms[0].groupIdList;
	  	for (i = 0; i < groupIdList.length; i++) {
	  		
		  	if (groupIdList.options[i].selected) {
				groupIdList.options[i]=null;
				i=i-1;
			}
		}

 
   	}
   	
   	function addToGroupList() 
   	{
	   	var groupSelect = document.forms[0].groupSelect;
	   	var group=document.forms[0].groupIdList;
	   	var i=0;
	   	for(i; i< groupSelect.length; i++)
	   	{
	   		if(groupSelect[i].selected)
	   		{
	   			if(chkDuplication(group,groupSelect[i].value))
	   			{
			   		group.options[i] = new Option(groupSelect[i].text,groupSelect[i].value);
					group.options[i].selected=true;
	   			}
	   			else
	   			{
	   				alert("Duplicate Group");
	   				break;
	   			}
	   		}
	   	}
		
   	}
   	
   	function disableFields()
   	{
	   	disableUserInfo();
	   	disableRoleInfo();
	   	disableEmpInfo();
	   	disableGroupInfo();
   	}
   	
   	function selectAll()
   	{
   		selectCheckedFields(document.getElementById("role"));
   		selectCheckedFields(document.getElementById("user"));
   		selectCheckedFields(document.getElementById("emp"));
   		selectCheckedFields(document.forms[0].groupIdList);
   	}
   	
   	function selectCheckedFields(objlist)
   	{
   	 	for(i=0; i< objlist.length; i++)
	   	{
	   		objlist.options[i].selected=true;
	   	}
	   	
   	}
   	function validateFields()
   	{
   		var msg = "";
   		var count = 1;
   		
   		if(document.getElementById('userChk').checked)
   		{
   			if(document.getElementById("user").length==0)
   			{
   				msg = (count++)+") User \n";
   			}
   		}
   		if(document.getElementById('roleChk').checked)
   		{
   			if(document.getElementById("role").length==0)
   			{
   				msg = msg+(count++)+") Role \n";
   			}
   		}
   		
   		if(document.getElementById('empChk').checked)
   		{
   			if(document.getElementById("emp").length==0)
   			{
   				msg = msg+ (count++)+") Employee \n";
   			}
   		}
   		if(document.getElementById('groupChk').checked)
   		{
   			if(document.getElementById("group").length==0)
   			{
   				msg = msg+ (count++)+") Group \n";
   			}
   		}
   		if(msg == "" ) {
   			return true;
   		} else {
   			alert("The following fields can not be empty \n"+msg);
   			return false;
   		}
   	}
