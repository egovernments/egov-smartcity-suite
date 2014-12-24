			
			
			
			<s:textfield class="labelcell" label="%{getText('Masters.complainttype.name')}" required="true"  name="model.ComplaintTypeName"/>
			<s:textfield class="labelcell" label="%{getText('Masters.complainttype.name.local')}" name="model.ComplaintTypeNameLocal"/>
			 <s:textfield class="labelcell" label="Comaplint Type Code" name="model.ComplaintTypeCode" id="model.ComplaintTypeCode" onblur="uniquecheck();"/> 
			
		<%----%>	<s:select  list="departmentGpMap" label="%{getText('Masters.department')}"  name="model.departmentId" Key="id" Value="deptName" headerKey="0" headerValue="%{getText('Please.Choose')}"></s:select>
			
			<s:select  list="complaintGpMap" label="%{getText('Masters.complaintgroup')}" required="true" name="model.ComplaintGroupId" Key="complantGroup_Id" Value="complaintGroupName" headerKey="0" headerValue="%{getText('Please.Choose')}"></s:select>
			
			<s:textfield class="labelcell" label="%{getText('Masters.noofdays')}" required="true" name="model.NoOfDays"/>
			
			<s:if test="%{categoryList.size() != 0}">
			<s:textfield class="labelcell" label="%{getText('Masters.complaint.category')}"  name="model.ComplaintCategory"/>
			</s:if>
			