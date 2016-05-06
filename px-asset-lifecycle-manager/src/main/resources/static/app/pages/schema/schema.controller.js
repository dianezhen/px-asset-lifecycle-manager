angular.module('app.pages.schema.controller',['ui.router', 'app.services.schema'])

.config(function ($stateProvider) {

	$stateProvider
		.state('app.schema', {
			url: "schema",
			views: {
				'content': {
					templateUrl: 'app/pages/schema/schema.controller.html',
					controller: 'schemaController as schema'
				}
			}
		})
		.state('app.schema.detail', {
			url: "/:domain",
			views: {
				'schema_details': {
					templateUrl: 'app/pages/schema/schema.detail.controller.html',
					controller: 'schemaDetailController as schemaDetail'
				}
			}
		})
		.state('app.schema.new', {
			url: "/new/domain",
			views: {
				'schema_details': {
					templateUrl: 'app/pages/schema/schema.detail.controller.html',
					controller: 'schemaDetailController as schemaDetail'
				}
			}
		})
		
})

.controller('schemaController', function () {
	var schema = this;
})
.controller('schemaDetailController', function ($scope, $state, $stateParams, schemaManager) {
	var schemaDetail = this;
	
	schemaDetail.isNew = false;
	schemaDetail.domainObj = $scope.main.getDomainObject($stateParams.domain);
	
	if(schemaDetail.domainObj === null) {
		schemaDetail.isNew = true;
		schemaDetail.domainObj = {};
	}
	
	schemaDetail.create = function() {
		schemaManager.resource.addDomainSchema(schemaDetail.domainObj).$promise.then(function(data) {
			$scope.main.getDomains();
			$state.go('app.schema');
		}, function(error) {
			alert(error.data[0].message);
			console.log(error);
		});
	}
	schemaDetail.update = function() {
		schemaManager.resource.updateDomainSchema(schemaDetail.domainObj).$promise.then(function(data) {
			$scope.main.getDomains();
			$state.go('app.schema');
		}, function(error) {
			alert(error.data[0].message);
			console.log(error);
		});
	}
})