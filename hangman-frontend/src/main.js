// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from '@/components/App/App.vue'
import router from './router'

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>',
  methods: {
    getAPIEndpoint: function (path) {
      if (path.startsWith('/')) path = path.substring(1)
      return location.protocol + '//' + location.hostname + ':4040/api/v1/' + path
    },
    getPageTitle: function (title) {
      return 'Game Centre - ' + title
    }
  },
  data: function () {
    return {
      loggedInUser: null
    }
  }
})
