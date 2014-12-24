function loadTableTdClass() {
	var mainTbl = document.getElementById("maintbl");
	var rows = mainTbl.rows;
	var className = "";
	for (var i=0;i<rows.length;i++) {
		if(i%2 != 0 ) {
			className = "bluebox";
		} else {
			className = "greybox";
		}				
		var cells = rows[i].cells;
		for (var j=0;j<cells.length;j++) {
			if (cells[j].className =="headingwk") {
				break;
			} else {
				cells[j].className = className;
			}
		}
	}
}

function  enableRentPaid(obj) {
	if(obj.value=="Rental") {
		document.getElementById("rentpaid").disabled=false;
	} else {
		document.getElementById("rentpaid").value="";
		document.getElementById("rentpaid").disabled=true;
	}

}

function  enableRentPaidForEdit(obj) {
	if(obj.value=="Rental") {
		document.getElementById("rentpaid").disabled=false;
	} else {
		document.getElementById("rentpaid").value="";
		document.getElementById("rentpaid").disabled=true;
	}

}