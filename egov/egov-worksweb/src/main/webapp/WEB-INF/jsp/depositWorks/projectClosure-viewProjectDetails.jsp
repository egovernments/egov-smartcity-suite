<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<html>
  <head>
      <title><s:text name='project.closure.project.details'/></title>
  </head>
<style type="text/css">
	td.amount3wka {
	font-size: 11px;
	padding-top: 3px;
	padding-bottom: 3px;
	padding-right: 5px;
	padding-left: 5px;
	color: #333333;
	text-align: right;
	font-family: Arial, Helvetica, sans-serif;
	white-space: nowrap;
	border-right-width: 1px;
	border-bottom-width: 1px;
	border-left-width: 1px;
	border-right-style: solid;
	border-bottom-style: solid;
	border-left-style: solid;
	border-right-color: #D1D9E1;
	border-bottom-color: #D1D9E1;
	border-left-color: #D1D9E1;
}
</style>

	<script src="<egov:url path='js/works.js'/>"></script>
	<script src="<egov:url path='js/helper.js'/>"></script>
	
  	<script type="text/javascript">
  		var warnings=new Array();
		warnings['improperProjectCodeSelection']='<s:text name="estimate.depositCode.warning.improperProjectCodeSelection"/>'

		function roundAmount(){
			if(dom.get("workValue")!=null){
				dom.get("workValue").value=roundTo(eval(dom.get("workValue").value));
			}
			if(dom.get("totalExpense")!=null){
				dom.get("totalExpense").value=roundTo(eval(dom.get("totalExpense").value));
			}
		}
		function enableFields(){
			for(i=0;i<document.projectClosureForm.elements.length;i++){
	        	document.projectClosureForm.elements[i].disabled=false;
			}
		} 	

	 	function validateSubmit(){
			clearMessage('projectClosureError');
			document.getElementById("projectClosureError").style.display='none';

			if(dom.get("completionDate").value==''){
				document.getElementById("projectClosureError").style.display='';
        		document.getElementById("projectClosureError").innerHTML='<s:text name="projectCompletionReport.completion.date.null" />';
        		return false;
			}
	  		if(validateDate()){
	  			enableFields();
	  			return true;
	  		}
	  		else{
				return false;
	  		}
	  	}
		
		function dateChange(){
			var error=false;
			clearMessage('projectClosureError');
			document.getElementById("projectClosureError").style.display='none';
			validateDateFormat(dom.get("completionDate"));
			var links=document.projectClosureForm.getElementsByTagName("span");
			for(i=0;i<links.length;i++) {
        		if(links[i].innerHTML=='x' && links[i].style.display!='none'){
        			error=true;
					document.getElementById("projectClosureError").style.display='';
        			document.getElementById("projectClosureError").innerHTML='<s:text name="contractor.validate_x.message" />';
     		      	break;
        		}
    		}
			if(error){
				return false;
			}
			return true;
		}
		function validateDate(){
			if(dom.get("completionDate").value==''){
				return false;
			}
			clearMessage('projectClosureError');
			document.getElementById("projectClosureError").style.display='none';
	  		var currentDate=getCurrentDate();
	  		var completionDate=dom.get("completionDate").value;
	  		var lastVoucherDate='<s:property value="lastVoucherDate" />';
			if(!compareDate(completionDate,currentDate)){
				document.getElementById("projectClosureError").style.display='';
        		document.getElementById("projectClosureError").innerHTML='<s:text name="projectCompletionReport.completion.date.error1"/>';
				return false;
			}
			if(!compareDate(lastVoucherDate,completionDate)){
				document.getElementById("projectClosureError").style.display='';
        		document.getElementById("projectClosureError").innerHTML='<s:text name="projectCompletionReport.completion.date.error2"/>'+':'+lastVoucherDate;
				return false;
			}
	  		return true;
		}
		
	function compareDate(obj1,obj2){
		if(obj1=='' || obj2==''){
			return false;
		}
		var dt1  = parseInt(obj1.substring(0,2),10);
		var mon1 = parseInt(obj1.substring(3,5),10);
		var yr1  = parseInt(obj1.substring(6,10),10);
		var date1 = new Date(eval(yr1), eval(mon1)-1,eval(dt1));
		var dt2  = parseInt(obj2.substring(0,2),10);
		var mon2 = parseInt(obj2.substring(3,5),10);
		var yr2  = parseInt(obj2.substring(6,10),10);
		var date2 = new Date(eval(yr2),eval(mon2)-1,eval(dt2)); 
		if(date2 < date1){
         	return false;
		}else{
			return true;
		} 
	}
  </script> 	
   <body onload="roundAmount();">
      	<s:if test="%{hasErrors()}">
        	<div class="errorstyle" >
          		<s:actionerror/>
          		<s:fielderror/>
        	</div>
    	</s:if>    

	<s:form theme="simple" name="projectClosureForm" id="projectClosureForm"> 
  	 	<input type="hidden" name="isEnableSelect" id="isEnableSelect"/>
    		<div id="projectClosureError" class="errorstyle" style="display:none;"></div>
 			<s:hidden id="projectCodeId" name="projectCodeId" value="%{projectDetails.estimate.projectCode.id}"/>
    		<div class="formmainbox">
 			<div class="insidecontent"> 
 				<div class="rbroundbox2">
 					<div class="rbtop2"><div></div></div>
 					<div class="rbcontent2">
 					</div>
 	
					<br/>
 					<div class="rbcontent2">
    						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td  class="headingwk" align="left">
									<div class="arrowiconwk">
										<img src="${pageContext.request.contextPath}/image/arrow.gif" />
									</div>
									<div class="headerplacer">
										<s:text name='project.closure.project.details' />
									</div>
								</td>
							</tr>
							<s:if test="%{projectDetails.keySet().size() != 0}">
							<tr>
								<td>
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
				        						<td class="greyboxwk" >
				        							<s:text name="projectCompletionReport.project.code" />
		        		 						</td>
		        								<td class="greybox2wk" >
		        									<s:textfield  value="%{projectDetails.estimate.projectCode.code}" id="projectCode" cssClass="selectwk" />
		         								</td>
				        						<td class="greyboxwk" >
				        							<s:text name="projectCompletionReport.estimate.number" />
		        		 						</td>
		        								<td class="greybox2wk" >
		        									<s:textfield  value="%{projectDetails.estimate.estimateNumber}" id="estimateNum" cssClass="selectwk" />
		         								</td>
                  							</tr>
                  							<tr>
				        						<td class="whiteboxwk" >
				        							<s:text name="depositworks.deposit.code" />
		        		 						</td>
		        								<td class="whitebox2wk" >
		        									<s:textfield  value="%{projectDetails.estimate.depositCode.code}" id="estimateDepositCode" cssClass="selectwk" />
		         								</td>
				        						<td class="whiteboxwk" >
				        							<s:text name="projectCompletionReport.estimate.workname" />
		        		 						</td>
		        								<td class="whitebox2wk" >
		        									<s:textarea cols="30" rows="2" cssClass="selectwk" id="name" value="%{projectDetails.estimate.name}" />
		         								</td>
                							</tr>
				  							<tr>
				        						<td class="greyboxwk" >
				        							<s:text name="projectCompletionReport.estimate.amount" />
		        		 						</td>
		        								<td class="greybox2wk" >
		        									<s:textfield  value="%{projectDetails.estimate.totalAmount.value}" id="workValue" cssClass="amount" />
		         								</td>
				        						<td class="greyboxwk" >
				        							<s:text name="projectCompletionReport.project.expenditure" />
		        		 						</td>
		        								<td class="greybox2wk" align="left" >
		        									<s:textfield  name="projectValue" value="%{projectDetails.totalExpense}" id="totalExpense" cssClass="amount" />
		        									<s:if test="%{projectDetails.totalExpense>projectDetails.estimate.totalAmount.value}" >
			        									<div id="excessAmountMsg" style="font-size: 10px;color: #F00;font-weight: bold;">
			        										<s:text name="projectCompletionReport.excess.amount.error" /> :&nbsp;<s:text name="contractor.format.number" >
			        											<s:param name="rate" value='%{projectDetails.totalExpense-projectDetails.estimate.totalAmount.value}' />
			        										</s:text>
			        									</div>
		        									</s:if>
		         								</td>
                							</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td><br/>
									<div>
										<table width="40%" border="0" cellpadding="0" cellspacing="0" align="left">
											<tr>
												<td width="4" class="tablesubheadwk">
													<s:text name="column.title.SLNo" />
												</td>
												<td width="12" class="tablesubheadwk">
														<s:text name="contractorBill.billNumber" />
												</td>
												<td width="14" class="tablesubheadwk">
														<s:text name="projectCompletionReport.project.voucherNumber" />
												</td>
												<td width="10" class="tablesubheadwk" align="right">
														<s:text name="projectCompletionReport.project.voucher.amount" />
												</td>
											</tr>
    										<s:iterator var="voucher" value="projectDetails.voucherDetails" status="status">  
												<tr>
													<td width="4" class="whitebox3wka">
														<s:property value="#status.count" />
													</td>
													<td width="12" class="whitebox3wka">
		    												<s:property value="#voucher.billNumber" />
													</td>
													<td width="14" class="whitebox3wka">
		    												<s:property value="#voucher.voucherNumber" />
													</td>
													<td width="10" class="amount3wka" >
														<s:text name="contractor.format.number" >
															<s:param name="rate" value='%{#voucher.amount}' />
														</s:text>
													</td>
												</tr>
											</s:iterator>
												<tr>
													<td width="4" class="whitebox3wka">
														&nbsp;
													</td>
													<td width="12" class="whitebox3wka">
		    												&nbsp;
													</td>
													<td width="14" class="whitebox3wka">
		    												<b><s:text name="projectCompletionReport.project.voucher.total" /></b>
													</td>
													<td width="10" class="amount3wka" >
														<s:text name="contractor.format.number" >
															<s:param name="rate" value='%{projectDetails.totalExpense}'/>
														</s:text>
													</td>
												</tr>
										</table>
									</div>
								</td>
							</tr>
						</s:if>
					</table>
					<br/>
						<s:if test="%{projectDetails.keySet().size() != 0}">
							<table>
								<tr>
									<td class="whiteboxwk">
										<span class="mandatory">*</span><s:text name="projectCompletionReport.date" />:
									</td>
									<td  colspan="3" class="whitebox2wk">
										<s:date name="completionDate" var="completionDateFormat" format="dd/MM/yyyy"/><s:textfield name="completionDate" value="%{completionDateFormat}" id="completionDate" cssClass="selectwk" onBlur="dateChange();validateDate();" onChange="dateChange();validateDate();"  onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
										<a href="javascript:show_calendar('forms[0].completionDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;">
											<img src="${pageContext.request.contextPath}/image/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" />
										</a>
										<span id="errorcompletionDate" style="display:none;color:red;font-weight:bold">x</span>
									</td>
								</tr>
							</table>
							<div class="buttonholderwk" >
								<!-- <input type="button" onclick="return validateBeforeSubmit()" class="buttonfinal" value="Close Project" id="closeprojectbutton" name="closeprojectbutton"/> -->
								<s:submit type="submit" cssClass="buttonfinal" value="Close Project" id="closeprojectbutton" name="%{name}" method="close" onclick="return validateSubmit();"/>
							</div>
						</s:if>
						<br/>
					</div>
				</div>
			</div>
		</div>
   	</s:form>
  <script>
  	<s:if test="%{projectDetails.keySet().size() != 0}">
		toggleForSelectedFields(true,['projectCode','estimateNum','name','workValue','totalExpense','estimateDepositCode']);
	</s:if>
  </script>
  </body>
</html>
