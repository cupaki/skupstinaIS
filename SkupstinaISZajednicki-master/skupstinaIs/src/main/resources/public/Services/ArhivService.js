(function(angular) {

    'use strict'
    var app = angular.module('app');

    app.factory('ArhivService', function($http, $resource) {
        return {
            arhiviraj: function(dataToSend, callback) {
                console.log('saljem na arhiv');
                console.log(dataToSend);
                var config = {
                    method: 'POST',
                    url: 'http://localhost:8080/api/arhiviranje/',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    data: dataToSend
                };

                $http(config).success(function(data) {
                    callback(data);
                })

            }
        }

    })

})(angular);
