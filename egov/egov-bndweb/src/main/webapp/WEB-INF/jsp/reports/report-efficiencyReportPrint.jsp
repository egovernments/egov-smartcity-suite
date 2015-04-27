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
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>
<html>

<script>


function printpage(obj)
{
 	document.getElementById("print").style.display="none";
	window.print();
	document.getElementById("print").style.display="block";	  

} 

  </script>
  
  
<body>
<center><font face="Times New Roman" size="4">
<s:text  name="EfficiencyHeading1Loc"/></font><br>

<font face="Times New Roman" size="3"><s:text name="EfficiencyHeading2Loc"/></font>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
		
		<tr>
	
		<td class="whitebox" width="5%"/>
		
		<br><td>
	   <s:text name="EfficiencyHeading3Loc"/>	
		<br></td>
	</tr>
	
	<tr>
	
		<td class="whitebox" width="10%"/>
		
		<br><td>
	   <s:text name="EfficiencyHeading4Loc"/>	
		<br></td>
	</tr>
	
	<tr>
	
		<td class="whitebox" width="10%"/>
		
		<br><td>
	   <s:text name="EfficiencyHeading5Loc"/>	
		<br></td>
	</tr>
	
		<tr>
	
		<td class="whitebox" width="5%"/>
		
		<br><td><p>
	   <s:text name="EfficiencyHeading6Loc"/>	:- <s:text name="EfficiencyHeading7Loc"/>
		</p><br></td>
	
	
	</tr>
	
	<tr>
	
		<td class="whitebox" width="5%"/>
		
		<br><td>
	   <s:text name="EfficiencyHeading8Loc"/>,	
		<br></td>
	</tr>
	
	<tr>
	
		<td class="whitebox" width="10%"/>
		
		<br><td>
	   <s:text name="EfficiencyHeading9Loc"/>	
		<br></td>
	</tr>
		
	<tr>
	<td class="whitebox" >&nbsp;<br></td>
	</tr>
	<tr>
	<td class="whitebox" >&nbsp;<br></td>
	</tr>
			
		</table>

 <table width="50%" border="1" align="center" 
						 class="tablebottom">

						<tr id="1">
							
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
								<s:text name="No" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:text name="Name" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">Year-
								<s:text name="%{regYear}" />
							</td>  
	 					</tr>
	 					<tr id="2">
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
							
							</td>
							<td class="blueborderfortd" width="40%" align="left" colspan="2">
								<s:text name="" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property  value="%{}" />
							</td>  
							
	 					</tr>
	 					<tr id="3">
							
							<td class="blueborderfortd" width="10%" align="left" colspan="1">
								<s:text name="1" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:text  name="Birth Rate" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property  value="resultMap.birthrate" />
							</td>  
	 					</tr>
	 					<tr id="4">
								<td class="blueborderfortd" width="10%" align="left" colspan="1">
								<s:text name="2" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:text  name="Death Rate" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property  value="resultMap.deathrate" />
							</td>  
	 					</tr>
	 					<tr id="5">
								<td class="blueborderfortd" width="10%" align="left" colspan="1">
								<s:text name="3" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:text  name="Still Birth" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property  value="resultMap.stillbirthrate" />
							</td>  
	 					</tr>
	 					<tr id="6">
										<td class="blueborderfortd" width="10%" align="left" colspan="1">
								<s:text name="4" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:text  name="Infant Mortality Rate" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property  value="resultMap.infantmortalityrate" />
							</td>  
	 					</tr>
	 					<tr id="7">
							<td class="blueborderfortd" width="10%" align="left" colspan="1">
								<s:text name="5" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:text  name="Maternal Mortality Rate" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property  value="resultMap.meternalmortalityrate" />
							</td>  
	 					</tr>
	 					<tr id="8">
							<td class="blueborderfortd" width="10%" align="left" colspan="1">
								<s:text name="6" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:text  name="Growth Rate" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property  value="resultMap.growthrate" />
							</td>  
	 					</tr>
	 					</table>
	 					</td>
	 					</tr>

 <br/>
   <br/>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
	

	
		<td class="whitebox" width="70%"/>
	
		<br><td>
	 <s:text name="EfficiencyHeading10Loc"/>
	
		<br></td>
	</tr>
	

     <tr>
	
	<td class="whitebox" width="70%"/>
		<br><td>
	
	<s:text name="EfficiencyHeading11Loc"/>
	
	
		<br></td>
	</tr>  
	
		<tr>

	
	<td class="whitebox" width="70%"/>
		<br><td>
	
	   <s:text name="EfficiencyHeading12Loc"/>
	
	
		<br></td>
	</tr> 
	
		
      </table>
<table align="center">
     <tr id="print">
		     <td  style="border-right:0px;border-bottom: 0px"  colspan="2" align="center" height="20"> <p> 
		     <input type="button" id="print" name="printButton" onclick="printpage(this)" value="PRINT" />		   
		     </p></td>
		</tr>
</table>
</center>
</body>
</html>
