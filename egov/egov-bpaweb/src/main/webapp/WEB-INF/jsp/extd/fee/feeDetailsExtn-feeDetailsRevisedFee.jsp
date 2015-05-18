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
<%@ taglib prefix="s" uri="/WEB-INF/taglibs/struts-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>

<s:set name="theme" value="'simple'" scope="page" />
<SCRIPT>
  jQuery.noConflict();

jQuery(document).ready(function(){

var cocoldtotal=0;
  var cmdaoldtotal=0;
   var mwgwfoldtotal=0;
   var grandoldtotal=0;
 jQuery("[id=cocfeeamount]").each(function(index){
  var feeval= jQuery(this).attr("value");
  if(feeval!=""){
  cocoldtotal=new Number(cocoldtotal)+new Number(feeval);
  }
 });	
 jQuery("#cocoldtotal").val(cocoldtotal);
 
  jQuery("[id=cmdafeeamount]").each(function(index){
  var feeval= jQuery(this).attr("value");
  if(feeval!=""){ 

  cmdaoldtotal=new Number(cmdaoldtotal)+new Number(feeval);
  
  }
 });	
 jQuery("#cmdaoldtotal").val(cmdaoldtotal);
 
 
  jQuery("[id=mwgwffeeamount]").each(function(index){
  var feeval= jQuery(this).attr("value");
  if(feeval!=""){

  mwgwfoldtotal=new Number(mwgwfoldtotal)+new Number(feeval);
   
  } 
 });	
 jQuery("#mwgwfoldtotal").val(mwgwfoldtotal);
 
 
 
  jQuery("#grandoldtotal").val(mwgwfoldtotal+cmdaoldtotal+cocoldtotal);	


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
		    
		    
		    <td class="blueborderfortd" id=""><s:hidden name="COCFeeList[%{#row_status.index}].isMandatory"/><div align="center" id="numbers">
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
			   <td class="blueborderfortd"><div align="center"><s:textfield id="cocoldtotal" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		      <s:if test="%{CMDAFeeList.size==0 && MWGWFFeeList.size==0}">
		       <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><b><s:text name="Grand Total" /></b></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="grandoldtotal" name="total" value="" disabled="true"/></div></td>	
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
		    
		    
		    <td class="blueborderfortd" id=""><s:hidden name="CMDAFeeList[%{#row_status.index}].isMandatory"/><div align="center" id="numbers">
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
			   <td class="blueborderfortd"><div align="center"><s:textfield id="cmdaoldtotal" name="cmdaoldtotal" value="" disabled="true"/></div></td>	
		    </tr>
		    <s:if test="%{MWGWFFeeList.size==0}">
		       <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><b><s:text name="Grand Total" /></b></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="grandoldtotal" name="total" value="" disabled="true"/></div></td>	
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
		    
		    
		    <td class="blueborderfortd" id=""><s:hidden name="MWGWFFeeList[%{#row_status.index}].isMandatory"/><div align="center" id="numbers">
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
			   <td class="blueborderfortd"><div align="center"><s:textfield id="mwgwfoldtotal" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		 
		       <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><b><s:text name="Grand Total" /></b></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="grandoldtotal" name="total" value="" disabled="true"/></div></td>	
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

