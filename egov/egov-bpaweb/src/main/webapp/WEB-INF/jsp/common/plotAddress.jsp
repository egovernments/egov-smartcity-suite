<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<style>
 .ui-combobox {
position: relative;
display: inline-block;
}
.ui-combobox-toggle {
position: absolute;
top: 0;
bottom: 0;
margin-left: -1px;
padding: 0;
/* support: IE7 */
*height: 1.7em;
*top: 0.1em;
}
.ui-combobox-input {
margin: 0;
padding: 0.3em;
}
.ui-combobox-input {
margin: 0;
padding: 0.3em;
}

</style>
<script type="text/javascript">

jQuery.noConflict();



jQuery(document).ready(function() {
	
	jQuery("#villageName").combobox();
	 //jQuery('.ui-autocomplete-input').css('backgroundColor', '#B3B3B3');
	 var mode=document.getElementById('mode').value;
	 var addMode=document.getElementById('additionalMode').value;
		
	if((mode=='noEdit' || mode=='view') && (addMode!=null && addMode!='editApprovedRecord')){
	  
		jQuery("#villageName").combobox();
		 //jQuery('.ui-autocomplete-input').css('backgroundColor', '#B3B3B3');
	    jQuery("#villageName").closest(".ui-widget").find("input, button").prop("disabled", true);
	    jQuery("#villageName").parent().find("a.ui-button").button("disable");	   
	  }
	  
	  <s:if test="%{mode=='reject'||rejectview=='reject'}">
	     jQuery("#villageName").closest(".ui-widget").find("input, button").prop("disabled", true);
	    jQuery("#villageName").parent().find("a.ui-button").button("disable");
	  </s:if>
		
	  });
</script>

<tr>
		<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="Site Address"/></span></div></td>
	</tr>
	<%try{ %>
 	<tr>
		<td class="greybox" width="13%">&nbsp;</td>
		<td class="greybox" width="13%"><s:text name="doorNum" /> :</td>
	    <td class="greybox" width="26%"><s:textfield id="plotDoorNum" name="siteAddress.plotDoorNumber" value="%{siteAddress.plotDoorNumber}" maxlength="32" /></td>
	    <td class="greybox">&nbsp;</td>
	    <td class="greybox" width="20%"><s:text name="blockNum" /> : <span class="mandatory" >*</span></td>
	    <td class="greybox"><s:textfield id="plotBlockNum" name="siteAddress.plotBlockNumber" value="%{siteAddress.plotBlockNumber}" maxlength="510" /></td>	 
		<s:hidden id="siteAddress.id" name="siteAddress.id"  />
   	</tr>
   
 	<tr>
		<td class="bluebox">&nbsp;</td>
       	<td class="bluebox"><s:text name="landmark" /> :</td>
      	<td class="bluebox"><s:textfield id="plotLandmark" name="siteAddress.plotLandmark" value="%{siteAddress.plotLandmark}"maxlength="126" /></td>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox"><s:text name="plotNum" /> : <span class="mandatory" >*</span></td>
      	<td class="bluebox"><s:textfield id="sitePlotNum" name="siteAddress.plotNumber" value="%{siteAddress.plotNumber}" maxlength="32" /></td>

	</tr>
             
    <tr>
      	<td class="greybox">&nbsp;</td>
      	<td class="greybox"><s:text name="surveyNum" /> : <span class="mandatory" >*</span></td>
      	<td class="greybox"><s:textfield id="sitePlotSurveyNum" name="siteAddress.plotSurveyNumber" value="%{siteAddress.plotSurveyNumber}" maxlength="250" /></td>
		<td class="greybox">&nbsp;</td>
		<div id="villageDiv">
		<td class="greybox"><s:text name="Reg.villageName" /> : <span class="mandatory" >*</span></td>
		<td class="greybox">
		 <s:select name="siteAddress.villageName.id" id="villageName" value="%{siteAddress.villageName.id}" 
				list="dropdownData.villageNameList" listKey="id" listValue="name" headerKey="-1" headerValue="----choose---" />
				</td>
				</div>
	</tr>             
		<tr>
  		<td class="bluebox">&nbsp;</td>
		<td class="bluebox"><s:text name="townOrCityName" /> : <span class="mandatory" >*</span></td>
        <td class="bluebox"><s:textfield name="siteAddress.cityTown" value="%{siteAddress.cityTown}" id="cityTown" /></td>
        <td class="bluebox">&nbsp;</td>
		<td class="bluebox"><s:text name="stateName" /> : <span class="mandatory" >*</span></td>
        <td class="bluebox" width=""><s:select headerKey="-1" headerValue="----choose-----" name="boundaryStateId" id="boundaryStateId" listKey="id" listValue="name" list="dropdownData.bndryStateList"
				cssClass="selectnew" value="%{boundaryStateId}"/>  
		</td>

	</tr>
	<tr>
	<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="pincode" /> : <span class="mandatory" >*</span></td>
        <td class="greybox"><s:textfield id="pincode" name="siteAddress.pincode" value="%{siteAddress.pincode}" maxlength="6" onblur="return validatePincodeValue(this);" /></td>
        <td class="greybox">&nbsp;</td>
        <td class="greybox">&nbsp;</td>
        <td class="greybox">&nbsp;</td>
        <td class="greybox">&nbsp;</td>
	</tr>
             
	<tr id="buildingCategory">
		<td class="bluebox">&nbsp;</td>
        <td class="bluebox"><s:text name="existingBuildingCategory" /> :</td>
        <td class="bluebox"><s:select headerKey="-1"
				headerValue="-----Choose-----" name="existingbuildingCategoryId" id="existingbuildingCategoryId"
				listKey="id" listValue="code" list="dropdownData.existingBuildingCategoryList"
				cssClass="selectnew" 
				value="%{existingbuildingCategoryId}" /></td>
		<td class="bluebox">&nbsp;</td>
		 <td class="bluebox"><s:text name="proposedBuildingCategory" /> :</td>
         <td class="bluebox"><s:select headerKey="-1"
				headerValue="-----Choose-----" name="proposedbuildingCategoryId" id="proposedbuildingCategoryId"
				listKey="id" listValue="code" list="dropdownData.proposedBuildingCategoryList"
				cssClass="selectnew" 
				value="%{proposedbuildingCategoryId}" /></td>

	</tr> 
	
	
<%}catch(Exception e) 
{System.out.println("---------------------"+e);}%>

 