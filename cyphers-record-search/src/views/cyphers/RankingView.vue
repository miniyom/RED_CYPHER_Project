<template>
    <div>
        <Header/>
        <b-container class="mt-3 mt-5 text-left">
            <h2>공식전 통합 랭킹</h2>
        </b-container>
        <b-container>
            <b-row class="my-5 pl-3">
                <b-col cols="3" class="container-box mx-3">
                    <b-row><h5>Legend 커트 라인</h5></b-row>
                    <b-row class="align-items-center">
                        <b-col><b-img src="/img/legend2.png" class="tier-image"></b-img></b-col>
                        <b-col><h2>3000 RP</h2></b-col>
                    </b-row>
                    <b-row class="px-4 pt-3">
                        <b-col>
                            <b-row>전일 대비 변화량</b-row>
                            <b-row>상위</b-row>
                            <b-row>플레이어 수</b-row>
                        </b-col>
                        <b-col class="d-flex flex-column">
                            <b-row class="align-self-end">+10</b-row>
                            <b-row class="align-self-end">0.1%</b-row>
                            <b-row class="align-self-end">50</b-row>
                        </b-col>
                    </b-row>
                </b-col>
                <b-col cols="3" class="container-box mx-3">
                    <b-row><h5>Hero 커트 라인</h5></b-row>
                    <b-row class="align-items-center">
                        <b-col class="justify-content-center"><b-img src="/img/hero.png" class="tier-image"></b-img></b-col>
                        <b-col><h2>2500 RP</h2></b-col>
                    </b-row>
                    <b-row class="px-4 pt-3">
                        <b-col>
                            <b-row>전일 대비 변화량</b-row>
                            <b-row>상위</b-row>
                            <b-row>플레이어 수</b-row>
                        </b-col>
                        <b-col class="d-flex flex-column">
                            <b-row class="align-self-end">+10</b-row>
                            <b-row class="align-self-end">0.1%</b-row>
                            <b-row class="align-self-end">50</b-row>
                        </b-col>
                    </b-row>
                </b-col>
            </b-row>
        </b-container>
        <b-container class="my-5">
            <!-- <b-list-group-item v-if="this.players.length < 2">
                랭킹이 존재하지 않습니다.
            </b-list-group-item> -->
            <b-row>
                <b-col>
                    <b-table class="custom-table" hover bordered :items="players" :fields="fields" :row-variant="rowVariant" >

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
            { key: 'rank', label: '랭크'},
            { key: 'name', label: '닉네임' },
            { key: 'grade', label: '급수' },
            { key: 'tier', label: '티어' },
            { key: 'ratingPoint', label: 'RP' }
        ],
    };
},
methods: {
    rowVariant(item, index) {
        return index % 2 === 0 ? 'even-row' : 'odd-row';
    }
}
};
</script>
  
<style scoped>
.custom-table {
  border: 2px solid #4682B4; /* 하늘색 테두리 */
}

.even-row {
  background-color: #87CEFA; /* 짝수 행 배경색 */
}

.odd-row {
  background-color: #add8e6; /* 홀수 행 배경색 */
}

.even-row:hover, .odd-row:hover {
  background-color: #4682B4; /* 마우스를 올렸을 때 어두운 하늘색 */
}

.text-left {
  text-align: left;
}

.container-box {
  border: 2px solid #87CEFA;
  box-sizing: border-box;
  border-radius: 10px; /* 둥근 테두리 적용 */
  padding: 15px; /* 내부 패딩을 조금 추가해줬습니다. */
}

.tier-image {
    width: 100px;
}
</style>