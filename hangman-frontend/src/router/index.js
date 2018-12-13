import Vue from 'vue'
import Router from 'vue-router'
import Hangman from '@/components/Hangman/Hangman.vue'
import Stats from '@/components/Stats/Stats.vue'

Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/',
      name: 'Hangman',
      component: Hangman,
      meta: {
        title: 'Play Game'
      }
    },
    {
      path: '/stats',
      name: 'Stats',
      component: Stats,
      meta: {
        title: 'View Stats'
      }
    }
  ]
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title + ' - Game Centre'
  next()
})

export default router
