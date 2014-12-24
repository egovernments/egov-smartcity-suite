<%@ include file="/includes/taglibs.jsp" %>
<html>
	<head>
		<title>Payslip Scheduled Jobs</title>
		
		
		<script language="JavaScript"  type="text/JavaScript">
		function deleteSchedule(rowObj,jobGroupName,jobName)
		{
			var tbl=document.getElementById("scheduledJobTable");
   			var tbody=tbl.tBodies[0];
		 	var curRowIndex = getRow(rowObj).rowIndex;
		 	if(rowObj.checked)
		 	{
			 	document.getElementsByName("deleteJobGroupName")[curRowIndex].value=jobGroupName;
			 	document.getElementsByName("deleteJobName")[curRowIndex].value=jobName;
		 	}
		 	else
		 	{
		 		document.getElementsByName("deleteJobGroupName")[curRowIndex-1].value="";
			 	document.getElementsByName("deleteJobName")[curRowIndex-1].value="";
		 	}
		}
		
		function showJobInfo(jobName,jobGroupName,jobStatus)
		{
			//alert('jobName---'+jobName+'---jobGroupName---'+jobGroupName);
			var url='${pageContext.request.contextPath}/payslip/payslipScheduledJobs!showJobDetails.action?jobName='+jobName+'&jobGroupName='+jobGroupName+'&jobStatus='+jobStatus;
			//alert(url);
			window.open(url,'JobInfo','width=800,height=600,top=150,left=80,scrollbars=yes,resizable=yes');
		}
		
		function validation()
		{
			var tbl=document.getElementById("currentRowObject");
   			var tbody=tbl.tBodies[0];
   			var rowCount = tbl.rows.length-1;
		    var isChecked=false;
			var isAllCheckboxDisabled=true;
			for (var i=0; i<rowCount; i++)
			{	
				if(!document.getElementsByName("delSchedule")[i].disabled)
				{
					isAllCheckboxDisabled=false;
					break;
				}
			}
			
			if(isAllCheckboxDisabled)
			{	
				alert("No schedule can be deleted,Since its not in Created/Failed status");
				return false;
			}
			
			for (var j=0; j<rowCount; j++)
			{				    
				if(!document.getElementsByName("delSchedule")[j].disabled)
				{
					if(document.getElementsByName("delSchedule")[j].checked){
						isChecked=true;
						break;
					}
				}
			}
			
			if(isChecked){
				document.getElementById("delSchBut").style.display="none";
			}else{
				alert("Please select the job to delete");
			}
			
			return isChecked;
			
		}
	
		</script>    
	</head>
	
	<body onload="">
	
		<s:form name="payslipScheduledJobs" action ="payslipScheduledJobs" onsubmit="return validation()" theme="simple">
		<s:token/>
						<table width="100%" cellpadding="0" cellspacing="0" border="0" id="scheduledJobTable">
							<span id="msg" style="height:1px">
								<s:actionerror cssStyle="font-size:10px;font-weight:bold;" cssClass="mandatory"/>  
								<s:fielderror cssStyle="font-size:10px;font-weight:bold;" cssClass="mandatory"/>
								<s:actionmessage cssClass="actionmessage"/>
							</span>

							<tr>
								<td colspan="8" class="headingwk">
									<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
							<div class="headplacer"><s:text name="PayslipScheduledJob.Heading"/></div>
							</td>
						    </tr>
						  
							<s:if test="schJobs null">
							    <tr>
								    <td>
								    <font color="red" size="2" >Nothing found to display</font>
								    </td>
							    </tr> 
							</s:if>	
						    <s:else>
								<display:table  name="schJobs" export="false"  requestURI="" uid="currentRowObject"  style="width:1100px;" >
									<s:if test="%{#attr.currentRowObject[4]=='COMPLETED'||#attr.currentRowObject[4]=='RUNNING'}">
										<display:column >
											<input type="checkbox" name="delSchedule" disabled="true">
										</display:column>	
									</s:if>
									<s:else>
										<display:column >
											<input type="checkbox" name="delSchedule" onclick='deleteSchedule(this,"<s:property value="#attr.currentRowObject[1]" />","<s:property value="#attr.currentRowObject[0]" />")'/>
										</display:column>
									</s:else>
									 <display:column class="hidden" headerClass="hidden"  media="html">
									<s:hidden name="deleteJobGroupName" value=""/>
									<s:hidden name="deleteJobName" value=""/>
									</display:column>
									<display:column style="text-align:left;" title="SlNo"><s:property value="#attr.currentRowObject_rowNum + (page-1)*pageSize" />
									</display:column>
									<display:column style="text-align:left;" title="JobName" >
									<a href="#" onclick="showJobInfo('<s:property value="#attr.currentRowObject[0]"/>','<s:property value="#attr.currentRowObject[1]"/>','<s:property value="#attr.currentRowObject[4]"/>')">
											<s:property value="#attr.currentRowObject[0]"/></a>
									</display:column>
									<display:column style="text-align:left;" title="Job Group Name" property="[1]"/>
									<display:column style="text-align:left;" title="Status" property="[4]"/>
									<display:column style="text-align:left;" title="Start Date" property="[2]"/>
									<display:column style="text-align:left;" title="Next Fire Date" property="[3]"/>
								</display:table>	
						    </s:else>
						</table>
		<div class="buttonholderwk">
				<s:submit name="delSchBut" id="delSchBut" value="Delete Scheduled Jobs"  method="deleteScheduledJobs" cssClass="buttonfinal" />
			<input type="button" value="Close" onclick="javascript:window.close()" class="buttonfinal"/> 
		</div>


		</s:form>
	</body>
</html>
