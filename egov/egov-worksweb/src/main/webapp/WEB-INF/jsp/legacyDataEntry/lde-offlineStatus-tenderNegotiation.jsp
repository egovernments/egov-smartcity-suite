<script type="text/javascript">


 function validateTNOfflineStatus(flag) {
     var tbl = dom.get('tnOfflineStatusTable');
     var lastRow = tbl.rows.length;
     if(lastRow>3) {
        for(var i=2;i<lastRow;i++) {        	
          
        	 var tnOfflineStatusName = getControlInBranch(tbl.rows[i],'tnOfflineStatusName').value;
        	 var prevStatusName = '';
        	 if(getControlInBranch(tbl.rows[i-1],'tnOfflineStatusName') != null)
        		 prevStatusName = getControlInBranch(tbl.rows[i-1],'tnOfflineStatusName').value;
     	 	        	 
        	 var tnOfflineStatusDate = getControlInBranch(tbl.rows[i],'tnOfflineStatusDate').value;
        	 var prevStatusDate = '';
        	 if(getControlInBranch(tbl.rows[i-1],'tnOfflineStatusDate') != null)
        	 	prevStatusDate = getControlInBranch(tbl.rows[i-1],'tnOfflineStatusDate').value;
     	 	
         	if(tnOfflineStatusDate=="") {
         		 dom.get("negotiation_error").innerHTML='<s:text name="lde.tn.statusDate.is.null"/>';  
          		 dom.get("negotiation_error").style.display='';
 		 		 return false;
         	}else{
         	   if(!isvalidTNFormat(getControlInBranch(tbl.rows[i],'tnOfflineStatusDate'))) return false;
         	 }
         	if(tnOfflineStatusDate != '' && compareDate(formatDate6(tnOfflineStatusDate),formatDate6(dom.get("negotiationDate").value)) == 1){
                dom.get("negotiation_error").style.display='';     
        		document.getElementById("negotiation_error").innerHTML='<s:text name="lde.tn.statusDate.lessthan.negotiationDate" />';
        		window.scroll(0,0);
        		return false  	 
        	}
         	if(tnOfflineStatusDate != '' && prevStatusDate != '' && compareDate(formatDate6(tnOfflineStatusDate),formatDate6(prevStatusDate)) == 1){
                dom.get("negotiation_error").style.display='';     
        		document.getElementById("negotiation_error").innerHTML=tnOfflineStatusName+' <s:text name="lde.tn.statusDate.lessthan.prevStatusDate" />'+prevStatusName+' <s:text name="common.label.date" />';
        		window.scroll(0,0);
        		return false  	 
        	}
        }
     }     
       dom.get("negotiation_error").style.display='none';
	   dom.get("negotiation_error").innerHTML='';
	   if(flag)toggleTNTableDetail(tbl,lastRow,false,false);
	   return true;
 }
 
 function formTNCalendarRef(type,rowNo) {
	 document.getElementsByName('tnOfflineStatusDate')[rowNo].setAttribute("id","tnOfflineStatusDate["+rowNo+"]");
	   document.getElementsByName('dateHref')[rowNo].setAttribute("id","dateHref["+rowNo+"]");
	 show_calendar("getElementById('tnOfflineStatusDate["+rowNo+"]')");
 }
  
 function toggleTNTableDetail(tbl,lastRow,flag,req) {
     if(lastRow>2){
        for(var i=1;i<lastRow;i++){
            getControlInBranch(tbl.rows[i],'tnOfflineStatusName').disabled=flag;
        	dom.get('tnOfflineStatusDate['+eval(i-1)+']').disabled=flag;
        	if(req){
        	  getControlInBranch(tbl.rows[i],'dateHref').style.visibility='hidden';
        	  getControlInBranch(tbl.rows[i],'delHref').style.visibility='hidden';
        	}
        }
     }
     else{
        dom.get('tnStatus').disabled=flag;
        dom.get('tnOfflineStatusDate[0]').disabled=flag;
    	if(req){
    	 dom.get('dateHref[0]').style.visibility='hidden';
    	 dom.get('delHref').style.visibility='hidden';
        }
     }
 }
 
 function isvalidTNFormat(obj) {
 	if(!checkDateFormat(obj)){
 		dom.get("negotiation_error").innerHTML='<s:text name="lde.tn.invalid.fieldvalue.statusDate"/>'; 
        dom.get("negotiation_error").style.display='';
	 	return false;
 	}
 	dom.get("negotiation_error").style.display='none';
	dom.get("negotiation_error").innerHTML='';
	return true;
 }
</script>

        
<table width="50%" align="center" border="0" cellspacing="0" cellpadding="0" id="tnOfflineStatusTable">
	<tr>
		<td colspan="3" class="headingwk"><div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" />
		</div><div class="headplacer"><s:text name="lde.tn.offline.status"/></div></td>
		
	</tr>
	<tr>
		   <td width="4%" class="tablesubheadwk">
			<s:text name="column.title.SLNo" />
		   </td>
		   <td width="20%" class="tablesubheadwk">
			 <s:text name="works.status"/>
			</td>
			<td width="20%" class="tablesubheadwk">
				<span class="mandatory">*</span><s:text name="status.date"/>
			</td>
			
			</tr>
			<s:if test="%{!tnOfflineStatus.isEmpty()}">
			<s:iterator status="status" value="tnOfflineStatus">
			<tr id="detailsRow">
			<td width="4%" class="whitebox3wka">
				<input type="text" name="slNo" id="slNo" disabled="true" size="1" cssClass="selectwk"  value='<s:property value="%{#status.count}"/>'/>
			</td>
			<td width="20%" class="whitebox3wka">
	  			<s:textfield name="tnOfflineStatusName" id="tnStatus" value='%{tnOfflineStatusName[#status.index]}'  size="25" cssClass="selectwk"  disabled="true"/>
			</td>
			<td width="10%" class="whitebox3wka">
			<s:date name="tnOfflineStatusDate" var="tnOfflineStatusDateFormat" format="dd/MM/yyyy" /> 
			<s:textfield name="tnOfflineStatusDate" value="%{tnOfflineStatusDateFormat}" id='tnOfflineStatusDate[#status.index]' cssClass="selectwk"  
        		 onfocus="javascript:vDateType='3';" 
        		 onkeyup="DateFormat(this,this.value,event,false,'3')"   onblur="isvalidTNFormat(this)"/>
        		 <a href="javascript:formTNCalendarRef('tnOfflineStatusDate','<s:property value="%{#status.index}"/>');" name="dateHref" id='dateHref[#status.index]'
        		 onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;">
        		 <img src="${pageContext.request.contextPath}/image/calendar.png" id="wsDateImg" alt="Calendar" width="16" height="16" 
       			  border="0" align="absmiddle" /></a>
       		</td>
			
			</tr>
			</s:iterator>
			</s:if>
</table>


             