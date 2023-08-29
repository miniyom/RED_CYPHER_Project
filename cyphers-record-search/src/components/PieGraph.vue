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
        }
      }
    }
  }
}
</script>



