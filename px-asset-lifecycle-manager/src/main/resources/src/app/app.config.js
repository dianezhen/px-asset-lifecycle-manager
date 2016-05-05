angular.module('app.config',[])

.provider('config', function() {
	var config = this;
	
	config.apiBasePath = "/" + window.location.pathname.split( '/' )[1] + "/";
	
	this.$get = function() {
		return config;
	};
});

