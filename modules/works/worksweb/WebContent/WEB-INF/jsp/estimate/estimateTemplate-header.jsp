<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
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
</script>

     <table width="100%" border="0" cellspacing="0" cellpadding="0">
             <tr>
                <td width="11%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="estimate.template.code" />:</td>
                <td width="21%" class="whitebox2wk"><s:textfield name="code" value="%{code}" id="code" cssClass="selectwk" maxlength="25" />
                <s:if test="%{model.id!=null}" >
                <td width="15%" class="whiteboxwk"><s:text name="estimate.template.status" />:</td>
                <td width="53%" class="whitebox2wk"><s:select headerKey="0"  list="#{'0':'INACTIVE', '1':'ACTIVE'}"  name="status"  value="%{status}" id="status" cssClass="selectwk"/> </td>
                </td>
                </s:if>
                <s:else>
                	<td class="whiteboxwk" colspan="2">&nbsp;</td>
                	<s:hidden name="status" value="%{status}" id="status"  />
                </s:else>
              </tr>
               <tr>
                <td class="greyboxwk"><span class="mandatory">*</span><s:text name="estimate.template.name" />:</td>
                <td class="greybox2wk"><s:textarea name="name" cols="35" cssClass="selectwk" id="name" maxlength="50" onkeyup="return ismaxlength(this)" value="%{name}"/></td>
                <td class="greyboxwk"><span class="mandatory">*</span><s:text name="estimate.template.description" />:</td>

                <td class="greybox2wk"><s:textarea name="description" cols="35" cssClass="selectwk" id="description" maxlength="250" onkeyup="return ismaxlength(this)" value="%{description}"/></td>
              </tr>
              <tr>
                <td class="whiteboxwk"><span class="mandatory">*</span><s:text name="estimate.template.type" />:</td>
                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="workType" id="workType" cssClass="selectwk" list="dropdownData.parentCategoryList" listKey="id" listValue="description" value="%{workType.id}" onChange="setupSubTypes(this);"/>
                <egov:ajaxdropdown id="categoryDropdown" fields="['Text','Value']" dropdownId='subType' url='estimate/ajaxEstimate!subcategories.action' selectedValue="%{subType.id}"/>
                </td>

                <td class="whiteboxwk"><s:text name="estimate.template.subtype" />:</td>
                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="subType" value="%{subType.id}" id="subType" cssClass="selectwk" list="dropdownData.categoryList" listKey="id" listValue="description"/>
                </td>
              </tr>
                 <tr>
                <td  colspan="4" class="shadowwk"> </td>               
                </tr>
                <tr><td>&nbsp;</td></tr>			
          </table>
 <script type="text/javascript">
  <s:if test="%{mode=='view'}">
	for(i=0;i<document.estimateTemplateForm.elements.length;i++){
		document.estimateTemplateForm.elements[i].disabled=true;
		document.estimateTemplateForm.elements[i].readonly=true;
	} 
  </s:if>
</script>           
