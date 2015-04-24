<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>

<s:set name="theme" value="'simple'" scope="page" />
<SCRIPT>
  jQuery.noConflict();

jQuery(document).ready(function(){
	
 jQuery('#checklist').find('input').attr('disabled','true');
 jQuery('#checklist').find('textarea').attr('disabled','true');
		
  });
  </SCRIPT>
<div id="checklist" align="center"> 
 <div id="lpchecketails" class="formmainbox">
	<div align="center" id="checklistdiv">
	<s:if test="%{lpChkListDet.size!=0}">
	<h1 class="subhead" ><s:text name="lp.docattach"/></h1>
		  <s:iterator value="lpChkListDet" status="row_status">
		 
		   <table id="checklists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		 
		    <tr>
		     
		    <td class="bluebox" width="40%">&nbsp;</td>			
		   	<td class="bluebox" width="20%"><s:text name="lpChkListDet[%{#row_status.index}].checkListDetails.description"/></td>		   	
			<td class="bluebox" id="mandatorycheck"><s:hidden  name="lpChkListDet[%{#row_status.index}].checkListDetails.isMandatory"/>
			<s:checkbox  name="lpChkListDet[%{#row_status.index}].isChecked"/></td>	
			<td class="bluebox" ><s:textarea   cols="10" rows="1" name="lpChkListDet[%{#row_status.index}].remarks"/></td>	
			<s:hidden name="lpChkListDet[%{#row_status.index}].checkListDetails.id"/>
			<s:hidden name="lpChkListDet[%{#row_status.index}].checkListDetails.checkList.id"/>
			<s:hidden name="lpChkListDet[%{#row_status.index}].id"/>
			 <td class="bluebox" width="">&nbsp;</td>	
			 
		  </tr>   
		    
		    </table>
		    </s:iterator>
	  </s:if>
	 
		 
    </div>
    
    
  
	</div> 

	
	
</div>

