<display:column style="width:1%" title=" Sl No">
	<s:property value="%{#attr.currentRowObject_rowNum+ (page-1)*pageSize}"/>
</display:column>
<display:column style="width:2%" property="employeeCode"
	title=" Employee Code" />
<display:column style="width:4%" property="employeeName"
	title="Employee Name" />
<display:column style="width:3%" property="deptId.deptName"
	title="Employee Department" />
<display:column style="width:2%" title=" ">
	<a href="javascript:initiateProcess(${currentRowObject.id});" >Initiate</a>
</display:column>	