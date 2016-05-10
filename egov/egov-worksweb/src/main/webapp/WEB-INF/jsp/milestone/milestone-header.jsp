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

function disableSelect(){
	for(i=0;i<document.milestoneForm.elements.length;i++){
			document.milestoneForm.elements[i].disabled=true;
			document.milestoneForm.elements[i].readonly=true;
	} 

	temptActvDataTable.removeListener('cellClickEvent');
}

function enableSelect(){
	for(i=0;i<document.milestoneForm.elements.length;i++){
			document.milestoneForm.elements[i].disabled=false;
			document.milestoneForm.elements[i].readonly=false;
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
                <td width="11%" class="whiteboxwk"></span><s:text name="milestone.estimate.status" />:</td>
                <td width="21%" class="whitebox2wk">
                <s:if test="%{model.workOrderEstimate.estimate.egwStatus!=null}">
					<s:textfield  value="%{model.workOrderEstimate.estimate.egwStatus.code}" id="status" cssClass="selectwk" />
				</s:if>
                
                <td width="15%" class="whiteboxwk"><s:text name="milestone.estimate.execDept" />:</td>
                <td width="53%" class="whitebox2wk"><s:textfield  value="%{model.workOrderEstimate.estimate.executingDepartment.deptName}" id="execDept" cssClass="selectwk" />
                </td>
              </tr>

               <tr>
                <td class="greyboxwk"><s:text name="milestone.estimate.number" />:</td>
                <td class="greybox2wk"><s:textfield  value="%{model.workOrderEstimate.estimate.estimateNumber}" id="estimateNumber" cssClass="selectwk" />
                <td class="greyboxwk"><s:text name="milestone.estimate.nature.work" />:</td>
                <td class="greybox2wk"><s:textfield  value="%{model.workOrderEstimate.estimate.type.name}" id="natureOfWork" cssClass="selectwk" />
              </tr>

              <tr>
                <td class="whiteboxwk"><s:text name="milestone.estimate.date" />:</td>
                <td class="whitebox2wk"><s:date name="model.workOrderEstimate.estimate.estimateDate" format="dd/MM/yyyy" />
                </td>
                <td class="whiteboxwk"><s:text name="milestone.estimate.typeofwork" />:</td>
                <td class="whitebox2wk"><s:textfield  value="%{model.workOrderEstimate.estimate.parentCategory.description}" id="typeOfWork" cssClass="selectwk" />
                </td>
              </tr>


              <tr>
                <td class="greyboxwk"><s:text name="milestone.estimate.subtypeofwork" />:</td>
                <td class="greybox2wk"><s:textfield  value="%{model.workOrderEstimate.estimate.category.description}" id="subtypeOfWork" cssClass="selectwk" />
                </td>
                <td class="greyboxwk"><s:text name="milestone.estimate.description" />:</td>
                <td class="greybox2wk"><s:textarea cols="35" cssClass="selectwk" id="description" maxlength="1024" value="%{model.workOrderEstimate.estimate.description}"/></td>
              </tr>

              <tr>
                <td class="whiteboxwk"><s:text name="milestone.estimate.preparedBy" />:</td>
                <td class="whitebox2wk"><s:textfield  value="%{model.workOrderEstimate.estimate.createdBy.name}" id="preparedBy" cssClass="selectwk" />
                </td>
                <td class="whiteboxwk"><s:text name="milestone.estimate.projectcode" />:</td>
                <td class="whitebox2wk"><s:textfield  value="%{model.workOrderEstimate.estimate.projectCode.code}" id="projectcode" cssClass="selectwk" />
              </tr>

              <tr>
                <td class="greyboxwk"><s:text name="milestone.workorder.workorderNumber" />:</td>
                <td class="greybox2wk"><s:textfield  value="%{model.workOrderEstimate.workOrder.workOrderNumber}" id="workOrderNumber" cssClass="selectwk" />
                </td>
                <td class="greyboxwk"><s:text name="milestone.workorder.woDate" />:</td>
                <td class="greybox2wk"><s:date name="model.workOrderEstimate.workOrder.workOrderDate" format="dd/MM/yyyy" />
              </tr>

              <tr>
                <td class="whiteboxwk"><s:text name="milestone.workorder.contractor" />:</td>
                <td class="whitebox2wk"><s:textfield  value="%{contractor}" id="contractor" cssClass="selectwk" />
                </td>
                <td class="whiteboxwk"><s:text name="milestone.workorder.value" />:</td>
                <td class="whitebox2wk"><s:textfield  value="%{model.workOrderEstimate.workOrder.workOrderAmount}" id="workOrderAmount" cssClass="selectwk" />
              </tr>


              <tr>
                <td  colspan="4" class="shadowwk"> </td>               
               </tr>
                <tr><td>&nbsp;</td></tr>			
          </table>
