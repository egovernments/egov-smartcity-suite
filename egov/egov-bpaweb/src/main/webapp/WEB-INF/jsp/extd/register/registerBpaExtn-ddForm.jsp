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
<title>
 <s:if test="%{mode=='view'}">
	<s:text name="View External(Third Party) DD Details"/>
	</s:if>
	<s:else>
	<s:text name="External(Third Party) DD Details"/>
	</s:else>
</title>	

<head>
<sj:head jqueryui="true" jquerytheme="redmond"  loadAtOnce="true"/>

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
</style>

 <script type="text/javascript" src="<c:url value='/common/js/jquerycombobox.js'/>"></script>
<script type="text/javascript">

jQuery.noConflict();

jQuery(document).ready(function(){

 jQuery('#hiddentr').hide();

 labourindex=document.getElementById("labourWelfarelisttable").rows.length-1;
 cmdaindex=document.getElementById("cmdaddlisttable").rows.length-1;

 jQuery('#externalfeecollectedDate').datepicker({ dateFormat: 'dd/mm/yy'});
 jQuery('#externalfeecollectedDate').datepicker('getDate');   
   jQuery('#externalfeecollectedDate').datepicker( "option", "dateFormat", "dd/mm/yy" );
 
 
  if(jQuery('#mode').val()!='view'){
 jQuery("[id=lwddbankName]").each(function(index){
 jQuery(this).combobox();
 jQuery('.ui-autocomplete-input').css('width', '100%');
  
  });
        
  }else{
  jQuery("[id=lwddbankName]").each(function(index){
   jQuery(this).combobox();
   jQuery('.ui-autocomplete-input').css('width', '100%');
    jQuery(this).closest(".ui-widget").find("input, button").prop("disabled", true);
   jQuery(this).parent().find("a.ui-button").button("disable");
   
  });
  }

  if(jQuery('#mode').val()!='view'){
 jQuery("[id=lwddDate]").each(function(index){
 jQuery(this).datepicker({ dateFormat: 'dd/mm/yy'});
  });
  }else{
  jQuery("[id=lwddDate]").each(function(index){
  jQuery(this).datepicker({ dateFormat: 'dd/mm/yy'});
  jQuery(this).datepicker('getDate');   
   jQuery(this).datepicker( "option", "dateFormat", "dd/mm/yy" );

  });
  }
  
   if(jQuery('#mode').val()!='view'){
 jQuery("[id=msddbankName]").each(function(index){
 jQuery(this).combobox();
 
 jQuery('.ui-autocomplete-input').css('width', '100%');
  
  });
  }else{
  jQuery("[id=msddbankName]").each(function(index){
   jQuery(this).combobox();
   jQuery('.ui-autocomplete-input').css('width', '100%');
    jQuery(this).closest(".ui-widget").find("input, button").prop("disabled", true);
   jQuery(this).parent().find("a.ui-button").button("disable");
  });
  }
     if(jQuery('#mode').val()!='view'){
 jQuery("[id=msddDate]").each(function(index){
 jQuery(this).datepicker({ dateFormat: 'dd/mm/yy'});
 
  
  });
   }else{
  jQuery("[id=msddDate]").each(function(index){
  jQuery(this).datepicker({ dateFormat: 'dd/mm/yy'});
  jQuery(this).datepicker('getDate');   
   jQuery(this).datepicker( "option", "dateFormat", "dd/mm/yy" );

  });
  }
 
 
if( jQuery('#mode').val()=="view"){
 refreshInbox();  

for(var i=0;i<document.forms[0].length;i++)
			{
				document.forms[0].elements[i].disabled =true;
			}
			jQuery("#close").removeAttr('disabled');
			jQuery("#Print").removeAttr('disabled');
			
			
			if( jQuery('#fromreg').val()=="true"){
            // window.opener.callfeedetails();
              }  
              
              
			}

 jQuery(this).each(function(index){
  var feeval= jQuery(this).attr("value");
  if(feeval==""){
  jQuery(this).val(0);
  }
  });
 calculateTotal();
 //if( jQuery('#mode').val()!="view")
 //alert("Total DD amount to be collected for CMDA is "+totalcmda.value +  " Rs. And for Labour Welfare fee is "+totalmwgwf.value + " Rs.");

});

	function calculateTotal(){
	
	  var cmdaAmounttotal=0;
	   var mwgwfAmounttotal=0;
	   
		//resetborder();
	 jQuery("[id=msddAmount]").each(function(index){
	  var feeval= jQuery(this).attr("value");
	  if(feeval!=""){ 
	  checkNumbers(feeval);
	  cmdaAmounttotal=new Number(cmdaAmounttotal)+new Number(feeval);
	  
	  }
	 });	
	 jQuery("#cmdaAmounttotal").val(cmdaAmounttotal);
	 
	 
	  jQuery("[id=lwddAmount]").each(function(index){
	  var feeval= jQuery(this).attr("value");
	  if(feeval!=""){
	  checkNumbers(feeval);
	  mwgwfAmounttotal=new Number(mwgwfAmounttotal)+new Number(feeval);
	   
	  } 
	 });	
	 jQuery("#mwgwfAmounttotal").val(mwgwfAmounttotal);
	 
	 
	 
	 
	 
	}

	function checkNumbers(){

 	jQuery("[id=numbers]").each(function(index) {	

	var values=(jQuery(this).find('input').attr("value"));
	if(values!=null&&values!=""){
   		checkUptoTwoDecimalPlace(values,"dd_error",jQuery(this).find('input').attr("id"),jQuery(this).find('input'));
   		}
   		});
	}




function validation(){
	var count =0
	
	resetborder();
		dom.get("dd_error").style.display = 'none';
	if(totalmwgwf.value>0 || totalcmda.value>0 ){
	if(totalmwgwf.value!=mwgwfAmounttotal.value)
		{
		showerrormsg(jQuery('#mwgwfAmounttotal')," Labour Welfare DD Amount Total should be Equal To  :"+ totalmwgwf.value);
		   count++;
		
		}
	if(count!=0)
		return false;
		
	if(totalcmda.value!=cmdaAmounttotal.value)
	{
	showerrormsg(jQuery('#cmdaAmounttotal')," CMDA DD Amount Total should be Equal To  :"+ totalcmda.value);
	   count++;
	}
	
	if(count!=0)
		return false;
	
	}
	
 jQuery("[id=numbers]").each(function(index) {	
   		  jQuery(this).find('input').css("border", "");
   		  });	
   		  

   jQuery("[id=numbers]").each(function(index) {	
	var values=(jQuery(this).find('input').attr("value"));
	if(values!=null&&values!=""){
   		var check=checkUptoTwoDecimalPlace(values,"dd_error",jQuery(this).find('input').attr("id"),jQuery(this).find('input'));
   		
   		if(check==false){
   		count++;
   		}
   		}
   		});
if(count>0){
return false;
}

 if( jQuery('#externalfeecollectedDate').val()==""){
   showerrormsg(jQuery('#externalfeecollectedDate'),"Date is mandatory");
  count++;
   }
   if(count!=0)
		return false;

   var extpernalDate= document.getElementById('externalfeecollectedDate').value;
   var applicationDate= document.getElementById('applicationDate').value;

   if(compareDate(applicationDate,extpernalDate) ==-1)
	{	
	   showerrormsg(jQuery('#externalfeecollectedDate')," Date should be greater than Registration Date :"+jQuery('#applicationDate').val())
	   count++;
	} 
 
 if(count!=0)
		return false;

if(!validateDDDetail('labourWelfarelisttable')){
    return false;
   }
  if(!validateDDDetail('cmdaddlisttable')){
    return false;
   } 
   	return true;
}

function validateDDNumberValue(obj){

	dom.get("dd_error").style.display='none';
	if(obj.value!=""){
		if(obj.value.length!=6){
			obj.value="";
			dom.get("dd_error").style.display = '';
			document.getElementById("dd_error").innerHTML = 'Pleas Enter 6 digit DD number';
			//jQuery(obj).css("border", "1px solid red");		
			return false;
		}
	}
	return true;
}


   		
function showerrormsg(obj,msg){
dom.get("dd_error").style.display = '';
document.getElementById("dd_error").innerHTML =msg;
jQuery(obj).css("border", "1px solid red");		

}

function resetborder(){
dom.get("dd_error").style.display = 'none';
jQuery("[id=numbers]").each(function(index) {
		jQuery(this).find('input').css("border", "");
		});
		
}


 function addlabourRow()
  {    
 jQuery("[id^=lwddbankName]").each(function(index){
		jQuery(this).combobox('destroy');
  	
  
  });
  
  jQuery("[id^=lwddDate]").each(function(index){
 jQuery(this).datepicker('destroy');
 });
   
 	    	var tableObj=document.getElementById('labourWelfarelisttable');
 			var tbody=tableObj.tBodies[0];
 			var lastRow = tableObj.rows.length;
 			jQuery(tableObj.rows[1]).clone().appendTo(tbody);
 			var rowno = parseInt(tableObj.rows.length)-2;
 			//document.forms[0].lwdsrlNo[lastRow-1].value=tableObj.rows.length - 1;						
 			document.forms[0].lwddAmount[lastRow-1].value="";
 			document.forms[0].lwddNumber[lastRow-1].value="";
 			document.forms[0].lwddbankName[lastRow-1].value="";
            document.forms[0].lwddDate[lastRow-1].value="";        
            document.forms[0].labourWelfareddListId[lastRow-1].value="";
            
           // document.forms[0].lwdsrlNo[lastRow-1].setAttribute("name","labourWelfareddList["+labourindex+"].lwdsrlNo");
 			document.forms[0].lwddAmount[lastRow-1].setAttribute("name","labourWelfareddList["+labourindex+"].lwddAmount");
 			document.forms[0].lwddNumber[lastRow-1].setAttribute("name","labourWelfareddList["+labourindex+"].lwddNumber");
            document.forms[0].lwddbankName[lastRow-1].setAttribute("name","labourWelfareddList["+labourindex+"].lwddbankName");
            document.forms[0].lwddDate[lastRow-1].setAttribute("name","labourWelfareddList["+labourindex+"].lwddDate");
 		    document.forms[0].labourWelfareddListId[lastRow-1].setAttribute("name","labourWelfareddList["+labourindex+"].id");		    
 			labourindex++;
 			
 	   jQuery("[id=lwddbankName]").each(function(index){
             jQuery(this).combobox();
 jQuery('.ui-autocomplete-input').css('width', '100%');

  });
   
   jQuery("[id=lwddDate]").each(function(index){
 jQuery(this).datepicker({
  dateFormat: 'dd/mm/yy',
 beforeShow: function(date) {
            jQuery("[id=lwddDate]").each(function(index){
               jQuery(this).attr('id','newid');
            });
            jQuery(this).attr('id','lwddDate');
        },
  onClose: function(date) {
            jQuery("[id=newid]").each(function(index){
               jQuery(this).attr('id','lwddDate');
            });
          
        }       
});
 });
  
 		
   }
   
   
   
 function addcmdaRow()
  {    
     jQuery("[id=msddbankName]").each(function(index){
		jQuery(this).combobox('destroy');
  	
  
  });
  
  jQuery("[id=msddDate]").each(function(index){
 jQuery(this).datepicker('destroy');
 });

 	    	var tableObj=document.getElementById('cmdaddlisttable');
 			var tbody=tableObj.tBodies[0];
 			var lastRow = tableObj.rows.length;
 			 jQuery(tableObj.rows[1]).clone().appendTo(tbody);
 			var rowno = parseInt(tableObj.rows.length)-2;
 		//	document.forms[0].msdsrlNo[lastRow-1].value=tableObj.rows.length - 1;	
            document.forms[0].msddAmount[lastRow-1].value="";
 			document.forms[0].msddNumber[lastRow-1].value="";
 			document.forms[0].msddbankName[lastRow-1].value="";
            document.forms[0].msddDate[lastRow-1].value="";
            document.forms[0].cmdaddListId[lastRow-1].value="";
            
            //document.forms[0].msdsrlNo[lastRow-1].setAttribute("name","cmdaddList["+cmdaindex+"].msdsrlNo");
 		    document.forms[0].msddAmount[lastRow-1].setAttribute("name","cmdaddList["+cmdaindex+"].msddAmount");
 			document.forms[0].msddNumber[lastRow-1].setAttribute("name","cmdaddList["+cmdaindex+"].msddNumber");
            document.forms[0].msddbankName[lastRow-1].setAttribute("name","cmdaddList["+cmdaindex+"].msddbankName");
            document.forms[0].msddDate[lastRow-1].setAttribute("name","cmdaddList["+cmdaindex+"].msddDate");
 		    document.forms[0].cmdaddListId[lastRow-1].setAttribute("name","cmdaddList["+cmdaindex+"].id");		    
 			cmdaindex++;
 			
 			
 	   jQuery("[id=msddbankName]").each(function(index){
             jQuery(this).combobox();
 jQuery('.ui-autocomplete-input').css('width', '100%');

  });
   
   jQuery("[id=msddDate]").each(function(index){
 jQuery(this).datepicker({
  dateFormat: 'dd/mm/yy',
 beforeShow: function(date) {
            jQuery("[id=msddDate]").each(function(index){
               jQuery(this).attr('id','newid');
            });
            jQuery(this).attr('id','msddDate');
        },
  onClose: function(date) {
            jQuery("[id=newid]").each(function(index){
               jQuery(this).attr('id','msddDate');
            });
          
        }       
});
 });
 	
 
 		
   }
  
  //This method is to remove rows from shopdetail table 
   function removelabourRow(obj)
   { 
	  
     	var tb1=document.getElementById("labourWelfarelisttable");
        var lastRow = (tb1.rows.length)-1;
        var curRow=getRow(obj).rowIndex;
        dom.get("dd_error").style.display='none';
        if(lastRow ==1 )
      	{
     		 dom.get("dd_error").style.display='none';
     		 document.getElementById("dd_error").innerHTML='This row can not be deleted';
   			 dom.get("dd_error").style.display='';
      	     return false;
        }
      	else
      	{
      		
       		var updateserialnumber=curRow;
       		
 			tb1.deleteRow(curRow);
 			//alert(curRow);
 			calculateTotal();
 	      	return true;
      }
   }
   
      function removecmdaRow(obj)
   { 
	  
     	var tb1=document.getElementById("cmdaddlisttable");
        var lastRow = (tb1.rows.length)-1;
        var curRow=getRow(obj).rowIndex;
        dom.get("dd_error").style.display='none';
        if(lastRow ==1 )
      	{
     		 dom.get("dd_error").style.display='none';
     		 document.getElementById("dd_error").innerHTML='This row can not be deleted';
   			 dom.get("dd_error").style.display='';
      	     return false;
        }
      	else
      	{
      		 
           	var updateserialnumber=curRow;
       		
 			tb1.deleteRow(curRow);
 			calculateTotal();
 	      	return true;
      }
   }
   
   
   function validateDDDetail(tableid)
  {    
      var tableObj=document.getElementById(tableid);
      var lastRow = tableObj.rows.length;
      var DDno,DDdate,DDAmount,DDbank;
      var i;
      if(lastRow>2)
      {
     	
      	for(i=0;i<lastRow-1;i++)
      	{      	 if(tableid=='labourWelfarelisttable'){
           	DDno=document.forms[0].lwddNumber[i].value;
            DDdate=document.forms[0].lwddDate[i].value;
            DDAmount=document.forms[0].lwddAmount[i].value;
            DDbank=document.forms[0].lwddbankName[i].value;
            }else if(tableid=='cmdaddlisttable'){
            	DDno=document.forms[0].msddNumber[i].value;
            DDdate=document.forms[0].msddDate[i].value;
            DDAmount=document.forms[0].msddAmount[i].value;
            DDbank=document.forms[0].msddbankName[i].value;
            }
             if(!validateDDLines(DDno,DDdate,DDAmount,DDbank,i+1,tableid))
            	   return false;
     	 }
     		return true;
     	}
     	else
     	{
     		 if(tableid=='labourWelfarelisttable'){
     	    DDno=document.getElementById('lwddNumber').value;
     	    DDdate=document.getElementById('lwddDate').value;
            DDAmount=document.getElementById('lwddAmount').value;
            DDbank=document.getElementById('lwddbankName').value;
            }else if(tableid=='cmdaddlisttable'){
              DDno=document.getElementById('msddNumber').value;
     	    DDdate=document.getElementById('msddDate').value;
            DDAmount=document.getElementById('msddAmount').value;
            DDbank=document.getElementById('msddbankName').value;
            }
     	   if(!validateDDLines(DDno,DDdate,DDAmount,DDbank,1,tableid))
     	      return false;
     	     else
     	       return true;
     	    
     	}
    }
    
    function validateDDLines(ddnodd,date,ddamount,ddbank,row,tableid)
    {
    if(totalmwgwf.value>0 || mwgwfAmounttotal.value>0){
       //alert(totalmwgwf.value);
  if(tableid=='labourWelfarelisttable'){
          dom.get("dd_error").style.display='none';
           if(ddnodd==""||date==""||ddamount==""||ddbank=="-1")
         {
                  document.getElementById("dd_error").innerHTML='';
     				document.getElementById("dd_error").innerHTML='DD Details are Required for table Labour Welfare Details and  row:'+" "+row;
     				dom.get("dd_error").style.display='';
      			return false;
         } else if(ddnodd!=""){
         
         if(date==""||ddamount==""||ddbank=="-1"){
            document.getElementById("dd_error").innerHTML='';
     				document.getElementById("dd_error").innerHTML='DD Details are Required for table Labour Welfare Details and row:'+" "+row;
     				dom.get("dd_error").style.display='';
     				return false;
         }
        
         
         } else if(date!=""){
         
         if(ddnodd==""||ddamount==""||ddbank=="-1"){
            document.getElementById("dd_error").innerHTML='';
     				document.getElementById("dd_error").innerHTML='DD Details are Required  for table Labour Welfare Details and  row:'+" "+row;
     				dom.get("dd_error").style.display='';
     				return false;
         }
         }else if(ddamount!=""){
         
         if(ddnodd==""||date==""||ddbank=="-1"){
            document.getElementById("dd_error").innerHTML='';
     				document.getElementById("dd_error").innerHTML='DD Details are Required for table Labour Welfare Details and row:'+" "+row;
     				dom.get("dd_error").style.display='';
     				return false;
         }
         }else if(ddbank!="-1"){
         
         if(ddnodd==""||date==""||ddamount==""){
            document.getElementById("dd_error").innerHTML='';
     				document.getElementById("dd_error").innerHTML='DD Details are Required for table Labour Welfare Details and row:'+" "+row;
     				dom.get("dd_error").style.display='';
     				return false;
         }
         }
         
         }
    }
     if(totalcmda.value>0  || cmdaAmounttotal.value>0)
		{
        	//alert(totalcmda.value);
            if( tableid=='cmdaddlisttable'){
        	
          dom.get("dd_error").style.display='none';       
           if(ddnodd!=""){
		if(date==""||ddamount==""||ddbank=="-1"){
            document.getElementById("dd_error").innerHTML='';
     				document.getElementById("dd_error").innerHTML='DD Details are Required for CMDA Details and row:'+" "+row;
     				dom.get("dd_error").style.display='';
     				return false;
         }
         } else if(date!=""){
         
         if(ddnodd==""||ddamount==""||ddbank=="-1"){
            document.getElementById("dd_error").innerHTML='';
     				document.getElementById("dd_error").innerHTML='DD Details are Required for CMDA Details and row:'+" "+row;
     				dom.get("dd_error").style.display='';
     				return false;
         }
         }else if(ddamount!=""){
         
         if(ddnodd==""||date==""||ddbank=="-1"){
            document.getElementById("dd_error").innerHTML='';
     				document.getElementById("dd_error").innerHTML='DD Details are Required for CMDA Details and row:'+" "+row;
     				dom.get("dd_error").style.display='';
     				return false;
         }
         }else if(ddbank!="-1"){
         
         if(ddnodd==""||date==""||ddamount==""){
            document.getElementById("dd_error").innerHTML='';
     				document.getElementById("dd_error").innerHTML='DD Details are Required for CMDA Details and row:'+" "+row;
     				dom.get("dd_error").style.display='';
     				return false;
         }
         }
         
         }
          
} 
     /*
     NOTE: For New record Both CMDA and MWGWF Total Cannot be Zero...
     */
     else  if(cmdaAmounttotal.value==0 && mwgwfAmounttotal.value==0){
    	 
  	   dom.get("dd_error").style.display = '';
			document.getElementById("dd_error").innerHTML = 'Please enter atleast one DD details';	
			return false; 
        
        
             
     }
          
           return true;
    }

   function enableFields(){
	for(var i=0;i<document.forms[0].length;i++)
	{
		document.forms[0].elements[i].disabled =false;	
	}
}

   function validateIsNan(obj)
	 {
		 
			 var iChars = "!@$%^*+=[']`~';{}|\":<>?#_";
		    if(obj!=null && obj.value!=null && isNaN(obj.value) )
		    {
		    //	alert('validateNAN');
		    	   document.getElementById("dd_error").innerHTML='';
     				document.getElementById("dd_error").innerHTML='Pleas Enter only Numbers';
     				dom.get("dd_error").style.display='';
				
				obj.value="";
		 		obj.focus();
			 return false;
		    }
		    if(obj!=null && obj.value < 0 ){
			    
			//	alert('pos '+ obj.value);
		    	 document.getElementById("dd_error").innerHTML='';
     				document.getElementById("dd_error").innerHTML='Pleas Enter only Positive Numbers';
				
				obj.value="";
		 		obj.focus(); 
				 return false;
			    }
		   
		 		for (var i = 0; i < obj.value.length; i++)
		 		{
				 if ((iChars.indexOf(obj.value.charAt(i)) != -1)||(obj.value.charAt(0)==" "))
		 		{
			 
		 		document.getElementById("dd_error").innerHTML='Special characters are not allowed';
		 		obj.value="";
		 		obj.focus();
		 		return false;
		 		}
		 		}
		  	 return true; 
		 }
   
</script>
</head>
<body onload="">


</div>

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
<div class="errorstyle" id="dd_error" style="display:none;"></div>
<s:form name="RegisterddForm" action="registerBpa" theme="simple" onsubmit="enableFields();" >
<s:token/>
<s:hidden id="registrationId" name="registrationId" value="%{registrationId}"/>
<s:hidden id="totalcmda" name="totalcmda" value="%{totalcmda}"/>
<s:hidden id="totalmwgwf" name="totalmwgwf" value="%{totalmwgwf}"/>
<s:hidden id="mode" name="mode" value="%{mode}"/>
<s:hidden id="fromreg" name="fromreg" value="%{fromreg}"/>

<div align="center"> 
 <div id="header" class="formmainbox">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
	   
	    	<tr id="hiddentr">
	 			
	   			<td class="greybox"> <sj:datepicker value="%{registration.planSubmissionDate}" id="applicationDate" name="registration.planSubmissionDate" displayFormat="dd/mm/yy" disabled="true" showOn="focus"/></td>   			
				
          </tr>
	    	<tr>
	 			<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox" width="13%"><s:text name="Date"/><span class="mandatory">*</span></td> 
	   			<td class="greybox"> <s:textfield value="%{registration.externalfeecollectedDate}" id="externalfeecollectedDate" name="registration.externalfeecollectedDate" /></td>   			
				<td class="greybox"><s:text name="Plan Submission Number"/></td>
				<td class="greybox"><s:textfield value="%{registration.planSubmissionNum}" disabled="true" id="plansubmissionNum" name="registration.planSubmissionNum" /></td>
				<td class="greybox">&nbsp;</td>
          </tr>
          </table>
   </div>
 
 <div id="labourwelfaredetails" class="formmainbox">
 <h1 class="subhead" ><s:text name="Labour Welfare Details"/></h1>
	<div align="center" id="dd_div">
			
	<s:if test="%{labourWelfareddList.size!=0}">
		   <table id="labourWelfarelisttable" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		  
	
 
	       <tr>
		    <th  class="bluebgheadtd" ><div align="center">DD Number</div></th>
		    <th  class="bluebgheadtd" ><div align="center">DD Amount</div></th>
		    <th  class="bluebgheadtd" ><div align="center">DD Bank</div></th>
		    <th  class="bluebgheadtd" ><div align="center">DD Date</div></th>		  
		   <s:if test="%{mode!='view'}">
		   <th  class="bluebgheadtd" width="2%"><div align="center"> Add</div></th>
		   <th  class="bluebgheadtd" width="2%"><div align="center"> Delete</div></th>
			</s:if>
		    </tr>
		    
		  <s:iterator value="labourWelfareddList" status="row_status">
		     <tr>
				
				<td class="blueborderfortd"><div align="center"><s:textfield name="labourWelfareddList[%{#row_status.index}].lwddNumber" id="lwddNumber" cssClass="tablerow" maxlength="6" onblur="validateIsNan(this); validateDDNumberValue(this);"/></div></td>
				<td class="blueborderfortd"><div align="center" id="numbers"><s:textfield id="lwddAmount"  name="labourWelfareddList[%{#row_status.index}].lwddAmount"   cssClass="tablerow" maxlength="10" onblur="validateIsNan(this);calculateTotal(this)"/></div></td>
				<td class="blueborderfortd" ><div align="left"><s:select id="lwddbankName" name="labourWelfareddList[%{#row_status.index}].lwddbankName" list="dropdownData.bankList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose------"/></div></td>
				<td class="blueborderfortd"><div align="center"><s:textfield name="labourWelfareddList[%{#row_status.index}].lwddDate" id="lwddDate"  readonly="true" displayFormat="dd/mm/yy" showOn="focus" cssClass="tablerow" /></div></td>
				
				<s:if test="%{mode!='view'}">
				<td  class="blueborderfortd"><div align="center"><a href="#"><img src="${pageContext.request.contextPath}/images/addrow.gif" height="16"  width="16" border="2" alt="Add" onclick="addlabourRow()"></a></div></td>
				<td  class="blueborderfortd"><div align="center"><a href="#"><img src="${pageContext.request.contextPath}/images/removerow.gif" height="16"  width="16" border="2" alt="Delete" onclick="removelabourRow(this);" ></a></div></td>
				</s:if>	
				<s:hidden id="labourWelfareddListId" name="labourWelfareddList[%{#row_status.index}].id" />
			</tr>
		    </s:iterator>
		   
		    
		    
	  </table>	
	 <table>
	<td class="greybox"><div align="center"><s:text name="Totalforwelfare.txt" /></div></td>
	  <td class="greybox" ></td>	
			   <td class="greybox" ><div align="center"><s:property value="%{totalmwgwf}" /></div></td>	
    	  <td class="greybox" width="25%">&nbsp;</td>
			  <td class="greybox"><div align="center"><s:text name="Total" /></div></td>	
			   <td class="greybox" id=numbers><div align="center"><s:textfield id="mwgwfAmounttotal" name="mwgwfAmounttotal" value="" disabled="true"/></div></td>	
		  
		   
		    </div>
	</table>
	  </s:if>
	 
    </div>
	</div> 
	
	
	
	 <div id="cmdadetails" class="formmainbox">
	 <h1 class="subhead" ><s:text name="CMDA Details"/></h1>
	<div align="center" id="dd_div">
	
	<s:if test="%{cmdaddList.size!=0}">
		  
		  <table id="cmdaddlisttable" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		  
		  
		<tr>
		    <th  class="bluebgheadtd" ><div align="center">DD Number</div></th>
		    <th  class="bluebgheadtd" ><div align="center">DD Amount</div></th>
		    <th  class="bluebgheadtd" ><div align="center">DD Bank</div></th>
		    <th  class="bluebgheadtd" ><div align="center">DD Date</div></th>
		   <s:if test="%{mode!='view'}">
		   <th  class="bluebgheadtd" width="2%"><div align="center"> Add</div></th>
		   <th  class="bluebgheadtd" width="2%"><div align="center"> Delete</div></th>
			</s:if>
		    </tr>
		    
		  <s:iterator value="cmdaddList" status="row_status">
		     <tr>
				<td class="blueborderfortd"><div align="center"><s:textfield name="cmdaddList[%{#row_status.index}].msddNumber" id="msddNumber" cssClass="tablerow" maxlength="6" onblur="validateIsNan(this);validateDDNumberValue(this);"/></div></td>
				<td class="blueborderfortd"><div align="center" id="numbers"><s:textfield id="msddAmount" name="cmdaddList[%{#row_status.index}].msddAmount"  cssClass="tablerow"  maxlength="10" onblur="validateIsNan(this);calculateTotal(this)"/></div></td>
				<td class="blueborderfortd" ><div align="left" ><s:select id="msddbankName" name="cmdaddList[%{#row_status.index}].msddbankName" list="dropdownData.bankList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose------"/></div></td>
				<td class="blueborderfortd"><div align="center"><s:textfield name="cmdaddList[%{#row_status.index}].msddDate" id="msddDate"  readonly="true" displayFormat="dd/mm/yy" showOn="focus" cssClass="tablerow" /></div></td>
				<s:if test="%{mode!='view'}">
				<td  class="blueborderfortd"><div align="center"><a href="#"><img src="${pageContext.request.contextPath}/images/addrow.gif" height="16"  width="16" border="2" alt="Add" onclick="addcmdaRow()"></a></div></td>
				<td  class="blueborderfortd"><div align="center"><a href="#"><img src="${pageContext.request.contextPath}/images/removerow.gif" height="16"  width="16" border="2" alt="Delete" onclick="removecmdaRow(this);" ></a></div></td>
				</s:if>	
				<s:hidden id="cmdaddListId" name="cmdaddList[%{#row_status.index}].id" />
			</tr>
		    </s:iterator>
		    
	  </table>	
	 <table>
	 <td class="greybox" ><div align="center"><s:text name="Totalforcmda.txt" /></div></td>	
	 <td class="greybox" ></td>
			   <td class="greybox" ><div align="center"><s:property  value="%{totalcmda}" /></div></td>	
		 <td class="greybox" width="30%">&nbsp;</td>
	     <td class="greybox"><div align="center"><s:text name="Total" /></div></td>	
			   <td class="greybox" id=numbers><div align="center"><s:textfield id="cmdaAmounttotal" name="cmdaAmounttotal" value="" disabled="true"/></div></td>	
	</table>
	  </s:if>
	 
    </div>
	</div> 

<div class="buttonbottom" align="center">
		<table>
		<tr>
		 <s:if test="%{mode!='view'}">
		 
		  	<td><s:submit  cssClass="buttonsubmit" id="save" name="save" value="Save" method="captureDDdetails" onclick="return validation();"/></td>	
	  			</s:if>	     
	  			<s:if test="%{mode=='view'}">
		 
		  	<td><s:submit  cssClass="buttonsubmit" id="Print" name="Print" value="Print" method="printExternalFeeDetails" /></td>	
	  			</s:if>	     
	  		 <td><input type="button" name="close" id="close" class="button"  value="Close" onclick="window.close();"/>
	  		</td>
	  	</tr>
        </table>
   </div>	
  
</div>
	








</s:form>
</body>
</html>


 

