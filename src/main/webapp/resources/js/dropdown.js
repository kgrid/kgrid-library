$(document).ready(function() {
	$('.login-link').click(function() {
		if ($('#signin-dropdown').is(":visible")) {
			/*            $('#signin-dropdown').slideUp(5000);*/
			$('#signin-dropdown').hide();
			$('#session').removeClass('active');
		} else {
			/*           $('#signin-dropdown').slideDown(5000);*/
			$('#signin-dropdown').show();
			$('#username').focus();
			$('#session').addClass('active');
		}
		return false;
	});
	
	$('.logout-link').click(function() {
		if ($('#logout-dropdown').is(":visible")) {
			/*            $('#signin-dropdown').slideUp(5000);*/
			$('#logout-dropdown').hide();
			$('#logoutsession').removeClass('active');
			$('#iconimg').removeClass('up');
			$('#iconimg').addClass('down');

		} else {
			/*           $('#signin-dropdown').slideDown(5000);*/
			$('#logout-dropdown').show();
			$('#logoutsession').addClass('active');
			$('#iconimg').addClass('up');
			$('#iconimg').removeClass('down');
		}
		return false;
	});
	
	$('#signin-dropdown').click(function(e) {
		e.stopPropagation();
	});

	$('#logout-dropdown').click(function(e) {
		e.stopPropagation();
	});
	
	$(document).click(function() {
		$('.dropdown').hide();
		$('#session').removeClass('active');
	});

});