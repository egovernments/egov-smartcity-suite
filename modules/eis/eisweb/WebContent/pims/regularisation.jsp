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
						Map deptMap = (Map)session.getAttribute("deptmap");
						session.setAttribute("deptMap",deptMap);
						Set regularisationSet = null;
						PersonalInformation egpimsPersonalInformation =null;
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
				
						
						if( egpimsPersonalInformation.getEgpimsRegularisations().isEmpty()) {
							regularisationSet = new HashSet();
							regularisationSet.add(new Regularisation());
						} else
							regularisationSet = egpimsPersonalInformation.getEgpimsRegularisations();
							
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


				%>

		
<table align='center' class="tableStyle" cellpadding ="0" cellspacing ="0" border = "1"  name ="RegnameTable" id ="RegnameTable" WIDTH=95% style="border: 1px solid #D7E5F2">

		<tbody>

		<tr>
		<td   class="labelcell" ><bean:message key="Regularisation"/></font></td>
		<td   class="labelcell" width="132" ><bean:message key="WhichPost"/></td>
		<td   class="labelcell" width="183" ><bean:message key="ProbationDeclaredDt"/></td>
		<td   class="labelcell" ><bean:message key="OrderNo"/></td>
		<td   class="labelcell" ><bean:message key="OrderDate"/></td>

		</tr>



		<%
		Iterator itreg = regularisationSet.iterator();
		for(int i=0;itreg.hasNext();i++)
		{
		Regularisation egpimsRegularisation = (Regularisation)itreg.next();
		%>
		<tr id="RegnameRow">
		<td class="labelcell">&nbsp;</td>
		<td class="labelcell">
		<input type = hidden name="regularisationId"  id="regularisationId" value="<%=egpimsRegularisation.getRegularisationId()==null?"0":egpimsRegularisation.getRegularisationId().toString()%>"onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" />
		<select  style="width: 160;"  name="regPostId" id="regPostId" >
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
		</select>
		</td>
		<td class="fieldcell"><input style="width: 160;" type="text" style = "width:142px" size="80" name="regDate"  id="regDate"onBlur = "validateDateFormat(this);validateDateJS(this);checkRegularisation(this)" value="<%=egpimsRegularisation.getRegularisationDate()==null?"":sdf.format(egpimsRegularisation.getRegularisationDate())%>" onkeyup="DateFormat(this,this.value,event,false,'3')" ></td>
		<td class="fieldcell"><input type="text" style="width: 160;" name="regOrder" id="regOrder" value="<%=egpimsRegularisation.getOrderNo()==null?"":egpimsRegularisation.getOrderNo()%>" onBlur = "checkRegularisation(this)" ></td>
		<td class="fieldcell"><input type="text"  style = "width:160" name="regOrderDate"id="regOrderDate" onBlur = "checkRegularisation(this)" value="<%=egpimsRegularisation.getOrderDate()==null?"":sdf.format(egpimsRegularisation.getOrderDate())%>" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" ></td>
		</tr>

		<%
		}
		%>
		</tbody>
		</table>

		<table align='center' class="tableStyle" cellpadding ="0" cellspacing ="0" border = "1"  name ="RegTableBtn" id ="RegTableBtn" WIDTH=95% style="border: 1px solid #D7E5F2">
		<tr>
		<%
		if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
		{
		%>
		<td> <input class="button2" id="AddRowReg" name="AddRowReg"  align ="center"type="button" value="Add Row" onclick="javascript:addRow(this,document.getElementById('RegnameTable'),document.getElementById('RegnameRow'))">

		<input class="button2" id="DeleteRowReg" name="DeleteRowReg"  type="button" value="Delete Row" onclick="javascript:deleteRow(this,document.getElementById('RegnameTable'),document.getElementById('addregBtn'))" ></td>
		<%
		}
		%>
		</tr>
		</table>
		