<template>
  <div>
    <!-- 최상단: Header -->
    <Header/>
    <HeaderSearch/>

    <!-- 프로필 박스 -->
    <b-container class="container-box my-3 mt-5">
      <b-row>
        <b-col sm="auto" class="p-1">
          <img :src="playerCharacterImage" alt="프로필 이미지" class="img-fluid rounded-circle profile-image">
        </b-col>
        <b-col class="pl-0 text-left align-self-end">
          <h2 class="mb-2">{{ this.playerNickname }}</h2>
          <b-button variant="primary" class="me-2">전적갱신</b-button>
          <span class="ml-2">최근 갱신: XX분 전</span>
        </b-col>
      </b-row>
    </b-container>

    <!-- 이동 탭 박스 -->
    <b-container class="my-3 container-box">
      <div class="d-flex justify-content-start">
        <b-button variant="primary" class="me-3">종합</b-button>
        <b-button variant="primary">캐릭터</b-button>
      </div>
    </b-container>

    <!-- 플레이어에 대한 상세 지표 박스 -->
    <b-container class="my-3 ">
      <!-- 모스트 사이퍼/모스트 포지션 탭 박스 -->
      <b-row>
        <b-col sm="7" class="container-box">
          <b-tabs>
            <b-tab title="모스트 사이퍼" active>
              <!-- 모스트 사이퍼 내용 -->
              <b-table :items="cypherData"></b-table>
            </b-tab>
            <b-tab title="모스트 포지션">
              <!-- 모스트 포지션 내용 -->
              <!-- 그래프 구현은 별도의 라이브러리를 사용해야 함 -->
              <b-col>
                <b-row>
                  <PieGraph style="height: 500px;" />
                </b-row>
                <b-row style="height: 100px;">
                  <b-col style="height: 100%;">
                    <b-row style="height: 50%; background-color: #b7d7ef; align-items: center; justify-content: center;">
                      <img src="/img/tanker.png" alt="탱커" style="height: 80%; width: auto; display: block;"/>
                      탱커
                    </b-row>
                    <b-row style="height: 50%; background-color: #ffecb3; align-items: center; justify-content: center;">
                      <img src="/img/supporter.png" alt="서포터" style="height: 80%; width: auto; display: block;"/>
                      서포터
                    </b-row>
                  </b-col>
                  <b-col style="height: 100%;">
                    <b-row style="height: 50%; background-color: #f5d7ee; align-items: center; justify-content: center;">
                      <img src="/img/ad.png" alt="원거리딜러" style="height: 80%; width: auto; display: block;"/>
                      원거리딜러
                    </b-row>
                    <b-row style="height: 50%; background-color: #ef9a9a; align-items: center; justify-content: center;">
                      <img src="/img/assassin.png" alt="근거리딜러" style="height: 80%; width: auto; display: block;"/>
                      근거리딜러
                    </b-row>
                  </b-col>
                </b-row>

              </b-col>
            </b-tab>
          </b-tabs>
        </b-col>

        <!-- 공식전 내용 -->
        <b-col sm="5" class="container-box">

          <b-container class="my-3">
            <b-row style="border-bottom: solid 1px #000;">
              <b-col sm="6">
                <h3>공식전</h3>
                <b-row>
                  <b-col cols="4" class="d-flex align-items-center justify-content-center">
                    <b-img src="https://img-api.neople.co.kr/cy/characters/c603a74ba02374026a535dc53e5b8d40?zoom=3" fluid alt="Tier Image"/>
                  </b-col>
                  <b-col cols="8">
                    <b-list-group flush>
                      <b-list-group-item>골드</b-list-group-item>
                      <b-list-group-item>20승 5패 2중단</b-list-group-item>
                      <b-list-group-item>승률: 80%</b-list-group-item>
                    </b-list-group>
                  </b-col>
                </b-row>
              </b-col>
              <b-col sm="6">
                <h3>일반전</h3>
                <b-list-group flush>
                  <b-list-group-item>20승 5패 2중단</b-list-group-item>
                  <b-list-group-item>승률: 80%</b-list-group-item>
                </b-list-group>
              </b-col>
            </b-row>

            <b-row style="height: 300px;">
              <LineGraph/>
            </b-row>

          </b-container>

        </b-col>
      </b-row>
    </b-container>

    <!-- 플레이어의 최근 플레이 지표 박스 -->
    <b-container class="my-3 container-box">
      <b-row>
        <b-col sm="2" class="br-1">
          <h4>게임 수</h4>
          <h2 class="mt-4">1</h2>
        </b-col>
        <b-col sm="2" class="br-1">
          <h4>승률</h4>
          <h2 class="mt-4">20%</h2>
        </b-col>
        <b-col sm="2" class="br-1">
          <h4>평균 KDA</h4>
          <h2 class="mt-4">3.24</h2>
        </b-col>
        <b-col sm="2" class="br-1">
          <h4>게임 점수</h4>
          <h2 class="mt-4">176</h2>
        </b-col>
        <b-col sm="4">
          <b-list-group flush>
            <b-list-group-item class="d-flex align-items-center py-2">
              <b-img src="https://img-api.neople.co.kr/cy/characters/c603a74ba02374026a535dc53e5b8d40?zoom=1" fluid alt="Character Image" class="mr-3"
                     style="width: 35px; height: 35px;"></b-img>
              <div class="flex-fill">캐릭터 이름</div>
              <div class="me-4">20승 5패</div>
              <div class="ml-3">KDA: 10/2/5</div>
            </b-list-group-item>
            <b-list-group-item class="d-flex align-items-center py-2">
              <b-img src="https://img-api.neople.co.kr/cy/characters/c603a74ba02374026a535dc53e5b8d40?zoom=1" fluid alt="Character Image" class="mr-3"
                     style="width: 35px; height: 35px;"></b-img>
              <div class="flex-fill">캐릭터 이름</div>
              <div class="me-4">20승 5패</div>
              <div class="ml-3">KDA: 10/2/5</div>
            </b-list-group-item>
            <b-list-group-item class="d-flex align-items-center py-2">
              <b-img src="https://img-api.neople.co.kr/cy/characters/c603a74ba02374026a535dc53e5b8d40?zoom=1" fluid alt="Character Image" class="mr-3"
                     style="width: 35px; height: 35px;"></b-img>
              <div class="flex-fill">캐릭터 이름</div>
              <div class="me-4">20승 5패</div>
              <div class="ml-3">KDA: 10/2/5</div>
            </b-list-group-item>
          </b-list-group>
        </b-col>
      </b-row>
    </b-container>

    <b-container class="my-3 container-box">
      <b-list-group>
        <!-- games 배열이 비어있을 때 메시지 표시 -->
        <b-list-group-item v-if="this.games.length < 2">
          게임 기록이 없습니다.
        </b-list-group-item>
        <!-- games 배열이 비어있지 않을 때 게임 리스트 표시 -->
        <template v-else>
          <b-list-group-item v-for="game in games" :key="game.id" class="p-1 text-left" :style="{ backgroundColor: backgroundColorByResult(game.result)}" style="color: #6E7474;">
            <b-row class="align-items-center justify-content-around" style="overflow: hidden;">
              <!-- 게임 타입 -->
              <b-col sm="1" class="ml-3 pe-2 fs-5 d-flex justify-content-center">
                <div class="flex-wrap">
                  <b-row>{{ game.type }}</b-row>
                  <b-row>{{ game.result }}</b-row>
                </div>
              </b-col>

              <!-- 캐릭터 이미지 -->
              <b-col sm="auto">
                <b-img :src="game.characterImage" rounded="circle" alt="Character" class="me-4" style="width: 80px;"></b-img>
              </b-col>

              <!-- 포지션 이미지 -->
              <b-col sm="auto">
                <b-img :src="game.positionImage" rounded="circle" alt="Position" class="me-4" style="width: 40px;"></b-img>
              </b-col>

              <!-- 특성 이미지 -->
              <b-col sm="auto" class=" pe-3">
                <b-row>
                  <b-img :src="game.traits[0]" rounded="circle" alt="Trait" class="m-0 p-0 me-1" style="width: 30px;"></b-img>
                  <b-img :src="game.traits[1]" rounded="circle" alt="Trait" class="m-0 p-0" style="width: 30px;"></b-img>
                </b-row>
                <b-row class="mt-1">
                  <b-img :src="game.traits[2]" rounded="circle" alt="Trait" class="m-0 p-0 me-1" style="width: 30px;"></b-img>
                  <b-img :src="game.traits[3]" rounded="circle" alt="Trait" class="m-0 p-0" style="width: 30px;"></b-img>
                </b-row>
              </b-col>

              <!-- 아이템 정보 -->
              <b-col sm="auto" class="me-2">
                <b-button @click="showItems(game.id)">아이템 보기</b-button>
              </b-col>


              <b-col sm="6">
                <b-row>
                  <!-- 게임 정보 -->
                  <b-col sm="3" class="d-flex align-items-center">
                    <div class="flex-wrap">
                      <div v-b-tooltip.hover title="킬 / 데스 / 어시 / 킬기여도">K / D / A / (KP)</div>
                      <div>{{ game.gameInfo.kills }} / {{ game.gameInfo.deaths }} / {{ game.gameInfo.assists }} ({{ game.gameInfo.participationRate }}%)</div>
                      <div>{{ game.gameInfo.kda == -1 ? "PERFECT" : game.gameInfo.kda}} KDA</div>
                      <div>{{ game.gameInfo.cs }} CS</div>
                    </div>
                  </b-col>
                  <!-- 상세정보1 -->
                  <b-col sm="4">
                    <b-row>
                      <b-col>
                        <div class="text-right">
                          <div v-b-tooltip.hover title="총 힐량">Heal</div>
                          <div v-b-tooltip.hover title="공격량">Attack</div>
                          <div v-b-tooltip.hover title="입은 피해량">Damaged</div>
                          <div v-b-tooltip.hover title="획득한 코인량">Coin</div>
                          <div v-b-tooltip.hover title="전투 참여">Battle</div>
                          <div v-b-tooltip.hover title="시야">Sight</div>
                        </div>
                      </b-col>
                      <b-col>
                        <div class="text-right">
                          <div>{{ formatNumber(game.details.heal) }}</div>
                          <div>{{ formatNumber(game.details.damage) }}</div>
                          <div>{{ formatNumber(game.details.takenDamage) }}</div>
                          <div>{{ formatNumber(game.details.coins) }}</div>
                          <div>{{ formatNumber(game.details.participation) }}</div>
                          <div>{{ formatNumber(game.details.vision) }}</div>
                        </div>
                      </b-col>
                    </b-row>
                  </b-col>
                  <!-- 함께한 플레이어 -->
                  <b-col sm="5">
                    <b-row>
                      <b-col col="6">
                        <div v-for="player in game.team1Players" :key="player.name" class="d-flex align-items-center mb-1">
                          <b-link class="custom-link" @click="forwardDetail(player.name)" @mouseover="hovered = true" @mouseleave="hovered = false">
                            <b-img class="rounded-image" :src="player.image" rounded alt="Player" style="width: 25px;"></b-img>
                          </b-link>
                          <b-link class="custom-link" @click="forwardDetail(player.name)" @mouseover="hovered = true" @mouseleave="hovered = false">
                            <div class="ml-2" style="font-size: 12px;">{{ shortenPlayerName(player.name) }}</div>
                          </b-link>
                        </div>
                      </b-col>
                      <b-col col="6">
                        <div v-for="player in game.team2Players" :key="player.name" class="d-flex align-items-center mb-1">
                          <b-link class="custom-link" @click="forwardDetail(player.name)" @mouseover="hovered = true" @mouseleave="hovered = false">
                            <b-img class="rounded-image" :src="player.image" rounded alt="Player" style="width: 25px;"></b-img>
                          </b-link>
                          <b-link class="custom-link" @click="forwardDetail(player.name)" @mouseover="hovered = true" @mouseleave="hovered = false">
                            <div class="ml-2" style="font-size: 12px;">{{ shortenPlayerName(player.name) }}</div>
                          </b-link>
                        </div>
                      </b-col>
                    </b-row>
                  </b-col>
                </b-row>
              </b-col>


            </b-row>
          </b-list-group-item>
        </template>
      </b-list-group>
    </b-container>

    <!-- Modal for Items -->
    <b-modal v-model="showItemModal" centered title="아이템 정보" hide-footer>
      <div v-if="currentGame">
        <!-- 첫 번째 줄 -->
        <div class="d-flex justify-content-between mb-2">
          <b-img
              v-for="item in currentGame.items.slice(0, 8)"
              :src="item"
              :alt="'Item ' + item"
              :key="item"
              class="flex-grow-1 pe-2"
              style="max-width: 12.5%;"
          ></b-img>
        </div>
        <!-- 두 번째 줄 -->
        <div class="d-flex justify-content-between">
          <b-img
              v-for="item in currentGame.items.slice(8, 16)"
              :src="item"
              :alt="'Item ' + item"
              :key="item"
              class="flex-grow-1 pe-2"
              style="max-width: 12.5%;"
          ></b-img>
        </div>
      </div>
    </b-modal>


  </div>
</template>

<script>
import axios from "axios";
import Header from "@/mycomponents/HeaderComponent.vue";
import HeaderSearch from "@/mycomponents/HeaderSearch.vue";
import LineGraph from "@/components/LineGraph";
import PieGraph from "@/components/PieGraph";

// import VueTooltip from 'v-tooltip';

export default {
  props: {
    nickname: String
  },
  created() {
    
  },
  components: {
    Header,
    HeaderSearch,
    LineGraph,
    PieGraph,
  },
  data() {
    return {
      playerNickname: this.$route.params.nickname,
      playerId: '',
      playerCharacterImage: '',
      activeTab: '모스트 사이퍼', // 예시
      // ... 나머지 데이터 구조
      cypherData: [
        {사이퍼: '사이퍼1', 승률: '75%', 게임횟수: '20'},
        {사이퍼: '사이퍼2', 승률: '60%', 게임횟수: '15'},
        {사이퍼: '사이퍼3', 승률: '55%', 게임횟수: '10'},
        {사이퍼: '사이퍼4', 승률: '80%', 게임횟수: '25'},
        {사이퍼: '사이퍼4', 승률: '80%', 게임횟수: '25'},
        {사이퍼: '사이퍼4', 승률: '80%', 게임횟수: '25'},
        {사이퍼: '사이퍼4', 승률: '80%', 게임횟수: '25'},
        {사이퍼: '사이퍼4', 승률: '80%', 게임횟수: '25'},
        {사이퍼: '사이퍼4', 승률: '80%', 게임횟수: '25'},
        {사이퍼: '사이퍼4', 승률: '80%', 게임횟수: '25'},
      ],
      games: [{
        id: 0,
        type: "공식전",
        result: "승리",
        characterImage: "https://placekitten.com/100/100",
        postionImage: "@/public/img/tanker.png",
        traits: [
          "https://placekitten.com/50/50",
          "https://placekitten.com/51/51",
          "https://placekitten.com/52/52",
          "https://placekitten.com/53/53"
        ],
        gameInfo: {
          kills: 10,
          deaths: 2,
          assists: 5,
          participationRate: 20,
          kda: 2.5,
          cs: 150
        },
        items: [
          "https://placekitten.com/80/80",
          "https://placekitten.com/81/81",
          "https://placekitten.com/82/82",
          "https://placekitten.com/83/83",
          "https://placekitten.com/84/84",
          "https://placekitten.com/85/85",
          "https://placekitten.com/86/86",
          "https://placekitten.com/87/87",
          "https://placekitten.com/88/88",
          "https://placekitten.com/89/89",
          "https://placekitten.com/90/90",
          "https://placekitten.com/91/91",
          "https://placekitten.com/92/92",
          "https://placekitten.com/93/93",
          "https://placekitten.com/94/94",
          "https://placekitten.com/95/95",
        ],
        details: {
          heal: 5000,
          damage: 15000,
          takenDamage: 2000,
          coins: 10000,
          participation: 70,
          vision: 12
        },
        team1Players: [
          {image: "https://placekitten.com/90/90", name: "Player1"},
          {image: "https://placekitten.com/91/91", name: "Player2"},
          {image: "https://placekitten.com/92/92", name: "Player3"},
          {image: "https://placekitten.com/93/93", name: "Player4"},
          {image: "https://placekitten.com/94/94", name: "Player5"},
        ],
        team2Players: [
          {image: "https://placekitten.com/95/95", name: "Player6"},
          {image: "https://placekitten.com/96/96", name: "Player7"},
          {image: "https://placekitten.com/97/97", name: "Player8"},
          {image: "https://placekitten.com/98/98", name: "Player9"},
          {image: "https://placekitten.com/99/99", name: "Player10"},
        ]
      }], // your games data
      hovered: false,
      showItemModal: false,
      currentGame: null,
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
      event.target.style.backgroundColor = "#f5f5f5"; // 예시로 배경색 변경 추가. 원하는 스타일로 조절 가능
    },
    onLeave(event) {
      event.target.style.cursor = "default";
      event.target.style.backgroundColor = "white"; // hover 효과가 사라질 때 원래 상태로 되돌리기 위한 코드
    },
    selectAutocompleteItem(text) {
      this.searchText = text;
      this.isInputFocused = false;
    },
    showItems(gameId) {
      this.currentGame = this.games.find(game => game.id === gameId);
      this.showItemModal = true;
    },
    // 데이터를 변형하는 함수
    transformGameData(rawData) {
      // rawData가 존재하지 않거나 빈 배열인 경우 빈 배열을 반환
      if (!rawData || rawData.length === 0) {
        return [];
      }

      let id = 0;

      const transformedData = rawData.map(record => ({
        id: id++,
        type: record.gameType === "RATING" ? "공식전" : "일반전",
        result: record.result,
        characterImage: `https://img-api.neople.co.kr/cy/characters/${record.playCharacterId}?zoom=2`,  // 플레이어 캐릭터 이미지 URL
        positionImage: this.getPostionImage(record.positionName),
        traits: record.attributeIds.map(traitId => `https://img-api.neople.co.kr/cy/position-attributes/${traitId}`),  // 특성 이미지 URL 배열
        gameInfo: {
          kills: record.killCount,
          deaths: record.deathCount,
          assists: record.assistCount,
          participationRate: record.killParticipation,
          kda: record.kda,
          cs: record.csCount
        },
        items: record.itemIds.map(itemId => `https://img-api.neople.co.kr/cy/items/${itemId}`),  // 아이템 이미지 URL 배열
        details: {
          heal: record.healAmount,
          damage: record.attackPoint,
          takenDamage: record.damagePoint,  
          coins: record.getCoin,
          participation: record.killParticipation,
          vision: record.sightPoint
        },
        team1Players: record.teamPlayerInfos.slice(0, 5).map(player => ({
          image: `https://img-api.neople.co.kr/cy/characters/${player.characterId}?zoom=1`,
          name: player.nickname
        })),
        team2Players: record.teamPlayerInfos.slice(5).map(player => ({
          image: `https://img-api.neople.co.kr/cy/characters/${player.characterId}?zoom=1`,
          name: player.nickname
        }))
      }));

      return transformedData;
    },
    fetchPlayerData(playerId) {
      // 서버에서 사용자 데이터를 가져오는 API 호출
      axios.get(`/api/search/records/RATING/${playerId}`)
        .then((response) => {
          const detailData = response.data;
          this.games = this.transformGameData(detailData.gameRecords);
        })
        .catch((error) => {
          alert("데이터를 불러오는 것에 실패했습니다", error);
          console.log("error: ", error);
          this.$router.push('/'); 
        });
    },
    forwardDetail(playerName) {
      if (this.$route.params.nickname === playerName) {
        window.location.reload(); //현재 페이지와 동일한 플레이어로 접근시 현재 페이지 새로고침
      } else {
        this.$router.push({ name: 'RecordDetail', params: { nickname: playerName }});
      }
    },
    getPostionImage(positionName) {
      switch (positionName) {
        case '탱커':
          return '/img/tanker.png';
        case '원거리딜러':
          return '/img/ad.png';
        case '근거리딜러':
          return '/img/assassin.png';
        case '서포터':
          return '/img/supporter.png';
      }
    },
    shortenPlayerName(name) {
      // 이름이 너무 길 경우 줄임말로 변형하여 반환
      if (name.length > 5) {
        return name.substring(0, 5) + '...';
      }
      // 그 외에는 원래의 이름 그대로 반환
      return name;
    },
    formatNumber(number) {
      if (number >= 10000) {
        return (number / 1000).toFixed(1) + 'k';
      }
      return number.toString();
    },
    backgroundColorByResult(result) {
      if (result === 'win') {
        return '#E7F0FC'
      } else {
        return '#FBECEA'
      }
    }
  },
  mounted() {
    // VueTooltip.init();
    axios.get(`/api/search/player/search/${this.$route.params.nickname}`)
      .then((response) => {
        const playerData = response.data;
        this.playerId = playerData.playerId;
        this.playerCharacterImage = `https://img-api.neople.co.kr/cy/characters/${playerData.represent.characterId}?zoom=3`;
        console.log(this.playerCharacterImage);
        // 사용자 데이터를 서버에서 가져오기
        this.fetchPlayerData(this.playerId);
      })
      .catch((error) => {
        alert("닉네임 정보가 없습니다.", error);
        console.log("error: ", error);
        this.$router.push('/'); 
      });
    
  }
}
</script>


<style scoped>
.container-box {
  border: 1px solid black;
  box-sizing: border-box;
  padding: 15px; /* 내부 패딩을 조금 추가해줬습니다. */
}

.profile-image {
  width: 100px;
  height: 100px;
}

.text-left {
  text-align: left;
}
.text-right {
  text-align: right;
}

.br-1 {
  border-right: 1px solid black;
}

.custom-link {
  color: #6c757d; /* 기본적으로 회색 */
  text-decoration: none; /* 밑줄 제거 */
}

.custom-link:hover {
  color: #343a40; /* 마우스를 올리면 글자색이 더 진한 색으로 변경 */
  text-decoration: underline; /* 밑줄 추가 */
}

</style>