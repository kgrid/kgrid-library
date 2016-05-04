/*jslint browser: true*/
/*global $, jQuery, alert*/
jQuery(document).ready(function () {

    var navOffset = jQuery(".header").offset().top-60;
    jQuery(window).scroll(function () {
        var scrollPos = jQuery(window).scrollTop();
       if (scrollPos >= navOffset) {
            jQuery(".header").addClass("fixed");
        } else {
            jQuery(".header").removeClass("fixed");
        }
    });
    

});