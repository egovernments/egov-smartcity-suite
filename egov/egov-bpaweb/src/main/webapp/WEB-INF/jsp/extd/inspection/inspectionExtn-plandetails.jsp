<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>


<s:set name="theme" value="'simple'" scope="page" />
<script>

<s:if test="%{serviceTypeId==7}"> 
jQuery('#dplandetails').find("td").each(function(){
if(jQuery(this).attr('id')=='mandatoryfields')
jQuery(this).find('span').remove();
jQuery(this).attr('id', '');
}
)
</s:if>

</script>

<div align="center"> 
 <div id="dplandetails" class="formmainbox">
  
	<div align="center">
	<s:if test="%{inspectMeasurementDtls.size!=0}">
	
		  <s:iterator value="inspectMeasurementDtls" status="row_status">
		 
		   <table id="plan" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		 
		  <tr>
		    <td class="bluebox" width="20%">&nbsp;</td>	
		    <td class="bluebox" width="">&nbsp;</td>	
		    <td class="bluebox" width="">&nbsp;</td>	
		    <td class="bluebox" width="">&nbsp;</td>	
		    <td class="bluebox" width="">&nbsp;</td>	
		 </tr>
		  <tr>
		    <td width="20%">&nbsp;</td>	
		   <td width="8%">&nbsp;</td>	
		   <td align="center">
		   <h1 class="subhead" ><s:text name="inspectMeasurementDtls[%{#row_status.index}].header"/></h1>
		 </td>
		  
		 </tr>
		 
		    <tr>
		    <td class="greybox" width="20%">&nbsp;</td>			
		   	<td class="greybox" id="mandatoryfields"><s:text name="inspectionlbl.msr.fsb"/><span class="mandatory">*</span></td>
			<td class="greybox" id="numbers"><s:textfield id="fsb" name="inspectMeasurementDtls[%{#row_status.index}].fsb" maxlength="10"/></td>
			<td class="greybox" id="mandatoryfields"><s:text name="inspectionlbl.msr.rsb"/><span class="mandatory">*</span></td>
			<td class="greybox" id="numbers"><s:textfield id="rsb" name="inspectMeasurementDtls[%{#row_status.index}].rsb " maxlength="10"/></td>
		
		    </tr>
		    <tr>
		    <td class="bluebox" width="20%">&nbsp;</td>
		   	<td class="bluebox" id="mandatoryfields"><s:text name="inspectionlbl.msr.ssb1"/><span class="mandatory">*</span></td>
			<td class="bluebox" id="numbers"><s:textfield id="ssb1" name="inspectMeasurementDtls[%{#row_status.index}].ssb1" maxlength="10"/></td>
			<td class="bluebox" id="mandatoryfields"><s:text name="inspectionlbl.msr.ssb2"/><span class="mandatory">*</span></td>
			<td class="bluebox" id="numbers"><s:textfield id="ssb2" name="inspectMeasurementDtls[%{#row_status.index}].ssb2" maxlength="10"/></td>
			<s:hidden name="inspectMeasurementDtls[%{#row_status.index}].inspectionSource.id"/>
			
		    </tr>
		    
		    
		    <tr>
		    <td class="greybox" width="20%">&nbsp;</td>	
		    <td class="greybox" width="">&nbsp;</td>	
		    <td class="greybox" width="">&nbsp;</td>	
		    <td class="greybox" width="">&nbsp;</td>	
		    <td class="greybox" width="">&nbsp;</td>	
		    <s:hidden name="inspectMeasurementDtls[%{#row_status.index}].id"/>	
		  
		
		    </tr>
		    </table>
		    </s:iterator>
	  
	</s:if>	 
    </div>
  
	</div> 

	
	
</div>

