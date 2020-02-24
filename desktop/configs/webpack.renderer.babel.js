import path from 'path';
import HtmlWebpackPlugin from "html-webpack-plugin";

const port = process.env.PORT || 8088;
const dist = path.resolve(__dirname, '../build');

export default {
    devtool: 'inline-source-map',
    mode: 'development',
    target: 'electron-renderer',
    entry: path.resolve(__dirname, '../renderer/index.js'),
    output: {
        path: dist,
        filename: 'renderer.dev.js'
    },
    node: {
        __dirname: false,
        __filename: false,
    },
    module: {
        rules: [
            {
                test: /\.jsx?$/,
                loader: 'babel-loader',
            },
        ],
    },
    devServer: {
        port,
        publicPath: '/',
        compress: true,
        stats: 'errors-only',
        inline: true,
        lazy: false,
        hot: true,
        headers: {'Access-Control-Allow-Origin': '*'},
        contentBase: dist,
        watchOptions: {
            aggregateTimeout: 300,
            ignored: /node_modules/,
            poll: 100,
        },
        historyApiFallback: {
            verbose: true,
            disableDotRule: false,
        },
    },
    plugins: [
        new HtmlWebpackPlugin({
            template: 'index.html',
            filename: `${dist}/index.html`,
        }),
    ],
};
