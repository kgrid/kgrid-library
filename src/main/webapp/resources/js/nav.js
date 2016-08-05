$(document).ready(function() {
	$("#nav ul li").click(function(e) {
		if (!$(this).hasClass("active")) {
			var tabNum = $(this).index();
			var nthChild = tabNum + 1;
			$("#nav ul li a.middleout").removeClass("middleout");
			$("#nav ul li.active").removeClass("active");
			$(this).addClass("active");
		}
		$(this).children().addClass("middleout");

	});
	$("#nav ul li.active a").addClass("middleout");
/*	$("#nav ul li").hover(
			
		function() {

			if (!$(this).hasClass("active")) {
				$("#nav ul li.active a").addClass("greenBar");
				$("#nav ul li.active a").removeClass("whiteBar");
				$(this).children().addClass("whiteBar");

			} else {
				$("#nav ul li.active a").removeClass("greenBar");
				$("#nav ul li.active a").addClass("whiteBar");
			}
		},
		function() {
			$("#nav ul li.active a").removeClass("greenBar");
			$("#nav ul li.active a").addClass("whiteBar");
			if (!$(this).hasClass("active")) {			
			$(this).children().removeClass("whiteBar");
		}}
	
	);*/
 	
})
