(function(angular) {
    'use strict'
    var app = angular.module('app');

    var navigationController = function($scope, AuthService, $state) {

        var user = {};
        AuthService.getLoggedUser(function(userOnSession) {
            user = userOnSession
        })

        $scope.viewPropisi = function() {
            switch (user.vrsta) {
                case 'gradjanin':
                    $state.go('gradjanin.propisi');
                    break;
                case 'odbornik':
                    $state.go('odbornik.propisi');
                    break;
                case 'predsednik':
                    $state.go('predsednik.propisi');
                    break;
                default:
            }
        }

        $scope.dodajPropis = function() {
            switch (user.vrsta) {
                case 'odbornik':
                    $state.go('odbornik.newPropis');
                    break;
                case 'predsednik':
                    $state.go('predsednik.newPropis');
                    break;
                default:
            }
        }
        $scope.napraviAmandman = function() {
            switch (user.vrsta) {
                case 'odbornik':
                    $state.go('odbornik.propisiAmandmani');
                    break;
                case 'predsednik':
                    $state.go('predsednik.propisiAmandmani');
                    break;
                default:
            }
        }
        
        $scope.viewAmandmani = function(){
        	 switch (user.vrsta) {
             case 'odbornik':
                 $state.go('odbornik.amandmani');
                 break;
             case 'predsednik':
                 $state.go('predsednik.amandmani');
                 break;
             case 'gradjanin':
                 $state.go('gradjanin.amandmani');
                 break;
             default:
         }
        }
        
        $scope.viewMain = function(){
        	 switch (user.vrsta) {
             case 'odbornik':
                 $state.go('odbornik.mainPage');
                 break;
             case 'predsednik':
                 $state.go('predsednik.mainPage');
                 break;
             case 'gradjanin':
                 $state.go('gradjanin.mainPage');
                 break;
             default:
         }
        	
        }
        

        $scope.search = function() {
            switch (user.vrsta) {
                case 'gradjanin':
                    $state.go('gradjanin.search')
                    break;
                case 'odbornik':
                    $state.go('odbornik.search')
                    break;
                case 'predsednik':
                    $state.go('predsednik.search')
                    break;
                default:
            }
        }


    }

    app.controller('navigationController', navigationController);
})(angular)
