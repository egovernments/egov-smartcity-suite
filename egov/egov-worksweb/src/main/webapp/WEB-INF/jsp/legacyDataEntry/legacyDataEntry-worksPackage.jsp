<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<style>
#warning {
  display:none;
  color:blue;
}
</style>
<script>

var currentDate='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';

function deleteAllrows(tableID) {
    var table = dom.get(tableID);
    var rowCount = table.rows.length;
	for(var i=0; i<rowCount; i++) {
         var row = table.rows[i];
          table.deleteRow(i);
            rowCount--;
             i--;
    }
     document.forms[0].hiddenDate.value=document.forms[0].packageDate.value;
     dom.get('hiddenDept').value=dom.get('department').value;
  }
  
var previousDate;

function dateChange(){
    var pacDate=document.forms[0].packageDate.value;
	var hiddenpacDate=document.forms[0].hiddenDate.value;
	if(hiddenpacDate!= ''){
		previousDate=hiddenpacDate;
	}
	else{
		previousDate=pacDate;
	}
	if(pacDate!='' && hiddenpacDate!= '' && hiddenpacDate!=pacDate){
	     if(dom.get("estimateListTable")!=null && dom.get("estimateListTable").rows.length>0){
	      	dom.get("popUpDiv").innerHTML="";
	        dom.get("popUpDiv").innerHTML='<s:text name="reset.on.date"/>'+dom.get("msgDiv").innerHTML ;
	       	popup('popUpDiv');
		 }	
	}
	document.forms[0].hiddenDate.value=document.forms[0].packageDate.value;
}

function validateWPBeforeSubmit() {
	if(dom.get("name").value==""){
		 dom.get("wp_error").innerHTML='<s:text name="lde.wp.name.is.null"/>'; 
	     dom.get("wp_error").style.display='';
		return false;
	}
	
	 if(dom.get("wpNumber").value==""){
	 	dom.get("wp_error").innerHTML='<s:text name="lde.wp.wpNumber.is.null"/>'; 
	    dom.get("wp_error").style.display='';
		return false;
	 }	   
	 
	 if(dom.get("packageDate").value==""){
	 	dom.get("wp_error").innerHTML='<s:text name="lde.wp.packageDate.is.null"/>'; 
	    dom.get("wp_error").style.display='';
		return false;
	 }
	 
	 if(compareDate(formatDate6(dom.get("packageDate").value),formatDate6(currentDate)) == -1){
        dom.get("wp_error").style.display='';     
		document.getElementById("wp_error").innerHTML='<s:text name="lde.wp.packageDate.greaterthanCurrentDate" />';
		window.scroll(0,0);
		return false  	
	}
	 
	 if(dom.get("tenderFileNumber").value==""){
	 	dom.get("wp_error").innerHTML='<s:text name="lde.wp.tenderFileNumber.is.null"/>'; 
	    dom.get("wp_error").style.display='';
		return false;
	 }	 
	 
	 if(dom.get("estimateListTable")==null){
	  	dom.get("wp_error").innerHTML='<s:text name="lde.wp.estimates.null"/>'; 
        dom.get("wp_error").style.display='';
		return false;
	  }
	  else if(dom.get("estimateListTable").rows.length<=0)
	  {
	  	 dom.get("wp_error").innerHTML='<s:text name="lde.wp.estimates.null"/>'; 
         dom.get("wp_error").style.display='';
		 return false;
	  }
	
	   dom.get("wp_error").style.display='none';
	   dom.get("wp_error").innerHTML='';
	   if(!validateWPOfflineStatus())
		   return false;
	   return true;
}

function resetPreviousDate(){
	document.forms[0].hiddenDate.value=previousDate;
	document.forms[0].packageDate.value=previousDate;
}


function uniqueCheckOntenderFileNumber() {
	tenderFileNumber = dom.get('tenderFileNumber').value;
	var id=null;
	if(dom.get('id')!=null){
		id = dom.get('id').value;
	}else{
	id=0;
	}
	
	if(tenderFileNumber!=''){
		populatewp_error({tenderFileNumber:tenderFileNumber,id:id});
	}else{
		dom.get("wp_error").style.display = "none";
	}
} 

</script>
<div class="errorstyle" id="wp_error" style="display: none;"> 
	<s:text name="wp.tenderfilenumber.isunique"/>
</div>
<div id="blanket" style="display:none;"></div>
<div id="msgDiv" style="display:none;">(<a href="#" onclick="popup('popUpDiv');deleteAllrows('estimateListTable');">Yes</a>/<a href="#" onclick="popup('popUpDiv');resetPreviousDate();">No</a>)?</div>
<div id="popUpDiv" style="display:none;" ></div>

		<table width="100%" border="0" cellspacing="0" cellpadding="0">          
          <tr>
            <td>&nbsp;</td>
          </tr>
          <tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
	        	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
	            <div class="headplacer"><s:text name="wp.header"/></div></td>
	        </tr>
	        <tr>
				 <td width="11%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="lde.wp.name"/>:</td>
		         <td width="21%" class="whitebox2wk" colspan="3" >
		         <s:textarea name="worksPackage.name" cols="100" rows="4" cssClass="selectwk" id="name" /></td>		         
			</tr>
		    <tr>
		        <td width="11%" class="greyboxwk"><span class="mandatory">*</span><s:text name="lde.wp.number"/>:</td>
		         <td width="21%" class=greybox2wk>
		         <s:textfield name="worksPackage.wpNumber" value="%{worksPackage.wpNumber}" id="wpNumber" maxlength="120" cssClass="selectwk" />
		         </td>
		         <td width="15%" class="greyboxwk"><span class="mandatory">*</span><s:text name="lde.wp.date"/>:</td>
		         <td width="53%" class="greybox2wk"><s:date name="worksPackage.packageDate" var="packageDateFormat" format="dd/MM/yyyy"/>
		         <s:hidden name="hiddenDate" id="hiddenDate" ></s:hidden> <s:hidden name="hiddenDept" id="hiddenDept" value="%{worksPackage.userDepartment.id}"></s:hidden>
        		 <s:textfield name="worksPackage.packageDate" value="%{packageDateFormat}" id="packageDate" cssClass="selectboldwk" 
        		 onfocus="javascript:vDateType='3';" 
        		 onkeyup="DateFormat(this,this.value,event,false,'3')"  onblur="dateChange()"/>
        		 <a href="javascript:show_calendar('forms[0].packageDate',null,null,'DD/MM/YYYY');" id="dateHref"
        		 onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;">
        		 <img src="${pageContext.request.contextPath}/image/calendar.png" id="wpDateImg" alt="Calendar" width="16" height="16" 
       			  border="0" align="absmiddle" /></a>
       			 </td>
		   </tr>
		   
		   <tr>
		         <td width="11%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="lde.wp.tenderFileNumber"/>:</td>
		         <td width="21%" class=whitebox2wk>
		         <s:textfield name="worksPackage.tenderFileNumber" value="%{worksPackage.tenderFileNumber}" id="tenderFileNumber" maxlength="50" cssClass="selectwk" onblur="uniqueCheckOntenderFileNumber();" />
		         </td>
				<egov:uniquecheck id="wp_error" fields="['Value']" url='tender/ajaxWorksPackage!tenderFileNumberUniqueCheck.action' />
		         
		         <td width="11%" class="whiteboxwk"><s:text name="lde.wp.department"/>:</td>
		         <td width="21%" class=whitebox2wk>
		         <s:textfield name="departmentName" value="%{worksPackage.userDepartment.deptName}" id="departmentName" cssClass="selectwk" readonly="true"/>
		         <s:hidden name="department" id="department"  value="%{worksPackage.userDepartment.id}" />
		         </td>
		   </tr>
		  <tr>
                <td  colspan="4" class="shadowwk"> </td>                 
          </tr>
		   </table>
		</td>
        </tr>
         <tr>
            <td colspan="4">
            <div id="wp_details">
           		<%@ include file="worksPackage-Details.jsp"%>  
            </div>        
            </td> 
          </tr>
           <tr>
                <td  colspan="4" class="shadowwk"> </td>                 
          </tr>
           <tr>
                <td  colspan="4" >&nbsp; </td>                 
          </tr>  
          <tr>
            <td colspan="4">
            <div id="wp_offlineStatus_details">
           		<%@ include file="lde-offlineStatus-worksPackage.jsp"%>  
            </div>        
            </td> 
          </tr> 
    </table>