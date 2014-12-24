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
<%@page import="org.egov.pims.service.EmployeeService,org.egov.pims.utils.EisManagersUtill,
				org.egov.pims.model.PersonalInformation,org.egov.pims.model.Assignment" %>
				
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
  "http://www.w3.org/TR/html4/loose.dtd">
  
<html>
<head>
	<title><s:text name="page.title.asset.search" /></title>
	<style type="text/css">

	#yui-dt0-bodytable,#yui-dt1-bodytable,#yui-dt2-bodytable {
		Width: 100%;
	}
	
	</style>
	<link rel="stylesheet" href="<%=request.getContextPath() +"/css/egov.css" %>" type="text/css">
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
	
	function setupAjaxWards(elem){
	    zone_id=elem.options[elem.selectedIndex].value;
	    populateassetward({zoneId:zone_id});
	}
	
	function assignValues(obj){
		document.getElementById("parentId").value =idValue.trim();
		document.getElementById("categoryName").value =descValue;
		nameValue="";idValue="", descValue = "";
		document.getElementById("categoryName").focus();
	 } 
	 
	function setassetId(elem){
		dom.get("assetId").value = elem.value; 
	}
	
	function showDetails(){
	var id = document.assetSearchForm.assetId.value;
	var projectType = dom.get("projectType").value;
	if(id!='')
		window.open('${pageContext.request.contextPath}/capitaliseAsset/capitaliseAsset!showDetails.action?assetId='+id+"&projectType="+projectType	,'_self');
	else{
		dom.get("page_error").style.display='';
		document.getElementById("page_error").innerHTML='No Asset selected for Capitalisation';
		return false;
	  }
	  dom.get("page_error").style.display='none';
	  document.getElementById("page_error").innerHTML='';
	}
	function filterStatus(obj){
		
		populatestatusId({projectTypeId:obj.value});
	}
	</script>
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
	<jsp:include page="aseetTreeScript.jsp"/>
	<s:form action="capitaliseAsset" theme="simple"
		name="assetSearchForm">
		<div class="errorstyle" id="page_error" style="display:none;"></div>
		<!-- s:push value="model"-->
		<div class="navibarshadowwk">
		</div>
		<div class="formmainbox">
		<div class="insidecontent">
		<div class="rbroundbox2">
		<div class="rbtop2">
		<div></div>
		</div>
		<s:hidden id="assetId" name="assetId" />
		<div class="rbcontent2">
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
									<table>
										<tr>
											<td width="11%" class="whiteboxwk">
												<s:text name="asset.cat.type" />
												:
											</td>
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
															<div id="egovtree" class="treeScrollVertical" align="left"
																style="width:140px;
																 height:150px;
																 background-color:#ffffff;
																 overflow:auto;">
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
														<s:hidden id="parentId" name="parentId"	value="%{parentId}" />
														<s:hidden id="xmlconfigname" name="xmlconfigname" value="%{xmlconfigname}" />
														<s:hidden id="categoryname" name="categoryname" value="%{categoryname}" />
													</tr>
											  </table>
										</td>
										</tr>
										<tr>
											<td width="11%" class="greyboxwk"><span class="mandatory">*</span> 
												<s:text name="asset.project.type" />
												:
											</td>
											<td width="21%" class="greybox2wk">
												<s:select headerKey="-1" name="projectType" id="projectType" 
													headerValue="%{getText('list.default.select')}"
													 cssClass="selectwk"
													list="listProjectType" onchange="filterStatus(this);"
													 />

											</td>
											<egov:ajaxdropdown id="populateStatus"
													fields="['Text','Value']" dropdownId='statusId'
													url='capitaliseAsset/ajaxCapitaliseAsset!populateStatus.action' />
											<td width="15%" class="greyboxwk">
												<s:text name="asset.status" />
												:
											</td>
											<td width="53%" class="greybox2wk">
												<s:select name="statusId"headerKey="-1"
													headerValue="%{getText('list.default.select')}" 													cssClass="selectwk" id="statusId"
													list="dropdownData.statusList" listKey="id"
													listValue='description' multiple="true" size="3"/>
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
											<td width="11%" class="greyboxwk">
												<s:text name="asset.location.zone" />
												:
											</td>
											<td width="21%" class="greybox2wk">
												<s:select id="zoneId" name="zoneId" cssClass="selectwk"
													list="dropdownData.zoneList" listKey="id"
													listValue="name" headerKey="-1"
													headerValue="%{getText('list.default.select')}"
													value="%{zoneId}" onChange="setupAjaxWards(this);" />
												<egov:ajaxdropdown id="populateWard"
													fields="['Text','Value']" dropdownId='assetward'
													url='capitaliseAsset/ajaxCapitaliseAsset!populateWard.action' />
											</td>
											<td width="11%" class="greyboxwk">
												<s:text name="asset.location.ward" />
												:
											</td>
											<td width="21%" class="greybox2wk">
												<s:select id="assetward" name="locationId"
													cssClass="selectwk" list="dropdownData.wardList"
													listKey="id" listValue="name" headerKey=""
													headerValue="%{getText('list.default.select')}" />
											</td>
										</tr>
										<tr>
											<td class="whiteboxwk">
												<s:text name="asset.from.date" />:
											</td>
											<td class="whitebox2wk">
												<s:date name="fromDate" var="fromDateFormat"
													format="dd/MM/yyyy" />
												<s:textfield name="fromDate" id="fromDate"
													cssClass="selectwk" value="%{fromDateFormat}"
													onfocus="javascript:vDateType='3';"
													onkeyup="DateFormat(this,this.value,event,false,'3')" />
												<a
													href="javascript:show_calendar('forms[0].fromDate',null,null,'DD/MM/YYYY');"
													onmouseover="window.status='Date Picker';return true;"
													onmouseout="window.status='';return true;"> <img
														src="${pageContext.request.contextPath}/image/calendar.png"
														alt="Calendar" width="16" height="16" border="0"
														align="absmiddle" />
												</a>

											</td>
											<td width="17%" class="whiteboxwk">
												<s:text name="asset.to.date" />:
											</td>
											<td width="17%" class="whitebox2wk">
												<s:date name="toDate" var="toDateFormat"
													format="dd/MM/yyyy" />
												<s:textfield name="toDate" id="toDate"
													value="%{toDateFormat}" cssClass="selectwk"
													onfocus="javascript:vDateType='3';"
													onkeyup="DateFormat(this,this.value,event,false,'3')" />
												<a
													href="javascript:show_calendar('forms[0].toDate',null,null,'DD/MM/YYYY');"
													onmouseover="window.status='Date Picker';return true;"
													onmouseout="window.status='';return true;"> <img
														src="${pageContext.request.contextPath}/image/calendar.png"
														alt="Calendar" width="16" height="16" border="0"
														align="absmiddle" />
												</a>
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
										</tr>
										<tr>
											<td colspan="4">
												<div align="right" class="mandatory"
													style="font-size: 11px; padding-right: 20px;">
													*
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
												<td colspan="8" class="headingwk">
													<div class="arrowiconwk">
														<img
															src="${pageContext.request.contextPath}/image/arrow.gif" />
													</div>
													<div class="headplacer">
														<s:text name="search.result" />
													</div>
												</td>
											</tr>
											<tr>
												<!--  <td width="10%" class="tablesubheadwk">ID</td>  -->
												<td width="15%" class="tablesubheadwk">
													Select
												</td>
												<td width="15%" class="tablesubheadwk">
													Asset Code
												</td>
												<td width="15%" class="tablesubheadwk">
													Asset Name
												</td>
												<td width="15%" class="tablesubheadwk">
													Asset Category
												</td>
												<td width="15%" class="tablesubheadwk">
													Department
												</td>
												<td width="15%" class="tablesubheadwk">
													Capitalisation Value
												</td>
												<td width="10%" class="tablesubheadwk">
													Status
												</td>
											</tr>
											
											<s:if test="%{assetList.size != 0}">
												<table width="100%" border="0" cellspacing="0"
													cellpadding="0">
													<s:iterator id="assetIterator" value="assetList"
														status="row_status">
														<tr>
															<!-- <td width="10%"><s:property value="%{id}" /> </td> -->
															<td width="4%">
																<input name="radio" type="radio" id="radio"
																	value="<s:property value='%{asset.id}'/>"
																	onClick="setassetId(this);" />
															</td>
															<td width="15%"">
																<s:property value="%{asset.code}" />
																&nbsp;
															</td>
															<td width="15%"">
																<s:property value="%{asset.name}" />
																&nbsp;
															</td>
															<td width="15%"">
																<s:property value="%{asset.assetCategory.name}" />
																&nbsp;
															</td>
															<td width="15%"">
																<s:property value="%{asset.department.deptName}" />
																&nbsp;
															</td>
															<td width="15%"">
																<s:property value="%{capitalisationValue}" />
																&nbsp;
															</td>
															<td width="10%"">
																<s:property value="%{asset.status.description}" />
																&nbsp;
															</td>
														</tr>
													</s:iterator>
												</table>
											</s:if>
											<s:elseif test="%{assetList.size == 0}">
												<tr>
													<td colspan="7" align="center">
														<font color="red">No record Found.</font>
													</td>
												</tr>
											</s:elseif>
										</table>
										<P align="center">
											<input type="button" class="buttonadd"
												value="Capitalise Asset" id="addButton"
												name="capitaliseAssetButton" onclick="showDetails()"
												align="center" />
										</P>
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
	</s:form>
</body>
</html>