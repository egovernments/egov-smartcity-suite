<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>

<script>

function setupSubTypes(elem){
    categoryId=elem.options[elem.selectedIndex].value;
    populatesubType({category:categoryId});
}

function disableSelect(){
	for(i=0;i<document.milestoneTemplateForm.elements.length;i++){
			document.milestoneTemplateForm.elements[i].disabled=true;
			document.milestoneTemplateForm.elements[i].readonly=true;
	} 

	temptActvDataTable.removeListener('cellClickEvent');
}

function enableSelect(){
	for(i=0;i<document.milestoneTemplateForm.elements.length;i++){
			document.milestoneTemplateForm.elements[i].disabled=false;
			document.milestoneTemplateForm.elements[i].readonly=false;
	} 
}

function validateHeaderBeforeSubmit(milestoneTemplateForm) {
	var code = milestoneTemplateForm.code.value;
	document.getElementById("milestonetemplateerror").innerHTML="";
	var err=false;
	if(code=='') {
		dom.get("milestonetemplateerror").style.display='';     
		document.getElementById("milestonetemplateerror").innerHTML+='<s:text name="milestonetemplate.code.not.null" /><br>';
		err=true;
	}
	
	var name = milestoneTemplateForm.name.value;
	if(name=='') {
		dom.get("milestonetemplateerror").style.display='';     
		document.getElementById("milestonetemplateerror").innerHTML+='<s:text name="milestonetemplate.name.not.null" /><br>';
		err=true;
	}
	
	var description = milestoneTemplateForm.description.value;
	if(description=='') {
		dom.get("milestonetemplateerror").style.display='';     
		document.getElementById("milestonetemplateerror").innerHTML+='<s:text name="milestonetemplate.description.not.null" /><br>';
		err=true;
	}
	
	var worktype = milestoneTemplateForm.workType.value;
	if(worktype<0) {
		dom.get("milestonetemplateerror").style.display='';     
		document.getElementById("milestonetemplateerror").innerHTML+='<s:text name="milestonetemplate.workType.not.null" /><br>';
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
</script>


<div class="panel panel-primary" data-collapsed="0" style="text-align:left">
		<div class="panel-heading">
			<div class="panel-title"></div>
		</div>
		<div class="panel-body no-margin-bottom">
			<div class="form-group">
				<label class="col-sm-2 control-label text-right">
				    <s:text name="milestone.template.code" /> <span class="mandatory"/>
				</label>
				<div class="col-sm-3 add-margin">
					<s:textfield name="code" value="%{code}" id="code" cssClass="form-control" maxlength="25" />
				</div>
				<label class="col-sm-2 control-label text-right">
				    <s:text name="milestone.template.status" />
				</label>
				<div class="col-sm-3 add-margin">
					<s:select headerKey="1"  list="#{ '1':'ACTIVE','0':'INACTIVE'}"  name="status"  value="%{status}" id="status" cssClass="form-control" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label text-right">
				    <s:text name="milestone.template.name" /> <span class="mandatory"></span>
				</label>
				<div class="col-sm-3 add-margin">
					<s:textarea name="name" cols="35" cssClass="form-control" id="name" maxlength="256" onkeyup="return ismaxlength(this)" value="%{name}"/>
				</div>
				<label class="col-sm-2 control-label text-right">
				    <s:text name="milestone.template.description" /> <span class="mandatory"></span>
				</label>
				<div class="col-sm-3 add-margin">
					<s:textarea name="description" cols="35" cssClass="form-control" id="description" maxlength="1024" onkeyup="return ismaxlength(this)" value="%{description}"/>
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label text-right">
				    <s:text name="milestone.template.type" /> <span class="mandatory"></span>
				</label>
				<div class="col-sm-3 add-margin">
					<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="workType" id="workType" cssClass="form-control" list="dropdownData.parentCategoryList" listKey="id" listValue="description" value="%{workType.id}" onChange="setupSubTypes(this);"/>
                	<egov:ajaxdropdown id="categoryDropdown" fields="['Text','Value']" dropdownId='subType' url='estimate/ajaxEstimate-subcategories.action' selectedValue="%{subType.id}"/>
				</div>
				<label class="col-sm-2 control-label text-right">
				    <s:text name="milestone.template.subtype" />
				</label>
				<div class="col-sm-3 add-margin">
					<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="subType" value="%{subType.id}" id="subType" cssClass="form-control" list="dropdownData.categoryList" listKey="id" listValue="description"/>
				</div>
			</div>
			
			
			
		</div>
</div>
