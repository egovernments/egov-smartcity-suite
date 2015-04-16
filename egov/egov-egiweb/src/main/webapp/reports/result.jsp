<%@ page import="java.util.*,org.egov.infstr.utils.*,org.egov.lib.rjbac.user.dao.*,org.egov.lib.rjbac.user.*,
org.egov.lib.security.terminal.model.*,java.text.*,org.egov.lib.security.terminal.dao.*,
org.apache.commons.lang.StringUtils" %>
<%@ include file="/includes/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>User Counter Map Reports</title>



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
<p> User Counter Map Reports</p></td>
  </tr>

</table>
<%
    try{
		List userCounterList = (List)request.getAttribute("userCounterList");
   	 	System.out.println("the size of the list >>>>> " +userCounterList.size());
   	 	  	LinkedList links = new LinkedList();
		  	request.setAttribute("links",links);

		  	if(userCounterList!= null && !userCounterList.isEmpty())
		  	{
		  		for(int i=0;i<userCounterList.size();i++)
		  		{
					Hashtable map=new Hashtable();
					String ip = "";
					String counterName="";
					String userName="";
					String userId="";
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
					Set locationIPMapSet = cataEl.getCounterId().getLocationIPMapSet();
					if(locationIPMapSet!=null)
					{
						int k=0;
						int j= locationIPMapSet.size()-1;
						for(Iterator iter = locationIPMapSet.iterator(); iter.hasNext();)
						{
							LocationIPMap locip = (LocationIPMap)iter.next();
							if(k<j)
							{
								ip =ip+locip.getIpAddress()+","+"";
							}
							if(k==j)
							{
								ip= ip+locip.getIpAddress();
							}
							k++;
						}
					}
					LocationDAO daoObj = new LocationHibernateDAO(Location.class,HibernateUtil.getCurrentSession());
					List counterList=null;
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
							counterName =counterName+loc.getName()+","+"";
						}
						if(k==h)
						{
							counterName= counterName+loc.getName();
						}
						k++;
					}
					userName =cataEl.getUserId().getUsername();
					userId =cataEl.getUserId().getId().toString();
				map.put("name",name);
				map.put("ip",ip);
				map.put("counterName",counterName);
				map.put("userName",userName);
				map.put("userId",userId);
				links.add(map);
				}




			}


	    }
	catch(Exception e){}
%>

 <display:table name="links" id="eid" cellspacing="0" style="width: 750;"
  export="true" defaultsort="2" pagesize = "15" sort="list"  class="its"  >

	 <display:column style="width:5%"   property="name" title="Location name" />
	 <display:column style="width:5%"   property="ip" title="IP Address" />
	 <display:column style="width:5%"   property="counterName" title="Associated Counters" />
	 <display:column style="width:5%" title="Associated Users">
  		<a href="${pageContext.request.contextPath}/reports/userMappingHistory.jsp?userId=<%= ((Map)pageContext.getAttribute("eid")).get("userId")%>"><%= ((Map)pageContext.getAttribute("eid")).get("userName")%></a>
	 </display:column>



</display:table>



</table>
</center>
</form>

</body>