$('[name="btn_view"]').click(function() {
	var card = $(this).closest('[name="card_patient"]')
	var patient_id = card.data('patient-id')
	document.location.href = '/patients/' + patient_id
})

$('[name="btn_delete"]').click(function() {
	alert('Not yet implemented.')
})