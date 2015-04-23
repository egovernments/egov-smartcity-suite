<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title><s:text name='nmc.ptis.pay.collection.title'/></title>
  <script type="text/javascript">
  </script>
  </head>
  
  <body>
  <s:form theme="simple" action="collectPropertyTax" name="CollectPropertyTaxForm">
  <s:token />
  <div class="formmainbox">
  <div class="formheading"></div>
		<div class="headingbg"><s:text name='nmc.ptis.pay.collection.title'/></div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
				<td colspan="5" style="background-color: #FDF7F0;font-size: 15px;" align="center">
		        	<span class="bold">Collection is fully/excess paid for index number : <s:property value="%{indexNum}"/>. For more details please go to DCB screen.</span>
		        		
		        </td>		    
		</tr>
		</table>
	</div>
	<div class="buttonbottom" align="center">
		<td>
			<input type="button" class="buttonsubmit" name="btnViewDCB" id="btnViewDCB" value="View DCB"
			onclick="window.location='../../view/viewDCBProperty!displayPropInfo.action?propertyId=<s:property value="%{indexNum}" />';" />
		</td>
		<td>
			<input type="button" class="buttonsubmit" name="SearchProperty"
					id="SearchProperty" value="Search Property" onclick="window.location='../../citizen/search/search!searchForm.action';" />
		</td>
		<td>
			<input type="button" name="button2" id="button2" value="Close" class="button" onclick="window.close();"/>
		</td>
		</div>
  </s:form>
  </body>
</html>