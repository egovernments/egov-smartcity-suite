<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<script src="<egov:url path='js/works.js'/>"></script>
<script src="<egov:url path='js/helper.js'/>"></script>
<script>

function submitRCSearchForm() {
clearMessage('rateContractsearcherror');
	var err=false;
	if(!validateDateFormat(document.rateContractSearchForm.fromCreateDate) || !validateDateFormat(document.rateContractSearchForm.toCreateDate)){
		dom.get("rateContractsearcherror").style.display=''; 
		document.getElementById("rateContractsearcherror").innerHTML+='<s:text name="invalid.fieldvalue.rcDate" /><br>';
		err=true; 
	}
	if(!err){
	    document.rateContractSearchForm.action='${pageContext.request.contextPath}/rateContract/rateContract!searchDetails.action?mode=view';
	    document.rateContractSearchForm.submit();
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

</script>

<html>
<title><s:text name='page.title.rateContract.search'/></title>
<body class="yui-skin-sam" >
<div id="rateContractsearcherror" class="errorstyle" style="display:none;"></div>
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
<s:form theme="simple" name="rateContractSearchForm">
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
					<s:text name='ratecontract.search.label.fromDate' />:
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
					<s:text name='rateContract.search.label.toDate' />:
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
					<s:text name="rateContract.search.label.department" />:
				</td>
				<td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('rateContract.default.select')}" name="deptId" id="deptId" cssClass="selectwk" list="dropdownData.departmentList" listKey="id" listValue="deptName" value="%{department.id}" />
				</td>
			</tr> 
			<tr>
				<td class="greyboxwk">
					<s:text name="rateContract.search.label.rateContractNumber" />:
				</td>
				<td class="greybox2wk">
					<s:textfield name="rcNumber" id="rcNumber" 	cssClass="selectboldwk" value="%{rcNumber}" />
				</td>
				<td class="greyboxwk">
					<s:text name="indent.search.label.ircType" />:
				</td>
				<td class="greybox2wk">
					<s:select headerKey="-1" headerValue="%{getText('rateContract.default.select')}" name="indentType" value="%{indentType}" id="indentType" cssClass="selectwk" list="#{'Amount':'Amount', 'Item':'Item'}" value="%{indentType}"/>
				</td>
				<td class="greyboxwk">
					<s:text name="rateContract.search.label.contractorClass" />:
				</td>
				<td class="greybox2wk"><s:select headerKey="-1" headerValue="%{getText('rateContract.default.select')}" name="contractorGradeId" id="contractorGradeID" cssClass="selectwk" list="dropdownData.contractorGradeList" listKey="id" listValue="grade" value="%{contractorGrade.id}"/>
				</td>
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
			           <input type="button" class="buttonadd" value="SEARCH" id="searchButton" name="button" onclick="submitRCSearchForm();" />&nbsp;
			           <input type="button" class="buttonfinal" value="RESET" id="resetbutton" name="clear" onclick="this.form.reset();" />&nbsp;
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
									<s:text name="rateContract.search.label.result.title" />
								</div>
							</td>
						 </tr>
                      </table> 
                 </td>
            </tr> 
       
     </table>               
     </div>
     
        <%@ include file='rateContract-rcSearchResults.jsp'%> 	
	 <div class="rbbot2"><div></div></div>
</div>
</div>
</div>

</s:form>
</body>

</html>