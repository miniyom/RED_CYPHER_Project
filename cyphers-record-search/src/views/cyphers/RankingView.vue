<template>
    <div>
        <Header/>
        <b-container class="container-box my-3 mt-5 text-left">
            <h2>공식전 통합 랭킹</h2>
        </b-container>
        <b-container>
            <!-- <b-list-group-item v-if="this.players.length < 2">
                랭킹이 존재하지 않습니다.
            </b-list-group-item> -->
            <b-row>
                <b-col>
                    <b-table striped striped-rows="true" striped-rows-class="custom-stripes" hover bordered :items="players" :fields="fields">
                        <!-- 필드 커스터마이징 또는 슬롯을 이용한 추가적인 컨텐츠 표시 가능 -->
                    </b-table>
                </b-col>
            </b-row>
        </b-container>
    </div>
</template>
  
<script>
import Header from "./HeaderComponent.vue";

export default {
components: {
    Header
},
data() {
    return {
        players: [
            { id: 1, name: '플레이어 1', rank: 'Diamond I' },
            { id: 2, name: '플레이어 2', rank: 'Platinum II' },
            { id: 3, name: '플레이어 3', rank: 'Gold III' },
            { id: 3, name: '플레이어 3', rank: 'Gold III' },
            { id: 3, name: '플레이어 3', rank: 'Gold III' },
            { id: 3, name: '플레이어 3', rank: 'Gold III' },
            { id: 3, name: '플레이어 3', rank: 'Gold III' },
            { id: 3, name: '플레이어 3', rank: 'Gold III' },
            { id: 3, name: '플레이어 3', rank: 'Gold III' },
            { id: 3, name: '플레이어 3', rank: 'Gold III' },
            // 데이터는 실제로 API 호출 등으로 받아와야 함
        ],
        fields: [
            { key: 'rank', label: '랭크' },
            { key: 'name', label: '닉네임' },
            { key: 'grade', label: '급수' },
            { key: 'tier', label: '티어' },
            { key: 'ratingPoint', label: 'RP' }
        ]
    };
},
methods: {

}
};
</script>
  
<style scoped>
.custom-stripes {
  background-color: #add8e6; /* 하늘색 */
}
.text-left {
  text-align: left;
}

</style>