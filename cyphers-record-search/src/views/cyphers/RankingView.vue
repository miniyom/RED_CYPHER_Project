<template>
    <div>
        <Header/>
        <b-container class="mt-3 mt-5 text-left">
            <h2>공식전 통합 랭킹</h2>
            <p class="p-font">플레이어를 클릭해서 모스트 픽을 확인해보세요!</p>
        </b-container>
        <b-container>
            <b-row class="mt-5 mb-3 pl-3">
                <b-col cols="3" class="container-box mx-3">
                    <b-row><h5>Legend 커트 라인</h5></b-row>
                    <b-row class="align-items-center">
                        <b-col><b-img src="/img/legend2.png" class="tier-image"></b-img></b-col>
                        <b-col><h2 :style="{ whiteSpace: 'pre-line' }">{{ this.legendCutPoint === 0 ? 'No\nLegends' : (this.legendCutPoint+"RP") }}</h2></b-col>
                    </b-row>
                    <b-row class="px-4 pt-3">
                        <b-col>
                            <b-row>전체 능력자</b-row>
                            <b-row>상위</b-row>
                            <b-row>능력자 수</b-row>
                        </b-col>
                        <b-col class="d-flex flex-column">
                            <b-row class="align-self-end">{{ this.totalRankerNum }}명</b-row>
                            <b-row class="align-self-end">{{ (this.legendCutPoint / this.totalRankerNum).toFixed(2) }}%</b-row>
                            <b-row class="align-self-end">{{ this.legendPlayerNum }}명</b-row>
                        </b-col>
                    </b-row>
                </b-col>
                <b-col cols="3" class="container-box mx-3">
                    <b-row><h5>Hero 커트 라인</h5></b-row>
                    <b-row class="align-items-center">
                        <b-col class="justify-content-center"><b-img src="/img/hero.png" class="tier-image"></b-img></b-col>
                        <b-col><h2 :style="{ whiteSpace: 'pre-line' }">{{ this.heroCutPoint === 0 ? 'No\nHeros' : (this.heroCutPoint+"RP") }}</h2></b-col>
                    </b-row>
                    <b-row class="px-4 pt-3">
                        <b-col>
                            <b-row>전체 능력자</b-row>
                            <b-row>상위</b-row>
                            <b-row>능력자 수</b-row>
                        </b-col>
                        <b-col class="d-flex flex-column">
                            <b-row class="align-self-end">{{ this.totalRankerNum }}명</b-row>
                            <b-row class="align-self-end">{{ (this.heroCutPoint / this.totalRankerNum).toFixed(2) }}%</b-row>
                            <b-row class="align-self-end">{{ this.heroPlayerNum}}명</b-row>
                        </b-col>
                    </b-row>
                </b-col>
            </b-row>
            <b-row>
                <p class="p-font text-left">전체 능력자는 배치고사를 마친 능력자를 집계한 수치입니다.</p>
            </b-row>
        </b-container>
        <!-- <b-container class="my-5">
            <b-list-group-item v-if="this.players.length < 2">
                랭킹이 존재하지 않습니다.
            </b-list-group-item>
            <b-table :items="players" :fields="fields" bordered striped hover responsive class="custom-table"></b-table>
        </b-container> -->
        <b-container class="mt-4 mb-5">
            <!-- <b-list-group-item v-if="this.players.length < 2">
            랭킹이 존재하지 않습니다.
            </b-list-group-item> -->
            <div class="ranking-table">
                <table class="custom-table">
                    <thead>
                        <tr>
                            <th :style="{width:'15%'}">랭크</th>
                            <th>닉네임</th>
                            <th>급수</th>
                            <th>티어</th>
                            <th>RP</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(player, index) in players" :key="index">
                            <td>
                                <div class="row">
                                    <div class="col-md-6 font-bold">{{ player.rank }}</div>
                                    <div v-if="player.zeroDifference" class="col-md-6 " style="vertical-align: middle; display: flex; align-items: center;">
                                        <div class="bar" :style="{width: '15px'}"></div>
                                    </div>
                                    <div v-else class="col-md-6" :style="{ color: player.textColor, fontSize: '15px'}">{{ player.sign }}{{ player.difference }}</div>
                                </div>
                            </td>
                            <td>{{ player.nickname }}</td>
                            <td>{{ player.grade }}급</td>
                            <td>{{ player.tier }}</td>
                            <td>{{ player.ratingPoint }}</td>
                        </tr>
                    </tbody>
                </table>
                <!-- 페이징 버튼 -->
                
                <!-- 페이지 넘버 버튼 -->
                <div class="pagination">
                    <!-- 이전 5개 페이지로 이동하는 버튼 -->
                    <button @click="moveBackwardPages" :disabled="currentPage <= 5" class="pagination-btn"> &lt;&lt; </button>

                    <!-- 이전 페이지로 이동하는 버튼 -->
                    <button @click="prevPage" :disabled="currentPage === 1" class="pagination-btn"> &lt; </button>

                    <!-- 페이지 넘버 버튼들 -->
                    <button v-for="pageNumber in displayedPageNumbers"
                            :key="pageNumber"
                            @click="goToPage(pageNumber)"
                            :class="{ 'active': pageNumber === currentPage }"
                            class="pagination-btn">{{ pageNumber }}</button>

                    <!-- 다음 페이지로 이동하는 버튼 -->
                    <button @click="nextPage" :disabled="currentPage === totalPages" class="pagination-btn">></button>

                    <!-- 다음 5개 페이지로 이동하는 버튼 -->
                    <button @click="moveForwardPages" :disabled="currentPage + 5 > totalPages" class="pagination-btn">>></button>
                </div>
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
            legendCutPoint: '',
            heroCutPoint: '',
            legendPlayerNum: 0,
            heroPlayerNum: 0,
            totalRankerNum: 0,
            
            players: [
                { 
                    nickname: '플레이어 1', 
                    rank: 1, 
                    beforeRank: 0,
                    grade: 0, 
                    tier: 'Legend', 
                    ratingPoint: 0,

                    textColor: '', // 폰트 색상을 동적으로 설정할 변수
                    difference: 0, // rank - beforeRank의 값
                    zeroDifference: false,
                    sign: '', // 부호를 추가할 변수
                }
            ],
            fields: [
                { key: 'rank', label: '랭크'},
                { key: 'nickname', label: '닉네임' },
                { key: 'grade', label: '급수' },
                { key: 'tier', label: '티어' },
                { key: 'ratingPoint', label: 'RP' }
            ],

            totalPlayers: 0,
            pageSize: 50,
            currentPage: 1
        };
    },
    computed: {
        totalPages() {
            return Math.ceil(this.totalRankerNum / this.pageSize);
        },
        displayedPlayers() {
            const startIndex = (this.currentPage - 1) * this.pageSize;
            const endIndex = startIndex + this.pageSize;
            return this.players.slice(startIndex, endIndex);
        },
        displayedPageNumbers() {
            const maxPageButtons = 10; // 최대 페이지 버튼 수
            let startPage = Math.max(1, this.currentPage - 4); // 현재 페이지를 중심으로 앞으로 4개의 페이지까지 표시
            const endPage = Math.min(this.totalPages, startPage + maxPageButtons - 1); // 현재 페이지를 중심으로 최대 10개까지 표시

            if (endPage - startPage < maxPageButtons - 1) {
                startPage = Math.max(1, endPage - maxPageButtons + 1);
            }

            return Array(endPage - startPage + 1).fill().map((_, index) => startPage + index);
        }
    },
    methods: {
        getLegendCut() {
            let lastLegendIndex = 0;
            if (this.players[0].tier !== 'Legend') {
                return 0;
            }
            // 레전드 티어는 30위까지
            for (let index = 0; index <= 30; index++) {
                if (this.players[index].tier === 'Legend') {
                    continue;
                } else {
                    lastLegendIndex = index - 1;
                    this.legendPlayerNum = index;
                    break;
                }
            }
            return this.players[lastLegendIndex].ratingPoint;
        },
        getHeroCut() {
            let lastHeroIndex = 0;
            if (this.players[30].tier !== 'Hero') {
                return 0;
            }
            // 히어로 티어는 300위까지
            for (let index = 30; index <= 300; index++) {
                if (this.players[index].tier === 'Hero') {
                    continue;
                } else {
                    lastHeroIndex = index - 1;
                    this.heroPlayerNum = index;
                    break;
                }
            }
            return this.players[lastHeroIndex].ratingPoint;
        },
        fetchTotalRanker() {
            axios.get(`/api/ranking/player/total-num`)
                .then((response) => {
                    this.totalRankerNum = response.data;
                    console.log("전체 랭커 수: "+this.totalRankerNum);
                })
                .catch((error) => {
                    alert("랭커를 계산하는데에 오류가 발생했습니다.", error);
                    console.log("error: ", error);
                    this.$router.push('/'); 
                });
        },
        // rank - beforeRank의 값을 계산하고, 색상 및 부호를 설정하는 메서드
        calculateDifference() {
            this.players.forEach(player => {
                player.difference = Math.abs(player.rank - player.beforeRank); // 차이값을 절댓값으로 계산
                if (player.beforeRank === 0) {
                    player.difference = 'new';
                    player.textColor = '#1CA484';
                    player.sign = '▲';
                } else if (player.rank < player.beforeRank) {
                    player.textColor = '#1CA484';
                    player.sign = '▲';
                } else if (player.rank > player.beforeRank){
                    player.textColor = '#E96767';
                    player.sign = '▼';
                } else {
                    player.difference = '';
                    player.zeroDifference = true;
                }
            });
        },
        fetchRankers() {
            const startIndex = (this.currentPage - 1) * this.pageSize;
            const endIndex = startIndex + this.pageSize;
            axios.get(`/api/ranking/player/list/${startIndex}/${endIndex}`)
                .then(response => {
                    this.players = response.data;

                    this.legendCutPoint = this.getLegendCut();
                    this.heroCutPoint = this.getHeroCut();
                    this.calculateDifference();
                })
                .catch(error => {
                    alert("서버에 오류가 발생했습니다.", error);
                    console.error('Error fetching players:', error);
                    this.$router.push('/'); 
                    throw error;
                });
        },
        nextPage() {
            if (this.currentPage < this.totalPages) {
                this.currentPage++;
                this.fetchRankers();
            }
        },
        prevPage() {
            if (this.currentPage > 1) {
                this.currentPage--;
                this.fetchRankers();
            }
        },
        moveForwardPages() {
            if (this.currentPage < this.totalPages) {
                this.currentPage += 5;
                this.fetchRankers();
            }
        },
        moveBackwardPages() {
            if (this.currentPage > 1) {
                this.currentPage -= 5;
                this.fetchRankers();
            }
        },
        goToPage(pageNumber) {
            this.currentPage = pageNumber;
            this.fetchRankers();
        }
    },
    mounted() {
        this.fetchTotalRanker();
        this.fetchRankers();
    }
}
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
    border-right: 1px solid #A5BED2;
    background-color: #DDEBE2;
}

.custom-table tbody tr:nth-child(odd) {
    background-color: #F5F7FB;
}

.custom-table tbody tr:hover {
    background-color: #E0E6F5;
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

.font-bold {
    font-weight: bold;
}
.bar{
    height: 3px;
    background-color: rgb(49, 49, 49);
    opacity: 0.6;
    vertical-align: middle;
    margin: auto;
}

.pagination {
  margin-top: 10px;
}

.pagination-btn {
  margin: 0 5px;
  padding: 5px 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #fff;
  color: #333;
  cursor: pointer;
  outline: none;
  transition: background-color 0.3s, color 0.3s;
}

.pagination-btn:hover {
  background-color: #f0f0f0;
}

.pagination-btn:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.pagination-btn.active {
  background-color: #007bff;
  color: #fff;
}

</style>