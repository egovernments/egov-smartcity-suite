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
<script src="<egov:url path='js/works.js'/>"></script> 
<script>
function codeSearchParameters(){
	   
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
		dom.get("sor_error").style.display='';
		document.getElementById("sor_error").innerHTML='Please select the template.';
		return false;
	}
	return true;
}
function afterCodeResults(sType,results){
    clearMessage('sor_error');
    document.getElementById("loadImageForCode").style.display='none';
    if(results[2].length==0) showMessage('sor_error','No Templates with the given code')
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
	if(code!="" && (sorDataTable.getRecordSet().getLength()>0 || nonSorDataTable.getRecordSet().getLength()>0)){ 
	var ans=confirm('<s:text name="estimate.template.confirm.reset"/>');	
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
	sorDataTable.deleteRows(0,sorDataTable.getRecordSet().getLength());
    nonSorDataTable.deleteRows(0,nonSorDataTable.getRecordSet().getLength());
}

function getActivitiesForTemplate(code){
 var myCodeSuccessHandler = function(req,res) {
                dom.get("sor_error").style.display='none';
                dom.get("sor_error").innerHTML='';
               
                var allresults=res.results;
                for(var i=0;i<allresults.length;i++){
                if(allresults[i].sor!=null && allresults[i].sor=="yes"){
                records=sorDataTable.getRecordSet();
                sorDataTable.addRow({schedule:allresults[i].Id,Code:allresults[i].Code,SlNo:sorDataTable.getRecordSet().getLength()+1,Description:allresults[i].Description,UOM:allresults[i].UOM,sorrate:allresults[i].UnitRate,rate:allresults[i].UnitRate,Delete:'X',FullDescription:allresults[i].FullDescription});
                getFactor(allresults[i].UOM,sorDataTable.getRecord(sorDataTable.getRecordSet().getLength()-1));
                }
                else if(allresults[i].sor!=null && allresults[i].sor=="no"){
                nonSorDataTable.addRow({NonSorId:allresults[i].Id,Sl_No:nonSorDataTable.getRecordSet().getLength()+1,SlNo:nonSorDataTable.getRecordSet().getLength()+1,nonSordescription:allresults[i].Description,Uom:allresults[i].UOM,nonSorUom:allresults[i].UOM,rate:allresults[i].UnitRate,Delete:'X'  });
                 var column = nonSorDataTable.getColumn('nonSordescription');
                 record = nonSorDataTable.getRecord(nonSorDataTable.getRecordSet().getLength()-1);
                
        // Important to use escape=false. Otherwise struts will replace double quotes with &quote;  
        dom.get(column.getKey()+record.getId()).value = allresults[i].Description;
        
        var column = nonSorDataTable.getColumn('rate');  
        dom.get("nonsor"+column.getKey()+record.getId()).value = allresults[i].UnitRate;
        
                }
              }
            };
            
	        var myCodeFailureHandler = function() {
	            dom.get("sor_error").style.display='';
	            document.getElementById("sor_error").innerHTML='<s:text name="estimate.sor.invalid.sor"/>';
	        };
	 makeJSONCall(["sor","Id","Description","Code","FullDescription","UOM","UnitRate"],'${pageContext.request.contextPath}/estimate/ajaxEstimateTemplate!findCodeAjax.action',{estimateDate:document.getElementById("estimateDate").value,code:code},myCodeSuccessHandler,myCodeFailureHandler) ;
}

function searchTemplate(){
	var typeOfWork="";
	var subTypeOfWork="";
	var estimateTemplateCode="";
	var dwRCRelatedParam = "";
	
	if(dom.get("codeSearch").value!=''){
		estimateTemplateCode=dom.get("codeSearch").value;
	}
	if(dom.get("parentCategory").value!= -1){
		typeOfWork=dom.get("parentCategory").value;
		if(dom.get("category").value!= -1){
			subTypeOfWork=dom.get("category").value;
		}
	}
	
	//TODO - where is this variable checkDWRelatedSORs is used?
	<s:if test="%{fromRateContract==true}" >
		dwRCRelatedParam = "&checkDWRelatedSORs=true"; 
	</s:if>
	window.open("${pageContext.request.contextPath}/estimate/estimateTemplate!search.action?sourcePage=searchForEstimate"+"&estimateTemplateCode="+estimateTemplateCode+"&typeOfWork="+typeOfWork+"&subTypeOfWork="+subTypeOfWork+dwRCRelatedParam,"", "height=650,width=980,scrollbars=yes,left=0,top=0,status=yes");
}
</script>


<table  width="100%" border="0" cellspacing="0" cellpadding="0">
 		<tr>
                <td colspan="4" class="headingwk"><div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div><div class="headplacer">Estimate Template</div></td>
              </tr>
 		<tr>
                <td class="whiteboxwk"><span class="mandatory">*</span><s:text name="estimate.work.type" />:</td>
                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="parentCategory" id="parentCategory" cssClass="selectwk" list="dropdownData.parentCategoryList" listKey="id" listValue="description" value="%{parentCategory.id}" onChange="setupSubTypes(this);"/>
                <egov:ajaxdropdown id="categoryDropdown" fields="['Text','Value']" dropdownId='category' url='estimate/ajaxEstimate!subcategories.action' selectedValue="%{category.id}"  afterSuccess="loadSubType" />
                </td>

                <td class="whiteboxwk"><s:text name="estimate.work.subtype" />:</td>
                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="category" value="%{category.id}" id="category" cssClass="selectwk" list="dropdownData.categoryList" listKey="id" listValue="description"/>
                </td>
        </tr>
        <tr>
        	<td class="greyboxwk"><s:text name="estimate.search.estimate.template" />:</td>
        	<td class="greybox2wk">  <div class="yui-skin-sam">
                <div id="codeSearch_autocomplete">
                <div><s:textfield id="codeSearch" type="text" name="templateCode" class="selectwk" /></div>
                <span id="codeSearchResults"></span>
                </div>
                </div>
                <egov:autocomplete name="codeSearch" width="20" field="codeSearch" url="ajaxEstimateTemplate!searchAjax.action?status=1&" queryQuestionMark="false" results="codeSearchResults" paramsFunction="codeSearchParameters" handler="codeSearchSelectionHandler" forceSelectionHandler="codeSelectionEnforceHandler" afterHandler="afterCodeResults"/>
                <span class='warning' id="improperCodeSelectionWarning"></span>
                <div id="loadImageForCode" style="display:none"><image src="<egov:url path='/images/loading.gif'/>" />Loading Templatecodes. Please wait..</div>
            </td>
            <td colspan="2" class="greybox2wk"><input type="button"  class="buttonadd" onclick="getTemplateForCode()" value="Submit" id="codeSubmitButton" name="codeSubmitButton"/>    			
  </td>
        </tr>
        <tr><td class="whiteboxwk"><input type="button"  class="buttonadd" onclick="searchTemplate()" value="Search Estimate Template" id="searchTemplateButton" name="searchTemplateButton"/></td>
        <td colspan="3" class="whiteboxwk"><s:text name="estimate.rate.disclaimer"/></td></tr>
</table>
