(function(angular) {

    var app = angular.module('app');

    var searchAmandmanController = function($scope, SearchService) {
        $scope.searchEntity = {
            usvojenost : "",
            mesto: "",
            datum: "",
            redniBroj: "",
            podnosilac: "",
            text: ""
        }
        $scope.amandmani = {};

        $scope.search = function() {
          console.log('Search entity');
          SearchService.searchAmandnan($scope.searchEntity, function(result){
            console.log('nakon pretrage');
            console.log(result.data);
            $scope.amandmani = result.data;
            console.log($scope.amandmani);
          });
        }
    }

    var searchPropisiContoller = function($scope, SearchService) {
      $scope.searchEntity = {
          usvojenost : "",
          naziv: "",
          id: "",
          text: ""
      }
      $scope.bilosta = {};
      $scope.search = function() {

        SearchService.searchPropisi($scope.searchEntity, function(result) {
          console.log('Pretraga propisa');
          console.log(result.data);
          $scope.bilosta = result.data;
        })
    }
  }

    app.controller('searchAmandmanController', searchAmandmanController)
        .controller('searchPropisiContoller', searchPropisiContoller);

    app.config(function($stateProvider) {
        $stateProvider
            .state('odbornik.search', {
                url: '/search',
                templateUrl: '/Templates/search.html'
            })
            .state('gradjanin.search', {
                url: '/search',
                templateUrl: '/Templates/search.html'
            })
            .state('predsednik.search', {
                url: '/search',
                templateUrl: '/Templates/search.html'
            })
    })
})(angular)
