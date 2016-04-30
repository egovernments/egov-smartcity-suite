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
<%@page import="org.egov.infstr.utils.EgovMasterDataCaching,
                java.util.ArrayList"
    %>

<script>
<%
        ArrayList approverDepartmentList = (ArrayList)EgovMasterDataCaching.getInstance().get("egi-department");	
		request.setAttribute("approverDepartmentList",approverDepartmentList);	
%>
<c:set var="approverDepartmentList" value="${approverDepartmentList}" scope="page" />
// This is to store nextAction value
var NEXTACTION="";
var WORKFLOWBUTTONDIV="";
var BUTTONCSS="";
var BUTTONARRAY="";

function getUsersByDesignationAndDept()
{
	populateapproverPositionId({approverDepartmentId:document.getElementById('approverDepartment').value,designationId:document.getElementById('approverDesignation').value});
}

function callAlertForDepartment()
{
    var value=document.getElementById("approverDepartment").value;
	if(value=="-1")
	{
		bootbox.alert("Please select the Approver Department");
		document.getElementById("approverDepartment").focus();
		return false;
	}
}

function callAlertForDesignation()
{
	var value=document.getElementById("approverDesignation").value;
	if(value=="-1")
	{
		bootbox.alert("Please select the approver designation");
		document.getElementById("approverDesignation").focus();
		return false;
	}
}
	
function loadDesignationByDeptAndType(typeValue,departmentValue,currentStateValue,amountRuleValue,additionalRuleValue,pendingActionValue)
{
	  var designationObj =document.getElementById('approverDesignation');
	  designationObj.options.length = 0;
	  designationObj.options[0] = new Option("----Choose----","-1");
	  var approverObj = document.getElementById('approverPositionId');
	  approverObj.options.length = 0;
	  approverObj.options[0] = new Option("----Choose----","-1");
	  populateapproverDesignation({departmentRule:departmentValue,type:typeValue,amountRule:amountRuleValue,additionalRule:additionalRuleValue,
	  													currentState:currentStateValue,pendingAction:pendingActionValue});
}

function loadDesignationByDeptAndTypeWithDate(typeValue,departmentValue,currentStateValue,amountRuleValue,additionalRuleValue,pendingActionValue,date)
{
	  var designationObj =document.getElementById('approverDesignation');
	  designationObj.options.length = 0;
	  designationObj.options[0] = new Option("----Choose----","-1");
	  var approverObj = document.getElementById('approverPositionId');
	  approverObj.options.length = 0;
	  approverObj.options[0] = new Option("----Choose----","-1");
	  populateapproverDesignation({departmentRule:departmentValue,type:typeValue,amountRule:amountRuleValue,additionalRule:additionalRuleValue,
	  													currentState:currentStateValue,pendingAction:pendingActionValue,date:date});
}

function getNextValidActions(type,departmentId,currentStateValue,
							amountRuleValue,additionalRuleValue,pendingActionValue,methodName,workflowbuttonDiv,buttonCssName)
{
		WORKFLOWBUTTONDIV=workflowbuttonDiv;
		BUTTONCSS=buttonCssName;
		BUTTONARRAY=new Array();
	   	var url = "/egi/workflow/ajaxWorkFlow!getAjaxValidButtonsAndNextAction.action?type="+type+"&departmentRule="+departmentId+"&amountRule="+amountRuleValue+"&additionalRule="+additionalRuleValue+"&currentState="+currentStateValue+"&pendingAction="+pendingActionValue;
	   	var request = initiateRequest();
	   	request.open("GET", url, false);
		request.send(null);
	
    	    if(request.readyState==4)
       	    {
           	   if (request.status == 200){
            	  var responseString=request.responseText;
            	  var responseArray=responseString.split('@');
            	  var actionArray=responseArray[0].split(',');
            	  for(var i=0;i<actionArray.length;i++){
            	    if(actionArray[i]!="" && actionArray[i]!=undefined && actionArray[i]!=null)
            	    	BUTTONARRAY.push(actionArray[i]);
            			addButton(actionArray[i],methodName);
            		}
            	   closeButton();
            	   if(responseArray[1]=="END")
            	   {
            	     NEXTACTION=responseArray[1];
            	     document.getElementById('commonApprovalInfo').style.visibility='hidden';
					 document.getElementById('commonApprovalInfo').style.width='0';
					 document.getElementById('commonApprovalInfo').style.height='0';	 
            	   }
            	   else 
            	   {
            	   	 document.getElementById('commonApprovalInfo').style.visibility='visible';
	      	  		 document.getElementById('commonApprovalInfo').style.width='100%';
		      		 document.getElementById('commonApprovalInfo').style.height='50';
           		   }
           		  } 
           		  else{
           			bootbox.alert("Error in getting Work Flow matrix");
           		  }
       		}
       	
       	
}

function getNextValidActionsWithDate(type,departmentId,currentStateValue,
		amountRuleValue,additionalRuleValue,pendingActionValue,methodName,workflowbuttonDiv,buttonCssName,date)
{
WORKFLOWBUTTONDIV=workflowbuttonDiv;
BUTTONCSS=buttonCssName;
BUTTONARRAY=new Array();
var url = "/egi/workflow/ajaxWorkFlow!getAjaxValidButtonsAndNextAction.action?type="+type+"&departmentRule="+departmentId+"&amountRule="+amountRuleValue+"&additionalRule="+additionalRuleValue+"&currentState="+currentStateValue+"&pendingAction="+pendingActionValue+"&date="+date;
var request = initiateRequest();
request.open("GET", url, false);
request.send(null);

if(request.readyState==4)
{
if (request.status == 200){
var responseString=request.responseText;
var responseArray=responseString.split('@');
var actionArray=responseArray[0].split(',');
for(var i=0;i<actionArray.length;i++){
if(actionArray[i]!="" && actionArray[i]!=undefined && actionArray[i]!=null)
	BUTTONARRAY.push(actionArray[i]);
	addButton(actionArray[i],methodName);
}
closeButton();
if(responseArray[1]=="END")
{
 NEXTACTION=responseArray[1];
 document.getElementById('commonApprovalInfo').style.visibility='hidden';
 document.getElementById('commonApprovalInfo').style.width='0';
 document.getElementById('commonApprovalInfo').style.height='0';	 
}
else 
{
	 document.getElementById('commonApprovalInfo').style.visibility='visible';
	 document.getElementById('commonApprovalInfo').style.width='100%';
	 document.getElementById('commonApprovalInfo').style.height='50';
  }
 } 
 else{
	 bootbox.alert("Error in getting Work Flow matrix");
 }
}


}
function trim(stringToTrim) {
	return stringToTrim.replace(/^\s+|\s+$/g,"");
}


function createNamedElement(type, name,css,method) {
	var element = null;
	// First try the IE way; if this fails then use the standard way
	try {
		element = document.createElement('<'+type+' name="'+name+'" class="'+css+'" onclick="'+method+'">');
	} catch (e) {
	}
	if (!element) {
		element = document.createElement(type);
		element.name = name;
		element.setAttribute("class",css);
		element.setAttribute("onclick",method);
	}
	return element;
}


function addButton(value,methodName)
{
    var METHODNAME="",buttonDiv="";
    if(value!=""){
   		if(methodName!="")
      	    METHODNAME = methodName+"('"+trim(value)+"')";
      	else
      	    METHODNAME = validateWorkFlowApprover+"('"+trim(value)+"')";
      	
      	if(BUTTONCSS=="")
      	    BUTTONCSS = "buttonsubmit";
      	 
      	  
      	if(WORKFLOWBUTTONDIV=="")
      	    WORKFLOWBUTTONDIV= "workFlowButtons";
      	
      
      	    
        var element =createNamedElement('input', value,"buttonsubmit",METHODNAME);
        element.setAttribute("type","button" );
    	element.setAttribute("id",value );
   		element.setAttribute("value", trim(value));
   		var buttonDiv= dom.get(WORKFLOWBUTTONDIV);
        buttonDiv.appendChild(element);
    	
    }
}


function closeButton()
{
    var element = createNamedElement('input',"Close","button","window.close()");
    element.setAttribute("type", "button");
    element.setAttribute("value", "Close");
    element.setAttribute("id", "Close");
    if(WORKFLOWBUTTONDIV=="")
      	 WORKFLOWBUTTONDIV= "workFlowButtons";
    var buttonDiv= dom.get(WORKFLOWBUTTONDIV);
        buttonDiv.appendChild(element);
}

function resetApprovalInfo()
{
     if(dom.get('commonApprovalInfo')!=null)
     {
     	  document.getElementById("approverDepartment").value='-1';
          document.getElementById("approverDesignation").value='-1';
		  document.getElementById("approverPositionId").value='-1';
     }
}

//disabling all the buttons
function disableAllButtons()
{
     var count;
     for (count=0;count<BUTTONARRAY.length;count++)
     {
        if(document.getElementById(BUTTONARRAY[count])!=null)
            document.getElementById(BUTTONARRAY[count]).disabled=true;
     }
}

function validateWorkFlowApprover(name)
{
    document.getElementById('actionName').value=name;
	if(NEXTACTION!='END'){
	   if((name=="Forward" || name=="Approve" || name=="approve" || name=="forward") 
	                && document.getElementById('approverPositionId').value=="-1")
	   {
		   bootbox.alert("Please Select the Approver ");
		   return false;
	   }
    }
	
    return true;
}

</script>


<div id="commonApprovalInfo">
 <input type="hidden" id="actionName" name="actionName" />
<h1 class="subhead" > Approval Authority Information </h1>
<table width="100%" cellpadding ="0" cellspacing ="0" border = "0" >
   <tr>
   		<td class="bluebox" width="8%"> &nbsp; </td>
    	<td class="bluebox"> Department: </td>
    	<td class="bluebox">
    		<select name="approverDepartment" id="approverDepartment" onchange="loadDesignationFromMatrix();" name="approverDepartment">
				<option value="-1">---------choose---------</option>
				<c:forEach items="${approverDepartmentList}" var="dept">
				     <option value= "${dept.id}" ><c:out value="${dept.deptName}"/></option>
				</c:forEach>
			</select>
			<egovtags:ajaxdropdown fields="['Text','Value']" url="workflow/ajaxWorkFlow!getDesignationsByObjectType.action" id="approverDesignation" dropdownId="approverDesignation" contextToBeUsed="/egi"/>
	   </td>
	
	  <td class="bluebox" > Designation: </td>
      <td class="bluebox">
    	<select id="approverDesignation" name="approverDesignation" onchange="populateApprover();" onfocus="callAlertForDepartment();" >
			<option value="-1">---------choose----------</option>
		</select>
		<egovtags:ajaxdropdown id="approverPositionId" fields="['Text','Value']" dropdownId="approverPositionId" url="workflow/ajaxWorkFlow!getPositionByPassingDesigId.action" contextToBeUsed="/egi"/>
	  </td>
	  <td class="bluebox" > Approver: </td>
	  <td class="bluebox">
		   <select id="approverPositionId" name="approverPositionId" onfocus="callAlertForDesignation();">
			   <option value="-1">------------choose----------</option>			
		   </select>
	  </td> 
	</tr>
  </table>
  </div>
  <div id="workflowCommentsDiv" align="center">
         <table width="100%">
         <tr>
           <td width="10%" class="bluebox">&nbsp;</td>
            <td width="20%" class="bluebox">&nbsp;</td>
           <td class="bluebox" width="13%"> Approver Comments: </td>
           <td class="bluebox"> 
           	<textarea id="approverComments" name="approverComments" rows="2" cols="35" ></textarea>  
           </td>
           <td class="bluebox">&nbsp;</td>
           <td width="10%" class="bluebox">&nbsp;</td>
           <td  class="bluebox">&nbsp;</td>
           </tr>
         </table>
  </div>
  
 
   
