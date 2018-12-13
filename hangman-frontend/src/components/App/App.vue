<template>
  <div id="app" class="d-flex flex-column" tabindex="-1" v-if="doneLoginCheck">
    <Navbar />
    <div class="container flex-grow-1" id="content">
      <router-view />
    </div>
  </div>
</template>

<script src="./App.js" />

<style>
@import 'bootstrap/dist/css/bootstrap.min.css';
@import 'bootstrap-vue/dist/bootstrap-vue.min.css';

#content {
  color: #2c3e50;
  background-color: #ebeaeb;
}

#app {
  min-height: 100vh;
  background-color: #e6e4e6;
}
</style>
