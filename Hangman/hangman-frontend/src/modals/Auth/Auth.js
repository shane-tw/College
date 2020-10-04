export default {
  name: 'AuthModal',
  props: ['mode'],
  computed: {
    title: function () {
      if (this.mode === 'login') {
        return 'Log in'
      }
      return 'Register'
    }
  },
  methods: {
    onModalSubmit: function (e) {
      $('#authModal form input[type="submit"]').click()
      e.preventDefault()
    },
    onFormSubmit: function () {
      if (this.authenticating) return
      this.authenticating = true
      let authURL = this.$root.getAPIEndpoint('session')
      if (this.mode === 'register') {
        authURL = this.$root.getAPIEndpoint('users')
      }

      $.ajax({
        type: 'POST',
        url: authURL,
        data: JSON.stringify(this.form),
        contentType: 'application/json',
        success: this.onAuthSuccess,
        error: this.onAuthFailure,
        complete: this.onAuthComplete,
        xhrFields: {
          withCredentials: true
        }
      })
    },
    onAuthSuccess: function ({user}) {
      this.$root.loggedInUser = user
      this.$refs.authModal.hide()
      this.$toasted.success('Logged in successfully', {duration: 3000})
    },
    onAuthFailure: function (e) {
      if (e.responseJSON && e.responseJSON.errors) {
        this.$toasted.error(e.responseJSON.errors[0].reason, {duration: 3000})
      } else {
        this.$toasted.error('Failed to authenticate', {duration: 3000})
      }
    },
    onAuthComplete: function () {
      this.authenticating = false
    },
    onModalHidden: function () {
      const self = this
      Object.keys(this.form).forEach(function (key) {
        self.form[key] = ''
      })
      this.$nextTick(function () {
        $('#authModal form')[0].reset()
      })
      $('div#app').focus()
    },
    focusFirstInput: function () {
      $('#authModal form').find('input').first().focus()
    }
  },
  mounted: function () {
    $(this.$el).on('keypress.kbKey', function (e) {
      e.stopPropagation() // We don't want pressing keys inside modal to cause hangman keypresses
    })
  },
  beforeDestroy: function () {
    $(this.$el).off('keypress.kbKey')
  },
  data: function () {
    return {
      form: {
        firstName: '',
        lastName: '',
        email: '',
        password: ''
      },
      authenticating: false
    }
  }
}
