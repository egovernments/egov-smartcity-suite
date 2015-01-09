<%@ include file="/includes/taglibs.jsp"%>
<%@ page import="java.util.*,org.egov.infstr.utils.*,org.egov.lib.security.terminal.client.LocationForm" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%!
ArrayList locationList,locationIPList;
%>
<%

locationList=(ArrayList) request.getAttribute("locationList");
locationIPList=(ArrayList) request.getAttribute("locationIPList");
 %>

<c:set var="locationList" value="<%=locationList%>" scope="page" />
<c:set var="locationIPList" value="<%=locationIPList%>" scope="page" />

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <link rel="stylesheet" type="text/css" href="../css/egov.css">
	 <LINK rel="stylesheet" type="text/css" href="../css/ccMenu.css">
        <title>Location / Terminal</title>
    </head>
    <% LocationForm locationform=(LocationForm)request.getAttribute("locationform");%>
    <SCRIPT type="text/javascript" src="../commonjs/ajaxCommonFunctions.js"></Script>


    <script type="text/javascript">

	/*
	 * Body Onload function
	 */

    	var submitType = "<%=(String) request.getAttribute("buttonType")%>";
    	var loginType = "<%=(String) request.getAttribute("loginType")%>";
    	var delLength = new Array();
    	function bodyonload()
    	{
   		if(submitType == "loadcreatedata")
    		{
			document.title="Location/Terminal - Create";
			document.getElementById('screenName').innerHTML="Create Location/Terminal";
			document.getElementById('iploc1').style.visibility="hidden";
			document.getElementById('ipDel').style.visibility = "hidden";
			document.getElementById('countertable').style.visibility = "hidden";
			document.getElementById('counterAddlabel').style.visibility = "hidden";
		}
    		else if(submitType == "loadmodifydata")
    		{
			document.title="Location/Terminal - Modify";
			document.getElementById('screenName').innerHTML="Modify Location/Terminal";
			<%if(locationform!=null && "1".equals(locationform.getIsActive()))
			{%>
				document.getElementById("isActiveValue").checked=true;
			<%}%>
			document.getElementById('counterlabel').style.visibility = "visible";
			if(loginType == "location")
			{
				document.getElementById('iploc').style.visibility = "visible";
				document.getElementById("isLocation").checked = true;
				document.getElementById('ip').style.display = 'none';
				document.getElementById('addDel').style.visibility = "visible";


			}
			else if(loginType == "terminal")
			{
				document.getElementById("isTerminal").checked = true;

			}
			document.getElementById("isLocation").disabled = true;
			document.getElementById("isTerminal").disabled = true;
			document.getElementById("lochidden").value = document.getElementById("locationname").value ;
		}
		else if(submitType == "loadviewdata")
		{
			document.title="Location/Terminal - View";
			document.getElementById('screenName').innerHTML="View Location/Terminal";
			<%if(locationform!=null && "1".equals(locationform.getIsActive()))
			{%>

				document.getElementById("isActiveValue").checked=true;
			<%}%>
			document.getElementById('counterlabel').style.visibility = "visible";
			if(loginType == "location")
			{
				document.getElementById('iploc').style.visibility = "visible";
				document.getElementById('ip').style.display = 'none';
				document.getElementById('addDel').style.visibility = "visible";
				document.getElementById("isLocation").checked = true;

			}
			else if(loginType == "terminal")
			{
				document.getElementById("isTerminal").checked = true;

			}
			document.getElementById("isLocation").disabled = true;
			document.getElementById("isTerminal").disabled = true;
			document.getElementById("lochidden").value = document.getElementById("locationname").value ;
			for(var i=0;i<document.forms[0].length;i++)
			{
					document.forms[0].elements[i].disabled =true;
			}


		}
        }

	/*
	 * On submit buttonpress function is called
	 */

   	function buttonpress()
        {

		if(document.getElementById("locationname").value == "")
		{
			alert("Select the Location Name");
			return false;
		}
		else if(document.getElementById("isLocation").checked == false && document.getElementById("isTerminal").checked == false)
		{
			alert("Select either Location or Terminal");
			return false;
		}
		else if(document.getElementById("isLocation").checked && document.getElementById("ipaddress").value == "")
		{
			alert("Please enter the IP Address");
			return false;
		}
		if(document.getElementById("isActiveValue").checked)
		{

			document.getElementById("isActive").value =1;
		}
		else
		{

			document.getElementById("isActive").value =0;

		}

		if(document.getElementById("isLocation").checked && validateIPAddress( document.getElementById("ipaddress")) == false)
		{
			return false;
		}

		if(validateCounterTable() == false)
		{
			return false;
		}

		if(document.getElementById("isLocation").checked)
		{
			document.forms[0].loginType.value="Location";
			if(submitType == "loadmodifydata")
			{
				var len = 	delLength.length
				//alert("len >>> " + len);
				for(var i=0;i<len;i++)
				{
					if(delLength[i]!=null && delLength[i]!="" && document.forms[0].deleteIPSet.length>0)
					{
						//alert("delLength[i] >>> " + delLength[i]);
						document.forms[0].deleteIPSet[i].value=delLength[i];


					}
					else if(delLength[i]!=null && delLength[i]!="")
					{
						//alert("last >>> " + delLength[i]);
						document.forms[0].deleteIPSet.value=delLength[i];

					}
				}
			}
			var tbl= document.getElementById('multipleIP_Table');
			var rCount=tbl.rows.length-1;
			for(var i=0;i<=rCount;i++)
			{
				if(getControlInBranch(tbl.rows[i],'ipaddress').value == "")
				{
					alert("Enter the IP Address for row  "+ eval(i+1));
					return false;
				}
				for(var k=0;k<=rCount;k++)
				{
				if(i != k && getControlInBranch(tbl.rows[i],'ipaddress').value != ""
					&& getControlInBranch(tbl.rows[k],'ipaddress').value != ""
					&& (getControlInBranch(tbl.rows[i],'ipaddress').value == getControlInBranch(tbl.rows[k],'ipaddress').value) )
					{
						alert(" Same IpAddress exists in more than one row ");
						getControlInBranch(tbl.rows[i],'ipaddress').focus();
						return false;
					}
				}

			}
		}
		if(document.getElementById("isLocation").checked)
		{
			document.forms[0].loginType.value="Location";
		}
		else
		{
			document.forms[0].loginType.value="Terminal";
		}

		if(submitType == "loadcreatedata")
		{
			document.forms[0].forward.value="saveorupdate";
			document.forms[0].action = "../terminal/location.do?submitType=createLocation";
			document.forms[0].submit();
		}
		else if(submitType == "loadmodifydata")
		{
			document.forms[0].forward.value="saveorupdate";
			document.forms[0].action = "../terminal/location.do?submitType=updateLocation";
			document.forms[0].submit();
		}
        }



	function validateLocation()
	{
		if(submitType == "loadcreatedata")
		{
				uniqueChecking('${pageContext.request.contextPath}/commonyui/egov/uniqueCheckAjax.jsp', 'EG_LOCATION','NAME', 'locationname', 'yes', 'no');
				
		}
    		else if(submitType == "loadmodifydata")
    		{
			<c:forEach var="obj" items="${locationList}">
			    var name = "${obj.name}";
			    if(name == (document.getElementById("locationname").value).toUpperCase())
			    {
			    	if(name != (document.getElementById("lochidden").value).toUpperCase())
			    	{
				alert("Entered Location already exists");
				document.getElementById("locationname").value = "";
				document.getElementById("locationname").focus();
				return false;
				}
			    }
			</c:forEach>
    		}
    		validateTextValueContainsAlpha();
	}
	 function validateTextValueContainsAlpha()
	{
			var iChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
			if (iChars.indexOf(document.getElementById("locationname").value.charAt(0)) == -1) {
			alert ("Please enter the first character as alphabet");
			document.getElementById("locationname").focus();
			return false;
			}
			var iSpecialChars = "_.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
			for (var i = 0; i < document.getElementById("locationname").value.length; i++) {

			       if (iSpecialChars.indexOf(document.getElementById("locationname").value.charAt(i)) == -1) {
				alert ("Only underscore and dot sysmbols are allowed");
				document.getElementById("locationname").focus();
				return false;	
			     }
			
			}
			
	   		
	   return true;
        }

      

        function isNumeric(passedVal)
        {

		var validChars = "0123456789.";
		var isNumber=true;
		var Char;
		if(passedVal == "")
		{
			return false;
		}
		for (var i = 0; i < passedVal.length && isNumber == true; i++)
		{
			Char = passedVal.charAt(i);
			if (validChars.indexOf(Char) == -1)
			{
			isNumber = false;
			}
		}
		return isNumber;

        }

        function showLocFields(obj)
        {
        	        if(document.getElementById("isLocation").checked)
        	        {
        	        var rowObj=getRow(obj);
			var table = document.getElementById('countertable');
			document.getElementById('counterlabel').style.visibility = "visible";
			document.getElementById('countertable').style.visibility = "visible";
			var len = table.rows.length;
			deleteAllRow('countertable');
			deleteAllRow('multipleIP_Table');
			document.getElementById('ipaddress').value="";
			document.getElementById('counter').value="";
			document.getElementById('description').value="";
			document.getElementById('counterId').value="";
			document.getElementById('locationname').value="";
			document.getElementById('locationdesc').value="";
			document.getElementById('isActiveValue').checked=false;
			if(len>1)
			{
				for(var i=1;i<len;i++)
				{
					if(document.getElementById("isLocation").checked)
					{
						document.getElementById("isTerminal").checked = false;
						document.getElementById('iploc').style.visibility = "visible";
						document.getElementById('ip').style.display = 'none';
						document.getElementById('ipText').style.display = 'none';
						document.getElementById('iploc1').style.visibility = "visible";
						document.getElementById('addDel').style.visibility = "visible";
						document.getElementById('ipDel').style.visibility = "visible";
						document.getElementById('counterAddlabel').style.visibility = "visible";

					}


				}
			}
			else
			{
				document.getElementById("isTerminal").checked = false;
				document.getElementById('iploc').style.visibility = "visible";
				document.getElementById('iploc1').style.visibility = "visible";
				document.getElementById('ip').style.display = 'none';
				document.getElementById('ipText').style.display = 'none';
				document.getElementById('addDel').style.visibility = "visible";
				document.getElementById('ipDel').style.visibility = "visible";
				document.getElementById('counterAddlabel').style.visibility = "visible";
			}
			}
			else
			{
				
				document.getElementById('iploc1').style.visibility="hidden";
				document.getElementById('iploc').style.visibility = "hidden";
				document.getElementById('ipDel').style.visibility = "hidden";
				document.getElementById('countertable').style.visibility = "hidden";
				document.getElementById('counterAddlabel').style.visibility = "hidden";
				document.getElementById('ip').style.display = 'block';
				document.getElementById('ipText').style.display = 'block';
				document.getElementById('addDel').style.visibility = "hidden";
				document.getElementById('ipDel').style.visibility = "hidden";
			
			}
		 }
        function getRow(obj)
		{
			if(!obj)return null;
			tag = obj.nodeName.toUpperCase();
			while(tag != 'BODY'){
				if (tag == 'TR') return obj;
				obj=obj.parentNode;
				tag = obj.nodeName.toUpperCase();
			}
			return null;
		}
		function getControlInBranch(obj,controlName)
		{
			if (!obj || !(obj.getAttribute)) return null;
			// check if the object itself has the name
			if (obj.getAttribute('name') == controlName) return obj;

			// try its children
			var children = obj.childNodes;
			var child;
			if (children && children.length > 0){
				for(var i=0; i<children.length; i++){
					child=this.getControlInBranch(children[i],controlName);
					if(child) return child;
				}
			}
			return null;
}


function showTerFields(obj)
{
	if(document.getElementById("isTerminal").checked)
       {
	var rowObj=getRow(obj);
	var table = document.getElementById('countertable');
	var len = table.rows.length;
	document.getElementById('counterlabel').style.visibility = "visible";
	document.getElementById('countertable').style.visibility = "visible";
	deleteAllRow('multipleIP_Table');
	deleteAllRow('countertable');
	document.getElementById('counter').value="";
	document.getElementById('description').value="";
	document.getElementById('ipaddr').value="";
	document.getElementById('counterId').value="";
	document.getElementById('locationname').value="";
	document.getElementById('locationdesc').value="";
	document.getElementById('isActiveValue').checked=false;

	if(len>1)
	{
		for(var i=1;i<len;i++)
		{
			if(document.getElementById("isTerminal").checked)
			{
				document.getElementById("isLocation").checked = false;
				document.getElementById('iploc').style.visibility = "hidden";
				document.getElementById('iploc1').style.visibility = "hidden";
				document.getElementById('ipText').style.display='block';
				document.getElementById("ip").style.display = 'block';
				document.getElementById('addDel').style.visibility = "hidden";
				document.getElementById('ipDel').style.visibility = "hidden";
				document.getElementById('counterAddlabel').style.visibility = "visible";

			}


	   	}
	}
	else
	{
		document.getElementById("isLocation").checked = false;
		document.getElementById('iploc').style.visibility = "hidden";
		document.getElementById('iploc1').style.visibility = "hidden";
		document.getElementById('ipText').style.display='block';
		document.getElementById("ip").style.display = 'block';
		document.getElementById('addDel').style.visibility = "hidden";
		document.getElementById('ipDel').style.visibility = "hidden";
		document.getElementById('counterAddlabel').style.visibility = "hidden";
	}
	}else
	{
	
		document.getElementById('iploc').style.visibility = "hidden";
		document.getElementById('iploc1').style.visibility = "hidden";
		document.getElementById('ipText').style.display='block';
		document.getElementById("ip").style.display = 'block';
		document.getElementById('addDel').style.visibility = "hidden";
		document.getElementById('ipDel').style.visibility = "hidden";
		document.getElementById('countertable').style.visibility = "hidden";
		document.getElementById('counterAddlabel').style.visibility = "hidden";
	
	}


}

	function addRow(arg)
	{
		if(arg=="countertable")
		{
			if(validateCounterTable() == false)
			{
				return false;
			}

				 var tableObj = document.getElementById("countertable");
				 var tbody=tableObj.tBodies[0];
				 var lastRow = tableObj.rows.length;
				 var rowObj = tableObj.rows[1].cloneNode(true);
				 tbody.appendChild(rowObj);
				 document.getElementsByName("selectTd")[tableObj.rows.length - 2].className='normaltext';
				 document.forms[0].counterId[lastRow-1].value="";
				 document.forms[0].counter[lastRow-1].value="";
				 document.forms[0].description[lastRow-1].value="";
				 if(document.getElementById("isTerminal").checked){
					document.forms[0].ipaddr[lastRow-1].value="";
				 document.forms[0].ipaddrhidden[lastRow-1].value="";
				  document.forms[0].locIPMapID[lastRow-1].value="";
				 }
				 document.forms[0].counterhidden[lastRow-1].value="";



		}
		else if(arg=="multipleIP_Table")
		{
			if(document.getElementById("isLocation").checked)
			{
				var tbl= document.getElementById('multipleIP_Table');
				var rCount=tbl.rows.length-1;
				for(var i=0;i<=rCount;i++)
				{
					if(getControlInBranch(tbl.rows[i],'ipaddress').value == "")
					{
						alert("Enter the IP Address for row  "+ eval(i+1));
						return false;
					}
					for(var k=0;k<=rCount;k++)
					{
					if(i != k && getControlInBranch(tbl.rows[i],'ipaddress').value != ""
						&& getControlInBranch(tbl.rows[k],'ipaddress').value != ""
						&& (getControlInBranch(tbl.rows[i],'ipaddress').value == getControlInBranch(tbl.rows[k],'ipaddress').value) )
						{
							alert(" Same IpAddress exists in more than one row ");
							getControlInBranch(tbl.rows[i],'ipaddress').focus();
							return false;
						}
					}

				}
			}
			var tableObj = document.getElementById('multipleIP_Table');
			var tbody=tableObj.tBodies[0];
			var lastRow = tableObj.rows.length;
			var rowObj = document.getElementById('multipleIP').cloneNode(true);
			tbody.appendChild(rowObj);
			document.forms[0].ipaddress[lastRow].value="";
			document.forms[0].iphidden[lastRow].value="";
			document.forms[0].locIPMapID[lastRow].value="";

		}

	}
	function deleteAllRow(arg)
	{
		if(arg=="multipleIP_Table")
		{
			var firsttable=document.getElementById("multipleIP_Table");
			var tablelength = firsttable.rows.length;
			while(tablelength > 1 )
			{
				firsttable.deleteRow(tablelength-1);
				tablelength = firsttable.rows.length;
			}
		}
		else if(arg=="countertable")
		{
			var firsttable=document.getElementById("countertable");
			var tablelength = firsttable.rows.length;
			while(tablelength > 2 )
			{
				firsttable.deleteRow(tablelength-1);
				tablelength = firsttable.rows.length;
			}

		}

	}
	function deleteRow(obj)
	{

		var tbl=document.getElementById('countertable');
		var rowNumber=getRow(obj).rowIndex;
		var rowo=tbl.rows.length;

		if(rowo<=2)
		{
			alert("This Row can not be deleted,Atleast One Row Should be there");
			return false;
		}
		if(rowo>2)
		{
			for(var i=1;i<tbl.rows.length;i++)
			{
				if(tbl.rows[i].cells[0].className == "rowRev")
				{
					tbl.deleteRow(i);
				}
			}
			return true;
		}

	}



	function checkUnique(url,tablename,columnname,fieldobj,uppercase,lowercase)
	{
		var fieldvalue = fieldobj.value;
		if(url != "" && tablename != "" && columnname != "" && fieldvalue != "" && uppercase != "" && lowercase != "")
		{
		fieldvalue = trimText(fieldvalue);
		var link = ""+url+"?tablename=" + tablename+"&columnname=" + columnname+ "&fieldvalue=" + fieldvalue+ "&uppercase=" + uppercase+ "&lowercase=" + lowercase+ " ";
		var request = initiateRequest();
		var isUnique=true;
		request.onreadystatechange = function()
		{
			if (request.readyState == 4)
			{
			if (request.status == 200)
			{
				var response=request.responseText.split("^");
				if(response[0] == "false")
				{
					alert("Entered "+columnname+" already exists. ");
					fieldobj.value="";
					fieldobj.focus();
					isUnique=false;
				}
			}
			}
		};
		return isUnique
		request.open("GET", link, true);
		request.send(null);
		}
	}

	function changeColor(currObj,obj)
	{

		var table=document.getElementById(obj);
		for(var i=1;i<table.rows.length;i++)
		{
			table.rows[i].cells[0].className='normaltext';
		}
		currObj.className='rowRev';
	}

	function isCheckBoxClicked()
	{
		if(document.getElementById("isLocation").checked == false && document.getElementById("isTerminal").checked == false)
		{
			alert("Select either Location or Terminal");
			document.getElementById("locationname").value = "";
			return false;
		}
	}

	function callLocationCentral()
	{
		window.location = "LocationCentral.jsp";

	}
	var i=0;
	function deleteRow1(obj)
	{
	   var tbl=document.getElementById('multipleIP_Table');
	   var rowNumber=getRow(obj).rowIndex;
	   var rowo=tbl.rows.length;

		if(rowo<=1)
		{
			alert("Last Row can not be deleted");
			return false;
		}
		else
		{

		  if(submitType == "loadmodifydata"){
			delLength[i] = getControlInBranch(tbl.rows[rowNumber],'locIPMapID').value;
			i++;
			tbl.deleteRow(rowNumber)
			return true;
		   }
		   else if(submitType == "loadcreatedata")
		   {
			   tbl.deleteRow(rowNumber)
				return true;

		   }

		}
    }



    function trim(obj,value)
	{
		value = value;
		if(value!=undefined)
	   {
		   while (value.charAt(value.length-1) == " ")
		   {
			value = value.substring(0,value.length-1);
		   }
		   while(value.substring(0,1) ==" ")
		   {
			value = value.substring(1,value.length);
		   }
		   obj.value = value;
		}
	   return value ;
	}
	function ipValidate()
	{

			var tbl= document.getElementById('multipleIP_Table');
			var rCount=tbl.rows.length-1;
			for(var i=0;i<=rCount;i++)
			{
				validateIPAddress(getControlInBranch(tbl.rows[i],'ipaddress'));
				if(submitType == "loadcreatedata"){
					var value = getControlInBranch(tbl.rows[i],'ipaddress').value;
					<c:forEach var="obj" items="${locationIPList}">
					var ip = "${obj.ipAddress}";
					if(ip == value)
					{

						alert("Entered IP Address already exists");
						getControlInBranch(tbl.rows[i],'ipaddress').value = "";
						getControlInBranch(tbl.rows[i],'ipaddress').focus();
						return false;
					}
					</c:forEach>
				}
					else if(submitType == "loadmodifydata" && document.getElementById("isLocation").checked)
				{
					var hidvalue = getControlInBranch(tbl.rows[i],'iphidden').value;
					var value = getControlInBranch(tbl.rows[i],'ipaddress').value;
					<c:forEach var="obj" items="${locationIPList}">
						var ip = "${obj.ipAddress}";
						if(ip == value)
						{
						if(ip != hidvalue)
						{
						alert("Entered IP Address already exists");
						getControlInBranch(tbl.rows[i],'ipaddress').value = "";
						getControlInBranch(tbl.rows[i],'ipaddress').focus();
						return false;
						}
						}
					</c:forEach>
				}
			}

		}

	 function validateCounterTable()
	{
		var tbl= document.getElementById('countertable');
		var rCount=tbl.rows.length-1;

		for(var i=1;i<=rCount;i++)
		{
			if(getControlInBranch(tbl.rows[i],'counter').value == "")
			{
				alert("Enter counter for row "+i);
				getControlInBranch(tbl.rows[i],'counter').focus();
				return false;
			}
			var k;
						
			if(getControlInBranch(tbl.rows[i],'counter').value == document.getElementById('locationname').value)
			{
					alert(" Location name and counter name cannot be same ");
					getControlInBranch(tbl.rows[i],'counter').focus();
					return false;
			}
			for( k=1;k<=rCount;k++)
			{
				if(i != k && getControlInBranch(tbl.rows[i],'counter').value != ""
				&& getControlInBranch(tbl.rows[k],'counter').value != ""
				&& (getControlInBranch(tbl.rows[i],'counter').value == getControlInBranch(tbl.rows[k],'counter').value) )
				{
					alert(" Same Counter exists in more than one row ");
					getControlInBranch(tbl.rows[i],'counter').focus();
					return false;
				}
			}


			var iChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
			if (iChars.indexOf(getControlInBranch(tbl.rows[i],'counter').value.charAt(0)) == -1) {
			alert ("Please enter the first character as alphabet");
			getControlInBranch(tbl.rows[i],'counter').focus();
			return false;
			}
			var iSpecialChars = "_.ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
			var len =getControlInBranch(tbl.rows[i],'counter').value.length
			for (var j = 0; j < len; j++) {

			       if (iSpecialChars.indexOf(getControlInBranch(tbl.rows[i],'counter').value.charAt(j)) == -1) {
				alert ("Only underscore and dot sysmbols are allowed");
				getControlInBranch(tbl.rows[i],'counter').focus();
				return false;	
			     }

			}

			if(submitType == "loadcreatedata"){

				var value = getControlInBranch(tbl.rows[i],'counter').value;

				<c:forEach var="obj" items="${locationList}">
					var name = "${obj.name}";
					if(name == value.toUpperCase())
					{
						alert("Entered Counter already exists");
						getControlInBranch(tbl.rows[i],'counter').value = "";
						getControlInBranch(tbl.rows[i],'counter').focus();
						return false;
					}
				</c:forEach>
			}
			else if(submitType == "loadmodifydata")
			{
				var hidvalue = getControlInBranch(tbl.rows[i],'counterhidden').value;
				var value = getControlInBranch(tbl.rows[i],'counter').value;

				<c:forEach var="obj" items="${locationList}">
					var name = "${obj.name}";
					if(name == value.toUpperCase())
					{
					if(name != hidvalue.toUpperCase())
					{
					alert("Entered Counter already exists");
					getControlInBranch(tbl.rows[i],'counter').value = "";
					getControlInBranch(tbl.rows[i],'counter').focus();
					return false;
					}
					}
				</c:forEach>
			}
			

		}

		 if(document.getElementById("isTerminal").checked)
		{
			var j,k;
			
			for(j=1;j<=rCount;j++)
			{
				
				if(getControlInBranch(tbl.rows[j],'ipaddr').value == "")
				{
					alert("Enter ipaddress for row "+i);
					getControlInBranch(tbl.rows[j],'ipaddr').focus();
					return false;
				}
				
				
				for( k=1;k<=rCount;k++)
				{
				if(j != k && getControlInBranch(tbl.rows[j],'ipaddr').value != ""
					&& getControlInBranch(tbl.rows[k],'ipaddr').value != ""
					&& (getControlInBranch(tbl.rows[j],'ipaddr').value == getControlInBranch(tbl.rows[k],'ipaddr').value))
					{
						alert(" Same IP address exists in more than one row ");
						getControlInBranch(tbl.rows[j],'ipaddr').focus();
						return false;
					}
				}
			}
			for(var i=1;i<=rCount;i++)
			{
				validateIPAddress(getControlInBranch(tbl.rows[i],'ipaddr'));
				if(submitType == "loadcreatedata"){
					var value = getControlInBranch(tbl.rows[i],'ipaddr').value;
					<c:forEach var="obj" items="${locationIPList}">
						var ip = "${obj.ipAddress}";
						if(ip == value)
						{

						alert("Entered IP Address already exists");
						getControlInBranch(tbl.rows[i],'ipaddr').value = "";
						getControlInBranch(tbl.rows[i],'ipaddr').focus();
						return false;

						}
					</c:forEach>
			    }
					else if(submitType == "loadmodifydata" && document.getElementById("isTerminal").checked)
				{
					var hidvalue = getControlInBranch(tbl.rows[i],'ipaddrhidden').value;
					var value = getControlInBranch(tbl.rows[i],'ipaddr').value;
					<c:forEach var="obj" items="${locationIPList}">
						var ip = "${obj.ipAddress}";
						if(ip == value)
						{
						if(ip != hidvalue)
						{
						alert("Entered IP Address already exists");
						getControlInBranch(tbl.rows[i],'ipaddr').value = "";
						getControlInBranch(tbl.rows[i],'ipaddr').focus();
						return false;
						}
						}
					</c:forEach>
				}
			}
		}
		
		return true;
	}

    </script>

   <body  onload="bodyonload()">


    <BR>

          <html:form action="/terminal/location" method="post">

          <table align="center" id="mainTable" name="mainTable" class="tableStyle" width="800">
            <tr>
                <td  class="tableheader" colspan="6" align="center" height="24"><span id="screenName"> <bean:message key="Location" /> <span></td>
            </tr>

	    <tr><td colspan=6>&nbsp;</td></tr>

            <tr>
            	<td class="labelcellforsingletd" align="center" width="35%"><bean:message key="Location" /><input type="checkbox"  id="isLocation" name="isLocation"  onclick="showLocFields(this)"/></td>
               <td class="labelcellforsingletd" align="left" width="35%"><bean:message key="terminal" /><input type="checkbox"  id="isTerminal" name="isTerminal"  onclick="showTerFields(this)"/></td>
            	  </tr>

	 <tr>
	 <td>
	</table>
	<table>

	<tr><td colspan=4>&nbsp;</td></tr>
    <tr>
        <td class="labelcell" align="right" width="35%"><bean:message key="LocationName" /><span class="leadon">*</span></td>
		<td class="labelcell" align="left" width="35%">&nbsp;&nbsp;&nbsp;&nbsp;<html:text styleId="locationname" property="name"  style=";text-align:left" onblur="isCheckBoxClicked();validateLocation();trim(this,this.value);"/></td>
		<td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
             </tr>


            <tr>
                <td class="labelcell" align="right" width="35%"><bean:message key="active" /></td>
			<td class="labelcell" align="center" width="35%">	<input type="checkbox" name="isActiveValue" id="isActiveValue" value="ON" >
		<input type="hidden" name="isActive" id="isActive"></td>
             </tr>

            <tr>
                <td class="labelcell" align="right" width="35%"><bean:message key="LocationDesc" /></td>
		<td class="labelcell" align="left" width="35%">&nbsp;&nbsp;&nbsp;&nbsp;<html:text styleId="locationdesc" property="desc"  style=";text-align:left" onblur="trim(this,this.value);"/></td>
             </tr>
			<tr>

				<td class="labelcell" valign="right" width="35%"><p align="right"><span style="visibility:hidden" id="iploc">&nbsp;<bean:message key="ipadress" /><span class="leadon">*</span></span></td>
				<td>
				 <table id ="multipleIP_Table">
		<%

		/*
		 *  Create mode
		 */

		if(locationform == null)
		{%>

				<tr id="multipleIP">
			<td align="left" width="35%"><span id="iploc1" class="labelcell" >&nbsp;&nbsp;&nbsp;
			<html:text  property="ipaddress" styleId="ipaddress"  onblur="trim(this,this.value);ipValidate()"/></span>
			<input type="hidden" id="iphidden" name="iphidden" /><input type="hidden" id="locIPMapID" name="locIPMapID"/>
			</td>
			<td ><span  id="ipDel" ><input type="button"  class="button" value="Delete Row"   name="delbuttonIP"  onclick=" deleteRow1(this);"/></span></td>
			</tr>
		<%}
			/*
			 *  Modify mode
			 */

			else
			{
				if(locationform.getIpaddress()!=null && locationform.getIpaddress().length!=0){
				System.out.println("location ---------------"+locationform.getIpaddress().length);
					for(int k=0;k<locationform.getIpaddress().length;k++)
				{
					if(locationform.getIpaddress()[k] != null)
					{System.out.println("location ---------------"+locationform.getLocIPMapID()[k]);%>
					<tr id="multipleIP">
								<td align="left" width="35%"><span  id="iploc1" class="fieldcell" ><html:text styleId="ipaddress" property="ipaddress"  value="<%=locationform.getIpaddress()[k]%>"  onblur="trim(this,this.value);ipValidate();"/></span>
								<input type="hidden" id="iphidden" name="iphidden" value="<%=locationform.getIpaddress()[k]%>"  onblur="trim(this,this.value);"/>

								<input type="hidden" id="locIPMapID" name="locIPMapID" value="<%=locationform.getLocIPMapID()[k]%>"  onblur="trim(this,this.value);"/>
							</td>
								<td ><span  id="ipDel" ><input type="button"  class="button" value="Delete Row"  name="delbuttonIP"  onclick=" deleteRow1(this);"/></span>
								</td>
						</tr>

					<%}%>

					<input type="hidden" id="deleteIPSet" name="deleteIPSet">
				<%}

			}

			}
		%>

				</table>
			</td></tr>
		 <tr>
		 <td>&nbsp;</td>
		 	 <td ><span style="visibility:hidden" id="addDel" ><input type="button"  class="button" value="Add Row"   name="addbuttonIP" onclick=" addRow('multipleIP_Table');"/>
			 </span></td>
		 </tr>
	<tr><td><html:hidden property="id" />	</td></tr>
	<tr><td colspan=2>&nbsp;<input type="hidden" id="lochidden" name="lochidden" /></td></tr>
	</table>
	<span style="visibility:hidden" id="counterlabel">
	<table id="countertable" name="countertable" border="1">
	<tr>
		<td border=0 class="thStlyle" ><div align="center">&nbsp;</div></td>
		<td class="thStlyle" ><div align="center"><bean:message key="counter" /></div></td>
		<td class="thStlyle" ><div align="center"><bean:message key="LocationDesc" /></div></td>
		<td class="thStlyle" id ="ip" ><div align="center"><bean:message key="ipadress" /></div></td>
	</tr>

	<%

		/*
		 *  Create mode
		 */

		if(locationform == null)
		{
	%>

	 <tr>
	 	<td  onClick="changeColor(this,'countertable');" name="selectTd" id="selectTd"><div align="center">&nbsp</div></td>
	 	<td class="fieldcell" ><div align="left" ><html:text property= "counter" styleId= "counter" value=""  onblur="trim(this,this.value);"/></div> </td>
		<td class="fieldcell" ><div align="left" ><html:text property= "description" styleId= "description" value=""  onblur="trim(this,this.value);"/></div> </td>
		<td class="fieldcell" id ="ipText"><div align="left" ><html:text property= "ipaddr" styleId= "ipaddr" value=""  onblur="trim(this,this.value);validateIPAddress(this);"/></div>
		<html:hidden property="counterId"/><input type="hidden" id="ipaddrhidden" name="ipaddrhidden" ><input type="hidden" id="locIPMapID" name="locIPMapID"><input type="hidden" id="counterhidden" name="counterhidden"></td>
	</tr>

	<%

		/*
		 *  Modify
		 */

		}
		else
		{
		for(int i=0;i<locationform.getCounter().length;i++)
		{
		if(locationform.getCounter()[i] != null)
		{
	%>

	 <tr>
	 	<td  onClick="changeColor(this,'countertable');" name="selectTd" id="selectTd"><div align="center">&nbsp</div></td>
	 	<td class="fieldcell" ><div align="left" ><html:text property= "counter" styleId= "counter"  value="<%= locationform.getCounter()[i] %>" onblur="trim(this,this.value);"/></div> </td>
		<td class="fieldcell" ><div align="left" ><html:text property= "description" styleId= "description"  value="<%= locationform.getDescription()[i] %>" onblur="trim(this,this.value);"/></div>
		<%String loginType = (String) request.getAttribute("loginType");
		if("terminal".equals(loginType) && locationform.getIpaddr()[i]!=null){ System. out.println("terminal -----------------" +locationform.getIpaddr()[i]);%>
		<td class="fieldcell" id ="ipText"><div align="left" ><html:text property= "ipaddr" styleId= "ipaddr"  value="<%= locationform.getIpaddr()[i] %>" onblur="trim(this,this.value);validateIPAddress(this);"/></div> </td>
		<input type="hidden" id="ipaddrhidden" name="ipaddrhidden"  value="<%= locationform.getIpaddr()[i] %>"/>
		<input type="hidden" id="locIPMapID" name="locIPMapID" value="<%=locationform.getLocIPMapID()[i]%>"/>
		<%}%>
		<html:hidden property="counterId" value="<%= locationform.getCounterId()[i] %>"/>
		<input type="hidden" id="counterhidden" name="counterhidden" value="<%= locationform.getCounter()[i] %>" /></td>
	</tr>

	<%
		}
		}
		}
	%>

	</table>

	</span>
	<span style="visibility:visible" id="counterAddlabel">
	<table>
		<tr>
			<td><input type="button"  class="button" value="Add Row"   name="addbutton" onclick=" addRow('countertable');" /></td>
			<td><input type="button"  class="button" value="Delete Row"   name="delbutton" onclick=" deleteRow(this);" /></td>

		</tr>
	</table>
	
	<table>
    <tr><td class="smalltext" ><span class="leadon">*</span> - Mandatory Fields
    </td></tr>
    <tr>
    <td><html:hidden property="forward" /></td>
    <td><input type="button" class="button"  id ="save" name="save" value="Save" onclick="buttonpress();" /></td>
    <td><html:hidden property="loginType" /></td>
   <tr>
   </table>
</span>
	</td>
	</tr>


        </html:form>

	</table>


    </body>
</html>
