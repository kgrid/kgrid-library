<template>
  <v-container fluid>
    <v-layout row wrap v-scroll="onScroll" >
      <v-flex xs2 md2 my-4>
        <v-text-field
          :label="searchlabel"
          v-model='searchinput'
          append-icon="search"
          :prepend-icon = 'prependIcon'
          @keyup.enter='addFilterString'
          @click:prepend="toggleOptions"
        ></v-text-field>
        <v-layout row wrap justify-center elevation-2  v-show='showOption'>
          <v-flex xs10 pb-3>
          <v-checkbox v-model="searchsources" label="Title" hide-details value="Title" height='21px'></v-checkbox>
          <v-checkbox v-model="searchsources" label="Description" hide-details value="Description" height='21px'></v-checkbox>
          <v-checkbox v-model="searchsources" label="Keywords" hide-details value="Keywords"></v-checkbox>
          <!-- <v-checkbox v-model="searchsources" label="Owners"  hide-details disabled value="Owners"></v-checkbox>
          <v-checkbox v-model="searchsources" label="Contributors" hide-details disabled value="Contributors"></v-checkbox> -->
          <v-checkbox v-model="searchsources" label="Object ID"  hide-details value="ID"></v-checkbox>
        </v-flex>
        </v-layout>
        <div>
          <v-chip close v-for='s in filterStrings' @input="removefilterstring(s)" :key='s'>{{s}}</v-chip>
          <v-chip v-if='filterStrings.length>0' @click="removeAllFilters">Remove All</v-chip>
        </div>
      </v-flex>
      <v-flex xs8 md8 px-0>
        <v-container>
          <v-layout >
            <v-flex>
              <KoList :list='koIndexer'/>
            </v-flex>
          </v-layout>
        </v-container>
      </v-flex>
      <v-flex xs2 md2 px-0 >
        <v-container fluid>
          <v-layout row wrap>
            <v-flex mt-4 mb-4 class='text-xs-center'>
              <span class='title'>
                {{countString}}
              </span>
            </v-flex>
            <v-flex>
              <v-select
                v-model='sortSelect'
                :items="optionlist"
                label="Sort By"
                outline
                return-object
                hide-details
              ></v-select>
            </v-flex>
          </v-layout>
        </v-container>
      </v-flex>
      <v-speed-dial
        v-model="fab"
        :top="top"
        :bottom="bottom"
        :right="right"
        :left="left"
        :direction="direction"
        :open-on-hover="hover"
        :transition="transition"
        fixed
        >
        <v-btn
         slot="activator"
         v-model="fab"
         color="blue darken-2"
         dark
         fab
        >
          <v-icon>account_circle</v-icon>
          <v-icon>close</v-icon>
        </v-btn>
        <v-btn
         fab
         dark
         small
         color="indigo"
         @click='depositko'
        >
          <v-icon>add</v-icon>
        </v-btn>
      </v-speed-dial>
      <v-fab-transition>
        <v-btn
          color="#0075bc"
          dark
          small
          fixed
          bottom
          left
          fab
          v-show='offsetTop>=10'
          @click = 'backtoTop'
        >
          <v-icon>keyboard_arrow_up</v-icon>
        </v-btn>
      </v-fab-transition>
    </v-layout>
  </v-container>
</template>

<script>
  import KoList from '../components/kolist'
  import store from '../store.js'
  export default {
    name:'List',
    data : function() {
      return {
        direction: 'top',
        fab:false,
        fling: false,
        hover: false,
        top: false,
        right: true,
        bottom: true,
        left: false,
        transition: 'slide-y-reverse-transition',
        searchsources: ['Title', 'Keywords'],
        radios: 'none',
        picker: new Date().toISOString().substr(0, 10),
        startdate: new Date().toISOString().substr(0, 10),
        enddate: new Date().toISOString().substr(0, 10),
        menu1: false,
        menu2: false,
        reactive: false,
        searchinput:'',
        rawlist:[],
        search:'',
        filter:'',
  			// filterStrings:[],
        items:['Title - A to Z','Title - Z to A','Object ID - A to Z','Object ID -Z to A'],
        optionlist:[
            {'text':'Title - A to Z','v':'title','order':'asc'},
            {'text':'Title - Z to A','v':'title','order':'desc'},
            {'text':'Object ID - A to Z','v':'uri','order':'asc'},
            {'text':'Object ID - Z to A','v':'uri','order':'desc'}
            ],
        sortSelect:{},
        offsetTop:0,
        duration: 300,
        offset:0,
        easing:'easeInOutCubic',
        prependIcon:'keyboard_arrow_down',
        showOption:  false
      }
    }
    , created : function() {
      var self= this
      this.rawlist=this.$store.getters.getobjectlist;
    }
    ,	beforeRouteEnter (to, from, next) {
        if(store.getters.getservers.length>0){
    			store.dispatch('fetchkolist').then(function(){
            if(store.getters.getErrorStatus){
              next('/')
            } else {
      			 	next()
            }
    		  }).catch(e=>{
      			console.log(e)
      		})
        } else {
          next('/')
        }
    }
    , computed: {
        koIndexer:function(){
          var indexer = {}
          this.orderedList.forEach(function(e){
            var id = e.identifier
            if(!indexer[id]){
              indexer[id]=[]
            }
            indexer[id].push(e)
          })
          for(var key in indexer){
            if(indexer[key].length>1){
              indexer[key] = _.orderBy(indexer[key], 'version','desc')
            }
          }
          return indexer
        },
        panelheight: function(){
          return this.windowHeight - 120
        },
        panelStyle () {
          return {
            height: `${this.panelheight}px`,
          }
        },
        options () {
          return {
            duration: this.duration,
            offset: this.offset,
            easing: this.easing
          }
        },
        orderedList : function() {
          var l = []
          this.filteredList.forEach(function(e){
            l.push(e)
          })
          switch(this.sortSelect.v) {
            case 'uri':
              return _.orderBy(l, 'identifier', this.sortSelect.order);
            case 'title':
              return _.orderBy(l, 'title', this.sortSelect.order);
            default:
              return l;
          }
        },
        countString : function() {
          var count = this.orderedList.length;
          if (count <= 1) {
            return 'Viewing ' +count + ' Object';
          } else if (count > 1200) {
            return 'Many many Objects';
          } else {
            return 'Viewing ' + count + ' Objects';
          }
        },
        searchlabel () {
          var s = 'Search by'
          var l = this.searchsources.length
          this.searchsources.forEach(function(e,index){
            switch(index){
              case 0:
                s=s+' '+e
                break
              case l-1:
                s=s+ ' and '+e
                break
              default:
                s=s+', '+e
                break
            }
          })
          return s
        },
        filteredList : function() {
    			var self = this;
    			var list = this.rawlist;
			    return list.filter(function(e){
    				var customFilter = true;
    				if (self.filterStrings.length > 0) {
    						self.filterStrings.forEach(function(s) {
    							var filterResult = false;
    							if (s === '') {
    								filterResult = true;
    							} else {
    								var fString = new RegExp(s,'i');
    								if (self.searchsources.indexOf('Title')!=-1&&e.title) {
    									filterResult = (filterResult || ((e.title
    											.search(fString))!=-1));
    								}
    								if (self.searchsources.indexOf('Keywords')!=-1&&e.keywords) {
                      var kw = ""
                      if(Array.isArray(e.keywords)){
                        kw=e.keywords.join(',')
                      }else {
                        kw=e.keywords
                      }
    									filterResult = (filterResult || ((kw
    											.search(fString))!=-1));													}
    								if (self.searchsources.indexOf('Description')!=-1&&e.description) {
    									filterResult = (filterResult || ((e.description
    											.search(fString))!=-1));
    								}
                    if (self.searchsources.indexOf('ID')!=-1&&e.identifier) {
                      filterResult = (filterResult || ((e.identifier
                          .search(fString))!=-1));
                    }
    							}
    								customFilter = customFilter	&& filterResult;
    						})
    					}
    					return customFilter;
      			});
  		    },
          filterStrings: function() {
            return this.$store.getters.getfilters;
          }
    }
    , components: {
      KoList
    },
    mounted :function(){
      this.sortSelect = this.$store.getters.getKGSelect
      this.searchsources= this.$store.getters.getSearchSources
    },
    watch: {
      searchsources:{
        handler: function(){
				  	this.setSearchSources()
				},
				deep:true
      },
      sortSelect :{
        handler: function(){
            this.setKgSelect()
        },
        deep:true
      }
    },
    methods: {
      onScroll (e) {
        this.offsetTop = e.target.scrollingElement.scrollTop
      },
      setSearchSources: function(){
        this.$store.commit('setSearchSources', this.searchsources);
      },
      setKgSelect: function(){
        this.$store.commit('setkgselect', this.sortSelect);
      },
      depositko () {
        this.$eventBus.$emit('addobj','ko')
        console.log("Deposit KO")
      },
      addFilterString () {
        var value = this.searchinput && this.searchinput.trim();
        if (!value) {      return;      }
        this.searchinput = '';
        this.$store.commit('addFilterString', value);
      },
      removefilterstring: function(s) {
        this.$store.commit('removeFilterString', s);
      },
      removeAllFilters () {
        this.$store.commit('setFilterStrings', []);
      },
      backtoTop () {
        this.$vuetify.goTo(0, this.options)
        this.offsetTop = 0
      },
      toggleOptions: function() {
        console.log(this.showOption)
        this.showOption = !this.showOption
        this.prependIcon = this.showOption ?'keyboard_arrow_up':'keyboard_arrow_down'
      }
    }
  }
</script>
