<template>
  <div>
    <b-form @submit.prevent="handleSubmit">
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
      <b-button type="submit">Submit</b-button>
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
    handleSubmit() {
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
    }
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
