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
<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sx" uri="/WEB-INF/struts-dojo-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

<%@ page language="java"%>
<html>
  <head>
    
    <title>
    	<s:text name="targetArea.view.title"/> 
    </title>
    <sx:head/>
  </head>
  <body>        
   <s:form name="targetAreaForm" action="targetArea" theme="css_xhtml" validate="true">
  
    <div  class="formmainbox"><div id="viewhead" class="subheadnew"><s:text name="targetArea.view.title"/></div>
    		<div style="color: red">
		<s:actionerror/>  
		</div>
		<div style="color: green">
		
		<s:actionmessage />
		</div>
   		<s:hidden name="mode" id="mode" value="%{mode}" />
	    <s:hidden name="id" id="id" value="%{id}" />
     	<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">  
     	<br/>                  
    		<tr>
					<td class="greybox"><s:text name="targetArea.code"/>:</td>
				    <td class="greybox"><s:property value="%{model.code}" /></td>
				    <td class="greybox"><s:text name="targetArea.name"/>:</td>
				    <td class="greybox"><s:property value="%{model.name}" /></td>
			</tr>
			<tr></tr>
			<tr></tr>
			<tr></tr>
			<tr>
			        <td class="bluebox"><s:text name="targetArea.assistant.engineer"/>:</td>
				    <td class="bluebox"><s:property value="%{model.position.name}" /></td>
				    
			</tr>
			</table>
			
			
         <s:if test="%{targetAreaMappingsList.size()>0}">
         <table width="80%" align="center" ><th><s:text name="targetArea.ward.details"/></th></table>
         <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom">
			<tr>
				<th class="bluebgheadtd">Sl No</th>
				<th class="bluebgheadtd"><s:text name="targetArea.ward.name"/></th> 
		  </tr>
		  <s:iterator value="targetAreaMappingsList" status="stat" var="targetArea">
			<tr>
				<td class="blueborderfortd"><div align="left"><s:property value="#stat.index+1"/>&nbsp;</div></td>
				<td class="blueborderfortd"><div align="left"><s:property value="boundary.name"/>&nbsp;</div></td>
			</tr>
		  </s:iterator>
      
	     </table>
         </s:if>
         <s:else>
             <b>No Ward Details found</b> 
         </s:else>
    	</div>
    	</s:form>    
    	<br/>
	  <div id="viewMode" class="buttonbottom" >	
		<table table align="center">  
	    	 <tr>
				<td><input type="button" name="close" id="Close" value="Close"  onclick="javascript:window.close()" class="button"/></td>
			 </tr>
		</table> 
	 </div>  
	   
  </body>
</html>
