<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>

<s:set name="theme" value="'simple'" scope="page" />
<div align="center"> 
 <div id="dplandetails" class="formmainbox">
  
	<div align="center" id="checklistdiv">
	<s:if test="%{chkListDet.size!=0}">
	<h1 class="subhead" ><s:text name="inspectionlbl.otherdtls"/></h1>
          
		  <s:iterator value="chkListDet" status="row_status">
		 
		   <table id="checklists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		 
		    <tr>
		     
		    <td class="bluebox" width="40%">&nbsp;</td>			
		   	<td class="bluebox" width="20%"><s:text name="chkListDet[%{#row_status.index}].checklistDetails.description"/></td>		   	
			<td class="bluebox" id="mandatorycheck"><s:hidden  name="chkListDet[%{#row_status.index}].checklistDetails.isMandatory"/>
			<s:checkbox  name="chkListDet[%{#row_status.index}].isChecked"/></td>		
			<s:hidden name="chkListDet[%{#row_status.index}].checklistDetails.id"/>
			<s:hidden name="chkListDet[%{#row_status.index}].checklistDetails.checkList.id"/>
			<s:hidden name="chkListDet[%{#row_status.index}].id"/>
			 <td class="bluebox" width="">&nbsp;</td>	
			 
		  </tr>   
		    
		    </table>
		    </s:iterator>
	  </s:if>
	 
		 
    </div>
    
    
  
	</div> 

	
	
</div>

