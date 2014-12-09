var apiUrl = "./api/";

$(document).ready(function() {
	console.log("App ready");
	
	$("#btn-call-waiter").click(callWaiter);
	$("#btn-request-receipt").click(requestReceipt);
	$("#btn-info").click(info);
	
	var table = getUrlParam("table" || "neznana");
	$("#text-table-name").text(table);
});

function callWaiter(e) {
	$.ajax({
		type: "POST",
		url: apiUrl + "request",
		data: {
			type: "waiter",
			table: $("#text-table-name").text()
		},
		success: function(data, status, jqxhr) {
			alertMsg("Zahteva uspešna", data);
		},
		error: function(jqxhr, status, error) {
			alertMsg("Napaka pri klicanju natakarja", error);
		}
	});
}

function requestReceipt(e) {
	$.ajax({
		type: "POST",
		url: apiUrl + "request",
		data: {
			type: "receipt",
			table: $("#text-table-name").text()
		},
		success: function(data, status, jqxhr) {
			alertMsg("Zahteva uspešna", data);
		},
		error: function(jqxhr, status, error) {
			alertMsg("Napaka pri zahtevanju računa", error);
		}
	});
}

function info(e) {
	alertMsg("Info", "To je prototip aplikacije za naročanje hrane in pijače v barih in restavracijah preko skeniranja NFC nalepk z mobilnim telefonom.");
}