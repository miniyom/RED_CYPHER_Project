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
  props: {
    chartData: {
      type : Object,
      required: true,
    }
  },
  data() {
    return {
      data: this.chartData,
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
            left: 100,   
            right: 100,  
            top: 20,    
            bottom: 20  
          }
        },
        animation: {
          duration: 1000, // 애니메이션 지속 시간
          easing: 'easeOutBounce', // 애니메이션의 easing 효과
          animateRotate: true, // 원의 회전 애니메이션 사용
          animateScale: false, // 원의 크기 변경 애니메이션 사용 여부
        },
      }
    }
  }
}
</script>



