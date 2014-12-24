	<tr>
								<td class="whiteboxwk"><span class="mandatory">*</span> <s:text
										name="fromdate.lbl" />&nbsp;</td>
								<td class="whitebox2wk"><s:date name='%{fromDate}'
										format='dd/MM/yyyy' var="fromdate" />
									<s:textfield name="fromDate" id="fromDate" value="%{fromdate}"
										cssClass="selectwk"
										onkeyup="DateFormat(this,this.value,event,false,'3')"
										onblur="validateDateFormat(this);compareDates();" /></td>

								<td class="whiteboxwk"><span class="mandatory">*</span> <s:text
										name="toDate.lbl" />&nbsp;</td>
								<td class="whitebox2wk"><s:date name='%{toDate}'
										format='dd/MM/yyyy' var="todate" />
									<s:textfield name="toDate" id="toDate" value="%{todate}"
										cssClass="selectwk"
										onkeyup="DateFormat(this,this.value,event,false,'3')"
										onblur="validateDateFormat(this);compareDates();" /></td>
							</tr>
							
							
										<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td class="greyboxwk"><s:text name="Department.lbl" />&nbsp;</td>
								<td class="greybox2wk"><s:select name="deptId" id="deptId"
										cssClass="selectwk" headerValue="----choose---" headerKey=""
										list="dropdownData.departmentlist" listValue="deptName" listKey="id"></s:select>
										</td>
								<td class="greyboxwk"></td>
								<td class="greybox2wk"></td>
							</tr>
							<tr>
								<td colspan="4"><div align="right" class="mandatory">*
										Mandatory Fields</div></td>
							</tr>