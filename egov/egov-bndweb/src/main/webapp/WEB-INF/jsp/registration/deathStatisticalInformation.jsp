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

   <div class="blueshadow"></div>
     	<h1 class="subhead" ><s:text name="regform.heading.part2"/></h1>
     	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" >
     	 <tr>
	 			<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="25%">&nbsp;</td>
	   			<td class="bluebox" width="25%">&nbsp;</td>
	   			<td class="bluebox" width="30%">&nbsp;</td>
	     </tr>
	     <tr>
	            <td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="deceased.residence.lbl"/></td>
	   			<td class="bluebox" colspan="2" >
	   			<s:hidden name="deceasedUsualAddress.addressID" id="deceasedUsualAddress.addressID" value="%{deceasedUsualAddress.addressID}"/>
	   				<s:text name="town.vill.lbl"/>:<s:textfield name="deceasedUsualAddress.cityTownVillage" id="deceasedUsualAddress.cityTownVillage" value="%{deceasedUsualAddress.cityTownVillage}" maxlength="512"/>
	   				<s:text name="district.lbl"/>:&nbsp;&nbsp;<s:textfield name="deceasedUsualAddress.district" id="deceasedUsualAddress.district" value="%{deceasedUsualAddress.district}" maxlength="512"  /><br><br/>
	   				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:text name="state.lbl"/>:&nbsp;&nbsp;&nbsp;
					<s:select name="deceasedUsualAddress.state" id="deceasedUsualAddress.state" list="dropdownData.stateList" value="%{deceasedUsualAddress.state}" listKey="id" listValue="name" headerKey="-1" headerValue="---choose---"/>
	   			</td>
				
	     </tr>
     	 <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="religion.deceased"/></td>
	   			<td class="bluebox"><s:select id="religion" name="citizenBDDetails.religion" list="dropdownData.religionList" listKey="id" listValue="religionName" headerKey="-1" headerValue="-----choose----" value="%{citizenBDDetails.religion.id}"/></td>
	   			<td class="bluebox">&nbsp;</td>
	     </tr>
	     <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="citizen.level.education"/></td>
	   			<td class="bluebox"><s:select id="citizenOccupation" name="citizenBDDetails.occupation" list="dropdownData.occupMasterList" listKey="id" listValue="occupName" headerKey="-1" headerValue="-----choose----"  value="%{citizenBDDetails.occupation.id}"/></td>
	   			<td class="bluebox">&nbsp;</td>
	   	</tr>
	  
	

	   	  <tr>
	   	        <td class="bluebox">&nbsp;</td>
	   			<td class="bluebox"><s:text name="type.attention.death"/></td>
				<td class="bluebox"><s:select id="typeMedAttention" name="typeMedAttention" list="dropdownData.attentionDeathList" listKey="id" listValue="attentionDesc" value="%{typeMedAttention.id}" headerKey="-1" headerValue="-----choose----"/></td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
	    
	   	<tr>
	   	        <td class="bluebox">&nbsp;</td>
	   			<td class="bluebox"><s:text name="certified.death"/></td>
				<td class="bluebox"><s:select id="deathCertified" name="deathCertified" list="decisionMap"  value="%{isDeathCertified()}" headerKey="-1" headerValue="-----choose----"/></td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
	      
	        <tr>
	 			<td class="greybox">&nbsp;</td>
				<td class="greybox"><b><s:text name="disease.name.lbl"/></b></td>
	   			<td class="greybox" colspan="4">&nbsp;</td>
			   </tr>  
	      
	          <tr>
	 			<td class="bluebox">&nbsp;</td>
	 			<td class="bluebox"  width="2%"><s:text name="main.category"/></td>
			   	<td class="bluebox" width="26%" ><s:select  id="causeOfDeathParent" name="causeOfDeathParent" value="%{causeOfDeathParent.id}" list="dropdownData.deathCauseList" listKey="id" listValue="desc" headerKey="-1" headerValue="----Choose----" onchange="populatedeathcause(this);"/>
	            <egov:ajaxdropdown fields="['Text','Value']" url="common/ajaxCommon!getDeathcauseByParent.action" id="causeOfDeath" dropdownId="causeOfDeath"/>
	            </td>
	            <td class="bluebox">&nbsp;</td>
	           </td>
	         
	           </tr>
			   
			   <tr>
	 			<td class="bluebox">&nbsp;</td>
	 			<td class="bluebox" width="2%"><s:text name="sub.category"/></td>
			   	<td class="bluebox" width="26%" ><s:select  id="causeOfDeath" name="citizenBDDetails.causeOfDeath" list="dropdownData.diseaseList" value="%{citizenBDDetails.causeOfDeath.id}" listKey="id" listValue="desc" headerKey="-1" headerValue="----Choose----" />
	            </td>
	            <td class="bluebox">&nbsp;</td>
	           </td>
	          
	           </tr>
	           
	          
	          	          
	            <tr>
	 			<td class="bluebox">&nbsp;</td>
	 			<td class="bluebox" width="10%" ><s:text name="death.pregnant.lbl"/></td>
			   	<td class="bluebox" width="26%" ><s:select  id="pregnancyRelated" name="pregnancyRelated"  value="%{isPregnancyRelated()}" list="decisionMap" headerKey="-1" headerValue="----Choose----" />	                      
	           </td>	          
	 			<td class="bluebox">&nbsp;</td>
	           </tr>
	           
	             <tr>
	 			<td class="greybox">&nbsp;</td>
	 			<td class="greybox" width="10%" ><b><s:text name="addicted.by.lbl"/></b></td>
	 			<td class="greybox">&nbsp;</td>
	 			<td class="greybox">&nbsp;</td>
	             </tr>
	                 <s:iterator value="addictionList" status="stat" var="addition"> 
	                  <tr>
	                  <td class="bluebox"  width="5%"></td>
		  	          <td class="bluebox" width="5%"><s:property value="%{desc}" /></td> 
		  	           <td class="bluebox"  width="10%"><s:textfield id="noOfYears[%{#stat.index}]" name="addictionList[%{#stat.index}].noOfYears" maxlength="2"/></td>                                    
                       <s:hidden id="addictionId" name="addictionList[%{#stat.index}].id" />
                       <s:hidden id="addictiondesc" name="addictionList[%{#stat.index}].desc" />
                       <td class="bluebox">&nbsp;</td>
                     </tr>                  
              		  </s:iterator>    
               
			   
     	</table>
  
