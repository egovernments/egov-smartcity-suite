<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>

<s:set name="theme" value="'simple'" scope="page" />
<div align="center"> 
 <div id="regchecklistdivid" class="formmainbox">
	<div  align="center">
	<s:if test="%{chkListDet.size!=0}">
	<div class="headingsmallbg"><span class="bold"><s:text name="attach.documents"/></span></div>
            
          <table id="checklists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">      
		  <s:iterator value="chkListDet" status="row_status">
		    <tr>
		     
		    <td class="bluebox" width="29%">&nbsp;</td>	
		    <td class="bluebox" width="3%"><s:textfield size="3%" name="srlNo" id="srlNo" readonly="true"  value="%{#row_status.count}" /></td>			
		   	<td class="bluebox" width="20%"><s:textfield size="50%" name="chkListDet[%{#row_status.index}].checkListDetails.description" readonly="true" /></td>	
		   	<td class="bluebox" id="mandatorycheckforregcl"><s:hidden  name="chkListDet[%{#row_status.index}].checkListDetails.isMandatory"/>	   	
			<s:checkbox  name="chkListDet[%{#row_status.index}].isChecked"/></td>
			<s:hidden name="chkListDet[%{#row_status.index}].checkListDetails.id"/>
			<s:hidden name="chkListDet[%{#row_status.index}].checkListDetails.checkList.id"/>
			<s:hidden name="chkListDet[%{#row_status.index}].id"/>
			<td class="bluebox" width="">&nbsp;</td>	
			 
		  	</tr>   	    
		   
		    </s:iterator>
		     <tr>		     
			    <td class="bluebox" >&nbsp;</td>			
			   	<td class="bluebox" >&nbsp;</td>	
			   	<td class="bluebox" >&nbsp;</td>	
			   	<td class="bluebox" >&nbsp;</td>	
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

