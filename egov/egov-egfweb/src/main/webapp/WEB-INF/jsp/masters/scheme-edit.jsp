#-------------------------------------------------------------------------------
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
#-------------------------------------------------------------------------------
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sx" uri="/WEB-INF/struts-dojo-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

<%@ page language="java"%>
<html>
  <head>
    
    <title>
    	<s:text name="scheme.edit.title"/> 
    </title>
    <sx:head/>
   <SCRIPT type="text/javascript">
    function checkuniquenesscode(){
    	document.getElementById('codeuniquecode').style.display ='none';
		var code=document.getElementById('code').value;
		var id=document.getElementById('id').value;
		populatecodeuniquecode({code:code,id:id});		
    }
    
    function checkuniquenessname(){
    	document.getElementById('uniquename').style.display ='none';
		var name=document.getElementById('name').value;
		var id=document.getElementById('id').value;
		populateuniquename({name:name,id:id});		
    }
    function validate(){
        if (!validateForm_scheme()) {
        	undoLoadingMask();
    		return false;
            }
        
        if (compareDate(document.getElementById('validfromId').value,document.getElementById('validtoId').value) == -1){
            alert("End date should be greater than Start date");
            undoLoadingMask();
            document.getElementById('validtoId').value = "";
            return false;
 	    } 
 	    
    	return true;
    }
  </SCRIPT>
  </head>
  <body>
   <s:form name="schemeForm" action="scheme" theme="css_xhtml" validate="true">
   <s:push value="model">
    <div  class="formmainbox"><div  class="subheadnew"><s:text name="scheme.edit.title"/></div>
   
    		<div style="color: red">
		<s:actionerror/>  
		</div>
		<div style="color: green">
		
		<s:actionmessage />
		</div>
	    <div class="errorstyle" style="display:none" id="codeuniquecode" >
	         <s:text name="scheme.code.already.exists"/>
	    </div>
	    <div class="errorstyle" style="display:none" id="uniquename" >
	         <s:text name="scheme.name.already.exists"/>
	    </div>
   
    <s:token/>           
   		<s:hidden name="mode" id="mode" value="%{mode}" />
	    <s:hidden name="id" id="id" value="%{id}" />
     	<table width="100%" border="0" cellspacing="0" cellpadding="0">                   
    		<tr>
					<td class="greybox" width="10%" ><s:text name="scheme.code"/><span class="mandatory">*</span></td>
				    <td class="greybox" width="30%" ><s:textfield id="code" name="code" value="%{scheme.code}" onblur="checkuniquenesscode();"/></td>
				     <egov:uniquecheck id="codeuniquecode" name="codeuniquecode" fieldtoreset="code" fields="['Value']" url='masters/scheme!codeUniqueCheck.action'/>
				                       
				    <td class="greybox" width="10%"><s:text name="scheme.name"/><span class="mandatory">*</span></td>
				    <td class="greybox"  width="30%"><s:textfield id="scheme.name" name="name" value="%{scheme.name}" onblur="checkuniquenessname();"/></td>
					<egov:uniquecheck id="uniquename" name="uniquename" fieldtoreset="name" fields="['Value']" url='masters/scheme!nameUniqueCheck.action'/>
			</tr>
			<tr>
			        <td class="bluebox"><s:text name="scheme.fund"/><span class="mandatory">*</span></td>
				    <td class="bluebox">
					<s:select name="fund" id="fundId" list="dropdownData.fundDropDownList" listKey="id" listValue="name" headerKey="" headerValue="----Select----"  value="scheme.fund.id" />
					</td>
					<td class="bluebox">IsActive</td>
					<td class="bluebox"><s:checkbox id="isactive" name="isactive" value="%{scheme.isactive}"/> </td>
			</tr>
			<tr>
					<td class="greybox" > <s:text name="scheme.startDate" /><span class="mandatory">*</span></td>
					<td  class="greybox" ><s:date name="validfrom" id="validfromId"  var="tempFromDate" format="dd/MM/yyyy" />
					<s:textfield name="validfrom" id="validfromId" value="%{tempFromDate}"  format="dd/MM/yyyy" maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
					<a href="javascript:show_calendar('schemeForm.validfrom',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img  src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
					
					<td  class="greybox" ><s:text name="scheme.endDate" /><span class="mandatory">*</span></td>
					<td  class="greybox">
					
					<s:date name="validto" id="validtoId" var="tempToDate" format="dd/MM/yyyy"/>
					<s:textfield name="validto" id="validtoId" value="%{tempToDate}"  format="dd/MM/yyyy" maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
					<a href="javascript:show_calendar('schemeForm.validto',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
			</tr>
			<tr>
					<td class="bluebox" width="10%"><s:text name="scheme.description" /></td>
					<td class="bluebox" colspan="3" ><s:textarea  id="description" name="description" value="%{scheme.description}" style="width:470px"/></td>				
			</tr>          
    	</table>    
    	<br/>
    	
    	<table align="center">
    	<div id="editMode" class="buttonbottom" >
	    	<tr>
				<td><s:submit name="Modify" value="Modify" method="edit" cssClass="buttonsubmit" onclick="return validate();"/></td>
			    <td><input type="button" id="Close" value="Close"  onclick="javascript:window.close()" class="button"/></td>
			 </tr>
			 </div> 
		   </table>
		   </div>
		    <s:hidden  name="createdBy" value="%{createdBy.id}"/>
	        <s:hidden  name="createdDate" value="%{createdDate}"/>
	        </s:push>
	  </s:form>
	
	
  </body>
</html>
