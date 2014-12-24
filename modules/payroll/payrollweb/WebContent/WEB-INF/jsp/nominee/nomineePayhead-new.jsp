<!--
	Program Name : nomineePayhead-new.jsp
	Author		: Surya	
	Purpose 	: Nominee Payhead details.
 -->
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/includes/taglibs.jsp" %>

<html>
  <head>
   
  
   <title>Nominee Payhead Details</title>
<SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/common/js/eispayroll.js" ></SCRIPT>
<script type="text/javascript">
     var dom=YAHOO.util.Dom;
    
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
     	
     function checkHeaderPct(obj){
        var id = obj.id;
        var tempPct = id.split("check")[1];       
       	var eligible = id.replace("check","eligible");
        var pct = id.replace("check","pct");
       	if(obj.checked){       	
       		if(getTblLength()<4){
		     	document.getElementById(eligible).value="Y";
		     	document.getElementById(pct).disabled=false;
		     }else{
		     	var row = getRow(obj);
		     	var rowNum = row.rowIndex;		     			     	
		     	<c:forEach var="nomHead" items="${dropdownData.nominationHeaderList}" >
		     		if(tempPct == ${nomHead.id}){		     			
		   	     		document.nomineePayheadForm.pct${nomHead.id}[rowNum-2].disabled=false;
		   	     		document.nomineePayheadForm.eligible${nomHead.id}[rowNum-2].value ="Y";
		   	     	}
			 	</c:forEach>
		     }
     	}
     	else{
     		if(getTblLength()<4){
	     		document.getElementById(eligible).value="N";
	     		document.getElementById(pct).value="";
		     	document.getElementById(pct).disabled=true;
	     	}else{
	     		var row = getRow(obj);
		     	var rowNum = row.rowIndex;		     			     	
		     	<c:forEach var="nomHead" items="${dropdownData.nominationHeaderList}" >
		     		if(tempPct == ${nomHead.id}){
		     			document.nomineePayheadForm.pct${nomHead.id}[rowNum-2].value="";
		   	     		document.nomineePayheadForm.pct${nomHead.id}[rowNum-2].disabled=true;
		   	     		document.nomineePayheadForm.eligible${nomHead.id}[rowNum-2].value ="N";
		   	     	}
			 	</c:forEach>
	     	}
     	}
     } 
    
     function getTblLength(){return dom.get('batchGrid').rows.length;}
     function getValues(){
     	var len = getTblLength();
 		if(len==3 && dom.get('nominee').value!="0"){
			return true;
	    }
	    else if(len>3 && document.nomineePayheadForm.nominee[len-3].value!="0"){
			return true;
	    }
       	return false;
     }
     
     var deletecount =0;
     var delNomineePayhead = "";
     function deleteObj(obj,tableId)
     {
	 	var tbl = dom.get(tableId);
	 	
	 	if(tbl.rows.length > 3){
		 	deletecount = deletecount+1;
		 	var rowNumber=getRow(obj).rowIndex;		 	
		 	<c:forEach var="nomHead" items="${dropdownData.nominationHeaderList}" varStatus="status">
		 		//alert("index--"+status);
		 		delNomineePayhead += document.nomineePayheadForm.nomPayheadDetid${nomHead.id}[rowNumber-2].value;
		 		delNomineePayhead += "-";     		
   	     		//alert(document.nomineePayheadForm.nomPayheadDetid${nomHead.id}[rowNumber-2].value);
			</c:forEach>
		 	
		 	//alert(delNomineePayhead);
		 	
		 	tbl.deleteRow(rowNumber);
	 	}	 	
     }
      	
	function addRow(tableObj,rowObj){
		var tbody=tableObj.tBodies[0];
		tbody.appendChild(rowObj);
  	 }
    
     function addBatchNom(tableId,trId){
     	var len = getTblLength();
     	var noOfNominee = ${fn:length(dropdownData.nomineeMasterList)}
     	//alert("table len----"+len);
     	//alert("nom len----"+noOfNominee);
     	if(noOfNominee > len-2){
	      	if(getValues()){
		   	     var tbl = dom.get(tableId);
		    	 var rowObj = dom.get(trId).cloneNode(true);
		   	     addRow(tbl,rowObj);
		   	     var lastRow = tbl.rows.length;
		   	     var s = tbl.rows.length-3+deletecount;	   	   
		   	     document.nomineePayheadForm.nominee[lastRow-3].setAttribute('name','nomineePayheadDTOList['+s+'].nomineeMaster.id');
		   	     document.nomineePayheadForm.nominee[lastRow-3].value = "0";
		   	     document.nomineePayheadForm.relationType[lastRow-3].value = ""; 
		   	     <s:iterator var="nomDet" value="nomineePayheadDTOList[0].nomineePayheadDetSet" status="status">
		   	     	document.nomineePayheadForm.nomPayheadDetid${nominationHeader.id}[lastRow-3].setAttribute('name','nomineePayheadDTOList['+s+'].nomineePayheadDetSet['+${status.index}+'].id');
		   	     	document.nomineePayheadForm.nomHid${nominationHeader.id}[lastRow-3].setAttribute('name','nomineePayheadDTOList['+s+'].nomineePayheadDetSet['+${status.index}+'].nominationHeader.id');
		   	     	document.nomineePayheadForm.eligible${nominationHeader.id}[lastRow-3].setAttribute('name','nomineePayheadDTOList['+s+'].nomineePayheadDetSet['+${status.index}+'].isEligible');
		   	     	document.nomineePayheadForm.pct${nominationHeader.id}[lastRow-3].setAttribute('name','nomineePayheadDTOList['+s+'].nomineePayheadDetSet['+${status.index}+'].pct');
		   	     	   	     	
		   	     	document.nomineePayheadForm.nomPayheadDetid${nominationHeader.id}[lastRow-3].value ="";
		   	     	//document.nomineePayheadForm.nomHid${nominationHeader.id}[lastRow-3].value ="";
		   	     	//document.nomineePayheadForm.eligible${nominationHeader.id}[lastRow-3].value ="Y";
		   	     	document.nomineePayheadForm.pct${nominationHeader.id}[lastRow-3].value ="";
				 </s:iterator>
	       	}
	    }
     }
     
     function setRelation(obj){     	
     	var len = getTblLength();
     	//alert(len);
     	var nomMasterId = obj.value;
     	if(len < 4){
     		<c:forEach var="nomMaster" items="${dropdownData.nomineeMasterList}" >
		     		if(nomMasterId == ${nomMaster.id}){
		     			dom.get('relationType').value = '${nomMaster.relationType.nomineeType}';
		   	     	}
			</c:forEach>
     	}
     	else{
     		var row = getRow(obj);
	     	var rowNum = row.rowIndex;	     	
	     	<c:forEach var="nomMaster" items="${dropdownData.nomineeMasterList}" >
	     		if(nomMasterId == ${nomMaster.id}){
		     		document.nomineePayheadForm.relationType[rowNum-2].value = '${nomMaster.relationType.nomineeType}';
		   	    }
		 	</c:forEach>
     	}
     }
     
     function checkForPct(obj){
		var objt = obj;
	    var amt = obj.value;
	    if(amt != null && amt != "")
	    {
	      if(amt < 0 || amt >100)
	        {
	            alert("Please enter value (0-100) for the amount");
	            obj.value="";
	            objt.focus();
	            return false;
	
	        }
	        if(isNaN(amt))
	        {
	            alert("Please enter a numeric value for the amount");
	            obj.value="";
	            objt.focus();
	            return false;	
	        }	           
	    }
	}
	
	function duplicateNomineeMaster(){
	   var len = getTblLength();
	   //alert("len---"+len);
	   if(len > 3){
	   	  for(var i=0;i<=len-3;i++){
		       for(var j=i+1;j<=len-3;j++){
			  		if(document.nomineePayheadForm.nominee[i].value==document.nomineePayheadForm.nominee[j].value){
			  			alert("Duplicate selection!!!!!");
			  			document.nomineePayheadForm.nominee[j].value="0";
			  			document.nomineePayheadForm.relationType[j].value="0";
			    		document.nomineePayheadForm.nominee[j].focus();
			    		return false;
			     	}
		       }
          }
       }
       return true;
	}
     	
	function checkOnSubmit(){
		document.nomineePayheadForm.mode.value='view';
    	document.nomineePayheadForm.delNomineePayheadId.value = delNomineePayhead;
    	var len = getTblLength();
    	if(len > 3){
    		<c:forEach var="nomHead" items="${dropdownData.nominationHeaderList}" varStatus="status">
    			var pctValue = 0;
    			var anyEmptyEligible = "N";
    			var anyNotEmptyEligible = "N";
	    		for(var i=0; i<=len-3; i++){
	    			if(document.nomineePayheadForm.pct${nomHead.id}[i].value != ""){
	    				pctValue += eval(document.nomineePayheadForm.pct${nomHead.id}[i].value);
	    			}
	    		}
	    		for(var i=0; i<=len-3; i++){
		    		if(document.nomineePayheadForm.pct${nomHead.id}[i].value=="" && document.nomineePayheadForm.eligible${nomHead.id}[i].value=="Y"){
	    				anyEmptyEligible = "Y";
		    			if(pctValue > 100){
		    				alert("Total percentage value for ${nomHead.code} can't be greater than 100 !!!");
		    				return false;
		    			}
		    			break;
					}
					else if(document.nomineePayheadForm.pct${nomHead.id}[i].value!="" && document.nomineePayheadForm.eligible${nomHead.id}[i].value=="Y"){
						anyNotEmptyEligible = "Y";
					}
				}
				
				if(anyEmptyEligible == "N" && anyNotEmptyEligible == "Y"){
					//alert(pctValue);
					if(eval(pctValue) != 100){
						alert("Total percentage value for ${nomHead.code} has to be 100 !!!!");
						return false;
					}
				}
    		</c:forEach>
    		for(var i=0; i<=len-3; i++){
	    		if(document.nomineePayheadForm.nominee[i].value=="0"){
	    			alert("Please select Nominee");
	    			document.nomineePayheadForm.nominee[i].focus();
	    			return false;
	    		}
	    	}
    	}
    	else{
    		if(document.nomineePayheadForm.nominee.value=="0"){
    			alert("Please select Nominee");
    			document.nomineePayheadForm.nominee.focus();
    			return false;
    		}
    		<c:forEach var="nomHead" items="${dropdownData.nominationHeaderList}" varStatus="status">
    			var pctValue = 0;    			    			
    			if(document.nomineePayheadForm.pct${nomHead.id}.value != ""){
					pctValue += eval(document.nomineePayheadForm.pct${nomHead.id}.value);
		    		if(pctValue != 100){
		    			alert("Percentage value for ${nomHead.code} has to be 100");
		    			return false;
		    		}
		    	}
    		</c:forEach>
    		
    	}
    	return true;
    }
   	    		 
</script>



  </head>
  <body>
  
   <s:form name="nomineePayheadForm" action="nomineePayhead" theme="simple" > 
   <s:hidden name="mode" /> 
   <s:token/>
   	<center>
		
			<div class="formmainbox">
				<div class="insidecontent">
			  		<div class="rbroundbox2">
						<div class="rbtop2">
							<div>
							</div>
						</div>
						<div class="rbcontent2">
							
	<!-- Decorator -->	
			  
			 
			 <span id="msg">
				<s:actionerror cssClass="mandatory"/>  
				<s:fielderror cssClass="mandatory"/>
				<s:actionmessage cssClass="actionmessage"/>
			 </span>
  	
	 
	 
	   <table width="95%" cellpadding="0" cellspacing="0" border="0" align="center">
				  <tbody>					
				  	<tr>
				  	 	<td colspan="${fn:length(salaryPaySlipForm.payHead)+3}" class="headingwk">
					  	 	<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
					   		<p><div class="headplacer">Employee Details</div></p></td>
				   		<td></td> 
				   	</tr>
				   </tbody>
	   </table>
  
  	<br>

		<table width="95%" cellpadding="0" cellspacing="0" border="0" align="center">
			  <tr>
				  <td  class="whiteboxwk" align="center">Employee Code:<br><br></td>
				  <td class="whitebox2wk" align="center">
				  	<input type="text" name="empCode" class="selectwk" value="${employee.employeeCode}" readonly /><br><br></td>
				  <td  class="whiteboxwk" align="center">Employee Name:<br><br></td>
				  <td class="whitebox2wk" align="center">
				  	<input type="text"  class="selectwk" value="${employee.employeeName}" readonly><br><br>
				  </td>
				   <td class="whitebox2wk" align="center" colspan="${fn:length(salaryPaySlipForm.payHead)}">
				   </td>
			  </tr>
		</table>
  		
		<table width="95%" cellpadding="0" cellspacing="0" border="1" align="center" id="batchGrid">		  
			  <tr>
				   <td colspan="${fn:length(dropdownData.nominationHeaderList)+3}" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
				   <p><div class="headplacer">Nominee Payhead Details</div></p></td><td></td> 
			  </tr>
			  <tr>
			      	<td class="tablesubheadwk" style="text-align: center">Nominee</td>
			      	<td class="tablesubheadwk" style="text-align: center">Relation Type</td>
			      	<s:iterator var="s" value="dropdownData.nominationHeaderList" status="status">
			      		<td class="tablesubheadwk" style="text-align: center">			      			
			      			 <s:property value="%{code}" />
			      		</td>
			      	</s:iterator>
			      	<td class="tablesubheadwk" style="text-align: center"></td>
		      </tr>
		      <s:hidden name="delNomineePayheadId" />	
		      <s:if test="isEmpty==null">
		      	<tr id="detailsBatchRow">
		      		<td class="whiteboxwk" style="text-align: center">
		      			<s:select name="nomineePayheadDTOList[0].nomineeMaster.id" id="nominee" list="dropdownData.nomineeMasterList" listKey="id" listValue="nomineeName" headerKey="0" headerValue="----Select----" value="%{nomineeMaster.id}" onchange="setRelation(this);duplicateNomineeMaster();"/>
		      		</td>
		      		<td class="whiteboxwk" style="text-align: center">
		      			<s:textfield name="nomineePayheadDTOList[0].nomineeMaster.relationType.nomineeType" id="relationType" />
		      		</td>
		      		<s:iterator value="nomineePayheadDTOList[0].nomineePayheadDetSet" status="status">	      			
				      		<td class="whiteboxwk" style="text-align: center">
				      			<a href="#" class="hintanchor" 
										onMouseover="showhint('Check/Uncheck to enable/disable the text box', this, event, '300px');"><img src="../common/image/help.png" alt="Note" width="16" height="16" border="0" align="top"/></a>
				      			<s:checkbox name="check%{nominationHeader.id}" id="check%{nominationHeader.id}" onclick="checkHeaderPct(this);"/>
				      			<s:hidden name="nomineePayheadDTOList[0].nomineePayheadDetSet[%{#status.index}].id" id="nomPayheadDetid%{nominationHeader.id}"/>
				      			<s:hidden name="nomineePayheadDTOList[0].nomineePayheadDetSet[%{#status.index}].nominationHeader.id" id="nomHid%{nominationHeader.id}"/>
				      			<s:hidden name="nomineePayheadDTOList[0].nomineePayheadDetSet[%{#status.index}].isEligible" id="eligible%{nominationHeader.id}" value="N"/>
				      			<s:textfield name="nomineePayheadDTOList[0].nomineePayheadDetSet[%{#status.index}].pct" id="pct%{nominationHeader.id}" disabled="true" onchange="checkForPct(this);"/>
				      		</td>
			      	</s:iterator>
			      	<td class="whiteboxwk" style="text-align: center">
			       		<p align="center"><img src="${pageContext.request.contextPath}/images/addrow.gif" alt="Add" width="18" name="add" height="18" border="0" onclick="addBatchNom('batchGrid','detailsBatchRow')"/>
			       		<img src="${pageContext.request.contextPath}/images/removerow.gif" alt="Remove" width="18" height="18" name="del" border="0" onclick="deleteObj(this,'batchGrid')"/></p>
			       		
		      		</td>
		      	</tr>
		     </s:if>
		     <s:else>
		      <s:iterator value="nomineePayheadDTOList" status="rowCount">
		      	<tr id="detailsBatchRow">
		      		<td class="whiteboxwk" style="text-align: center">
		      			<s:select name="nomineePayheadDTOList[%{#rowCount.index}].nomineeMaster.id" id="nominee" list="dropdownData.nomineeMasterList" listKey="id" listValue="nomineeName" headerKey="0" headerValue="----Select----" value="%{nomineeMaster.id}" onchange="setRelation(this);duplicateNomineeMaster();"/>
		      		</td>
		      		<td class="whiteboxwk" style="text-align: center">
		      			<s:textfield name="nomineePayheadDTOList[%{#rowCount.index}].nomineeMaster.relationType.nomineeType" id="relationType" />
		      		</td>
		      		
		      		<s:iterator value="nomineePayheadDetSet" status="status">	      			
				      		<td class="whiteboxwk" style="text-align: center">
				      			<s:if test='%{isEligible=="N"}'>
				      				<a href="#" class="hintanchor" 
										onMouseover="showhint('Check/Uncheck to enable/disable the text box', this, event, '300px');">
										<img src="../common/image/help.png" alt="Note" width="16" height="16" border="0" align="top"/></a>
				      				<s:checkbox name="check%{nominationHeader.id}" id="check%{nominationHeader.id}" onclick="checkHeaderPct(this);"/>
				      				<s:hidden name="nomineePayheadDTOList[%{#rowCount.index}].nomineePayheadDetSet[%{#status.index}].id" id="nomPayheadDetid%{nominationHeader.id}"/>
					      			<s:hidden name="nomineePayheadDTOList[%{#rowCount.index}].nomineePayheadDetSet[%{#status.index}].nominationHeader.id" id="nomHid%{nominationHeader.id}"/>
					      			<s:hidden name="nomineePayheadDTOList[%{#rowCount.index}].nomineePayheadDetSet[%{#status.index}].isEligible" id="eligible%{nominationHeader.id}"/>
					      			<s:textfield name="nomineePayheadDTOList[%{#rowCount.index}].nomineePayheadDetSet[%{#status.index}].pct" id="pct%{nominationHeader.id}" disabled="true" onchange="checkForPct(this);"/>
				      			</s:if>
				      			<s:else>
				      				<a href="#" class="hintanchor" 
										onMouseover="showhint('Check/Uncheck to enable/disable the text box', this, event, '300px');">
										<img src="../common/image/help.png" alt="Note" width="16" height="16" border="0" align="top"/></a>
					      			<s:checkbox name="check%{nominationHeader.id}" id="check%{nominationHeader.id}" onclick="checkHeaderPct(this);" checked=""/>
					      			<s:hidden name="nomineePayheadDTOList[%{#rowCount.index}].nomineePayheadDetSet[%{#status.index}].id" id="nomPayheadDetid%{nominationHeader.id}"/>
					      			<s:hidden name="nomineePayheadDTOList[%{#rowCount.index}].nomineePayheadDetSet[%{#status.index}].nominationHeader.id" id="nomHid%{nominationHeader.id}"/>
					      			<s:hidden name="nomineePayheadDTOList[%{#rowCount.index}].nomineePayheadDetSet[%{#status.index}].isEligible" id="eligible%{nominationHeader.id}"/>
					      			<s:textfield name="nomineePayheadDTOList[%{#rowCount.index}].nomineePayheadDetSet[%{#status.index}].pct" id="pct%{nominationHeader.id}" onchange="checkForPct(this);"/>
					      		</s:else>	
				      		</td>
			      	</s:iterator>
			      	<td class="whiteboxwk" style="text-align: center">
			       		<p align="center">
			       		<img src="${pageContext.request.contextPath}/images/addrow.gif" alt="Add" width="18" name="add" height="18" border="0" onclick="addBatchNom('batchGrid','detailsBatchRow')"/>
			       		<img src="${pageContext.request.contextPath}/images/removerow.gif" alt="Remove" width="18" height="18" name="del" border="0" onclick="deleteObj(this,'batchGrid')"/>
			       		</p>
		      		</td>
		      	</tr>
		      </s:iterator>
		     </s:else>
				  
		</table>	
	
	<br>
	
	
<!-- Decorator div -->	
 	</div>
 	<div class="rbbot2"><div/></div>
	</div>
	</div>
	</div>
	<div class="buttonholderwk">
				<s:submit method="create" value="Create" cssClass="buttonfinal" onclick="return checkOnSubmit();"/>
	</div>
				<%@ include file='../common/payrollFooter.jsp'%>
	</center>
		
	
</s:form>

  </body>
  </html>
