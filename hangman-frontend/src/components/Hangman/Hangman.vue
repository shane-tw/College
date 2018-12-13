<template>
  <div class="hangman">
    <div class="hangman-title">
      <img src="static/icons/noose.svg" class="hangman-title__noose">
      <h1 class="hangman-title__text">Hangman</h1>
    </div>
    <div class="keyboard">
      <div class="keyboard-row" v-for="(kbRow, kbRowIdx) in keyboardRows" :key="kbRowIdx">
        <button class="keyboard-key keyboard-key--orange keyboard-key--clickable" v-for="(kbKey, kbKeyIdx) in kbRow" :key="kbKeyIdx"
              :id="`kbKey-${kbKey}`" :data-letter="kbKey" @click="pressKey($event.target)">{{ kbKey }}</button>
      </div>
    </div>
    <div class="hangman-text" v-if="currentGame.status === 'active'">
      <div>Category: <em>{{ currentGame.word.category }}</em></div>
      <div class="hangman-text-bottom keyboard">
        <div class="keyboard-row">
          <div class="keyboard-key keyboard-key--green keyboard-key--no-v-margin"
              v-for="(letter, letterIdx) in knownLetters" :key="letterIdx">{{ letter }}</div>
        </div>
      </div>
    </div>
    <div class="hangman-text" v-if="currentGame.status !== 'active'">
      <div v-if="currentGame.status === 'lost'">Sorry, you lost! The secret word was {{ currentGame.word.answer }}</div>
      <div v-else-if="currentGame.status === 'won'">Great job, you guessed the secret word!</div>
      <button class="hangman-text-bottom hangman-text__button hangman-text__button--clickable" @click="playGame">{{ playGameBtnLabel }}</button>
    </div>
    <div class="hangman-image-holder">
      <img class="hangman-image-holder__image" src="static/images/won.png" v-if="currentGame.status === 'won'">
      <img class="hangman-image-holder__image" :src="`static/images/hangman-${wrongCount}.png`" v-else>
    </div>
    <div class="hangman-note" v-if="!$root.loggedInUser">
      <b>Note</b>: Your stats will not be tracked until you log in
    </div>
  </div>
</template>

<script src="./Hangman.js" />

<style lang="scss">
@import '@/assets/css/hangman.scss';
@import '@/assets/css/keyboard.scss';
</style>
