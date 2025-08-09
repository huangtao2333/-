const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  
  // 开发服务器配置
  devServer: {
    port: 3000,
    open: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: {
          '^/api': '/api'
        }
      }
    }
  },

  // 生产环境配置
  publicPath: process.env.NODE_ENV === 'production' ? './' : '/',
  outputDir: 'dist',
  assetsDir: 'static',

  // CSS配置
  css: {
    loaderOptions: {
      sass: {
        additionalData: `@import "@/assets/css/variables.scss";`
      }
    }
  },

  // 链式操作配置
  chainWebpack: config => {
    // 设置别名
    config.resolve.alias
      .set('@', require('path').resolve(__dirname, 'src'))
      .set('components', require('path').resolve(__dirname, 'src/components'))
      .set('views', require('path').resolve(__dirname, 'src/views'))
      .set('assets', require('path').resolve(__dirname, 'src/assets'))
      .set('api', require('path').resolve(__dirname, 'src/api'))
      .set('utils', require('path').resolve(__dirname, 'src/utils'))

    // 优化打包
    if (process.env.NODE_ENV === 'production') {
      // 移除console
      config.optimization.minimizer('terser').tap(args => {
        args[0].terserOptions.compress.drop_console = true
        return args
      })
    }
  },

  // 插件配置
  pluginOptions: {
    // Element Plus 自动导入配置
    'element-plus': {
      useSource: true
    }
  }
})
