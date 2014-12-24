<%@ include file="/includes/taglibs.jsp" %>
<html>
	<head>
		<title>Payslip Scheduled Job Detail</title>
	
		<script language="JavaScript"  type="text/JavaScript">
		</script>    
	</head>
	
	<body onload="">
	
		<s:form name="payslipScheduledJobDetail" action ="payslipScheduledJobs" theme="simple">
		<s:token/>
			<center>
			
			
					
								<table width="95%" cellpadding="0" cellspacing="0" border="0" id="scheduledJobTable">
									<span id="msg" style="height:1px">
										<s:actionerror cssStyle="font-size:10px;font-weight:bold;" cssClass="mandatory"/>  
										<s:fielderror cssStyle="font-size:10px;font-weight:bold;" cssClass="mandatory"/>
										<s:actionmessage cssClass="actionmessage"/>
									</span>
								    
									<tr>
										<td colspan="4" class="headingwk">
											<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
							              	<div class="headplacer"><s:text name="PayslipScheduledJobDtl.Heading"/></div>
							        	</td>
								    </tr>
    
									<s:if test="batchGenDtl.schJobName!=null">
							        	<tr>
											<td class="greyboxwk"><s:text name="ScheduledJob.JobName"/> &nbsp;:&nbsp;</td>
											<td class="greybox2wk">
												 <s:property value="%{batchGenDtl.schJobName}" />
											</td>
											<td class="greyboxwk"><s:text name="ScheduledJob.JobGroupName"/> &nbsp;:&nbsp;</td>
											<td class="greybox2wk">
												 <s:property value="%{batchGenDtl.schJobGroupName}" />
											</td>
										</tr>

							        	<tr>
											<td class="whiteboxwk"><s:text name="ScheduledJob.Dept"/> &nbsp;:&nbsp;</td>
											<td class="whitebox2wk">
												 <s:property value="%{batchGenDtl.department.deptName}"/>
											</td>
											<td class="whiteboxwk"><s:text name="ScheduledJob.BillNumber"/> &nbsp;:&nbsp;</td>
											<td class="whitebox2wk">
												 <s:property value="%{batchGenDtl.billNumber.billNumber}" />
											</td>
										</tr>
										
										<tr>
											<td class="greyboxwk"><s:text name="ScheduledJob.FromDate"/> &nbsp;:&nbsp;</td>
											
											<td class="greybox2wk">
												  <s:date  name="%{batchGenDtl.fromDate}" format="dd/MM/yyyy" />
											</td>
											<td class="greyboxwk"><s:text name="ScheduledJob.ToDate"/> &nbsp;:&nbsp;</td>
											<td class="greybox2wk">
												  <s:date  name="%{batchGenDtl.toDate}" format="dd/MM/yyyy" />
											</td>
										</tr>
										
										<tr>
											<td class="whiteboxwk"><s:text name="ScheduledJob.ScheduledTime"/> &nbsp;:&nbsp;</td>
											<td class="whitebox2wk">
												 <s:date  name="%{batchGenDtl.createdDate}" format="dd/MM/yyyy hh:mm:ss" />
											</td>
											<td class="whiteboxwk"><s:text name="ScheduledJob.ScheduledBy"/> &nbsp;:&nbsp;</td>
											<td class="whitebox2wk">
												 <s:property value="%{batchGenDtl.createdBy.userName}" />
											</td>
										</tr>
										
										<tr>
											<td class="greyboxwk"><s:text name="ScheduledJob.Success"/> &nbsp;:&nbsp;</td>
											<td class="greybox2wk">
												 <s:property value="%{batchGenDtl.succCount}" />
											</td>
											<td class="greyboxwk"><s:text name="ScheduledJob.Failure"/> &nbsp;:&nbsp;</td>
											<td class="greybox2wk">
												 <s:property value="%{batchGenDtl.failCount}" />
											</td>
										</tr>
										
										<tr>
											<td class="whiteboxwk"><s:text name="ScheduledJob.Status"/> &nbsp;:&nbsp;</td>
											<td class="whitebox2wk">
												 <s:property value="%{jobStatus}" />
											</td>
											<td class="whiteboxwk"><s:text name="ScheduledJob.Functionary"/> &nbsp;:&nbsp;</td>
											<td class="whitebox2wk">
												 <s:property value="%{batchGenDtl.functionary.name}" />
											</td>
										</tr>
								    </s:if>
								    <s:else>
									    <tr>
										    <td>
										    <font color="red" size="2" >Nothing found to display</font>
										    </td>
									    </tr>
								    </s:else>
								</table>
							
		</s:form>
	</body>
</html>