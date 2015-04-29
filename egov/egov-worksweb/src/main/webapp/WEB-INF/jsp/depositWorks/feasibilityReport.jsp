<script>
function viewInputFields(){
	    if(document.roadCutForm.depCode.checked==true){
	  		document.getElementById("depCodeTable").style.display='block';
	  		document.roadCutForm.depCode.checked=true;
	 		document.roadCutForm.depCode.value=true;
	    }else{  
	    	document.getElementById("depCodeTable").style.display='none';
	    	document.roadCutForm.depCode.checked=false;
	 		document.roadCutForm.depCode.value=false;
	 		dom.get("codeName").value='';
			dom.get("description").value='';
			dom.get("fundId").value='-1';
			dom.get("fundSourceId").value='-1';
	    }
}

function validateFeasibilityBeforeSubmit() {
	<s:if test="%{sourcepage=='genFeasibilityReport' || sourcepage=='inbox'}">
		if(dom.get("recommendation").value=='' || dom.get("recommendation").value==null){
			dom.get("roadcut_error").style.display='';
			document.getElementById("roadcut_error").innerHTML='<s:text name="dw.feasibilityReport.validate.recommendation"/>';
			dom.get("recommendation").focus();
			return false;
		}
		if(dom.get("estimatedCost").value=='' || dom.get("estimatedCost").value==null){
			dom.get("roadcut_error").style.display='';
			document.getElementById("roadcut_error").innerHTML='<s:text name="dw.feasibilityReport.validate.damageFee"/>';
			dom.get("estimatedCost").focus();
			return false;
		}
		if(dom.get('roadLastLaidDate').value=='')
		{
			dom.get("roadcut_error").style.display='';
			document.getElementById("roadcut_error").innerHTML='<s:text name="dw.feasibilityReport.validate.date" />';
			dom.get("roadLastLaidDate").focus(); 
			return false;
		}
		if(dom.get('roadLastLaidDate').value!='')
		{
			var currentDate='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
			if(compareDate(dom.get('roadLastLaidDate').value,currentDate) == -1 ){
				dom.get("roadcut_error").style.display='';
				document.getElementById("roadcut_error").innerHTML='<s:text name="dw.feasibilityReport.roadLastLaidDate.greaterthan.currenDate" />';
				dom.get("roadLastLaidDate").value='';
				dom.get("roadLastLaidDate").focus(); 
				return false;
			} 
		}
		if(!validateDamageFee(dom.get("estimatedCost"))) 
			return false;
		
		if(document.roadCutForm.depCode.checked==true){
			if(dom.get("codeName").value=='' || dom.get("codeName").value==null){
				dom.get("roadcut_error").style.display='';
				document.getElementById("roadcut_error").innerHTML='<s:text name="dw.feasibilityReport.validate.codeName"/>';
				dom.get("codeName").focus();
				return false;
			}

			if(dom.get("fundId").value=='-1'){
				dom.get("roadcut_error").style.display='';
				document.getElementById("roadcut_error").innerHTML='<s:text name="dw.feasibilityReport.validate.fund"/>';
				dom.get("fundId").focus();
				return false;
			}

			if(dom.get("financialYearId").value=='-1'){
				dom.get("roadcut_error").style.display='';
				document.getElementById("roadcut_error").innerHTML='<s:text name="dw.feasibilityReport.validate.financialYear"/>';
				dom.get("financialYearId").focus();
				return false;
			}

			if(dom.get("fundSourceId").value=='-1'){
				dom.get("roadcut_error").style.display='';
				document.getElementById("roadcut_error").innerHTML='<s:text name="dw.feasibilityReport.validate.fundSource"/>';
				dom.get("fundSourceId").focus();
				return false;
			}
		}
		
		dom.get("roadcut_error").style.display='none'; 
	  	dom.get("roadcut_error").innerHTML='';
		return true;
	</s:if>
	<s:else>
	 	dom.get("roadcut_error").style.display='none'; 
	  	dom.get("roadcut_error").innerHTML='';
		return true;
	</s:else>
}

function validateDamageFee(obj){
	var text = obj.value;
	if(isNaN(text))
	{
		dom.get("roadcut_error").style.display='';
		document.getElementById("roadcut_error").innerHTML='<s:text name="dw.feasibilityReport.invalid.damageFee"/>';
		obj.value="";
		obj.focus();
		return false;
	}
	else if(text<=0)
	{
		dom.get("roadcut_error").style.display='';
		document.getElementById("roadcut_error").innerHTML='<s:text name="dw.feasibilityReport.invalid.damageFee"/>';
		obj.value='';
		obj.focus();
		return false;
	}
	else{
		dom.get("roadcut_error").style.display='none'; 
	  	dom.get("roadcut_error").innerHTML='';
	  	return true;
	}
}
var docCommonNumber = null;
var docCommonId = null;
var clickButton = null;
function showWarning(obj){
	docCommonId=obj.id;
	<s:if test="applicationRequest.depositWorksCategory!=@org.egov.works.models.depositWorks.DepositWorksCategory@BPA">
		popup('popUpDiv');
	</s:if>
	<s:else>
		showUploadDoc();
	</s:else>
}

function showUploadDoc(){
	var docUploadNum;
	if(docCommonId == 'fbRepDocUploadButton'){
		clickButton = 'fbRepDocUploadButton';
		docCommonNumber = 'feasibilityRepDocNumber';
		docUploadNum = document.getElementById("feasibilityRepDocNumber").value;
		showDocumentManagerForFeasibilityReport(docUploadNum);return false;
	}
	else{
		docCommonNumber = 'docNumber';
		clickButton = 'docUploadButton';
		docUploadNum = document.getElementById("docNumber").value;
		showDocumentManagerForFeasibilityReport(docUploadNum);return false;
	}
}

var docNumberUpdater = function(docCommonNumber) {
	if(clickButton == 'fbRepDocUploadButton'){
		document.getElementById('feasibilityRepDocNumber').value = docCommonNumber;
	}
	else if(clickButton == 'docUploadButton'){
		document.getElementById("docNumber").value = docCommonNumber;
		}
};

function backToPage(){
	return false;
}

function showDocumentManagerForFeasibilityReport(docUploadNum)
{
    var v= docUploadNum;
    var url;
    if(v==null||v==''||v=='To be assigned')
    {
      url="/egi/docmgmt/basicDocumentManager.action?moduleName=Works";
    }
    else
    {
      url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="+v+"&moduleName=Works";
    }
    var wdth = 1000;
    var hght = 400;
    window.open(url,'docupload','width='+wdth+',height='+hght);
}

</script>

<div id="blanket" style="display:none;"></div>
<div id="popUpDiv" style="display:none;" ><s:text name="dw.feasibilityReport.uploadDoc.warning1"/><font size="2" color="Green"> estimate_claimcharges</font>, <s:text name="dw.feasibilityReport.uploadDoc.warning2"/>(<a href="#" onclick="popup('popUpDiv');showUploadDoc();">Yes</a>/<a href="#" onclick="popup('popUpDiv');backToPage();">No</a>)?</div>
<table width="100%" cellspacing="0" cellpadding="0" border="0" >
	<tr>
		<td colspan="4" class="headingwk">
			<div class="arrowiconwk">
				<img src="${pageContext.request.contextPath}/image/arrow.gif" /> 
			</div>
			<div class="headplacer"><s:text name="depositworks.feasibilityRep.details.title" /></div>
		</td>
	</tr>
	 <tr>
       <td class="whiteboxwk"><span class="mandatory">*</span><s:text name="dw.feasibilityRep.recommendation" />:</td>
       <td class="whitebox2wk" colspan="3"><s:textarea name="recommendation" cols="40" rows="4" cssClass="selectwk" id="recommendation" value="%{recommendation}"/></td>
     </tr>
     <tr>
       <td class="greyboxwk"><span class="mandatory">*</span><s:text name="dw.feasibilityRep.damageFee" />:</td>
       <td class="greybox2wk" colspan="3"><s:textfield name="estimatedCost" onblur="validateDamageFee(this)" cssClass="selectamountwk" id="estimatedCost" value="%{estimatedCost}" size="11" maxlength="11" align="right"/>&nbsp;&nbsp;&nbsp;&nbsp;<span class="mandatory">Amount in Rupees</span></td>
     </tr> 
     <tr>
     	<td class="whiteboxwk">
			<span class="mandatory">*</span><s:text name="dw.feasibilityReport.roadLaid.date">:</s:text>
		</td>
		<td class="whitebox2wk">
			<s:date name="roadLastLaidDate" var="fbDateFormat" format="dd/MM/yyyy"/>
         	<s:textfield name="roadLastLaidDate" value="%{fbDateFormat}" id="roadLastLaidDate" cssClass="selectboldwk" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" />
         	<a name="fbDatelnk" id="fbDatelnk" href="javascript:show_calendar('forms[0].roadLastLaidDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"><img name="fbDateimg" id="fbDateimg" src="${pageContext.request.contextPath}/image/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
		</td>
     </tr>
     <br>
</table>
<table id="depCodeCheck">
 <tr>
     	<td colspan="4" class="headingwk">
			<div class="headplacer"><s:text name="dw.feasibilityRep.depositCode.header" /></div>
		</td>
     </tr>	
     <tr>
	    <td width="11%" class="whiteboxwk"><s:text name="subledgerCode.type.depositCode" /></td>
		<td width="21%" class="whitebox2wk">
		   <input name="depCode" type="checkbox" id="depCode" onclick="viewInputFields();" />
		</td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" id="depCodeTable" style="display: none;">
		 <tr>
			<td class="greyboxwk"><span class="mandatory">*</span><s:text name="dw.feasibilityRep.depositCode.work.name" /></td>
	        <td class="greybox2wk"><s:textfield name="appReqDepositCode.codeName" type="text" cssClass="selectwk" id="codeName" value="%{depositCode.codeName}"/></td>
	
	        <td class="greyboxwk"><s:text name="dw.feasibilityRep.depositCode.work.description" /></td>
	        <td class="greybox2wk"><s:textarea name="appReqDepositCode.description" cols="35" cssClass="selectwk" id="description" value="%{depositCode.description}"/></td>
	     </tr> 
	     
	      <tr>
	        <td width="11%" class="whiteboxwk"><span class="mandatory">*</span><s:text name='dw.feasibilityRep.subledgerCode.fund'/> : </td>
	        <td width="21%" class="whitebox2wk" >
	            <s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="fundId" id="fundId" cssClass="selectwk" list="dropdownData.fundList" listKey="id" listValue="name" value="%{depositCode.fund.id}" />
	         </td>
	
       		<td class="whiteboxwk"><span class="mandatory">*</span><s:text name='dw.feasibilityRep.subledgerCode.financialYear'/> : </td>
       		<td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="financialYearId" id="financialYearId" cssClass="selectwk" list="dropdownData.financialYearList" listKey="id" listValue="finYearRange" value="%{currentFinancialYearId}" /></td>
      	</tr> 
      	
      	<tr> 
      	 	<td width="11%" class="greyboxwk" ><span class="mandatory">*</span><s:text name="dw.feasibilityRep.subledgerCode.fundSource.name" />:</td>
           	<td width="21%" class="greybox2wk">
               	<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="fundSourceId" id="fundSourceId" 
              			 cssClass="selectwk" list="dropdownData.fundSourceList" listKey="id" listValue="name" value="%{depositCode.fundSource.id}"/>
			</td>
			<td class="greyboxwk"></td> 
			<td class="greyboxwk"></td>
      	</tr>
</table>