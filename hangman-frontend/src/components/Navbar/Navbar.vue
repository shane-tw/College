<template>
  <b-navbar toggleable="md" type="dark" variant="dark">
    <b-navbar-toggle target="nav_collapse"></b-navbar-toggle>
    <b-navbar-brand href="#">Game Centre</b-navbar-brand>
    <b-collapse is-nav id="nav_collapse">
      <b-navbar-nav>
        <b-nav-item to="/" exact>Play Game</b-nav-item>
        <b-nav-item to="/stats">View Stats</b-nav-item>
      </b-navbar-nav>

      <b-navbar-nav class="ml-auto">
        <b-nav-item href="#" v-b-modal.authModal @click="authMode = 'login'" v-if="!$root.loggedInUser">Log in</b-nav-item>
        <b-nav-item href="#" v-b-modal.authModal @click="authMode = 'register'" v-if="!$root.loggedInUser">Register</b-nav-item>
        <b-nav-item-dropdown right v-if="$root.loggedInUser">
          <template slot="button-content">
            <span>{{ $root.loggedInUser.firstName }} {{$root.loggedInUser.lastName}}</span>
          </template>
          <b-dropdown-item href="#" @click="signOut">Sign out</b-dropdown-item>
        </b-nav-item-dropdown>
      </b-navbar-nav>
    </b-collapse>
    <AuthModal :mode="authMode" />
  </b-navbar>
</template>

<script src="./Navbar.js" />
