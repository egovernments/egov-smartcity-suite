<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="egovtags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>GPF DEDUCTION REPORT</title>
</head>

<script type="text/javascript">

function exportto(exportformat) {
		 var deptId=document.getElementById("department").value;
		 var year=document.getElementById("year").value;
		 var month=document.getElementById("month").value;
		
		 if(deptId<=0 || year<=0 || month<=0) {
			 alert("Please select all search criteria");
			 return false;
	}
		 if(dom.get("billNumber").value==""){
 			dom.get("billNumberId").value=0;	
 		}
		 var billNumber=document.getElementById("billNumber").value;
		 var billNumberId=document.getElementById("billNumberId").value;
	 var url="${pageContext.request.contextPath}/reports/pFDeductionReport!showPFList.action?year="+year+"&department="+deptId+"&month="+month+"&fileFormat="+exportformat+"&billNumber="+billNumber+"&billNumberId="+billNumberId;
	 window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}


function init(){
	document.pFDeductionReport.fileFormat.value="PDF";
}

</script>

<body>
	<div class="errorstyle" id="searchDeduciton_error" style="display: none;"></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:form name="pFDeductionReport" action="pFDeductionReport!search.action" theme="simple">
		<center>
							 		<table style="width: 760;" align="center" cellpadding="0" cellspacing="0" border="0" id="pFDeductionReport">
										<tr>
						                	<td class="headingwk" colspan="5">
							                	<div class="arrowiconwk">
							                		<img src="../common/image/arrow.gif" />
							                	</div>
							                  	<div class="headplacer">GPF Deduction</div>
						                  	</td>
						              	</tr>
						              	
						              	<%@ include file="commonSearchReport.jsp" %>
										<s:hidden name="fileFormat" styleId="fileFormat" />
									   
									   
									   	 </table>
									   	 </center>
									   	 <s:if test="%{searchResult != null && !searchResult.getList().isEmpty() }" >
								
											<display:table name="searchResult" uid="currentRow" class="simple" export="false" 
											style="width:790px" requestURI="" pagesize="15">  
											<display:caption class="headerbold">NAGPUR MUNICIPAL CORPORATION,NAGPUR
											<br>DEPARTMENT-${deptName}</br>
											<br>${billNumberHeading}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Schedule Of GPF in the Month of  ${monthName} - ${yearStr} </br></display:caption>
										<display:column title="Sl.No" style="width:3%;text-align:left" >
												<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
										</display:column>

                                   		<display:column title="EMP. NO." property="code" />
                                  		
                                  		<display:column title="PF NO."  property="PFNum" />
                                  		
                                  		<display:column title="NAME" property="name" /> 
                                  		
                                  		<display:column title="PF SUBSCRIPTION"  property="GPFSUbamt"  style="text-align:right" />
                                  		
                                  		<display:column title="PF ADVANCE" property="GPFADVamt"  style="text-align:right" />
                                  		
                                  		<display:column title="TOTAL"  style="text-align:right">${currentRow.GPFSUbamt+currentRow.GPFADVamt}
                                  		</display:column>   
                                  		
                                  		
                                  		<display:column title="INST. NO.">
                                  		<s:if test="%{#attr.currentRow.noofinst!=null && #attr.currentRow.noofinst!=0}">
                                  		 <s:property  value='%{#attr.currentRow.instno}'/>/
                                             <s:property  value='%{#attr.currentRow.noofinst}' /> 
                                            
                                         </s:if>
                                  		</display:column>
                                  		 <s:if test="%{getLastpage()}" >   
	 										<display:footer>
	    										<tr>
	      											<td colspan="4" align="right"><b>Grand Total:</b></td>
	      											<td align="right" ><b><s:property  value="%{totalPF}" /></b></td>
	      											<td align="right"><b><s:property  value="%{totalPFAdv}" /></b></td>
	      											<td align="right"><b><s:property  value="%{grandTotal}" /></b></td>
	    										<tr>
	  										</display:footer>
  										</s:if>            		
                                  		
								    		</display:table >
								    		<div>
										<table width="100%" border="0" cellpadding="0"
											cellspacing="0">
											<tr><td>&nbsp;</td></tr>
											<tr>
												<td colspan="7" align="center" >
													<input type="button" class="buttonfinal" value="EXPORT TO PDF" id="exportpdf" name="exportpdf" onclick="exportto('PDF');"/>
													<input type="button" class="buttonfinal" value="EXPORT TO EXCEL" id="exportxls" name="exportxls" onclick="exportto('XLS');"/>
													
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
												<font color="red">No records Found.</font>
											</td>
										</tr>
									</table>
								</div>
		 				</s:elseif>  
						 				
						 				
						 	
	</s:form>
</body>
</html>
