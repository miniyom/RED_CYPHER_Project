<template>
  <div>
    <b-button variant="primary" @click="goToAddUser">Add User</b-button>
    <b-table :items="users" :fields="fields">
      <template #cell(actions)="data">
        <b-button @click="goToEditUser(data.item.userId)">Edit</b-button>
      </template>
    </b-table>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      fields: [
        'userId',
        'nickname',
        'actions',
      ],
      users: [] // You would typically populate this from an API call
    }
  },
  methods: {
    goToAddUser() {
      this.$router.push('/user/add')
    },
    goToEditUser(userId) {
      this.$router.push(`/user/edit/${userId}`)
    }
  },
  mounted() {
    axios.get('/api/user/all').then(response => {
      console.log('response = ', response)
      this.users = response.data;
      console.log('users = ', this.users)
    });
  }
}
</script>
