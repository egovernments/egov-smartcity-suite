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

<html>
<title><s:text name="estimate.budgetfolio.header" /></title>
<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script>

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
