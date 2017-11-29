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
      <title><s:text name='page.title.depositWorksFolioReport'/></title>
  </head>
	<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script>
  	<script type="text/javascript">
  		var warnings=new Array();
		warnings['improperDepositCodeSelection']='<s:text name="estimate.depositCode.warning.improperDepositCodeSelection"/>'
		var currentDate='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';

		function enableFields(){
			for(i=0;i<document.financialDetail.elements.length;i++){
	        	document.financialDetail.elements[i].disabled=false;
			}
		} 	

	
	 	function validateBeforeSubmit(){
			enableFields();
	 		document.getElementById("isEnableSelect").value='<s:property value="%{isEnableSelect}"/>';
	  	if(!checkAllFields())
	  		return false;
	  		document.getElementById("option").value="searchDepositWorksFolioDetails";
	  		return true;
	  	}
	  	
	  	function checkAllFields(){
	  		clearMessage('depstWrksFolioRprtError');
	  		if(dom.get('fund').value==-1){
		  		 dom.get("depstWrksFolioRprtError").innerHTML='<s:text name="depositWorksFolioReport.fund.null"/>'; 
		         dom.get("depstWrksFolioRprtError").style.display='';
				 return false;
	  		}
	  		if(dom.get('asOnDate').value=="" || dom.get('asOnDate').value==null){
	  		 	dom.get("depstWrksFolioRprtError").innerHTML='<s:text name="depositWorksFolioReport.asOnDate.null"/>'; 
		        dom.get("depstWrksFolioRprtError").style.display='';
				return false;
	  		}
	  		else if(dom.get('asOnDate').value!="" && dom.get('asOnDate').value!=null){
	  			 if(compareDate(dom.get('asOnDate').value,currentDate) == -1 ){
					dom.get("depstWrksFolioRprtError").innerHTML='<s:text name="depositWorksFolioReport.asOnDate.greaterThan.currentDate"/>'; 
		        	dom.get("depstWrksFolioRprtError").style.display='';
					return false;   			 
	  			 } 
			}	  			  		
	  		if(dom.get('coa').value==-1){
	  			dom.get("depstWrksFolioRprtError").innerHTML='<s:text name="depositWorksFolioReport.glCode.null"/>'; 
		        dom.get("depstWrksFolioRprtError").style.display='';
				return false;
	  		}
	  		if(document.financialDetail.code.value=="" || document.financialDetail.code.value==null){
	  			dom.get("depstWrksFolioRprtError").innerHTML='<s:text name="depositWorksFolioReport.depositCode.null"/>'; 
		        dom.get("depstWrksFolioRprtError").style.display='';
				return false;
	  		}
	  		return true;
	  	}
	  	
	  	function resetAllFields(){
	    	 dom.get('fund').value=-1;
	    	 dom.get('coa').value=-1;
	    	 document.financialDetail.code.value ="";
	    	 dom.get('asOnDate').value="";
    	}
	  	
	  	function validateFundSelection(){
			clearMessage('depstWrksFolioRprtError');
		    var fundElem = document.getElementById('fund');
			var fundId = fundElem.options[fundElem.selectedIndex].value;
			if(fundId =='-1'){
		    	showMessage('depstWrksFolioRprtError','Please Choose The Fund Before Selecting the Deposit Code.');
	    	}
	    }
	    
	    function depositCodeSearchParameters(){ 
			if(dom.get('fund').value !='-1'){
				return "fundId="+dom.get('fund').value;
    		}
		}
		
		function clearDepositCode(){
			document.financialDetail.code.value="";
		}
		
		var depositCodeSearchSelectionHandler = function(sType, arguments){ 
            var oData = arguments[2];
            dom.get("depositCodeId").value = oData[1];
        }
        
		var depositCodeSelectionEnforceHandler = function(sType, arguments){
		    warn('improperDepositCodeSelection');
		}
		
		function viewDepositReport(){
				enableFields();
		 		window.open('${pageContext.request.contextPath}/estimate/financialDetail!viewDepositFolioPDF.action?fundId='+dom.get('fund').value+'&glcodeId='+dom.get('coa').value+'&depositCodeId='+dom.get('depositCodeId').value+
		 		'&asOnDate='+dom.get('asOnDate').value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}

		
  </script>
  	
   <body>
      <s:if test="%{hasErrors()}">
        <div class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
    <s:form action="financialDetail"   theme="simple" name="financialDetail" > 
  	 <input type="hidden" name="option" id="option" />
  	 <input type="hidden" name="isEnableSelect" id="isEnableSelect"/>
    <div id="depstWrksFolioRprtError" class="errorstyle" style="display:none;"></div>
 
    <div class="formmainbox">
 	<div class="insidecontent"> 
 	<div class="rbroundbox2">
 	<div class="rbtop2"><div></div></div>
 	<div class="rbcontent2">
 		<table width="100%" border="0" cellspacing="0" cellpadding="0" id="table2"> 
 			<tr>
 				<td width="11%" class="whiteboxwk" ><span class="mandatory">*</span><s:text name='depositWorksFolioReport.fund'/></td>
		        <td width="21%" class="whitebox2wk" >
		            <s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="fund" id="fund" disabled="%{isEnableSelect}" cssClass="selectwk" list="dropdownData.fundList" listKey="id" listValue="name" value="%{fund.id}" onChange="clearMessage('depstWrksFolioRprtError');clearDepositCode();"/>
		         </td>
		         
		          <td class="whiteboxwk"><span class="mandatory">*</span><s:text name="depositWorksFolioReport.date" /></td>
		          <td class="whitebox2wk"><s:date name="asOnDate" var="asOnDateFormatted" format="dd/MM/yyyy"/><s:textfield name="asOnDate" value="%{asOnDateFormatted}" id="asOnDate" cssClass="selectwk"  onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
                    <a href="javascript:show_calendar('forms[0].asOnDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"><img src="/egworks/resources/erp2/images/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
                 </td>
		    </tr>
		    
		    <tr>
		    	<td width="11%" class="greyboxwk"><span class="mandatory">*</span><s:text name="depositWorksFolioReport.glCode" /></td>
				<td width="21%" class="greybox2wk"><s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="coa" id="coa" cssClass="selectwk" list="dropdownData.coaList"
										listKey="id" listValue='glcode  + " : " + name' value="%{coa.id}" disabled="%{isEnableSelect}" onChange="clearMessage('depstWrksFolioRprtError');"/></td>
				<td class="greyboxwk"><span class="mandatory">*</span><s:text name="depositWorksFolioReport.depositCode" /></td>
	            <td class="greybox2wk">
	                <div class="yui-skin-sam">
	                <div id="depositCodeSearch_autocomplete">
	                <div><s:textfield id="depositCodeSearch" name="code" onkeypress="return validateFundSelection()" value="%{code}" disabled="%{isEnableSelect}" class="selectwk" /><s:hidden id="depositCodeId" name="depositCodeId" value="%{depositCodeId}"/></div>
	                <span id="depositCodeSearchResults"></span>
	                </div>
	                </div>
	                <egov:autocomplete name="depositCodeSearch" width="20" field="depositCodeSearch" url="depositCodeSearch!searchAjax.action?" queryQuestionMark="false" results="depositCodeSearchResults" paramsFunction="depositCodeSearchParameters" handler="depositCodeSearchSelectionHandler" forceSelectionHandler="depositCodeSelectionEnforceHandler"/> 
	                <span class='warning' id="improperDepositCodeSelectionWarning"></span> 
	            </td> 
		    </tr>
 		</table>
 	</div>
 	
 	<div id="mandatary" align="right" class="mandatory" style="font-size: 11px; padding-right: 20px;">*
		<s:text name="message.mandatory" />
	</div>
 	 	
 	<!-- To Show Save and Close Buttons -->
	<div class="buttonholderwk" id="slCodeButtons">
		<s:submit cssClass="buttonfinal" value="SEARCH" id="searchButton" name="searchButton" method="searchDepositWorksFolio" onclick="return validateBeforeSubmit();"/>
	  			&nbsp;
	  	<input type="button" class="buttonfinal" value="CLEAR" id="clearButton" name="clearButton" onclick="window.open('${pageContext.request.contextPath}/estimate/financialDetail!searchDepositWorksFolio.action?option=input','_self');"/>
	  			&nbsp;
	 	<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="confirmClose('<s:text name='depositWorksFolioReport.close.confirm'/>');"/>
    </div>
    <br>
  
	
 	<div class="rbcontent2">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr height="5">
		<td></td>
	</tr>
	<tr>
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="9" class="headingwk" align="left">
						<div class="arrowiconwk">
							<img src="/egworks/resources/erp2/images/arrow.gif" />
						</div>

						<div class="headerplacer">
							<s:text name='page.result.search.depositWrksFolioReprt' />
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td align="center">
			<br/>
			<table border="0">
				<tr>
					<td>
						<s:if test="%{approvedBudgetFolioDetails.size != 0 || approvedBudgetFolioDetails.size == 0}">
							<s:text name="depositWorksFolioReport.search.availBalance" />:&nbsp;
                        </s:if>
                   </td>
                   <td>
						<div align="left">
                        	<s:if test="%{approvedBudgetFolioDetails.size != 0}">
                        		<s:text name="contractor.format.number" >
                        			<s:param name="value" value="%{totalDepositAmount-latestCumulative}"/>
                        		</s:text>
                        	</s:if>
                        	<s:else>
                        		<s:if test="%{approvedBudgetFolioDetails.size == 0}">
                        			<s:text name="contractor.format.number" >
                        				<s:param name="value" value="%{totalDepositAmount}"/>
                        			</s:text>
                        		</s:if>
                        	</s:else> 
                        </div>
					</td>		
                  </tr>
				</table>
			</td>
	</tr>

	<s:if test="%{approvedBudgetFolioDetails.size == 0}">
		<div>	
			<tr>
				<td align="center"><font color="red">Estimates are not appropriated for selected Budget Head.</font></td>
			</tr>
		</div>
	</s:if>
</table>
	<s:if test="%{approvedBudgetFolioDetails.size != 0}" >
	    <div class="scrollerhori" style="width:1200px">
	    <display:table name="approvedBudgetFolioDetails" uid="currentRow"
		cellpadding="0" cellspacing="0" requestURI=""  
		style="border:1px;width:1500px;empty-cells:show;border-collapse:collapse;"> 
		<display:column titleKey='depositWorksFolioReport.search.slno' title="Sl No." headerClass="pagetableth" class="pagetabletd" style="width:3%;text-align:left;" property='srlNo' />
		<display:column headerClass="pagetableth" class="pagetabletd" titleKey="depositWorksFolioReport.search.budgApprCanclNo"  title="Budget Appropriation / Cancellation No"  style="width:7%;text-align:left" property='budgetApprNo' />
		<display:column headerClass="pagetableth" class="pagetabletd" titleKey='depositWorksFolioReport.search.appDate'  title="Appropriation Date" style="width:4%;text-align:left" property='appDate' />
		<display:column headerClass="pagetableth" class="pagetabletd" titleKey='depositWorksFolioReport.search.appType'  title="Type" style="width:6%;text-align:left" property='appType' />
		<display:column headerClass="pagetableth" class="pagetabletd" titleKey='depositWorksFolioReport.search.appAmount'  title="Appropriated Amount (current year)" style="width:6%;text-align:right">
			<s:if test="%{#attr.currentRow.workValue==null}">
				<s:text name="" >
				   	<s:param name="value" value="" />
				</s:text>   	
			</s:if>
			<s:else>
				<s:text name="contractor.format.number" >
				   	 <s:param name="value" value="%{#attr.currentRow.appropriatedValue}" />
				</s:text>
			</s:else>
		</display:column>
		<display:column headerClass="pagetableth" class="pagetabletd" titleKey='depositWorksFolioReport.search.estimateNo'  title="Estimate No." style="width:7%;text-align:left" property='estimateNo' />
		<display:column headerClass="pagetableth" class="pagetabletd" titleKey='depositWorksFolioReport.search.workName'  title="Name of the Work" style="width:12%;text-align:left" property='nameOfWork' />
		<display:column headerClass="pagetableth" class="pagetabletd" titleKey='depositWorksFolioReport.search.estimateDate'  title="Estimate Date" style="width:4%;text-align:left" property='estimateDate' />
		<display:column headerClass="pagetableth" style="align:right" class="pagetabletd" titleKey='depositWorksFolioReport.search.estimateValue'  title="Estimate Value" style="width:6%;text-align:right">
			<s:if test="%{#attr.currentRow.workValue==null}">
				<s:text name="" >
				   	<s:param name="value" value="" />
				</s:text>   	
			</s:if>
			<s:else>
				<s:text name="contractor.format.number" >
				   	 <s:param name="value" value="%{#attr.currentRow.workValue}" />
				</s:text>
			</s:else>
		</display:column>
		<display:column headerClass="pagetableth" class="pagetabletd" titleKey='depositWorksFolioReport.search.expenses.incurred'  title="Expenses Incurred" style="width:6%;text-align:right">
			<s:if test="%{#attr.currentRow.workValue==null}">
			</s:if>
			<s:else>
				<s:text name="contractor.format.number" >
					<s:param name="value" value="%{#attr.currentRow.expensesIncurred}" />
				</s:text>
			</s:else>
		</display:column>

		<display:column headerClass="pagetableth" class="pagetabletd" titleKey='depositWorksFolioReport.search.cumulativeTotal'  title="Cumulative Total" style="width:9%;text-align:right">
			<s:if test="%{#attr.currentRow.workValue==null}">
			<b>
				<s:text name="contractor.format.number" >
					<s:param name="value" value="%{#attr.currentRow.cumulativeTotal}" />
				</s:text>
			</b>	
			</s:if>
			<s:else>
				<s:text name="contractor.format.number" >
					<s:param name="value" value="%{#attr.currentRow.cumulativeTotal}" />
				</s:text>
			</s:else>
		</display:column>
		<display:column headerClass="pagetableth" class='pagetabletd' titleKey='depositWorksFolioReport.search.totalDeposits'  title="Total Deposits" style="width:9%;text-align:right">
			<s:if test="%{#attr.currentRow.workValue==null}">
			<b>
				<s:text name="contractor.format.number" >
				<s:param name="value" value="%{#attr.currentRow.balanceAvailable}" />
				</s:text>
			</b>	
			</s:if>
			<s:else>
				<s:text name="contractor.format.number" >
					<s:param name="value" value="%{#attr.currentRow.balanceAvailable}" />
				</s:text>
			</s:else>	
		</display:column>
		<display:column headerClass="pagetableth" class="pagetabletd" titleKey='depositWorksFolioReport.search.availBalance'  title="Balance Available" style="width:9%;text-align:right">
		 	<s:if test="%{#attr.currentRow.workValue==null}">
			<b>
				<s:text name="contractor.format.number" >
					<s:param name="value" value="%{#attr.currentRow.balanceAvailable-#attr.currentRow.cumulativeTotal}"/>
				</s:text>
			</b>	
			</s:if>
			<s:else>
			 	<s:text name="contractor.format.number" >
					<s:param name="value" value="%{#attr.currentRow.balanceAvailable-#attr.currentRow.cumulativeTotal}"/>
				</s:text>
			</s:else>
		</display:column>
		<display:column headerClass="pagetableth" class="pagetabletd" titleKey='depositWorksFolioReport.search.cummulative.actual'  title="Cummulative Actual Expense" style="width:6%;text-align:right">
			<s:if test="%{#attr.currentRow.workValue==null}">
				<b>
					<s:text name="contractor.format.number" >
						<s:param name="value" value="%{#attr.currentRow.cumulativeExpensesIncurred}" />
					</s:text>
				</b>
			</s:if>
			<s:else>
					<s:text name="contractor.format.number" >
						<s:param name="value" value="%{#attr.currentRow.cumulativeExpensesIncurred}" />
					</s:text>
			</s:else>
		</display:column>
		<display:column headerClass="pagetableth" class="pagetabletd" titleKey='depositWorksFolioReport.search.actual.balance'  title="Actual Balance" style="width:6%;text-align:right">
			<s:if test="%{#attr.currentRow.workValue==null}">
				<b>
					<s:text name="contractor.format.number" >
						<s:param name="value" value="%{#attr.currentRow.actualBalanceAvailable}" />
					</s:text>
				</b>
			</s:if>
			<s:else>
				<s:text name="contractor.format.number" >
					<s:param name="value" value="%{#attr.currentRow.actualBalanceAvailable}" />
				</s:text>
			</s:else>
		</display:column>
		</display:table> 
		</div>
	</s:if>
	<s:if test="%{!(approvedBudgetFolioDetails==null || approvedBudgetFolioDetails.isEmpty())}">
		<div class="buttonholderwk" >
		<br>
			<input type="button" onclick="return viewDepositReport()" class="buttonpdf" value="View PDF" id="depositfolioreportButton" name="depositfolioreportButton"/>
		</div>
	</s:if>
	</div>

 	</div></div></div>
   </s:form>
  </body>
</html>
