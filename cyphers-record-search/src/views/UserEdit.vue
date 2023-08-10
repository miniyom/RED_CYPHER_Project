<template>
  <div>
    <b-form>
      <b-form-input
          v-if="!isEdit"
          v-model="user.userId"
          placeholder="Enter user ID"
      ></b-form-input>
      <b-form-input
          v-if="isEdit"
          v-model="user.nickname"
          placeholder="Enter nickname"
      ></b-form-input>
      <b-button @click="confirm" variant="success">save</b-button>
      <b-button class="m-2" @click="goToList" variant="info">list</b-button>
      <b-button v-if="isEdit" @click="deleteUser" variant="danger">Delete</b-button>
    </b-form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  props: {
    userId: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      user: {
        userId: '',
        nickname: ''
      },
      isEdit: false
    }
  },
  methods: {
    confirm() {
      if (this.isEdit) {
        axios.get(`/api/user/update/${this.user.userId}/${this.user.nickname}`)
            .then(response => {
              console.log('res =', response);
              // After handling the response, redirect to the list page
              this.$router.push('/user/list');
            });
      } else {
        axios.get(`/api/user/add/${this.user.userId}`)
            .then(response => {
              console.log('res =', response);
              // After handling the response, redirect to the list page
              this.$router.push('/user/list');
            });
      }
    },
    deleteUser() {
      axios.get(`/api/user/remove/${this.user.userId}`)
          .then(response => {
            console.log('Delete Response:', response);
            this.$router.push('/user/list');  // 삭제 후 목록 페이지로 리다이렉트
          });
    },
    goToList() {
      this.$router.push('/user/list');
    },
  },
  mounted() {
    if (this.userId) {
      this.isEdit = true;
      axios.get(`/api/user/get/${this.userId}`).then(response => {
        this.user = response.data;
      });
    }
  }
}
</script>
