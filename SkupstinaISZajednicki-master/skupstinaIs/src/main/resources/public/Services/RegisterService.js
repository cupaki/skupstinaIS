(function(angular) {

  'use strict'
  var app = angular.module('app');

  app.factory('RegisterService', function($http) {
    return {
      registerUser : function(user, callback, errorCallback){
        console.log(user);
        $http.post('/api/auth/signUp', user).then(function(result) {
          if(result.status===200) {
            callback();
          } else {
            errorCallback();
          }
        }, function() {
          console.log('Greska');
        })
      }
    }
  })
})(angular)
