<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>

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
                getUOMFactor(allresults[i].UOM,sorDataTable.getRecord(sorDataTable.getRecordSet().getLength()-1));
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
	 makeJSONCall(["sor","Id","Description","Code","FullDescription","UOM","UnitRate"],'${pageContext.request.contextPath}/estimate/ajaxEstimateTemplate-findCodeAjax.action',{estimateDate:document.getElementById("estimateDate").value,code:code},myCodeSuccessHandler,myCodeFailureHandler) ;
}

function searchTemplate(){
	var typeOfWork="";
	var subTypeOfWork="";
	var estimateTemplateCode="";

	console.log(dom.get('codeSearch'));

	if(dom.get("codeSearch").value!=''){
		estimateTemplateCode=dom.get("codeSearch").value;
	}
	if(dom.get("parentCategory").value!= -1){
		typeOfWork=dom.get("parentCategory").value;
		if(dom.get("category").value!= -1){
			subTypeOfWork=dom.get("category").value;
		}
	}
	
	window.open("${pageContext.request.contextPath}/estimate/estimateTemplate-search.action?sourcePage=searchForEstimate"+"&estimateTemplateCode="+estimateTemplateCode+"&typeOfWork="+typeOfWork+"&subTypeOfWork="+subTypeOfWork,"", "height=650,width=980,scrollbars=yes,left=0,top=0,status=yes");
}
</script>

<div class="panel panel-primary" data-collapsed="0" style="text-align:left">
	<div class="panel-heading">
		<div class="panel-title">
		   Estimate Template
		</div>
	</div>
	<div class="panel-body">
	   
	  <div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <s:text name="estimate.work.type" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="parentCategory" id="parentCategory" cssClass="form-control" list="dropdownData.parentCategoryList" listKey="id" listValue="description" value="%{parentCategory.id}" onChange="setupSubTypes(this);"/>
                <egov:ajaxdropdown id="categoryDropdown" fields="['Text','Value']" dropdownId='category' url='estimate/ajaxEstimate-subcategories.action' selectedValue="%{category.id}"  afterSuccess="loadSubType" /> 
			</div>
			<label class="col-sm-2 control-label text-right">
			    <s:text name="estimate.work.subtype" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="category" value="%{category.id}" id="category" cssClass="form-control" list="dropdownData.categoryList" listKey="id" listValue="description"/>
			</div>
		</div>
		
		<div class="form-group no-margin-bottom">
			<label class="col-sm-2 control-label text-right">
			    <s:text name="estimate.search.estimate.template" />
			</label>
			<div class="col-sm-3 add-margin">
				<div id="codeSearch_autocomplete">
					<div class="right-inner-addon">
					    <s:textfield id="codeSearch" type="text" name="templateCode" class="form-control" />
					    <i id="loadImageForCode" class="fa fa-circle-o-notch fa-spin" style="display:none"></i>
	    			</div>    
	                <span id="codeSearchResults"></span>
	                <egov:autocomplete name="codeSearch" width="20" field="codeSearch" url="ajaxEstimateTemplate-searchAjax.action?status=1&" queryQuestionMark="false" results="codeSearchResults" paramsFunction="codeSearchParameters" handler="codeSearchSelectionHandler" forceSelectionHandler="codeSelectionEnforceHandler" afterHandler="afterCodeResults"/>						               
                </div> 
			</div>
			<div class="col-sm-5 add-margin">
			   <input type="button" class="btn btn-primary" onclick="getTemplateForCode()" value="Submit" id="codeSubmitButton" name="codeSubmitButton"/>
			</div>
		</div>
		
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-3">
			   <input type="button" class="btn btn-primary full-width mtb-5" onclick="searchTemplate()" value="Search Estimate Template" id="searchTemplateButton" name="searchTemplateButton"/>
	            <div id="improperCodeSelectionWarning" class="alert alert-warning no-margin" style="display:none;">
				  <strong>Warning!</strong> Indicates a warning that might need attention.
				</div>
			</div>
		</div>
		
	</div>
</div>