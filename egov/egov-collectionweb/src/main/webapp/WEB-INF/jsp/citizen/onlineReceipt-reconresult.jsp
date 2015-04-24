<%@ include file="/includes/taglibs.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><s:text name="coc.title"/></title>
<script type="text/javascript">
function onBodyLoad(){
	
}

</script>
</head>
<body>
<s:if test="%{hasErrors()}">
    <div class="errorstyle">
      <s:actionerror/>
      <s:fielderror/>
    </div>
</s:if>
<s:else>
	<table cellpadding="0" cellspacing="0" border="0" class="main" align="center">
		
	<tr>Manual Reconciliation of Online Payment Receipts Successful</tr>
	
	</table>
	
</s:else>

<br/>
<div class="buttonbottom">
<input name="button2" type="button" class="button" id="button" onclick="window.close()" value="Close"/>
</div>
</body>
</html>
