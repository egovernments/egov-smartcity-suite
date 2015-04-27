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
<title>Search By Receipt</title>
<head>
</head>
<SCRIPT type="text/javascript">

	function changeRegType(){		
		var type=document.getElementsByName('regType');
		for(var i=0 ; i < type.length; i++){		
			if(type[i].checked){
				document.getElementById('hiddenRegType').value=type[i].value;
			}
		}
	}
	
	function validateForm(){
		if(document.getElementById('receiptNo').value==null || document.getElementById('receiptNo').value==""){
			alert("Receipt Number Is Mandatory");
			document.getElementById("receiptNo").focus(); 
			return false;
		}
	
	}
	
	function generateBirthCert(Id){	
		
		window.open("${pageContext.request.contextPath}/registration/birthRegistration!print.action?idTemp="+Id,"simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
	  		
	}
	
	function generateDeathCert(Id){
			window.open("${pageContext.request.contextPath}/registration/deathRegistration!print.action?idTemp="+Id,"simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");

	}
	
	function generateNACert(Id){
	
			window.open("${pageContext.request.contextPath}/registration/nonAvailabilityRegistration!print.action?idTemp="+Id,"simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");

	}
	
	
</SCRIPT>
<body onload="changeRegType();">

	<s:form action="searchByReceipt" theme="simple" name="searchByReceiptForm">
	<s:hidden name="hiddenRegType" id="hiddenRegType"/>
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
			<tr>
				<td class="greybox" width="45%">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
			</tr>
			<tr>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">
					<s:radio list="registrationTypeList" value="%{regType}" name="regType" id="regType" onclick="changeRegType();" />
			
				</td>
				<td class="bluebox">&nbsp;</td>
			</tr>
			<tr>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
			</tr>
		</table>
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
			<tr>
				<td class="bluebox" width="30%">&nbsp;</td>
				<td class="bluebox" >&nbsp;</td>
				<td class="bluebox" >&nbsp;</td>
				<td class="bluebox" width="8%"><s:text name="receipt.number.lbl"/><span class="mandatory">*</span></td>
				<td class="bluebox" ><s:textfield id="receiptNo" name="receiptNo" value="%{receiptNo}" /></td>
				<td class="bluebox" >&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
			</tr>
			<tr>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
			</tr>
		</table>
		<div class="buttonbottom" align="center">
			<table>
				<tr>
					<td><s:submit cssClass="buttonsubmit" id="submit" name="submit" value="Search"  method="searchresults" onclick="return validateForm();" /></td>
			  		
			  		<td><input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();"/></td>
			  	</tr>
	        </table>
	   </div>
	
	<div id="tableData">
	   <div class="infostyle" id="search_error" style="display:none;"></div> 
		 <s:if test="%{searchMode=='result'}">
          		 <div id="displaytbl">	
          		     	 <display:table  name="searchResultList" export="false" requestURI="" id="regListid"  class="its" uid="currentRowObject" >
          			 	 <div STYLE="display: table-header-group" align="center">
          			 	  
 						 	<display:column title=" Sl No" style="text-align:center;"  >
 						 	 		<s:property value="%{#attr.currentRowObject_rowNum}"/>
 						 	</display:column>
						
							<s:if test="%{hiddenRegType==nonAvailability}">
								<display:column title="Registration No " style="text-align:center;" >	
	 						 	<a href="#" onclick="generateNACert('${currentRowObject.id}')">
	 						 		 ${currentRowObject.registrationNo}
	 						 	</a>
	 						 	</display:column>	
	 						 	
	 						 	<display:column title="Year" style="text-align:center;" property="yearOfEvent" >						
								</display:column>
							
								<display:column title="Event Type " style="text-align:center;" property="eventType">	 								
								</display:column>
								
								<display:column title="Citizen Name" style="text-align:center;" property="citizenName" >						
								</display:column>
							
								<display:column title="Applicant Name " style="text-align:center;" property="applicantName">	 								
								</display:column>
	 						 	
							</s:if>
							<s:elseif test="%{hiddenRegType==death}">								
															
	 						 	<display:column title="Registration No " style="text-align:center;" >	
	 						 	<a href="#" onclick="generateDeathCert('${currentRowObject.id}')">
	 						 		 ${currentRowObject.registrationNo}
	 						 	</a>
	 						 	</display:column>	
 						 					 								
								<display:column class="hidden" headerClass="hidden"  media="html">
	 						 		<s:hidden id="deathId" name="deathId" value="%{#attr.currentRowObject.id}" />						
								</display:column>
								
								<display:column title="Date of Death" style="text-align:center;" property="dateOfEvent" >						
								</display:column>
							
								<display:column title="Name of Person " style="text-align:center;" property="citizenName">	 								
								</display:column>
								
	 						 	<display:column title="Sex" style="text-align:center;" property="citizen.sex" >
 						 		</display:column>
 						 		
 						 		<display:column title="Place of Death " style="text-align:center;" property="placeOfEventAddress" >	 						 
	 						 	</display:column>
	 						 		 						 	
	 						 	<display:column title="Registration Unit" style="text-align:center;" property="registrationUnit.regUnitDesc" >
 						 		</display:column>
 							
							</s:elseif>												
							<s:else>	
																				
								<display:column title="Registration No " style="text-align:center;" >	
	 						 	<a href="#" onclick="generateBirthCert('${currentRowObject.id}')">
	 						 		 ${currentRowObject.registrationNo}
	 						 	</a>
	 						 		
 						 		</display:column>
								
								<display:column class="hidden" headerClass="hidden"  media="html">
	 						 		<s:hidden id="birthId" name="birthId" value="%{#attr.currentRowObject.id}" />						
								</display:column>
							
								<display:column title="Date of Birth" style="text-align:center;" property="dateOfEvent" >						
								</display:column>
							
								<display:column title="Name of Child " style="text-align:center;" property="citizenName">	 								
								</display:column>
									 
								<display:column title="Sex" style="text-align:center;" property="citizen.sex" >
 						 		</display:column>
 						 		
								<display:column title="Father Name " style="text-align:center;" property="fatherFullName" >	 						 	
	 						 	</display:column>
	 						 		
	 						 	<display:column title="Mother Name " style="text-align:center;" property="motherFullName" >	 						   	
	 						 	</display:column>	 
							 						 	
	 						 	<display:column title="Place of birth " style="text-align:center;" property="placeOfEventAddress" >	 						 
	 						 	</display:column>
	 						 	
	 						 	<display:column title="Registration Unit" style="text-align:center;" property="registrationUnit.regUnitDesc" >
 						 		</display:column> 						 		
	 						 								
							</s:else>
								  			 						 
						</div>
						</display:table>
					</div>
  	   			</s:if>
  	     </div>
	
	</s:form>

</body>

</html>
