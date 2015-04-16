
<%@ include file="/includes/taglibs.jsp"%>
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		 org.egov.commons.*,
		 org.egov.pims.commons.client.*,
		 org.egov.pims.commons.*,
		 org.egov.pims.commons.service.*,
		 org.egov.pims.commons.dao.*,
		 org.egov.commons.service.*,
		 org.egov.commons.dao.*,
		 java.text.SimpleDateFormat
		 "
%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>Choose Object Type</title>
	

	<SCRIPT type="text/javascript" src="../javascript/dateValidation.js" type="text/javascript"></SCRIPT>
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
		#codescontainer {position:absolute;left:11em;width:9%}
		#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
		#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#codescontainer ul {padding:5px 0;width:80%;}
		#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
		#codescontainer li.yui-ac-highlight {background:#ff0;}
		#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
	</style>
	<style type="text/css">
		#codescontainer1 {position:absolute;left:11em;width:9%}
		#codescontainer1 .yui-ac-content {position:absolute;width:80%;border:1px solid	#404040;background:#fff;overflow:hidden;z-index:9050;}
		#codescontainer1 .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#codescontainer1 ul {padding:5px 0;width:80%;}
		#codescontainer1 li {padding:0 5px;cursor:default;white-space:nowrap;}
		#codescontainer1 li.yui-ac-highlight {background:#ff0;}
		#codescontainer1 li.yui-ac-prehighlight {background:#FFFFCC;}
	</style>
<%

Set tpSet = new HashSet();
String type=null;

%>

<script>
var codeObj;
var yuiflag1 = new Array();
var yuiflag = new Array();
var delLength=new Array();

function loadData()
{
	var link = "<c:url value='/commonyui/egov/yuiDataAjax.jsp?applXmlName=egi_sqlconfig.xml&xmlTagName=egi-position' />";
	
	var req = initiateRequest();
	req.onreadystatechange = function()
	{
		if (req.readyState == 4)
		{
			if (req.status == 200)
			{
				var values = req.responseText.split("^");
				var result  = values[0];	
				var resultArray = result.split("+");
		
				codeObj = new YAHOO.widget.DS_JSArray(resultArray);	
			}
		}
	};
	req.open("GET", link, true);
	req.send(null);
}

function loadPositionFrom(obj,event)
{
 	// set position of dropdown
 	var src = obj;
 	var target = document.getElementById('codescontainer');
 	var posSrc=findPos(src);
 	target.style.left=posSrc[0];
 	target.style.top=posSrc[1]+20;
	if(obj.name=='posFrom') target.style.left=posSrc[0]+0;

 	target.style.width=400;
	
 	var currRow=getRow(obj);
	var coaCodeObj = obj;
 	if(yuiflag1[currRow.rowIndex] == undefined)
 	{
	 	//40 --> Down arrow, 38 --> Up arrow
	 	if(event.keyCode != 40 )
	 	{
	 		if(event.keyCode != 38 )
	 		{ 
				var oAutoComp1 = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', codeObj);
				oAutoComp1.queryDelay = 0;
				oAutoComp1.useShadow = true;
				oAutoComp1.maxResultsDisplayed = 15;
	 			oAutoComp1.useIFrame = true;
	 		}
	 	}
 		yuiflag1[currRow.rowIndex]=1;
  	}
}

function fillNeibrAfterSplit(obj,neibrObjName)
{
	markYuiflagUndefined(1);
	var currRow=getRow(obj);
	neibrObj1=getControlInBranch(currRow,neibrObjName);
	var temp = obj.value;
	temp = temp.split("`-`");
	
	var xyz = getControlInBranch(currRow,'posFrom');
	xyz.value=temp[0];

	if(temp[1]==null && (neibrObj1.value!='' || neibrObj1.value!=null) ) {  
	
	return ;}
	else {
			
			neibrObj1.value=temp[1];


	}
}
function loadPositionTo(obj,event)
{

	var src = obj;
	var target = document.getElementById('codescontainer1');
	var posSrc=findPos(src);
	target.style.left=posSrc[0];
	target.style.top=posSrc[1]+20;
	
	//if(obj.name=='posTo') target.style.left=posSrc[0]+0;

	target.style.width=400;
	//doAutoComplete(1,obj,'codescontainer1',codeObj,15);
	var currRow=getRow(obj);
	var coaCodeObj = obj;
 	if(yuiflag[currRow.rowIndex] == undefined)
 	{
	 	//40 --> Down arrow, 38 --> Up arrow
	 	if(event.keyCode != 40 )
	 	{
	 		if(event.keyCode != 38 )
	 		{ 
				var oAutoComp1 = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer1', codeObj);
				oAutoComp1.queryDelay = 0;
				oAutoComp1.useShadow = true;
				oAutoComp1.maxResultsDisplayed = 15;
	 			oAutoComp1.useIFrame = true;
	 		}
	 	}
 		yuiflag[currRow.rowIndex]=1;
  	}

}
function fillNeibrAfterSplit1(obj,neibrObjName)
{
	markYuiflagUndefined(1);
	var currRow=getRow(obj);
	neibrObj1=getControlInBranch(currRow,neibrObjName);
	var temp = obj.value;
	temp = temp.split("`-`");
	var xyz = getControlInBranch(currRow,'posTo');
	xyz.value=temp[0];
	/*if(temp[1]==null || temp[1]=="") 
	{ 
	neibrObj1.value=""; 
	return; 
	}*/
	
	if(temp[1]==null && (neibrObj1.value!='' || neibrObj1.value!=null) ) 
	{  
	return;
	}
	else 
	{
		
		neibrObj1.value=temp[1];

	}	

}



function checkMode()
{
	var target="<%=(request.getAttribute("alertMessage"))%>";
	if(target!="null")
	{
		alert("<%=request.getAttribute("alertMessage")%>");

	}
}
function checkInput()
{
	
	if(document.getElementById("Id").value == 0)
	{
			alert('<bean:message key="alertSelObjectType"/>');
			return false;
	}

}
function addTitleAttributes(obj)
   {
	 
      var objName = obj.name;
      numOptions = document.getElementById(objName).options.length;
      for (i = 0; i < numOptions; i++)
         document.getElementById(objName).options[i].title =
               document.getElementById(objName).options[i].text;
   }
</script>
</head>

<body onload="checkMode();loadData();" >


<html:form  action="commons/BeforePositionHeirarchyMasterAction?submitType=setIdForDetails" onsubmit="return checkInput()">

		<div class="formmainbox"><div class="insidecontent">
		<div class="rbroundbox2">
		<div class="rbtop2"><div></div></div>
		<div class="rbcontent2">
		<div class="datewk"></div>
<table  id="table3" WIDTH=95%  cellpadding ="0" cellspacing ="0" border = "0">
 <tr>
 <td ></td>
</tr>
<tr>
<td class="whiteboxwk"><span class="mandatory">*</span>Choose ObjectType&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td class="whitebox2wk"  >
	<select style="selectwk" name="Id" id="Id" onMouseOver="addTitleAttributes(this);" onChange="loadPositionFrom(this,event);" >
	<option value='0'>----choose----</option>
		<%

			Map objTypeMap = (Map)session.getAttribute("objTypeMap");
			System.out.println("objTypeMap.size()  "+objTypeMap.size());
		for (Iterator it = objTypeMap.entrySet().iterator(); it.hasNext(); )
		{
			Map.Entry entry = (Map.Entry) it.next();
		%>
		<option value='<%= entry.getKey().toString() %>'><%=entry.getValue()%></option>

		<%
		}
		%>



	</select>

</td>
</tr>
 <tr>

   <td class="greyboxwk" >Position From </td>
   <td class="greybox2wk">
   <input type="hidden" name="positionFrom" id="positionFrom" >
    <input type="text" id="posFrom"  name="posFrom"  class = "selectwk" autocomplete="off"
	onkeypress="loadPositionFrom(this,event);" onblur="fillNeibrAfterSplit(this,'positionFrom')"/></td>
</tr>
<tr>
   <td   class="whiteboxwk">Position To</td>

   <td class="whitebox2wk"> <input type="hidden" name="positionTo" id="positionTo" >
		<input type="text" id="posTo"  name="posTo"  class = "selectwk" autocomplete="off" 
		onkeypress="loadPositionTo(this,event);" onblur="fillNeibrAfterSplit1(this,'positionTo');"/></td>
  </tr>




<tr>
<td colspan="4" align="center"><div class="buttonholderwk">

</div>
</td>
</tr>

  <tr><table><tr><td><div id="codescontainer"></div></td>
 <td><div id="codescontainer1"></div></td></tr></table></tr>
<tr>
            <td><div align="right" class="mandatory">* Mandatory Fields</div></td>
          </tr>
</table>
</div>
		<div class="rbbot2"><div></div></div>
		</div>
		</div>
		</div>
		
		<div class="buttonholderwk">
		<html:submit value="Search" styleClass="buttonfinal"/>
		<input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close()"  />
		</div>
		
</html:form>
</body>
</html>