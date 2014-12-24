<%@ taglib uri="http://displaytag.sf.net" prefix="display"%> 

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
							<s:text name='revisionEstimate.Revisionestimates.results' />
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
					value="<s:property value='%{#attr.currentRow.id}'/>" />
			</display:column>
			<display:column headerClass="pagetableth"
			   class="pagetabletd" title="Sl No"
			   titleKey="column.title.SLNo"
			   style="width:3%;text-align:left" >
			     <s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
			     <s:hidden name="revEstimateId" id="revEstimateId" value="%{#attr.currentRow.estimate.id}" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Revision Estimate Number" style="width:6%;text-align:left">
			<s:property value='%{#attr.currentRow.estimate.estimateNumber}' />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Contractor Name" titleKey="workorder.search.contractor" style="width:15%;text-align:left">
				<s:property value='%{#attr.currentRow.workOrder.contractor.name}' />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Total" titleKey="estimate.search.Wo.value" style="width:10%;text-align:right">
			<s:text name="contractor.format.number" >
					<s:param name="woAmount" value='%{#attr.currentRow.workOrder.workOrderAmount}' /></s:text>
			</display:column>
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
	<s:if test="%{searchResult.fullListSize != 0 && !hasErrors()}">
		 <P align="center">
				<input type="button" class="buttonadd"
				value="Create Revision Work Order" id="addButton"
				name="createRevisionEstimateButton" onclick="gotoCreateRWO();"
				align="center" />
		</P> 
		</s:if>
</div>
