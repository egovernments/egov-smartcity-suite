<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<script type="text/javascript">

function isNumericFloors(value)
{
	if(eval(value)==0)
   	{
	   	dom.get("shop_error").style.display='';
    	document.getElementById("shop_error").innerHTML='No of Floors in building cannot be zero';
    	document.getElementById("floorCount").value="";
    	return false;
    }
     
    return true;
}
function loadFloorDetailJs(value)
{
  
	clearRows();
	 if(isNumericFloors(value))
	 {	  
		  
		var  objSelect=document.getElementById('floorNum');
	  	createDropdown(objSelect,value);
	}
} 

function clearRows()
{
	var table = document.getElementById('floorDetails');
	var rowslength = table.rows.length;
	while(rowslength>2) {
		table.deleteRow(rowslength-1);
		rowslength--;
		cmdaindex=1;
	}
	 document.getElementById('floorNum').options.length=1; 
		    
} 
function createDropdown(selectObj,value)
{
	
	var objOption=document.createElement("OPTION");
	objOption.text="Ground Floor";
	objOption.value=0;
	selectObj.options.add(objOption);
	var key,remainder;
	for(var i=1;i<value;i++)	
	{
	if(i>=10 && i<=20)
		key=i+"th";
	else
		{

		 remainder=i%10;
		 if(remainder == 1)
			 key = i+"st";
		 else if(remainder == 2)
			 key = i+"nd";
		 else if(remainder == 3)
			 key = i+"rd";	
		 else
			 key = i+"th";
		}
	objOption=document.createElement("OPTION");
	objOption.text = key;
	objOption.value =  i;

	selectObj.options.add(objOption);
			
			  
  } 
  	
}

function validateFloorDetail()
{

var tableObj=document.getElementById('floorDetails');
var lastRow = tableObj.rows.length;
var floorName,floorValue;
var i;
if(lastRow>2)
{
	
	for(i=0;i<lastRow-1;i++)
	{
    	
     	floorName=document.forms[0].floorNum[i].value;
     	floorValue=document.forms[0].floorValue[i].value;
     	
      	 if(!validateFloorLines(floorName,floorValue,i+1))
        	   return false;
 		}
		
		return true;
	}
	else
	{
	    floorName=document.getElementById('floorNum').value;
	    floorValue=document.getElementById('floorValue').value;
		
	    if(!validateFloorLines(floorName,floorValue,1))
	       return false;
	     else
	       return true;
	    
	}
}

function validateFloorLines(floorName,floorValue,row)
{
dom.get("shop_error").style.display='none';

	 	if(floorName=="" || floorName==-10)
 	{
	     	
    	document.getElementById("shop_error").innerHTML='';
			document.getElementById("shop_error").innerHTML='Enter Floor Name for row'+" :"+row;
			dom.get("shop_error").style.display='';
			return false;
    }
	if(floorValue=="" || floorValue==null)
         {
    	 	document.getElementById("shop_error").innerHTML='Enter Floor Value for row'+" :"+row;
			dom.get("shop_error").style.display='';
			return false;
         }
	
  
   return true;
}

function addFloor()
{   
	if(validateFloorDetail()){
		var tableObj=document.getElementById('floorDetails');
		var	cmdaindex=tableObj.rows.length-1;
		var tbody=tableObj.tBodies[0];
		var lastRow = tableObj.rows.length;
		var rowObj = tableObj.rows[1].cloneNode(true);
		tbody.appendChild(rowObj);
		var rowno = parseInt(tableObj.rows.length)-2;
		document.forms[0].srlNo[lastRow-1].value=tableObj.rows.length - 1;		
		document.forms[0].floorNum[lastRow-1].value="-10";
	    document.forms[0].floorValue[lastRow-1].value="";
	    
	  	document.forms[0].dockrtFlrListId[lastRow-1].value="";
	  
	  	document.forms[0].srlNo[lastRow-1].setAttribute("name","docketFloorDetail["+cmdaindex+"].srlNo");
	    document.forms[0].floorNum[lastRow-1].setAttribute("name","docketFloorDetail["+cmdaindex+"].floorNum");
		document.forms[0].floorValue[lastRow-1].setAttribute("name","docketFloorDetail["+cmdaindex+"].floorValue");
        
        document.forms[0].dockrtFlrListId[lastRow-1].setAttribute("name","docketFloorDetail["+cmdaindex+"].id");		    
		cmdaindex++;
	}	

   }


function delFloor(obj)
{
			var tb1=document.getElementById("floorDetails");
	        var lastRow = (tb1.rows.length)-1;
	        var curRow=getRow(obj).rowIndex;
	        dom.get("shop_error").style.display='none';
	        if(lastRow ==1)
	      	{
	     		 dom.get("shop_error").style.display='none';
	     		 document.getElementById("shop_error").innerHTML='This row can not be deleted';
	   			 dom.get("shop_error").style.display='';
	      	     return false;
	        }
	      	else
	      	{
	      		// alert(curRow);
	      		var updateserialnumber=curRow;
	 			for(updateserialnumber;updateserialnumber<tb1.rows.length-1;updateserialnumber++)
	 			{
	 				if(document.forms[0].srlNo[updateserialnumber]!=null)
	 					document.forms[0].srlNo[updateserialnumber].value=updateserialnumber;
	 			}
	 			
	 			tb1.deleteRow(curRow);
	 			return true;
	      }
	  
				
}
function validateIsNan(obj)
{
		 var iChars = "!@$%^*+=[']`~';{}|\":<>?#_";
	    if(obj!=null && obj.value!=null && isNaN(obj.value) )
	    {
	    	dom.get("shop_error").style.display='';
			document.getElementById("shop_error").innerHTML='Pleas Enter only Numbers';
			 dom.get(obj).value = "";  
		 return false;
	    }
	    if(obj!=null && obj.value < 0 ){
			//alert('pos '+ obj.value);
	    	dom.get("shop_error").style.display='';
			document.getElementById("shop_error").innerHTML='Pleas Enter only Positive Numbers';
			 dom.get(obj).value = "";  
			 return false;
		    }
	   
	 		for (var i = 0; i < obj.value.length; i++)
	 		{
			 if ((iChars.indexOf(obj.value.charAt(i)) != -1)||(obj.value.charAt(0)==" "))
	 		{
		 	dom.get("shop_error").style.display='';
	 		document.getElementById("shop_error").innerHTML='Special characters are not allowed';
	 		obj.value="";
	 		obj.focus();
	 		return false;
	 		}
	 		}
	  	 return true; 
	 }
</script>
<s:set name="theme" value="'simple'" scope="page" />
<s:hidden id="docket" name="docket" value="%{docket.id}"/>
<s:hidden id="docket.createdBy" name="docket.createdBy" value="%{docket.createdBy.id}"/>
<s:hidden id="docket.modifiedBy" name="docket.modifiedBy" value="%{docket.modifiedBy.id}"/>

<div align="center"> 
  <div id="docketSheetDiv" class="formmainbox">
  
    <div align="center" id="checklistdiv">
		
		<%@ include file="../inspection/inspectionExtn-commonDocketSheet.jsp"%>
		
		<h1 class="subhead" ><s:text name="inspectionlbl.docket.buildUpArea.demolition"/></h1>
	<table  width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom"><tr>	
	<td class="bluebox" width="20%">&nbsp;</td>
	<td class="bluebox"  id="mandatoryfields"><s:text name="Total No.Of Floors"/><span class="mandatory">*</span></td> 
	<td class="bluebox">&nbsp;</td>
	 <td class="bluebox" id="numbers" > <s:textfield id="floorCount" name="docket.floorCount" value="%{docket.floorCount}" maxlength="2" onchange="validateIsNan(this);checkZero(this,'floorCount');loadFloorDetailJs(value);" />
	   </td>
	   <td class="bluebox" width="20%">&nbsp;</td>
	   </tr>
	   </table>
	    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom" id="floorDetails" >
      
        <tr>
   			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		 	<th class="bluebgheadtd" width="3%"><s:text name="Floor Number" /></th>
          	<th class="bluebgheadtd" width="3%"><s:text name="Floor Value" /></th>
         	<!--<s:if test="%{mode != 'view'}">     --> 
     		<th class="bluebgheadtd" width="5%" id="AddRemoveFloorLabel">Add/ Delete</th>    	
    	<!--	</s:if>-->
        </tr>

   		<s:iterator value="docketFloorDetail" status="row_status" >
	   		<tr id="Floorinfo"   >
	     		<td  class="blueborderfortd"><s:textfield name="docketFloorDetail[%{#row_status.index}].srlNo" id="srlNo" readonly="true"  cssClass="tablerow" value="%{#row_status.count}" cssStyle="text-align:center"/></td>
				<td class="blueborderfortd"  align="centre"><s:select name="docketFloorDetail[%{#row_status.index}].floorNum" id="floorNum"  list="floorNoMap" listKey="key" listValue="value" headerKey="-10" headerValue="----Choose------" cssStyle="text-align:center"/></td>
	 			<td class="blueborderfortd" id="numbers" ><s:textfield name="docketFloorDetail[%{#row_status.index}].floorValue" id="floorValue" cssClass="tablerow" maxlength="10" onblur="validateIsNan(this);" cssStyle="text-align:center" /></td>
				<!--<s:if test="%{mode != 'view'}">   -->     
	            <td class="blueborderfortd"  id = "AddRemoveFloor" >
	                <img id="addF" name="addF" src="${pageContext.request.contextPath}/images/addrow.gif" alt="Add" onclick="javascript:addFloor(); return false;" width="18" height="18" border="0"  />
	                <img id="dDelF" name="dDelF" src="${pageContext.request.contextPath}/images/removerow.gif" alt="Remove" onclick="javascript:delFloor(this);return false;" width="18" height="18" border="0" />
	             </td>         	
	         <!-- 	</s:if>-->
	          	<s:hidden id="dockrtFlrListId" name="docketFloorDetail[%{#row_status.index}].id" />
	        </tr>
   		</s:iterator>
   	
    
</table>
 <table  width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" >
 <tr>
 <td class="greybox" width="20%">&nbsp;</td>
 <td class="greybox"  ><s:text name="inspectionlbl.docket.terraced"/></td>
<td class="greybox" id="numbers"><s:textfield  id="terraced" name="docket.terraced" value="%{docket.terraced}" onblur="validateIsNan(this);" maxlength="10"/></td>
<td class="greybox" >&nbsp;</td>
  <td class="greybox" ><s:text name="inspectionlbl.docket.tiledRoof"/></td>
	<td class="greybox" id="numbers"><s:textfield  id="tiledRoof" name="docket.tiledRoof" value="%{docket.tiledRoof}"  onblur="validateIsNan(this);" maxlength="10" /></td>
	 <td class="greybox" >&nbsp;</td>
 </tr>
<tr>
			<td class="bluebox" width="20%">&nbsp;</td>
			<td class="bluebox" id="mandatoryfields" ><s:text name="inspectionlbl.docket.lengthofcompound"/><span class="mandatory">*</span></td>
			<td class="bluebox" id="numbers"><s:textfield  id="lengthOfCompoundWall" name="docket.lengthOfCompoundWall" value="%{docket.lengthOfCompoundWall}" onblur="validateIsNan(this);" maxlength="10"/></td>
			<td class="bluebox" >&nbsp;</td>
			<td class="bluebox" id="mandatoryfields" ><s:text name="inspectionlbl.docket.diameterofWell"/><span class="mandatory">*</span></td>
			<td class="bluebox" id="numbers"><s:textfield  id="diameterOfWell" name="docket.diameterOfWell" value="%{docket.diameterOfWell}"  onblur="validateIsNan(this);" maxlength="10" /></td>
			<td class="bluebox" >&nbsp;</td>
</tr>
<tr>
	<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox" width="13%" id="mandatoryfields"><s:text name="inspectionlbl.docket.seperateTank"/><span class="mandatory">*</span></td> 
	   			<td class="greybox" id="numbers" ><s:textfield  id="seperateLatORTank" name="docket.seperateLatORTank" value="%{docket.seperateLatORTank}"  onblur="validateIsNan(this);" maxlength="10"/></td>

<td class="greybox" width="20%">&nbsp;</td>
<td class="greybox" width="20%">&nbsp;</td>			
</tr>
</table>
	    <table  width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="otherinfo_Tbl">
	       <tr>
	 			<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox" width="13%"><s:text name="inspectionlbl.docket.aeereport"/></td> 
	   			<td class="greybox"><s:textarea id="aeeInspectionReport" name="docket.aeeInspectionReport"  value="%{docket.aeeInspectionReport}"  cols="20" rows="2"/></td>
				
				<td class="greybox" width="13%" ><s:text name="inspectionlbl.docket.otherDetails"/></td> 
	   			<td class="greybox" >  <s:textarea id="remarks" name="docket.remarks"  value="%{docket.remarks}" cols="20" rows="2"/></td>
		 </tr>
	    </table>
	</div>
  
   </div> 
</div>