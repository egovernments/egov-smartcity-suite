	                         <script>
	                         	if(document.getElementById("searchButton")!=null) {
	                         		document.getElementById("searchButton").style.visibility='hidden';
	                         	}
	                         	if(document.getElementById("closeButton")!=null) {
	                         		document.getElementById("closeButton").style.visibility='hidden';
	                         	}
	                         	 function exportPDF() {
	                         		 var deptId=document.getElementById("department").value;
	                         		 var year=document.getElementById("year").value;
	                         		 var month=document.getElementById("month").value;
	                         		 if(deptId<=0 || year<=0 || month<=0) {
	                         			alert('<bean:message key="alert.search.criteria"/>');
	                         			 return false;
	                         		}
	                         		billNumberSetting();	
									 document.deductionReport.action='${pageContext.request.contextPath}/reports/deductionReport!exportLICPDF.action';
									 document.deductionReport.submit();	
	                         		 }
	                         	 function exportExcel() {
	                         		 var deptId=document.getElementById("department").value;
	                         		 var year=document.getElementById("year").value;
	                         		 var month=document.getElementById("month").value;
	                         		 if(deptId<=0 || year<=0 || month<=0) {
	                         			alert('<bean:message key="alert.search.criteria"/>');
	                         			 return false;
	                         		}
	                         		billNumberSetting();
	                         		 document.deductionReport.action='${pageContext.request.contextPath}/reports/deductionReport!exportLICExcel.action';
	                         		 document.deductionReport.submit();	
	                         	}
	                         	 function exportHTML() {
	                         		 var deptId=document.getElementById("department").value;
	                         		 var year=document.getElementById("year").value;
	                         		 var month=document.getElementById("month").value;
	                         		 if(deptId<=0 || year<=0 || month<=0) {
	                         			alert('<bean:message key="alert.search.criteria"/>');
	                         			 return false;
	                         		}
	                         		billNumberSetting();	
	                         		 document.deductionReport.action='${pageContext.request.contextPath}/reports/deductionReport!exportLICHTML.action';
	                         		 document.deductionReport.submit();
	                         	}
	                         	
	                         	function billNumberSetting(){
	                         		if(document.getElementById("billNumberId").value !=0)
	                         			   document.getElementById("billNumber").value = document.getElementById("billNumberId").options[document.getElementById("billNumberId").selectedIndex].text;
	                         		else
	                         			document.getElementById("billNumber").value = "";
	                         	} 
	                         </script>
	                         <tr>
								<td colspan="4" align="center" >
									<p>
										<input type="button" class="buttonfinal" value="EXPORT PDF" id="exportpdf" name="exportpdf" onclick="exportPDF();"/>
										<input type="button" class="buttonfinal" value="EXPORT EXCEL" id="exportpdf" name="exportpdf" onclick="exportExcel();"/>
										<input type="button" class="buttonfinal" value="EXPORT HTML" id="exporthtml" name="exporthtml" onclick="exportHTML();"/>
										<input type="button" class="buttonfinal" value="CLOSE" id="closelic" name="closelic" onclick="window.close();"/>
									</p>
								</td>
							</tr> 