<!--  #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------  -->
<%@page language="java" import="ChartDirector.*,java.sql.*,java.util.*,java.text.*,com.exilant.eGov.src.reports.GISIncomeExpenditure,com.exilant.eGov.src.reports.*,org.egov.infstr.utils.EGovConfig"%>
<%
int index=0;
String[] labels=null;
double[] data = null;
 String    chart1URL           =   null;
 String    imageMap1           =   null;
 
 int index1=0;
 String[] labels1=null;
 double[] data1 = null;
 String      chart2URL         =   null;
 String      imageMap2          =   null;
 
 NumberFormat formatter = new DecimalFormat();
 formatter = new DecimalFormat("###############.00");
 
 String fyearName="";
 try{ fyearName=EGovConfig.getProperty("egf_config.xml","getFinancialyearName","","GISReports");
 }catch(Exception e){
 	System.out.println("Exception in getting the Year:"+e);
 }
 
 System.out.println("Year is :"+fyearName);
 
try{
	 GISIncomeExpenditure obj= new GISIncomeExpenditure();
	 Map accCodeAmountMap = obj.GISReportData("E");
	 labels = new String[accCodeAmountMap.size()];
	 data = new double[accCodeAmountMap.size()];
	
	
		 Iterator itr = accCodeAmountMap.keySet().iterator();
		 while(itr.hasNext())
		 {
		 	labels[index] = (String)itr.next();
		 	 data[index] = Double.parseDouble(formatter.format(Double.parseDouble((String)accCodeAmountMap.get((String)labels[index]))));
		 	index=index+1;
		 	
		 }
		 
	 
 }catch(Exception e) {
	 e.printStackTrace();
	 System.out.println("Exception in main try");
 }
 
 try{
 	 GISIncomeExpenditure obj= new GISIncomeExpenditure();
 	 Map accCodeAmountMap1 = obj.GISReportData("I");
 	 labels1 = new String[accCodeAmountMap1.size()];
 	 data1 = new double[accCodeAmountMap1.size()];
 	
               
 		 Iterator itr = accCodeAmountMap1.keySet().iterator();
 		 
 		 while(itr.hasNext())
 		 {
 		 	labels1[index1] = (String)itr.next();
 		 	data1[index1] = Double.parseDouble(formatter.format(Double.parseDouble((String)accCodeAmountMap1.get((String)labels1[index1]))));
 		  	index1=index1+1;
 		 	
 		 }
 		
 	 
  }catch(Exception e) {
 	 e.printStackTrace();
 	 System.out.println("Exception in main try");
 }
 		
 		PieChart c = new PieChart(1150, 600);
		PieChart c1 = new PieChart(1150, 600);
                //Set the center of the pie at (450, 200) and the radius to 150 pixels
                c.setPieSize(550, 150, 100);
                c.setStartAngle(90);
                c1.setPieSize(550, 150, 100);
		c1.setStartAngle(90);


                //Add Redressed Complaints as Extra Field
                //c.addExtraField(exFields);

                //Add a title to the pie chart with Background colour
               c.addTitle("<*block,valign=absmiddle*>Summary Expenditure<*/*>");
			   c1.addTitle("<*block,valign=absmiddle*>Summary Income<*/*>");
		//Add Legend
                c.addLegend(350, 275, true,"verdana;Mangal",8);
		c1.addLegend(350,275, true,"verdana;Mangal",8);
                //Draw the pie in 3D with Tilting angle 60 degrees
                c.set3D(-1,60);
                c1.set3D(-1,60);
                //Use the side label layout method
                //c.setLabelFormat("{label}: {value} ({percent}%)");
                c.setLabelFormat("{label}: {value} ({percent}%)");
                c.setLabelStyle("verdana",7);
                c.setLabelLayout(Chart.SideLayout, -1, 60, 400);
                c.setColors(Chart.transparentPalette);
                
								
                c1.setLabelFormat("{label}: {value} ({percent}%)");
				c1.setLabelStyle("verdana",7);
				c1.setLabelLayout(Chart.SideLayout, -1, 60, 400);
                c1.setColors(Chart.transparentPalette);

                //Explode all the sectors of the piechart
                for(int i=0; i<index; i++)
                {
                    c.setExplode(i);
                }
		for(int j=0; j<index1; j++)
                {
                    c1.setExplode(j);
                }

                //Set the pie data and the pie labels

	                c.setData(data, labels);
	                c1.setData(data1, labels1);

                //output the chart
                chart1URL = c.makeSession(request, "chart1");
		chart2URL = c1.makeSession(request, "chart2");
                //include tool tip for the chart
                imageMap1 = c.getHTMLImageMap("", "","title='{label}: {value} ({percent}%)'");
                imageMap2 = c1.getHTMLImageMap("", "","title='{label}: {value} ({percent}%)'");

System.out.println(">>>>>>done!!!");
        

 
 %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<!-- Inclusion of the CSS files that contains the styles -->
<link rel=stylesheet href="../exility/global.css" type="text/css">
</head>
<BODY  bottomMargin=0 bgColor=#ffffff background="../images/Financial-accounting-overview.gif" leftMargin=0 topMargin=0 rightMargin=0 marginwidth ="0"  marginheight="0">
<iframe
src ="maintenance.htm" 
width="100%" height="33" noscroll>
</iframe> 


	 <!-- the following displays chart -->
            <img src="../Reports/getchart.jsp?<%=chart1URL%>" usemap="#map1" border="0">

            <!-- the following will display tool tip if we move cursor on image    -->
            <map name="map1"><%=imageMap1%></map>
            
             <!-- the following displays chart -->
	                <img src="../Reports/getchart.jsp?<%=chart2URL%>" usemap="#map2" border="0">
	    
	                <!-- the following will display tool tip if we move cursor on image    -->
            <map name="map2"><%=imageMap2%></map>
    <p><font name="verdana" size=1><b>** All amounts are in Lakhs and for the year <%=fyearName%></b></p>        
</BODY>
</html>
