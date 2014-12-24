<!--
	Program Name : disciplinaryPunishment-eidt.jsp
	Author		: Jagadeesan M
	Created	on	: 05-07-2010
	Purpose 	: To modify Disciplinary Punishment.
 -->

<%@ page import="java.util.*"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/struts-tags.tld" prefix="s"%> 
<html>
  	<head>
	  	<title>Modify Disciplinary Punishment</title>
	  <link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/reset/reset.css'/>" />
	  <link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/fonts/fonts.css'/>" />
	<script type="text/javascript" src="<c:url value='/commonyui/build/yahoo/yahoo.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/dom/dom.js'/>"></script>

	<script type="text/javascript" src="<c:url value='/commonyui/build/event/event-debug.js'/>"></script>
	
	<script type="text/javascript" src="<c:url value='/commonyui/build/connection/connection-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/autocomplete/autocomplete-debug.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/animation/animation.js'/>"></script>
		<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.7/fonts/fonts-min.css"/>
		<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.7/datatable/assets/skins/sam/datatable.css"/>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/yuiloader/yuiloader-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/element/element.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/connection/connection-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/datasource/datasource-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/datatable/datatable.js"></script>
		<script type="text/javascript" src="/egi/commonyui/build/autocomplete/autocomplete-debug.js"></script>

	
	
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	  	<style type="text/css">
			#empCodecontainer {position:absolute;left:11em;width:9%;text-align: left;}
			#empCodecontainer .yui-ac-content {position:absolute;width:600px;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
			#empCodecontainer .yui-ac-shadow {position:absolute;margin:.3em;width:300px;background:#a0a0a0;z-index:9049;}
			#empCodecontainer ul {padding:5px 0;width:100%;}
			#empCodecontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
			#empCodecontainer li.yui-ac-highlight {background:#ff0;}
			#empCodecontainer li.yui-ac-prehighlight {background:#FFFFCC;}
		</style>
  </head>
  
  <script>
  		var oACDS;
  		var oAutoComp1;
		function autocompleteEnquiryEmp(obj){
			oACDS = new YAHOO.widget.DS_XHR("/eis/common/employeeSearch!getEmpListByEmpCodeLike.action", ["##"]);
			oACDS.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
			oACDS.scriptQueryParam = "startsWith";
			if ( oAutoComp1!= undefined) {
				oAutoComp1.destroy();
				oAutoComp1 = null;
			}
			oAutoComp1 = new YAHOO.widget.AutoComplete(obj,'empCodecontainer',oACDS);
			oAutoComp1.queryDelay = 0.5;
			oAutoComp1.minQueryLength = 1;
			oAutoComp1.prehighlightClassName = "yui-ac-prehighlight";
			oAutoComp1.useShadow = true;
			oAutoComp1.forceSelection = true; 
			oAutoComp1.maxResultsDisplayed = 20;
			oAutoComp1.useIFrame = true;
			oAutoComp1.doBeforeExpandContainer = function(oTextbox, oContainer,sQDetauery, aResults) {
		        //clearWaitingImage();
		        var pos = YAHOO.util.Dom.getXY(oTextbox);
		        pos[1] += YAHOO.util.Dom.get(oTextbox).offsetHeight + 6;
		        oContainer.style.width=300;
		        YAHOO.util.Dom.setXY(oContainer,pos);
		        return true;
		    };
		}
		
		function splitEnquiryEmpCode(obj)
		{
			var currRow=getRow(obj);
			var currIndex = currRow.rowIndex;
			var entity=obj.value;
			var index=obj.id.substr(18,obj.id.length);
			if(entity.trim()!="")
			{
				var entity_array=entity.split("~");
				if(entity_array.length==4)
				{
					document.getElementById(obj.id).value=trimText(entity_array[0]);
					document.getElementById('enquiryOfficerName'+parseInt(index)).value=trimText(entity_array[1]);
					document.getElementById('empId'+parseInt(index)).value=trimText(entity_array[2]);
					document.getElementById('designation'+parseInt(index)).value=trimText(entity_array[3]);
				}
			}
		}
		
  		function resetForm()
		{
			document.getElementById('msg').innerHTML ='';
		}
		
		function checkRadioButtions()
		{
			if('<s:property value="%{disciplinaryPunishment.isUnauthorisedAbsent}"/>' == '1')
			{
				document.getElementsByName('isUnauthorisedAbsent')[0].checked=true;
				document.getElementById("absentRowLbl").style.display="";
				document.getElementById("absentRowFld").style.display="";
				document.getElementById('absentFrom').value="<s:date name='%{disciplinaryPunishment.absentFrom}' format='dd/MM/yyyy'/>";
				document.getElementById('absentTo').value="<s:date name='%{disciplinaryPunishment.absentTo}' format='dd/MM/yyyy'/>";
			}
			else
			{
				document.getElementsByName('isUnauthorisedAbsent')[1].checked=true;
				document.getElementById("absentRowLbl").style.display="none";
				document.getElementById("absentRowFld").style.display="none";
				document.getElementById("absentFrom").value="";
				document.getElementById("absentTo").value="";
			}
			
			if('<s:property value="%{disciplinaryPunishment.whetherSuspended}"/>' == '1')
			{
				document.getElementsByName('whetherSuspended')[0].checked=true;
				document.getElementById("suspendedRowLbl").style.display="";
				document.getElementById("suspendedRowFld").style.display="";
				document.getElementById('dateOfSuspension').value="<s:date name='%{disciplinaryPunishment.dateOfSuspension}' format='dd/MM/yyyy'/>";
				document.getElementById('dateOfReinstatement').value="<s:date name='%{disciplinaryPunishment.dateOfReinstatement}' format='dd/MM/yyyy'/>";
			}
			else
			{
				document.getElementsByName('whetherSuspended')[1].checked=true;
				document.getElementById("suspendedRowLbl").style.display="none";
				document.getElementById("suspendedRowFld").style.display="none";
				document.getElementById("dateOfSuspension").value="";
				document.getElementById("dateOfReinstatement").value="";
			}
			if('<s:property value="%{disciplinaryPunishment.isSubsistencePaid}"/>' == '1')
			{
				document.getElementsByName('isSubsistencePaid')[0].checked=true;
			}
			else
			{
				document.getElementsByName('isSubsistencePaid')[1].checked=true;
			}
		}
		
		function checkAbsent()
		{
			if(document.getElementsByName('isUnauthorisedAbsent')[0].checked == true)
			{
				document.getElementById("absentRowLbl").style.display="";
				document.getElementById("absentRowFld").style.display="";
				document.getElementById('absentFrom').value="<s:date name='%{disciplinaryPunishment.absentFrom}' format='dd/MM/yyyy'/>";
				document.getElementById('absentTo').value="<s:date name='%{disciplinaryPunishment.absentTo}' format='dd/MM/yyyy'/>";
			
			}
			else
			{
				document.getElementById("absentRowLbl").style.display="none";
				document.getElementById("absentRowFld").style.display="none";
			}
		}
		
		function checkSuspended()
		{
			if(document.getElementsByName('whetherSuspended')[0].checked == true)
			{
				document.getElementById("suspendedRowLbl").style.display="";
				document.getElementById("suspendedRowFld").style.display="";
				document.getElementById('dateOfSuspension').value="<s:date name='%{disciplinaryPunishment.dateOfSuspension}' format='dd/MM/yyyy'/>";
				document.getElementById('dateOfReinstatement').value="<s:date name='%{disciplinaryPunishment.dateOfReinstatement}' format='dd/MM/yyyy'/>";
			}
			else
			{
				document.getElementById("suspendedRowLbl").style.display="none";
				document.getElementById("suspendedRowFld").style.display="none";
			}
		}
		
		function setApproveOrReject(paramValue)
		{
			document.getElementById('approveOrReject').value=paramValue;
		}
		
		function disableAll(){
			if('<s:property value="%{modifyType}"/>'=='menutree'){
				var el = document.forms[0].elements;
				for(var i=0;i<el.length;i++){
					if(el[i].getAttribute('id')!='submitBut' && el[i].getAttribute('id')!='closeBut' && el[i].getAttribute('id')!='dateOfReinstatement' ){
						el[i].setAttribute('disabled',true);
					}
				}
				//To disable all anchors
				var anchors = document.getElementsByTagName("A");
				for(var i=0;i<anchors.length;i++){
					if(anchors[i].getAttribute('id')!='dateOfReInstAnchor'){
						anchors[i].setAttribute('onclick','return false;');
					}
				}
			}
		}
		
		function enableAll(){
			if('<s:property value="%{modifyType}"/>'=='menutree'){
				var el = document.forms[0].elements;
				for(var i=0;i<el.length;i++){
					el[i].disabled=false;
				}
			}
		}
		
		/**function generateExceptionForDisciplinary()
	    { 
		     var winLocUrl = document.getElementById('exceptionActionUrl').value+document.getElementById('id').value+"&disciplinaryEmpId="+document.getElementById('disEmpId').value;
		     window.location=winLocUrl;
		}**/
		
		function formExceptionURL()
	    { 
		     document.getElementById('exceptionActionUrl').value = document.getElementById('exceptionActionUrl').value+"&Id="+document.getElementById('id').value+"&disciplinaryEmpId="+document.getElementById('employeeId').value;
		     //alert(document.getElementById('exceptionActionUrl').value);
		}
		
		function genericOnload()
		{
			if('<s:property value="%{wfStatus}"/>'=='APPROVER_REJECTED')
			{
				populateApproverEmp();
			}
		}
		function callDMSFileSearch()
		{							
			var mozillaFirefox=document.getElementById&&!document.all;
			if(mozillaFirefox)
			{
				window.open("<%="/dms/dms/searchFile.action?module=external"%>","","height=500,width=600px,scrollbars=yes,left=30,top=30,status=yes");
			}
			else
			{
				window.open("/dms/dms/searchFile.action?module=external");
			}



		}
				
		function viewFile(fileId)
		{		     
		    if(document.getElementById("fileId").value != "")
		    	fileId =document.getElementById("fileId").value;		     
		     var mozillaFirefox=document.getElementById&&!document.all;
		     var url="/dms/dms/genericFile.action?fileId=";
		     if(fileId != null && fileId != "")
		     {
			 url=url+fileId;		    
				 if(mozillaFirefox)
				 {
					window.open(url,"View File","height=500,width=600px,scrollbars=yes,left=30,top=30,status=yes");
				 }
				 else
				 {
					window.open(url);
				 }
		     }		     
		}
		var fileUpdater=function(fileId)
		{
			 if(fileId != null && fileId != "")
		     {
			document.getElementById("fileImg").style.display="";
		     }
		}
		function hideFileImage()
		{
			var fileId =document.getElementById("fileId").value;			
			if((fileId == null || fileId == "") && ('<s:property value="%{disciplinaryPunishment.fileId}"/>' == ""))
				document.getElementById("fileImg").style.display="none";
		}

  </script>
  
  <body onload="checkRadioButtions();disableAll();genericOnload();hideFileImage();">
  
   	<s:form action="disciplinaryPunishment" theme="simple" onsubmit="enableAll();">  
   		<s:token/>
  		<div class="formmainbox">
			<div class="insidecontent">
		  		<div class="rbroundbox2">
					<div class="rbtop2">
						<div></div>
					</div>
			  		<div class="rbcontent2">
			  			<div class="datewk">	
							<span class="bold">Today:</span><egovtags:now/>
						</div>
			  			<span id="msg"  style="height:1px">
							<s:actionerror cssStyle="font-size:12px;font-weight:bold;" cssClass="mandatory"/>  
							<s:fielderror cssStyle="font-size:12px;font-weight:bold;" cssClass="mandatory"/>
							<s:actionmessage cssClass="actionmessage"/>
						</span>

			  			<table width="100%" cellpadding ="0" cellspacing ="0" border = "0">
	  						<tbody>
									<table  width="100%" border="0" cellpadding="0" cellspacing="0" id="disciplinaryPunishmentHeader" name="disciplinaryPunishmentHeader">
										<tr>
											<td colspan="4" class="headingwk">
												<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
								              	<div class="headplacer"><s:text name="Heading"/></div>
								        	</td>
								        </tr>

										<tr>
											
											<td class="whiteboxwk"><s:hidden name="id" id="id" value="%{disciplinaryPunishment.id}"/><s:text name="EmpName"/> </td>
											<td class="whitebox2wk"><s:hidden name="disEmpId" id="disEmpId" /><s:hidden name="employeeId" id="employeeId" value="%{disciplinaryPunishment.employeeId.idPersonalInformation}"/><s:text name="%{disciplinaryPunishment.employeeId.employeeName}" /></td>
											<td class="whiteboxwk"><s:text name="EmpCode"/> </td>
											<td class="whitebox2wk">
												<s:text name="%{disciplinaryPunishment.employeeId.code}" />
											</td>											
										</tr>
										<tr>
											<td class="greyboxwk"><span class="mandatory">*</span><s:text name="NatureOfAllegation"/> </td>
											<td class="greybox2wk"><s:textfield name="natureOfAlligations" id ="natureOfAlligations" value="%{disciplinaryPunishment.natureOfAlligations}"/></td>
											<td class="greyboxwk"><span class="mandatory">*</span><s:text name="ChargeMemoNum"/> </td>
											<td class="greybox2wk"><s:textfield name="chargeMemoNo" id ="chargeMemoNo" value="%{disciplinaryPunishment.chargeMemoNo}"/></td>
										</tr>
										<tr>
											<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="ChargeMemoDt"/> </td>
											<td class="whitebox2wk">
												<s:date name='%{disciplinaryPunishment.chargeMemoDate}' format='dd/MM/yyyy' var="chrgMemoDt"/>
												<s:textfield name="chargeMemoDate" id ="chargeMemoDate" value="%{chrgMemoDt}" onblur = "validateDateFormat(this);" size="10" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"></s:textfield>
												<a href="javascript:show_calendar('forms[0].chargeMemoDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;" ><img src="<c:url value='../common/image/calendar.png' />" border="0"></a>
											</td>
											<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="ChargeMemoServDt"  /> </td>
											<td class="whitebox2wk">
												<s:date name='%{disciplinaryPunishment.chargeMemoServedDate}' format='dd/MM/yyyy' var="chrgMemoSerDt"/>
												<s:textfield name="chargeMemoServedDate" id ="chargeMemoServedDate" value="%{chrgMemoSerDt}" onblur = "validateDateFormat(this);" size="10" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
												<a href="javascript:show_calendar('forms[0].chargeMemoServedDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border="0"></a>
											</td>
										</tr>
										<tr>
											<td class="greyboxwk"><s:text name="NatureOfDisp"/> </td>
											<td class="greybox2wk"><s:textfield name="natureOfDisposal" id ="natureOfDisposal" value="%{disciplinaryPunishment.natureOfDisposal}"/></td>
											<td class="greyboxwk"><s:text name="ApplicationNo"/> </td>
											<td class="greybox2wk"><s:textfield  name="applicationNumber" id="applicationNumber" value="%{disciplinaryPunishment.applicationNumber}" readonly="true" /></td>
										</tr>
										<tr>
											<td class="whiteboxwk"><s:text name="UnauthorisedAbs"/></td>
											<td class="whitebox2wk">
												<s:text name="Yes"/>&nbsp;
													<input type="radio" name="isUnauthorisedAbsent" id="isUnauthorisedAbsent" value="1" onclick="checkAbsent(this.value)"> 
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<s:text name="No"/>&nbsp;
													<input type="radio" name="isUnauthorisedAbsent" id="isUnauthorisedAbsent" value="0" onclick="checkAbsent(this.value)" checked="true" > 
												</td>
											<td class="whiteboxwk" id="absentRowLbl"><s:text name="PeriodOfAbs"/> </td>
											<td class="whitebox2wk" id="absentRowFld">
												<s:text name="FromDt"/><span class="mandatory">*</span>
												<s:date name='%{disciplinaryPunishment.absentFrom}' format='dd/MM/yyyy' var="absFrom"/>
												<s:textfield name="absentFrom" id ="absentFrom" value="%{absFrom}" onblur = "validateDateFormat(this);" size="10" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" />
												<a href="javascript:show_calendar('forms[0].absentFrom');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border="0"></a>
												<br/>
												<s:text name="ToDt"/><span class="mandatory">*</span>
												<s:date name='%{disciplinaryPunishment.absentTo}' format='dd/MM/yyyy' var="absTo"/>
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:textfield name="absentTo" id ="absentTo" value="%{absTo}" onblur = "validateDateFormat(this);" size="10" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" />
												<a href="javascript:show_calendar('forms[0].absentTo');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border="0"></a>
											</td>
										</tr>
										<tr>
											<td class="greyboxwk"><s:text name="WhetherSuspended"/></td>
											<td class="greybox2wk">
												<s:text name="Yes"/>&nbsp;
												<input type="radio" name="whetherSuspended" id="whetherSuspended" value="1"  onclick="checkSuspended()"> 
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<s:text name="No"/>&nbsp;
												<input type="radio" name="whetherSuspended" id="whetherSuspended"  value="0" onclick="checkSuspended()" checked="true" > 
											</td>
											<td class="greyboxwk" id="suspendedRowLbl"><s:text name="PeriodOfSuspended"/> </td>
											<td class="greybox2wk" id="suspendedRowFld">
												<s:text name="FromDt"/><span class="mandatory">*</span> 
												<s:date name='%{disciplinaryPunishment.dateOfSuspension}' format='dd/MM/yyyy' var="dtOfSus"/>
												<s:textfield name="dateOfSuspension" id ="dateOfSuspension" value="%{dtOfSus}" onblur = "validateDateFormat(this);" size="10" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" />
												<a href="javascript:show_calendar('forms[0].dateOfSuspension');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border="0"></a>
												<br/>
												<s:text name="ToDt"/>
												<s:date name='%{disciplinaryPunishment.dateOfReinstatement}' format='dd/MM/yyyy' var="dtOfReInst"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:textfield name="dateOfReinstatement" id ="dateOfReinstatement" value="%{dtOfReInst}" onblur = "validateDateFormat(this);" size="10" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" />
												<a id="dateOfReInstAnchor" href="javascript:show_calendar('forms[0].dateOfReinstatement');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border="0"></a>
											</td>
										</tr>
										<tr>
											<td class="whiteboxwk"><s:text name="WhetherSubsistencePd"/> </td>
											<td class="whitebox2wk" colspan="3">
												<s:text name="Yes"/>&nbsp;
												<input type="radio" value="1" name="isSubsistencePaid" id="isSubsistencePaid" onclick="checkSuspended()"> 
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<s:text name="No"/>&nbsp;
												<input name="isSubsistencePaid" id="isSubsistencePaid"  type="radio" value="0"  checked="true" onclick="checkSuspended()">
											</td>
										</tr>
									</table>
									<table  width="100%" border="0" cellpadding="0" cellspacing="0" id="enquiryDtl" name="enquiryDtl">
										<tr>
											<td colspan="5" class="headingwk">
												<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
								              	<div class="headplacer"><s:text name="EnquiryHeading"/></div>
								        	</td>
								        </tr>
								    </table>
									<jsp:include page="disciplinaryPunishmentLoadYITable.jsp"/>
									<div class="yui-skin-sam" align="center">
								       <div id="enqDetailTable"></div>
								    </div>
								    <script>
										makeEnqDetailTable();
										document.getElementById('enqDetailTable').getElementsByTagName('table')[0].width="100%"
									</script>
									<div id="empCodecontainer"></div>
									<table  width="100%" border="0" cellpadding="0" cellspacing="0" id="otherDtl" name="otherDtl">
										<tr>
											<td colspan="4" class="headingwk">
												<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
								              	<div class="headplacer"><s:text name="OthHeading"/></div>
								        	</td>
								        </tr>
								        <tr>
											<td class="whiteboxwk"><s:text name="PunishmentOrderDate"/> </td>
											<td class="whitebox2wk">
												<s:date name='%{disciplinaryPunishment.punisOrderDate}' format='dd/MM/yyyy' var="pOdrDt"/>
												<s:textfield name="punisOrderDate" id ="punisOrderDate" value="%{pOdrDt}" onblur = "validateDateFormat(this);" size="10" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" />
												<a href="javascript:show_calendar('forms[0].punisOrderDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border="0"></a>
											</td>
											<td class="whiteboxwk"><s:text name="UnauthoriseabsperReg"/> </td>
											<td class="whitebox2wk"><s:textfield name="howSuspention" id ="howSuspention" value="%{disciplinaryPunishment.howSuspention}"/></td>
										</tr>
										<tr>
											<td class="greyboxwk"><s:text name="NatureOfPunish"/> </td>
											<td class="greybox2wk"><s:textfield name="natureOfPunisment" id ="natureOfPunisment" value="%{disciplinaryPunishment.natureOfPunisment}"/></td>
											<td class="greyboxwk"><s:text name="PunishmentEffDate"/> </td>
											<td class="greybox2wk">
												<s:date name='%{disciplinaryPunishment.punEffectDate}' format='dd/MM/yyyy' var="pEffDt"/>
												<s:textfield name="punEffectDate" id ="punEffectDate" value="%{pEffDt}" onblur = "validateDateFormat(this);" size="10" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
												<a href="javascript:show_calendar('forms[0].punEffectDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border="0"></a>
											</td>
										</tr>
										<tr>																											
											<td class="whiteboxwk"><s:text name="FileAttached"/>
											<td class="whitebox2wk"><s:hidden name="fileId" id="fileId"/><s:hidden name="fileNo" id="fileNo"/>																						
											<img id="fileImg" name="fileImg"  src="../images/download.gif" name="ViewFile" onclick="viewFile('<s:property value="%{disciplinaryPunishment.fileId}"/>')" title="View File" />
											<input type="button" value="Attach" onclick="javascript:callDMSFileSearch();" class="buttonfinal"/>
											</td>																																																																														
										</tr>										
										<tr>
											<td class="greyboxwk"><s:text name="StatusLbls"/> : </td>
											<td class="greybox2wk"><s:hidden name="status" id ="status" value="%{disciplinaryPunishment.status.id}" />
											<s:text name="%{disciplinaryPunishment.status.code}" /></td>
											<td class="greyboxwk"><s:hidden name="state" id ="state" value="%{disciplinaryPunishment.state.id}" /></td>
										</tr>
										<s:if test="%{modifyType=='workflow' && wfStatus=='CREATED'}">
											<tr>
												<td class="whiteboxwk"><s:text name="SanctionNos"/><span class="mandatory">*</span></td>
												<td class="whitebox2wk"><s:textfield name="sanctionNo" id ="sanctionNo" value="%{disciplinaryPunishment.sanctionNo}" /></td>
												<td class="whiteboxwk"><s:text name="Comments"/> : </td>
												<td class="whitebox2wk"><s:textarea name="comments" id ="comments" value="" cols="30" rows="2"/></td>
											</tr>
										</s:if>
										<s:elseif test="%{modifyType=='workflow' && wfStatus=='APPROVER_REJECTED'}">
											<tr>
												<td class="whiteboxwk"><s:text name="SanctionNos"/> </td>
												<td class="whitebox2wk"><s:textfield name="sanctionNo" id ="sanctionNo" value="%{disciplinaryPunishment.sanctionNo}" readonly="true"/></td>
												<td class="whiteboxwk"><s:text name="Comments"/></td>
												<td class="whitebox2wk"><s:textarea name="comments" id ="comments" value="" cols="30" rows="2"/></td>
											
											</tr>
										</s:elseif>
										<s:else>
											<tr>
												<td class="whiteboxwk"><s:text name="SanctionNos"/></td>
												<td class="whitebox2wk" colspan="3"><s:textfield name="sanctionNo" id ="sanctionNo" value="%{disciplinaryPunishment.sanctionNo}" readonly="true"/></td>
											</tr>
										</s:else>
										
										<s:if test="%{typeOfDisciplinaryPunishmentWF=='Manual'}">
											<s:if test="%{wfStatus=='APPROVER_REJECTED'}">   		
  												<%@ include file='/WEB-INF/jsp/common/manualWfApproverSelection.jsp'%>
  											</s:if>
  										</s:if>
										
								    </table>

									<tr>
					               		<td colspan="3" class="shadowwk"></td>
					            	</tr>
								
									<tr>
								 		<td colspan="3" ><div align="right" class="mandatory">* Mandatory Fields</div></td>
									</tr>
					            
					           		<tr>
							       		<td align="center" colspan="3"> 
											<s:hidden  name="mode" value="modify" />
											<s:hidden  name="wfStatus" value="%{wfStatus}" />
											<s:hidden  name="modifyType" value="%{modifyType}" />
											<s:hidden  name="approveOrReject" id="approveOrReject" value=""/>
								   		</td>
					           		</tr>
						      </tbody>
						</table>
					</div><div class="rbbot2"><div></div></div>
				</div>
			</div>
		</div>
		<div class="buttonholderwk">
			<s:submit method="editDisciplinaryPunishmentOnly" id="submitBut" value="Modify" cssClass="buttonfinal"/>
			<s:if test="%{modifyType=='workflow' && wfStatus=='CREATED'}">
				<s:hidden id="exceptionActionUrl" name="exceptionActionUrl" value="%{exceptionActionUrl}"/>
				<s:submit method="createExceptionAndApprove" cssClass="buttonfinal"  value="Create Payslip Exception And Approve" onclick="formExceptionURL()" />
				<s:submit method="modifyAndApproveWithoutException" value="Approve Without Payslip Exception" onclick="setApproveOrReject('approve');" cssClass="buttonfinal"/>
				<s:submit method="rejectDisciplinaryPunishment" value="Reject" onclick="setApproveOrReject('reject');" cssClass="buttonfinal"/>
			</s:if>
			<s:if test="%{modifyType=='workflow' && wfStatus=='APPROVER_REJECTED'}">
				<s:submit method="modifyAndApproveWithoutException" value="Approve" onclick="setApproveOrReject('approve');" cssClass="buttonfinal"/>
				<s:submit method="rejectDisciplinaryPunishment" value="Reject" onclick="setApproveOrReject('reject');" cssClass="buttonfinal"/>
			</s:if>
			<!--s:reset name="cancelBut" id="cancelBut" value="Cancel" onclick="resetForm()" cssClass="buttonfinal"/-->
			<input type="button" name="closeBut" id="closeBut" value="Close" onclick="javascript:window.close()" class="buttonfinal"/> 
		</div>
	</s:form>
</body>
</html>	