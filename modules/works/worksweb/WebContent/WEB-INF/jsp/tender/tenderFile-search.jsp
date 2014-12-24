<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>

<html>
<title><s:text name='page.title.tender.file.search'/></title>
<head>
	<script src="<egov:url path='js/works.js'/>"></script>
	<script src="<egov:url path='js/helper.js'/>"></script>
	<script>
	
	function submitTenderFileSearchForm() {
	    clearMessage('tenderfilesearcherror')
		var err=false;
		var department = document.tenderFileSearchForm.department.value;
		if(document.tenderFileSearchForm.sourcepage.value != "rcSourcePage"){
		document.getElementById('egwStatus').disabled=false;	
		if(department<0) {
			dom.get("tenderfilesearcherror").style.display=''; 
			document.getElementById("tenderfilesearcherror").innerHTML+='<s:text name="tenderfile.search.department.not.null" /><br>';
			err=true;    
		}
		}
		if(!validateDateFormat(document.tenderFileSearchForm.fromDate) || !validateDateFormat(document.tenderFileSearchForm.toDate)){
			dom.get("tenderfilesearcherror").style.display=''; 
			document.getElementById("tenderfilesearcherror").innerHTML+='<s:text name="invalid.fieldvalue.tenderfileDate" /><br>';
			err=true; 
		}
		if(!err){
		
		    document.tenderFileSearchForm.action='${pageContext.request.contextPath}/tender/tenderFile!searchDetails.action';
		    document.tenderFileSearchForm.submit();
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
	
	
	function returnBackToParent(fileNumber) {
				
					var wind;
					var data = new Array();
					wind=window.dialogArguments;
					row_id = $('rowid').value;
					
					if(wind==undefined){
						wind=window.opener;
						data=row_id+ '`~`' + fileNumber;
						window.opener.update(data);
					}
			
					else{
					
						wind=window.dialogArguments;
						wind.result=row_id+ '`~`'  + fileNumber;
					}
					window.close();
				}
	
				
	
				function ChangeColor(tableRow, highLight)
				{
					if (highLight)
					{
					  tableRow.style.backgroundColor = '#dcfac9';
					}
					else
					{
					  tableRow.style.backgroundColor = 'white';
					}
				}
				
	
	function setDefault() {
		<s:if test="%{sourcepage=='rcSourcePage'}">
			document.getElementById('egwStatus').disabled=true;	 
		</s:if>
	}
	
	function checkQuotation(obj){ 
	   if(obj.checked){
		 document.tenderFileSearchForm.quotation.value=true;
		}
		else if(!obj.checked){
		 document.tenderFileSearchForm.quotation.value=false;
		} 
	}
	
	</script>
</head>
<body class="yui-skin-sam" onload="setDefault()">
<div id="tenderfilesearcherror" class="errorstyle" style="display:none;"></div>
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
<s:form theme="simple" name="tenderFileSearchForm" >
<s:hidden name="sourcepage" value="%{sourcepage}" id="sourcepage"/>
<s:hidden name="rowId" id="rowid"/>


<div class="errorstyle" id="indent_error"
				style="display: none;"></div>
<div class="formmainbox"><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	<div class="rbcontent2"><div class="datewk">
	 <table width="100%" border="0" cellspacing="0" cellpadding="0">
             <tr>
				<td colspan="4">&nbsp;</td>
			 </tr>
			 <tr>
				<td colspan="4">&nbsp;</td>
			 </tr>
			 <tr>
			 <td colspan="4" class="headingwk" align="left">
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
					<s:text name='tender.file.search.label.fromDate' />:
				</td>
				<td width="37%" class="whitebox2wk">
					<s:date name="fromDate" var="fromDateFormat" format="dd/MM/yyyy" />
					<s:textfield name="fromDate"  id="fromDate" cssClass="selectboldwk" value="%{fromDateFormat}" onfocus="javascript:vDateType='3';"
						onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="checkDate(this)"/>
					<a href="javascript:show_calendar('forms[0].fromDate',null,null,'DD/MM/YYYY');" 
						onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;">
					<img src="${pageContext.request.contextPath}/image/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
				<span id='errorfromDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>									
				</td>  
				<td width="14%" class="whiteboxwk">
					<s:text name='tender.file.search.label.toDate' />:
				</td>
				<td width="37%" class="whitebox2wk">
					<s:date name="toDate" var="toDateFormat" format="dd/MM/yyyy" />
					<s:textfield name="toDate"  id="toDate" cssClass="selectboldwk" value="%{toDateFormat}" onfocus="javascript:vDateType='3';"
						onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="checkDate(this)"/>
					<a href="javascript:show_calendar('forms[0].toDate',null,null,'DD/MM/YYYY');" 
						onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;">
					<img src="${pageContext.request.contextPath}/image/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
				<span id='errortoDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>									
				</td>  
				</tr> 
				<tr>
				<td width="11%" class="greyboxwk"><s:if test="%{sourcepage!='rcSourcePage'}"><span class="mandatory">*</span></s:if>
					<s:text name="tender.file.search.label.department" />:
				</td>
				<td class="greybox2wk"><s:select headerKey="-1" headerValue="%{getText('indent.default.select')}" name="department" id="department" cssClass="selectwk" list="dropdownData.departmentList" listKey="id" listValue="deptName" value="%{department.id}" />
				</td>
				<s:if test="%{sourcepage!='rcSourcePage'}">
				<td class="greyboxwk">
					<s:text name="tender.file.search.label.fileNumber" />:
				</td>
				<td class="greybox2wk">
				
					<s:textfield name="fileNumber" id="fileNumber" 	cssClass="selectboldwk" value="%{fileNumber}" />
					
					
				</td>
				
				</s:if>
				
				<s:else><td width="11%" class="greyboxwk">
					<s:text name="tender.file.search.label.status" />:
				</td>
				<td class="greybox2wk">
				<s:select name="egwStatus" id="egwStatus" cssClass="selectwk" list="dropdownData.statusList" listKey="id" listValue="description" value="%{egwStatus.id}"  />
				</td>
				</s:else>
				</tr> 
				<tr>
				<s:if test="%{sourcepage!='rcSourcePage'}">
				<td width="11%" class="whiteboxwk">
					<s:text name="tender.file.search.label.status" />:
				</td>
				<td  class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('indent.default.select')}" name="egwStatus" id="egwStatus" cssClass="selectwk" list="dropdownData.statusList" listKey="id" listValue="description" value="%{egwStatus.id}"  />
						</td>
				
						 <td class="whiteboxwk" ><s:checkbox name="quotation" id="quotation" value="%{quotation}" onclick="checkQuotation(this)"/></td>
					 	 <td class="whitebox2wk"><b><s:text name="tenderFile.isQuotation.label"/></b></td>
				</s:if>
				</tr>
				
	            <tr>
					<td width="11%" class="greyboxwk">
						<s:text name="tenderFile.search.estimateNo" />
						:
					</td>
					<td width="21%" class="greybox2wk">
						<s:textfield name="estNumber"
							value="%{estNumber}" id="estNumber"
							cssClass="selectwk" />
					</td>     
					<td  colspan="2" class="greybox2wk"> </td>          
	            </tr>
	             
               <tr><td>&nbsp;</td></tr>	
            <tr>
			<td colspan="4" class="mandatory" align="right">* <s:text name="message.mandatory" /></td> 
			</tr>  		
            <tr>
                 <td colspan="4"> 
                   <div class="buttonholderwk">
		             <p>
			           <input type="button" class="buttonadd" value="SEARCH" id="searchButton" name="button" onclick="submitTenderFileSearchForm()" />&nbsp;
			           <input type="button" class="buttonfinal" value="RESET" id="resetbutton" name="clear" onclick="this.form.reset();">&nbsp;
		               <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();" />
	                 </p>
		          </div>
                </td>
            </tr>
            <s:if test="%{sourcepage!='rcSourcePage'}">
            <tr>
            
				<td colspan="4" align="left">
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
       </s:if>
     </table>               
     </div>
     <s:if test="%{sourcepage!='rcSourcePage'}">
        <%@ include file='tenderFile-searchResults.jsp'%> 	
        </s:if>
        <s:elseif test="%{tenderFileLst != null}">
         				
						<div id="capsearch" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td><table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="4%" class="headingwk">
													<div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" />
													</div>
													<div class="headplacer"><s:text name='contractor.search.label.searchResult' />
													</div>
													</td>
												</tr>
																		
												<tr>
													<td class="aligncenter"><table width="600" border="0" cellpadding="0" cellspacing="0" align="center">
												<tr>
													<td width="11%" class="tablesubheadwka"><s:text name='contractor.search.label.slno' />:
													</td>
																						
													<td width="35%" class="tablesubheadwka"><s:text name='Tender File Name' />:
													</td>
													
													<td width="20%" class="tablesubheadwka"><s:text name='Tender File Number' />:
													</td>
												</tr>
												</table>
												
												<div class="searchscrollershort2wk">
													<table width="600" border="0" cellpadding="0" cellspacing="0">
													<s:iterator value="tenderFileLst" var="cont" status="stat">
													<tr onmousedown="" onmouseover="ChangeColor(this, true);" onmouseout="ChangeColor(this, false);" href="#"
															onclick="javascript:returnBackToParent('<s:property value="fileNumber"/>')"
															id="getdate" style="cursor: hand">
															
															<td width="11%" class="whitebox3wka1">
																	<s:label value="%{#stat.index + 1}" />
															</td>
															
															<td width="35%" class="whitebox3wka1">
																	<s:property  value="fileName" />
															</td>
															
															<td width="20%" class="whitebox3wka1">
																	<s:property  value="fileNumber" />
															</td>
													</tr>
													</s:iterator>
													</table>
													</div>
													</td>
												</tr>
											
												<tr>
													<td class="shadowwk"></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</div>
						</s:elseif>
	 <div class="rbbot2"><div></div></div>
</div>
</div>
</div>

</s:form>
</body>
</html>