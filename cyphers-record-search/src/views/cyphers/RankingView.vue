<template>
    <div>
        <Header/>
        <HeaderRankSearch/>
        <b-container class="mt-3 mt-5 text-left">
            <h2>공식전 통합 랭킹</h2>
            <p class="p-font">플레이어를 클릭해서 모스트 픽을 확인해보세요!</p>
        </b-container>
        <b-container>
            <b-row class="mt-5 mb-3 pl-3 d-flex justifty-content-between">
                <b-col class="shadow container-box mx-4">
                    <b-row><h5>Legend 커트 라인</h5></b-row>
                    <b-row class="align-items-center">
                        <b-col><b-img src="/img/legend.png" class="tier-image"></b-img></b-col>
                        <b-col><h2 :style="{ whiteSpace: 'pre-line' }">{{ legendCutPoint === 0 ? 'No\nLegends' : (legendCutPoint+"RP") }}</h2></b-col>
                    </b-row>
                    <b-row class="px-4 pt-3">
                        <b-col>
                            <b-row>전체 능력자</b-row>
                            <b-row>상위</b-row>
                            <b-row>능력자 수</b-row>
                        </b-col>
                        <b-col class="d-flex flex-column">
                            <b-row class="align-self-end">{{ totalRankerNum }}명</b-row>
                            <b-row class="align-self-end">{{ (legendCutPoint / totalRankerNum).toFixed(2) }}%</b-row>
                            <b-row class="align-self-end">{{ legendPlayerNum }}명</b-row>
                        </b-col>
                    </b-row>
                </b-col>
                <b-col class="shadow container-box mx-4">
                    <b-row><h5>Hero 커트 라인</h5></b-row>
                    <b-row class="align-items-center">
                        <b-col class="justify-content-center"><b-img src="/img/hero.png" class="tier-image"></b-img></b-col>
                        <b-col><h2 :style="{ whiteSpace: 'pre-line' }">{{ heroCutPoint === 0 ? 'No\nHeros' : (heroCutPoint+"RP") }}</h2></b-col>
                    </b-row>
                    <b-row class="px-4 pt-3">
                        <b-col>
                            <b-row>전체 능력자</b-row>
                            <b-row>상위</b-row>
                            <b-row>능력자 수</b-row>
                        </b-col>
                        <b-col class="d-flex flex-column">
                            <b-row class="align-self-end">{{ totalRankerNum }}명</b-row>
                            <b-row class="align-self-end">{{ ((heroCutPoint !== 0 ? legendCutPoint+heroCutPoint : 0) / totalRankerNum).toFixed(2) }}%</b-row>
                            <b-row class="align-self-end">{{ heroPlayerNum}}명</b-row>
                        </b-col>
                    </b-row>
                </b-col>
                <b-col class="shadow container-box mx-4">
                    <b-row>
                        <h3 class="mt-3 font-bold">랭킹 산정 기준</h3>
                        <ul class="p-3 m-2 text-left">
                            <li class="m-2">30급 이상/공식전 10판 이상 참여</li>
                            <li class="m-2">중단 게임 수 10% 이하</li>
                            <li class="m-2">일주일에 3게임 이상 참여</li>
                        </ul>
                    </b-row>
                </b-col>
            </b-row>
            <b-row>
                <p class="p-font text-left">전체 능력자는 배치고사를 마친 능력자를 집계한 수치입니다.</p>
            </b-row>
        </b-container>
        <b-container v-if="players.length < 2" class=" my-4 container-box">
            랭킹이 존재하지 않습니다.
        </b-container>
        <b-container v-else class="mt-4 mb-5">
            <div class="ranking-table">
                <table class="custom-table" id="my-table">
                    <thead>
                        <tr>
                            <th :style="{width:'15%'}">랭크</th>
                            <th>닉네임</th>
                            <th>급수</th>
                            <th>티어</th>
                            <th>RP</th>
                        </tr>
                    </thead>
                    <tbody v-if="$route.params.type === 'all_list'">
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
                            <td>
                                <b-link class="custom-link" 
                                        @click="forwardDetail(player.nickname)" 
                                        @mouseover="hovered = true" 
                                        @mouseleave="hovered = false">
                                    <div class="ml-3">{{ player.nickname }}</div>
                                </b-link>
                            </td>
                            <td>{{ player.grade }}급</td>
                            <td>{{ player.tier }}</td>
                            <td>{{ player.ratingPoint }}</td>
                        </tr>
                    </tbody>
                    <tbody v-else>
                        <tr>
                            <td>
                                <div class="row">
                                    <div class="col-md-6 font-bold">{{ singlePlayer.rank }}</div>
                                    <div v-if="singlePlayer.zeroDifference" class="col-md-6 " style="vertical-align: middle; display: flex; align-items: center;">
                                        <div class="bar" :style="{width: '15px'}"></div>
                                    </div>
                                    <div v-else class="col-md-6" :style="{ color: singlePlayer.textColor, fontSize: '15px'}">{{ singlePlayer.sign }}{{ singlePlayer.difference }}</div>
                                </div>
                            </td>
                            <td>
                                <b-link class="custom-link" 
                                        @click="forwardDetail(singlePlayer.nickname)" 
                                        @mouseover="hovered = true" 
                                        @mouseleave="hovered = false">
                                    <div class="ml-3">{{ singlePlayer.nickname }}</div>
                                </b-link>
                            </td>
                            <td>{{ singlePlayer.grade }}급</td>
                            <td>{{ singlePlayer.tier }}</td>
                            <td>{{ singlePlayer.ratingPoint }}</td>
                        </tr>
                    </tbody>
                </table>
                
                <div class="pagination" v-if="$route.params.type === 'all_list'">

                    <button @click="goToPage(1)" class="pagination-btn"> First </button>
                    <button @click="moveBackwardPages" class="pagination-btn"> ≪ </button>
                    <button @click="prevPage" :disabled="currentPage === 1" class="pagination-btn"> &lt; </button>
                    <button v-for="pageNumber in displayedPageNumbers"
                            :key="pageNumber"
                            @click="goToPage(pageNumber)"
                            :class="{ 'active': pageNumber === currentPage }"
                            class="pagination-btn">{{ pageNumber }}</button>
                    <button @click="nextPage" :disabled="currentPage === totalPages" class="pagination-btn">></button>
                    <button @click="moveForwardPages" class="pagination-btn">≫</button>
                    <button @click="goToPage(totalPages)" class="pagination-btn"> Last </button>

                </div>
                <!-- <div class="my-5">
                    <b-pagination
                        v-model="currentPage"
                        :total-rows="totalRankerNum"
                        :per-page="pageSize"
                        first-text="⏮"
                        prev-text="⏪"
                        next-text="⏩"
                        last-text="⏭"
                        class="mt-4"
                        size="lg"
                    ></b-pagination>
                </div> -->

            </div>
        </b-container>
    </div>
</template>
  
<script>
import Header from "@/mycomponents/HeaderComponent.vue";
import HeaderRankSearch from "@/mycomponents/HeaderRankingSearch.vue";
import axios from "axios";
// import { BPagination } from 'bootstrap-vue'

export default {
    components: {
        Header,
        HeaderRankSearch,
        // BPagination
    },
    data() {
        return {
            isInputFocused: false,
            searchText: '',
            searchData: [],

            legendCutPoint: '',
            heroCutPoint: '',
            legendPlayerNum: 0,
            heroPlayerNum: 0,
            totalRankerNum: 0,
            
            singlePlayer: {
                nickname: '', 
                rank: 0, 
                beforeRank: 0,
                grade: 0, 
                tier: '', 
                ratingPoint: 0,

                textColor: '', 
                difference: 0, 
                zeroDifference: false,
                sign: '', 
            },
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

            pageSize: 50,
            currentPage: 1
        };
    },
    computed: {
        totalPages() {
            return Math.ceil(this.totalRankerNum / this.pageSize);
        },
        displayedPageNumbers() {
            const maxPageButtons = 10; // 최대 페이지 버튼 수
            let startPage = 1; 
            if (this.currentPage % 10 === 0) {
                startPage = this.currentPage - maxPageButtons + 1
            } else if (this.currentPage > 10) {
                startPage = this.currentPage - (this.currentPage % 10) + 1
            } else {
                startPage = 1;
            }
            const endPage = Math.min(this.totalPages, startPage + maxPageButtons - 1);

            return Array(endPage - startPage + 1).fill().map((_, index) => startPage + index);
        }
    },
    watch: {
        currentPage(newPage, oldPage) {
            // currentPage 값이 변경될 때마다 fetchData 함수 실행
            if (newPage !== oldPage) {
                this.fetchRankers();
            }
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
            if (this.players[29].tier !== 'Legend') {
                return 2800;
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
            if (this.players[129].tier !== 'Hero') {
                return 2800;
            }
            return this.players[lastHeroIndex].ratingPoint;
        },
        fetchTotalRanker() {
            axios.get(`/api/ranking/player/total-num`)
                .then((response) => {
                    this.totalRankerNum = response.data;
                    this.legendCutPoint = this.getLegendCut();
                    this.heroCutPoint = this.getHeroCut();
                })
                .catch((error) => {
                    alert("서버에 오류가 발생했습니다.", error);
                    console.log("error: ", error);
                    this.$router.push('/'); 
                });
        },
        fetchRankers() {
            const offset = (this.currentPage - 1) * this.pageSize;
            const limit = this.pageSize;
            axios.get(`/api/ranking/player/list/${offset}/${limit}`)
                .then(response => {

                    this.players = response.data;
                    this.calcListDifference();
                    this.scrollTop();
                })
                .catch(error => {
                    alert("랭킹 리스트를 불러올 수 없습니다.", error);
                    console.error('Error fetching players:', error);
                });
        },
        fetchSingleRanker() {
            axios.get(`/api/ranking/player/search/${this.$route.params.type}`)
                .then(response => {
                    this.singlePlayer = response.data;
                    this.calcSingleDifference();
                    this.scrollTop();
                })
                .catch(error => {
                    alert("랭킹 정보를 불러올 수 없습니다.", error);
                    console.error('Error fetching Single player:', error);
                    this.$router.push('/ranking/all_list'); 
                });
        },
        forwardDetail(playerNickname) {
            this.$router.push({ name: 'RecordDetail', params: { nickname: playerNickname }});
        },



        // rank - beforeRank의 값을 계산하고, 색상 및 부호를 설정하는 메서드
        calcListDifference() {
            this.players.forEach(player => {
                this.designFluctuations(player);
            });
        },
        calcSingleDifference() {
            this.designFluctuations(this.singlePlayer);
        },
        designFluctuations(player) {
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
            if (this.totalPages - this.currentPage < 10) {
                this.currentPage = this.totalPages;
                this.fetchRankers();
            } else if (this.currentPage < this.totalPages) {
                this.currentPage += 10;
                this.fetchRankers();
            }
        },
        moveBackwardPages() {
            if (this.currentPage < 10) {
                this.currentPage = 1;
                this.fetchRankers();
            } else if (this.currentPage > 1) {
                this.currentPage -= 10;
                this.fetchRankers();
            }
        },
        goToPage(pageNumber) {
            this.currentPage = pageNumber;
            this.fetchRankers();
        },
        scrollTop() {
            window.scrollTo({
                top: 0,
                behavior: 'smooth' // 부드러운 스크롤
            });
        },



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
        fetchData() {
            axios.get(`/api/search/auto-complete/${this.searchText}`)
                .then(response => {
                this.searchData = [];
                    for (let index = 0; index < response.data.length; index++) {
                        this.searchData.push({'text': response.data[index], 'id' : index});
                    }
                });
        },
    },
    mounted() {
        this.fetchTotalRanker();
        this.fetchRankers();
        if (this.$route.params.type !== 'all_list') {
            this.fetchSingleRanker();
        }
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
    background-color: #313131;
    opacity: 0.6;
    vertical-align: middle;
    margin: auto;
}

.pagination {
    margin-top: 40px;
    display: flex;
    justify-content: center; /* 가로 정렬을 중앙에 맞춤 */
    align-items: center; /* 세로 정렬을 중앙에 맞춤 */
}

.pagination-btn {
  display: inline-block; 
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
  background-color: #6c757d;
  color: #fff;
}
.custom-link {
  color: #000000; /* 기본적으로 회색 */
  text-decoration: none; /* 밑줄 제거 */
}

.custom-link:hover {
  color: #343a40; /* 마우스를 올리면 글자색이 더 진한 색으로 변경 */
  text-decoration: underline; /* 밑줄 추가 */
}

</style>