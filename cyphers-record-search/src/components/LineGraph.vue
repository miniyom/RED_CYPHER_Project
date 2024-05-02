<template>
    <LineChartGenerator
        v-if="loaded"
        :options="chartOptions"
        :data="chartData"
        :chart-id="chartId"
        :dataset-id-key="datasetIdKey"
        :plugins="plugins"
        :css-classes="cssClasses"
        :styles="styles"
        :width="width"
        :height="height"
        ref="lineGraph"
    />
</template>


<script>

import { Line as LineChartGenerator } from 'vue-chartjs'

import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  LineElement,
  LinearScale,
  CategoryScale,
  PointElement
} from 'chart.js'
import axios from 'axios'

ChartJS.register(
    Title,
    Tooltip,
    Legend,
    LineElement,
    LinearScale,
    CategoryScale,
    PointElement
)

export default {
  name: 'LineChart',
  components: {
    LineChartGenerator
  },
  props: {
    chartId: {
      type: String,
      default: 'line-chart'
    },
    datasetIdKey: {
      type: String,
      default: 'label'
    },
    width: {
      type: Number,
      default: 400
    },
    height: {
      type: Number,
      default: 400
    },
    cssClasses: {
      default: '',
      type: String
    },
    styles: {
      type: Object,
      default: () => {}
    },
    plugins: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      loaded: false,
      chartData: {
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
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false
      },
    }
  },
  created() {
    this.fetchChartData();
  },
  methods: {
    fetchChartData() {
      axios.get(`/api/search/player/detail/${this.$route.params.nickname}`)
        .then((response) => {
          const detailData = response.data;
          this.chartData.labels = this.generateDateLabels();

          this.chartData.datasets[0].data = detailData.resultHistory.map(entry => entry.winCount);
          this.chartData.datasets[1].data = detailData.resultHistory.map(entry => entry.loseCount);
          this.loaded = true;

        })
        .catch((error) => {
          alert("승패 데이터를 가져올 수 없습니다.", error);
          console.log("chart error: ", error);
        });
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
  }
}
</script>
