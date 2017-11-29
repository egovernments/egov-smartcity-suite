/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces, 
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any 
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines, 
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
   function validate(){
        if (!validateForm_subLedgerScheduleForm()) {
        	undoLoadingMask();
    		return false;
        }
       document.getElementById('subLedgerTypeName').value = jQuery("#accEntityId option:selected").text();
       var strtDate = document.getElementById('startDate').value;
       var endDate = document.getElementById('endDate').value;
       var currDate = new Date();
       var currentDate = currDate.getDate() + "/" + (currDate.getMonth()+1) + "/" + currDate.getFullYear() ;
    	 /* checking Fiscal year start Date with End Date of Fiscal year */	
    	var tMonth=endDate.substr(endDate.length-7,2);
    	if(tMonth<4)
    		var fiscalYearStartDate="01/04/"+(endDate.substr(endDate.length-4,4)-1);
    	else
    		var fiscalYearStartDate="01/04/"+endDate.substr(endDate.length-4,4);
    	if(compareDate(fiscalYearStartDate,strtDate) == -1 )
   		{ 
	       bootbox.alert("Start Date and End Date should be in same financial year");
	       document.getElementById('startDate').focus();
	       return false;
	    
   		} 
    	
    	/*To check whether Start Date is Greater than End Date*/
    	if( compareDate(formatDate6(strtDate),formatDate6(endDate)) == -1 )
    	{
	    	bootbox.alert('Start Date cannot be greater than End Date');
	    	document.getElementById('startDate').value='';
	    	document.getElementById('endDate').value='';
	    	document.getElementById('startDate').focus();
	    	return false;
    	}	
    	   /*to check whether the End Date is greater than the Current Date*/
    	if( compareDate(formatDate6(currentDate),formatDate6(endDate)) == 1 )
    	{
    		bootbox.alert('End Date cannot be greater than Current Date');
    		document.getElementById('endDate').value='';
    		document.getElementById('endDate').focus();	
    		return false;	
   		}	
    	doLoadingMask();
      	document.getElementById("resultDiv").style.display="none";
          var formObj = jQuery(document.getElementById("subLedgerScheduleForm"));
          var formURL = '/EGF/report/subLedgerScheduleReport-ajaxSearch.action';
          var formData = new FormData(document.getElementById("subLedgerScheduleForm"));
          jQuery.ajax({
              url: formURL,
              data:  formData,
              type : 'POST',
      		async : false,
      		datatype : 'text',  
      		processData: false, 
      		contentType: false,
          	
          success: function(data)
          {
              document.getElementById("resultDiv").innerHTML=data;
              document.getElementById("resultDiv").style.display="block";
              undoLoadingMask();
          },
           error: function(jqXHR, textStatus, errorThrown)
           {
          	 undoLoadingMask();
           }         
          });
    	
        return true;
           
    	
    }
    var path="../..";
	var oAutoCompEntityForJV;
	function autocompleteAccountCodes(obj)
	{
	  	   oACDS = new YAHOO.widget.DS_XHR(path+"/EGF/voucher/common-ajaxLoadSLreportCodes.action", [ "~^"]);
		   oACDS.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
		   oACDS.scriptQueryParam = "startsWith";
		   oAutoCompEntityForJV = new YAHOO.widget.AutoComplete(obj.name,'codescontainer',oACDS);
		   oAutoCompEntityForJV.doBeforeSendQuery = function(sQuery){
			   loadWaitingImage(); 
			   return sQuery+"&glCode="+document.getElementById("glcode").value;
		   } 
		   oAutoCompEntityForJV.queryDelay = 0.5;
		   oAutoCompEntityForJV.minQueryLength = 3;
		   oAutoCompEntityForJV.prehighlightClassName = "yui-ac-prehighlight";
		   oAutoCompEntityForJV.useShadow = true;
		  // oAutoCompEntityForJV.forceSelection = true;
		   oAutoCompEntityForJV.maxResultsDisplayed = 20;
		   oAutoCompEntityForJV.useIFrame = true;
		   oAutoCompEntityForJV.doBeforeExpandContainer = function(oTextbox, oContainer, sQDetauery, aResults) {
			   clearWaitingImage();
		           var pos = YAHOO.util.Dom.getXY(oTextbox);
		           pos[1] += YAHOO.util.Dom.get(oTextbox).offsetHeight + 6;
		           oContainer.style.width=300;
		           YAHOO.util.Dom.setXY(oContainer,pos);
		           return true;
		   };
	}
	function splitAccountCodes(obj) 
	{	
		var entity=obj.value;
		if(entity.trim()!="")
		{
			var entity_array=entity.split("`~`");
			if(entity_array.length==2)
			{
				document.getElementById("glcode").value=entity_array[0].split("`-`")[0];
				
			}
		}
		populateSubLedger();
	}
	
	function viewVoucher(vid){
		var url = '../voucher/preApprovedVoucher-loadvoucherview.action?vhid='+vid;
		window.open(url,'','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700,status=yes');
	}
	
	function viewSubLedger(name,accEntId,accEntkey){
		var glcode=document.getElementById("glcode").value;
		var fund_id=document.getElementById("fund_id").value;
		var dept_id=document.getElementById("deptId").value;
		var startDate=document.getElementById("startDate").value;
		var endDate=document.getElementById("endDate").value; 	
		var url = "../report/subLedgerReport-search.action?glCode1="+glcode+"&glCode2="+glcode+"&fund_id="+fund_id+"&departmentId="+dept_id+"&startDate="+startDate+"&endDate="+endDate+"&accEntityId="+accEntId+"&entityName="+name+"&accEntityKey="+accEntkey+"&drillDownFromSchedule=true";
		window.open(url,'','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700,status=yes');
	}
