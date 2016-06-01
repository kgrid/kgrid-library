/*jslint browser: true*/
/*global $, jQuery, alert*/
jQuery(document).ready(function () {

    var navOffset = jQuery(".header").offset().top;
    jQuery(".header").wrap('<div class="theadwrapper"></div>');
	jQuery(".theadwrapper").height(jQuery(".header").outerHeight());

    jQuery(window).scroll(function () {
        var scrollPos = jQuery(window).scrollTop();
        if(scrollPos>60){
/*        	$("#backtotop").css({
        		"display":"block"
        	});*/
        	$("#backtotop").fadeIn('slow');
        }else{
/*        	$("#backtotop").css({
        		"display":"none"
        	});*/
        	$("#backtotop").fadeOut('slow');
      	
        }
       if (scrollPos >= navOffset) {
            jQuery(".header").addClass("fixed");
        } else {
            jQuery(".header").removeClass("fixed");
        }
    });
    
  	function setIconPos(){
			var vp_width=$(window).width();
			var right_value_offset=(vp_width-1024)/2;
			if(right_value_offset<=0){
				right_value=15;
			}else{
				right_value=15+right_value_offset;
			}
			var css_right=right_value.toString()+"px";
			$("#backtotop").css("right",css_right);
			$("#infoicon").css("right",css_right);
			$("#addObjbutton").css("right",css_right);
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