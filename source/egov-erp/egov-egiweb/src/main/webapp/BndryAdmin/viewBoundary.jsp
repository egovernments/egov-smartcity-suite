<%@page import="org.egov.lib.admbndry.ejb.server.CityWebsiteServiceImpl"%>
<%@page import="org.egov.lib.admbndry.ejb.server.BoundaryServiceImpl"%>
<%@page import="org.egov.lib.admbndry.ejb.server.BoundaryTypeServiceImpl"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@page import="java.util.*,java.text.*, org.egov.infstr.utils.*,
	  org.egov.lib.admbndry.ejb.api.*,
	  org.egov.lib.admbndry.*" %>
<%
	String mandatoryFields = "boundaryNum";
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	BoundaryTypeService	bmType = new BoundaryTypeServiceImpl();
	BoundaryService	bm = new BoundaryServiceImpl();
	CityWebsiteService cityWebsiteManager = new CityWebsiteServiceImpl();

	String BoundaryID = (String)request.getParameter("BoundaryId");
	String heirarchyType = request.getParameter("heirarchyType");
	Boundary bndry=null;
	BoundaryType bndryType1=null;
	List cityList = null;
	CityWebsite cityWebsite=null;
	String pbnum=null;
	int isActive=0;

	BoundaryType bndryType = null;
	int topLevelBoundaryID = -1;
	short bndryTypeHeirarchyLevel = -1;
	String targetBoundaryName=null;
	String targetBoundaryNum="";
	String parentBoundary=null;
	String boundaryNameLocal="";
	String boundaryType = request.getParameter("boundaryType");
	int bndryid = Integer.parseInt(boundaryType);

	bndryType1 =bmType.getBoundaryType(new Integer(boundaryType));
	if(BoundaryID!=null){
		bndry = bm.getBoundaryById(Integer.parseInt(BoundaryID));
		if(bndry != null){
			Boundary tempBndry = bndry;
			if(tempBndry.getParent() == null) {
				topLevelBoundaryID = tempBndry.getId().intValue();
			}
			while(tempBndry.getParent() != null) {
				topLevelBoundaryID = tempBndry.getParent().getId().intValue();
				tempBndry = tempBndry.getParent();
			}
			bndryType = bndry.getBoundaryType();
			bndryTypeHeirarchyLevel = bndryType.getHeirarchy();
	
			if(!bndry.getChildren().isEmpty() )	{
				Boundary parent = bndry.getParent();
				if(parent != null) {
					parentBoundary = parent.getName();
					if(parent.getBoundaryNum()!=null){
						pbnum =  parent.getBoundaryNum().toString();
					}
				}
			}
			if(bndry.getBndryNameLocal() != null) {
				boundaryNameLocal =bndry.getBndryNameLocal();
			} else {
				Boundary parent = bndry.getParent();
				if(parent != null){
					parentBoundary = parent.getName();
					if(parent.getBoundaryNum()!=null){
						pbnum =  parent.getBoundaryNum().toString();
					}
				}
			}
			cityList = cityWebsiteManager.getCityWebsite(new Integer(topLevelBoundaryID));
			for(Iterator itr =cityList.iterator();itr.hasNext();) {
				cityWebsite = (CityWebsite)itr.next();
				String url = cityWebsite.getCityBaseURL();
				if(cityWebsite.getIsActive()!=null){
					isActive= cityWebsite.getIsActive().intValue();
				}
			}
			if(bndry.getBoundaryNum()!=null){
				targetBoundaryNum = bndry.getBoundaryNum().toString();
			}
			targetBoundaryName = bndry.getName();
		}
	}
%>

<html>
	<head>
		<title>View Boundary Information</title>
		<script>
			var a="";
			function checkingChildForParent(bndryTypeId,heirarchyType) {
				var type='checkingChildForParent';
				var url = "../commons/Process.jsp?type="+type+"&heirarchyType="+heirarchyType+"&bndryTypeId="+bndryTypeId+" ";
				var req2 = initiateRequest();
				req2.open("GET", url, false);
				req2.send(null);
				if (req2.status == 200) {
					var result=req2.responseText;
					result = result.split("/");
					if(result[0]!= null && result[0]!= "") {
						a=result[0];
					}
				}
				return a;
			}

			function validate(bndryTypeId,heirarchyType) {
				var checking = checkingChildForParent(bndryTypeId,heirarchyType);
				if(checking=='false'){
					alert("There is no Child Boundary Type defined for this!");
					return false;
				}
				return true;
			}
			
			function bodyonload() {
				var isactive ="<%= isActive%>";
				if(isactive==1) {
					document.getElementById("isActiveValue").checked=true;
				}
			}
	</script>
	</head>
	<body onload ="bodyonload()">
		<form>
			<table align="center" width="400">
				<tr>
					<td class="tableheader" valign="middle" align="center" width="100%" colspan="2" height="23">
						<p align="center"><b>View properties of <%=targetBoundaryName%></b></p>
					</td>
				</tr>
				<tr>
					<td  class="labelcell" width="50%" height="31" >
						<p align="left">Hierarchy Type</p>
					</td>
					<td class="labelcell" align="left" width="50%" height="31" >
						<input type="text" name= "hierarchyTypeid" id="hierarchyTypeid" value="<%= bndryType1.getHeirarchyType().getName()%>"  readonly/>
					</td>
				</tr>
				<tr>
					<td class="labelcell" width="50%" height="31" >
						<p align="left" >Boundary Type
					</td>
					<td class="labelcell" align="left" width="50%" height="31">
						<input type="text" value="<%= bndryType1.getName()%>" readonly/>
						<input type="hidden" name="boundaryId" id="boundaryId" value="<%= bndryid%>">
					</td>
				</tr>
				<tr>
					<td class="labelcell"  width="50%" height="31">
						<p align="left">Boundary Name</p>
					</td>
					<td  class="labelcell" align="left" width="50%">
						<input type="text" name="name" value="<%=targetBoundaryName%>" readonly>
					</td>
				</tr>
				<tr>
					<td class="labelcell" >
						<p align="left">Boundary Name Local</p>
					</td>
					<td class="labelcell" align="left" width="50%" height="31" >
						<input type="text" value="<%= boundaryNameLocal%>" readonly/>
					</td>
				</tr>
				<tr>
					<td  class="labelcell" width="50%" height="31">
						<p align="left">Boundary Number</p>
					</td>
					<td class="labelcell" align="left" width="50%">
						<input type="text" name="boundaryNum" value="<%=targetBoundaryNum%>" readonly>
					</td>
				</tr>
				<tr>
					<td class="labelcellforsingletd" width="50%" height="31">
						<p align="left">From Date</p>
					</td>
					<td class="labelcell" align="left" width="50%">
						<%if(bndry.getFromDate()!=null)
						{%>
						<input type="text" name="fromDate" value="<%=formatter.format(bndry.getFromDate())%>" readonly>
						<%}
						else{%>
						<input type="text" name="fromDate" value="" readonly>
						<%}%>
					</td>
				</tr>
				<tr>
					<td class="labelcellforsingletd" width="50%" height="31">
						<p align="left">To Date</p>
					</td>
					<td class="labelcell" align="left" width="50%">
						<%if(bndry.getToDate()!=null)
						{%>
						<input type="text" name="toDate" value="<%=formatter.format(bndry.getToDate())%>" readonly>
						<%}
						else{%>
						<input type="text" name="toDate" value="" readonly>
						<%}%>
					</td>
				</tr>
		        <%
                   	String parent=request.getParameter("checkForParent");
					if(cityList.size()!=0 && parent.equals("true")) {
				%>
				<tr>
					<td class="labelcellforsingletd">
						<p align="left">City Logo</p>
					</td>
					<td class="labelcell"  align="left" width="50%" height="31">
						<input type="text" name="cityLogo" id="cityLogo" value="<%= ((CityWebsite)cityList.get(0)).getLogo()%>" readonly="readonly" >
						<input type="hidden" name="isActive" id="isActive">
					</td>
				</tr>
				<tr>
					<td class="labelcellforsingletd">
						<p align="left">Is Active</p>
					</td>
					<td class="labelcell"  align="left" width="50%" height="31">
						<input type="checkbox" name="isActiveValue" id="isActiveValue" value="ON" disabled="disabled" />
						<input type="hidden" name="isActive" id="isActive">
					</td>
				</tr>
				<tr>
					<td class="labelcell" valign="middle" ><p align="left">
						City Base URL</p>
					</td>
					<td>
					<%
					for(Iterator itr =cityList.iterator();itr.hasNext();)
					{
					cityWebsite = (CityWebsite)itr.next();
					String url = cityWebsite.getCityBaseURL();
					%>
					<table align="center"  id="cityType">
						<tr id="reasonrow">
							<td  class="labelcell" align="left" width="50%" height="31">
								<input type="text"value="<%= url%>" readonly="readonly" size="30"/>
							</td>
						</tr>
					</table>
					<%}
				}
				%>
						<input type="hidden" name="parentBoundaryNum" value="<%=pbnum%>">
						<input type="hidden" name="boundaryNum" value="<%=targetBoundaryNum%>">
						<input type="hidden" name="TargetBoundaryNum" value="<%=targetBoundaryNum%>">
						<input type="hidden" name="topLevelBoundaryID" value="<%=topLevelBoundaryID%>">
						<input type="hidden" name="bndryTypeHeirarchyLevel" value="<%=bndryTypeHeirarchyLevel%>">
						<input type="hidden" name="heirarchyType" value="<%=heirarchyType%>">
							
					</td>
				</tr>
				<tr height="30px">
					<td>&nbsp;</td>
				</tr>	
				<tr bgColor="#dddddd">
					<td class="button2" valign="bottom" align="center" width="29%" colspan="2" height="23">
						<c:set var="topLevelBoundaryID"><%=topLevelBoundaryID%>	</c:set>
						<c:set var="bndryTypeHeirarchyLevel"><%=bndryTypeHeirarchyLevel%></c:set>
						<c:set var="targetBoundaryNum"><%=targetBoundaryNum%></c:set>
						<c:set var="targetBoundaryName"><%=targetBoundaryName%></c:set>
						<c:set var="bndryId"><%=BoundaryID%></c:set>
						<c:set var="parentBoundary"><%=parentBoundary%></c:set>
						<c:set var="heirarchyType"><%=heirarchyType%></c:set>
						<c:set var="boundaryType"><%= bndryType1.getId() %>	</c:set>
						<input type=button  value="Add Child Boundary" onclick="if(validate(<%= bndryType1.getId() %>,<%= bndryType1.getHeirarchyType().getId()%> )){window.location = '<c:url value='/BndryAdmin/addUpdateDeleteBoundary.jsp?parent=${targetBoundaryName}&parentBndryNum=${targetBoundaryNum}&topLevelBoundaryID=${topLevelBoundaryID}&BndryTypeHeirarchyLevel=${bndryTypeHeirarchyLevel}&heirarchyType=${heirarchyType}&bndryId=${bndryId}&operation=create'/>';}"/>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>