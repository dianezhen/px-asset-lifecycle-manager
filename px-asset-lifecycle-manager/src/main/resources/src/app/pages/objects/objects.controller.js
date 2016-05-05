angular.module('app.pages.objects.controller',['ui.router', 'schemaForm', 'app.services.object'])

.config(function ($stateProvider) {

	$stateProvider
		.state('app.objects', {
			url: "objects/:domain",
			views: {
				'content': {
					templateUrl: 'app/pages/objects/objects.controller.html',
					controller: 'objectsController as objects'
				}
			}
		})
		.state('app.objects.detail', {
			url: "/:uri",
			views: {
				'object_details': {
					templateUrl: 'app/pages/objects/objects.detail.controller.html',
					controller: 'objectDetailsController as objectDetails'
				}
			}
		})
		.state('app.objects.new', {
			url: "/new/instance",
			views: {
				'object_details': {
					templateUrl: 'app/pages/objects/objects.detail.controller.html',
					controller: 'objectDetailsController as objectDetails'
				}
			}
		})
		.state('app.objects.bulk', {
			url: "/new/bulk",
			views: {
				'object_details': {
					templateUrl: 'app/pages/objects/objects.bulk.controller.html',
					controller: 'objectBulkController as objectBulk'
				}
			}
		})
})

.controller('objectsController', function ($scope, $stateParams, objectManager) {
	var objects = this;
	
	objects.domainObj = $scope.main.getDomainObject($stateParams.domain);
	
	objects.loadInstance = function(uriDisplay) {
		objects.instances = objectManager.resource.getObjectsByDomain({domain: objects.domainObj.domainName});
	}
	objects.loadInstance();
	
	objects.getCurrentInstance = function(uriDisplay) {
		var uri = objects.getURIfromDisplay(uriDisplay);
		var selectedInstance = null;
		objects.instances.forEach(function(element) {
			if(element.uri === uri) {
				selectedInstance = angular.copy(element);
			}
		}, this);
		
		return selectedInstance;
	}
	objects.getDisplayURI = function(uri) {
		return uri.replace('/' + objects.domainObj.domainName + '/', '');
	}
	objects.getURIfromDisplay = function(uri) {
		return  '/' + objects.domainObj.domainName + '/' + uri;
	}
})

.controller('objectDetailsController', function ($scope, $state, $stateParams, objectManager) {
	var objectDetails = this;
	
	objectDetails.isNew = false;
	objectDetails.model = $scope.objects.getCurrentInstance($stateParams.uri);
	
	if(objectDetails.model === null) {
		objectDetails.isNew = true;
		objectDetails.model = {};
	}
	
	try {
		objectDetails.schema = JSON.parse($scope.objects.domainObj.schema);
	}
	catch(exception) {
		alert('Bad Schema');
		$state.go('app.objects', {domain: $scope.objects.domainObj.domainName});
		return;
	}

	objectDetails.form = ["*"];

	objectDetails.save = function() {
		var saveModel = angular.copy(objectDetails.model);
		if(saveModel.uriDisplay) {
			saveModel.uri = $scope.objects.getURIfromDisplay(saveModel.uriDisplay);
			delete saveModel.uriDisplay;
		}
		console.log(saveModel);
		
		objectManager.resource.addObjectForDomain({domain: $scope.objects.domainObj.domainName}, [saveModel]).$promise.then(function(data) {
			$state.go('app.objects', {domain: $scope.objects.domainObj.domainName},{reload: true});
		}, function(error) {
			alert(error.data.message);
			console.log(error);
		});
		
	}
})

.controller('objectBulkController', function ($scope, $state, $stateParams, objectManager) {
	var objectBulk = this;
	
	objectBulk.save = function() {
		var saveModel = {};
		try {
			saveModel = JSON.parse(objectBulk.model);
		}
		catch(exception) {
			alert('Unable to parse JSON');
			return;
		}
	
		
		objectManager.resource.addObjectForDomain({domain: $scope.objects.domainObj.domainName}, saveModel).$promise.then(function(data) {
			$state.go('app.objects', {domain: $scope.objects.domainObj.domainName},{reload: true});
		}, function(error) {
			alert(error.data.message);
			console.log(error);
		});
		
	}
})