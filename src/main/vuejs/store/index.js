import Vue from 'vue'
import Vuex from 'vuex'
import moment from 'moment'

Vue.use(Vuex)

const debug = process.env.NODE_ENV !== 'production'

export default new Vuex.Store({
  strict: debug,
  state:{
    debugEnabled:true,
    baseurl:'',
    currentUser: {username:"",password:"",admin:false},
    currentObject: { metadata:{title:"",keywords:"",contributors:"",published:"",citations:[],license:{licenseName:"",licenseLink:""}}, payload:{functionName:"",engineType:"",content:""},inputMessage:"", outputMessage:"", uri:"",published:false,lastModified:0,createdOn:0} ,
    activeTab:'METADATA',
    libraryEnv: {git:{commit:{time:0,id:''}},build:{version:'',artifact:'',name:'',group:'',time:0},'library.name':''},
    libraryname: '',
    fields_json:{fields:[]}
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
    },
    setobject(state, obj) {
      state.currentObject = obj;
    },
    setenv(state, env){

      state.libraryEnv = env;
      state.libraryname= env["library.name"];
    },
    setfields(state,data){
      state.fields_json=data;
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
    },
    getobject: state =>{
      return state.currentObject
    },
    getLibraryName: state =>{
        return (state.libraryname || '')
    },
    getGitID: state=>{
        return (state.libraryEnv.git.commit.id ||'')
    },
    getBuildTime: state=>{
        return (state.libraryEnv.build.time || 0)
    },
    getBuildTimeString: state=>{
     var t = 'Not Available'
     if(state.libraryEnv.build.time){
          t= moment(new Date(
    					state.libraryEnv.build.time)).toString()
      }
        return t
    },
    getVersion: state=>{
        return (state.libraryEnv.build.version ||'')
    },
    getfields: state=>{
        return (state.fields_json)
    }
  }
  // plugins: debug ? [createLogger()] : []
})
