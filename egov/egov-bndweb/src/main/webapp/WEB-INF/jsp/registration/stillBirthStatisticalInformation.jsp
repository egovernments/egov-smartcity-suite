#-------------------------------------------------------------------------------
# /*******************************************************************************
#  *    eGov suite of products aim to improve the internal efficiency,transparency,
#  *    accountability and the service delivery of the government  organizations.
#  *
#  *     Copyright (C) <2015>  eGovernments Foundation
#  *
#  *     The updated version of eGov suite of products as by eGovernments Foundation
#  *     is available at http://www.egovernments.org
#  *
#  *     This program is free software: you can redistribute it and/or modify
#  *     it under the terms of the GNU General Public License as published by
#  *     the Free Software Foundation, either version 3 of the License, or
#  *     any later version.
#  *
#  *     This program is distributed in the hope that it will be useful,
#  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
#  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  *     GNU General Public License for more details.
#  *
#  *     You should have received a copy of the GNU General Public License
#  *     along with this program. If not, see http://www.gnu.org/licenses/ or
#  *     http://www.gnu.org/licenses/gpl.html .
#  *
#  *     In addition to the terms of the GPL license to be adhered to in using this
#  *     program, the following additional terms are to be complied with:
#  *
#  * 	1) All versions of this program, verbatim or modified must carry this
#  * 	   Legal Notice.
#  *
#  * 	2) Any misrepresentation of the origin of the material is prohibited. It
#  * 	   is required that all modified versions of this material be marked in
#  * 	   reasonable ways as different from the original version.
#  *
#  * 	3) This license does not grant any rights to any user of the program
#  * 	   with regards to rights under trademark law for use of the trade names
#  * 	   or trademarks of eGovernments Foundation.
#  *
#  *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#  ******************************************************************************/
#-------------------------------------------------------------------------------
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>


   <div class="blueshadow" ></div>
     	<h1 class="subhead" ><s:text name="regform.heading.part2"/></h1>
     	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" >
     	   <tr>
	 			<td class="bluebox" width="20%"></td>
				<td class="bluebox" width="25%"></td>
	   			<td class="bluebox" width="25%"></td>
	   			<td class="bluebox" width="30%"></td>
	     </tr>
	     <tr>
	            <td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="25%"><s:text name="mother.residence.lbl"/></td>
	   			<td class="bluebox" colspan="2" >
	   			    <s:hidden id="motherResidenceAddress" name="motherResidenceAddress.addressID" value="%{motherResidenceAddress.addressID}" />
	   				<s:text name="town.vill.lbl"/>:<s:textfield id="motherResidenceAddress.cityTownVillage" name="motherResidenceAddress.cityTownVillage"/>
	   				<s:text name="district.lbl"/>:<s:textfield id="motherResidenceAddress.district" name="motherResidenceAddress.district" value="%{motherResidenceAddress.district}"/><br><br/>
	   				<s:text name="state.lbl"/>:&nbsp;&nbsp;&nbsp;
					<s:select id="motherResidenceAddress.state" name="motherResidenceAddress.state" list="dropdownData.stateList" listKey="id" listValue="name" headerKey="-1" headerValue="-----choose----"/></td>
	   			</td>
				
	     </tr>
	     <tr>
	   	        <td class="greybox">&nbsp;</td>
	   			<td class="greybox"><s:text name="mother.age.at.delivery"/></td>
				<td class="greybox"><s:select id="ageMomBirth" name="citizenBDDetails.ageMomBirth" list="motherAgeList" headerKey="-1" headerValue="-----choose----"/></td>
				<td class="greybox">&nbsp;</td>
	    </tr>
     	<tr>
	   			<td class="bluebox">&nbsp;</td>
	   			<td class="bluebox"><s:text name="mothers.level.education"/></td>
				<td class="bluebox">
					<s:select id="motherEducation" name="citizenBDDetails.motherEducation" list="dropdownData.educationMasterList" listKey="id" listValue="eduDesc" 
						headerKey="-1" headerValue="-----choose----" value="%{citizenBDDetails.motherEducation.id}"/>
				</td>
				<td class="bluebox">&nbsp;</td>
	     </tr>
	     
	      <tr>
	   	        <td class="greybox">&nbsp;</td>
	   			<td class="greybox"><s:text name="type.attention.delivery"/></td>
				<td class="greybox"><s:select id="typeAttention" name="typeAttention" list="dropdownData.attentionList" listKey="id" listValue="attentionDesc" value="%{typeAttention.id}" headerKey="-1" headerValue="-----choose----"/></td>
				<td class="greybox">&nbsp;</td>
	     </tr>
	     <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="pregnancy.duration.lbl"/></td>
	   			<td class="bluebox">
	   				<s:select id="pregDuration" name="pregDuration" list="pregDurationList" headerKey="-1" headerValue="-----choose----"/>
	   			    &nbsp;<font color="blue"><s:text name="weeks.lbl"/></font>
	   			</td>
	   			<td class="bluebox">&nbsp;</td>
	   	  </tr>
	   	  
	   	   <tr>
	 			<td class="greybox">&nbsp;</td>
	 			<td class="greybox"><s:text name="cause.death.lbl"/></td>
			   	<td class="greybox">
			   	    <s:text name="main.category"/>
			   		<s:select  id="causeOfDeathParent" name="causeOfDeathParent" list="dropdownData.deathCauseList" listKey="id" listValue="desc" 
			   			headerKey="-1" headerValue="----Choose----" onchange="populatedeathcause(this);" value="%{causeOfDeathParent.id}"/>
	            	<egov:ajaxdropdown fields="['Text','Value']" url="common/ajaxCommon!getDeathcauseByParent.action" id="causeOfDeath" dropdownId="causeOfDeath"/>
	            	<br/>
	            	<s:text name="sub.category"/>
	            	<s:select  id="causeOfDeath" name="citizenBDDetails.causeOfDeath" list="dropdownData.diseaseList" value="%{citizenBDDetails.causeOfDeath.id}" listKey="id" listValue="desc" headerKey="-1" headerValue="----Choose----" />
	            </td>
	            <td class="greybox">&nbsp;</td>
	           </td>
           </tr>
	    	        
     	</table>
  
