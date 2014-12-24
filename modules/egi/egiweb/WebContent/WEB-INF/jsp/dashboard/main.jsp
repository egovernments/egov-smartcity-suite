<%@ include file="/includes/taglibs.jsp" %>	
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<head>
	<center>
	    <title>Dashboard</title>
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/css/dashboard/dashboard.css'/>" />
		<title>Dashboard Page</title>
		<script>
		function getSubMenuNames()
		{				
			  //alert('Inside getMainMenuItems');			 
			  var menuItems = {"subMenuNames": [
				{"name": "finMan_submenu"},
				{"name": "expMan_submenu"},
				{"name": "revMan_submenu"},
				{"name": " pgr_submenu"}				
			    ]
			 };

			return menuItems;
		  }
		function getSubMenuItems()
		  {				
			 // alert('Inside getMenuItems');
			  var url1="/jasperserver/flow.html?_flowId=viewReportFlow&ndefined=&ParentFolderUri=%2Fanalysis%2Freports&reportUnit=%2Fanalysis%2Freports%2F";
			  var url2="&j_username=jasperadmin&j_password=jasperadmin&decorate=no&output=html&dashboard=true&userLocale=en_IN";
			  var analysisUrl = "/jasperserver/olap/viewOlap.html?ndefined&name=/analysis/reports/PropertyDemandView";
			  var menuItems = {"subMenuItems": [
				{"name": "finBudget", "src": url1+"BudgetVsActuals"+url2},
				{"name": "finIncome", "src": "" },
				{"name": "finColl", "src": "" },
				{"name": "expZoneWard", "src": url1+"CapRev"+url2 },
				{"name": "expTopCon", "src": "" },
				{"name": "expPurCons", "src": "" },
				{"name": "revTopDemand", "src": "" },
				{"name": "revPropDem", "src": analysisUrl + url2 },
				{"name": "revYrWise", "src": "" },
				{"name": "revTopDef", "src": "" },
				{"name": "revArrColl", "src": "" },
				{"name": "revLicIss", "src": "" },
				{"name": "pgrGrvReg", "src": url1+"PgrEffReport"+url2 },
				{"name": "pgrGrvRed", "src": "" },
                            {"name": "revTopWardWise", "src": "" },
                           {"name": "bndReport", "src": "" }
			    ]
		 	};
					
			return menuItems;
		  }
		  		  
		   function loadCurView()
		   {		   	
		   	var divNode = document.getElementById("mainmenu");
		   	var subNavItems = divNode.getElementsByTagName("li");		   	
		   	subNavItems[0].setAttribute("class","current");
		    	subNavItems[0].parentNode.style.visibility="visible";
		    	var subMenu = subNavItems[0].getAttribute("name")+"_submenu";		    	   
			for (i=0; i<subNavItems.length; i++) 
			{		
			 	if(i != 0)
			 	{
					var hideMenu = subNavItems[i].getAttribute("name")+"_submenu";					
					document.getElementById(hideMenu).style.display = "none";								
				}
			}				
			showSubMenu(null, subMenu,"");		   		   					   		  
		   }
		   function showMainMenu(obj, idSelected) 
		   {		   		
			var divNode = document.getElementById("mainmenu");
			document.getElementById("mainmenu").style.display = "";
			var subNavItems = divNode.getElementsByTagName("li");
			var subMenu = document.getElementById(idSelected+"_submenu").getAttribute("name");			    
			document.getElementById(subMenu).style.display = "";
			var subMenuNode = document.getElementById(subMenu);			    		    
			for (i=0; i<subNavItems.length; i++) 
			{								
				subNavItems[i].setAttribute("class", "");					
			}				
			var subMenulist = getSubMenuNames();				
			for (i=0; i<subMenulist.subMenuNames.length; i++) 
			{				
			  if(subMenu != subMenulist.subMenuNames[i].name)
			  {				  	
				var namelist = eval('(' +subMenulist.subMenuNames[i].name+ ')');				  									  	
				document.getElementById(namelist.getAttribute("name")).style.display = "none";
			  }
			}				
			var subNavLiNode = obj.parentNode;					
			subNavLiNode.setAttribute("class","current");							
			document.getElementById(subMenu).style.display = "";	
			document.getElementById(subMenu).parentNode.style.display = "";	
			var subdivnode = document.getElementById(subMenu);
			showSubMenu(obj,idSelected+"_submenu", ""); 
		    }
		    		    
		    function showSubMenu(obj,submenuId, idSelected) 
		    {
		    	var menuItems = getSubMenuItems();		    	
		    	var divNode = document.getElementById(submenuId);		    	
		    	var reportNode = document.getElementById("reportFrame");		    	
			var subNavItems = divNode.getElementsByTagName("li");
			var border = document.getElementById("border");
			border.style.visibility="visible";				
			for (i=0; i<subNavItems.length; i++) 
			{		    						  
			  if(idSelected == subNavItems[i].getAttribute("name"))
			  {								  
				subNavItems[i].setAttribute("class","selected");
			  }
			  else
			  {				  	
				subNavItems[i].setAttribute("class","");	
			  }
			}
			if(idSelected == "")
			{
				subNavItems[0].setAttribute("class","selected");
				idSelected=subNavItems[0].getAttribute("name");
			}
			var subNavLiNode = null;			
			if( obj == null)
				subNavItems[0].setAttribute("class","selected");			
		    	for (i=0; i<menuItems.subMenuItems.length; i++) 
			{
			  if(idSelected == menuItems.subMenuItems[i].name)
			  {				
				reportNode.setAttribute("src",menuItems.subMenuItems[i].src);
				reportNode.parentNode.style.visibility="visible";		    
				break;
			  }
			}
			    
			   
		    }		    
		</script>
	</head> 

	<body onload="loadCurView();">
	
	<div id="mainmenu" name="mainmenu" class="header">	
	  <ul>
	   
	    <li id="expMan" name="expMan"><a href="#" onclick="showMainMenu(this,'expMan')">Expenditure Management</a></li>
	    <li id="revMan" name="revMan"><a href="#" onclick="showMainMenu(this,'revMan')">Revenue Management</a></li>
 	    <li id="finMan" name="finMan"><a href="#" onclick="showMainMenu(this,'finMan')">Financial Management </a></li>
	    <li id="pgr"  name="pgr"><a href="#" onclick="showMainMenu(this,'pgr')">Citizen Sevice Delivery</a></li>
	    
	
	  </ul>
	</div>

	
	<div id="finMan_submenu" name="finMan_submenu" class="navigation">
	<ul> 
	<li id="finBudget" name="finBudget" ><a href="#" onclick="showSubMenu(this,'finMan_submenu','finBudget')"><span>Budget Variance</span></a></li> 
	<li id="finIncome" name="finIncome" ><a href="#" onclick="showSubMenu(this,'finMan_submenu','finIncome')"><span>Income Expense</span></a></li> 
	<li id="finColl" name="finColl" ><a href="#" onclick="showSubMenu(this,'finMan_submenu','finColl')"><span>Monthly Collection</span></a></li> 
	</ul> 
	</div>
	
	<div id="expMan_submenu" name="expMan_submenu" class="navigation">
	<ul> 
	<li id="expZoneWard" name="expZoneWard" ><a href="#" onclick="showSubMenu(this,'expMan_submenu','expZoneWard')"><span>Capital Revenue Spend</span></a></li> 
	<li id="expTopCon" name="expTopCon" ><a href="#" onclick="showSubMenu(this,'expMan_submenu','expTopCon')"><span>Top contractors/suppliers</span></a></li> 
	<li id="expPurCons" name="expPurCons" ><a href="#" onclick="showSubMenu(this,'expMan_submenu','expPurCons')"><span>Purpose wise consumption</span></a></li> 
	</ul> 
	</div>
	
	<div id="revMan_submenu" name="revMan_submenu" class="navigation">
	<ul> 
	<li id="revPropDem" name="revPropDem" ><a href="#" onclick="showSubMenu(this,'revMan_submenu','revPropDem')"><span>Property type wise demand</span></a></li> 
	<li id="revYrWise" name="revYrWise" ><a href="#" onclick="showSubMenu(this,'revMan_submenu','revYrWise')"><span>Year wise Collection</span></a></li>
	<li id="revTopDef" name="revTopDef" ><a href="#" onclick="showSubMenu(this,'revMan_submenu','revTopDef')"><span>Top Defaulters</span></a></li> 
	<li id="revArrColl" name="revArrColl" ><a href="#" onclick="showSubMenu(this,'revMan_submenu','revArrColl')"><span>Arrears collection</span></a></li>
	<li id="revLicIss" name="revLicIss" ><a href="#" onclick="showSubMenu(this,'revMan_submenu','revLicIss')"><span>Licenses issued</span></a></li>
       <li id="revTopWardWise" name="revTopWardWise" ><a href="#" onclick="showSubMenu(this,'revMan_submenu','revTopWardWise')"><span>Top Ward wise Demand/Collection</span></a></li>
	</ul> 
	</div>
	
	<div id="pgr_submenu" name="pgr_submenu" class="navigation">
	<ul> 
	<li id="pgrGrvReg" name="pgrGrvReg" ><a href="#" onclick="showSubMenu(this,'pgr_submenu','pgrGrvReg')"><span>Redressal Efficiency</span></a></li> 
	<li id="pgrGrvRed" name="pgrGrvRed" ><a href="#" onclick="showSubMenu(this,'pgr_submenu','pgrGrvRed')"><span>Grievances Registered</span></a></li> 	
       <li id="bndReport" name="bndReport" ><a href="#" onclick="showSubMenu(this,'pgr_submenu','bndReport')"><span>Number of births/deaths</span></a></li>
	</ul> 
	</div>
	
	
<div id="border"></div>

	
	<div id="maincontent">
	<!-- remove standalone=true from the src url -->
	 <IFRAME id="reportFrame"
	 src="" 
	 	width="100%" height="100%"
	        scrolling="yes" frameborder="0"
	        >
	  [Your user agent does not support frames or is currently configured
	  not to display frames.]
	  </IFRAME>

	
	</div>	
				
	</body>
</html>
