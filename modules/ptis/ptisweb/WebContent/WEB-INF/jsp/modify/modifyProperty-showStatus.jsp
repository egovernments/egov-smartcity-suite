<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
		   <title><s:text name='AmalgPropStatus.title'/></title>
	
	<script type="text/javascript">
		function onLoad() {
			var propStatus =  '<s:property value="%{amalgStatus}"/>';
			if(propStatus != "Property is Ready for Amalgamation") {
				targetitem.value = "";
			}
			return false;
		}  
	</script>
  </head>
  
  <body onload="onLoad();">
  <s:form name="ModifyPropertyFormForm" theme="simple">
  <s:push value="model">
  <div class="formmainbox">
  <div class="formheading"></div>
		<div class="headingbg"><s:text name="AmalgPropStatus.title"/></div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
	        <td colspan="5" style="background-color: #FDF7F0;font-size: 15px;" align="center">
	        	<span class="bold"><s:property value="%{amalgStatus}"/></span>
	        </td>
		</tr>
		<tr>
				<td class="greybox" width="10%">&nbsp;</td>
				<td class="greybox" width="15%"><s:text name="prop.Id"/> :</td>
				<td class="greybox"><span class="bold"><s:property default="N/A" value="%{oldpropId}"/> </span> </td>
				<td class="greybox">&nbsp;</td>
				<td class="greybox">&nbsp;</td>
			</tr>
		<s:if test="amalgPropBasicProp!=null">
			<tr>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="OwnerName"/> :</td>
				<td class="bluebox"><span class="bold"><s:property default="N/A" value="%{oldOwnerName}"/> </span> </td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
			</tr>
			<tr>
				<td class="greybox" >&nbsp;</td>
	    		<td class="greybox"><s:text name="PropertyAddress"/> : </td>
	    		<td class="greybox"><span class="bold"><s:property default="N/A" value="%{oldPropAddress}"/></span></td>
	    		<td class="greybox" >&nbsp;</td>
	    		<td class="greybox" >&nbsp;</td>
			</tr>
		</s:if>
		</table>
	</div>
	<div class="buttonbottom" align="center">
			<td><input type="button" name="button2" id="button2" value="Close" class="button" onclick="window.close();"/></td>
		</div>
  </s:push>
  </s:form>
  </body>
</html>