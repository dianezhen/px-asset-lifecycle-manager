var gulp = require('gulp');
var path = require('path');
var del = require('del');
var notifier = require('node-notifier');
var bowerFiles = require('main-bower-files');
var LessPluginBowerResolve = require('less-plugin-bower-resolve');

//gulp load plugins makes a short cut for any npm module starting with 'gulp-'
var $ = require('gulp-load-plugins')();

var version = '1';

var OUT_ROOT	= '.';
var OUT_DIR 	= OUT_ROOT + '/static';
var OUT_APP 	= OUT_DIR + '/app';
var OUT_CSS 	= OUT_DIR + '/styles';
var OUT_ASSETS 	= OUT_DIR + '/assets';
var OUT_FONT 	= OUT_DIR + '/fonts';
var OUT_IMG 	= OUT_DIR + '/images';
var OUT_BOWER 	= OUT_DIR + '/bower_components';

var APP_DIR  = gulp.src('src/app/**/*.*');
var APP_ALL	 = gulp.src(bowerFiles({ filter: '**/!(*spec|*mock).*', includeSelf: true }));
var APP_JS	 = gulp.src(bowerFiles({ filter: '**/!(web-animations*).js', includeSelf: true }));
var APP_HTML = gulp.src(bowerFiles({ filter: '**/*.html', includeSelf: true }));
var APP_LESS = gulp.src(bowerFiles({ filter: '**/*(*.css|*.less)', includeSelf: true }));
var BOWER_ALL= gulp.src(bowerFiles({ filter: '**/*.*' }), {base: 'src'});
var APP_TEST = gulp.src(bowerFiles({ filter: '**/*(*.js|*.html)', includeSelf: true, includeDev: true }));
var APP_FONT = gulp.src(bowerFiles({ filter: '**/*(*.eot|*.ttf|*.woff)', includeSelf: true, includeDev: true }));
var APP_IMG  = gulp.src(bowerFiles({ filter: '**/*(*.png|*.gif|*.jpg|*.svg)', includeSelf: true, includeDev: true }));
var ASSETS   = gulp.src('src/assets/**/*.*');

var appContext = '/';

var baseFromTemplate = '<base href="/">';
var baseForHtml = '<base href="' + appContext + '">';


var COMPILED_LESS = gulp.src('src/app/**/*.css');
// var COMPILED_LESS = APP_LESS
// 		.pipe($.less({
// 			plugins: [LessPluginBowerResolve]
// 		}));

gulp.task('clean', function (done) {
	del([OUT_DIR], {force:true}, done);
});

//copy bower files
gulp.task('copy-bower', ['clean'], function () {

	return BOWER_ALL.pipe(gulp.dest(OUT_DIR))
});

//just copy compiled less
gulp.task('copy-less', ['clean'], function () {
	
	// return COMPILED_LESS
	// 	.pipe(gulp.dest(OUT_CSS))
});

//just app js
gulp.task('copy-app', ['clean'], function () {
	return APP_DIR.pipe(gulp.dest(OUT_APP))
});

//copy assets to dist
gulp.task('copy-assets', ['clean', 'copy-img', 'copy-font'], function () {

	return ASSETS.pipe(gulp.dest(OUT_ASSETS))
});

gulp.task('copy-font', ['clean'], function () {
	return APP_FONT
		.pipe(gulp.dest(OUT_FONT))
});

gulp.task('copy-img', ['clean'], function () {
	return APP_IMG
		.pipe(gulp.dest(OUT_IMG))
});

//combine js and minify
gulp.task('minify-js', ['clean'], function () {

	return APP_JS
		.pipe($.concat('main.js'))
		.pipe(gulp.dest(OUT_APP))
		.pipe($.rename({suffix: '.min'}))
		.pipe($.ngAnnotate())
		.pipe($.uglify())
		.pipe(gulp.dest(OUT_APP))
});

//combine css and minify
gulp.task('minify-less', ['clean'], function() {
	
	return COMPILED_LESS
		.pipe($.concat('styles.css'))
		.pipe(gulp.dest(OUT_CSS))
		.pipe($.rename({suffix: '.min'}))
		.pipe($.minifyCss())
		.pipe(gulp.dest(OUT_CSS))
});

//put all of the HTML in one file
gulp.task('minify-html', ['clean'], function () {

	return APP_HTML
		.pipe($.minifyHtml({conditionals:true, comments: true, loose: true}))
		.pipe($.angularTemplatecache('templates.js', {module:'app', root:'static/', base: function(file) { return file.base.replace(__dirname +'/src', '') + file.relative; }}))
		.pipe($.rename({suffix: '.min'}))
		.pipe(gulp.dest(OUT_APP))
});

//inject production files into index
gulp.task('dist-index', ['clean', 'minify-js', 'minify-less', 'minify-html'], function() {

	return gulp.src('src/index.html')
		.pipe($.inject(gulp.src(OUT_DIR + "/**/*.min.*"), {addRootSlash: false, relative: false, ignorePath: OUT_DIR, addSuffix: '?v=' + version}))
		.pipe($.replace(baseFromTemplate, baseForHtml))
		.pipe(gulp.dest(OUT_DIR))
		.pipe($.notify({ message: 'Front End: production build complete' }));
});

//inject development files into index
gulp.task('dev-index', ['clean'], function() {

	return gulp.src('src/index.html')
		.pipe($.inject(APP_JS, { addRootSlash: false, relative: true, addSuffix: '?v=' + version }))
		.pipe($.inject(COMPILED_LESS, { addRootSlash: false, relative: true, addSuffix: '?v=' + version }))
		.pipe($.replace(baseFromTemplate, baseForHtml))
		.pipe(gulp.dest(OUT_DIR))
		.pipe($.notify({ message: 'Front End: dev files updated' }));
});

gulp.task('watch', ['dev'], function() {

	gulp.watch('src/**/*.*', {debounceDelay: 2000}, function(event) {
		
		//if its a change move that one file, else run dev build
		if(event.type === 'changed') {
			var fileDir = path.dirname(event.path.replace(__dirname +'/src', ''));
			console.log('File was ' + event.type + ', copying to ' + path.join(OUT_DIR, fileDir));
			
			gulp.src(event.path)
				.pipe(gulp.dest(path.join(OUT_DIR, fileDir)))
		}
		else {
			console.log('File ' + event.path + ' was ' + event.type + ', ********** running "gulp dev" again');
			gulp.start('dev');
		}
	});
});

gulp.task('webserver', ['watch'], function() {
	gulp.src(OUT_DIR)
	.pipe($.webserver({
		host: 'localhost',
		fallback: 'index.html',
		livereload: false,
		//open: 'http://localhost:8000' + appContext,
		proxies: [
			{source: '/domain', target: 'https://px-asset-lifecycle-manager-trilokee-gupta.run.aws-usw02-pr.ice.predix.io/domain'},
			{source: '/api', target: 'https://px-asset-lifecycle-manager-trilokee-gupta.run.aws-usw02-pr.ice.predix.io/api'}
		]
	}));
});


/*** MAIN TASKS ***/
gulp.task('default', ['dist']);
gulp.task('dist', ['clean', 'dist-index', 'copy-assets', 'minify-js', 'minify-less', 'minify-html']);
gulp.task('dev', ['clean', 'dev-index', 'copy-assets', 'copy-app', 'copy-bower', 'copy-less']);
