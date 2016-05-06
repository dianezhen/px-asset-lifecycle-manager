angular.module('app', [
'ui.router',
'app.pages.schema.controller',
'app.pages.objects.controller',
'app.services.schema'
])

.config(function ($httpProvider, $stateProvider, $locationProvider, $urlRouterProvider) {

	//$locationProvider.html5Mode(true);
	$urlRouterProvider.otherwise("schema");

	$stateProvider
		.state('app', {
			url: "/",
			abstract: true,
			resolve: {
				currentDomains: function(schemaManager) {
					return schemaManager.domains.$promise;
				}
			},
			views: {
				'': {
					templateUrl: 'app/app.template.html',
					controller: 'mainController as main'
				}
			}
		})
		
		
	var defaultTransformResponse = $httpProvider.defaults.transformResponse[0];
	
	function myTRSdefaultTransform(data, headers, status) {
		//success codes, allow transform to json
		if(200 == status && (
			data.indexOf('created.') > -1 ||
			data.indexOf('updated.') > -1
		)) {
			return data;
		}
		else {
			return defaultTransformResponse(data, headers);
		}
	}
	
	//overwrite the defaults transform
	$httpProvider.defaults.transformResponse = [myTRSdefaultTransform];
})

.controller('mainController', function ($rootScope, schemaManager) {
	var main = this;
	
	
	//Global application object
	window.App = $rootScope.App = {
		version: '1.0',
		name: 'Predix Seed',
		session: {},
		tabs: [
            {
              "path": "/schema", "icon": "fa-sitemap", "label":"Schema"
            },
            {
              "icon": "fa-cubes", "label": "Objects",
              "subitems": []
            }
          ]
	};
	
	main.getDomains = function() {
		
		main.domains = schemaManager.resource.getDomainSchemas();
		
		main.setNav();
	}
	main.setNav = function() {
		
		main.domains.$promise.then(function() {
			$rootScope.App.tabs[1].subitems = [];
			main.domains.forEach(function(element) {
				$rootScope.App.tabs[1].subitems.push({"label": element.domainName, "path" : "/objects/"+element.domainName})
			}, this);
		});
	}
	
	main.getDomainObject = function(name) {
		var selectedDomain = null;
		main.domains.forEach(function(element) {
			if(element.domainName === name) {
				selectedDomain = element;
			}
		}, this);
		
		return selectedDomain;
	}
	
	main.domains = schemaManager.domains;
	main.setNav();
 	
})
