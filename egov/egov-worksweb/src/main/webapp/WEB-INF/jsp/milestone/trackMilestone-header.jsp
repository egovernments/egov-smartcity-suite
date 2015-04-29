<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<script>

function checkProjectCompletion(obj){ 
   if(obj.checked){
	 	document.trackMilestoneForm.isProjectCompleted.value=true;
	 	document.trackMilestoneForm.isProjectCompleted.checked=true;
	}
	else if(!obj.checked){
	 	document.trackMilestoneForm.isProjectCompleted.value=false;
	 	document.trackMilestoneForm.isProjectCompleted.checked=false;
	}
}

function disableSelect(){
	for(var i=0;i<document.trackMilestoneForm.elements.length;i++){
			document.trackMilestoneForm.elements[i].disabled=true;
			document.trackMilestoneForm.elements[i].readonly=true;
	} 

	temptActvDataTable.removeListener('cellClickEvent');

	var links=document.trackMilestoneForm.getElementsByTagName("a");
			for(var i=0;i<links.length;i++){
     				links[i].onclick=function(){return false;};
			}
}

function enableSelect(){
	for(var i=0;i<document.trackMilestoneForm.elements.length;i++){
			document.trackMilestoneForm.elements[i].disabled=false;
			document.trackMilestoneForm.elements[i].readonly=false;
	} 
}

function ismaxlength(obj){
	var mlength=obj.getAttribute? parseInt(obj.getAttribute("maxlength")) : "";
	if (obj.getAttribute && obj.value.length>mlength)
		obj.value=obj.value.substring(0,mlength);
}

</script>

     <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                	<td class="whiteboxwk"><s:text name="milestone.workorder.workorderNumber" />:</td>
                	<td class="whitebox2wk"><s:textfield  value="%{model.milestone.workOrderEstimate.workOrder.workOrderNumber}" id="workOrderNumber" cssClass="selectwk" /></td>
                	<td class="whiteboxwk"><s:text name="milestone.workorder.woDate" />:</td>
                	<td class="whitebox2wk"><s:date name="model.milestone.workOrderEstimate.workOrder.workOrderDate" format="dd/MM/yyyy" />
              	</tr>
              	<tr>
                	<td class="greyboxwk"><s:text name="milestone.workorder.contractor" />:</td>
                	<td class="greybox2wk"><s:textfield  value="%{model.milestone.contractor}" id="contractor" cssClass="selectwk" /></td>
	               	<td width="11%" class="greyboxwk"><s:text name="milestone.estimate.status" />:</td>
	                <td width="21%" class="greybox2wk">
	                <s:if test="%{model.milestone.workOrderEstimate.estimate.egwStatus!=null}">
						<s:textfield  value="%{model.milestone.workOrderEstimate.estimate.egwStatus.code}" id="status" cssClass="selectwk" />
					</s:if>
	            </tr>
     
             	<tr>
                <td class="whiteboxwk"><s:text name="milestone.estimate.number" />:</td>
                <td class="whitebox2wk"><s:textfield  value="%{model.milestone.workOrderEstimate.estimate.estimateNumber}" id="estimateNumber" cssClass="selectwk" />
                <td class="whiteboxwk"><s:text name="milestone.estimate.date" />:</td>
                <td class="whitebox2wk"><s:date name="model.milestone.workOrderEstimate.estimate.estimateDate" format="dd/MM/yyyy" /></td>
                </td>
              </tr>

               <tr>
                <td class="greyboxwk"><s:text name="milestone.estimate.workName" />:</td>
                <td class="greybox2wk"><s:textarea cols="35" cssClass="selectwk" id="workName" maxlength="1024" value="%{model.milestone.workOrderEstimate.estimate.name}"/></td>
                <td width="15%" class="greyboxwk"><s:text name="milestone.estimate.execDept" />:</td>
                <td width="53%" class="greybox2wk"><s:textfield  value="%{model.milestone.workOrderEstimate.estimate.executingDepartment.deptName}" id="execDept" cssClass="selectwk" />
              </tr>

              <tr>
                <td class="whiteboxwk"><s:text name="milestone.estimate.typeofwork" />:</td>
                <td class="whitebox2wk"><s:textfield  value="%{model.milestone.workOrderEstimate.estimate.parentCategory.description}" id="typeOfWork" cssClass="selectwk" /></td>
                <td class="whiteboxwk"><s:text name="milestone.estimate.subtypeofwork" />:</td>
                <td class="whitebox2wk"><s:textfield  value="%{model.milestone.workOrderEstimate.estimate.category.description}" id="subtypeOfWork" cssClass="selectwk" /></td>
              </tr>

              <tr>
                <td class="greyboxwk"><s:text name="milestone.estimate.projectcode" />:</td>
                <td class="greybox2wk"><s:textfield  value="%{model.milestone.workOrderEstimate.estimate.projectCode.code}" id="projectcode" cssClass="selectwk" />
 				<td class="greyboxwk" ><s:checkbox name="isProjectCompleted" id="isProjectCompleted" value="%{isProjectCompleted}" onclick="checkProjectCompletion(this)"/></td>
              	<td class="greybox2wk"><s:text name="milestone.completion.flag" /></td>
              </tr>

              <tr>
                <td  colspan="4" class="shadowwk"> </td>               
               </tr>
                <tr><td>&nbsp;</td></tr>			
          </table>
