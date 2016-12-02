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
 	
})
