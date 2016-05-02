<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency,
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It
# 	   is required that all modified versions of this material be marked in
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program
# 	   with regards to rights under trademark law for use of the trade names
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->

<script>

function setupAjaxAssettype(elem){
    populateparentcat({assetType:elem.value});
}
</script>
<%@ include file="/includes/taglibs.jsp" %>
<html>
	<head>
		<s:if test="%{userMode=='view'}">
			<title> <s:text name="page.title.asset.cat.view" /></title>
		</s:if>
		<s:elseif test="%{userMode=='edit'}">
			<title> <s:text name="page.title.asset.cat.edit" /></title>
		</s:elseif>
	</head>
	<body id=home>

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
		<s:form action="assetCategory" theme="simple"
			name="assetCategoryViewForm">
			<div class="errorstyle" id="category_error" style="display:none;"></div>
			<!-- s:push value="model"-->
			<div class="navibarshadowwk">
			</div>
			<div class="formmainbox">
			<div class="insidecontent">
			<div class="rbroundbox2">
			<div class="rbtop2">
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
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img
											src="/egassets/resources/erp2/images/arrow.gif" />
									</div>
									<div class="headplacer">
										<s:text name="search.criteria" />
									</div>
								</td>
							</tr>
							<tr>
								<td width="11%" class="greyboxwk">
									<s:text name="asset.cat.type" />
								</td>
								<td width="21%" class="greybox2wk">
									<s:select headerKey=""
										headerValue="%{getText('list.default.select')}"
										name="assetType" id="assettype"
										cssClass="selectwk" list="dropdownData.assetTypeList"
										onChange="setupAjaxAssettype(this);" />
									<egov:ajaxdropdown id="populateParentcat"
										fields="['Text','Value']" dropdownId='parentcat'
										url='assetcategory/ajaxAssetCategory-populateParentCategories.action'
										selectedValue="%{id}" />
								</td>
								<td width="15%" class="greyboxwk">
									<s:text name="asset.cat.name" />
								</td>
								<td width="53%" class="greybox2wk">
									<s:select headerKey="-1"
										headerValue="%{getText('list.default.select')}" name="id"
										id="parentcat" cssClass="selectwk"
										list="parentMap" value="%{id}" />

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
							<tr>
								<td colspan="4">
									<div class="buttonholderwk">
										<s:submit cssClass="buttonfinal" value="SEARCH" id="submitButton" method="list" />
										<input type="button" class="buttonfinal" value="CLOSE"
											id="closeButton" name="button" onclick="window.close();" />
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="4">&nbsp;</td>
							</tr>
							<tr>
								<td colspan="4">
								<div>
									<table width="100%" border="0" cellspacing="0"
										cellpadding="0">
										<tr>
											<td colspan="6" class="headingwk">
												<div class="arrowiconwk">
													<img
														src="/egassets/resources/erp2/images/arrow.gif" />
												</div>
												<div class="headplacer">
													<s:text name="search.result" />
												</div>
											</td>
										</tr>
										<tr>
											<!--  <td width="10%" class="tablesubheadwk">ID</td>  -->
											<td width="10%" class="tablesubheadwk">
												Sr No.
											</td>
											<td width="15%" class="tablesubheadwk">
												Name
											</td>
											<td width="15%" class="tablesubheadwk">
												Code
											</td>
											<td width="15%" class="tablesubheadwk">
												Type
											</td>
											<td width="15%" class="tablesubheadwk">
												UOM
											</td>
											
										</tr>
										<s:if test="%{dataDisplayStyle=='display'}">
											<table width="100%" border="0" cellspacing="0"
													cellpadding="0">
													<s:iterator id="catIterator" value="assetCategoryList"
														status="row_status">
														<tr>
															<!-- <td width="10%"><s:property value="%{id}" /> </td> -->
															<td width="10%"">
																<s:property value="#row_status.count" />
																&nbsp;
															</td>
															<td width="15%">
																<a
																	href="${pageContext.request.contextPath}/assetcategory/assetCategory-showform.action?
																	id=<s:property value='%{id}'/>&userMode=<s:property value='%{userMode}'/>">
																	<s:property value="%{name}" />
																</a> &nbsp;
															</td>
															<td width="15%"">
																<s:property value="%{code}" />
																&nbsp;
															</td>
															<td width="15%">
																<s:property value="%{assetType}" />
																&nbsp;
															</td>
															<td width="15%">
																<s:property value="%{uom.uom}" />
																&nbsp;
															</td>
															
														</tr>
													</s:iterator>
												</table>
										</s:if>
										<s:elseif test="%{dataDisplayStyle=='noRecords'}">
											<tr>
												<td colspan="6" align="center">
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
			</div>
			</div>
			</div>
			</div>
			<!-- /s:push-->
		</s:form>
	</body>
</html>
