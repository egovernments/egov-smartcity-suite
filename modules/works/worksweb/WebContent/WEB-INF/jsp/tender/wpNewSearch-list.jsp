<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<script type="text/javascript">

function setWpId(elem){
	document.workspackageForm.workPackageId.value = elem.value;;
}


</script>
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
							<s:text name='page.result.search.estimate' />
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<display:table name="searchResult" pagesize="30" uid="currentRow"
							cellpadding="0" cellspacing="0" requestURI=""
							style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">

							<display:column headerClass="pagetableth" class="pagetabletd"
								title="Sl.No" titleKey="estimate.search.slno"
								style="width:3%;text-align:right">
								<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
							</display:column>

							<display:column headerClass="pagetableth" class="pagetabletd"
								title="Select" titleKey="column.title.select"
								style="width:3%;text-align:left">
								<input name="radio" type="radio" id="radio"
									value="<s:property value='%{#attr.currentRow.id}'/>"
									onClick="setWpId(this);" />
							</display:column>

							<display:column headerClass="pagetableth" class="pagetabletd"
								title="WorksPackage Date" titleKey="wp.date" 
								style="width:15%;text-align:left;WORD-BREAK:BREAK-ALL" >
								<s:date name="#attr.currentRow.packageDate" format="dd/MM/yyyy" />
							</display:column>

							<display:column headerClass="pagetableth" class="pagetabletd"
								title="Package Number" titleKey="wp.No" style="width:12%;text-align:left"
								property="wpNumber" />

							<display:column headerClass="pagetableth" class="pagetabletd"
								title="Name" titleKey="wp.name" style="width:13%;text-align:left">
								<a
									href="${pageContext.request.contextPath}/tender/worksPackage!edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&sourcepage=search"><s:property
										value='%{#attr.currentRow.name}' /> </a>
							</display:column>

							<display:column headerClass="pagetableth" class="pagetabletd"
								title="Total" titleKey="estimate.search.total"
								style="width:6%;text-align:right">
								<s:text name="contractor.format.number">
									<s:param name="value" value="%{#attr.currentRow.totalAmount}" />
								</s:text>
								<s:hidden name="abEstId" id="abEstId"
									value="%{#attr.currentRow.id}" />
							</display:column>
							
						</display:table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>