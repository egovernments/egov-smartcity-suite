<!--
	Program Name : searchForTempAssignment.jsp
	Author		: DivyaShree M.S
	Created	on	: 03-11-2009
	Purpose 	: To Get Report For Employee's with Temporary Assignment
 -->
 
 <%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
 <%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ page import="java.util.*"%>
<style>
#warning {
  display:none;
  color:blue;
}

.mandatoryone{
color:red;
font-weight:normal;
}

.mandatoryTwo{
color:green;
font-weight:normal;
}

.arrow{
	
}

#yui-dt0-bodytable {
Width:100%;
}



</style>

<html>  
	<head>  
	<center>
	    <title>eGov- Employee Temporary Assignment </title>
	   
		<style type="text/css">
				#codescontainer {position:absolute;left:11em;width:9%}
				#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
				#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
				#codescontainer ul {padding:5px 0;width:80%;}
				#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
				#codescontainer li.yui-ac-highlight {background:#ff0;}
				#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
		</style>

		<style>div.errorstyle {
font-size: 10px;
font-weight: bold;
text-align: left;
margin-bottom: 10px;
padding:10px;
background-color:#FFFFEE;
font-family: Arial, Helvetica, sans-serif;
border: 1px solid #EEEE00;
		}
</style>

    
<script>


var dom=YAHOO.util.Dom;
var yuiflag1 = new Array();
var selectedEmpCode;

function setFocus(obj,defaultval)
{
	if(obj.value==defaultval ){
		document.getElementById(obj.id).value="";
		document.getElementById(obj.id).style.color='black';
		}
}
function setBlur(obj,defaultval)
{
	if(obj.value == ""){
		document.getElementById(obj.id).value=defaultval;
		document.getElementById(obj.id).style.color='';
	}
}

function methodTest()
{
if(document.getElementById("fromDate").value=="dd/mm/yyyy"){
document.getElementById("fromDate").value="";


}




}


function populatePosition()
{
	loadPosition();
}
function initiateRequest() {
	if (window.XMLHttpRequest) {
		return new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		isIE = true;
		return new ActiveXObject("Microsoft.XMLHTTP");
	}
   }
function loadPosition()
{
		var type='getAllPosition';
		var url = "${pageContext.request.contextPath}/pims/searchPositionAjax.jsp?type=" +type;
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
		empCodeArray=codes.split("+");
		selectedEmpCode = new YAHOO.widget.DS_JSArray(empCodeArray);
		}
		}
		};
		req2.open("GET", url, true);
		req2.send(null);
}
function autocompleteEmpCode(obj,event)
		{
			
			// set position of dropdown
		var src = obj;
		var target = document.getElementById('codescontainer');
		var posSrc=findPos(src);
		target.style.left=posSrc[0];
		target.style.top=posSrc[1]+25;
		if(obj.name=='positionSearch') target.style.left=posSrc[0]+0;

		target.style.width=500;

		var currRow=getRow(obj);
		var coaCodeObj = obj;
		if(yuiflag1[currRow.rowIndex] == undefined)
		{
		//40 --> Down arrow, 38 --> Up arrow
		if(event.keyCode != 40 )
		{
		if(event.keyCode != 38 )
		{

		var oAutoComp1 = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', selectedEmpCode);
		oAutoComp1.queryDelay = 0;
		oAutoComp1.useShadow = true;
		oAutoComp1.maxResultsDisplayed = 15;
		oAutoComp1.useIFrame = true;
		}
		}
		yuiflag1[currRow.rowIndex]=1;
		}
		}
		function getControlInBranch(obj,controlName)
		{
			if (!obj || !(obj.getAttribute)) return null;
			if (obj.getAttribute('name') == controlName) return obj;

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
		function fillNeibrAfterSplit(obj,neibrObjName)
		{
				var currRow=getRow(obj);	
				yuiflag1[currRow.rowIndex] = undefined;
				//neibrObj=getControlInBranch(currRow,neibrObjName);
				var temp = obj.value;
				temp = temp.split("`-`");
				
				document.getElementById("positionName").value=temp[0];

				if(temp[0]==''){return}
				else
				{
					document.getElementById("positionID").value=temp[1];
				}
					
		}
		function removeValue(obj)
		{
			
			if(obj.value!=null && obj.value=='')
			{
				
				document.getElementById("positionID").value='';
				
			}
		}
		
		
var employeeDataTable;
var makeAssignmentReportDataTable = function() {
var cellEditor=new YAHOO.widget.TextboxCellEditor()
var employeeReportColumnDefs = [
{key:"slNo",label:'Sl No', sortable:true, resizeable:false},
{key:"employeeCode",label:'Employee code', sortable:true, resizeable:false},
{key:"employeeName", label:'Employee name', sortable:true, resizeable:false},
{key:"Designation",label:'Designation', sortable:true, resizeable:false},
{key:"Dept",label:'Department', sortable:true, resizeable:false},
{key:"fromDate", label:'From Date', sortable:true, resizeable:false},
{key:"toDate",label:'To Date', sortable:true, resizeable:false},
{key:"tempPos",label:'Temprorary Position', sortable:true, resizeable:false},
{key:"primaryPos",label:'Primary Position', sortable:true, resizeable:false}
];

var employeeDataSource = new YAHOO.util.DataSource();
employeeDataTable = new YAHOO.widget.DataTable("employeeViewTable",employeeReportColumnDefs, employeeDataSource,{MSG_EMPTY:""});
//employeeDataTable.subscribe("cellClickEvent", employeeDataTable.onEventShowCellEditor);

return {
oDS: employeeDataSource,
oDT: employeeDataTable
};
}
		


</script>

	    </head>
	    <body onload="populatePosition();">
		
				<div class="formmainbox">
					<div class="insidecontent">
				  		<div class="rbroundbox2">
							<div class="rbtop2">
								<div>
								</div>
							</div>
							<div class="rbcontent2">
								<div class="datewk">	
								     
								</div>	
		<span id="msg">
			<s:if test="%{hasErrors()}">
				<div class="errorstyle">
				<s:fielderror  cssClass="mandatoryone"/>
				<s:actionerror  cssClass="mandatoryone"/>
				</div>
			</s:if>
		</span>

		


		 <s:form action="searchForTempAssign" theme="simple" >  
		 <center>
		<table width="100%" cellpadding ="0" cellspacing ="0" border = "0"  >
<tbody>
<tr><td>&nbsp;</td></tr>
		<tr>
  <td colspan="8" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
  <p><div class="headplacer">Search Employee</div></p></td>
  </tr>

		<table width="95%" cellpadding="0" cellspacing="0" border="0" id="empTempTable">
		
		<tr>
		
			<td width="12%" class="whiteboxwk"><s:text name="valid.Date"/></td>
			<td  width="18%" class="whitebox2wk">
				<s:textfield cssClass="selectwk grey"  name="fromDate" id="fromDate" value="dd/mm/yyyy" onblur="setBlur(this,'dd/mm/yyyy');" onfocus="setFocus(this,'dd/mm/yyyy')" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
				<a href="javascript:show_calendar('forms[0].fromDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img  src="${pageContext.request.contextPath}/common/image/calendar.png"  border=0></a>
			</td>


			<!--new Type -->
			<td class="whiteboxwk"><s:text name="choose.Position"/></td>
			<!--old Type -->
			<td  class="whitebox2wk">
				<div id="codescontainer"></div>
				<div><s:textfield cssClass="selectwk"  name="positionSearch" id="positionName" size="20"  onkeyup="autocompleteEmpCode(this,event);" onblur="fillNeibrAfterSplit(this,'positionID');trim(this,this.value)" onChange="removeValue(this);"/>
				<s:hidden id="positionID" name="positionID"  /></div>
				 
             </td>




			</tr>

			<tr>
			<td width="16%" class="greyboxwk" >Employee Code:</td>
			<td  colspan="3" class="greybox2wk">
			<s:textfield name="employeeCode" id="employeeCode" cssClass="selectwk"/></td>
			</tr>


			
	
		</table>
		<br>

		<center><s:submit method="search" value="SEARCH" cssClass="buttonfinal" onclick="methodTest();"/></center>
		



		<s:if test="showList!=null && showList.size()>0">
		<br>
		<div style="overflow-x:scroll;width:78%;">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
 	   <tr>
		<td>
		
		
		<div class="yui-skin-sam" >
			<center><div id="employeeViewTable"></div></center>
		</div>
		
		</td>
		</tr>
</table>
</div>
</s:if>
	<script>
				makeAssignmentReportDataTable();
				
				<s:iterator id="showList" value="showList" status="row_status">
			
					employeeDataTable.addRow({
					slNo:'<s:property value="#row_status.count"/>',
					employeeCode:'<s:property value="empCode"/>',
					employeeName:'<s:property value="empFirstName"/>',
					Designation:'<s:property value="empDesgName"/>',
					Dept:'<s:property value="deptName"/>',
					fromDate:'<s:property value="fromDate"/>',
					toDate:'<s:property value="toDate"/>',
					tempPos:'<s:property value="tempPos"/>',
					primaryPos:'<s:property value="holdingPriPos"/>'});
          </s:iterator>
          </script>

<br>
		<center><s:actionmessage cssClass="mandatoryTwo"/></center>
		

		<br>
				</div>
				<div class="rbbot2"><div></div></div>
						</div>
					</div>
				</div>
				
			

				<div class="buttonholderwk">
					
					<input type="button" value="CLOSE" onclick="javascript:window.close()" class="buttonfinal"/> 
				</div>
			</center>
		 </s:form>

		 
	    </body>
	    </html>
	    