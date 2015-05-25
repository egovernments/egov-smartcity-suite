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
<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sx" uri="/WEB-INF/struts-dojo-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

<%@ page language="java"%> 
 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>
<s:text name="targetArea.create.title"/> 
</title>
<sx:head/>
</head>
<script type="text/javascript" src="/EGF/resources/javascript/helper.js"></script>
<script type="text/javascript" src="/EGF/resources/javascript/targetArea.js"></script>
<body> 
	<s:form name="targetAreaForm" action="targetArea"  theme="css_xhtml" validate="true"> 
	    <s:token />
        <div style="color: green">
        <s:actionmessage/>
        </div>
        <div style="color: red">
        <s:actionerror/>
        </div>     
		<%@ include file='targetArea-WardTableSetup.jsp'%>
		<div class="formmainbox"> 
		<div style="color: red">
		    <div class="errorstyle" style="display:none" id="codeUniqueCheck" >
	         <s:text name="targetArea.code.already.exists"/>  
	    </div>
		</div>
		 <s:hidden name="mode" id="mode" value="%{mode}" />
			<div class="formheading" />
			<div class="subheadnew"><s:text name="targetArea.create.title"/></div>    
			<br/>

			<table align="center" width="90%">
				<tr>
					<td class="greybox" width="10%"><s:text name="targetArea.code" />:<span
						class="mandatory">*</span></td>
					<td class="greybox" width="20%"><s:textfield name="model.code"
							id="code" onblur="checkCodeUnique();checkAlpaNumeric(this);"
							value="%{model.code}" /></td>
					<egov:uniquecheck id="codeUniqueCheck" name="codeUniqueCheck"
						fieldtoreset="code" fields="['Value']"
						url='/master/targetArea!codeUniqueCheck.action' />

					<td class="greybox" width="10%"><s:text name="targetArea.name" />:<span
						class="mandatory">*</span></td>
					<td class="greybox" width="20%"><s:textfield name="model.name" style="width: 200px;"
							id="name" value="%{model.name}" /></td></tr>
							
					<tr><td class="bluebox"><s:text
							name="targetArea.assistant.engineer" />:<span class="mandatory">*</span></td>
					<td class="bluebox"><s:select name="model.position.id"
							id="position" list="dropdownData.positionsList" headerKey=""
							headerValue="---Choose---" listKey="position.id"
							listValue="employeeName+'--'+position.name"
							value="%{model.position.id}" /></td></tr>
				
			</table>

			<div id="labelSL" align="center">  
					<table width="80%">  
						<th><s:text name="targetArea.ward.details" /></th>
					</table>
				</div>
				<br />
				<div class="yui-skin-sam" align="center">
					<div id="wardDetailTable"></div>  
				</div>

			<script>
	     	makeWardDetailTable();
	     	<s:if test="%{targetAreaMappingsList.size()>0}">
	     	<s:iterator value="targetAreaMappingsList" status="stat" var="TargetAreaMappings">
			wardDataTable.addRow({SlNo:<s:property value="#stat.index+1"/>,
	         	 id:'<s:property value="boundary.id"/>',
  	         name:'<s:property value="boundary.name"/>'});
			var index = '<s:property value="#stat.index"/>';
			updateGridTargetArea('targetAreaMappingsResultList','boundary.id',index,'<s:property value="boundary.id"/>');
			</s:iterator>
			</s:if>
			<s:else>
			wardDataTable.addRow({SlNo:1,
	         	 id:'',
 	         name:''});
			</s:else>
			document.getElementById('wardDetailTable').getElementsByTagName('table')[0].width="60%";  
		   </script>   
		   
        <div class="buttonbottom" >
		<table align="center">
		<tr></tr>
		<tr class="buttonbottom" id="buttondiv" style="align: middle">
		    <s:if test="%{mode != 'create'}">
							<td><s:submit name="save" cssClass="buttonsubmit"
									value="Save" method="create" onclick="return validate();" /></td>
							<td><s:reset id="Reset" value="Reset" cssClass="button"  /></td>
			</s:if>
							<td><input type="button" value="Close"
								onclick="javascript:window.close();" class="button" /></td>
						</tr>
					</table>
				</div>
		</div>
		<s:token/>
	</s:form>
</body>
</html>
