<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<title>VOUCHER HISTORY</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">

<!-- Inclusion of the CSS files that contains the styles --> 
<link rel=stylesheet href="../css/egov.css" type="text/css">


<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<script language="javascript" src="../script/jsCommonMethods.js"></script>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>


<script language="javascript">


function onloadTasks()
{
       //To get the parameter values from the url
	var vhId="<%=(String)request.getParameter("vhId") %>";
	

	PageManager.DataService.setQueryField('vhId',vhId);
	PageManager.DataService.callDataService('listVouchHistory');
	PageManager.DataService.callDataService('list_VouchHistory');
	

}

</script>
</head>

<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="onloadTasks();" >
<form name="vouchHistory">

<br>
<h2 align=center><font color=#3670A7><u style=dash>VOUCHER HISTORY</u></font></h2>

<!--  History table starts here -->	
<table width="75%" cellpadding="0" align=center cellspacing="0" name="voucherHistory" ID="voucherHistory">
<br>
 <tr>
 	<td class="thStlyle" width="20%"><div align="center"  >TYPE</div></td>
	<td class="thStlyle" width="20%"><div align="center"  >NAME</div></td>
</tr>
	
<tr>
	<td class="tdStlyle"><div name="vType"  id="vType">&nbsp;</div></td>
	<td class="tdStlyle"><div name="vName"  id="vName">&nbsp;</div></td>	
</tr>
	
<tr>
	<td class="thStlyle" width="20%"><div align="center" >CREATED BY</div></td>
	<td class="thStlyle" width="20%"><div align="center" >CREATED ON</div></td>
</tr>

<tr >
	<td class="tdStlyle"><div name="vCreat"  id="vCreat">&nbsp;</div></td>
	<td class="tdStlyle"><div name="vCreat_on"  id="vCreat_on">&nbsp;</div></td>
</tr>
</table>

<table width="75%" cellpadding="0" align=center cellspacing="0" name="voucher_History" ID="voucher_History">
<tr>
	<td class="thStlyle" width="20%"><div align="center" >MODIFIED BY</div></td>
	<td class="thStlyle" width="20%"><div align="center" >MODIFIED DATE</div></td>
	<td class="thStlyle" width="20%"><div align="center" >STATUS</div></td>
</tr>

<tr >
	<td class="tdStlyle"><div name="vMod_by"  id="vMod_by">&nbsp;</div></td>
	<td class="tdStlyle"><div name="vMod_date"  id="vMod_date">&nbsp;</div></td>
	<td class="tdStlyle"><div name="vStat"  id="vStat">&nbsp;</div></td>
</tr>
</table>
   
</form>

<!-- Buttons start here -->   
<table border="0" cellpadding="0" cellspacing="0" id="buttons" align=center>
<br><br><br>
<tr id="submitRow"><td align="center">
<td>
<input type=button class=button onclick="back(window.self)" href="#" value="Back">
</td>
</tr>
</table>
    
</body>
</html>