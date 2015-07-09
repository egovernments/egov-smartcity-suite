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
<% response.setHeader("Cache-Control","no-cache"); //HTTP 1.1 
response.setHeader("Pragma","no-cache"); //HTTP 1.0 
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server %>

<html>
  <head>
    <title><s:text name="paymentDetail.header"/></title>
    <jsp:include page='/WEB-INF/jsp/bill/feeCollection.jsp'/>
    
  </head>
  
<body onload="calculateTotal();resetCertificateType();">
<div class="errorstyle" id="feeCollection_error" style="display: none;">
</div>
 <s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />	
			<s:fielderror />
		</div>
	</s:if>
	<s:if test="%{hasActionMessages()}">
		<div class="errorstyle">
			<s:actionmessage />
		</div>
	</s:if>
 <s:form action="feeCollection" theme="simple" name="feeCollection" onsubmit="validateForm();">
	<div class="formheading"/></div>
	<s:token/>
	 <s:push value="model">
	 	<s:hidden id="createdBy" name="createdBy" value="%{createdBy.id}"/>
   	 	<s:hidden id="egBills" name="egBills" value="%{egBills.id}"/>
   	 	<s:hidden id="reportId" name="reportId" value="%{reportId}"/>
   	 	<s:hidden id="statusType" name="statusType" value="%{statusType.id}"/>
   	 		
   	 	<s:hidden id="regType" name="regType" value="%{regType}"/>
     	<s:hidden id="mode" name="mode" value="%{mode}"/>
     	<s:hidden id="isFreeCertificate" name="isFreeCertificate" value="%{isFreeCertificate}"/>
     	<s:hidden id="id" name="id" value="%{id}"/>
     	<s:hidden id="collectionDate" name="collectionDate" value="%{collectionDate}"/>
     	
     	<div class="blueshadow"></div>
     	<table width="80%" align="center"   border="0" cellspacing="0" cellpadding="0" class="tablebottom" border="1">
       
	      <tr>
	 		 <td  class="bluebox" align="center"  height="5" width="90%" colSpan="6">
	 		 <h1 class="subhead" align="center" ><s:text name="applicantDetails.label"/></h1></td>
	   			
	     </tr>
	     <tr>
	 			<td width="10%" class="greybox"><s:text name="applicantName.label"/><span class="mandatory">*</span> </td>
	   			<td class="greybox" colSpan="5">
	   				<s:textfield name="applicantName" id="applicantName" value="%{applicantName}"  maxlength="150" size="100" onblur="return checkSpecialCharsInName(this);"/>
	   	 </tr>
	      <tr>
	 			<td width="10%" class="bluebox"><s:text name="applicantAddress.label"/></td>
	   			<td class="bluebox" colSpan="5"> <s:textfield name="applicantAddress" id="applicantAddress" value="%{applicantAddress}"  maxlength="150" size="100" />
	   			</td>
	     </tr>
	        <tr>
	 			<td width="10%" class="greybox"><s:text name="remarks.label"/></td>
	   			<td class="greybox" colSpan="5">
	   							<s:textarea name="remarks" id="remarks" value="%{remarks}" rows="2" cols="40" />
	   			</td>					
	   	 </tr>
	   	   <tr>
	 			<td width="10%" class="greybox"><s:text name="numberOfcopies.label"/></td>
	   			<td class="greybox" colSpan="5">
	   				 <s:textfield name="no_Of_copies" id="no_Of_copies" value="%{no_Of_copies}"  maxlength="2" size="5"  onblur="onChangeOfNoOfCopies(this);calculateTotal();" /></td>	
	   	 </tr>
	   	 <tr>
	 			<td width="10%" class="greybox"><s:text name="freeCopyOrPaidCopy.label"/></td>
	   			<td class="greybox" colSpan="5">
	   				<s:radio list="trsancationTypesList" id="transType" name="transType" value="%{transType}" onclick="onChangeOfCertificateType()"></s:radio>
	   				</td>	
	   	 </tr>
	   	   
	   </table>
	  
	 		<div align="center"> <jsp:include page='/WEB-INF/jsp/bill/feeCollection-amountDetails.jsp'/></div>	
	   
	   
     
		
     </s:push>
 </s:form>
</body>
</html>
