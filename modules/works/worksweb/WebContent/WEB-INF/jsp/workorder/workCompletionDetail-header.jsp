<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<script>
function disableSelect(){
	for(var i=0;i<document.workCompletionForm.elements.length;i++){
			document.workCompletionForm.elements[i].disabled=true;
			document.workCompletionForm.elements[i].readonly=true;
	} 


	var links=document.workCompletionForm.getElementsByTagName("a");
			for(var i=0;i<links.length;i++){
     				links[i].onclick=function(){return false;};
			}
}

function enableSelect(){
	for(var i=0;i<document.workCompletionForm.elements.length;i++){
			document.workCompletionForm.elements[i].disabled=false;
			document.workCompletionForm.elements[i].readonly=false;
	} 
}
</script>

     <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                	<td class="whiteboxwk"><s:text name="workcompletion.workorder.workorderNumber" />:</td>
                	<td class="whitebox2wk"><s:textfield  value="%{workOrder.workOrderNumber}" id="workOrderNumber" cssClass="selectwk" /></td>
                	<td class="whiteboxwk"><s:text name="workcompletion.workorder.woDate" />:</td>
                	<td class="whitebox2wk"><s:date name="workOrder.workOrderDate" format="dd/MM/yyyy" /></td>
              	</tr>
              	<tr>
                	<td class="greyboxwk"><s:text name="workcompletion.workorder.contractor" />:</td>
                	<td class="greybox2wk"><s:textfield  value="%{workOrder.contractor.name}" id="contractor" cssClass="selectwk" /></td>
	               	<td width="11%" class="greyboxwk"><s:text name="workcompletion.estimate.status" />:</td>
	                <td width="21%" class="greybox2wk">
	                <s:if test="%{estimate.state.previous.value=='ADMIN_SANCTIONED' || estimate.state.previous.value=='CANCELLED'}">
						<s:textfield  value="%{estimate.state.previous.value}" id="status" cssClass="selectwk" />
					</s:if>
					<s:else>
						<s:textfield  value="%{estimate.state.value}" id="status" cssClass="selectwk" />
					</s:else>
					</td>
	            </tr>
     
             	<tr>
                <td class="whiteboxwk"><s:text name="workcompletion.estimate.number" />:</td>
                <td class="whitebox2wk"><s:textfield  value="%{estimate.estimateNumber}" id="estimateNumber" cssClass="selectwk" /></td>
                <td class="whiteboxwk"><s:text name="workcompletion.estimate.date" />:</td>
                <td class="whitebox2wk"><s:date name="estimate.estimateDate" format="dd/MM/yyyy" /></td>
                </td>
              </tr>

               <tr>
                <td class="greyboxwk"><s:text name="workcompletion.estimate.workName" />:</td>
                <td class="greybox2wk"><s:textarea cols="35" cssClass="selectwk" id="workName" maxlength="1024" value="%{estimate.name}"/></td>
                <td width="15%" class="greyboxwk"><s:text name="workcompletion.estimate.execDept" />:</td>
                <td width="53%" class="greybox2wk"><s:textfield  value="%{estimate.executingDepartment.deptName}" id="execDept" cssClass="selectwk" /></td>
              </tr>

              <tr>
                <td class="whiteboxwk"><s:text name="workcompletion.estimate.typeofwork" />:</td>
                <td class="whitebox2wk"><s:textfield  value="%{estimate.parentCategory.description}" id="typeOfWork" cssClass="selectwk" /></td>
                <td class="whiteboxwk"><s:text name="workcompletion.estimate.subtypeofwork" />:</td>
                <td class="whitebox2wk"><s:textfield  value="%{estimate.category.description}" id="subtypeOfWork" cssClass="selectwk" /></td>
              </tr>

              <tr>
                <td class="greyboxwk"><s:text name="workcompletion.estimate.projectcode" />:</td>
                <td class="greybox2wk"><s:textfield  value="%{estimate.projectCode.code}" id="projectcode" cssClass="selectwk" /></td>
 				<td class="greyboxwk" colspan="2">&nbsp;</td>
              </tr>

              <tr>
                <td  colspan="4" class="shadowwk"> </td>               
               </tr>
                <tr><td>&nbsp;</td></tr>			
          </table>
