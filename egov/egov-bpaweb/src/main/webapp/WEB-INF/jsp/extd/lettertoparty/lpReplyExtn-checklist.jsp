<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>

<s:set name="theme" value="'simple'" scope="page" />
<SCRIPT>
function ismaxlength(obj){
	var mlength=obj.getAttribute? parseInt(obj.getAttribute("maxlength")) : ""
	if (obj.getAttribute && obj.value.length>mlength)
	obj.value=obj.value.substring(0,mlength)
}
</SCRIPT>
<div align="center"> 
  
	<div id="lpchecketails" class="formmainbox">
	<s:if test="%{lpChkListDet.size!=0}">
	<h1 class="subhead" >Check List Details</h1>
          
		  <s:iterator value="lpChkListDet" status="row_status">
		 
		   <table id="checklists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		 
			    <tr>	     
				    <td class="bluebox" width="30%">&nbsp;</td>			
				   	<td class="bluebox" width="30%"><s:text name="lpChkListDet[%{#row_status.index}].checkListDetails.description"/></td>		   	
					<td class="bluebox" id="mandatorycheck"><s:hidden  name="lpChkListDet[%{#row_status.index}].checkListDetails.isMandatory"/>
					<s:checkbox  name="lpChkListDet[%{#row_status.index}].isChecked"/></td>	
					<td class="bluebox" ><s:textarea   cols="20" rows="3" name="lpChkListDet[%{#row_status.index}].remarks"  maxlength="300" onkeyup="return ismaxlength(this)" /></td>	
					<s:hidden name="lpChkListDet[%{#row_status.index}].checkListDetails.id"/>
					<s:hidden name="lpChkListDet[%{#row_status.index}].checkListDetails.checkList.id"/>
					<s:hidden name="lpChkListDet[%{#row_status.index}].checkListDetails.description"/>  
					<s:hidden name="lpChkListDet[%{#row_status.index}].id"/>
					<td class="bluebox" width="">&nbsp;</td>				 
			  	</tr>   
		    
		    </table>
		 
		    </s:iterator>
		       <s:hidden id="checklistsize" name="lpChkListDet.size"/>
	  </s:if>	 
		 
    </div>
</div>

