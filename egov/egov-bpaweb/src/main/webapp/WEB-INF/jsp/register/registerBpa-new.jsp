<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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
.submenu
{
background: #fff;
position: absolute;
top: 30px;
left: 0px;
z-index: 100;
width: 135px;
display: none;
margin-left: 0px;
padding: 0px 0 5px;
border-radius: 6px;
box-shadow: 0 2px 8px rgba(0, 0, 0, 0.45);
}
.dropdown li a
{
color: #555555;
display: block;
font-family: arial;
font-weight: bold;
padding: 6px 15px;
cursor: pointer;
text-decoration:none;
}

.dropdown li a:hover
{
background:#155FB0;
color: #FFFFFF;
text-decoration: none;
}
a.account 
{
font-size: 11px;
line-height: 16px;
color: #555;
position: absolute;
z-index: 110;
display: block;
padding: 11px 0 0 20px;
height: 28px;
width: 121px;
margin: -11px 0 0 -10px;
text-decoration: none;
background: url(icons/arrow.png) 116px 17px no-repeat;
cursor:pointer;
}
.root
{
list-style:none;
margin:0px;
padding:0px;
font-size: 11px;
padding: 11px 0 0 0px;
border-top:1px solid #dedede;
}

 </style>

  <title><s:text name='NewBPA.title'/></title>
  
  <sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true"/>
  <script type="text/javascript" src="<c:url value='/common/js/jquerycombobox.js'/>"></script>
  
  <title><s:text name='NewBPA.title'/></title>
  
   
<script type="text/javascript">

jQuery.noConflict();

jQuery(document).ready(function() {
			
  <s:if test="%{mode=='reject'||rejectview=='reject'}">
	disableRegistrationDetails();
	showRejectDetTab();
  </s:if>

  
  <s:if test="%{registration.id==null||registration.state.value=='NEW'}">
  	jQuery("#approverPositionId").remove();
  	jQuery("#approverInfo").hide();
  </s:if>
 
  /*
  <s:if test="%{registration.id==null||registration.state.value=='NEW'}">
 var approverpos = '<s:property value="zonalLevelAssistantForLoggedInUser"/>';

if(approverpos==null||approverpos==""){
	  jQuery(".buttonbottom").hide();
	  alert(" Assistant not created/configured to receive file from CFC’")
}
jQuery("#approverPositionId").remove();
  jQuery(".buttonbottom").prepend('<input type="hidden" name="approverPositionId" id="approverPositionId" value="<s:property value="zonalLevelAssistantForLoggedInUser"/>"/>');
  jQuery("#approverInfo").hide();
</s:if>
*/

jQuery("#account").click(function()
{
var X=jQuery(this).attr('id');
if(X==1)
{
jQuery(".submenu").hide();
jQuery(this).attr('id', '0'); 
}
else
{
jQuery(".submenu").show();
jQuery(this).attr('id', '1');
}

});

//Mouse click on sub menu
jQuery(".submenu").mouseup(function()
{
return false
});

//Mouse click on my account link
jQuery("#account").mouseup(function()
{
return false
});


//Document Click
jQuery(document).mouseup(function()
{
//jQuery(".submenu").hide();
jQuery("#account").attr('id', '');

});

//jQuery('div[id^="tab"]').tabs({ collapsible: true });

 // jQuery("#content").toggle(expand(),compress());

	hideExistingApplDetails();
	hideShowBuildingCategory();	
	hideShowCMDAfields();
	
	jQuery("#left").click(function() {
          
        jQuery("#content").animate(
            { "width": "2%"},
            "fast", function(){
               jQuery('#communication').hide();
            });
             jQuery("#content").css({left:left}).animate({"left":"10%"}, "fast");
});



jQuery("#right").click(function() {

       
       jQuery("#content").css({left:left}).animate({"left":"10%"}, "fast");
       
        jQuery("#content").animate(
            { "width": "80%"},
            "fast", function(){
                jQuery('#communication').show();
            });
            
           
});

}

);

function resetcombo(){

	jQuery(".ui-autocomplete-input").val(jQuery("#villageDiv option:selected").text());
}

var zoneData = "";
var wardData = "";
var areaData = "";
var locationData = "";
var streetData = "";
var oldWard,oldArea,oldLocality,oldStreet,oldOwnerFirstname,oldSpouseOrFatheName;
var oldApplicantAddress1,oldApplicantAddress2,oldPlotBlockNum,oldMobileNo,oldContactNo;
var oldCityTown,oldBoundaryStateId,oldPincode,oldEmail,oldDoorNo,oldLandmark;
var oldPlotNo,oldSitePlotSurveyNum,oldVillageName,oldSurveyorId,oldExistingBC,oldProposedBC;

function bodyOnLoad(){

	 jQuery(".buttonsubmit").each(function(index) {
		
			var values=(jQuery(this).attr("value"));
			if(values!=null&&values!=""&&values=='Close Registration'){
				jQuery(this).attr("name","method:closeRegistration");
			}
	 });
	 jQuery('#Region').parent('td').prev('td').append('<span class="mandatory">*</span>');
		jQuery('#Zone').parent('td').prev('td').append('<span class="mandatory">*</span>');
		jQuery('#Ward').parent('td').prev('td').append('<span class="mandatory">*</span>');
	var mode=document.getElementById('mode').value;
	if(mode=='noEdit'){
		disableRegistrationDetails();
	}
	else if(mode=='view' )
	{		
		jQuery('#showjsp').show();
		jQuery('#hidejsp').hide();
		for(var i=0;i<document.forms[0].length;i++)
		{
			if( document.forms[0].elements[i].name!='close' && document.forms[0].elements[i].id!='print')
				document.forms[0].elements[i].disabled =true;
		}
	} 
	else if(mode=='edit'){
		jQuery('#showjsp').show();
		 jQuery('#hidejsp').hide();
		 hideShowBuildingCategory();
		 hideExistingApplDetails();
		 hideShowCMDAfields();
		/* if(document.getElementById('autoDcrNum').value!=""){
			 //alert("autoDcrNum" +autoDcrNum);
		 disablefieldsforAutodcr();
		 }*/
		 document.getElementById('serviceTypeId').disabled=true;
		 document.getElementById('Zone').disabled=true;	
		 document.getElementById('Region').disabled=true;	
		 var isPlotAreaEditableValue=document.getElementById('isPlotAreaEditable').value;
		 if(isPlotAreaEditableValue=="false"){
			 document.getElementById('plotAreaInSqft').disabled=true;
			 document.getElementById('plotAreaInSqmt').disabled=true;
			}

		 }
	var addMode=document.getElementById('additionalMode').value;
	if(addMode!=null && addMode=='editApprovedRecord'){
			enabelRegistrationDetailsOnFeecollection(); 
			
			oldWard=document.getElementById('Ward').options[document.getElementById('Ward').selectedIndex].text;		
			oldArea=document.getElementById('Area').options[document.getElementById('Area').selectedIndex].text;
			oldLocality=document.getElementById('Locality').options[document.getElementById('Locality').selectedIndex].text;
			oldStreet=document.getElementById('Street').options[document.getElementById('Street').selectedIndex].text;
			oldOwnerFirstname=document.getElementById('ownerFirstname').value;
			oldSpouseOrFatheName=document.getElementById('spouseOrFatheName').value;
			oldApplicantAddress1=document.getElementById('applicantAddress1').value;
			oldApplicantAddress2=document.getElementById('applicantAddress2').value;
			oldPlotBlockNum=document.getElementById('plotBlockNum').value;
			oldMobileNo=document.getElementById('mobileNo').value;
			oldContactNo=document.getElementById('contactNo').value;
			oldCityTown=document.getElementById('cityTown').value;
			oldBoundaryStateId=document.getElementById('boundaryStateId').options[document.getElementById('boundaryStateId').selectedIndex].text;;
			oldPincode=document.getElementById('pincode').value;
			oldEmail=document.getElementById('email').value;
			oldDoorNo=document.getElementById('plotDoorNum').value;
			oldLandmark=document.getElementById('plotLandmark').value;
			oldPlotNo=document.getElementById('sitePlotNum').value;
			oldSitePlotSurveyNum=document.getElementById('sitePlotSurveyNum').value;
			oldVillageName=document.getElementById('villageName').options[document.getElementById('villageName').selectedIndex].text;
			oldSurveyorId=document.getElementById('surveyorId').options[document.getElementById('surveyorId').selectedIndex].text;
			oldExistingBC=document.getElementById('existingbuildingCategoryId').options[document.getElementById('existingbuildingCategoryId').selectedIndex].text;
			oldProposedBC=document.getElementById('proposedbuildingCategoryId').options[document.getElementById('proposedbuildingCategoryId').selectedIndex].text;
			
	}
		
	var addlstate=jQuery('#additionalState').val();		
		  if(mode!='reject'||addlstate=='RejectedBPA'||addlstate=='UnconsiderdBPA'){
		
			  jQuery('#rejectDetForm').find('input,select,textarea').attr('disabled','true');
		 }

			 var dept='<s:property value="departmentForLoggedInUser"/>';
				 if(dept!=null){
				jQuery("#approverDepartment option[value!='<s:property value="departmentForLoggedInUser"/>']").each(function() {
				if(jQuery(this).val()!=-1)
				    jQuery(this).remove();
			});
				 }
			 
}
function regObjecthistoryLog() {
	var message="";

	var newValue=document.getElementById('Ward').options[document.getElementById('Ward').selectedIndex].text;
	if(newValue!=oldWard ){	
		message=message+"Ward:"+oldWard+" to "+newValue +"| \n";
	}
	//oldArea=document.getElementById('Area').value;
	newValue=document.getElementById('Area').options[document.getElementById('Area').selectedIndex].text;
	if(newValue!=oldArea ){	
		message=message+"Area:"+oldArea+" to "+newValue +"| \n";
	}
	
	//oldLocality=document.getElementById('Locality').value;
	newValue=document.getElementById('Locality').options[document.getElementById('Locality').selectedIndex].text;
	if(newValue!=oldLocality ){	
		message=message+"Locality:"+oldLocality+" to "+newValue +"| \n";
	}
	
	//oldStreet=document.getElementById('Street').value;
	newValue=document.getElementById('Street').options[document.getElementById('Street').selectedIndex].text;
	if(newValue!=oldStreet ){	
		message=message+"Street:"+oldStreet+" to "+newValue +"| \n";
	}
	//oldOwnerFirstname=document.getElementById('ownerFirstname').value;
	newValue=document.getElementById('ownerFirstname').value;
	if(newValue!=oldOwnerFirstname ){	
		message=message+"Applicant Name:"+oldOwnerFirstname+" to "+newValue +"| \n";
	}
	//oldSpouseOrFatheName=document.getElementById('spouseOrFatheName').value;
	newValue=document.getElementById('spouseOrFatheName').value;
	if(newValue!=oldSpouseOrFatheName ){	
		message=message+"Father/Husband Name:"+oldSpouseOrFatheName+" to "+newValue +"| \n";
	}
	//oldApplicantAddress1=document.getElementById('applicantAddress1').value;
	newValue=document.getElementById('applicantAddress1').value;
	if(newValue!=oldApplicantAddress1 ){	
		message=message+"Applicant Address 1:"+oldApplicantAddress1+" to "+newValue +"| \n";
	}
	//oldApplicantAddress2=document.getElementById('applicantAddress2').value;
	newValue=document.getElementById('applicantAddress2').value;
	if(newValue!=oldApplicantAddress2 ){	
		message=message+"Applicant Address 2:"+oldApplicantAddress2+" to "+newValue +"| \n";
	}
	//oldPlotBlockNum=document.getElementById('plotBlockNum').value;
	newValue=document.getElementById('plotBlockNum').value;
	if(newValue!=oldPlotBlockNum ){	
		message=message+"Block Number:"+oldPlotBlockNum+" to "+newValue +"| \n";
	}
	//oldMobileNo=document.getElementById('mobileNo').value;
	newValue=document.getElementById('mobileNo').value;
	if(newValue!=oldMobileNo ){	
		message=message+"MobileNo:"+oldMobileNo+" to "+newValue +"| \n";
	}
	//oldContactNo=document.getElementById('contactNo').value;
	newValue=document.getElementById('contactNo').value;
	if(newValue!=oldContactNo ){	
		message=message+"ContactNo:"+oldContactNo+" to "+newValue +"| \n";
	}
	//oldCityTown=document.getElementById('cityTown').value;
	newValue=document.getElementById('cityTown').value;
	if(newValue!=oldCityTown ){	
		message=message+"City/Town:"+oldCityTown+" to "+newValue +"| \n";
	}
	
	//oldBoundaryStateId=document.getElementById('boundaryStateId').value;
	newValue=document.getElementById('boundaryStateId').options[document.getElementById('boundaryStateId').selectedIndex].text;
	if(newValue!=oldBoundaryStateId ){	
		message=message+"State:"+oldBoundaryStateId+" to "+newValue +"| \n";
	}
	//oldPincode=document.getElementById('pincode').value;
	newValue=document.getElementById('pincode').value;
	if(newValue!=oldPincode ){	
		message=message+"Pincode:"+oldPincode+" to "+newValue +"| \n";
	}
	//oldEmail=document.getElementById('email').value;
	newValue=document.getElementById('email').value;
	if(newValue!=oldEmail ){	
		message=message+"Email:"+oldEmail+" to "+newValue +"| \n";
	}
	//oldDoorNo=document.getElementById('plotDoorNum').value;
	newValue=document.getElementById('plotDoorNum').value;
	if(newValue!=oldDoorNo ){	
		message=message+"DoorNumber:"+oldDoorNo+" to "+newValue +"| \n";
	}
	//oldLandmark=document.getElementById('plotLandmark').value;
	newValue=document.getElementById('plotLandmark').value;
	if(newValue!=oldLandmark ){	
		message=message+"Landmark:"+oldLandmark+" to "+newValue +"| \n";
	}
	oldPlotNo=document.getElementById('sitePlotNum').value;
	newValue=document.getElementById('sitePlotNum').value;
	if(newValue!=oldPlotNo ){	
		message=message+"PlotNumber:"+oldPlotNo+" to "+newValue +"| \n";
	}
	oldSitePlotSurveyNum=document.getElementById('sitePlotSurveyNum').value;
	newValue=document.getElementById('sitePlotSurveyNum').value;
	if(newValue!=oldSitePlotSurveyNum ){	
		message=message+"SurveyNumber:"+oldSitePlotSurveyNum+" to "+newValue +"| \n";
	}
	//oldVillageName=document.getElementById('villageName').value;
	newValue=document.getElementById('villageName').options[document.getElementById('villageName').selectedIndex].text;
	if(newValue!=oldVillageName ){	
		message=message+"Village Name:"+oldVillageName+" to "+newValue +"| \n";
	}
	//oldSurveyorId=document.getElementById('surveyorId').value;
	newValue=document.getElementById('surveyorId').options[document.getElementById('surveyorId').selectedIndex].text;
	if(newValue!=oldSurveyorId ){	
		message=message+"Surveyor Name:"+oldSurveyorId+" to "+newValue +"| \n";
	}
	//oldExistingBC=document.getElementById('existingbuildingCategoryId').value;
	newValue=document.getElementById('existingbuildingCategoryId').options[document.getElementById('existingbuildingCategoryId').selectedIndex].text;
	if(newValue!=oldExistingBC ){	
		message=message+"Existing Building Category:"+oldExistingBC+" to "+newValue +"| \n";
	}
	//oldProposedBC=document.getElementById('proposedbuildingCategoryId').value;
	newValue=document.getElementById('proposedbuildingCategoryId').options[document.getElementById('proposedbuildingCategoryId').selectedIndex].text;
	if(newValue!=oldProposedBC ){	
		message=message+"Proposed building Category:"+oldProposedBC+" to "+newValue +"| \n";
	}
	document.getElementById('regObjectHistoryRemarks').value=message;
	//alert(message);
}

function enabelRegistrationDetailsOnFeecollection(){

	jQuery('#plotAddress').find('input,select,textarea').attr('disabled',false);
	jQuery('#ownerDtls').find('input,select,textarea').attr('disabled',false);

	//document.getElementById("Zone").disabled=false;
	document.getElementById("Ward").disabled=false;
	document.getElementById("Area").disabled=false;
	document.getElementById("Locality").disabled=false; 
	document.getElementById("Street").disabled=false; 

	jQuery('#surveyorDiv').find(':input').prop('disabled',false);	


	}
function disableRegistrationDetails(){
	
	 jQuery('#regForm').find('input,select,textarea').attr('disabled','true');
	 if(document.getElementById('autodcrAtag1')!=null)
	 document.getElementById('autodcrAtag1').style.display='none';
	 if(document.getElementById('autodcrAtag2')!=null)
	 document.getElementById('autodcrAtag2').style.display='none';
	 if(document.getElementById('propertyAtag')!=null)
	 document.getElementById('propertyAtag').style.display='none';
	 jQuery('#uploadForm').find('input,select,textarea').attr('disabled','true');
	 
}


function validateEmail()
{	
	dom.get("bpa_error_area").style.display='none';
	var x=document.getElementById("email").value;
	if(x!=""){
		var atpos=x.indexOf("@");
		var dotpos=x.lastIndexOf(".");
		if (atpos<1 || dotpos<atpos+2 || dotpos+2>=x.length)
		{
			dom.get("bpa_error_area").style.display = '';
			document.getElementById("bpa_error_area").innerHTML = '<s:text name="invalid.email.address" />';
			document.getElementById("email").value="";
		  return false;
		}
	}
	return true;
}
function validateContactNumber(obj)
{

	var text = obj.value;
	if(text!=''){	
	validatePhoneNumber(obj,'contact');
	}
	return true;
}


function validateMobileNumber(obj)
{

	var text = obj.value;
	if(text!=''){
		
		if(text.length!=10)
		{		
			obj.value="";
			dom.get("bpa_error_area").style.display = '';
			document.getElementById("bpa_error_area").innerHTML = '<s:text name="invalid.mobileno.length" />';
			return false;
	
		}
	validatePhoneNumber(obj,'mobile');
	}
	return true;
}

function validatePhoneNumber(obj,mode){
	var text = obj.value;
	if(text!=""){
		
	var msg;
	if(mode=='mobile')
		msg='<s:text name="invalid.mobileno" />';
	else
		msg='<s:text name="invalid.teleno" />';
	if(isNaN(text))
	{
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = msg;
		obj.value="";
		return false;
	}
	if(text<=0)
	{
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = msg;
		obj.value="";
		return false;
	}
	if(text.replace(".","~").search("~")!=-1)
	{
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="period.notallowed" />';
		obj.value='';
		return false;
	}
	if(text.replace("+","~").search("~")!=-1)
	{
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="plus.notallowed" />';
		obj.value='';
		return false;
	}
	}
	return true;
}

function validateMandatoryFields(){
	var todaysDate=getTodayDate();
	if(document.getElementById('planSubmissionDate').value==null || document.getElementById('planSubmissionDate').value==""){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="registration.planSubmissionDate.required" />';
		return false;
	}
	if(compareDate(document.getElementById("planSubmissionDate").value,todaysDate) == -1)
	{						  	 	
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="planSubmissionDate.greaterthan.todaysdate" />';
			return false;
	}
	if(document.getElementById('serviceTypeId').value==null || document.getElementById('serviceTypeId').value=="-1"){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="registration.serviceType.required" />';
		return false;
	}
	
	if(document.getElementById('appType').value==null || document.getElementById('appType').value=="-1"){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="appType.required" />';
		return false;
	}
	if(document.getElementById('appMode').value==null || document.getElementById('appMode').value=="-1"){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="appMode.required" />';
		return false;
	}
	if(document.getElementById('isPropertyMandatory').value=="true" && (document.getElementById('propertyid').value==null || document.getElementById('propertyid').value=="")){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="property.required" />';
		return false;
	}

	if(document.getElementById('isAutoDcrMandatory').value=="true" && (document.getElementById('autoDcrNum').value==null || document.getElementById('autoDcrNum').value=="")){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="autoDCRnumber.required" />';
		return false;
	}

	if(document.getElementById('isCmdaServicetype').value=="true"){
		 if(document.getElementById('cmdaProposalNumber').value=="" || document.getElementById('cmdaProposalNumber').value==null){
				dom.get("bpa_error_area").style.display = '';
				document.getElementById("bpa_error_area").innerHTML = '<s:text name="cmdaProposalNumber.required" />';
				return false;
			}

		 if(document.getElementById('cmdaRefDate').value=="" || document.getElementById('cmdaRefDate').value==null){
				dom.get("bpa_error_area").style.display = '';
				document.getElementById("bpa_error_area").innerHTML = '<s:text name="cmdaRefDate.required" />';
				return false;
		}
		/*if(compareDate(document.getElementById('planSubmissionDate').value,document.getElementById("cmdaRefDate").value) == -1 )
		{
			dom.get("bpa_error_area").style.display = '';
			document.getElementById("bpa_error_area").innerHTML = '<s:text name="cmdaRefDate.greaterthan.appdate" />';			   
			return false;
		}*/
	  	if(compareDate(document.getElementById("cmdaRefDate").value,todaysDate) == -1)
		{						  	 	
	  		dom.get("bpa_error_area").style.display = '';
			document.getElementById("bpa_error_area").innerHTML = '<s:text name="cmdaRefDate.greaterthan.todaysdate" />';			   
			return false;
		}
	}
	else if(document.getElementById('isCmdaServicetype').value=="false") {
		if(document.getElementById('admissionfeeAmount').value=="" || document.getElementById('admissionfeeAmount').value==null
				||document.getElementById('admissionfeeAmount').value==0){
			dom.get("bpa_error_area").style.display = '';
			document.getElementById("bpa_error_area").innerHTML = '<s:text name="admission.fee.amount.required" />';
			return false;
		} 
	}
 	
	if(document.getElementById('adminboundaryid').value=="" ||document.getElementById('adminboundaryid').value==null || document.getElementById('adminboundaryid').value=="-1"){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="adminboundaryid.required" />';
		return false;
	}
	if(document.getElementById('Zone').value==null || document.getElementById('Zone').value=="-1"){
		///alert("Zone"+Zone);
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="zone.required" />';
		return false;
	}
	if(document.getElementById('Ward').value==null || document.getElementById('Ward').value=="-1"){
	//	alert("Ward"+Ward);
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="ward.required" />';
		return false;
	}
	
/*	if(document.getElementById('locboundaryid').value=="" ||document.getElementById('locboundaryid').value==null || document.getElementById('locboundaryid').value=="-1"){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="locboundaryid.required" />';
		return false;
	}
	if(document.getElementById('Area').value==null || document.getElementById('Area').value=="-1"){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="area.required" />';
		return false;
	}
	if(document.getElementById('Locality').value==null || document.getElementById('Locality').value=="-1"){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="locality.required" />';
		return false;
	}
	if(document.getElementById('Street').value==null || document.getElementById('Street').value=="-1"){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="street.required" />';
		return false;
	}*/
	if(document.getElementById('ownerFirstname').value=="" || document.getElementById('ownerFirstname').value==null){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="owner.name.required" />';
		return false;
	}
	
	if(document.getElementById('spouseOrFatheName').value=="" || document.getElementById('spouseOrFatheName').value==null){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="owner.fathername.required" />';
		return false;
	}
	if(document.getElementById('applicantAddress1').value=="" || document.getElementById('applicantAddress1').value==null){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="applicantrAddress.StreetAddress1.required" />';
		return false;
	}

	var stateid=document.getElementById('state').value;
	var statevalue=document.getElementById('statevalue').value;
	if( (  stateid==null || stateid=="") || statevalue=='NEW' ){
	if(document.getElementById('mobileNo').value=="" || document.getElementById('mobileNo').value==null){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="owner.mobileNumber.required" />';
		return false;
	}
	}
	/*if(document.getElementById('mobileNo').value=="" || document.getElementById('mobileNo').value==null){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="mobileNo.required" />';
		return false;
	}
*/
/*
	if(document.getElementById('applicantAddress2').value=="" || document.getElementById('applicantAddress2').value==null){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="applicantrAddress.StreetAddress2.required" />';
		return false;
	}*/
	if(document.getElementById('plotBlockNum').value=="" || document.getElementById('plotBlockNum').value==null){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="siteAddress.PlotBlockNumber.required" />';
		return false;
	}
	if(document.getElementById('sitePlotNum').value=="" || document.getElementById('sitePlotNum').value==null){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="siteAddress.PlotNumber.required" />';
		return false;
	}
	if(document.getElementById('villageName').value==null || document.getElementById('villageName').value=="-1"){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="siteAddress.village.required" />';
		return false;
	}
	if(document.getElementById('pincode').value=="" || document.getElementById('pincode').value==null){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="siteAddress.pincode.required" />';
		return false;
	}
	if(document.getElementById('cityTown').value=="" || document.getElementById('cityTown').value==null){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="siteAddress.cityTown.required" />';
		return false;
	}
	if( document.getElementById('boundaryStateId').value==null || document.getElementById('boundaryStateId').value=="-1"){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="siteAddress.StateName.required" />';
		return false;
	}
	if(document.getElementById('sitePlotSurveyNum').value=="" || document.getElementById('sitePlotSurveyNum').value==null){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="siteAddress.PlotSurveyNumber.required" />';
		return false;
	}
	if(document.getElementById('plotAreaInSqft').value=="" || document.getElementById('plotAreaInSqft').value==null){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="regnDetails.sitalAreasqft.required" />';
		return false;
	}if(document.getElementById('plotAreaInSqmt').value=="" || document.getElementById('plotAreaInSqmt').value==null){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="regnDetails.sitalAreasqmt.required" />';
		return false;
	}
	if(document.getElementById('surveyorId').value==null ||document.getElementById('surveyorId').value=="-1"){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="surveyorName.required" />';
		return false;
	}
	   

	
	return true;
	
}

function filterAddress(obj)
{
	var objValue=obj.value; 
	objValue=objValue.replace(/\s/g,' ').replace(/  ,/g,' '); 
	obj.value=objValue; 
}

function validateForm(obj){
	
	
	document.getElementById('workFlowAction').value=obj;

	dom.get("bpa_error_area").style.display='none';
	
	 jQuery("[id=mandatorycheck]").each(function(index) {	
	   		jQuery(this).find(':checkbox').css('outline-color', '');
			jQuery(this).find(':checkbox').css('outline-style', '');
			jQuery(this).find(':checkbox').css('outline-width', '');
	   		jQuery(this).next("td").find('textarea').css("border", '');
	   		});

  jQuery("[id=mandatorycheckforregcl]").each(function(index) {	
   		jQuery(this).find(':checkbox').css('outline-color', '');
		jQuery(this).find(':checkbox').css('outline-style', '');
		jQuery(this).find(':checkbox').css('outline-width', '');
   		jQuery(this).next("td").find('textarea').css("border", '');
   		});	


	

	
 if(jQuery("#mode").val()=='reject'){
 if(jQuery("#rejectionDate").val()==''){
   dom.get("bpa_error_area").style.display='';
   document.getElementById("bpa_error_area").innerHTML = 'Please select the Unconsidered Date';
   return false;
   }

 var rejectionDates=document.getElementById('rejectionDate').value;	
	var applicationDates=document.getElementById('planSubmissionDate').value;	
 
	if(compareDate(rejectionDates,applicationDates) == 1 )
	{ 
		 dom.get("bpa_error_area").style.display='';
		   document.getElementById("bpa_error_area").innerHTML = 'Unconsidered Date should be greater than Registration Date';
		   return false;
	}
   }
	
	if(!validateMandatoryFields()){
		return false;
	}
	
	var pincodeObj=	document.getElementById('pincode');
	if(!validatePincodeValue(pincodeObj)){
		return false;
	}
	
	if(!validateEmail()){
		return false;
	}
	
	var mobileNoObj=	document.getElementById('mobileNo');
	if(!validateMobileNumber(mobileNoObj)){
		return false;
	}
	
	var contactNoObj=	document.getElementById('contactNo');
	if(!validateContactNumber(contactNoObj)){
		return false;
	}

	var addMode=document.getElementById('additionalMode').value;
	if(obj=='save' && addMode!=null && addMode=='editApprovedRecord'){
		regObjecthistoryLog();
		if(document.getElementById("regObjectHistoryRemarks").value!="" && document.getElementById("regObjectHistoryRemarks").value!=null){
			var messageLength=document.getElementById("regObjectHistoryRemarks").value.length;
			
			if(messageLength>4000){
					alert("Unable to Save the changed Log in one Transaction.");
					return false;
				}
			else{
				var r=confirm("The Below Details are modified. Do you want to continue ? \n\n" +document.getElementById("regObjectHistoryRemarks").value)
				if (r!=true){			
					return false;
				}
			}
		}
	}
	
	  var count =0;
	  jQuery("[id=mandatorycheckforregcl]").each(function(index) {	
	
		var values=(jQuery(this).find('input').attr("value"));
		
		if(values=='true'){
				if(jQuery(this).find(':checkbox').prop('checked')){
				}else{ 		  		
				dom.get("bpa_error_area").style.display = '';
					
				jQuery(this).find(':checkbox').css('outline-color', 'red');
			jQuery(this).find(':checkbox').css('outline-style', 'solid');
			jQuery(this).find(':checkbox').css('outline-width', 'thin');
			document.getElementById("bpa_error_area").innerHTML = 'Please select the Documents to Attach';		
		   count++;
				}
				}
				});		
				
				if(count!=0)
			return false;

		
				
		if(document.getElementById("mobileNo").value=="" && document.getElementById("id").value==""){
			var r=confirm("Mobile Number is empty ? Do you want to continue ?")
			if (r!=true){
				document.getElementById("mobileNo").focus();
			return false;
			}
		 }
	
		var buttonclickedname=obj;
		
		if(obj=='Approve Registration'||obj=='Approve Unconsidered'||obj=='Approve Unconsideration'||obj=='Forward-Approver')
		buttonclickedname="forward";
		return validateBPAWorkFlowApprover(buttonclickedname,'bpa_error_area');
		
	
		
	
}

function validateBPAWorkFlowApprover(name,errorDivId)
{   
	document.getElementById(errorDivId).style.display='none';
      
    if((name=="Forward" || name=="Approve" || name=="approve" || name=="forward") && 
    		document.getElementById('approverPositionId').value=="-1" ||document.getElementById('approverPositionId').value=="")
    {
       
    	<s:if test="%{registration.id!=null && registration.state.value!='NEW'}">
        document.getElementById(errorDivId).style.display='';
        document.getElementById(errorDivId).innerHTML = "Please Select the Approver Details";
		return false;
		</s:if>
    }
    var regid=jQuery("#id").val();	
    var egwStatus= jQuery("#egwStatuscode").val();
    if( regid =="" && egwStatus=="" || regid !="" && egwStatus=='Registered' ){
	 var r=confirm("Do you really want to continue?")
  
	if (r==true)
		{
 		//alert("hi");
		return true;
		}
	else
		{
 		//alert("hello");
			return false;
		}

	 }
}

function enableFields(){
	for(var i=0;i<document.forms[0].length;i++)
	{
		document.forms[0].elements[i].disabled =false;	
	}
}



function validateName(obj){
	if(obj.value!=""){
        var num=obj.value;
        var regExp  = /^[A-Za-z0-9. ]+$/;
        if(!regExp.test(num)){
        	obj.value="";	
			dom.get("bpa_error_area").style.display = '';
			document.getElementById("bpa_error_area").innerHTML = '<s:text name="invalid.name" />';
			//jQuery(obj).css("border", "1px solid red");	
			
			return false;
		}
	}
	return true;
}

function validatePincodeValue(obj){

	dom.get("bpa_error_area").style.display='none';
	if(obj.value!=""){
		if(!validatePincode(obj)){
			obj.value="";
			dom.get("bpa_error_area").style.display = '';
			document.getElementById("bpa_error_area").innerHTML = '<s:text name="invalid.pincode" />';
			//jQuery(obj).css("border", "1px solid red");		
			return false;
		}
	}
	return true;
}

function hideFieldError()
{
		if (dom.get("fieldError") != null)
			dom.get("fieldError").style.display = 'none';
		
		dom.get("bpa_error_area").style.display='none';
		dom.get("bpa_error_area").style.display = '';
}
function openSearchScreen() {
	window.open('${pageContext.request.contextPath}/search/searchProperty!searchForm.action?','window','scrollbars=yes,resizable=no,height=800,width=900,status=yes');
}

function  openSearchAutoDcr()
{
	window.open('${pageContext.request.contextPath}/search/searchAutoDcr!searchAuto.action?','window','scrollbars=yes,resizable=no,height=800,width=900,status=yes');


	}
function callSetPropertyId(propId,zone,ward,area,location,street,ownerName) {
	zoneData = zone;
	wardData = ward;
	areaData = area;
	locationData = location;
	streetData = street;
	
	document.getElementById("propertyid").value=propId;
	document.getElementById("ownerFirstname").value=ownerName;
	loadLeafLevelData2(zoneData,'Ward','ADMINISTRATION','0');
	loadLeafLevelData2(wardData,'','ADMINISTRATION','1');
	loadLeafLevelData2(areaData,'Locality','LOCATION','0');
	loadLeafLevelData2(locationData,'Street','LOCATION','0');
	loadLeafLevelData2(streetData,'','LOCATION','0');
	loadBoundaryData(zoneData,wardData,areaData,locationData,streetData);
}

function loadBoundaryData(zoneData,wardData,areaData,locationData,streetData)
{
	document.getElementById("Zone").value = zoneData;
	document.getElementById("Ward").value = wardData;
	document.getElementById("Area").value = areaData;
	document.getElementById("Locality").value = locationData;
	document.getElementById("Street").value = streetData;
	//alert("document.getElementById(Street).value==="+document.getElementById("Street").value);

	/*document.getElementById("Zone").disabled=true;
	document.getElementById("Ward").disabled=true;
	document.getElementById("Area").disabled=true;
	document.getElementById("Locality").disabled=true;
	document.getElementById("Street").disabled=true;
	document.getElementById("ownerFirstname").disabled=true;*/
}
function callSetAutoDCR(autoDcrNumber,applicant_name,address,email,mobileno,plotno,doorno,village,surveyno,blockno,plotarea)
{
	document.getElementById("autoDcrNum").value=autoDcrNumber;
	var propId=document.getElementById("propertyid").value;
	if(propId==""){
		//alert("property id in null ");
	document.getElementById("ownerFirstname").value=applicant_name;
	}
	document.getElementById("applicantAddress1").value=address;
	document.getElementById("email").value=email;
	document.getElementById("mobileNo").value=mobileno;
	
	document.getElementById("sitePlotNum").value=plotno;
	document.getElementById("plotDoorNum").value=doorno;
   //document.getElementById("villageName").value=village;

	document.getElementById("sitePlotSurveyNum").value=surveyno;
	document.getElementById("plotBlockNum").value=blockno;
	document.getElementById("plotAreaInSqft").value=plotarea;
	convertSQFTtoSQMT();
	//disablefieldsforAutodcr();

}	
function ResetAutoDcr(){


if(document.getElementById("autoDcrNum").value!="")
	
	//alert("less");
	document.getElementById("autoDcrNum").value="";
	

	
}

/*function OnChangeReset()
{
	//alert("reset");
	document.getElementById("autoDcrNum").value="";
	var propId=document.getElementById("propertyid").value;
	if(propId==""){
		//alert("propid");
    document.getElementById("ownerFirstname").value="";
	}
	document.getElementById("applicantAddress1").value="";
	document.getElementById("email").value="";
	document.getElementById("mobileNo").value="";
	
	document.getElementById("sitePlotNum").value="";
	document.getElementById("plotDoorNum").value="";
	document.getElementById("siteVillageName").value="";

	document.getElementById("sitePlotSurveyNum").value="";
	document.getElementById("plotBlockNum").value="";
	document.getElementById("plotAreaInSqft").value="";


	}
	
function disablefieldsforAutodcr()
{

	var autodcr=document.getElementById("autoDcrNum").value;
	if(autodcr!=null){
	//alert("autodcr"+document.getElementById("autoDcr.autoDcrNum").value);
	//document.getElementById("autoDcr.autoDcrNum").disabled=true;
	if(document.getElementById("ownerFirstname") != null) {
		
		document.getElementById("ownerFirstname").disabled=true;
		}
	document.getElementById("applicantAddress1").disabled=true;
	//document.getElementById("email").disabled=true;
	//document.getElementById("mobileNo").disabled=true;

	document.getElementById("sitePlotNum").disabled=true;
	if(document.getElementById("plotDoorNum") != null) {
		document.getElementById("plotDoorNum").disabled=true;
		}
	
	document.getElementById("siteVillageName").disabled=true;

	document.getElementById("sitePlotSurveyNum").disabled=true;
	document.getElementById("plotBlockNum").disabled=true;
	document.getElementById("plotAreaInSqft").disabled=true;
	}
}
*/
//javascript for tabs

function showRegTab(){
  document.getElementById('regForm').style.display='';
  document.getElementById('uploadForm').style.display='none';
  document.getElementById('inspectionForm').style.display='none';
  document.getElementById('letterToPartyForm').style.display='none';
  document.getElementById('feeDetForm').style.display='none';

  document.getElementById('admissionTbl').style.display='';
  if(document.getElementById('rejectDetForm')!=null) {
	  document.getElementById('rejectDetForm').style.display='none';
	  }
  setCSSClasses('uploadTab','');
  setCSSClasses('inspectionTab','');
  setCSSClasses('feeDetTab','');  
 
  setCSSClasses('letterPartyTab','');
  setCSSClasses('regTab','First Active');
  setCSSClasses('rejectionTab','Last');
}

function showUploadTab(){
	document.getElementById('regForm').style.display='none';
	document.getElementById('uploadForm').style.display='';
	document.getElementById('inspectionForm').style.display='none';
	document.getElementById('letterToPartyForm').style.display='none';
	document.getElementById('feeDetForm').style.display='none';
	
	document.getElementById('admissionTbl').style.display='none';
	 if(document.getElementById('rejectDetForm')!=null) {
		  document.getElementById('rejectDetForm').style.display='none';
		  }
	
    setCSSClasses('regTab','BeforeActive');
    setCSSClasses('uploadTab','Active');
    setCSSClasses('inspectionTab','');
	setCSSClasses('letterPartyTab','');
	setCSSClasses('feeDetTab','');
	
	setCSSClasses('rejectionTab','Last');
}

function showInspectionTab(){
	document.getElementById('regForm').style.display='none';
	 document.getElementById('uploadForm').style.display='none';
	document.getElementById('inspectionForm').style.display='';
	document.getElementById('letterToPartyForm').style.display='none';
	document.getElementById('feeDetForm').style.display='none';
	
	document.getElementById('admissionTbl').style.display='none';
	 if(document.getElementById('rejectDetForm')!=null) {
		  document.getElementById('rejectDetForm').style.display='none';
		  }
	
    setCSSClasses('regTab','First');
    setCSSClasses('uploadTab','BeforeActive');
    setCSSClasses('inspectionTab','Active');
	setCSSClasses('letterPartyTab','');
	setCSSClasses('feeDetTab','');
	 
	setCSSClasses('rejectionTab','Last');
}

function showLetterPartyTab(){
	document.getElementById('regForm').style.display='none';
	 document.getElementById('uploadForm').style.display='none';
	document.getElementById('inspectionForm').style.display='none';
	document.getElementById('letterToPartyForm').style.display='';
	document.getElementById('feeDetForm').style.display='none';
	
	document.getElementById('admissionTbl').style.display='none';
	 if(document.getElementById('rejectDetForm')!=null) {
		  document.getElementById('rejectDetForm').style.display='none';
		  }
	
    setCSSClasses('regForm','');
    setCSSClasses('uploadTab','');
    setCSSClasses('inspectionTab','BeforeActive');
	setCSSClasses('letterPartyTab','Active');
	setCSSClasses('feeDetTab','');
	 
	setCSSClasses('rejectionTab','Last');
}

function showFeeDetTab(){
	document.getElementById('regForm').style.display='none';
	document.getElementById('inspectionForm').style.display='none';
	document.getElementById('letterToPartyForm').style.display='none';
	document.getElementById('feeDetForm').style.display='';
	 document.getElementById('uploadForm').style.display='none';
	document.getElementById('admissionTbl').style.display='none';
	 if(document.getElementById('rejectDetForm')!=null) {
		  document.getElementById('rejectDetForm').style.display='none';
		  }
	
    setCSSClasses('regForm','');
    setCSSClasses('uploadTab','');
    setCSSClasses('inspectionTab','');
	setCSSClasses('letterPartyTab','BeforeActive');
	setCSSClasses('feeDetTab','Active');
	
	setCSSClasses('rejectionTab','Last');
}




function showRejectDetTab(){
	document.getElementById('regForm').style.display='none';
	document.getElementById('inspectionForm').style.display='none';
	document.getElementById('letterToPartyForm').style.display='none';
	document.getElementById('feeDetForm').style.display='none';
	 document.getElementById('uploadForm').style.display='none';
	document.getElementById('admissionTbl').style.display='none';
	 if(document.getElementById('rejectDetForm')!=null) {
		  document.getElementById('rejectDetForm').style.display='';
		  }
	
    setCSSClasses('regForm','');
    setCSSClasses('uploadTab','');
    setCSSClasses('inspectionTab','');
	setCSSClasses('letterPartyTab','');
	setCSSClasses('feeDetTab','BeforeActive');
	 
	setCSSClasses('rejectionTab','Last Active ActiveLast');
}

function setCSSClasses(id,classes){
    document.getElementById(id).setAttribute('class',classes);
    document.getElementById(id).setAttribute('className',classes);
}

function ismaxlength(obj){
	var mlength=obj.getAttribute? parseInt(obj.getAttribute("maxlength")) : ""
	if (obj.getAttribute && obj.value.length>mlength)
	obj.value=obj.value.substring(0,mlength)
}
</script>
  </head>
  <body onload="bodyOnLoad();refreshInbox();">

  
	<div class="errorstyle" id="bpa_error_area" style="display: none;"></div>
	<div class="errorstyle" style="display: none" ></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />	
			<s:fielderror />
		</div>
	</s:if>
	<s:if test="%{hasActionMessages()}">
		<div class="errorstyle">
			<s:actionmessage />
		</div>
	</s:if>

<s:form name="RegisterBpaForm" action="registerBpa" theme="simple" onsubmit="enableFields();" >
  <s:push value="model">
  <s:token/> 
  <div class="formmainbox">
  
  	<s:hidden id="request_number" name="request_number" value="%{request_number}"/>
			<s:hidden id="serviceRegistryId" name="serviceRegistryId" value="%{serviceRegistryId}"/>
		
  			<s:hidden id="id" name="id" value="%{id}"/>
			<s:hidden id="createdBy" name="createdBy" value="%{createdBy.id}"/>
	   		<s:hidden id="modifiedBy" name="modifiedBy" value="%{modifiedBy.id}"/>
	   		<s:hidden id="createdDate" name="createdDate" value="%{createdDate}"/>
	   		<s:hidden id="modifiedDate" name="modifiedDate" value="%{modifiedDate}"/>
		    <s:hidden id="mode" name="mode" value="%{mode}"/>
		    <s:hidden id="state" name="state" value="%{state.id}" />
		    <s:hidden id="statevalue" name="statevalue" value="%{state.value}" />
		    <s:hidden id="egwStatus" name="egwStatus" value="%{egwStatus.id}"/>
		   	<s:hidden id="egwStatuscode" name="egwStatuscode" value="%{egwStatus.code}"/>
		    <s:hidden id="egDemand" name="egDemand" value="%{egDemand.id}"  />
     		<s:hidden  id="rejection"  name="rejection" value="%{rejection.id}" />
     		<s:hidden id="additionalState" name="additionalState" value="%{additionalState}"  />
     		<s:hidden  id="isSanctionFeeRaised"  name="isSanctionFeeRaised" value="%{isSanctionFeeRaised}" />	
     		<s:hidden  id="signDetails"  name="signDetails" value="%{signDetails.id}" />	
     		<s:hidden  id="orderDetails"  name="orderDetails" value="%{orderDetails.id}" />	
     		<s:hidden  id="orderIssueDet"  name="orderIssueDet" value="%{orderIssueDet.id}" />	
     		<s:hidden  id="rejectOrdPrepDet"  name="rejectOrdPrepDet" value="%{rejectOrdPrepDet.id}" />	
     		<s:hidden  id="rejectOrdIssDet"  name="rejectOrdIssDet" value="%{rejectOrdIssDet.id}" />	
     		<s:hidden  id="reqdAction"  name="reqdAction" value="%{reqdAction}" />
     		<s:hidden  id="isPlotAreaEditable"  name="isPlotAreaEditable" value="%{isPlotAreaEditable}" />
     		<s:hidden id="securityKey" name="securityKey" value="%{securityKey}"/>
			<s:hidden id="additionalMode" name="additionalMode" value="%{additionalMode}"/>
     		<s:hidden id="regObjectHistoryRemarks" name="regObjectHistoryRemarks" value="%{regObjectHistoryRemarks}"/>
     		
  	<div class="insidecontent">
  		<div class="rbroundbox2">
  			<div class="rbcontent2">
  			  <table width="100%" border="0" cellspacing="0" cellpadding="0">
  				<tr>
	            	<td><div id="tabs2">
						<ul id="Tabs">
							<li id="regTab" class="First Active"><a id="header_1" href="#" onclick="showRegTab();"><s:text name="RegisterBpaHeader"/></a></li>
							<li id="uploadTab" class="Befor"><a id="header_5" href="#" onclick="showUploadTab();"><s:text name="Document Details"/></a></li>
					     	<li id="inspectionTab" class="Befor"><a id="header_2" href="#" onclick="showInspectionTab();"><s:text name="Inspection Details"/></a></li>					
							<li id=letterPartyTab class="Befor"><a id="header_3" href="#" onclick="showLetterPartyTab();"><s:text name="lettertoparty.title"/></a></li>
							<li id="feeDetTab" class="Befor"><a id="header_4" href="#" onclick="showFeeDetTab();"><s:text name="Fees Details"/></a></li>
							<li id="rejectionTab" class="Last"><a id="header_6" href="#" onclick="showRejectDetTab();"><s:text name="Unconsidered Details"/></a></li>
						</ul>
            		</div></td>
          		</tr>
          		<tr><td>&nbsp;</td></tr>
	           <tr>
		            <td>
			            <div id="regForm">
			                <%@ include file="registerBpaForm.jsp"%>      
			            </div>
		            </td>
	          </tr>
	          <tr>
		            <td>
			            <div id="inspectionForm" style="display:none;">
								<s:if test="postponedInspectionDetails.size!=0">
								<sj:dialog 
								    	id="datedialog" 
								    	autoOpen="false" 
								    	modal="true" 
								    	title="PostPoned Details"
								    	openTopics="openRemoteDialog"
								    	height="500"
								    	width="700"
								    	dialogClass="formmainbox"
								    	loadingText="Please Wait ...."
								    	showEffect="slide" 
								    	hideEffect="slide" 
								    	onOpenTopics="openTopicDialog" cssStyle="BACKGROUND-COLOR: #ffffff"
								    />
									
									    <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
									   	 	<tr>
									 			 <s:url id="measurelink" value="/inspection/inspection!showInspectioninRegistration.action" escapeAmp="false">
										      <s:param name="registrationId" value="%{id}"></s:param>	  
										   </s:url>
										        <td class="greybox" width="15%">&nbsp;</td>
												<td class="greybox"  width="15%"><s:text name="Site Inspection Fixed Date"/></td> 
									   			<td class="greybox" width="15%"> <s:textfield value="%{postponedInspectionDetails[0].inspectionDateString}" id="inspectionFixedDate" name="inspectionFixedDate" disabled="true" /></td>   			
												<td  class="greybox"><div align="center"> <sj:a  onClickTopics="openRemoteDialog" href="%{measurelink}" button="true"  buttonIcon="ui-icon-newwin">Postponed Details</sj:a></div></td>
												
									         </tr>  
									         
									         <tr>
									 			 <s:url id="measurementlink" value="/inspection/inspection!showMeasurementdetailsinRegistration.action" escapeAmp="false">
										      <s:param name="registrationId" value="%{id}"></s:param>	  
										   </s:url>
										        <td class="bluebox" width="15%">&nbsp;</td>
											    <td class="bluebox">&nbsp;</td>
											    <td class="bluebox">&nbsp;</td>
												<td class="bluebox">&nbsp;</td>
									         </tr>  
									               
									      </table>
									
								  
								    </s:if>
								    
								     <s:url id="measureajax" value="/inspection/inspection!showExistingInspectionMeasurementDetails.action"  escapeAmp="false">	 
									   <s:param name="registrationId" value="%{id}"></s:param>	      
									   </s:url>
									   <div style="display:none">
									  <sj:a  id="reloadLink" onClickTopics="reloadmeasurement">Refresh Div</sj:a>
									 </div> 
								       <sj:div href="%{measureajax}" indicator="indicator" reloadTopics="reloadmeasurement"  cssClass="formmainbox" id="tab3"  dataType="html" onCompleteTopics="">
								        <img id="indicator" src="<egov:url path='/images/loading.gif'/>" alt="Loading..." style="display:none"/>
								    </sj:div>
								    
			            </div>
		            </td>
	          </tr>
	          <tr>
		            <td>
			            <div id="letterToPartyForm" style="display:none;">
							 <s:url id="lettertoparty" value="/register/registerBpa!showExistingLetterToPartyDetails.action"  escapeAmp="false">	 
								   <s:param name="registrationId" value="%{id}"></s:param>	  
								    <s:param name="mode" value="%{mode}"></s:param>	    
							</s:url>
							   <div style="display:none">
								  <sj:a  id="reloadLink" onClickTopics="reloadlettertoparty">Refresh Div</sj:a>
								    </div> 
							       <sj:div href="%{lettertoparty}" indicator="indicator" reloadTopics="reloadlettertoparty"  cssClass="" id="tab6"  dataType="html" onCompleteTopics="completeDiv">
							        <img id="indicator" src="<egov:url path='/images/loading.gif'/>" alt="Loading..." style="display:none"/>
							    </sj:div>
			            </div>
		            </td>
	          </tr>
	          <tr>
		            <td>
		            
		            
			            <div id="feeDetForm" style="display:none;">
			            
			             
	                         <s:url id="existingfeedetllink" value="/fee/revisedFee!showExistingFeeDetails.action" escapeAmp="false" >
	                         <s:param name="registrationId" value="%{id}"></s:param>	  
	                        </s:url>
                             <sj:div href="%{existingfeedetllink}" indicator="indicator"  cssClass="" id="previousfee"  dataType="html" onCompleteTopics="completeDiv">
	                         <img id="indicator" src="<egov:url path='/images/loading.gif'/>" alt="Loading..." style="display:none"/>
	                       </sj:div>

			            
			            
			            
							 <s:url id="feelink" value="/fee/feeDetails!showFeeDetailsinRegistration.action" escapeAmp="false">
									      <s:param name="registrationId" value="%{id}"></s:param>	  
									   </s:url>
									   <div style="display:none">
									    <sj:a  id="reloadFeeLink" onClickTopics="reloadfee">Refresh FeeDiv</sj:a>
							 </div> 
							 <sj:div href="%{feelink}" indicator="indicator" reloadTopics="reloadfee"  cssClass="" id="tab4"  dataType="html" onCompleteTopics="completeDiv">
							        <img id="indicator" src="<egov:url path='/images/loading.gif'/>" alt="Loading..." style="display:none"/>
							    </sj:div>
			            </div>
		            </td>
	          </tr>	
	          <tr>
	          <td>
	          	
	          <div id="uploadForm" style="display:none;">
	          
	  <%@ include file="../common/submitChecklist.jsp"%>
       <table>
		<tr>
			 <td width="40%" class="greybox"/>
			<td class="greybox">
								<s:text name="Document upload: " />
						</td>
						<s:if test="documentNum==null || documentNum==''">
								<td id="addlink" class="greybox">
									<div class="buttonholderrnew">
										<input type="button" id="browse" value="Browse"
											onclick="onreportupload();return false;"
											class="buttoncreatenewcase" />
									</div>
								</td>
							</s:if>
					
						<s:else>
							<td id="viewlink" class="greybox">
									<s:text name="view documents" />
									<egov:docFiles moduleName="BPA"
										documentNumber="${documentNum}" id="documentRef"
										showOnLoad="true" cssClass="greybox" />
							</td>
						</s:else>
							<td class="greybox"/>
							  <td class="greybox"/>
                        <td class="greybox"/>
                      
                       
						<s:hidden name="documentNum" id="docNumber" value="%{documentNum}"></s:hidden>
                      <td class="greybox"/>
                       <td width="40%" class="greybox"/>
                        <td width="40%" class="greybox"/>
                </tr>
		</table>
		 
		</div>  
		</td>
		</tr>
		
		
	          	<s:if test="%{mode=='reject'||rejectview=='reject'}">
	          	
	          <tr>
		            <td>
			            <div id="rejectDetForm" style="display:none;">
							<div align="center" id="rejectchecklistdiv">
							
								<table id="rejectchecklists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		 							<tr>		 						
		 							<td class="bluebox" width="5%">&nbsp;</td>	
		 							<td class="bluebox" width="5%"><s:text name="Unconsidered Date" /><span class="mandatory">*</span></td>			
		   							<td class="bluebox"  width="5%"><sj:datepicker  id="rejectionDate" name="rejection.rejectionDate" displayFormat="dd/mm/yy" showOn="focus"/></td>		   	
		 						    <td class="bluebox"  width="5%"><s:text name="Remarks" /></td>	
		 							<td class="bluebox"  width="5%"><s:textarea  id="rejection.remarks" name="rejection.remarks"  rows="1" cols="10" maxlength="250" onkeyup="return ismaxlength(this)" /></td>
		 							<td class="bluebox"  width="5%"><s:text name="Unconsideration Number" /></td>	
		 							<td class="bluebox"  width="5%"><s:textfield disabled="true" id="rejection.rejectionNumber" name="rejection.rejectionNumber"  value="%{rejection.rejectionNumber}"/></td>
		 							</tr>   
		    					</table>
									<s:if test="%{rejectChkListDet.size!=0}">
								<h1 class="subhead" ><s:text name="Unconsideration Details"/></h1>
				<table id="rejectchecklists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	          							 <s:iterator value="rejectChkListDet" status="rejectrow_status">
		 										
		 										   
			 										<tr>
		     											<td class="bluebox" width="30%">&nbsp;</td>			
		   												<td class="bluebox" width="20%"><s:textfield name="rejectChkListDet[%{#rejectrow_status.index}].checkListDetails.description" readonly="true" /></td>		   	
														<td class="bluebox" id=""><s:hidden  name="rejectChkListDet[%{#rejectrow_status.index}].checkListDetails.isMandatory"/>
														<s:checkbox  name="rejectChkListDet[%{#rejectrow_status.index}].isChecked"/></td>	
													   <td class="bluebox" ><s:textarea cols="10" rows="1"  name="rejectChkListDet[%{#rejectrow_status.index}].remarks" maxlength="300" onkeyup="return ismaxlength(this)" /></td>		
														<s:hidden name="rejectChkListDet[%{#rejectrow_status.index}].checkListDetails.id"/>
														<s:hidden name="rejectChkListDet[%{#rejectrow_status.index}].checkListDetails.checkList.id"/>
														<s:hidden name="rejectChkListDet[%{#rejectrow_status.index}].id"/>
			 											<td class="bluebox" width="">&nbsp;</td>	
					 								</tr>   
		    								
		    							 	</s:iterator>
		    							 		</table>
	  							</s:if>
	 						 </div>
			            </div>
		            </td>
	          </tr>	    
	          </s:if>
	         </table>       
  			</div>
  		</div>
  	</div>

	<s:if test="%{mode!='view'}"> 
		<div id="approverInfo">
	        <c:set var="approverHeadTDCSS" value="headingwk" scope="request" /> 
	        <c:set var="approverHeaderCss" value="headplacerlbl" scope="request"/>
	        <c:set var="headerImgCss" value="arrowiconwk" scope="request"/>
	        <c:set var="headerImgUrl" value="../common/image/arrow.gif" scope="request"/>
	        <c:set var="approverOddCSS" value="whiteboxwk" scope="request" />
	        <c:set var="approverOddTextCss" value="whitebox2wk" scope="request" />
	        <c:set var="approverEvenCSS" value="greyboxwk" scope="request" />
	        <c:set var="approverEvenTextCSS" value="greybox2wk" scope="request" />
	        
			<c:import url="/commons/commonWorkflow.jsp" context="/bpa" />
	</div>
	</s:if>	  
		
	<div class="buttonbottom" align="center">
		<table>
		<s:if test="%{mode=='view'}">
			 <td>
	  				<s:submit type="submit" cssClass="buttonsubmit" value="Print" id="print" name="print" method="print"/>
	  			</td>
	  		</s:if>
	  		<s:else> 
	  		<s:if test="%{mode!='reject'}">
	  		<s:if test="%{rejectview!='reject'}">
	  		 <td><s:submit  cssClass="buttonsubmit" id="save" name="save" value="Save"  method="save" onclick="return validateForm('save');" /></td> 
	  		</s:if>
	  		</s:if>
	  		<s:iterator value="%{getValidActions()}" var="p">
		  		<td><s:submit type="submit" cssClass="buttonsubmit" value="%{p}" id="%{p}" name="%{p}" method="save" onclick="return validateForm('%{p}')" /></td>
			</s:iterator>
	  		</s:else>
	  		
	  		<td>
	  			<input type="button" name="close" id="close" class="button"  value="Close" onclick="window.close();"/>
	  		</td>
	  		<s:if test="%{mode!='view'}">	  		
	  		<td>
				<div class="dropdown">
					<input type="button" name="actionsbutton" id="account" class="button" value="Actions"/>
					<div class="submenu">
					<ul class="root">
					<s:if test="{showActions.size!=0}">			  
					<s:url id="actionsajax" value="/register/registerBpa!getActionsByLoginUserId.action"  escapeAmp="false">	 
					<s:param name="registrationId" value="%{id}"></s:param>	
					<s:param name="additionalMode" value="%{additionalState}"></s:param>	      
					</s:url>
					  <div style="display:none">
					<sj:a  id="reloadactionsLink" onClickTopics="reloadactionsdiv">Refresh Div</sj:a>	 
					</div>
					<sj:div href="%{actionsajax}" indicator="indicator" reloadTopics="reloadactionsdiv"  cssClass="formmainbox" id="actionsshown"  dataType="html" >
					<img id="indicator" src="<egov:url path='/images/loading.gif'/>" alt="Loading..." style="display:none"/>
					</sj:div>
					</s:if>
					</ul>
				   </div>
			   </div>
	  		</td>
	  		</s:if>
	  		
					</table>	    			    	
			    </div>
 </div>
       </s:push>
  </s:form>
   
  <script type="text/javascript">
  function loadLeafLevelData2(obj,values,heirarchyType, crossHier)
  {
	if(heirarchyType=='ADMINISTRATION')
  	{  
  	  	document.getElementById("adminboundaryid").value=obj;
  	}
  	else
  	{   
  		document.getElementById("locboundaryid").value=obj;
  	}
  	
  	var url;
  	if (crossHier == '1')
  	{
  		url = "<%=request.getContextPath()%>"+"/common/ajaxCommon!getCrossHierarchyBoundaries.action?boundaryId="+obj;
  		values = 'Area';
  	}
  	else
  	{
  		url = "<%=request.getContextPath()%>"+"/common/ajaxCommon!getChildBoundaries.action?boundaryId="+obj;
  	}
        	var request = initiateRequest();
  	request.open("GET", url, false);
  	request.send(null);
  	if (request.readyState == 4)
  	{
  		if (request.status == 200)
  		{
  			var response=request.responseText;
  			var tempvalues=response.split("!$");
  			var id = tempvalues[0].split("^");
  			if(values!=null && values!=""){
  				Dropdown=dom.get(values);
  				if(id.length >0 && values!=null)
  				{
  				    Dropdown.options.length = 0;
  					Dropdown.options[0] = new Option("---Choose---","-1");
  					for(var i = 1 ; i <= id.length  ; i++)
  					{
  					var name = id[i-1].split("+");
  					var ids= name[0].split(",");
  					if(ids!=null && ids!=""){
  					var names= name[1].split(",");
  					Dropdown.options[i] = new Option(names,ids);
  					}
  					}
  				}
  			}													
  		}
  		 else
  			alert("Error occurred while getting getBoundaryData..");
  	}
  }



  function onreportupload()
  	 {
  	     var v= dom.get("docNumber").value;
  	     var url;
  	     if(v==null||v==''||v=='To be assigned')
  	     {
  		   url="/egi/docmgmt/basicDocumentManager.action?moduleName=BPA";
  	     }
  	     else
  	     {
  		   
  	       url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="+v+"&moduleName=BPA";
  	     }
  	     var wdth = 1000;
  	     var hght = 400;
  	     window.open(url,'docupload','width='+wdth+',height='+hght);
  	 }
  	 
  function loadDesignationFromMatrix()
  {
        var currentState=document.getElementById('currentState').value;
       if(document.getElementById('additionalState').value!=""){
       currentState=document.getElementById('additionalState').value;
       }
        
        var amountRule=document.getElementById('amountRule').value;
        var additionalRule=document.getElementById('additionalRule').value;
        var pendingAction=document.getElementById('pendingActions').value;
        var dept="";
        loadDesignationByDeptAndType('Registration',dept,currentState,amountRule,additionalRule,pendingAction); 
  }

  function populateApprover()
	{
		getUsersByDesignationAndDept();
	}

  </script>
  </body>
  </html>