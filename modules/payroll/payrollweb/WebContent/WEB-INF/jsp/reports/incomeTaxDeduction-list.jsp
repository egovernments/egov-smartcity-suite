<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%> 
      <div>
							<table width="100%" border="0" cellspacing="0"
								cellpadding="0">
								<tr><td>&nbsp;</td></tr>
								<tr>
									<td class="headingwk">
										<div class="arrowiconwk">
											<img src="../common/image/arrow.gif" />
										</div>
										<div class="headplacer">
											<s:text name="title.search.result" />
										</div>
									</td>
								</tr>
							</table>
                                 <s:if test="%{searchResult.fullListSize != 0}">
                                     <display:table name="searchResult" pagesize="30"
										uid="currentRow" cellpadding="0" cellspacing="0" 
										requestURI="" class="simple" style="width:100%;">	
										<display:caption class="headerbold">DEPARTMENT-${deptStr}
										</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Schedule Of Income-tax deductions in the Month of ${monthStr}-${yearStr}</br>
										
										${billNumberHeading}
										</display:caption>
                                    	<display:column title="Sl.No"
											titleKey="column.title.SLNo"
											style="width:3%;text-align:left" >
												<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
										</display:column>

                                   		<display:column title="EMP. NO."
											titleKey="report.deduction.emp.no"
											style="width:9%;text-align:left">
                                             <s:property  value='%{#attr.currentRow.empCode}' />
                                  		</display:column>
                                       
                                  		<display:column title="NAME/DESIGNATION"
											titleKey="report.deduction.emp.name"
											style="width:38%;text-align:left" >
                                            <s:property  value='%{#attr.currentRow.empName}' /><br/><s:property  value='%{#attr.currentRow.designation}' />
                                 		</display:column>
                                      
                                 		<display:column title="GROSS SALARY"
											titleKey="report.deduction.gross.salary"
											style="width:10%;text-align:right" >
											<s:text name="common.format.number" >
					   	 						<s:param name="dispTotalGrossSalary" value='%{#attr.currentRow.grossSalary}' />
					   	 					</s:text>
										</display:column>
                                           
                                      	<display:column title="DED. ON PF."
											titleKey="report.deduction.pf"
											style="width:10%;text-align:right" >
											<s:text name="common.format.number" >
					   	 						<s:param name="dispTotalPF" value='%{#attr.currentRow.totalPF}' />
					   	 					</s:text>
										</display:column>
                                      
                                        <display:column title="DED. ON LIC"
											titleKey="report.deduction.lic"
											style="width:10%;text-align:right" >
											<s:text name="common.format.number" >
					   	 						<s:param name="dispTotalLIC" value='%{#attr.currentRow.totalLIC}' />
					   	 					</s:text>
										</display:column>

				      					<display:column title="NET SALARY"
											titleKey="report.deduction.netsalary"
											style="width:10%;text-align:right" >
											<s:text name="common.format.number" >
					   	 						<s:param name="dispTotalNetSalary" value='%{#attr.currentRow.netSalary}' />
					   	 					</s:text>
					   					</display:column>
                                        <display:column title="INC TAX"
											titleKey="report.deduction.inctax"
											style="width:10%;text-align:right" >
											<s:text name="common.format.number" >
					   	 						<s:param name="totalIncTax" value='%{#attr.currentRow.totalIncomeTax}' />
					   	 					</s:text>
                                       </display:column>
										<s:if test="%{(pageSize*(page-1)+searchResult.getList().size())==searchResult.fullListSize}" >
	 										<display:footer>
	    										<tr>
	      											<td colspan="7" align="right"><b><bean:message key="grand.total"/></b></td>
	      											<td align="right">
	      												<s:text name="common.format.number" >
	      													<s:param name="dispGrandTotalIncTax" value='%{grandTotalIncTax}' />
	      												</s:text>
	      											</td>
	    										<tr>
	  										</display:footer>
  										</s:if>
                     				</display:table> 
									<div>
										<table width="100%" border="0" cellpadding="0"
											cellspacing="0">
											<tr><td>&nbsp;</td></tr>
											<tr>
												<td align="center">
													<input type="button" class="buttonfinal" value="EXPORT PDF" id="exportpdf" name="exportpdf" onclick="exportPDF();"/>
													<input type="button" class="buttonfinal" value="EXPORT EXCEL" id="exportpdf" name="exportpdf" onclick="exportExcel();"/>
													<input type="button" class="buttonfinal" value="EXPORT RTF" id="exportrtf" name="exportrtf" onclick="exportRTF();"/>
												</td>
											</tr>
										</table>
									</div>
                            </s:if> 
                            <s:elseif test="%{searchResult.fullListSize == 0}">
								<div>
									<table width="100%" border="0" cellpadding="0"
										cellspacing="0">
										<tr>
											<td align="center">
												<font color="red"><bean:message key="no.record.found"/></font>
											</td>
										</tr>
									</table>
								</div>
		 				</s:elseif>  
  		</div>
 <script>
 function exportPDF() {
	 var deptId=document.getElementById("department").value;
	 var year=document.getElementById("year").value;
	 var month=document.getElementById("month").value;
	 if(deptId<=0 || year<=0 || month<=0) {
		 alert('<bean:message key="alert.search.criteria"/>');
		 return false;
	}
	 var billNumber=document.getElementById("billNumber").value;
	 var billNumberId=document.getElementById("billNumberId").value;
	 var url="${pageContext.request.contextPath}/reports/deductionReport!exportIncomeTaxPDF.action?year="+year+"&department="+deptId+"&month="+month+"&billNumber="+billNumber+"&billNumberId="+billNumberId;
	 window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	 }
 function exportExcel() {
	 var deptId=document.getElementById("department").value;
	 var year=document.getElementById("year").value;
	 var month=document.getElementById("month").value;
	 if(deptId<=0 || year<=0 || month<=0) {
		 alert('<bean:message key="alert.search.criteria"/>');
		 return false;
	}
	 var billNumber=document.getElementById("billNumber").value;
	 var billNumberId=document.getElementById("billNumberId").value;
	 var url="${pageContext.request.contextPath}/reports/deductionReport!exportIncomeTaxExcel.action?year="+year+"&department="+deptId+"&month="+month+"&billNumber="+billNumber+"&billNumberId="+billNumberId;
	 window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}
 function exportRTF() {
	 var deptId=document.getElementById("department").value;
	 var year=document.getElementById("year").value;
	 var month=document.getElementById("month").value;
	 if(deptId<=0 || year<=0 || month<=0) {
		 alert('<bean:message key="alert.search.criteria"/>');
		 return false;
	}
	 var billNumber=document.getElementById("billNumber").value;
	 var billNumberId=document.getElementById("billNumberId").value;
	 var url="${pageContext.request.contextPath}/reports/deductionReport!exportIncomeTaxRTF.action?year="+year+"&department="+deptId+"&month="+month+"&billNumber="+billNumber+"&billNumberId="+billNumberId;
	 window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}
 </script> 
                           		