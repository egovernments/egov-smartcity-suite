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
<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java"%>
<html>
  <head>
    
    <title>
    	<s:text name="eBConsumer.edit.title"/> 
    </title>
    <sx:head/>
  </head>
  <script type="text/javascript" src="/EGF/resources/javascript/ebConsumerHelper.js"></script>
  <body onload = "validateCodeAndName('<s:property value="%{hasValidEBDetails}"/>');">
   <s:form name="eBConsumerForm" id="eBConsumerForm" action="eBConsumer" theme="css_xhtml" validate="true" >
   <s:push value="model">
    <div  class="formmainbox"><div  class="subheadnew"><s:text name="eBConsumer.edit.title"/></div>
    <s:token/>
    <s:hidden name="mode" id="mode" value="%{mode}" />
   	<s:hidden name="check" id="check" value="%{check}" />
    	 <%@ include file='eBConsumer-form.jsp'%>   
    	<br/>
    	
    	<table align="center">
    	<div id="editMode" class="buttonbottom" >
	    	<tr>
	    	<s:if test="%{mode != 'modify'}">
				<td><s:submit name="Modify" value="Modify" method="edit" cssClass="buttonsubmit" onclick="return validate();"/></td>
			</s:if>
			    <td><input type="button" id="Close" value="Close"  onclick="javascript:window.close()" class="button"/></td>
			 </tr>
			 </div> 
		   </table>
		   </div>
	        </s:push>
	  </s:form>
	
	
  </body>
</html>
