<template>
  <div>
    <v-card block>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn icon @click="uploadAll" :disabled="pendingFiles.length === 0 || fileErrors.length > 0">
          <v-icon>file_upload</v-icon>
        </v-btn>
      </v-card-actions>
      <v-container grid-list-xl fluid>
      <v-toolbar class="error" dark dense v-if="errorBar">
        <v-toolbar-title>
          <h3 class="title">{{ errorBar }}</h3>
        </v-toolbar-title>
      </v-toolbar>
      <v-layout row wrap>
        <v-flex xs12 sm6 md4 lg3 v-for="(file, f) in files" :key="f">
          <v-card class="text-xs-center">
            <v-card-media
              height="200px"
              :src="file.src"
              contain
            >
              <v-container fill-height fluid v-if="!file.src" >
                <v-layout fill-height align-center wrap>
                  <v-flex xs12>
                    <v-icon x-large>web_asset</v-icon>
                  </v-flex>
                </v-layout>
              </v-container>
            </v-card-media>
            <v-card-title text-xs-center>
              <div style="width:100%">
                <h3 class="body-2">
                  <a v-if="file.url" :href="file.url">{{ file.data.name }}</a>
                  <span v-else>{{ file.data.name }}</span>
                </h3>
                <h3 class="body-2 grey--text" >{{ file.data.size | humanSize }}</h3>
                <h3><v-progress-linear :value="file.uploadProgress"></v-progress-linear></h3>
              </div>
            </v-card-title>
            <v-card-actions v-if="!file.uploaded">
              <v-spacer></v-spacer>
              <v-tooltip top v-if="file.error">
                <v-btn icon @click="remove(file)" slot="activator" class="error">
                  <v-icon>close</v-icon>
                </v-btn>
                <span>{{ file.error }}</span>
              </v-tooltip>
              <v-btn icon @click="uploadFile(file)" v-else-if="!file.uploadProgress">
                <v-icon >file_upload</v-icon>
              </v-btn>
              <v-btn icon @click="remove(file)" v-else-if="!file.error">
                <v-icon>close</v-icon>
              </v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
            <v-card-actions v-else>
              <v-spacer></v-spacer>
              <v-btn icon class="success" @click="">
                <v-icon>check</v-icon>
              </v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-flex>
      </v-layout>
      </v-container>
      <v-card-actions>
        <v-btn flat text block class="jbtn-file">{{ btnLabel }}<input id="fileinput" type="file" multiple @change="fileSelected"></v-btn>
      </v-card-actions>
    </v-card>

  </div>
</template>

<script>
const axios = require('axios')
const uuidv4 = require('uuid/v4')
export default {
  props: {
    maxFileSize: {
      type: Number,
      default: 10000000
    },
    btnLabel: {
      type: String,
      default: 'Drag or click here to select files'
    },
    region: {
      type: String,
      default: 'eu-west-1'
    },
    prefix: {
      type: String,
      default: 'upload'
    },
    maxFiles: {
      type: Number,
      default: 100
    }
  },
  data () {
    return {
      uploadedFiles: [],
      selectedFiles: {},
      selectedFileIds: [],
      errorBar: null
    }
  },
  filters: {
    humanSize (size) {
      var i = Math.floor(Math.log(size) / Math.log(1024))
      return (size / Math.pow(1024, i)).toFixed(2) * 1 + ' ' + ['B', 'kB', 'MB', 'GB', 'TB'][i]
    }
  },
  computed: {
    fileErrors () {
      return this.files.filter(file => !!file.error)
    },
    files () {
      return this.selectedFileIds.map(id => this.selectedFiles[id])
    },
    pendingFiles () {
      return this.files.filter(file => !file.uploaded)
    }
  },
  methods: {
    selectFile (file) {
      var id = uuidv4()
      var key = this.prefix + '/' + id + '/' + file.name
      var f = {
        id: id,
        data: file,
        uploaded: false,
        src: null,
        url: null,
        error: null,
        key,
        uploadProgress: null
      }
      if (file.size > this.maxFileSize) {
        f.error = 'File is above the max allowed size'
      }
      if (file.size < 5000000 && file.type === 'image/png') {
        var reader = new FileReader()
        reader.onloadend = function () {
          f.src = reader.result
        }
        reader.readAsDataURL(file)
      }
      this.selectedFiles = { ...this.selectedFiles, [f.id]: f }
      this.selectedFileIds.push(f.id)
    },
    fileSelected (e) {
      for (var i = 0; i < e.target.files.length; i++) {
        var file = e.target.files[i]
        this.selectFile(file)
      }
      document.getElementById('fileinput').value = ''
    },
    remove (file) {
      delete this.selectedFiles[file.id]
      this.selectedFileIds.splice(this.selectedFileIds.indexOf(file.id), 1)
    },
    uploadAll () {
      var files = this.files.filter(file => !file.uploaded && !file.error)
      for (var f in files) {
        var file = files[f]
        this.uploadFile(file)
      }
    },
    async uploadFile (file) {
      var self = this
      file.progress = 0
      var post = { prefix: file.id + '/' + file.data.name }
      var { data } = await axios.post('/api/sign-upload', post)
      var form = new FormData()
      form.append('key', process.env.PREFIX + '/' + post.prefix)
      form.append('X-Amz-Algorithm', data.fields['X-Amz-Algorithm'])
      form.append('X-Amz-Credential', data.fields['X-Amz-Credential'])
      form.append('X-Amz-Date', data.fields['X-Amz-Date'])
      form.append('Policy', data.fields['Policy'])
      form.append('X-Amz-Signature', data.fields['X-Amz-Signature'])
      form.append('file', file.data)
      var xhr = new XMLHttpRequest()
      xhr.addEventListener('readystatechange', async function (result) {
        if (this.readyState === 4) {
          if (this.status === 204) {
            var { data } = await axios.post('/api/sign-download', { key: process.env.PREFIX + '/' + post.prefix })
            file.uploaded = true
            file.url = data.url
            file.uploadProgress = 100
            self.uploadedFiles.push(file)
            self.$emit('fileUploaded', file)
            if (self.uploadedFiles.length === self.files.length) {
              self.$emit('allFilesUploaded', self.uploadedFiles)
            }
          } else {
            file.error = this.response
          }
        }
      })
      xhr.upload.addEventListener('progress', function (progress) {
        file.uploadProgress = progress.loaded / progress.total * 100
      })
      xhr.upload.addEventListener('error', function (error) {
        file.error = error
      })
      xhr.open('POST', data.url)
      xhr.setRequestHeader('cache-control', 'no-cache')
      await xhr.send(form)
    }
  }
}
</script>

<style scoped>
  .jbtn-file {
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .jbtn-file input[type=file] {
    position: absolute;
    top: 0;
    right: 0;
    min-width: 100%;
    min-height: 100%;
    text-align: right;
    filter: alpha(opacity=0);
    opacity: 0;
    outline: none;
    cursor: inherit;
    display: block;
  }
</style>
