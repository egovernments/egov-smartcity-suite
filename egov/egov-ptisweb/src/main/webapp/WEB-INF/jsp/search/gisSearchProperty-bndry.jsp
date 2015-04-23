<%@ include file="/includes/taglibs.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html>
	<head>
		<script type="text/javascript">
		function populateWard(){
		populatewardId({zoneId:document.getElementById("zoneId").value});
		document.getElementById("areaId").options.length = 0;
		document.getElementById("areaId").value="-1";
	}
	function populateArea(){
		populateareaId({wardId:document.getElementById("wardId").value});
	}
	function zoomward(obj)
	{
	
	var selWard = obj.options[obj.selectedIndex].value;
	var mgsession='<s:property value="%{SESSION}"/>';
	var gisVer='<s:property value="%{gisVersion}"/>';
	var gisCity='<s:property value="%{gisCity}"/>';
	var gisUrl=gisVer+gisCity;
	
	parent.parent.formFrame.Submit(gisUrl+"/ZoomToWardSelect.jsp?mgSession="+mgsession+"&DomainName="+gisCity+"&wardNum="+selWard,null,"scriptFrame");
	
	}
</script>
	</head>
	<body>
		<div class="formmainbox">
			<s:if test="%{hasErrors()}">
				<div align="left">
					<s:actionerror />
				</div>
			</s:if>
			<table width="320px" border="0">
				<s:form action="gisSearchProperty" name="srchform" theme="simple">
					<div class="headingbg">
						<center>
					<tr>
						<td class="headingbg"
							style="font-weight: bold; font-size: 13px; text-align: center;">
							<s:text name="gissrchbybndry" />
						</td>
					</tr>

					</center>
					</div>
					<tr>

						<td class="greybox">
							<s:text name="Zone" />
							<span class="mandatory1">*</span> :
						</td>
						<td class="greybox">
							<s:select headerKey="-1"
								headerValue="%{getText('default.select')}" name="zoneId"
								id="zoneId" listKey="key" listValue="value"
								list="ZoneBndryMap" cssClass="selectnew"
								onchange="return populateWard();" value="%{zoneId}" />
						</td>
					</tr>
					<tr>

						<egov:ajaxdropdown id="wardId" fields="['Text','Value']"
							dropdownId="wardId" url="common/ajaxCommon!wardByZone.action" />
						<td class="bluebox">
							<s:text name="Ward" />
							:
						</td>
						<td class="bluebox">
							<s:select name="wardId" id="wardId" list="dropdownData.wardList"
								listKey="id" listValue="name" headerKey="-1"
								headerValue="%{getText('default.select')}"
								onchange="return populateArea(),zoomward(this);"
								value="%{wardId}" />
						</td>
						</td>
					</tr>
					<tr>

						<egov:ajaxdropdown id="areaId" fields="['Text','Value']"
							dropdownId="areaId" url="common/ajaxCommon!areaByWard.action" />
						<td class="greybox">
							<s:text name="Area" />
							:
						</td>
						<td class="greybox">
							<s:select name="areaId" id="areaId" list="dropdownData.areaList"
								listKey="id" listValue="name" headerKey="-1"
								headerValue="%{getText('default.select')}" value="%{areaId}" />
						</td>
					</tr>
					<tr>

						<td class="bluebox">


							<s:text name="HouseNo" />
							:
						</td>
						<td class="bluebox">
							<s:textfield name="houseNum" />
						</td>
					</tr>


					<tr>

						<td class="greybox">


							<s:text name="OwnerName" />
							:
						</td>
						<td class="greybox">
							<s:textfield name="ownerName" />
						</td>
					</tr>
					<tr>
						<td>
							<s:hidden name="SESSION" value="%{SESSION}"></s:hidden>
							<s:hidden id="mode" name="mode" value="bndry"></s:hidden>
							<s:submit name="search" value="Search" cssClass="button"
								method="srchByBndry"></s:submit>
						</td>
					</tr>

				</s:form>
			</table>
	</body>
</html>
