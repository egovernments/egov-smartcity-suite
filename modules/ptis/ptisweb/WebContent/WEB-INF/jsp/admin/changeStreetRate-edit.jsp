<%@ include file="/includes/taglibs.jsp"%>

<html>
	<head>
		<title><s:text name="chngLocScrRes"></s:text></title>
		<script type="text/javascript">
			function populateCategoryList(usage, struct){
				populatecategoryList({
					revisedRate: document.getElementById("revisedRate").value, usageFactor: usage, structFactor: struct  
				});						
			}			
		</script>
		<style type="text/css">
			td {
				width: 100px
			}
		</style>
	</head>
	<body>
		<div align="left">
			<s:actionerror />
		</div>
		<s:form name="changeStreetRateEditForm" theme="simple" method="post">
			<div class="formmainbox">
				<div class="formheading"></div>
				<div class="headingbg">
					<s:text name="ChBaseRateAreaHeader" />
				</div>
				<table width="100%" border="0" class="tablebottom">
					<tr>
						<th class="bluebgheadtd">
							<s:text name="useFact"></s:text>
						</th>
						<th class="bluebgheadtd">
							<s:text name="structFact"></s:text>
						</th>
						<th class="bluebgheadtd">
							<s:text name="currRate"></s:text>
						</th>
						<th class="bluebgheadtd">
							<s:text name="currLocFact"></s:text>
						</th>
						<th class="bluebgheadtd">
							<s:text name="revRate"></s:text>
						</th>
						<th class="bluebgheadtd">
							<s:text name="revLocation"></s:text>
						</th>
					</tr>
					<tr>
						<td class="blueborderfortd">
							<s:property value="%{usageFactor}" />
							<s:hidden name="usageFactor"></s:hidden>
						</td>
						<td class="blueborderfortd">
							<s:property value="%{structFactor}" />
							<s:hidden name="structFactor"></s:hidden>
						</td>
						<td class="blueborderfortd" width="100px">
							<s:property value="%{currentRate}" />
							<s:hidden name="currentRate"></s:hidden>
						</td>
						<td class="blueborderfortd">
							<s:property value="%{currLocFactor}" />
							<s:hidden name="currLocFactor"></s:hidden>
						</td>
						<td class="blueborderfortd">
							<s:textfield id="revisedRate" name="revisedRate"
								value="%{revisedRate}" onblur="trim(this,this.value)"
								onchange="populateCategoryList('%{usageFactor}', '%{structFactor}')"></s:textfield>
						</td>
						<egov:ajaxdropdown id="categoryList" fields="['Text','Value']"
							dropdownId="categoryList"
							url="common/ajaxCommon!categoryByRateUsageAndStructClass.action" />
						<td class="blueborderfortd">
							<s:select id="categoryList" list="dropdownData.categoryList" 
								name="revisedLocFactor" headerKey="-1"
								headerValue="%{getText('default.select')}" listKey="id"
								listValue="categoryName">
							</s:select>
						</td>
						<s:hidden name="areaId"></s:hidden>
					</tr>
				</table>
				<div class="buttonsearch" align="center">

					<s:hidden name="saveAction" value="saveData"></s:hidden>
					<s:submit value="Save" method="saveData" cssClass="button">
					</s:submit>
					<s:submit value="Search Results" cssClass="button"
						method="showSearchResults"></s:submit>
					<input type="button" value="Close" class="button"
						onClick="window.close()" />
				</div>


			</div>
		</s:form>
	</body>
</html>