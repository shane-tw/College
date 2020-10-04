function updatePicture() {
	var patient_id = $('#patients-details-container').data('patient-id')
	if (patient_id == null) {
		return
	}
	$.getJSON('/api/patients/' + patient_id, function (data) {
		var patient = data.data
		$('#remote-camera-img').prop('src', patient.remote_camera.last_picture + '#' + new Date().getTime())
	})
}

updatePicture()
setInterval(updatePicture, 30 * 1000)