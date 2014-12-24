<!--
	Program Name : disciplinaryPunishment-list.jsp
	Author		: Jagadeesan M
	Created	on	: 1-08-2010
	Purpose 	: To view list of disciplinary punishment for an employee
 -->

<%@ page import="java.util.*"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/struts-tags.tld" prefix="s"%> 
<html>
  <head>
	  
	  <title>List of Disciplinary Punishment</title>
	  
	  <script type="text/javascript">
	  
	  	function viewModifyDisciplinaryInfo(disId,empId,mode)
		{			
	    	//alert(disId+"---"+empId+'----'+mode);
	    	var url='${pageContext.request.contextPath}/disciplinaryPunishment/disciplinaryPunishment!loadToViewOrModify.action?mode='+mode+'&modifyType=menutree&disciplinaryPunishment.id='+disId+'&disEmpId='+empId;
			//alert(url);
			window.open(url,'View/Modify Disciplinary Punishment','width=800,height=600,top=150,left=80,scrollbars=yes,resizable=yes');
	    }
	    
	  </script>
   
  </head>
  
  <body onload="">
  
   	<s:form action="disciplinaryPunishment" theme="simple" >  
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

						<table  width="100%" border="0" cellpadding="0" cellspacing="0" id="disciplinaryPunishmentList" name="disciplinaryPunishmentList">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
					              	<div class="headplacer"><s:text name="Heading"/></div>
					        	</td>
					        </tr>

							<tr>
								<td class="whiteboxwk"><s:text name="EmpName"/> : </td>
								<td class="whitebox2wk"><s:hidden name="disEmpId" /><s:text name="%{disciplinaryPunishment.employeeId.employeeName}" /></td>
								<td class="whiteboxwk"><s:text name="EmpCode"/> :  </td>
								<td class="whitebox2wk" width="30%">
									<s:text name="%{disciplinaryPunishment.employeeId.code}" />
								</td>											
							</tr>
						</table>
						<div style="overflow-x:auto">
						<table  width="100%" border="0" cellpadding="0" cellspacing="0" id="list" name="list" >
							<s:set var="recCount" value="0"/>
							<s:if test="!disciplinaryPunishmentList.isEmpty()">
								<s:iterator status="status" var="currentObj" value="%{disciplinaryPunishmentList}">
									<s:if test="%{status.code=='Approved' || #attr.mode=='view'}">
										<s:set var="recCount" value="%{#recCount+1}"/>
										<s:if test="%{#recCount==1}">
											<tr>
											<td class="greybox2wk"><b><s:text name="NatureOfAllegation"/></b></td>
											<td class="greybox2wk"><b><s:text name="ChargeMemoDt"/></b></td>
											<td class="greybox2wk"><b><s:text name="UnauthorisedAbs"/></b></td>
											<td class="greybox2wk"><b><s:text name="WhetherSuspended"/></b></td>
											<td class="greybox2wk" ><b><s:text name="WhetherSubsistencePd" /></b></td>
											<td class="greybox2wk"><b><s:text name="PunishmentOrderDate"/></b></td>
											<td class="greybox2wk"><b><s:text name="PunishmentEffDate"/></b></td>
											<td class="greybox2wk"><b><s:text name="StatusLbls"/></b></td>
											<td class="greybox2wk"><b></b></td>
							        	</tr>
										</s:if>
										<tr>
											<td class="whitebox2wk"><s:hidden id="id" name="id" value="%{id}" /><s:property value="%{natureOfAlligations}" /></td>
											<td class="whitebox2wk"><s:date name="%{chargeMemoDate}" format="dd/MM/yyyy" /></td>
											<td class="whitebox2wk">
												<s:if test="%{isUnauthorisedAbsent=='0'}"><s:text name="No"/></s:if>
												<s:if test="%{isUnauthorisedAbsent=='1'}"><s:text name="Yes"/></s:if>
											</td>
											<td class="whitebox2wk">
												<s:if test="%{whetherSuspended=='0'}"><s:text name="No"/></s:if>
												<s:if test="%{whetherSuspended=='1'}"><s:text name="Yes"/></s:if>
											</td>
											<td class="whitebox2wk">
												<s:if test="%{isSubsistencePaid=='0'}"><s:text name="No"/></s:if>
												<s:if test="%{isSubsistencePaid=='1'}"><s:text name="Yes"/></s:if>
											</td>
											<td class="whitebox2wk"><s:date name="%{punisOrderDate}" format="dd/MM/yyyy"  /></td>
											<td class="whitebox2wk"><s:date name="%{punEffectDate}" format="dd/MM/yyyy" /></td>
											<td class="whitebox2wk"><s:property value="%{status.code}" /></td>
											<td class="whitebox2wk"><a href="#" onclick="viewModifyDisciplinaryInfo('<s:property value='#attr.disciplinaryPunishmentList[#status.index].id' />','<s:property value='#attr.disciplinaryPunishment.employeeId.idPersonalInformation' />','<s:property value='#attr.mode'/>')"><s:text name="%{#attr.mode}"/></a></td>
											
										</tr>
									</s:if>
					        	</s:iterator>
					        </s:if>
					       	<s:if test="%{#recCount==0}">
								<tr>
									<td colspan="9" class="message">Nothing found to display</td>
								</tr>
							</s:if>
					    </table>
					    </div>
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
