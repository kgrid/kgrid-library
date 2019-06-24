import Vue from 'vue';
import Router from 'vue-router';
import Home from './views/Home.vue';
import objDetail from './views/objdetail.vue';
import objList from './views/objlist.vue';

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
    },
    {
      path: '/list',
      name: 'objlist',
      component: objList,
    },
    {
      path: '/object/:uri',
      name: 'object',
      component: objDetail,
      data() {
        console.log(`current URI${this.$route.params.uri}`);
      },
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import(/* webpackChunkName: "about" */ './views/About.vue'),
    },
    {
      path: '/faq',
      name: 'faq',
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import(/* webpackChunkName: "about" */ './views/Faq.vue'),
    },
    {
      path: '/contactus',
      name: 'contactus',
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import(/* webpackChunkName: "about" */ './views/Contactus.vue'),
    }
  ],
});
