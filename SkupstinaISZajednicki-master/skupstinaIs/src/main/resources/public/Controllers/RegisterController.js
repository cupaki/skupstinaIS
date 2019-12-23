(function(angular) {
    'use strict'
    var app = angular.module('app');

    var registerGradjaninController = function($scope, RegisterService, $state) {
        $scope.user = {vrsta: 'gradjanin'};
        $scope.register = function() {
            RegisterService.registerUser($scope.user, function() {
                $state.go('login');
            }, function(){
              $state.go('register')
            });
        }
    }



    app.controller('registerController', registerGradjaninController);



})(angular)
