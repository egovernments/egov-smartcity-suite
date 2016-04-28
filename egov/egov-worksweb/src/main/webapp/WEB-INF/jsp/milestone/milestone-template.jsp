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
<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script> 
<script>

function setupSubTypes(elem){
    categoryId=elem.options[elem.selectedIndex].value;
    populatecategory({category:categoryId});
}

function checkTypeOfWork(){
	clearMessage('milestone_activity_error');
	if(dom.get("parentCategory").value== -1){
        dom.get("milestone_activity_error").style.display='';
    	document.getElementById("milestone_activity_error").innerHTML='<s:text name="milestone.worktype.null" />';
    	return false;
	}	
	return true;
}

function codeSearchParameters(){
	var params;   
	if(dom.get('parentCategory').value !='-1'){
		params= "&workTypeId="+dom.get('parentCategory').value;
    }
    if(dom.get('parentCategory').value !='-1' && dom.get('category').value !='-1'){
    	params+="&";
    }
    if(dom.get('category').value !='-1'){
		params+= "subTypeId="+dom.get('category').value;
    }
    return params;
}

var codeSearchSelectionHandler = function(sType, arguments) { 
            var oData = arguments[2];
            
        }
        
var codeSelectionEnforceHandler = function(sType, arguments) {
    warn('improperCodeSelection');
}

function showCodeProcessImage(event) {
	if(!checkForCode())
		return false;

	var unicode=event.keyCode? event.keyCode : event.charCode;
	if((unicode==46 || unicode==8) && dom.get("codeSearch").value.length==1){
	   document.getElementById("loadImageForCode").style.display='none';
	}
	else if(unicode!=9 && unicode!=37 && unicode!=38 && unicode!=39 && unicode!=40){
	   document.getElementById("loadImageForCode").style.display='';
	}

	return true;
}

function checkForCode(){
	if(dom.get("codeSearch").value==''){
		dom.get("milestone_activity_error").style.display='';
		document.getElementById("milestone_activity_error").innerHTML='Please select the template.';
		return false;
	}
	return true;
}
function afterCodeResults(sType,results){
    clearMessage('milestone_activity_error');
    document.getElementById("loadImageForCode").style.display='none';
    if(results[2].length==0 && checkTypeOfWork()) {
        dom.get("milestone_activity_error").style.display='';
    	document.getElementById("milestone_activity_error").innerHTML+='No Templates with the given code';
    }
}


function getTemplateForCode(){
var code=dom.get("codeSearch").value;
resetTemplate(code,'','');
}

function resetTemplate(code,workType,subType){
	if(workType!=""){
		dom.get('parentCategory').value=workType;
		setupSubTypes(dom.get('parentCategory'));
		loadSubType= function(req, res){
				if(dom.get('category').value != ""){
					dom.get('category').value=subType;
				}
		}
		if(subType!=""){
			dom.get('category').value=subType;
		}
	}
	if(code!=""){
		dom.get("codeSearch").value=code;
	}
	if(code!="" && (temptActvDataTable.getRecordSet().getLength()>0)){ 
		var ans=confirm('<s:text name="milestone.template.confirm.reset"/>');	
		if(ans) {
			clearActivities();
			getActivitiesForTemplate(code);
		}
		else {
			return false;		
		}
	}
	else{
		getActivitiesForTemplate(code);
	}
}

function clearActivities(){
	temptActvDataTable.deleteRows(0,temptActvDataTable.getRecordSet().getLength());
}

function getActivitiesForTemplate(code){
 var myCodeSuccessHandler = function(req,res) {
                document.getElementById("milestone_activity_error").style.display='none';
                document.getElementById("milestone_activity_error").innerHTML='';
               
                var allresults=res.results;
                for(var i=0;i<allresults.length;i++){
                 	var records=temptActvDataTable.getRecordSet();
                	temptActvDataTable.addRow({stageOrderNo:allresults[i].stageOrderNo,description:allresults[i].description,percentage:allresults[i].percentage,Delete:'X'});
              	}
				if(allresults.length!=0){
					calculateTotalPercentage();
				}
            };
            
	        var myCodeFailureHandler = function() {
	            document.getElementById("milestone_activity_error").style.display='';
	            document.getElementById("milestone_activity_error").innerHTML='<s:text name="milestone.template.code.invalid"/>';
	        };
	 makeJSONCall(["stageOrderNo","description","percentage"],'${pageContext.request.contextPath}/milestone/ajaxMilestone!findByCode.action',{code:code},myCodeSuccessHandler,myCodeFailureHandler) ;
}

function searchTemplate(){
	clearMessage('milestone_activity_error');
	var typeOfWork="";
	var subTypeOfWork="";
	var milestoneTemplateCode="";
	
	if(dom.get("codeSearch").value!=''){
		milestoneTemplateCode=dom.get("codeSearch").value;
	}
	
	if(dom.get("parentCategory").value== -1){
        dom.get("milestone_activity_error").style.display='';
    	document.getElementById("milestone_activity_error").innerHTML='<s:text name="milestone.worktype.null" />';
    	return false;
	}	
	
	if(dom.get("parentCategory").value!= -1){
		typeOfWork=dom.get("parentCategory").value;
		if(dom.get("category").value!= -1){
			subTypeOfWork=dom.get("category").value;
		}
	}
	window.open("${pageContext.request.contextPath}/masters/milestoneTemplate!searchTemplate.action?sourcepage=searchForMilestone"+"&milestoneTemplateCode="+milestoneTemplateCode+"&workType="+typeOfWork+"&subType="+subTypeOfWork,"", "height=650,width=980,scrollbars=yes,left=0,top=0,status=yes");
}
</script>


<table  width="100%" border="0" cellspacing="0" cellpadding="0">
 		<tr>
                <td colspan="4" class="headingwk"><div class="arrowiconwk"><image src="<egov:url path='/resources/erp2/images/arrow.gif'/>" /></div><div class="headplacer">Milestone Template</div></td>
              </tr>
              <tr>
                <td class="whiteboxwk"><span class="mandatory">*</span><s:text name="milestone.template.type" />:</td>
                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="parentCategory" id="parentCategory" cssClass="selectwk" list="dropdownData.parentCategoryList" listKey="id" listValue="description" value="%{parentCategory.id}" onChange="setupSubTypes(this);"/>
                <egov:ajaxdropdown id="categoryDropdown" fields="['Text','Value']" dropdownId='category' url='estimate/ajaxEstimate!subcategories.action' selectedValue="%{category.id}"/>
                </td>

                <td class="whiteboxwk"><s:text name="milestone.template.subtype" />:</td>
                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="category" value="%{category.id}" id="category" cssClass="selectwk" list="dropdownData.categoryList" listKey="id" listValue="description"/>
                </td>
              </tr>
        <tr>
        	<td class="greyboxwk"><s:text name="milestone.search.template" />:</td>
        	<td class="greybox2wk">  <div class="yui-skin-sam">
                <div id="codeSearch_autocomplete">
                <div><s:textfield id="codeSearch" type="text" name="templateCode" class="selectwk" /></div>
                <span id="codeSearchResults"></span>
                </div>
                </div>
                <egov:autocomplete name="codeSearch" width="20" field="codeSearch" url="ajaxMilestone!searchAjax.action?" queryQuestionMark="false" results="codeSearchResults" paramsFunction="codeSearchParameters" handler="codeSearchSelectionHandler" forceSelectionHandler="codeSelectionEnforceHandler" afterHandler="afterCodeResults"/>
                <span class='warning' id="improperCodeSelectionWarning"></span>
                <div id="loadImageForCode" style="display:none"><image src="<egov:url path='/images/loading.gif'/>" />Loading Templatecodes. Please wait..</div>
            </td>
            <td colspan="2" class="greybox2wk"><input type="button"  class="buttonadd" onclick="getTemplateForCode()" value="Submit" id="codeSubmitButton" name="codeSubmitButton"/>    			
  </td>
        </tr>
        <tr><td class="whiteboxwk"><input type="button"  class="buttonadd" onclick="searchTemplate()" value="Search Milestone Template" id="searchTemplateButton" name="searchTemplateButton"/></td>
        <td colspan="3" class="whiteboxwk">&nbsp;</td></tr>
</table>
<div id="milestone_activity_error" class="errorstyle" style="display:none;"></div>
