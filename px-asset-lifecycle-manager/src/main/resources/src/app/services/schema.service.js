angular.module('app.services.schema',['ngResource','app.config'])

.factory('schema', function($resource, config) {

	var _actions = {
		getDomainSchemas: {
			method: 'GET',
			url: 'domain',
			isArray:true
		},
		addDomainSchema: {
			method: 'POST',
			headers: {
				'Content-Type':'application/x-www-form-urlencoded'
			},
			url: 'domain',
			params: {
				domainName: '@domainName',
				schema: '@schema'
			},
			data: {} //you have data present for content-type header to be applied
		},
		updateDomainSchema: {
			method: 'PUT',
			headers: {
				'Content-Type':'application/x-www-form-urlencoded'
			},
			url: 'domain',
			params: {
				domainName: '@domainName',
				schema: '@schema'
			},
			data: {} //you have data present for content-type header to be applied
		}
	};
	var _resource = $resource("domain", {}, _actions);

	return function() {
		return _resource
	}
})

.service('schemaManager', function (schema) {
	var schemaManager = this;
	
	schemaManager.resource = new schema();
	
	schemaManager.domains = schemaManager.resource.getDomainSchemas();
})