<%@ taglib prefix="s" uri="/struts-tags"%>

<style type="text/css">
</style>

<script>

function searchAsset(){
	var a = new Array(2);	
	document.assetPluginForm.assetname.value='';
	document.assetPluginForm.asset.value='';
	window.open("${pageContext.request.contextPath}/assetmaster/asset!showSearchPage.action?assetStatus=revaluated&assetStatus=capitalized","",
	 "height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");	
}

function createAsset(){
	var a = new Array(2);	
	document.assetPluginForm.assetname.value='';
	document.assetPluginForm.asset.value='';
	window.open("${pageContext.request.contextPath}/assetmaster/asset!showCreatePage.action?assetStatus=capitalized","",
	 "height=600,width=600,scrollbars=yes,left=200,top=75,status=yes");	
}

function update(elemValue) {	
	if(elemValue!="" || elemValue!=null) {
		var a = elemValue.split("`~`");
		document.assetPluginForm.assetname.value=a[2];
		document.assetPluginForm.asset.value=a[0];
		document.assetPluginForm.assetname.disabled=true;		
	}
}

</script>
<s:form action="asset" theme="simple" name="assetPluginForm">
<table id="formTable" width="100%" border="0" cellspacing="0"
	cellpadding="0">
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			<table id="asDetailsTable" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td colspan="4" class="headingwk">
						<div class="arrowiconwk">
							<img src="${pageContext.request.contextPath}/image/arrow.gif" />
						</div>
						<div class="headplacer">
							<s:text name='Search PlugIn' />
						</div>
					</td>
				</tr>
				
				<tr>
					<td class="whiteboxwk"><span class="mandatory">*</span>
						<s:text name='Asset'/>: 
					</td>
			        <td class="whitebox2wk" colspan="3">
			        	<input type="text" id="assetname" class="selectboldwk" />
			        		<s:hidden id="asset" name="asset" value="%{asset.id}"/>
			       		<a onclick="searchAsset()" href="#">
			       		<IMG id="img" height=16 src="${pageContext.request.contextPath}/image/magnifier.png" 
			       			width=16 alt="Search" border="0" align="absmiddle"></a>
						<input type="button" class="buttonfinal" value="CREATE ASSET"
							id="createButton" name="button"
							onclick="createAsset()" />
						
			        </td>
			       </tr>
				
			</table>
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>
</table>
</s:form>