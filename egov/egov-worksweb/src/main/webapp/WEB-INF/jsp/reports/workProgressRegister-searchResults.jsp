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

<script type="text/javascript" src="<egov:url path='resources/js/jquery/jquery.fixheadertable.js'/>"></script>
<link rel="stylesheet" type="text/css" href="../resources/css/jquery/base.css" />
<link rel="stylesheet" type="text/css" href="../resources/css/jquery-ui/css/redmond/jquery-ui-1.8.4.custom.css" />

<style type="text/css">

td.pagetabletdr{
	color: #333333;
	font-family: Arial, Helvetica, sans-serif;
	font-size: 10px;
	text-align: right;
	line-height: 12px;
	overflow: hidden;
	border: 1px solid #D1D9E1;
	padding: 3px;
}		
</style>

<script>
jQuery.noConflict();

jQuery(document).ready(function() {
    jQuery('.table-header-fix').fixheadertable({
         caption : 'Work Progress Register', 
         height  : 400,
         width : 1170, 
         minColWidth	 : 10 , 
         resizeCol	 : true, 
         colratio    : [28,100,100,150,100,100,100,100,90,100,100,100,100,100,100,100,120,440,100,80]
    });
});

function viewPDF(){
 var parameter='exportType=printPDF';
 <s:if test="%{sourcePage!=null && (sourcePage=='deptWiseReport' || sourcePage=='deptWiseReportForWP')}">
 	if(dom.get("estId").value!="")
 		parameter=parameter+'&sourcePage=deptWiseReport'+'&estId='+dom.get("estId").value;
 	else if(dom.get("woId").value!="")	
	 	parameter=parameter+'&sourcePage=deptWiseReport'+'&woId='+dom.get("woId").value;
 </s:if>
 <s:elseif test="%{sourcePage==null || sourcePage==''}">
 	 parameter=parameter+'&parentCategory='+dom.get("parentCategory").value+'&category='+dom.get("category").value+'&workOrderStatus='+dom.get("workOrderStatus").value;
	 parameter=parameter+'&milestoneStatus='+dom.get("milestoneStatus").value+'&expenditureType='+dom.get("type").value+'&fund='+dom.get("fund").value;
	 parameter=parameter+'&function='+dom.get("function").value+'&budgetHead='+dom.get("budgetHead").value+'&execDept='+dom.get("executingDepartment").value;
	 parameter=parameter+'&preparedBy='+dom.get("preparedBy").value+'&engineerIncharge='+dom.get("engineerIncharge").value+'&engineerIncharge2='+dom.get("engineerIncharge2").value;
	 parameter=parameter+'&fromDate='+dom.get("fromDate").value+'&toDate='+dom.get("toDate").value+"&scheme="+document.getElementById("scheme").value+"&subScheme="+document.getElementById("subScheme").value;
	 parameter=parameter+'&contractorId='+dom.get("contractorId").value+'&wardId='+dom.get("wardId").value;
 </s:elseif>
 window.open('${pageContext.request.contextPath}/reports/workProgressRegister!viewWorkProgressRegister.action?'+parameter);

}

function viewExcel(){
 
 var parameter='exportType=printExcel';
 <s:if test="%{sourcePage!=null && (sourcePage=='deptWiseReport' || sourcePage=='deptWiseReportForWP')}">
 	if(dom.get("estId").value!="")
		parameter=parameter+'&sourcePage=deptWiseReport'+'&estId='+dom.get("estId").value;
	else if(dom.get("woId").value!="")	
	 	parameter=parameter+'&sourcePage=deptWiseReport'+'&woId='+dom.get("woId").value;
 </s:if>
 <s:elseif test="%{sourcePage==null || sourcePage==''}">
 parameter=parameter+'&parentCategory='+dom.get("parentCategory").value+'&category='+dom.get("category").value+'&workOrderStatus='+dom.get("workOrderStatus").value;
 parameter=parameter+'&milestoneStatus='+dom.get("milestoneStatus").value+'&expenditureType='+dom.get("type").value+'&fund='+dom.get("fund").value;
 parameter=parameter+'&function='+dom.get("function").value+'&budgetHead='+dom.get("budgetHead").value+'&execDept='+dom.get("executingDepartment").value;
 parameter=parameter+'&preparedBy='+dom.get("preparedBy").value+'&engineerIncharge='+dom.get("engineerIncharge").value+'&engineerIncharge2='+dom.get("engineerIncharge2").value;
 parameter=parameter+'&fromDate='+dom.get("fromDate").value+'&toDate='+dom.get("toDate").value+"&scheme="+document.getElementById("scheme").value+"&subScheme="+document.getElementById("subScheme").value;
 parameter=parameter+'&contractorId='+dom.get("contractorId").value+'&wardId='+dom.get("wardId").value;
 </s:elseif>
 window.open('${pageContext.request.contextPath}/reports/workProgressRegister!viewWorkProgressRegister.action?'+parameter);

}

</script>


<div width="100%">
     <s:if test="%{searchResult.fullListSize != 0}">
     	<div style='font-weight:bold;text-align:center' >
					<s:property value="%{searchCriteria}" />
	    </div>
		<div>
 	     <display:table name="searchResult" uid="currentRow" cellpadding="0" cellspacing="0"
			requestURI="" class="table-header-fix">
			
	        <display:column title="<table width=100% border=1 cellpadding=0 cellspacing=0><tr><td align=center>Sl No</td></tr></table>"
			   titleKey="column.title.SLNo"
			   style="text-align:left" >
			     <s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
			</display:column>
	        
	        <display:column title="<table width=50% border=1px cellpadding=0 cellspacing=0><tr><td align=center>Dept</td></tr><tr><td align=center>Ward</td></tr><tr><td align=center>Location</td></tr></table>"
			   style="text-align:left">
					<table width=50% cellpadding=0	cellspacing=0>
						<tr>
							<td align="left" >
								<s:if test="%{#attr.currentRow.dept == null || #attr.currentRow.dept == ''}">
				  					&nbsp;
				  				</s:if>
				  				<s:else>
				  					<s:property  value='%{#attr.currentRow.dept}' />
				  				</s:else>
							</td>
						</tr>
						<tr>
							<td align="left" >
								<s:if test="%{#attr.currentRow.ward == null || #attr.currentRow.ward == ''}">
					  				&nbsp;
				  				</s:if>
				  				<s:else>
				  					<s:property  value='%{#attr.currentRow.ward}' />
				  				</s:else>
							</td>
						</tr>
						<tr>
							<td align="left" >
								<s:if test="%{#attr.currentRow.location == null || #attr.currentRow.location == ''}">
									&nbsp;
				  				</s:if>
				  				<s:else>
				  					<s:property  value='%{#attr.currentRow.location}' />
				  				</s:else>
							</td>
						</tr>
					</table>
            </display:column>
                   
            <display:column title="<table width=100% border=1 cellpadding=0 	cellspacing=0><tr><td align=center>Estimate No</td></tr><tr><td align=center>Project Code</td></tr></table>"
			   style="text-align:left" >
					<table width=100% cellpadding=0	cellspacing=0>
						<tr>
							<td align="left" >
								<s:if test="%{#attr.currentRow.estimateNo == null || #attr.currentRow.estimateNo == ''}">
				  					&nbsp;
				  				</s:if>
				  				<s:else>
				  					<s:property  value='%{#attr.currentRow.estimateNo}' />
				  				</s:else>
							</td>
						</tr>
						<tr>
							<td align="left" >
								<s:if test="%{#attr.currentRow.projectCode == null || #attr.currentRow.projectCode == ''}">
					  				&nbsp;
				  				</s:if>
				  				<s:else>
				  					<s:property  value='%{#attr.currentRow.projectCode}' />
				  				</s:else>
							</td>
						</tr>
					</table>
 		    </display:column>
                 
            <display:column title="<table width=100% border=1 cellpadding=0 	cellspacing=0><tr><td align=center>Name of Work</td></tr></table>"
			   style="text-align:left" >
					<table width=100% cellpadding=0	cellspacing=0>
						<tr>
							<td align="left" >
								<s:if test="%{#attr.currentRow.nameOfWork == null || #attr.currentRow.nameOfWork == ''}">
				  					&nbsp;
				  				</s:if>
				  				<s:else>
				  					<s:property  value='%{#attr.currentRow.nameOfWork}' />
				  				</s:else>
							</td>
						</tr>
					</table>
		    </display:column>
                     
            <display:column title="<table width=100% border=1 cellpadding=0 cellspacing=0><tr><td align=center>Type of Work</td></tr></table>"
			   titleKey="workprogress.register.work.type"
			   style="text-align:left" >
					<table width=100% cellpadding=0	cellspacing=0>
						<tr>
							<td align="left" >
								<s:if test="%{#attr.currentRow.typeOfWork == null || #attr.currentRow.typeOfWork == ''}">
				  					&nbsp;
				  				</s:if>
				  				<s:else>
				  					<s:property  value='%{#attr.currentRow.typeOfWork}' />
				  				</s:else>
							</td>
						</tr>
					</table>
			</display:column>
                
            <display:column title="<table width=100% border=1 cellpadding=0 cellspacing=0><tr><td align=center>Estimate Amount</td></tr></table>"
			   style="text-align:left" >
					<table width=100% cellpadding=0	cellspacing=0>
						<tr>
							<td class="pagetabletdr">
								<s:if test="%{#attr.currentRow.estimateAmt == null}">
				  					&nbsp;
				  				</s:if>
				  				<s:else>
							   		<s:text name="contractor.format.number" >
										<s:param name="rate" value='%{#attr.currentRow.estimateAmt}' />
									</s:text>
				  				</s:else>
							</td>
						</tr>
					</table>
			   		
			</display:column>
                			  
			<display:column title="<table width=100% border=1 cellpadding=0 	cellspacing=0><tr><td align=center>Estimate Date</td></tr><tr><td align=center>Tech Sanction Date</td></tr><tr><td align=center>Adminstrative Sanction Date</td></tr></table>"
			   style="text-align:left" >
					<table width=100% cellpadding=0	cellspacing=0>
						<tr>
							<td >
								<s:if test="%{#attr.currentRow.estimateDate == null || #attr.currentRow.estimateDate == ''}">
				  					&nbsp;
				  				</s:if>
				  				<s:else>
				  					<s:property  value='%{#attr.currentRow.estimateDate}' />
				  				</s:else>
							</td>
						</tr>
						<tr>
							<td >
								<s:if test="%{#attr.currentRow.techSanctionDate == null || #attr.currentRow.techSanctionDate == ''}">
					  				&nbsp;
				  				</s:if>
				  				<s:else>
				  					<s:property  value='%{#attr.currentRow.techSanctionDate}' />
				  				</s:else>
							</td>
						</tr>
						<tr>
							<td >
								<s:if test="%{#attr.currentRow.adminSanctionDate == null || #attr.currentRow.adminSanctionDate == ''}">
									&nbsp;
				  				</s:if>
				  				<s:else>
				  					<s:property  value='%{#attr.currentRow.adminSanctionDate}' />
				  				</s:else>
							</td>
						</tr>
					</table>
			</display:column>                                       
	          	                                      
	        <display:column title="<table width=100% border=1 cellpadding=0 cellspacing=0>
		       	<tr><td align=center>Fund</td></tr>
		       	<tr><td align=center>Function</td></tr>
		       	<tr><td align=center>Budget Head</td></tr>
		       	<tr><td align=center>Appr Year-Appr Amount</td></tr></table>"
			   style="text-align:left">

					<table width=100% cellpadding=0	cellspacing=0>
						<tr>
							<td >
								<s:if test="%{#attr.currentRow.fund == null || #attr.currentRow.fund == ''}">
				  					&nbsp;
				  				</s:if>
				  				<s:else>
				  					<s:property  value='%{#attr.currentRow.fund}' />
				  				</s:else>
							</td>
						</tr>
						<tr>
							<td >
								<s:if test="%{#attr.currentRow.function == null || #attr.currentRow.function == ''}">
				  					&nbsp;
				  				</s:if>
				  				<s:else>
				  					<s:property  value='%{#attr.currentRow.function}' />
				  				</s:else>
							</td>
						</tr>
						<tr>
							<td >
								<s:if test="%{#attr.currentRow.budgetHead == null || #attr.currentRow.budgetHead == ''}">
				  					&nbsp;
				  				</s:if>
				  				<s:else>
				  					<s:property  value='%{#attr.currentRow.budgetHead}' />
				  				</s:else>
							</td>
						</tr>
						<tr>
							<td >
								<s:if test="%{#attr.currentRow.apprDetails == null || #attr.currentRow.apprDetails == ''}">
				  					&nbsp;
				  				</s:if>
				  				<s:else>
				  					<s:property  value='%{#attr.currentRow.apprDetails}' />
				  				</s:else>
							</td>
						</tr>
					</table>
            </display:column>
			

	        <display:column title="<table width=100% border=1 cellpadding=0 cellspacing=0><tr><td align=center>Tender Date</td></tr><tr><td align=center>Tender Finalization Date</td></tr><tr><td align=center>Agreement Date</td></tr></table>"
			   style="text-align:left">
					<table width=100% cellpadding=0	cellspacing=0>
						<tr>
							<td >
								<s:if test="%{#attr.currentRow.tenderDate == null || #attr.currentRow.tenderDate == ''}">
				  					&nbsp;
				  				</s:if>
				  				<s:else>
				  					<s:property  value='%{#attr.currentRow.tenderDate}' />
				  				</s:else>
							</td>
						</tr>
						<tr>
							<td >
								<s:if test="%{#attr.currentRow.tenderFinalizationDate == null || #attr.currentRow.tenderFinalizationDate == ''}">
					  				&nbsp;
				  				</s:if>
				  				<s:else>
				  					<s:property  value='%{#attr.currentRow.tenderFinalizationDate}' />
				  				</s:else>
							</td>
						</tr>
						<tr>
							<td >
								<s:if test="%{#attr.currentRow.tenderAgreementDate == null || #attr.currentRow.tenderAgreementDate == ''}">
									&nbsp;
				  				</s:if>
				  				<s:else>
				  					<s:property  value='%{#attr.currentRow.tenderAgreementDate}' />
				  				</s:else>
							</td>
						</tr>
					</table>
            </display:column>

            <display:column title="<table width=100% border=1 cellpadding=0 cellspacing=0><tr><td align=center>Work Order Value</td></tr></table>"
			   style="text-align:left" >
					<table width=100% cellpadding=0	cellspacing=0>
						<tr>
							<td class="pagetabletdr">
								<s:if test="%{#attr.currentRow.workOrderValue == null}">
				  					&nbsp;
				  				</s:if>
				  				<s:else>
							   		<s:text name="contractor.format.number" >
										<s:param name="rate" value='%{#attr.currentRow.workOrderValue}' />
									</s:text>
				  				</s:else>
							</td>
						</tr>
					</table>
			</display:column>

            <display:column title="<table width=100% border=1 cellpadding=0 cellspacing=0><tr><td align=center>Work Order Date</td>
			   			</tr><tr><td align=center>Contract Period</td></tr>
			   			<tr><td align=center>Work Commencement Date</td></tr>
			   			<tr><td align=center>Site Handed Over Date</td></tr></table>"
			   titleKey="workprogress.register.estimate.amount"
			   style="text-align:left" >
					<table width=100% cellpadding=0	cellspacing=0>
						<tr>
							<td>
								<s:if test="%{#attr.currentRow.workOrderDate == null || #attr.currentRow.workOrderDate == ''}">
				  					&nbsp;
				  				</s:if>
				  				<s:else>
				  					<s:property  value='%{#attr.currentRow.workOrderDate}' /> 
				  				</s:else>
							</td>
						</tr>
						<tr>
							<td >
								<s:if test="%{#attr.currentRow.contractPeriod == null || #attr.currentRow.contractPeriod == ''}">
					  				&nbsp;
				  				</s:if>
				  				<s:else>
				  					<s:property  value='%{#attr.currentRow.contractPeriod}' />
				  				</s:else>
							</td>
						</tr>
						<tr>
							<td >
								<s:if test="%{#attr.currentRow.workCommencementDate == null || #attr.currentRow.workCommencementDate == ''}">
									&nbsp;
				  				</s:if>
				  				<s:else>
				  					<s:property  value='%{#attr.currentRow.workCommencementDate}' />
				  				</s:else>
							</td>
						</tr>
						<tr>
							<td >
								<s:if test="%{#attr.currentRow.siteHandedOverDate == null || #attr.currentRow.siteHandedOverDate == ''}">
									&nbsp;
				  				</s:if>
				  				<s:else>
				  					<s:property  value='%{#attr.currentRow.siteHandedOverDate}' />
				  				</s:else>
							</td>
						</tr>
					</table>
			</display:column>

            <display:column title="<table width=100% border=1 cellpadding=0 cellspacing=0><tr><td align=center>Stage of Work</td></tr></table>"
			   titleKey="workprogress.register.estimate.amount"
			   style="text-align:left" >
					<table width=100% cellpadding=0	cellspacing=0>
						<s:iterator var="s" value="%{#attr.currentRow.trackMilestoneActivities}" status="status"> 
							<tr>
								<td >
									<s:if test="%{milestoneActivity.description == null}">
				  						&nbsp;
				  					</s:if>
				  					<s:else>
										<s:property value="%{milestoneActivity.stageOrderNo}" />-<s:property value="%{milestoneActivity.description}" />		
				  					</s:else>
								</td>
							</tr>
						</s:iterator>
					</table>
			</display:column>

            <display:column title="<table width=100% border=1 cellpadding=0 cellspacing=0><tr><td align=center>Status</td></tr></table>"
			   titleKey="workprogress.register.estimate.amount"
			   style="text-align:left" >
					<table width=100% cellpadding=0	cellspacing=0>
						<s:iterator var="s" value="%{#attr.currentRow.trackMilestoneActivities}" status="status"> 
							<tr>
								<td >
									<s:if test="%{status == null}">
				  						&nbsp;
				  					</s:if>
				  					<s:else>
										<s:property value="%{status}" />		
				  					</s:else>
								</td>
							</tr>
						</s:iterator>
					</table>
			</display:column>

            <display:column title="<table width=100% border=1 cellpadding=0 cellspacing=0><tr><td align=center>Completion %</td></tr></table>"
			   style="text-align:left" >
					<table width=100% cellpadding=0	cellspacing=0>
						<s:iterator var="s" value="%{#attr.currentRow.trackMilestoneActivities}" status="status"> 
							<tr>
								<td class="pagetabletdr">
									<s:if test="%{complPercentage == null}">
				  						&nbsp;
				  					</s:if>
				  					<s:else>
										<s:property value="%{complPercentage}" />	
				  					</s:else>
								</td>
							</tr>
						</s:iterator>
					</table>
			</display:column>
			<display:column title="<table width=100% border=1 cellpadding=0 cellspacing=0><tr><td align=center>Completion Date</td></tr></table>"
			   style="text-align:left" >
					<table width=100% cellpadding=0	cellspacing=0>
						<s:iterator var="s" value="%{#attr.currentRow.trackMilestoneActivities}" status="status"> 
							<tr>
								<td >
									<s:if test="%{completionDate == null}">
				  						&nbsp;
				  					</s:if>
				  					<s:else>
				  						<s:date name="#s.completionDate" format="dd/MM/yyyy"/>
				  					</s:else>
								</td>
							</tr>
						</s:iterator>
					</table>
			</display:column>


			<display:column title="<table width=100% border=1 cellpadding=0 cellspacing=0><tr><td align=center>Total Work Completion</td></tr></table>"
			   style="text-align:left" >
					<table width=100% cellpadding=0	cellspacing=0>
							<tr>
								<s:if test="%{#attr.currentRow.trackMilestoneActivities.size() != 0}">
									<td class="pagetabletdr">
										<s:if test="%{#attr.currentRow.trackMilestoneActivities.get(0).trackMilestone.total == null}">
				  							&nbsp;
				  						</s:if>
				  						<s:else>
											<s:property value="%{#attr.currentRow.trackMilestoneActivities.get(0).trackMilestone.total}" />	
				  						</s:else>
									</td>
								</s:if>
							</tr>
					</table>
			</display:column>

			<display:column title="<table width=100% border=1 cellpadding=0 cellspacing=0><tr><td align=center>Contractor Code & Name</td></tr></table>"
			   style="text-align:left" >
					<table width=100% cellpadding=0	cellspacing=0>
							<tr>
								<td >
									<s:if test="%{#attr.currentRow.contractorName == null || #attr.currentRow.contractorName == ''}">
				  						&nbsp;
				  					</s:if>
				  					<s:else>
											<s:property value="%{#attr.currentRow.contractorName}" />
				  					</s:else>
								</td>
							</tr>
					</table>
			</display:column>

	        <display:column title="<table width=100% border=1 cellpadding=0 cellspacing=0>
		       		<tr><td colspan=6 align=center width=100%>Payment Details</td></tr>
		       		<tr><td >Bill No</td><td >Bill  Date</td>
		       			<td >Bill Type</td><td >Bill Amount</td>
		       			<td >CJV No</td>
		       			<td >Released Amount</td></tr></table>"
			   style="text-align:left">
					<table width=100% cellpadding=0	cellspacing=0>
						<s:iterator var="p" value="%{#attr.currentRow.paymentDetails}" status="status"> 
							<tr>
								<td  >
									<s:if test="%{billNumber == null}">
				  						&nbsp;
				  					</s:if>
				  					<s:else>
										<s:property value="%{billNumber}" />		
				  					</s:else>
								</td>
								<td   >
									<s:if test="%{billDate == null}">
				  						&nbsp;
				  					</s:if>
				  					<s:else>
										<s:property value="%{billDate}" />		
				  					</s:else>
								</td>
								<td  >
									<s:if test="%{billType == null}">
				  						&nbsp;
				  					</s:if>
				  					<s:else>
										<s:property value="%{billType}" />		
				  					</s:else>
								</td>
								<td  align="right">
									<s:if test="%{billAmount == null}">
				  						&nbsp;
				  					</s:if>
				  					<s:else>
							   			<s:text name="contractor.format.number" >
											<s:param name="rate" value='%{billAmount}' />
										</s:text>
				  					</s:else>
								</td>
								<td >
									<s:if test="%{cjvNo == null}">
				  						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				  					</s:if>
				  					<s:else>
											<s:property value="%{cjvNo}" />	
				  					</s:else>
								</td>
								<td  align="right">
									<s:if test="%{releasedAmount == null}">
				  						&nbsp;
				  					</s:if>
				  					<s:else>
				  						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							   			<s:text name="contractor.format.number" >
											<s:param name="rate" value='%{releasedAmount}' />
										</s:text>
				  					</s:else>
								</td>

							</tr>
						</s:iterator>
						<s:if test="%{#attr.currentRow.paymentDetails.size() != 0}">
							<tr>
								<td colspan="3" width="57%"  align="right">
									Total
								</td>
								<td width="20%" align="right">
									<s:if test="%{#attr.currentRow.totalBillAmt == null}">
				  						&nbsp;
				  					</s:if>
				  					<s:else>
							   			<s:text name="contractor.format.number" >
											<s:param name="rate" value='%{#attr.currentRow.totalBillAmt}' />
										</s:text>
				  					</s:else>
								</td>
								<td width="24%">&nbsp;</td>
								<td width="20%" align="right">
									<s:if test="%{#attr.currentRow.totalReleasedAmt == null}">
				  						&nbsp;
				  					</s:if>
				  					<s:else>
							   			<s:text name="contractor.format.number" >
											<s:param name="rate" value='%{#attr.currentRow.totalReleasedAmt}' />
										</s:text>
				  					</s:else>
								</td>
							</tr>
						</s:if>
					</table>
            </display:column>
 
            <display:column title="<table width=100% border=1 cellpadding=0 cellspacing=0><tr><td align=center>Outstanding Payment to Contractor</td></tr></table>"
			   style="text-align:left" >
					<table width=100% cellpadding=0	cellspacing=0>
						<s:iterator var="p" value="%{#attr.currentRow.paymentDetails}" status="status"> 
							<tr>
								<td class="pagetabletdr">
									<s:if test="%{outstandingAmount == null}">
				  						&nbsp;
				  					</s:if>
				  					<s:else>
							   			<s:text name="contractor.format.number" >
											<s:param name="rate" value='%{outstandingAmount}' />
										</s:text>
				  					</s:else>
								</td>
							</tr>
						</s:iterator>
						<s:if test="%{#attr.currentRow.paymentDetails.size() != 0}">
							<tr>
								<td class="pagetabletdr">
									<s:if test="%{#attr.currentRow.totalOutstandingAmt == null}">
				  						&nbsp;
				  					</s:if>
				  					<s:else>
							   			<s:text name="contractor.format.number" >
											<s:param name="rate" value='%{#attr.currentRow.totalOutstandingAmt}' />
										</s:text>
				  					</s:else>
								</td>
							</tr>
						</s:if>
					</table>
			</display:column>

            <display:column title="<table width=100% border=1 cellpadding=0 cellspacing=0><tr><td align=center>Status of Project</td></tr></table>"
			   titleKey="workprogress.register.estimate.amount"
			   style="text-align:left" >
					<table width=100% cellpadding=0	cellspacing=0>
						<tr>
							<td align="left" >
				  					<s:property  value='%{#attr.currentRow.projectStatus}' />
							</td>
						</tr>
					</table>
			</display:column>
			
	   </display:table>
 	</div>
</s:if>  

	<s:if test="%{searchResult.fullListSize == 0}">
		  <div>
			<table width="100%" border="0" cellpadding="0" 	cellspacing="0">
				<tr>
				   <td align="center"> 
				    <s:if test="%{sourcePage!=null && sourcePage=='deptWiseReportForWP'}">
						 <font color="red"><s:text name="workprogress.search.no.wo.found" /></font>
					</s:if>
					<s:else>
						<font color="red"><s:text name="workprogress.search.no.record.found" /></font>
					</s:else>
				   </td>
			    </tr>
			</table>
		  </div>
	</s:if>  
	<s:if test="%{searchResult.fullListSize != 0}">
 	   <div class="buttonholderwk">
  			<br/>
	    		<input type="button" class="buttonpdf"
					value="VIEW PDF" id="viewpdf"
					name="viewpdf" onclick="viewPDF();"
					align="center" />

	    		<input type="button" class="buttonpdf"
					value="VIEW EXCEL" id="viewexcel"
					name="viewpdf" onclick="viewExcel()"
					align="center" />
	   </div> 
	</s:if>
</div>	
 
