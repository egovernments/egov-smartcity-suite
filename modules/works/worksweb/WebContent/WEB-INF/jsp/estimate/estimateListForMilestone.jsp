<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%> 
<script>

function setWOEstimateId(elem){
	dom.get('woEstimateId').value = elem.value;
	checkMilestone(elem);
}

function checkMilestone(elem){
	var workOrderEstimateId = elem.value;
	

var myMilestoneSuccessHandler = function(req,res) {
                dom.get("searchEstimate_error").style.display='none';
                dom.get("searchEstimate_error").innerHTML='';

				if(res.results[0].value=='true'){
					dom.get("trackMilestoneButton").style.visibility='visible';
					dom.get("createMilestoneButton").style.visibility='hidden';
				}
				else{
					dom.get("createMilestoneButton").style.visibility='visible';
					dom.get("trackMilestoneButton").style.visibility='hidden';
				}
            };
            
var myMilestoneFailureHandler = function() {
	            dom.get("searchEstimate_error").style.display='';
	            document.getElementById("searchEstimate_error").innerHTML='<s:text name="milestone.check.workorder"/>';
	        };
	 <s:if test="%{source=='searchEstimateForMilestone'}">
	 	makeJSONCall(["value"],'${pageContext.request.contextPath}/milestone/ajaxMilestone!checkMilestone.action',{workOrderEstimateId:workOrderEstimateId},myMilestoneSuccessHandler,myMilestoneFailureHandler) ;
	 </s:if>
	 <s:else>
	 	dom.get("trackMilestoneButton").style.visibility='hidden';
		dom.get("createMilestoneButton").style.visibility='hidden';
		dom.get("viewMilestoneButton").style.visibility='visible';
	 </s:else>
}


</script>

<div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr height="5">
		<td></td>
	</tr>
	<tr>
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="9" class="headingwk" align="left">
						<div class="arrowiconwk">
							<img src="${pageContext.request.contextPath}/image/arrow.gif" />
						</div>

						<div class="headerplacer">
							<s:text name='searh.milestone.estimate.header' />
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
     <s:if test="%{searchResult.fullListSize != 0 && !hasErrors()}">
 	     <display:table name="searchResult" pagesize="30"
			uid="currentRow" cellpadding="0" cellspacing="0"
			requestURI=""
			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">

			<display:column headerClass="pagetableth" class="pagetabletd" title="Select" style="width:2%;" titleKey="column.title.select">
				<input name="radio" type="radio" id="radio"
					value="<s:property value='%{#attr.currentRow.id}'/>"
					onClick="setWOEstimateId(this);" />
				<s:hidden name="woEstimateId" id="woEstimateId" />
			</display:column>
	        <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Sl No"
			   titleKey="column.title.SLNo"
			   style="width:3%;text-align:left" >
			     <s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Estimate Number" style="width:6%;text-align:left" >
				<s:property value="#attr.currentRow.estimate.estimateNumber" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Executing Department" titleKey='estimate.search.executingdept' style="width:8%;text-align:left">
				<s:property value="#attr.currentRow.estimate.executingDepartment.deptName" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Name" titleKey='estimate.search.name' style="width:20%;text-align:left" >
				<s:property value="#attr.currentRow.estimate.name" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="<a href='javascript:sortBy();'>Status</a>" titleKey='estimate.search.status' style="width:10%;text-align:left">
				<s:if test="%{#attr.currentRow.estimate.state.previous.value=='ADMIN_SANCTIONED' || #attr.currentRow.estimate.state.previous.value=='CANCELLED'}">
					<s:property value="#attr.currentRow.estimate.state.previous.value" />
				</s:if>
				<s:else>
					<s:property value="#attr.currentRow.estimate.state.value" />
				</s:else>
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Type" titleKey='estimate.search.type' style="width:10%;text-align:left" property='estimate.type.name' />
			<display:column headerClass="pagetableth" class="pagetabletd" title="Estimate Date" titleKey='estimate.search.estimateDate' style="width:10%;text-align:center" >
				<s:date name="#attr.currentRow.estimate.estimateDate" format="dd/MM/yyyy" />
			</display:column>	
			<display:column headerClass="pagetableth" class="pagetabletd" title="WO Number" style="width:10%;text-align:left" property="workOrder.workOrderNumber" />
			<display:column headerClass="pagetableth" class="pagetabletd" title="WO Date" titleKey='estimate.search.estimateDate' style="width:10%;text-align:center" >
				<s:date name="#attr.currentRow.workOrder.workOrderDate" format="dd/MM/yyyy" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" 
				title="WO Value" titleKey="estimate.search.Wo.value"
				style="width:10%;text-align:right"  >
				<s:text name="contractor.format.number" >
					<s:param name="woAmount" value='%{#attr.currentRow.workOrder.workOrderAmount}' /></s:text>
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Project Code" style="width:10%;text-align:left" property="estimate.projectCode.code" />	
	   </display:table> 
	 </s:if> 
	 <s:elseif test="%{searchResult.fullListSize == 0 && !hasErrors()}">
		  <div>
			<table width="100%" border="0" cellpadding="0" 	cellspacing="0">
				<tr>
				   <td align="center">
					 <font color="red"><s:text name="search.result.no.recorod" /></font>
				   </td>
			    </tr>
			</table>
		  </div>
	</s:elseif>   
 </div>
