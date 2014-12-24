<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<html>
  <head>
      <title><s:text name='page.title.depositWorksFolioReport'/></title>
		<script src="<egov:url path='js/works.js'/>"></script>
	  	<script type="text/javascript">
	  		var warnings=new Array();
			warnings['improperDepositCodeSelection']='<s:text name="estimate.depositCode.warning.improperDepositCodeSelection"/>'
			var currentDate='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
	
		  	function validateBeforeSubmit(){
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
			 		window.open('${pageContext.request.contextPath}/estimate/financialDetail!viewDepositFolio.action?fundId='+dom.get('fund').value+'&glcodeId='+dom.get('coa').value+'&depositCodeId='+dom.get('depositCodeId').value+
			 		'&asOnDate='+dom.get('asOnDate').value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
		}
	
			
	  	</script>
	</head>  	
   <body>
      <s:if test="%{hasErrors()}">
        <div class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
    <s:form action="financialDetail"   theme="simple" name="financialDetail" > 
  	 <input type="hidden" name="option" id="option" />
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
		            <s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="fund" id="fund" cssClass="selectwk" list="dropdownData.fundList" listKey="id" listValue="name" value="%{fund.id}" onChange="clearMessage('depstWrksFolioRprtError');clearDepositCode();"/>
		         </td>
		         
		          <td class="whiteboxwk"><span class="mandatory">*</span><s:text name="depositWorksFolioReport.date" /></td>
		          <td class="whitebox2wk"><s:date name="asOnDate" var="asOnDateFormatted" format="dd/MM/yyyy"/><s:textfield name="asOnDate" value="%{asOnDateFormatted}" id="asOnDate" cssClass="selectwk"  onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
                    <a href="javascript:show_calendar('forms[0].asOnDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"><img src="${pageContext.request.contextPath}/image/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
                 </td>
		    </tr>
		    
		    <tr>
		    	<td width="11%" class="greyboxwk"><span class="mandatory">*</span><s:text name="depositWorksFolioReport.glCode" /></td>
				<td width="21%" class="greybox2wk"><s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="coa" id="coa" cssClass="selectwk" list="dropdownData.coaList"
										listKey="id" listValue='glcode  + " : " + name' value="%{coa.id}" onChange="clearMessage('depstWrksFolioRprtError');"/></td>
				<td class="greyboxwk"><span class="mandatory">*</span><s:text name="depositWorksFolioReport.depositCode" /></td>
	            <td class="greybox2wk">
	                <div class="yui-skin-sam">
	                <div id="depositCodeSearch_autocomplete">
	                <div><s:textfield id="depositCodeSearch" name="code" onkeypress="return validateFundSelection()" value="%{code}" class="selectwk" /><s:hidden id="depositCodeId" name="depositCodeId" value="%{depositCodeId}"/></div>
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
							<img src="${pageContext.request.contextPath}/image/arrow.gif" />
						</div>

						<div class="headerplacer">
							<s:text name='page.result.search.depositWrksFolioReprt' />
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	</table>

    <display:table name="approvedBudgetFolioDetails" uid="currentRow"
	cellpadding="0" cellspacing="0" requestURI=""  
	style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;"> 
	<display:column titleKey='depositWorksFolioReport.search.slno' title="Sl No." headerClass="pagetableth" class="pagetabletd" style="width:5%;text-align:left;" property='srlNo' />
	<display:column headerClass="pagetableth" class="pagetabletd" titleKey="depositWorksFolioReport.search.budgApprCanclNo"  title="Budget Appropriation / Cancellation No"  style="width:13%;text-align:left" property='budgetApprNo' />
	<display:column headerClass="pagetableth" class="pagetabletd" titleKey='depositWorksFolioReport.search.estimateNo'  title="Estimate No." style="width:10%;text-align:left" property='estimateNo' />
	<display:column headerClass="pagetableth" class="pagetabletd" titleKey='depositWorksFolioReport.search.workName'  title="Name of the Work" style="width:15%;text-align:left" property='nameOfWork' />
	<display:column headerClass="pagetableth" class="pagetabletd" titleKey='depositWorksFolioReport.search.estimateDate'  title="Estimate Date" style="width:10%;text-align:left" property='estimateDate' />
	<display:column headerClass="pagetableth" style="align:right" class="pagetabletd" titleKey='depositWorksFolioReport.search.estimateValue'  title="Estimate Value" style="width:10%;text-align:right">
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
	<display:column headerClass="pagetableth" class="pagetabletd" titleKey='depositWorksFolioReport.search.cumulativeTotal'  title="Cumulative Total" style="width:10%;text-align:right">
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
	<display:column headerClass="pagetableth" class='pagetabletd' titleKey='depositWorksFolioReport.search.totalDeposits'  title="Total Deposits" style="width:10%;text-align:right">
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
	<display:column headerClass="pagetableth" class="pagetabletd" titleKey='depositWorksFolioReport.search.availBalance'  title="Balance Available" style="width:10%;text-align:right">
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
	
	</display:table> 
	
	<s:if test="%{!(approvedBudgetFolioDetails==null || approvedBudgetFolioDetails.isEmpty())}">
		<div class="buttonholderwk" >
		<br>
			<input type="button" onclick="return viewDepositReport()" class="buttonadd" value="View PDF" id="depositfolioreportButton" name="depositfolioreportButton"/>
		</div>
	</s:if>
	</div>

 	</div></div></div>
   </s:form>
  </body>
</html>
