   <%@ page import= "org.json.JSONArray,
                    java.util.*,
                    org.egov.pims.commons.*,
                    org.apache.log4j.Logger,
                    org.json.JSONException,
                    org.egov.exceptions.EGOVException,
                   
		    org.egov.pims.service.*" 
		    %>
<%
 Date toDate = new Date();
String beginsWith = request.getParameter("beginsWith");
String desTempId = request.getParameter("desId");
System.out.println(desTempId);
String deptTempId = request.getParameter("deptId");
String jurTempId = request.getParameter("jurId");
String roleTempId= request.getParameter("roleId");
String pos= request.getParameter("pos");
Integer desId=null;
Integer deptId=null;
Integer jurId=null;
Integer roleId=null;


System.out.println(">DDDDDDDDDDDD>>>>"+pos);
	  if (desTempId==null||desTempId=="")
	    {
		      desId = null;
	    }
          else
	   {
		       desId = new Integer(desTempId);
	    
	    }
		  
	  if(deptTempId==null||deptTempId=="")
	   {
		       deptId =null;
	   } 
	  else
           {
		        deptId = new Integer(deptTempId);
	   }
	  if(jurTempId==null ||jurTempId=="" )
	   { 
		        jurId=null;
	   }
	 else
	  {
		   	jurId = new Integer(jurTempId );
	   }
         if(roleTempId==null||roleTempId=="")
         {
         
		   roleId=null;
         }
         else
         {  
                        roleId= new Integer(roleTempId);
         }
	List<String> posList = new ArrayList<String>(); 
	SearchPositionService searchPos= new SearchPositionService();
	posList=searchPos.getPositionBySearchParameters(pos,desId,deptId,jurId,roleId,toDate,0);
   
		   if(posList!=null)
				System.out.println("posList.size()>>>>>>>>>>>>>>>>>>>"+posList.size());

    
    JSONArray posArray  = new JSONArray();
    if (posList != null) {
       for(String  indPos: posList)
       {
    	pos = indPos.replaceAll("~"," - ");
        posArray.put(pos );
                
            }
        }
    
        out.println(posArray.toString());
        response.setContentType("text/x-json;charset=utf-8");
	response.setHeader("Cache-Control", "no-cache");
	
%>