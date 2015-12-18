function doAction () {		
	 jQuery.ajax({ 
		 type: "PUT",
		 dataType: "json",
		 url: getUrl + addParam,
		 async: asyncParam,
		 success: function(data){  
			 return data;
		 },
		 error: function (error) {
			return {successful: false};
		 }
	});
}

$(document).ready(function() {
	
	/* 3 index of aaSorting param is related to 'Start Date' column */
    $('#datatable').DataTable({
		"aaSorting": [[3,'desc']]
	});
	
	
	/* Reload button needs to be injected after the initialization of datatable element
	*  We can't add the button to the dom before this step because is currently displayed on the right of the search button
	*  Search button is a dom element created by DataTable
	*/
	jQuery("#datatable_filter").append(jQuery('<button type="button" class="btn btn-primary reload-button" onclick="selectMonitoring()">Reload</button>'));
	
	connectSubmitKeyHandler();
} );

function connectSubmitKeyHandler () {
	var fields = jQuery("#provisioning-container input");
	
	fields.keypress(function (e) {
		if (e.which == 13) {
			jQuery("#submitButton").click();
		}
	});
	
}

function selectMonitoring () {
	console.info("selectMonitoring");
	jQuery("#monitoring-container").css("display", "block");
	jQuery("#provisioning-container").css("display", "none");
	jQuery(".submit-button-container").css("display", "none");
	
	jQuery("#monitoringButton").addClass("active");
	jQuery("#provisioningButton").removeClass("active");
	
	getRequestList();
}

function selectProvisioning () {
	console.info("selectProvisioning");
	jQuery("#monitoring-container").css("display", "none");
	jQuery("#provisioning-container").css("display", "block");
	jQuery("#monitoringButton").removeClass("active");
	jQuery("#provisioningButton").addClass("active");
	jQuery(".submit-button-container").css("display", "block");
}


function getRequestList() {
	showLoadingMessage("#loadingDiv");
	jQuery.ajax({ 
		 type: "GET",
		 //dataType: "json",
		 url: "/api/submissions",
	     contentType: 'application/json',
		 success: function(data){
		 	 hideLoadingMessage("#loadingDiv");
		 	 populateTable(data);
		 	 
			 return data;
		 },
		 error: function (error) {
		 	hideLoadingMessage("#loadingDiv");
		 	alert("Error during the monitoring request");
			return {successful: false};
		 }
	});
}

function populateTable(data) {
	var dataTable = jQuery("#datatable").DataTable();
	dataTable.clear().draw();
	
	for (var i=0; i<data.length; i++) {
		var sub = data[i];
		
		dataTable.row.add([
			escapeHTML(sub.address),
			escapeHTML(sub.subject),
			escapeHTML(sub.status),
			dateConverter(sub.startDate)
		]);
	}
	dataTable.draw();
}

function dateConverter(UNIX_timestamp){
	  var date = new Date(UNIX_timestamp);
	  return date.toLocaleString();
}

function escapeHTML(str) {
	if (str.replace) {
		return str.replace(/</g, "&lt;").replace(/>/g, "&gt;");
	} else {
		return str;
	}
}

function showLoadingMessage(id) {
	var loading = jQuery(id);
	loading.removeClass("loading-hide");
	loading.addClass("loading-show");
}

function hideLoadingMessage(id) {
	var loading = jQuery(id);
	loading.removeClass("loading-show");
	loading.addClass("loading-hide");
}