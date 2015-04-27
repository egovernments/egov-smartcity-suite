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
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>
<html>	

		<div align="center" >
		 <h1 class="subhead" ><s:text name="amountDetails.label"/></h1>
			<table id="feeTypeTbl" width="60%" border="1" cellspacing="0" cellpadding="0" class="tablebottom">
		       <tr>
				<th  class="bluebgheadtd" width="20%"><div align="center"> <s:text name="Sl No"/>	</div></th>
			    <th  class="bluebgheadtd" width="30%"><div align="center"> <s:text name="feeType.label"/>	</div></th>
			    <th  class="bluebgheadtd" width="30%"><div align="center"> <s:text name="feeAmount.label"/>	</div></th>
			 </tr>  
			 <s:iterator value="feeCollnList" status="row_status">  
					<tr>
						<td class="greybox">
							<div align="center"><s:text name="%{#row_status.count}" /></div>						
						</td>
						
						<td class="greybox">
							<s:hidden id="feeCollectionid" name="feeCollnList[%{#row_status.index}].id" />
							<s:hidden id="feeTypeid" name="feeCollnList[%{#row_status.index}].feeType.id" />
							<s:hidden id="glcodeId" name="feeCollnList[%{#row_status.index}].feeType.glcodeId.glcode" />
							<s:textfield name="feeCollnList[%{#row_status.index}].feeType.description" id="description%{#row_status.index}" readonly="true"  value="%{feeType.description}"  size="20" />
						</td>
						
						<td class="greybox" >   
						
							<s:textfield name="feeCollnList[%{#row_status.index}].amount" id="amount%{#row_status.index}"  value="%{feeType.amount}"  maxlength="4" size="20" onblur="validateNumber(this);calculateTotal();" />
						</td>  
						      
					</tr>
				
			</s:iterator>
			 <tr>
	 			<td width="10%" class="greybox"><s:text name="totalAmount.label"/></td>
	   			<td class="greybox" colSpan="5">
	   				<s:textfield name="totalAmount" id="totalAmount" readonly="true"  value="%{totalAmount}"  maxlength="4" size="20"  />	</td>
	   	 </tr>
	   	 
			</table>
			
			<br/><br/>
			<tr>
					    <s:if test="%{mode!='view'}">
					      <td>
								<s:submit cssClass="buttonsubmit" id="savesubmit" name="savesubmit" value="Save" method="create" onclick="return validateForm('save');" />
						 </td>
						 </s:if>
						 <td>
								<input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();" />
						 </td>
					</tr>
		</table>
	</div>
		</html>
