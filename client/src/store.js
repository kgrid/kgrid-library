import Vue from 'vue';
import Vuex from 'vuex';
import api from './api.js'
import moment from 'moment'
import axios from 'axios'
import 'es6-promise/auto'
import VuexPersistence from 'vuex-persist'
Vue.use(Vuex);
const vuexLocal = new VuexPersistence ({
    key:"kgridlib",
    storage: window.localStorage,
    reducer: state => ({
      searchsources:state.searchsources
      , filterStrings:state.filterStrings
      , kgselect:state.kgselect
      , paths:state.paths
      , currentKOId:state.currentKOId
      , currentKO:state.currentKO
      , currentObject:state.currentObject

    })
})
export default new Vuex.Store({
  plugins: [vuexLocal.plugin],
  state: {
    searchsources:['Title'],
    filterStrings:[],
    kgselect:{'text':'Title - A to Z','v':'title','order':'asc'},
    currentKOId:{'naan':'','name':'','version':''},
    currentKO:{},
    currentObject:{},
    currentIOFileurl:'',
    kolist:[],
    paths:{
      "shelf_url": "./shelf/",
      "library_url": "./",
      "user_url": "./",
      "default_act_url_index":   0,
      "default_demo_url_index":    0,
      "activator_urls":["https://kgrid-activator.herokuapp.com"],
      "demo_urls":["https://kgrid-demos.github.io/swaggerui", "https://editor.swagger.io"]
    },
    metaschema:{},
  },
  getters: {
    getshelfurl:state=>{
      return state.paths.shelf_url
    },
    getobjectlist: state=>{
      return state.kolist
    },
    getCurrentKOId: state=>{
      return state.currentKOId
    },
    getCurrentKO: state=>{
      return state.currentKO
    },
    getCurrentObject: state=>{
      return state.currentObject
    },
    getcurrenturl: state=>{
      var s = state.currentKOId.naan+'/'+state.currentKOId.name
      if(state.currentKOId.version!=''){
        s= s+'/'+state.currentKOId.version
      }
      return s
    },
    getkoiourl: state=> {
      return state.currentIOFileurl
    },
    getschemabysection: state=>{
      return function(section){
        if(state.metaschema.properties[section]) {
          return state.metaschema.properties[section]
        } else {
          return {}
        }
      }
    },
    getcheck: state=>{
      return state.filtercheck
    },
    getfilters:state=>{
      return state.filterStrings
    },
    getSortKey: state=>{
      return (state.kgselect.value)
    },
    getSortOrder: state=>{
      return (state.kgselect.order)
    },
    getSearchSources : state =>{
      return state.searchsources
    },
    getKGSelect: state=>{
      return state.kgselect
    },

    getactivatorurls:state=>{
      return state.paths.activator_urls
    },
    getlibraryurl:state=>{
      return state.paths.library_url
    },
    getdefaultdemourlindex:state=>{
      return state.paths.default_demo_url_index
    },
    getdemourls:state=>{
      return state.paths.demo_urls
    },
    getdefaultactivatorurlindex:state=>{
      var i = state.paths.default_act_url_index
      return i
    },
  },
  mutations: {
    setmetaschema(state,schema){
      state.metaschema = schema
    },
    setkoiourl(state, uri){
      state.currentIOFileurl = uri
    },
    setCurrentKO(state, id){
      var s = id.replace("ark:/",'').split(/[/-]+/)
      state.currentKOId.naan = s[0]
      state.currentKOId.name = s[1]
      state.currentKOId.version = s[2] || ""
      var arkid = 'ark:/'+s[0]+'/'+s[1]
      state.kolist.forEach(function(e){
        var key = Object.keys(e)[0]
        if(key==arkid){
          state.currentKO=Object.values(e)[0]
        }
      })
    },
    setCurrentObject(state,obj){
      state.currentObject = obj
    },
    setkolist(state, list){
      state.kolist=[]
      list.forEach(function(e){
        state.kolist.push(e)
      })
      console.log("Retrieving KO List ...")
      console.log(state.kolist)
    },
    setcheck(state,obj){
      Object.assign(state.filtercheck, obj)
    },
    setkgselect(state,obj){
      Object.assign(state.kgselect, obj)
    },
    setfilterstrings(state,arr){
      state.filterStrings=arr;
    },
    setSearchSources(state, arr){
      state.searchsources = arr
    },
    setactivatorurls(state,urls){
      if(urls.length>0){
        state.paths.activator_urls = []
        urls.forEach(function(e){
            state.paths.activator_urls.push(e)
          }
        )
      }
    },
    setdefaultactivatorurlindex(state, i){
      state.paths.default_act_url_index = i
    },
  },
  actions: {
    fetchkolist (context) {
      var url = context.getters.getshelfurl
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
    fetchko (context, param) {
      console.log('Fetch KO:')
      console.log(param)
      var endpoint = context.getters.getshelfurl + param.uri;
      console.log(endpoint)
      return api.get(endpoint)
        .then((response) => {
          context.commit('setCurrentObject', response.data)
        })
        .catch((error) => context.commit('API_FAIL', error))
    },
    saveMetadata (context, data) {
      var bundle=JSON.parse(data)
      console.log(bundle)
      var endpoint = context.getters.getshelfurl +context.getters.getcurrenturl
      console.log(endpoint)
      return api.put(endpoint, bundle)
        .then((respnse)=>{
          console.log(respnse)
        })
        .catch((error)=>{
          console.log(error)
        })
    },
  },
});
