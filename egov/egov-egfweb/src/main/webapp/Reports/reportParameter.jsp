<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>


<tr>
		<td align="right"><div align="right" valign="center" class="labelcell" id="departmentLevel">Department</div></td>
		<td align="left" width="260" class="smallfieldcell">
			<SELECT class="fieldinput" id="departmentId" name="departmentId" exilListSource="departmentNameList" ></SELECT>
		</td>
		<td align="right"><div align="right" valign="center" class="labelcell" id="functionaryLevel">Functionary</div></td>
		<td align="left" width="260" class="smallfieldcell">
			<SELECT class="fieldinput" id="functionaryId" name="functionaryId" exilListSource="functionaryName" ></SELECT>
		</td>
		
	</tr>
	<tr><td align="right"><div align="right" valign="center" class="labelcell" id="functionLevel">Function &nbsp; </div></td>
                       
                        <td class=fieldcelldesc >
                        <input type="hidden" name="functionCodeId" id="functionCodeId" >
                        <input type="text" styleClass="fieldinputlarge"  id="functionCode" name="functionCode"  value=""  onkeyup="autocompletecodeFunction(this);"   onblur="fillNeibrAfterSplitFunction(this,'functionCodeId')" /><IMG   id=IMG1 onclick=openSearch1(this,'functionCodeId') src="/egi/resources/erp2/images/plus1.gif"  >
						</td><div id="codescontainer"></div>
	<td align="right"><div align="right" valign="center" class="labelcell" id="filedLevel">Field &nbsp;</div></td>
		<td align="left" width="260" class="smallfieldcell">
			<SELECT class="fieldinput" id="fieldId" name="fieldId" exilListSource="field_name"  style="width: 200px"></SELECT>
		</td>
	</tr>
	<head>
	<script type="text/javascript" src="../commonyui/build/yahoo/yahoo.js"></script>
<script type="text/javascript" src="../commonyui/build/dom/dom.js" ></script>
<script type="text/javascript" src="../commonyui/build/autocomplete/autocomplete-debug.js"></script>
<script type="text/javascript" src="../commonyui/build/event/event-debug.js"></script>
<script type="text/javascript" src="../commonyui/build/animation/animation.js"></script>

<link type="text/css" rel="stylesheet" href="../commonyui/build/reset/reset.css">
<link type="text/css" rel="stylesheet" href="../commonyui/build/fonts/fonts.css">


<style type="text/css">
	#codescontainer {position:absolute;left:11em;width:9%}
	#codescontainer .yui-ac-content {position:absolute;width:100%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
	#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:100%;background:#a0a0a0;z-index:9049;}
	#codescontainer ul {padding:5px 0;width:100%;}
	#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
	#codescontainer li.yui-ac-highlight {background:#ff0;}
	#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>
</head>
<SCRIPT LANGUAGE="javascript">
var yuiflag = new Array();
loadDropDownFuncNames();
function autocompletecodeFunction(obj)
 {
	
   			 // set position of dropdown
   			var src = obj;
		
   			var target = document.getElementById('codescontainer');
			
   			//target.style.position="absolute";
   			var posSrc=findPos(src);
		
   			target.style.left=posSrc[0];
			
   			target.style.top=posSrc[1]+25;
			
   			target.style.width=250;

   			var currRow=getRow(obj);
   			var coaCodeObj = getControlInBranch(currRow,obj.name);
			
   			//40 --> Down arrow, 38 --> Up arrow
   		if(yuiflag[currRow.rowIndex] == undefined)
   		{ 
   			if(event.keyCode != 40 )
   			{
   				if(event.keyCode != 38 )
   				{
   						var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', funcObj);
						
   						oAutoComp.queryDelay = 0;
   						oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
   						oAutoComp.useShadow = true;
   						oAutoComp.maxResultsDisplayed = 15;
						
   						oAutoComp.useIFrame = true;
						
   				}
   			}
   		yuiflag[currRow.rowIndex] = 1;
   		}
}
function fillNeibrAfterSplitFunction(obj,neibrObjName) 
 {
 	if(obj.value==''){
		document.getElementById("functionCodeId").value='';
		return;
	}
 	var temp = obj.value; 
 	temp = temp.split("`~`");
 	obj.value=temp[0];
	var currRow=getRow(obj);
	yuiflag[currRow.rowIndex] = undefined;
	neibrObj=getControlInBranch(currRow,neibrObjName);
 	if(temp.length==1)
 	{
		var url="../commons/Process.jsp?type=validateFunctionName&functionName="+obj.value;
		var req3 = initiateRequest();
		req3.onreadystatechange = function()
		{
			  if (req3.readyState == 4)
			  {
				  if (req3.status == 200)
				  {
					var codes2=req3.responseText;
					var a = codes2.split("^");
					var codes = a[0];
					codes = codes.split("`-`");
					if(codes.length>1)
					{
						neibrObj.value=codes[1];
					}
					else
					{
						bootbox.alert('Invalid Code\nPlease use autocomplete option');
						neibrObj.value='';
						obj.value='';
						return;
					}
				 }
			}
		};
		req3.open("GET", url, true);
		req3.send(null);
 	}
 	else
 	{
		if(obj.value!='' && temp.length<2)
		{
			bootbox.alert('Invalid Code\nPlease use autocomplete option');
			neibrObj.value='';
			obj.value='';	
			return;
		}
		if(temp[1]==null) return; else 	neibrObj.value = temp[1];
	}
 }
 	function funClear(obj, tableName)
		 {
		   if(tableName.toLowerCase() == 'chartofaccounts')
			{
				var v =getControlInBranch(obj.parentNode.parentNode,'function_code')
				var w =getControlInBranch(obj.parentNode.parentNode,'cv_fromFunctionCodeId')
				if(v.value=="")
				{
				v.value = "";
				w.value = "";
				}
			}
		}
function loadDropDownFuncNames()
	 {

	 	var type='getAllFunctionName';
		var url = "../commons/Process.jsp?type=" +type+ " ";
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
					funcArray=codes.split("+");
					funcObj= new YAHOO.widget.DS_JSArray(funcArray);


				  }
			  }
		};
		req2.open("GET", url, true);
		req2.send(null);
	 }

function openSearch1(obj, tableName){
		var a = new Array(2);
		var sRtn;
		var str="";
		
		str = "../HTML/Search.html?tableNameForCode=function";
		sRtn= showModalDialog(str,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
		if(sRtn != '')
		{
				a = sRtn.split("`~`");
				getControlInBranch(obj.parentNode,'functionCodeId').value=a[2];
				getControlInBranch(obj.parentNode,'functionCode').value=a[1];
	
		}
  }
</SCRIPT>
