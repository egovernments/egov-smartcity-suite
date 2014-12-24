<!--
	Program Name : disciplinaryPunishment-view.jsp
	Author		: Jagadeesan M
	Created	on	: 31-07-2010
	Purpose 	: To view a disciplinary punishment.
 -->

<%@ page import="java.util.*"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/struts-tags.tld" prefix="s"%> 
<html>
  <head>

	<title>View Disciplinary Punishment</title>
	  
	<script type="text/javascript">
	  
	  	function checkRadioButtions()
		{
			if('<s:property value="%{disciplinaryPunishment.isUnauthorisedAbsent}"/>' == '1')
			{
				document.getElementsByName('isUnauthorisedAbsent')[0].checked=true;
				document.getElementById("absentRowLbl").style.display="";
				document.getElementById("absentRowFld").style.display="";
			}
			else
			{
				document.getElementsByName('isUnauthorisedAbsent')[1].checked=true;
				document.getElementById("absentRowLbl").style.display="none";
				document.getElementById("absentRowFld").style.display="none";
			}
			
			if('<s:property value="%{disciplinaryPunishment.whetherSuspended}"/>' == '1')
			{
				document.getElementsByName('whetherSuspended')[0].checked=true;
				document.getElementById("suspendedRowLbl").style.display="";
				document.getElementById("suspendedRowFld").style.display="";
			}
			else
			{
				document.getElementsByName('whetherSuspended')[1].checked=true;
				document.getElementById("suspendedRowLbl").style.display="none";
				document.getElementById("suspendedRowFld").style.display="none";
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

		function refreshInbox()
		{
			if(opener.top.document.getElementById('inboxframe')!=null)
			{    	
				if(opener.top.document.getElementById('inboxframe').contentWindow.name!=null && opener.top.document.getElementById('inboxframe').contentWindow.name=="inboxframe")
				{    		
				opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
				}
			
			}
		}
		function popupWindow(fileId)
		{			
			var mozillaFirefox=document.getElementById&&!document.all;
			if(mozillaFirefox)
			{
				window.open("<%="/dms/dms/genericFile.action?fileId="%>"+fileId,"","height=500,width=600px,scrollbars=yes,left=30,top=30,status=yes");
			}
			else
			{
				window.open("/dms/dms/genericFile.action?fileId="+fileId);
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

	  </script>
   
  </head>
  
  <body onload="checkRadioButtions();refreshInbox();">
  
   	<s:form action="disciplinaryPunishment" theme="simple" >  
   		
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

						<table  width="100%" border="0" cellpadding="0" cellspacing="0" id="disciplinaryPunishmentHeader" name="disciplinaryPunishmentHeader">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
					              	<div class="headplacer"><s:text name="Heading"/></div>
					        	</td>
					        </tr>

							<tr>
								<td class="whiteboxwk"><s:text name="EmpName"/> : </td>
								<td class="whitebox2wk"><s:hidden name="disEmpId" id="disEmpId" value="%{disciplinaryPunishment.employeeId.idPersonalInformation}"/><s:hidden name="id" id="id" value="%{disciplinaryPunishment.id}"/><s:text name="%{disciplinaryPunishment.employeeId.employeeName}" /></td>
								<td class="whiteboxwk"><s:text name="EmpCode"/> :  </td>
								<td class="whitebox2wk">
									<s:text name="%{disciplinaryPunishment.employeeId.code}" />
								</td>											
							</tr>
							<tr>
								<td class="greyboxwk"><s:text name="NatureOfAllegation"/>  : </td>
								<td class="greybox2wk"><s:text name="%{disciplinaryPunishment.natureOfAlligations}" /></td>
								<td class="greyboxwk"><s:text name="ChargeMemoNum"/> :  </td>
								<td class="greybox2wk"><s:text name="%{disciplinaryPunishment.chargeMemoNo}" /></td>
							</tr>
							<tr>
								<td class="whiteboxwk"><s:text name="ChargeMemoDt"/>  : </td>
								<td class="whitebox2wk"><s:date name="%{disciplinaryPunishment.chargeMemoDate}" format="dd/MM/yyyy"/></td>
								<td class="whiteboxwk"><s:text name="ChargeMemoServDt"  />  : </td>
								<td class="whitebox2wk"><s:date name="%{disciplinaryPunishment.chargeMemoDate}" format="dd/MM/yyyy"/></td>
							</tr>
							<tr>
								<td class="greyboxwk"><s:text name="NatureOfDisp"/> :  </td>
								<td class="greybox2wk"><s:text name="%{disciplinaryPunishment.natureOfDisposal}" /></td>
								<td class="greyboxwk"><s:text name="ApplicationNo"/> :  </td>
								<td class="greybox2wk"><s:text name="%{disciplinaryPunishment.applicationNumber}" /></td>
							</tr>
							<tr>
								<td class="whiteboxwk"><s:text name="UnauthorisedAbs"/> : </td>
								<td class="whitebox2wk">
									<s:text name="Yes"/>&nbsp;
										<input type="radio" name="isUnauthorisedAbsent" id="isUnauthorisedAbsent" value="1" disabled="true" > 
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<s:text name="No"/>&nbsp;
										<input type="radio" name="isUnauthorisedAbsent" id="isUnauthorisedAbsent" value="0" disabled="true"> 
									</td>
								<td class="whiteboxwk" id="absentRowLbl"><s:text name="PeriodOfAbs"/> :  </td>
								<td class="whitebox2wk" id="absentRowFld">
									<s:text name="FromDt"/>&nbsp;: <s:date name="%{disciplinaryPunishment.absentFrom}" format="dd/MM/yyyy"/>
									<br/>
									<s:text name="ToDt"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: <s:date name="%{disciplinaryPunishment.absentTo}" format="dd/MM/yyyy"/>
								
								</td>
							</tr>
							<tr>
								<td class="greyboxwk"><s:text name="WhetherSuspended"/>  : </td>
								<td class="greybox2wk">
									<s:text name="Yes"/>&nbsp;
									<input type="radio" name="whetherSuspended" id="whetherSuspended" disabled="true" value="1" > 
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<s:text name="No"/>&nbsp;
									<input type="radio" name="whetherSuspended" id="whetherSuspended"  disabled="true" value="0" > 
								</td>
								<td class="greyboxwk" id="suspendedRowLbl"><s:text name="PeriodOfSuspended"/>  </td>
								<td class="greybox2wk" id="suspendedRowFld">
									<s:text name="FromDt"/>&nbsp;: <s:date name="%{disciplinaryPunishment.dateOfSuspension}" format="dd/MM/yyyy" />
									<br/>
									<s:text name="ToDt"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: <s:date name="%{disciplinaryPunishment.dateOfReinstatement}" format="dd/MM/yyyy"/>
								</td>
							</tr>
							<tr>
								<td class="whiteboxwk"><s:text name="WhetherSubsistencePd"/>  :  </td>
								<td class="whitebox2wk" colspan="3">
									<s:text name="Yes"/>&nbsp;
									<input type="radio" name="isSubsistencePaid" id="isSubsistencePaid" value="1"  disabled="true"> 
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<s:text name="No"/>&nbsp;
									<input name="isSubsistencePaid" id="isSubsistencePaid"  type="radio" value="0"  disabled="true" >
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
					        <s:if test="!disciplinaryPunishment.egpimsDetOfEnquiryOfficers.isEmpty()">
						   		 <tr>
									<td class="greybox2wk"><b><s:text name="EnquiryOfficerCode"/></b></td>
									<td class="greybox2wk"><b><s:text name="EnquiryOfficerName"/></b></td>
									<td class="greybox2wk"><b><s:text name="EODesignation"/></b></td>
									<td class="greybox2wk"><b><s:text name="EONominatedDate"/></b></td>
									<td class="greybox2wk" ><b><s:text name="EOReportDate" /></b></td>
					        	</tr>
								<s:iterator status="status" value="%{disciplinaryPunishment.egpimsDetOfEnquiryOfficers}">
									<s:if test="%{empId!=null}">
									<tr>
										<td class="whitebox2wk"><s:property value="%{enquiryOfficerCode}" /></td>
										<td class="whitebox2wk"><s:property value="%{enquiryOfficerName}" /></td>
										<td class="whitebox2wk"><s:text name="%{designation}" /></td>
										<td class="whitebox2wk"><s:date name="%{nominatedDate}" format="dd/MM/yyyy"/></td>
										<td class="whitebox2wk"><s:date name="%{reportDate}" format="dd/MM/yyyy"/></td>
									</tr>
									</s:if>
					        	</s:iterator>
					        </s:if>
					    </table>
					    <table  width="100%" border="0" cellpadding="0" cellspacing="0" id="otherDtl" name="otherDtl">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
					              	<div class="headplacer"><s:text name="OthHeading"/></div>
					        	</td>
					        </tr>
					        <tr>
								<td class="whiteboxwk"><s:text name="PunishmentOrderDate"/>  : </td>
								<td class="whitebox2wk"><s:date name="%{disciplinaryPunishment.punisOrderDate}" format="dd/MM/yyyy"/></td>
								<td class="whiteboxwk"><s:text name="UnauthoriseabsperReg"/> :  </td>
								<td class="whitebox2wk"><s:text name="%{disciplinaryPunishment.howSuspention}"/></td>
							</tr>
							<tr>
								<td class="greyboxwk"><s:text name="NatureOfPunish"/> :  </td>
								<td class="greybox2wk"><s:text name="%{disciplinaryPunishment.natureOfPunisment}"/></td>
								<td class="greyboxwk"><s:text name="PunishmentEffDate"/> :  </td>
								<td class="greybox2wk"><s:date name="%{disciplinaryPunishment.punEffectDate}" format="dd/MM/yyyy"/></td>
							</tr>
							<s:if test="%{disciplinaryPunishment.fileId!=null}">							
							<tr>
								<td class="whiteboxwk"><s:text name="FileAttached"/>
								<td class="whitebox2wk"><s:hidden name="fileId" id="fileId"/><s:hidden name="fileNo" id="fileNo"/>
								<img id="fileImg" name="fileImg"  src="../images/download.gif" name="ViewFile" onclick="viewFile('<s:property value="%{disciplinaryPunishment.fileId}"/>')" title="View File" />								
								</td>											
							</tr>	
							</s:if>
							<tr>
								<td class="greyboxwk"><s:text name="SanctionNos"/> : </td>
								<td class="greybox2wk"><s:text name="%{disciplinaryPunishment.sanctionNo}"/></td>
								<td class="greyboxwk"><s:text name="StatusLbls"/> : </td>
								<td class="greybox2wk"><s:text name="%{disciplinaryPunishment.status.code}" /></td>
							</tr>
							<tr>
								<td class="whiteboxwk"><s:text name="Comments"/> : </td>
								<td class="whitebox2wk" colspan="3"><s:text name="%{disciplinaryPunishment.state.text1}"/></td>
							</tr>
					    </table>				
					</div>
				</div>
			</div>
		</div>
		<div class="buttonholderwk">
			<input type="button" value="Close" onclick="javascript:window.close()" class="buttonfinal"/> 
		</div>
	</s:form>
</body>
</html>	