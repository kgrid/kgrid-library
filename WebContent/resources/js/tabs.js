$(document).ready(function() {
	$("ul#tabs li").click(function(e) {
		if (!$(this).hasClass("active")) {
			var tabNum = $(this).index();
			var nthChild = tabNum + 1;
			$("ul#tabs li.whiteBar").removeClass("whiteBar");
			$("ul#tabs li.active").removeClass("active");
			$(this).addClass("active");
			$("ul#tab li.active").removeClass("active");
			$("ul#tab li:nth-child(" + nthChild + ")").addClass("active");
		}
	});
	$("ul#tabs li.active").addClass("greenBar");
	$("ul#tabs li").hover(
		function() {
			console.log("Hovering list");
			if (!$(this).hasClass("active")) {
				$("ul#tabs li.active").addClass("whiteBar");
				$("ul#tabs li.active").removeClass("greenBar");
			} else {
				$("ul#tabs li.active").removeClass("whiteBar");
				$("ul#tabs li.active").addClass("greenBar");
			}
		},
		function() {
			$("ul#tabs li.active").removeClass("whiteBar");
			$("ul#tabs li.active").addClass("greenBar");
		}
	
	);
});