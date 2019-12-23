var gulp = require('gulp');
var jscs = require('gulp-jscs');
var nodemon = require('gulp-nodemon');

var jsFiles = ['*.js', 'src/**/*.js'];


gulp.task('inject', function() {
    var wiredep = require('wiredep').stream;
    var inject = require('gulp-inject');
    var options = {
        bowerJson: require('./bower.json'),
        directory: './public/lib',
        ignorePath: '../../public'
    };

    var injectSrc = gulp.src(['./*.js', './Controllers/*.js', './Services/*.js'], {read: false});
    var injectOptions = {
        ignorePath: '/public'
    };

    return gulp.src('./*.html')
    .pipe(wiredep(options))
    .pipe(inject(injectSrc, injectOptions))
    .pipe(gulp.dest('./'));
});
