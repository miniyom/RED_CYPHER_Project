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
          <h2 class="mb-2">{{ playerNickname }}</h2>
          <b-button variant="primary" class="me-2" @click="renewalDetailData(playerNickname)">전적갱신</b-button>
          <span class="ml-2">최근 갱신: {{ detailData.renewalTime }}</span>
        </b-col>
      </b-row>
    </b-container>

    <!-- 이동 탭 박스 -->
    <b-container class="my-3 container-box">
      <div class="d-flex justify-content-start">
        <b-button
          variant="success"
          @click="switchRatingDetail(playerNickname)"
          :disabled="isRatingDetail"
          class="me-3"
        >
          공식전
        </b-button>
        <b-button
          variant="success"
          @click="switchNormalDetail(playerNickname)"
          :disabled="!isRatingDetail"
        >
          일반전
        </b-button>
      </div>
    </b-container>

    <!-- 플레이어에 대한 상세 지표 박스 -->
    <b-container class="my-3 ">
      <!-- 모스트 사이퍼/모스트 포지션 탭 박스 -->
      <b-row>
        <b-col sm="6" class="container-box">
          <b-tabs>
            <b-tab title="모스트 사이퍼" active>
              <!-- 모스트 사이퍼 내용 -->
              <b-table :items="detailData.mostCypherInfos" :fields="mostCypherFields">
                <template #cell(characterImage)="data">
                  <img :src="data.value" alt="Character Image" style="width: 30px; height: 30px; border-radius: 50%;">
                </template>
                <template #cell(characterName)="data">
                  {{ data.value }}
                </template>
                <template #cell(winRate)="data">
                  {{ data.value }}%
                </template>
                <template #cell(playCount)="data">
                  {{ data.value }}
                </template>
                <template #cell(kda)="data">
                  {{ data.value }}
                </template>
              </b-table>
            </b-tab>
            <b-tab title="모스트 포지션">
              <!-- 모스트 포지션 내용 -->
              <b-col>
                <b-row>
                  <PieGraph v-if="pieGraphLoaded" :chartData="pieGraphData" style="height: 400px;" />
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

        <!-- 공성전 전적 -->
        <b-col sm="6" class="container-box">

          <b-container class="my-3">
            <b-row style="border-bottom: solid 1px #000;">
              <b-col sm="6">
                <h3>공식전</h3>
                <b-row>
                  <b-col cols="4" class="d-flex align-items-center justify-content-center">
                    <b-img :src="getTierImage(ratingGameTier)" fluid alt="Tier Image"/>
                  </b-col>
                  <b-col cols="8">
                    <b-list-group flush>
                      <b-list-group-item>{{ ratingGameTier }}</b-list-group-item>
                      <b-list-group-item>{{ ratingWinCount }}승 {{ ratingLoseCount }}패 {{ ratingStopCount }}중단</b-list-group-item>
                      <b-list-group-item>승률: {{ ratingWinRate }}%</b-list-group-item>
                    </b-list-group>
                  </b-col>
                </b-row>
              </b-col>
              <b-col sm="6">
                <h3>일반전</h3>
                <b-list-group flush>
                  <b-list-group-item>{{ normalWinCount }}승 {{ normalLoseCount }}패 {{ normalStopCount }}중단</b-list-group-item>
                  <b-list-group-item>승률: {{ normalWinRate }}%</b-list-group-item>
                </b-list-group>
              </b-col>
            </b-row>

            <b-row style="height: 300px;" class="mt-4">
              <!-- LineGraph 컴포넌트를 import하여 사용하고, 필요한 데이터를 props로 전달 -->
              <LineGraph  v-if="lineGraphLoaded" :chartData="lineGraphData" />
            </b-row>

          </b-container>

        </b-col>
      </b-row>
    </b-container>

    <!-- 플레이어의 최근 플레이 지표 박스 -->
    <b-container>
      <p class="p-font d-flex justify-content-start">2주간의 게임기록을 분석한 데이터입니다.</p>
    </b-container>
    <b-container class="my-3 container-box fluid">
      <b-row>
        <b-col sm="7" class="br-1">
          <b-row>
            <b-col sm="3" class="br-1">
              <h4>게임 수</h4>
              <h2 class="mt-5 mb-5">{{ detailData.recentlyPlayCount }}경기</h2>
            </b-col>
            <b-col sm="3" class="br-1">
              <h4>승률</h4>
              <h2 class="mt-5 mb-5">{{ detailData.recentlyWinRate }}%</h2>
            </b-col>
            <b-col sm="3" class="br-1">
              <h4>평균 KDA</h4>
              <h2 class="mt-5 mb-5">{{ detailData.recentlyKda }}</h2>
            </b-col>
            <b-col sm="3">
              <h4>평균 생존시간</h4>
              <h2 class="mt-5 mb-5">{{ detailData.recentlyAverageSurvivalRate }}초</h2>
            </b-col>
          </b-row>
        </b-col>
        <b-col sm="5">
          <b-list-group flush>
            <b-list-group-item v-for="recentCypherInfo in detailData.recentlyPlayCyphersInfos" :key="recentCypherInfo.characterId" class="d-flex align-items-center py-2 text-left">
              <b-col sm="1" class="me-2">
                <b-img :src="recentCypherInfo.characterImage" fluid alt="Character Image" class="rounded-circle mr-3"
                     style="width: 35px; height: 35px;"></b-img>
              </b-col>
              <b-col sm="2" class="me-2">{{ recentCypherInfo.characterName }}</b-col>
              <b-col sm="2" class="me-3">{{ recentCypherInfo.winCount }}승 {{ recentCypherInfo.loseCount }}패</b-col>
              <b-col sm="4" >K/D/A: {{ recentCypherInfo.killCount }}/{{ recentCypherInfo.deathCount }}/{{ recentCypherInfo.assistCount }}</b-col>
              <b-col sm="2" >KDA: {{ formatSmallNumber((recentCypherInfo.killCount+recentCypherInfo.assistCount)/recentCypherInfo.deathCount) }}</b-col>
            </b-list-group-item>
          </b-list-group>
        </b-col>
      </b-row>
    </b-container>

    <!-- 공식전, 일반전 게임기록 -->
    <b-container class="my-3 container-box">
      <b-list-group>
        <b-list-group-item class="d-flex justify-content-start align-items-center">
          <b-button
            variant="secondary"
            @click="switchRatingRecord(playerId)"
            :disabled="isRatingRecord"
            class="me-3"
          >
            공식전
          </b-button>
          <b-button
            variant="secondary"
            @click="switchNormalRecord(playerId)"
            :disabled="!isRatingRecord"
          >
            일반전
          </b-button>
        </b-list-group-item>
        <!-- games 배열이 비어있을 때 메시지 표시 -->
        <b-list-group-item v-if="this.games.length < 1">
          게임 기록이 없습니다.
        </b-list-group-item>
        <!-- games 배열이 비어있지 않을 때 게임 리스트 표시 -->
        <template v-else>
          <b-list-group-item v-for="game in games" :key="game.matchId" class="p-1 text-left" :style="{ backgroundColor: backgroundColorByResult(game.result)}" style="color: #6E7474;">
            <b-row class="align-items-center justify-content-around" style="overflow: hidden;">
              <!-- 게임 타입 -->
              <b-col sm="1" class="ml-3 pe-2 fs-5 d-flex justify-content-center">
                <div class="flex-wrap font-bold d-flex flex-column" :style="{ color : fontColorByResult(game.result)}">
                  <b-row>{{ game.type }}</b-row>
                  <b-row>{{ game.result }}</b-row>
                  <!-- <b-row class="mt-3" style="font-size: small;">{{ game.playDate }}</b-row> -->
                  <div style="font-size: small; position: absolute; bottom: 7px; left:7px">{{ game.playDate }}</div>
                </div>
              </b-col>

              <!-- 캐릭터 이미지 -->
              <b-col sm="auto">
                <b-img v-b-tooltip.hover :title=game.characterName :src="game.characterImage" rounded="circle" alt="Character" class="me-4" style="width: 85px;"></b-img>
              </b-col>

              <b-col sm="3">
                <b-row class="mb-2 pb-2" style="border-bottom: solid 1px #6E7474;">
                  <b-col sm="3">
                    <!-- 포지션 이미지 -->
                    <b-img :src="game.positionImage" rounded="circle" alt="Position" class="p-0 me-4" style="width: 40px; "></b-img>
                  </b-col>
                  <b-col sm="9" class="d-flex justify-content-end">
                     <!-- 특성 이미지 -->
                    <b-img 
                      v-for="attribute in game.attributes"
                      :src="attribute.attributeImage" 
                      rounded="circle" 
                      alt="'Attribute' + attribute" 
                      :key="attribute.attributeId"
                      class="m-0 p-0 me-1" 
                      style="width: 40px; cursor: pointer;"
                      @click="fetchAttributeData(attribute.attributeId)"
                      v-b-tooltip.hover :title=attribute.attributeName
                    >
                    </b-img>
                  </b-col>
                </b-row>
                <b-row>
                  <!-- 아이템 정보 -->
                  <div class="d-flex justify-content-between mb-2">
                    <div 
                      v-for="item in game.items.slice(0, 8)"
                      :key="item.itemId"
                      :style="{border: getBorderColor(item), position: 'relative', width: '100%', maxWidth: '100%'}"
                      class="me-1"
                      v-b-tooltip.hover :title=item.itemName
                    >
                      <b-img 
                        :src="item.itemId !== null ? item.itemImage : 'http://static.cyphers.co.kr/img/league/icon_nil.jpg'"
                        style="width: 100%; height: auto; cursor: pointer;"
                        fluid
                        @click="fetchItemData(item.itemId)"
                      ></b-img>
                    </div>
                  </div>
                  <div class="d-flex justify-content-between mb-2">
                    <div 
                      v-for="item in game.items.slice(8, 16)"
                      :key="item.itemId"
                      :style="{border: getBorderColor(item), position: 'relative', width: '100%', maxWidth: '100%'}"
                      class="me-1"
                      v-b-tooltip.hover :title=item.itemName
                    >
                      <b-img 
                        :src="item.itemId !== null ? item.itemImage : 'http://static.cyphers.co.kr/img/league/icon_nil.jpg'"
                        style="width: 100%; height: auto; cursor: pointer;"
                        fluid
                        @click="fetchItemData(item.itemId)"
                      ></b-img>
                    </div>
                  </div>
                </b-row>
              </b-col>


              <b-col sm="6">
                <b-row>
                  <!-- 게임 정보 -->
                  <b-col sm="3" class="d-flex align-items-center">
                    <div class="flex-wrap">
                      <div v-b-tooltip.hover title="킬 / 데스 / 어시 (킬기여도)">K / D / A (KP)</div>
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
                          <div>{{ formatBigNumber(game.details.heal) }}</div>
                          <div>{{ formatBigNumber(game.details.damage) }}</div>
                          <div>{{ formatBigNumber(game.details.takenDamage) }}</div>
                          <div>{{ formatBigNumber(game.details.coins) }}</div>
                          <div>{{ formatBigNumber(game.details.participation) }}</div>
                          <div>{{ formatBigNumber(game.details.vision) }}</div>
                        </div>
                      </b-col>
                    </b-row>
                  </b-col>
                  <!-- 함께한 플레이어 -->
                  <b-col sm="5">
                    <b-row>
                      <b-col sm="6">
                        <div v-for="player in game.team1Players" :key="player.name" class="d-flex align-items-center mb-1">
                          <b-link class="custom-link" @click="forwardDetail(player.name)">
                            <b-img class="rounded-image" :src="player.image" rounded alt="Player" style="width: 25px;" ></b-img>
                          </b-link>
                          <b-link class="custom-link" @click="forwardDetail(player.name)">
                            <div v-b-tooltip.hover :title="player.name" class="ml-2" style="font-size: 12px;">{{ shortenPlayerName(player.name) }}</div>
                          </b-link>
                        </div>
                      </b-col>
                      <b-col sm="6">
                        <div v-for="player in game.team2Players" :key="player.name" class="d-flex align-items-center mb-1">
                          <b-link class="custom-link" @click="forwardDetail(player.name)">
                            <b-img class="rounded-image" :src="player.image" rounded alt="Player" style="width: 25px;"></b-img>
                          </b-link>
                          <b-link class="custom-link" @click="forwardDetail(player.name)">
                            <div v-b-tooltip.hover :title="player.name" class="ml-2" style="font-size: 12px;">{{ shortenPlayerName(player.name) }}</div>
                          </b-link>
                        </div>
                      </b-col>
                    </b-row>
                  </b-col>
                </b-row>
              </b-col>

            </b-row>
          </b-list-group-item>

          <b-list-group-item v-if="showLoadMoreGames" class="text-center">
            <b-button class="more-games" @click="loadMoreGames">20게임 추가 검색</b-button>
          </b-list-group-item>

        </template>
      </b-list-group>
    </b-container>

    <b-modal v-model="showAttributeModal" title="특성 정보" centered hide-footer>
      <div class="p-3" v-if="attributeDetail" style="background-color: #f5deb3;">
        <h5>
          <img class="me-3" :src="attributeDetail.image" alt="특성 이미지" style="width: 60px; height: 60px;">
          <span class="font-bold">{{ attributeDetail.attributeName }}</span>
          <span class="text-right" style="float: right;">{{ attributeDetail.positionName }}</span>
        </h5>
        <br><br>
        <p>{{ attributeDetail.explain }}</p>
      </div>
    </b-modal>

    <b-modal v-model="showItemModal" title="아이템 정보" centered hide-footer>
      <div class="p-3" v-if="itemDetail" style="background-color: #f5deb3;">
        <h5>
          <img class="me-3" :src="itemDetail.image" alt="아이템 이미지" style="width: 60px; height: 60px;">
          <span class="font-bold" :style="{ color: itemDetail.rarityColor }">{{ itemDetail.itemName }}</span>
          <span class="text-right" style="float: right;">{{ itemDetail.slotName }}</span>
        </h5>
        <p style="white-space: pre-line; ">{{ itemDetail.explainDetail }}</p>
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
      isRatingDetail: true,
      mostCypherFields: [
          { key: 'characterImage', label: '능력자' },
          { key: 'characterName', label: '능력자명' },
          { key: 'winRate', label: '승률' },
          { key: 'playCount', label: '게임 수' },
          { key: 'kda', label: 'KDA' }
      ],
      ratingGameTier: 'UNRANK',
      ratingWinCount: 0,
      ratingLoseCount: 0,
      ratingStopCount: 0,
      ratingWinRate: 0,
      normalWinCount: 0,
      normalLoseCount: 0,
      normalStopCount: 0,
      normalWinRate: 0,
      detailData: {
        renewalTime: '없음',

        recentlyPlayCount: 0,
        recentlyWinRate: 0,
        recentlyKda: 0.0,
        recentlyAverageSurvivalRate: 0,
        recentlyPlayCyphersInfos: [
          {
            characterImage: '',
            characterName: '',
            winRate: 0,
            playCount: 0,
            kda: 0,
          }
        ]
      },
      lineGraphData: {
        labels: [
          '08-19',
          '08-20',
          '08-21',
          '08-22',
          '08-23',
          '08-24',
          '08-25'
        ],
        datasets: [
          {
            label: '승수',
            backgroundColor: '#f87979',
            data: [0, 0, 0, 0, 0, 0, 0]
          },
          {
            label: '패수',
            backgroundColor: '#f87979',
            data: [0, 0, 0, 0, 0, 0, 0]
          },
        ]
      },
      lineGraphLoaded: false,
      pieGraphData: {
        type: 'pie',
        labels: ['탱커', '원거리딜러', '서포터', '근거리딜러'],
        datasets: [
          {
            backgroundColor: ['#b7d7ef', '#f5d7ee', '#ffecb3', '#ef9a9a'],
            data: [40, 20, 80, 10],
          },
        ]
      },
      pieGraphLoaded: false,
      games: [],
        // {
        //   matchId: '',    
        //   type: "공식전",
        //   result: "승리",
        //   playDate: '',
        //   characterImage: "https://placekitten.com/100/100",
        //   characterName: '엘리',
        //   postionImage: "@/public/img/tanker.png",
        //   attributes: [
        //     // {
        //     //   attributeImage: "https://img-api.neople.co.kr/cy/position-attributes/e29cbec17de6ae981984c6d279400483",
        //     //   attributeId: "e29cbec17de6ae981984c6d279400483",
        //     //   attributeName: "완벽주의자",
        //     // }
        //     //총 4개 특성
        //   ],
        //   gameInfo: {
        //     kills: 10,
        //     deaths: 2,
        //     assists: 5,
        //     participationRate: 20,
        //     kda: 2.5,
        //     cs: 150
        //   },
        //   items: [
        //     {
        //       image: "https://img-api.neople.co.kr/cy/items/19f0134c20a835546c760c38293ce67a",
        //       itemId: "19f0134c20a835546c760c38293ce67a",
        //       itemName: "E 파이어 포르테",
        //       rarityName: "유니크",
        //       rarityColor: "",
        //       slotName: "발(이동)",
        //       seasonName: "시즌 1 : Eclipse",
        //       explainDetail: "\n\n[1레벨] : 장비레벨+3\n비용 650 coin\n이동속도 : +63\n\n[2레벨] : 장비레벨+3\n비용 850 coin\n이동속도 : +63\n불놀이(SL) 공격속도 : +6%\n\n난 언제나 내가 내린 결정에 확신이 있어. 같은 상황이 온다고 해도 언제나 내 답은 같아. "
        //     },
        //     // 총 16개 아이템
        //   ],
        //   details: {
        //     heal: 5000,
        //     damage: 15000,
        //     takenDamage: 2000,
        //     coins: 10000,
        //     participation: 70,
        //     vision: 12
        //   },
        //   team1Players: [
        //     {image: "https://placekitten.com/90/90", name: "Player1"},
        //     {image: "https://placekitten.com/91/91", name: "Player2"},
        //     {image: "https://placekitten.com/92/92", name: "Player3"},
        //     {image: "https://placekitten.com/93/93", name: "Player4"},
        //     {image: "https://placekitten.com/94/94", name: "Player5"},
        //   ],
        //   team2Players: [
        //     {image: "https://placekitten.com/95/95", name: "Player6"},
        //     {image: "https://placekitten.com/96/96", name: "Player7"},
        //     {image: "https://placekitten.com/97/97", name: "Player8"},
        //     {image: "https://placekitten.com/98/98", name: "Player9"},
        //     {image: "https://placekitten.com/99/99", name: "Player10"},
        //   ]
        // }
      isRatingRecord: true,
      showItemModal: false,
      itemDetail: null,
      // {
      //   image: "https://img-api.neople.co.kr/cy/items/19f0134c20a835546c760c38293ce67a",
      //   itemId: "19f0134c20a835546c760c38293ce67a",
      //   itemName: "E 파이어 포르테",
      //   rarity: "유니크",
      //   rarityColor: "",
      //   slotName: "발(이동)",
      //   seasonName: "시즌 1 : Eclipse",
      //   explainDetail: "\n\n[1레벨] : 장비레벨+3\n비용 650 coin\n이동속도 : +63\n\n[2레벨] : 장비레벨+3\n비용 850 coin\n이동속도 : +63\n불놀이(SL) 공격속도 : +6%\n\n난 언제나 내가 내린 결정에 확신이 있어. 같은 상황이 온다고 해도 언제나 내 답은 같아. "
      // },
      showAttributeModal: false,
      attributeDetail: null,
        // {
        //   attributeId: '',
        //   attributeName: '',
        //   explain: '',
        //   positionName: '',
        // }
      nextGames: '',
      showLoadMoreGames: false,
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
    // 데이터를 변형하는 함수
    transformGameData(rawData) {
      // rawData가 존재하지 않거나 빈 배열인 경우 빈 배열을 반환
      if (!rawData || rawData.length === 0) {
        return [];
      }

      const transformedData = rawData.map(record => ({
        matchId: record.matchId,
        type: record.gameType === "RATING" ? "공식전" : "일반전",
        result: record.result,
        playDate: record.playDate,
        characterImage: `https://img-api.neople.co.kr/cy/characters/${record.characterId}?zoom=2`,  // 플레이어 캐릭터 이미지 URL
        characterName: record.characterName,
        positionImage: this.getPostionImage(record.positionName),
        attributes: record.attributeInfos.map(attributeInfo => ({
          attributeId: attributeInfo.id,
          attributeName: attributeInfo.name,
          attributeImage: `https://img-api.neople.co.kr/cy/position-attributes/${attributeInfo.id}`
        })),  // 특성 이미지 URL 배열
        gameInfo: {
          kills: record.killCount,
          deaths: record.deathCount,
          assists: record.assistCount,
          participationRate: record.killParticipation,
          kda: record.kda,
          cs: record.csCount
        },
        items: record.itemInfos.map(itemInfo => ({
          itemId: itemInfo.itemId, 
          itemImage: `https://img-api.neople.co.kr/cy/items/${itemInfo.itemId}`,  // 아이템 이미지 URL
          itemName: itemInfo.itemName,
          rarityName: itemInfo.rarityName,
          equipSlotCode: itemInfo.equipSlotCode,
          equipSlotName: itemInfo.equipSlotName
        })), 
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
          name: player.nickname !== null ? player.nickname : '???'
        })),
        team2Players: record.teamPlayerInfos.slice(5).map(player => ({
          image: `https://img-api.neople.co.kr/cy/characters/${player.characterId}?zoom=1`,
          name: player.nickname !== null ? player.nickname : '???'
        }))
      }));

      return transformedData;
    },
    renewalDetailData(nickname) {
      axios.get(`/api/search/renewal/${nickname}`)
      .then(() => {
        alert(`갱신이 완료되었습니다.`);
        this.$router.go();
      })
      .catch((error) => {
        alert("닉네임을 찾을 수 없습니다.", error);
        console.log("renewal detail error: ", error);
      });
    },
     fetchDetailData(gameType, nickname) {
      axios.get(`/api/search/player/detail/${gameType}/${nickname}`)
      .then((response) => {
        this.pieGraphLoaded = false;
        this.lineGraphLoaded = false;

        this.detailData = response.data;

        this.lineGraphData.labels = this.generateDateLabels();
        this.lineGraphData.datasets[0].data = this.detailData.resultHistory.map(entry => entry.winCount);
        this.lineGraphData.datasets[1].data = this.detailData.resultHistory.map(entry => entry.loseCount);

        this.pieGraphData.datasets[0].data = [this.detailData.tankerUseRate, this.detailData.rangeDealerUseRate, this.detailData.supporterUseRate, this.detailData.meleeDealerUseRate];

      })
      .then(()=> {
        this.lineGraphLoaded = true;
        this.pieGraphLoaded = true;
      })
      .catch((error) => {
        alert("갱신된 정보가 없습니다.", error);
        console.log("fetch detail error: ", error);
      });
    },
    switchRatingDetail(nickname) {
      if (!this.isRatingDetail) {
        this.isRatingDetail = true;
      }
      this.fetchDetailData('RATING', nickname);
    },
    switchNormalDetail(nickname) {
      if (this.isRatingDetail) {
        this.isRatingDetail = false;
      }
      this.fetchDetailData('NORMAL', nickname);
    },
    fetchGameData(gameType, playerId) {
      // 서버에서 사용자 데이터를 가져오는 API 호출
      axios.get(`/api/search/records/${gameType}/${playerId}`)
        .then((response) => {
          const gameData = response.data;

          if (gameData.gameRecords.length === 0) {
            return;
          }

          this.games = this.transformGameData(gameData.gameRecords);
          if (gameData.next !== 'no more records') {
            this.showLoadMoreGames = true;
            this.nextGames = gameData.next;
          } else {
            this.showLoadMoreGames = false;
          }

          if (gameData.next !== 'no more records') {
            this.showLoadMoreGames = true;
            this.nextGames = gameData.next;
          }

        })
        .catch((error) => {
          alert("게임기록을 불러오는 것에 실패했습니다", error);
          console.log("fetch gamerecord error: ", error);
        });
    },
    switchRatingRecord(playerId) {
      if (!this.isRatingRecord) {
        this.isRatingRecord = true;
      }
      this.fetchGameData('RATING', playerId);
    },
    switchNormalRecord(playerId) {
      if (this.isRatingRecord) {
        this.isRatingRecord = false;
      }
      this.fetchGameData('NORMAL', playerId);
    },
    loadMoreGames() {
      axios.get(`/api/search/records/next/${this.playerId}/${this.nextGames}`)
        .then((response) => {
          const nextGameData = response.data;

          if (nextGameData.gameRecords.length === 0) {
            return;
          }

          this.games = this.games.concat(this.transformGameData(nextGameData.gameRecords));
          console.log("게임기록 개수", this.games.length);
          if (nextGameData.next !== 'no more records') {
            this.showLoadMoreGames = true;
            this.nextGames = nextGameData.next;
          } else {
            this.showLoadMoreGames = false;
          }
        })
        .catch((error) => {
          alert("추가 게임기록을 불러오는 것에 실패했습니다", error);
          console.log("load more gamerecord error: ", error);
        });
    },
    fetchItemData(itemId) {
      if (itemId === null) {
        return
      }
      axios.get(`/api/search/item/${itemId}`)
        .then((response) => {
          const itemData = response.data;
          this.itemDetail = itemData;
          this.itemDetail.image = `https://img-api.neople.co.kr/cy/items/${itemData.itemId}`;
          this.showItemModal = true;
          this.setRarityColor();
        })
        .catch((error) => {
          alert("아이템 정보를 불러오는 것에 실패했습니다", error);
          console.log("fetch item error: ", error);
        });
    },
    fetchAttributeData(attributeId) {
      if (attributeId === null) {
        return
      }
      axios.get(`/api/search/attribute/${attributeId}`)
        .then((response) => {
          const attributeData = response.data;
          this.attributeDetail = attributeData;
          this.attributeDetail.image = `https://img-api.neople.co.kr/cy/position-attributes/${attributeData.attributeId}`;
          this.showAttributeModal = true;
        })
        .catch((error) => {
          alert("특성 정보를 불러오는 것에 실패했습니다", error);
          console.log("fetch attribute error: ", error);
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
    getTierImage(tierName) {
      switch (tierName) {
        case 'UNRANK':
          return '/img/unrank.png';
        case 'BRONZE':
          return '/img/bronze.png';
        case 'SILVER':
          return '/img/silver.png';
        case 'GOLD':
          return '/img/gold.png';
        case 'ZOKER':
          return '/img/zoker.png';
        case 'ACE':
          return '/img/ace.png';
        case 'HERO':
          return '/img/hero.png';
        case 'LEGEND':
          return '/img/legend.png';
      }
    },
    shortenPlayerName(name) {
      // 이름이 너무 길 경우 줄임말로 변형하여 반환
      if (name.length > 6) {
        return name.substring(0, 6) + '...';
      }
      // 그 외에는 원래의 이름 그대로 반환
      return name;
    },
    formatBigNumber(number) {
      //1000이상 수치를 k단위로 변환
      if (number >= 10000) {
        return (number / 1000).toFixed(1) + 'k';
      }
      return number.toString();
    },
    formatSmallNumber(value) {
      if (typeof value !== 'number') {
        return value;
      }
      return value.toFixed(2);
    },
    backgroundColorByResult(result) {
      if (result === 'win') {
        return '#E7F0FC'
      } else {
        return '#FBECEA'
      }
    },
    fontColorByResult(result) {
      if (result === 'win') {
        return '#699CE3'
      } else {
        return '#F57593'
      }
    },
    setRarityColor() {
      // 레어리티에 따라 색상을 지정할 수 있습니다.
      switch (this.itemDetail.rarity) {
        case "유니크":
          this.itemDetail.rarityColor = "#EB61AD";
          break;
        case "레어":
          this.itemDetail.rarityColor = "#9C5AFF";
          break;
        case "언커먼":
          this.itemDetail.rarityColor = "#3EB7FF";
          break;
        default:
          this.itemDetail.rarityColor = "#454545";
      }
    },
    // 아이템의 rarity에 따라 적절한 색상을 반환하는 함수
    getBorderColor(item) {
      switch(item.rarityName) {
          case "유니크":
              return '3px solid #EB61AD';
          case "레어":
              return '3px solid #9C5AFF';
          case "언커먼":
              return '3px solid #3EB7FF';
          default:
              return '3px solid #454545';  
      }
    },
    generateDateLabels() {
      const today = new Date(); 
      const dateLabels = [];

      for (let i = 6; i >= 0; i--) {
        const date = new Date(today);
        date.setDate(today.getDate() - i);
        const formattedDate = this.formatDate(date); 
        dateLabels.push(formattedDate);
      }

      return dateLabels; 
    },
    formatDate(date) {
      const month = String(date.getMonth() + 1).padStart(2, '0'); 
      const day = String(date.getDate()).padStart(2, '0');
      return `${month}-${day}`; 
    },
  },
  mounted() {
    axios.get(`/api/search/player/search/${this.$route.params.nickname}`)
      .then((response) => {
        const playerData = response.data;
        this.playerId = playerData.playerId;
        this.playerCharacterImage = `https://img-api.neople.co.kr/cy/characters/${playerData.represent.characterId}?zoom=3`;
        this.ratingGameTier = playerData.tierTest ? playerData.tierName : 'UNRANK';
        this.ratingWinCount = playerData.records[0].winCount;
        this.ratingLoseCount = playerData.records[0].loseCount;
        this.ratingStopCount = playerData.records[0].stopCount;
        this.normalWinCount = playerData.records[1].winCount;
        this.normalLoseCount = playerData.records[1].loseCount;
        this.normalStopCount = playerData.records[1].stopCount;

        this.fetchGameData('RATING', this.playerId);
        this.fetchDetailData('RATING', this.playerNickname);
      })
      .catch((error) => {
        alert("닉네임 정보가 없습니다.", error);
        console.log("mount error: ", error);
        this.$router.go(-1); 
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

.p-font {
    color : #BDBDBD;
}

.custom-link {
  color: #6c757d; /* 기본적으로 회색 */
  text-decoration: none; /* 밑줄 제거 */
}

.custom-link:hover {
  color: #343a40; /* 마우스를 올리면 글자색이 더 진한 색으로 변경 */
  text-decoration: underline; /* 밑줄 추가 */
}
.font-bold {
    font-weight: bold;
}

.item-badge {
  position: absolute;
  top: 0;
  left: 0;
  width: calc(100% / 8); /* 이미지의 1/8 크기로 설정 */
  max-width: calc(100% / 8); /* 이미지의 1/8 크기로 설정 */
  height: auto;
  background-color: #ED05A6;
  display: block;
  color: white;
  font-size: 0.8em; /* 원하는 크기로 설정 */
  text-align: center;
}

.more-games {
  position: relative;
  display: block;
  width: 100%;
  height: 40px;
  margin-top: 6px;
  background: rgb(242, 242, 242);
  font-size: 16px;
  color: rgb(127, 127, 127);
  text-align: center;
  border: 1px solid rgb(172, 172, 172);
  border-radius: 6px;
}
</style>