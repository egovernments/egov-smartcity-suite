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
	
		function validate(){
			if(document.getElementById('materialType').value==-1)
			{
				dom.get("materialTypeSearch_error").style.display='';
				document.getElementById("materialTypeSearch_error").innerHTML='<s:text name="materialType.select.name" />';
				dom.get("materialType").focus();
				return false; 
			}
	
			document.getElementById("materialTypeSearch_error").innerHTML='';
			dom.get("materialTypeSearch_error").style.display="none";
	
			var id=document.getElementById('materialType');
			<s:if test="%{mode=='view'}"> 
				window.open("${pageContext.request.contextPath}/qualityControl/materialType!view.action?sourcepage=view&materialTypeId="+id.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
			</s:if>
			<s:if test="%{mode=='edit'}"> 
				window.open("${pageContext.request.contextPath}/qualityControl/materialType!edit.action?sourcepage=edit&materialTypeId="+id.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
			</s:if>
			<s:if test="%{mode=='createTestMaster'}"> 
				window.open("${pageContext.request.contextPath}/qualityControl/testMaster!newform.action?sourcepage=create&materialTypeId="+id.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
			</s:if>
			<s:if test="%{mode=='viewEditTestMaster'}"> 
				window.open("${pageContext.request.contextPath}/qualityControl/testMaster!searchTestMaster.action?materialTypeId="+id.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
			</s:if>
		}  
		
		</script>
	</head>	
	<body>	
	<div class="errorstyle" id="materialTypeSearch_error" style="display: none;"></div>
	 <s:form name="materialTypeUpdate" id="materialTypeUpdateForm" theme="simple"> 
     <s:token />
	 <s:hidden name="mode" id="mode" /> 
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
			<td class="greyboxwk">
				<span class="mandatory">*</span><s:text name="qualityControl.materialType" />:
			</td>
			<td class="greybox2wk">
				<s:select headerKey="-1" headerValue="--- Select ---" name="materialType" id="materialType" 
	        	cssClass="selectwk" list="dropdownData.materialTypeList" listKey="id" listValue="name" value="%{materialType.id}" />         	
			</td>
		</tr>
	 </table> 
	 <br> 
	</div>
	<div class="rbbot2"><div></div></div>
	</div>
	</div>
	
	<div class="buttonholderwk">
		<s:if test="%{mode=='view'}"> 
	 		<input type="button" class="buttonfinal" value="View" id="viewButton" name="viewButton" onclick="return validate();"/>
	 	</s:if>
	 	<s:if test="%{mode=='edit'}"> 
	 		<input type="button" class="buttonfinal" value="Modify" id="modifyButton" name="modifyButton" onclick="return validate();"/>
	 	</s:if>
	 	<s:if test="%{mode=='createTestMaster'}"> 
	 		<input type="button" class="buttonfinal" value="Create Test Master" id="createTM" name="createTM" onclick="return validate();"/>
	 	</s:if>
	 	<s:if test="%{mode=='viewEditTestMaster'}"> 
	 		<input type="button" class="buttonfinal" value="View" id="viewEditTM" name="viewEditTM" onclick="return validate();"/>
	 	</s:if>
	 	<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
	</div>
	
	</div>
	</s:form>
	</body>
</html>