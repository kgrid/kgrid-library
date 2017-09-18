export function retrieveObjectList(baseurl, fillObjectList){
		$.ajax({
			type : "GET",
			url : baseurl+"knowledgeObject",
			xhrFields: {
			     withCredentials: true
			},
			success : function(response, tStatus, xhr) {
					fillObjectList(response);
			},
			error : function(response) {
				 //errorHandler(response);
			}
		});
}

export function retrieveObject(baseurl, uri, section, fillObjectContent,errorHandler){
	var endPoint = uri;
	if(section!=""){
		endPoint = endPoint+ "/"+section;
	}
	console.log("Endpoint:"+endPoint);
	$.ajax({
		type : "GET",
		url : baseurl+"knowledgeObject/"+endPoint,
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

export function overlayHeightResize() {
	var ol_pane_height = $(window).height();
	var boardHeight = (ol_pane_height - 200);
	var formHeight = (boardHeight - 150);
	var liHeight = (formHeight - 4);
	var listHeight =(formHeight-100);
	$('.ol_pane').css("height", ol_pane_height + "px");
	$('.overlay-board').css("height", boardHeight + "px");
	$('.entryform').css("height", formHeight + "px");
	$('ul#edittab>li').css("height", liHeight + "px");
	$('#uList').css("max-height", listHeight + "px");
	// console.log("Heights:Pane="+ol_pane_height+" Board="+boardHeight+" Form="+formHeight+"Li="+liHeight);
	return ol_pane_height;
}

export function editTabNav(){
	$("ul#edittabs li").click(function(e) {
		var tabClass=".view";
		if($(this).parent().hasClass("inEdit")){
			tabClass=".inEdit";
		}
		if (!$(this).hasClass("active")) {
			var tabNum = $(this).index();
			var nthChild = tabNum + 1;
			$("ul#edittabs"+tabClass+" li.active").removeClass("active");
			$("ul#edittabs"+tabClass+"  li.middleout").removeClass("middleout");
			$(this).addClass("active");
			$(this).addClass("middleout");
			$("ul#edittab"+tabClass+"  li.active").removeClass("active");
			$("ul#edittab"+tabClass+"  li:nth-child(" + nthChild + ")").addClass("active");
		}
	});
}


export function retrieveUserList(fillUserList, errorHandler){
	$.ajax({
		type : "GET",
		url : "user",
		xhrFields: {
		     withCredentials: true
		},
		success : function(response, tStatus, xhr) {
			console.log(xhr);
			fillUserList(response);
		},
		error : function(response) {
			errorHandler(response);
		}
	});
}

export function getCurrentUser(baseurl, getUser, errorhandler) {
	console.log("Baseurl:"+baseurl);
	$.ajax({
		type : "GET",
		url : baseurl+"user/me",
		xhrFields: {
	 		withCredentials: true
		},
		success : function(response, tStatus, xhr) {
				getUser(response);
		},
		error : function(response, tStatus, xhr) {
			errorhandler(response);
		}
	});
}

export function checkEnv(baseurl, getEnv, errorhandler) {
	console.log("Baseurl:"+baseurl);
	$.ajax({
		type : "GET",
		headers: {
    	Accept: "application/json",
  	},
		url : baseurl+"info",
		xhrFields: {
	 		withCredentials: true
		},
		success : function(response, tStatus, xhr) {
				getEnv(response);
		},
		error : function(response, tStatus, xhr) {
			errorhandler(response);
		}
	});
}

export function otScroll() {
    var navOffset = $(".header").offset().top;
    $(window).scroll(function () {
    	var supportPageOffset = window.pageXOffset !== undefined;
    	var isCSS1Compat = ((document.compatMode || "") === "CSS1Compat");
 	    var x = supportPageOffset ? window.pageXOffset : isCSS1Compat ? document.documentElement.scrollLeft : document.body.scrollLeft;
    	var y = supportPageOffset ? window.pageYOffset : isCSS1Compat ? document.documentElement.scrollTop : document.body.scrollTop;
      var scrollPos = $(window).scrollTop();
			if(scrollPos>60){
        	$(".header").css('z-index', 350);
      }else{
        	$(".header").css('z-index', 150);
    	}
      if (scrollPos >= navOffset) {
            $(".header").addClass("fixed");
        } else {
            $(".header").removeClass("fixed");
        }
       $(".fixed").css("left",-x+"px");
    });
}

$(document).ready(function() {
	overlayHeightResize();
	$(window).resize(function(){
			overlayHeightResize();
		}
	);
})
