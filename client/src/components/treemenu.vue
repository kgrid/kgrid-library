<template>
  <div>
    <div class="label-wrapper" @click="toggleChildren">
       <div :style="indent" :class="labelClasses">
         <i v-if="nodes" style="color:#777777;" class="fa" :class="iconClasses"></i>
         <i v-else style="color:#777777;" class="fa fa-file" ></i>
         {{ label }}
         <p v-show='!showChildren' v-if='label=="resource"|label=="service"'> {{nodes.length}}</p>
       </div>
     </div>
    <tree-menu
      v-if="showChildren"
      v-for="node in nodes"
      :nodes="node.nodes"
      :label="node.label"
      :depth="depth + 1"
      :key='node.label'
    >
    </tree-menu>
  </div>
</template>
<script>
  export default {
    props: [ 'label', 'nodes', 'depth' ],
    data() {
      return { showChildren: true }
    },
    name: 'tree-menu',
    computed: {
      iconClasses() {
        return {
          'fa-folder': !this.showChildren,
          'fa-folder-open': this.showChildren
        }
      },
      labelClasses() {
        return { 'has-children': this.nodes }
      },
      indent() {
        return { transform: `translate(${this.depth * 25}px)` }
      }
    },
    methods: {
      toggleChildren() {
        this.showChildren = !this.showChildren;
      }
    }
  }
</script>
<style scoped>
.label-wrapper {
  font-size:16px;
}
.label-wrapper p {
  display: inline-block;
  font-size: 10px;
  width: 1.5em;
  height:1.2em;
  text-align: center;
  border-radius:50%;
  background:#555555;
  color:#fff;
}
</style>
