		
<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="java.util.*,
		org.egov.infstr.utils.*,
		org.apache.log4j.Logger,
		org.egov.pims.*,
		org.egov.pims.dao.*,
		org.egov.pims.service.*,
		org.egov.pims.utils.*,
		org.hibernate.LockMode,
		org.egov.pims.model.*,
		org.egov.infstr.commons.*,
		org.egov.pims.commons.client.*,
		org.egov.infstr.commons.dao.*,
		org.egov.infstr.utils.*,
		org.egov.lib.rjbac.dept.*,
		org.egov.lib.address.model.*,
		java.math.BigDecimal,
		org.egov.lib.address.dao.*,
		java.text.SimpleDateFormat,
		java.util.StringTokenizer,
		org.egov.pims.client.*"
		%>
		<%
						Set serviceSet = null;
						Set probationSet = null;
						Set regularisationSet = null;
						PersonalInformation egpimsPersonalInformation =null;
						List commentDateList = new ArrayList();
						List probList = new ArrayList();

						String id ="";
						if(request.getAttribute("employeeOb")!=null)
						{

						//id = request.getParameter("Id").trim();

						egpimsPersonalInformation = (PersonalInformation)request.getAttribute("employeeOb");

						}
						else
						{
						egpimsPersonalInformation = new PersonalInformation();

						}

												
						if(egpimsPersonalInformation.getEgpimsServiceHistory().isEmpty()) {
							serviceSet = new HashSet();
							serviceSet.add(new ServiceHistory());
							commentDateList.addAll(serviceSet);
						} else
						{
							serviceSet = egpimsPersonalInformation.getEgpimsServiceHistory();
							commentDateList.addAll(egpimsPersonalInformation.getEgpimsServiceHistory());
							Collections.sort(commentDateList,ServiceHistory.commentDateComparator);	
						}

						//Probation
						if(egpimsPersonalInformation.getEgpimsProbations().isEmpty()) {
							probationSet = new HashSet();
							probationSet.add(new Probation());
							probList.addAll(probationSet);
						} else
						{
							probationSet = egpimsPersonalInformation.getEgpimsProbations();
							probList.addAll(probationSet);
							Collections.sort(probList);
						}
						
						//Regularization

						if( egpimsPersonalInformation.getEgpimsRegularisations().isEmpty())
						{
							regularisationSet = new HashSet();
							regularisationSet.add(new Regularisation());
						} else
							regularisationSet = egpimsPersonalInformation.getEgpimsRegularisations();

						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
 

				%>




		<br>
		<table   cellpadding ="0" cellspacing ="0" border = "0"  name ="ServiceTable" id ="ServiceTable" >
		<tbody>

		<tr>
                <td colspan="9" class="headingwk"><div class="arrowiconwk"><img src="<%=request.getContextPath()%>/common/image/arrow.gif" /></div>
                  <div class="headplacer"><bean:message key="ServiceLedger"/></div></td>
         </tr>

		<tr>
		
		<td  width="4%" class="tablesubheadwk"><bean:message key="slNo"/></font></td>
		<td   class="tablesubheadwk" width="40%" ><bean:message key="ServiceEntryDesc"/><SPAN class="leadon">*</SPAN></td>
		<td   class="tablesubheadwk" width="10%" ><bean:message key="DateGeneral"/><SPAN class="leadon">*</SPAN></td>
		<td   class="tablesubheadwk" width="20%" ><bean:message key="Comments"/></td>
		<td   class="tablesubheadwk" width="6%" ><bean:message key="OrderNo"/></td>
		<td    class="tablesubheadwk" width="10%" ><bean:message key="PayScale"/></td>
		<%
		if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
		{
		%>
			<td   class="tablesubheadwk" width="5%" ><bean:message key="DocAttach"/></td>
		<%
		}
		else
		{
		%>
			<td   class="tablesubheadwk" width="5%" ><bean:message key="DocView"/></td>
		<%
		}

		if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
		{
		%>
		<td width="5%" class="tablesubheadwk">Add/Del</td>
		<%}%>

		</tr>

		<%
		Iterator itr1 = commentDateList.iterator();
		for(int i=0;itr1.hasNext();i++)
		{
		ServiceHistory egpimsService = (ServiceHistory)itr1.next();
		%>
		
		<tr id="ServiceRow">
		
		<td class="whitebox3wk"><div align="center">
		<input type ="hidden" size="10" name="idService" id="idService" value="<%=egpimsService.getIdService()==null?"0":egpimsService.getIdService().toString()%>" />
		<%
		String modermr=((String)(session.getAttribute("viewMode"))).trim();
		if(modermr.equalsIgnoreCase("create") )
		{
		%>
		<input type="text" size="1" name="serialNo" id="serialNo" value="1" disabled="true" >
		
		<%}else if(modermr.equalsIgnoreCase("modify")){ %>

		<input type="text" class="selectwk " size="1" name="serialNo" id="serialNo" value="<%=i+1%>" disabled="true" >
		<%}else{%>

		<%=i+1%>
		<%}%>
        </td>

		
		<td class="whitebox3wk">
		<input type="text"  class="selectwk textmxwidth2" name="comments" id="comments" value="<%=egpimsService.getComments()==null?"":egpimsService.getComments().toString()%>" ></td>


		
		<td class="whitebox3wk"><input type="text"  class="selectwk grey" name="commentDate"  id="commentDate"  size="10" value="<%=egpimsService.getCommentDate()==null?"":sdf.format(egpimsService.getCommentDate())%>" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" onBlur = "validateDateFormat(this);validateDateJS(this);" >
		<a href="javascript:show_calendar('pIMSForm.commentDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border="0"></a>
		</td>

		<td class="whitebox3wk"><input type="text"  class="selectwk textmxwidth2" name="reason" id="reason" value="<%=egpimsService.getReason()==null?"":egpimsService.getReason().toString()%>"></td>
		<td class="whitebox3wk">
			<input type="text"  class="selectwk textmxwidth2" name="serviceOrderNo" id="serviceOrderNo" value="<%=egpimsService.getOrderNo()==null?"":egpimsService.getOrderNo().toString()%>"><br/>
			<input type="hidden"  class="selectwk textmxwidth2" name="serviceDocNo" id="serviceDocNo" value="<%=egpimsService.getDocNo()==null?"":egpimsService.getDocNo().toString()%>">
		</td>
		<td class="whitebox3wk"><input type="text"  class="selectwk grey" name="payScale"  id="payScale" value="<%=egpimsService.getPayScale()==null?"":egpimsService.getPayScale().toString()%>" onblur="validatePayscale(this);"></td>
		<%
		
		if(modermr.equalsIgnoreCase("view") )
		{
		%>
			<td>
				<egovtags:docFiles moduleName="EIS" documentNumber='<%=egpimsService.getDocNo()==null?"":egpimsService.getDocNo().toString()%>' id='<%="EmpService"+i%>' showOnLoad="true"/>
			</td>
		<%
		}
		else
		{
			if(egpimsService.getDocNo()==null)
			{
		%>
				<td>
					<div class="buttonholderrnew">
						<a href="#"><img src="<%=request.getContextPath()%>/images/page_text.gif" title="Add" alt="Add" width="16" height="16" border="0" onclick="onUpload(this);return false;" /></a>
					</div>
				</td>
		<%
			}
			else
			{
				if(modermr.equalsIgnoreCase("modify"))
				{
		%>
				<td>
					<egovtags:docFiles moduleName="EIS" documentNumber='<%=egpimsService.getDocNo()==null?"":egpimsService.getDocNo().toString()%>' id='<%="EmpService"+i%>' showOnLoad="true"/>
					<div class="buttonholderrnew">
						<a href="#"><img src="<%=request.getContextPath()%>/images/page_text.gif" title="Edit" alt="Edit" width="16" height="16" border="0" onclick="onUpload(this);return false;" /></a>
					</div>
				</td>
		<%
				}
			}
		}
				
		if(!modermr.equalsIgnoreCase("view") )
		{
		%>

		<td class="labelcell"  >
			
			<a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16" border="0" onclick="whichButtonService('ServiceTable',this,'ServiceRow');" /></a>
			<a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16" border="0" onclick="deleteServiceRow('ServiceTable',this);" /></a>
		
     	 </td>

		 <%}%>
		
		</tr>


		<%
		}
		%>
		</tbody>
		
		</table>

		<table   cellpadding ="0" cellspacing ="0" border = "0"  name ="PronameTable" id ="PronameTable" >
		<tbody>
		<tr>
                <td colspan="6" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer"><bean:message key="Probation"/></div></td>
              </tr>


		<tr>
		
		<td  width="53%" class="tablesubheadwk"><bean:message key="WhichPost"/></td>
		<td   width="10%" class="tablesubheadwk"><bean:message key="ProbationFrmDt"/></td>
		<td   width="10%" class="tablesubheadwk"><bean:message key="ProbationToDt"/></td>
		<td width="20%"  class="tablesubheadwk" ><bean:message key="OrderNo"/></td>
		<td   width="10%" class="tablesubheadwk" ><bean:message key="OrderDate"/></td>
		<%
		if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
		{
		%>
		<td width="5%" class="tablesubheadwk">Add/Del</td>
		<%}%>
		</tr>
		<%
		Iterator probationItr = probList.iterator();
		for(int i=0;probationItr.hasNext();i++)
		{
		Probation egpimsProbation = (Probation)probationItr.next();
		%>
		<tr id="PronameRow">
		
		<input type = hidden name="idProbation" id="idProbation" value="<%=egpimsProbation.getIdProbation()==null?"0":egpimsProbation.getIdProbation().toString()%>" />

		<td class="whitebox3wk"><span class="whitebox2wk">
		<select class="selectwk" name="proPostId" id="proPostId"  >
		<option value='0' selected="selected"><bean:message key="Choose"/></option>
		<%
		Map postMap =(Map)session.getAttribute("mapOfDesignation");
		for (Iterator it = postMap.entrySet().iterator(); it.hasNext(); )
		{
		Map.Entry entry = (Map.Entry) it.next();
		%>
		<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == (egpimsProbation.getPostMstr()==null?0:egpimsProbation.getPostMstr().getDesignationId().intValue()))? "selected":"")%>><%= entry.getValue() %></option>
		<%
		}
		%>
		</select></span>
		</td>

		<td class="whitebox3wk">
		<input type="text"  class="selectwk grey" name="proFrom"  id="proFrom"  onblur = "checkprobation(this);chkPrbnPrevFromCurToDate(this)"   value="<%=egpimsProbation.getProbationFromDate()==null?"":sdf.format(egpimsProbation.getProbationFromDate())%>" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" >
		<a href="javascript:show_calendar('pIMSForm.proFrom');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border=0></a>
		</td>

		<td class="whitebox3wk">
		<input type="text"  class="selectwk grey" name="proTo"  id="proTo"  onBlur = "checkprobation(this);probationDateOverlap(this)"   value="<%=egpimsProbation.getProbationToDate()==null?"":sdf.format(egpimsProbation.getProbationToDate())%>" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" >
		<a href="javascript:show_calendar('pIMSForm.proTo');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border=0></a>
		</td>

		<td class="whitebox3wk"><input type="text"  class="selectwk textmxwidth2" name="proOrderNo" id="proOrderNo" value="<%=egpimsProbation.getOrderNo()==null?"":egpimsProbation.getOrderNo()%>" onBlur = "checkprobation(this)"></td>


		<td class="whitebox3wk"><input type="text"  class="selectwk grey" name="proOrderDate" id="proOrderDate" onBlur = "validateDateFormat(this);validateDateJS(this);checkprobation(this)" value="<%=egpimsProbation.getOrderDate()==null?"":sdf.format(egpimsProbation.getOrderDate())%>" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" >
		<a href="javascript:show_calendar('pIMSForm.proOrderDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border=0></a>
		</td>
		<%
		String modePro=((String)(session.getAttribute("viewMode"))).trim();
		if(!modePro.equalsIgnoreCase("view") )
		{
		%>

		<td class="labelcell"  ><p>
			
			<a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16" border="0" onclick="whichButtonService('PronameTable',this,'PronameRow');" /></a>
			<a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16" border="0" onclick="deleteServiceRow('PronameTable',this);" /></a>
		
     	 </td>

		 <%}%>
		</tr>
		<%
		}
		%>

		
		
		</tr>

</tbody>


		</table>

		<table  cellpadding ="0" cellspacing ="0" border = "0"  name ="PronameTableBtn" id ="PronameTableBtn" >
		<tr>
		<!--
		<%
		if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
		{
		%>
		<td colspan="4" class="aligncenter"><input class="buttonfinal" id="AddRowPro" name="AddRowPro"  align ="center" type="button" value="Add Row" onclick="javascript:addRow(this,document.getElementById('PronameTable'),document.getElementById('PronameRow'))">

		<input class="buttonfinal" id="DeleteRowPro" name="DeleteRowPro"  type="button" value="Delete Row" onclick="javascript:deleteRow(this,document.getElementById('PronameTable'),document.getElementById('addproBtn'))" ></td>
		<%
		}
		%>-->
		</tr>
		</table>

		

		<table  cellpadding ="0" cellspacing ="0" border = "0"  name ="RegnameTable" id ="RegnameTable" >
		<tbody>

		<tr>
            <td colspan="5" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                <div class="headplacer"><bean:message key="Regularisation"/></div>
			</td>
        </tr>

		<tr>
		<td  width="53%" class="tablesubheadwk"><bean:message key="WhichPost"/></td>
		<td   width="13%" class="tablesubheadwk">Regularisation Declared Date</td>
		<td width="18%"  class="tablesubheadwk" ><bean:message key="OrderNo"/></td>
		<td   width="11%" class="tablesubheadwk" ><bean:message key="OrderDate"/></td>
		<%
		if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
		{
		%>
		<td width="5%" class="tablesubheadwk">Add/Del</td>
		<%}%>
		</tr>



		<%
		Iterator itreg = regularisationSet.iterator();
		for(int i=0;itreg.hasNext();i++)
		{
		Regularisation egpimsRegularisation = (Regularisation)itreg.next();
		%>
		<tr id="RegnameRow">
		
		<td class="whitebox3wk">
		<input type = "hidden" name="regularisationId"  id="regularisationId" value="<%=egpimsRegularisation.getRegularisationId()==null?"0":egpimsRegularisation.getRegularisationId().toString()%>"onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" />
		<span class="whitebox2wk">
		<select  class="selectwk"  name="regPostId" id="regPostId" >
		<option value='0' selected="selected" ><bean:message key="Choose"/></option>
		<%

		Map postMapreg =(Map)session.getAttribute("mapOfDesignation");
		for (Iterator it = postMapreg.entrySet().iterator(); it.hasNext(); )
		{
		Map.Entry entry = (Map.Entry) it.next();

		%>
		<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == (egpimsRegularisation.getPostMstr()==null?0:egpimsRegularisation.getPostMstr().getDesignationId().intValue()))? "selected":"")%>><%= entry.getValue() %></option>

		<%
		}

		%>
		</select></span>
		</td>


		<td class="whitebox3wk"><input class="selectwk grey" type="text" size="20" name="regDate"  id="regDate"onBlur = "validateDateFormat(this);validateDateJS(this);checkRegularisation(this)" value="<%=egpimsRegularisation.getRegularisationDate()==null?"":sdf.format(egpimsRegularisation.getRegularisationDate())%>" onkeyup="DateFormat(this,this.value,event,false,'3')" >
		<a href="javascript:show_calendar('pIMSForm.regDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border=0></a>
		</td>

		<td class="whitebox3wk"><input type="text" class="selectwk textmxwidth2" name="regOrder" id="regOrder" value="<%=egpimsRegularisation.getOrderNo()==null?"":egpimsRegularisation.getOrderNo()%>" onBlur = "checkRegularisation(this)" ></td>

		<td class="whitebox3wk"><input type="text"  class="selectwk grey" name="regOrderDate"id="regOrderDate" onBlur = "checkRegularisation(this)" value="<%=egpimsRegularisation.getOrderDate()==null?"":sdf.format(egpimsRegularisation.getOrderDate())%>" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" >
		<a href="javascript:show_calendar('pIMSForm.regOrderDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border=0></a>
		</td>

		<%
		String modePro=((String)(session.getAttribute("viewMode"))).trim();
		if(!modePro.equalsIgnoreCase("view") )
		{
		%>

		<td class="labelcell"  ><p>
			
			<a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16" border="0" onclick="whichButtonService('RegnameTable',this,'RegnameRow');" /></a>
			<a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16" border="0" onclick="deleteServiceRow('RegnameTable',this);" /></a>
		
     	 </td>

		 <%}%>

		</tr>

		<%
		}
		%>
		</tbody>
		</table>

		<table align='center' class="tableStyle" cellpadding ="0" cellspacing ="0" border = "1"  name ="RegTableBtn" id ="RegTableBtn" WIDTH=95% style="border: 1px solid #D7E5F2">
		<tr>
		
		</tr>
		</table>
		
	


