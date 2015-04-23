<html>
<head>
<title> Unit rent agreement details </title>
<link href="/ptis/css/propertytax.css" rel="stylesheet" type="text/css" />
<link href="/ptis/css/commonegov.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="/ptis/css/jquery/jquery-ui-1.8.4.custom.css"  />

<script src="../javascript/jquery/jquery-1.7.2.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/ptis/javascript/jquery/jquery-ui-1.8.22.custom.min.js" ></script>
<script type="text/javascript" src="/ptis/javascript/jquery/ajax-script.js"></script>

<script type="text/javascript" src="/ptis/javascript/validations.js"></script>
	
	<script type="text/javascript">
	
	jQuery(function() {
		if (window.opener.agreementPeriod != undefined || window.opener.agreementPeriod != null) {
			jQuery('#divAgreementPeriod').html(window.opener.agreementPeriod);
			jQuery('#divAgreementDate').html(window.opener.agreementDate);
			jQuery('#divIncrementInRent').html(window.opener.incrementInRent);
		}
	}) 
		
	</script>
	
	<style type="text/css">
		td.zeroBorder { border: 0px; }	
	</style>
</head>

<body>
	<div class="formmainbox">
		<div class="formheading"></div>
		<div class="headingbg">
			Unit Rent Agreement Details
		</div>
		<form>
			<br />
			<table width="100%" border="0" align="center" cellspacing="0"
				cellpadding="0">
				<tr>
					<td class="bluebox" width="25%">
					<td class="bluebox zeroBorder" width="25%">
							Agreement period : 
					</td>
					<td class="bluebox zeroBorder" width="25%">
						<div id="divAgreementPeriod" style="font-weight: bold" ></div>
					</td>
					<td class="bluebox" width="25%" />
				</tr>
				<tr>
					<td class="bluebox" width="25%">
					<td class="bluebox zeroBorder" width="25%">
							Agreement date : 
					</td>
					<td class="bluebox zeroBorder" width="25%">
						<div id="divAgreementDate" style="font-weight: bold"></div>
					</td>
					<td class="bluebox" width="25%">
				</tr>
				<tr>
					<td class="bluebox" width="25%">
					<td class="bluebox zeroBorder" width="25%">
							Increment in Rent (%) : 
					</td>
					<td class="bluebox zeroBorder" width="25%">
						<div id="divIncrementInRent" style="font-weight: bold"></div>
					</td>
					<td class="bluebox" width="25%">
				</tr>
			</table>

			<div class="buttonbottom" align="center">
				<input type="button" name="button2" id="button2" value="Close"
					class="button" onclick="return confirmClose();" />
			</div>
		</form>
	</div>
</body>
</html>