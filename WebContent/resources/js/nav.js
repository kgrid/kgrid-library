$(document).ready(function() {
	$("#nav ul li").click(function(e) {
		if (!$(this).hasClass("active")) {
			var tabNum = $(this).index();
			var nthChild = tabNum + 1;
			$("#nav ul li.greenBar").removeClass("greenBar");
			$("#nav ul li.active").removeClass("active");
			$(this).addClass("active");
		}
	});
	$("#nav ul li.active").addClass("whiteBar");
	$("#nav ul li").hover(
		function() {
			console.log("Hovering list");
			if (!$(this).hasClass("active")) {
				$("#nav ul li.active").addClass("greenBar");
				$("#nav ul li.active").removeClass("whiteBar");
			} else {
				$("#nav ul li.active").removeClass("greenBar");
				$("#nav ul li.active").addClass("whiteBar");
			}
		},
		function() {
			$("#nav ul li.active").removeClass("greenBar");
			$("#nav ul li.active").addClass("whiteBar");
		}
	
	);
 	
})
