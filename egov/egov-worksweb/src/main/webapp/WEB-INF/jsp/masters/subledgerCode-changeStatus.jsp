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
  <head>
      <title><s:text name='page.title.projectCompletionReport'/></title>
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

	<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script>
	
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
			for(i=0;i<document.projectCompletionReportForm.elements.length;i++){
	        	document.projectCompletionReportForm.elements[i].disabled=false;
			}
		} 	

		function validateSearch(){
			clearMessage('projectCompletionReportError');
			document.getElementById("projectCompletionReportError").style.display='none';
			if(dom.get("projectCodeId").value=='' || dom.get("projectCodeSearch").value==''){
				document.getElementById("projectCompletionReportError").style.display='';
        		document.getElementById("projectCompletionReportError").innerHTML='<s:text name="projectCompletionReport.project.code.null" />';
				return false;
			}
			return true;
		}
	 	function validateSubmit(){
			clearMessage('projectCompletionReportError');
			document.getElementById("projectCompletionReportError").style.display='none';

			if(dom.get("completionDate").value==''){
				document.getElementById("projectCompletionReportError").style.display='';
        		document.getElementById("projectCompletionReportError").innerHTML='<s:text name="projectCompletionReport.completion.date.null" />';
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
			clearMessage('projectCompletionReportError');
			document.getElementById("projectCompletionReportError").style.display='none';
			validateDateFormat(dom.get("completionDate"));
			var links=document.projectCompletionReportForm.getElementsByTagName("span");
			for(i=0;i<links.length;i++) {
        		if(links[i].innerHTML=='x' && links[i].style.display!='none'){
        			error=true;
					document.getElementById("projectCompletionReportError").style.display='';
        			document.getElementById("projectCompletionReportError").innerHTML='<s:text name="contractor.validate_x.message" />';
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
			clearMessage('projectCompletionReportError');
			document.getElementById("projectCompletionReportError").style.display='none';
	  		var currentDate=getCurrentDate();
	  		var completionDate=dom.get("completionDate").value;
	  		var lastVoucherDate='<s:property value="lastVoucherDate" />';
			if(!compareDate(completionDate,currentDate)){
				document.getElementById("projectCompletionReportError").style.display='';
        		document.getElementById("projectCompletionReportError").innerHTML='<s:text name="projectCompletionReport.completion.date.error1"/>';
				return false;
			}
			if(!compareDate(lastVoucherDate,completionDate)){
				document.getElementById("projectCompletionReportError").style.display='';
        		document.getElementById("projectCompletionReportError").innerHTML='<s:text name="projectCompletionReport.completion.date.error2"/>'+':'+lastVoucherDate;
				return false;
			}
	  		return true;
		}
		function clearProjectCode(){
			dom.get("projectCodeId").value="";
		}
		
        var projectCodeSearchSelectionHandler = function(sType, arguments){ 
        	var oData = arguments[2];
            dom.get("projectCodeId").value = oData[1];
        }
        
	var projectCodeSelectionEnforceHandler = function(sType, arguments){
	    warn('improperProjectCodeSelection');
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
	function closeCurrentWindow()
	{
		var closeObj = document.getElementById("closeprojectbutton");
		if(closeObj!=null)
		{
			confirmClose('<s:text name='projectCompletionReport.close.confirm'/>');
		}
		else
		{
			window.close();
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

	<s:form theme="simple" name="projectCompletionReportForm" id="projectCompletionReportForm"> 
  	 	<input type="hidden" name="isEnableSelect" id="isEnableSelect"/>
    		<div id="projectCompletionReportError" class="errorstyle" style="display:none;"></div>
 
    		<div class="formmainbox">
 			<div class="insidecontent"> 
 				<div class="rbroundbox2">
 					<div class="rbtop2"><div></div></div>
 					<div class="rbcontent2">
 						<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
							<tr>
								<td colspan="4">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td class="headingwk" align="left">
												<div class="arrowiconwk">
													<img src="/egworks/resources/erp2/images/arrow.gif" />
												</div>
												<div class="headerplacer">
													<s:text name='title.search.criteria' />
												</div>
											</td>
										</tr>
									</table>
								</td>
							</tr>
 							<tr>
								<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="projectCompletionReport.projectCode" />
								</td>
	            						<td class="whitebox2wk">
	                						<div class="yui-skin-sam">
	                							<div id="projectCodeSearch_autocomplete">
	                								<div>
												<s:textfield id="projectCodeSearch" name="code" value="%{code}" disabled="%{isEnableSelect}" class="selectwk" /><s:hidden id="projectCodeId" name="projectCodeId" value="%{projectCodeId}"/>
											</div>
	                								<span id="projectCodeSearchResults"></span>
	                							</div>
	                						</div>
	                						<egov:autocomplete name="projectCodeSearch" width="20" field="projectCodeSearch" url="ajaxSubledgerCode!searchProjectCode.action?" queryQuestionMark="false" results="projectCodeSearchResults" handler="projectCodeSearchSelectionHandler" forceSelectionHandler="projectCodeSelectionEnforceHandler"/> 
	                						<span class='warning' id="improperProjectCodeSelectionWarning"></span> 
	            						</td> 
		        					<td class="whiteboxwk" >
		        						<s:text name="projectCompletionReport.estimate.number" />
		         					</td>
		        					<td class="whitebox2wk" >
		        						<s:textfield  name="estimateNumber" value="%{estimateNumber}" id="estimateNumber" cssClass="selectwk" />
		         					</td>
		    					</tr>
 						</table>
 					</div>
 	
 					<div id="mandatary" align="right" class="mandatory" style="font-size: 11px; padding-right: 20px;">*
						<s:text name="message.mandatory" />
					</div>
 	 	
 					<div class="buttonholderwk" id="slCodeButtons">
						<s:submit cssClass="buttonadd" value="SEARCH" id="searchButton" name="searchButton" method="searchProjectDetails" onclick="return validateSearch();"/>
	  					&nbsp;
	  					<input type="button" class="buttonfinal" value="CLEAR" id="clearButton" name="clearButton" onclick="window.open('${pageContext.request.contextPath}/masters/subledgerCode!changeStatus.action','_self');"/>
	  					&nbsp;
	 					<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="closeCurrentWindow();"/>
    				</div>
					<br/>
 					<div class="rbcontent2">
    						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td></td>
							</tr>
							<s:if test="%{projectDetails.keySet().size() != 0}">
							<tr>
								<td>
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td  class="headingwk" align="left">
												<div class="arrowiconwk">
													<img src="/egworks/resources/erp2/images/arrow.gif" />
												</div>
												<div class="headerplacer">
													<s:text name='search.result' />
												</div>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
				        						<td class="whiteboxwk" >
				        							<s:text name="projectCompletionReport.project.code" />
		        		 						</td>
		        								<td class="whitebox2wk" >
		        									<s:textfield  value="%{projectDetails.estimate.projectCode.code}" id="projectCode" cssClass="selectwk" />
		         								</td>
				        						<td class="whiteboxwk" >
				        							<s:text name="projectCompletionReport.estimate.number" />
		        		 						</td>
		        								<td class="whitebox2wk" >
		        									<s:textfield  value="%{projectDetails.estimate.estimateNumber}" id="estimateNum" cssClass="selectwk" />
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
		        								<td class="greybox2wk" colspan="3" align="left" >
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
											<img src="/egworks/resources/erp2/images/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" />
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
		toggleForSelectedFields(true,['projectCode','estimateNum','name','workValue','totalExpense']);
	</s:if>
  </script>
  </body>
</html>
