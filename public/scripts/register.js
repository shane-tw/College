$(function() {
	$('#register-form').ajaxForm({
		url: '/api/register',
		type: 'post',
		dataType: 'json',
		timeout: 5000,
		beforeSubmit: function(arr, form, options) { 
			$('#register-btn').prop('disabled', true)
		},
		success: function(response, textStatus, xhr, form) {
			new PNotify({
				title: 'Registration successful',
				text: 'You will be redirected soon.',
				type: 'success'
			})
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
				title: 'Registration failed',
				text: message,
				type: 'error'
			})
		},
		complete: function() {
			$('#register-btn').prop('disabled', false)
		}
	})
})