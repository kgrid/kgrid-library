import Vue from 'vue'
import Vuex from 'vuex'
import api from './api.js'
import moment from 'moment'
import axios from 'axios'
import 'es6-promise/auto'
import VuexPersistence from 'vuex-persist'
Vue.use(Vuex)
const debug = process.env.NODE_ENV !== 'production'
const objModel={ metadata:{ metadata:{title:"",keywords:"",contributors:"",arkId:{arkId:'',fedoraPath:''},published:false,citations:[],license:{licenseName:"",licenseLink:""},published:false,lastModified:0,createdOn:0}, model:{functionName:"",adapterType:"",resource:"",service:""}}}
const defaultfilterCheck={ keywords : true, owners : true, title : true, citations : false, contributors : false, objectID : false, pub : true, pri : false, showmyobj:false};
const defaultSort={sortkey:'metadata.lastModified', order:'desc'};
const defaultkgselect={'label':'Last Updated - Newest', 'value':'metadata.lastModified','order':'desc'};
const defaultdateRange={datetype:'none',startTime: {time: moment('2016-09-01T00:00:00-05:00').format("MM/DD/YYYY")}, endTime: {time: moment().endOf('day').format("MM/DD/YYYY")}};
const sections = [ {name:"metadata",id:"#metadata",label:"METADATA"}
                 , {name:"model",id:"#model",label:"MODEL"}
                 // , {name:"resource",id:"#resource", label:"RESOURCE"}
                 // , {name:"service",id:"#service", label:"SERVICE"}
                 , {name:"assets",id:"#assets",label:"ASSETS"}
                 , {name:"logData",id:"#logData", label:"LOG DATA"}
                 , {name:"versions",id:"#version", label:"VERSIONS"}
               ];
const config=  { headers: {
      'Access-Control-Allow-Origin': '*',
      'Content-Type': 'application/json',
      'Accept':'application/json'
    }, withCredentials: true, crossDomain: true }
const vuexLocal = new VuexPersistence ({
    key:"first",
    storage: window.localStorage,
    reducer: state => ({filtercheck:state.filtercheck
      , filterStrings:state.filterStrings
      , kgselect:state.kgselect
      , baseurl:state.baseurl
      , paths:state.paths
      , currentDownloadlink:state.currentDownloadlink
      , currentVersion:state.currentVersion
      , currentUrl:state.currentUrl
      , currenturi:state.currenturi
      , currentObjectId:state.currentObjectId
    })
})
export default new Vuex.Store({
  // strict: debug,
  plugins: [vuexLocal.plugin],
  state:{
    filtercheck:{ keywords : true, owners : true, title : true, citations : false, contributors : false, objectID : false, pub : true, pri : true, showmyobj:false},
    filterStrings:[],
    kgselect:{'label':'Last Updated - Newest', 'value':'metadata.lastModified','order':'desc'},
    dateRange:{datetype:'none',startTime: {time: moment('2016-09-01T00:00:00-05:00').format("MM/DD/YYYY")}, endTime: {time: moment().format("MM/DD/YYYY")}},
    debugEnabled:true,
    baseurl:'',
    currentUser: {},
    isloading:false,
    currentObject: {} ,
    currentVersionlist:[],
    currentObjectId:{'naan':'','name':'','version':''},
    currentDownloadlink:'',
    currentUrl:'',
    currenturi:'',
    currentVersion:'',
    activeTab:'METADATA',
    currentbundlename:'',
    currentbundle:{},
    libraryEnv: {},
    libraryname: '',
    fields_json:{fields:[]},
    kolist:[],
    userlist:[],
    kotree:{},
    currentMetadataBundle:{},
    paths:{},
    metaschema:{}
  },
  mutations: {
    seturl(state, url) {
      state.baseurl = url;
    },
    setpaths(state, paths){
      state.paths=JSON.parse(JSON.stringify(paths))
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
    setkolist(state, list){
      state.kolist=list
    },
    setuserlist(state, list){
      state.userlist=list
    },
    showprivate(state,b){
      state.filtercheck.pri=b
    },
    setversionlist(state,list){
      state.currentVersionlist=[]
      list.forEach(function(e, index){
        var ver={};
        ver.version=e.version;
        ver.title=e.object.title
        ver.notes=e.version
        ver.isdefault=false;
        if(index==0)  ver.isdefault=true
        state.currentVersionlist.push(ver)
      })
    },
    setmetaschema(state,schema){
      state.metaschema = schema
    },
    setdefaultversion(state, ver){
        var i=state.currentVersionlist.indexOf(ver)
        console.log(i);
        console.log(ver.version);
        state.currentVersionlist.forEach(function(e, index){
          if(index==i){
            e.isdefault=true;
          }else{
              e.isdefault=false;
            }
        })
    },
    setenv(state, env){
      console.log(env)
      state.libraryEnv = JSON.parse(JSON.stringify(env));
      state.libraryname= env["library.name"];
    },
    setfields(state,data){
      state.fields_json=data;
    },
    setkottree(state,data){
      state.kotree=data
    },
    setkgselect(state, sel){
      state.kgselect=sel;
    },
    setcheck(state,obj){
      Object.assign(state.filtercheck, obj)
    },
    setdaterange(state, daterange){
      state.dateRange=daterange
    },
    setfilterstrings(state,arr){
      state.filterStrings=arr;
    },
    updateMetadata(state,obj){
      Object.assign(state.currentMetadataBundle,obj)
    },
    resetCurrentobj(state,id){
      var arr=id.replace('ark:/','').split('/');
      // console.log('Array:')
      // console.log(arr)
      state.currentObjectId.naan=arr[0]
      state.currentObjectId.name=arr[1]
      state.currentObjectId.version=arr[2]
      // state.currentObject = objModel;
      state.currentDownloadlink=state.paths.shelf_url+id.replace('ark:/','')
      state.currentUrl=state.paths.shelf_url+id;
      state.currenturi=id;
    },
    'API_FAIL': function (state, error) {
      // console.error(error)
    },
    'LOGIN_FAIL':function(state, error) {
      console.log(error);
    },
    setloadingstatus(state,bool){
      state.isloading=bool;
    },
    setcurrenturl(state,id){
      state.currentUrl=state.paths.shelf_url+id
    }
  },
  getters: {
    getyamlurl:state=>{
      return state.currentUrl +'/'+state.currentObject.service
    },
    getcodeurl:state=>{
      if(state.currentObject.model){
        return state.currentUrl +'/'+state.currentObject.model.resource
      } else {
        return state.currentUrl
      }
    },
    constSections:state=>{
      return sections
    },
    getcurrenturl:state=>{
      return state.currentUrl
    },
    getcurrenturi:state=>{
      return state.currenturi
    },
    getcurrentdownloadlink:state=>{
      return state.currentDownloadlink
    },
    getSortKey: state=>{
      return (state.kgselect.value)
    },
    getSortOrder: state=>{
      return (state.kgselect.order)
    },
    getkgselect: state=>{
      return  state.kgselect
    },
    getcheck: state=>{
      return state.filtercheck
    },
    getdaterange:state=>{
      return state.dateRange
    },
    getfilters:state=>{
      return state.filterStrings
    },
    isLoggedIn: state => {
     return (state.currentUser.username!='')
    },
    isAdmin: state => {
     return (state.currentUser.admin)
    },
    getfirstname: state => {
      return (state.currentUser.first_name || '')
    },
    getkotree:state=>{
      return state.kotree
    },
    getuser: state => {
       return state.currentUser
    },
    getuserlist:state=>{
      return state.userlist
    },
    getactivetab: state => {
      return state.activeTab
    },
    getobjectlist: state=>{
      return state.kolist
    },
    getobject: state =>{
      return state.currentObject
    },
    getverlist:state=>{
      return state.currentVersionlist
    },
    getLibraryName: state =>{
        return (state.libraryname || '')
    },
    // getGitID: state=>{
    //     return (state.libraryEnv.git.commit.id ||'')
    // },
    getBuildTime: state=>{
      if(state.libraryEnv.build){
        return (state.libraryEnv.build.time || 0)
      } else {
        return 0
      }
    },
    getBuildTimeString: state=>{
     var t = 'Not Available'
     if(state.libraryEnv.build){
          t= moment(new Date(
              state.libraryEnv.build.time)).toString()
      }
      return t
    },
    getVersion: state=>{
      if(state.libraryEnv.build){
        return (state.libraryEnv.build.version ||'')
      } else {
        return ''
      }
    },
    getfields: state=>{
        return (state.fields_json)
    },
    isLoading:state=>{
      return state.isloading
    },
    getshelfurl:state=>{
      return state.paths.shelf_url
    },
    getuserurl:state=>{
      return state.paths.user_url
    },
    getlibraryurl:state=>{
      return state.paths.library_url
    },
    getschemabysection: state=>{
      return function(section){
        if(state.metaschema.properties[section]) {
          return state.metaschema.properties[section]
        } else {
          return {}
        }
      }
    }
  },
  actions: {
    fetchkolist (context) {
      var url = context.getters.getshelfurl
      console.log('fetch ko list from '+url)
      return api.get(url)
        .then(function(response){
          var l = [];
          for(var key in response.data){
            var obj={}
            obj[key]=response.data[key]
            l.push(obj)
          }
          context.commit('setkolist',l)
        })
        .catch((error) => {
          context.commit('API_FAIL', error)
        })
    },
    fetchuserlist (context) {
      var url = context.getters.getuserurl + 'user'
      return api.get(url)
        .then(function(response){
          var r= false;
          var l = [];
          response.data.forEach(function(e){
            l.push(e)
          });
          context.commit('setuserlist',l)
        })
        .catch((error) => {
          context.commit('API_FAIL', error)
        })
    },
    fetchversionlist (context, uri) {
      var endpoint = context.getters.getshelfurl +uri;
      context.commit('setversionlist', [])
      return api.get(endpoint, config)
        .then((response) => {
          var vlist =[]
          var obj = response.data
          for(var key in obj){
            if(obj.hasOwnProperty(key)){
              vlist.push({"version":key, "object":obj[key]})
            }
          }
          context.commit('setversionlist', vlist)
        })
        .catch((error) => context.commit('API_FAIL', error))
    },
    saveMetadata (context, data) {
      var bundle=JSON.parse(data)
      console.log(bundle)
      var path='/'
      if(bundle.section!='metadata'){
        path=path+bundle.section
      }
      var endpoint = context.getters.getcurrenturl+path
      console.log(endpoint)
      return api.put(endpoint, bundle.data)
        .then((respnse)=>{
          console.log(respnse)
        })
        .catch((error)=>{
          console.log(error)
        })
    },
    checkenv (context) {
      var url = context.getters.getlibraryurl+'info'
      console.log(url)
      return api.get(url)
        .then((response) => {
          console.log(response.data)
          context.commit('setenv', response.data)
        })
        .catch((error) => context.commit('API_FAIL', error))
    },
    fetchko (context, param) {
      console.log('Fetch KO:')
      console.log(param)
      var endpoint = context.getters.getshelfurl + param.uri;
      console.log(endpoint)
      return api.get(endpoint)
        .then((response) => {
          context.commit('setobject', response.data)
          context.commit('resetCurrentobj',param.uri)
        })
        .catch((error) => context.commit('API_FAIL', error))
    },
    login (context, user) {
      var url = context.getters.getuserurl+'login'
      return api.formpost(url,user)
        .then((response) => context.commit('setuser',response.data))
        .catch((error) => context.commit('API_FAIL', error))
    },
    logout (context) {
      var url = context.getters.getuserurl+'logout'
      return api.post(url)
        .then((response) => context.commit('setuser',{username: '', password: ''}))
        .catch((error) => context.commit('API_FAIL', error))
    },
    createobject(context,data){
      var url = context.getters.getshelfurl+'knowledgeObject'
      return api.post(url,data,{headers:{'Content-type':'text/plain'}})
        .then((response)=>console.log(response))
        .catch((error)=>context.commit('API_FAIL',error))
    },
    setdefaultversion(context, uri){
      console.log('Fetch KO:')
      console.log(uri)
      var endpoint = context.getters.getshelfurl+uri;
      return api.patch(endpoint)
        .then((response) => console.log('successfully reverted the version'))
        .catch((error) => context.commit('API_FAIL', error))
    },
    getcurrentuser(context){
      var url = context.getters.getuserurl+'user/me'
      return api.get(url, config)
        .then((response) => context.commit('setuser', response.data))
        .catch((error) => {
          context.commit('setuser',{username: '', password: ''})
          // context.commit('API_FAIL', error)
        })
    },
    copytoclipboard(context, text){
      if (window.clipboardData && window.clipboardData.setData) {
        // IE specific code path to prevent textarea being shown while dialog is visible.
        return clipboardData.setData("Text", text);
      } else if (document.queryCommandSupported && document.queryCommandSupported("copy")) {
        var textarea = document.createElement("textarea");
        textarea.textContent = text;
        textarea.style.position = "fixed";  // Prevent scrolling to bottom of page in MS Edge.
        document.body.appendChild(textarea);
        textarea.select();
        try {
            return document.execCommand("copy");  // Security exception may be thrown by some browsers.
        } catch (ex) {
            console.warn("Copy to clipboard failed.", ex);
            return false;
        } finally {
            document.body.removeChild(textarea);
        }
    }
    }
  }
})
