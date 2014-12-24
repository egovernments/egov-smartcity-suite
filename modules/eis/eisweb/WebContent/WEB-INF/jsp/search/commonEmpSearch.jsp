<tr>
								<td class="greyboxwk"> <s:text name="emp.code"/></td>
									<td class="greybox2wk" width="20%" valign="top" align="left">
										<div class="yui-skin-sam">
											<div id="empSearch_autocomplete" class="yui-ac">
												<s:textfield id="empCode" name="empCode" value="%{empCode}"
													onblur="checkAlphaNumeric(this);" size="20" cssClass="selectwk" />
												<div id="codeSearchResults"></div>
											</div>
										</div> 
										<egovtags:autocomplete name="empCode" field="empCode"
													url="${pageContext.request.contextPath}/common/employeeSearch!getActiveEmpListByEmpCodeLike.action" 
													queryQuestionMark="true" results="codeSearchResults"
													handler="codeSelectionHandler"
													forceSelectionHandler="codeSelectionEnforceHandler" />
										<span class='warning' id="impropercodeSelectionWarning"></span>
										
									</td>
									<td class="greyboxwk"><s:text name="emp.name" /></td>
									<td class="greybox2wk" colspan="9">
										<s:textfield  id="empName" name="empName" value="%{empName}" size="40" cssClass="selectwk" readonly="true"/>
									</td>
							</tr>		
							<tr>
								
									<td class="whiteboxwk"><s:text name="emp.dept" /></td>
									<td class="whitebox2wk"><s:select
											headerValue="-------choose-------" headerKey="0"
											list="dropdownData.departmentlist" listKey="id"
											listValue="deptName" id="deptId"
											name="deptId"  value="%{deptId}" cssClass="selectwk"/>
									</td>
											
									<td class="whiteboxwk"><s:text name="emp.desig" /></td>
									<td class="whitebox2wk"><s:select
											headerValue="-------choose-------" headerKey="0"
											list="dropdownData.designationlist" listKey="designationId"
											listValue="designationName" id="designationId"
											name="designationId" value="%{designationId}" cssClass="selectwk">
										</s:select></td>
								</tr>