#-------------------------------------------------------------------------------
# <!-- #-------------------------------------------------------------------------------
# # eGov suite of products aim to improve the internal efficiency,transparency, 
# #    accountability and the service delivery of the government  organizations.
# # 
# #     Copyright (C) <2015>  eGovernments Foundation
# # 
# #     The updated version of eGov suite of products as by eGovernments Foundation 
# #     is available at http://www.egovernments.org
# # 
# #     This program is free software: you can redistribute it and/or modify
# #     it under the terms of the GNU General Public License as published by
# #     the Free Software Foundation, either version 3 of the License, or
# #     any later version.
# # 
# #     This program is distributed in the hope that it will be useful,
# #     but WITHOUT ANY WARRANTY; without even the implied warranty of
# #     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# #     GNU General Public License for more details.
# # 
# #     You should have received a copy of the GNU General Public License
# #     along with this program. If not, see http://www.gnu.org/licenses/ or 
# #     http://www.gnu.org/licenses/gpl.html .
# # 
# #     In addition to the terms of the GPL license to be adhered to in using this
# #     program, the following additional terms are to be complied with:
# # 
# # 	1) All versions of this program, verbatim or modified must carry this 
# # 	   Legal Notice.
# # 
# # 	2) Any misrepresentation of the origin of the material is prohibited. It 
# # 	   is required that all modified versions of this material be marked in 
# # 	   reasonable ways as different from the original version.
# # 
# # 	3) This license does not grant any rights to any user of the program 
# # 	   with regards to rights under trademark law for use of the trade names 
# # 	   or trademarks of eGovernments Foundation.
# # 
# #   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
# #------------------------------------------------------------------------------- -->
#-------------------------------------------------------------------------------
<%@ include file="/includes/taglibs.jsp" %>
	<tr>
		<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="Site Details"/></span></div></td>
	</tr>
  	<tr>
  		<td class="greybox" width="13%">&nbsp;</td>
        <td class="greybox" width="13%"  ><s:text name="plotAreaSqft" /> : <span class="mandatory" >*</span></td>
        
    <s:if test="%{userRole!='PORTALUSERSURVEYOR'}">
      <td class="greybox" width="26%" style="font-weight:bold;font-size:13px">
        <s:textfield id="plotAreaInSqft" name="regnDetails.sitalAreasqft" value="%{regnDetails.sitalAreasqft}" maxlength="9" onblur="convertSQFTtoSQMT()" /></td>
		
        </s:if>
        <s:else>
        
         <td class="greybox" width="26%" > <s:property value="%{regnDetails.sitalAreasqft}"/></td>
        <td style="display: none;" > <s:textfield id="plotAreaInSqft" name="regnDetails.sitalAreasqft" value="%{regnDetails.sitalAreasqft}" maxlength="9" onblur="convertSQFTtoSQMT()" /></td>
		
        </s:else>
		<td class="greybox">&nbsp;</td>
		<td class="greybox" width="20%"   ><s:text name="plotAreaSqmt" /> :</td>
		<td class="greybox" style="font-weight:bold;font-size:13px"><s:textfield id="plotAreaInSqmt"  style ="border: none" name="regnDetails.sitalAreasqmt" value="%{regnDetails.sitalAreasqmt}" maxlength="9"  readonly="true"  /></td>
      	<s:hidden id="regnDetails.id" name="regnDetails.id" /></td>
	</tr>  
             
 <script>

 function convertSQFTtoSQMTforDisplay(){} 

	function convertSQFTtoSQMT(){
		var sqft= document.getElementById('plotAreaInSqft').value;
		if (!isNaN(sqft)){
				var sqmt=(sqft*0.092903);
				document.getElementById('plotAreaInSqmt').value=sqmt;
				callAdmissionFeeAmount();
			}
		else{
			alert("Enter Numeric values");
			document.getElementById('plotAreaInSqft').value="";
			document.getElementById('plotAreaInSqmt').value="";
			}
	}

	function convertSQMTtoSQFT(){
		var sqmt= document.getElementById('plotAreaInSqmt').value;
		if(!isNaN(sqmt)){
			var sqft=(sqmt*10.7639);
			document.getElementById('plotAreaInSqft').value=sqft;
			callAdmissionFeeAmount();
		}
	else{
		alert("Enter Numeric values");
		document.getElementById('plotAreaInSqft').value="";
		document.getElementById('plotAreaInSqmt').value="";
		}
	}
	
 </script>
