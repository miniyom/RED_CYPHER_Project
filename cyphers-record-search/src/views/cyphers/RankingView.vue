<template>
    <div>
        <Header/>
        <b-container class="mt-3 mt-5 text-left">
            <h2>공식전 통합 랭킹</h2>
            <p class="p-font">플레이어를 클릭해서 모스트 픽을 확인해보세요!</p>
        </b-container>
        <b-container>
            <b-row class="my-5 pl-3">
                <b-col cols="3" class="container-box mx-3">
                    <b-row><h5>Legend 커트 라인</h5></b-row>
                    <b-row class="align-items-center">
                        <b-col><b-img src="/img/legend2.png" class="tier-image"></b-img></b-col>
                        <b-col><h2 :style="{ whiteSpace: 'pre-line' }">{{ legendCutPoint }}</h2></b-col>
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
                        <b-col><h2 :style="{ whiteSpace: 'pre-line' }">{{ heroCutPoint }}</h2></b-col>
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
        <!-- <b-container class="my-5">
            <b-list-group-item v-if="this.players.length < 2">
                랭킹이 존재하지 않습니다.
            </b-list-group-item>
            <b-table :items="players" :fields="fields" bordered striped hover responsive class="custom-table"></b-table>
        </b-container> -->
        <b-container class="my-5">
            <!-- <b-list-group-item v-if="this.players.length < 2">
            랭킹이 존재하지 않습니다.
            </b-list-group-item> -->
            <div class="ranking-table">
            <table class="custom-table">
                <thead>
                    <tr>
                        <th>랭크</th>
                        <th>닉네임</th>
                        <th>급수</th>
                        <th>티어</th>
                        <th>RP</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(player, index) in players" :key="index">
                        <td>{{ player.rank }}</td>
                        <td>{{ player.nickname }}</td>
                        <td>{{ player.grade }}</td>
                        <td>{{ player.tier }}</td>
                        <td>{{ player.ratingPoint }}</td>
                    </tr>
                </tbody>
            </table>
            </div>
        </b-container>
    </div>
</template>
  
<script>
import Header from "./HeaderComponent.vue";
import axios from "axios";

export default {
    components: {
        Header
    },
    data() {
        return {
            players: [
                { id: 1, name: '플레이어 1', rank: 'Diamond I', grade: 'A', tier: '1', ratingPoint: '2000' },
                { id: 2, name: '플레이어 2', rank: 'Platinum II', grade: 'B', tier: '2', ratingPoint: '1800' },
                { id: 3, name: '플레이어 3', rank: 'Gold III', grade: 'C', tier: '3', ratingPoint: '1600' },
                { id: 4, name: '플레이어 3', rank: 'Gold III', grade: 'C', tier: '3', ratingPoint: '1600' },
                { id: 5, name: '플레이어 3', rank: 'Gold III', grade: 'C', tier: '3', ratingPoint: '1600' },
                { id: 6, name: '플레이어 3', rank: 'Gold III', grade: 'C', tier: '3', ratingPoint: '1600' },
                // 데이터는 실제로 API 호출 등으로 받아와야 함
            ],
            fields: [
                { key: 'rank', label: '랭크'},
                { key: 'nickname', label: '닉네임' },
                { key: 'grade', label: '급수' },
                { key: 'tier', label: '티어' },
                { key: 'ratingPoint', label: 'RP' }
            ],
        };
    },
    computed: {
        legendCutPoint() {
            return this.getLegendCut();
        },
        heroCutPoint() {
            return this.getHeroCut();
        }
    },
    methods: {
        getLegendCut() {
            const lastLegendIndex = 0;
            if (this.players[0].tier !== 'Legend') {
                return 'No\nLegends'
            }
            // 레전드 티어는 30위까지
            for (let index = 0; index < 31; index++) {
                if (this.players[index].tier === 'Legend') {
                    continue;
                } else {
                    this.lastLegendIndex = index - 1;
                }
            }
            return this.players[lastLegendIndex].ratingPoint+"RP";
        },
        getHeroCut() {
            const lastHeroIndex = 0;
            if (this.players[30].tier !== 'Hero') {
                return 'No\nHeros'
            }
            // 히어로 티어는 30위까지
            for (let index = 0; index < 31; index++) {
                if (this.players[index].tier === 'Hero') {
                    continue;
                } else {
                    this.lastHeroIndex = index - 1;
                }
            }
            return this.players[lastHeroIndex].ratingPoint+"RP";
        }
    },
    mounted() {
        axios.get(`/api/ranking/player/0/50`)
            .then((response) => {
                this.players = response.data;
                console.log("30위의 정보: ", this.players[29]);
            })
            .catch((error) => {
                alert("서버에 오류가 발생했습니다.", error);
                console.log("error: ", error);
                this.$router.push('/'); 
            });
    }
};
</script>
  
<style scoped>
.ranking-table {
  font-family: 'Arial', sans-serif;
}

.custom-table {
    width: 100%;
    text-align: center;
    background-color: white;
    border-collapse: collapse;
    border: 2px solid #A5BED2;
    /* border-radius: .25rem; */
    overflow: hidden;
}

.custom-table th,
.custom-table td {
    padding: .75rem;
    border-top: 1px solid #A5BED2;
}

.custom-table th {
    text-align: center;
    border-bottom: 2px solid #A5BED2;
    border-right: 2px solid #A5BED2;
    background-color: #DDEBE2;
}

.custom-table tbody tr:nth-child(odd) {
    background-color: #F5F7FB;
}

.custom-table tbody tr:hover {
    background-color: #E0E6F5;
}

.colors {
    background-color: #87CEFA; /* 짝수 행 배경색 */
    background-color: #add8e6; /* 홀수 행 배경색 */
    background-color: #4682B4; /* 마우스를 올렸을 때 어두운 하늘색 */

    background-color: #A5BED2; /* 테두리 */
    background-color: #E0E6F5; /* 마우스 올렸을 때 */
    background-color: #F5F7FB; /* 짝수행 */
    background-color: #DDEBE2; /* 테이블헤드 */
}

.text-left {
    text-align: left;
}

.container-box {
    border: 2px solid #A5BED2;
    background-color: #F9F9FA;
    box-sizing: border-box;
    border-radius: 10px; /* 둥근 테두리 적용 */
    padding: 15px; /* 내부 패딩을 조금 추가해줬습니다. */
}
.p-font {
    color : #BDBDBD;
}

.tier-image {
    width: 100px;
}
</style>