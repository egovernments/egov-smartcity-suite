<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<style>
#warning {
  display:none;
  color:blue;
}
</style>

<script> 

var warnings=new Array();
warnings['natureOfWorkChanged']='<s:text name="estimate.header.warning.natureOfWorkChanged"/>'
warnings['dateChanged']='<s:text name="estimate.header.warning.dateChanged"/>'
warnings['improperWardSelection']='<s:text name="estimate.header.warning.improperWardSelection"/>'
warnings['improperDepositCodeSelection']='<s:text name="estimate.depositCode.warning.improperDepositCodeSelection"/>'

function warn(type){
    dom.get(type+"Warning").innerHTML=warnings[type]
    dom.get(type+"Warning").style.display='';
    YAHOO.lang.later(3000,null,function(){dom.get(type+"Warning").style.display='none';});
}

function setupPreparedByList(elem){
    deptId=elem.options[elem.selectedIndex].value;
    populatepreparedBy({executingDepartment:deptId});
    //getTemplateByDepName();
}
function clearDesignation(elem) {
    dom.get("designation").value='';
}
function setupSubTypes(elem){
    categoryId=elem.options[elem.selectedIndex].value;
    populatecategory({category:categoryId});
    dom.get('codeSearch').value='';
}

var wardSearchSelectionHandler = function(sType, arguments) { 
            var oData = arguments[2];
            dom.get("wardSearch").value=oData[0];
            dom.get("wardID").value = oData[1];
            getWorkflowAdditionalRule();
        }
        
var wardSelectionEnforceHandler = function(sType, arguments) {
    warn('improperWardSelection');
}

function validateHeaderBeforeSubmit(abstractEstimateForm) {
    var wardName = abstractEstimateForm.wardSearch.value;
    var workType=document.getElementById('parentCategory').value;
    var subType=document.getElementById('category').value;
    var currentState = document.getElementById('currentState').value;
    
    if (wardName.length == 0) {
        abstractEstimateForm.wardID.value = -1;
       }
   	 if(workType=='-1'){
   	    dom.get("worktypeerror").style.display='';
        dom.get("worktypeerror").innerHTML='<s:text name="estimate.worktype.null" />';
        return false;
	}
   	if(currentState=='' || currentState=='NEW' || currentState=='REJECTED')
   	{
   		if(subType=='-1'){
   		dom.get("worktypeerror").style.display='';
    	dom.get("worktypeerror").innerHTML='<s:text name="estimate.subworktype.null" />';
    	return false;
     	}
   	}
 	else{
         dom.get("worktypeerror").style.display='none';
        dom.get("worktypeerror").innerHTML='';
        return true;
        }
}

function checkSpillOverWorks(obj){ 
   if(obj.checked){
	 	document.abstractEstimateForm.isSpillOverWorks.value=true;
	 	document.abstractEstimateForm.isSpillOverWorks.checked=true;
	 	if(dom.get("emergencyWorks")!=null){
	 		dom.get("emergencyWorks").style.display='none';
	 	}
	 	if(dom.get("spillOverWorks")!=null){
	 		dom.get("spillOverWorks").style.display='block';
	 	}
	 	if(dom.get("manual_workflow")!=null){
	 		dom.get("manual_workflow").style.display='none';
	 	}
	}
	else if(!obj.checked){
	 	document.abstractEstimateForm.isSpillOverWorks.value=false;
	 	document.abstractEstimateForm.isSpillOverWorks.checked=false;
	 	if(dom.get("emergencyWorks")!=null){
	 		dom.get("emergencyWorks").style.display='block';
	 	}
	 	if(dom.get("spillOverWorks")!=null){
	 		dom.get("spillOverWorks").style.display='none';
	 	}
	 	if(dom.get("manual_workflow")!=null){
	 		dom.get("manual_workflow").style.display='block';
	 	}
	}
}

function trim(str) {
        return str.replace(/^\s+|\s+$/g,"");
}

var previousDate;

function dateChange(){

	var estimateDate=document.forms[0].estimateDate.value;
	var curDate='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
	var msg='<s:property value="%{allowFutureDate}"/>';

	if(msg=='NO' || msg=='no'){
		if(estimateDate!='' && (compareDate(formatDate6(estimateDate),formatDate6(curDate)) == -1) ){
	  	  dom.get("worktypeerror").style.display='block';
		  document.getElementById("worktypeerror").innerHTML='<s:text name="greaterthan.systemdate.estimatedate" />';
		  document.getElementById("estimateDate").value='';
		  document.getElementById("estimateDate").focus();
		  return false;
		}
		else{
				dom.get("worktypeerror").style.display='none';
			  	document.getElementById("worktypeerror").innerHTML='';
			}
	}	
	
	var id=document.forms[0].id.value;

	var hiddenEstimateDate=document.forms[0].hiddenEstimateDate.value;
	if(hiddenEstimateDate!= ''){
		previousDate=hiddenEstimateDate;
	}
	else{
		previousDate=estimateDate;
	}

	if(estimateDate!=''){
		javascript:warn('dateChanged');
    		resetOverheads();
	}
	
	if(trim(estimateDate)!='' && hiddenEstimateDate!= '' && hiddenEstimateDate!=estimateDate){
		popup('popUpDiv');		
	}
	else if(id!='' && id!=undefined && estimateDate!='' && hiddenEstimateDate!=estimateDate){
		popup('popUpDiv');
	}
	document.forms[0].hiddenEstimateDate.value=document.forms[0].estimateDate.value;
}

function resetDetails(){    
	resetSorTable();
	resetNonSorTable();
    var elem = document.getElementById('type');
    resetOverheads();
    // resetOverheads('0');
}


function resetPreviousDate(){
	document.forms[0].hiddenEstimateDate.value=previousDate;
	document.forms[0].estimateDate.value=previousDate;
}

function setExpenditureType(){
	 document.getElementById('expenditureType').value = document.getElementById('type').options[document.getElementById('type').selectedIndex].value;
}

function checkEmergencyWorks(obj){ 
   if(obj.checked){
	 document.abstractEstimateForm.isEmergencyWorks.value=true;
	}
	else if(!obj.checked){
	 document.abstractEstimateForm.isEmergencyWorks.value=false;
	}
}

function checkRateContract(obj){ 
	var msg='<s:text name="estimate.rcDetails.reset"/>';
	var ans=confirm(msg+" ?");
	if(ans) {
		 if(obj.checked){
		 	 resetRCTable();
		 	 resetSorTable();
			 resetNonSorTable();
			 contractorsDataTable.addRow({SlNo:contractorsDataTable.getRecordSet().getLength()+1});
			 rateContractDataTable.addRow({SlNo:rateContractDataTable.getRecordSet().getLength()+1});
			 
			 document.abstractEstimateForm.isRateContract.value=true;
			 document.getElementById("rctypeHeader").style.display="block";
			 document.getElementById("rctypeDropDown").style.display="block";
			 document.getElementById('estimateRCtab').style.display="block";
			 document.getElementById('baseSORTable').style.display='none';
			 document.getElementById('sorTableforRC').style.display="block";
			 document.getElementById('nonSorTableforRC').style.display="block"; 
			 document.getElementById('addnonSorButtn').style.display="none";  
			 
			 sorDataTable.showColumn("Contractor");
		     sorDataTable.showColumn("RCNumber");
			 
			 nonSorDataTable.showColumn("Contractor");
		     nonSorDataTable.showColumn("RCNumber");
		     		     
			 document.abstractEstimateForm.codeSearch.disabled=true;
			 document.getElementById('codeSubmitButton').disabled=true;
			 document.getElementById('searchTemplateButton').disabled=true;
			 nonSorDataTable.showTableMessage ('<s:text name="estimate.ratecontract.nonsor.initial.table.message"/>');
		}
		else if(!obj.checked){
				resetRCTable();
				resetSorTable();
				resetNonSorTable();
				document.getElementById("rcType").value="-1";
				document.getElementById("rctypeHeader").style.display="none";
			 	document.getElementById("rctypeDropDown").style.display="none";
			 	document.abstractEstimateForm.isRateContract.value=false;
			  	document.getElementById('baseSORTable').style.display="block"; 
			  	document.getElementById('estimateRCtab').style.display="none";
			  	document.getElementById('sorTableforRC').style.display="none";
			  	document.getElementById('nonSorTableforRC').style.display="none"; 
			  	document.getElementById('addnonSorButtn').style.display="block";
			  	
		 	  	sorDataTable.hideColumn("Contractor");
			    sorDataTable.hideColumn("RCNumber");
			    sorDataTable.hideColumn("rcId");
			  	
			  	nonSorDataTable.hideColumn("Contractor");
			    nonSorDataTable.hideColumn("RCNumber");
			    nonSorDataTable.hideColumn("rcId");
			    
			    document.abstractEstimateForm.codeSearch.disabled=false;
			 	document.getElementById('codeSubmitButton').disabled=false;
				document.getElementById('searchTemplateButton').disabled=false;
				nonSorDataTable.showTableMessage ('<s:text name="estimate.nonsor.initial.table.message"/>');
		}
		return true;
	}
	else{
	 	if(obj.checked){
	 	 	document.abstractEstimateForm.isRateContract.value=false;
	 	 	document.abstractEstimateForm.isRateContract.checked=false;
	 	}
	 	else if(!obj.checked){
	 		document.abstractEstimateForm.isRateContract.value=true;
	 	 	document.abstractEstimateForm.isRateContract.checked=true;
	 	}
		return false;		
	}
} 

function getInit() 
{
	dom.get("worktypeerror").style.display='none';
	document.getElementById("worktypeerror").innerHTML=""; 
	if(dom.get("rcType").value != -1 && dom.get("rcType").value=='Amount'){ 
		checkRCType();
	}
}

function addGisMarker()  {  
   var latvalue = document.abstractEstimateForm.lat.value;
   var lonvalue = document.abstractEstimateForm.lon.value;   
   if(latvalue == '' && lonvalue == '') { 
	   <s:if test="%{model.id==null || model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED'}"> 
		   var target="/mapguide/nmc/ajaxtiledviewersample.jsp?DomainName=nmc&mode=ADDGISMARKER";  
		   popupwin =    window.open(target,"AddGisFeature",'height=600,width=850,left=50,top=100,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no');
		   popupwin.focus();
		</s:if>
		<s:else>
			alert('<s:text name="estimate.gismap.notfound"/>');
		</s:else>
   }
   else {
   		var latlonString =latvalue+'^'+lonvalue+'@';
   		var target="/mapguide/nmc/ajaxtiledviewersample.jsp?DomainName=nmc&mode=VIEWGISMARKER&resultString="+latlonString;  
 	   	popupwin =    window.open(target,"ViewGisFeature",'height=600,width=850,left=50,top=100,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no');
 	  	popupwin.focus();
   }
}

</script>

<div id="blanket" style="display:none;"></div>
<div id="popUpDiv" style="display:none;" ><s:text name="estimate.dateChange.warning"/>(<a href="#" onclick="popup('popUpDiv');resetDetails();">Yes</a>/<a href="#" onclick="popup('popUpDiv');resetPreviousDate();">No</a>)?</div>

<div id="popLoadingDiv" style="display:none;font-size: 12px;font-weight: bold;color: #cc0000;position:absolute;
	width:350px;height:150px;z-index: 9002;text-align: center;" >
	
	<span>
		<p>Loading Enclosure.....</p>
		<img src="<egov:url path='/images/loading.gif'/>" alt="Help" width="50" height="50" border="0" />
	</span>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
                <div class="headplacer"><s:text name="estimate.header" />:</div></td>
                </tr>
              <tr>
                <td width="11%" class="whiteboxwk"><s:text name="estimate.user.department" />:</td>
                <td width="21%" class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="userDepartment" id="userDepartment" cssClass="selectwk" list="dropdownData.userDepartmentList" listKey="id" listValue="deptName" value="%{userDepartment.id}"/>

                <td width="15%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="estimate.executing.department" />:</td>
                <td width="53%" class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="executingDepartment" id="executingDepartment" cssClass="selectwk" list="dropdownData.executingDepartmentList" listKey="id" listValue="deptName" value="%{executingDepartment.id}" onChange="setupPreparedByList(this);clearDesignation(this);getWorkflowAdditionalRule();"/>
                <egov:ajaxdropdown id="preparedBy" fields="['Text','Value','Designation']" dropdownId='preparedBy' optionAttributes='Designation' url='estimate/ajaxEstimate!usersInExecutingDepartment.action'/>
                </td>
              </tr>
              <tr>
                <td width="11%" class="greyboxwk"><span class="mandatory">*</span><s:text name="estimate.ward" />:</td>
                <td width="21%" class="greybox2wk">
                <div class="yui-skin-sam">
                <div id="wardSearch_autocomplete">
                <div><s:textfield id="wardSearch" type="text" name="wardName" value="%{ward.parent?(ward.parent?ward.name+'('+ward.parent.name+')':''):(ward.name?ward.name:'')}" class="selectwk"/><s:hidden id="wardID" name="ward" value="%{ward.id}"/></div>
                <span id="wardSearchResults"></span>
                </div>
                </div>
                <egov:autocomplete name="wardSearch" width="20" field="wardSearch" url="wardSearch!searchAjax.action?" queryQuestionMark="false" results="wardSearchResults" handler="wardSearchSelectionHandler" forceSelectionHandler="wardSelectionEnforceHandler"/>
                <span class='warning' id="improperWardSelectionWarning"></span>
                </td> 
                <td class="greyboxwk"><span class="mandatory">*</span><s:text name="estimate.date" />:</td>

                <td class="greybox2wk"><s:date name="estimateDate" var="estDateFormat" format="dd/MM/yyyy"/><s:textfield name="estimateDate" value="%{estDateFormat}" id="estimateDate" cssClass="selectwk" onBlur="dateChange()" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
                    <a href="javascript:show_calendar('forms[0].estimateDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"><img src="${pageContext.request.contextPath}/image/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
                    <input type='hidden' id='hiddenEstimateDate' name='hiddenEstimateDate'/>
		<span class='warning' id="dateChangedWarning"></span></td>
                </td>
              </tr>
              <tr> 
                <td class="whiteboxwk"><s:text name="estimate.location" />:</td>
                <td class="whitebox2wk"><s:textfield name="location" value="%{location}" id="location" cssClass="selectwk" />
                <a href="javascript:void(0);" id="gisImage" name="ADDGISFEATURE" title="Click here to add/view gis marker on map" onClick= "addGisMarker();" >
                <img height="18" width="18" align="absmiddle" src="${pageContext.request.contextPath}/image/map_icon_small.jpg" /></a>
              	 <s:hidden id="lat" name="lat"/> 
                 <s:hidden id="lon" name="lon"/>
                </td>
                <td width="15%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="estimate.work.nature" /></td>
                <td width="53%" class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="type" id="type" cssClass="selectwk" list="dropdownData.typeList" listKey="id" listValue="name" value="%{type.id}" 
                onChange="javascript:warn('natureOfWorkChanged');setExpenditureType();resetAssets(this.options[this.selectedIndex].value);toggleBudgetDetails();checkRCType();getWorkflowAdditionalRule();"/>
                <s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" id="expenditureType" cssClass="selectwk" list="dropdownData.typeList" listKey="id" listValue="expenditureType.value" value="%{type.id}" style="display:none;"/>
                <span class='warning' id="natureOfWorkChangedWarning"></span></td>
              </tr>
              <tr>
                <td class="greyboxwk"><span class="mandatory">*</span><s:text name="estimate.work.name" />:</td>
                <td class="greybox2wk"><s:textarea name="name" cols="35" cssClass="selectwk" id="name" value="%{name}"/></td>
                <td class="greyboxwk"><span class="mandatory">*</span><s:text name="estimate.work.description" />:</td>
                <td class="greybox2wk"><s:textarea name="description" cols="35" cssClass="selectwk" id="description" value="%{description}"/></td>
              </tr>
             
              <tr>
                <td class="whiteboxwk"><span class="mandatory">*</span><s:text name="estimate.necessity.header" />:</td>
                <td class="whitebox2wk"><s:textarea name="necessity" cols="35" cssClass="selectwk" id="necessity" value="%{necessity}"/></td>
                <td class="whiteboxwk"><span class="mandatory">*</span><s:text name="estimate.scopeOfWork.header" />:</td>
                <td class="whitebox2wk"><s:textarea name="scopeOfWork" cols="35" cssClass="selectwk" id="scopeOfWork" value="%{scopeOfWork}"/></td>
              </tr>
             
              <tr>
                <td width="11%" class="greyboxwk"><span class="mandatory">*</span><s:text name="estimate.fund.name" />:</td>
                <td width="21%" class="greybox2wk">
                <s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="fundSource" id="fundSource" 
                cssClass="selectwk" list="dropdownData.fundSourceList" listKey="id" listValue="name" value="%{fundSource.id}" onchange="setFinancingSource(this)"/>
                </td>
				
			    <td width="15%" class="greybox2wk">&nbsp;</td>
                <td width="53%" class="greybox2wk">&nbsp;</td>
              </tr>
              
             
              <tr>
              <td colspan="2">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
               	<tr>
	                	<td class="whiteboxwk"><s:checkbox name="isEmergencyWorks" id="isEmergencyWorks"  value="%{isEmergencyWorks}"  onclick="checkEmergencyWorks(this)" /></td> 
	              		<td class="whitebox2wk"><s:text name="estimate.emergency.work"/></td>
				  		<td class="whiteboxwk" ><s:checkbox name="isSpillOverWorks" id="isSpillOverWorks" value="%{isSpillOverWorks}" onclick="checkSpillOverWorks(this)"/></td>
				  		<td class="whitebox2wk"><s:text name="estimate.spillOver.work"/></td>
              	</tr>
              </table>
				</td>
	              
	              
	              <td colspan="2">
	              <table width="100%" border="0" cellspacing="0" cellpadding="0">
	               	<tr>
	                	<td class="whiteboxwk" width="25%"><s:text name="estimate.RateContract"/></td>
	             		<td class="whitebox2wk" width="25%"><s:checkbox name="isRateContract" id="isRateContract"  value="%{isRateContract}"  onclick="checkRateContract(this)" /></td> 
	              		<td class="whiteboxwk" id="rctypeHeader" style="display: none;"  width="25%"><span class="mandatory">*</span><s:text name="rateContract.type" />:</td>
            	  		<td class="whitebox2wk" id="rctypeDropDown" style="display: none;" width="25%"><s:select headerKey="-1" headerValue="%{getText('rateContract.default.select')}" name="rcType" id="rcType" value="%{rcType}" cssClass="selectwk" list="#{'Amount':'Amount', 'Item':'Item'}" onChange="getInit();"/></td>		
	              	</tr>
	              </table> 
	              </td>
	             
	             <!-- 
              <td class="greybox">&nbsp;</td>
              <td class="greybox">&nbsp;</td>
	              -->
              </tr>
              
              
              <tr>
                <td  colspan="4" class="shadowwk"> </td>               
                </tr>
                <tr><td>&nbsp;</td></tr>			
            </table>   
      <script>
    	if(document.forms[0].estimateDate.value!=''){
    		document.forms[0].hiddenEstimateDate.value=document.forms[0].estimateDate.value;
    	}
    	else{
    		document.forms[0].hiddenEstimateDate.value=getCurrentDate();
    	}
    </script>                 
            
                     