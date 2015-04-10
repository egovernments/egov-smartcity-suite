<%@page import="org.egov.lib.admbndry.ejb.server.HeirarchyTypeServiceImpl"%>
<%@page import="org.egov.lib.admbndry.ejb.server.BoundaryServiceImpl"%>
<%@page import="org.egov.lib.admbndry.ejb.server.BoundaryTypeServiceImpl"%>
<%@ page import="
		org.egov.infstr.utils.*,
		org.egov.lib.admbndry.BoundaryType,
		org.egov.infra.admin.master.entity.Boundary,
		org.egov.lib.admbndry.HeirarchyType,
		org.egov.lib.admbndry.ejb.api.*,
		java.util.*" %>

<html>
<title>
	Create Boundary
</title>
<head>
	<style>
	<!--
	#foldheader{cursor:pointer;cursor:hand ; font-weight:bold ;
	list-style-image:url(/images/fold.gif)}
	#foldinglist{list-style-image:url(/images/list.gif)}
	//-->
	</style>
	<script>
	<!--

	//Smart Folding Menu tree- By Dynamic Drive (rewritten 03/03/02)
	//For full source code and more DHTML scripts, visit http://www.dynamicdrive.com
	//This credit MUST stay intact for use

	var head="display:''"
	img1=new Image()
	img1.src="/images/fold.gif"
	img2=new Image()
	img2.src="/images/open.gif"

	var ns6=document.getElementById&&!document.all
	var ie4=document.all&&navigator.userAgent.indexOf("Opera")==-1

	function checkcontained(e){
	var iscontained=0
	cur=ns6? e.target : event.srcElement
	i=0
	if (cur.id=="foldheader")
	iscontained=1
	else
	while (ns6&&cur.parentNode||(ie4&&cur.parentElement)){
	if (cur.id=="foldheader"||cur.id=="foldinglist"){
	iscontained=(cur.id=="foldheader")? 1 : 0
	break
	}
	cur=ns6? cur.parentNode : cur.parentElement
	}

	if (iscontained){
	var foldercontent=ns6? cur.nextSibling.nextSibling : cur.all.tags("UL")[0]
	if (foldercontent.style.display=="none"){
	foldercontent.style.display=""
	cur.style.listStyleImage="url(/images/open.gif)"
	}
	else{
	foldercontent.style.display="none"
	cur.style.listStyleImage="url(/images/fold.gif)"
	}
	}
	}

	if (ie4||ns6)
	document.onclick=checkcontained

	//-->
	</script>
	<script>

		function showHiddenTextField(varId)
		{
			alert("hi"+this.document.AdminBoundaryForm);

			alert('hi'+this.document.AdminBoundaryForm.id.style.visibility);
			//this.document.AdminBoundaryForm.inText6.style.visibility= "visible"

		}
	</script>
</head>
<body>



<font face="Comic">

<%!
	public static void goRecursive(Boundary bndry, JspWriter out) throws Exception
	{

		if(bndry != null)
		{
			int topLevelBoundaryID = -1;
			short parentBndryTypeHeirarchyLevel = -1;
			Boundary tempBndry = bndry;

			if(tempBndry.getParent() == null)
			{
				topLevelBoundaryID = tempBndry.getId().intValue();
			}

			while(tempBndry.getParent() != null)
			{
				topLevelBoundaryID = tempBndry.getParent().getId().intValue();
				tempBndry = tempBndry.getParent();
			}

			parentBndryTypeHeirarchyLevel = bndry.getBoundaryType().getHeirarchy();
			if(!bndry.getChildren().isEmpty() )
			{
				Boundary parent = bndry.getParent();
				String parentBoundaryName = null;
				if(parent != null)
				{
					parentBoundaryName = parent.getName();
				}

				//Boundary parentBndry = bndry.getParent();

				out.println("<li id=\"foldheader\">"+bndry.getName()+"</li> &nbsp <a href=\"/BndryAdmin/addUpdateDeleteBoundary.jsp?parent="+bndry.getName()+"&parentBndryNum="+bndry.getBoundaryNum()+"&topLevelBoundaryID="+topLevelBoundaryID+"&BndryTypeHeirarchyLevel="+parentBndryTypeHeirarchyLevel+"&operation=create\" target=\"rowsideWindow\">add</a> &nbsp;<a href=\"/BndryAdmin/addUpdateDeleteBoundary.jsp?topLevelBoundaryID="+topLevelBoundaryID+"&BndryTypeHeirarchyLevel="+parentBndryTypeHeirarchyLevel+"&TargetBoundaryNum="+bndry.getBoundaryNum()+"&TargetBoundaryName="+bndry.getName()+"&parentBoundaryName="+parentBoundaryName+"&operation=delete\" target=\"rowsideWindow\">delete</a> &nbsp;<a href=\"/BndryAdmin/addUpdateDeleteBoundary.jsp?topLevelBoundaryID="+topLevelBoundaryID+"&BndryTypeHeirarchyLevel="+parentBndryTypeHeirarchyLevel+"&TargetBoundaryNum="+bndry.getBoundaryNum()+"&TargetBoundaryName="+bndry.getName()+"&parentBoundaryName="+parentBoundaryName+"&operation=edit\" target=\"rowsideWindow\">edit</a>"+
					"<ul id=\"foldinglist\" style=\"display:none\" style=&{head};>");

				//closeUlList.add("OneMore");
				Set bndriesSet = bndry.getChildren();

				for(Iterator itr=bndriesSet.iterator();itr.hasNext();)
				{
					Boundary bndryCh = (Boundary)itr.next();
					System.out.println("child values....!!"+bndryCh.getName());
					goRecursive(bndryCh, out);

					if(! itr.hasNext())
						out.println("</ul>");
				}
			}
			else
			{
				Boundary parent = bndry.getParent();
				String parentBoundaryName = null;
				if(parent != null)
				{
					parentBoundaryName = parent.getName();
				}

				out.println("<li id=\"foldheader\">"+bndry.getName()+"</li> &nbsp <a href=\"/BndryAdmin/addUpdateDeleteBoundary.jsp?parent="+bndry.getName()+"&parentBndryNum="+bndry.getBoundaryNum()+"&topLevelBoundaryID="+topLevelBoundaryID+"&BndryTypeHeirarchyLevel="+parentBndryTypeHeirarchyLevel+"&operation=create\" target=\"rowsideWindow\">add</a> &nbsp;<a href=\"/BndryAdmin/addUpdateDeleteBoundary.jsp?topLevelBoundaryID="+topLevelBoundaryID+"&BndryTypeHeirarchyLevel="+parentBndryTypeHeirarchyLevel+"&TargetBoundaryNum="+bndry.getBoundaryNum()+"&TargetBoundaryName="+bndry.getName()+"&parentBoundaryName="+parentBoundaryName+"&operation=delete\" target=\"rowsideWindow\">delete</a> &nbsp;<a href=\"/BndryAdmin/addUpdateDeleteBoundary.jsp?topLevelBoundaryID="+topLevelBoundaryID+"&BndryTypeHeirarchyLevel="+parentBndryTypeHeirarchyLevel+"&TargetBoundaryNum="+bndry.getBoundaryNum()+"&TargetBoundaryName="+bndry.getName()+"&operation=edit\" target=\"rowsideWindow\">edit</a>"+
					"<ul id=\"foldinglist\" style=\"display:none\" style=&{head};>"+
					"</ul>");



			}
		}
	}
%>


<%

		String message	 = "";
		message	 = request.getParameter("message");
		if(message ==  null || message.equals("null"))
			message	= "";

		Boundary topBoundary = null;
		List bndriesList = null;

		BoundaryTypeService	btm = new BoundaryTypeServiceImpl();
		BoundaryService	bm = new BoundaryServiceImpl();
		HeirarchyTypeService htm = new HeirarchyTypeServiceImpl();



	 	System.out.println("calling testGetTopBoundaryType()");
	 	BoundaryType topBndryType = null, bndryType = null ;

		Set hTypesSet = htm.getAllHeirarchyTypes();

		HeirarchyType ht  = null;

		for(Iterator itr =hTypesSet.iterator(); itr.hasNext();)
		{
			ht = (HeirarchyType)itr.next();
			if(ht.getName().equalsIgnoreCase("ADMINISTRATION"))
			{
				System.out.println("got heirarchy type:"+ht.getName());
				break;
			}
		}

		session.setAttribute("heirarchyType", ht);

	 	if(btm != null && ht != null)
	 		topBndryType = btm.getTopBoundaryType(ht);

	 	if(bm != null && ht != null)
	 		bndriesList = bm.getTopBoundaries(ht);

%>

<form name="AdminBoundaryForm">
<table border="1" width=746 bgcolor="#FFFFFF" borderColorDark="#000000" >
<tr>
<td>
<center>
<br><font color=red><b>
<%=message%></b><br></font>
<B>Configure the administrative boundaries.</B>

<br><br><br>
<%
System.out.println(">>>>>>>>>>>>topBndryType"+topBndryType+"bndriesList"+bndriesList.size());
if(topBndryType == null)
{

%>
	<ul>
	  <li id="foldheader">Boundary Types</li> &nbsp;<a href="/BndryAdmin/addUpdateDeleteBoundaryType.jsp?operation=create" >add</a>
	   <ul id="foldinglist" style="display:none" style=&{head};>
	   </ul>
	</ul>
<%
}
else
{
	System.out.println("Top boundary is not null");
	System.out.println(">>>>>>>>>>>>topBndryType"+topBndryType+"bndriesList"+bndriesList.size());
%>
	<ul>
	  <li id="foldheader">Boundary Types</li> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	   <ul id="foldinglist" style="display:none" style=&{head};>
			<li id="foldheader"><%=topBndryType.getName()%></li> &nbsp;<a href="/BndryAdmin/addUpdateDeleteBoundaryType.jsp?parent=<%=topBndryType.getName()%>&operation=delete&target=<%=topBndryType.getName()%>" >delete</a>&nbsp;<a href="/BndryAdmin/addUpdateDeleteBoundaryType.jsp?parent=<%=topBndryType.getName()%>&operation=edit&target=<%=topBndryType.getName()%>">edit</a>
			<ul id="foldinglist" style="display:none" style=&{head};>


<%
			bndryType = topBndryType;
			System.out.println("@@@@@@@@@@@@@bndryType.getName() :"+bndryType.getName());
			List ulCloseLst = new ArrayList();

//			for(Iterator itr = bndryType.getChildBoundaryTypes().iterator(); !bndryType.getChildBoundaryTypes().isEmpty();)
			while(true)
			{
				System.out.println("hey in while true");
	         	if(bndryType.getChildBoundaryTypes().isEmpty())
	         	{
%>
					<a href="/BndryAdmin/addUpdateDeleteBoundaryType.jsp?parent=<%=bndryType.getName()%>&operation=create" >add</a>
					
<%
					System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%Here BT does not have sub levels"+bndryType.getName());
					break;

				}
				else
				{
					BoundaryType bndry = (BoundaryType)bndryType.getChildBoundaryTypes().iterator().next();
%>
			        <li id="foldheader" ><%=bndry.getName()%></li> &nbsp;<a href="/BndryAdmin/addUpdateDeleteBoundaryType.jsp?parent=<%=bndryType.getName()%>&operation=delete&target=<%=bndry.getName()%>" >delete</a>&nbsp;<a href="/BndryAdmin/addUpdateDeleteBoundaryType.jsp?parent=<%=bndryType.getName()%>&operation=edit&target=<%=bndry.getName()%>" >edit</a>
		               <ul id="foldinglist" style="display:none" style=&{head};>
<%
					System.out.println("@@@@@@@@@@@@@bndryType.getName() :"+bndryType.getName());
					bndryType = bndry;
					ulCloseLst.add("plusOne");
					ulCloseLst.add("plusOne");

				}
			}

			for(Iterator itr1 = ulCloseLst.iterator();itr1.hasNext();)
			{
				itr1.next();
%>
				</ul>
<%
			}
%>

		</ul>
	   </ul>
	</ul>

<%
}
%>

</ul>
<%
	if(bndriesList != null)
	{
		List closeUlList = new ArrayList();
		System.out.println("bndriesList : "+bndriesList.size());
		if(bndriesList.size() <= 0 )
		{
%>

		<ul>
		  <li id="foldheader">Boundary Values</li> &nbsp;<a href="/BndryAdmin/addUpdateDeleteBoundary.jsp?parentBndryNum=0&BndryTypeHeirarchyLevel=1&operation=create&topLevelBoundaryID=0">add</a>
		   <ul id="foldinglist" style="display:none" style=&{head};>
		   </ul>
		</ul>
<%
		}
		else
		{
%>
			<ul>
			  <li id="foldheader">Boundary Values</li> &nbsp;<a href="/BndryAdmin/addUpdateDeleteBoundary.jsp?parentBndryNum=0&BndryTypeHeirarchyLevel=1&operation=create&topLevelBoundaryID=0">add</a>
			   <ul id="foldinglist" style="display:none" style=&{head};>
<%
			closeUlList.add("OneMore");
			closeUlList.add("OneMore");
		}

		if(bndriesList.size() > 0 )
			while(true)
			{
				System.out.println("hello!!!!!!!!!!!!");

				for(Iterator itr=bndriesList.iterator();itr.hasNext();)
				{
					System.out.println("calling recursive..");
					Boundary bndry = (Boundary)itr.next();
					goRecursive(bndry, out);
				}
				break;
			}

			for(Iterator itr2 = closeUlList.iterator();itr2.hasNext();)
			{
				itr2.next();
%>
				</ul>
<%
			}

	}

%>




</font>
</center>
</tr>
</td>
</table>
</form>



</body>
