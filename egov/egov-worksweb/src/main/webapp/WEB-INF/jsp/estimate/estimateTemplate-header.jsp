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
function setupSubTypes(elem){
    categoryId=elem.options[elem.selectedIndex].value;
    populatesubType({category:categoryId});
}

function uniqueCheckOnCode(obj)
{
	code = dom.get('code').value;
	status = dom.get('status').value;
		if(code!=''){
		     populatetemplatecodeerror({code:code,status:status});
		}else{
			dom.get("templatecodeerror").style.display = "none";
		}
}

function validateHeaderBeforeSubmit(estimateTemplateForm) {
	var code = estimateTemplateForm.code.value;
	document.getElementById("estimatetemplateerror").innerHTML="";
	var err=false;
	if(code=='') {
		dom.get("estimatetemplateerror").style.display='';     
		document.getElementById("estimatetemplateerror").innerHTML+='<s:text name="estimatetemplate.code.not.null" /><br>';
		err=true;
	}
	else{
		if(dom.get("templatecodeerror").style.display!='none'){
			err=true;
		}
	}
	
	var name = estimateTemplateForm.name.value;
	if(name=='') {
		dom.get("estimatetemplateerror").style.display='';     
		document.getElementById("estimatetemplateerror").innerHTML+='<s:text name="estimatetemplate.name.not.null" /><br>';
		err=true;
	}
	
	var description = estimateTemplateForm.description.value;
	if(description=='') {
		dom.get("estimatetemplateerror").style.display='';     
		document.getElementById("estimatetemplateerror").innerHTML+='<s:text name="estimatetemplate.description.not.null" /><br>';
		err=true;
	}
	
	var worktype = estimateTemplateForm.workType.value;
	if(worktype<0) {
		dom.get("estimatetemplateerror").style.display='';     
		document.getElementById("estimatetemplateerror").innerHTML+='<s:text name="estimatetemplate.workType.not.null" /><br>';
		err=true;
	}
	if(sorDataTable.getRecordSet().getLength()<1 && nonSorDataTable.getRecordSet().getLength()<1){
		dom.get("estimatetemplateerror").style.display='';     
		document.getElementById("estimatetemplateerror").innerHTML+='<s:text name="estimatetemplate.sornonsor.not.null" /><br>';
		err=true;
	}
	if(err)
		return false;
	else
		return true;
}

function ismaxlength(obj){
var mlength=obj.getAttribute? parseInt(obj.getAttribute("maxlength")) : ""
if (obj.getAttribute && obj.value.length>mlength)
obj.value=obj.value.substring(0,mlength)
}
<s:if test="%{mode=='view'}">
for(i=0;i<document.estimateTemplateForm.elements.length;i++){
	document.estimateTemplateForm.elements[i].disabled=true;
	document.estimateTemplateForm.elements[i].readonly=true;
} 
</s:if>
</script>

<div class="panel panel-primary" data-collapsed="0" style="text-align:left">
	<div class="panel-heading">
		<div class="panel-title" ></div>
	</div>
	<div class="panel-body">
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <s:text name="estimate.template.code" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<s:textfield name="code" value="%{code}" id="code" cssClass="form-control" maxlength="25"/>
			</div>
			<s:if test="%{model.id!=null}" >
				<label class="col-sm-2 control-label text-right">
				    <s:text name="estimate.template.status" />
				</label>
			<div class="col-sm-3 add-margin">
				<s:select headerKey="1"  list="#{'0':'ACTIVE', '1':'INACTIVE'}"  name="status"  value="%{status}" id="status" cssClass="form-control"/>
			</div>
            </s:if>
            <s:else>
            <s:hidden name="status" value="%{status}" id="status"  />
            </s:else>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <s:text name="estimate.template.name" /> <span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
			
				<s:textarea name="name" cols="35" rows="2" cssClass="form-control" id="name" maxlength="250" onkeyup="return ismaxlength(this)" value="%{name}"/>
			</div>
			<label class="col-sm-2 control-label text-right">
			    <s:text name="estimate.template.description" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:textarea name="description" cols="35" rows="2" cssClass="form-control" id="description" maxlength="250" onkeyup="return ismaxlength(this)" value="%{description}"/>
			</div>
		</div>
					
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <s:text name="estimate.template.type" /> <span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="workType" id="workType" cssClass="form-control" list="dropdownData.parentCategoryList" listKey="id" listValue="description" value="%{workType.id}" onChange="setupSubTypes(this);"/>
            	<egov:ajaxdropdown id="categoryDropdown" fields="['Text','Value']" dropdownId='subType' url='estimate/ajaxEstimate-subcategories.action' selectedValue="%{subType.id}"/>
			</div>
			<label class="col-sm-2 control-label text-right">
			    <s:text name="estimate.template.subtype" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="subType" value="%{subType.id}" id="subType" cssClass="form-control" list="dropdownData.categoryList" listKey="id" listValue="description"/>
			</div>
		</div>
	</div>
</div>         
