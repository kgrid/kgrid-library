/*jslint browser: true*/
/*global $, $, alert*/
$(document).ready(function () {
	   
	   var roffset=[2,-40,60,-30,25,15];
	   var iDescription = ["ADD/REMOVE USER","LIBRARY SETTING","ADD/REMOVE USER","OBJECT SETTING","JAMES RAMPTON","ALLEN FLYNN"];
  
       $('#backtotop').click(function () {
           $("html, body").animate({
               scrollTop: 0
           }, 600);
           return false;
       });
       
       $(".iconBtn").hover(	   
    		   
    	function(){
    		var iconHovered=$(this);
    		var p=iconHovered.position();
    		var iconID = iconHovered.attr("id");
    		var right_offset=p.left+30;
    	   var idx=0;
     	   switch(iconID){
    	   case "userlink":
    		   idx=0;
    		   break;
    	   case "settinglink":
    		   idx=1;
    		   break;
    	   case "objuserlink":
    		   idx=2;
    		   break;
    	   case "objsettinglink":
    		   idx=3;
    		   break;
    	   case "JR":
    		   idx=4;
    		   break;
    	   case "AF":
    		   idx=5;
    		   break;
    	   }
     	   console.log("Hover in:"+iconHovered.attr("id")+"  Position:"+p.left);    	   
     	   $(".floatingInfo > span").text(iDescription[idx]);
    	   $(".floatingInfo").css("right", roffset[idx]+"px");
     	   $(".floatingInfo").css("display","inline-block");
			
    	
    	},
       function(){
    	   console.log("Hover out");
       	   $(".floatingInfo").css("display","none");
       }
       
    		   );
   });