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
		org.egov.payroll.model.*,
		org.egov.lib.address.dao.*,
		java.text.SimpleDateFormat,
		java.util.StringTokenizer,
		org.egov.pims.client.*"
		%>
		

	   <%!
						//ServiceLocator 		serviceloc      =null;
						//EisManagerHome	eisManagerHome			=null;
						//EisManager 		eisManager			=null;
				%>


				<%
						Map deptMap = (Map)session.getAttribute("deptmap");
						session.setAttribute("deptMap",deptMap);
						/* try
						{
						serviceloc      = 	ServiceLocator.getInstance();
						eisManagerHome	=	(EisManagerHome)serviceloc.getLocalHome("EisManagerHome");
						eisManager		=	eisManagerHome.create();
						}
						catch(ServiceLocatorException e)
						{
						e.printStackTrace();
						}
						*/
						Set nomimationPirtiSet = null;
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

						nomimationPirtiSet = egpimsPersonalInformation.getEgpimsNomimationPirticularses();
						
						//if(nomimationPirtiSet.isEmpty())
						//nomimationPirtiSet.add(new NomimationPirticulars());
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


				%>

<table align='center' class="tableStyle" cellpadding ="0" cellspacing ="0" border = "1"  name ="nomPir" id ="nomPir" WIDTH=95% style="border: 1px solid #D7E5F2">
		<tbody>
		<tr>
		<td colspan="8" height=20 bgcolor=#dddddd align=middle  class="tableheader">
		<p><bean:message key="NominationParticular"/></p></td>
		</tr>
		</table>


		<table align='center' class="tableStyle" cellpadding ="0" cellspacing ="0" border = "1"  name ="NDnameTable" id ="NDnameTable" WIDTH=95% style="border: 1px solid #D7E5F2">
		<tbody>
		<tr>
		<td   class="labelcell" style = "width:60px" ><bean:message key="EmpName"/></td>
		<td   class="labelcell" ><bean:message key="Age"/></td>
		<td   class="labelcell" ><bean:message key="MaritalStatus"/></td>
		<td   class="labelcell" ><bean:message key="Relation"/></td>
		<td   class="labelcell" ><bean:message key="GPF"/></td>
		<td   class="labelcell" ><bean:message key="SPFGS"/></td>
		<td   class="labelcell" ><bean:message key="FBF"/></td>
		<td   class="labelcell" ><bean:message key="DCRG"/></td>
		<td   class="labelcell" ><bean:message key="Pension"/></td>
		</tr>
		<%
		Iterator itrnp = nomimationPirtiSet.iterator();
		for(int i=0;itrnp.hasNext();i++)
		{
		NomimationPirticulars egpimsNomimationPirticulars = (NomimationPirticulars)itrnp.next();
		%>
		<tr id="NDnameRow">
		<input type = hidden name="nomDetailsId" id="nomDetailsId" value="<%=egpimsNomimationPirticulars.getNomDetailsId()==null?"0":egpimsNomimationPirticulars.getNomDetailsId().toString()%>" />
		<td class="fieldcell"><input type="text"  style = "width:70px" id="nameOfTheNominee" name="nameOfTheNominee"  onBlur = "validateName(this)" value="<%=egpimsNomimationPirticulars.getNameOfNominee()==null?"":egpimsNomimationPirticulars.getNameOfNominee()%>"></td>
		<td class="fieldcell"><input type="text"  style = "width:70px" name="age"  id="age" onBlur = "checkNumber(this);checkNameOfTheNominee(this)" value="<%=egpimsNomimationPirticulars.getAge()==null?"":egpimsNomimationPirticulars.getAge().toString()%>"></td>
		<td class="fieldcell">
		<input type="text" style = "width:154" name="maritialStatus" id="maritialStatus" onBlur = "checkNameOfTheNominee(this);" value="<%=egpimsNomimationPirticulars.getMaritalStatus()==null?"":egpimsNomimationPirticulars.getMaritalStatus()%>"></td>
		<td class="fieldcell">
		<input type="text"  style = "width:148" name="relationId" id="relationId" onBlur = "checkNameOfTheNominee(this);" value="<%=egpimsNomimationPirticulars.getEgpimsRelation()==null?"":egpimsNomimationPirticulars.getEgpimsRelation()%>"></td>
		<td class="fieldcell"><input type="text" style = "width:70px" name="gpfnd" id="gpfnd"onBlur = "checkNumber(this);checkNameOfTheNominee(this)" value="<%=egpimsNomimationPirticulars.getGpf()==null?"":egpimsNomimationPirticulars.getGpf().toString()%>"></td>
		<td class="fieldcell"><input type="text" style = "width:70px" name="spfgs" id="spfgs"onBlur = "checkNumber(this);checkNameOfTheNominee(this)" value="<%=egpimsNomimationPirticulars.getSpfgs()==null?"":egpimsNomimationPirticulars.getSpfgs().toString()%>"></td>
		<td class="fieldcell"><input type="text"  style = "width:70px" name="fbf" id="fbf"onBlur = "checkNumber(this);checkNameOfTheNominee(this)" value="<%=egpimsNomimationPirticulars.getFbf()==null?"":egpimsNomimationPirticulars.getFbf().toString()%>"></td>
		<td class="fieldcell"><input type="text" style = "width:70px" name="dcrg" id="dcrg"onBlur = "checkNumber(this);checkNameOfTheNominee(this)" value="<%=egpimsNomimationPirticulars.getDcrg()==null?"":egpimsNomimationPirticulars.getDcrg().toString()%>"></td>
		<td class="fieldcell"><input type="text" style = "width:70px" name="pension" id="pension" onBlur = "checkNumber(this);checkNameOfTheNominee(this)" value="<%=egpimsNomimationPirticulars.getPension()==null?"":egpimsNomimationPirticulars.getPension().toString()%>"></td>
		</tr>
		<%
		}//for
		%>
		</tbody>
		</table>
		<table  WIDTH=95% colspan="5" align='center' cellpadding ="0" class="tableStyle" cellspacing ="0" border = "1" id="NPBtn" name="NPBtn" style="border: 1px solid #D7E5F2">
		<tr>
		<%
		if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
		{
		%>
		<td> <input class="button2" id="AddRowNom" name="AddRowNom"  type="button" value="Add Row" onclick="javascript:addRow(this,document.getElementById('NDnameTable'),document.getElementById('NDnameRow'))" >
		<input class="button2" id="DeleteRowNom" name="DeleteRowNom"  type="button" value="Delete Row" onclick="javascript:deleteRow(this,document.getElementById('NDnameTable'),document.getElementById('addNDBtn'))" ></td>
		<%
		}
		%>
		</tr>
		</table>
		
