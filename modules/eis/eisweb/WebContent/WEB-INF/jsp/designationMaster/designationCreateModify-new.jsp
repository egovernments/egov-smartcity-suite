<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<html>
<head>
<title><s:text name="Designation" /></title>
<script type="text/javascript">	

	function validateForm() 
	{
	if (document.getElementById('designationName').value == "")
	{				
		showError("Please enter designation name");
		return false;
	}
		if (document.getElementById('designationDescription').value == "" )
		{				
			showError("Please enter description for the designation");
			return false;
		}
		return true;
	}	

	function validateDesignationNameUniqueness(obj)
	{
		
		if (!document.getElementById('designationName').value == "")
		{			
			showError("");
			var desName =  document.getElementById('designationName').value;
			populatedesignationNameUniqueness({desName:desName});
		}    
		else
		{				
			showError("Please enter designation name");
			return false;
		}
	} 

	function clearMsgIfNotEmpty()
	{
		if (!document.getElementById('designationDescription').value == "")
			{
			showError("");
			}
		else
			{				
				showError("Please enter description for the designation");
				return false;
			}

	}
	function showError(msg)
	{
		document.getElementById("designationCreateModify_error").style.display='none';
		if(document.getElementById("fieldError")!=null)
			document.getElementById("fieldError").style.display='none';
		dom.get("designationCreateModify_error").style.display = '';
		document.getElementById("designationCreateModify_error").innerHTML = msg;
	}

	function onView()
	{
		<s:if test="%{mode=='view'}">
		{
			document.getElementById('designationName').disabled = true;
			document.getElementById('designationDescription').disabled = true;
			
		}
		</s:if>

	}	
</script>
</head>

<body onload="onView();">
<div class="formmainbox">
<div class="insidecontent">
<div class="rbroundbox2">

	<s:form action="designationCreateModify" theme="simple">
		<s:push value="model">
			<div class="mandatory" id="designationCreateModify_error"
				style="display: none;"></div>
			<div class="mandatory" style="display: none"
				id="designationNameUniqueness">
				<s:text name="designation.name.alreadyExists" />
			</div>
			<s:if test="%{hasErrors()}">
				<div class="errorcss" id="fieldError">
					<s:actionerror cssClass="mandatory" />
					<s:fielderror cssClass="mandatory" />
				</div>
			</s:if>
			<div align="center">
			<s:if test="hasActionMessages()">
			<s:actionmessage cssClass="actionmessage" />
			</s:if>
			</div><s:token />
			<s:hidden id="mode" name="mode" value="%{mode}" />
			<s:hidden id="designationId" name="designationId"
				value="%{designationId}" />
				<div class="rbcontent2">
			<table width="95%" cellpadding="0" cellspacing="0" border="0">
				<tbody><tr><td>&nbsp;</td></tr>
				
					<tr>
						<td  class="headingwk">
						<div class="arrowiconwk">
								<img src="${pageContext.request.contextPath}/common/image/arrow.gif" />
							</div>
							<p>
							<div class="headplacer">
							<s:if test="%{mode=='save'}">
								DESIGNATION
							</s:if>
							<s:if test="%{mode=='edit'}">
							<s:text name="Modify designation" />
							</s:if>
							<s:if test="%{mode=='view'}">
							<s:text name="View designation" />
							</s:if>
							</div>
							</p></td>
						<td></td>
					</tr>
				</tbody>
			</table>
			<br>
			<table width="95%" cellpadding="0" cellspacing="0" border="0">
				<tbody>
					<tr>
						<td class="greyboxwk"><span class="mandatory">*</span><s:text name="Designation Name" /></td>
						<td class="greybox2wk"><s:textfield id="designationName"
								name="designationName" value="%{designationName}"
								onblur="validateDesignationNameUniqueness(this);" /> <egov:uniquecheck
								id="designationNameUniqueness" fields="['Value']"
								url='designationMaster/designationCreateModify!designationNameUniqueness.action'
								fieldtoreset="designationName" /></td>
						<td class="greyboxwk"><span class="mandatory">*</span><s:text name="Description" /></td>
						<td class="greybox2wk"><s:textfield
								id="designationDescription" name="designationDescription"
								value="%{designationDescription}" onblur="return clearMsgIfNotEmpty();"/></td>
					</tr>
				
				</tbody>
			</table>
<table width="95%" cellpadding="0" cellspacing="0" border="0">	<tbody><tr>
								    	<td align="right">
								    	<div class="mandatory">* Mandatory Fields</div></td>
								    </tr></tbody></table>

			<br>

			
				</div>
			<div class="rbbot2"><div></div></div>
		</s:push>
	<table align="center">
				<tr></tr>
				<tr>
					<td align="center" colspan="4"><s:if test="%{mode =='save'}">

							<s:submit method="saveDesignation" value="SAVE"
								cssClass="buttonfinal" onclick="return validateForm();"  />
						</s:if> <s:if test="%{mode=='edit'}">
							<s:submit method="saveModifiedDesignation" value="MODIFY"
								cssClass="buttonfinal" onclick="return validateForm();" />
						</s:if> <input type="button" name="close" id="close" value="CLOSE"
						onclick="window.close();" class="buttonfinal" /></td>
				</tr>
			</table></s:form></div></div>
</div>
</body>
</html>