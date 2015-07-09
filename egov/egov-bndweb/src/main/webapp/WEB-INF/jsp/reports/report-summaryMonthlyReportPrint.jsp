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


 <table width="100%" border="0" class="tableStyle"
							style="width: 60%; border: 2px solid;" align="center">

							<tr align="center" >
								<td class="whitebox" width="8%" align="center" />
									<br>
								<td>
									<b><s:text name="form12" /> </b>
									<br>
								</td>
							</tr>
							<tr align="center" >
								<td class="whitebox" width="8%" align="center" />
									<br>
								<td>
										<s:text name="summonBirthmonthly" /> 
									<br>
								</td>
							</tr>
								<tr align="center" >
								<td class="whitebox" width="8%" align="center" />
									<br>
								<td>
										<b><s:property value="%{regType}"/> Certificate</b>
									<br>
								</td>
							</tr>

                        </table >
                        <table width="100%" border="0" class="tableStyle" style="width: 60%; border: 1px solid;" align="center">
							
										<tr>
								<td class="whitebox">
									&nbsp;
									<br>
								</td>
							</tr>
							
							<tr>
								<td class="whitebox">
									&nbsp;
									<br>
								</td>
							</tr>

							<tr align="">
								<td class="whitebox" width="30%"></td>
								<td class="whitebox" width="30%"><s:text name ="Report for the Month - Year"/></td>
								<td><s:property value="%{resultMap.Duration}" /></td>
							</tr>
							
							<tr align="">
								<td class="whitebox" width="30%"></td>
								<td class="whitebox" width="30%"><s:text name ="Registration Unit: "/></td>
								<td><s:property value="%{regUnit.regUnitDesc}" /></td>
							</tr>
							
							<tr align="">
								<td class="whitebox" width="30%"></td>
								<td class="whitebox" width="30%"><s:text name ="Registration Unit Address "/></td>
								<td><s:property value="%{regUnit.regUnitAddress}" /></td>
							</tr>

                             <tr>
								<td class="whitebox" width="30%"/>
									
								<td class="whitebox" width="30%"> Number of <s:property value="%{regType}" />s Registered </td>
							
								<td><s:property value="%{resultMap.Total}"/></td>
							</tr>

							<tr>
								<td class="whitebox" width="30%"/>									
								<td class="whitebox" width="30%"> <s:text name ="Within one year of occurence -"/></td>
								<td><s:property value="%{resultMap.numberofcurrentRegistrations}" /></td>
							</tr>

							<tr>
								<td class="whitebox" width="30%"/>
									
								<td class="whitebox" width="30%"><s:text name ="After one year of occurence -"/></td>
								<td><s:property value="%{resultMap.numberofdelayedRegistrations}" /></td>
							</tr>
							
							<tr>
								<td class="whitebox">
									&nbsp;
									<br>
								</td>
							</tr>
							
						</table>
	 					</td>
	 					</tr>

 <br/>
   <br/>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
	
	<td class="whitebox" width="20%"/>
	
		<td>
	 <s:text name="Date:"/> <s:property value="%{todaysDate}"/>	
		</td>

	
		<td class="whitebox" width="30%"/>
	
		<br><td>
	 <s:text name="Signature and Seal"/>
	
		<br></td>
	</tr>
	

     <tr>
	
	<td class="whitebox" width="20%"/>
	
		<br><td>
	 <s:text name=""/>
	
		<br></td>

	
		<td class="whitebox" width="30%"/>
	
		<br><td>
	   <s:property value="%{}"/>
	
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
</body>
</html>
