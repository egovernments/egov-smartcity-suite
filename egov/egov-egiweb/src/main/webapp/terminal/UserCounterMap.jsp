<%@ page import="java.util.*,org.egov.infstr.utils.*,org.egov.lib.rjbac.user.dao.*,org.egov.lib.rjbac.user.*,
org.egov.lib.security.terminal.model.*,java.text.*,org.egov.lib.security.terminal.client.ObjComparator,
org.apache.commons.lang.StringUtils" %>
<%@ include file="/includes/taglibs.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%!
ArrayList locationParentList,userList,usercounterList,locationList;
//String loginType;
Location loc=null;
%>
<%SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
locationList=(ArrayList)request.getAttribute("locationList");
Collections.sort(locationList,new ObjComparator());
locationParentList=(ArrayList)request.getAttribute("locationParentList");
userList=(ArrayList)request.getAttribute("userList");
usercounterList=(ArrayList)request.getAttribute("userCounterMapList");

loc=(Location)request.getAttribute("locationobj");

%>
<c:set var="locationList" value="<%=locationList%>" scope="page" />
<c:set var="locationParentList" value="<%=locationParentList%>" scope="page" />
<c:set var="userList" value="<%=userList%>" scope="page" />
<c:set var="usercounterList" value="<%=usercounterList%>" scope="page" />
<c:set var="loc" value="<%=loc%>" scope="page" />

<html>

<head>
		<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
		<title>User Counter Map</title>

		<SCRIPT type="text/javascript" src="<c:url value="/commonjs/ajaxCommonFunctions.js" />"></Script>

		    <SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/javascript/dateValidation.js" type="text/javascript"></SCRIPT>
			<script type="text/javascript" src="${pageContext.request.contextPath}/commonyui/build/yahoo/yahoo.js"></script>
			<script type="text/javascript" src="${pageContext.request.contextPath}/commonyui/build/dom/dom.js" ></script>
			<script type="text/javascript" src="${pageContext.request.contextPath}/commonyui/build/autocomplete/autocomplete-debug.js"></script>
			<script type="text/javascript" src="${pageContext.request.contextPath}/commonyui/build/event/event-debug.js"></script>
			<script type="text/javascript" src="${pageContext.request.contextPath}/commonyui/build/animation/animation.js"></script>

			<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/commonyui/build/reset/reset.css">
			<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/commonyui/build/fonts/fonts.css">
			<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/commonyui/examples/autocomplete/css/examples.css">

<style type="text/css">
		#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
		#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#codescontainer ul {padding:5px 0;width:80%;}
		#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
		#codescontainer li.yui-ac-highlight {background:#ff0;}
		#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>
<style type="text/css">
#codescontainer1 .yui-ac-content {position:absolute;width:100%;border:1px solid
#404040;background:#fff;overflow:hidden;z-index:9050;}
#codescontainer1 .yui-ac-shadow {position:absolute;margin:.3em;width:100%;background:#a0a0a0;z-index:9049;}
#codescontainer1 ul {padding:5px 0;width:100%;}
#codescontainer1 li {padding:0 5px;cursor:default;white-space:nowrap;}
#codescontainer1 li.yui-ac-highlight {background:#ff0;}
#codescontainer1 li.yui-ac-prehighlight {background:#FFFFCC;}
</style>

	<style type="text/css">
	#codescontainer2 .yui-ac-content {position:absolute;width:100%;border:1px solid
	#404040;background:#fff;overflow:hidden;z-index:9050;}
	#codescontainer2 .yui-ac-shadow {position:absolute;margin:.3em;width:100%;background:#a0a0a0;z-index:9049;}
	#codescontainer2 ul {padding:5px 0;width:100%;}
	#codescontainer2 li {padding:0 5px;cursor:default;white-space:nowrap;}
	#codescontainer2 li.yui-ac-highlight {background:#ff0;}
	#codescontainer2 li.yui-ac-prehighlight {background:#FFFFCC;}
	</style>



<script type="text/javascript">

		var userCodeArray;
		var selectedUserCode;
		var countersArray;
		var selectedCounterCode;

	/*
	 * Body Onload function
	 */
		var submitType = "<%=(String) request.getAttribute("buttonType")%>";
		var loginType="<%=(String)request.getAttribute("loginType")%>";

		function onBodyLoad()
    	{

			if("Location"==loginType)
			{
				document.getElementById("isLocation").checked=true;
				document.getElementById('loc').style.visibility = "visible";
				document.getElementById('loc1').style.visibility = "visible";
				document.getElementById('locdata').style.visibility = "visible";
				var locationData = document.getElementById('locationData');
				document.getElementById("isTerminal").checked=false;
				locationData.setAttribute("border","2px");
				document.getElementById('coudata').style.visibility = "hidden";
				addNewRow('locationData');
				var gltable= document.getElementById("locationData");
				gltable.deleteRow(gltable.rows.length-1);
			}
			if("Terminal"==loginType)
			{
				document.getElementById("isTerminal").checked=true;
				document.getElementById("isLocation").checked=false;
				document.getElementById('loc').style.visibility = "visible";
				document.getElementById('loc1').style.visibility = "visible";
				document.getElementById('coudata').style.visibility = "visible";
				var locationData = document.getElementById('terminaltable');
				locationData.setAttribute("border","2px");
				document.getElementById('locdata').style.visibility = "hidden";
				addNewRow('terminaltable');
				var gltable= document.getElementById("terminaltable");
				gltable.deleteRow(gltable.rows.length-1);
			}
			if(submitType == "showAll")
			{
				document.getElementById('loc2').style.visibility = "visible";
				document.getElementById('loc3').style.visibility = "visible";
			}
			else
			{
				document.getElementById('loc2').style.visibility = "hidden";
				document.getElementById('loc3').style.visibility = "hidden";
			}

			document.getElementById("locationname").options[0] = new Option('${loc.name}','${loc.id}');

			loadUsersCodes();
			loadCounters();
			modifyRows();

        }

	/*
	 * On submit buttonpress function is called
	 */
  		function buttonpress(arg)
        {



		 if(arg == "loadcreatedata")
		{

			if(document.getElementById("isLocation").checked == false && document.getElementById("isTerminal").checked == false)
			{
				alert("Select Location or Terminal check box ");
				return false;
			}
			if(validateTable() == false)
			{
				return false;
			}
			if(document.getElementById("isLocation").checked)
			{
				document.forms[0].loginType.value="Location";

			}
			else if(document.getElementById("isTerminal").checked)
			{
				document.forms[0].loginType.value="Terminal";
			}
			var table;
			var len ;
			if(document.getElementById("isLocation").checked)
			{
				table= document.getElementById('locationData');
				len =table.rows.length;
			}
			else if(document.getElementById("isTerminal").checked)
			{
				table= document.getElementById('terminaltable');
				len =table.rows.length;
			}
			for(var i=1;i<len;i++)
			{
				getControlInBranch(table.rows[i],'fromDate').disabled=false;
				getControlInBranch(table.rows[i],'toDate').disabled=false;
				getControlInBranch(table.rows[i],'userName').disabled=false;
				if(document.getElementById("isTerminal").checked)
				{
					getControlInBranch(table.rows[i],'counterName').disabled=false;
				}

			}
			if(assignUser() == false)
			{
				return false;
		    }
			document.getElementById("isLocation").disabled=false;
			document.getElementById("isTerminal").disabled=false;
			document.getElementById("locationname").disabled=false;
			document.forms[0].forward.value=arg;
			document.forms[0].action = "${pageContext.request.contextPath}/terminal/usercountermap.do?submitType=createUserControlMapping";
			
				var table;
				var len ;
				var hit = false;
				if(document.getElementById("isLocation").checked)
				{
				table= document.getElementById('locationData');
				len =table.rows.length;
				}
				else if(document.getElementById("isTerminal").checked)
				{
				table= document.getElementById('terminaltable');
				len =table.rows.length;
				}
				for(var i=1;i<len;i++)
				{
			
					if(getControlInBranch(table.rows[i],'userName').value=="")
					{
					var check = confirm("Empty rows exists. Click OK to proceed. Click Cancel to abort" );
						if(check)
						{
						document.forms[0].submit();
						hit=true;
						break;

						}
						else
						hit=true;
						break;
						}
				}
				if(hit==false)
				document.forms[0].submit();
			
		}

        }

     function assignUser()
     {
		//alert("hiiiiii");
		var table;
		if(document.getElementById("isLocation").checked)
		{
			table= document.getElementById('locationData');
			var len = table.rows.length;
			if(len>2)
			{
				for(var j=1;j<len;j++)
				{
					for(var k=j+1;k<len;k++)
					{
						if(j != k && getControlInBranch(table.rows[j],'userName').value != ""
										&& getControlInBranch(table.rows[k],'userName').value != ""
										&& (getControlInBranch(table.rows[j],'userName').value == getControlInBranch(table.rows[k],'userName').value) )
						{

							if((getControlInBranch(table.rows[j],'toDate').value==""
							&& getControlInBranch(table.rows[k],'toDate').value!="")||
							(getControlInBranch(table.rows[j],'toDate').value=="" &&
							getControlInBranch(table.rows[k],'toDate').value==""))
							{
								alert("This "+getControlInBranch(table.rows[j],'userName').value+
								" is already assigned in this period for row "+j);
								return false;

							}
							else if((getControlInBranch(table.rows[k],'toDate').value==""
							&& getControlInBranch(table.rows[j],'toDate').value!="")||
							(getControlInBranch(table.rows[j],'toDate').value!="" &&
							getControlInBranch(table.rows[k],'toDate').value!=""))
							{
								dt1 = getControlInBranch(table.rows[k],'fromDate').value;
								dt2 = getControlInBranch(table.rows[j],'toDate').value;
								dt1 = dt1.split('/');
								dt2 = dt2.split('/');
								var d = new Date();
								var d1 = new Date();
								d.setFullYear(dt1[2],dt1[1]-1,dt1[0]);
								d.setDate(dt1[0]);
								d.setMonth(dt1[1]-1);
								d1.setFullYear(dt2[2],dt2[1]-1,dt2[0]);
								d1.setDate(dt2[0]);
								d1.setMonth(dt2[1]-1);
								var selectedDate = d.getTime();
								var oldDate = d1.getTime();
								if (selectedDate <= oldDate) {

								alert("This "+getControlInBranch(table.rows[j],'userName').value+
									" is already assigned in this period for row "+j);
								return false;
								}

							}
						}
					}

				}
			}

		}
		else if(document.getElementById("isTerminal").checked)
		{
			table= document.getElementById('terminaltable');
			var len = table.rows.length;
			if(len>2)
			{
				for(var j=1;j<len;j++)
				{
					for(var k=j+1;k<len;k++)
					{
						if(j != k && getControlInBranch(table.rows[j],'userName').value != ""
						&& getControlInBranch(table.rows[k],'userName').value != ""
						&& (getControlInBranch(table.rows[j],'userName').value ==
						getControlInBranch(table.rows[k],'userName').value) &&
						getControlInBranch(table.rows[j],'counterName').value!="" &&
						getControlInBranch(table.rows[k],'counterName').value!="" &&
						(getControlInBranch(table.rows[j],'counterName').value ==
						getControlInBranch(table.rows[k],'counterName').value)
						)
						{
							if((getControlInBranch(table.rows[j],'toDate').value==""
							&& getControlInBranch(table.rows[k],'toDate').value!="")||
							(getControlInBranch(table.rows[j],'toDate').value=="" &&
							getControlInBranch(table.rows[k],'toDate').value==""))
							{
								alert("The userName : "+getControlInBranch(table.rows[j],'userName').value+
								" is already assigned in this period for row "+j);
								return false;

							}
							else if((getControlInBranch(table.rows[k],'toDate').value==""
							&& getControlInBranch(table.rows[j],'toDate').value!="")||
							(getControlInBranch(table.rows[j],'toDate').value!="" &&
							getControlInBranch(table.rows[k],'toDate').value!=""))
							{
								dt1 = getControlInBranch(table.rows[k],'fromDate').value;
								dt2 = getControlInBranch(table.rows[j],'toDate').value;
								dt1 = dt1.split('/');
								dt2 = dt2.split('/');
								var d = new Date();
								var d1 = new Date();
								d.setFullYear(dt1[2],dt1[1]-1,dt1[0]);
								d.setDate(dt1[0]);
								d.setMonth(dt1[1]-1);
								d1.setFullYear(dt2[2],dt2[1]-1,dt2[0]);
								d1.setDate(dt2[0]);
								d1.setMonth(dt2[1]-1);
								var selectedDate = d.getTime();
								var oldDate = d1.getTime();
								if (selectedDate <= oldDate) {

								alert("The userName : "+getControlInBranch(table.rows[j],'userName').value+
									" is already assigned in this period for row "+j);
								return false;
								}

							}
						}
					}

				}
			}

	}
	return true;
}

	function deleteRow()
	{
		var firsttable=document.getElementById("terminaltable");
		var tablelength = firsttable.rows.length;
		while(tablelength > 2 )
		{
			firsttable.deleteRow(tablelength-1);
			tablelength = firsttable.rows.length;
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






   function loadUsersCodes()
   {
   	    var type='getAllUserNames';
   		var url = "${pageContext.request.contextPath}/commons/Process.jsp?type=" +type+ " ";
   		var req2 = initiateRequest();
   		 req2.onreadystatechange = function()
   		 {
   				  if (req2.readyState == 4)
   				  {
   					  if (req2.status == 200)
   					  {

   						var codes2=req2.responseText;
   						var a = codes2.split("^");
   						var codes = a[0];
   						userCodeArray=codes.split("+");
   						selectedUserCode = new YAHOO.widget.DS_JSArray(userCodeArray);
   						//alert("selectedUserCode >> " +selectedUserCode);
   					  }
   				  }
   		};
   		req2.open("GET", url, true);
   		req2.send(null);

 	}
 	var yuiflag1 = new Array();


function getUserByEnteringCode(code,userId){
var url = "${pageContext.request.contextPath}/commons/Process.jsp?type=getUserByEnteringCode&code="+code;
var req = initiateRequest();
req.onreadystatechange = function(){
if (req.readyState == 4){
if (req.status == 200){
var glcodes=req.responseText
var a = glcodes.split("^");
var codes = a[0];
var emp = codes.split("`--`");
if(codes == ""){
}
else{
userId.value = emp[1];
}
}
}
};
req.open("GET", url, true);
req.send(null);
}
function getCounterByEnteringCode(code,counter){
var url = "${pageContext.request.contextPath}/commons/Process.jsp?type=getCounterByEnteringCode&code="+code;
var req = initiateRequest();
req.onreadystatechange = function(){
if (req.readyState == 4){
if (req.status == 200){
var glcodes=req.responseText
var a = glcodes.split("^");
var codes = a[0];
var emp = codes.split("`--`");
if(codes == ""){
}
else{
counter.value = emp[1];
}
}
}
};
req.open("GET", url, true);
req.send(null);
}

 	function autocompletecode(obj)
	{
		var src = obj;
		var target = document.getElementById('codescontainer');
		var posSrc=findPos(src);
		target.style.left=posSrc[0];
		target.style.top=posSrc[1]+25;
		if(obj.name=='userName') target.style.left=posSrc[0]+0;

 		target.style.width=280;
		doAutoComplete(1,obj,'codescontainer',selectedUserCode,15);
		

	}

	function fillusernames(obj,neibrObjName)
	{
		markYuiflagUndefined(1);
		var currRow=getRow(obj);
		neibrObj1=getControlInBranch(currRow,neibrObjName);
		var temp = obj.value;temp = temp.split("`--`");
		var xyz = getControlInBranch(currRow,'userName');
		xyz.value=temp[0];
		if(temp[1] == null)
		getUserByEnteringCode(temp[0],neibrObj1);
		if(temp[1]==null || temp[1]=="") { neibrObj1.value=""; return; }
		if(temp[1]==null && (neibrObj1.value!='' || neibrObj1.value!=null) ) {  return ;}
		else {
				neibrObj1.value=temp[1];
		}
	}
	function autocompletecode1(obj)
	{
		var src = obj;
		var target = document.getElementById('codescontainer1');
		var posSrc=findPos(src);
		target.style.left=posSrc[0];
		target.style.top=posSrc[1]+25;
		if(obj.name=='userName') target.style.left=posSrc[0]+0;

 		target.style.width=300;
 		doAutoComplete(1,obj,'codescontainer1',selectedUserCode,15);
 		
	}
	function fillusernames1(obj,neibrObjName)
	{
		var currRow=getRow(obj);
		markYuiflagUndefined(1);
		neibrObj1=getControlInBranch(currRow,neibrObjName);
		var temp = obj.value;temp = temp.split("`--`");
		var xyz = getControlInBranch(currRow,'userName');
		xyz.value=temp[0];
		if(temp[1] == null)
		getUserByEnteringCode(temp[0],neibrObj1);
		if(temp[1]==null || temp[1]=="") { neibrObj1.value=""; return; }
		 	if(temp[0]==null && (neibrObj1.value!='' || neibrObj1.value!=null) ) {  return ;}
		 	else {	neibrObj1.value=temp[1];
		 		 }
	}
	function loadCounters()
	{

			var id ="${loc.id}";
			var type='getAllCounters';
			var url = "${pageContext.request.contextPath}/commons/Process.jsp?type=" +type+ "&id="+id+" ";
			var req2 = initiateRequest();
			 req2.onreadystatechange = function()
			 {
					  if (req2.readyState == 4)
					  {
						  if (req2.status == 200)
						  {

							var codes2=req2.responseText;
							//alert("codes2 >> " +codes2);
							var a = codes2.split("^");
							var codes = a[0];
							countersArray=codes.split("+");
							//alert("countersArray " +countersArray);
							selectedCounterCode = new YAHOO.widget.DS_JSArray(countersArray);
							//alert("selectedCounterCode >>>  " +selectedCounterCode);
						  }
					  }
			};
			req2.open("GET", url, true);
			req2.send(null);
	}



	function autocompletecode2(obj)
	{
		var src = obj;
		var target = document.getElementById('codescontainer2');
		var posSrc=findPos(src);
		target.style.left=posSrc[0];
		target.style.top=posSrc[1]+0;
		if(obj.name=='counterName') target.style.left=posSrc[0]+0;

		target.style.width=420;
		doAutoComplete(1,obj,'codescontainer2',selectedCounterCode,15);

	}
	function fillusernames2(obj,neibrObjName)
	{
		markYuiflagUndefined(1);
		var currRow=getRow(obj);
		neibrObj1=getControlInBranch(currRow,neibrObjName);
		var temp = obj.value;temp = temp.split("`--`");
		var xyz = getControlInBranch(currRow,'counterName');
		xyz.value=temp[0];
		if(temp[1] == null)
		getCounterByEnteringCode(temp[0],neibrObj1);
		if(temp[1]==null || temp[1]=="") { neibrObj1.value=""; return; }
		if(temp[1]==null && (neibrObj1.value!='' || neibrObj1.value!=null) ) {  return ;}
		else {
				neibrObj1.value=temp[1];
		}
	}
	function checkUserCounter(obj)
	{
		var row=getRow(obj);
		var date = getControlInBranch(row,"fromDate");
		var datefrom = date.value;
		if(datefrom!="")
		{
		var dateto = getControlInBranch(row,"toDate");
		var dateto = dateto.value;
		var usrId = getControlInBranch(row,"userId");
		var userId = usrId.value;
		if(document.getElementById("isLocation").checked)
		{
			locId = document.getElementById("locationname").value;
		}
		if(document.getElementById("isTerminal").checked)
		{
			var couId = getControlInBranch(row,"counter");
			locId = couId.value;
		}
		var http = initiateRequest();

		var url = "<c:url value='/commons/checkUserCounter.jsp?userId="+userId+"&dateFrom="+datefrom+"&dateTo="+dateto+" '/> ";
		var hit="false";
		http.open("GET", url, true);
		http.onreadystatechange = function()
		{
			if (http.readyState == 4)
			{
				if (http.status == 200)
				{
				       var statusString =http.responseText.split("^");
					    if(statusString[0]=="true")
					{
						var popup = statusString[0];
						if(popup!="" && hit=="false")
						{
							alert('this User is already assigned for this period');
							getControlInBranch(row,"toDate").focus();
							return false;
						}
				       }
				 }
			}
		};
			http.send(null);
		}

	}

	function validateTable()
		{
		
		if(document.getElementById("locationname").value=="")
		{
		alert("Please select the Location/Terminal");
		document.getElementById("locationname").focus();
		return false;
		}
		var table;
		var len ;
		if(document.getElementById("isLocation").checked)
		{
		table= document.getElementById('locationData');
		len =table.rows.length;
		}
		else if(document.getElementById("isTerminal").checked)
		{
		table= document.getElementById('terminaltable');
		len =table.rows.length;
		}
		for(var i=1;i<len;i++)
		{
		if(document.getElementById("isTerminal").checked)
		{
		
			if(getControlInBranch(table.rows[i],'counterName').value!="")
			{
				if(getControlInBranch(table.rows[i],'userName').value=="")
				{
				alert("Please enter User Name for row" +i);
				return false;
				}
				if(getControlInBranch(table.rows[i],'counter').value=="")
					{
					alert("Please select the proper Counter Name for row "+i);
					return false;
					}
	
			}
			if(getControlInBranch(table.rows[i],'toDate').value!="")
			{
				if(getControlInBranch(table.rows[i],'counterName').value=="")
				{
				alert("Please enter Counter Name for row" +i);
				return false;
				}
					
			}
			if(getControlInBranch(table.rows[i],'userName').value!="")
			{
				if(getControlInBranch(table.rows[i],'counterName').value=="")
				{
				alert("Please enter Counter Name for row" +i);
				return false;
				}
			
			}
		
	
		}
		//alert(getControlInBranch(table.rows[i],'fromDate').value);
		
		if(getControlInBranch(table.rows[i],'fromDate').value!="")
		{
			if(getControlInBranch(table.rows[i],'userName').value=="")
			{
			alert("Please enter User Name for row" +i);
			return false;
			}
		
		}
		if(getControlInBranch(table.rows[i],'toDate').value!="")
			{
			if(getControlInBranch(table.rows[i],'userName').value=="")
			{
			alert("Please enter User Name for row" +i);
			return false;
			}
			
		}
		if(getControlInBranch(table.rows[i],'userName').value!="")
		{
			if(getControlInBranch(table.rows[i],'fromDate').value=="")
			{
			alert("Please enter From Date for row" +i);
			return false;
			}
			if(getControlInBranch(table.rows[i],'userId').value=="")
					{
					alert("Please select the proper user Name for row "+i);
					return false;
			}
			
		}
		if(getControlInBranch(table.rows[i],'fromDate').value!="" && getControlInBranch(table.rows[i],'toDate').value!="")
		{
		rTF=checkFdateTdate(getControlInBranch(table.rows[i],'fromDate').value,getControlInBranch(table.rows[i],'toDate').value);
		if(rTF==false)
		{
		alert('From date should be less than or equal to To Date for row '+i);
		return false;
		}
		}
	
		
		if(getControlInBranch(table.rows[i],'modifyJur').checked)
		{
		getControlInBranch(table.rows[i],'selCheck').value="yes";
		}
		else
		{
		getControlInBranch(table.rows[i],'selCheck').value="no";
		}
		if(assignUser() == false)
		{
		return false;
		}
		
		}
	
		
		return true;
	}
		function deleteRow(arg)
		{
			var firsttable;
			var tablelength;
			if(arg=='locationData')
			{
				firsttable=document.getElementById("locationData");
				tablelength = firsttable.rows.length;
				while(tablelength > 2 )
				{
					firsttable.deleteRow(tablelength-1);
					tablelength = firsttable.rows.length;
				}
	
			}
			else if(arg=='terminaltable')
			{
				firsttable=document.getElementById("terminaltable");
				tablelength = firsttable.rows.length;
				while(tablelength > 2 )
				{
					firsttable.deleteRow(tablelength-1);
					tablelength = firsttable.rows.length;
				}
	
			}
	
	}
	function deleteRow(arg)
	{
		var firsttable;
		var tablelength;
		if(arg=='locationData')
		{
			firsttable=document.getElementById("locationData");
			tablelength = firsttable.rows.length;
			while(tablelength > 2 )
			{
				firsttable.deleteRow(tablelength-1);
				tablelength = firsttable.rows.length;
			}

		}
		else if(arg=='terminaltable')
		{
			firsttable=document.getElementById("terminaltable");
			tablelength = firsttable.rows.length;
			while(tablelength > 2 )
			{
				firsttable.deleteRow(tablelength-1);
				tablelength = firsttable.rows.length;
			}

		}

	}
	function addRow(arg)
	{
		if(validateTable() == false)
		{
				return false;
		}
		if(arg=="locationData")
		{
			var tableObj = document.getElementById("locationData");
			var tbody=tableObj.tBodies[0];
			var lastRow = tableObj.rows.length;
			addNewRow('locationData');
			document.forms[0].userId[lastRow].value="";
			document.forms[0].userName[lastRow].value="";
			document.forms[0].fromDate[lastRow].value="";
			document.forms[0].toDate[lastRow].value="";
			document.forms[0].modifyJur[lastRow].style.visibility="hidden";
			document.forms[0].fromDate[lastRow].disabled=false;
			document.forms[0].toDate[lastRow].disabled=false;
			document.forms[0].userName[lastRow].disabled=false;
			document.forms[0].userCounterId[lastRow].value="";


		}
		if(arg=="terminaltable")
		{

			
			 var tableObj = document.getElementById("terminaltable");
			 var tbody=tableObj.tBodies[0];
			 var lastRow = tableObj.rows.length;
			 addNewRow('terminaltable');
			 document.forms[0].userId[lastRow-1].value="";
			 document.forms[0].counter[lastRow-1].value="";
			 document.forms[0].counterName[lastRow-1].value="";
			 document.forms[0].userName[lastRow-1].value="";
			 document.forms[0].fromDate[lastRow-1].value="";
			 document.forms[0].toDate[lastRow-1].value="";
			 document.forms[0].fromDate[lastRow-1].disabled=false;
			 document.forms[0].userName[lastRow-1].disabled=false;
			 document.forms[0].counterName[lastRow-1].disabled=false;
			 document.forms[0].toDate[lastRow-1].disabled=false;
			 document.forms[0].modifyJur[lastRow-1].style.visibility="hidden";
			 document.forms[0].userCounterId[lastRow-1].value="";
		}
	}
function addNewRow(arg)
{
		
	var tbl = document.getElementById(arg);
	
	if(!tbl || !tbl.rows) return false;			
	rowLength = tbl.rows.length;
	
	//addNewRow(TableID);
	var tbody=tbl.tBodies[0];
	var rows=tbl.rows;
	var existingRows=rows.length -1; //header is excluded
	var lastRow = tbl.rows.length;
	var rowsToClone ; //
	rowsToClone = tbl.getAttribute("rowsToClone");
	//alert("rowsToClone"+rowsToClone);
	if (!rowsToClone){ // not yet copied..
		rowsToClone = new Array();
		rowsToClone[0] = rows[1].cloneNode(true);
		if (existingRows > 1)rowsToClone[1] = rows[2].cloneNode(true);
		else rowsToClone[1] = rows[1].cloneNode(true);
		tbl.setAttribute("rowsToClone", rowsToClone);
	}	
	//add more rows
	var rowstoAdd = rowLength -existingRows;
	var rowToClone=0;
	var newNode;
	for(var i=existingRows; i<rowLength; i++){
		if(i%2==0 || existingRows < 2) rowToClone=0;//For alternate cloning of records
		else rowToClone=1;
		newNode=rowsToClone[rowToClone].cloneNode(true);
		tbody.appendChild(newNode);


	}
	
	
	
	//getControlInBranch(tbl.rows[rowLength-1],'countername').focus();

	return true;
	
	
}

	function validateDateFormat(obj)
	{
	 var dtStr=obj.value;
	 var year;
	 var day;
	 var month;
	 var leap=0;
	 var valid=true;
	 var oth_valid=true;
	 var feb=false;
	 var validDate=true;

	    if(dtStr!="" && dtStr!=null)
	    {
	    	year=dtStr.substr(6,4);
	    	month=dtStr.substr(3,2);
	    	day=dtStr.substr(0,2);

	    	if(year=="0000" || year<1900 || month=="00" || day=="00" || dtStr.length<10)
	    	{
	    		validDate=false;
	    	}

	    	if(validDate==true)
	    	{
	    		//if(year>1900 && year<=
	    		leap=year%4;

	 		if(leap==0)
	    		{
	    			//alert("Leap Year");

	    			if(month=="2") // || month==02)
	    			{
	    				valid=false;
	    				feb=true;
	    			}

	    			else if(month=="02")
	    			{
	    				if(day>29)
	    				{
	    					valid=false;
	    				}
	    				feb=true;
	    			}

	    		}

	    		else
	    		{
	    			if(month=="2")
	    			{
	    				valid=false;
	    				feb=true;
	    			}
	    			else if(month=="02")
	    			{
	    				if(day>28)
	    				{
	    					valid=false;
	    				}
	    				feb=true;
	    			}

	    		}

	    		if(feb==false)
	    		{
	    			if(month=="03" || month=="01" || month=="05" || month=="07" || month=="08" || month=="10" || month=="12")
	    			{
	    				if(day>31)
	    				{
	    					oth_valid=false;
	    				}
	    			}

	    			else if(month=="04" || month=="06" || month=="09" || month=="11")
	    			{
	    				if(day>30)
	    				{
	    					oth_valid=false;
	    				}
	    			}

	    			else
	    			{
	    				oth_valid=false;
	    			}

	    		}
	    	}
	    }

	    if(valid==false || oth_valid==false || validDate==false)
	    {
	    	alert("Please enter the valid date in the dd/MM/yyyy Format only");
	    	obj.value="";
	    	obj.focus();
	    }
	    return;
	}
	function modifyRows()
	{
		var location = document.getElementById("locationname").value;
		//alert("location >>>" + location);
		var hit=false;
		var table;
		var dataLength = new Array();
		if(document.getElementById("isLocation").checked && location!="")
		{
			var k = 0;
			table= document.getElementById('locationData');
			<%for(int i=0;i<usercounterList.size();i++){
				UserCounterMap obj =(UserCounterMap) usercounterList.get(i);%>
				//alert("UserCounterMap "+ "<%=obj.getId()%>");

			if(location == "<%=obj.getCounterId().getId()%>")
			{
				//	alert("location lll>>>" + location);
				dataLength[k]="<%=obj.getCounterId().getId()%>";
				k++;

			}
			<%}%>
			//alert("dataLength "+ dataLength.length);
			//alert("usercounterList.size() "+ "<%=usercounterList.size()%>");

			var i=1;
			<%for(int i=0;i<usercounterList.size();i++){
				UserCounterMap obj =(UserCounterMap) usercounterList.get(i);%>
				if(location == "<%=obj.getCounterId().getId()%>")
			{
				//alert("location data  >>>" + location);
				getControlInBranch(table.rows[i],'modifyJur').style.visibility ="visible";
				getControlInBranch(table.rows[i],'fromDate').value="<%=formatter.format(obj.getFromDate())%>";
				getControlInBranch(table.rows[i],'fromDate').disabled=true;
				getControlInBranch(table.rows[i],'userCounterId').value="<%=obj.getId()%>"
				<%if(obj.getToDate()!=null && !obj.getToDate().equals(""))
				{%>
					getControlInBranch(table.rows[i],'toDate').value="<%=formatter.format(obj.getToDate())%>";
				<%}%>
				getControlInBranch(table.rows[i],'toDate').disabled=true;
				getControlInBranch(table.rows[i],'userName').disabled=true;
				<c:forEach var="userObj" items="${userList}" >
				if("${userObj.id}"=="<%=obj.getUserId().getId()%>")
				{
					getControlInBranch(table.rows[i],'userId').value="${userObj.id}";
					getControlInBranch(table.rows[i],'userName').value="${userObj.userName}";
				}
				</c:forEach>
				hit=true;
				if(i==dataLength.length)
				{
					hit=false;
				}
				i++;
				if(hit==true)
				{
					addRow('locationData');
				}
			}
			<%}%>

		}
		else if(document.getElementById("isTerminal").checked && location!="")
		{
			table= document.getElementById('terminaltable');
			var i=1;
			var k = 0;

			<c:forEach var="obj1" items="${locationList}" >
			if(location == "${obj1.locationId.id}")
			{

				<%for(int i=0;i<usercounterList.size();i++){
					UserCounterMap obj =(UserCounterMap) usercounterList.get(i);%>
				if("${obj1.id}" ==  "<%=obj.getCounterId().getId()%>")
				{
					dataLength[k]="<%=obj.getCounterId().getId()%>";
					k++;
				}
				<%}%>
			}
			</c:forEach>
			<c:forEach var="obj1" items="${locationList}" >
			if(location == "${obj1.locationId.id}")
			{
				<%for(int i=0;i<usercounterList.size();i++){

				UserCounterMap obj =(UserCounterMap) usercounterList.get(i);%>
				if("${obj1.id}" ==  "<%=obj.getCounterId().getId()%>")
				{
					//alert("success1111");
					getControlInBranch(table.rows[i],'modifyJur').style.visibility ="visible";
					getControlInBranch(table.rows[i],'fromDate').disabled=true;
					getControlInBranch(table.rows[i],'toDate').disabled=true;
					getControlInBranch(table.rows[i],'counterName').disabled=true;
					getControlInBranch(table.rows[i],'userName').disabled=true;
					getControlInBranch(table.rows[i],'userCounterId').value="<%=obj.getId()%>"
					getControlInBranch(table.rows[i],'fromDate').value="<%=formatter.format(obj.getFromDate())%>";
					<%if(obj.getToDate()!=null && !obj.getToDate().equals(""))
					{%>
						getControlInBranch(table.rows[i],'toDate').value="<%=formatter.format(obj.getToDate())%>";
					<%}%>
					<c:forEach var="userObj" items="${userList}" >

					if("${userObj.id}"=="<%=obj.getUserId().getId()%>")
					{
						getControlInBranch(table.rows[i],'userId').value="${userObj.id}";
						getControlInBranch(table.rows[i],'userName').value="${userObj.userName}";
				    }
					</c:forEach>
					getControlInBranch(table.rows[i],'counter').value="<%=obj.getCounterId().getId()%>";
					getControlInBranch(table.rows[i],'counterName').value="<%=obj.getCounterId().getName()%>";
					hit=true;
					if(i==dataLength.length)
					{
						hit=false;

					}

					i++;
					if(hit==true)
					{
						addRow('terminaltable');

					}
				}
				<%}%>
			}
			</c:forEach>

		}
	}
	function validate()
	{
			var table;
			var len ;
			if(document.getElementById("isLocation").checked)
			{
				table= document.getElementById('locationData');
				len =table.rows.length;
			}
			else if(document.getElementById("isTerminal").checked)
			{
				table= document.getElementById('terminaltable');
				len =table.rows.length;
			}
			for(var i=1;i<len;i++)
			{
				if(getControlInBranch(table.rows[i],'modifyJur').checked)
				{
					getControlInBranch(table.rows[i],'fromDate').disabled=false;
					getControlInBranch(table.rows[i],'toDate').disabled=false;
				}
				else if(getControlInBranch(table.rows[i],'userName').disabled==true &&
				getControlInBranch(table.rows[i],'modifyJur').checked==false)
				{
					getControlInBranch(table.rows[i],'fromDate').disabled=true;
					getControlInBranch(table.rows[i],'toDate').disabled=true;
					getControlInBranch(table.rows[i],'userName').disabled=true;

				}

			}


	}
	function checkDate(obj) {
	//alert("gdfgdfg");
	dt1 = obj.value;
	var now = new Date().getTime();
	dt1 = dt1.split('/');
	var d = new Date();
	d.setFullYear(dt1[2],dt1[1]-1,dt1[0]);
	d.setDate(dt1[0]);
	d.setMonth(dt1[1]-1);
	var selectedDate = d.getTime();

	if (selectedDate < now) {  // not before today's date

	alert ("Date should be greater than or equal to today's date!");
	obj.value="";
	obj.focus();
	return false;
	}

	return true;
}


    </script>

</head>

	<body bgcolor="#FFFFFF"  onLoad="onBodyLoad()">


	 <html:form action="/terminal/usercountermap" method="post">



		  <table align="center" id="mainTable" name="mainTable" class="tableStyle">
		 <tr>
			<td  class="tableheader" colspan="6" align="center" height="24"><bean:message key="userCounter" /><span></td>
		</tr>
 		<tr><td colspan=6>&nbsp;</td></tr>


             <tr>
					<td class="labelcellforsingletd"align="center" width="35%"><bean:message key="Location" /><input type="checkbox"  id="isLocation" name="isLocation"  disabled="true"/></td>
				   <td class="labelcellforsingletd" align="left" width="35%"><bean:message key="terminal" /><input type="checkbox"  id="isTerminal" name="isTerminal"  disabled="true"/></td>
             </tr>

		</table>
		<table>
			<tr><td colspan=4>&nbsp;</td></tr>

			<tr>
				<td class="labelcell" align="right" width="35%"><span style="visibility:hidden" id="loc"><bean:message key="location/terminal" /><span class="leadon">*</span></span></td>
				<td  align="left" width="35%"><span style="visibility:hidden" id="loc1" class="smallfieldcell"> <html:select  styleId="locationname" disabled="true" property="locationId" styleClass="fieldinput" >
					<html:option value="">--Choose--</html:option></html:select></span>
					<td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				</td>
			</tr>
			<tr id="showAll">
			<td class="labelcell" align="right" width="35%"><span style="visibility:hidden" id="loc2">Show All</span></td>
							<td  align="left" width="35%"><span style="visibility:hidden" id="loc3" class="smallfieldcell"><input type="checkbox" name="isActiveValue" id="isActiveValue" value="ON" checked="true" disabled="true"> </span>
								<td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
							</td>
			</tr>
		</table>
		<table>
			<tr><td colspan=4>&nbsp;</td></tr>
		</table>
		<span style="visibility:hidden" id="coudata">
		<table id="terminaltable" name="terminaltable">
		<tr>
			<td ><div align="center">&nbsp;<bean:message key="modify" />&nbsp;</div></td>
			<td class="labelcell"><div align="center"><bean:message key="counter" /></div></td>
			<td class="labelcell"><div align="center"><bean:message key="user" /></div></td>
			<td class="labelcell"><div align="center"><bean:message key="fromDate" />(dd/mm/yyyy)</div></td>
			<td class="labelcell"><div align="center"><bean:message key="toDate" />(dd/mm/yyyy)</div></td>
			<td align="right" ><input type="button" name="add" id="add" value="AddRow" onclick="addRow('terminaltable')"></td>
		</tr>

		<tr>
		<td ><div align="center" ><span id="terDataBox"><input type="checkbox" name="modifyJur" id="modifyJur" value="ON" onclick="validate()" style="visibility:hidden" >
												<input type="hidden" name="selCheck" id="selCheck">
												<input type="hidden" name="userCounterId" id="userCounterId"></span></div></td>
			<td class="fieldcell"><div align="left" ><html:hidden property="counter" styleId="counter" /><input type="text " name="counterName" Id= "counterName"   autocomplete="off" onkeyup="autocompletecode2(this);" onblur="fillusernames2(this,'counter');trim(this,this.value);checkUserCounter(this)"/></div> </td>
			<td class="fieldcell"><div align="left" ><input type="hidden" name="userId" id="userId"><input type="text " name="userName" Id= "userName"    autocomplete="off" onkeyup="autocompletecode(this);" onblur="fillusernames(this,'userId');trim(this,this.value);checkUserCounter(this)"/></div> </td>
			<td class="fieldcell"><div align="left" ></div><input type="text " name="fromDate"  id="fromDate"size="25" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="DateFormat(this,this.value,event,true,'3');validateDateFormat(this);checkDate(this);" onchange="checkUserCounter(this)"/></td>
			<td class="fieldcell"><div align="left" ><input type="text " name="toDate" id="toDate"size="25" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="DateFormat(this,this.value,event,true,'3');validateDateFormat(this);checkDate(this);checkUserCounter(this)"/></div></td>
		</tr>

		</table>
		<table>
		<tr><td><div id="codescontainer2"></div></td></tr>
		<tr><td><div id="codescontainer"></div></td></tr>
				</table>
		</span>

		<span style="visibility:hidden" id="locdata">
		<table id="locationData">
			<tr>
				<td ><div align="center">&nbsp;<bean:message key="modify" />&nbsp;</div></td>
				<td class="labelcell"><div align="center"><bean:message key="user" /></div></td>
				<td class="labelcell"><div align="center"><bean:message key="fromDate" />(dd/mm/yyyy)</div></td>
				<td class="labelcell"><div align="center"><bean:message key="toDate" />(dd/mm/yyyy)</div></td>
				<td align="right" ><input type="button" name="add" id="add" value="AddRow" onclick="addRow('locationData')"></td>
			</tr>

				 <tr>
				<td ><div align="center" ><span id="locDataBox"><input type="checkbox" name="modifyJur" id="modifyJur" value="ON" onclick="validate()" style="visibility:hidden" >
												<input type="hidden" name="selCheck" id="selCheck"><input type="hidden" name="userCounterId" id="userCounterId"></span></div></td>
		 			<td class="fieldcell" ><div align="left" ><input type="hidden" name="userId" id="userId"><input type="text " name="userName" Id= "userName"  autocomplete="off" onkeyup="autocompletecode1(this);" onblur="fillusernames1(this,'userId');trim(this,this.value);checkUserCounter(this)"/></div> </td>
					<td class="fieldcell" ><div align="left" ></div><input type="text " name="fromDate"  id="fromDate"size="25" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="DateFormat(this,this.value,event,true,'3');validateDateFormat(this);checkDate(this);" onchange="checkUserCounter(this)"/></td>
					<td class="fieldcell" ><div align="left" ><input type="text " name="toDate" id="toDate"size="25" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="DateFormat(this,this.value,event,true,'3');validateDateFormat(this);checkDate(this);checkUserCounter(this)" /></div></td>
		</tr>
		</table>
		<table><tr><td><div id="codescontainer1"></div></td></tr>


				</table>

		</span>
		<table>
			<tr><td colspan=4>&nbsp;</td></tr>
			<tr>
		    <td><html:hidden property="forward" /></td>
		    <td><html:hidden property="loginType" /></td>
		    <td><input type="button" class="button" value="Save" onclick="buttonpress('loadcreatedata');" /></td>
		    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		   <tr>
   		</table>




   </html:form>
   </body>
   </html>