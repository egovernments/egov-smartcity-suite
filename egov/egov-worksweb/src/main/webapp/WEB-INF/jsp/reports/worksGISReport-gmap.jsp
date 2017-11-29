<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>

<%@ include file="/includes/taglibs.jsp" %>
<html>
<head>
	<title><s:text name='gis.report.title' /></title>
</head>
<body onload="loadDependentDDs();">
	<div id="worksGISError" class="errorstyle" style="display:none;"></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle" id="messages">
		 <s:actionerror />
		</div>
	</s:if>
	<s:form name="worksGISForm" action="worksGISReport!search.action" id="worksGISForm" theme="simple" onsubmit="return validateSearch();">
		<div class="formmainbox">
			<div class="insidecontent">
			<div id="printContent" class="rbroundbox2">
			<div class="rbtop2">
			<div></div>
			</div>
				<div class="rbcontent2">
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
									<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img src="/egworks/resources/erp2/images/arrow.gif" />
									</div>
									<div class="headplacer">
										<s:text name="page.subheader.search.estimate" />
									</div>
									</td>
								</tr>
								<tr>
									<td class="whiteboxwk">
										<span class="mandatory">*</span><s:text name="estimate.zone" />:
									</td>
									<td class="whitebox2wk">
										<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="zoneId"
											id="zoneId" cssClass="selectwk" list="dropdownData.zoneList" listKey="id"
											listValue="name"  onChange="setupWards(this);" />
										<egov:ajaxdropdown id="wardsDropDown" fields="['Text','Value']" dropdownId='wardId'
											url='reports/ajaxWorksGISReport!ajaxLoadWardsByZone.action' selectedValue="%{zoneId}" />
									</td>
									<td class="whiteboxwk">
										<s:text name="gis.estimate.ward" />:
									</td>
									<td class="whitebox2wk">
										<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="wardId"
											id="wardId" cssClass="selectwk" list="dropdownData.wardList" listKey="id"
											listValue="name" />
									</td>
								</tr>
								<tr>
									<td class="greyboxwk">
										<span class="mandatory">*</span><s:text name="estimate.work.nature" />:
									</td>
									<td class="greybox2wk">
										<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="expenditureType"
											id="expenditureType" cssClass="selectwk" list="dropdownData.typeList" listKey="id" listValue="name"
											value="%{expenditureType}" />
									</td>
									<td class="greyboxwk">
										<s:text name="estimate.number" />:
									</td>
									<td class="greybox2wk">
										<div class="yui-skin-sam">
											<div id="estimateNumberSearch_autocomplete">
												<div>
													<s:textfield id="estimateNumberSearch" name="estimatenumber"  value="%{estimatenumber}" cssClass="selectwk" />
												</div>
												<span id="estimateNumberSearchResults"></span>
											</div>	
										</div>
										<egov:autocomplete name="estimateNumberSearch" width="20" 
											field="estimateNumberSearch" url="ajaxWorksGISReport!searchEstimateNumber.action" 
											queryQuestionMark="false" results="estimateNumberSearchResults" 
											handler="estimateNumberSearchSelectionHandler" queryLength="3"/>	
									</td>
								</tr>
								<tr>
									<td class="whiteboxwk">
									<s:text name="estimate.work.type" />:
									</td>
									<td class="whitebox2wk">
										<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="parentCategory"
											id="parentCategory" cssClass="selectwk" list="dropdownData.parentCategoryList" listKey="id"
											listValue="description" value="%{parentCategory}" onChange="setupSubTypes(this);" />
										<egov:ajaxdropdown id="categoryDropdown" fields="['Text','Value']" dropdownId='category'
											url='estimate/ajaxEstimate!subcategories.action' selectedValue="%{category.id}" />
									</td>
									<td class="whiteboxwk">
										<s:text name="estimate.work.subtype" />:
									</td>
									<td class="whitebox2wk">
										<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="category"
											value="%{category}" id="category" cssClass="selectwk" list="dropdownData.categoryList" listKey="id"
											listValue="description" />
									</td>
								</tr>
								<tr>
									<td class="greyboxwk">
										<s:text name="gis.contractorname" />:
									</td>
									<td class="greybox2wk"  colspan="3">
										<s:select id="contractor" name="contractorId"
											cssClass="selectwk"
											list="%{contractorForApprovedWorkOrder}"
											headerKey="-1"															
											headerValue="%{getText('default.dropdown.select')}" 
											value="%{contractorId}"/>
									</td>
								</tr>
								<tr></tr>
								<tr>
									<td colspan="4">
										<div class="buttonholdersearch">
											<s:hidden name="reportType" id="reportType" value="workwise" />
											<s:submit cssClass="buttonadd" value="SEARCH" id="saveButton" name="button"   method="search" onClick="validateSearch()" />
											&nbsp;&nbsp;&nbsp;
											<input type="button" class="buttonfinal" value="CLEAR" id="button" name="clear" onclick="this.form.reset();">
											&nbsp;&nbsp;&nbsp;
											<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button"
												onclick="window.close();" />
										</div>
									</td>
								</tr>
								<tr>
									
								</tr>
								<tr><td>&nbsp;</td></tr>
								<tr id="resultRow">
									<s:if test="%{locationList.size()>=1}">
										<td colspan="4" style="height:1000px;width:1200px;">
											<b>&nbsp;&nbsp;&nbsp;MAP LEGEND:&nbsp;&nbsp;&nbsp;Roads - <img src="http://www.google.com/mapfiles/ms/icons/orange-dot.png" />
											Bridges - <img src="http://www.google.com/mapfiles/ms/icons/purple-dot.png" />
											Electrical - <img src="http://www.google.com/mapfiles/ms/icons/green-dot.png" />
											Buildings - <img src="http://www.google.com/mapfiles/ms/icons/yellow-dot.png" />
											Storm water drain - <img src="http://www.google.com/mapfiles/ms/icons/blue-dot.png" />
											Others - <img src="http://www.google.com/mapfiles/ms/icons/red-dot.png" /></b>
											<div id="map_canvas" style="position: static;"></div>
												<c:import url="/commons/googleMapReport.jsp" context="/egi">
												<c:param name="latitude"  value="<%= AppConfigTagUtil.getAppConfigValue("CITY_LATITUDE","egi",this.getServletContext()) %>"></c:param>
												<c:param name="longitude"  value="<%= AppConfigTagUtil.getAppConfigValue("CITY_LONGITUDE","egi",this.getServletContext()) %>"></c:param>
												<c:param name="replaceMarker" value="<%= Boolean.FALSE.toString() %>"></c:param> 
											</c:import>
										</td>
									</s:if>
									<s:else>
										<td colspan="4" >
											<s:if test="%{resultStatus=='afterSearch'}">
											<div align="center"><font color="red">No Records Found.</font></div>
											</s:if>
											<s:else>&nbsp;</s:else>
										</td>	
									</s:else>
								</tr>
								<tr><td colspan="4">&nbsp;</td></tr>
								<tr>
									<td colspan="4">
										<div align="right" class="mandatory" style="font-size: 11px; padding-right: 20px;">
											*<s:text name="default.message.mandatory" />
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					</table>
				</div>
			</div>
			</div>
		</div>
		<div id="codescontainer"></div>
	</s:form>
	<script type="text/javascript">
	
		function setupSubTypes(elem){
			categoryId=elem.options[elem.selectedIndex].value;
			populatecategory({category:categoryId});
		}	
		  
		function setupWards(elem){
			zone=elem.options[elem.selectedIndex].value;
			populatewardId({zoneId:zone});
		}
		function loadWaitingImage() {
			if(document.getElementById('loading')){
				document.getElementById('loading').style.display='block';
			}
			if(document.getElementById('resultRow')){
				document.getElementById('resultRow').style.display='none';
			}
			
		}
		
		function validateSearch()
		{
			if(document.getElementById("expenditureType").value==null || document.getElementById("expenditureType").value==-1
					|| document.getElementById("zoneId").value==null || document.getElementById("zoneId").value==-1)
			{
				dom.get("worksGISError").style.display='';
		        dom.get("worksGISError").innerHTML='<s:text name="gis.select.zone.natureofwork" />';
		        window.scroll(0,0);
		        return false;
			}
			else
			{
				dom.get("worksGISError").style.display='none';
		        dom.get("worksGISError").innerHTML='';
		        return true;	
			}		
		}
		var estimateNumberSearchSelectionHandler = function(sType, arguments){ 
			var oData = arguments[2];
		};
		function loadDependentDDs()
		{
			<s:if test="%{zoneId!=null && zoneId!=-1 && zoneId!=''}">
				setupWards(dom.get("zoneId"));
			</s:if>
			<s:if test="%{parentCategory!=null && parentCategory!=-1 && parentCategory!=''}">
				setupSubTypes(dom.get("parentCategory"));
			</s:if>
		}
	</script>
</body>
</html>
