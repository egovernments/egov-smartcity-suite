<html>
<head>
<title> Unit rent agreement details </title>
<link href="/ptis/css/propertytax.css" rel="stylesheet" type="text/css" />
<link href="/ptis/css/commonegov.css" rel="stylesheet" type="text/css" />
<link href="/ptis/css/jquery/jquery-ui-1.8.4.custom.css" rel="stylesheet" type="text/css" />

<script src="../javascript/jquery/jquery-1.7.2.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/ptis/javascript/jquery/jquery-ui-1.8.22.custom.min.js" ></script>
<script type="text/javascript" src="/ptis/javascript/jquery/ajax-script.js"></script>

<script type="text/javascript" src="/ptis/javascript/validations.js"></script>
	
	<script type="text/javascript">
	
	jQuery(function() {

		jQuery('#txtAgreementDate').datepicker({
			dateFormat: "dd/mm/yy",
			changeMonth: true,
			changeYear: true,
			minDate: window.opener.floorEffectiveDate
		});

		jQuery('#chkboxAgreementSubmitted').attr('checked', true);

		if (window.opener.agreementPeriod != undefined || window.opener.agreementPeriod != null) {
			jQuery('#txtAgreementPeriod').val(window.opener.agreementPeriod);
			jQuery('#txtAgreementDate').val(window.opener.agreementDate);
			jQuery('#txtIncrementInRent').val(window.opener.incrementInRent);
		}
			
	}) 
		
	function setFormValues() {
		var period = jQuery('#txtAgreementPeriod').val();
		var date = jQuery('#txtAgreementDate').val();
		var increment = jQuery('#txtIncrementInRent').val();
		
		window.opener.setUnitRentAgreementDetails(period, date, increment);		
		window.close();	
	}
	
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
					<td class="bluebox " width="25%"/>
					<td class="bluebox zeroBorder" width="25%">
							Is Agreement submitted : <%-- <s:text name="isAgreementSubmitted" /> --%>
					</td>
					<td class="bluebox zeroBorder" width="25%">
						<input type="checkbox" id="chkboxAgreementSubmitted">
					</td>
					<td class="bluebox" width="25%" />
				</tr>
				<tr>
					<td class="bluebox" width="25%">
					<td class="bluebox zeroBorder" width="25%">
							Agreement period : 
					</td>
					<td class="bluebox zeroBorder" width="25%">
						<input type="text" id="txtAgreementPeriod"
							name="txtAgreementPeriod"
							onblur="checkfornumber(this, this.value); checkNumberRange(this, this.value, 1, 12);" />
					</td>
					<td class="bluebox" width="25%" />
				</tr>
				<tr>
					<td class="bluebox" width="25%">
					<td class="bluebox zeroBorder" width="25%">
							Agreement date : 
					</td>
					<td class="bluebox zeroBorder" width="25%">
						<input type="text" id="txtAgreementDate" name="txtAgreementDate" />
					</td>
					<td class="bluebox" width="25%">
				</tr>
				<tr>
					<td class="bluebox" width="25%">
					<td class="bluebox zeroBorder" width="25%">
							Increment in Rent (%) : 
					</td>
					<td class="bluebox zeroBorder" width="25%">
						<input type="text" id="txtIncrementInRent"
							name="txtIncrementInRent"
							onabort="checkfornumber(this, this.value); checkNumberRange(this, this.value, 1, 100);" />
					</td>
					<td class="bluebox" width="25%">
				</tr>
			</table>

			<div class="buttonbottom" align="center">
				<input type="button" name="buttonSave" id="buttonSave" value="Save"
					class="buttonsubmit" onclick="return setFormValues();" />
				<input type="reset" name="buttonReset" id="buttonReset" value="Cancel"
					class="button" />
				<input type="button" name="button2" id="button2" value="Close"
					class="button" onclick="return confirmClose();" />
			</div>
		</form>
	</div>
</body>
</html>