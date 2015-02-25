function getUrlParam(sParam) {
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam) {
            return sParameterName[1];
        }
    }
}

function msg(title, desc, btnYes, btnNo, callback, scope) {
	$("#msg .msg-title")[title ? "show" : "hide"]();
	$("#msg .msg-title").text(title);
	$("#msg .msg-desc")[desc ? "show" : "hide"]();
	$("#msg .msg-desc").text(desc);
	$("#msg .btn-yes")[btnYes ? "show" : "hide"]();
	$("#msg .btn-yes").text(btnYes).on("click.sure", function() {
		if(typeof callback == "function") {
			callback.call(scope || this);
		}
		$(this).off("click.sure");
	});
	$("#msg .btn-no")[btnNo ? "show" : "hide"]();
	$("#msg .btn-no").text(btnNo)
	$.mobile.changePage("#msg");
}

function alertMsg(title, desc, callback, scope) {
	msg(title, desc, "OK", null, callback, scope);
}

function confirmMsg(title, desc, callback, scope) {
	msg(title, desc, "Da", "Ne", callback, scope);
}