<%@ include file="/includes/taglibs.jsp" %>

	<tr>
		<td class="bluebox" width="13%">&nbsp;</td>
		<td class="bluebox" width="13%"   ><s:text name="cmdaProposalNum" /> :<span class="mandatory" >*</span></td>
        
    
       <td class="bluebox" width="26%" >
       <s:textfield id="cmdaProposalNumber" name="cmdaNum" value="%{cmdaNum}"/></td>
      
      
        <td class="bluebox">&nbsp;</td>
        <td class="bluebox" width="20%"   ><s:text name="cmdaRefDate" /> :<span class="mandatory" >*</span></td>
   
	<td class="bluebox">
	  <sj:datepicker value="%{cmdaRefDate}" id="cmdaRefDate" name="cmdaRefDate" displayFormat="dd/mm/yy" showOn="focus" readonly="true"/></td>
	
	</tr> 	