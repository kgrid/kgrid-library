/*jslint browser: true*/
/*global $, jQuery, alert*/
jQuery(document).ready(function () {

    var navOffset = jQuery(".header").offset().top;
    jQuery(".header").wrap('<div class="theadwrapper"></div>');
	jQuery(".theadwrapper").height(jQuery(".header").outerHeight());

    jQuery(window).scroll(function () {
    	var supportPageOffset = window.pageXOffset !== undefined;
    	var isCSS1Compat = ((document.compatMode || "") === "CSS1Compat");
 	    var x = supportPageOffset ? window.pageXOffset : isCSS1Compat ? document.documentElement.scrollLeft : document.body.scrollLeft;
    	var y = supportPageOffset ? window.pageYOffset : isCSS1Compat ? document.documentElement.scrollTop : document.body.scrollTop;
            var scrollPos = jQuery(window).scrollTop();
        if(scrollPos>60){
        	$("#backtotop").fadeIn('slow');
        }else{
        	$("#backtotop").fadeOut('slow');
      	
        }
       if (scrollPos >= navOffset) {
            jQuery(".header").addClass("fixed");
        } else {
            jQuery(".header").removeClass("fixed");
        }
       jQuery(".fixed").css("left",-x+"px");
    });
    
    function getCSSright(parent){
		var vp_width=parent.width();
		var right_value_offset=(vp_width-1024)/2;
		if(right_value_offset<=0){
			right_value=15;
		}else{
			right_value=15+right_value_offset;
		}
		var css_right=right_value.toString()+"px";
		return css_right;
    }
    
  	function setIconPos(){
			var css_right_doc=getCSSright($(document));
			var css_right_window=getCSSright($(window));
			$("#backtotop").css("right",css_right_window);
			$("#infoicon").css("right",css_right_doc);
			$("#addObjbutton").css("right",css_right_doc);
			
			var vp_width=$(window).width();
			var left_value_offset=(vp_width-1024)/2;
			if(left_value_offset<=240){
				$("#bannerbk").css("left",240);
				$("#landing").css("left",240);
				$(".banner-content").css("left",240);
				
				$(".datagrid").css("left",240);
			}else{
				$("#bannerbk").css("left",left_value_offset);
				$("#landing").css("left",left_value_offset);
				$(".banner-content").css("left",left_value_offset);
				$(".datagrid").css("left",left_value_offset);
			}
			
			
		}
 	$(window).resize(function(){setIconPos()});
 	setIconPos();
  	$('#backtotop').click(function () {
       $("html, body").animate({
           scrollTop: 0
       }, 600);
       return false;
   });
});