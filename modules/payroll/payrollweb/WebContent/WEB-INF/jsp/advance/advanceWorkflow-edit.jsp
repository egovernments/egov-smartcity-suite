<%@ include file="/includes/taglibs.jsp" %>

<html>
	<head>
		<s:if test="%{wfStatus=='FINAL_APPROVED'}">
			<title>Create Advance BPV</title>
		</s:if>
		<s:else>
		
		<title>Advance Sanction/Reject </title>
		</s:else>
		
		<script language="JavaScript"  type="text/JavaScript">

		function modifyAdvanceInfo(advanceId,empId)
		{			
		    var url='${pageContext.request.contextPath}/salaryadvance/modifyAdvanceInfo.do?salAdvId='+advanceId+'&selectedEmp='+empId+'&mode=modify';
			window.open(url,'ModifyAdvance','width=800,height=600,top=150,left=80,scrollbars=yes,resizable=yes');
	    }
	    
	    function viewAdvanceInfo(advanceId,empId)
		{			
		    var url='${pageContext.request.contextPath}/salaryadvance/modifyAdvanceInfo.do?salAdvId='+advanceId+'&selectedEmp='+empId+'&mode=view';
			window.open(url,'ViewAdvance','width=800,height=600,top=150,left=80,scrollbars=yes,resizable=yes');
	    }
    
    	function refreshInbox()
    	{
    		if(opener.top.document.getElementById('inboxframe')!=null)
    		{    	
    			if(opener.top.document.getElementById('inboxframe').contentWindow.name!=null && opener.top.document.getElementById('inboxframe').contentWindow.name=="inboxframe")
    			{    		
    				opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
    			}
    		}
    	}
	    
	    function generateBPV()
	    { 
		     window.location=document.getElementById('bpvActionUrl').value +document.getElementById('salaryARFId').value;
		}
	    function validate()
	    {
	    	
	    	if(document.getElementById('comments').value!="")
	    	{
		    	if(document.getElementById('comments').value.length>1000)
		    	{
		    		document.getElementById("commentsErrSpn").style.display = "";
		    		return false;
		    	}
		    	else{
		    		document.getElementById("commentsErrSpn").style.display = "none";
		    		return true;
		    	}
		    }
	    }
	    </script>    
	</head>

	<body onload="refreshInbox();">
	
		<s:form name="advanceApproveRejectForm" action ="advanceWorkflow"  theme="simple" onsubmit="return validate();">
			<s:hidden name="isSalaryARF" id ="isSalaryARF" value="%{isSalaryARF}"/>
			<div align="left">
				<s:actionerror cssClass="mandatory"/>
				<s:fielderror cssClass="mandatory"/>
			</div>
			<s:actionmessage />
			<span align="center" style="display:none" id="commentsErrSpn">
			  <li>
			     <font size="2" color="red"><b>
			         Maximum of 1024 Characters allowed in Comments
			     </b></font>
			  </li>
			</span>
			
			<s:if test='advanceList!=null'>
				<s:if test="%{wfStatus=='CREATED'}">
					<display:table name="advanceList" uid="currentRowObject" pagesize = "30"   cellpadding="1" cellspacing="2" export="false" style="background-color:#e8edf1;padding:0px;margin:10 0 5 5px;width:135%;overflow-x:auto" requestURI="">
						<display:caption>&nbsp;</display:caption>	
						<display:column property="advance.employee.employeeCode" title="Employee Code " style="width:10%;"/>
						<display:column property="advance.employee.employeeName" title="Employee Name " style="width:15%;"/>
						<display:column  property="advance.salaryCodes.head" title="Advance" style="padding:1px;width:10%;" />
						<display:column  property="advance.numOfInst" title="No. Of Inst. "  style="text-align:center;padding:1px;width:7%;"/>
						<display:column  property="advance.advanceAmt" style="text-align:right;padding:1px;width:9%;" title="Advance Amount"  />
						<display:column  property="advance.instAmt" title="Inst. Amt"  style="text-align:right;padding:1px;width:6%;"/>
						<display:column  style="text-align:center;width:10%;padding-left:3px;" title="Action"  >
					   		<a href="#" onclick="modifyAdvanceInfo(<s:property  value='#attr.currentRowObject.advance.id'/>,<s:property  value='#attr.currentRowObject.advance.employee.idPersonalInformation'/>)"> Modify Advance</a>
						</display:column>
						<display:column  title="Sanction No" style="padding:1px;"><s:textfield maxlength="32" size="15" name="sanctionNum" id="sanctionNum" /></display:column>
						<display:column  title="Sanction/Reject Date" style="padding:1px;width:12%;" >
							<input type="text" name="sanctionDate" id="sanctionDate" maxlength="10" size="12" value="" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);"/>
							<a href="javascript:show_calendar('forms[0].sanctionDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img id="sanctionDateImg" src="${pageContext.request.contextPath}/common/image/calendar.png" width="15" height="15" border="0"></a>
						</display:column>
						
						<display:column  title="Sanction/Reject By" style="padding:1px;width:12%;">
							<s:label name="sanctionOrRejectBy.userName" />
						</display:column>
						<display:column  title="Comments" style="padding:1px;"><s:textarea label="title" cols="40" rows="2" name="comments" id="comments" value="" /></display:column>
						<display:column>
				  			<s:if test="%{isSalaryARF=='false'}">
				  				<input type="hidden" name="advanceId" id="advanceId" value='<s:property  value="#attr.currentRowObject.advance.id"/>' />
				  			</s:if>
				  			<s:if test="%{isSalaryARF=='true'}">
				  				<input type="hidden" name="salaryARFId" id="salaryARFId" value='<s:property  value="#attr.currentRowObject.advance.salaryARF.id"/>' />
				  			</s:if>
				  		</display:column>
					  	<display:column  title=""  style="width:3%;"/>
					  	<div STYLE="display:table-header-group;">			      
					  		<display:setProperty name="basic.show.header" value="true" />
					  		<display:setProperty name="paging.banner.placement" value="" />
						</div>
					</display:table>
				</s:if>
				<s:elseif test="%{wfStatus=='FINAL_APPROVED'}">
				  	<display:table name="advanceList" uid="currentRowObject" pagesize = "10"   cellpadding="1" cellspacing="2" export="false" style="align:center;background-color:#e8edf1;padding:0px;margin:10 0 5 100px;width:70%;overflow-x:auto" requestURI="">
				  		<display:caption>&nbsp;</display:caption>	
						<display:column property="advance.employee.employeeCode" title="Employee Code " style=""/>
						<display:column property="advance.employee.employeeName" title="Employee Name " style=""/>
						<display:column  property="advance.salaryCodes.head" title="Advance" style="padding:1px;" />
						<display:column  property="advance.numOfInst" title="No. Of Inst. "  style="text-align:center;padding:1px;"/>
						<display:column  property="advance.advanceAmt" title="Advance Amount" style="text-align:right;padding-right:16px;width:15%;" />
						<display:column  property="advance.instAmt" title="Inst. Amt"  style="text-align:right;padding-right:4px;width:10%;"/>
						<display:column  style="text-align:center;width:10%;padding-left:3px;" title="Action"  >
					   		<a href="#" onclick="viewAdvanceInfo(<s:property  value='#attr.currentRowObject.advance.id'/>,<s:property  value='#attr.currentRowObject.advance.employee.idPersonalInformation'/>)"> View Advance</a>
						</display:column>
						<display:column>
				  			<input type="hidden" name="salaryARFId" id="salaryARFId" value='<s:property  value="#attr.currentRowObject.advance.salaryARF.id"/>' />
				  		</display:column>
					  	<div STYLE="display:table-header-group;">			      
					  		<display:setProperty name="basic.show.header" value="true" />
					  		<display:setProperty name="paging.banner.placement" value="" />
						</div>
					</display:table>
				</s:elseif>
				  	
		        <s:if test="%{advanceWfType=='Manual'}">   		
  						<s:if test='%{wfStatus=="CREATED" && isSalaryARF=="true"}'>
  							<%@ include file='/WEB-INF/jsp/payslip/manualWfApproverSelection.jsp'%>	
  						</s:if>
  				</s:if>
		        <tr>
		   			<td colspan="7" align="center"/>
	   			</tr>
		        <tr>
		   			<td>
		   				<DIV STYLE="border:0 #000000 solid;padding:10px">
		   				<s:if test="%{wfStatus=='CREATED'}">
		   				<s:submit name="button1" type="submit" cssClass="buttonfinal" id="button1" method="approveAdvance" value="Approve"/>
		   				<s:submit name="button2" type="submit" cssClass="buttonfinal" id="button2" method="rejectAdvance" value="Reject"/>
		   				</s:if>
		   				<s:elseif test="%{wfStatus=='FINAL_APPROVED'}">
		   					<s:hidden id="bpvActionUrl" name="bpvActionUrl" value="%{bpvActionUrl}"/>
		   					<input type="button"  class="buttonfinal" id="createBPV" name="createBPV" onclick="generateBPV();" value="Create BPV" >
		   				</s:elseif>
		   				<input name="closeBut" type="button" class="buttonfinal" id="closeBut" onclick="window.close()" value="Close"/>
		   				</DIV>
		   			</td>
	   			</tr>
	        </s:if>	
	        <s:if test='advanceList==null'>
		        <table>
		         	<tr>
			   			<td colspan="7" align="center" style="width:1000px">
			   				<input name="closeBut" type="button" class="button" id="closeBut" onclick="window.close()" value="Close"/>
		   				</td>
		   			</tr>
		        </table>
	        </s:if>
		</s:form>
	</body>
</html>
