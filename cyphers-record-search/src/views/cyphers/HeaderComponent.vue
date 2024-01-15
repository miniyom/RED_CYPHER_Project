<template>
  <div>
    <b-navbar type="dark" variant="dark" class="navbar-container">
      <!-- Image and text -->
      
      <b-navbar-brand href="/">
        <img src="https://placekitten.com/30/30" class="d-inline-block align-top" alt="고먐미">
        RED CYPHER
      </b-navbar-brand>

      <b-navbar-nav class="mx-auto font-size-small">
        <b-nav-item href="/" class="font-weight-bold mr-5">메인 페이지</b-nav-item>

        <b-nav-item href="#">사이퍼 정보</b-nav-item>
        <b-nav-item href="#">전체 랭킹</b-nav-item>
        <b-nav-item href="#">캐릭터 코멘트</b-nav-item>
        <b-nav-item href="#">게시판</b-nav-item>
      </b-navbar-nav>

      <b-navbar-brand href="https://cyphers.game.naver.com/">
        <img src="https://resource.cyphers.co.kr/ui/img/comm/logo.png" class="d-inline-block align-top" alt="고먐미">
      </b-navbar-brand>
      
    </b-navbar>
  </div>
</template>

<style scoped>
.navbar-container {
  display: flex;
  justify-content: space-between;
}
.navbar-brand-container {
  order: 1; /* Navbar Brand를 위로 이동 */
}

.navbar-nav-container {
  order: 2; /* Navbar Nav를 아래로 이동 */
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
