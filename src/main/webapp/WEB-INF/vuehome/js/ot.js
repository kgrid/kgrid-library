'use strict';
function retrieveObjectList(fillObjectList){
		$.ajax({
			type : "GET",
			url : "/ObjectTeller/knowledgeObject",
			xhrFields: {
			     withCredentials: true
			},
			success : function(response, tStatus, xhr) {
				console.log(response);
				fillObjectList(response);
			},
			error : function(response) {
				errorHandler(response);
			}
		});
}

function retrieveObject(uri, section, fillObjectContent){
	var endPoint = uri; 
	if(section!=""){
		endPoint = endPoint+ "/"+section;
	}
	console.log("Endpoint:"+endPoint);
	$.ajax({
		type : "GET",
		url : "/ObjectTeller/knowledgeObject/"+endPoint,
		xhrFields: {
		     withCredentials: true
		},
		success : function(response, tStatus, xhr) {
			fillObjectContent(response);
		},
		error : function(response) {
			errorHandler(response);
		}
	});
}



function loadFieldsConfig(fillFieldsConfig){
	$.getJSON("json/fields.json", fillFieldsConfig);
}
function overlayHeightResize(overlayID, window_height) {
	var overlayPane = $('#' + overlayID).find("> .ol_pane");
	var entryform = overlayPane.find(".entryform");
	var ol_pane_height = window_height;
	var calcHeight = (window_height - 157);
	entryform.css("height", calcHeight + "px");
	var ef_margin = (ol_pane_height - calcHeight) / 2;
	var addContent = entryform.find(".Add-content");
	addContent.css("height", (calcHeight - 70) + "px");
	$("ul#tab.inEdit li").css("height", (calcHeight - 120) + "px");
	$("ul#tab.inEdit li").css("min-height", (calcHeight - 120) + "px");
	if (calcHeight < 700) {
		$("[id$='EditWrapper']").css("bottom", "0px");
	}
	/*
	 * console.log("Overlay Height Resize - window height:
	 * "+window_height+"px"); console.log("Overlay Height Resize - overlay
	 * height: "+ol_pane_height+"px"); console.log("Overlay Height Resize -
	 * calculate height: "+calcHeight+"px"); console.log("Overlay Height Resize -
	 * ef Margin: "+ef_margin+"px");
	 */
	return ol_pane_height;
}
function overlaySlide(overlayID, open, mode) {
	var curMode = mode;
	document.body.classList.toggle('noscroll', open);
	var overlayPane = $('#' + overlayID).find("> .ol_pane");
	var window_width = $(window).width();
	var window_height = $(window).height();
	var overlayPane_width = overlayPane.width();
	var overlayPane_height = overlayHeightResize(overlayID, window_height);
	var overlayPane_left = window_width - overlayPane_width;
	/*
	 * if(overlayID=="login_overlay"){
	 * overlayPane_left=overlayPane_left+overlayPane_width/2; }
	 */
	if (overlayPane_left <= (window_width * 0.27)) {
		overlayPane_left = (window_width * 0.27);
	}
	if (overlayID == "addObject") {
		resetInputText();
		if (mode != "new") {
			$("#begin_page").hide();
			$("#entry_form1").show();
			if (open) {
				console.log("Init Obj:" + editObj);
				initInputTextFromObject(editObj,"overlay");
			}
		} else {
			curURI="";
			$("#begin_page").show();
			$("#end_page").hide();
			$("#entry_form1").hide();
			console.log("Overlay IN with URI:" + curURI);

		}
	}
	if (overlayID == "login_overlay") {
		//resetLoginForm();
	}
	if (overlayID == "libraryuser") {
		resetUserInfoText();
	}
	if (overlayID == "citation") {
		resetCitationText();
	}
	if (overlayID == "license") {
		resetLicenseText();
	}
	if (open) {
		$('#' + overlayID).css("display", "block");
		$('#' + overlayID).fadeIn('slow', function() {
			overlayPane.animate({
				'left' : overlayPane_left + "px"
			}, 1000);
		});
	} else {

		overlayPane.animate({
			'left' : '100%'
		}, 1000, function() {
			$('#' + overlayID).delay(500).fadeOut('fast');
		});

//		$('#' + overlayID).css("display", "none");
	}

}





function login() {
	
	var user = new Object();
	user.username = document.getElementById("username").value;
	user.passwd = document.getElementById("password").value;

	var text = JSON.stringify(user);
	console.log(text);
	
	var validForm=login_validator.form();
	if(validForm){
		console.log("validation result: " +validForm);
		$( "div.processing" ).fadeIn( 300 );
		$.ajax({
				beforeSend : function(xhrObj) {
					xhrObj.setRequestHeader("Content-Type",
							"application/json");
					xhrObj.setRequestHeader("Accept", "application/json");
				},
				type : 'POST',
				url : "/ObjectTeller/login",
				data : text,
				dataType : "json",

				success : function(response) {
				 if(response!='empty') {
						  var test = JSON.stringify(response);
					      var obj = JSON.parse(test);
					      location.reload();
				    }
				} ,
				
				error : function(response) {
					// TODO: Handle Error Message
					
					$( "div.processing" ).fadeOut( 200 );
					 $( "div.failure" ).fadeIn( 300 ).delay( 1500 ).fadeOut( 400 );
				}
			});
	}else{
		}
	
	}

function autoresize() {
	var eid= $(this).attr("id");
	var sh = $(this)[0].scrollHeight;
	$(this).css("height","0px");     //Reset height, so that it not only grows but also shrinks
	$(this).css('height',sh+ 'px');    //Set new height
}

function backToTop() {
    $("html, body").animate({
        scrollTop: 0
    }, 600);
    return false;
}

function setBannerbkSize(){
  		var vp_width=$(window).width();
  		if(vp_width>1584){
  			$("#bannerbk").css("width",vp_width+"px");
  		}
  	}
 
 	
$(document).ready(function() {
	/*    var link = document.querySelector('link[rel="import"]');
	    var content = link.import;
	    // Grab DOM from warning.html's document.
	    var el = content.querySelector('.ot-nav');
	    document.body.appendChild(el.cloneNode(true));
	*/
	    $('[data-toggle="tooltip"]').tooltip();
		$(window).resize(function(){setBannerbkSize()});
	 	setBannerbkSize();
	    $('.autosize').each(autoresize);
	    $('ul#tabs li:first').addClass('active'); 
	    $('ul#tab li:first').addClass('active'); 

		var padLeft = 280;
	    var navOffset = $(".header").offset().top;
	    $(".header").wrap('<div class="theadwrapper"></div>');
		$(".theadwrapper").height($(".header").outerHeight(false));

	    $(window).scroll(function () {
	    	var supportPageOffset = window.pageXOffset !== undefined;
	    	var isCSS1Compat = ((document.compatMode || "") === "CSS1Compat");
	 	    var x = supportPageOffset ? window.pageXOffset : isCSS1Compat ? document.documentElement.scrollLeft : document.body.scrollLeft;
	    	var y = supportPageOffset ? window.pageYOffset : isCSS1Compat ? document.documentElement.scrollTop : document.body.scrollTop;
	            var scrollPos = $(window).scrollTop();
	        if(scrollPos>60){
	        	$(".ot-backtotop").fadeIn('slow');
	        	$("#backtotop").fadeIn('slow');
	        }else{
	        	$(".ot-backtotop").fadeOut('slow');
	        	$("#backtotop").fadeOut('slow');
	        }
	       if (scrollPos >= navOffset) {
	            $(".header").addClass("fixed");
	        } else {
	            $(".header").removeClass("fixed");
	        }
	       $(".fixed").css("left",-x+"px");
	    });
	    
	    $('.login-link').click(function() {
   			overlaySlide("login_overlay",true, "view");
  			return false;
   		});

})