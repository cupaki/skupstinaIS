/**
 *
 */
(function(angular) {
    var app = angular.module('app');

    app.factory('PropisService', function($http, $resource) {
        return {
            insertPropis: function(propis, callback) {
                $http.post('/api/propis/novi', propis).then(function() {
                    callback();
                });
            },
            
            insertPropisBezGlave : function(propis, callback) {
                $http.post('/api/propis/noviBezGlave', propis).then(function() {
                    callback();
                });
            },
            
            insertGlava : function(glava, callback){
            	$http.post('/api/propis/novi/dodajGlavu', glava).then(function() {
                    callback();
                });
            },
            
            insertDeo: function(deo, callback){
            	$http.post('/api/propis/novi/dodajDeo', deo).then(function(){
            		callback();
            	});
            },
            
            insertDeoBezGlave : function(deo, callback){
            	$http.post('/api/propis/novi/dodajDeoBezGlave', deo).then(function(){
            		callback();
            	});
            	
            },
            
            insertClan: function(clan, callback){
            	$http.post('/api/propis/novi/dodajClan', clan).then(function(){
            		callback();
            	});
            },
            
            insertStav : function(stav, callback){
            	$http.post('/api/propis/novi/dodajStav', stav).then(function(){
            		callback();
            	});
            },
            
            getPropis: function(callback){
            	$http.get('/api/propis/novi/prikaziPropis').then(function(result){
            		callback(result);
            	});
            },
            
            savePropis: function(callback){
            	$http.get('/api/propis/novi/save').then(function(){
            		callback();
            	});
            },
            
            getPropisi: function(callback){
            	$http.get('/api/propis/propisi').then(function(result){
            		callback(result);
            	});
            },
            
            getPropisiUProceduri : function(callback){
            	$http.get('/api/propis/propisiProcedura').then(function(result){
            		callback(result);
            	});
            },
            
            getPropisiUsvojeni : function(callback){
            	$http.get('/api/propis/propisiUsvojeni').then(function(result){
            		callback(result);
            	});
            },
            
            getPropisiUsvojeniUNacelu : function(callback){
            	$http.get('/api/propis/propisiUsvojeniUNacelu').then(function(result){
            		callback(result);
            	});
            },
            
            usvojiPropis : function(propisId, callback){
            	var url = '/api/propis/propisUsvoj/'+propisId;

            	$http.get(url).then(function(result){
            		callback(result);
            	});
            },
            
            usvojiPropisUNacelu : function(propisId, callback){
            	var url = '/api/propis/propisUsvojUNacelu/'+propisId;
            	$http.get(url).then(function(result){
            		callback(result);
            	});
            	
            },
            
            odbijPropis : function(propisId, callback){
            	var url = '/api/propis/propisOdbij/'+propisId;

            	$http.get(url).then(function(result){
            		callback(result);
            	});
            	
            },
            
            getPropisById: function(propisId, callback){
            	var url = '/api/propis/'+propisId;
            	console.log('PRE ');
            	$http.get(url).then(function(result) {
            		console.log('PRE RESULT');
            		console.log(result);
            		callback(result);
            	}, function(result){
            		console.log('ERROR CALLBACK');
            		console.log(result);

            	})
            },
            
            getBrojStava : function(callback){
            	 $http.get('/api/propis/brojStava').then(function(result) {
	                    callback(result);
	             });
            },
            
            prihvatiAmandman : function(amandmanId, callback){
            	var url = '/api/propis/propisUsvojAmandman/'+amandmanId;
            	$http.get(url).then(function(result){
            		callback(result);
            	});
            },
            
            odbijAmandman : function(amandmanId, callback){
            	var url = '/api/propis/propisOdbijAmandman/'+amandmanId;
            	$http.get(url).then(function(result){
            		callback(result);
            	});
            },
            
            kreirajPDFFilePropis : function(propisId, callback){
            	var url = '/api/propis/kreirajPDFFilePropis/'+propisId;
            	$http.get(url).then(function(result){
            		callback(result);
            	});
            }
            
            
        }
    })
})(angular)
