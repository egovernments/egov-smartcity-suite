<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<html>
	<head>
		<s:if test="%{userMode=='view'}">
			<title>- <s:text name="page.title.asset.cat.view" /></title>
		</s:if>
		<s:elseif test="%{userMode=='edit'}">
			<title>- <s:text name="page.title.asset.cat.edit" /></title>
		</s:elseif>
		<script>

			function setupAjaxAssettype(elem){
	    		asset_type_id=elem.options[elem.selectedIndex].value;
	    		populateparentcat({assetTypeId:asset_type_id});
			}
		</script>
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
											src="${pageContext.request.contextPath}/image/arrow.gif" />
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
									<s:select headerKey="-1"
										headerValue="%{getText('list.default.select')}"
										name="catTypeId" id="assettype"
										cssClass="selectwk" list="dropdownData.assetTypeList"
										listKey="id" listValue='name'
										value="%{catTypeId}"
										onChange="setupAjaxAssettype(this);" />
									<egov:ajaxdropdown id="populateParentcat"
										fields="['Text','Value']" dropdownId='parentcat'
										url='assetcategory/ajaxAssetCategory!populateParentCategories.action'
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
														src="${pageContext.request.contextPath}/image/arrow.gif" />
												</div>
												<div class="headplacer">
													<s:text name="search.result" />
												</div>
											</td>
										</tr>
										<tr>
											<!--  <td width="10%" class="tablesubheadwk">ID</td>  -->
											<td width="10%" class="tablesubheadwk">
												<s:text name="asset.search.sl.no" />
											</td>
											<td width="15%" class="tablesubheadwk">
												<s:text name="asset.search.name" />
											</td>
											<td width="15%" class="tablesubheadwk">
												<s:text name="%{getText('asset.search.code')}"></s:text>
											</td>
											<td width="15%" class="tablesubheadwk">
												<s:text name="asset.search.type" />
											</td>
											<td width="15%" class="tablesubheadwk">
												<s:text name="asset.search.uom" />
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
																	href="${pageContext.request.contextPath}/assetcategory/assetCategory!showform.action?
																	id=<s:property value='%{id}'/>&userMode=<s:property value='%{userMode}'/>">
																	<s:property value="%{name}" />
																</a> &nbsp;
															</td>
															<td width="15%">
																<s:property value="%{code}" />
																&nbsp;
															</td>
															<td width="15%">
																<s:property value="%{assetType.name}" />
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
