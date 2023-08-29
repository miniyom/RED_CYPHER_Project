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
        scales: {
          y1: {   // 첫 번째 y 축
            position: 'left',
            title: {
              display: true,
              text: 'Y1 Axis'
            }
          },
          y2: {   // 두 번째 y 축
            position: 'right',
            title: {
              display: true,
              text: 'Y2 Axis'
            },
            grid: {
              drawOnChartArea: false // y2에만 격자를 그리지 않음
            }
          }
        }
      }
    }
  }
}
</script>



