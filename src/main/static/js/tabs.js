$(document).ready(function() {
	$("ul#tabs.view  li.active").addClass("middleout");
	$("ul#tabs.inEdit  li.active").addClass("middleout");
	
	$("ul#tabs li").click(function(e) {
		var tabClass=".view";
		if($(this).parent().hasClass("inEdit")){
			tabClass=".inEdit";
		}
		if (!$(this).hasClass("active")) {
			var tabNum = $(this).index();
			var nthChild = tabNum + 1;
			$("ul#tabs"+tabClass+" li.active").removeClass("active");
			$("ul#tabs"+tabClass+"  li.middleout").removeClass("middleout");
			$(this).addClass("active");
			$(this).addClass("middleout");
			
			$("ul#tab"+tabClass+"  li.active").removeClass("active");
			$("ul#tab"+tabClass+"  li:nth-child(" + nthChild + ")").addClass("active");
		}
		$('.autosize').each(autoresize);
	});

	function autoresize() {
		var eid= $(this).attr("id");
		var sh = $(this)[0].scrollHeight;
		$(this).css("height","0px");     //Reset height, so that it not only grows but also shrinks
		$(this).css('height',sh+ 'px');    //Set new height
	}


	
});