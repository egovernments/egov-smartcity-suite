$('.navbar-nav li a').tooltip();
$('.navbar-brand').tooltip();
$(".navbar-nav li").on("click", function() {
    $(".navbar-nav li").removeClass("active");
    $(this).addClass("active");
  });

function toggleBtnHandler(currentBtn, lastBtn) {
	if(lastBtn.attr('id') === currentBtn.attr('id')) {
		return;
	}
	
	if(lastBtn != null) {
		lastBtn.removeClass("btn-primary");
		lastBtn.addClass("btn-default");
	}
	if(currentBtn != null) {
		currentBtn.removeClass("btn-default");
		currentBtn.addClass("btn-primary");
		var fn = window[currentBtn.data('fn')];
		if(typeof fn === "function") {	
			setTimeout(fn,10);
		}
	}
}



var mapStyle = [{
    "featureType": "water",
    "elementType": "geometry",
    "stylers": [{
        "color": "#000000"
    }, {
        "lightness": 17
    }]
}, {
    "featureType": "landscape",
    "elementType": "geometry",
    "stylers": [{
        "color": "#000000"
    }, {
        "lightness": 20
    }]
}, {
    "featureType": "road.highway",
    "elementType": "geometry.fill",
    "stylers": [{
        "color": "#000000"
    }, {
        "lightness": 17
    }]
}, {
    "featureType": "road.highway",
    "elementType": "geometry.stroke",
    "stylers": [{
        "color": "#000000"
    }, {
        "lightness": 29
    }, {
        "weight": 0.2
    }]
}, {
    "featureType": "road.arterial",
    "elementType": "geometry",
    "stylers": [{
        "color": "#000000"
    }, {
        "lightness": 18
    }]
}, {
    "featureType": "road.local",
    "elementType": "geometry",
    "stylers": [{
        "color": "#000000"
    }, {
        "lightness": 16
    }]
}, {
    "featureType": "poi",
    "elementType": "geometry",
    "stylers": [{
        "color": "#000000"
    }, {
        "lightness": 21
    }]
}, {
    "elementType": "labels.text.stroke",
    "stylers": [{
        "visibility": "on"
    }, {
        "color": "#000000"
    }, {
        "lightness": 16
    }]
}, {
    "elementType": "labels.text.fill",
    "stylers": [{
        "saturation": 36
    }, {
        "color": "#000000"
    }, {
        "lightness": 40
    }]
}, {
    "elementType": "labels.icon",
    "stylers": [{
        "visibility": "off"
    }]
}, {
    "featureType": "transit",
    "elementType": "geometry",
    "stylers": [{
        "color": "#000000"
    }, {
        "lightness": 19
    }]
}, {
    "featureType": "administrative",
    "elementType": "geometry.fill",
    "stylers": [{
        "color": "#000000"
    }, {
        "lightness": 20
    }]
}, {
    "featureType": "administrative",
    "elementType": "geometry.stroke",
    "stylers": [{
        "color": "#000000"
    }, {
        "lightness": 17
    }, {
        "weight": 1.2
    }]
}];

var markers = [];
var compNos = [];
var rankColor = ["#5cb85c","#5cb85c","#5cb85c","#f0ad4e","#f0ad4e","#f0ad4e","#f0ad4e","#f0ad4e","#f0ad4e","#f0ad4e","#f0ad4e","#f0ad4e","#d9534f","#d9534f","#d9534f"];

var stopAnimation = function(marker) {
	setTimeout(function () {
		marker.setAnimation(null);
	}, 3000);
};


function setTitle(title) {
	$(".title").text(title);
}

$('.navbar a').click(function(){ 
    var $target = $($(this).data('target')); 
    if(!$target.hasClass('in'))
    	$('.container-fluid .in').removeClass('in').height(0);
});