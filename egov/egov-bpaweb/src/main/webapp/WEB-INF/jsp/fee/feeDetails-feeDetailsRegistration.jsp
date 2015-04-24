<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>

<s:set name="theme" value="'simple'" scope="page" />
<SCRIPT>
  jQuery.noConflict();

jQuery(document).ready(function(){

var coctotal=0;
  var cmdatotal=0;
   var mwgwftotal=0;
   var grandtotal=0;
 jQuery("[id=cocfeeamount]").each(function(index){
  var feeval= jQuery(this).attr("value");
  if(feeval!=""){
  coctotal=new Number(coctotal)+new Number(feeval);
  }
 });	
 jQuery("#coctotal").val(coctotal);
 
  jQuery("[id=cmdafeeamount]").each(function(index){
  var feeval= jQuery(this).attr("value");
  if(feeval!=""){ 

  cmdatotal=new Number(cmdatotal)+new Number(feeval);
  
  }
 });	
 jQuery("#cmdatotal").val(cmdatotal);
 
 
  jQuery("[id=mwgwffeeamount]").each(function(index){
  var feeval= jQuery(this).attr("value");
  if(feeval!=""){

  mwgwftotal=new Number(mwgwftotal)+new Number(feeval);
   
  } 
 });	
 jQuery("#mwgwftotal").val(mwgwftotal);
 
 
 
  jQuery("#grandtotal").val(mwgwftotal+cmdatotal+coctotal);	


 jQuery('#regfeeetails').find('input').attr('disabled','true');	 

  });

</SCRIPT>
 <div align="center"> 
 <div id="regfeeetails" class="formmainbox">
	<div align="center" id="feelistdiv">
	
	  <s:if test="%{COCFeeList.size!=0}">
	   <h1 class="subhead" ><s:text name=" Fee Details"/></h1>
	    <div align="center">
 <table id="feehead" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
   <tr>
	 			
			   <td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="13%"><s:text name="Challan Number"/></td> 
	   			<td class="bluebox"> <s:textfield readonly="true" value="%{registrationObj.registrationFeeChallanNumber}" id="challanNumber" name="challanNumber"  disabled="true" /></td>   						
			   <td class="bluebox" width="13%"><s:text name="Fee Date"/></td> 
	   			<td class="bluebox"> <s:textfield readonly="true" value="%{registrationObj.feeDate}" id="feedate" name="feedate"  disabled="true" /></td>   						
			
				<td class="bluebox" >&nbsp;</td>
	       
          </tr>
          </table>
     <div>
		   <s:if test="%{COCFeeList.size!=0}">
	<h1 class="subhead" ><s:text name="COC Fees"/></h1>
	  
		   <table id="feelists" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	     
	       <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Description</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Amount</div></th>
		    </tr>
		    
		  <s:iterator value="COCFeeList" status="row_status">
		    <tr>
		  	<td  class="blueborderfortd"><div align="center"><s:text name="#row_status.count" /></div></td>
		    <td class="blueborderfortd"><div align="center"><s:property value="%{feeDescription}"/>
		    <s:if test="{COCFeeList[%{#row_status.index}].isMandatory=='true'}"><span class="mandatory">*</span></s:if></div>
		    <s:hidden  value="COCFeeList[%{#row_status.index}].isMandatory"/></td>
		    
		    
		    <td class="blueborderfortd" id="mandatorycheck"><s:hidden name="COCFeeList[%{#row_status.index}].isMandatory"/><div align="center" id="numbers">
		    <s:textfield id="cocfeeamount" name="COCFeeList[%{#row_status.index}].feeAmount" disabled="true" onblur="calculateTotal()"/></div></td>		  			  		    
		    <s:hidden  name="COCFeeList[%{#row_status.index}].id"/>
		      <s:hidden  name="COCFeeList[%{#row_status.index}].feeType"/>
		        <s:hidden  name="COCFeeList[%{#row_status.index}].feeCode"/>
		        <s:hidden  name="COCFeeList[%{#row_status.index}].demandDetailId"/>
		         <s:hidden  name="COCFeeList[%{#row_status.index}].feeDescription"/>
		    
		    </tr>
		   
		    </s:iterator>
		     <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><s:text name="Total" /></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="coctotal" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		      <s:if test="%{CMDAFeeList.size==0 && MWGWFFeeList.size==0}">
		       <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><b><s:text name="Grand Total" /></b></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="grandtotal" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		      </s:if>
		    
		    </table>
		    </s:if>
		    <s:if test="%{CMDAFeeList.size!=0}">
	
	  <h1 class="subhead" ><s:text name="CMDA Fees"/></h1>
		   <table id="feelists" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	     
	       <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Description</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Amount</div></th>
		    </tr>
		    <s:iterator value="CMDAFeeList" status="row_status">
		    <tr>
		  	<td  class="blueborderfortd"><div align="center"><s:text name="#row_status.count" /></div></td>
		    <td class="blueborderfortd"><div align="center"><s:property value="%{feeDescription}"/>
		    <s:if test="{CMDAFeeList[%{#row_status.index}].isMandatory=='true'}"><span class="mandatory">*</span></s:if></div>
		    <s:hidden  value="CMDAFeeList[%{#row_status.index}].isMandatory"/></td>
		    
		    
		    <td class="blueborderfortd" id="mandatorycheck"><s:hidden name="CMDAFeeList[%{#row_status.index}].isMandatory"/><div align="center" id="numbers">
		    <s:textfield id="cmdafeeamount" name="CMDAFeeList[%{#row_status.index}].feeAmount"  disabled="true" onblur="calculateTotal()"/></div></td>		  			  		    
		    <s:hidden  name="CMDAFeeList[%{#row_status.index}].id"/>
		      <s:hidden  name="CMDAFeeList[%{#row_status.index}].feeType"/>
		        <s:hidden  name="CMDAFeeList[%{#row_status.index}].feeCode"/>
		        <s:hidden  name="CMDAFeeList[%{#row_status.index}].demandDetailId"/>
		         <s:hidden  name="CMDAFeeList[%{#row_status.index}].feeDescription"/>
		    
		    </tr>
		 
		    </s:iterator>
		     <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><s:text name="Total" /></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="cmdatotal" name="cmdatotal" value="" disabled="true"/></div></td>	
		    </tr>
		    <s:if test="%{MWGWFFeeList.size==0}">
		       <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><b><s:text name="Grand Total" /></b></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="grandtotal" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		      </s:if>
		    </table>
		    </s:if>
		     <s:if test="%{MWGWFFeeList.size!=0}">
	<h1 class="subhead" ><s:text name="M.W.G.W.F Fees"/></h1>
	  
		   <table id="feelists" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	     
	       <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Description</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Amount</div></th>
		    </tr>
		    <s:iterator value="MWGWFFeeList" status="row_status">
		    <tr>
		  	<td  class="blueborderfortd"><div align="center"><s:text name="#row_status.count" /></div></td>
		    <td class="blueborderfortd"><div align="center"><s:property value="%{feeDescription}"/>
		    <s:if test="{MWGWFFeeList[%{#row_status.index}].isMandatory=='true'}"><span class="mandatory">*</span></s:if></div>
		    <s:hidden  value="MWGWFFeeList[%{#row_status.index}].isMandatory"/></td>
		    
		    
		    <td class="blueborderfortd" id="mandatorycheck"><s:hidden name="MWGWFFeeList[%{#row_status.index}].isMandatory"/><div align="center" id="numbers">
		    <s:textfield id="mwgwffeeamount" name="MWGWFFeeList[%{#row_status.index}].feeAmount" disabled="true"  onblur="calculateTotal()"/></div></td>		  			  		    
		    <s:hidden  name="MWGWFFeeList[%{#row_status.index}].id"/>
		      <s:hidden  name="MWGWFFeeList[%{#row_status.index}].feeType"/>
		        <s:hidden  name="MWGWFFeeList[%{#row_status.index}].feeCode"/>
		        <s:hidden  name="MWGWFFeeList[%{#row_status.index}].demandDetailId"/>
		         <s:hidden  name="MWGWFFeeList[%{#row_status.index}].feeDescription"/>
		    
		    </tr>
		    </s:iterator>
		  
		      <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><s:text name="Total" /></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="mwgwftotal" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		 
		       <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><b><s:text name="Grand Total" /></b></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="grandtotal" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		    
	  </table>	
	   <div align="center" class="formmainbox">
	  <h1 class="subhead" ><s:text name="Remarks"/></h1>
	 
	  <table id="feelistremarks" width="37%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	   <tr>		 
			   <td class="blueborderfortd"><div align="center"><s:textarea cols="100" rows="10" id="feeRemarks" name="feeRemarks" value="%{feeRemarks}"  readonly="true"/></div></td>	
		    </tr>
	  </table>
	   </div>
	  </s:if>
	 
    </div>
	</div> 
	</s:if>
	
</div>

