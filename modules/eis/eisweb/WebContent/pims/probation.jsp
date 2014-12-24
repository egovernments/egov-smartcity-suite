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
						Set probationSet = null;
						
						PersonalInformation egpimsPersonalInformation =null;

						String id ="";
						System.out.println("deptSet"+(String)session.getAttribute("viewMode"));
						System.out.println("deptSet"+request.getParameter("Id"));
						if(request.getAttribute("employeeOb")!=null)
						{

						//id = request.getParameter("Id").trim();

						egpimsPersonalInformation = (PersonalInformation)request.getAttribute("employeeOb");

						}
						else
						{
						egpimsPersonalInformation = new PersonalInformation();

						}

												
						if(egpimsPersonalInformation.getEgpimsProbations().isEmpty()) {
							probationSet = new HashSet();
							probationSet.add(new Probation());
						} else
							probationSet = egpimsPersonalInformation.getEgpimsProbations();
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
 

				%>





<table align='center' class="tableStyle" cellpadding ="0" cellspacing ="0" border = "1"  name ="PronameTable" id ="PronameTable" WIDTH=95% style="border: 1px solid #D7E5F2">
		<tbody>


		<tr>
		<td   class="labelcell" ><bean:message key="Probation"/></font></td>
		<td   class="labelcell" width="132" ><bean:message key="WhichPost"/></td>
		<td   class="labelcell" width="183" ><bean:message key="ProbationDeclaredDt"/></td>
		<td   class="labelcell" ><bean:message key="OrderNo"/></td>
		<td   class="labelcell" ><bean:message key="OrderDate"/></td>

		</tr>
		<%
		Iterator itr1 = probationSet.iterator();
		for(int i=0;itr1.hasNext();i++)
		{
		Probation egpimsProbation = (Probation)itr1.next();
		%>
		<tr id="PronameRow">
		<td class="labelcell">&nbsp;</td>
		<input type = hidden name="idProbation" id="idProbation" value="<%=egpimsProbation.getIdProbation()==null?"0":egpimsProbation.getIdProbation().toString()%>" />
		<td class="labelcell" width="132">
		<select style="width: 160;" name="proPostId" id="proPostId"  >
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
		</select>
		</td>
		<td class="labelcell"><input type="text"  style="width: 160;" name="proDec"  id="proDec"  onBlur = "checkprobation(this)"   value="<%=egpimsProbation.getProbationFromDate()==null?"":sdf.format(egpimsProbation.getProbationFromDate())%>" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" ></td>
		<td class="labelcell"><input type="text"  style="width: 160;" name="proOrderNo" id="proOrderNo" value="<%=egpimsProbation.getOrderNo()==null?"":egpimsProbation.getOrderNo()%>" onBlur = "checkprobation(this)"></td>
		<td class="labelcell"><input type="text"  style="width: 160;" name="proOrderDate" id="proOrderDate" onBlur = "validateDateFormat(this);validateDateJS(this);checkprobation(this)" value="<%=egpimsProbation.getOrderDate()==null?"":sdf.format(egpimsProbation.getOrderDate())%>" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" ></td>
		</tr>
		<%
		}
		%>
		</tbody>
		</table>

		<table align='center' class="tableStyle" cellpadding ="0" cellspacing ="0" border = "1"  name ="PronameTableBtn" id ="PronameTableBtn" WIDTH=95% style="border: 1px solid #D7E5F2">
		<tr>
		<%
		if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
		{
		%>
		<td ><input class="button2" id="AddRowPro" name="AddRowPro"  align ="center"type="button" value="Add Row" onclick="javascript:addRow(this,document.getElementById('PronameTable'),document.getElementById('PronameRow'))">

		<input class="button2" id="DeleteRowPro" name="DeleteRowPro"  type="button" value="Delete Row" onclick="javascript:deleteRow(this,document.getElementById('PronameTable'),document.getElementById('addproBtn'))" ></td>
		<%
		}
		%>
		</tr>
		</table>
