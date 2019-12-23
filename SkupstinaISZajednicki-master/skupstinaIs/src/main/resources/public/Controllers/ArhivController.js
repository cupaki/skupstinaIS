(function (angular) {
    var app = angular.module('app');

    var arhiviranjeController = function($scope, AmandmanService) {

        AmandmanService.getAmandmani(function(result) {

        })
    }
    app.config(function($stateProvider) {
      $stateProvider
      .state('predsednik.arhiv', {
        url: '/arhiviranje',
        templatesUrl: '/Templates/predsednik/arhiviranje.html',
        controller: 'arhiviranjeController'
      })
    })
})(angular);
