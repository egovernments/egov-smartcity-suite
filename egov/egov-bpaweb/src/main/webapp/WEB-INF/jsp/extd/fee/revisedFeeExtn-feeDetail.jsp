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


jQuery('#regfeedetails').children().each(function(){


jQuery(this).attr('disabled','true');

})


 var coctotalview=0;
  var cmdatotalview=0;
   var mwgwftotalview=0;
   var grandtotalview=0;

 jQuery("[id=cocfeeamountview]").each(function(index){
  var feeval= jQuery(this).attr("value");

  if(feeval!=""){

  coctotalview=new Number(coctotalview)+new Number(feeval);
  
  }
 });	
 jQuery("#coctotalview").val(coctotalview);
   
  jQuery("[id=cmdafeeamountview]").each(function(index){
  var feeval= jQuery(this).attr("value");
  if(feeval!=""){ 

  cmdatotalview=new Number(cmdatotalview)+new Number(feeval);
  
  }
 });	
 jQuery("#cmdatotalview").val(cmdatotalview);
 
 
  jQuery("[id=mwgwffeeamountview]").each(function(index){
  var feeval= jQuery(this).attr("value");
  if(feeval!=""){

  mwgwftotalview=new Number(mwgwftotalview)+new Number(feeval);
   
  } 
 });	
 jQuery("#mwgwftotalview").val(mwgwftotalview);
 

 
  jQuery("#grandtotalview").val(mwgwftotalview+cmdatotalview+coctotalview);
  });





</SCRIPT>
  
<div id="regfeedetails" align="center"> 


 <div class="formmainbox">
  
	<div align="center" >
<s:if test="%{COCRegFeeList.size!=0}">
	
	  <h1 class="subhead" ><s:text name="COC Fees"/></h1>
		   <table id="feelists" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	     
	       <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Description</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Amount</div></th>
		    </tr>
		    
		  <s:iterator value="COCRegFeeList" status="row_status">
		    <tr>
		  	<td  class="blueborderfortd"><div align="center"><s:text name="#row_status.count" /></div></td>
		  	<script>
		  	
		  
		  	
		  	</script>
		    <td class="blueborderfortd"><div align="center"><s:property value="%{bpaFee.feeDescription}"/>
		    <s:if test="{COCRegFeeList[%{#row_status.index}].isMandatory=='true'}"><span class="mandatory">*</span></s:if></div>
		    <s:hidden  value="COCRegFeeList[%{#row_status.index}].bpaFee.isMandatory"/></td>
		    
		    
		    <td class="blueborderfortd" id=""><s:hidden name="COCRegFeeList[%{#row_status.index}].bpaFee.isMandatory"/><div align="center" id="numbers">
		    <s:textfield id="cocfeeamountview" name="COCRegFeeList[%{#row_status.index}].amount"  onblur="calculateTotal()"/></div></td>		  			  		    
		    <s:hidden  name="COCRegFeeList[%{#row_status.index}].id"/>
		     <s:hidden  name="COCRegFeeList[%{#row_status.index}].bpaFee.id"/>	    
		      <s:hidden  name="COCRegFeeList[%{#row_status.index}].bpaFee.feeDescription"/>	    
		    </tr>
		    </s:iterator>
		     <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><s:text name="Total" /></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="coctotalview" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		   
		      <s:if test="%{CMDARegFeeList.size==0 && MWGWFRegFeeList.size==0}">
		       <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><b><s:text name="Grand Total" /></b></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="grandtotalview" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		      </s:if>
		   
		    
	  </table>	
	
	  </s:if>
	  
	  <s:if test="%{CMDARegFeeList.size!=0}">
	
	   <h1 class="subhead" ><s:text name="CMDA Fees"/></h1>
		   <table id="feelists" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	     
	       <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Description</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Amount</div></th>
		    </tr>
		    
		  <s:iterator value="CMDARegFeeList" status="row_status">
		    <tr>
		  	<td  class="blueborderfortd"><div align="center"><s:text name="#row_status.count" /></div></td>
		  	<script>
		  	
		  
		  	
		  	</script>
		    <td class="blueborderfortd"><div align="center"><s:property value="%{bpaFee.feeDescription}"/>
		    <s:if test="{CMDARegFeeList[%{#row_status.index}].isMandatory=='true'}"><span class="mandatory">*</span></s:if></div>
		    <s:hidden  value="CMDARegFeeList[%{#row_status.index}].bpaFee.isMandatory"/></td>
		    
		    
		    <td class="blueborderfortd" id=""><s:hidden name="CMDARegFeeList[%{#row_status.index}].bpaFee.isMandatory"/><div align="center" id="numbers">
		    <s:textfield id="cmdafeeamountview" name="CMDARegFeeList[%{#row_status.index}].amount"  onblur="calculateTotal()"/></div></td>		  			  		    
		    <s:hidden  name="CMDARegFeeList[%{#row_status.index}].id"/>
		     <s:hidden  name="CMDARegFeeList[%{#row_status.index}].bpaFee.id"/>	    
		      <s:hidden  name="CMDARegFeeList[%{#row_status.index}].bpaFee.feeDescription"/>	    
		    </tr>
		    </s:iterator>
		     <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><s:text name="Total" /></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="cmdatotalview" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		   
		     <s:if test="%{MWGWFRegFeeList.size==0}">
		       <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><b><s:text name="Grand Total" /></b></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="grandtotalview" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		      </s:if>
		   
		    
	  </table>	
	
	  </s:if>
	  
	  
	  <s:if test="%{MWGWFRegFeeList.size!=0}">
	<h1 class="subhead" ><s:text name="M.W.G.W.F Fees"/></h1>
	  
	  
		   <table id="feelists" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	     
	       <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Description</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Amount</div></th>
		    </tr>
		    
		  <s:iterator value="MWGWFRegFeeList" status="row_status">
		    <tr>
		  	<td  class="blueborderfortd"><div align="center"><s:text name="#row_status.count" /></div></td>
		  	<script>
		  	
		  
		  	
		  	</script>
		    <td class="blueborderfortd"><div align="center"><s:property value="%{bpaFee.feeDescription}"/>
		    <s:if test="{MWGWFRegFeeList[%{#row_status.index}].isMandatory=='true'}"><span class="mandatory">*</span></s:if></div>
		    <s:hidden  value="MWGWFRegFeeList[%{#row_status.index}].bpaFee.isMandatory"/></td>
		    
		    
		    <td class="blueborderfortd" id=""><s:hidden name="MWGWFRegFeeList[%{#row_status.index}].bpaFee.isMandatory"/><div align="center" id="numbers">
		    <s:textfield id="mwgwffeeamountview" name="MWGWFRegFeeList[%{#row_status.index}].amount"  onblur="calculateTotal()"/></div></td>		  			  		    
		    <s:hidden  name="MWGWFRegFeeList[%{#row_status.index}].id"/>
		     <s:hidden  name="MWGWFRegFeeList[%{#row_status.index}].bpaFee.id"/>	    
		      <s:hidden  name="MWGWFRegFeeList[%{#row_status.index}].bpaFee.feeDescription"/>	    
		    </tr>
		    </s:iterator>
		    <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><s:text name="Total" /></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="mwgwftotalview" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		     <tr>
			  <td class="blueborderfortd"><div align="center"></div></td>				
			  <td class="blueborderfortd"><div align="center"><b><s:text name="Grand Total" /></b></div></td>	
			   <td class="blueborderfortd"><div align="center"><s:textfield id="grandtotalview" name="total" value="" disabled="true"/></div></td>	
		    </tr>
		    
	  </table>	
	
	  </s:if>
    </div>
  </div> 
</div> 	
