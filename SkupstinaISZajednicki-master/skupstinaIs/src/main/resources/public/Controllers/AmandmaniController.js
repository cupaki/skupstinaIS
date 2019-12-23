(function(angular) {

    var newAmandmanController = function($scope, $http, $state, AmandmanService, PropisService, AuthService) {
        var user = {};
        AuthService.getLoggedUser(function(userOnSession) {
            user = userOnSession;
        })
        PropisService.getPropisiUsvojeniUNacelu(function(result) {
            $scope.propisi = result.data.propis;
        });
        $scope.getPropisById = function(id) {
            switch (user.vrsta) {
                case 'odbornik':
                    $state.go('odbornik.propis', {
                        id: id
                    });
                    break;
                case 'predsednik':
                    $state.go('predsednik.propis', {
                        id: id
                    });
                    break;
                default:
            }
        }

        $scope.insertAmandmanSamoPropis = function(propis) {
        	console.log('SDSAE');
        	console.log(propis);
            AmandmanService.insertAmandmanSamoPropis(propis, function(result) {
                var idAmandmana = result.data.id;
                switch (user.vrsta) {
                    case 'odbornik':
                        $state.go('odbornik.createAmandman', {
                            id: idAmandmana
                        });
                        break;
                    case 'predsednik':
                        $state.go('predsednik.createAmandman', {
                            id: idAmandmana
                        });
                        break;
                    default:
                }
            });
        }
    }

    var createAmandmanController = function($scope, $http, $state, $stateParams, AmandmanService, PropisService, AuthService) {

    	 var user = {};
         AuthService.getLoggedUser(function(userOnSession) {
             user = userOnSession;
         })
    	
        AmandmanService.getAmandmanById($stateParams.id, function(result) {
            $scope.amandman = result.data;
            $scope.propis = result.data.propis;
        })

        $scope.sacuvajPopunjenAmandman = function() {
        	 
        	 var user = {};
             AuthService.getLoggedUser(function(userOnSession) {
                 user = userOnSession;
             });

            var pokupljenAmandman = $scope.amandman;
            console.log($scope.amandman.id + ' ID KT');
            AmandmanService.getNumberOfAmandmans(function(result) {
                var redniBrojAmandmana = result.data;

                pokupljenAmandman.redniBroj = redniBrojAmandmana;
                pokupljenAmandman.datum = new Date();
                pokupljenAmandman.podnosilac = user.ime + " " + user.prezime;

                AmandmanService.insertModifikovanAmandman(pokupljenAmandman, function(result) {
                    //state treba ubaciti
                	
                	switch (user.vrsta) {
                    case 'odbornik':
                        $state.go('odbornik.amandman', {
                            id: $scope.amandman.id
                        });
                        break;
                    case 'predsednik':
                        $state.go('predsednik.amandman', {
                            id: $scope.amandman.id
                        });
                        break;
                    default:
                }
                });
            });
        }
         
         
         
         
    }
    
    
    
    var amandmaniController = function($scope, $http, $state, $stateParams, AmandmanService, PropisService, AuthService){
		
    	var user = {};
    	AuthService.getLoggedUser(function(userOnSession){
    		user = userOnSession;
    		
    		
    	})
    	
		AmandmanService.getAmandmani(function(result) {
			console.log(result.data.amandman);
			$scope.amandmani = result.data.amandman;
		});
		
		$scope.getAmandmanById = function(id){
			
			switch(user.vrsta){
			case 'odbornik' : 
				$state.go('odbornik.amandman', {id:id}); break;
			case 'predsednik' : 
				$state.go('predsednik.amandman', {id:id}); break;
			}
		}
		
		$scope.kreirajPDFFileAmandman = function(AmandmanId){
			
			AmandmanService.kreirajPDFFileAmandman(AmandmanId, function(){
    			console.log('KREIRAO FAJL');
    		});
		}
	}
	
    
	var amandmanController = function($scope, AmandmanService, $state, $stateParams, PropisService) {
		
		AmandmanService.getAmandmanById($stateParams.id, function(result) {
			$scope.amandman = result.data;
		//	$scope.htmlXsl = result.data;
		});
		
		$scope.prihvatiAmandman = function(amandman){
			console.log(' U KONTROLLERU SAM');
			PropisService.prihvatiAmandman(amandman.id, function(result){
				console.log('Zavrsio prihvatanje amandmana u propis');
				
			});
		}
		
		$scope.odbijAmandman = function(amandman){ 
			PropisService.odbijAmandman(amandman.id, function(result){
				console.log('Zavrsio odbijanje amandmana');
			})
		}
	}


    var app = angular.module('app');
    app.controller('newAmandmanController', newAmandmanController)
        .controller('createAmandmanController', createAmandmanController)
        .controller('amandmaniController', amandmaniController)
        .controller('amandmanController', amandmanController);
    
    app.config(function($stateProvider) {
        $stateProvider

            .state('odbornik.propisiAmandmani', {
                url: '/propisiAmandmani',
                templateUrl: '/Templates/odbornik/propisiAmandmani.html',
                controller: 'newAmandmanController'
            })
            .state('predsednik.propisiAmandmani', {
                url: '/propisiAmandmani',
                templateUrl: '/Templates/predsednik/propisiAmandmani.html',
                controller: 'newAmandmanController'
            })
            .state('odbornik.createAmandman', {
                url: '/createAmandman/:id',
                templateUrl: '/Templates/odbornik/createAmandman.html',
                controller: 'createAmandmanController'
            })
            .state('predsednik.createAmandman', {
                url: '/createAmandman/:id',
                templateUrl: '/Templates/predsednik/createAmandman.html',
                controller: 'createAmandmanController'
            })
            
            
            .state('predsednik.amandmani',{
				url : '/amandmani',
				templateUrl : '/Templates/predsednik/amandmani.html',
				controller : 'amandmaniController'
            })
			.state('predsednik.amandman',{
				url : '/amandman/:id',
				templateUrl : '/Templates/predsednik/amandman.html',
				controller : 'amandmanController'
			})
			.state('odbornik.amandmani',{
				url : '/amandmani',
				templateUrl : '/Templates/odbornik/amandmani.html',
				controller : 'amandmaniController'
            })
			.state('odbornik.amandman',{
				url : '/amandman/:id',
				templateUrl : '/Templates/odbornik/amandman.html',
				controller : 'amandmanController'
			})
			.state('gradjanin.amandmani',{
				url : '/amandmani',
				templateUrl : '/Templates/gradjanin/amandmani.html',
				controller : 'amandmaniController'
            })
			.state('gradjanin.amandman',{
				url : '/amandman/:id',
				templateUrl : '/Templates/gradjanin/amandman.html',
				controller : 'amandmanController'
			})


    })
})(angular);