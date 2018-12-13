export default {
  name: 'Hangman',
  data: function () {
    return {
      keyboardRows: [
        ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'],
        ['J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q'],
        ['R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z']
      ],
      wrongCount: 0,
      currentGame: {
        status: 'none-played'
      },
      knownLetters: []
    }
  },
  methods: {
    getKey: function (charCode) { // Given letter charCode, get keyboard key elem
      if (!((charCode >= 65 && charCode <= 90) || (charCode >= 97 && charCode <= 122))) return
      const letter = String.fromCharCode(charCode).toUpperCase()
      return $('.keyboard-key--clickable[data-letter="' + letter + '"]')[0]
    },
    pressKey: function (keyElem, noAjax) { // Add pressed styles to keyboard key, send guess request to API
      if (!keyElem || this.currentGame.status !== 'active') return

      const jqKeyElem = $(keyElem)
      if (jqKeyElem.hasClass('keyboard-key--pressed')) return
      jqKeyElem.addClass('keyboard-key--pressed')
      jqKeyElem.prop('aria-disabled', true)
      jqKeyElem.prop('tabindex', -1)
      jqKeyElem.focus()
      if (noAjax) return
      const self = this

      $.ajax({
        type: 'POST',
        url: this.$root.getAPIEndpoint('games/' + this.currentGame.id + '/guesses'),
        data: JSON.stringify({letter: jqKeyElem.data('letter')}),
        contentType: 'application/json',
        success: this.onGuessLetterSuccess,
        error: function (res) {
          self.onGuessLetterFailure(res, keyElem)
        },
        xhrFields: {
          withCredentials: true
        }
      })
    },
    onGuessLetterSuccess: function ({guess}) { // Update the current game details with new letter
      this.currentGame.status = guess.gameStatus
      if (!guess.correct) this.wrongCount++
      if (guess.gameStatus === 'lost') this.wrongCount = 9
      if (guess.answer) this.currentGame.word.answer = guess.answer
      if (guess.indexes) {
        for (let i = 0; i < guess.indexes.length; i++) {
          let idx = guess.indexes[i]
          this.$set(this.knownLetters, idx, guess.letter)
        }
      }
    },
    onGuessLetterFailure: function (res, keyElem) { // Generally this happens when the game was changed in another tab
      if (res.status === 409) return // This means the letter is correct but guessed already
      if (res.status === 404) {
        this.updateGameInfo()
      } else {
        this.$toasted.error('Failed to guess letter', {duration: 3000})
        this.unPressButton(keyElem)
      }
    },
    playGame: function () { // Attempt to create a new game
      $.ajax({
        type: 'POST',
        url: this.$root.getAPIEndpoint('games'),
        success: this.onGetGameSuccess,
        error: this.onCreateGameFailure,
        complete: this.onGetGameComplete,
        xhrFields: {
          withCredentials: true
        }
      })
    },
    unPressButton: function (selector) {
      const jqKeyElem = $(selector)
      jqKeyElem.removeClass('keyboard-key--pressed')
      jqKeyElem.prop('aria-disabled', false)
      jqKeyElem.prop('tabindex', 0)
    },
    clearKeyboard: function () { // Clear the keyboard UI grey keys
      this.unPressButton('.keyboard-key--pressed')
      this.wrongCount = 0;
      $('div#app').focus();
    },
    onGetGameSuccess: function ({game}) { // Update the game with the new details
      this.currentGame = game
      this.knownLetters = Array(game.word.length).fill('')
      if (game.guessedCount > 0) {
        $.ajax({
          type: 'GET',
          url: this.$root.getAPIEndpoint('games/' + game.id + '/guesses'),
          success: this.onGetGuessesSuccess,
          error: this.onGetGuessesFailure,
          xhrFields: {
            withCredentials: true
          }
        })
      }
    },
    onGetGuessesSuccess: function ({guesses}) {
      for (let g = 0; g < guesses.length; g++) {
        const guess = guesses[g]
        if (!guess.correct) this.wrongCount++
        this.pressKey(this.getKey(guess.letter.charCodeAt(0)), true)
        if (!guess.indexes) continue
        for (let i = 0; i < guess.indexes.length; i++) {
          const idx = guess.indexes[i]
          this.$set(this.knownLetters, idx, guess.letter)
        }
      }
    },
    onGetGuessesFailure: function () {
      this.$toasted.error('Failed to obtain this game\'s previous guesses', {duration: 3000})
    },
    onGetGameFailure: function (res) { // Likely no active game exists since it couldn't retrieve a game
      this.currentGame.status = 'none-played'
      if (res.status === 404) return
      this.$toasted.error('Failed to retrieve game details', {duration: 3000})
    },
    onGetGameComplete: function () { // Reset keyboard since we just changed game
      this.clearKeyboard()
    },
    onCreateGameFailure: function (res) { // Create probably failed because game already exists, attempt fetch
      if (res.status === 409) {
        this.updateGameInfo()
      } else {
        this.$toasted.error('Failed to create game', {duration: 3000})
      }
    },
    updateGameInfo: function () { // Fetch current game info via API
      $.ajax({
        type: 'GET',
        url: this.$root.getAPIEndpoint('games/current'),
        success: this.onGetGameSuccess,
        error: this.onGetGameFailure,
        complete: this.onGetGameComplete,
        xhrFields: {
          withCredentials: true
        }
      })
    }
  },
  mounted: function () {
    const self = this
    $('div#app').on('keypress.kbKey', function (e) {
      const charCode = e.originalEvent.charCode
      self.pressKey(self.getKey(charCode))
    })
    this.updateGameInfo()
  },
  computed: {
    playGameBtnLabel: function () {
      if (this.currentGame.status === 'none-played') {
        return 'Play game'
      } else {
        return 'Play again'
      }
    }
  },
  watch: {
    '$root.loggedInUser': function () {
      this.updateGameInfo()
    }
  },
  beforeDestroy () {
    $('div#app').off('keypress.kbKey')
  }
}
