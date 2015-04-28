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
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<%@ include file="/includes/taglibs.jsp" %>

<html>

<head>
<style>
.dropdown
{
background: #fff;
position: absolute;
color: #555;
margin: 0px 0px 0 0;
width: 120px;
position: relative;
height: 25px;
text-align:left;
}

input.buttonsubmit { 
 	background-image: url("../../images/buttonbg.gif");
    border-style: none;
    color: #333333;
    font-family: Arial,Helvetica,sans-serif;
    font-size: 12px;
    font-weight: bold;
    height: 27px;
    text-align: center;
    width: 133px;
	}


.small-button {
   font-size: .8em !important;
}
 </style>

<title>
	<s:text name="Site Inspection Details"/>
</title>	

<sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true"/>
<script type="text/javascript">

jQuery.noConflict();

jQuery(document).ready(function(){

	jQuery('#InspTabs').tabs();

var servictypeid=<s:property value='%{registration.serviceType.code}'/>


jQuery.subscribe('openTopicDialog', function(event,ui) {
	      
		});
    


 //var tabs = jQuery('div[id^="tab"]').tabs();

jQuery('#showAppDateDivId').hide();
jQuery('#showInspecteddBy').hide();
jQuery('#idshowinspectNumberDiv').hide();
jQuery('#showInspecteddByheader').hide();
jQuery('#idshowinspectNumberDivheader').hide(); 

if( jQuery('#mode').val()=="view"){
for(var i=0;i<document.forms[0].length;i++)
			{
				document.forms[0].elements[i].disabled =true;
			}
			
			var inspectionnum=jQuery("#inspectionNum").val();
		if( jQuery('#fromreg').val()=="true"){
           //  window.opener.callinspectionmeasurementdetails('<s:property value="registrationId"/>');
              }  
              
       refreshInbox();       
}

   jQuery.subscribe('beforeDiv', function(event,data) {		
    });
    
    
 jQuery.subscribe('completeDiv', function(event,data) { 	
 	disable();
    });

});





function disable(){
if( jQuery('#mode').val()=="view"){
for(var i=0;i<document.forms[0].length;i++)
			{
				document.forms[0].elements[i].disabled =true;
			}
jQuery("#close").removeAttr('disabled');
}

}

function validationmethod(){
var count=0;
var serviceTypeCode=document.getElementById('serviceTypeCode').value;
if(jQuery('#serviceType').val()!="2"){
 if( jQuery('#remarks').val()==""){
   showerrormsg(jQuery('#remarks'),"Inspection Remarks is mandatory");
  count++;
   }
  }
   if(count!=0)
		return false;


 if( jQuery('#inspectionDate').val()==""){
   showerrormsg(jQuery('#inspectionDate'),"Inspection Date is mandatory");
  count++;
   }
   if(count!=0)
		return false;

	var layoutType=jQuery('#layoutType').val();
	var landUsage=jQuery('#landUsage').val();
	<s:if test="%{(serviceTypeCode==@org.egov.bpa.constants.BpaConstants@NEWBUILDINGONVACANTPLOTCODE) ||(serviceTypeCode==@org.egov.bpa.constants.BpaConstants@DEMOLITIONRECONSTRUCTIONCODE) ||(serviceTypeCode==@org.egov.bpa.constants.BpaConstants@ADDITIONALCONSTRUCTIONCODE) }">
	if(!(document.getElementById("landZoningCONTINOUS").checked || document.getElementById("landZoningDETACHED").checked ||
			document.getElementById("landZoningEWS").checked)){
		showerrormsg(jQuery('#landZoning'),"Zoning of Land is mandatory");
		return false;
	}
	
	if(layoutType!=undefined && (layoutType==null || layoutType=="-1")){
		showerrormsg(jQuery('#layoutType'),"Type of Layout is mandatory");
		return false;
	}

    var elem = document.getElementById('layoutType');
    if(elem.options[elem.selectedIndex].value!="-1"){
		if(elem.options[elem.selectedIndex].innerHTML=="UnApproved Layout"){
			var surveyorLogin=document.getElementById('isSurveyor').value; 
			if(document.getElementById('lndOsrLandExtent').value == "" || document.getElementById('lndOsrLandExtent').value==0){
				showerrormsg(jQuery('#lndOsrLandExtent'),"OSR Land Extent is mandatory and can not be 0 for UnApproved Layout");
				return false;
			}

			if(document.getElementById('lndOsrLandExtent').value != "" && document.getElementById('lndOsrLandExtent').value>=3000){
				if(document.getElementById('lndGuideLineValue').value == "" || document.getElementById('lndGuideLineValue').value==0){
					showerrormsg(jQuery('#lndGuideLineValue'),"GuideLine Value is mandatory and can not be 0 if OSR Land Extent is greater than 3000");
					return false;
				}
				if(surveyorLogin!='true'){
					if(document.getElementById('lndPenaltyPeriod').value == "" || document.getElementById('lndPenaltyPeriod').value==0){
						showerrormsg(jQuery('#lndPenaltyPeriod'),"Penalty Period in Half Years is mandatory and can not be 0 if OSR Land Extent is greater than 3000");
						return false;
					}

					if(document.getElementById('lndRegularizationArea').value == "" || document.getElementById('lndRegularizationArea').value==0){
						showerrormsg(jQuery('#lndRegularizationArea'),"Area For Regularization(Sq. Mt.) is mandatory and can not be 0 if OSR Land Extent is greater than 3000");
						return false;
					}
				} 
		  	}
		}
	 }

   if(landUsage!=undefined && (landUsage==null || landUsage=="-1")){
		showerrormsg(jQuery('#landUsage'),"Land Usage is mandatory");
		return false;
	}

    if(document.getElementById('lndMinPlotExtent').value == "" || document.getElementById('lndMinPlotExtent').value==0){
		showerrormsg(jQuery('#lndMinPlotExtent'),"Plot Extent is mandatory and can not be 0");
		return false;
	}

    if(document.getElementById('bldngRoadWidth').value == "" || document.getElementById('bldngRoadWidth').value==0){
		showerrormsg(jQuery('#bldngRoadWidth'),"Road Width is mandatory and can not be 0");
		return false;
	}
	
    <s:if test="%{!isUserMappedToSurveyorRole()}">
    
	var regCharges=document.getElementById('lndIsRegularisationCharges').value;
	if(regCharges!=undefined && (regCharges== "" || regCharges=="-1")){
		showerrormsg(jQuery('#lndIsRegularisationCharges'),"Is Regularisation Charges - Land Applicable is mandatory");
		return false;
	}

	var improvementCharges=document.getElementById('bldngIsImprovementCharges').value;
	if(improvementCharges!=undefined && (improvementCharges== "" || improvementCharges=="-1")){
		showerrormsg(jQuery('#bldngIsImprovementCharges'),"Is Tentative improvement charges Applicable? is mandatory");
		return false;
	}

	var bldndRegCharges=document.getElementById('bldngIsRegularisationCharges').value;
	if(bldndRegCharges!=undefined && (bldndRegCharges== "" || bldndRegCharges=="-1")){
		showerrormsg(jQuery('#bldngIsRegularisationCharges'),"Is Regularisation Charges (penalty under section 244A) Applicable? is mandatory");
		return false;
	}

	if(document.getElementById('bldngRoadWidth').value!="" && document.getElementById('bldngRoadWidth').value>=12.21){
		var bldngStormWaterDrain=jQuery('#bldngStormWaterDrain').val();
		if(bldngStormWaterDrain!=undefined && (bldngStormWaterDrain==null || bldngStormWaterDrain=="-1")){
			showerrormsg(jQuery('#bldngStormWaterDrain'),"Storm Water Drain is mandatory if Road Width is greater than 12.21");
			return false;
		}
	}
   </s:if>

	if(document.getElementById('bldngBuildUpArea').value == "" || document.getElementById('bldngBuildUpArea').value==0){
		showerrormsg(jQuery('#bldngBuildUpArea'),"Total Built Up Area is mandatory and can not be 0");
		return false;
	}
	if(document.getElementById('bldngProposedPlotFrntage').value == "" || document.getElementById('bldngProposedPlotFrntage').value==0){
		showerrormsg(jQuery('#bldngProposedPlotFrntage'),"Proposed Plot Frontage is mandatory and can not be 0");
		return false;
	}	
	if(document.getElementById('bldngCompoundWall').value == "" || document.getElementById('bldngCompoundWall').value==0){
		showerrormsg(jQuery('#bldngCompoundWall'),"Compound Wall is mandatory and can not be 0");
		return false;
	}

	if(document.getElementById('bldngWellOht_SumpTankArea').value == "" || document.getElementById('bldngWellOht_SumpTankArea').value==0){
		showerrormsg(jQuery('#bldngWellOht_SumpTankArea'),"Well/OHT/ Sump&Septic Tank Area is mandatory and can not be 0");
		return false;
	}
	<s:if test="%{(serviceTypeCode==@org.egov.bpa.constants.BpaConstants@DEMOLITIONRECONSTRUCTIONCODE) }">
	if(document.getElementById('bldngProposedBldngArea').value == "" || document.getElementById('bldngProposedBldngArea').value==0){
		showerrormsg(jQuery('#bldngProposedBldngArea'),"Proposed Building Area is mandatory and can not be 0");
		return false;
	}
	</s:if>
	var bTypeElem = document.getElementById('buildingType');
	if(bTypeElem.options[bTypeElem.selectedIndex].value=="-1"){
		showerrormsg(jQuery('#buildingType'),"Type Of Building is mandatory");
		return false;
	}
	
    if(bTypeElem.options[bTypeElem.selectedIndex].value!="-1"){
		if(bTypeElem.options[bTypeElem.selectedIndex].innerHTML=="Mixed Residential"){
			if(document.getElementById('bldngCommercial').value == "" || document.getElementById('bldngCommercial').value==0){
				showerrormsg(jQuery('#bldngCommercial'),"Commercial is mandatory and can not be 0 for Mixed Residential Building");
				return false;
			}

			if(document.getElementById('bldngResidential').value == "" || document.getElementById('bldngResidential').value==0){
				showerrormsg(jQuery('#bldngResidential'),"Residential is mandatory and can not be 0 for Mixed Residential Building");
				return false;
			}
		}
		if(bTypeElem.options[bTypeElem.selectedIndex].innerHTML=="Residential" ||
				bTypeElem.options[bTypeElem.selectedIndex].innerHTML=="Mixed Residential"){
			if(document.getElementById('dwellingUnit').value == "" || document.getElementById('dwellingUnit').value==0){
				showerrormsg(jQuery('#dwellingUnit'),"Dwelling Unit is mandatory and can not be 0 for Residential and Mixed Residential Building");
				return false;
			} 
		}
	 }
	</s:if>
   	var todaysDate=getTodayDate();
	var inspectionDate=document.getElementById('inspectionDate').value;	
	var applicationDate=document.getElementById('applicationDate').value;	
	var lastInspectionDate=document.getElementById('lastInspectionDate').value;
	var surroundedByNrth=jQuery('#surroundedByNorth').val();
	var surroundedBySuth=jQuery('#surroundedBySouth').val();	
   
 // if(new Date(jQuery('#inspectionDate').val()).getTime() < new Date(jQuery('#applicationDate').val()).getTime() ){
 	if(inspectionDate!=null && inspectionDate!="" && inspectionDate!=undefined &&
 		 	 applicationDate!=null && applicationDate!="" && applicationDate!=undefined ){

 		if(compareDate(inspectionDate,todaysDate) == -1)
		{	
		    if(lastInspectionDate==""){					  	 	
 			showerrormsg(jQuery('#inspectionDate'),"Inspection Date should not be greater than Todays Date ")
			count++;
			}
		}
 	
	 	if(compareDate(applicationDate,inspectionDate) == -1 )
		{
		   	showerrormsg(jQuery('#inspectionDate'),"Inspection Date should be greater than Registration Date :"+jQuery('#applicationDate').val())
			count++;
		}
 	}
 	<s:if test="%{(serviceTypeCode==@org.egov.bpa.constants.BpaConstants@NEWBUILDINGONVACANTPLOTCODE)||(serviceTypeCode==@org.egov.bpa.constants.BpaConstants@CMDACODE)  ||(serviceTypeCode==@org.egov.bpa.constants.BpaConstants@DEMOLITIONRECONSTRUCTIONCODE) ||(serviceTypeCode==@org.egov.bpa.constants.BpaConstants@ADDITIONALCONSTRUCTIONCODE) }">
	
 		if(jQuery('#constructionstage').val()==null || jQuery('#constructionstage').val()=="-1" )
 	 	{
 			showerrormsg(jQuery('#constructionstage'),"Stages of Construction is mandatory")
 			return false;
 	 	}
 	 	</s:if>
 	if(surroundedByNrth!=undefined && (surroundedByNrth==null || surroundedByNrth=="-1")){
 		showerrormsg(jQuery('#surroundedByNorth'),"surrounded By North is mandatory")
		return false;
	}
 	if(surroundedBySuth!=undefined &&  (surroundedBySuth==null || surroundedBySuth=="-1")){
 		showerrormsg(jQuery('#surroundedBySouth'),"surrounded By South is mandatory")
		return false; 
	}

 	<s:if test="%{!isUserMappedToSurveyorRole() && ((serviceTypeCode==@org.egov.bpa.constants.BpaConstants@NEWBUILDINGONVACANTPLOTCODE) ||(serviceTypeCode==@org.egov.bpa.constants.BpaConstants@DEMOLITIONRECONSTRUCTIONCODE) ||(serviceTypeCode==@org.egov.bpa.constants.BpaConstants@ADDITIONALCONSTRUCTIONCODE)) }">
	if(!(document.getElementById("statusOfApplicantOWNER").checked || document.getElementById("statusOfApplicantLASSEE").checked ||
 				document.getElementById("statusOfApplicantPOWER_OF_ATTORNEY").checked)){
 			showerrormsg(jQuery('#statusOfApplicant'),"Please select Proposed activity is permissible details from docket sheet tab.");
 			return false;
 		}
 	</s:if> 
 	
 	/*if(serviceTypeCode=='02'){ // commenting cos as of now they dont need docket sheet for servicetype 2
 		if(document.getElementById('floorCount').value == "" || document.getElementById('floorCount').value==0){
 			showerrormsg(jQuery('#floorCount')," Total Number is memdatory and can not be 0");
 			return false;
 		}
 		/*if(document.getElementById('terraced').value == "" || document.getElementById('terraced').value==null){
 			showerrormsg(jQuery('#terraced')," Terraced Value is mendatory");
 			return false;
 		}
 		if(document.getElementById('tiledRoof').value == "" || document.getElementById('tiledRoof').value==null){
 			showerrormsg(jQuery('#tiledRoof')," Tiled roof Value is mendatory");
 			return false;
 		}
 		if(document.getElementById('lengthOfCompoundWall').value == "" || document.getElementById('lengthOfCompoundWall').value==null){
 			showerrormsg(jQuery('#lengthOfCompoundWall')," Length Of CompoundWall Value is mendatory");
 			return false;
 		}
 		if(document.getElementById('diameterOfWell').value == "" || document.getElementById('diameterOfWell').value==null){
 			showerrormsg(jQuery('#diameterOfWell')," Diameter of Wall Value is mendatory");
 			return false;
 		}
 		if(document.getElementById('seperateLatORTank').value == "" || document.getElementById('seperateLatORTank').value==null){
 			showerrormsg(jQuery('#seperateLatORTank')," Seperate Latrine OR Tank Value is mendatry");
 			return false;
 		}
 		if(!validateFloorDetail())
 	 		{
				return false;
 	 		}
 	}*/
	if(inspectionDate!=null && inspectionDate!="" && inspectionDate!=undefined 
			&& lastInspectionDate!=null && lastInspectionDate!="" && lastInspectionDate!=undefined ){		
		if(jQuery('#lastInspectionDate').val()!=""){
			//if(new Date(jQuery('#inspectionDate').val()).getTime() <= new Date(jQuery('#lastInspectionDate').val()).getTime() ){
			 if(compareDate(lastInspectionDate,inspectionDate) == -1){
			   showerrormsg(jQuery('#inspectionDate'),"Inspection Date should be greater than previous Inspection Date :"+jQuery('#lastInspectionDate').val())
			count++;
			}
		}
	}



if(count!=0)
		return false;
		
    jQuery("[id=numbers]").each(function(index) {	
	var values=(jQuery(this).find('input').attr("value"));
	if(values!=null&&values!=""){
   		var check=checkUptoTwoDecimalPlace(values,"shop_error",jQuery(this).find('input').attr("id"),jQuery(this).find('input'));
   		
   		if(check==false){
   		count++;
   		}
   		}
   		});
   		
   		if(count!=0)
		return false;
   	jQuery("[id=wholenumbers]").each(function(index) {	
	var values=(jQuery(this).find('input').attr("value"));
	if(values!=null&&values!=""){
   		var check=checkSpecialCharacters(values,"shop_error",jQuery(this).find('input').attr("id"),jQuery(this).find('input'));
   			if(check==false){
   		count++;
   		}
   			var check=checkNumbers(values,"shop_error",jQuery(this).find('input').attr("id"),jQuery(this).find('input'))
   			if(check==false){
   		count++;
   		}
   		
   		}
   		});	
   		
   		if(count!=0)
		return false;
   	jQuery('input[type=text], textarea').each(function(index) {	
	var values=(jQuery(this).attr("value"));
	if(values!=null&&values!=""){	
	if(values.length>256){
   		showerrormsg(jQuery(this),"Size cannot be greater than 256 characters");
   		 count++;
   		 }
   		}
   		});		
   		
   		if(count!=0)
		return false;
		
   jQuery("[id=mandatorycheck]").each(function(index) {	
  
	var values=(jQuery(this).find('input').attr("value"));
	if(values=='true'){
   		if(jQuery(this).find(':checkbox').prop('checked')){
   		}else{ 		
   		
   		jQuery(this).find(':checkbox').css('outline-color', 'red');
		jQuery(this).find(':checkbox').css('outline-style', 'solid');
		jQuery(this).find(':checkbox').css('outline-width', 'thin');
		showerrormsgwithoutcolor("Please select the other details")
	    count++;
   		}
   		}
   		});		
   		
   		if(count!=0)
		return false;


    jQuery("[id=mandatoryfields]").each(function(index) {	
  
	var values=(jQuery(this).next('td').find('input').attr("value"));
	if(values==""){
		<s:if test="%{!isUserMappedToSurveyorRole() && ((serviceTypeCode==@org.egov.bpa.constants.BpaConstants@NEWBUILDINGONVACANTPLOTCODE) ||(serviceTypeCode==@org.egov.bpa.constants.BpaConstants@DEMOLITIONRECONSTRUCTIONCODE) ||(serviceTypeCode==@org.egov.bpa.constants.BpaConstants@ADDITIONALCONSTRUCTIONCODE))}">
	showerrormsg(jQuery(this).next('td').find('input'),"Please enter all mandatory fields from \"Inspection\" and \"Docket Sheet Details\" Tab");
	count++;
	</s:if>
	<s:else>
	showerrormsg(jQuery(this).next('td').find('input'),"Please enter all mandatory fields from \"Inspection\" Tab");
	count++;
	</s:else>
 	}
 	});			
  

if(count==0){
return true;

}
else return false;

return false; 
}


function validation(){
  hideFieldError();
  dom.get("shop_error").style.display='none';

  if(!validationmethod()){
  return false;
  }else {
return true;
 
}
}


function showerrormsg(obj,msg){
dom.get("shop_error").style.display = '';
document.getElementById("shop_error").innerHTML =msg;
jQuery(obj).css("border", "1px solid red");		
return false;
}


function showerrormsgwithoutcolor(msg){
dom.get("shop_error").style.display = '';
document.getElementById("shop_error").innerHTML =msg;
return false;
}

function hideFieldError()
  {
			if (dom.get("fieldError") != null)
			dom.get("fieldError").style.display = 'none';
			
			jQuery("[id=datedetails]").each(function(index) {
			jQuery(this).find('input').css("border", "");
			});
			
		jQuery("[id=numbers]").each(function(index) {
		jQuery(this).find('input').css("border", "");
		});
		
		  jQuery("[id=mandatorycheck]").each(function(index) {	
   		jQuery(this).find(':checkbox').css('outline-color', '');
		jQuery(this).find(':checkbox').css('outline-style', '');
		jQuery(this).find(':checkbox').css('outline-width', '');
   		
   		});	
   		
   		  jQuery("[id=mandatoryfields]").each(function(index) {	
   		  jQuery(this).next('td').find('input').css("border", "");
   		  });	
 }
 
 function enableFields(){
	for(var i=0;i<document.forms[0].length;i++)
	{
		document.forms[0].elements[i].disabled =false;	
	}
}

//javascript for tabs

 function showInspFeeDtlsTab(){
 	document.getElementById('inspectionTab').style.display='none';
  	document.getElementById('docketTab').style.display='';
 	
    setCSSClasses('inspectionTab','First');
    setCSSClasses('docketTab','Last Active ActiveLast');
 }

 function setCSSClasses(id,classes){
    document.getElementById(id).setAttribute('class',classes);
    document.getElementById(id).setAttribute('className',classes);
 }

 function makeDwelUnit_CommerResdntlMandatory(){
	 var surveyorLogin=document.getElementById('isSurveyor').value; 
	 jQuery('#bldng_DetailsTbl').find("td").each(function(){
	  var bTypeElem = document.getElementById('buildingType');
	  if(bTypeElem!=null){
	  if(bTypeElem.options[bTypeElem.selectedIndex].value!="-1"){
		  if(bTypeElem.options[bTypeElem.selectedIndex].innerHTML=="Residential" ||
				  bTypeElem.options[bTypeElem.selectedIndex].innerHTML=="Mixed Residential"){
			  if(surveyorLogin=='false'){
				  <s:if test="%{(serviceTypeCode==@org.egov.bpa.constants.BpaConstants@NEWBUILDINGONVACANTPLOTCODE ||
						 serviceTypeCode==@org.egov.bpa.constants.BpaConstants@DEMOLITIONRECONSTRUCTIONCODE || 
						 serviceTypeCode==@org.egov.bpa.constants.BpaConstants@ADDITIONALCONSTRUCTIONCODE )}">
				  	document.getElementById('dwlUnitRow').style.display='';
				  </s:if>
				} else{ 
				  document.getElementById('dwlUnitRow').style.display='';
				}	
		  } else{
			  if(surveyorLogin=='false'){
				  document.getElementById('dwlUnitRow').style.display='none';
				} else{
				  document.getElementById('dwlUnitRow').style.display='none';
				}
			  document.getElementById('dwellingUnit').value=0;
			}
		  
			if(bTypeElem.options[bTypeElem.selectedIndex].innerHTML=="Mixed Residential"){
				document.getElementById('resdntlCmrclRow').style.display='';
			} else{
				document.getElementById('resdntlCmrclRow').style.display='none';
				document.getElementById('bldngResidential').value=0;
				document.getElementById('bldngCommercial').value=0;
			}
		}
	  }	
 	})
 	
 }

 function osrLandMandatory(){
	 var layoutTypeElem = document.getElementById('layoutType');
	 if(layoutTypeElem!=null){
		 jQuery('#land_DetailsTbl').find("td").each(function(){
			 if(layoutTypeElem.options[layoutTypeElem.selectedIndex].value!="-1"){
				 if(layoutTypeElem.options[layoutTypeElem.selectedIndex].innerHTML=="UnApproved Layout"){	
					if(jQuery(this).attr('id')=='osrLandLbl'){
						 document.getElementById('osrLandRow').style.display="";
						 document.getElementById('osrLandLbl').style.display="";
						 document.getElementById('lndOsrLandExtent').style.display="";
					 }
				}
			    else{
					 document.getElementById('osrLandRow').style.display="none";
					 document.getElementById('osrLandLbl').style.display="none";
					 document.getElementById('lndOsrLandExtent').style.display="none";
					 document.getElementById('lndOsrLandExtent').value=0;
					 resetGuideLn_Pnltyvalues();
				 }	
			 }
		 })
	 }
  }

 function guideLneMandatory(){
	 var surveyorLogin=document.getElementById('isSurveyor').value; 
	 jQuery('#land_DetailsTbl').find("td").each(function(){
	 if(document.getElementById('lndOsrLandExtent')!=null){	 
	  if(document.getElementById('lndOsrLandExtent').value != "" && document.getElementById('lndOsrLandExtent').value>=3000){
		  	document.getElementById('lndGuideLineLbl').style.display="";
			document.getElementById('lndGuideLineValue').style.display="";
			document.getElementById('guideLnMsg').style.display="";
			if(surveyorLogin!='true'){
				document.getElementById('showToOfficialRow').style.display="";
			}
	    }
	  else{
		  resetGuideLn_Pnltyvalues();
		}
	 }
	})
 }

 function resetGuideLn_Pnltyvalues(){
	 var surveyorLogin=document.getElementById('isSurveyor').value; 
	 document.getElementById('lndGuideLineValue').value=0;
	 document.getElementById('lndGuideLineLbl').style.display="none";
	 document.getElementById('lndGuideLineValue').style.display="none";
	 document.getElementById('guideLnMsg').style.display="none";
	 if(surveyorLogin!='true'){
		 document.getElementById('showToOfficialRow').style.display="none";
		 document.getElementById('lndPenaltyPeriod').value=0;
		 document.getElementById('lndRegularizationArea').value=0; 
	 }
  }
 
 function strmWaterMandatory(){
	 var surveyorLogin=document.getElementById('isSurveyor').value;
	 if(surveyorLogin!='true'){
		 if(document.getElementById('bldngRoadWidth')!=null){
			 if(document.getElementById('bldngRoadWidth').value!="" && document.getElementById('bldngRoadWidth').value>=12.21){
				 document.getElementById('strmWaterRow').style.display="";
			 }else{
				 document.getElementById('strmWaterRow').style.display="none";
				 document.getElementById('bldngStormWaterDrain').value=0;
			}
		 }
	 }
 }

 function onBodyLoad(){ 
	 osrLandMandatory();
	 guideLneMandatory();
	 makeDwelUnit_CommerResdntlMandatory();
	 strmWaterMandatory();
 } 
</script> 
</head>
<body onload="onBodyLoad();">

<div class="errorstyle" id="shop_error" style="display:none;" >
</div>

<s:if test="%{hasErrors()}">
		<div class="errorstyle" id="fieldError">
			<s:actionerror />
			<s:fielderror/>
		</div>
</s:if>

<s:if test="%{hasActionMessages()}">
		<div class="errorstyle">
				<s:actionmessage />
		</div>
</s:if>

<s:form  action="inspectionExtn" theme="simple" onkeypress="return disableEnterKey(event);" onsubmit="enableFields();">
<s:token/>
<div class="formmainbox">
<s:push value="model">
<s:hidden id="createdBy" name="createdBy" value="%{createdBy.id}"/>
<s:hidden id="modifiedBy" name="modifiedBy" value="%{modifiedBy.id}"/>
<s:hidden id="createdDate" name="createdDate" value="%{createdDate}"/>
<s:hidden id="registration" name="registration" value="%{registration.id}"/>
<s:hidden id="id" name="id" value="%{id}"/>
<s:hidden id="inspectionDetails" name="inspectionDetails" value="%{inspectionDetails.id}"/>
<s:hidden id="inspectedBy" name="inspectedBy" value="%{inspectedBy.id}"/>
<s:hidden id="mode" name="mode" value="%{mode}"/>
<s:hidden id="fromreg" name="fromreg" value="%{fromreg}"/>
<s:hidden id="lastInspectionDate" name="lastInspectionDate" value="%{lastInspectionDate}"/>
<s:hidden id="serviceTypeCode" name="serviceTypeCode" value="%{serviceTypeCode}"/>
<s:hidden id="isSurveyor" name="isSurveyor" value="%{isSurveyor}"/>
<s:hidden id="lndProposedPlotExtent" name="lndProposedPlotExtent" value="%{lndProposedPlotExtent}"/>
<s:hidden id="buildingZoning" name="buildingZoning" value="%{buildingZoning}"/>
<div id="InspTabs">

<ul>
			
<li><a href="#inspectionTab" title=""><s:text name="Inspection.tabLbl"/></a></li>
<s:if test="%{!isUserMappedToSurveyorRole() &&  (serviceTypeCode==@org.egov.bpa.constants.BpaConstants@NEWBUILDINGONVACANTPLOTCODE ||
 serviceTypeCode==@org.egov.bpa.constants.BpaConstants@DEMOLITIONRECONSTRUCTIONCODE || 
 serviceTypeCode==@org.egov.bpa.constants.BpaConstants@ADDITIONALCONSTRUCTIONCODE)}">
<li><a href="#docketTab" title=""><s:text name="Inspection.DockettabLbl"/> </a></li>
  </s:if>
                   <li>    <td>
            		  			</td></li>  
</ul>

<div id="inspectionTab">
<s:if test="%{!isUserMappedToSurveyorRole() &&  (serviceTypeCode==@org.egov.bpa.constants.BpaConstants@NEWBUILDINGONVACANTPLOTCODE ||
 serviceTypeCode==@org.egov.bpa.constants.BpaConstants@DEMOLITIONRECONSTRUCTIONCODE || 
 serviceTypeCode==@org.egov.bpa.constants.BpaConstants@ADDITIONALCONSTRUCTIONCODE)}">
<sj:dialog 
    	id="measure" 
    	autoOpen="false" 
    	modal="true" 
    	title="Measurement Details"
    	openTopics="openRemoteMeasurementDialog"
    	height="500"
    	width="700"
    	dialogClass="formmainbox"
    	showEffect="slide" 
    	hideEffect="slide" 
    	onOpenTopics="openTopicDialog" cssStyle="BACKGROUND-COLOR: #ffffff"
    	onCompleteTopics="dialogopentopic" 
    	loadingText="Please Wait ...."
    	errorText="Permission Denied"
    />
<h1 class="subhead" ><s:text name="Surveyor details"/></h1>
 <div id="header" class="formmainbox">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center"> 
	    	<tr>
	 		<s:url id="measurementlink" value="/extd/inspection/inspectionExtn!showSurveyorMeasurementDetails.action" escapeAmp="false">
		       <s:param name="inspectionId" value="%{id}"></s:param>	 
		       <s:param name="registrationId" value="%{registration.id}"></s:param>	
		       <s:param name="serviceTypeId" value="%{registration.serviceType.id}"></s:param>	 
		   </s:url> 
		  	<td class="bluebox" ><div align="center"> <sj:a  onClickTopics="openRemoteMeasurementDialog" href="%{measurementlink}" button="true" buttonIcon="ui-icon-newwin">View</sj:a></div></td>
          </tr>
          </table>
        </div>  
</s:if>
 <h1 class="subhead" ><s:text name="Inspection details"/></h1>
  <div id="header" class="formmainbox">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center"> 
	  
	    	<tr id="showAppDateDivId" >
	 			<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox" width="13%"><s:text name="inspectionlbl.applicationdate"/></td> 
	   			<td class="greybox"> <sj:datepicker value="%{registration.planSubmissionDate}" id="applicationDate" name="registration.planSubmissionDate" displayFormat="dd/mm/yy" disabled="true" showOn="focus"/></td>   			
				<td class="greybox" ><s:text name="inspectionlbl.sevicetype"/></td>
				<td class="greybox" ><s:select id="serviceType" name="serviceType" list="dropdownData.servicetypeList" listKey="id" listValue="code" disabled="true"  value="registration.serviceType.id" headerKey="-1" headerValue="----Choose------"/></td>
          </tr>
	      
	   	 	<tr>
	 			<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="13%"><s:text name="inspectionlbl.siteinspecteddate"/><span class="mandatory">*</span></td> 
	   			<td class="bluebox"> <sj:datepicker   value="%{inspectionDate}" id="inspectionDate" name="inspectionDate" displayFormat="dd/mm/yy" showOn="focus" /></td>   		
				<td id="showInspecteddByheader" class="bluebox" ><s:text name="inspectionlbl.siteinspectedby"/></td>
				<td id="showInspecteddBy" class="bluebox" ><s:textfield  id="inspectedbyname" name="inspectedBy.userName" value="%{inspectedBy.userName}" /></td>
				 <td class="bluebox" >&nbsp;</td>
		  <td class="bluebox" >&nbsp;</td>
		  
		    </tr>  
	       
          	<tr>
	 			<td class="greybox" width="20%">&nbsp;</td> 
				<td class="greybox" width="13%"><s:text name="inspectionlbl.siteinspectedremarks"/><span class="mandatory">*</span></td>
	   			<td class="greybox"><s:textarea  id="remarks" name="inspectionDetails.remarks" value="%{inspectionDetails.remarks}" cols="20" rows="3"/></td>   			
				<td id="idshowinspectNumberDivheader" class="greybox" ><s:text name="Inspection Number"/></td>
				<td id="idshowinspectNumberDiv"  class="greybox" ><s:textfield  id="inspectionNum" name="inspectionNum" value="%{inspectionNum}" disabled="true"/></td>
			 <td class="greybox" >&nbsp;</td>
		  <td class="greybox" >&nbsp;</td>
		  
	         </tr>  
	      </table>
	 </div>
	<s:if test="%{(serviceTypeCode==@org.egov.bpa.constants.BpaConstants@NEWBUILDINGONVACANTPLOTCODE ||
 serviceTypeCode==@org.egov.bpa.constants.BpaConstants@DEMOLITIONRECONSTRUCTIONCODE || 
 serviceTypeCode==@org.egov.bpa.constants.BpaConstants@ADDITIONALCONSTRUCTIONCODE)}">
	      <%@ include file="inspectionExtn-landBldngDetails.jsp"%>    
	   </s:if> 
   
<div align="center"> 

	  
		<s:if test="%{!isUserMappedToSurveyorRole() &&  (serviceTypeCode==@org.egov.bpa.constants.BpaConstants@NEWBUILDINGONVACANTPLOTCODE ||
 serviceTypeCode==@org.egov.bpa.constants.BpaConstants@DEMOLITIONRECONSTRUCTIONCODE || 
 serviceTypeCode==@org.egov.bpa.constants.BpaConstants@ADDITIONALCONSTRUCTIONCODE)}"> 
		<sj:dialog 
		    	id="measure" 
		    	autoOpen="false" 
		    	modal="true" 
		    	title="Measurement Details"
		    	openTopics="openRemoteMeasurementDialog"
		    	height="500"
		    	width="700"
		    	dialogClass="formmainbox"
		    	showEffect="slide" 
		    	hideEffect="slide" 
		    	onOpenTopics="openTopicDialog" cssStyle="BACKGROUND-COLOR: #ffffff"
		    	onCompleteTopics="dialogopentopic" 
		    	loadingText="Please Wait ...."
		    	errorText="Permission Denied"
		    />
		</s:if>
	  
	   <s:url id="ajax" value="/extd/inspection/inspectionExtn!showPlanDetails.action"  escapeAmp="false">
	   <s:param name="serviceTypeId" value="%{registration.serviceType.id}"></s:param>	 
	     <s:param name="inspectionId" value="%{inspectionId}"></s:param>	         
	   </s:url>
   
       <sj:div href="%{ajax}" indicator="indicator"  cssClass="formmainbox" id="tab1"  dataType="html" onCompleteTopics="completeDiv">
        <img id="indicator" src="<egov:url path='/images/loading.gif'/>" alt="Loading..." style="display:none"/> 
      </sj:div> 
        
      <s:url id="consajax" value="/extd/inspection/inspectionExtn!showConstructionDetails.action"  escapeAmp="false">
	   <s:param name="serviceTypeId" value="%{registration.serviceType.id}"></s:param>	   
	   <s:param name="inspectionId" value="%{inspectionId}"></s:param>	   
	   </s:url>
       <sj:div href="%{consajax}" indicator="indicator"  cssClass="formmainbox" id="tab2" dataType="html" onCompleteTopics="completeDiv">
        <img id="indicator" src="<egov:url path='/images/loading.gif'/>" alt="Loading..." style="display:none"/>
      </sj:div> 
    
     <s:url id="checklistajax" value="/extd/inspection/inspectionExtn!showCheckList.action"  escapeAmp="false">
	  <s:param name="serviceTypeId" value="%{registration.serviceType.id}"></s:param>	
	   <s:param name="inspectionId" value="%{inspectionId}"></s:param>	       
	   </s:url>
       <sj:div href="%{checklistajax}" indicator="indicator"  cssClass="formmainbox" id="tab3"  dataType="html" onCompleteTopics="completedchecklistDiv">
        <img id="indicator" src="<egov:url path='/images/loading.gif'/>" alt="Loading..." style="display:none"/>
    </sj:div>
 
    <s:url id="plotdetajax" value="/extd/inspection/inspectionExtn!showPlotDetails.action"  escapeAmp="false">
	  <s:param name="serviceTypeId" value="%{registration.serviceType.id}"></s:param>	
	   <s:param name="inspectionId" value="%{inspectionId}"></s:param>	      
	   </s:url>
       <sj:div href="%{plotdetajax}" indicator="indicator"  cssClass="formmainbox" id="tab4"  dataType="html" onCompleteTopics="completeDiv">
        <img id="indicator" src="<egov:url path='/images/loading.gif'/>" alt="Loading..." style="display:none"/>
    </sj:div> 
    
	</div>
	</div>
	
	
	
	<s:if test="%{!isUserMappedToSurveyorRole() && (serviceTypeCode==@org.egov.bpa.constants.BpaConstants@NEWBUILDINGONVACANTPLOTCODE ||
 serviceTypeCode==@org.egov.bpa.constants.BpaConstants@DEMOLITIONRECONSTRUCTIONCODE || 
 serviceTypeCode==@org.egov.bpa.constants.BpaConstants@ADDITIONALCONSTRUCTIONCODE)}">
	<div id="docketTab">
	<div align="center">
	<%@ include file="inspectionExtn-docketSheet.jsp"%> 
	</div>
	</div>
	</s:if>
	</div>
	
  <div class="buttonbottom" align="center">
		<table>
		<tr>
		 <s:if test="%{mode!='view'}">
		  	<td><s:submit type="submit" cssClass="buttonsubmit" id="save" name="save" value="Save" method="createSiteInspection" onclick="return validation()"/></td>	
	  			</s:if>	         
	  		 <td><input type="button" name="close" id="close" class="button"  value="Close" onclick="window.close();"/>
	  		</td>
	  	</tr>
        </table>
   </div>	
	
 
</div>
</s:push>	
	
</s:form>
</body>
</html>
