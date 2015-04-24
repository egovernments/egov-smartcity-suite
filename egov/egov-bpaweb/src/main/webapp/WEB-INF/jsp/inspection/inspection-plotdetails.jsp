<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>


<s:set name="theme" value="'simple'" scope="page" />


<div align="center"> 
 <div id="plotdetails" class="formmainbox">

	<div align="center">
	<s:if test="%{plotDetails=='TRUE'}">
	
		   <div id="" class="formmainbox">
      <h1 class="subhead" ><s:text name="inspectionlbl.sitemsrheader"/></h1>
	    <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
	    <tr>
	     <td class="bluebox" width="20%">&nbsp;</td>	
		    <td class="bluebox" width="">&nbsp;</td>	
		    <td class="bluebox" width="">&nbsp;</td>	
		    <td class="bluebox" width="">&nbsp;</td>	
		    <td class="bluebox" width="">&nbsp;</td>	
	    <td class="bluebox" width="">&nbsp;</td>
	    </tr>
	   	 	<tr>
	 			<td class="bluebox" width="20%">&nbsp;</td>
	 			<td class="bluebox" id="mandatoryfields"><s:text name="inspectionlbl.msr.bldgextnt"/><span class="mandatory">*</span></td> 
	   			<td class="bluebox" id="wholenumbers"><s:textfield id="buildingextent" name="inspection.inspectionDetails.buildingExtent" value="%{inspection.inspectionDetails.buildingExtent}" maxlength="10"/></td>
				<td class="bluebox" id="mandatoryfields"><s:text name="inspectionlbl.msr.plots"/><span class="mandatory">*</span></td> 
				<td class="bluebox" id="wholenumbers"><s:textfield id="numberofplots" name="inspection.inspectionDetails.numOfPlots" value="%{inspection.inspectionDetails.numOfPlots}" maxlength="10"/></td>
				<td class="bluebox">&nbsp;</td>
	         </tr>         
	      </table>
	 </div>
	  
	</s:if>	 
    </div>
  
	</div> 

	
	
</div>

