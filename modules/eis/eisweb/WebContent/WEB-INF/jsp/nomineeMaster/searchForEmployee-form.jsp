<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<script type="text/javascript">
var codeSelectionHandler = function(sType, arguments)
    { 
	    var oData = arguments[2];
	 	var empDetails = oData[0];
	 	var empCode = empDetails.split('~')[0];
	 	var empName = empDetails.split('~')[1];
	 	document.getElementById("employeeCode").value = empCode;
	 	document.getElementById("employeeName").value = empName;
	 	employeeName=empName;
	 	employeeCode=empCode;
 	}
    var codeSelectionEnforceHandler = function(sType, arguments) {
        alert("codeselectionenforce");
      		warn('impropercodeSelection');
  	}

</script>
	<s:push value="model">
	
		<tr>
		 
			<td class="whiteboxwk">Designation</td>
			<td class="whitebox2wk">
			<s:select name="designationId" id="designationId" list="dropdownData.designationMasterList" listKey="designationId" 
			listValue="designationName" headerKey="-1" headerValue="----Select----"  value="%{desigId.designationId}"/> </td>
			
			<td class="whiteboxwk"><egovtags:filterByDeptMandatory/>Department</td>
			<td class="whitebox2wk">
			<s:select name="deptId" id="deptId" list="dropdownData.DepartmentImplList" listKey="id" 
			listValue="deptName" headerKey="-1" headerValue="----Select----"  value="%{deptId.id}"/> </td>
			
		</tr>
		
		<tr>
		<td  class="greyboxwk">Employee Code</td>
   			 <td  class="greybox2wk" width="20%" valign="top" >  	
	  			<div class="yui-skin-sam">
		    		<div id="empSearch_autocomplete" class="yui-ac" >
						<input type="text" id="employeeCode" name="employeeCode" class="selectwk" >
						<div id="codeSearchResults"></div>
					</div>
				</div>		
				
			<egovtags:autocomplete name="employeeCode"  field="employeeCode" 
		   	    	url="${pageContext.request.contextPath}/common/employeeSearch!getActiveEmpListByEmpCodeLike.action" queryQuestionMark="true"  results="codeSearchResults" 
		   	    	handler="codeSelectionHandler" forceSelectionHandler="codeSelectionEnforceHandler"/>
		   	    <span class='warning' id="impropercodeSelectionWarning"></span>	
		</td>
		<td  class="greyboxwk">Employee Name</td>
      	<td  class="greybox2wk" >
  		<input type="text" id="employeeName" name="employeeName" class="selectwk">
		</tr>
		
		<tr>
		<td class="whiteboxwk">Status</td>
			<td class="whitebox2wk">
			<s:select name="employeeStatus" id="employeeStatus" list="dropdownData.EgwStatusList" listKey="id" 
			listValue="description" headerKey="-1" headerValue="----Select----"  value="%{employeeStatus.id}"/>
			</td>
			
			<td class="whiteboxwk">functionary</td>
			<td class="whitebox2wk">
			<s:select name="functionary" id="functionary" list="dropdownData.functionaryList" listKey="id" 
			listValue="name" headerKey="-1" headerValue="----Select----"  />
			</td>
		</tr>
		<tr>
		<td class="greyboxwk">Type</td>
			<td class="greybox2wk" colspan="3">
			<s:select name="employeeType" id="employeeType" list="dropdownData.employeeTypeList" listKey="id" 
			listValue="name" headerKey="-1" headerValue="----Select----"  value="%{employeeType.id}"/>
			</td>
			
		</tr>
		
		 <tr>
            
          </tr>
	</s:push>
