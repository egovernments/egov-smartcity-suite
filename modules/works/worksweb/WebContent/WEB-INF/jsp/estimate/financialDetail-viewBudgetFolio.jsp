<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>


<html>
<title><s:text name="estimate.budgetfolio.header" /></title>
<script src="<egov:url path='js/works.js'/>"></script>
<script src="<egov:url path='js/helper.js'/>"></script>

<body >
		<s:if test="%{hasErrors()}">
       		 <div class="errorstyle">
          		<s:actionerror/>
          		<s:fielderror/>
        	</div>
    	</s:if>
   		<s:if test="%{hasActionMessages()}">
       		<div id="msgsDiv" class="messagestyle">
        		<s:actionmessage theme="simple"/>
        	</div>
    	</s:if>
		<s:form  theme="simple" name="financialDetailForm" >   
			<div class="navibarshadowwk">
			</div>
			<div class="formmainbox">
			<div class="insidecontent">
			<div class="rbroundbox2">
			<div class="rbtop2"><div></div>
			</div>
			<div class="rbcontent2">
			<table id="formTable" width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<table id="budgetFolioHeader" width="100%" border="0"
							cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4">&nbsp;</td>
							</tr>
							<tr>
								<td width="11%" class="whiteboxwk">
									<s:text name='estimate.executing.department'/> :
								</td>
								<td width="21%" class="whitebox2wk">
									<span class="bold"><s:property value="%{abstractEstimate.executingDepartment.deptName}" /></span>
								</td>
								<td width="11%" class="whiteboxwk">
									<s:text name='estimate.financial.function'/> :
								</td>
								<td width="21%" class="whitebox2wk">
									<span class="bold"><s:property value="%{function.name}" /></span>
								</td>
							</tr>
							<tr>
								<td width="11%" class="greyboxwk">
									<s:text name='estimate.financial.budgethead'/> :
								</td>
								<td width="21%" class="greybox2wk">
									<span class="bold"><s:property value="%{budgetGroup.name}" /></span>	
								</td>
								<td width="15%" class="greyboxwk">
								<s:text name='estimate.financial.fund'/> :
								</td>
								<td width="53%" class="greybox2wk">
										<span class="bold"><s:property value="%{fund.name}" /></span>
								</td>
							</tr>
							<tr>
								<td width="11%" class="whiteboxwk">
								<s:text name='estimate.budgetfolio.totalgrant'/> :									
								</td>
								<td width="21%" class="whitebox2wk">
									<span class="bold">									
									  <s:text name="contractor.format.number" >
                        				 <s:param name="value" value="%{totalGrant}"/> 
                        			  </s:text>
									</span>
								</td>
								<td width="11%" class="whiteboxwk">
									<s:text name='estimate.budgetfolio.totalgrantper'/>&nbsp;<s:property value="%{appValueLabel}" /> % :										
								</td>
								<td width="21%" class="whitebox2wk">
									<span class="bold">
									 <s:text name="contractor.format.number" >
                        				 <s:param name="value" value="%{totalGrantPerc}"/> 
                        			  </s:text>
									</span>
								</td>
							</tr>
						<tr><td colspan="4">&nbsp;</td></tr>
			
				  				<%@ include file="financialDetail-approvedBudgetFolioDetail.jsp" %>    
				 			<tr><td colspan="4">&nbsp;</td></tr>
				  				<%--   @ include file="financialDetail-budgetFolioDetails.jsp"  --%>   
		
							<%-- tr>
								<td colspan="4">
					 				<div class="buttonholdersearch">					 
										<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/financialDetail!viewBudgetFolioPdf.action?estimateId=<s:property value='%{abstractEstimate.id}'/>');"
										class="buttonpdf" value="VIEW PDF" id="pdfButton" name="pdfButton" />
										<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="window.close();" />
									</div>
								</td>
							</tr --%>
					</table>			
					</td>
				</tr>
				<tr>
					<td colspan="4" class="shadowwk"></td>
				</tr>
			</table>	
			</div>
			<div class="rbbot2"><div></div></div>
			</div>
			</div>
			</div>
			
			<div class="buttonholderwk">
				<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/financialDetail!viewBudgetFolioPdf.action?estimateId=<s:property value='%{abstractEstimate.id}'/>');" class="buttonpdf" value="VIEW PDF" id="pdfButton" name="pdfButton" />
				<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="window.close();" />			
			</div>
		</s:form>
	</body>
</html>
