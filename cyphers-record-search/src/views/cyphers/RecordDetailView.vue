<template>
  <div>
    <!-- 최상단: Header -->
    <Header/>

    <!-- 프로필 박스 -->
    <b-container class="container-box my-3 mt-5">
      <b-row>
        <b-col sm="auto" class="p-0">
          <img src="path_to_profile_image" alt="프로필 이미지" class="img-fluid rounded-circle profile-image">
        </b-col>
        <b-col class="pl-0 text-left align-self-end">
          <h2 class="mb-2">닉네임</h2>
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
            </b-tab>
          </b-tabs>
        </b-col>

        <!-- 공식전 내용 -->
        <b-col sm="4" class="container-box">
          <h3>공식전</h3>
          <ul>
            <li>게임1: 승리 (KDA: 10/2/5)</li>
            <li>게임2: 패배 (KDA: 3/5/1)</li>
            <li>게임3: 승리 (KDA: 8/0/6)</li>
          </ul>
        </b-col>

        <!-- 일반전 내용 -->
        <b-col sm="2" class="container-box">
          <h3>일반전</h3>
          <ul>
            <li>게임1: 패배 (KDA: 2/7/4)</li>
            <li>게임2: 승리 (KDA: 15/1/3)</li>
          </ul>
        </b-col>
      </b-row>
    </b-container>

    <!-- 플레이어의 최근 플레이 지표 박스 -->
    <b-container class="my-3">
      <b-row>
        <b-col sm="2">
          <h4>게임수</h4>
          1
        </b-col>
        <b-col sm="2">
          <h4>승률</h4>
          2
        </b-col>
        <b-col sm="2">
          <h4>KDA</h4>
          3
        </b-col>
        <b-col sm="2">
          <h4>게임 점수</h4>
          4
        </b-col>
        <b-col sm="4">
          <h4>최근 플레이</h4>
          <b-table small :items="recent_play_data_source"></b-table>
        </b-col>
      </b-row>
    </b-container>

  </div>
</template>

<script>
// import axios from "axios";
import Header from "./HeaderComponent.vue";

export default {
  components: {
    Header
  },
  data() {
    return {
      activeTab: '모스트 사이퍼', // 예시
      // ... 나머지 데이터 구조
      cypherData: [
        { 사이퍼: '사이퍼1', 승률: '75%', 게임횟수: '20' },
        { 사이퍼: '사이퍼2', 승률: '60%', 게임횟수: '15' },
        { 사이퍼: '사이퍼3', 승률: '55%', 게임횟수: '10' },
        { 사이퍼: '사이퍼4', 승률: '80%', 게임횟수: '25' },
      ]
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
    }
  },
  mounted() {

  }
}
</script>


<style scoped>
.container-box {
  border: 1px solid black;
  padding: 15px; /* 내부 패딩을 조금 추가해줬습니다. */
}

.profile-image {
  width: 100px;
  height: 100px;
}
.text-left {
  text-align: left;
}

</style>