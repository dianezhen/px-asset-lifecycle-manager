var gulp = require('gulp');
var path = require('path');
//var debug = require('gulp-debug');
//gulp load plugins makes a short cut for any npm module starting with 'gulp-'
var $ = require('gulp-load-plugins')();

gulp.task('default', function(done) {
	gulp.src('.')
	.pipe($.webserver({
		host: 'localhost',
		fallback: 'index.html',
		livereload: false,
		directoryListing: false,
		//open: true,
		proxies: [
			{source: '/test', target: 'http://jsonplaceholder.typicode.com/get/1'},
			{source: '/domain', target: 'https://px-asset-lifecycle-manager-trilokee-gupta.run.aws-usw02-pr.ice.predix.io/domain'},
			{source: '/api', target: 'https://px-asset-lifecycle-manager-trilokee-gupta.run.aws-usw02-pr.ice.predix.io/api'}
		]
	}));
})