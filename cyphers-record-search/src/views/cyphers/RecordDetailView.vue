<template>
  <div>
    <!-- 최상단: Header -->
    <Header/>

    <!-- 프로필 박스 -->
    <b-container class="container-box my-3 mt-5">
      <b-row>
        <b-col sm="auto" class="p-0">
          <img src="https://placekitten.com/150/150" alt="프로필 이미지" class="img-fluid rounded-circle profile-image">
        </b-col>
        <b-col class="pl-0 text-left align-self-end">
          <h2 class="mb-2">닉네임 {{detailData.nickname}}</h2>
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
                    <b-img src="https://placekitten.com/120/120" fluid alt="Tier Image"/>
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
          <h4>게임수</h4>
          <h2 class="mt-4">1</h2>
        </b-col>
        <b-col sm="2" class="br-1">
          <h4>승률</h4>
          <h2 class="mt-4">20%</h2>
        </b-col>
        <b-col sm="2" class="br-1">
          <h4>KDA</h4>
          <h2 class="mt-4">3.24</h2>
        </b-col>
        <b-col sm="2" class="br-1">
          <h4>게임 점수</h4>
          <h2 class="mt-4">176</h2>
        </b-col>
        <b-col sm="4">
          <b-list-group flush>
            <b-list-group-item class="d-flex align-items-center py-2">
              <b-img src="path_to_character_image.jpg" fluid alt="Character Image" class="mr-3"
                     style="width: 20px; height: 20px;"></b-img>
              <div class="flex-fill">캐릭터 이름</div>
              <div class="me-4">20승 5패</div>
              <div class="ml-3">KDA: 10/2/5</div>
            </b-list-group-item>
            <b-list-group-item class="d-flex align-items-center py-2">
              <b-img src="path_to_character_image.jpg" fluid alt="Character Image" class="mr-3"
                     style="width: 20px; height: 20px;"></b-img>
              <div class="flex-fill">캐릭터 이름</div>
              <div class="me-4">20승 5패</div>
              <div class="ml-3">KDA: 10/2/5</div>
            </b-list-group-item>
            <b-list-group-item class="d-flex align-items-center py-2">
              <b-img src="path_to_character_image.jpg" fluid alt="Character Image" class="mr-3"
                     style="width: 20px; height: 20px;"></b-img>
              <div class="flex-fill">캐릭터 이름</div>
              <div class="me-4">20승 5패</div>
              <div class="ml-3">KDA: 10/2/5</div>
            </b-list-group-item>
          </b-list-group>
        </b-col>
      </b-row>
    </b-container>

    <b-container class="my-3 container-box h-100">
      <b-list-group class="h-100">
        <b-list-group-item v-for="game in games" :key="game.id" class="p-1">
          <!-- 게임 타입 -->
          <div class="d-flex align-items-center" style="justify-content: space-around;">
            <div class="flex-shrink-0 me-3 pe-2 fs-5">{{ game.type }}</div>

            <!-- 캐릭터 이미지 -->
            <b-img :src="game.characterImage" alt="Character" class="me-5 rounded-image" style="width: 80px;"></b-img>

            <!-- 특성 이미지 -->
            <div class="me-5 pe-3 h-100">
              <b-row>
                <b-img :src="game.traits[0]" alt="Trait" class="m-0 p-0 me-1 pe-1 rounded-image" style="width: 30px;"></b-img>
                <b-img :src="game.traits[1]" alt="Trait" class="m-0 p-0 rounded-image" style="width: 30px;"></b-img>
              </b-row>
              <b-row class="mt-1">
                <b-img :src="game.traits[2]" alt="Trait" class="m-0 p-0 me-1 pe-1 rounded-image" style="width: 30px;"></b-img>
                <b-img :src="game.traits[3]" alt="Trait" class="m-0 p-0 rounded-image" style="width: 30px;"></b-img>
              </b-row>
            </div>


            <!-- 게임 정보 -->
            <div class="me-5">
              <div>{{ game.gameInfo.kills }} / {{ game.gameInfo.deaths }} / {{ game.gameInfo.assists }} ({{ game.participationRate }}%)</div>
              <div>{{ game.gameInfo.kda }} KDA</div>
              <div>{{ game.gameInfo.cs }} CS</div>
            </div>

            <!-- 아이템 정보 -->
            <div class="me-5">
              <b-button @click="showItems(game.id)">아이템 보기</b-button>
            </div>

            <!-- 상세정보1 -->
            <div class="me-5">
              <div>{{ game.details.heal }} 힐량</div>
              <div>{{ game.details.damage }} 준 데미지</div>
              <div>{{ game.details.takenDamage }} 받은 데미지</div>
            </div>

            <!-- 상세정보2 -->
            <div class="me-5">
              <div>{{ game.details.coins }} 코인량</div>
              <div>{{ game.details.participation }} 전투참여</div>
              <div>{{ game.details.vision }} 시야점수</div>
            </div>

            <!-- 함께한 플레이어 -->
            <div class="me-4 d-flex flex-column">
              <b-row>
                <b-col>
                  <div v-for="player in game.team1Players" :key="player.name" class="d-flex align-items-center mb-1">
                    <b-img class="rounded-image" :src="player.image" alt="Player" style="width: 15px;"></b-img>
                    <div class="ml-2" style="font-size: 12px;">{{ player.name }}</div>
                  </div>
                </b-col>
                <b-col>
                  <div v-for="player in game.team2Players" :key="player.name" class="d-flex align-items-center mb-1">
                    <b-img class="rounded-image" :src="player.image" alt="Player" style="width: 15px;"></b-img>
                    <div class="ml-2" style="font-size: 12px;">{{ player.name }}</div>
                  </div>
                </b-col>
              </b-row>



            </div>
<!--            <div class="d-flex flex-column">-->

<!--            </div>-->

          </div>
        </b-list-group-item>
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
//import axios from "axios";
import Header from "./HeaderComponent.vue";
import LineGraph from "@/components/LineGraph";
import PieGraph from "@/components/PieGraph";

export default {
  props: {
    nickname: String
  },
  created() {
    console.log("검색 결과 데이터:", this.nickname);
  },
  components: {
    Header,
    LineGraph,
    PieGraph,
  },
  data() {
    return {
      playernickname: '',
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
        type: "공식전",
        characterImage: "https://placekitten.com/100/100",
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
    }
  },
  mounted() {
    // const searchNickname = this.$route.params.nickname;
    // axios.get(`/api/search/${searchNickname}`)
    //   .then((response) => {
    //     const detailData = response.data;
    //     this.playernickname = detailData.nickname;
    //   })
    //   .catch((error) => {
    //     alert("데이터를 불러오는 것에 실패했습니다");
    //     console.log("error: ", error);
    //     this.$router.push('/');
    //   });
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

.br-1 {
  border-right: 1px solid black;
}

.rounded-image {
  border-radius: 50%; /* 이미지를 동그랗게 만들기 위한 설정 */
}

</style>