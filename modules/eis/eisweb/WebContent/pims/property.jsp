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
						
						Set ImmovablePropDetailsSet = null;
						Set movablePropDetailsSet = null;
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

											
						if(egpimsPersonalInformation.getEgpimsImmovablePropDetailses().isEmpty()) {
							ImmovablePropDetailsSet = new HashSet();
							ImmovablePropDetailsSet.add(new ImmovablePropDetails());
						} else
						 	ImmovablePropDetailsSet = egpimsPersonalInformation.getEgpimsImmovablePropDetailses();
						
						if(egpimsPersonalInformation.getEgpimsMovablePropDetailses().isEmpty()) {
							movablePropDetailsSet = new HashSet();
							movablePropDetailsSet.add(new MovablePropDetails());
						} else
							movablePropDetailsSet = egpimsPersonalInformation.getEgpimsMovablePropDetailses();
						
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


				%>
<table  name = "immpropDet" align='center'  cellpadding ="0" class="tableStyle" cellspacing ="0" border = "1" id = "immpropDet" WIDTH=95% colspan="5" >
		<tbody>
		<tr>
		<td colspan="10" height=20 bgcolor=#dddddd align=middle  class="tableheader">
		<p><bean:message key="PropertyDetails"/>&nbsp;&nbsp;&nbsp;</td>
		</tr>
		<tr>
		<td colspan="10"  class = "tableheader"><bean:message key="ImMovablePropDetails"/></td>
		</tr>
		</table>
		<table  WIDTH=95% align='center' colspan="5" cellpadding ="0" class="tableStyle" cellspacing ="0" border = "1" id="PDImmnameTable" name="PDImmnameTable" style="border: 1px solid #D7E5F2">
		<tbody>
		<tr>
		<td   class="labelcell" ><bean:message key="PropDesc"/></td>
		<td   class="labelcell" ><bean:message key="Place"/></td>
		<td   class="labelcell" ><bean:message key="HowAcq"/></td>
		<td   class="labelcell" ><bean:message key="PresentValue"/></td>
		<td   class="labelcell" ><bean:message key="PermissionObtained"/></td>
		<td   class="labelcell" ><bean:message key="OrderNo"/></td>
		<td   class="labelcell" ><bean:message key="OrderDate"/></td>

		</tr>
		<%
		Iterator itrimm = ImmovablePropDetailsSet.iterator();
		for(int i=0;itrimm.hasNext();i++)
		{
		ImmovablePropDetails egpimsImmovablePropDetails = (ImmovablePropDetails)itrimm.next();
		%>
		<tr id="PDImmnameRow">
		<input type = hidden name="immPropertyDetailsId" id="immPropertyDetailsId" value="<%=egpimsImmovablePropDetails.getImmPropertyDetailsId()==null?"0":egpimsImmovablePropDetails.getImmPropertyDetailsId().toString()%>" />
		<td class="fieldcell">
		<input type="text"   id="propertydiscriptionImm" style = "width:141; height:20" name="propertydiscriptionImm" value="<%=egpimsImmovablePropDetails.getPropertyDescription()==null?"":egpimsImmovablePropDetails.getPropertyDescription()%>" ></td>
		<td class="fieldcell">
		 <input type="text"   style = "width:135; height:20" name="placeImm" value="<%=egpimsImmovablePropDetails.getPlace()==null?"":egpimsImmovablePropDetails.getPlace()%>"></td>
		<td class="labelcell">
		<select style = "width:143; height:21" class="labelcell"  name="howAcquiredImm" id="howAcquiredImm" >
		<option value='0' selected="selected" ><bean:message key="Choose"/></option>
		<%
		Map howAcquiredMasterMap =(Map)session.getAttribute("howAcquiredMasterMap");
		for (Iterator it = howAcquiredMasterMap.entrySet().iterator(); it.hasNext(); )
		{
		Map.Entry entry = (Map.Entry) it.next();
		%>
		<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == (egpimsImmovablePropDetails.getHowAcquiredMstr()==null?0:egpimsImmovablePropDetails.getHowAcquiredMstr().getId().intValue()))? "selected":"")%>><%= entry.getValue() %></option>
		<%
		}
		%>
		</select></td>
		<td class="fieldcell">
		<input type="text"  style = "width:143; height:19"  id="presentValueImm"name="presentValueImm" value="<%=egpimsImmovablePropDetails.getPresentValue()==null?"":egpimsImmovablePropDetails.getPresentValue().toString()%>" ></td>
		<td class="fieldcell"><input type="text"   style = "width:75px" id="permissionObtainedImm"name="permissionObtainedImm" value="<%=egpimsImmovablePropDetails.getPermissionObtained().toString().equals("0")?"N":"Y"%>"></td>
		<td class="fieldcell"><input type="text"   style = "width:75px" id="orderNoImm"name="orderNoImm" value="<%=egpimsImmovablePropDetails.getOrderNo()==null?"":egpimsImmovablePropDetails.getOrderNo()%>"   ></td>
		<td class="fieldcell"><input type="text"   style = "width:75px"  id="dateImm" name="dateImm" value="<%=egpimsImmovablePropDetails.getOrderDate()==null?"":sdf.format(egpimsImmovablePropDetails.getOrderDate())%>" onBlur = "validateDateFormat(this);validateDateJS(this);" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"></td>
		</tr>

		<%
		}//for
		%>
		</tbody>
		</table>
		<table  WIDTH=95% colspan="5" align='center' class="tableStyle" cellpadding ="0" cellspacing ="0" border = "1" id="ImmProBtn" name="ImmProBtn" style="border: 1px solid #D7E5F2">
		<tr>
		<%
		if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
		{
		%>
		<td> <input class="button2" id="AddRowImm" name="AddRowImm"  type="button" value="Add Row" onclick="javascript:addRow(this,document.getElementById('PDImmnameTable'),document.getElementById('PDImmnameRow'))" >
		<input class="button2" id="DeleteRowImm" name="DeleteRowImm"  type="button" value="Delete Row" onclick="javascript:deleteRow(this,document.getElementById('PDImmnameTable'),document.getElementById('addPDImmBtn'))" ></td>
		<%
		}
		%>
		</tr>
		</table>
		<table  id="movableProperty" WIDTH=95% align='center' class="tableStyle" cellpadding ="0" cellspacing ="0" border = "1" name="movableProperty"  colspan="5" style="border: 1px solid #D7E5F2" >
		<tr>
		<td colspan="10"  class = "tableheader"WIDTH=95%><bean:message key="MovablePropDetails"/></td>
		</tr>
		</table>

		<table  WIDTH=95%  cellpadding ="0" align='center' class="tableStyle" cellspacing ="0" border = "1" colspan="5" id="PDMovnameTable" name="PDMovnameTable" style="border: 1px solid #D7E5F2">
		<tbody>
		<tr>

		<td   class="labelcell" ><bean:message key="PropDesc"/></td>
		<td   class="labelcell" width="148" ><bean:message key="TimeOfPur"/></td>
		<td   class="labelcell" width="145" ><bean:message key="HowAcq"/></td>
		<td   class="labelcell" ><bean:message key="PermissionObtained"/></td>
		<td   class="labelcell" ><bean:message key="OrderNo"/> </td>
		<td   class="labelcell" ><bean:message key="OrderDate"/></td>
		</tr>
		<%
		Iterator itrmov = movablePropDetailsSet.iterator();
		for(int i=0;itrmov.hasNext();i++)
		{
		MovablePropDetails egpimsMovablePropDetails = (MovablePropDetails)itrmov.next();

		%>
		<tr id="PDMovnameRow">
		<input type = hidden name="movPropertyDetailsId" id="movPropertyDetailsId" value="<%=egpimsMovablePropDetails.getMovPropertyDetailsId()==null?"0":egpimsMovablePropDetails.getMovPropertyDetailsId().toString()%>" />
		<td class="fieldcell">
		<input type="text"   style = "width:129; height:20" name="propertydiscriptionMo" value="<%=egpimsMovablePropDetails.getPropertyDiscription()==null?"":egpimsMovablePropDetails.getPropertyDiscription()%>" onBlur = "checkpropertydiscriptionMo(this);" ></td>
		<td class="fieldcell" width="148">
		<input type="text"  style = "width:146; height:19" name="valueOfTimeOfPurchaseMo" value="<%=egpimsMovablePropDetails.getValAtPurchase()==null?"":egpimsMovablePropDetails.getValAtPurchase().toString()%>" onBlur = "checkNumber(this);checkpropertydiscriptionMo(this)" ></td>
		<td class="fieldcell" width="145">

		<select  class="labelcell" name="howAcquiredMov" id="howAcquiredMov"  >
		<option value='0' class="labelcell"  selected="selected"><bean:message key="Choose"/></option>
		<%
		Map howAcquiredMasterMapmo =(Map)session.getAttribute("howAcquiredMasterMap");
		for (Iterator it = howAcquiredMasterMapmo.entrySet().iterator(); it.hasNext(); )
		{
		Map.Entry entry = (Map.Entry) it.next();

		%>
		<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == (egpimsMovablePropDetails.getHowAcquiredMstr()==null?0:egpimsMovablePropDetails.getHowAcquiredMstr().getId().intValue()))? "selected":"")%>><%= entry.getValue() %></option>

		<%
		}
		%>
		</select></td>
		<td class="fieldcell">
		<input type="text"   style = "width:143; height:20" name="permissionObtainedMo"  onBlur = "checkpropertydiscriptionMo(this);checkPerObtained(this)" value="<%=egpimsMovablePropDetails.getPermissionObtained().toString().equals("0")?"N":"Y"%>"></td>
		<td class="fieldcell">
		<input type="text"   style = "width:128; height:20" value="<%=egpimsMovablePropDetails.getOrderNo()==null?"":egpimsMovablePropDetails.getOrderNo()%>" name="orderNoMo" onBlur = "checkpropertydiscriptionMo(this);"  ></td>
		<td class="fieldcell"><input   type="text"  style = "width:100px" name="dateMo" onBlur = "validateDateFormat(this);validateDateJS(this);checkpropertydiscriptionMo(this)"  value="<%=egpimsMovablePropDetails.getOrderDate()==null?"":sdf.format(egpimsMovablePropDetails.getOrderDate())%>" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" ></td>
		</tr>
		</tr>
		<%
		}//for
		%>
		</tbody>
		</table>
		<table  WIDTH=95%  align='center' class="tableStyle" colspan="5" cellpadding ="0" cellspacing ="0" border = "1" id="MovProBtn" name="MovProBtn" style="border: 1px solid #D7E5F2">
		<tr>
		<%
		if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
		{
		%>
		<td> <input class="button2" id="AddRowMov" name="AddRowMov"  type="button" value="Add Row" onclick="javascript:addRow(this,document.getElementById('PDMovnameTable'),document.getElementById('PDMovnameRow'))" >
		<input class="button2" id="DeleteRowMov" name="DeleteRowMov"  type="button" value="Delete Row" onclick="javascript:deleteRow(this,document.getElementById('PDMovnameTable'),document.getElementById('addPDMovBtn'))" ></td>
		<%
		}
		%>
		</tr>
		</table>

		