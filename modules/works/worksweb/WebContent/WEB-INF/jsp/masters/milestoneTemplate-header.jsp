<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
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

     <table width="100%" border="0" cellspacing="0" cellpadding="0">
             <tr>
                <td width="11%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="milestone.template.code" />:</td>
                <td width="21%" class="whitebox2wk"><s:textfield name="code" value="%{code}" id="code" cssClass="selectwk" maxlength="25" />
                <td width="15%" class="whiteboxwk"><s:text name="milestone.template.status" />:</td>
                <td width="53%" class="whitebox2wk"><s:select headerKey="1"  list="#{ '1':'ACTIVE','0':'INACTIVE'}"  name="status"  value="%{status}" id="status" cssClass="selectwk" /> </td>
                </td>
              </tr>
               <tr>
                <td class="greyboxwk"><span class="mandatory">*</span><s:text name="milestone.template.name" />:</td>
                <td class="greybox2wk"><s:textarea name="name" cols="35" cssClass="selectwk" id="name" maxlength="256" onkeyup="return ismaxlength(this)" value="%{name}"/></td>
                <td class="greyboxwk"><span class="mandatory">*</span><s:text name="milestone.template.description" />:</td>

                <td class="greybox2wk"><s:textarea name="description" cols="35" cssClass="selectwk" id="description" maxlength="1024" onkeyup="return ismaxlength(this)" value="%{description}"/></td>
              </tr>
              <tr>
                <td class="whiteboxwk"><span class="mandatory">*</span><s:text name="milestone.template.type" />:</td>
                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="workType" id="workType" cssClass="selectwk" list="dropdownData.parentCategoryList" listKey="id" listValue="description" value="%{workType.id}" onChange="setupSubTypes(this);"/>
                <egov:ajaxdropdown id="categoryDropdown" fields="['Text','Value']" dropdownId='subType' url='estimate/ajaxEstimate!subcategories.action' selectedValue="%{subType.id}"/>
                </td>

                <td class="whiteboxwk"><s:text name="milestone.template.subtype" />:</td>
                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="subType" value="%{subType.id}" id="subType" cssClass="selectwk" list="dropdownData.categoryList" listKey="id" listValue="description"/>
                </td>
              </tr>
                 <tr>
                <td  colspan="4" class="shadowwk"> </td>               
                </tr>
                <tr><td>&nbsp;</td></tr>			
          </table>
