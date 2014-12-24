<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<script>
function validateData(){
	if(document.getElementById('financialYear').value == -1){
		alert("Please select a Financial Year")
		return false;
	}
	if(document.getElementById('budget').value == -1){
		alert("Please select a Budget")
		return false;
	}
	return true;	
}

	var callback = {
	success: function(o){
		if(o.responseText != '')
			document.getElementById('budgetData').innerHTML = o.responseText;
		},
		failure: function(o) {
	    }
	}
	
	function populateBudgets(){
		var finYear = document.getElementById('financialYear');
		if(finYear.value == -1)
			return;
		var bereValue;
		if(document.getElementById('budgetReport_budgetDetail_budget_isbereBE').checked == true)
			bereValue = document.getElementById('budgetReport_budgetDetail_budget_isbereBE').value;
		else
			bereValue = document.getElementById('budgetReport_budgetDetail_budget_isbereRE').value;
		var url = '/EGF/report/budgetReport!ajaxLoadBudgets.action?budgetDetail.budget.financialYear.id='+finYear.value+'&budgetDetail.budget.isbere='+bereValue;
		YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
	}
	
	function exportXls(){
		var finYear =  document.getElementById('financialYear').value;
		var budget =  document.getElementById('budget').value;
		var department =  document.getElementById('executingDepartment').value;
		var budgetGroup =  document.getElementById('budgetGroup').value;
		window.open('/EGF/report/budgetReport!exportXls.action?budgetDetail.budget.financialYear.id='+finYear+'&budgetDetail.budget.id='+budget+'&budgetDetail.budgetGroup.id='+budgetGroup+'&budgetDetail.executingDepartment.id='+department,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	}

	function exportPdf(){
		var finYear =  document.getElementById('financialYear').value;
		var budget =  document.getElementById('budget').value;
		var department =  document.getElementById('executingDepartment').value;
		var budgetGroup =  document.getElementById('budgetGroup').value;
		window.open('/EGF/report/budgetReport!exportPdf.action?budgetDetail.budget.financialYear.id='+finYear+'&budgetDetail.budget.id='+budget+'&budgetDetail.budgetGroup.id='+budgetGroup+'&budgetDetail.executingDepartment.id='+department,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	}
	
</script>
<body>
<div class="formmainbox">
<div class="formheading"></div>
<div class="subheadnew">Working Budget Report</div>

<s:form action="budgetReport" theme="simple" name="budgetReport">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td class="greybox" width="10%">Financial Year:<span class="bluebox"><span class="mandatory">*</span></span></td>
		<td class="greybox">
			<s:select name="budgetDetail.budget.financialYear.id" id="financialYear" list="dropdownData.financialYearList" listKey="id" listValue="finYearRange" headerKey="-1" headerValue="----Choose----" onchange="populateBudgets()" value="%{budgetDetail.budget.financialYear.id}"/>
		</td>
		<td class="greybox" width="10%">Type:<span class="bluebox"><span class="mandatory">*</span></span></td>
		<td class="greybox">
			<s:radio name="budgetDetail.budget.isbere" list="dropdownData.isbereList" value="%{budgetDetail.budget.isbere}" onchange="populateBudgets()"/>
		</td>
	</tr>
	<tr>
	    <td class="bluebox" width="10%">Budget:<span class="bluebox"><span class="mandatory">*</span></span></td>
	    <td class="bluebox">
	    	<div id="budgetData">
	    		<s:select name="budgetDetail.budget.id" id="budget" list="budgetList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----" value="%{budget.id}"/>
	    	</div>
	    </td>
		<td class="bluebox" width="10%">Department:</td>
		<td class="bluebox">
			<s:select  name="budgetDetail.executingDepartment.id" id="executingDepartment" list="dropdownData.executingDepartmentList" listKey="id" listValue="deptName" headerKey="-1" headerValue="----Choose----" value="%{executingDepartment.id}"/>
		</td>
	</tr>
	<tr>
		<td class="greybox" width="10%">Budget Head:</td>
		<td class="greybox">
			<s:select  name="budgetDetail.budgetGroup.id" id="budgetGroup" list="dropdownData.budgetGroupList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----" value="%{budgetGroup.id}"/>
		</td>
		<td class="greybox" width="10%">&nbsp;</td>
		<td class="greybox">&nbsp;</td>
	</tr>
</table>
<br/><br/>
<div class="buttonbottom">
	<s:submit value="Submit" method="generateReport" onclick="return validateData();" cssClass="buttonsubmit"/>
	<s:reset name="button" type="submit" cssClass="button" id="button" value="Cancel"/>
	<s:submit value="Close" onclick="javascript: self.close()" cssClass="button"/>
</div>
</s:form>
</div>
<div>
<s:if test="%{showResults == true}">
<s:if test="%{budgetDetailsList.size()>0}">
<br/>
	<table width="99%" border="0" cellspacing="0" cellpadding="0">
      <tr>
      	<td colspan="7">
      	<div class="subheadsmallnew"><strong>Working Budget Report for <s:property value="budgetDetail.budget.name"/></strong></div>
      	</td>
      	</tr>
      	<tr>
        <td class="blueborderfortd">
		<div>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom">
              <tr>
                <th class="bluebgheadtd" width="2%" >Department Code</th>
                <th class="bluebgheadtd" width="10%" >Function Code</th>
                <th class="bluebgheadtd" width="15%" >Account Head</th>
                <th class="bluebgheadtd" width="10%" >Actuals <s:property value="lastYearRange"/>(Rs) </th>
                <th class="bluebgheadtd" width="10%" >BE <s:property value="currentYearRange"/>(Rs)</th>
               	<th class="bluebgheadtd" width="2%" >RE Proposed <br/><s:property value="currentYearRange"/>(Rs)</th>
                <s:if test="%{canViewREApprovedAmount == true}">
	                <th class="bluebgheadtd" width="2%" >RE Approved <br/><s:property value="currentYearRange"/>(Rs)</th>
				</s:if>            
                <th class="bluebgheadtd" width="10%" >BE Proposed <br/><s:property value="nextYearRange"/>(Rs)</th>
                <s:if test="%{canViewBEApprovedAmount == true}">
                	<th class="bluebgheadtd" width="10%" >BE Approved <br/><s:property value="nextYearRange"/>(Rs)</th>
                </s:if>            
			  </tr>
		<s:iterator value="budgetDetailsList" status="stat" var="p">
			<tr>
				<td class="blueborderfortd"><div align="center"><s:property value="departmentCode"/>&nbsp;</div></td>
				<td class="blueborderfortd"><s:property value="functionCode" /> </a>&nbsp;</td>
				<td class="blueborderfortd"><s:property value="budgetGroupName"/>&nbsp;</td>
				<td class="blueborderfortd"><div align="right">
					<s:if test="%{#p.actualsLastYear != null}">
						<s:text name="format.number"><s:param name="value" value="actualsLastYear"/></s:text>&nbsp;
					</s:if>
				</div></td>
				<td class="blueborderfortd"><div align="right">
					<s:text name="format.number"><s:param name="value" value="beCurrentYearApproved"/></s:text>&nbsp;
				</div></td>
				<td class="blueborderfortd"><div align="right">
					<s:text name="format.number"><s:param name="value" value="reCurrentYearOriginal"/></s:text>&nbsp;
				</div></td>
				<s:if test="%{canViewREApprovedAmount == true}">
					<td class="blueborderfortd"><div align="right">
						<s:text name="format.number"><s:param name="value" value="reCurrentYearApproved"/></s:text>&nbsp;
					</div></td>
				</s:if>
				<td class="blueborderfortd"><div align="right">
					<s:text name="format.number"><s:param name="value" value="beNextYearOriginal"/></s:text>&nbsp;
				</div></td>
				<s:if test="%{canViewBEApprovedAmount == true}">
					<td class="blueborderfortd"><div align="right">
						<s:text name="format.number"><s:param name="value" value="beNextYearApproved"/></s:text>&nbsp;
					</div></td>
				</s:if>
			</tr>
		</s:iterator>
			</table>
			<div class="buttonbottom" align="center">
				Export Options: 
				<label onclick="exportXls()"><a href='javascript:void(0);'>Excel</a></label> 
				| <label onclick="exportPdf()"><a href="javascript:void(0);">PDF</a></label>
			</div>
        </div></td>
      </tr>
	</table></td>
	</tr>
</table>
</s:if>
<s:else>No data found</s:else>
</s:if>
</div>
</body>
</html>
