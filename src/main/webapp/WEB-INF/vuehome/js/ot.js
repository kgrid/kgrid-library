'use strict';
function retrieveObjectList(fillObjectList){
		$.ajax({
			type : "GET",
			url : "/ObjectTeller/knowledgeObject",
			xhrFields: {
			     withCredentials: true
			},
			success : function(response, tStatus, xhr) {
				console.log(xhr);
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
			//errorHandler(response);
		}
	});
}



function loadFieldsConfig(fillFieldsConfig){
	$.getJSON("json/fields.json", fillFieldsConfig);
}
function overlayHeightResize() {
	var ol_pane_height = $(window).height();
	var boardHeight = (ol_pane_height - 180);
	var formHeight = (boardHeight - 80);
	var liHeight = (formHeight - 10);
	$('.ol_pane').css("height", ol_pane_height + "px");
	$('.overlay-board').css("height", boardHeight + "px");
	$('.entryform').css("height", formHeight + "px");
	$('ul#edittab>li').css("height", liHeight + "px");
	console.log("Heights:Pane="+ol_pane_height+" Board="+boardHeight+" Form="+formHeight+"Li="+liHeight);
	return ol_pane_height;
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
	var eid= $(this).text();
	var sh = $(this)[0].scrollHeight;
	$(this).css("height","0px");     //Reset height, so that it not only grows but also shrinks
	$(this).css('height',sh+ 'px');    //Set new height
	console.log(this+ "Text:["+ eid+"] "+sh);
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
 
function editTabNav(){
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
		$('.autosize').each(autoresize);
	});
}

function setstartdate(){
	 var startdate=$("#startdatepicker").val();
	 var sstamp=new Date(startdate).getTime();
	 eventBus.$emit("startdate",sstamp);
	console.log("Start date:"+ sstamp);
 }

function setenddate(){
				var enddate=$("#enddatepicker").val();
				 var estamp=new Date(enddate).getTime();
				eventBus.$emit("enddate",estamp);
				console.log("End date:"+ estamp);
			 }
 
function otScroll() {
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
}
$(document).ready(function() {
	/*    var link = document.querySelector('link[rel="import"]');
	    var content = link.import;
	    // Grab DOM from warning.html's document.
	    var el = content.querySelector('.ot-nav');
	    document.body.appendChild(el.cloneNode(true));
	*/
	$("#startdatepicker").datepicker();
	$("#enddatepicker").datepicker();
	$("#startdatepicker").val("03/01/16");
	$("#enddatepicker").val(new Date().format("shortDate"));
	overlayHeightResize();
	$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
		  var target = $(e.target).attr("href") // activated tab
		  $(".autosize").each(autoresize);
	});	
	$('[data-toggle="tooltip"]').tooltip();
	$('#userDropdown').on('show.bs.dropdown', function(e){
		  var target = $(e.target).attr("id"); // activated tab
		  $("img#dropdowniconimg").removeClass('down');
		  $("img#dropdowniconimg").addClass('up');	
	});
	$('#userDropdown').on('hide.bs.dropdown', function(e){
		  var target = $(e.target).attr("id"); // activated tab
		  $("img#dropdowniconimg").removeClass('up');
		  $("img#dropdowniconimg").addClass('down');		  
	});
	$(window).resize(function(){
			overlayHeightResize();
			setBannerbkSize()});
	 	setBannerbkSize();
/*	    $('.autosize').each(autoresize);*/
	    $('ul#tabs li:first').addClass('active'); 
	    $('ul#tab li:first').addClass('active'); 

		$("ul#tabs.inEdit  li.active").addClass("middleout");
		
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
/*			$('.autosize').each(autoresize);*/
		});
	    
})