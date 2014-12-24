<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<html> 
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=8" />
		<title>
			<s:text name="testMaster.searchResult.header" />
		</title>
	</head>	
	<script src="<egov:url path='js/works.js'/>"></script>
	<script src="<egov:url path='js/helper.js'/>"></script> 
	<script type="text/javascript"> 

	</script>
	
	<body>	
	
	 <s:form name="testMasterSearch" id="testMasterSearch" theme="simple">  
     <s:token />
	 <s:hidden name="mode" id="mode" />
	 <s:hidden name="materialName" id="materialName" />
	 <s:hidden name="materialTypeId" id="materialTypeId" /> 
	<div class="formmainbox">
	<div class="insidecontent">
	<div class="rbroundbox2">
	<div class="rbtop2"><div></div></div> 
	<div class="rbcontent2">
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
      	<tr>
       		<td colspan="4" class="headingwk">
       			<div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
         		<div class="headplacer"><s:text name="testMaster.searchResult.header" />: <s:property value='%{materialName}'/></div> 
          	</td>
      	</tr>
      	
      	<tr>
								<td colspan="4">
								<div>
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td>
											<s:if test="%{searchResult.fullListSize != 0}">
												<display:table name="searchResult" pagesize="30" uid="currentRow"
													cellpadding="0" cellspacing="0" requestURI=""
													style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
													
													<display:column headerClass="pagetableth" class="pagetabletd" 
														title="Sl.No" titleKey="column.title.SLNo"
														style="width:4%;text-align:right" >
														<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize" />
													</display:column>
													
													<display:column headerClass="pagetableth" class="pagetabletd" 
														title="Test Name" titleKey="qualityControl.testMaster.testName"
														style="width:20%;text-align:left" property="testName"/>
																										
													<display:column headerClass="pagetableth" class="pagetabletd" 
														title="Remarks" titleKey="qualityControl.testMaster.remarks"
														style="width:40%;text-align:left" property="remarks"/>
																										
													<display:column headerClass="pagetableth" class="pagetabletd" 
														title="Unit" titleKey="qualityControl.testMaster.unit"
														style="width:10%;text-align:left" property="unit"/>
														
													<display:column headerClass="pagetableth" class="pagetabletd" 
														title="Edit/View" style="width:13%;text-align:left" >
														<table width="100" border="0" cellpadding="0" cellspacing="2">
															<tr>     
															                		
																<td width="20">
																	<a href="${pageContext.request.contextPath}/qualityControl/testMaster!edit.action?sourcepage=edit&id=<s:property value='%{#attr.currentRow.id}'/>">
																		<s:text name="testMaster.edit" /></a>
																</td> 
																<td width="20">
																	<a href="${pageContext.request.contextPath}/qualityControl/testMaster!edit.action?sourcepage=edit&id=<s:property value='%{#attr.currentRow.id}'/>">
																		<img src='${pageContext.request.contextPath}/image/page_edit.png' alt="Edit Data" width="16" height="16" border="0" align="absmiddle" />
																	</a>
																</td>
																 
																<td width="20">&#47;
																</td>
																<td width="20">
																	<a href="${pageContext.request.contextPath}/qualityControl/testMaster!edit.action?sourcepage=view&id=<s:property value='%{#attr.currentRow.id}'/>">
																		<s:text name="testMaster.view" />
																	</a>
																</td>
																<td width="20">
																	<a href="${pageContext.request.contextPath}/qualityControl/testMaster!edit.action?sourcepage=view&id=<s:property value='%{#attr.currentRow.id}'/>">
																		<img src='${pageContext.request.contextPath}/image/book_open.png' alt="View Data" width="16" height="16" border="0" align="absmiddle" />
																	</a>
																</td>
															</tr>
														</table>
													</display:column>	
												</display:table>
												</s:if>
												<s:elseif test="%{searchResult.fullListSize == 0}">
													<div >	
														<table width="100%" border="0" cellpadding="0"
														cellspacing="0">
															<tr>
																<td align="center">
																	<font color="red">No record Found.</font>
																</td>
															</tr>
														</table>
													</div>
												</s:elseif>
											</td>
										</tr>
										
									</table>
								</div>
								</td>
							</tr>
				<tr>
					<td colspan="4" class="shadowwk"></td>
				</tr>
	 </table> 
	 <br> 
	</div>
	<div class="rbbot2"><div></div></div>
	</div>
	</div>
	
	<div class="buttonholderwk">
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
	</div>
	
	</div>
	</s:form>
	</body>
</html>