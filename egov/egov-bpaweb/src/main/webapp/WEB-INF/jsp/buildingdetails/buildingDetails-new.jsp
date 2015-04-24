<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<style>
</style>

<title><s:text name='bldgMeasure.Title' /></title>

<sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true"/>

<title><s:text name='bldgMeasure.Title' /></title>


<script type="text/javascript">

jQuery.noConflict();

var cmdaindex=0;

		
	function validateForm(){
	
	var tbl = document.getElementById('floorDetails');
    var flrCount = document.getElementById('floorCount').value;
    var tbody=tbl.tBodies[0];
    var lastRow = tbl.rows.length;  
    var servType = document.getElementById("serviceType").value;
   
    var autodcrname=document.getElementById("autodcr").value;
   
    var count=0;
    
    hideFieldError();
    if(jQuery('#unitClassification').val()==-1){
    	   
        showerrormsg(jQuery('#unitClassification'),"Select type of housing unit ");
       	count++;
        }
    if(count!=0)
 		return false;
    if((jQuery('#unitClassification').val()=="Multiple" || jQuery('#unitClassification').val()=="Single") && jQuery('#unitCount').val()==""){
    	 showerrormsg(jQuery('#unitCount'),"Number of housing units is mandatory if housing unit is Multiple/Single ");
    	count++;
     }
    if(count!=0)
 		return false;
		
		if(jQuery('#unitCount').val()!=""){
			 var num=jQuery('#unitCount').val();
		        var regExp  = /^[0-9]+$/;
		        if(!regExp.test(num)){
		        	 showerrormsg(jQuery('#unitCount'),"Please Enter Numeric Value");
		    	    	count++;
			}
		}
		  if(count!=0)
				return false;
    if(jQuery('#unitClassification').val()=="Multiple" && jQuery('#unitCount').val()!="")
    	{
    	 if(jQuery('#unitCount').val()!=null && jQuery('#unitCount').val() < 2){
				//alert('pos '+ obj.value);
    		 showerrormsg(jQuery('#unitCount'),"If Type of Housing Unit is Multiple Then Number of Housing Unit Should be greater than or equal to 2");
    	    	count++;
			    }
    	 
	    }
    if(count!=0)
		return false;
    if(jQuery('#unitClassification').val()=="Single" )
	{
	 if(jQuery('#unitCount').val()!=null && jQuery('#unitCount').val() > 1){
			//alert('pos '+ obj.value);
		 showerrormsg(jQuery('#unitCount'),"If Type of Housing Unit is Single Then Number of Housing Unit Should be less than or equal 1");
	    	count++;
		    }
	 
    }
 		
    if(count!=0)
 		return false;

	
    	if(jQuery('#floorCount').val()==""){
   
       	showerrormsg(jQuery('#floorCount'),"floor count is mandatory");
      	count++;
       	}
    	if(jQuery('#floorCount').val() <= 0){
    	showerrormsg(jQuery('#floorCount'),"floor count value should not be Zero");
        count++;
        }
    	
		if(count!=0)
    		return false;
	
	
		if(jQuery('#totalFloorArea').val()=="")
		{
		showerrormsg(jQuery('#totalFloorArea'),"Please enter total floorArea");
		count++;
		}
		if(count!=0)
		return false;

		if(jQuery('#buildingHeight').val()=="")
		{
		showerrormsg(jQuery('#buildingHeight'),"Please enter building height");
		count++;
		}
		if(count!=0)
		return false;
		
		if((servType=="01" || servType=="03" || servType=="06" )&& (jQuery('#buildingHeight').val()!=null && jQuery('#buildingHeight').val()!="")) 
		    {
			 if(jQuery('#buildingHeight').val()!=null && jQuery('#buildingHeight').val() > 9){
					//alert('pos '+ obj.value);
				 showerrormsg(jQuery('#buildingHeight'),"Please enter Height of Building(in Mts.) should be less or equal to 9.");
			    	count++;
				    }
	     	}
		 if(count!=0)
				return false;
    	if(!validateFloorDetail())
		{ 
		return false;
		}
		
		/*if((servType=="01" || servType=="03" ) && autodcrname==0){
			
		if((lastRow-1) != flrCount) 
		{
		dom.get("bpa_error_area").style.display='';
    	document.getElementById("bpa_error_area").innerHTML='Number of floors must be equal to the number of storeys in building';
        dom.get("bpa_error_area").style.display='';
   		return false;
		}
		} */
		if(!uniqueFloorName())
	    {
		return false;
		}
		
			
	
	}
	
	function hideFieldError()
	  {
		
					jQuery("[id=mandatoryfields]").each(function(index) {	
			   		  jQuery(this).next('td').find('input,select').css("border", "");
			   		  });
	
	  }


	function validateIsNan(obj)
	 {
			 var iChars = "!@$%^*+=[']`~';{}|\":<>?#_";
		    if(obj!=null && obj.value!=null && isNaN(obj.value) )
		    {
		    	dom.get("bpa_error_area").style.display='';
				document.getElementById("bpa_error_area").innerHTML='Pleas Enter only Numbers';
				 dom.get(obj).value = "";  
			 return false;
		    }
		    if(obj!=null && obj.value < 0 ){
				//alert('pos '+ obj.value);
		    	dom.get("bpa_error_area").style.display='';
				document.getElementById("bpa_error_area").innerHTML='Pleas Enter only Positive Numbers';
				 dom.get(obj).value = "";  
				 return false;
			    }
		   
		 		for (var i = 0; i < obj.value.length; i++)
		 		{
				 if ((iChars.indexOf(obj.value.charAt(i)) != -1)||(obj.value.charAt(0)==" "))
		 		{
			 	dom.get("bpa_error_area").style.display='';
		 		document.getElementById("bpa_error_area").innerHTML='Special characters are not allowed';
		 		obj.value="";
		 		obj.focus();
		 		return false;
		 		}
		 		}
		  	 return true; 
		 }
	
/*
 * To check the Unique Floore Name for rows...
 */
 function uniqueFloorName()
	{
 	var tableObj=document.getElementById('floorDetails');
 	var lastRow = tableObj.rows.length;
 	var floorName1,floorName2,proposeusage,proposeusage1;
 	var i,j,k;
 	if(lastRow>2)
 	{
		for(i=0;i<lastRow-2;i++)
		{
			existusage=document.forms[0].existBldgUsage[i].value;
  			proposeusage=document.forms[0].propBldgUsage[i].value;
    	 	floorName=document.forms[0].floorNum[i].value;
    	 	for(j=i+1;j<lastRow-1;j++)
    		 {
     	    	floorName1=document.forms[0].floorNum[j].value;
     	    	existusage1=document.forms[0].existBldgUsage[j].value;
          		proposeusage1=document.forms[0].propBldgUsage[j].value;
          		if(floorName==floorName1)
     	    	{ 
         	    if((floorName && proposeusage)==(floorName1 && proposeusage1) ){
             	
     	    	dom.get("bpa_error_area").style.display='';
     	    	document.getElementById("bpa_error_area").innerHTML='Floor number and Proposed Building Usage should be unique';
     	        dom.get("bpa_error_area").style.display='';
     	   		return false;
         	    }
           	}
     	  	 for(k=j+1;k<lastRow-1;k++)
        	 	{
        		floorName2=document.forms[0].floorNum[k].value;
        		if(floorName1 == floorName2 && floorName == floorName2){
        		dom.get("bpa_error_area").style.display='';
       	    	document.getElementById("bpa_error_area").innerHTML='Floor number cannot be enterd more than two times';
       	        dom.get("bpa_error_area").style.display='';
  				return false;
            		 }
        	 }
     		}
    	}
 	}
		return true;
	}


/*
 * this method is for validate the floore details...
 */

	function validateFloorDetail()
	{
	
    var tableObj=document.getElementById('floorDetails');
    var lastRow = tableObj.rows.length;
    var floorName,existarea,existusage,proposearea,proposeusage;
    var i;
    if(lastRow>2)
    {
    	
    	for(i=0;i<lastRow-1;i++)
    	{
        	
         	floorName=document.forms[0].floorNum[i].value;
         	existarea=document.forms[0].existingBldgArea[i].value;
         	existusage=document.forms[0].existBldgUsage[i].value;
         	proposearea=document.forms[0].proposedBldgArea[i].value;
         	proposeusage=document.forms[0].propBldgUsage[i].value;
         	
          	 if(!validateFloorLines(floorName,existarea,existusage,proposearea,proposeusage,i+1))
            	   return false;
     		}
   		
   		return true;
   	}
   	else
   	{
   	    floorName=document.getElementById('floorNum').value;
   	 	existarea=document.getElementById('existingBldgArea').value;
  		existusage=document.getElementById('existBldgUsage').value;
  		proposearea=document.getElementById('proposedBldgArea').value;
  		proposeusage=document.getElementById('propBldgUsage').value;
   	    if(!validateFloorLines(floorName,existarea,existusage,proposearea,proposeusage,1))
   	       return false;
   	     else
   	       return true;
   	    
   	}
  }

	function validateFloorLines(floorName,existarea,existusage,proposearea,proposeusage,row)
	{
	var autodcr=document.getElementById("autodcr").value;
	var servType = document.getElementById("serviceType").value;
    dom.get("bpa_error_area").style.display='none';
  
   	 	if(floorName=="" || floorName==-10)
     	{
   	     	
        	document.getElementById("bpa_error_area").innerHTML='';
 			document.getElementById("bpa_error_area").innerHTML='Enter floor Number for row'+" :"+row;
 			dom.get("bpa_error_area").style.display='';
 			return false;
        }
   	/* if(autodcr!=0){
   		
      if(floorName=="" || floorName==-10){
    	 
    	  document.getElementById("bpa_error_area").innerHTML='';
		document.getElementById("bpa_error_area").innerHTML='Floore Details Entered in Backend are not proper from jsp';
		dom.get("bpa_error_area").style.display='';
		return false;
      }
   	   	 }*/
     
     	if(servType=="01" || servType=="03" || servType=="06" || servType=="07") {
         	if(proposearea=="" || proposeusage=="" || proposeusage==-1 )
             {
        	 	document.getElementById("bpa_error_area").innerHTML='Enter proposed details for row'+" :"+row;
				dom.get("bpa_error_area").style.display='';
				return false;
             }
    	 }
     	if(servType=="03" || servType=="06")
         {
    	 	if(existarea=="" || existusage=="" || existusage==-1 )
         	{
    	 	document.getElementById("bpa_error_area").innerHTML='Enter existed details for row'+" :"+row;
			dom.get("bpa_error_area").style.display='';
				return false;
        	 }
         }
      
       return true;
	}
	
	function addFloor()
	{   
			var tableObj=document.getElementById('floorDetails');
			//var	cmdaindex=tableObj.rows.length-1;
			var tbody=tableObj.tBodies[0];
			var lastRow = tableObj.rows.length;
			var rowObj = tableObj.rows[1].cloneNode(true);
			tbody.appendChild(rowObj);
			var rowno = parseInt(tableObj.rows.length)-2;
			document.forms[0].srlNo[lastRow-1].value=tableObj.rows.length - 1;		
			document.forms[0].floorNum[lastRow-1].value="-10";
		    document.forms[0].existingBldgArea[lastRow-1].value="";
		    document.forms[0].existBldgUsage[lastRow-1].value="-1";
		    document.forms[0].proposedBldgArea[lastRow-1].value="";
		    document.forms[0].propBldgUsage[lastRow-1].value="-1"; 
		  	document.forms[0].cmdaddListId[lastRow-1].value="";
		  
		  	document.forms[0].srlNo[lastRow-1].setAttribute("name","builflorlsList["+cmdaindex+"].srlNo");
		    document.forms[0].floorNum[lastRow-1].setAttribute("name","builflorlsList["+cmdaindex+"].floorNum");
			document.forms[0].existingBldgArea[lastRow-1].setAttribute("name","builflorlsList["+cmdaindex+"].existingBldgArea");
	        document.forms[0].existBldgUsage[lastRow-1].setAttribute("name","builflorlsList["+cmdaindex+"].existingBldgUsage.id");
	        document.forms[0].proposedBldgArea[lastRow-1].setAttribute("name","builflorlsList["+cmdaindex+"].proposedBldgArea");
	        document.forms[0].propBldgUsage[lastRow-1].setAttribute("name","builflorlsList["+cmdaindex+"].proposedBldgUsage.id");
	        document.forms[0].cmdaddListId[lastRow-1].setAttribute("name","builflorlsList["+cmdaindex+"].id");		    
			cmdaindex++;
			
	
	   }
	

	function delFloor(obj)
	{
				var tb1=document.getElementById("floorDetails");
		        var lastRow = (tb1.rows.length)-1;
		        var curRow=getRow(obj).rowIndex;
		        dom.get("bpa_error_area").style.display='none';
		        if(lastRow ==1)
		      	{
		     		 dom.get("bpa_error_area").style.display='none';
		     		 document.getElementById("bpa_error_area").innerHTML='This row can not be deleted';
		   			 dom.get("bpa_error_area").style.display='';
		      	     return false;
		        }
		      	else
		      	{
		      		// alert(curRow);
		      		var updateserialnumber=curRow;
		 			for(updateserialnumber;updateserialnumber<tb1.rows.length-1;updateserialnumber++)
		 			{
		 				if(document.forms[0].srlNo[updateserialnumber]!=null)
		 					document.forms[0].srlNo[updateserialnumber].value=updateserialnumber;
		 			}
		 			
		 			tb1.deleteRow(curRow);
		 			return true;
		      }
		  
  				
	}
	

	function bodyOnLoad() {
		
		var servType = document.getElementById("serviceType").value;
		var mode = document.getElementById("mode").value;
		cmdaindex=document.getElementById('floorDetails').rows.length-1;
			if(servType=="01" || servType=="05" || servType=="07") {
					document.getElementById("existBldgAreaReqd").style.display='none';
					document.getElementById("existBldgUsgReqd").style.display='none';
					document.getElementById("propBldgAreaReqd").style.display='';
					document.getElementById("propBldgUsgReqd").style.display='';
				}
			if(servType=="03" || servType=="06") {
				document.getElementById("existBldgAreaReqd").style.display='';
				document.getElementById("existBldgUsgReqd").style.display='';
				document.getElementById("propBldgAreaReqd").style.display='';
				document.getElementById("propBldgUsgReqd").style.display='';
			}
			
			if(mode=="view") {
				refreshInbox();
				  for(var i=0;i<document.forms[0].length;i++)
		    		{  
			    	
			    	document.forms[0].elements[i].disabled =true;
			    	 document.getElementById("close").disabled=false;
		    	
				}
			}
	}
	function onchangeofdropdown()
	{
		var classsification=document.getElementById("unitClassification").value;
        if(classsification=='Single')
            {
        	document.getElementById("unitCount").value="";
             document.getElementById("unitCount").readOnly=false;
            }
        else
        	document.getElementById("unitCount").value="";
        	 document.getElementById("unitCount").readOnly=false;

		}
	function loadFloorDetailJsforBasement()
		{
	  
	  	clearRows();
	  	var value=document.getElementById('floorCount').value;
		  if(value!=null && value!=""){
			  var  objSelect=document.getElementById('floorNum');
					createDropdown(objSelect,value);
		  }
		
		} 
/*
 * this method is for create dropdown for flooreName based on Storey's in building
 */
	function loadFloorDetailJs(value)
	{
	  
		clearRows();
		 if(isNumericFloors(value))
		 {	  
			  
			var  objSelect=document.getElementById('floorNum');
		  	createDropdown(objSelect,value);
		}
	} 
	 
	function createDropdown(selectObj,value)
	{
		
		var base=document.getElementById("isBasementUnit").checked;	
		objOption=document.createElement("OPTION");
		objOption.text="Terrace";
		objOption.value=-3;
		selectObj.options.add(objOption);
		if( base==true)
		{
		var objOption=document.createElement("OPTION");
		objOption.text="Basement";
		objOption.value=-2;
		selectObj.options.add(objOption);
		}
		
		objOption=document.createElement("OPTION");
		objOption.text="Stilt";
		objOption.value=-1;
		selectObj.options.add(objOption);
		var objOption=document.createElement("OPTION");
		objOption.text="Ground Floor";
		objOption.value=0;
		selectObj.options.add(objOption);
		var key,remainder;
		for(var i=1;i<value;i++)	
		{
		if(i>=10 && i<=20)
			key=i+"th";
		else
			{

			 remainder=i%10;
			 if(remainder == 1)
				 key = i+"st";
			 else if(remainder == 2)
				 key = i+"nd";
			 else if(remainder == 3)
				 key = i+"rd";	
			 else
				 key = i+"th";
			}
		objOption=document.createElement("OPTION");
		objOption.text = key;
		objOption.value =  i;
	
		selectObj.options.add(objOption);
				
				  
	  } 
	  	
	}


	function clearRows()
	{
		var table = document.getElementById('floorDetails');
		var rowslength = table.rows.length;
		while(rowslength>2) {
			table.deleteRow(rowslength-1);
			rowslength--;
			cmdaindex=1;
		}
		 document.getElementById('floorNum').options.length=1; 
			    
	}  
			 	
	function isNumericFloors(value)
	{
		if(eval(value)==0)
	   	{
		   	dom.get("bpa_error_area").style.display='';
	    	document.getElementById("bpa_error_area").innerHTML='No of storeys in building cannot be zero';
	    	document.getElementById("floorCount").value="";
	    	return false;
	    }
	     
	    return true;
	}
	function showerrormsg(obj,msg){
		
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML =msg;
		jQuery(obj).css("border", "1px solid red");		
		return false;
		}
	function enableFields()
	{
		for(var i=0;i<document.forms[0].length;i++)
		{
			document.forms[0].elements[i].disabled =false;	
		}
	}
	/*To load Parent Jsp..
	
	function refreshparentInbox() {
		
		window.opener.location.reload(true);
	}
		

	function closeParentWindow() {
	window.opener.close();
	return true;
	 }
	*/
	</script>

</head>

<body onload="bodyOnLoad();">
	<div align="left">
		<s:actionerror />
	</div>
	<div class="errorstyle" id="bpa_error_area" style="display: none;"></div>
	<div class="errorstyle" style="display: none"></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle" id="fieldError">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:if test="%{hasActionMessages()}">
		<div class="errorstyle">
			<s:actionmessage />
		</div>
	</s:if>

	<s:form name="buildingDetailsForm" action="buildingDetails"
		theme="simple" onsubmit="enableFields();">
		<s:push value="model">
			<s:token />
			<div class="formmainbox">

				<s:hidden id="registrationId" name="registrationId"
					value="%{registrationId}" />
				<s:hidden id="autodcr" name="autodcr"
					value="%{getRegistration().getAutoDcrSet().size()}" />
				<s:hidden id="serviceType" name="serviceType" value="%{serviceType}" />
				<s:hidden id="mode" name="mode" value="%{mode}" />

				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<div id="buildingForm">
								<%@ include file="buildingDetailsForm.jsp"%>
							</div>
						</td>
					</tr>
				</table>
			</div>
			<div class="buttonbottom" align="center">
				<table>
					<s:if test="%{mode != 'view'}">
						<td><s:submit type="submit" cssClass="buttonsubmit"
								value="Update Measurement" id="save" name="save" method="save"
								onclick="return validateForm();" /></td>
					</s:if>
					<td><input type="button" name="close" id="close"
						class="button" value="Close" onclick="window.close();" /></td>
				</table>
			</div>
		</s:push>
	</s:form>

</body>
</html>