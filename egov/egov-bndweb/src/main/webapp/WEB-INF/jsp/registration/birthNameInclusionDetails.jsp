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
 

<div class="errorstyle" id="nameinclusion_error" style="display: none;">
</div>


<s:if test="%{hasErrors()}">
<div class="errorstyle" id="nameinclusionreceipt_error">
<s:actionerror />
</div>
</s:if>

<div class="errorstyle" style="display:none" id="nameInclusionReceiptCheck">
			<s:text name="invalid.receipt.number"/>				
</div>
 
   <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
   
    <tr>
	 			<td class="greybox">&nbsp;</td>
				<td class="greybox"><b><s:text name="child.details.lb"/>:</b></td>
	   			<td class="greybox" colspan="4">&nbsp;</td>
	   				<s:hidden name="updateNameFlag" id="updateNameFlag" value="%{updateNameFlag}"/>
	   				<s:hidden name="nameChange.citizen" id="nameChange.citizen" value="%{nameChange.citizen}"/>
	   					<s:hidden name="receiptNoFlag" id="receiptNoFlag" value="%{receiptNoFlag}"/>
		 </tr> 
		 
		  <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="receipt.number"/>
				<s:if test="%{receiptNoFlag==true}"> 
				<span class="mandatory">*</span>
				</s:if>
				<br/></td>
				
				<td class="bluebox">
				<s:textfield name="nameChange.receiptNo" id="nameChange.receiptNo" value="%{nameChange.receiptNo}" onblur="return isvalidReceiptNumbers();"/><br/>
				     <egov:uniquecheck id="nameInclusionReceiptCheck" fieldtoreset="nameChange.receiptNo" fields="['Value']"
										url='common/ajaxCommon!uniqueValidateReceiptNumber.action' /> 
					
				</td>
				<td class="bluebox"></td>
					<td class="bluebox"></td>
				<td class="bluebox">&nbsp;</td>
		</tr>		
		 
	     <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="child.newname.details"/></td>
	   			<td class="bluebox">
	   				<s:text name="firstName.lbl"/><span class="mandatory">*</span><br/>
	   				<s:textfield name="nameChange.firstName" id="nameChange.firstName" value="%{nameChange.firstName}"/><br/>
	   			</td>
	   			<td class="bluebox">
	   				<s:text name="middleName.lbl"/><br>
	   				<s:textfield name="nameChange.middleName" id="nameChange.middleName" value="%{nameChange.middleName}" />
	   			</td>
				<td class="bluebox">
					<s:text name="lastName.lbl"/><br>
					<s:textfield name="nameChange.lastName" id="nameChange.lastName" value="%{nameChange.lastName}" />
					
				</td>
				<td class="bluebox">&nbsp;</td>
	     </tr>
	     
	     
	     <tr>
	 			<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="remarks.lbl"/><span class="mandatory">*</span><br/></td>
	   			<td class="bluebox" colspan="2">
	   				<s:textarea name="nameChange.remarks" id="nameChange.remarks"  value="%{nameChange.remarks}" rows="3" cols="30"/>
	   			</td>
	   			<td class="bluebox"></td>
				<td class="bluebox">&nbsp;</td>
	      </tr>
  </table> 
  
  <div class="buttonbottom" align="center">


				<table>
					<tr>
                   
						<td>
								<s:submit cssClass="buttonsubmit" id="changename" name="changename" value="Change Name" method="edit" onclick="return validateNameInclusion();" />
								  
						</td>
					</tr>	
				</table>
				
			</div>

  
  
