
function disableError(id) {
	$('#' + id).css({'border-color':''});
	$('#' + id + "_error").css({'display':'none'});
}

function showError(id, msg) {
	$('#' + id).css({'border-color':'red'});
	$('#' + id).focus();
	$('#' + id + "_error").html(msg);
	$('#' + id + "_error").css({'display':'block'});
}