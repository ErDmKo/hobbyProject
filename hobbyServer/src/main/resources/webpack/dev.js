const Merge = require('webpack-merge');
const common = require('./common');

module.exports = () => {
    return Merge(common, {
    });
}
