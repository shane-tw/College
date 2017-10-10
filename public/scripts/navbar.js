$(function() {
	$('#logout-btn').click(function (e) {
		$('#logout-btn').prop('disabled', true)
		e.preventDefault()
		$.ajax({
			url: '/api/logout',
			type: 'get',
			dataType: 'json',
			timeout: 5000,
			success: function () {
				new PNotify({
					title: 'Logout successful',
					text: 'You will be redirected soon.',
					type: 'success'
				})
				document.location.href = '/'
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
					title: 'Logout failed',
					text: message,
					type: 'error'
				})
			},
			complete: function() {
				$('#logout-btn').prop('disabled', false)
			}
		});
	})
})