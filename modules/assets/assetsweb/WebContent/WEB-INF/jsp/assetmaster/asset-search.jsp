<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="java.util.*,org.egov.infstr.utils.EgovMasterDataCaching" %>
<%@page import="java.util.*,
		org.egov.lib.admbndry.*,
		org.egov.infstr.commons.Module" %>
<%@page import="org.egov.pims.utils.EisManagersUtill,
				org.egov.pims.model.PersonalInformation,org.egov.pims.model.Assignment" %>
				
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
  "http://www.w3.org/TR/html4/loose.dtd">  
  
<html>
	<head>
		<style type="text/css">

		#yui-dt0-bodytable,#yui-dt1-bodytable,#yui-dt2-bodytable {
			Width: 100%;
		}
		
		</style>
		<link rel="stylesheet" href="<%=request.getContextPath() +"/commonyui/build/treeview/assets/tree.css" %>" type="text/css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() +"/commonyui/build/assets/skins/sam/datatable.css" %>" type="text/css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() +"/commonyui/build/button/assets/button.css" %>" type="text/css">
		
		<script type="text/javascript" src="<%=request.getContextPath() +"/commonyui/build/yahoo/yahoo.js"%>"></script>
		<script type="text/javascript" src="<%=request.getContextPath() +"/commonyui/build/dom/dom.js"%>"></script>
		<script type="text/javascript" src="<%=request.getContextPath() +"/commonyui/build/event/event.js"%>"></script>
		<script type="text/javascript" src="<%=request.getContextPath() +"/commonyui/build/element/element-beta.js"%>"></script>
		<script type="text/javascript" src="<%=request.getContextPath() +"/commonyui/build/datasource/datasource-beta.js"%>"></script>
		<script type="text/javascript" src="<%=request.getContextPath() +"/commonyui/build/datatable/datatable-beta.js"%>"></script>
		<script type="text/javascript" src="<%=request.getContextPath() +"/commonyui/build/button/button-beta.js"%>"></script>
		<script type="text/javascript" src="<%=request.getContextPath() +"/commonyui/build/treeview/treeview.js"%>"></script>
		<script type="text/javascript" src="<%=request.getContextPath() +"/commonjs/ajaxCommonFunctions.js"%>"></script>
		<script src="<egov:url path='js/works.js'/>"></script>
		<script src="<egov:url path='js/helper.js'/>"></script>	
		
		<script>
		function  callsearchScreen(obj){
		  	$('category').value ='';
		  	$('parentId').value ='';
		  
		  	if(obj.value == '')
		  		buildMyTree(obj.value,"AssetCategoryName");
		 	else	
		  		buildMyTree(obj.value,"AssetCategoryParent");
		}
				  
		String.prototype.trim = function () {
		    return this.replace(/^\s*/, "").replace(/\s*$/, "");
		}
		
		function setupAjaxAssettype(elem){
		    asset_type_id=elem.options[elem.selectedIndex].value;
		   	populateparentcat({assetTypeId:asset_type_id});
		}
		function setupAjaxWards(elem){
		    zone_id=elem.options[elem.selectedIndex].value;
		    populatewardId({zoneId:zone_id});
		}
		function setupAjaxStreet(elem){
		    ward_id=elem.options[elem.selectedIndex].value;
		    //populateassetarea({wardId:ward_id});
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
			window.open("../assetmaster/asset!parentCategoriesForm.action", "height=650,width=480,scrollbars=yes,left=0,top=0,status=yes");
		}
		
		function assignValues(obj){
			document.getElementById("parentId").value =idValue.trim();
			document.getElementById("categoryName").value =descValue;
			nameValue="";idValue="", descValue = "";
			document.getElementById("categoryName").focus();
		 } 
		</script>
		<s:if test="%{userMode=='view'}">
			<title>- <s:text name="page.title.asset.view" />
			</title>
		</s:if>
		<s:elseif test="%{userMode=='edit'}">
			<title>- <s:text name="page.title.asset.edit" />
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
		<jsp:include page="aseetTreeScript.jsp" />
		<s:form action="asset" theme="simple" name="assetViewForm">
			<div class="errorstyle" id="category_error" style="display: none;"></div>
			<!-- s:push value="model"-->
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
															src="${pageContext.request.contextPath}/image/arrow.gif" />
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
															<td width="11%" class="whiteboxwk"><s:text name="asset.cat.type" />:</td>
															<td width="21%" class="whitebox2wk">
																<s:select headerKey=""
																	headerValue="%{getText('list.default.select')}"
																	name="catTypeId" id="assettype" cssClass="selectwk"
																	list="dropdownData.assetTypeList" listKey="id"
																	listValue='name' value="%{catTypeId}"
																	onchange="callsearchScreen(this)" />
															</td>
															<td colspan="2">
																<table width="100%">
																	<tr>
																		<td width="15%" class="whiteboxwk">
																			<s:text name="asset.cat.tree" />
																			:
																		</td>
																		<td width="53%" class="whitebox2wk">
																			<div id="egovtree" class="treeScrollVertical"
																				align="left"
																				style="width: 140px; height: 150px; background-color: #ffffff; overflow: auto;">
																			</div>
																		</td>
																	</tr>
																	<tr>
																		<td width="15%" class="whiteboxwk">
																			<s:text name="asset.cat.name" />
																			:
																		</td>
																		<td width="21%" class="whitebox2wk">
																			<s:textfield name="category" id="category"
																				value="%{category}" readonly="true"
																				cssClass="selectboldwk" />
																		</td>
																		<s:hidden id="parentId" name="parentId"
																			value="%{parentId}" />
																		<s:hidden id="xmlconfigname" name="xmlconfigname"
																			value="%{xmlconfigname}" />
																		<s:hidden id="categoryname" name="categoryname"
																			value="%{categoryname}" />
																	</tr>
																</table>
															</td>
														</tr>
														<tr>
															<td width="11%" class="greyboxwk">
																<s:text name="asset.department" />
																:
															</td>
															<td width="21%" class="greybox2wk">
																<s:select id="status" name="departmentId"
																	cssClass="selectwk" list="dropdownData.departmentList"
																	listKey="id" listValue="deptName" headerKey=""
																	headerValue="%{getText('list.default.select')}" />
															</td>
															<td width="15%" class="greyboxwk">
																<s:text name="asset.status" />
																:
															</td>
															<td width="53%" class="greybox2wk">
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
																				url='assetmaster/ajaxAsset!populateWard.action' />
																		</td>
																		<td width="11%" class="whiteboxwk"><s:text name="asset.location.ward" /> :</td>
																		<td width="21%" class="whiteboxwk">
																			<s:select id="wardId" name="wardId" cssClass="selectwk" list="dropdownData.wardList"
																				listKey="id" listValue="name" headerKey="" headerValue="%{getText('list.default.select')}" onchange="setupAjaxStreet(this);"/>
							 												<egov:ajaxdropdown id="populateStreetId" fields="['Text','Value']" dropdownId='streetId' url='assetmaster/ajaxAsset!populateStreets.action' selectedValue="%{ward.id}" />
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
																			<egov:ajaxdropdown id="areaId" fields="['Text','Value']" dropdownId="areaId" url="assetmaster/ajaxAsset!populateAreaByLocation.action"  selectedValue="%{area.id}" />
																			<egov:ajaxdropdown id="locationId" fields="['Text','Value']" dropdownId='locationId' url='assetmaster/ajaxAsset!populateLocations.action' selectedValue="%{location.id}" />
																		</td>
																		<td width="11%" class="whiteboxwk"><s:text name="asset.location" />	:</td>
																		<td width="21%" class="whiteboxwk">
																			<s:select headerKey="-1" headerValue="%{getText('list.default.select')}" name="locationId"
																			id="locationId" cssClass="selectwk" list="dropdownData.locationList" listKey="id" listValue='name' value="%{locationId}" onchange="setupAjaxStreetByLocation(this)"/>
																			<egov:ajaxdropdown id="populateStreet2Id" fields="['Text','Value']" dropdownId='street2Id' url='assetmaster/ajaxAsset!populateStreetsByLocation.action'/>
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
														<s:submit cssClass="buttonfinal" value="SEARCH"
															id="submitButton" method="list" />
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
																			src="${pageContext.request.contextPath}/image/arrow.gif" />
																	</div>
																	<div class="headplacer">
																		<s:text name="search.result" />
																	</div>
																</td>
															</tr>
															
															
															<s:if test="%{searchResult.fullListSize != 0}">
															<s:text id="SlNo" name="%{getText('asset.search.sl.no')}"></s:text>
															<s:text id="name" name="%{getText('asset.search.name')}"></s:text>
															<s:text id="CodeNo" name="%{getText('asset.search.code')}"></s:text>
															<s:text id="categ" name="%{getText('asset.search.category')}"></s:text>
															<s:text id="dept" name="%{getText('asset.search.dept')}"></s:text>
															<s:text id="ward" name="%{getText('asset.search.ward')}"></s:text>
															<s:text id="status" name="%{getText('asset.search.status')}"></s:text>
															<tr>
																	<td>
																		<display:table name="searchResult" pagesize="30"
																			uid="currentRow" cellpadding="0" cellspacing="0"
																			requestURI=""
																			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
																							
																			<display:column headerClass="pagetableth"
																				class="pagetabletd" title="${SlNo}"
																				style="width:10%;text-align:left">
																				<s:property
																					value="#attr.currentRow_rowNum + (page-1)*pageSize" />
																			</display:column>

																			<display:column headerClass="pagetableth"
																				class="pagetabletd" title="${CodeNo}"
																				style="width:15%;text-align:left">
																				<a
																					href="${pageContext.request.contextPath}/assetmaster/asset!showform.action?id=<s:property value='%{#attr.currentRow.id}'/>&userMode=<s:property value='%{userMode}'/>">
																					<s:property value="%{#attr.currentRow.code}" /> </a>
																			</display:column>

																			<display:column headerClass="pagetableth"
																				class="pagetabletd" title="${name}"
																				style="width:15%;text-align:left" property="name" />

																			<display:column headerClass="pagetableth"
																				class="pagetabletd" title="${categ}"
																				style="width:15%;text-align:left"
																				property="assetCategory.name" />

																			<display:column headerClass="pagetableth"
																				class="pagetabletd" title="${dept}"
																				style="width:15%;text-align:left"
																				property="department.deptName" />

																			<display:column headerClass="pagetableth"
																				class="pagetabletd" title="${ward}"
																				style="width:15%;text-align:left"
																				property="ward.name" />

																			<display:column headerClass="pagetableth"
																				class="pagetabletd" title="${status}"
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
			<!-- /s:push-->
			<script>
				showRow();
					
			</script>
		</s:form>
	</body>
</html>