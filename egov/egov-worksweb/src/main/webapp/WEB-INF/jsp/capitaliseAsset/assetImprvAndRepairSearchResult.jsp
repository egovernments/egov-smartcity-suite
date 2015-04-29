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
												
												<td width="5%" class="tablesubheadwk">
													Select
												</td>
												<td width="15%" class="tablesubheadwk">
													Estimate Name
												</td>
												<td width="10%" class="tablesubheadwk">
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
												<td width="10%" class="tablesubheadwk">
													Improvment Value
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
															<td width="5%">
																<input name="radio" type="radio" id="radio"
																	onClick="setassetId(<s:property value='%{asset.id}'/>,<s:property value='%{estimateId}'/>);" />
															</td>
															<td width="15%"">
																<s:property value="%{estimateName}" />
																
															</td>
															<td width="10%"">
																<s:property value="%{asset.code}" />
																
															</td>
															<td width="15%"">
																<s:property value="%{asset.name}" />
																
															</td>
															<td width="15%"">
																<s:property value="%{asset.assetCategory.name}" />
																
															</td>
															<td width="15%"">
																<s:property value="%{asset.department.deptName}" />
																
															</td>
															<td width="10%"" align="right">
																<s:property value="%{capitalisationValue}" />
																
															</td>
															<td width="10%"" align="center">
																<s:property value="%{asset.status.description}" />
																
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
											<s:hidden name="estimateId" id="estimateId"></s:hidden>
</table>
<script>
	function setassetId(assetId,estmtId){
	dom.get("assetId").value = assetId; 
	dom.get("estimateId").value = estmtId; 
}
</script>