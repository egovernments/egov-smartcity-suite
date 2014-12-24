<%@page import="org.egov.infstr.utils.AppConfigTagUtil"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<title><s:text name="workprogress.report.title" /></title>
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false">
</script>
<style type="text/css">
  html { height: 100% }
  body { height: 100%; margin: 0; padding: 0 }
  #map_canvas { height: 100%;width:100% }
</style>
 
<script type="text/javascript">

      <%
   				String url = request.getRequestURL().toString();
				String uri = request.getRequestURI().toString(); 
				String serverPath = url;
				if(url != null && url.indexOf(uri) != -1){
					serverPath = url.substring(0, url.indexOf(uri));
				}
     %>

  var zoneKmlFileURL='<%=serverPath%>'+'/egi/images/nmc_zone.kml';
  var map;
  var zones =new Object();
  function initialize() {
  var latlng = new google.maps.LatLng('<%= AppConfigTagUtil.getAppConfigValue("CITY_LATITUDE","egi",this.getServletContext()) %>'
, '<%= AppConfigTagUtil.getAppConfigValue("CITY_LONGITUDE","egi",this.getServletContext()) %>');
    var myOptions = {
      zoom: 8,
      center: latlng,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    map = new google.maps.Map(document.getElementById("map_canvas"),
        myOptions);
	var zoneLayer = new google.maps.KmlLayer(zoneKmlFileURL);
	zoneLayer.setMap(map);

		<s:iterator  value="gisReportBeans" status="row_status">
			zones['<s:property value="boundary.name"/>']='<s:property value="boundary.id"/>';
			var totalbudget=roundTo(eval('<s:property value="budgetConsumed"/>')+eval('<s:property value="budgetAvailable"/>'));
			var consumemedPerc=roundTo((eval('<s:property value="budgetConsumed"/>') * 100)/totalbudget);
			var availablePerc=roundTo((eval('<s:property value="budgetAvailable"/>') * 100)/totalbudget);
			var chartImage=new google.maps.MarkerImage('http://chart.apis.google.com/chart?cht=p&chd=t:'+availablePerc+','+consumemedPerc+'&chs=100x50&chf=bg,s,ffffff00&chco=00FF00|0000FF&chdls=FF0000FF&chdl='+availablePerc+'|'+consumemedPerc);
			var marker = new google.maps.Marker({
			    position: new google.maps.LatLng(eval('<s:property value="boundary.lat"/>'),eval('<s:property value="boundary.lng"/>')),
			    title:'<s:property value="boundary.name"/>',
			   	icon:chartImage
			});
			marker.setMap(map);
			google.maps.event.addListener(marker, 'click', showEstimates);
		</s:iterator>

}
var infowindow = new google.maps.InfoWindow();

function showEstimates(event){
	var title=this.getTitle();
	var boundaryId=zones[this.getTitle()];
	var html="";
 	var myEstimateListSuccessHandler = function(req,res) {
 				var totalConsumedBudget=0.0;
 				var totalEstimates=0;
                html="<table  width='100%' cellpadding='0' cellspacing='0'><tr><th colspan='4'><table><tr><th>"+title+"</th></tr></table></th></tr>" ;
                html=html+"<tr><td colspan='4'><table border='1' cellpadding='0' cellspacing='0'><tr><th>SlNo.</th><th>Department</th><th>No Of Estimates</th><th>Consumed Amt.</th></tr>"
                var allresults=res.results;
                if(allresults.length==0){
                	html=html+"<tr><td colspan='4'><s:text name='workprogress.report.consumed.budget.null' /></td></tr>";
                }
                for(var i=0;i<allresults.length;i++){
                	totalConsumedBudget+=eval(allresults[i].Amount);
                	totalEstimates+=eval(allresults[i].NoOfEstimates);
                	html=html+"<tr><td align='right'>"+(i+1)+"</td><td align='left'>"+allresults[i].Department+"</td><td align='right'>"+allresults[i].NoOfEstimates+"</td><td align='right'>"+roundTo(eval(allresults[i].Amount))+"</td></tr>";
              	}
				if(allresults.length!=0){
					html=html+"<tr><td colspan='2' align='right'><b>Total</b></td><td align='right'>"+totalEstimates+"</td><td align='right'>"+roundTo(totalConsumedBudget)+"</td></tr>";
				}
				html=html+"</table></td></tr></table>"
				infowindow.setContent(html);
  				infowindow.setPosition(event.latLng);
  				infowindow.open(map);
            };
            
	 var myEstimateListFailureHandler = function() {
	         alert("Sorry,unable to process...");
			 return;
	        };
	 
	 makeJSONCall(["Department","NoOfEstimates","Amount"],'${pageContext.request.contextPath}/report/ajaxGisReport!getDeptWiseEstimateCounts.action',{boundaryId:boundaryId},myEstimateListSuccessHandler,myEstimateListFailureHandler) ;

}
</script>
<body onload="initialize()">
    <div id="legend" align="right">
  		<img src='http://chart.apis.google.com/chart?cht=p&chd=t:10,3&chs=100x50&chf=bg,s,ffffff00&chco=00FF00|0000FF&chdls=FF0000FF&chdl=Available|Consumed' />
	</div>
	<div id="map_canvas"></div>
</body>
</html>

