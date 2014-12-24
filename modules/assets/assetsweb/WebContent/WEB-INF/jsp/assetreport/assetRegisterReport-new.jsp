<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@page import="java.util.*,
	org.egov.infstr.utils.EgovMasterDataCaching,
		org.egov.lib.admbndry.*,
		org.egov.infstr.commons.Module,
		org.egov.pims.utils.EisManagersUtill,
		org.egov.pims.model.PersonalInformation,org.egov.pims.model.Assignment" %>
				
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
  "http://www.w3.org/TR/html4/loose.dtd">  
  
<html>
	<head> <title>- <s:text name="page.title.assset.register.report" /></title>
		<style type="text/css">

			#yui-dt0-bodytable,#yui-dt1-bodytable,#yui-dt2-bodytable {
				Width: 100%;
			}
		
		</style>
		<link rel="stylesheet" href="<%=request.getContextPath() +"/commonyui/build/treeview/assets/tree.css" %>" type="text/css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() +"/commonyui/build/assets/skins/sam/datatable.css" %>" type="text/css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() +"/commonyui/build/button/assets/button.css" %>" type="text/css">
		<link href="<egov:url path='/css/displaytag.css'/>" rel="stylesheet" type="text/css" />
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
		
		</script>
	</head> 
	<body id="home" >

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
		
		<jsp:include page="../assetmaster/aseetTreeScript.jsp" />
<s:form action="assetRegisterReport.action" theme="simple" name="assetViewForm">
			<div class="errorstyle" id="asset_error" style="display:none;"></div>
			<s:push value="model">
				<div class="navibarshadowwk">
				</div>
				<div class="formmainbox">
					<div class="insidecontent">
						<div class="rbroundbox2">
							<div class="rbtop2"><div></div>
							</div>
							<div class="rbcontent2">
								<div class="datewk">
									<span class="bold">Today</span>
									<egov:now />
								</div>
								
			<table id="asDetailsTable" width="100%" border="0" cellspacing="0"cellpadding="0">
			<tr>
				<td colspan="4" class="headingwk">
					<div class="arrowiconwk">
						<img src="${pageContext.request.contextPath}/image/arrow.gif" />
					</div>
					<div class="headplacer">
						<s:text name='search.criteria' />
					</div>
				</td>
			</tr>
										
			<tr>
			<td colspan="2">
			<table width="100%">
			<tr style="width: 140px; height: 155px;">
			<td width="25%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="asset.cat.type" /></td>
			<td width="25%" class="whitebox2wk"><s:select headerKey=""headerValue="%{getText('list.default.select')}"name="catTypeId" 	
				id="assettype" cssClass="selectwk"list="dropdownData.assetTypeList" listKey="id"listValue='name'
				value="%{catTypeId}"onchange="callsearchScreen(this)" /></td>
			
			</tr>
			<tr>
			<td width="27%" class="greyboxwk"><s:text name="asset.name" /></td>
			<td width="26%" class="greybox2wk" >
			<s:textfield name="name" value="%{name}" id="name" cssClass="selectwk" maxlength="255" />
			</td>
			</tr>
			</table></td>
			<td colspan="2">
			<table width="100%">
			<tr>
				<td width="25%" class="whiteboxwk"><s:text name="asset.cat.tree" /></td>
				<td width="25%" class="whitebox2wk">
					<div id="egovtree" class="treeScrollVertical" align="left" style="width: 140px; height: 150px; background-color: 							#ffffff; overflow: auto;"></div>
				</td>
			</tr>
			<tr>
				<td width="27%" class="greyboxwk"><s:text name="asset.cat.name" /></td>
				<td width="26%" class="greybox2wk"><s:textfield name="assetCategory.name" id="category" readonly="true"
					cssClass="selectboldwk" value="%{assetCategory.name}"/></td>
				<s:hidden id="parentId" name="assetCategory" value="%{assetCategory.id}" />
				<s:hidden id="xmlconfigname" name="xmlconfigname"value="%{xmlconfigname}" />
				<s:hidden id="categoryname" name="categoryname" value="%{categoryname}" />
			</tr>
			</table>
			</td></tr>
			<tr>
				<td width="25%" class="whiteboxwk"> <s:text name="asset.status" /></td>
				<td width="25%" class="whitebox2wk" >
				<s:select headerKey="-1" headerValue="%{getText('list.default.select')}" name="status" id="status"value="%{status.id}" 						cssClass="selectwk" list="dropdownData.statusList" listKey="id" listValue='description'/></td>
				<td width="25%" class="whiteboxwk"><s:text name="asset.description" /></td>
				<td width="25%" class="whitebox2wk">
				<s:textfield name="description" id="description"cssClass="selectboldwk" /></td>
			</tr>
			
			<jsp:include page="../assetmaster/assetLocation.jsp"/>
			<tr>
					<td width="11%" class="whiteboxwk"><s:text name="asset.dept" /></td>
					<td width="21%" class="whitebox2wk" >
						<s:select headerKey="-1" headerValue="%{getText('list.default.select')}" name="department"
							id="department" cssClass="selectwk" list="dropdownData.departmentList" listKey="id" 								listValue='deptName'value="%{department.id}" />
					</td>
				</tr>		
			</table><br>

			<table width="100%">	<tr>
					<td colspan="4" class="shadowwk"></td>
				</tr>
				<tr>
            				<td colspan="4"><div align="right" class="mandatory" style="font-size:11px;padding-right:20px;">* <s:text 						name="default.message.mandatory" />
            				</div></td>
          		</tr>
			</table>
			<table align="center">
			
				<tr>
					<td >
						<div class="buttonholderwk">
						<s:submit cssClass="buttonfinal" value="SEARCH" id="submitButton" method="list" />
						<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="window.close();" />
						</div>
					</td>
				</tr>
			</table>
			
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
				<td colspan="7" class="headingwk">
					<div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
						<div class="headplacer"><s:text name="search.result" /></div>											
						</td>
		</tr>
			<s:if test="%{searchResult.fullListSize != 0}">
			<tr align="center">
				<td>
				<display:table name="searchResult" export="true"  id="searchResultid" uid="currentRowObject" cellpadding="0" cellspacing="0"
				requestURI="" sort="external"  class="its" style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
									
				<display:column  title="Sl.No" style="width:3%;text-align:center">
						<s:property value="%{#attr.currentRowObject_rowNum}"/>
				</display:column>			
					
				<display:column  title="Code"style="width:10%;text-align:center" property="assetCode" />
				<display:column  title="Name"style="width:10%;text-align:center" property="assetName" />	
				<display:column title="Date of Capitalization"style="width:5%;text-align:center" property="capDate" />			
				<display:column  title="Original Cost" style="width:10%;text-align:center" property="capAmount"/>		
				<display:column  title="Accumulated Depreciation" style="width:10%;text-align:center" property="accDep"/>					
				<display:column  title="Written Down Value" style="width:10%;text-align:center" property="writtenDownVal"/>	
				<display:column title="Department" style="width:10%;text-align:center" property="deptName" sortProperty="dept_name"  sortable="true" />				
				<display:column title="Zone" style="width:7%;text-align:center" property="zoneName" sortProperty="zone"  sortable="true" />
				<display:column  title="Ward" style="width:7%;text-align:center" property="wardName" />
				<display:column  title="Street" style="width:7%;text-align:center" property="streetName" />
				<display:column  title="Area" style="width:7%;text-align:center" property="areaName" sortProperty="area"  sortable="true"/>
				<display:column  title="Location" style="width:10%;text-align:center" property="locationName" />		
				<display:column  title="Status"style="width:10%;text-align:center"property="status" sortProperty="description"  sortable="true" />
					<display:caption  media="pdf">
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Asset Register-Report
				   </display:caption>
				        <display:setProperty name="export.pdf" value="true" />
					<display:setProperty name="export.pdf.filename" value="AssetRegister-Report.pdf" /> 
					<display:setProperty name="export.excel" value="true" />
					<display:setProperty name="export.excel.filename" value="AssetRegister-Report.xls"/>	
					<display:setProperty name="export.csv" value="false" />	
					<display:setProperty name="export.xml" value="false" />							
				</display:table>
				  	</td>
				  </tr>
				 	
				 </s:if>
				<s:elseif test="%{searchResult.fullListSize == 0}">
					<tr><td colspan="7" align="center"><font color="red">No record Found.</font></td>
																	
					</tr>
			</s:elseif>
			</table>
			
			</div>
			<div class="rbbot2">
			<div></div>
			</div>
		</div>
	</div>
</div>
				
				
</s:push>
</s:form>
		
</body>
</html>