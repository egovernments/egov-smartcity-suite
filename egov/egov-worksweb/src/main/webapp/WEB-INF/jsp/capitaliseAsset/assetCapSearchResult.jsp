<%@ taglib prefix="s" uri="/struts-tags"%>
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
															<td width="15%">
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
															<td width="15%"" align="right">
																<s:property value="%{capitalisationValue}" />
																&nbsp;
															</td>
															<td width="10%"" align="center">
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
<script>
	function setassetId(elem){
	dom.get("assetId").value = elem.value; 
}
</script>