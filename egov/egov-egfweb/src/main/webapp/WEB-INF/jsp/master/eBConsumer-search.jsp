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
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

<%@ page language="java"%>

<html>
  <head>
    <title>
    	<s:text name="eBConsumer.search.title"/> 
    </title>
	<style type="text/css">
	#codescontainer {position:absolute;left:11em;width:9%;text-align: left;}
	#codescontainer .yui-ac-content {position:absolute;width:600px;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
	#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:300px;background:#a0a0a0;z-index:9049;}
	#codescontainer ul {padding:5px 0;width:100%;}
	#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
	#codescontainer li.yui-ac-highlight {background:#ff0;}
	#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
	</style>
<script type="text/javascript" src="/EGF/commonjs/ajaxCommonFunctions.js"></script>
<script type="text/javascript" src="/EGF/resources/javascript/calender.js"></script>
<script type="text/javascript" src="/EGF/resources/javascript/calendar.js" ></script>
<script type="text/javascript" src="/EGF/javascript/dateValidation.js"></script>
<script type="text/javascript" src="/EGF/resources/javascript/ebConsumerHelper.js"></script>
  </head>
  
  <body >
   <s:form name="eBConsumerForm" action="eBConsumer" theme="simple" id ="eBConsumerForm" validate="true">
   <div id="loading" style="position:absolute; left:25%; top:70%; padding:2px; z-index:20001; height:auto;width:500px;display: none;">
    <div class="loading-indicator" style="background:white;  color:#444; font:bold 13px tohoma,arial,helvetica; padding:10px; margin:0; height:auto;">
        <img src="/egi/resources/erp2/images/loading.gif" width="32" height="32" style="margin-right:8px;vertical-align:top;"/> Loading...
    </div>
</div>
    <div class="formmainbox"><div class="subheadnew"><s:text name="eBConsumer.search.title"/></div>
    
    <s:hidden name="mode" id="mode" value="%{mode}" />
  		<table width="100%" border="0" cellspacing="0" cellpadding="0">                   
    		<tr>
					<td class="greybox" width="10%" ><s:text name="eBConsumer.ConsumerNumber"/></td>
				    <td class="greybox" width="30%" ><s:textfield id="code" name="code" value="%{code}" autocomplete="off"  onfocus='autocompleteConsumerNumbers(this);' onblur='splitConsumerNumber(this);' /></td>
				                       
				    <td class="greybox" width="10%"><s:text name="eBConsumer.accountNumber"/></td>
				    <td class="greybox"  width="30%"><s:textfield id="name" name="name" value="%{name}" autocomplete="off"  onfocus='autocompleteAccountNumbers(this);' onblur='splitAccountNumber(this);' /></td>
			</tr>
			<tr>
			        <td class="bluebox"><s:text name="eBConsumer.region"/></td>
				    <td class="bluebox">
					<s:select name="region" id="region" list="dropdownData.regionsList"  headerKey="" headerValue="----Choose----" />
					</td>
					<td class="bluebox"><s:text name="eBConsumer.targetArea"/></td>
				    <td class="bluebox">
					<s:select name="targetArea" id="targetArea" list="dropdownData.targetAreaDropDownList" listKey="name" listValue="name" headerKey="" headerValue="----Choose----"  value="targetArea" />
					</td>
					
			</tr>
			<tr>
					<td class="greybox"><s:text name="eBConsumer.ward"/></td>
				    <td class="greybox">
					<s:select name="ward" id="ward" list="dropdownData.wardsList" listKey="id" listValue="name" headerKey="" headerValue="----Choose----" value="%{ward.id}"/>
					</td>
					<td class="greybox"><s:text name="eBConsumer.oddOrEvenBilling"/></td>
				    <td class="greybox">
					 <s:select name="oddOrEvenBilling" id="oddOrEvenBilling" list="dropdownData.billingList"  headerKey="" headerValue="----Choose----" />
					</td>				    
		    </tr>
			        
    	</table>    
    	<br/>            
    	<div class="buttonbottom" >
    	<table align="center">  
    	 <tr>  
    	    <td><s:submit cssClass="buttonsubmit" value="Search" method="Search" onclick="return validateSearch();" /></td>
		    <td><input type="button" id="Close" value="Close"  onclick="javascript:window.close()" class="button"/></td>
	  </table>
	  </div>
	  		<div>
			<jsp:include page="eBConsumer-results.jsp"/> 
			</div>
			
	  </s:form>
	  
	 <div id="codescontainer"/>
  </body>
</html>
