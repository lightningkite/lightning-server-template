const path = require('path');
const webpack = require('webpack')
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;
const zlib = require("zlib");

module.exports = {
  entry: './src/index.ts',
  // devtool: 'inline-source-map',
  plugins: [
    new webpack.EnvironmentPlugin({
      API_URL: 'http://localhost:8000/',
      WEBSOCKET_URL: 'ws://localhost:8000/api/v2/ws/?token=',
    })
    // new BundleAnalyzerPlugin({ analyzerMode: "static"}),
  ],
  module: {
    rules: [
      // all files with a `.ts` or `.tsx` extension will be handled by `ts-loader`
      { test: /\.tsx?$/, loader: "ts-loader" },
      {
        test: /\.s[ac]ss$/i,
        use: [
          'style-loader',
          'css-loader',
          'sass-loader',
        ],
      },
      {
        test: /\.(yaml|ogg|mp3|png|jpg|gif|ttf|eot|svg|woff(2)?)(\?[a-z0-9]+)?$/,
        type: 'asset/resource'
      },
      {
        test: /\.html$/i,
        loader: 'html-loader',
        options: {
        },
      },
    ],
  },
  resolve: {
    extensions: ['.ts', '.js', '.html', '.scss'],
    fallback: {
      buffer: require.resolve('buffer/'),
    },
  },
  output: {
    filename: 'index.js',
    path: path.resolve(__dirname, 'dist'),
  },
  watchOptions: {
    aggregateTimeout: 2000,
  },
  devServer: {
    allowedHosts: ['all'],
    static: {
      directory: './dist',
      publicPath: process.env.DEV_PUBLIC_PATH || '/',
    },
    host: process.env.DEV_HOST || '0.0.0.0',
    // port: process.env.DEV_PORT || '8941',
    port: process.env.DEV_PORT || '8940',
    open: false
  }
};
