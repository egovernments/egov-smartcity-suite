<%@page import="org.egov.lib.admbndry.ejb.server.BoundaryServiceImpl"%>
<%@page import="org.egov.lib.admbndry.ejb.server.BoundaryTypeServiceImpl"%>
<%@ page import="
		org.egov.infstr.utils.*,
		org.egov.lib.admbndry.BoundaryType,
		org.egov.lib.admbndry.BoundaryTypeImpl,
		org.egov.lib.admbndry.Boundary,
		org.egov.lib.admbndry.HeirarchyType,
		org.egov.lib.admbndry.BoundaryImpl,
		org.egov.lib.admbndry.ejb.api.*,
		java.util.*"
 %>



 <%

		String parentBoundaryNum = request.getParameter("ParentBoundaryNum");
		String newBoundaryName = request.getParameter("NewBoundaryName");
		String operation = request.getParameter("operation");
		String targetBoundaryNum = request.getParameter("TargetBoundaryNum");
		String topLevelBoundaryID = request.getParameter("topLevelBoundaryID");
		System.out.println("***************************topLevelBoundaryID :"+topLevelBoundaryID+"/");


		String bndryTypeHeirarchyLevel = request.getParameter("bndryTypeHeirarchyLevel");
		String newBoundaryNum = request.getParameter("newBoundaryNum");
		String bndryName = request.getParameter("changedBndryName");
		String bndryNum = request.getParameter("changedBndryNum");


		

		HeirarchyType heirarchyType = (HeirarchyType)session.getAttribute("heirarchyType");

		System.out.println("heirarchyType :"+heirarchyType.getName()+","+heirarchyType.getId()+"|");

		
			BoundaryTypeServiceImpl boundaryTypeServiceImpl= new BoundaryTypeServiceImpl();
			BoundaryServiceImpl boundaryServiceImpl= new BoundaryServiceImpl();
			
		
		if(operation!= null && operation.equals("create") && heirarchyType != null)
		{
			if(parentBoundaryNum != null && newBoundaryName != null)
			{

				System.out.println("calling getBoundary(String)");
				Boundary pb = null;

				if(boundaryServiceImpl != null)
				{
					System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+parentBoundaryNum +","+bndryTypeHeirarchyLevel +","+topLevelBoundaryID );
					pb = boundaryServiceImpl.getBoundary(Short.parseShort(parentBoundaryNum), Short.parseShort(bndryTypeHeirarchyLevel), heirarchyType,  Integer.parseInt(topLevelBoundaryID));

					BoundaryType pbt = boundaryTypeServiceImpl.getBoundaryType(Short.parseShort(bndryTypeHeirarchyLevel), heirarchyType);
					System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% pbt.getID()"+pbt.getId());
					Iterator itr = null;
					if(pbt != null && pbt.getChildBoundaryTypes() != null && ! pbt.getChildBoundaryTypes().isEmpty())
						itr = pbt.getChildBoundaryTypes().iterator();

					System.out.println(">>>>>>>>>>>>>>>>>>>>>> here");
					Boundary chB = null;
					if(itr != null && itr.hasNext() && pb != null)
					{
						System.out.println("hey has child boundary type!!!!!!!!!!");
						chB = new BoundaryImpl((BoundaryType)pbt.getChildBoundaryTypes().iterator().next());
						System.out.println("hey has child boundary type!!!!!!!!!!"+chB.getName());
					}
					else if(pb == null && pbt != null)
					{
						System.out.println("hey no child boundary type!!!!!!!!!!");
						System.out.println("hey pb is :"+pb);
						System.out.println("hey pbt is :"+pbt.getName());
						chB = new BoundaryImpl(pbt);
					}
					else
					{
						String pbtName = pbt.getName();
%>
						<jsp:forward page="/BndryAdmin/bndryAdminFrames.jsp">
							<jsp:param name="message" value="Boundary not added. Please make sure that you have a proper boundary type for the boundary you are adding."/>
						</jsp:forward>

<%

					}


					if(chB != null)
					{
						chB.setParent(pb);
						chB.setName(newBoundaryName);
						chB.setBoundaryNum(Integer.parseInt(newBoundaryNum));
/*
					short hn = pbt.getHeirarchy();
					short nhn  = ++hn;
					chBt.setHeirarchy(nhn);
					chBt.setName(newBoundaryTypeName);
					pbt.addChildBoundaryType(chBt);
*/
						boundaryServiceImpl.createBoundary(chB);
					}
					//btm.createBoundaryType(chBt);
				}
%>
					<jsp:forward page="/BndryAdmin/bndryAdminFrames.jsp">
						<jsp:param name="message" value="Boundary successfully addded"/>
					</jsp:forward>

<%

			}
			else
				out.println("Please provide both New BoundaryType Name and Parent BoundaryType Name ");

		}
		else if(operation!= null && operation.equals("delete") && heirarchyType != null)
		{

			System.out.println(targetBoundaryNum +","+bndryTypeHeirarchyLevel +","+topLevelBoundaryID );

			if(targetBoundaryNum != null && bndryTypeHeirarchyLevel != null && topLevelBoundaryID != null)
			{

				System.out.println("calling getBoundary(String)");
				Boundary pb = null;

				if(boundaryServiceImpl != null)
				{
					//pb = bm.getBoundary(Short.parseShort(parentBoundaryNum), Short.parseShort(bndryTypeHeirarchyLevel), Integer.parseInt(topLevelBoundaryID));
					//BoundaryType pbt = btm.getBoundaryType(Short.parseShort(bndryTypeHeirarchyLevel));
					//Iterator itr = pbt.getChildBoundaryTypes().iterator();
					Boundary chB = null;
					//if(itr.hasNext())
					//	chB = new BoundaryImpl((BoundaryType)pbt.getChildBoundaryTypes().iterator().next());
					chB = boundaryServiceImpl.getBoundary(Short.parseShort(targetBoundaryNum),Short.parseShort(bndryTypeHeirarchyLevel), heirarchyType, Integer.parseInt(topLevelBoundaryID));
					pb = chB.getParent();
					//chB.setParent(pb);
					//chB.setName(newBoundaryName);
					//chB.setBoundaryNum(Integer.parseInt(newBoundaryNum));
/*
					short hn = pbt.getHeirarchy();
					short nhn  = ++hn;
					chBt.setHeirarchy(nhn);
					chBt.setName(newBoundaryTypeName);
					pbt.addChildBoundaryType(chBt);
*/
					if(pb != null)
					{
						pb.deleteChild(chB);
					}

					chB.setParent(null);
					boundaryServiceImpl.removeBoundary(chB);

					//btm.createBoundaryType(chBt);
				}
%>
					<jsp:forward page="/BndryAdmin/bndryAdminFrames.jsp">
						<jsp:param name="message" value="Boundary successfully deleted"/>
					</jsp:forward>

<%

			}
			else
				out.println("Please provide both New BoundaryType Name and Parent BoundaryType Name ");

		}else if(operation!= null && operation.equals("edit") && bndryName != null && !bndryName.trim().equalsIgnoreCase("null") && !bndryName.trim().equalsIgnoreCase("") && bndryNum != null && !bndryNum.trim().equalsIgnoreCase("null") && !bndryNum.trim().equalsIgnoreCase("") && heirarchyType != null)
		{

			System.out.println(targetBoundaryNum +","+bndryTypeHeirarchyLevel +","+topLevelBoundaryID );

			if(targetBoundaryNum != null && bndryTypeHeirarchyLevel != null && topLevelBoundaryID != null)
			{

				System.out.println("calling edit(String)");
				Boundary bndry = null;

				if(boundaryServiceImpl != null)
				{
					bndry = boundaryServiceImpl.getBoundary(Short.parseShort(targetBoundaryNum), Short.parseShort(bndryTypeHeirarchyLevel), heirarchyType, Integer.parseInt(topLevelBoundaryID));
					bndry.setName(bndryName);
					bndry.setBoundaryNum(Integer.parseInt(bndryNum));

					boundaryServiceImpl.updateBoundary(bndry);

				}
%>
					<jsp:forward page="/BndryAdmin/bndryAdminFrames.jsp">
						<jsp:param name="message" value="Boundary successfully edited."/>
					</jsp:forward>

<%

			}
			else
				out.println("Please provide both New BoundaryType Name and Parent BoundaryType Name ");

		}
		else if(heirarchyType != null)
			out.println("No operation defined.");
		else
			out.println("System error. Sorry for the inconvenience. heirarchyType is not provided.");

 %>

