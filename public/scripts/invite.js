$(function() {
	$('#invite-form').ajaxForm({
		url: '/api' + location.pathname,
		type: 'post',
		dataType: 'json',
		timeout: 5000,
		beforeSubmit: function(arr, form, options) { 
			$('#invite-btn').prop('disabled', true)
		},
		success: function(response, textStatus, xhr, form) {
			new PNotify({
				title: 'invite successful',
				text: 'You will be redirected soon.',
				type: 'success'
			})
			document.location.href = $('#invite-next-url').val()
		},
		error: function(xhr, textStatus, errorThrown) {
			var response = { errors: [] }
			var message = ''
			if (xhr.readyState == 0) {
				message = 'Request timed out.'
			} else {
				try {
					response = JSON.parse(xhr.responseText)
				} catch (e) {
					message = 'Endpoint gave non-JSON response.'
				}
			}
			for (var i = 0; i < response.errors.length; i++) {
				var error = response.errors[i]
				message += error.message
				if (i != response.errors.length - 1) {
					message += '<br/>'
				}
			}
			new PNotify({
				title: 'Invite failed',
				text: message,
				type: 'error'
			})
		},
		complete: function() {
			$('#invite-btn').prop('disabled', false)
		}
	})
})