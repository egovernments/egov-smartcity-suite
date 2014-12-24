	function calculateFunds(obj) {
		//alert("hi");
		if (isNaN(obj.value)) {
			alert("Only Numbers allowed");
			obj.value="0.00";
			obj.focus();
			return;
		}else
		{
			obj.value=eval(obj.value).toFixed(2);
			if(obj.value.length>10)
		{
			alert("Max number of digits limited to 7 since amounts in LAKH's ");
			obj.value="0.00";
			obj.focus();
			return;
		}else
		{
		obj.value=eval(obj.value).toFixed(2);	
		}
		}
		var table = document.getElementById('receiptTable');
		
		for (i = 0; i < table.rows.length - 2; i++) {
			document.getElementById("receiptList[" + i + "].fundsAvailable").value = (eval(document
					.getElementById('receiptList[' + i + '].openingBalance').value)
					+ eval(document
							.getElementById('receiptList[' + i + '].currentReceipt').value)).toFixed(2);
		}
		calculateClosingBalance(obj);
	}
	function calculateClosingBalance(obj) {
		var table = document.getElementById('receiptTable');
		for (i = 0; i < table.rows.length - 2; i++) {
			document.getElementById('receiptList[' + i + '].closingBalance').value = (eval(document
					.getElementById('receiptList[' + i + '].fundsAvailable').value)
					- eval(document
							.getElementById('receiptList[' + i + '].btbPayment').value)).toFixed(2);
		}

		claulateColumnTotal();
	}
	function claulateColumnTotal() {
		var opTotal = 0;//opening balance total
		var crTotal = 0;//Current receipt total
		var faTotal = 0;
		var paymentTotal = 0;
		var cbTotal = 0;
		var table = document.getElementById('receiptTable');
		if(table!=null)
		{
		for (i = 0; i < table.rows.length - 2; i++) {
			opTotal = eval(opTotal)
					+ eval(document
							.getElementById('receiptList[' + i + '].openingBalance').value);
			crTotal = eval(crTotal)
					+ eval(document
							.getElementById('receiptList[' + i + '].currentReceipt').value);
			faTotal = eval(faTotal)
					+ eval(document
							.getElementById('receiptList[' + i + '].fundsAvailable').value);
			paymentTotal = eval(paymentTotal)
					+ eval(document
							.getElementById('receiptList[' + i + '].btbPayment').value);
			cbTotal = eval(cbTotal)
					+ eval(document
							.getElementById('receiptList[' + i + '].closingBalance').value);
		}
		document.getElementById('total[0].openingBalance').value = opTotal.toFixed(2);
		document.getElementById('total[0].currentReceipt').value = crTotal.toFixed(2);
		document.getElementById('total[0].fundsAvailable').value = faTotal.toFixed(2);
		document.getElementById('total[0].btbPayment').value = paymentTotal.toFixed(2);
		document.getElementById('total[0].closingBalance').value = cbTotal.toFixed(2);
		}
		calculateAplusB();
		
	}
	function calculateAplusB()
	{
	
	if(document.getElementById('total[0].openingBalance')!=null && document.getElementById('total[1].openingBalance')!=null)
	{
		document.getElementById('total[2].openingBalance').value = (eval(document.getElementById('total[0].openingBalance').value)+eval(document.getElementById('total[1].openingBalance').value)).toFixed(2);
		
		document.getElementById('total[2].closingBalance').value = (eval(document.getElementById('total[0].closingBalance').value)+
		eval(document.getElementById('total[1].closingBalance').value)).toFixed(2);
	}
	else if(document.getElementById('total[0].openingBalance')!=null )
	{
	//	document.getElementById('total[2].openingBalance').value = (eval(document.getElementById('total[0].openingBalance').value)).toFixed(2);
		
		//document.getElementById('total[2].closingBalance').value = (eval(document.getElementById('total[0].closingBalance').value)).toFixed(2);
	}
	else if( document.getElementById('total[1].openingBalance')!=null)
	{
		document.getElementById('total[2].openingBalance').value = (eval(document.getElementById('total[1].openingBalance').value)).toFixed(2);
		
		document.getElementById('total[2].closingBalance').value = (eval(document.getElementById('total[1].closingBalance').value)).toFixed(2);
	}
		
		
	}
	
	function calculateFundsForPayment(obj) {
		if (isNaN(obj.value)) {
			alert("Only Numbers allowed");
			obj.focus();
			return;
		}else
			{
			obj.value=eval(obj.value).toFixed(2);
			if(obj.value.length>10)
			
		{
			alert("Max number of digits limited to 7 since amounts in LAKH's ");
			obj.value="0.00";
			obj.focus();
			return;
		}else
		{
		obj.value=eval(obj.value).toFixed(2);  
		}
			}   
		
		var table = document.getElementById('paymentTable');
		for (i = 0; i < table.rows.length - 3; i++) {
			//alert("hiii");     
			document.getElementById("paymentList[" + i + "].fundsAvailable").value = 
				(eval(document.getElementById('paymentList[' + i + '].openingBalance').value)
					+ eval(document.getElementById('paymentList[' + i + '].currentReceipt').value)
					- eval(document.getElementById('paymentList[' + i + '].btbPayment').value)
					+ eval(document.getElementById('paymentList[' + i + '].btbReceipt').value)
					- eval(document.getElementById('paymentList[' + i + '].concurranceBPV').value)).toFixed(2);
					//alert(document.getElementById("paymentList[" + i + "].fundsAvailable").value );
		}
		calculateClosingBalanceForPayment(obj);
	}
	function calculateClosingBalanceForPayment(obj) {
		var table = document.getElementById('paymentTable');
		for (i = 0; i < table.rows.length - 3; i++) {
			document.getElementById('paymentList[' + i + '].closingBalance').value = (eval(document
					.getElementById('paymentList[' + i + '].fundsAvailable').value)
					- eval(document.getElementById('paymentList[' + i + '].outStandingBPV').value)).toFixed(2);
		}

		claulateColumnTotalForPayment();
	}
	function claulateColumnTotalForPayment() {
		var opTotal = 0;//opening balance total
		var crTotal = 0;//Current payment total
		var faTotal = 0;
		var paymentTotal = 0;
		var receiptTotal = 0;		
		var cbTotal = 0;
		var bpvTotal=0;
		var osTotal=0;
		var table = document.getElementById('paymentTable');
		if(table!=null)
		{
		for (i = 0; i < table.rows.length - 3; i++) {
			opTotal = eval(opTotal)
					+ eval(document
							.getElementById('paymentList[' + i + '].openingBalance').value);
			crTotal = eval(crTotal)
					+ eval(document
							.getElementById('paymentList[' + i + '].currentReceipt').value);
			faTotal = eval(faTotal)
					+ eval(document
							.getElementById('paymentList[' + i + '].fundsAvailable').value);
			
			paymentTotal = eval(paymentTotal)
			+ eval(document
					.getElementById('paymentList[' + i + '].btbPayment').value);
	
			
			receiptTotal = eval(receiptTotal)
					+ eval(document
							.getElementById('paymentList[' + i + '].btbReceipt').value);
			bpvTotal = eval(bpvTotal)
					+ eval(document
							.getElementById('paymentList[' + i + '].concurranceBPV').value);							
			osTotal = eval(osTotal)
			+ eval(document
					.getElementById('paymentList[' + i + '].outStandingBPV').value);
			cbTotal = eval(cbTotal)
					+ eval(document
							.getElementById('paymentList[' + i + '].closingBalance').value);
			
		}
		document.getElementById('total[1].openingBalance').value = opTotal.toFixed(2);
		document.getElementById('total[1].currentReceipt').value = crTotal.toFixed(2);
		document.getElementById('total[1].fundsAvailable').value = faTotal.toFixed(2);
		document.getElementById('total[1].btbPayment').value = paymentTotal.toFixed(2);
		document.getElementById('total[1].btbReceipt').value = receiptTotal.toFixed(2);
		document.getElementById('total[1].closingBalance').value = cbTotal.toFixed(2);
		document.getElementById('total[1].concurranceBPV').value = bpvTotal.toFixed(2);
		document.getElementById('total[1].outStandingBPV').value = osTotal.toFixed(2);
		}
		calculateAplusB();
	}