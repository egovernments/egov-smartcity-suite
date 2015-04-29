
<script>
 function validateResolutionData(){	 
 	if($F("resolutionNumber").blank()){
		$('resolutiondata_error').show();
		$("resolutiondata_error").innerHTML='<s:text name="estimate.resolutionnumber.null" />';
		return false;
	}
	
	if($F("resolutionDate").blank()){
		$('resolutiondata_error').show();
		$("resolutiondata_error").innerHTML='<s:text name="estimate.date.null" />';
		return false;
	}

	if(!validateDateFormat($("resolutionDate"))) {
    	$('resolutiondata_error').show();
		$("resolutiondata_error").innerHTML='<s:text name="estimate.resolution.invaliddate" />';
    	return false;
	}
	return true;
 }

function enableResolutionFields(){
	document.getElementById('resolution_details').style.display ='';
	document.getElementById("resolutionNumber").disabled=false;
	document.getElementById("resolutionNumber").readonly=false;
 	document.getElementById("resolutionDate").disabled=false;
 	document.getElementById("resolutionDate").readonly=false;
 	document.getElementById("resolutionDate").readonly=false;
	document.getElementById("resolutionDatelnk").onclick=function(){ return true;}
}
 
 
</script>
<div class="errorstyle" id="resolutiondata_error" style="display:none;"></div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr><td>&nbsp;</td></tr>	
   <!--  <tr>
       <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
       <div class="headplacer"><s:text name="estimate.resolution" />:</div></td>
     </tr> -->
     <tr>
       <td width="11%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="estimate.resolution.resolutonnumber" />:</td>
       <td width="21%" class="whitebox2wk">
        <s:textfield name="resolutionNumber"  id="resolutionNumber" cssClass="selectboldwk" />
       </td>
       <td width="15%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="estimate.resolution.resolutondate" />:</td>
       <td width="53%" class="whitebox2wk">
        <s:date name="resolutionDate" var="resolutionDateFormat" format="dd/MM/yyyy" />
		<s:textfield name="resolutionDate" value="%{resolutionDateFormat}" id="resolutionDate" cssClass="selectwk" onfocus="javascript:vDateType='3';"
		onkeyup="DateFormat(this,this.value,event,false,'3')" maxlength="10"/>
		<a id="resolutionDatelnk" name="resolutionDatelnk" href="javascript:show_calendar('forms[0].resolutionDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" 	onmouseout="window.status='';return true;"><img src="${pageContext.request.contextPath}/image/calendar.png"
			alt="Calendar" width="16" height="16" border="0" align="absmiddle" />
		</a>
		<span id='errorresolutionDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>

       </td>
     </tr>           
     <tr>
       <td  colspan="4" class="shadowwk"> </td>               
     </tr>
     <tr><td>&nbsp;</td></tr>		
   
</table>            


