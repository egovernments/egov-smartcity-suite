<%@ page import="java.util.*,org.egov.infstr.utils.*,org.egov.lib.rjbac.user.dao.*,org.egov.lib.rjbac.user.*,
org.egov.lib.security.terminal.model.*,java.text.*,org.egov.lib.security.terminal.dao.*,
org.apache.commons.lang.StringUtils" %>
<%@ include file="/includes/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>User Mapping Reports</title>



</head>


<body>

<!-- Header Section Begins -->

<!-- Header Section Ends -->

<table align='center' id="table2">
<tr>
<td>
<!-- Tab Navigation Begins -->

<!-- Tab Navigation Ends -->

<!-- Body Begins -->


<!-- Body Begins -->

<div align="center">
<center>
<form>
<table  style="width: 750px;" cellpadding ="0" cellspacing ="0" border = "1"  >
<tbody>
<tr>
  <td colspan="8" height=20 bgcolor=#dddddd align=middle  class="tableheader">
  <%
      String userId = request.getParameter("userId");
    User userobj = (User) HibernateUtil.getCurrentSession().get(User.class, Integer.valueOf(userId));
  %>
<p> User Mapping Reports : <%=userobj.getUsername()%></p></td>
  </tr>

</table>

<%

   	 try{

   	 	UserCounterDAO objDao = (UserCounterDAO) new UserCounterHibernateDAO();
		List userCounterList =(List)objDao.getUserCounterMapForUserId(new Integer(userId));
		System.out.println("the size of the list &&&&&>>>>> " +userCounterList.size());
			LinkedList links = new LinkedList();
		  	request.setAttribute("links",links);

		  	if(userCounterList!= null && !userCounterList.isEmpty())
		  	{
		  		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

				for(int i=0;i<userCounterList.size();i++)
		  		{
					Hashtable map=new Hashtable();
					String fromDate = "";
					String counterName="";
					String toDate="";
					String name = "";
					UserCounterMap cataEl = (UserCounterMap)userCounterList.get(i);
					if(new Integer(1).equals(cataEl.getCounterId().getIsLocation()))
					{
						name = cataEl.getCounterId().getName();
					}
					else if(new Integer(0).equals(cataEl.getCounterId().getLocationId().getIsLocation()))
					{
						name = cataEl.getCounterId().getLocationId().getName();
					}
					LocationDAO daoObj = new LocationHibernateDAO(Location.class,HibernateUtil.getCurrentSession());
					List counterList = null;
					if(new Integer(1).equals(cataEl.getCounterId().getIsLocation()))
					{
						counterList = (List)daoObj.getCountersByLocation(cataEl.getCounterId().getId().intValue());
					}
					else if(new Integer(0).equals(cataEl.getCounterId().getLocationId().getIsLocation()))
					{
						counterList = (List)daoObj.getCountersByLocation(cataEl.getCounterId().getLocationId().getId().intValue());
					}
					int k=0;
					int h= counterList.size()-1;
					for(int j=0;j<counterList.size();j++)
					{
						Location loc = (Location)counterList.get(j);
						if(k<h)
						{
							counterName =counterName+loc.getName()+","+" ";
						}
						if(k==h)
						{
							counterName= counterName+loc.getName();
						}
						k++;

					}
					fromDate =sdf.format(cataEl.getFromDate());
					if(cataEl.getToDate()!=null)
					{
						toDate =sdf.format(cataEl.getToDate());

					}
					else
					{
						toDate ="Till Date";
					}

				map.put("name",name);
				map.put("counterName",counterName);
				map.put("fromDate",fromDate);
				map.put("toDate",toDate);
				links.add(map);
				}




			}


	    }
	catch(Exception e){}
  %>

 <display:table name="links" id="eid" cellspacing="0" style="width: 750px;"
  export="true" defaultsort="2" pagesize = "15" sort="list"  class="its"  >

	 <display:column style="width:5%"  property="name" title="Location name" />
	 <display:column style="width:5%"  property="counterName" title="Associated Counters" />
 	 <display:column style="width:5%"  property="fromDate" title="From Date" />
	 <display:column style="width:5%"  property="toDate" title="To Date" />





</display:table>



</table>
</center>
</form>

</body>