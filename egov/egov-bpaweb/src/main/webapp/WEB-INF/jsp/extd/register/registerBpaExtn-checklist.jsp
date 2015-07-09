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
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>

<s:set name="theme" value="'simple'" scope="page" />
<SCRIPT>
jQuery.noConflict();

jQuery(document).ready(function(){
	
	jQuery('input[type=file]').attr('disabled', true);
  });
function ismaxlength(obj){
	var mlength=obj.getAttribute? parseInt(obj.getAttribute("maxlength")) : ""
	if (obj.getAttribute && obj.value.length>mlength)
	obj.value=obj.value.substring(0,mlength)
}

function copyFileName(obj,count) {
	
	document.getElementById('chkListDet['+count+'].fileName').value=obj.value;
	
	var fileName = obj.value;
    var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
    
    document.getElementById('chkListDet['+count+'].contentType').value= obj.files[0].type;
    
    if(ext == "exe")
    {		
		document.getElementById('chkListDet['+count+'].uploadFile').value = '';
		document.getElementById('chkListDet['+count+'].fileName').value='';
		document.getElementById('chkListDet['+count+'].contentType').value='';
		
		alert("exe Files can not be Uploaded");
		return false;
    }
}

</SCRIPT>
<div align="center"> 
 <div id="regchecklistdivid" class="formmainbox">
	<div  align="center">
	<s:if test="%{chkListDet.size!=0}">
	
            
          <table id="checklists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">      
		  <s:iterator value="chkListDet" status="row_status">
		    <tr>
		     
		    <td class="bluebox" width="29%">&nbsp;</td>	
		    <td class="bluebox" width="3%"><s:textfield size="3%" name="srlNo" id="srlNo" readonly="true"  value="%{#row_status.count}" /></td>			
		   	<td class="bluebox" width="20%"><s:textfield size="50%" name="chkListDet[%{#row_status.index}].checkListDetails.description" readonly="true" /></td>	
		   	<td class="bluebox" id="mandatorycheckforregcl"><s:hidden  name="chkListDet[%{#row_status.index}].checkListDetails.isMandatory"/>	   	
			<s:checkbox  name="chkListDet[%{#row_status.index}].isChecked" id="isChecked"  onchange="onchangeOfCheckList(this,'%{#row_status.index}');"/>
			</td>
		<s:if test="%{fileName != null && fileName != ''}">	
			    	  <td class="bluebox" width="20%"> 
			    	  		<s:file name="chkListDet[%{#row_status.index}].uploadFile" id="chkListDet[%{#row_status.index}].uploadFile"  value="chkListDet[%{#row_status.index}].uploadFile" label="Select a File to upload" onchange="copyFileName(this,'%{#row_status.index}');"  size="40" /> 
			<br><br>
			    	   <a href="${pageContext.request.contextPath}/extd/portal/citizenRegisterBpaExtn!downLoadFile.action?regDocUpldDtlsId=<s:property value='docUpload'/>" > 
											<s:property value="%{fileName}" /></a>		<br><br>
					  </td>
				</s:if>	
				<s:else>
						<td class="bluebox" >
							<s:file name="chkListDet[%{#row_status.index}].uploadFile" id="chkListDet[%{#row_status.index}].uploadFile"  value="chkListDet[%{#row_status.index}].uploadFile" label="Select a File to upload" onchange="copyFileName(this,'%{#row_status.index}');"  size="40" /> 
						</td>
				</s:else>	
					<s:hidden name="chkListDet[%{#row_status.index}].checkListDetails.id"/>
			<s:hidden name="chkListDet[%{#row_status.index}].checkListDetails.checkList.id"/>
			<s:hidden name="chkListDet[%{#row_status.index}].checkListDetails.code"/>
			<s:hidden name="chkListDet[%{#row_status.index}].id"/>
			<s:hidden name="chkListDet[%{#row_status.index}].fileName" id="chkListDet[%{#row_status.index}].fileName" />
				<s:hidden name="chkListDet[%{#row_status.index}].contentType" id="chkListDet[%{#row_status.index}].contentType" />
			
			<s:hidden name="chkListDet[%{#row_status.index}].docUpload" id="chkListDet[%{#row_status.index}].docUpload" />
			
		  	</tr>   	    
		   
		    </s:iterator>
		     <tr>		     
			    <td class="bluebox" >&nbsp;</td>			
			   	<td class="bluebox" >&nbsp;</td>	
			   	<td class="bluebox" >&nbsp;</td>	
			   	<td class="bluebox" >&nbsp;</td>	
			   	<td class="bluebox" >&nbsp;</td>	   	<td class="bluebox" >&nbsp;</td>	
			   	<td class="bluebox" >&nbsp;</td>	
	  		</tr>
		    </table>
	  </s:if>
	  <s:else>
	  
	   <table id="checklists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">		 
		    <tr>		     
			    <td class="bluebox" width="30%">&nbsp;</td>			
			   	<td class="bluebox" width="20%"><s:text name="No CheckList Found"/></td>	
			   	<td class="bluebox" width="20%">&nbsp;</td>			
	  		</tr>
	  </table>
	  
	  </s:else>
		 
    </div>
    
    
  
	</div> 

	
	
</div>

