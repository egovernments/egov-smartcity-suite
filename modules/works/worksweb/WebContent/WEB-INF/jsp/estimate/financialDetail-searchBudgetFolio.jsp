<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>


<html>
<title><s:text name="estimate.budgetfolio.header" /></title>
<script src="<egov:url path='js/works.js'/>"></script>
<script src="<egov:url path='js/helper.js'/>"></script>

<script type="text/javascript">
	function checkDate(obj){
		if(!validateDateFormat(obj)) {
	    	dom.get('errorreportDate').style.display='none';
			dom.get("budgetFolio_error").style.display='block';
			document.getElementById("budgetFolio_error").innerHTML='Please enter valid Date';
			document.budgetFolioForm.reportDate.focus();
	    	return;
		}
		else {
			dom.get("budgetFolio_error").style.display='none';
			dom.get("budgetFolio_error").style.display='none';
		}
		return true;
	}
	function getReportDate() {
		var reportDate=document.budgetFolioForm.reportDate;	
		if(reportDate.value=='') {
			reportDate.value=getCurrentDate();
		}
	}	
	
	budgetLoadFailureHandler=function(){
	   showMessage('budgetFolio_error','Unable to load budget head information');
	}
	
	
	function setupBudgetGroups(elem){
		var id=elem.options[elem.selectedIndex].value;
	    var date=document.getElementById("reportDate").value;
	    populatebudgetGroup({functionId:id,estimateDate:date});
	}
	
	function setUp(element){
	var obj=document.getElementById("executingDepartment");
	document.getElementById("deptName").value=obj.options[obj.selectedIndex].text;
	 document.getElementById("option").value="searchPdf";
	 return true;
	}
	
	function validate(){
	 document.getElementById("option").value="searchdetails";
	 if(document.getElementById("reportDate").value==""){
	 	 dom.get("budgetFolio_error").style.display='';
		document.getElementById("budgetFolio_error").innerHTML="Please enter the Date";
	 	return false;
	 }
	 if(!checkDate(document.getElementById("reportDate"))){	
	  return false;
	 }
	 else
	 {
	  return true;
	 }
	  return true;
	}
	
	function setMandatoryFields(){
		 var dept='<s:property value="%{mandatoryFields['department']}"/>';
		 var fund='<s:property value="%{mandatoryFields['fund']}"/>';
		 if(dept=='M'){
		 	dom.get("deptDisp").style.display='';
		 }
		 if(fund=='M'){
		 	dom.get("fundDisp").style.display='';
		 }
	}
	
</script>

<body onload="getReportDate();setMandatoryFields();">
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
		<s:form  theme="simple" name="budgetFolioForm"  action="financialDetail">   
		<div class="errorstyle" id="budgetFolio_error" style="display: none;"></div>
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
								<td colspan="4">&nbsp;<s:hidden name="financialYearStartDate" /></td>
								
							</tr>
							<tr>
								<td width="11%" class="whiteboxwk"><span class="mandatory" id="deptDisp" style="display:none;">*</span>
									<s:text name='estimate.executing.department'/> :
								</td>
								<td width="21%" class="whitebox2wk">
										<s:select headerKey="-1" headerValue="%{getText('budgetfolio.default.select')}" name="executingDepartment" 
										id="executingDepartment" cssClass="selectwk" list="dropdownData.executingDepartmentList" listKey="id" 
										listValue="deptName" value="%{executingDepartment}" />
								</td>
								<td width="15%" class="whiteboxwk"><span class="mandatory">*</span>
									<s:text name='budgetfolio.asondate'/> :	
								</td>
								<td width="53%" class="whitebox2wk">
									<s:date name="reportDate" var="estDateFormat" format="dd/MM/yyyy"/>
									<s:textfield name="reportDate" id="reportDate" value="%{estDateFormat}" cssClass="selectboldwk" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="checkDate(this)"/>
				         			<a href="javascript:show_calendar('forms[0].reportDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
			    		            <img src="${pageContext.request.contextPath}/image/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
						   			<span id='errorreportDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>
								</td>
							</tr>
							<tr>
								<td width="11%" class="greyboxwk">
									<span class="mandatory">*</span><s:text name='estimate.financial.function'/> :
								</td>
								<td width="21%" class="greybox2wk">
									<s:select headerKey="-1" headerValue="%{getText('budgetfolio.default.select')}"
									 name="function" id="function" cssClass="selectwk" list="dropdownData.functionList" 
									 listKey="id" listValue="name" value="%{function.id}" onChange="setupBudgetGroups(this);"/>
									  <egov:ajaxdropdown id="budgetGroupDropdown" fields="['Text','Value']" dropdownId='budgetGroup' 
									  url='estimate/ajaxFinancialDetail!loadBudgetGroups.action' selectedValue="%{budgetGroup.id}"/>
									</td>
									  
								</td>
								<td width="15%" class="greyboxwk">
									<span class="mandatory">*</span><s:text name='estimate.financial.budgethead'/> :
								</td>
								<td width="53%" class="greybox2wk">
										<s:select headerKey="-1" headerValue="%{getText('budgetfolio.default.select')}" 
									name="budgetGroup" id="budgetGroup" cssClass="selectwk" list="dropdownData.budgetHeadList" 
									listKey="id" listValue="name" value="%{budgetGroup.id}" />
								</td>
							</tr>
							<tr>
								<td width="11%" class="whiteboxwk"><span class="mandatory" id="fundDisp" style="display:none;">*</span>
									<s:text name='estimate.financial.fund'/> :				
								</td>
								<td width="21%" class="whitebox2wk">
									<s:select headerKey="-1" headerValue="%{getText('budgetfolio.default.select')}" 
										name="fund" id="fund" cssClass="selectwk" list="dropdownData.fundList" listKey="id" 
										listValue="name" value="%{fund.id}" />
								</td>
								<td width="15%" class="whiteboxwk">&nbsp;</td>
								<td width="53%" class="whitebox2wk">&nbsp;
								<input type="hidden" name="option" id="option" />
								<input type="hidden" name="deptName" id="deptName" />
								</td>
							</tr>
							<tr>
								<td colspan="4">
									<div class="buttonholdersearch">
									    <s:submit cssClass="buttonadd" value="SEARCH" id="saveButton" name="button" method="searchBudgetFolio"
									    onClick="return validate();"/>
									</div>
								</td>
							</tr>
						<tr><td colspan="4">&nbsp;</td></tr>
				  				<%@ include file="financialDetail-approvedBudgetFolioDetail.jsp"%>   
				 			<tr><td colspan="4">&nbsp;</td></tr>
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
				<s:if test="%{approvedBudgetFolioDetails}">
				 <s:submit cssClass="buttonpdf" value="VIEW PDF" id="pdfButton" name="pdfButton" method="viewBudgetFolioPdf"
									    onClick="return setUp();"/>
					<%-- input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/financialDetail!viewBudgetFolioPdf.action?option=searchPdf');"
					 class="buttonpdf" value="VIEW PDF" id="pdfButton" name="pdfButton" / --%>
				</s:if>
				<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="window.close();" />			
			</div>
		</s:form>
	</body>
</html>
