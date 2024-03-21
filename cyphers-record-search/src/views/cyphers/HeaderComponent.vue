<template>
  <div>
    <b-navbar type="dark" variant="dark" class="navbar-container">
      <!-- Image and text -->
      
      <b-navbar-brand href="/" title="메인 홈으로">
        <img src="./img/rclogo4.png" class="d-inline-block align-top" alt="메인">
      </b-navbar-brand>

      <b-navbar-nav class="mx-auto font-size-small font-weight-bold mr-5">
        <b-nav-item>
          <router-link to="/" class="nav-link">메인 페이지</router-link>
        </b-nav-item>
        <b-nav-item>
          <router-link to="/ranking/all" class="nav-link">통합 랭킹</router-link>
        </b-nav-item>
        
        

        <b-nav-item href="#">사이퍼 정보</b-nav-item>
        <b-nav-item href="#">캐릭터 코멘트</b-nav-item>
        <b-nav-item href="#">게시판</b-nav-item>
      </b-navbar-nav>

      <b-navbar-brand href="https://cyphers.game.naver.com/" title="사이퍼즈 공식 홈페이지">
        <img src="./img/cyphers_logo.png" class="d-inline-block align-top" alt="사이퍼즈 공홈">
      </b-navbar-brand>
      
    </b-navbar>
  </div>
</template>

<style scoped>
.navbar-container {
  display: flex;
  justify-content: space-between;
}
.custom-link {
  color: gray; /* 기본적으로 회색 */
  text-decoration: none; /* 밑줄 제거 */
}
.custom-link:hover {
  color: white; /* 마우스를 올리면 글자색이 더 진한 색으로 변경 */
  text-decoration: underline; /* 밑줄 추가 */
}

@media (max-width: 768px) {
  /* 화면이 작아질 때의 스타일링을 지정할 수 있습니다. */
  .navbar-container {
    flex-direction: column; /* 작은 화면에서 세로 배치로 변경 */
  }
  .font-size-small {
    font-size: 14px; /* 작은 화면에서 폰트 크기를 조절 */
  }
}
</style>

<script>

export default {
  data() {
    return {
    }
  },
  methods: {
  },
  mounted() {
  }
}

</script>
