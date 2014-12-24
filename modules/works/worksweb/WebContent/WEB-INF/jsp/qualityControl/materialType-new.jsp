<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<html> 
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=8" />
		<title>
			<s:text name="qualityControl.materialType.title" />
		</title>
		<script src="<egov:url path='js/works.js'/>"></script>
		<script src="<egov:url path='js/helper.js'/>"></script>
		<script type="text/javascript"> 
		function validateInput(){
			if(document.getElementById('name').value=="")
			{
				dom.get("materialType_error").style.display='';
				document.getElementById("materialType_error").innerHTML='<s:text name="materialType.enter.name" />';
				dom.get("name").focus();
				return false; 
			}
			// Enable Fields				
			for(var i=0;i<document.forms[0].length;i++) {
	      		document.forms[0].elements[i].disabled =false; 
	      	}
			document.getElementById("materialType_error").innerHTML='';
			dom.get("materialType_error").style.display="none";
	      	return true;
		}
	
		function viewDocument(){
			  viewDocumentManager(dom.get("docNumber").value); 
		} 
	
		function enableOrdisableElements()
		{
			 if('<s:property value="isActive"/>' == "true"){
				 document.materialTypeForm.isMTActive.checked=true;
				 document.materialTypeForm.isMTActive.value=true;	
			  }
			
			<s:if test="%{sourcepage=='view'}">
				for(var i=0;i<document.forms[0].length;i++) {
		      		document.forms[0].elements[i].disabled =true;
		      	}
		      	
				document.materialTypeForm.closeButton.readonly=false;
				document.materialTypeForm.closeButton.disabled=false;
	
				if(document.materialTypeForm.docViewButton!=null){
				    document.materialTypeForm.docViewButton.readonly=false; 
					document.materialTypeForm.docViewButton.disabled=false; 
			    }
			</s:if>
	
			<s:if test="%{sourcepage=='edit'}">
				document.materialTypeForm.name.disabled=true;
				document.materialTypeForm.isMTActive.disabled=false;
			</s:if>
	
			<s:if test="%{sourcepage!='edit' && sourcepage!='view'}">
				document.materialTypeForm.isMTActive.checked=true;
	 			document.materialTypeForm.isMTActive.value=true;
	 			document.materialTypeForm.isMTActive.disabled=true;
			</s:if>
		}
	
	
		function markCheckBox(){
		    if(document.materialTypeForm.isMTActive.checked==true){
		  		document.materialTypeForm.isMTActive.checked=true;
		 		document.materialTypeForm.isMTActive.value=true;
		    }else{  
		    	document.materialTypeForm.isMTActive.checked=false;
		 		document.materialTypeForm.isMTActive.value=false;
		    }
		}
	
		</script>
	</head>	
	
	<body onload="enableOrdisableElements();">	
	<s:if test="%{hasErrors()}"> 
        <div class="errorstyle">
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
   
	<s:if test="%{hasActionMessages()}">
        <div class="messagestyle"> 
        	<s:actionmessage theme="simple"/>
        </div>
    </s:if>
   	<div class="errorstyle" id="materialType_error" style="display: none;"></div>
	<s:form action="materialType" name="materialTypeForm"  theme="simple">
	<s:if test="%{sourcepage!='view'}">
	<s:token />
	</s:if>
	<s:push value="model">
	<s:hidden name="id" id="id" />
	<s:hidden name="documentNumber" id="docNumber" /> 
	<s:hidden name="sourcepage" id="sourcepage" /> 
	<div class="formmainbox">
	<div class="insidecontent">
	<div class="rbroundbox2">
	<div class="rbtop2"><div></div></div> 
	<div class="rbcontent2">
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
      	<tr>
       		<td colspan="4" class="headingwk">
       			<div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
         		<div class="headplacer"><s:text name="materialType.header" /></div>
          	</td>
      	</tr>
     
      	 <tr>
			<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="qualityControl.materialType.name" /></td>
	        <td class="whitebox2wk"><s:textfield name="name" type="text" cssClass="selectwk" id="name" value="%{name}"/></td>
	
	        <td class="whiteboxwk"><s:text name="qualityControl.materialType.description" /></td>
	        <td class="whitebox2wk"><s:textarea name="description" cols="35" cssClass="selectwk" id="description" value="%{description}"/></td>
	     </tr>
	     
	     <tr>
	     	<td class="greyboxwk"><s:text name="qualityControl.materialType.freqChart" /></td>
			<td  class="greybox2wk">
			<s:if test="%{sourcepage == null || sourcepage == '' || (sourcepage != null && sourcepage == 'edit')}">
				<input type="submit" class="buttonadd" value="Upload" id="docUploadButton" onclick="showDocumentManager();return false;" />
			</s:if>
			<s:if test="%{sourcepage != null && sourcepage == 'view' }">
				<input type="submit" class="buttonadd" value="View Document" id="docViewButton" onclick="viewDocument();return false;" />
			</s:if>
			</td>
	     
	    	<td class="greyboxwk"><s:text name="qualityControl.materialType.isActive" /></td>
			<td  class="greybox2wk"><input name="isMTActive" type="checkbox" id="isMTActive"  onclick="markCheckBox();" /></td> 
		</tr>
	 </table> 
	 <br> 
	 <div id="mandatary" align="right" class="mandatory" style="font-size: 11px; padding-right: 20px;">* 
		<s:text name="message.mandatory" />
	</div>    
	</div>
	<div class="rbbot2"><div></div></div>
	</div>
	</div>
	
	<div class="buttonholderwk">
	<s:if test="%{sourcepage == null || sourcepage == ''  || (sourcepage != null && sourcepage == 'edit')}">
	 	<s:submit cssClass="buttonfinal" onclick='return validateInput()' value="Submit" id="submitButton" method="save" />
	 	<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="confirmClose('<s:text name='materialType.close.confirm'/>');"/>
	 </s:if>
	 <s:if test="%{sourcepage != null && sourcepage == 'view' }">
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
	</s:if>
	</div>
	
	</div>
	</s:push>
	</s:form>
	</body>
</html>