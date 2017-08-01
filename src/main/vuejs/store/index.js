import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const debug = process.env.NODE_ENV !== 'production'

export default new Vuex.Store({
  strict: debug,
  state:{
    debugEnabled:true,
    baseurl:'',
    currentUser:{username:"",password:"",admin:false},
    libraryname:'DLHS Development Server',
    activeTab:'METADATA'
  },
  mutations: {
    seturl(state, url) {
      state.baseurl = url;
    },
    setuser(state, user){
      state.currentUser =user;
    },
    setactivetab(state, tab){
      state.activeTab = tab;
    }
  },
  getters: {
    isLoggedIn: state => {
     return (state.currentUser.username!='')
    },
    isAdmin: state => {
     return (state.currentUser.admin)
    },
    getfirstname: state => {
      return (state.currentUser.first_name || '')
    },
    getuser: state => {
       return state.currentUser
    },
    getactivetab: state => {
      return state.activeTab
    }
  }
  // plugins: debug ? [createLogger()] : []
})
