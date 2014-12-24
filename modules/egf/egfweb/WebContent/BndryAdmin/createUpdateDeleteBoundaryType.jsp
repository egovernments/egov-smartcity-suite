<%@page import="org.egov.lib.admbndry.ejb.server.BoundaryTypeServiceImpl"%>
<%@ page import="
		org.egov.infstr.utils.*,
		org.egov.lib.admbndry.BoundaryType,
		org.egov.lib.admbndry.BoundaryTypeImpl,
		org.egov.lib.admbndry.HeirarchyType,
		org.egov.lib.admbndry.ejb.api.*,
		java.util.*"
 %>



 <%

		String parentBoundaryTypeName = request.getParameter("ParentBoundaryTypeName");
		String newBoundaryTypeName = request.getParameter("newBoundaryTypeName");
		String operation = request.getParameter("operation");
		String targetBoundaryType = request.getParameter("TargetBoundaryType");
		String changedBoundaryTypeName = request.getParameter("changedBoundaryTypeName");
		HeirarchyType heirarchyType = (HeirarchyType)session.getAttribute("heirarchyType");

		System.out.println("((((((((((heirarchyType :"+heirarchyType.getName()+","+heirarchyType.getId()+"|");

		BoundaryTypeServiceImpl boundaryTypeServiceImpl= new BoundaryTypeServiceImpl();
		
		if(operation!= null && operation.equals("create"))
		{
			if(newBoundaryTypeName != null)
			{

				System.out.println("calling getBoundaryType(String)");
				BoundaryType pbt = null;

				if(boundaryTypeServiceImpl != null)
				{
					short hn = 0;
					short nhn  = ++hn;
					BoundaryType chBt = new BoundaryTypeImpl();

					System.out.println("parentBoundaryTypeName"+parentBoundaryTypeName);
					if( parentBoundaryTypeName != null && ! parentBoundaryTypeName.trim().equalsIgnoreCase("null"))
					{
						System.out.println("entering pbt");
						pbt = boundaryTypeServiceImpl.getBoundaryType(parentBoundaryTypeName, heirarchyType);
						hn = pbt.getHeirarchy();
						nhn  = ++hn;

					}

					chBt.setHeirarchy(nhn);
					chBt.setHeirarchyType(heirarchyType);
					chBt.setName(newBoundaryTypeName);

					if( parentBoundaryTypeName != null && ! parentBoundaryTypeName.trim().equalsIgnoreCase("null"))
					{
						pbt.addChildBoundaryType(chBt);
					}

					boundaryTypeServiceImpl.createBoundaryType(chBt);
				}
%>
					<jsp:forward page="/BndryAdmin/bndryAdminFrames.jsp">
						<jsp:param name="message" value="Boundary Type successfully addded"/>
					</jsp:forward>

<%

			}
			else
				out.println("Please provide both New BoundaryType Name and Parent BoundaryType Name ");

		}
		if(operation!= null && operation.equals("delete"))
		{
			if(parentBoundaryTypeName != null && targetBoundaryType != null)
			{

				System.out.println("calling getBoundaryType(String)");
				BoundaryType pbtype = null, tgbType = null;
/*
				if(btm != null)
				{
					tgbType = btm.getBoundaryType(targetBoundaryType);
					pbtype = tgbType.getParent();
					tgbType.setParent(null);
					//pbtype = btm.getBoundaryType(parentBoundaryTypeName);
					pbtype.setChildBoundaryTypes(new HashSet());


					//BoundaryType chBt = new BoundaryTypeImpl();
					//short hn = pbtype.getHeirarchy();
					//short nhn  = ++hn;
					//chBt.setHeirarchy(nhn);
					//chBt.setName(newBoundaryTypeName);
					//pbtype.addChildBoundaryType(chBt);



					//btm.removeBoundaryType(tgbType);
					//btm.updateBoundaryType(pbtype);
					//btm.updateBoundaryType(tgbType);
					btm.removeBoundaryType(tgbType);
				}
*/

	 		pbtype = boundaryTypeServiceImpl.getBoundaryType(parentBoundaryTypeName, heirarchyType);
			System.out.println("*************** pbtype"+pbtype.getName());
	 		if(!pbtype.getChildBoundaryTypes().isEmpty())
	 		{
				System.out.println("*************** pbtype.getChildBoundaryTypes().isEmpty()"+pbtype.getName());
	 			tgbType  = (BoundaryType)pbtype.getChildBoundaryTypes().iterator().next();
	 			System.out.println("*************** tgbType "+tgbType.getName());
	 			tgbType.setParent(null);
	 		}

	 		pbtype.setChildBoundaryTypes(new HashSet());

//			chBt = new BoundaryTypeImpl();
	//		short hn = pbt.getHeirarchy();
		//	short nhn  = ++hn;
			//chBt.setHeirarchy(nhn);
			//chBt.setName("TESTBT99");
			//pbt.addChildBoundaryType(chBt);

			boundaryTypeServiceImpl.updateBoundaryType(tgbType);
			boundaryTypeServiceImpl.removeBoundaryType(tgbType);

%>
					<jsp:forward page="/BndryAdmin/bndryAdminFrames.jsp">
						<jsp:param name="message" value="Boundary Type successfully deleted"/>
					</jsp:forward>

<%

			}
			else
				out.println("Please provide both Target BoundaryType Name and Parent BoundaryType Name ");

		}
		else if(operation!= null && operation.equals("edit"))
		{
			if(targetBoundaryType != null)
			{
				BoundaryType btype = boundaryTypeServiceImpl.getBoundaryType(targetBoundaryType, heirarchyType);

				btype.setName(changedBoundaryTypeName);

				boundaryTypeServiceImpl.updateBoundaryType(btype);
%>
				<jsp:forward page="/BndryAdmin/bndryAdminFrames.jsp">
					<jsp:param name="message" value="Boundary Type successfully edited"/>
				</jsp:forward>

<%

			}
			else
				out.println("Please provide Target BoundaryType Name.");


		}


 %>