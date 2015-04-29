<script type="text/javascript">


 function validateWPOfflineStatus(flag) {
     var tbl = dom.get('wpOfflineStatusTable');
     var lastRow = tbl.rows.length;
     if(lastRow>3) {
        for(var i=2;i<lastRow;i++) {        	
          /*  alert(dom.get('wpOfflineStatusDate['+eval(i-1)+']').value);
        	if(dom.get('wpOfflineStatusDate['+eval(i-1)+']').value=="") {
        		 dom.get("wp_error").innerHTML='<s:text name="lde.wp.statusDate.is.null"/>';  
         		 dom.get("wp_error").style.display='';
		 		 return false;
        	}else{
        	   if(!isvalidFormat(dom.get('wpOfflineStatusDate['+eval(i-1)+']')))return false;
        	 }*/

        	 var wpOfflineStatusName = getControlInBranch(tbl.rows[i],'wpOfflineStatusName').value;
        	 var prevStatusName = '';
        	 if(getControlInBranch(tbl.rows[i-1],'wpOfflineStatusName') != null)
        		 prevStatusName = getControlInBranch(tbl.rows[i-1],'wpOfflineStatusName').value;
     	 	        	 
        	 var wpOfflineStatusDate = getControlInBranch(tbl.rows[i],'wpOfflineStatusDate').value;
        	 var prevStatusDate = '';
        	 if(getControlInBranch(tbl.rows[i-1],'wpOfflineStatusDate') != null)
        	 	prevStatusDate = getControlInBranch(tbl.rows[i-1],'wpOfflineStatusDate').value;
     	 	
         	if(wpOfflineStatusDate=="") {
         		 dom.get("wp_error").innerHTML='<s:text name="lde.wp.statusDate.is.null"/>';  
          		 dom.get("wp_error").style.display='';
 		 		 return false;
         	}else{
         	   if(!isvalidFormat(getControlInBranch(tbl.rows[i],'wpOfflineStatusDate'))) return false;
         	 }
         	if(wpOfflineStatusDate != '' && compareDate(formatDate6(wpOfflineStatusDate),formatDate6(dom.get("packageDate").value)) == 1){
                dom.get("wp_error").style.display='';     
        		document.getElementById("wp_error").innerHTML='<s:text name="lde.wp.statusDate.lessthan.packageDate" />';
        		window.scroll(0,0);
        		return false  	 
        	}
         	if(wpOfflineStatusDate != '' && prevStatusDate != '' && compareDate(formatDate6(wpOfflineStatusDate),formatDate6(prevStatusDate)) == 1){
                dom.get("wp_error").style.display='';     
        		document.getElementById("wp_error").innerHTML=wpOfflineStatusName+' <s:text name="lde.wp.statusDate.lessthan.prevStatusDate" />'+prevStatusName+' <s:text name="common.label.date" />';
        		window.scroll(0,0);
        		return false  	 
        	}
        }
     }     
       dom.get("wp_error").style.display='none';
	   dom.get("wp_error").innerHTML='';
	   if(flag)toggleTableDetail(tbl,lastRow,false,false);
	   return true;
 }
 
 function formCalendarRef(type,rowNo) {
	 document.getElementsByName('wpOfflineStatusDate')[rowNo].setAttribute("id","wpOfflineStatusDate["+rowNo+"]");
	   document.getElementsByName('dateHref')[rowNo].setAttribute("id","dateHref["+rowNo+"]");
	 show_calendar("getElementById('wpOfflineStatusDate["+rowNo+"]')");
 }
  
 function toggleTableDetail(tbl,lastRow,flag,req) {
     if(lastRow>2){
        for(var i=1;i<lastRow;i++){
            getControlInBranch(tbl.rows[i],'wpOfflineStatusName').disabled=flag;
        	dom.get('wpOfflineStatusDate['+eval(i-1)+']').disabled=flag;
        	if(req){
        	  getControlInBranch(tbl.rows[i],'dateHref').style.visibility='hidden';
        	  getControlInBranch(tbl.rows[i],'delHref').style.visibility='hidden';
        	}
        }
     }
     else{
        dom.get('status').disabled=flag;
        dom.get('wpOfflineStatusDate[0]').disabled=flag;
    	if(req){
    	 dom.get('dateHref[0]').style.visibility='hidden';
    	 dom.get('delHref').style.visibility='hidden';
        }
     }
 }
 
 function isvalidFormat(obj) {
 	if(!checkDateFormat(obj)){
 		dom.get("wp_error").innerHTML='<s:text name="lde.wp.invalid.fieldvalue.statusDate"/>'; 
        dom.get("wp_error").style.display='';
	 	return false;
 	}
 	dom.get("wp_error").style.display='none';
	dom.get("wp_error").innerHTML='';
	return true;
 }
</script>

        
<table width="50%" align="center" border="0" cellspacing="0" cellpadding="0" id="wpOfflineStatusTable">
	<tr>
		<td colspan="3" class="headingwk"><div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" />
		</div><div class="headplacer"><s:text name="lde.wp.offline.status"/></div></td>
		
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
			<s:if test="%{!wpOfflineStatus.isEmpty()}">
			<s:iterator status="status" value="wpOfflineStatus">
			<tr id="detailsRow">
			<td width="4%" class="whitebox3wka">
				<input type="text" name="slNo" id="slNo" disabled="true" size="1" cssClass="selectwk"  value='<s:property value="%{#status.count}"/>'/>
			</td>
			<td width="20%" class="whitebox3wka">
	  			<s:textfield name="wpOfflineStatusName" id="status" value='%{wpOfflineStatusName[#status.index]}'  size="25" cssClass="selectwk"  disabled="true"/>
			</td>
			<td width="10%" class="whitebox3wka">
			<s:date name="wpOfflineStatusDate" var="wpOfflineStatusDateFormat" format="dd/MM/yyyy" /> 
			<s:textfield name="wpOfflineStatusDate" value="%{wpOfflineStatusDateFormat}" id='wpOfflineStatusDate[#status.index]' cssClass="selectwk"  
        		 onfocus="javascript:vDateType='3';" 
        		 onkeyup="DateFormat(this,this.value,event,false,'3')"   onblur="isvalidFormat(this)"/>
        		 <a href="javascript:formCalendarRef('wpOfflineStatusDate','<s:property value="%{#status.index}"/>');" name="dateHref" id='dateHref[#status.index]'
        		 onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;">
        		 <img src="${pageContext.request.contextPath}/image/calendar.png" id="wsDateImg" alt="Calendar" width="16" height="16" 
       			  border="0" align="absmiddle" /></a>
       		</td>
			
			</tr>
			</s:iterator>
			</s:if>
			
			</table>
             