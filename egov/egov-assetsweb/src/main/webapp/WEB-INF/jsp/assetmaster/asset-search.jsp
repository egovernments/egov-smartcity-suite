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

<style type="text/css">

	#yui-dt0-bodytable,#yui-dt1-bodytable,#yui-dt2-bodytable {
		Width: 100%;
	}

</style>

<script>
		  
String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}

function setUpSubCategories(elem){
    asset_cat_id=elem.options[elem.selectedIndex].value;
    populatesubCategoryIds({parentCatId:asset_cat_id});
}

function setupAjaxWards(elem){
    zone_id=elem.options[elem.selectedIndex].value;
    populatewardId({zoneId:zone_id});
}
function setupAjaxStreet(elem){
    ward_id=elem.options[elem.selectedIndex].value;
    populatestreetId({wardId:ward_id});
}
function setupAjaxStreetByLocation(elem){
	location_id=elem.options[elem.selectedIndex].value;
    populatestreet2Id({locationId:location_id});
}
function setupAjaxArea(elem){
    area_id=elem.options[elem.selectedIndex].value;
    populatelocationId({areaId:area_id});
}
function showRow(){
	if(document.getElementById('asset_searchBy1').checked == true)
	{
		document.getElementById('zoneRow').style.display='block';
		document.getElementById('areaRow').style.display='none';
	}
	else
	{
		document.getElementById('zoneRow').style.display='none';
		document.getElementById('areaRow').style.display='block';
		if(document.getElementById('areaId').value==-1)
			populateareaId();
	}
}
function setupAjaxPopup(){
	window.open("../assetmaster/asset-parentCategoriesForm.action", "height=650,width=480,scrollbars=yes,left=0,top=0,status=yes");
}

function assignValues(obj){
	document.getElementById("parentId").value =idValue.trim();
	document.getElementById("categoryName").value =descValue;
	nameValue="";idValue="", descValue = "";
	document.getElementById("categoryName").focus();
 } 


function validateFormAndSubmit(){

	if(document.getElementById('assettype').value=="-1" && document.getElementById('parentCategoryId').value==-1 && document.getElementById('subCategoryIds').value=="" &&
			document.getElementById('status').value=="" && document.getElementById('asset_statusId').value=="" && document.getElementById('code').value=="" && 
			document.getElementById('description').value=="" )
		{	
			if(document.getElementById('asset_searchBy1').checked == true){ 
				if(document.getElementById('zoneId').value==-1 && document.getElementById('wardId').value=="" && document.getElementById('streetId').value==-1)
				{	
					document.getElementById("category_error").innerHTML='<s:text name="view.asset.search.null" />'; 
			        document.getElementById("category_error").style.display='';
					return false;
				}
			}
			else{
				if(document.getElementById('areaId').value==-1 && document.getElementById('locationId').value==-1 && document.getElementById('street2Id').value==-1)
					{
						document.getElementById("category_error").innerHTML='<s:text name="view.asset.search.null" />'; 
				        document.getElementById("category_error").style.display='';
						return false;
					}
				}
		}
	else
	{
		document.getElementById("category_error").innerHTML='';
		document.getElementById("category_error").style.display="none";
		
	}
	document.assetViewForm.action='${pageContext.request.contextPath}/assetmaster/asset-list.action';
	document.assetViewForm.submit();
}
</script>
<html>
	<head>
		<s:if test="%{userMode=='view'}">
			<title> <s:text name="page.title.asset.view" />
			</title>
		</s:if>
		<s:elseif test="%{userMode=='edit'}">
			<title> <s:text name="page.title.asset.edit" />
			</title>
		</s:elseif>
	</head>
	<body>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:if test="%{hasActionMessages()}">
			<div class="messagestyle">
				<s:actionmessage theme="simple" />
			</div>
		</s:if>

		<s:form action="asset" theme="simple" name="assetViewForm">
			<div class="errorstyle" id="category_error" style="display: none;"></div>
			<div class="navibarshadowwk">
			</div>
			<div class="formmainbox">
				<div class="insidecontent">
					<div class="rbroundbox2">
						<div class="rbtop2">
							<div></div>
						</div>
						<div class="rbcontent2">
							<s:hidden name="userMode" />
																		
							<table id="formTable" width="100%" border="0" cellspacing="0"
								cellpadding="0">
								<tr>
									<td>
										&nbsp;
									</td>
								</tr>
								<tr>
									<td>
										<table id="catSearchTable" width="100%" border="0"
											cellspacing="0" cellpadding="0">
											<tr>
												<td colspan="3" class="headingwk">
													<div class="arrowiconwk">
														<img
															src="/egassets/resources/erp2/images/arrow.gif" />
													</div>
													<div class="headplacer">
														<s:text name="search.criteria" />
													</div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<table width="100%">
											<tr>
												<td>
													<table cellspacing="0" cellpadding="0">
														<tr>
															<td width="11%" class="greyboxwk"><s:text name="asset.cat.type" />:</td>
															<td width="21%" class="greybox2wk">
																<s:select headerKey="-1"
																	headerValue="%{getText('list.default.select')}"
																	name="assetType" id="assettype"
																	cssClass="selectwk" list="dropdownData.assetTypeList"
																	value="%{assetType}"
																 />
																
															</td>
															<td colspan="2" class="greyboxwk" />
															
														</tr>
														<tr>
															<td width="11%" class="whiteboxwk"><s:text name="asset.parent.category" />:</td>
															<td class="whitebox2wk"><s:select headerKey="-1"
																	headerValue="%{getText('list.default.select')}" name="parentCategoryId"
																	id="parentCategoryId" cssClass="selectwk"
																	list="dropdownData.parentCatList" value="%{parentCategoryId}"
																	listKey="id" listValue='name'
																	 onchange="setUpSubCategories(this);"/>
																	<egov:ajaxdropdown id="populateSubcat"
																	fields="['Text','Value']" dropdownId='subCategoryIds'
																	url='assetmaster/ajaxAsset-populateSubCategories.action'
																	selectedValue="%{parentCategoryId}" />
															</td>
															<td width="11%" class="whiteboxwk"><s:text name="asset.sub.category" />:</td>
															<td class="whitebox2wk">
																<s:select headerKey="-1" multiple="true" size="6" headerValue="%{getText('list.default.select')}" name="subCategoryIds" 
																	id="subCategoryIds" cssClass="selectwk" list="dropdownData.subCategoriesList" 
																	listKey="id" listValue='name' value="%{subCategoryIds}" />
															</td>
															
															
														</tr>
														<tr>
															<td width="11%" class="greyboxwk">
																<s:text name="asset.department" />
																:
															</td>
															<td width="19%" class="greybox2wk">
																<s:select id="status" name="departmentId"
																	cssClass="selectwk" list="dropdownData.departmentList"
																	listKey="id" listValue="name" headerKey=""
																	headerValue="%{getText('list.default.select')}" />
															</td>
															<td width="10%" class="greyboxwk">
																<s:text name="asset.status" />
																:
															</td>
															<td width="60%" class="greybox2wk">
																<s:select name="statusId" cssClass="selectwk"
																	list="dropdownData.statusList" listKey="id"
																	listValue='description' multiple="true" size="3" />
															</td>
														</tr>

														<tr>
															<td width="11%" class="whiteboxwk">
																<s:text name="asset.code" />
																:
															</td>
															<td width="21%" class="whitebox2wk">
																<s:textfield name="code" id="code"
																	cssClass="selectboldwk" />
															</td>
															<td width="15%" class="whiteboxwk">
																<s:text name="asset.description" />
																:
															</td>
															<td width="53%" class="whitebox2wk">
																<s:textfield name="description" id="description"
																	cssClass="selectboldwk" />
															</td>
														</tr>
														<tr>
															<td width="11%" class="greyboxwk"><s:text name="asset.search.searchby" /> :</td>
															<td width="21%" class="greybox2wk">
																<s:radio name="searchBy" list="#{'1':'Zone','2':'Area'}" value="%{searchBy}" onchange="showRow()"/> 
															</td>
															<td colspan="2"  class="greyboxwk"/>
														</tr>
														<tr>
															<td colspan="4">
																<table  cellspacing="0" cellpadding="0" >
																	<tr id="zoneRow"  style="display:visible">
																		<td width="11%" class="whiteboxwk"><s:text name="asset.location.zone" /> :</td>
																		<td width="21%" class="whiteboxwk">
																			<s:select id="zoneId" name="zoneId" cssClass="selectwk"
																				list="dropdownData.zoneList" listKey="id" listValue="name" headerKey="-1"
																				headerValue="%{getText('list.default.select')}" value="%{zoneId}" onchange="setupAjaxWards(this);" />
																			<egov:ajaxdropdown id="populateWardId" fields="['Text','Value']" dropdownId='wardId'
																				url='assetmaster/ajaxAsset-populateWard.action' />
																		</td>
																		<td width="11%" class="whiteboxwk"><s:text name="asset.location.ward" /> :</td>
																		<td width="21%" class="whiteboxwk">
																			<s:select id="wardId" name="wardId" cssClass="selectwk" list="dropdownData.wardList"
																				listKey="id" listValue="name" headerKey="" headerValue="%{getText('list.default.select')}" onchange="setupAjaxStreet(this);"/>
							 												<egov:ajaxdropdown id="populateStreetId" fields="['Text','Value']" dropdownId='streetId' url='assetmaster/ajaxAsset-populateStreets.action' selectedValue="%{ward.id}" />
																		</td>
																		<td width="11%" class="whiteboxwk"><s:text name="asset.location.street" /> :</td>
																		<td width="21%" class="whiteboxwk">
																			<s:select headerKey="-1" headerValue="%{getText('list.default.select')}" name="streetId"
																				id="streetId" cssClass="selectwk"	list="dropdownData.streetList" listKey="id" listValue='name' selectedValue="%{street.id}" />
																		</td>
																	</tr>
																	<tr id="areaRow" style="display:none">
																		<td width="11%" class="whiteboxwk"><s:text name="asset.location.area" /> :</td>
																		<td width="21%" class="whiteboxwk">
																			<s:select headerKey="-1" headerValue="%{getText('list.default.select')}" name="areaId" id="areaId" cssClass="selectwk"
																			list="dropdownData.areaList" listKey="id" listValue='name' onchange="setupAjaxArea(this);"  value="%{areaId}"/>
																			<egov:ajaxdropdown id="areaId" fields="['Text','Value']" dropdownId="areaId" url="assetmaster/ajaxAsset-populateAreaByLocation.action"  selectedValue="%{area.id}" />
																			<egov:ajaxdropdown id="locationId" fields="['Text','Value']" dropdownId='locationId' url='assetmaster/ajaxAsset-populateLocationsByArea.action' selectedValue="%{location.id}" />
																		</td>
																		<td width="11%" class="whiteboxwk"><s:text name="asset.location" />	:</td>
																		<td width="21%" class="whiteboxwk">
																			<s:select headerKey="-1" headerValue="%{getText('list.default.select')}" name="locationId"
																			id="locationId" cssClass="selectwk" list="dropdownData.locationList" listKey="id" listValue='name' value="%{locationId}" onchange="setupAjaxStreetByLocation(this)"/>
																			<egov:ajaxdropdown id="populateStreet2Id" fields="['Text','Value']" dropdownId='street2Id' url='assetmaster/ajaxAsset-populateStreetsByLocation.action'/>
																		</td>
																		<td width="11%" class="whiteboxwk"><s:text name="asset.location.street" /> :</td>
																		<td width="21%" class="whiteboxwk">
																			<s:select headerKey="-1" headerValue="%{getText('list.default.select')}" name="street2Id"
																				id="street2Id" cssClass="selectwk" list="dropdownData.street2List" listKey="id" listValue='name' value="%{street2Id}" />
																		</td>
																	</tr>
																</table>
															</td>
														</tr>
														<tr>
															<td colspan="4">
																<div align="right" class="mandatory" style="font-size: 11px; padding-right: 20px;">	*
																	<s:text name="default.message.mandatory" />
																</div>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td colspan="4">
													<div class="buttonholderwk">
														<input type="submit" class="buttonfinal" value="SEARCH" id="submitButton" 
															onclick="return validateFormAndSubmit();"/>
														<input type="button" class="buttonfinal" value="CLOSE"
															id="closeButton" name="button" onclick="window.close();" />
													</div>
												</td>
											</tr>
											<tr>
												<td colspan="4">
													&nbsp;
												</td>
											</tr>
											<tr>
												<td colspan="4">
													<div>
														<table width="100%" border="0" cellspacing="0"
															cellpadding="0">
															<tr>
																<td colspan="7" class="headingwk">
																	<div class="arrowiconwk">
																		<img
																			src="/egassets/resources/erp2/images/arrow.gif" />
																	</div>
																	<div class="headplacer">
																		<s:text name="search.result" />
																	</div>
																</td>
															</tr>

															<s:if test="%{searchResult.fullListSize != 0}">
																<tr>
																	<td>
																		<display:table name="searchResult" pagesize="30"
																			uid="currentRow" cellpadding="0" cellspacing="0"
																			requestURI=""
																			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">

																			<display:column headerClass="pagetableth"
																				class="pagetabletd" title="Sl.No"
																				style="width:10%;text-align:left">
																				<s:property
																					value="#attr.currentRow_rowNum + (page-1)*pageSize" />
																			</display:column>

																			<display:column headerClass="pagetableth"
																				class="pagetabletd" title="Code"
																				style="width:15%;text-align:left">
																				<a
																					href="${pageContext.request.contextPath}/assetmaster/asset-showform.action?id=<s:property value='%{#attr.currentRow.id}'/>&userMode=<s:property value='%{userMode}'/>">
																					<s:property value="%{#attr.currentRow.code}" /> </a>
																			</display:column>

																			<display:column headerClass="pagetableth"
																				class="pagetabletd" title="Name"
																				style="width:15%;text-align:left" property="name" />

																			<display:column headerClass="pagetableth"
																				class="pagetabletd" title="Category"
																				style="width:15%;text-align:left"
																				property="assetCategory.name" />

																			<display:column headerClass="pagetableth"
																				class="pagetabletd" title="Department"
																				style="width:15%;text-align:left"
																				property="department.name" />

																			<display:column headerClass="pagetableth"
																				class="pagetabletd" title="Ward"
																				style="width:15%;text-align:left"
																				property="ward.name" />

																			<display:column headerClass="pagetableth"
																				class="pagetabletd" title="Status"
																				style="width:15%;text-align:left"
																				property="status.description" />

																		</display:table>
																	</td>
																</tr>
															</s:if>
															<s:elseif test="%{searchResult.fullListSize == 0}">
																<tr>
																	<td colspan="7" align="center">
																		<font color="red">No record Found.</font>
																	</td>
																</tr>
															</s:elseif>
														</table>
													</div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</div>
						<div class="rbbot2">
							<div></div>
						</div>
					</div>
				</div>
			</div>
			<script>
				showRow();
			</script>
		</s:form>
	</body>
</html>
