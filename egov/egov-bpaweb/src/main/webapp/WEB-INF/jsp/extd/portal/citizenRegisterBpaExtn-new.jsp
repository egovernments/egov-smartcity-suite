<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
 <html >
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

.small-button {
   font-size: .8em !important;
}
 </style>

  <title><s:text name='NewBPA.title'/></title>
  
  <sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true"/>
  <script type="text/javascript" src="<c:url value='/common/js/jquerycombobox.js'/>"></script>
  
  <title><s:text name='NewBPA.title'/></title> 
  
<script type="text/javascript">

jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery("#Region").closest('tr').hide();
	
	jQuery('#Ward').parent('td').prev('td').hide();
	jQuery('#Ward').hide();
	//jQuery("#Area").closest('tr').append('<td  class="bluebox">&nbsp;</td> ');
	
	jQuery('#regtabs').tabs();
	jQuery( "#propertyAtag" ).button();
  <s:if test="%{mode=='reject'||rejectview=='reject'}">
	disableRegistrationDetails();
  </s:if>
  
//Mouse click on sub menu
jQuery(".submenu").mouseup(function()
{
return false
});

	hideExistingApplDetails();
	hideShowBuildingCategory();	
	hideShowCMDAfields();
	/*jQuery AutoComplete for to load AutoDcrNumber*/
	jQuery("#autoDcrNum").autocomplete({
		source: function(request,response) {
				var url='${pageContext.request.contextPath}/extd/portal/citizenRegisterBpaExtn!autoCompleteapprovedAutoDcrNUmber.action';
				showWaiting();
				jQuery.getJSON(url,{autoDcrNumberAutocomplete: request.term},function(data){
					response(data);		
					if(data==null || data==""){
						jQuery("#autoDcrNum").val('');
						jQuery("#autoDcrId").val('');
					}		
					clearWaiting();	
		      })	
		},
	    minLength: 2,
	    select: function( event, ui ) {
			jQuery("#autoDcrId").val(ui.item.id);
		 }
	});	

	jQuery("#surveyorCode").autocomplete({
		source: function(request,response) {
				var url='${pageContext.request.contextPath}/extd/common/ajaxExtnCommon!getSurveyDetailbyParam.action';
				showWaiting();
				jQuery.getJSON(url,{surveyorCode: request.term},function(data){
					response(data);		
					if(data==null || data==""){
						dom.get("bpa_error_area").style.display = '';
						document.getElementById("bpa_error_area").innerHTML = '<s:text name="invalid.surveyor.details" />';
						jQuery("#surveyorCode").val('');
						jQuery("#Surveyor").val('');
						jQuery("#surveyorNameLocal").val('');
						jQuery("#surveyorMobNo").val('');
					}
					clearWaiting();	
		      })	
		},
	    minLength: 2,
	    select: function( event, ui ) {
		    jQuery("#Surveyor").val(ui.item.id);
		    if(ui.item.id!=null && ui.item.id!=""){
		    	var flags = (ui.item.value).split("-");
	       	    document.getElementById('surveyorCode').value=flags[0];  
	    		document.getElementById('surveyorNameLocal').value=flags[1];  
	    		document.getElementById('surveyorMobNo').value=flags[2];  	
		    }
		 }
	});	

	jQuery("#surveyorNameLocal").autocomplete({
		source: function(request,response) {
				var url='${pageContext.request.contextPath}/extd/common/ajaxExtnCommon!getSurveyDetailbyParam.action';
				showWaiting();
				jQuery.getJSON(url,{surveyorName: request.term},function(data){
					response(data);		
					if(data==null || data==""){
						dom.get("bpa_error_area").style.display = '';
						document.getElementById("bpa_error_area").innerHTML = '<s:text name="invalid.surveyor.details" />';
						jQuery("#surveyorCode").val('');
						jQuery("#Surveyor").val('');
						jQuery("#surveyorNameLocal").val('');
						jQuery("#surveyorMobNo").val('');
					}
					clearWaiting();	
		      })	
		},
	    minLength: 2,
	    select: function( event, ui ) {
		    jQuery("#Surveyor").val(ui.item.id);
		    if(ui.item.id!=null && ui.item.id!=""){
		    	var flags = (ui.item.value).split("-");
	       	    document.getElementById('surveyorCode').value=flags[0];  
	    		document.getElementById('surveyorNameLocal').value=flags[1];  
	    		document.getElementById('surveyorMobNo').value=flags[2];  	
		    }
		 }
	});	

	jQuery("#surveyorMobNo").autocomplete({
		source: function(request,response) {
				var url='${pageContext.request.contextPath}/extd/common/ajaxExtnCommon!getSurveyDetailbyParam.action';
				showWaiting();
				jQuery.getJSON(url,{surveyorMobNo: request.term},function(data){
					response(data);		
					if(data==null || data==""){
						dom.get("bpa_error_area").style.display = '';
						document.getElementById("bpa_error_area").innerHTML = '<s:text name="invalid.surveyor.details" />';
						jQuery("#surveyorCode").val('');
						jQuery("#Surveyor").val('');
						jQuery("#surveyorNameLocal").val('');
						jQuery("#surveyorMobNo").val('');
					}
					clearWaiting();	
		      })	
		},
	    minLength: 2,
	    select: function( event, ui ) {
		    jQuery("#Surveyor").val(ui.item.id);
		    if(ui.item.id!=null && ui.item.id!=""){
		    	var flags = (ui.item.value).split("-");
	       	    document.getElementById('surveyorCode').value=flags[0];  
	    		document.getElementById('surveyorNameLocal').value=flags[1];  
	    		document.getElementById('surveyorMobNo').value=flags[2];  	
		    }
		 }
	});	

	

	jQuery("#left").click(function() {
          
        jQuery("#content").animate(
            { "width": "2%"},
            "fast", function(){
               jQuery('#communication').hide();
            });
             jQuery("#content").css({left:left}).animate({"left":"10%"}, "fast");

             
});
	var serviceTypeCode=document.getElementById('serviceTypeCode').value;
	if(serviceTypeCode=='02' || serviceTypeCode=='04' || serviceTypeCode=='05' || serviceTypeCode=='07' || serviceTypeCode=='08'){
	
	jQuery('#Street').change( function (){
		//getParentBoundaryData();
	});
}

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


function splitValue(code,name,mobNo){
	var temp;
	if(code!=null){
		temp=(document.getElementById('surveyorCode').value).split("-");
		if(temp.length>1){
			document.getElementById('surveyorCode').value=temp[0];
		}
	}
	if(name!=null){
		temp=(document.getElementById('surveyorNameLocal').value).split("-");
		if(temp.length>1){
			document.getElementById('surveyorNameLocal').value=temp[1];
		}
	}
	if(mobNo!=null){
		temp=(document.getElementById('surveyorMobNo').value).split("-");
		if(temp.length>1){
			document.getElementById('surveyorMobNo').value=temp[2];
		}
	}
}

function clearWaiting() 
{
	document.getElementById('loading').style.display ='none';
}
function showWaiting() 
	{
	document.getElementById('loading').style.display ='block';
}




/*
function getSurveyourDetail(){
	var survId=document.getElementById('Surveyor').value;
		jQuery.ajax({
		    url:"${pageContext.request.contextPath}/extd/common/ajaxExtnCommon!getSurveyObjectforZone.action",
		     data: { surveyorId:survId}, 
		      type: "POST",
		     dataType: "html",
		    success: function(data){
		    	var flags = data.split("-");
		    	var serCodeaFlag = flags[0];
		    	var surNameFlag = flags[1];
		    var SurvClassFlag=flags[2];
		    document.getElementById('surveyorCode').value=serCodeaFlag;
			document.getElementById('surveyorNameLocal').value=surNameFlag;
			document.getElementById('surveyorClass').value=SurvClassFlag;	
		       
		    }
		    });
	
}
*/

function getParentBoundaryData(){
	var areaid=jQuery("#Street").val();
		jQuery.ajax({
		    url:"${pageContext.request.contextPath}/extd/portal/citizenRegisterBpaExtn!getSurveyorDeatilList.action",
		     data: { adminBoundaryId:areaid }, 
		      type: "POST",
		     dataType: "html",
		    success: function(data){
		      document.getElementById('Surveyor').value=data;
		   }
		    });
	
}

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
	
	jQuery('#Area').parent('td').prev('td').append('<span class="mandatory">*</span>');
	jQuery("#Area").closest('tr').append('<td  class="bluebox">&nbsp;</td> <td  class="bluebox">&nbsp;</td> <td  class="bluebox">&nbsp;</td> <td  class="bluebox">&nbsp;</td>  ');
	jQuery('#Ward').closest('td').remove();
	jQuery('#Area').parent('td').prev('td').prev('td').remove();
	jQuery('#Locality').parent('td').prev('td').append('<span class="mandatory">*</span>');
	jQuery('#Street').parent('td').prev('td').append('<span class="mandatory">*</span>');
	//jQuery('#Ward').parent('td').prev('td').append('<span class="mandatory">*</span>');
	jQuery('#Area').parent('td').prev('td').prev('td').remove();
	var userRoles= document.getElementById('userRole').value;

	if(userRoles!="" && userRoles!=null && userRoles!='PORTALUSERSURVEYOR'){
		dom.get("breadcrumbHeader").innerHTML='<s:text name="citizenPortal.header" />';
	}
	else{
		dom.get("breadcrumbHeader").innerHTML='<s:text name="surveyorPortal.header" />';
	}
	
	if(userRoles!="" && userRoles!=null && userRoles!='PORTALUSERSURVEYOR' && document.getElementById('id').value!=null && document.getElementById('regStatus').value!=""){
		document.getElementById('termsCondition').checked=true;
	}
	var stateid=document.getElementById('state').value;
	 jQuery(".buttonsubmit").each(function(index) {
		
			var values=(jQuery(this).attr("value"));
			if(values!=null&&values!=""&&values=='Close Registration'){
				jQuery(this).attr("name","method:closeRegistration");
			}
	 });
    document.getElementById('serviceTypeId').disabled=true;
	var autoDcrFlagEnabled=document.getElementById('autoDcrFlag').value;
	if(userRoles!="" && userRoles!=null && userRoles!='PORTALUSERSURVEYOR' && autoDcrFlagEnabled=="false" )
	 {
		 if(document.getElementById('autodcrAtag1')!=null)
			 document.getElementById('autodcrAtag1').style.display='none';
			 if(document.getElementById('autodcrAtag2')!=null)
			 document.getElementById('autodcrAtag2').style.display='none';
	 }
     //Ajax call to get feedetails,mandatory field.. etc
     onChangeOfServiceType();
    var serviceTypeCode=document.getElementById('serviceTypeCode').value;
     var mobileNumber=document.getElementById('mobileNo').value;
     var stateForValidate=document.getElementById('stateForValidate').value;
     var email=document.getElementById('email').value;
     if(mobileNumber!=null && mobileNumber!="")
    	 document.getElementById('mobileNo').readOnly=true;
	 if(email!=null && email!="")
		 document.getElementById('email').readOnly=true;
	var mode=document.getElementById('mode').value;
	documentHistoryFormodifyAndView();
	var regstatus=document.getElementById('regStatus').value;
	if(document.getElementById('autoDcrNum')!=null)
	document.getElementById('autoDcrNum').disabled=true;
	if(userRoles!="" && userRoles!=null && userRoles!='PORTALUSERSURVEYOR' && (serviceTypeCode=='01' || serviceTypeCode=='03' || serviceTypeCode=='06'))
	{
	jQuery('#attachDocForm :input').attr('disabled', true);
	//jQuery('#attachDocForm').find('input,select,textarea,checkbox').attr('disabled',false);
	}
	if(mode=='noEdit'){
		disableRegistrationDetails();
	}
	
	else if(mode=='view' )
	{	
		jQuery('#showjsp').show();
		jQuery('#hidejsp').hide();
		jQuery('#docHistoryForm').show(); 
		jQuery('#addrowDeleteID').hide();
		for(var i=0;i<document.forms[0].length;i++)
		{
			if( document.forms[0].elements[i].name!='close' && document.forms[0].elements[i].id!='print'
				 && document.forms[0].elements[i].name!='printChallan'	&& document.forms[0].elements[i].name!='createLp' && document.forms[0].elements[i].name!='enterInspDate' && document.forms[0].elements[i].name!='inspectionDetails')
				{
				document.forms[0].elements[i].disabled =true;
			}
		}
		var workbutton=document.getElementById('workFlowAction').value;
		if(userRoles!="" && userRoles!=null && userRoles=='PORTALUSERSURVEYOR' ){
			index=document.getElementById('dochistorydetails').rows.length-1;
			var autunum=document.getElementById('autoDcrNum').value;
		
		if( (autunum=="" ||autunum==null && autoDcrFlagEnabled=="true"  ) )
		{
			document.getElementById('autoDcrNum').disabled=false;
			document.getElementById('termsCondition').disabled=false;
			//	jQuery('#modifyAutoDcr').hide();
			enableRegnFieldsForSurveyor();
			jQuery('#docHistoryForm').find('input,select,textarea').attr('disabled',false);
		}
		else if(((regstatus=='Application Forwarded to LS') ||
				(regstatus=='Site Insp Scheduled By LS') || (regstatus=='Site Inspected By LS')) &&  autoDcrFlagEnabled=="true" )
			{	
			document.getElementById('autoDcrNum').disabled=false;
			jQuery('#docTempDivForShadowtable').show();
			document.getElementById('termsCondition').disabled=false;
			enableRegnFieldsForSurveyor();
			jQuery('#docHistoryForm').find('input,select,textarea').attr('disabled',false);
			
			}
		else {
			document.getElementById('autoDcrNum').disabled=true;
			jQuery('#surveyorDiv').hide();
			jQuery('#docTempDivForShadowtable').hide();
			document.getElementById('termsCondition').disabled=true;
			document.getElementById('appType').disabled =true;
			document.getElementById('appMode').disabled =true;
			jQuery('#ownerDtls').hide();
			jQuery('#plotAddress').hide();
			jQuery('#docHistoryForm').show(); 
			jQuery('#docHistoryForm').find('input,select,textarea').attr('disabled',true);
			}
	
		checkAutoDcrCheckBox();
		//var docNum=dom.get("docNumber").value;
		var docHistoryId=dom.get("dochistoryDetailId").value;
		var docMainId=dom.get("documentHistoryId").value;
		if( (docHistoryId!=null && docHistoryId!="") && (docMainId!=null && docMainId!=""))
			{
			document.getElementById("documentTabEnabled").checked=true;
			}
		else{
			document.getElementById("documentTabEnabled").checked=false;
		}
		checkdocumentDetailCheckBoxOnload();
		checkRegnFormCheckBox();
		document.getElementById("regInspecyionEnabled").checked=true;
		if(userRoles!="" && userRoles!=null && userRoles=='PORTALUSERSURVEYOR' && stateid!=null && stateid!=""){
		
			document.getElementById("regInspecyionEnabled").checked=true;
			document.getElementById("documentTabEnabled").checked=true;
		document.getElementById("regFormTabEnabled").checked=true;
			document.getElementById("documentTabEnabled").checked=true;
			document.getElementById('termsCondition').checked=true;
		}
		jQuery('input:file[value=""]').attr('disabled', true);
		jQuery('#disableCheckBox').find('input[type="checkbox"]').prop("disabled", true);
		
	}
		//documentHistoryFormodifyAndView();
	} 
	
	else if(mode=='edit'){
		jQuery('#showjsp').show();
		 jQuery('#hidejsp').hide();
		 hideShowBuildingCategory();
		 hideExistingApplDetails();
		 hideShowCMDAfields();
		 document.getElementById('serviceTypeId').disabled=true;
		 var statevalue=document.getElementById('state').value;
		 if(statevalue==null || statevalue==""){
			
		 document.getElementById('Zone').disabled=false;	
		 }
		 else
			 {
			 document.getElementById('Zone').disabled=true;	
			 }
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
		
		 //jQuery('#surveyorDiv').show();
		
	var addlstate=jQuery('#additionalState').val();		
		  if(mode!='reject'||addlstate=='RejectedBPA'||addlstate=='UnconsiderdBPA'){
		
			  jQuery('#rejectDetForm').find('input,select,textarea').attr('disabled','true');
		 }

		  jQuery('.buttonsubmit').each(
			       function(){
		
			           jQuery(this).removeAttr("disabled");
	
			           }
						)	

}

	function enableRegnFieldsForSurveyor()
	{
		document.getElementById('plotAreaInSqft').readOnly=true;
		document.getElementById('plotAreaInSqmt').readOnly=true;
		jQuery('#uploadTDocId :input').attr('disabled', false);
		jQuery('#docHistoryForm :input').attr('disabled', false);
		jQuery('#ownerDtls').show();
		jQuery('#surveyorDiv').hide();
		jQuery('#buttonSave').show();
		 document.getElementById('Zone').disabled=false;
		document.getElementById('appType').disabled =false;
		document.getElementById('appMode').disabled =false;
		document.getElementById('ownerFirstname').disabled =false;
		document.getElementById('spouseOrFatheName').disabled =false;
		document.getElementById('applicantAddress1').disabled =false;
		document.getElementById('spouseOrFatheName').disabled =false;
		jQuery("#villageName").closest(".ui-widget").find("input, button").prop("disabled", false);
		jQuery("#villageName").parent().find("a.ui-button").button("enable");	
		document.getElementById('plotBlockNum').disabled =false;
		document.getElementById('plotLandmark').disabled =false;
		document.getElementById('cityTown').disabled =false;
		document.getElementById('boundaryStateId').disabled =false;
		document.getElementById('proposedbuildingCategoryId').disabled =false;  
		document.getElementById('existingbuildingCategoryId').disabled =false;
		document.getElementById('sitePlotSurveyNum').disabled =false;   
		document.getElementById('plotSurveyType').disabled =false;
		document.getElementById('villageName').disabled =false;
		document.getElementById('sitePlotNum').disabled =false;
		document.getElementById('plotLandmark').disabled =false;
		document.getElementById('plotDoorNum').disabled =false;
	}

	function documentHistoryFormodifyAndView()
	{
		var fixAmt=jQuery("input[name='documentHistory.wheatherdocumentEnclosed']:checked").val();
		var fixlayout= jQuery("input[name='documentHistory.wheatherpartOfLayout']:checked").val();

		var fmsSketch = jQuery("input[name='documentHistory.wheatherFmsOrSketchCopyOfReg']:checked").val();
	 	var devlopBy=jQuery("input[name='documentHistory.plotDevelopedBy']:checked").val(); 
	 	if(fixAmt == undefined)
	 	 	{
	 		jQuery("#docandLayoutDiv").show();
	 		jQuery('#documentNumberDiv').hide();
			jQuery('#docapprovedDiv').hide();
			jQuery('#plotDevelopNewDiv').hide();
			jQuery('#plotdevFmsSketchDiv').hide();
			jQuery('#fmsfullDiv').hide();
			jQuery('#partofLaout').hide();
	 	 	}
	 	else{
	 	if(fixAmt == "true" ){

		 jQuery("input[name='documentHistory.wheatherpartOfLayout']:checked").prop('checked', false);
			jQuery("input[name='documentHistory.plotDevelopedBy']:checked").prop('checked', false);
			jQuery("input[name='documentHistory.wheatherplotDevelopedBy']:checked").prop('checked', false);
			jQuery("input[name='documentHistory.wheatherFmsOrSketchCopyOfReg']:checked").prop('checked', false);
			jQuery('#documentNumberDiv').show();
			jQuery('#docapprovedDiv').hide();
			jQuery('#plotDevelopNewDiv').hide();
			jQuery('#plotdevFmsSketchDiv').hide();
			jQuery('#fmsfullDiv').hide();
			jQuery('#partofLaout').hide();
			
			document.getElementById("layoutdextentInsqmt").value="";

		}		
		else{

			jQuery('#docapprovedDiv').show();
			jQuery('#documentNumberDiv').hide();
			//jQuery('#fmsfullDiv').hide();
			jQuery("#docandLayoutDiv").show();
		//	jQuery('#plotDevelopNewDiv').hide();
         //jQuery('#partofLaout').hide();
		if(fixlayout==undefined){
		
			jQuery("#docandLayoutDiv").show();
	 		jQuery('#documentNumberDiv').hide();
			jQuery('#docapprovedDiv').show();
			jQuery('#plotDevelopNewDiv').hide();
			jQuery('#plotdevFmsSketchDiv').hide();
			jQuery('#fmsfullDiv').hide();
			jQuery('#partofLaout').hide();
			}
		else{
		if( fixlayout == "false" ){
			
			jQuery('#partofLaout').show();
			jQuery('#plotdevFmsSketchDiv').hide();
			jQuery('#plotDevelopNewDiv').hide();
			jQuery("#docandLayoutDiv").show();
			jQuery('#fmsfullDiv').show();
			jQuery('#partofLaout').hide();
			if(fmsSketch== undefined){
				jQuery('#showmes').hide();
			}else{
			if(fmsSketch == "true")
			{
			jQuery('#showmes').hide();
			}
		else{
			jQuery('#showmes').show();
			}
		}		
		}		
		else
			{
			jQuery('#fmsfullDiv').hide();
			jQuery('#partofLaout').show();
			jQuery('#docapprovedDiv').show();
			jQuery('#plotdevFmsSketchDiv').hide();
			jQuery('#plotDevelopNewDiv').show();
			jQuery("#docandLayoutDiv").show();
			jQuery('#showmes').hide();
			if(devlopBy==undefined){
				jQuery('#plotDevelopNewDiv').hide();
				jQuery("#docandLayoutDiv").show();
				jQuery('#docapprovedDiv').show();
				jQuery('#plotDevelopNewDiv').show();
				jQuery('#partofLaout').hide();
				jQuery('#plotdevFmsSketchDiv').hide();
				jQuery('#fmsfullDiv').hide();
			}
			else{
			if(devlopBy== "true")
			{
			
			jQuery('#plotdevFmsSketchDiv').show();
			jQuery('#partofLaout').hide();
			}
		else{

			jQuery('#plotDevelopNewDiv').show();
			jQuery("#docandLayoutDiv").show();
			jQuery('#partofLaout').show();
			jQuery('#plotdevFmsSketchDiv').hide();
			jQuery('#fmsfullDiv').hide();
			}
			}
			
		}	
		}	
		}
	 	}
		}
				
	function checkdocumentDetailCheckBoxOnload()
	{

		var docEnclosed=jQuery("input[name='documentHistory.wheatherdocumentEnclosed']:checked").val();
		var partlayout=jQuery("input[name='documentHistory.wheatherpartOfLayout']:checked").val();
		var plotdevelop=jQuery("input[name='documentHistory.plotDevelopedBy']:checked").val();
		var fixAmt = jQuery("input[name='documentHistory.wheatherFmsOrSketchCopyOfReg']:checked").val();
		var plotDevebywhom=jQuery("input[name='documentHistory.wheatherplotDevelopedBy']:checked").val();

		if(docEnclosed== undefined)
			{
			document.getElementById("documentTabEnabled").checked=false;
		}
		else
			{
			if(docEnclosed=="true"  &&(document.getElementById('documentNum').value == "" || document.getElementById('docEnclosedextentInsqmt').value == ""|| document.getElementById('documentDate').value == "" ||document.getElementById('documentDate').value == undefined)){
				document.getElementById("documentTabEnabled").checked=false;
	 	}
			else{
				if(docEnclosed=="true"  && (document.getElementById('documentNum').value != "" &&  document.getElementById('docEnclosedextentInsqmt').value != "" && document.getElementById('documentDate').value!= "" && document.getElementById('documentDate').value != undefined)){
					document.getElementById("documentTabEnabled").checked=true;
		}
				else{
			if( docEnclosed=="false" && partlayout== undefined){
				document.getElementById("documentTabEnabled").checked=false;
			}
			else
				{
				if(docEnclosed=="false" && partlayout== "false" && fixAmt==undefined){
					document.getElementById("documentTabEnabled").checked=false;
					}
				if(docEnclosed=="false" && partlayout== "false" && fixAmt!=undefined){
					document.getElementById("documentTabEnabled").checked=true;
				}
				if(docEnclosed=="false" && partlayout== "false" && plotdevelop !=undefined && fixAmt!=undefined){
					document.getElementById("documentTabEnabled").checked=true;
					}
				
				if(docEnclosed=="false" && partlayout== "true" && plotdevelop==undefined){
					document.getElementById("documentTabEnabled").checked=false;
				}
				
			
			else
				{
				if(docEnclosed=="false" && partlayout== "false" && plotdevelop=="true" && plotDevebywhom==undefined )
					document.getElementById("documentTabEnabled").checked=false;
				
				else 
					{
					if(docEnclosed=="false" && partlayout=="true" && plotdevelop=="false" && (document.getElementById('layoutdextentInsqmt').value=="" || document.getElementById('layoutdextentInsqmt').value==null))
						document.getElementById("documentTabEnabled").checked=false;

					if(docEnclosed=="false" && partlayout== "true" && plotdevelop=="true" && plotDevebywhom==undefined)
						{
						document.getElementById("documentTabEnabled").checked=false;
						}
					if(docEnclosed=="false" && partlayout== "true" && plotdevelop =="true" && plotDevebywhom!=undefined && fixAmt==undefined)
						document.getElementById("documentTabEnabled").checked=true;
					
						}
				
				}
				}
			
			}
			}
			}


		}
	function checkAutoDcrCheckBox()
	{
		var autunum=document.getElementById('autoDcrNum').value;
		if(autunum!=null && autunum!=""){
			
		document.getElementById("autoDcrTabEnabled").checked=true;
		}
		else{
			document.getElementById("autoDcrTabEnabled").checked=false;
			//document.getElementById("autoDcrTabEnabled").value=null;
			}
		
	}
	function checkRegnFormCheckBox()
	{
	
		if( document.getElementById('ownerFirstname').value==""|| 
				document.getElementById('spouseOrFatheName').value==""|| document.getElementById('applicantAddress1').value=="" || 
				document.getElementById('plotBlockNum').value==""|| document.getElementById('sitePlotNum').value=="" ||
				document.getElementById('villageName').value==null || document.getElementById('villageName').value=="-1" ||
				document.getElementById('pincode').value=="" || document.getElementById('sitePlotSurveyNum').value=="-1")
			{
			
			document.getElementById("regFormTabEnabled").checked=false;
			}
		else{
			document.getElementById("regFormTabEnabled").checked=true;
			}
	}

	function checkDocuMentCheckBox(obj)
	{
	
		if(obj.value!=null && obj.value!="" && obj.value!=-1){
			
			document.getElementById("documentTabEnabled").checked=true;
			}
			else
			{
			document.getElementById("documentTabEnabled").checked=false;
				
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
   function onchangeOfCheckList(obj,count)
	{
	//alert('remarks'+ document.getElementById('chkListDet['+count+'].checkListRemarks').value);
	base=obj.checked;
	if(base==false){
		
		//jQuery('#mandatoryForUpload').find('input,select,textarea').attr('disabled','true');
		document.getElementById('chkListDet['+count+'].uploadFile').disabled=true;
		document.getElementById('chkListDet['+count+'].uploadFile').value = '';
		document.getElementById('chkListDet['+count+'].fileName').value='';
		document.getElementById('chkListDet['+count+'].docUpload').value='';
		
	}
	else{
		document.getElementById('chkListDet['+count+'].uploadFile').disabled=false;
		var filelocalname=document.getElementById('chkListDet['+count+'].fileName').value;
		//if(filelocalname=="" || filelocalname==null){
		//alert('Please select document for the checked checkList');
		//}
		}
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
		var at="@";
		var atpos=x.indexOf("@");
		var dotpos=x.lastIndexOf(".");
		if (atpos<1 || dotpos<atpos+2 || dotpos+2>=x.length || (x.indexOf(at,(atpos+1))!=-1))
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

function validateMandatoryFieldsforServType136(){
	
	var todaysDate=getTodayDate();
	var serviceTypeCode=document.getElementById('serviceTypeCode').value;
	var user_Role= document.getElementById('userRole').value;
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
	
	if(document.getElementById('isAutoDcrMandatory').value=="true" && (document.getElementById('autoDcrNum').value==null || document.getElementById('autoDcrNum').value=="")){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="autoDCRnumber.required" />';
		return false; 
	}

	/*if(document.getElementById('isDocUplaodMendatory').value=="true" && (document.getElementById('docNumber').value==null || document.getElementById('docNumber').value=="")){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="registration.Document.required" />';
		return false; 
	}*/
	if(document.getElementById('locboundaryid').value=="" ||document.getElementById('locboundaryid').value==null || document.getElementById('locboundaryid').value=="-1"){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="locboundaryid.required" />';
		return false;
	}
	if(document.getElementById('Area').value==null || document.getElementById('Area').value=="-1"){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="Area.required" />';
		return false;
	}
	if(document.getElementById('Locality').value==null || document.getElementById('Locality').value=="-1"){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="Locality.required" />';
		return false;
	}
	if(document.getElementById('Street').value==null || document.getElementById('Street').value=="-1"){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="Street.required" />';
		return false;
	}
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
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="applicant.CommunicationAddress.required" />';
		return false;
	}
	var stateid=document.getElementById('state').value;
	if(stateid==null || stateid==""){
		if(document.getElementById('mobileNo').value=="" || document.getElementById('mobileNo').value==null){
			dom.get("bpa_error_area").style.display = '';
			document.getElementById("bpa_error_area").innerHTML = '<s:text name="owner.mobileNumber.required" />';
			return false;
		}
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
		if(document.getElementById('plotAreaInSqft').value=="" || document.getElementById('plotAreaInSqft').value==null){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="regnDetails.sitalAreasqft.required" />';
		return false;
	}if(document.getElementById('plotAreaInSqmt').value=="" || document.getElementById('plotAreaInSqmt').value==null){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="regnDetails.sitalAreasqmt.required" />';
		return false;
	}
	if(document.getElementById('Surveyor').value==null || document.getElementById('Surveyor').value=="-1")
	{
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="surveyor.required" />';
		return false;
	}
	if(document.getElementById('surveyorCode').value=="" ||document.getElementById('surveyorCode').value==null){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="surveyorCode.required" />';
		return false;
	}
	if(document.getElementById('surveyorNameLocal').value=="" ||document.getElementById('surveyorNameLocal').value==null){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="surveyorName.required" />';
		return false;
	}
	if(document.getElementById('surveyorMobNo').value=="" ||document.getElementById('surveyorMobNo').value==null){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="surveyorMobNo.required" />';
		return false;
	}
	if(user_Role!="" && user_Role!=null && user_Role!='PORTALUSERSURVEYOR'){
		if(document.getElementById('termsCondition').checked==false){
			dom.get("bpa_error_area").style.display = '';
			document.getElementById("bpa_error_area").innerHTML = '<s:text name="citizen.termsCondition.required" />';
			return false;
		}
	}
	return true;
}

function validateMandatoryFields(){
	var todaysDate=getTodayDate();
	var serviceTypeCode=document.getElementById('serviceTypeCode').value;
	
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
	/*if(document.getElementById('isDocUplaodMendatory').value=="true" && (document.getElementById('docNumber').value==null || document.getElementById('docNumber').value=="")){

		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="registration.Document.required" />';
		return false;
	}*/
/*	if(document.getElementById('adminboundaryid').value=="" ||document.getElementById('adminboundaryid').value==null || document.getElementById('adminboundaryid').value=="-1"){
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
	*/
	if(document.getElementById('locboundaryid').value=="" ||document.getElementById('locboundaryid').value==null || document.getElementById('locboundaryid').value=="-1"){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="locboundaryid.required" />';
		return false;
	}
	if(document.getElementById('Area').value==null || document.getElementById('Area').value=="-1"){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="Area.required" />';
		return false;
	}
	if(document.getElementById('Locality').value==null || document.getElementById('Locality').value=="-1"){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="Locality.required" />';
		return false;
	}
	if(document.getElementById('Street').value==null || document.getElementById('Street').value=="-1"){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="Street.required" />';
		return false;
	}
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
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="applicant.CommunicationAddress.required" />';
		return false;
	}
	var stateid=document.getElementById('state').value;
	if(stateid==null || stateid==""){
	if(document.getElementById('mobileNo').value=="" || document.getElementById('mobileNo').value==null){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="owner.mobileNumber.required" />';
		return false;
	}
	}
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
	if(document.getElementById('sitePlotSurveyNum').value==null || 
			document.getElementById('sitePlotSurveyNum').value=="-1" ||	document.getElementById('sitePlotSurveyNum').value==""){
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
	if(document.getElementById('userRole').value!="" && document.getElementById('userRole').value!=null 
			&& document.getElementById('userRole').value!='PORTALUSERSURVEYOR'){
	if(document.getElementById('Surveyor').value==null || document.getElementById('Surveyor').value=="-1")
		{
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="surveyor.required" />';
		return false;
		}
	if(document.getElementById('surveyorCode').value=="" ||document.getElementById('surveyorCode').value==null){
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="surveyorCode.required" />';
		return false;
		}
		if(document.getElementById('surveyorNameLocal').value=="" ||document.getElementById('surveyorNameLocal').value==null){
			dom.get("bpa_error_area").style.display = '';
			document.getElementById("bpa_error_area").innerHTML = '<s:text name="surveyorName.required" />';
			return false;
		}
		if(document.getElementById('surveyorMobNo').value=="" ||document.getElementById('surveyorMobNo').value==null){
			dom.get("bpa_error_area").style.display = '';
			document.getElementById("bpa_error_area").innerHTML = '<s:text name="surveyorMobNo.required" />';
			return false;
		}
	}
	var user_Role= document.getElementById('userRole').value;
	if(user_Role!="" && user_Role!=null && user_Role!='PORTALUSERSURVEYOR'){
		if(document.getElementById('termsCondition').checked==false){
			dom.get("bpa_error_area").style.display = '';
			document.getElementById("bpa_error_area").innerHTML = '<s:text name="citizen.termsCondition.required" />';
			return false;
		}
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

	if(obj=='Discard' || obj=='Modify')
		{
		return true;
		}
	document.getElementById('workFlowAction').value=obj;
	dom.get("bpa_error_area").style.display='none';
	if(dom.get("mode").value=='Forward To EE'){
		<s:if test="%{!regInspected}">
			dom.get("bpa_error_area").style.display = '';
			document.getElementById("bpa_error_area").innerHTML = 'Inspection is pending for this registration,Please complete the inspection and try again';
			return false;
		</s:if>
	}
	
	//var documentFile =dom.get("docNumber").value;
	var userRoles= document.getElementById('userRole').value;
	var autoDcrFlagEnabled=document.getElementById('autoDcrFlag').value;
	var docUploadForSurveyor=document.getElementById('isDocUploadForSurveyorFlag').value; 
	//var docTabEnable=document.getElementById('documentTabEnabled').value;
	if(userRoles!="" && userRoles!=null && userRoles=='PORTALUSERSURVEYOR' && autoDcrFlagEnabled=="true")
	{

		var count=0;
		var count1=0;
		var autoDcrNumber=document.getElementById("autoDcrNum").value;
		var autoFlag=autoDcrNumber.split("|");
		var flagauto1 = autoFlag[0];
		document.getElementById("autoDcrNum").value=flagauto1;

		var docCheckboxChecked =document.getElementById("documentTabEnabled").checked;
		var autodcrchecked=jQuery('#autoDcrTabEnabled').prop('checked');
		var inspectionformchecked =jQuery('#regInspecyionEnabled').prop('checked');
		var registerformchecked =jQuery('#regFormTabEnabled').prop('checked');
		if(obj=='Forward To AE/AEE'){
			
		/*if( docUploadForSurveyor=="true" && (documentFile==null || documentFile==""))
		 {
		 	dom.get("bpa_error_area").style.display = '';
			document.getElementById("bpa_error_area").innerHTML = '<s:text name="registration.Document.required" />';
			return false;
		 }*/
					if(!validateDocumentEvidence() )
						{
						return false;
						}
					if(!validateDocHistoryDetail())
					{ 
					return false;
					}
							
			
	 	if(!validateAutodcr())
			return false;
/* 		jQuery("[id=mandatorycheckforregcl]").each(function(index) {	
				if(jQuery(this).find(':checkbox').prop('checked')){
					var rem=jQuery(this).next("td").find('textarea').attr("value");
						if(rem==""){
							dom.get("bpa_error_area").style.display = '';
								document.getElementById("bpa_error_area").innerHTML = 'Please enter the remarks for the checked checkList';
								jQuery(this).next("td").find('textarea').css("border", "1px solid red");
								count++;
								}
				}
		});	
	if(count!=0)
	return false; */
	 
		}

		jQuery("[id=mandatorycheckforregcl]").each(function(index) {	
			if(jQuery(this).find(':checkbox').prop('checked')){
							var rem=jQuery(this).next("td").find('file').attr("value");
								if(rem==""){
									dom.get("bpa_error_area").style.display = '';
										document.getElementById("bpa_error_area").innerHTML = 'Please select document for the checked checkList';
										jQuery(this).next("td").find('file').css("border", "1px solid red");
										count1++;
										}
						}
			});	
			if(count1!=0)
			return false;
	}
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

		
	
 if(userRoles!="" && userRoles!=null && userRoles!='PORTALUSERSURVEYOR' && 
		  (document.getElementById('regServiceTypeCode').value=='01' || 
				  document.getElementById('regServiceTypeCode').value=='03' || 
				  	document.getElementById('regServiceTypeCode').value=='06')){
	if(!validateMandatoryFieldsforServType136()){
		return false; 
		}
  }
 	else{	
	  if(obj!='Save' ){
 	 	if(!validateMandatoryFields()){
		return false;
	}
	  }
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
	
		var appaddress1=document.getElementById('applicantAddress1').value;
	
		var appaddress2=document.getElementById('applicantAddress2');
	
		if( appaddress1.length>512 || (appaddress2!=null && appaddress2.length>512)){
			dom.get("bpa_error_area").style.display = '';
			document.getElementById("bpa_error_area").innerHTML = 'Applicant Address size cannot be greater than 512 characters';
			//jQuery(obj).css("border", "1px solid red");		
			return false;
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
		if(userRoles!="" && userRoles!=null && userRoles=='PORTALUSERSURVEYOR' && autoDcrFlagEnabled=="true" && obj=='Forward To AE/AEE'){
			if(document.getElementById('villageName').value!=null || document.getElementById('villageName').value!="-1"){

				document.getElementById("regFormTabEnabled").checked=true;
	}
			/*if(docCheckboxChecked==false)
			{
					dom.get("bpa_error_area").style.display = '';
					document.getElementById("bpa_error_area").innerHTML = 'Please select the Document Details Furnished checkbox ';
			return false;
				}
			
				if(autodcrchecked==false){
					dom.get("bpa_error_area").style.display = '';
					document.getElementById("bpa_error_area").innerHTML = 'Please select the Approved Auto DCR Number Entered checkbox';
					return false;
				}

				if(registerformchecked==false){
					dom.get("bpa_error_area").style.display = '';
					document.getElementById("bpa_error_area").innerHTML = 'Please select Registration Form Completed checkbox';
	return false;
					}*/
	
				if(document.getElementById('termsCondition').checked==false){
						dom.get("bpa_error_area").style.display = '';
						document.getElementById("bpa_error_area").innerHTML = '<s:text name="citizen.termsCondition.required" />';
		return false;
			}
		
			}
		 return true;
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
function validatFormOnSurveyor()
{
	if(!validateAutodcr())
		return false;
	var count=0;
	var documentFile =dom.get("docNumber").value;
	//var checkListRemarks=document.getElementById('checkListRemarks').value;
	
	
		/* if(documentFile==null || documentFile=="")
			 {
			 	dom.get("bpa_error_area").style.display = '';
				document.getElementById("bpa_error_area").innerHTML = '<s:text name="registration.Document.required" />';
				return false;
			 }*/
		
	jQuery("[id=mandatorycheckforregcl]").each(function(index) {	
		if(jQuery(this).find(':checkbox').prop('checked')){
						var rem=jQuery(this).next("td").find('file').attr("value");
							if(rem==""){
								dom.get("bpa_error_area").style.display = '';
									document.getElementById("bpa_error_area").innerHTML = 'Please select document for the checked checkList';
									jQuery(this).next("td").find('file').css("border", "1px solid red");
									count++;
									}
					}
		});	
		if(count!=0)
		return false;	
		
		if(!validateMandatoryFields()){
			return false;
		}
	return true;
}
function validautoDcr(obj){
	if(obj.value!=""){
        var num=obj.value;
        var regExp  = /^[A-Za-z0-9]+$/;
        if(!regExp.test(num)){
        	obj.value="";	
			dom.get("bpa_error_area").style.display = '';
			document.getElementById("bpa_error_area").innerHTML = '<s:text name="invalid.autoDcr" />';
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
	window.open('${pageContext.request.contextPath}/extd/search/searchAutoDcrExtn!searchAuto.action?','window','scrollbars=yes,resizable=no,height=800,width=900,status=yes');


	}
function callSetPropertyId(propId,zone,ward,area,location,street,ownerName) {
	zoneData = zone;
	wardData = ward;
	areaData = area;
	locationData = location;
	streetData = street;

	document.getElementById("propertyid").value=propId;
	document.getElementById("ownerFirstname").value=ownerName;
	//loadLeafLevelData2(zoneData,'Ward','ADMINISTRATION','0');
	//loadLeafLevelData2(wardData,'','ADMINISTRATION','1');
	loadLeafLevelData2(areaData,'Locality','LOCATION','0');
	loadLeafLevelData2(locationData,'Street','LOCATION','0');
	loadLeafLevelData2(streetData,'','LOCATION','0');
	loadBoundaryData(zoneData,wardData,areaData,locationData,streetData);
}

function loadBoundaryData(zoneData,wardData,areaData,locationData,streetData)
{
	//document.getElementById("Zone").value = zoneData;
	//document.getElementById("Ward").value = wardData;
	document.getElementById("Area").value = areaData;
	document.getElementById("Locality").value = locationData;
	document.getElementById("Street").value = streetData;

}
function callSetAutoDCR(autoDcrNumber,applicant_name,address,email,mobileno,plotno,doorno,village,surveyno,blockno,plotarea)
{
	document.getElementById("autoDcrNum").value=autoDcrNumber;
	/*var propId=document.getElementById("propertyid").value;
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
	convertSQFTtoSQMT();*/
	//disablefieldsforAutodcr();

}	
function ResetAutoDcr(){
if(document.getElementById("autoDcrNum").value!="")
	document.getElementById("autoDcrNum").value="";
}

//javascript for tabs

function showRegTab(){
  document.getElementById('regForm').style.display='';
  document.getElementById('uploadForm').style.display='none';
  document.getElementById('docHistoryForm').style.display='none';
  setCSSClasses('uploadTab','');
  
  setCSSClasses('autoDcrTab','');
  setCSSClasses('inspDtlTab','Last');
  setCSSClasses('regTab','First Active');
}

function showAutoDcrTab(){
	document.getElementById('regForm').style.display='none';
	document.getElementById('uploadForm').style.display='';
	 document.getElementById('docHistoryForm').style.display='';
    setCSSClasses('regTab','First');
    setCSSClasses('uploadTab','');
    setCSSClasses('inspDtlTab','');
    setCSSClasses('autoDcrTab','Last Active ActiveLast');
	
}

function showUploadTab(){
	document.getElementById('regForm').style.display='none';
	document.getElementById('uploadForm').style.display='';
	document.getElementById('docHistoryForm').style.display=''; 
    setCSSClasses('regTab','First');
    setCSSClasses('uploadTab','Active');
    setCSSClasses('autoDcrTab','');
    setCSSClasses('inspDtlTab','Last');
  
}

function showinspDtlTab(){
	document.getElementById('regForm').style.display='none';
	document.getElementById('uploadForm').style.display='none';
	document.getElementById('docHistoryForm').style.display='none';
    setCSSClasses('regTab','First');
    setCSSClasses('uploadTab','');
    setCSSClasses('autoDcrTab','');
    setCSSClasses('inspDtlTab','Active');
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
	function validateAutodcr()
	{
		var autunum=document.getElementById('autoDcrNum').value;
		var userRoles= document.getElementById('userRole').value;
		var autoDcrFlagEnabled=document.getElementById('autoDcrFlag').value;
		if(userRoles!="" && userRoles!=null && userRoles=='PORTALUSERSURVEYOR' && (autunum=="" ||autunum==null ) && autoDcrFlagEnabled=="true")
		{
			dom.get("bpa_error_area").style.display = '';
			document.getElementById("bpa_error_area").innerHTML = '<s:text name="autoDCRnumber.required" />';
			return false;
		}
	 return true;
	}

	function createLetterToParty(){
	if(validateAutodcr()){
	var regid=jQuery("#id").val();	
	document.location.href="${pageContext.request.contextPath}/extd/register/registerBpaExtn!letterToPartyForm.action?registrationId="+regid;
	}
	}

	function addMode(obj){
		dom.get("mode").value=obj;
	}

	function gotoPage(obj){
		
		var flag=obj;
		var regid=jQuery("#id").val();
	
	 if(flag=="Print Building Permit"){
		
		//window.open("${pageContext.request.contextPath}/extd/report/bpaReportExtn!printReport.action?registrationId="+regid+"&printMode=BuildingPermitCitizen","simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		document.location.href="${pageContext.request.contextPath}/extd/report/bpaReportExtn!printReport.action?registrationId="+regid+"&printMode=BuildingPermitCitizen";
	}

	else if(flag=="Print Plan Permit"){
		document.location.href="${pageContext.request.contextPath}/extd/report/bpaReportExtn!printReport.action?registrationId="+regid+"&printMode=PlanPermitCitizen";
	}
	else if(flag=="PrintPrintChallan")
		{
		window.open("${pageContext.request.contextPath}/extd/register/registerBpaExtn!feePaymentPdf.action?registrationId="+regid,"simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		}
	
	}
	 	
</script>
  </head>
 <body onload="bodyOnLoad();refreshInbox();" >

  
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

<s:form name="citizenRegisterBpaForm" action="citizenRegisterBpaExtn" theme="simple" onsubmit="enableFields();"   enctype="multipart/form-data">
  <s:push value="model">
  <s:token/> 
  <div class="formmainbox">
  			<s:hidden id="userRole" name="userRole" value="%{userRole}"/>
  			<s:hidden id="autoDcrFlag" name="autoDcrFlag" value="%{autoDcrFlag}"/>
		 	<s:hidden id="workFlowAction" name="workFlowAction"/>
		 	<s:hidden id="initialPlanSubmissionNum" name="initialPlanSubmissionNum" value="%{initialPlanSubmissionNum}"/>
		 	<s:hidden id="planPermitApprovalNum" name="planPermitApprovalNum" value="%{planPermitApprovalNum}"/>
		 	<s:hidden id="baNum" name="baNum" value="%{baNum}"/>
     		<s:hidden id="request_number" name="request_number" value="%{request_number}"/>
			<s:hidden id="serviceRegistryId" name="serviceRegistryId" value="%{serviceRegistryId}"/>
			<s:hidden id="isDocUploadForSurveyorFlag" name="isDocUploadForSurveyorFlag" value="%{isDocUploadForSurveyorFlag}"/>
			<s:hidden id="id" name="id" value="%{id}"/>
			<s:hidden id="createdBy" name="createdBy" value="%{createdBy.id}"/>
	   		<s:hidden id="modifiedBy" name="modifiedBy" value="%{modifiedBy.id}"/>
	   		<s:hidden id="createdDate" name="createdDate" value="%{createdDate}"/>
	   		<s:hidden id="modifiedDate" name="modifiedDate" value="%{modifiedDate}"/>
		    <s:hidden id="mode" name="mode" value="%{mode}"/>
		    <s:hidden id="state" name="state" value="%{state.id}" />
		    <s:hidden id="serviceTypeCode" name="serviceTypeCode" value="%{serviceType.code}" />
		    <s:hidden id="egwStatus" name="egwStatus" value="%{egwStatus.id}"/>
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
     		<s:hidden id="regStatus" name="regStatus" value="%{egwStatus.code}"/>
     		<s:hidden id="regInspected" name="regInspected" value="%{regInspected}"/>
     		<s:hidden id="stateForValidate" name="stateForValidate" value="%{stateForValidate}"/>
     		<s:hidden id="regServiceTypeCode" name="regServiceTypeCode" value="%{regServiceTypeCode}"/>
     		<s:hidden id="mobileNo" name="mobileNumber" value="%{mobileNumber}"/>
     		<s:hidden id="email" name="emailId" value="%{emailId}"/>
     		
  	<div id="regtabs">
  		               <ul > 
						<s:if test="(userRole=='PORTALUSERSURVEYOR')">
							<li><a href="#inspDtlTab" title=""><s:text name="Inspection Details" /></a></li>
							<li><a href="#autoDcrTab" title=""><s:text name="Auto Dcr Details" /></a></li>
							<li><a href="#uploadTab" title=""><s:text name="Document List" /></a></li>
							<li><a href="#regTab" title=""><s:text name="RegisterBpaHeader" /> </a></li>
							<li><a href="#submitTab" title=""><s:text name="Submit Details" /> </a></li>
							
						 	  </s:if>
						 <s:else>
						 	<li><a href="#regTab" title=""><s:text name="RegisterBpaHeader"/> </a></li>
						 	<li><a href="#uploadTab" title=""><s:text name="Document List"/></a></li>
						 	
						 </s:else>
						<li>
							<td></td>
						</li>
						</ul>
          		 <div id="regTab">
			                <%@ include file="citizenRegisterBpaExtnForm.jsp"%>    
				</div> 
		<s:if test="(userRole=='PORTALUSERSURVEYOR')"> 
		 <div id="autoDcrTab">
			<tr>	
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox" width="20%"><s:text name="autoDcrNum" /> :</td>
			<td id="autodcrAtag" class="bluebox" ><s:textfield id="autoDcrNum" name="autoDcrNum" value="%{autoDcrNum}" onchange="checkAutoDcrCheckBox();"  />
				<s:hidden name="autoDcrId" id="autoDcrId"
						value="%{autoDcrId}" /></td>
		</tr>
		 </div>
			</s:if>
		  		

	         <div id="uploadTDocId" >
	     <div id="uploadTab" >
	    	<s:if test="(mode=='view'|| mode=='noEdit' )">
	   	<s:if test="(userRole=='PORTALUSERSURVEYOR')">
	    <div id="docHistoryForm" style="display:none;">    
	          	<%@ include file="../common/documentHistoryDetails.jsp"%>
	   		</div>
	   		</s:if>
	   		</s:if>
	   		<div id="attachDocForm">
	     <div  class="headingsmallbg"><span class="bold"><s:text name="attach.documents"/></span></div>
	       <%@ include file="../common/submitChecklistExtn.jsp"%>
			</div>
				</div>
				
						 	
  	
  	</div>
  	 
  	  <s:if test="(userRole=='PORTALUSERSURVEYOR')">
  	 	<div id="inspDtlTab">
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
									 			 <s:url id="measurelink" value="/extd/inspection/inspectionExtn!showInspectioninRegistration.action" escapeAmp="false">
										      <s:param name="registrationId" value="%{id}"></s:param>	  
										   </s:url>
										        <td class="greybox" width="15%">&nbsp;</td>
												<td class="greybox"  width="15%"><s:text name="Site Inspection Fixed Date"/></td> 
									   			<td class="greybox" width="15%"> <s:textfield value="%{postponedInspectionDetails[0].inspectionDateString}" id="inspectionFixedDate" name="inspectionFixedDate" disabled="true" /></td>   			
												<td  class="greybox"><div align="center"> <sj:a  onClickTopics="openRemoteDialog" href="%{measurelink}" button="true"  buttonIcon="ui-icon-newwin">Postponed Details</sj:a></div></td>
												
									         </tr>  
									         
									         <tr>
									 			 <s:url id="measurementlink" value="/extd/inspection/inspectionExtn!showMeasurementdetailsinRegistration.action" escapeAmp="false">
										      <s:param name="registrationId" value="%{id}"></s:param>	  
										   </s:url>
										        <td class="bluebox" width="15%">&nbsp;</td>
											    <td class="bluebox">&nbsp;</td>
											    <td class="bluebox">&nbsp;</td>
												<td class="bluebox">&nbsp;</td>
									         </tr>  
									               
									      </table>
									
								  
								    </s:if>
								     <s:if test="(userRole=='PORTALUSERSURVEYOR') " >
								     <s:url id="measureajax" value="/extd/inspection/inspectionExtn!showExistingInspectionMeasurementDetails.action"  escapeAmp="false">	 
									   <s:param name="registrationId" value="%{id}"></s:param>	      
									   </s:url>
									   <div style="display:none">
									  <sj:a  id="reloadLink" onClickTopics="reloadmeasurement">Refresh Div</sj:a>
									 </div> 
								       <sj:div href="%{measureajax}" indicator="indicator" reloadTopics="reloadmeasurement"  cssClass="formmainbox" id="tab3"  dataType="html" onCompleteTopics="">
								          <img id="indicator" src="<egov:url path='/images/loading.gif'/>" alt="Loading..." style="display:none"/>
								    </sj:div>
								    </s:if>
		 </div>
		 </s:if>
		   <div id="submitTab">
						<s:if test="%{mode=='view'}">
  	        <s:if test="%{isUserMappedToSurveyorRole()}">
             <s:if test="%{regInspected}">
             <div id="disableCheckBox">
		  <table>
		  
		  <tr>
		  <td class="bluebox" width="30%">&nbsp;</td>       
											<td class="bluebox" width="15%"><s:text
													name=" Registration Form Completed"></s:text></td>
											<td class="bluebox"><s:checkbox name="regFormTabEnabled"
													id="regFormTabEnabled" value="%{regFormTabEnabled}"
													 /></td>
                 </tr>
                             <tr>  
											<td class="greybox" width="30%">&nbsp;</td>
											<td class="greybox" width="15%"><s:text
													name=" Approved Auto DCR Number Entered"></s:text></td>
											<td class="greybox"><s:checkbox name="autoDcrTabEnabled"
													id="autoDcrTabEnabled" value="%{autoDcrTabEnabled}"
													 /></td>
										</tr>
										<tr>
                             <td class="bluebox" width="30%">&nbsp;</td>   
											<td class="bluebox" width="15%"><s:text
													name="Document Details Furnished"></s:text></td>
											<td class="bluebox"><s:checkbox
													name="documentTabEnabled" id="documentTabEnabled"
													value="%{documentTabEnabled}"  />
		  </tr>
		  
										<tr>
											<td class="greybox" width="30%">&nbsp;</td>
											<td class="greybox" width="15%"><s:text
													name="Site Inspection details Furnished"></s:text></td>
											<td class="greybox"><s:checkbox
													name="regInspecyionEnabled" id="regInspecyionEnabled"
													value="%{regInspecyionEnabled}" />
		  </tr>
		  </table></div><table>
										<tr>
									
	<div>
		<sj:dialog 
	    	id="rules" 
	    	autoOpen="false" 
	    	modal="true" 
	    	title="Terms & Conditions"
	    	openTopics="openRemoteDialog"
	    	height="300"
	    	width="500"
	    	requiredposition="left"
	    	zindex=""
	    	dialogClass="formmainbox"
	    	showEffect="slide" 
	    	hideEffect="slide" 
	    	onOpenTopics="openTopicDialog" cssStyle="BACKGROUND-COLOR: #ffffff"
	    	onCompleteTopics="dialogopentopic" 
	    	loadingText="Please Wait ...."
	    	errorText="Permission Denied"
	    />
	    <br></br>
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	       		<tr>
	       		<s:url id="conditionsLink" value="/extd/portal/citizenRegisterBpaExtn!showTermsandConditions.action" escapeAmp="false">
				        </s:url> 
					
					<td class="bluebox"><div  class="subheadsmallnew" style="background-color: #fffff0; border-bottom:1px #cccccc;">
						<s:checkbox name="termsCondition" id="termsCondition" value="%{termsCondition}"/>
						<sj:a  onClickTopics="openRemoteDialog" href="%{conditionsLink}" button="true" buttonIcon="ui-icon-newwin"><s:text name="citizen.termsCondition.lbl" /></sj:a>
						
						</div></td>
					
				</tr>
		  </table>
	</div> 
	
										</tr>
				</table>
			</s:if>
			</s:if>
			
			</s:if>
				</div>
		
		
  	<table>
  	 <tr>
  <td class="greybox" align="left" ><span class="mandatory">* Mendatory Fields </span></td>
  	 </tr>
  	 <tr>
  	<td class="greybox" align="left" ><span class="mandatory">** Use Document Details Tab to upload files </span></td></tr>
  	</table>
  	</div>     
  		
  		</div>
 
		
	<div class="buttonbottom" align="center">
		<table>
			<s:if test="%{mode=='view'}">
  			<td>
			<s:if test="%{isUserMappedToSurveyorRole()}">
								<s:iterator value="citizenActionMap">
									<td><s:submit type="submit" cssClass="buttonsubmit"
											value="%{key}" id="%{key}" name="%{key}" method="%{value}"
											onclick="return validateForm('%{key}');" /></td>
								</s:iterator>
				</s:if> 
  			<s:if test="%{!isUserMappedToSurveyorRole()}">
  				<s:iterator value="citizenActionMap" > 
	  				<td><s:submit type="submit" cssClass="buttonsubmit" value="%{key}" id="%{key}" name="%{key}" method="%{value}"  onclick="return validateForm('%{key}');" /></td>
				</s:iterator>
				</s:if>
				<s:if test="%{isUserMappedToSurveyorRole()}">
                                 <s:if test="%{!regInspected}">
                                   <td> <input type="button" name="inspectionDetails" id="inspectionDetails" class="button" value="Add Site Inspection" onclick="addInspection();"/>  </td>
                                      </s:if>
               </s:if>
			 	<s:else>
				<td width="10% "></td>
				<!--<s:if test="%{egwStatus.code=='Application Forwarded to LS' || egwStatus.code=='Site Inspected By LS'}">
				<td width="20%">&nbsp;</td>
					<td width="10%">&nbsp;</td>
					<td width="10%">&nbsp;</td>
				<td width="10%"><input type="button" name="printChallan" id="printChallan" class="button" value="Print Challan" onclick=" gotoPage('PrintPrintChallan');"/></td>
			
				</s:if> -->
				<s:iterator value="citizenActionPrintPermitMap" > 
			
					<td width="10% "><input  type="button"   class="buttonsubmit" id="<s:property value="key"/>" value="<s:property value="key"/>" name="<s:property value="key"/>"   onclick=" return gotoPage('<s:property value="key"/>')" /></td>
				
		     </s:iterator>
			 <s:iterator value="citizenActionTabMap" >
		  	  	   <td><s:submit type="submit" cssClass="buttonsubmit" value="%{key}" id="%{key}" name="%{key}" method="%{value}" /></td>
			     </s:iterator>
				
			</s:else>
			</td>
			

	  	
	  	</s:if>  	
	  		<s:if test="(userRole!='PORTALUSERSURVEYOR') ">
	  		 <s:if test="mode==null || mode=='' || mode=='editForCitizen'">
	  		   <s:if test="%{!citizenActionMap.isEmpty}">	
	  			  <td>
	  				<s:iterator value="citizenActionMap" > 
		  				<td><s:submit type="submit" cssClass="buttonsubmit" value="%{key}" id="%{key}" name="%{key}" method="%{value}"  onclick=" return validateForm('submit');" /></td>
					</s:iterator>
		  		  </td>
		  		</s:if>	
		  		</s:if>
		  		</s:if>
		  		
		  		<td>
	  			&nbsp;<input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();"/>
	  		</td>
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
  	   var mode=document.getElementById('mode').value;
  	     var url;
  	     if(v==null||v==''||v=='To be assigned')
  	     {
  		   url="/egi/docmgmt/basicDocumentManager.action?moduleName=BPA";
  	     }
  	     /*else if(v!=null && mode=='view'  )
  	  	   {
  	    	url="/egi/docmgmt/basicDocumentManager.action?moduleName=BPA";
  	  	  }*/
  	     else
  	     {
  	       url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="+v+"&moduleName=BPA";
  	     }
  	     var wdth = 1000;
  	     var hght = 400;
  	     window.open(url,'docupload','width='+wdth+',height='+hght);
  	 }

  function addInspectiondate(){
		var regid=jQuery("#id").val();
		document.location.href="${pageContext.request.contextPath}/extd/inspection/inspectionExtn!newForm.action?registrationId="+regid+"&fromCitizenReg=true","simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes";
	}


	function addInspection(){
		var regid=jQuery("#id").val();	
		document.location.href="${pageContext.request.contextPath}/extd/inspection/inspectionExtn!inspectionForm.action?registrationId="+regid+"&fromreg=true","simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes";
	}
		
  </script>
  </body>
  </html>
  	