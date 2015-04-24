<%@ include file="/includes/taglibs.jsp"%>
<html>
<head>
<title>  <s:text name="service.master.search.header"></s:text> </title>

<script>
function validate(){
	dom.get('error_area').innerHTML = '';
	dom.get("error_area").style.display="none"
	if(dom.get('serviceCategoryid').value == -1){
		dom.get("error_area").innerHTML = '<s:text name="error.select.service.category" />';
		dom.get("error_area").style.display="block";
		return false;
	}
   return true;
}
</script>
</head>

<body>
<s:form action="serviceDetails" theme="simple" name="serviceDetailsForm" method="post">

	 <div class="errorstyle" id="error_area" style="display:none;"></div>
	<div class="formmainbox">
	<div class="subheadnew"><s:text name="service.master.search.header"></s:text> </div>
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0">

		
		<tr>
			<td width="4%" class="bluebox2">&nbsp;</td>
			<td width="21%" class="bluebox2"><s:text name="service.master.search.category"></s:text> <span class="mandatory">*</span></td>
			<td width="30%" class="bluebox2"><s:select headerKey="-1"
				headerValue="----Choose----"
				name="serviceCategory" id="serviceCategoryid" cssClass="selectwk"
				list="dropdownData.serviceCategoryList" listKey="id" listValue="name"
				value="%{serviceCategory.id}" /></td>
			
		</tr>
		

	</table>
<div align="left" class="mandatorycoll"><s:text name="common.mandatoryfields"/></div>
	</div>
	
	<div class="buttonbottom">
			<label>
				<s:submit type="submit" cssClass="buttonsubmit" id="button"
					value="Create Service" method="beforeCreate"
					onclick="return validate();" />
			</label>&nbsp;
			
			<label>
				<s:submit type="submit" cssClass="buttonsubmit" id="button"
					value="List Services" method="listServices"
					onclick="return validate();" />
			</label>			
		</div>

</s:form>
</body>
</html>