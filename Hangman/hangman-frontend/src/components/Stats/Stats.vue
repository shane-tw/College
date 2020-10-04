<template>
  <div class="hangman">
    <div class="hangman-title">
      <img src="static/icons/noose.svg" class="hangman-title__noose">
      <h1 class="hangman-title__text">Stats</h1>
    </div>
    <div class="hangman-section">
      <div v-if="!$root.loggedInUser">You need to log in for your stats to be tracked</div>
      <div v-else>
        <div v-if="games.length === 0">You haven't played any games yet</div>
        <div v-else>
          <h2>Wins and Losses</h2>
          <table class="table col-md-12 bg-white" style="margin-bottom: 1.5rem;" v-if="$root.loggedInUser">
            <thead>
              <tr>
                <th scope="col">Games won</th>
                <th scope="col">Games lost</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{{ gamesWon }}</td>
                <td>{{ gamesLost }}</td>
              </tr>
            </tbody>
          </table>

          <h2>Games</h2>
          <table class="table col-md-12 bg-white" v-if="$root.loggedInUser">
            <thead>
              <tr>
                <th scope="col">Category</th>
                <th scope="col">Answer</th>
                <th scope="col"># Guesses</th>
                <th scope="col">Status</th>
              </tr>
              <tr v-for="(game, gameIdx) in games" :key="gameIdx">
                <td>{{ game.word.category }}</td>
                <td>{{ game.word.answer }}</td>
                <td>{{ game.guessedCount }}</td>
                <td style="text-transform:capitalize;">{{ game.status }}</td>
              </tr>
            </thead>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script src="./Stats.js" />

<style lang="scss">
@import '@/assets/css/hangman.scss';
</style>
