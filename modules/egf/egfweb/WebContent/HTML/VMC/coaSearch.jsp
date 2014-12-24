<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<title>COA Search</title>

<SCRIPT LANGUAGE="javascript">
	function getGrid()
	{
		if(document.getElementById('coaName').value=='')
		{
			alert('Please fill the account name');
			return;
		}
		PageManager.DataService.setQueryField('coaName',document.getElementById('coaName').value);
		PageManager.DataService.callDataService('coaSearch');
	}
	function load()
	{
		var temp='<%=request.getParameter("coaName")%>';
		if(temp=='null')
			document.getElementById('coaName').focus();
		else
		{
			document.getElementById('coaName').value=temp;
			getGrid();
		}
	}
	function afterRefreshPage(dc)
	{
		document.getElementById('coaSearchGrid').style.display = "block";
		if(dc.grids['coaSearchGrid'].length==1)
		{
			alert('Nothing found to display');
			document.getElementById('coaSearchGrid').style.display = "none";
			document.getElementById('coaName').focus();
		}
	}
	function getDetails(obj)
	{
		var row=PageManager.DataService.getRow(obj);
		var table=document.getElementById('coaSearchGrid');
		var accountCode=PageManager.DataService.getControlInBranch(table.rows[row.rowIndex],"accountCode");
		accountCode=accountCode.innerHTML;
		window.open("../DetailCodeEnquiry.htm?chartOfAccounts_glCode="+accountCode+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
	}
</SCRIPT>
</head>

<body onload="load()" bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0">
	<form name="coaSearchForm" >
	<table width="100%" border=0 cellpadding="3" cellspacing="0">
		<table align='center'  id="table3" class="tableStyle"> 
			<tr>
				<td class="tableHeader" valign="center" colspan="3"><span >COA Search</span></td>
			</tr>
			<tr height="20">
				<td/>
			</tr>
			<tr>
				<td align="right" valign="center" ><div class="labelcell">Account Name&nbsp;<span class="leadon">* &nbsp;</span></div></td>
				<td class="largeFieldcell" width="200">
					<input class="fieldinputlarge" name="coaName" id="coaName" size=150 exilMustEnter="true" tabindex=1>
				</td>
			</tr>
			<tr >
				<td colspan="4" align="middle">
					<table border="0" cellpadding="0" cellspacing="0" ID="Table1">
						<tr>
							<td align="right">
							<input type=button class=button onclick=getGrid() href="#" value="Search"  tabindex=1>
							</td>
							<td align="right">
							<input type=button class=button onclick=javascript:window.close() href="#" value="Close"  tabindex=1>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	
		<div id="codescontainer"></div>
		<table width="100%"  border="0" cellpadding="0" cellspacing="0" id=TABLE1>
			<tr >
				<td>
					<table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" name="coaSearchGrid" id="coaSearchGrid" style="DISPLAY: none">
						<tr class="tableheader">
							<td class="thStlyle"><div align="center" id="gridA">Account Name</div></td>
							<td class="thStlyle"><div align="center" id="gridB">Account Code</div></td>
							<td class="thStlyle"><div align="center" id="gridC">Type</div></td>
							<td class="thStlyle"><div align="center" id="gridD">Active For Posting</div></td>
							<td class="thStlyle"><div align="center" id="gridE">Parent Account</div></td>
							<td class="thStlyle"><div align="center" id="gridF">Purpose</div></td>
					  	</tr>
					 	<tr class="tdStlyle">
							<td><A onclick=getDetails(this) href="#"><div id="accountName" name="accountName">&nbsp;</div></A></td>
							<td><div id="accountCode" name="accountCode" align="center">&nbsp;</div></td>
							<td><div id="type" name="type" align="center">&nbsp;</div></td>
							<td><div id="activeforposting" name="activeforposting" align="center">&nbsp;</div></td>
							<td><div id="parentName" name="parentName">&nbsp;</div></td>
							<td><div id="purpose" name="purpose">&nbsp;</div></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</table>
	</form>
</body>
</html>