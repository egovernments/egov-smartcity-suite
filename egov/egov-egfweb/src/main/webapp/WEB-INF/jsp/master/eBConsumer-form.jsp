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

	   <div style="color: red">            
		<s:actionerror/>  
		</div>
		<div style="color: green">
		<s:actionmessage />
		</div>
	    <div class="errorstyle" style="display:none" id="uniquecode" >
	         <s:text name="eBConsumer.consumerNumber.already.exists"/>
	    </div>
	    <div class="errorstyle" style="display:none" id="uniquename" >
	         <s:text name="eBConsumer.accountNumber.already.exists"/>
	    </div>
	    <s:hidden name="id" id="id" value="%{id}" />
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">                   
    		<tr>
					<td class="greybox" width="10%" ><s:text name="eBConsumer.ConsumerNumber"/><span class="mandatory">*</span></td>
				    <td class="greybox" width="30%" ><s:textfield id="code" name="code" value="%{code}" onchange="checkuniquenessconsumerno();"/></td>
				     <egov:uniquecheck id="uniquecode" name="uniquecode" fieldtoreset="code" fields="['Value']" url='/master/eBConsumer!isCodeUnique.action'/>
				                       
				    <td class="greybox" width="10%"><s:text name="eBConsumer.accountNumber"/><span class="mandatory">*</span></td>
				    <td class="greybox"  width="30%"><s:textfield id="name" name="name" value="%{name}"  onchange="checkuniquenessaccountno();"/></td>
				    <egov:uniquecheck id="uniquename" name="uniquename" fieldtoreset="name" fields="['Value']" url='/master/eBConsumer!isNameUnique.action'/>
			</tr>
			<tr>
			        <td class="bluebox"><s:text name="eBConsumer.region"/></td>
				    <td class="bluebox">
					<s:select name="region" id="region" list="dropdownData.regionsList"  headerKey="" headerValue="----Choose----" />
					</td>
					<td class="bluebox"><s:text name="eBConsumer.oddOrEvenBilling"/></td>
				    <td class="bluebox">
				    <s:select name="oddOrEvenBilling" id="oddOrEvenBilling" list="dropdownData.billingList"  headerKey="" headerValue="----Choose----" />
					</td>
					
			</tr>
			<tr>
			
					<td class="greybox"><s:text name="eBConsumer.ward"/></td>
				    <td class="greybox">
					<s:select name="ward" id="ward" list="dropdownData.wardsList" listKey="id" listValue="name" headerKey="" headerValue="----Choose----"  onChange="setTargetArea();" value="%{ward.id}"/>
					</td>
					<td class="greybox"><s:text name="eBConsumer.targetArea"/></td>
					<td class="greybox"><s:textfield id="targetArea" name="targetArea" value="%{targetArea}" readonly="true"/></td>	
					
					 
		    </tr>
			<tr>
					<td class="bluebox"><s:text name="eBConsumer.location"/></td>
				    <td class="bluebox"><s:textfield id="location" name="location" style="width:250px" value="%{location}"  /></td>
				    
					<td class="bluebox"><s:text name="eBConsumer.isActive"/></td>
					<td class="bluebox"><s:checkbox id="isActive" name="isActive" value="%{isActive}"/> </td>			
			</tr>  
			<tr>
			
					<td class="greybox"><s:text name="eBConsumer.address" /></td>
					<td class="greybox" colspan="3"><s:textarea  id="address" name="address" style="width:470px"/></td>
			</tr>          
    	</table>    
