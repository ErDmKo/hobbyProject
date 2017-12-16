const path = require('path');

module.exports = {
    entry: {
        bundle: "./assets/index.tsx",
    },
    output: {
        filename: "[name].js",
        path: path.resolve(__dirname, '..', 'static')
    },
    resolve: {
        extensions: ['.ts', '.js', '.json', '.tsx']
    },
    module: {
        rules: [
            {test: /\.(ts|tsx?)$/, loader: 'awesome-typescript-loader'},
        ]
    }
}
