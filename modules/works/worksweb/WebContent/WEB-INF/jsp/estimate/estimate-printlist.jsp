<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<script src="<egov:url path='js/works.js'/>"></script>
<script type="text/javascript">

</script> 
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 	
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="9" class="headingwk" align="left">
						<div class="arrowiconwk">
							<img src="${pageContext.request.contextPath}/image/arrow.gif"/>
						</div>
						<s:if test="%{source=='financialdetail'}">
							<div class="headerplacer">
								<s:text name='page.title.financial.detail' />
							</div>
						</s:if>
						<s:elseif test="%{source=='technical sanction'}">
							<div class="headerplacer">
								<s:text name='page.title.Technical.Sanction' />
							</div>
						</s:elseif>
						<s:elseif test="%{source=='Financial Sanction'}">
							<div class="headerplacer">
								<s:text name='page.title.Financial.Sanction' />
							</div>
						</s:elseif>
						<s:elseif test="%{source=='AdministrativeSanction'}">
							<div class="headerplacer">
								<s:text name='page.title.Admin.Sanction' />
							</div>
						</s:elseif>
						<s:elseif test="%{source=='createNegotiation'}">
							<div class="headerplacer">
								<s:text name='page.result.search.estimate' />
							</div>
						</s:elseif>

						<s:else>
							<div class="headerplacer">
								<s:text name='page.title.search.estimates' />
							</div>
						</s:else>
					</td>
				</tr>	
			</table>				
		</td>
	</tr>
</table>	

<display:table name="searchResult" pagesize="30" uid="currentRow" cellpadding="0" cellspacing="0" requestURI="" style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">

	<s:if test="%{source=='financialdetail' || source=='createNegotiation'}">
		<display:column headerClass="pagetableth" class="pagetabletd" title="Select" titleKey="column.title.select" style="width:4%;text-align:center" >
			<input name="radio" type="radio" id="radio" value="<s:property value='%{#attr.currentRow.id}'/>" onClick="setEstimateId(this);" />
		</display:column>
	</s:if>
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="Sl.No" titleKey="estimate.search.slno" style="width:3%;text-align:right" >
		<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
		<s:hidden name="docNumber" id="docNumber" value="%{#attr.currentRow.documentNumber}"/>
		<s:hidden name="estimateIden" id="estimateIden" value="%{#attr.currentRow.id}"/>		
	</display:column>
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="Estimate Number" titleKey="estimate.search.estimateNo" style="width:11%;text-align:left" property="estimateNumber" ></display:column>
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="Executing Department" titleKey="estimate.search.executingdept" style="width:10%;text-align:left" property="executingDepartment.deptName" ></display:column>
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="Name" titleKey="estimate.search.name" style="width:10%;text-align:left" property="name" ></display:column>
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="Status" titleKey='estimate.search.status' style="width:10%;text-align:left">
		<s:if test="%{#attr.currentRow.state.previous.value=='ADMIN_SANCTIONED' || #attr.currentRow.state.previous.value=='CANCELLED'}">
			<s:property value="#attr.currentRow.state.previous.value" />
		</s:if>
		<s:else>
			<s:property value="#attr.currentRow.state.value" />
		</s:else>
	</display:column>
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="Type" titleKey="estimate.search.type" style="width:10%;text-align:left" property="type.name" ></display:column>
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="Estimate Date" titleKey="estimate.search.estimateDate" style="width:7%;text-align:center" >
	<s:date name="#attr.currentRow.estimateDate" format="dd/MM/yyyy" />
	</display:column>
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="Owner" titleKey="estimate.search.owner" style="width:10%;text-align:left" property="positionAndUserName" ></display:column>
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="Total" titleKey="estimate.search.total" style="width:10%;text-align:right" property="totalAmount.formattedString" ></display:column>

</display:table>