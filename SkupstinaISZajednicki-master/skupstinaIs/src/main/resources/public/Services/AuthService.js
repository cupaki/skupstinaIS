
(function(angular) {
  'use strict'
  var app = angular.module('app');

  app.factory('AuthService', function($http) {
    return {
      login: function(userParams, callback) {
        $http.post('/api/auth/login', userParams).then(function(result) {
            callback(result);
        }, function() {
          console.log('Greska');
        });
      },
      logout : function(callback) {
        $http.get('/api/auth/logout').then(function() {
          callback();
        })
      },
      getLoggedUser : function(callback) {
        $http.get('/api/auth/loggedUser').then(function(result){
          callback(result.data)
        })
      }
    }
  })
})(angular)
