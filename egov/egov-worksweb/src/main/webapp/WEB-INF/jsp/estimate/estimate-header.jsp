<!-- -------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency,
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It
# 	   is required that all modified versions of this material be marked in
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program
# 	   with regards to rights under trademark law for use of the trade names
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
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

function warn(type){
    dom.get(type+"Warning").innerHTML=warnings[type]
    dom.get(type+"Warning").style.display='';
    YAHOO.lang.later(3000,null,function(){dom.get(type+"Warning").style.display='none';});
}

function setupSubTypes(elem){
    categoryId=elem.options[elem.selectedIndex].value;
    populatecategory({category:categoryId});
}

var wardSearchSelectionHandler = function(sType, arguments) { 
            var oData = arguments[2];
            dom.get("wardSearch").value=oData[0];
            dom.get("wardID").value = oData[1];
        }
        
var wardSelectionEnforceHandler = function(sType, arguments) {
    warn('improperWardSelection');
}


function validateCharacters(obj,msg){
	var string=obj.value;
	string=string.replace(/(\r\n|\r|\n){3,}/g,"\n\n\n");
	string=string.replace(/(\t)/g," ");
	string=string.replace(/([ ]){3,}/g,"  ");
	obj.value=string;
	if(string.search(/http:/i)>-1){
		alert(msg);
		return false;
	}
	else{
		return true;
	}
}

function validateHeaderBeforeSubmit(abstractEstimateForm) {
	var nameObj=document.getElementById('name');
	var descriptionObj=document.getElementById('description');
	if(!validateCharacters(nameObj,'<s:text name="estimate.name.error" />') || !validateCharacters(descriptionObj,'<s:text name="estimate.description.error" />')){
		return false;
	}
    var wardName = abstractEstimateForm.wardSearch.value;
    var workType=document.getElementById('parentCategory').value;
      
      if (wardName.length == 0) {
        abstractEstimateForm.wardID.value = -1;
       }
      if(dom.get('userDepartment').value==-1)
      {
    	  dom.get("worktypeerror").style.display='';
          dom.get("worktypeerror").innerHTML='<s:text name="estimate.userDept.null" />';
          window.scroll(0,0);
          return false;   
      }
      else
      {
          if(dom.get("worktypeerror").innerHTML=='<s:text name="estimate.userDept.null" />')
          {
        	  dom.get("worktypeerror").style.display='none';
              dom.get("worktypeerror").innerHTML='';    
          }    
      }
      if(document.getElementById('wardSearch').value=='')
      {
    	  dom.get("worktypeerror").style.display='';
          dom.get("worktypeerror").innerHTML='<s:text name="estimate.ward.null" />';
          window.scroll(0,0);
          return false;   
      }
      else
      {
          if(dom.get("worktypeerror").innerHTML=='<s:text name="estimate.ward.null" />')
          {
        	  dom.get("worktypeerror").style.display='none';
              dom.get("worktypeerror").innerHTML='';    
          }    
      }
      if(dom.get('type').value==-1)
      {
    	  dom.get("worktypeerror").style.display='';
          dom.get("worktypeerror").innerHTML='<s:text name="estimate.natureofwork.null" />';
          window.scroll(0,0);
          return false;   
      }
      else
      {
          if(dom.get("worktypeerror").innerHTML=='<s:text name="estimate.natureofwork.null" />')
          {
        	  dom.get("worktypeerror").style.display='none';
              dom.get("worktypeerror").innerHTML='';    
          }    
      }
      if(document.getElementById('name').value=='')
      {
    	  dom.get("worktypeerror").style.display='';
          dom.get("worktypeerror").innerHTML='<s:text name="estimate.name.null" />';
          window.scroll(0,0);
          return false;   
      }
      else
      {
          if(dom.get("worktypeerror").innerHTML=='<s:text name="estimate.name.null" />')
          {
        	  dom.get("worktypeerror").style.display='none';
              dom.get("worktypeerror").innerHTML='';    
          }    
      }
      if(document.getElementById('description').value=='')
      {
    	  dom.get("worktypeerror").style.display='';
          dom.get("worktypeerror").innerHTML='<s:text name="estimate.desc.null" />';
          window.scroll(0,0);
          return false;   
      }
      else
      {
          if(dom.get("worktypeerror").innerHTML=='<s:text name="estimate.desc.null" />')
          {
        	  dom.get("worktypeerror").style.display='none';
              dom.get("worktypeerror").innerHTML='';    
          }    
      }
      if(dom.get('fundSource').value==-1)
      {
    	  dom.get("worktypeerror").style.display='';
          dom.get("worktypeerror").innerHTML='<s:text name="estimate.fund.null" />';
          window.scroll(0,0);
          return false;   
      }
      else
      {
          if(dom.get("worktypeerror").innerHTML=='<s:text name="estimate.fund.null" />')
          {
        	  dom.get("worktypeerror").style.display='none';
              dom.get("worktypeerror").innerHTML='';    
          }    
      }
      
   	 if(workType=='-1'){
       
   	    dom.get("worktypeerror").style.display='';
        dom.get("worktypeerror").innerHTML='<s:text name="estimate.worktype.null" />';
        window.scroll(0,0);
        return false;
	}

   	if(dom.get('preparedBy').value==-1 || dom.get('preparedBy').value==0 || dom.get('preparedBy').value== '') {
   	  	 dom.get("worktypeerror").style.display='';
         dom.get("worktypeerror").innerHTML='<s:text name="estimate.preparedBy.null" />';
         window.scroll(0,0);
         return false;   
    }
   	else{
        dom.get("worktypeerror").style.display='none';
        dom.get("worktypeerror").innerHTML='';
        return true;
    }
}

function trim(str) {
        return str.replace(/^\s+|\s+$/g,"");
}

var previousDate;

function dateChange(){
	var estimateDate=document.forms[0].estimateDate.value;
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
function clearMsg(obj)
{
	if(obj.value==dom.get("executingDepartment").value && '<s:property value="%{errorCode}" />'=='estimate.depositworks.dept.check')
	{
		dom.get('errorstyle').style.display='none';
	}	
}
function openMap()
{
	var params = [
	              'height='+screen.height,
	              'width='+screen.width,
	              'fullscreen=yes' 
	          ].join(',');
	var popup ;
	var  lat = document.getElementById("latitude").value ;
	var lon = document.getElementById("longitude").value ;
	var status = '<s:property value="%{egwStatus.code}" />';
	if(status==null || status=='' || status =='NEW' || status=='REJECTED')
	{
		if(lat!='' && lon!='')
		{
			popup = window.open('${pageContext.request.contextPath}/estimate/abstractEstimate-maps.action?mapMode=edit&latitude='+lat+'&longitude='+lon,'popup_window', params);
		}
		else
		{
			popup = window.open('${pageContext.request.contextPath}/estimate/abstractEstimate-maps.action?mapMode=edit','popup_window', params);	
		}
	}	
	else
	{
		if(lat!='' && lon!='')
		{
			popup = window.open('${pageContext.request.contextPath}/estimate/abstractEstimate-maps.action?mapMode=view&latitude='+lat+'&longitude='+lon,'popup_window', params);
		}
		else
			return;
	}	
	popup.moveTo(0,0);
}
function updateLocation(values)
{
	if(values!=null && values.length>0)
	{
		document.getElementById("latitude").value=values[0];
		document.getElementById("longitude").value=values[1];
		document.getElementById("location").value=values[2];
		document.getElementById("latlonDiv").style.display="";
	}
	if(values==null)
	{
		document.getElementById("latitude").value="";
		document.getElementById("longitude").value="";
		document.getElementById("location").value="";
		document.getElementById("latlonDiv").style.display="none";	
	}			
}

function jurisdictionSearchParameters(){
	return "isBoundaryHistory=false";
}
</script>


<div id="blanket" style="display:none;"></div>
<div id="popUpDiv" style="display:none;" ><s:text name="estimate.dateChange.warning"/>(<a href="#" onclick="popup('popUpDiv');resetDetails();">Yes</a>/<a href="#" onclick="popup('popUpDiv');resetPreviousDate();">No</a>)?</div>

<div id="popLoadingDiv" style="display:none;font-size: 12px;font-weight: bold;color: #cc0000;position:absolute;
	width:350px;height:150px;z-index: 9002;text-align: center;" >
	
	<span>
		<p>Loading Enclosure.....</p>
		<img src="/egi/resources/erp2/images/loading.gif" alt="Help" width="50" height="50" border="0" />
	</span>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="/egi/resources/erp2/images/arrow.gif" /></div>
                <div class="headplacer"><s:text name="estimate.header" />:</div></td>
                </tr>
              <tr>
                <td width="11%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="estimate.user.department" />:</td>
                <td width="21%" class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="userDepartment" id="userDepartment" cssClass="selectwk" list="dropdownData.userDepartmentList" onchange="clearMsg(this);" listKey="id" listValue="name" value="%{userDepartment.id}"/>

                <td width="15%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="estimate.executing.department" />:</td>
                <td width="53%" class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="executingDepartment" id="executingDepartment" cssClass="selectwk" list="dropdownData.executingDepartmentList" listKey="id" listValue="name" value="%{executingDepartment.id}" />
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
                <egov:autocomplete name="wardSearch" width="20" field="wardSearch" url="wardSearch-searchAjax.action?" queryQuestionMark="false" results="wardSearchResults" handler="wardSearchSelectionHandler" forceSelectionHandler="wardSelectionEnforceHandler" paramsFunction="jurisdictionSearchParameters"/>
                <span class='warning' id="improperWardSelectionWarning"></span>
                </td> 
                <td class="greyboxwk"><span class="mandatory">*</span><s:text name="estimate.date" />:</td>

                <td class="greybox2wk"><s:date name="estimateDate" var="estDateFormat" format="dd/MM/yyyy"/><s:textfield name="estimateDate" value="%{estDateFormat}" id="estimateDate" cssClass="selectwk" onBlur="dateChange()" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
                    <a id="estDatePicker" href="javascript:show_calendar('forms[0].estimateDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"><img src="/egi/resources/erp2/images/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
                    <input type='hidden' id='hiddenEstimateDate' name='hiddenEstimateDate'/>
					<span class='warning' id="dateChangedWarning"></span></td>
                </td>
              </tr>
              <tr>
                <td class="whiteboxwk"><s:text name="estimate.location" />:</td>
                <td class="whitebox2wk"><s:textfield name="location" value="%{location}" id="location" size="35" cssClass="selectwk" /><a href="javascript:openMap();" id="mapAnchor" title="Click here to add/view gis marker on map"><img height="18" width="18" align="absmiddle" src="/egi/resources/erp2/images/map_icon_small.jpg" /></a></td>
                <td width="15%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="estimate.work.nature" /></td>
                <td width="53%" class="whitebox2wk">
                <s:if test="%{(dropdownData.typeList.size==1)}" >
	                <s:select name="type" id="type" cssClass="selectwk" list="dropdownData.typeList" listKey="id" listValue="name" value="%{type.id}" />
	                <s:select id="expenditureType" cssClass="selectwk" list="dropdownData.typeList" listKey="id" listValue="expenditureType.value" value="%{type.id}" style="display:none;"/>
                </s:if>
                <s:else>
	                <s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="type" id="type" cssClass="selectwk" list="dropdownData.typeList" listKey="id" listValue="name" value="%{type.id}" 
	                	onChange="javascript:warn('natureOfWorkChanged');setExpenditureType();resetAssets(this.options[this.selectedIndex].value);"/>
	                <s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" id="expenditureType" cssClass="selectwk" list="dropdownData.typeList" listKey="id" listValue="expenditureType.value" value="%{type.id}" style="display:none;"/>
                </s:else>
                <span class='warning' id="natureOfWorkChangedWarning"></span></td>
              </tr>
              <tr id="latlonDiv" style="display:none;">
	                <td width="11%" class="whiteboxwk"><s:text name="estimate.latitude" />:</td>
	                <td width="21%" class="whitebox2wk"><s:textfield name="latitude" value="%{latitude}" id="latitude" readonly='true' cssClass="selectwk" /></td>
	                <td width="15%" class="whiteboxwk"><s:text name="estimate.longitude" />:</td>
	                <td width="53%" class="whitebox2wk"><s:textfield name="longitude" value="%{longitude}" id="longitude" readonly='true' cssClass="selectwk" /></td>
              </tr>
              <tr>
                <td class="greyboxwk"><span class="mandatory">*</span><s:text name="estimate.work.name" />:</td>
                <td class="greybox2wk" colspan="3"><s:textarea name="name" cols="100" rows="4" cssClass="selectwk" id="name" value="%{name}"/></td>
              </tr>
              <tr>
                <td class="whiteboxwk"><span class="mandatory">*</span><s:text name="estimate.work.description" />:</td>
                <td class="whitebox2wk" colspan="3"><s:textarea name="description" cols="100" rows="4" cssClass="selectwk" id="description" value="%{description}"/></td>
              </tr>

              <tr>
                <td width="11%" class="greyboxwk"><span class="mandatory">*</span><s:text name="estimate.fund.name" />:</td>
                <td width="21%" class="greybox2wk">
	                <s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="fundSource" id="fundSource" 
	                cssClass="selectwk" list="dropdownData.fundSourceList" listKey="id" listValue="name" value="%{fundSource.id}"/>
				</td>
				
                <td width="15%" class="greyboxwk">&nbsp;</td>
                <td width="53%" class="greybox2wk">&nbsp;</td>
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
