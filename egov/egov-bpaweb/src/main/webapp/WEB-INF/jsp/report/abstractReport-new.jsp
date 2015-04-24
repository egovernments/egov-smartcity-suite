<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@	taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>

<%@ include file="/includes/taglibs.jsp" %>
<html>

<head>
<title>Abstract Report</title>
	
	<sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true"/>

<link rel="stylesheet" type="text/css" href="<c:url value='/common/css/jquery/jquery.multiselect.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/common/css/jquery/jquery.multiselect.filter.css'/>" />
<script type="text/javascript" src="<c:url value='/common/js/jquery/jquery.multiselect.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/common/js/jquery/jquery.multiselect.filter.js'/>"></script>
<style type="text/css">
body.printingContent > *{
    display: none !important; /* hide everything in body when in print mode*/
}

.printContainer {
    display: block !important; /* Override the rule above to only show the printables*/
    position: fixed;
    z-index: 99999;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
} 
.visiblediv {display:block;}
</style>

<script type="text/javascript">
	jQuery.noConflict();
	jQuery(document).ready(function(){
		
	jQuery("#serviceType").multiselect();
	jQuery("#locboundaryid").hide();
	jQuery("#AreaArea").hide();
	jQuery("#StreetStreet").hide();
	jQuery("#Ward").removeAttr("onchange");

});

function dateValidate(){

	var wardnum=document.getElementById('Ward').value;
	var zonenum=document.getElementById('Zone').value;
	if(wardnum!=null && wardnum!=-1){
		
		document.getElementById('adminboundaryid').value=wardnum;
	}
	else if(wardnum==-1)
		{
		document.getElementById('adminboundaryid').value=zonenum;
		}
	var todaysDate=getTodayDate();
//alert("validate "+todaysDate);
	var fromdate=document.getElementById('applicationFromDate').value;
	var todate=document.getElementById('applicationToDate').value;
	
	if((fromdate!=null && fromdate!="" && fromdate!=undefined )||( todate!=null && todate!="" && todate!=undefined )){
		if(compareDate(fromdate,todaysDate) == -1)
		{						  	 	
			 dom.get("searchRecords_error").style.display = '';
			 document.getElementById("searchRecords_error").innerHTML = '<s:text name="fromDate.todaysDate.validate" />';			  					 
			 document.getElementById('applicationFromDate').value=""; 
			 document.getElementById('applicationFromDate').focus();
			 return false;
		}
		if(compareDate(todate,todaysDate) == -1)
		{		
			 
			 dom.get("searchRecords_error").style.display = '';
			 document.getElementById("searchRecords_error").innerHTML = '<s:text name="toDate.todaysDate.validate" />';			  
								 
			  document.getElementById('applicationToDate').value="";  
			  document.getElementById('applicationToDate').focus();
			  return false;
		}
		if(compareDate(fromdate,todate) == -1)
		{		
				
			 dom.get("searchRecords_error").style.display = '';
			 document.getElementById("searchRecords_error").innerHTML = '<s:text name="fromDate.toDate.validate" />';			  
			 document.getElementById('applicationFromDate').value="";
			 document.getElementById('applicationToDate').value="";    
			 document.getElementById('applicationFromDate').focus();
			 return false;
		}
		
   }
	if(!checkAllfields())
		return false;
	
		
		
}
function resetValues(){
	//alert("reset");
	document.getElementById('serviceType').value="-1";
 	document.getElementById('adminboundaryid').value="-1" ;
 	document.getElementById('Zone').value="-1";
 	document.getElementById('Ward').value="-1";
  	document.getElementById('applicationFromDate').value="" ;
  	jQuery('#serviceType').multiselect('uncheckAll');
 	document.getElementById('applicationToDate').value="" ;
 	document.getElementById("searchdetails").style.display='none';
 	document.getElementById("printid").style.display='none';
 	document.getElementById('appMode').value="-1" ; 
 	document.getElementById('plotDoorNum').value="" ;
 
}
function closeWindow()
{
  window.close();
  }


function checkAllfields(){
	  if(document.getElementById('adminboundaryid').value ==-1 && document.getElementById('applicationFromDate').value=="" && document.getElementById('applicationToDate').value=="" &&
				 document.getElementById("serviceType").value =="" && document.getElementById('plotDoorNum').value=="" && document.getElementById('appMode').value==-1)
		{                     
			dom.get("searchRecords_error").style.display = '';
 			document.getElementById("searchRecords_error").innerHTML = 'At Least one information has to be provided';	
 			return false;         
 			}
		return true;
	   	
		
} 
  function printDiv(divName) 
  { 
	  jQuery("#breadcrumb").hide();
		jQuery(".commontopyellowbg").hide();
		jQuery(".commontopbluebg").hide();
		 jQuery("#searchscreen").hide();
		jQuery("#printid").hide();
 		 window.print();
 		jQuery("#breadcrumb").show();
		jQuery(".commontopyellowbg").show();
		jQuery(".commontopbluebg").show();
		 jQuery("#searchscreen").show();
		 jQuery("#printid").show();
 
    	} 
	 
</script>
</head>
<body >
<div class="errorstyle" id="searchRecords_error" style="display: none;"></div>
	<div class="errorstyle" style="display: none" ></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />	
			<s:fielderror />
		</div>
	</s:if>
<s:form action="abstractReport" name="abstractReportForm" theme="simple">


<div id="searchscreen">
<div class="formheading"/></div>
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
		
				<tr>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="Application Fromdate"/> </td>
				<td class="bluebox"><sj:datepicker value="%{applicationFromDate}" id="applicationFromDate" name="applicationFromDate" displayFormat="dd/mm/yy" showOn="focus" maxlength="10"/></td>					
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="Application Todate"/> </td>
				<td class="bluebox"><sj:datepicker value="%{applicationToDate}" id="applicationToDate" name="applicationToDate" displayFormat="dd/mm/yy" showOn="focus" maxlength="10"/></td>
					
			</tr>
			<tr class="greybox">								
				<egov:loadBoundaryData adminboundaryid="${adminboundaryid}"
						locboundaryid="${locboundaryid}" adminBndryVarId="adminboundaryid" locBndryVarId="locboundaryid" />
				<s:hidden id= "adminboundaryid" name="adminboundaryid" value="%{adminboundaryid}" />
				<s:hidden id= "locboundaryid"  name="locboundaryid" value="%{locboundaryid}" />	
			</tr>
			<tr>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox"><s:text name="Service Type"/> </td>
				<td width="21%" class="bluebox"><s:select id="serviceType"
						name="serviceType" multiple="true" checkmark="true" list="dropdownData.serviceTypeList"
						listKey="id" 
						listValue="code+'-'+description" value="%{serviceType}" OnClick="return show();"/></td>
				<td class="bluebox">&nbsp;</td>	
		<td class="bluebox" ><s:text name="doorNum" /> :</td>
	    <td class="bluebox" ><s:textfield id="plotDoorNum" name="plotDoorNum" value="%{plotDoorNum}"  maxlength="10" /></td>
	     </td>	
			</tr>
				<tr>
			<td class="greybox" width="13%">&nbsp;</td>
		<td class="greybox" width="13%"><s:text name="applMode" /> </td>
		<td class="greybox"> <s:select  id="appMode" name="appMode" value="%{appMode}" 
		list="dropdownData.applicationModeList" listKey="code" listValue="code" headerKey="-1" headerValue="-----choose----"/>
		 </td>
            <td class="greybox" width="13%">&nbsp;</td>
              <td class="greybox" width="13%">&nbsp;</td>  <td class="greybox" width="13%">&nbsp;</td>
              
          </tr>
		</table>
	
			<div class="buttonbottom" align="center">
			<table>
				<tr>
					<td><s:submit cssClass="buttonsubmit" id="search" name="search" value="Search" method="searchResult"  onclick="return dateValidate();" /></td>
				 <td><input type="button" id="reset" name="reset" class="button" value="Reset"  onclick="return resetValues();" /></td>
			  		<td><input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();"/></td>
			  	</tr>
	        </table>
	   </div>
	   	</div>
	      <div id="tableData">
	   <div class="infostyle" id="searchRecords_error" style="display:none;"></div> 
	    
	<s:if test="%{searchMode=='result'}">
	    <div id="searchdetails" >
 	<table align="center" width="100%" border="0" cellpadding="0"
                    cellspacing="0">
                    <tr>
                        <td class="headingwk" colspan="5">
                             <div style="font-size: 13;font-color: red; margin-top: 4px;" align="center"> Building Plan Approval : Abstract 
                                Report</div>
                        </td>
                    </tr>
        </table>	  
<table width="100%" class="its" border="1" cellspacing="0" cellpadding="2">	
	<tr>						   
<div align="center">
<th class="bluebgheadtd" width="10%"><s:text name="Zone.lbl" />
</th>   
</div>
<div align="center">
<th class="bluebgheadtd" colspan="1" width="10%"><s:text name="TotalRec.lbll" />
</th>   
</div>
<div align="center">
<th class="bluebgheadtd" colspan="3" width="30%"><s:text name="TDisp.lbll" />
</th>   
</div>

<div align="center">
<th class="bluebgheadtd" colspan="3" width="30%"><s:text name="application.lbl" />
</th>   
</div>
<div align="center">
<th class="bluebgheadtd"  colspan="3" width="30%"><s:text name="LP.lbl" />
</th>   
</div>
<div align="center">
<th class="bluebgheadtd" colspan="3" width="30%"><s:text name="LREF.lbl" />
</th>   
</div>

<div align="center">
<th class="bluebgheadtd" colspan="1" width="10%"><s:text name="LPTotal.lbl" />
</th>   
</div>  




</tr>
<tr>
<div align="center">
<th class="bluebgheadtd" width="10%">
</div></th>
<s:iterator value="appzonestatusmap" status="headermapstatus">
   <s:if test="#headermapstatus.index ==0">
  
    <s:iterator value="value">
   
     <th class="bluebgheadtd" width="10%">
     <div align="center">
     <s:property value="%{getHeaderkey(key)}"/>
     </div></th>  
     
   </s:iterator>
   
   </s:if>
</s:iterator>

</tr>


  <s:iterator value="appzonestatusmap" status="outerstatus">
  
 <tr height="100%" >

 
     <td class="blueborderfortd">
  <s:property value="key"  /> 
  <s:set var="count" value="key"/>
  
 </td>
      <s:iterator value="value">
 <td class="blueborderfortd" height="100%">
 <div align="center">
   <s:if test="%{value>0}">
<b>
       <s:property value="value"/>
</b>
      </s:if>  
      <s:else>
          <s:property value="value"/>
 	 </s:else>


 </div>
     </td>
  </s:iterator>
</tr>
 
</s:iterator>


  </table>
 </div>
 </s:if>
 
 </div> 
 <div id="printid" class="buttonbottom" align="center">
		<s:if test="%{searchMode=='result'}">
			<table>
			<tr>
			<td><input type="button" name="Print" id="Print" class="button"  value="Print"  onclick="printDiv('tableData')" />
			</td>
			</tr>
			</table>
			</s:if>
		</div>
	
	    </s:form>
	</body>
</html>
	        