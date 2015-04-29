<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<script type="text/javascript">
function setEstimateId(elem){
	document.getElementById("estId").value = elem.value;
}
function createWorkorder(){
	var id = document.getElementById("estId").value;
	if(id!='' ){
		window.open('${pageContext.request.contextPath}/workorder/workOrder!newform.action?estimateId='+id,'_self');
	}
	else{
		dom.get("searchEstimate_error").style.display=''; 
		document.getElementById("searchEstimate_error").innerHTML='<s:text name="createWO.est.not.selected" />';  
		return false;
	  }
	  dom.get("searchEstimate_error").style.display='none';
	  document.getElementById("searchEstimate_error").innerHTML='';
	}
</script>
<div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
			&nbsp;
		</td>
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
							<s:text name='searchEst.result' />
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<s:hidden name="estId" id="estId" />
 <s:if test="%{searchResult.fullListSize != 0 && !hasErrors()}">
		<display:table name="searchResult" pagesize="30" uid="currentRow"
			cellpadding="0" cellspacing="0" requestURI=""
			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Select" titleKey="column.title.select"
				style="width:2%;text-align:left">
				<input name="radio" type="radio" id="radio"
					value="<s:property value='%{#attr.currentRow.id}'/>"
					onClick="setEstimateId(this);" />
			</display:column>			
			<display:column title="Sl.No" titleKey='estimate.search.slno'
				headerClass="pagetableth" class="pagetabletd"
				style="width:4%;text-align:right">
				<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Estimate Number"
				titleKey='searchEst.No'
				style="width:13%;text-align:left">
				<s:property value='#attr.currentRow.estimateNumber' />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Estimate Date" titleKey='searchEst.date' style="width:10%;text-align:center" >
				<s:date name="#attr.currentRow.estimateDate" format="dd/MM/yyyy" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Rate Contract Number"
				titleKey='searchEst.rcNo'
				style="width:13%;text-align:left">
				<s:property value='#attr.currentRow.rateContract.rcNumber' />
			</display:column>				
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Executing Department"
				titleKey='searchEst.execDept'
				style="width:13%;text-align:left">
				<s:property value='#attr.currentRow.executingDepartment.deptName' />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Contractor Name"
				titleKey='searchEst.contractorName'
				style="width:13%;text-align:left">
				<s:property value='#attr.currentRow.rateContract.contractor.name' />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Total(Rs)" titleKey='searchEst.total'
				style="width:9%;text-align:right">
				<s:property value='#attr.currentRow.workValue.formattedString' />
			</display:column>
		</display:table>
		<br />
		<div>
			<table width="100%" border="0" cellpadding="0" 	cellspacing="0">
				<tr>
					<td colspan="4">
						<div  class="buttonholderwk" align="center">
							<input type="button" class="buttonadd" value="Create Work Order" id="createButton" name="button" onclick="createWorkorder()"/>
						</div>	
				   </td>
			    </tr>
			</table>
		  </div>
	</s:if>
 <s:elseif test="%{searchResult.fullListSize == 0 && !hasErrors()}">
		  <div>
			<table width="100%" border="0" cellpadding="0" 	cellspacing="0">
				<tr>
				   <td align="center">
					 <font color="red"><s:text name="search.result.no.record" /></font>
				   </td>
			    </tr>
			</table>
		  </div>
</s:elseif>
	
<div class="errorstyle" id="error_search" style="display: none;"></div>
</div>

