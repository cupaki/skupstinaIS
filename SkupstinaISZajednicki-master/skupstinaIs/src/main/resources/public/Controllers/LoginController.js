(function(angular) {
    'use strict'
    var app = angular.module('app');

    var loginController = function($scope, $http, $rootScope, $state, AuthService) {
        $scope.credentials = {}
        $scope.login = function() {
            AuthService.login($scope.credentials, function(result) {
                var user = result.data;
                switch (user.vrsta) {
                    case 'gradjanin':
                        $state.go('gradjanin.mainPage');
                        break;
                    case 'odbornik':
                        $state.go('odbornik.mainPage');
                        break;
                    case 'predsednik':
                        $state.go('predsednik.mainPage');
                        break;
                    default:
                }
            });
        }
    }
    
    var mainPageController = function($scope, $http, $rootScope, $state, AuthService) {
      
    	var user = {};
    	AuthService.getLoggedUser(function(userOnSession){
    		user = userOnSession;
    	})
    	
    	$scope.prikaziAmandmane = function(){
    		switch(user.vrsta){
			case 'odbornik' : 
				$state.go('odbornik.amandmani'); break;
			case 'predsednik' : 
				$state.go('predsednik.amandmani'); break;
			case 'gradjanin' : 
				$state.go('gradjanin.amandmani'); break;
			}
    	}
    	
    	$scope.kreirajPropis = function(){
    		switch(user.vrsta){
			case 'odbornik' : 
				$state.go('odbornik.newPropis'); break;
			case 'predsednik' : 
				$state.go('predsednik.newPropis'); break;
			}
    	}
    	
    	$scope.kreirajAmandman = function(){
    		switch(user.vrsta){
			case 'odbornik' : 
				$state.go('odbornik.propisiAmandmani'); break;
			case 'predsednik' : 
				$state.go('predsednik.propisiAmandmani'); break;
			}
    	}
    	
    	$scope.prikaziPropiseUProceduri = function(){
    		switch(user.vrsta){
			case 'odbornik' : 
				$state.go('odbornik.propisiProcedura'); break;
			case 'predsednik' : 
				$state.go('predsednik.propisiProcedura'); break;
			case 'gradjanin' : 
				$state.go('gradjanin.propisiProcedura'); break;
			}
    	}
    	
    	$scope.prikaziPropiseUsvojene = function(){
    		switch(user.vrsta){
			case 'odbornik' : 
				$state.go('odbornik.propisiUsvojeni'); break;
			case 'predsednik' : 
				$state.go('predsednik.propisiUsvojeni'); break;
			case 'gradjanin' : 
				$state.go('gradjanin.propisiUsvojeni'); break;
			}
    	}
    	
    	$scope.prikaziPropiseUsvojeneUNacelu = function(){
    		switch(user.vrsta){
			case 'odbornik' : 
				$state.go('odbornik.propisiUsvojeniUNacelu'); break;
			case 'predsednik' : 
				$state.go('predsednik.propisiUsvojeniUNacelu'); break;
			case 'gradjanin' : 
				$state.go('gradjanin.propisiUsvojeniUNacelu'); break;
			}
    		
    	}
    	
    	
    }
    
    

    app.controller('loginController', loginController)
    	.controller('mainPageController', mainPageController);

    app.config(function($stateProvider) {
        $stateProvider
            .state('login', {
                url: '/login',
                templateUrl: 'Templates/login.html',
                controller: 'loginController'
            })
            .state('predsednik.mainPage', {
                url: '/mainPage',
                templateUrl: 'Templates/predsednik/mainPage.html',
                controller: 'mainPageController'
            })
            .state('odbornik.mainPage', {
                url: '/mainPage',
                templateUrl: 'Templates/odbornik/mainPage.html',
                controller: 'mainPageController'
            })
            .state('gradjanin.mainPage', {
                url: '/mainPage',
                templateUrl: 'Templates/gradjanin/mainPage.html',
                controller: 'mainPageController'
            })
    });


})(angular);
