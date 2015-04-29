<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<script src="<egov:url path='js/works.js'/>"></script>
<script src="<egov:url path='js/helper.js'/>"></script>
<script type="text/javascript" src="/egi/script/jsCommonMethods.js"></script>
<link href="<egov:url path='/css/works.css'/>" rel="stylesheet" type="text/css" />
<link href="<egov:url path='/css/commonegov.css'/>" rel="stylesheet" type="text/css" />
<script>
function gotoPage(obj){
	var currRow=getRow(obj); 
	var id = getControlInBranch(currRow,'applicationDetailsId');
	if(obj.value=='View'){	
		window.open("${pageContext.request.contextPath}/citizen/depositWorks!view.action?mode=view&sourcepage=search&appDetailsId="+id.value+"&citizenId=<s:property value='citizenId'/>",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if (obj.value == 'View Technical Details') {
		window.open("${pageContext.request.contextPath}/citizen/bpaRoadCut!view.action?mode=viewTechnicalDetails&sourcepage=search&appDetailsId="+id.value+"&citizenId=<s:property value='citizenId'/>",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if (obj.value == 'Update Technical Details') {
		window.open("${pageContext.request.contextPath}/citizen/bpaRoadCut!view.action?mode=updateTechnicalDetails&sourcepage=search&appDetailsId="+id.value+"&citizenId=<s:property value='citizenId'/>",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(obj.value=='Modify Application'){	
		window.open("${pageContext.request.contextPath}/citizen/depositWorks!edit.action?mode=edit&sourcepage=search&appDetailsId="+id.value+"&citizenId=<s:property value='citizenId'/>",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(obj.value=='Modify'){	
		window.open("${pageContext.request.contextPath}/citizen/depositWorks!edit.action?mode=edit&sourcepage=search&appDetailsId="+id.value+"&citizenId=<s:property value='citizenId'/>",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(obj.value=='Print Damage Fee Communication Letter'){	
		window.open("${pageContext.request.contextPath}/citizen/depositWorks!exportPdf.action?mode=view&sourcepage=damageFeePDF&appDetailsId="+id.value+"&citizenId=<s:property value='citizenId'/>",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(obj.value=='Print Road-Cut Approval Letter'){	 
		window.open("${pageContext.request.contextPath}/citizen/depositWorks!viewRoadCutApprovalPDF.action?appDetailsId="+id.value+"&citizenId=<s:property value='citizenId'/>",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(obj.value=='Print Utilization Certificate'){	
		window.open("${pageContext.request.contextPath}/citizen/depositWorks!viewUtilizationCertificatePDF.action?appDetailsId="+id.value+"&citizenId=<s:property value='citizenId'/>",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(obj.value=='Update Road Cut Date'){	
		window.open("${pageContext.request.contextPath}/citizen/depositWorks!view.action?mode=UpdateRoadCutDate&appDetailsId="+id.value+"&citizenId=<s:property value='citizenId'/>",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	} 
}
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
						<div class="headerplacer"><s:text name='depositworks.citizeninterface.applicationdetails' /> </div>  
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>	

<div>
	<s:if test="%{searchResult.fullListSize != 0}">
		<display:table name="searchResult" pagesize="30"
			uid="currentRow" cellpadding="0" cellspacing="0"
			requestURI=""
			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
			
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Sl.No"
				titleKey="column.title.SLNo"
				style="width:2%;text-align:right" >
					<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
					<s:hidden name="applicationDetailsId" id="applicationDetailsId" value="%{#attr.currentRow.id}" />
			</display:column>
			
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Type Of Cut"
				titleKey="depositworks.roadcut.typeofcut"
				style="width:8%;text-align:left" property="applicationRequest.depositWorksType.name" />
			
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Zone"
				titleKey="depositworks.zone"
				style="width:5%;text-align:left" >
				<s:iterator var="s" value="#attr.currentRow.applicationRequest.roadCutDetailsList" status="status">
					<s:property value="%{zone.name}" />
					<s:if test="!#status.last">,</s:if>
				</s:iterator>
			</display:column>	
					
			
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Ward"
				titleKey="depositworks.ward"
				style="width:5%;text-align:left" >
				<s:iterator var="s" value="#attr.currentRow.applicationRequest.roadCutDetailsList" status="status">
					<s:property value="%{ward.name}" />
					<s:if test="!#status.last">,</s:if>
				</s:iterator>
			</display:column>	
			
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Application Request Number"
				titleKey="depositworks.roadcut.search.applicationno"
				style="width:10%;text-align:left" property="applicationRequest.applicationNo" />
				
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Application Date"
				titleKey="depositworks.date"
				style="width:6%;text-align:left" >
					<s:date name="#attr.currentRow.applicationRequest.applicationDate" format="dd/MM/yyyy" />
			</display:column>
			
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Deposit Code"
				titleKey="depositworks.deposit.code"
				style="width:10%;text-align:left" >
				<s:if test="%{#attr.currentRow.depositCode.code!=''}">
					<s:property value="#attr.currentRow.depositCode.code"/>
				</s:if>
				<s:elseif test="%{#attr.currentRow.applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@BPA}">
					<s:text name='search.appRequest.depositWorksCategory.BPA' />
				</s:elseif>
				<s:else>
				 	<s:text name='search.appRequest.deposit.code.not.generated' />
				</s:else>
			</display:column>
			
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Claim Charges"
				titleKey="depositworks.claimcharges"
				style="width:6%;text-align:right" >
				<s:if test="%{#attr.currentRow.applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@BPA}">
					<s:text name='search.appRequest.depositWorksCategory.BPA' />
				</s:if>
				<s:else>
					<s:text name="contractor.format.number" >
						<s:param name="rate" value="#attr.currentRow.estimatedCost"/>
					</s:text>
				</s:else>
			</display:column>	
			
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Application Status"
				titleKey="depositworks.roadcut.status"
				style="width:10%;text-align:left" property="applicationRequest.egwStatus.description" />
				
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Actions"
				titleKey="depositworks.roadcut.search.actions"
				style="width:10%;text-align:left">
				
				<s:select theme="simple"
						list="%{#attr.currentRow.applicationDetailsActions}"
						name="appDetailsActions" id="appDetailsActions"
						headerValue="--- Select ---"
						headerKey="-1" onchange="gotoPage(this);">
				</s:select>
					
			</display:column>
		</display:table>
	</s:if>	
	<s:elseif test="%{searchResult.fullListSize == 0}">
			<div>
				<table width="100%" border="0" cellpadding="0"
					cellspacing="0">
					<tr>
						<td align="center">
							<font color="red">No Application Request Found</font>
						</td>
					</tr>
				</table>
			</div>
	</s:elseif>
</div>	