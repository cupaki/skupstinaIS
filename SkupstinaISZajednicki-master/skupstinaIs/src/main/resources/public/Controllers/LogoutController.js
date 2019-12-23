(function(angular) {

    'use strict'
    var app = angular.module('app');

    var logoutController = function($scope, $http, $rootScope, $location, AuthService, $state) {
        $scope.logout = function() {
          console.log('ovde');
            AuthService.logout(function(){
              $state.go('login');
            });
        }
    }

    app.controller('logoutController', logoutController);
})(angular)
