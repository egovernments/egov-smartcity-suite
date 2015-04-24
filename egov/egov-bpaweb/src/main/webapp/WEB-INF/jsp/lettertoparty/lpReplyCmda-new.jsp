<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<%@ include file="/includes/taglibs.jsp" %>

<html>
<title>
	<s:text name="letterToPartyReply"/>
</title>

<head>
<sj:head jqueryui="true" jquerytheme="blitzer" loadAtOnce="true" />


<script type="text/javascript">
jQuery.noConflict();

function validateForm(){
	var count =0;
	var j=0;
	var documentFile =dom.get("docNumber").value;

   		
	dom.get("lp_error").style.display='none';
	
	if(document.getElementById('lpReplyDescription').value==null || document.getElementById('lpReplyDescription').value==""){
		dom.get("lp_error").style.display = '';
		document.getElementById("lp_error").innerHTML = '<s:text name="lpreply.desp.mandatory" />';
		return false;
	}
	
		/* if(documentFile==null || documentFile=="")
			 {
			 	dom.get("lp_error").style.display = '';
				document.getElementById("lp_error").innerHTML = '<s:text name="lpreply.Document.required" />';
				return false;
			 }
	
	jQuery("[id=mandatorycheck]").each(function(index) {	
		
       if(jQuery(this).find(':checkbox').prop('checked')){
    	  	dom.get("lp_error").style.display = 'none';
    		document.getElementById("lp_error").innerHTML='';
   	   	;
   		
   		}else{ 		
   		  j++;
   		
   		dom.get("lp_error").style.display = '';
   		jQuery(this).find(':checkbox').css('outline-color', 'red');
		jQuery(this).find(':checkbox').css('outline-style', 'solid');
		jQuery(this).find(':checkbox').css('outline-width', 'thin');
		
		}
		});		
	var size=jQuery('#checklistsize').val();
	
	if(size==j){
	document.getElementById("lp_error").innerHTML = 'Atleast one checkList needs be select';
			return false;
	}
	
	jQuery("[id=mandatorycheck]").each(function(index) {	
			if(jQuery(this).find(':checkbox').prop('checked')){
							var rem=jQuery(this).next("td").find('textarea').attr("value");
   							if(rem==""){
   								dom.get("lp_error").style.display = '';
   									document.getElementById("lp_error").innerHTML = 'Please enter the remarks for the checked checkList';
   									jQuery(this).next("td").find('textarea').css("border", "1px solid red");
   									count++;
   									}
   					}
   		});	
   		if(count!=0)
			return false;
	*/
}

function confirmClose(){
	var result = confirm("Do you want to close the window?");
	if(result==true){
		window.close();
		return true;
	}else{
		return false;
	}
}


function onreportupload()
	 {
	     var v= dom.get("docNumber").value;
	     var url;
	     if(v==null||v==''||v=='To be assigned')
	     {
		   url="/egi/docmgmt/basicDocumentManager.action?moduleName=BPA";
	     }
	     else
	     {
		   
	       url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="+v+"&moduleName=BPA";
	     }
	     var wdth = 1000;
	     var hght = 400;
	     window.open(url,'docupload','width='+wdth+',height='+hght);
	 }	
</script>

</head>
<body onload="refreshInbox();">
<div class="errorstyle" id="lp_error" style="display:none;" >
</div>
<s:if test="%{hasErrors()}">
		<div class="errorstyle" id="fieldError">
			<s:actionerror />
			<s:fielderror />
		</div>
</s:if>

<s:if test="%{hasActionMessages()}">
		<div class="errorstyle">
				<s:actionmessage />
		</div>
</s:if>

<s:form action="lpReplyCmda" theme="simple">
<s:token/>
<s:push value="model">

<s:hidden id="mode" name="mode" value="%{mode}"/>
<s:hidden id="registrationId" name="registrationId" value="%{registrationId}"/>
<s:hidden id="requestID" name="requestID" value="%{requestID}"/>

<div  id="lpdetails" class="formmainbox">
<s:if test="%{letterParty != null && letterParty.isHistory != 'Y'}">
<div class="headingbg"><s:text name="lettertoparty.header"/></div>
 <s:if test="%{mode !='noEditMode'}">
 <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
 		
	   	 	<tr>
	   	 		<td class="bluebox" ><s:text name="lpNum"/> : </td>
				<td class="bluebox" >
					<s:property value="existLpNum"/>
				</td>
	   	 		<td class="bluebox" colspan="2">&nbsp;</td>
          </tr>
          <tr>
	   	 		<td class="bluebox" ><s:text name="lpreason"/> : </td>
				<td class="bluebox" >
					<s:property value="existLpReason"/>
				</td>
	   	 		<td class="bluebox" ><s:text name="lpdescription"/></td>
	 			<td class="bluebox">
	 				<s:property value="existLpRemarks"/>
	 			</td>
          </tr>
       
          <tr>
	   	 	     <td class="bluebox" ><s:text name="lpReplyDesc"/> <span class="mandatory" >*</span>: </td>
	 			<td class="bluebox">
	 				<s:textarea id="lpReplyDescription" name="lpReplyDescription" value="%{lpReplyDescription}" cols="20" rows="3"/>
	 			</td>  
	 			<td class="bluebox" ><s:text name="lpReplyRemarks"/> : </td>
	 			<td class="bluebox">
	 				<s:textarea id="lpReplyRemarks" name="lpReplyRemarks" value="%{lpReplyRemarks}" cols="20" rows="3"/>
	 			</td>   			
          </tr>
         
		<tr> 
			<td class="bluebox">
								<s:text name="Document upload: " />
						</td>
						<s:if test="documentNum==null || documentNum==''">
								<td id="addlink" class="bluebox">
									<div class="buttonholderrnew">
										<input type="button" id="browse" value="Browse"
											onclick="onreportupload();return false;"
											class="buttoncreatenewcase" />
									</div>
								</td>
							</s:if>
					
					   		 <td class="bluebox"/>
							  <td class="bluebox"/>
                       
				<s:hidden name="documentNum" id="docNumber" value="%{documentNum}"></s:hidden>
                      
                </tr>

	 </table>
	 </s:if>
	 </s:if>
	 </div>
  	
	 <div class="buttonbottom" align="center"> 
		<table>
		<tr> 
		 <s:if test="%{(mode !='noEditMode' && mode!='view') && (letterParty != null && letterParty.isHistory != 'Y')}">
		  	<td><s:submit  cssClass="buttonsubmit" id="save" name="save" value="Save" method="createLpReply" onclick="return validateForm()"/></td>	
	  	 </s:if>	         
	  	 <s:if test="%{mode=='view' && mode !='noEditMode'}">
	  		<td>
	  			<s:submit type="submit" cssClass="buttonsubmit" value="Print Ack" id="print" name="print" method="ackPrint"/>
	  		</td>
	  	</s:if>
	  		 <td><input type="button" name="close" id="close" class="button"  value="Close" onclick="confirmClose();"/>
	  		</td>
	  	</tr>
        </table>
    </div>
</s:push>
</s:form>

</body>
</html>
