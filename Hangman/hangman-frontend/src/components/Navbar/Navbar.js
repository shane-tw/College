import AuthModal from '@/modals/Auth/Auth.vue'

export default {
  name: 'Navbar',
  components: {
    AuthModal
  },
  data: function () {
    return {
      authMode: 'register'
    }
  },
  methods: {
    signOut: function () {
      $.ajax({
        type: 'DELETE',
        url: this.$root.getAPIEndpoint("session"),
        success: this.onSignOutSuccess,
        error: this.onSignOutFailure,
        xhrFields: {
          withCredentials: true
        }
      })
    },
    onSignOutSuccess: function () {
      this.$root.loggedInUser = null
    },
    onSignOutFailure: function () {
      this.$toasted.error('Failed to sign out', {duration: 3000})
    }
  }
}
