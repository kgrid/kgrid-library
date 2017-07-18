import Vue from 'vue'
import Vuex from 'vuex'
import * as getters from './getters'

Vue.use(Vuex)

const debug = process.env.NODE_ENV !== 'production'

export default new Vuex.Store({
  strict: debug,
  state:{
    debugEnabled:true,
    baseurl:'',
    currentUser:{username:"",password:""}
  },
  mutations: {
    seturl(state, url) {
      state.baseurl = url;
    },
    setuser(state, user){
      state.currentUser =user;
    }
  },
  getters: {
    isLoggedIn: state => {
     return (state.currentUser.username!='')
    },
    getfirstname: state => {
      return (state.currentUser.first_name || '')
    },
    getuser: state => {
       return state.currentUser
    }
  }
  // plugins: debug ? [createLogger()] : []
})
