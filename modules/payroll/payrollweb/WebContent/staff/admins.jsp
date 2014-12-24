<%@ page import="
		java.util.*,
		org.egov.infstr.utils.*,
		org.egov.commons.utils.EgovInfrastrUtil,
		
		org.egov.lib.rjbac.dept.*,
		org.egov.lib.rjbac.role.*,
		org.egov.lib.rjbac.user.*,
		org.egov.lib.rjbac.dept.ejb.api.DepartmentService,
		org.egov.lib.rjbac.role.ejb.api.*,
		org.egov.infstr.utils.HibernateUtil,
		org.egov.lib.rjbac.dept.ejb.server.DepartmentServiceImpl,
		org.egov.lib.rjbac.role.ejb.server.RoleServiceImpl"
		
 %>

<%@ page import="javax.servlet.http.HttpServletRequest,
				javax.servlet.http.HttpSession" 
%>

<%!
	public static void goRecursiveForDept(Department dept, JspWriter out,RoleService roleService,int toplevelID,DepartmentService deptMgr,User usr) throws Exception
	{
		System.out.println("************in recursive DEPARTMENT....!!");
		
		if(dept != null)
		{
		
			System.out.println("dept!=null//////////////DEPT_NAME="+dept.getDeptName()+" DEPT_ID="+dept.getId());
				Set roleSet = null;
			
			if(!usr.getRoles().isEmpty() )
			{
				System.out.println("////////////// dept.getRoles !=null");
				roleSet = usr.getRoles();
				//roleSet=deptMgr.getAllRolesByDept(dept.getId());
				
					//System.out.println("%%%%%%%%%%%%%% Here1"+bndry.getDeptName()+"No:"+parentBndryTypeHeirarchyLevel);
				out.println("<li id=\"foldheader\"><a href=\"../admin/DeleteUpdateDepartment.do?deptid="+dept.getId()+"\"target=\"rowsideWindow\">"+dept.getDeptName()+"</a></li> &nbsp" +"<ul id=\"foldinglist\" style=\"display:none\" style=&{head};>");
				for(Iterator itr=roleSet.iterator();itr.hasNext();)
				{
					Role role = (Role)itr.next();
					System.out.println("child values....!!"+role.getRoleName());
					goRecursiveForRole(role, out,roleService,toplevelID);

					if(! itr.hasNext())
						out.println("</ul>");
				}
				System.out.println(">>>>>>>>>>>>>>>>>> in if blk");
			}
			else
				out.println("<li id=\"foldheader\"><a href=\"../admin/DeleteUpdateDepartment.do?deptid="+dept.getId()+"\"target=\"rowsideWindow\">"+dept.getDeptName()+"</a></li> &nbsp" +"<ul id=\"foldinglist\" style=\"display:none\" style=&{head};></ul>");
			
		}
	}
	public static void goRecursiveForRole(Role role, JspWriter out,RoleService roleService,int toplevelID) throws Exception
	{
		System.out.println("************in recursive....ROLE !!"+role.getRoleName()+" ROLE="+role);
				//String	toplevelID = 		(String)session.getAttribute("org.egov.topBndryID");;//(String)gSession.getAttribute("org.egov.topBndryID");


		if(role != null)
		{
			List userSet = null;
			System.out.println("goRecursiveForRole:::userSet::"+userSet);

			//userSet = roleServiceImpl.getAllUsersByRole(role,toplevelID);
			userSet = null;
			System.out.println("goRecursiveForRole:::userSet::"+userSet);

			if(!userSet.isEmpty())
			{
				//System.out.println("%%%%%%%%%%%%%% Here1"+bndry.getDeptName()+"No:"+parentBndryTypeHeirarchyLevel);
				out.println("<li id=\"foldheader\"><a href=\"../admin/Role.do?roleid="+role.getId()+" \"target=\"rowsideWindow\">"+role.getRoleName()+"</a></li> &nbsp" +"<ul id=\"foldinglist\" style=\"display:none\" style=&{head};>");


				for(Iterator itr=userSet.iterator();itr.hasNext();)
				{
					User user = (User)itr.next();
					//System.out.println("child values....!!");
					goRecursiveForUser(user, out);

					if(! itr.hasNext())
						out.println("</ul>");
				}
				System.out.println(">>>>>>>>>>>>>>>>>> in if blk");
			}
			else
			out.println("<li id=\"foldheader\"><a href=\"../admin/Role.do?roleid="+role.getId()+" \"target=\"rowsideWindow\">"+role.getRoleName()+"</a></li> &nbsp" + "<ul id=\"foldinglist\" style=\"display:none\" style=&{head};></ul>");
			

		}
		System.out.println(">>>>>>>>>>>>>>>>>> goRecursiveForRole end");
	}
	
	public static void goRecursiveForUser(User user, JspWriter out) throws Exception
	{
		System.out.println("************in recursive....USER!!"+user.getUserName());

		if(user != null)
		{
			
					//System.out.println("%%%%%%%%%%%%%% Here1"+bndry.getDeptName()+"No:"+parentBndryTypeHeirarchyLevel);
			out.println("<li id=\"foldheader\"><a href=\"../admin/DeleteUpdateUserView.do?userid="+user.getId()+" \"target=\"rowsideWindow\">"+user.getUserName()+"</a></li> &nbsp" + "<ul id=\"foldinglist\" style=\"display:none\" style=&{head};></ul>");
		}
	}

%>
<%

		DepartmentServiceImpl departmentServiceImpl=new DepartmentServiceImpl();
		RoleServiceImpl roleServiceImpl=new RoleServiceImpl();
		List deptList = null;

		String topId = (String)session.getAttribute("org.egov.topBndryID");
		System.out.println("&&&&org.egov.topBndryID "+topId);
		System.out.println("\\\\\\\\\\\\\topID"+topId);
		int topID_int = -1;
		
		if(topId!=null)
		{
			//topID_int = new Integer(topId).intValue();
			topID_int = Integer.parseInt(topId);
			System.out.println("\\\\\\\\\\\\\topID"+topID_int);
		}

		
		if(departmentServiceImpl!=null)
			deptList = departmentServiceImpl.getAllDepartments();	
	%>



<html>
<title>
	Create Update Admins
</title>
<head>


<script language="JavaScript">
	function selectDeselect(obj)
	{
		tempObj = obj;
		//alert('____here');
		obj.focus();
		document.alinkColor="green";
	}
	
	function selectDeselectLink()
	{
		//tempObj = obj;
		//alert('____here');
		//obj.focus();
		document.alinkColor="green";
	}


</script>
	<style>
	
	#foldheader{cursor:pointer;cursor:hand ; font-weight:normal ;
	list-style-image:url(/ptisnn/images/collapsed_button.gif)}
	#foldinglist{
	list-style-image:url(/ptisnn/images/expanded_button.gif)}
	
	</style>
	<script language="JavaScript1.2">
	

	//Smart Folding Menu tree- By Dynamic Drive (rewritten 03/03/02)
	//For full source code and more DHTML scripts, visit http://www.dynamicdrive.com
	//This credit MUST stay intact for use

	var head="display:''"
	img1=new Image()
	img1.src="/images/collapsed_button.gif"
	img2=new Image()
	img2.src="/images/expanded_button.gif"

	var ns6=document.getElementById&&!document.all
	var ie4=document.all&&navigator.userAgent.indexOf("Opera")==-1
	//alert("ie4="+ie4+" ns6="+ns6);
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
	cur.style.listStyleImage="url(/ptisnn/images/expanded_button.gif)"
	}
	else{
	foldercontent.style.display="none"
	cur.style.listStyleImage="url(/ptisnn//images/collapsed_button.gif)"
	}
	}
	}

	if (ie4||ns6)
	document.onclick=checkcontained

	
	</script>
	<script>

		function showHiddenTextField(varId)
		{
			//alert("hi"+this.document.AdminBoundaryForm);

			//alert('hi'+this.document.AdminBoundaryForm.id.style.visibility);
			//this.document.AdminBoundaryForm.inText6.style.visibility= "visible"

		}
	</script>
</head>
<body bgcolor="#E1D4C0"  onload="selectDeselectLink();">
 <% 
  	EgovInfrastrUtil egovInfrastrUtil = new EgovInfrastrUtil();
  	Map allInstallments = egovInfrastrUtil.getAllInstallmentYears();
  	session.setAttribute("allInstallments", allInstallments);    
 %>

<font face="Comic">


<form name="afterUpdateExemptionForm">
<table border="0" width=220 bgcolor="#E1D4C0"  align="left"  >
<tr>
<td>

<br><font color=red><b>
</b><br></font>
<span align="center"><B>Admin Menu</B></span>

<br><br>


    <% try{    
    	    
    		//Map 
    		allInstallments=(Map) session.getAttribute("allInstallments");
    		Integer insYear=null;
    		System.out.println("List size in JSP:"+allInstallments.size());
    		Iterator itr=allInstallments.keySet().iterator();%>

		<!-- 1.EXEMPTION MASTER --> 
	        <ul>
		   <li id="foldheader"><b>ExemptionMaster</li>
		   <ul id="foldinglist" style="display:none" style=&{head};> 
		   <% 
		   System.out.println("Raj-Exemption:In tree Jsp Loop");
		   while(itr.hasNext())
		   {
		      	insYear=(Integer)itr.next();	
	
			String fd=String.valueOf(allInstallments.get(insYear));
		     %>	
 			<li id="foldheader"><a href="/ptisnn/admin/BeforeUpdateExemption.do?selExemYearId=<%=insYear%>" target="rowsideWindow"><%=fd%></a></li>
			<ul id="foldinglist" style="display:none" style=&{head};></ul>
		<%} %>
		  </ul>
	       </ul>

		<!-- 2.REBATE MASTER -->
		<ul>
		   <li id="foldheader"><b>RebateMaster</li>
		  <ul id="foldinglist" style="display:none" style=&{head};> 
	  
		  <%        
		  System.out.println("Raj-RebateMaster:In tree Jsp Loop");
		
		  itr=allInstallments.keySet().iterator();
		  while(itr.hasNext())
		  {
 	                insYear=(Integer)itr.next();	
				 	
			String fd=String.valueOf(allInstallments.get(insYear));
		   %>

			<li id="foldheader"><a href="/ptisnn/admin/BeforeUpdateRebate.do?selRebYearId=<%=insYear%>" target="rowsideWindow"><%=fd%></a></li>
			<ul id="foldinglist" style="display:none" style=&{head};> </ul>
  
		<%} %>
		</ul>
	      </ul>

		
		<!-- 3.DEPRECIATION MASTER -->
		<ul>
 		  <li id="foldheader"><b>DepreciationMaster</li> 
		    <ul id="foldinglist" style="display:none" style=&{head};>		   
			
		    <% System.out.println("Raj-Depreciation: In tree Jsp Loop");
		      itr=allInstallments.keySet().iterator();
		      while(itr.hasNext())
		      {
 	                         insYear=(Integer)itr.next();
			String fd=String.valueOf(allInstallments.get(insYear));
                         
		      %>

			<li id="foldheader"><a href="/ptisnn/admin/BeforeUpdateDepreciation.do?selDepYearId=<%=insYear%>" target="rowsideWindow"><%=fd%></a></li>
			<ul id="foldinglist" style="display:none" style=&{head};>  </ul>
  
		    <%}%>
		</ul>
	      </ul>
	      
	      <!-- 4.CESS MASTER -->
	      <ul>
	      <li id="foldheader"><b>CessMaster</li> 
		   <ul id="foldinglist" style="display:none" style=&{head};>
 	               <%  System.out.println("Raj-Cess:In tree Jsp Loop");
		         itr=allInstallments.keySet().iterator();
		         while(itr.hasNext())
		         {
		    	 insYear=(Integer)itr.next();
		         String fd=String.valueOf(allInstallments.get(insYear));
		      %>
		         <li id="foldheader"><a href="/ptisnn/admin/BeforeUpdateCess.do?selCessYearId=<%=insYear%>" target="rowsideWindow"><%=fd%></a></li>
		         <ul id="foldinglist" style="display:none" style=&{head};></ul>
  
		       <%}%>
		  </ul>
	      </ul>
  
	      <!-- 5.CATEGORY MASTER -->
	      <ul>
	      <li id="foldheader"><b>CategoryMaster</li> 
	    	   <ul id="foldinglist" style="display:none" style=&{head};> 
		      <%  System.out.println("Raj-Category:In tree Jsp Loop");
		          itr=allInstallments.keySet().iterator();

		          while(itr.hasNext())
		          {
			    insYear=(Integer)itr.next();
			    String fd=String.valueOf(allInstallments.get(insYear));
			%>
                        <li id="foldheader"><a href="/ptisnn/admin/BeforeUpdateCategory.do?selCategYearId=<%=insYear%>" target="rowsideWindow"><%=fd%></a></li>
			<ul id="foldinglist" style="display:none" style=&{head};></ul>  
			<%}%>
		    </ul>
		</ul>

	      <!-- 6.INTEREST MASTER -->
	      <ul>
		  <li id="foldheader"><b>InterestMaster</li> 
		   <ul id="foldinglist" style="display:none" style=&{head};>

	       		<%  System.out.println("Raj-Interest:In tree Jsp Loop");
		   	itr=allInstallments.keySet().iterator();				   
		  	while(itr.hasNext())
		  	{
			  insYear=(Integer)itr.next();
			  String fd=String.valueOf(allInstallments.get(insYear));
			%>
			<li id="foldheader"><a href="/ptisnn/admin/BeforeUpdateInterest.do?selIntrstYearID=<%=insYear%>" target="rowsideWindow"><%=fd%></a></li>
			<ul id="foldinglist" style="display:none" style=&{head};></ul>
  
			<%}%>
		</ul>
	     </ul>


	      <!-- 7.TRANSACTION LOCATION MASTER -->
	      <ul>
		  <li id="foldheader"><a href="/ptisnn/admin/BeforeUpdateTransactionLoc.do" target="rowsideWindow"><b>TransactionLocMaster</a></li> &nbsp;&nbsp;&nbsp;&nbsp;
		   <ul id="foldinglist" style="display:none" style=&{head};>  </ul>
	      </ul>

	      <!-- 8.STRUCTURAL CLASSIFICATION MASTER -->
	      <ul>
		  <li id="foldheader"><b>StructuralClsMaster</a></li> 
		   <ul id="foldinglist" style="display:none" style=&{head};>	   

		         <% System.out.println("Raj-StructuralClsfctn:In tree Jsp Loop");
			  itr=allInstallments.keySet().iterator();
			  while(itr.hasNext())
			  {
 	                    insYear=(Integer)itr.next();
			    String fd=String.valueOf(allInstallments.get(insYear));
			%>
			<li id="foldheader"><a href="/ptisnn/admin/AfterUpdateStrucCls.do?selStrClsYearId=<%=insYear%>" target="rowsideWindow"><%=fd%></a></li>
			<ul id="foldinglist" style="display:none" style=&{head};> </ul>
  			<%}%>
		</ul>
	    </ul>
		
	      <!-- 9.BOUNDARY CATEGORY MASTER -->
 	<ul>
	  <li id="foldheader"><b>BoundaryCategoryMaster</li> 
	   <ul id="foldinglist" style="display:none" style=&{head};>
	      <li id="foldheader"><a href="/ptisnn/admin/BeforeCreateBndryCategory.do" target="rowsideWindow">Create</a></li>
			<ul id="foldinglist" style="display:none" style=&{head};></ul>
	      <li id="foldheader"><a href="/ptisnn/admin/BeforeUpdateBndryCategory.do"target="rowsideWindow">Update</a></li>
			<ul id="foldinglist" style="display:none" style=&{head};></ul>
	  </ul>
       </ul>




	      <!-- 10.DEPARTMENT MASTER -->



<%

System.out.println("DEPT LISTTTTTTTTTTTTTTT="+deptList);
if(deptList != null)
	{
		List closeDeptUlList = new ArrayList();
		System.out.println("deptList : "+deptList.size());
		if(deptList.size() <= 0 )
		{
%>

		<ul>
		  <li id="foldheader"><a href="../admin/rjbac/department/createDepartment.jsp" target="rowsideWindow"><b>Departments</b></a></li> &nbsp;
		   <ul id="foldinglist" style="display:none" style=&{head};>
		   </ul>
		</ul>
<%
		}
		else
		{
%>
			<ul>
			  <li id="foldheader"><a href="../admin/rjbac/department/createDepartment.jsp"target="rowsideWindow"><b>Departments</b></a></li> &nbsp;
			   <ul id="foldinglist" style="display:none" style=&{head};>
<%
			closeDeptUlList.add("OneMore");
			closeDeptUlList.add("OneMore");
		}

		if(deptList.size() > 0 )
			while(true)
			{
				for(itr=deptList.iterator();itr.hasNext();)
				{
					//System.out.println("calling recursive..");
					Department dept = (Department)itr.next();
					goRecursiveForDept(dept, out,roleManager,topID_int,deptMgr);
				}
				break;
			}

			for(Iterator itr2 = closeDeptUlList.iterator();itr2.hasNext();)
			{
				itr2.next();
%>
				</ul>
<%
			}

	}
%>




<%

}
	catch(Exception exp)
	{
		exp.printStackTrace();
	}

%>
<%

	}
	catch(Exception exp)
	{
		/*logger.error("Error in Hibernate Session doFilter" + exp.getMessage());
		Throwable th = exp.getCause();
		logger.error("th::"+th);
		while(th != null)
		{
		logger.error("th:"+th.getMessage());
		logger.error("th.getCause()"+th.getCause());
		//logger.error("th.getCause().getMessage()"+th.getCause().getMessage());
		th = th.getCause();
		}*/

		HibernateUtil.rollbackTransaction();
		exp.printStackTrace();
	}
	finally
	{
		//logger.info(">>>>Closing Session");
		HibernateUtil.commitTransaction();
		HibernateUtil.closeSession();
	}



%>

</font>

</tr>
</td>
</table>
</form>



</body>
</html>