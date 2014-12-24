<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>

<html>  
<head>  
    <title><s:text name='rateContract.search.title'/></title>  
</head>  
<body>
<script src="<egov:url path='js/works.js'/>"></script>

<script type="text/javascript">

function setupIndents(elem){
	var fileNum=elem.value;
	if(fileNum!=null){
		populateindent({fileNum:fileNum});
	}
	else
    {
    	document.getElementById("indent").value=-1;
    }
}

function openTenderFile(){
window.open("${pageContext.request.contextPath}/tender/tenderFile!search.action?sourcepage=rcSourcePage",'viewTenderFile',"width=800, height=800, resizable=yes, scrollbars=yes, left=250,top=400");
}


function update(elemValue) {	
	if(elemValue!="" || elemValue!=null) {
			var a = elemValue.split("`~`");
			var row_id=a[0];
			var tenderFileNum=a[1];
			document.getElementById('fileNumber').value=tenderFileNum;
			document.getElementById('fileNumber').focus();
	}
}
function submitTenderFileSearchForm(){
clearMessage('rateContract_error');
var err = false;

		if(document.getElementById('fileNumber').value == "")
		{
			dom.get("rateContract_error").style.display='';
			document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.fileNumber.null" /><br>';
			err = true;
		}
	 	
	 	if(document.getElementById('indent').value==-1)
	 	{
	 		dom.get("rateContract_error").style.display='';
			document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.indent.null" /><br>';
			err = true;
	 	}
	 	if(!err)
	 	{
	 		var fileNumber = document.getElementById('fileNumber').value;
			document.rateContractForm.action='${pageContext.request.contextPath}/rateContract/rateContract!searchTenderResponse.action?mode=search&tenderFileNum='+fileNumber+'';
	    	document.rateContractForm.submit();
	    }
}

</script>
<div class="errorstyle" id="rateContract_error" style="display:none;"></div>
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

<s:form theme="simple" name="rateContractForm" > 
<div class="formmainbox">
	<div class="insidecontent">
  		<div class="rbroundbox2">
		<div class="rbtop2"><div></div></div>  
     	<div class="rbcontent2">  
     	
     	<div class="datewk">
 		&nbsp;
	  </div>
	  	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  	
	  	
	  	
	  	<tr>
	        	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
	            <div class="headplacer"><s:text name="page.subheader.search.rateContract" /></div></td>
	        </tr>
	          
		 <tr>
            	<td  colspan="4" class="shadowwk"> </td>               
	             </tr>
			 <tr>
				 <td  class="greyboxwk"><span class="mandatory">*</span><s:text name="rateContract.fileNumber" />:</td>
				 <td  class="greybox2wk" ><s:textfield id="fileNumber" name="fileNumber" value="%{fileNumber}" cssClass="selectwk" onfocus="setupIndents(this);" onblur="setupIndents(this);"/>&nbsp;<a href="javascript:openTenderFile();"><img src="${pageContext.request.contextPath}/image/searchicon.gif" width="16" height="16" border="0"/></a></td>				
					<egov:ajaxdropdown id="indentDropdown" fields="['Text','Value']" dropdownId='indent' url='rateContract/ajaxRateContract!loadIndents.action' selectedValue="%{indent.id}"/>	
				 <td  class="greyboxwk"><span class="mandatory">*</span><s:text name="rateContract.indent" />:</td>
		         <td  class="greybox2wk" ><s:select headerKey="-1" headerValue="%{getText('indent.default.select')}" name="indent" id="indent"  cssClass="selectwk" list="dropdownData.indentNumList" listKey="id" listValue="indentNumber" value="%{indent.id}"  /></td>
			</tr>
	  	  
		 <tr>
            	<td  colspan="4" class="shadowwk"> </td>               
	             </tr>
              
            <tr>
			<td colspan="4" class="mandatory" align="right">* <s:text name="message.mandatory" /></td> 
			</tr>  		
            <tr>
                 <td colspan="4"> 
                   <div class="buttonholderwk">
		             <p>
			           <input type="button" class="buttonadd" value="SEARCH" id="searchButton" name="button" onclick="submitTenderFileSearchForm();" />&nbsp;
			           <input type="button" class="buttonfinal" value="RESET" id="resetbutton" name="clear" onclick="this.form.reset();">&nbsp;
		               <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();" />
	                 </p>
		          </div>
                </td>
            </tr>
            <tr>
				<td colspan="4" align="left">
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
     
        <%@ include file='rateContract-searchResults.jsp'%> 	</div>
	 <div class="rbbot2"><div></div></div>
</div>
</div>
</div>

</s:form>
</body>
</html>