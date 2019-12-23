(function(angular) {

  'use strict'
  var app = angular.module('app');

  app.factory('SearchService', function($http) {
    return {
      searchAmandnan : function(searchEntity, callback) {
          $http.post('/api/search/amandmani', searchEntity).then(function(result) {
            callback(result);
          });
      },
      searchPropisi : function(searchEntity, callback) {
        $http.post('/api/search/propisi', searchEntity).then(function(result){
          callback(result);
        })
      }
    }
  })
})(angular)
