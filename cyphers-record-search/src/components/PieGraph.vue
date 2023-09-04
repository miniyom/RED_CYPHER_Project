<template>
  <Pie :data="data" :options="options" />
</template>

<script lang="ts">
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js'
import { Pie } from 'vue-chartjs'

ChartJS.register(ArcElement, Tooltip, Legend)

export default {
  name: 'App',
  components: {
    Pie
  },
  data() {
    return {
      data: {
        labels: ['탱커', '원거리딜러', '서포터', '근거리딜러'],
        datasets: [
          {
            backgroundColor: ['#b7d7ef', '#f5d7ee', '#ffecb3', '#ef9a9a'],
            data: [40, 20, 80, 10],
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false
          }
        },
        layout: {
          padding: {
            left: 100,   // 왼쪽 패딩
            right: 100,  // 오른쪽 패딩
            top: 20,    // 상단 패딩
            bottom: 20  // 하단 패딩
          }
        },
        // 여기에 애니메이션 옵션을 추가
        animation: {
          duration: 1000, // 애니메이션 지속 시간
          easing: 'easeOutBounce', // 애니메이션의 easing 효과
          animateRotate: true, // 원의 회전 애니메이션 사용
          animateScale: false, // 원의 크기 변경 애니메이션 사용 여부
        },
        chartOptions: {
          responsive: true
        }
      }
    }
  }
}
</script>



