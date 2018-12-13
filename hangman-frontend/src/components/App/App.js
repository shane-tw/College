import Vue from 'vue'
import BootstrapVue from 'bootstrap-vue'
import { library } from '@fortawesome/fontawesome-svg-core'
import { faUser, faAt, faLock } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import Toasted from 'vue-toasted';
import Navbar from '@/components/Navbar/Navbar.vue'
window.$ = require('jquery')

library.add(faUser, faAt, faLock)

Vue.use(BootstrapVue)
Vue.use(Toasted)
Vue.component('font-awesome-icon', FontAwesomeIcon)

export default {
  name: 'App',
  components: {
    Navbar
  },
  data: function () {
    return {
      doneLoginCheck: false
    }
  },
  mounted: function () {
    $.ajax({
      type: 'GET',
      url: this.$root.getAPIEndpoint('users/me'),
      success: this.onAuthSuccess,
      complete: this.onAuthComplete,
      xhrFields: {
        withCredentials: true
      }
    })
  },
  methods: {
    onAuthSuccess: function ({user}) {
      this.$root.loggedInUser = user
    },
    onAuthComplete: function () {
      this.doneLoginCheck = true
      this.$nextTick(function () {
        $('div#app').focus()
      })
    }
  }
}
