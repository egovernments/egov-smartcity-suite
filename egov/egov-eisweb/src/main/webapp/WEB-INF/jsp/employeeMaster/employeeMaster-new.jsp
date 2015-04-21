<!-- #-------------------------------------------------------------------------------
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
#------------------------------------------------------------------------------- -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
  <c:catch var ="catchException"> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="eis.title"/></title>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
  <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
  <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
  <link rel="stylesheet" href="/resources/demos/style.css" />
<script  type="text/JavaScript">


function setCSSClasses(id,classes)
{
	
	document.getElementById(id).setAttribute('class',classes);
	document.getElementById(id).setAttribute('className',classes);

}

function validate()
{
	var records= assignmentDataTable.getRecordSet();
	var recordId;
    for(var i=0;i<records.getLength();i++)
    {
    	recordId=records.getRecord(i).getId();
    	dom.get("hodDeptListDisplay"+recordId).disabled=false;
    }
  	if(!validatePersonalInfo())
  	  	return false;

	if(!records.getLength()>0)
	{
		showError('<bean:message key="alertEmpPrimaryAssign"/>');//alertEmpPrimaryAssign = Employee should have a primary assignment'
		return false;
	}	
	if(!validateAssignmentForPrimary())
	{
		return false;
	}	
	if(document.getElementById("mode").value=='Modify')
	{
		if(trimAll(document.getElementById("remarksId").value)=="" || document.getElementById("remarksId").value==null)
		{
			showError('<bean:message key="alertRmkModify"/>');//alertRmkModify=Please fill the remarks for modification 	
			return false;
		}	
	}
	return true;
}

function show(arg)
{
	<s:if test="%{mode=='View'}">
	{
		disableFields('topTable');
		disableFields('personalInfo');
		disableFields('topTable');
		assignmentDataTable.disable();
	}
	</s:if>
	switch(arg)
	{
		case "PersonalInfo":
			document.getElementById("PersonalInfo").style.display="";
			document.getElementById("Assignment").style.display="none";

			setCSSClasses('personalInfoPage','First Active');
			setCSSClasses('assignmentPage','Last');

			break;

		case "Assignment":
				if(document.getElementById("mode").value!='View' && !validatePersonalInfo())
	  	  			return false;
				document.getElementById("PersonalInfo").style.display="none";
				document.getElementById("Assignment").style.display="";
	
				setCSSClasses('assignmentPage','First Active ActiveLast');
				setCSSClasses('personalInfoPage','Last');
				break;

				
	}
}

function onload()
{
	mandatoryDeathFiled();
}

function showError(msg)
{
	var errordiv=document.getElementById("empMstr_errors");
	errordiv.style.display = 'block';	
	if(document.getElementById("fieldError")!=null)
		document.getElementById("fieldError").style.display='none';
	errordiv.innerHTML = msg;
	window.scroll(0,0);
	return false;
}

</script>

</head>
<body onload="show('PersonalInfo');onload();">
<div class="errorcss" id="empMstr_errors" style="display:none;">
	</div>
 <div class="errorcss" style="display:none" id="empCodeUnique" >
	<s:text name="empcode.exists"/>  
</div>

 <div class="errorcss" style="display:none" id="panNumberUnique" >
	<s:text name="panno.exists"/>  
</div>	

<s:if test="%{hasErrors()}">
	<div class="errorcss" id="fieldError" >
			<s:actionerror cssClass="errorcss" />
			<s:fielderror cssClass="errorcss" />
	</div>		
</s:if>

<s:if test="%{hasActionMessages()}">
	<div class="errorcss">
			<s:actionmessage />
	</div>		
</s:if>	
<s:form action="employeeMaster" theme="simple"  name="empMaster" >
	<%@ include file='empMstr.jsp'%>
	<s:token/>
	<s:push value="model">
	<s:hidden id="mode" name="mode" value="%{mode}"/>
	<s:hidden id="id" name="empId" value="%{empId}"/>
	<s:hidden id="empCodeAutogen" name="empCodeAutogen" value="%{empCodeAutogen}"/>
	<div class="formmainbox">
	<div class="insidecontent">
	<div class="rbroundbox2">
	<div class="rbcontent2">
			<div class="estimateno">
				<s:text name="emp.no"/>
				<s:if test="%{mode=='Create'}"><s:text name="not.assigned"/>
				</s:if>
				<s:else>
					<s:property value="%{employeeCode}" />
				</s:else>
			</div>
			<br/>
			<ul id="Tabs" >
				<li id="personalInfoPage" class="First Active"><a href="#"  id="PersonalInfoMstr" onclick="show('PersonalInfo')"><s:text name="emp.details"/></a></li>
				<li id="assignmentPage" class="Last"><a href="#"  id="AssignmentDetails"  onclick="show('Assignment');"><s:text name="assign.details"/></a></li>
			</ul>
			<div id="PersonalInfo">
				<br/>
				<%@ include file='employeeMaster-personalInfo.jsp'%>
			</div>
			<div id="Assignment">
				<br/>
				<%@ include file='employeeMaster-assignmentDetails.jsp'%>
			</div>
			<br/>
	</div>
	</div>
	</div>
			<s:if test="%{mode=='Modify'}">
			<table>
				<tr>
						<td class="whiteboxwk">
							<span class="mandatory">*</span><s:text name="remarks"/>
						</td>
						<td class="whitebox2wk">
							<s:textarea name="remarks" value="%{remarks}" id="remarksId" cols="50" />
						</td>
				</tr>	
			</table>							
			</s:if>
	</div>		
			<center>
						<s:if test="%{mode!='View'}">
							<s:submit method="save" value="Save" cssClass="buttonfinal" onclick="return validate();" align="center"/>
						</s:if>	
							<input type="button" value="Close" class="buttonfinal" name="Close"/>
			</center>
				<div align="right" class="mandatory"><s:text name="mandate.fields"/></div>
	</s:push>
</s:form>

</body>
</html>
</c:catch>
<c:if test = "${catchException!=null}">
The error  is : ${catchException}<br>
</c:if>		
