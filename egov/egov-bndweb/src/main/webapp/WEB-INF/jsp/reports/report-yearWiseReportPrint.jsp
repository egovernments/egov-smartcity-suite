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
 
 <center>
<font face="Times New Roman" size="4">
<s:text  name="EfficiencyHeading1Loc"/></font><br>


<font face="Times New Roman" size="3"><s:text name="EfficiencyHeading2Loc"/></font><br>
			

<font face="Times New Roman" size="3"><s:text name="YearlyWiseHeading"/></font>

</center>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
	
	
	<tr>
	
	<td class="whitebox" >&nbsp;</td>
	<td class="whitebox" >&nbsp;</td>
	<td class="whitebox" >&nbsp;</td>
	<td class="whitebox" >&nbsp;</td>
	
	</tr>
	<tr>
	
	<td class="whitebox" >&nbsp;</td>
	<td class="whitebox" >&nbsp;</td>
	<td class="whitebox" >&nbsp;</td>
	<td class="whitebox" >&nbsp;</td>
	
	</tr>
	
	</table>
 <table width="100%" border="1" align="center" 
						 class="tablebottom">
						 
						 	<tr>
							<th class="whitebox" width="10%" align="center" colspan="2" rowspan="3">
								<s:text name="RegUnit" />
							</th>
							<th class="whitebox" width="10%" align="center" colspan="16">
								<s:text name="birth.lbl" />
							</th>  
							<th class="whitebox" width="10%" align="center" colspan="16">
								<s:text name="death.lbl" />
							</th>  						
							<th class="whitebox" width="10%" align="center" colspan="2" rowspan="2">
								<s:text name="stillBirth.lbl" />
							</th>  
							<th class="whitebox" width="10%" align="center" colspan="2" rowspan="2">
								<s:text name="InfantDeath.lbl" />
							</th> 
							<th class="whitebox" width="10%" align="center" colspan="2" rowspan="2">
								<s:text name="DeliveryDeath.lbl" />
							</th>  
							<th class="whitebox" width="10%" align="center" colspan="2" rowspan="2">
								<s:text name="MedicallyCertified.lbl" />
							</th>  
	 					</tr>

						<tr>
							
							<th class="whitebox" width="10%" align="center" colspan="8">
								<s:text name="Monthly.lbl" />
							</th>  
								 
							
							<th class="whitebox" width="10%" align="center" colspan="8">
								<s:text name="Progressive.lbl" />
							</th>  	
							
							
							<th class="whitebox" width="10%" align="center" colspan="8">
								<s:text name="Monthly.lbl" />
							</th>  
								 
							 
							
							<th class="whitebox" width="10%" align="center" colspan="8">
								<s:text name="Progressive.lbl" />
							</th>  	
							
							
	 					</tr>
	 					
	 					
	 					
	 					
	 					
						<tr>
							
							<th class="whitebox" width="10%" align="center" colspan="2">
								<s:text name="Male.lbl" />
							</th>  
							
							<th class="whitebox" width="10%" align="center" colspan="2">
								<s:text name="FeMale.lbl" />
							</th>  
								 
							<th class="whitebox" width="10%" align="center" colspan="4">
								<s:text name="Total.lbl" />
							</th> 
							
							<th class="whitebox" width="10%" align="center" colspan="2">
								<s:text name="Male.lbl" />
							</th> 

							<th class="whitebox" width="10%" align="center" colspan="2">
								<s:text name="FeMale.lbl" />
							</th> 	
							
							<th class="whitebox" width="10%" align="center" colspan="4">
								<s:text name="Total.lbl" />
							</th>  
							<th class="whitebox" width="10%" align="center" colspan="2">
								<s:text name="Male.lbl" />
							</th>  
							
							<th class="whitebox" width="10%" align="center" colspan="2">
								<s:text name="FeMale.lbl" />
							</th>  
								 
							<th class="whitebox" width="10%" align="center" colspan="4">
								<s:text name="Total.lbl" />
							</th> 
							
							<th class="whitebox" width="10%" align="center" colspan="2">
								<s:text name="Male.lbl" />
							</th> 

							<th class="whitebox" width="10%" align="center" colspan="2">
								<s:text name="FeMale.lbl" />
							</th> 	
							
							<th class="whitebox" width="10%" align="center" colspan="4">
								<s:text name="Total.lbl" />
							</th>  
							<th class="whitebox" width="10%" align="center" colspan="1">
								<s:text name="Monthly.lbl" />
							</th>  
							
							<th class="whitebox" width="10%" align="center" colspan="1">
								<s:text name="Progressive.lbl" />
							</th> 
							<th class="whitebox" width="10%" align="center" colspan="1">
								<s:text name="Monthly.lbl" />
							</th>  
							
							<th class="whitebox" width="10%" align="center" colspan="1">
								<s:text name="Progressive.lbl" />
							</th> 
							<th class="whitebox" width="10%" align="center" colspan="1">
								<s:text name="Monthly.lbl" />
							</th>  
							
							<th class="whitebox" width="10%" align="center" colspan="1">
								<s:text name="Progressive.lbl" />
							</th> 
							<th class="whitebox" width="10%" align="center" colspan="1">
								<s:text name="Monthly.lbl" />
							</th>  
							
							<th class="whitebox" width="10%" align="center" colspan="1">
								<s:text name="Progressive.lbl" />
							</th> 
														
	 					</tr>
	 					
	 					<s:iterator value="reportdetailsList" var="report">
	 						<tr>
	 						<td class="whitebox" width="10%" align="center" colspan="2">
								<s:property value="regUnit" />
							</td>
							<td class="whitebox" width="10%" align="center" colspan="2">
								<s:property value="birthMale" />
							</td>  
							
							<td class="whitebox" width="10%" align="center" colspan="2">
								<s:property value="birthFemale" />
							</td>  
								 
							<td class="whitebox" width="10%" align="center" colspan="4">
								<s:property value="birthTotal" />
							</td> 
							
							<td class="whitebox" width="10%" align="center" colspan="2">
								<s:property value="birthMaleprogressive" />
							</td> 

							<td class="whitebox" width="10%" align="center" colspan="2">
								<s:property value="birthFemaleprogressive" />
							</td> 	
							
							<td class="whitebox" width="10%" align="center" colspan="4">
								<s:property value="birthTotalprogressive" />
							</td>  
							<td class="whitebox" width="10%" align="center" colspan="2">
								<s:property value="deathMale" />
							</td>  
							
							<td class="whitebox" width="10%" align="center" colspan="2">
								<s:property value="deathFemale" />
							</td>  
								 
							<td class="whitebox" width="10%" align="center" colspan="4">
							<s:property value="deathTotal" />
							</td> 
							
							<td class="whitebox" width="10%" align="center" colspan="2">
								<s:property value="deathMaleprogressive" />
							</td> 

							<td class="whitebox" width="10%" align="center" colspan="2">
								<s:property value="deathFemaleprogressive" />
							</td> 	
							
							<td class="whitebox" width="10%" align="center" colspan="4">
								<s:property value="deathTotalprogressive" />
							</td>  
							<td class="whitebox" width="10%" align="center" colspan="1">
								<s:property value="stillbirth" />
							</td>  
							
							<td class="whitebox" width="10%" align="center" colspan="1">
								<s:property value="stillbirthprogressive" />
							</td> 
							<td class="whitebox" width="10%" align="center" colspan="1">
								<s:property value="infantdeath" />
							</td>  
							
							<td class="whitebox" width="10%" align="center" colspan="1">
								<s:property value="infantdeathprogressive" />
							</td> 
							<td class="whitebox" width="10%" align="center" colspan="1">
								<s:property value="deliverydeath" />
							</td>  
							
							<td class="whitebox" width="10%" align="center" colspan="1">
								<s:property value="deliverydeathprogressive" />
							</td> 
							<td class="whitebox" width="10%" align="center" colspan="1">
								<s:property value="certifieddeath" />
							</td>  
							
							<td class="whitebox" width="10%" align="center" colspan="1">
								<s:property value="certifieddeathprogressive" />
							</td> 
									
							</tr>
	 						</s:iterator>
	 							<s:iterator value="totalReportdetail" var="report1">
	 						<tr>
	 						<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:text name="Total.lbl" />
							</td>
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property value="birthMale" />
							</td>  
							
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property value="birthFemale" />
							</td>  
								 
							<td class="blueborderfortd" width="10%" align="center" colspan="4">
								<s:property value="birthTotal" />
							</td> 
							
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property value="birthMaleprogressive" />
							</td> 

							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property value="birthFemaleprogressive" />
							</td> 	
							
							<td class="blueborderfortd" width="10%" align="center" colspan="4">
								<s:property value="birthTotalprogressive" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property value="deathMale" />
							</td>  
							
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property value="deathFemale" />
							</td>  
								 
							<td class="blueborderfortd" width="10%" align="center" colspan="4">
							<s:property value="deathTotal" />
							</td> 
							
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property value="deathMaleprogressive" />
							</td> 

							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property value="deathFemaleprogressive" />
							</td> 	
							
							<td class="blueborderfortd" width="10%" align="center" colspan="4">
								<s:property value="deathTotalprogressive" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
								<s:property value="stillbirth" />
							</td>  
							
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
								<s:property value="stillbirthprogressive" />
							</td> 
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
								<s:property value="infantdeath" />
							</td>  
							
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
								<s:property value="infantdeathprogressive" />
							</td> 
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
								<s:property value="deliverydeath" />
							</td>  
							
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
								<s:property value="deliverydeathprogressive" />
							</td> 
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
								<s:property value="certifieddeath" />
							</td>  
							
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
								<s:property value="certifieddeathprogressive" />
							</td> 
									
							</tr>
	 						</s:iterator>
	 				
	 						
	 	</table>
	  <table width="100%" border="0" cellspacing="0" cellpadding="0">
	
	
	<tr>
	
	<td class="whitebox" >&nbsp;</td>
	<td class="whitebox" >&nbsp;</td>
	<td class="whitebox" >&nbsp;</td>
	<td class="whitebox" >&nbsp;</td>
	
	</tr>
		<tr>
	
	<td class="whitebox" >&nbsp;</td>
	<td class="whitebox" >&nbsp;</td>
	<td class="whitebox" >&nbsp;</td>
	<td class="whitebox" >&nbsp;</td>
	
	</tr>
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
		
		</tr>
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
