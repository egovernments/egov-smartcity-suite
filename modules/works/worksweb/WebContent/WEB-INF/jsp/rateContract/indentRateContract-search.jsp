<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<script src="<egov:url path='js/works.js'/>"></script>
<script src="<egov:url path='js/helper.js'/>"></script>
<script>
function disableBudgetGroup(){
	var budgetHeader = document.getElementById('budgetHeader');
	var budgetHeaderSelect = document.getElementById('budgetHeaderSelect');
	budgetHeader.style.display = 'none';
	budgetHeaderSelect.style.display = 'none';
	var indentType=document.indentSearchForm.indentType.value;
	if(indentType=='Item'){
		budgetHeader.style.display = 'none';
		budgetHeaderSelect.style.display = 'none';
	}
	if(indentType=='Amount'){
		budgetHeader.style.display = 'block';
		budgetHeaderSelect.style.display = 'block';
	}
}
function submitIndentSearchForm() {
    clearMessage('indentsearcherror')
	var err=false;
	if(!validateDateFormat(document.indentSearchForm.fromCreateDate) || !validateDateFormat(document.indentSearchForm.toCreateDate)){
		dom.get("indentsearcherror").style.display=''; 
		document.getElementById("indentsearcherror").innerHTML+='<s:text name="invalid.fieldvalue.indentDate" /><br>';
		err=true; 
	}
	if(!err){
	    document.indentSearchForm.action='${pageContext.request.contextPath}/rateContract/indentRateContract!searchDetails.action?mode=search';
	    document.indentSearchForm.submit();
   }
}

function checkDate(obj){
	if(!validateDateFormat(obj)) {
    	$('date_error').show();
    	$('form_error').show();
		$('mandatory_error').hide();
		$(obj.id).focus();
    	return false;
	}
	else {
    	$('date_error').hide();
    	$('form_error').hide();
		$('mandatory_error').hide();
	}
	
	return true;
}

function setIndentId(elem){
	document.techSanctionEstimatesForm.indentId.value = elem.value;
}

function checkAllIndents(obj){ 
	var len=document.forms[0].selectedIndent.length;
	if(obj.checked){
		if(len>0){
			for (i = 0; i < len; i++)
				document.forms[0].selectedIndent[i].checked = true;
		}else document.forms[0].selectedIndent.checked = true;
	}
	else{
		if(len>0){
			for (i = 0; i < len; i++)
				document.forms[0].selectedIndent[i].checked = false;
		}else document.forms[0].selectedIndent.checked = false;
	}
}

function disableSelect(){
	document.getElementById('egwStatus').disabled=true;
	dom.get('department').value='<s:property value="%{dept}"/>'; 
	dom.get('department').disabled=true;
}
function enableSelect(){
	document.getElementById('egwStatus').disabled=false;
	dom.get('department').disabled=false;
}

function returnBackToParent() { 
	var value = new Array();
	var wind=window.dialogArguments;
	var len=document.forms[0].selectedIndent.length; 
	var j=0;
	if(len >0){
		for (i = 0; i < len; i++){
			if(document.forms[0].selectedIndent[i].checked){
				value[j] = document.forms[0].indentIds[i].value;
				j++;
			}
		}
	}
	else{
		if(document.forms[0].selectedIndent.checked){
			value[j] = dom.get('indentIds').value;
		}
	}
	if(value.length>0)
	{
		var wind;
		var data = new Array();
		wind=window.dialogArguments;
		if(wind==undefined){
			wind=window.opener;
			data=value;
			window.opener.update('indent'+'~'+data);
		}
		else{
			wind=window.dialogArguments;
			wind.result='indent'+'~'+value;
		}
		window.close();
	}
	else{
		dom.get("indentsearcherror").innerHTML='Please Select any one of the Indents'; 
        dom.get("indentsearcherror").style.display='';
		return false;
	 }
	 dom.get("indentsearcherror").style.display='none';
	 dom.get("indentsearcherror").innerHTML='';
	return true;
}
</script>

<html>
<title><s:text name='page.title.indent.search'/></title>
<body class="yui-skin-sam" onload="disableBudgetGroup();">
<div id="indentsearcherror" class="errorstyle" style="display:none;"></div>
   <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
   </s:if>
   <s:if test="%{hasActionMessages()}">
       <div class="messagestyle">
        	<s:actionmessage theme="simple"/>
       </div>
   </s:if>
<s:form theme="simple" name="indentSearchForm">
<s:hidden name="tenderFileDate" id="tenderFileDate" value="%{tenderFileDate}"/>
<s:hidden name="status" id="status" value="%{status}"/>
<s:hidden name="dept" id="dept" value="%{dept}"/>
<div class="formmainbox"><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	<div class="rbcontent2"><div class="datewk">
	 <table width="100%" border="0" cellspacing="0" cellpadding="0">
             <tr>
				<td colspan="6">&nbsp;</td>
			 </tr>
			 <tr>
				<td colspan="6">&nbsp;</td>
			 </tr>
			 <tr>
			 <td colspan="6" class="headingwk" align="left">
				<div class="arrowiconwk">
				  <img src="${pageContext.request.contextPath}/image/arrow.gif" />
				</div>
				<div class="headplacer">
				  <s:text name='title.search.criteria' />
				</div>
			  </td>
			 </tr>
             <tr>
				<td width="14%" class="whiteboxwk">
					<s:text name='indent.search.label.fromDate' />:
				</td>
				<td width="37%" class="whitebox2wk">
					<s:date name="fromCreateDate" var="fromCreateDateFormat" format="dd/MM/yyyy" />
					<s:textfield name="fromCreateDate"  id="fromCreateDate" cssClass="selectboldwk" value="%{fromCreateDateFormat}" onfocus="javascript:vDateType='3';"
						onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="checkDate(this)"/>
					<a href="javascript:show_calendar('forms[0].fromCreateDate',null,null,'DD/MM/YYYY');" 
						onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;">
					<img src="${pageContext.request.contextPath}/image/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
				<span id='errorfromCreateDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>									
				</td>  
				<td width="14%" class="whiteboxwk">
					<s:text name='indent.search.label.toDate' />:
				</td>
				<td width="37%" class="whitebox2wk">
					<s:date name="toCreateDate" var="toCreateDateFormat" format="dd/MM/yyyy" />
					<s:textfield name="toCreateDate"  id="toCreateDate" cssClass="selectboldwk" value="%{toCreateDateFormat}" onfocus="javascript:vDateType='3';"
						onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="checkDate(this)"/>
					<a href="javascript:show_calendar('forms[0].toCreateDate',null,null,'DD/MM/YYYY');" 
						onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;">
					<img src="${pageContext.request.contextPath}/image/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
				<span id='errortoCreateDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>									
				</td>  
				<td width="11%" class="whiteboxwk">
					<s:text name="indent.search.label.department" />:
				</td>
				<td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('indent.default.select')}" name="department" id="department" cssClass="selectwk" list="dropdownData.departmentList" listKey="id" listValue="deptName" value="%{department.id}" />
				</td>
			</tr> 
			<tr>
				<td class="greyboxwk">
					<s:text name="indent.search.label.indentRateContractNumber" />:
				</td>
				<td class="greybox2wk">
					<s:textfield name="indentNumber" id="indentNumber" 	cssClass="selectboldwk" value="%{indentNumber}" />
				</td>
				<td class="greyboxwk">
					<s:text name="indent.search.label.ircType" />:
				</td>
				<td class="greybox2wk">
					<s:select headerKey="-1" headerValue="%{getText('indent.default.select')}" name="indentType" value="%{indentType.value}" id="indentType" cssClass="selectwk" list="#{'Amount':'Amount', 'Item':'Item'}" value="%{indentType}" onChange="disableBudgetGroup()"/>
				</td>
				<td width="11%" class="greyboxwk">
					<s:text name="indent.search.label.status" />:
				</td>
				<td  class="greybox2wk"><s:select headerKey="-1" headerValue="%{getText('indent.default.select')}" name="egwStatus" id="egwStatus" cssClass="selectwk" list="dropdownData.statusList" listKey="id" listValue="description" value="%{egwStatus.id}"  />
				</td>
				<s:if test="%{source=='tenderFile'}">
					<script>disableSelect();</script>
				</s:if>
			</tr>
			<tr>
				<td width="11%" class="whiteboxwk">
					<s:text name="indent.search.label.contractorClass" />:
				</td>
				<td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('indent.default.select')}" name="contractorGrade" id="contractorGrade" cssClass="selectwk" list="dropdownData.contractorGradeList" listKey="id" listValue="grade" value="%{contractorGrade.id}" />
				</td>
				<td width="11%" class="whiteboxwk"><div id="budgetHeader">
					<s:text name="indent.search.label.budgethead" />:</div>
				</td>
 				<td colspan="3" class="whitebox2wk"><div id="budgetHeaderSelect"><s:select headerKey="-1" headerValue="%{getText('indent.default.select')}" name="budgetGroup" id="budgetGroup" cssClass="selectwk" list="dropdownData.budgetList" listKey="id" listValue="name" value="%{budgetGroup.id}" />
				</div></td>					
			</tr>
            <tr>
            	<td  colspan="6" class="shadowwk"> </td>               
             </tr>
               <tr><td>&nbsp;</td></tr>	
            <tr>
			<td colspan="6" class="mandatory" align="right">* <s:text name="message.mandatory" /></td> 
			</tr>  		
            <tr>
                 <td colspan="6"> 
                   <div class="buttonholderwk">
		             <p>
			           <input type="button" class="buttonadd" value="SEARCH" id="searchButton" name="button" onclick="enableSelect();submitIndentSearchForm()" />&nbsp;
			           <input type="button" class="buttonfinal" value="RESET" id="resetbutton" name="clear" onclick="this.form.reset();">&nbsp;
		               <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();" />
	                 </p>
		          </div>
                </td>
            </tr>
            <tr>
				<td colspan="6" align="left">
					 <table width="100%" border="0" cellspacing="0" cellpadding="0">
						 <tr>
							<td class="headingwk">
								<div class="arrowiconwk">
									<img src="${pageContext.request.contextPath}/image/arrow.gif" />
								</div>
								<div class="headplacer">
									<s:text name="indent.search.label.result.title" />
								</div>
							</td>
						 </tr>
                      </table> 
                 </td>
            </tr> 
       
     </table>               
     </div>
     <s:hidden id="indentId" name="indentId" />
	<s:hidden id="source" name="source" />
        <%@ include file='indentRateContract-searchResults.jsp'%> 	
	 <div class="rbbot2"><div></div></div>
</div>
</div>
</div>

</s:form>
</body>

</html>