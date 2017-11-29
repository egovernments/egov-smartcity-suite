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
<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script>
<script>

function escapeSpecialChars(str) {
	str1 = str.replace(/\'/g, "\\'");
	str2 = str1.replace(/\r\n/g, "<br>");
	return str2;
}

function validateAndSubmit() {
	document.activitySearchForm.action='${pageContext.request.contextPath}/revisionEstimate/revisionEstimate!searchActivitiesForRE.action';
 	document.activitySearchForm.submit();
}
</script>
<html>
	<head>
	    <title><s:text name="title.activity.search" /></title>
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
		<s:form action="revisionEstimate" theme="simple"	name="activitySearchForm" onsubmit="validateAndSubmit();">
			<div class="errorstyle" id="search_error" style="display:none;"></div>
			<div class="navibarshadowwk">
			</div>
			<div class="formmainbox">
			<div class="insidecontent">
			<div class="rbroundbox2">
			<div class="rbtop2">
			</div>
			<div class="rbcontent2">
			<s:hidden name="workorderNo" id="workorderNo"/>
			<s:hidden id="originalWOId" name="originalWOId"/>
			<s:hidden id="originalEstimateId" name="originalEstimateId"/>		
			<table id="formTable" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table id="activitySearchTable" width="100%" border="0"
							cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4">&nbsp;</td>
							</tr>
							<tr>
								<td colspan="2">
									<div class="estimateno"><s:text name="revisionEstimate.workOrderNo"/> 
									<s:if test="%{not workorderNo}"><s:text name="revisionEstimate.not.selected.label"/></s:if>
									<s:property value="workorderNo" />
									</div>
								</td>
								<td colspan="2">
									<div class="estimateno">
									<s:text name="estimate.number"/> 
									<s:property value="abstractEstimate.estimateNumber" />
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img src="/egworks/resources/erp2/images/arrow.gif" />
									</div>
									<div class="headplacer">
										<s:text name='title.search.criteria' />
									</div>
								</td>
							</tr>
							<tr>
								<td width="19%" class="greyboxwk">
									<s:text name='item.code' />:
								</td>
								<td width="30%" class="greybox2wk">
									<s:textfield name="activityCode" cssClass="selectwk" id="activityCode" maxlength = "50" />
								</td>
								<td width="14%" class="greyboxwk">
									<s:text name='item.desc' />:
								</td>
								<td width="37%" class="greybox2wk">
									<s:textarea name="activityDesc" cols="25"  rows="2" cssClass="selectwk" id="activityDesc" />
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
							<tr>
								<td colspan="4">
									<div class="buttonholdersearch">
										<input type="submit" class="buttonadd" value="Search" id="searchButton" 
											name="Search"  onclick="return validateAndSubmit();" />
										<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button"
											onclick="window.close();" />
									</div>
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
														src="/egworks/resources/erp2/images/arrow.gif" />
												</div>
												<div class="headplacer">
													<s:text name="title.search.result" />
												</div>
											</td>
										</tr>
										<tr>
											<td class="aligncenter">
												<s:if test="%{activityList.size != 0}">
												<div >
													<display:table name="activityList" uid="currentRow"
														cellpadding="0" cellspacing="0" requestURI=""
														style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
																<display:column headerClass="pagetableth" class="pagetabletd"
																	title="Sl No" titleKey="column.title.SLNo"
																	style="width:3%;text-align:right">
																	<s:property value="#attr.currentRow_rowNum"/>
																	<s:hidden name="actid" id="actid" value="%{#attr.currentRow.id}" />
																	<s:hidden name="woid" id="woid" value="%{#attr.currentRow.workOrderEstimate.workOrder.id}" />
																</display:column>
																<display:column headerClass="pagetableth" class="pagetabletd"
																	title='<input type="checkbox" id="checkedAll" name="checkedAll" onclick="checkAll(this)" />' 
																	style="width:3%;text-align:center">
																	<s:checkbox id="selectedActivity" name="selectedActivity" />
																</display:column>
																<display:column headerClass="pagetableth" class="pagetabletd"
																	title="Description" titleKey="activity.search.column.desc" style="width:40%;text-align:left">
																	<s:if test="%{#attr.currentRow.activity.schedule}">																
																		<s:property value="%{#attr.currentRow.activity.schedule.summary}" />
																		<a href="#" class="hintanchor" onMouseover="showhint('<s:property value="%{#attr.currentRow.activity.schedule.descriptionJS}" />', this, event, '300px')"><img src="/egworks/resources/erp2/images/help.gif"	alt="Help" width="16" height="16" border="0" align="absmiddle"/></a>
																	</s:if>
																	<s:elseif test="%{#attr.currentRow.activity.nonSor}">
																		<s:property value="%{#attr.currentRow.activity.nonSor.description}" />
																		<a href="#" class="hintanchor" onMouseover="showhint('<s:property value="%{#attr.currentRow.activity.nonSor.descriptionJS}" />', this, event, '300px')"><img src="/egworks/resources/erp2/images/help.gif"	alt="Help" width="16" height="16" border="0" align="absmiddle"/></a>
																	</s:elseif>
																</display:column>
																<display:column headerClass="pagetableth" class="pagetabletd"
																	title="SOR/Non-SOR Type" titleKey="activity.search.column.type" style="width:15%;text-align:left">
																	<s:if test="%{#attr.currentRow.activity.schedule}">																	
																		<s:text name="sor.master.title"/>
																	</s:if>
																	<s:elseif test="%{#attr.currentRow.activity.nonSor}">
																		<s:text name="non.sor.master.title"/>
																	</s:elseif>
																</display:column>
																<display:column headerClass="pagetableth" class="pagetabletd"
																	title="SOR Code" titleKey="activity.search.column.sor.code" style="width:15%;text-align:left">
																	<s:if test="%{#attr.currentRow.activity.schedule}">																	
																		<s:property value="%{#attr.currentRow.activity.schedule.code}" />
																	</s:if>
																	<s:elseif test="%{#attr.currentRow.activity.nonSor}">
																		<s:text name="revisionEstimate.NA"/>
																	</s:elseif>
																</display:column>
																<display:column headerClass="pagetableth" class="pagetabletd"
																	title="Category Type" titleKey="activity.search.column.cattype" style="width:15%;text-align:left">
																	<s:if test="%{#attr.currentRow.activity.schedule}">																	
																		<s:property value="%{#attr.currentRow.activity.schedule.category.code}" />
																	</s:if>
																	<s:elseif test="%{#attr.currentRow.activity.nonSor}">
																		<s:text name="revisionEstimate.NA"/>
																	</s:elseif>
																</display:column>
																<display:column headerClass="pagetableth" class="pagetabletd"
																	title="UOM" titleKey="activity.search.column.uom" style="width:15%;text-align:left">
																	<s:if test="%{#attr.currentRow.activity.schedule}">																	
																		<s:property value="%{#attr.currentRow.activity.schedule.uom.uom}" />
																	</s:if>
																	<s:elseif test="%{#attr.currentRow.activity.nonSor}">
																		<s:property value="%{#attr.currentRow.activity.nonSor.uom.uom}" />
																	</s:elseif>
																</display:column>
														</display:table>
												</div>
												</s:if>
												<s:elseif test="%{activityList.size == 0}">
													<div >	
														<table width="100%" border="0" cellpadding="0"
														cellspacing="0">
															<tr>
																<td align="center">
																	<font color="red"><s:text name="label.no.records.found"/></font>
																</td>
															</tr>
														</table>
													</div>
												</s:elseif>
												<s:if test="%{activityList.size>0}">
													<br/>
													<div class="buttonholderwk">
														<input type="button" class="buttonfinal" value="ADD" id="button"
															name="button" onclick="returnBackToParent()" />
														<input type="button" class="buttonfinal" value="CLOSE"
															id="closeButton" name="closeButton" onclick="window.close();" />
													</div>
												</s:if>
											</td>
										</tr>
									</table>
								</div>
								</td>
							</tr>
							
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="4" class="shadowwk"></td>
				</tr>
			</table>
			</div>
			<div class="rbbot2">
			</div>
			</div>
			</div>
			</div>
		</s:form>
		<script type="text/javascript">
			
			function checkAll(obj){ 
				var len=document.forms[0].selectedActivity.length;
				if(obj.checked){
					if(len>0){
						for (i = 0; i < len; i++)
							document.forms[0].selectedActivity[i].checked = true;
					}else document.forms[0].selectedActivity.checked = true;
				}
				else{
					if(len>0){
						for (i = 0; i < len; i++)
							document.forms[0].selectedActivity[i].checked = false;
					}else document.forms[0].selectedActivity.checked = false;
				}
			}

			function returnBackToParent() { 
				var value = new Array();
				var wind=window.dialogArguments;
				var len=document.forms[0].selectedActivity.length; 
				var j=0;
				if(len >0){
					for (i = 0; i < len; i++){
						if(document.forms[0].selectedActivity[i].checked){
							value[j] = document.forms[0].actid[i].value+'~'+document.forms[0].woid[i].value;
							j++;
						}
					}
				}
				else{
					if(document.forms[0].selectedActivity.checked){
						value[j] = dom.get('actid').value+'~'+dom.get('woid').value;
					}
				}
				if(value.length>0) {
					var wind;
					var data = new Array();
					wind=window.dialogArguments;
					if(wind==undefined){
						wind=window.opener;
						data=value;
						window.opener.updateActivity(data);
					}
					else{
						wind=window.dialogArguments;
						wind.result=value;
					}
					window.close();
				}
				else {
					dom.get("search_error").innerHTML='<s:text name="revisionEstimate.select.one.activity"/>'; 
        			dom.get("search_error").style.display='';
					return false;
	 			}
	 			dom.get("search_error").style.display='none';
	 			dom.get("search_error").innerHTML='';
				return true;
			}
		</script>
	</body>
</html>

