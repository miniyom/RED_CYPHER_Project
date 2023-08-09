const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 3000,
    proxy: {
      '/api': {  // '/api' 경로로 시작하는 모든 요청은 아래의 target으로 프록시됩니다.
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''  // 요청 URL에서 '/api'를 제거합니다. 필요에 따라 조절하십시오.
        }
      }
    }
  }
})
