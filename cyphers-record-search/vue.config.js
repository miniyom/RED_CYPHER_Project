const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  outputDir: '../src/main/resources/static/',
  devServer: {
    port: 3000,
    proxy: {
      '/api': {  // '/api' 경로로 시작하는 모든 요청은 아래의 target으로 프록시됩니다.
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: {
          '^/api': '/api'
        }
      },
    }
  }
})
