<%@page import="org.egov.lib.admbndry.ejb.server.BoundaryServiceImpl"%>
<%@page import="org.egov.lib.admbndry.ejb.server.BoundaryTypeServiceImpl"%>
<%@ page import="
   org.egov.infstr.utils.*,
   org.egov.lib.admbndry.BoundaryType,
   org.egov.lib.admbndry.BoundaryTypeImpl,
   org.egov.lib.admbndry.BoundaryImpl,
   org.egov.lib.admbndry.Boundary,
   org.egov.lib.admbndry.ejb.api.*,
  java.util.*"
 %>
 
 <%
 	BoundaryTypeServiceImpl boundaryTypeServiceImpl= new BoundaryTypeServiceImpl();
 	int i =1, j=1;
 	System.out.println("trying again");
 	BoundaryType bt = boundaryTypeServiceImpl.getBoundaryType(j);
 	System.out.println("done");
 	if(bt != null)
 	out.println("hi got results name is "+bt.getName());
 	
 	BoundaryServiceImpl boundaryServiceImpl= new BoundaryServiceImpl();
 	
/* 	Boundary bnd = new BoundaryImpl();
 	bnd.setName("Bangalore");
 	bnd.setBoundaryNum(1);
 	bnd.setBoundaryType(bt);
 	bm.createBoundary(bnd);


//	BoundaryType nbt = new BoundaryTypeImpl(bt);
//	bt.setChild(nbt);
	
//	short h = 2;
//	nbt.setHeirarchy(h);
//	nbt.setName("ZONE");

*/
	int hirt = 3;
 	BoundaryType nbtt = boundaryTypeServiceImpl.getBoundaryType(hirt);

	BoundaryType nbtw = new BoundaryTypeImpl(nbtt);
	//nbt.setChild(nbtw);
	//addChildBoundaryType()
	short hn = 4;
	nbtw.setHeirarchy(hn);
	nbtw.setName("TESTBT");
	//nbtw.setParent(nbtt);	
	//btm.createBoundaryType(nbtt);	
	boundaryTypeServiceImpl.createBoundaryType(nbtw);	
	//btm.updateBoundaryType(nbt);	

/*
*/
/*
	short hir = 2;
 	BoundaryType nbt = btm.getBoundaryType(hir);

 	Boundary bnd = new BoundaryImpl();
 	bnd.setName("ZONE1");
 	bnd.setBoundaryNum(1);
 	bnd.setBoundaryType(nbt);
 	bm.createBoundary(bnd);
 	
 	Boundary bnd1 = new BoundaryImpl();
 	bnd1.setName("ZONE2");
 	bnd1.setBoundaryNum(2);
 	bnd1.setBoundaryType(nbt);
 	bm.createBoundary(bnd1);
 	
	short hir = 3;
 	BoundaryType nbt = btm.getBoundaryType(hir);

 	Boundary bnd = new BoundaryImpl();
 	bnd.setName("WARD1");
 	bnd.setBoundaryNum(1);
 	bnd.setBoundaryType(nbt);
 	
 	//Boundary bnd = bm.get();
 	
 	bm.createBoundary(bnd);
 	
 	Boundary bnd1 = new BoundaryImpl();
 	bnd1.setName("WARD2");
 	bnd1.setBoundaryNum(2);
 	bnd1.setBoundaryType(nbt);
 	bm.createBoundary(bnd1);


	short bnum = 3;
	short hir = 3;
 	BoundaryType nbt = btm.getBoundaryType(hir);
	
	Boundary fb = bm.getBoundary(bnum, nbt, 0);
	
	out.println("hey found boundary:"+fb.getName());


	short bnum = 2;
	short hir = 3, hir1 = 2;
 	Boundary bnd1 = new BoundaryImpl();
 	bnd1.setName("WARD3");
 	bnd1.setBoundaryNum(1);
 	BoundaryType nbt = btm.getBoundaryType(hir);
 	BoundaryType nbtz = btm.getBoundaryType(hir1);
	
	Boundary fb = bm.getBoundary(bnum, nbtz, 0);
 	
 	bnd1.setBoundaryType(nbt);
 	bnd1.setParent(fb);
 	bm.createBoundary(bnd1);
	
	out.println("hey found boundary:"+fb.getName());
*/

	short bnum = 2;
	int hir = 1, hir1 = 2;

 	BoundaryType nbt = boundaryTypeServiceImpl.getBoundaryType(hir);
	
	Set set = nbt.getChildBoundaryTypes();	
	out.println("hey found boundary Type "+nbt.getName());

		
	for(Iterator itr=set.iterator();itr.hasNext();)
	{
		BoundaryType bt1 = (BoundaryType)itr.next();
		out.println("\n hey found childboundary Type "+bt1.getName()+" hir:"+bt1.getHeirarchy()+" parent:"+bt1.getParent().getName()+" child:"+nbt.getChildBoundaryTypes());
	}
	
	
	
	

 %>