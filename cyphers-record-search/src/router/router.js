import Vue from 'vue'
import VueRouter from 'vue-router'
import Main from '@/views/cyphers/MainView.vue'
import UserList from '@/views/UserList.vue'
import UserEdit from '@/views/UserEdit.vue'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Main',
        component: Main
    },
    {
        path: '/user/list',
        name: 'UserList',
        component: UserList
    },
    {
        path: '/user/edit/:userId',
        name: 'UserEdit',
        component: UserEdit,
        props: true
    },
    {
        path: '/user/add',
        name: 'UserAdd',
        component: UserEdit
    }
]

const router = new VueRouter({
    mode: 'hash',
    base: '/vue/html', // 이 부분을 추가하세요.
    routes
})

export default router
