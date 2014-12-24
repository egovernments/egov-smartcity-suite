<%@ include file="/includes/taglibs.jsp"%>

<html>
	<head>
		<title><s:text name="chngLocScrRes"/></title>
		<script type="text/javascript" src="/javascript/calendar.js">	
		</script>
	</head>
	<body>
		<div class="formmainbox">
			<table width="100%" border="0" class="tablebottom">
				<s:form name="searchResultsForm" theme="simple" method="post">
					<div class="headingsmallbgnew">
							<s:text name="scrhCriteria"></s:text>
						<span class="mandatory"> By Area  </span> /
							<s:text name="totRec"></s:text>
						<span class="mandatory"><s:property
								value="%{readOnlyFields.size}" /> <s:text name="matchRecFound" /></span>
						<div class="searchvalue1">
							<s:text name="scrhVal"></s:text>
							<s:property value="%{searchValue}" />
						</div>
					</div>
				</s:form>
				<s:if test="%{readOnlyFields != null && readOnlyFields.size > 0}">
					<tr>
						<display:table name="readOnlyFields" id="linksTables"
							pagesize="10" export="false" requestURI="" class="tablebottom"
							style="width:100%" uid="currentRowObject">
							<display:column property="usageFactor" title="Usage Factor"
								headerClass="bluebgheadtd" class="blueborderfortd"
								style="text-align:center" />
							<display:column property="structFactor" title="Structural Factor"
								headerClass="bluebgheadtd" class="blueborderfortd"
								style="text-align:center" />
							<display:column property="currentRate" title="Current Rate"
								headerClass="bluebgheadtd" class="blueborderfortd"
								style="text-align:center" />
							<display:column property="currLocFactor"
								title="Current Location Factor" headerClass="bluebgheadtd"
								class="blueborderfortd" style="text-align:center" />
							<display:column title="" headerClass="bluebgheadtd"
								class="blueborderfortd" style="text-align:center">
								<a
									href='../admin/changeStreetRate!editPage.action?usageFactor=
											<s:property value="%{#attr.currentRowObject.usageFactor}"/>&structFactor=
											<s:property value="%{#attr.currentRowObject.structFactor}"/>&currentRate=
											<s:property value="%{#attr.currentRowObject.currentRate}"/>&currLocFactor=
											<s:property value="%{#attr.currentRowObject.currLocFactor}"/>&areaId=
											<s:property value="%{areaId}"/>'>
									edit </a>
							</display:column>
							<display:setProperty name="paging.banner.item" value="Record" />
							<display:setProperty name="paging.banner.items_name"
								value="Records" />
						</display:table>
					</tr>
				</s:if>
				<s:else>
					<tr>
						<td align="center">
							<span class="mandatory"><s:text name="noRecFound"></s:text> </span>
						</td>
					</tr>
				</s:else>
					<div class="buttonsearch" align="center">
							<input value="Search Again" class="buttonsubmit" onclick="window.location='../admin/changeStreetRate!searchForm.action';"/>
							<input type="button" value="Close" class="button" onClick="return confirmClose()"/>
					</div>
			</table>
		</div>
	</body>
</html>