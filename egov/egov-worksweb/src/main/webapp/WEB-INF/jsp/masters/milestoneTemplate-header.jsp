#-------------------------------------------------------------------------------
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
#-------------------------------------------------------------------------------
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
