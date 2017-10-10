$(function() {
	$('#login-form').ajaxForm({
		url: '/api/login',
		type: 'post',
		dataType: 'json',
		timeout: 5000,
		beforeSubmit: function(arr, form, options) { 
			$('#login-btn').prop('disabled', true)
		},
		success: function(response, textStatus, xhr, form) {
			new PNotify({
				title: 'Login successful',
				text: 'You will be redirected soon.',
				type: 'success'
			})
			document.location.href = $('#login-next-url').val()
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
				title: 'Login failed',
				text: message,
				type: 'error'
			})
		},
		complete: function() {
			$('#login-btn').prop('disabled', false)
		}
	})
})