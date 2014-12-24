var locationbased = false;
var dataLength = new Array();

function buttonpress() {
	if (validate() == false) {
		return false;
	}
	document.forms[0].submit();
}

function validate() {
	if (document.getElementById("j_username").value == "") {
		alert("Please enter your User Name");
		return false;
	} else if (document.getElementById("j_password").value == "") {
		alert("Please enter your Password");
		return false;
	}

	if (locationbased) {
		if (document.getElementById("locationname").value == "") {
			alert("Please select the Location");
			return false;
		} else if (document.getElementById("counterId").value == "") {
			alert("Please select the Counter");
			return false;
		}
	}
}

function bodyonload() {
	document.getElementById("j_username").setAttribute("autocomplete", "off");
	document.getElementById("j_password").setAttribute("autocomplete", "off");
	document.getElementById("UserValidateForm").setAttribute("autocomplete","off");
	document.getElementById("j_username").focus();
}

function checkRole() {
	var username = document.getElementById("j_username").value;
	locationbased = false;
	if (username !== "") {
		var link = "../login/TerminalAjax.jsp?username=" + username;
		var request = initiateRequest();
		request.open("GET", link, false);

		if (!document.all) {
			request.send(null);
			if (request.status) {
				showLocationData(eval("(" + request.responseText + ")"));
			}
			return;
		}
		request.onreadystatechange = function() {
			if (request.readyState == 4) {
				if (request.status == 200) {
					showLocationData(eval("(" + request.responseText + ")"));
				}
			}
		};
		request.send(null);
	}
}

function showLocationData(locationData) {
	if (locationData.isLocation) {
		locationbased = true;
		document.getElementById('loginType').value = "Location";
		document.getElementById('locationRow').style.display = "table-row";
		document.getElementById('counterRow').style.display = "table-row";
		document.getElementById('locationname').value = locationData.locationName;
		document.getElementById('locationId').value = locationData.locationId;
		loadTerminalData(locationData.counters);
	} else {
		document.getElementById('loginType').value = "";
		document.getElementById('locationname').value = "";
		document.getElementById('locationId').value = "";
		document.getElementById('locationRow').style.display = "none";
		document.getElementById('counterRow').style.display = "none";
		document.getElementById("counterId").length = 0;
		
	}
}

function loadTerminalData(counter) {
	var terminalObj = document.getElementById("counterId");
	terminalObj.length = 0;
	terminalObj.options[0] = new Option('--- Choose ---', '');
	for ( var i = 0; i < counter.length; i++) {
		terminalObj.options[i + 1] = new Option(counter[i].name, counter[i].id);
	}

}

function checkEnterKey(e) {
	if (e.keyCode == 13 && !e.shiftKey && !e.ctrlKey && !e.altKey) {
		buttonpress();
	}
}

function closeWindow() {
	var x = window.close();
	window.setTimeout('if (!window.closed) {alert("Please use Browser close button!");}',10);

}
function checkCapsLock(e) {
	var kc = e.keyCode ? e.keyCode : e.which;
	var sk = e.shiftKey ? e.shiftKey : ((kc == 16) ? true : false);
	if (((kc >= 65 && kc <= 90) && !sk) || ((kc >= 97 && kc <= 122) && sk)) {
		document.getElementById('tooltip').style.display = 'block';
	} else {
		document.getElementById('tooltip').style.display = 'none';
	}

}

function initiateRequest() {
	if (window.XMLHttpRequest) {
		var req = new XMLHttpRequest();
		if (req.overrideMimeType) {
			req.overrideMimeType("text/html;charset=utf-8");
		}
		return req;
	} else {
		if (window.ActiveXObject) {
			isIE = true;
			return new ActiveXObject("Microsoft.XMLHTTP");
		}
	}
}