<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ include file="/includes/taglibs.jsp" %>

<script>

function searchAsset(){
	var a = new Array(2);	
	document.assetPluginForm.assetname.value='';
	document.assetPluginForm.asset.value='';
	window.open("${pageContext.request.contextPath}/assetmaster/asset-showSearchPage.action?assetStatus=revaluated&assetStatus=capitalized","",
	 "height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");	
}

function createAsset(){
	var a = new Array(2);	
	document.assetPluginForm.assetname.value='';
	document.assetPluginForm.asset.value='';
	window.open("${pageContext.request.contextPath}/assetmaster/asset-showCreatePage.action?assetStatus=capitalized","",
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
<s:token name="%{tokenName()}"/> 		
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
							<img src="/egassets/resources/erp2/images/arrow.gif" />
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
			       		<IMG id="img" height=16 src="/egassets/resources/erp2/images/magnifier.png" 
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
