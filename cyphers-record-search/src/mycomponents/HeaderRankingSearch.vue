<template>
    <div>
        <b-navbar type="light" variant="dark" class="justify-content-center">
            <b-form-input 
                class="mr-sm-2 w-50" 
                placeholder="능력자 랭킹을 검색해보세요"
                v-model="searchText"
                @input="handleInput"
                @focus="isInputFocused = true"
                @blur="handleBlur"
                @keyup.enter="searchRank"></b-form-input>
            <b-button 
                variant="success" 
                class="mb-2 my-sm-0 mx-3" 
                type="button"
                @click="searchRank">Search</b-button>
            <ul v-if="isInputFocused" class="list-group position-absolute custom-autoitem" style="top: 100%;">
                <div v-if="searchData.length > 0">
                    <li
                        class="list-group-item list-group-item-action"
                        v-for="item in searchData"
                        :key="item.id"
                        @mouseover="onHover"
                        @mouseleave="onLeave"
                        @mousedown="preventBlur = true"
                        @click="selectAutocompleteItem(item.text)">
                        {{ item.text }}
                    </li>
                </div>
                <div v-else>
                    <li class="list-group-item list-group-item-action">일치하는 닉네임이 없습니다.</li>
                </div>
            </ul>
        </b-navbar>
    </div>
</template>
  
<script>
import axios from "axios";

export default {
    data() {
        return {
            isInputFocused: false,
            searchText: '',
            searchData: [],
            searchNickname: '',
        }
    },
    methods: {
        handleBlur() {
            if (!this.preventBlur) {
                this.isInputFocused = false;
            }
        },
        onHover(event) {
            event.target.style.cursor = "pointer";
            event.target.style.backgroundColor = "#E0E6F5"; 
        },
        onLeave(event) {
            event.target.style.cursor = "default";
            event.target.style.backgroundColor = "white"; // hover 효과가 사라질 때 원래 상태로 되돌리기 위한 코드
        },
        selectAutocompleteItem(text) {
            this.searchText = text;
            this.isInputFocused = false;
        },
        handleInput() {
            if (this.searchText.length<2 || this.searchText.length>8) {
                this.searchData = [];
                return;
            } else {
                this.fetchData();
            }
        },
        fetchData() {
            axios.get(`/api/search/auto-complete/${this.searchText}`)
                .then(response => {
                this.searchData = [];
                    for (let index = 0; index < response.data.length; index++) {
                        this.searchData.push({'text': response.data[index], 'id' : index});
                    }
                });
        },
        searchRank() {
            axios.get(`/api/search/nickname/${this.searchText}`)
                .then(response => {
                    this.searchNickname = response.data;
                    this.$router.push({ name: 'Ranking', params: { type: this.searchText} });
                })
                .catch((error) => {
                    alert("닉네임 정보가 없습니다.", error);
                    console.log("오류내용: ", error);
                    this.$router.go();
                });
        },
    },
    mounted() {
    }
}

</script>

<style scoped>
.custom-autoitem {
    background-color: 'lightblue';
    border: 2px solid #A5BED2;
    border-radius: 10px;
    width: 55%
}
</style>