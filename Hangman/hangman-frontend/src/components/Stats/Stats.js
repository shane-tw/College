export default {
  name: 'Stats',
  data: function () {
    return {
      games: []
    }
  },
  mounted: function () {
    if (this.$root.loggedInUser) {
      this.updateStats()
    }
  },
  methods: {
    onGetGamesSuccess: function ({games}) {
      this.games = games
    },
    onGetGamesFailure: function (res) {
      this.$toasted.error('Failed to retrieve games', {duration: 3000})
    },
    updateStats: function () {
      $.ajax({
        type: 'GET',
        url: this.$root.getAPIEndpoint('games'),
        success: this.onGetGamesSuccess,
        error: this.onGetGamesFailure,
        xhrFields: {
          withCredentials: true
        }
      })
    }
  },
  computed: {
    gamesWon: function () {
      return this.games.filter(function (game) {
        return game.status === 'won'
      }).length
    },
    gamesLost: function () {
      return this.games.filter(function (game) {
        return game.status === 'lost'
      }).length
    }
  },
  watch: {
    '$root.loggedInUser': function (loggedInUser) {
      if (!loggedInUser) return
      this.updateStats()
    }
  }
}
