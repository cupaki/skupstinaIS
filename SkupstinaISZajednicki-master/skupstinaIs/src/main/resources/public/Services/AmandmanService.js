(function(angular){
	
	var app = angular.module('app');
	
	app.factory('AmandmanService', function($http, $resource){
		return {
			
			insertAmandmanSamoPropis : function(propis, callback){
				 $http.post('/api/amandman/novi', propis).then(function(result) {
	                    callback(result);
	             });
			},
			
			getAmandmanById : function(id, callback){
				var url = '/api/amandman/' + id;
				$http.get(url).then(function(result) {
	                    callback(result);
	             });
				
			},
			
			insertModifikovanAmandman : function(amandman, callback){
				 $http.post('/api/amandman/modifikovanAmandman', amandman).then(function(result) {
	                    callback(result);
	             });
			},
			
			getNumberOfAmandmans : function(callback){
				 $http.get('/api/amandman/brojAmandmana').then(function(result) {
	                    callback(result);
	             });
			},
			
			getAmandmani : function(callback){
				$http.get('/api/amandman/amandmani').then(function(result){
            		callback(result);
            	});
			},
			
			kreirajPDFFileAmandman : function(amandmanId, callback){
				
				var url = '/api/amandman/kreirajPDFFileAmandman/'+amandmanId;
            	$http.get(url).then(function(result){
            		callback(result);
            	});
			}
			
			
			
			
			
		}
		
	})

})(angular);