angular.module('app.services.object',['ngResource','app.config'])

.factory('object', function($resource, config) {

	var _actions = {
		getObjectsByDomain: {
			method: 'GET',
			url: 'api/adm/v1/viewAssets',
			params: {
				domain: '@domain'
			},
			isArray:true
		},
		addObjectForDomain: {
			method: 'POST',
			headers: {
				'Content-Type':'application/json'
			},
			url: 'api/adm/v1/createAssets',
			params: {
				domain: '@domain'
			},
			data: {} //you have data present for content-type header to be applied
		}
		// updateDomainSchema: {
		// 	method: 'PUT',
		// 	headers: {
		// 		'Content-Type':'application/x-www-form-urlencoded'
		// 	},
		// 	url: 'domain',
		// 	params: {
		// 		domainName: '@domainName',
		// 		schema: '@schema'
		// 	},
		// 	data: {} //you have data present for content-type header to be applied
		// }
	};
	var _resource = $resource("domain", {}, _actions);

	return function() {
		return _resource
	}
})

.service('objectManager', function (object) {
	var objectManager = this;
	
	objectManager.resource = new object();
})