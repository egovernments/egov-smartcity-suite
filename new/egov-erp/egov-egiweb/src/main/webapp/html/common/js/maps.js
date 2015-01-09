    var tilesHome="http://www.egovernments.org/dashboard/Tiles/"
    var tilesBase=tilesHome
    var centreLat=21.94304553343818;
    var centreLon=78.31960667738718;
    var initialZoomLevel=4;
    var n_buttonText="Map"; //Text that shows up on the button for the custom layer (n=normal, s=sat, h=hybrid)
    var s_buttonText="Satellite";
    var h_buttonText="Hybrid";
    var mapBounds=new GLatLngBounds(new GLatLng(11.58305516523285,74.05093342527434),new GLatLng(18.47587617165226,78.58827992950003));
    var map; //the GMap2 itself
    var manager;
    var opacity=0.5;
    

    function customGetTileURL(a,b) {
      //converts tile x,y into keyhole string
      if (b>10) { return tilesBase+"blank-tile.png"; }

      var c=Math.pow(2,b);
      var x=360/c*a.x-180;
      var y=180-360/c*a.y;
      var x2=x+360/c;
      var y2=y-360/c;
      var lon=x; //Math.toRadians(x); //would be lon=x+lon0, but lon0=0 degrees
      var lat=(2.0*Math.atan(Math.exp(y/180*Math.PI))-Math.PI/2.0)*180/Math.PI; //in degrees
      var lon2=x2;
      var lat2=(2.0*Math.atan(Math.exp(y2/180*Math.PI))-Math.PI/2.0)*180/Math.PI; //in degrees
      var tileBounds=new GLatLngBounds(new GLatLng(lat2,lon),new GLatLng(lat,lon2));

      //if (!tileBounds.intersects(mapBounds)) { return tilesBase+"blank-tile.png"; };
        var d=a.x;
        var e=a.y;
        var f="t";
        for(var g=0;g<b;g++){
            c=c/2;
            if(e<c){
                if(d<c){f+="q"}
                else{f+="r";d-=c}
            }
            else{
                if(d<c){f+="t";e-=c}
                else{f+="s";d-=c;e-=c}
            }
        }
      
        return tilesBase+f+".png"
    }

    function changeOpacity(op) {
	//this works as long as there are at least two map types
        var current=map.getCurrentMapType();
        if (current==map.getMapTypes()[0])
        	map.setMapType(map.getMapTypes()[1]);
	else
		map.setMapType(map.getMapTypes()[0]);
        opacity=op;
        map.setMapType(current); //was map.getMapTypes()[1]
    }


    function getWindowHeight() {
        if (window.self&&self.innerHeight) {
            return self.innerHeight;
        }
        if (document.documentElement&&document.documentElement.clientHeight) {
            return document.documentElement.clientHeight;
        }
        return 0;
    }

    function resizeMapDiv() {
        //Resize the height of the div containing the map.
        //Do not call any map methods here as the resize is called before the map is created.
    	var d=document.getElementById("map");
        var offsetTop=0;
        for (var elem=d; elem!=null; elem=elem.offsetParent) {
            offsetTop+=elem.offsetTop;
        }
        var height=getWindowHeight()-offsetTop-16;
        if (height>=0) {
            d.style.height=height+"px";
        }
    }
var n_customMap;

    function load() {
      if (GBrowserIsCompatible()) {
        resizeMapDiv();
        
        var copyrightCollection = new GCopyrightCollection("");
        copyrightCollection.addCopyright(
          new GCopyright("egov",
          new GLatLngBounds(new GLatLng(-90,-180), new GLatLng(90,180)),
          0,
          "")
        );

        //create a custom G_NORMAL_MAP layer
        var n_tileLayers = [G_NORMAL_MAP.getTileLayers()[0], new GTileLayer(copyrightCollection , 0, 17)];
        n_tileLayers[1].getTileUrl = customGetTileURL;
        n_tileLayers[1].isPng = function() { return false; };
        n_tileLayers[1].getOpacity = function() { return opacity; };
        n_customMap = new GMapType(n_tileLayers, new GMercatorProjection(n_tileLayers[0].maxResolution()+1), n_buttonText,
            {maxResolution:10, minResolution:0, errorMessage:"Data not available"});

        //create a custom G_SATELLITE_MAP layer
        var s_tileLayers = [ G_SATELLITE_MAP.getTileLayers()[0], new GTileLayer(copyrightCollection , 0, 17)];
        s_tileLayers[1].getTileUrl = customGetTileURL;
        s_tileLayers[1].isPng = function() { return false; };
        s_tileLayers[1].getOpacity = function() { return opacity; };
        var s_customMap = new GMapType(s_tileLayers, new GMercatorProjection(s_tileLayers[0].maxResolution()+1), s_buttonText,
            {maxResolution:10, minResolution:0, errorMessage:"Data not available"});
            
        //create a custom G_HYBRID_MAP layer
        var h_tileLayers = [ G_HYBRID_MAP.getTileLayers()[0], new GTileLayer(copyrightCollection , 0, 17),G_HYBRID_MAP.getTileLayers()[1]];
        h_tileLayers[1].getTileUrl = customGetTileURL;
        h_tileLayers[1].isPng = function() { return false; };
        h_tileLayers[1].getOpacity = function() { return opacity; };
        var h_customMap = new GMapType(h_tileLayers, new GMercatorProjection(h_tileLayers[0].maxResolution()+1), h_buttonText,
            {maxResolution:10, minResolution:0, errorMessage:"Data not available"});

        //Now create the custom map. Would normally be G_NORMAL_MAP,G_SATELLITE_MAP,G_HYBRID_MAP
        map = new GMap2(document.getElementById("map"),{mapTypes:[n_customMap,s_customMap,h_customMap]});
        GEvent.addListener(map, "zoomend", function(o,n) {
          mapBounds=map.getBounds();
        });        
        map.addControl(new GLargeMapControl());
        map.addControl(new GMapTypeControl());
        map.enableContinuousZoom();
        map.enableScrollWheelZoom();
        map.setCenter(new GLatLng(centreLat, centreLon), initialZoomLevel, n_customMap);
		map.enableGoogleBar();
        manager=new MarkerManager(map);
      }
    }

function addMarkers(locs){
        
        var blueIcon = new GIcon(G_DEFAULT_ICON);
        blueIcon.image = locs[0].marker;
     	// Set up our GMarkerOptions object
		markerOptions = { icon:blueIcon };
		markers=[];
        for(i=0;i<locs.length;i++){
          markers.push(new GMarker(new GLatLng(locs[i].lat,locs[i].lng), markerOptions));
        }
        manager.addMarkers(markers,map.getZoom());
        manager.refresh();
        manager.show();
}
