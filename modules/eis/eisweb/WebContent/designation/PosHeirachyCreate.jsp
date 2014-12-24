
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
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Create Position Hierarchy</title>

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
		#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid	#404040;background:#fff;overflow:hidden;z-index:9050;}
		#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:100%;background:#a0a0a0;z-index:9049;}
		#codescontainer ul {padding:5px 0;width:80%;}
		#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
		#codescontainer li.yui-ac-highlight {background:#ff0;}
		#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
	</style>
	<style type="text/css">
		#codescontainer1 {position:absolute;left:11em;width:9%}
		#codescontainer1 .yui-ac-content {position:absolute;width:80%;border:1px solid	#404040;background:#fff;overflow:hidden;z-index:9050;}
		#codescontainer1 .yui-ac-shadow {position:absolute;margin:.3em;width:100%;background:#a0a0a0;z-index:9049;}
		#codescontainer1 ul {padding:5px 0;width:80%;}
		#codescontainer1 li {padding:0 5px;cursor:default;white-space:nowrap;}
		#codescontainer1 li.yui-ac-highlight {background:#ff0;}
		#codescontainer1 li.yui-ac-prehighlight {background:#FFFFCC;}
</style>
<%
String id = "";
if(request.getParameter("Id")!=null && !request.getParameter("Id").equals(""))
	id=request.getParameter("Id").trim();

else
   id=(String)session.getAttribute("objId");
System.out.println("the value of t he id >>>>> " + id);
Set tpSet = new HashSet();
String type=null;
CommonsServiceImpl commonsService=new CommonsServiceImpl();
ObjectType objectType = commonsService.getObjectTypeById(new Integer(id));
//tpSet =genericManager.getSetOfPositionHeirarchyForObjTypeId(new Integer(id));
tpSet=(Set)request.getAttribute("tpSetList");
if(tpSet==null || tpSet.isEmpty())
{
	System.out.println("the tpSet1111111111 >>>>> " + tpSet);
	type="create";
}

if(tpSet==null || tpSet.isEmpty())
	tpSet.add(new PositionHeirarchy());
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

System.out.println("the tpSet >>>>> " + tpSet.size());


%>
</head>


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
function checkMode()
{
	var target="<%=(request.getAttribute("alertMessage"))%>";
	if(target!="null")
	{
		alert("<%=request.getAttribute("alertMessage")%>");

	}
}
function autocompletecode(obj,event)
{
 	// set position of dropdown
 	var src = obj;
 	var target = document.getElementById('codescontainer');
 	var posSrc=findPos(src);
 	target.style.left=posSrc[0];
 	target.style.top=posSrc[1]+25;
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
function autocompletecode1(obj,event)
{
	var src = obj;
	var target = document.getElementById('codescontainer1');
	var posSrc=findPos(src);
	target.style.left=posSrc[0];
	target.style.top=posSrc[1]+20;
	if(obj.name=='posTo') target.style.left=posSrc[0]+0;

	target.style.width=400;
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

function goindex(arg)
{

	if(arg == "Index")
	{

		document.forms("posForm").action = "${pageContext.request.contextPath}/index.jsp";
		document.forms("posForm").submit();
	}


}

function addRow(obj,tableName,row)
{
	if(checkUnique()  ==  false)
	{
		return false;
	}
	var tbl=tableName;
	var rowO=tbl.rows.length;
	var name=obj.alt;

	var tname = "resetRowValues"+name;
	if(row != null)
	{
		var tbody=tbl.tBodies[0];
		var lastRow = tbl.rows.length;
		rIndex = 0;
		var rowObj = row.cloneNode(true);
		//rowObj.document.getElementById('positionHeirId').value = 0;
		rIndex = rowObj.rowIndex;
		tbody.appendChild(rowObj);
		getControlInBranch(tbl.rows[tbl.rows.length-1],'positionHeirId').value=0;
		resetRowValues(lastRow,name);
		
	}
	else
	{
		alert("row Object is null");
		return false;
	}
}

function resetRowValues(lastRow,name)
{
	if(name=="Add")
	{
		var tbl= document.getElementById("TPTable");
		var rows= tbl.rows.length;
		<%
		String modeonAdd=(String)(request.getAttribute("pos"));
	    if(modeonAdd!=null && !modeonAdd.equals(""))
	    {
			if(modeonAdd.equalsIgnoreCase("positionTo"))
			{
		%>
				//make position from next row empty
				document.posForm.positionFrom[lastRow-1].value="";
				document.posForm.posFrom[lastRow-1].value="";
	
				var posTo=getControlInBranch(tbl.rows[rows-2],'posTo');
				var posToID=getControlInBranch(tbl.rows[rows-2],'positionTo');alert("posTo.value=="+posTo.value);
				document.posForm.posTo[lastRow-1].value=posTo.value;
				document.posForm.positionTo[lastRow-1].value=posToID.value;
			<%
			}
			else if(modeonAdd.equalsIgnoreCase("positionFrom"))
			{
			%>
				document.posForm.positionFrom[lastRow-1].value="";
				document.posForm.positionTo[lastRow-1].value="";
				document.posForm.posFrom[lastRow-1].value="";
				document.posForm.posTo[lastRow-1].value="";
			<%
			} 
		}
		else
		{
		%>
			document.posForm.positionFrom[lastRow-1].value="";
			document.posForm.positionTo[lastRow-1].value="";
			document.posForm.posFrom[lastRow-1].value="";
			document.posForm.posTo[lastRow-1].value="";
		<%
		}
		%>
    }
}
var firstLength = 0;
var rIndex = 0;
var i=0;
  function setLength()
  {
 	 var tbl=document.getElementById('TPTable');
    	var rowo=tbl.rows.length;
  	firstLength = rowo;

  }
  function deleteRow(obj,tableName)
  {
      var tbl=tableName;
      var rowo=getRow(obj).rowIndex;     
      if(tbl.rows.length == 2)
  	{
  		if("<%=type%>"=="create" || getControlInBranch(tbl.rows[rowo],'positionHeirId').value==0)
  		{
  		
  		       
  			document.posForm.positionFrom.value="";
			document.posForm.positionTo.value="";
			document.posForm.posFrom.value="";
			document.posForm.posTo.value="";
			alert("This row can't be deleted");
			
  		}
  		else
  		{
  	
			delLength[i] = getControlInBranch(tbl.rows[rowo],'positionHeirId').value;
			getControlInBranch(tbl.rows[rowo],'positionFrom').value="";
			getControlInBranch(tbl.rows[rowo],'positionTo').value="";
			getControlInBranch(tbl.rows[rowo],'posFrom').value="";
			getControlInBranch(tbl.rows[rowo],'posTo').value="";
			getControlInBranch(tbl.rows[rowo],'positionHeirId').value="0";
			
			i++;
		}

  		return true;
  	}
  	else
  	{
		
		if("<%=type%>"=="create" || (getControlInBranch(tbl.rows[rowo],'positionHeirId')!=null &&
			getControlInBranch(tbl.rows[rowo],'positionHeirId').value==0))
			
  		{
                       
  			tbl.deleteRow(rowo);
  			return true;
  		}
  		else
  		{
  			delLength[i] = getControlInBranch(tbl.rows[rowo],'positionHeirId').value;
  			//alert("del Length of i value"+delLength[i]);
  			document.posForm.positionHeirId.value="0";
			i++;
			tbl.deleteRow(rowo);
  			return true;
  		}
  		
		
  	}
  }



function validateNotEmpty( strValue )
{
   var strTemp = strValue;
   strTemp = trimAll(strTemp);
   if(strTemp.length > 0)
   {
      return true;
   }
   return false;
 }



function checkUnique()
{
	
	var tbl= document.getElementById("TPTable");
	var rowLength= tbl.rows.length;
	
    if(rowLength>2)
	{
		for(var i=0 ;i<rowLength-1;i++)
		{

			if(document.posForm.posFrom[i].value=="" && document.posForm.posTo[i].value!="" )
			{
				alert('<bean:message key="alertEnterPosFrom"/>');
				document.posForm.posFrom[i].focus();
				return false;
			}
	
			if(document.posForm.posTo[i].value=="" && document.posForm.posFrom[i].value!="")
			{
				alert('<bean:message key="alertEnterPosTo"/>');
				document.posForm.posTo[i].focus();
				return false;
			}
	
			/**if(document.posForm.posFrom[i].value!="" &&
			document.posForm.posTo[i].value !="" && 
			document.posForm.posFrom[i].value==document.posForm.posTo[i].value)
			{
				
				alert('<bean:message key="alertPosFromPosToNotSame"/>');
				return false;
			}**/
					
			if(document.posForm.positionFrom[i].value=="")
			{
				alert('<bean:message key="alertNoPosFrom"/>');
				document.posForm.posFrom[i].focus();
				return false;
			}
			if(document.posForm.positionTo[i].value=="")
			{
				alert('<bean:message key="alertNoPosTo"/>');
				document.posForm.posTo[i].focus();
				return false;
			}
	
	    	var posFrom = document.posForm.posFrom[i];
			var posTo = document.posForm.posTo[i];
		}

	}
	else
	{
       	var posFromVal=getControlInBranch(tbl.rows[rowLength-1],'posFrom').value;
		var posToVal=getControlInBranch(tbl.rows[rowLength-1],'posTo').value;
        if(getControlInBranch(tbl.rows[rowLength-1],'posFrom').value=="" && getControlInBranch(tbl.rows[rowLength-1],'posTo').value!="")
		{
			alert('<bean:message key="alertEnterPosFrom"/>');
			getControlInBranch(tbl.rows[rowLength-1],'posFrom').focus();
			return false;
		}
		if(getControlInBranch(tbl.rows[rowLength-1],'posTo').value=="" && getControlInBranch(tbl.rows[rowLength-1],'posFrom').value!="")
		{
			alert('<bean:message key="alertEnterPosTo"/>');
			getControlInBranch(tbl.rows[rowLength-1],'posTo').focus();
			return false;
		}

		/**if(getControlInBranch(tbl.rows[rowLength-1],'posFrom').value!="" && 
		getControlInBranch(tbl.rows[rowLength-1],'posTo').value!="" && getControlInBranch(tbl.rows[rowLength-1],'posFrom').value!=undefined && 
		getControlInBranch(tbl.rows[rowLength-1],'posFrom').value==getControlInBranch(tbl.rows[rowLength-1],'posTo').value)
		{
			alert('<bean:message key="alertPosFromPosToNotSame"/>');
			getControlInBranch(tbl.rows[rowLength-1],'posFrom').focus();
			return false;
		}**/
		
		if(getControlInBranch(tbl.rows[rowLength-1],'posFrom').value!="")
		{
			if(getControlInBranch(tbl.rows[rowLength-1],'positionFrom').value=="")
			{
				alert('<bean:message key="alertNoPosFrom"/>');
				getControlInBranch(tbl.rows[rowLength-1],'posFrom').focus();
				return false;
			}
		}
		if(getControlInBranch(tbl.rows[rowLength-1],'posTo').value!="")
		{
			if(getControlInBranch(tbl.rows[rowLength-1],'positionTo').value=="")
			{
				alert('<bean:message key="alertNoPosTo"/>');
				getControlInBranch(tbl.rows[rowLength-1],'posTo').focus();
				return false;
			}
		}
	}
	
	return true;

}


//HAS TO BE REMOVED?
function getCombo(obj1, obj2)
{
	//alert("Inside getCombo");

	var combo = "";

	if(obj1 !=null && obj1.value != null)
	{
			combo = combo + trimText(obj1.value).toUpperCase();
			//alert("obj1.value="+obj1.value);
	}
	if(obj2 != null && obj2.value != null)
	{
		combo = combo+','+ trimText(obj2.value).toUpperCase();
		//alert("obj2.value="+obj2.value);
	}
	if(combo != null)
	{
		//alert("combo = "+combo);
		return combo;
	}
	else
		return null;


}

function ButtonPressNew(arg)
{
	if(arg == "savenew")
	{

		if(checkUnique()  ==  false)
		{
			return false;
		}
		var len = 	delLength.length
		//alert("del length"+len);
		for(var i=0;i<len;i++)
		{

			if(delLength[i]!=null && delLength[i]!="" && document.forms[0].deletePositionSet.length>0)
			{
				document.forms[0].deletePositionSet[i].value=delLength[i];
				//alert("success >> " +document.forms[0].deletePositionSet[i].value);

			}
			else if(delLength[i]!=null && delLength[i]!="")
			{
			//alert("delete set id"+document.forms[0].deletePositionSet.value);
				document.forms[0].deletePositionSet.value=delLength[i];
			}

		}

		document.forms[0].submit();
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

   
   function uniquePositionFrom(obj)
   {
  
        var scripts = new Array();
   		var tbl= document.getElementById("TPTable");
   		var rowLength= tbl.rows.length;
		var row=getRow(obj);
		var posFrom = getControlInBranch(row,"posFrom");
		var posFromVal = posFrom.value;
		 var rowIndex = row.rowIndex-1;
		 
    	for(i=0;i<rowLength-1;i++)
    	{
    	   if(rowLength>2)
    	   {
    	      
    	      if(i!=(rowIndex))
    	     {
    	        
    	        scripts.push(document.posForm.posFrom[i].value);
    	     }
    	   }
    	   else
    	   {
    	    	if(i!=(rowIndex))
    	     	{
    	      		scripts.push(getControlInBranch(tbl.rows[rowLength-1],'posFrom').value);
    	      }
    	   }
    	   
    	  
    	   
    	}
		
		
		
		if(scripts.contains(posFromVal))
		{
		    alert('<bean:message key="alertForSamePosition"/>');
			posFrom.value = "";
			posFrom.focus();
			return false;
		}
   }
   

Array.prototype.contains = function (element) {
	for (var i = 0; i < this.length; i++) {
		if (this[i] == element) {
		return true;
		}
	}
	return false;
}


</script>
<body onLoad ="setLength();loadData();checkMode()">

<html:form  action="commons/PositionHeirarchyMasterAction?submitType=saveDetails" >

		<div class="formmainbox"><div class="insidecontent">
		<div class="rbroundbox2">
		<div class="rbtop2"><div></div></div>
		<div class="rbcontent2">
		
<table width="95%" cellpadding ="0" cellspacing ="0" border = "0" >
 <tr>
 <td>
<input type=hidden name="objId" id="objId" value="<%=id%> " />

  <table  width="95%" cellpadding ="0" cellspacing ="0" border = "0">
   <tbody>

				<tr>
				<td>
				<td>&nbsp;&nbsp;&nbsp;<b>Object Type : </b>"<%=objectType.getType()%>"</td>
				</td>
				
				</tr>

</tbody>

</table>
<%
 if(tpSet != null && !tpSet.isEmpty())
 {
 System.out.println("ggggggggggggg");
 %>
 <br>
 <table width="95%" cellpadding ="0" cellspacing ="0" border = "0" id="TPTable" name="TPTable" >
   <tbody>
   <tr>
<td   class="greyboxwk" >
   
    <td   class="greybox2wk" align="left">
   <SPAN class="mandatory">*</SPAN><bean:message key="positionFrom"/>
   </td>

   <td   class="greybox2wk">
   <SPAN class="mandatory">*</SPAN><bean:message key="positionTo"/>
   
   <td   class="greybox2wk">
   ADD/DEL
  
</td>
  </tr>


 <%
 	Iterator itr1 = tpSet.iterator();
 	Position positionFrom = null;
 	Position positionTo = null;
 	int posFrom = 0;
 	int posTo = 0;
	for(int i=0;itr1.hasNext();i++)
	{

		PositionHeirarchy positionHeirarchy = (PositionHeirarchy)itr1.next();
		positionFrom = positionHeirarchy.getPosFrom();
		positionTo = positionHeirarchy.getPosTo();
		if(positionFrom!=null)
			posFrom =positionFrom.getId().intValue();
		if(positionTo!=null)
					posTo =positionTo.getId().intValue();


 %>
	<tr id="TPRow" width="95%">
	
	<td class="whiteboxwk">
	<input type ="hidden" name="positionHeirId" id="positionHeirId" value="<%=positionHeirarchy.getId()==null?
	"0":positionHeirarchy.getId().toString()%>" />
	<%Map positionMap =(Map)session.getAttribute("positionMap");
	String hit ="false";
	if(positionMap.size()!=0)
	{
		for (Iterator it = positionMap.entrySet().iterator(); it.hasNext(); )
		{
			Map.Entry entry = (Map.Entry) it.next();
			if(((Integer)entry.getKey()).intValue() ==posFrom){

				hit="true";%>
			<input type="hidden" name="positionFrom" id="positionFrom" value="<%=positionFrom.getId()%>">
			<td class ="whitebox2wk">
             <input type="text" id="posFrom"  name="posFrom" value="<%= entry.getValue() %>" 
 autocomplete="off" onkeyup="autocompletecode(this,event);" 
 class="selectwk" onblur="fillNeibrAfterSplit(this,'positionFrom');uniquePositionFrom(this);"/>
</td>
			<%}

		}

	}

	 if(hit=="false")
	{%>
	<input type="hidden" name="positionFrom" id="positionFrom" >
	<td class ="whitebox2wk">
	<input type="text" id="posFrom"  name="posFrom"  class="selectwk" autocomplete="off" onkeyup="autocompletecode(this,event);"
	onblur="fillNeibrAfterSplit(this,'positionFrom');uniquePositionFrom(this);"/>
	<%}%>


	</td>


		<%String hit1 ="false";
		if(positionMap.size()!=0)
		{
			for (Iterator it = positionMap.entrySet().iterator(); it.hasNext(); )
			{
				Map.Entry entry = (Map.Entry) it.next();
				if(((Integer)entry.getKey()).intValue() ==posTo){
					hit1="true";%>
				<input type="hidden" name="positionTo" id="positionTo" value="<%=posTo%>" >
				<td class="whitebox2wk" >
				<input type="text" id="posTo"  name="posTo" value="<%= entry.getValue() %>" 
				class="selectwk" autocomplete="off" onkeyup="autocompletecode1(this,event);" onblur="fillNeibrAfterSplit1(this,'positionTo');"/>
				
				<td class="whiteboxwk" ><p>
				<div align="left"><a href="#"><img src="../common/image/add.png" alt="Add" width="16" height="16" border="0"
				onclick="javascript:addRow(this,document.getElementById('TPTable'),document.getElementById('TPRow'))"  /></a>
				<a href="#"><img src="../common/image/cancel.png" alt="Del" width="16" height="16" border="0"
				onclick="javascript:deleteRow(this,document.getElementById('TPTable'))"/></a></div>
				</td> 

				<!--<td>
				<input class="button2" id="deltp" name="deltp" class="selectwk"
				type="button" value="-" onclick="javascript:deleteRow(this,document.getElementById('TPTable'),document.getElementById('deltp'))" >
				</td>-->

				<%}
			}

		}
		if(hit1=="false")
		{%>
		<input type="hidden" name="positionTo" id="positionTo" >
		<td class="whitebox2wk">
		<input type="text" id="posTo"  name="posTo"  class="selectwk"
		autocomplete="off" onkeyup="autocompletecode1(this,event);" onblur="fillNeibrAfterSplit1(this,'positionTo');"/>
		</td>
                 <td class="whitebox2wk" align="left"><p>
					<div align="center"><a href="#"><img src="../common/image/add.png" alt="Add" width="16" height="16" border="0"
					onclick="javascript:addRow(this,document.getElementById('TPTable'),document.getElementById('TPRow'))" /></a>
					<a href="#"><img src="../common/image/cancel.png" alt="Del" width="16" height="16" border="0"
					onclick="javascript:deleteRow(this,document.getElementById('TPTable'),document.getElementById('deltp'))"/></a></div>
					</td> 
									 
				 <!--<td>
		 				<input class="button2" id="deltp" name="deltp" class="selectwk"
						type="button" value="-" onclick="javascript:deleteRow(this,document.getElementById('TPTable'),document.getElementById('deltp'))" >
				</td>-->
		<%}%>

			</td>
	</tr>
	
<input type="hidden" id="deletePositionSet" name="deletePositionSet" />
	
<%
}%>
<%}%>

</table>
 <tr>

 <table><tr><td><div id="codescontainer"></div></td>
 <td><div id="codescontainer1"></div></td></tr></table></tr>
</tbody>
</table>

<table width="95%" cellpadding ="0" cellspacing ="0" border = "0" >
<tr>

   <td> 
   <!--<input class="buttonfinal" id="addtpBtn" name="addtpBtn"  type="button" value="AddRow" 
   onclick="javascript:addRow(this,document.getElementById('TPTable'),document.getElementById('TPRow'))">-->
   </td>
   
   </tr>
   <tr>
  	<td  colspan="4" align="left">
  		
  	</td>

</tr>
</html:form>
</table>
</td>
</tr>
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
		<html:button styleClass="buttonfinal" value="Save" property="b2" onclick="ButtonPressNew('savenew');" />
		<input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close();"  />
		  			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</div>
		

</body>
