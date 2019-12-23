(function(angular) {
    'use strict'
    var newPropisContoller = function($scope, $http, $state, PropisService, AuthService) {

        $scope.hideClanove = true;
        $scope.hideStav = true;
        var user = {};
        AuthService.getLoggedUser(function(userOnSession) {
            user = userOnSession;
        })
        PropisService.getPropisi(function(result) {
            $scope.propisi = result.data.propis;
        });
        $scope.getPropisById = function(id) {
            switch (user.vrsta) {
                case 'gradjanin':
                    $state.go('gradjanin.propis', {
                        id: id
                    })
                    break;
                case 'predsednik':
                    $state.go('predsednik.propis', {
                        id: id
                    })
                    break;
                case 'odbornik':
                    $state.go('odbornik.propis', {
                        id: id
                    })
                    break;
                default:

            }
        }

        $scope.dodajStav = function() {
            if ($scope.hideStav === false) {
                $scope.hideStav = true;
            } else {
                $scope.hideStav = false;
            }
        }

        $scope.insertPropis = function() {
            var propis = {
                naziv: $scope.propis_naziv,
                deo: {
                    naziv: $scope.deo_naziv,
                    glava: {
                        naziv: $scope.glava_naziv,
                        clan: {
                            naziv: $scope.clan_naziv,
                            opis: $scope.clan_opis,
                            sadrzaj: {
                             /*   stav: {

                                    tekst: $scope.stav_tekst
                                },*/
                                tekst: $scope.sadrzaj_tekst
                            }
                        }
                    }

                }

            }
            $scope.propis = propis;
            PropisService.insertPropis(propis, function() {
                switch (user.vrsta) {
                    case 'odbornik':
                        $state.go('odbornik.addToPropis');
                        break;
                    case 'predsednik':
                        $state.go('predsednik.addToPropis');
                        break;
                    default:
                }
            })
        }

        $scope.insertPropisBezGlave = function() {

            var propis = {
                naziv: $scope.propis_naziv,
                deo: {
                    naziv: $scope.deo_naziv,
                    clan: {
                        naziv: $scope.clan_naziv,
                        opis: $scope.clan_opis,
                        sadrzaj: {
                           /* stav: {
                                tekst: $scope.stav_tekst
                            },*/
                            tekst: $scope.sadrzaj_tekst
                        }
                    }
                }
            }

            $scope.propis = propis;
            PropisService.insertPropisBezGlave(propis, function() {
                switch (user.vrsta) {
                    case 'odbornik':
                        $state.go('odbornik.addToPropis');
                        break;
                    case 'predsednik':
                        $state.go('predsednik.addToPropis');
                        break;
                    default:
                }
            })
        }

        $scope.sakrijClanove = function() {
            if ($scope.hideClanove === false) {
                $scope.hideClanove = true;
            } else {
                $scope.hideClanove = false;
            }

        }


        $scope.dodajReferencuNaOvajClan = function(clan, glava, deo, propis) {
            //	$scope.sadrzaj_tekst = $scope.sadrzaj_tekst + ' &lt;a&gt;' + clan.naziv + '&lt;/a&gt;'
        	
        	if($scope.sadrzaj_tekst){
	            $scope.sadrzaj_tekst = $scope.sadrzaj_tekst + ' Prema clanu:' +
	                //	' <jump xlink:href="localhost:8000/#/clan/' + clan.naziv + '/' + propis.id + '" xlink:type="simple" xlink:show="replace" >' + clan.naziv + ' </jump>' ;
	                ' "' + clan.naziv + '", propisa:' + ' "' + propis.naziv + '"';
        	}
        	else
        		{
        		 $scope.sadrzaj_tekst = ' Prema clanu:' +
	                //	' <jump xlink:href="localhost:8000/#/clan/' + clan.naziv + '/' + propis.id + '" xlink:type="simple" xlink:show="replace" >' + clan.naziv + ' </jump>' ;
	                ' "' + clan.naziv + '", propisa:' + ' "' + propis.naziv + '"';
        		
        		}
        }

        $scope.informacijeOClanu = function(clan, glava, deo, propis) {
            var pr = propis.id;
            switch (user.vrsta) {
                case 'gradjanin':
                    $state.go('gradjanin.clan', {
                        id: clan.naziv,
                        propis: pr
                    });
                    break;
                case 'odbornik':
                    $state.go('odbornik.clan', {
                        id: clan.naziv,
                        propis: pr
                    });
                    break;
                case 'predsednik':
                    $state.go('predsednik.clan', {
                        id: clan.naziv,
                        propis: pr
                    });
                    break;
                default:

            }


        }


    };

    var addStuffToPropisController = function($scope, $state, PropisService, AuthService) {
        var user = {};
        AuthService.getLoggedUser(function(userOnSession) {
            user = userOnSession;
        })

        PropisService.getPropis(function(result) {
            $scope.propis = result.data;
        });

        $scope.addDeo = function() {
            switch (user.vrsta) {
                case 'odbornik':
                    $state.go('odbornik.addDeo')
                    break;
                case 'predsednik':
                    $state.go('predsednik.addDeo')
                    break;
                default:

            }

        }

        $scope.addClan = function() {
            switch (user.vrsta) {
                case 'odbornik':
                    $state.go('odbornik.addClan')
                    break;
                case 'predsednik':
                    $state.go('predsednik.addClan')
                    break;
                default:
            }
        }

        $scope.addGlava = function() {
            switch (user.vrsta) {
                case 'odbornik':
                    $state.go('odbornik.addGlava')
                    break;
                case 'predsednik':
                    $state.go('predsednik.addGlava')
                    break;
                default:
            }
        }

        $scope.addStav = function() {

            switch (user.vrsta) {
                case 'odbornik':
                    $state.go('odbornik.addStav')
                    break;
                case 'predsednik':
                    $state.go('predsednik.addStav')
                    break;
                default:
            }
        }


        $scope.save = function() {

            PropisService.savePropis(function() {
                switch (user.vrsta) {
                    case 'odbornik':
                        $state.go('odbornik.propisi');
                        break;
                    case 'predsednik':
                        $state.go('predsednik.propisi');
                        break;
                    default:
                }
            });

        }
    }

    var addGlavaController = function($scope, PropisService, $state, AuthService) {

        var user = {};
        AuthService.getLoggedUser(function(userOnSession) {
            user = userOnSession;
        })

        PropisService.getPropisi(function(result) {
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




        $scope.hideStav = true;
        $scope.hideTekst = true;
        $scope.hideClanove = true;

        $scope.dodajTekst = function() {
            if ($scope.hideTekst === false) {
                $scope.hideTekst = true;
            } else {
                $scope.hideTekst = false;
            }
        }

        $scope.dodajStav = function() {

            if ($scope.hideStav === false) {
                $scope.hideStav = true;
            } else {
                $scope.hideStav = false;
            }
        }

        $scope.sakrijClanove = function() {

            if ($scope.hideClanove === false) {
                $scope.hideClanove = true;
            } else {
                $scope.hideClanove = false;
            }

        }

        $scope.insertGlava = function() {
            var glava = {
                naziv: $scope.glava_naziv,
                clan: {
                    naziv: $scope.clan_naziv,
                    opis: $scope.clan_opis,
                    sadrzaj: {
                       /* stav: {

                            tekst: $scope.stav_tekst
                        },*/
                        tekst: $scope.sadrzaj_tekst
                    }
                }
            }

            PropisService.insertGlava(glava, function() {
                switch (user.vrsta) {
                    case 'odbornik':
                        $state.go('odbornik.addToPropis')
                        break;
                    case 'predsednik':
                        $state.go('predsednik.addToPropis');
                        break;
                    default:
                }
            });
        }


        $scope.dodajReferencuNaOvajClan = function(clan, glava, deo, propis) {

        	if($scope.sadrzaj_tekst){
	            $scope.sadrzaj_tekst = $scope.sadrzaj_tekst + ' Prema clanu:' +
	                //	' <jump xlink:href="localhost:8000/#/clan/' + clan.naziv + '/' + propis.id + '" xlink:type="simple" xlink:show="replace" >' + clan.naziv + ' </jump>' ;
	                ' "' + clan.naziv + '", propisa:' + ' "' + propis.naziv + '"';
        	}
        	else
        		{
        		 $scope.sadrzaj_tekst = ' Prema clanu:' +
	                //	' <jump xlink:href="localhost:8000/#/clan/' + clan.naziv + '/' + propis.id + '" xlink:type="simple" xlink:show="replace" >' + clan.naziv + ' </jump>' ;
	                ' "' + clan.naziv + '", propisa:' + ' "' + propis.naziv + '"';
        		
        		}
        }

        $scope.informacijeOClanu = function(clan, glava, deo, propis) {
            var pr = propis.id;
            switch (user.vrsta) {
                case 'odbornik':
                    $state.go('odbornik.clan', {
                        id: clan.naziv,
                        propis: pr
                    })
                    break;
                case 'predsednik':
                    $state.go('predsednik.clan', {
                        id: clan.naziv,
                        propis: pr
                    })
                    break;
                default:
            }

        }

    }

    var addDeoController = function($scope, PropisService, $state, AuthService) {

        $scope.hideClanove = true;
        $scope.hideStav = true;
        var user = {};

        AuthService.getLoggedUser(function(userOnSession) {
            user = userOnSession;
        })
        $scope.insertDeo = function() {
            var deo = {
                naziv: $scope.deo_naziv,
                glava: {
                    naziv: $scope.glava_naziv,
                    clan: {
                        naziv: $scope.clan_naziv,
                        opis: $scope.clan_opis,
                        sadrzaj: {
                         /*   stav: {

                                tekst: $scope.stav_tekst
                            },*/
                            tekst: $scope.sadrzaj_tekst
                        }
                    }
                }
            }

            PropisService.insertDeo(deo, function() {
                switch (user.vrsta) {
                    case 'odbornik':
                        $state.go('odbornik.addToPropis');
                        break;
                    case 'predsednik':
                        $state.go('predsednik.addToPropis');
                        break;
                    default:
                }

            });
        }
        
        
        $scope.insertDeoBezGlave = function(){
        	
        	 var deoBezGlave = {
                     naziv: $scope.deo_naziv,
                         clan: {
                             naziv: $scope.clan_naziv,
                             opis: $scope.clan_opis,
                             sadrzaj: {
                              /*   stav: {

                                     tekst: $scope.stav_tekst
                                 },*/
                                 tekst: $scope.sadrzaj_tekst
                             }
                         }
                 }

                 PropisService.insertDeoBezGlave(deoBezGlave, function() {
                     switch (user.vrsta) {
                         case 'odbornik':
                             $state.go('odbornik.addToPropis');
                             break;
                         case 'predsednik':
                             $state.go('predsednik.addToPropis');
                             break;
                         default:
                     }

                 });
        	
        	
        	
        }

        $scope.dodajStav = function() {

            if ($scope.hideStav === false) {
                $scope.hideStav = true;
            } else {
                $scope.hideStav = false;
            }
        }

        $scope.sakrijClanove = function() {
            if ($scope.hideClanove === false) {
                $scope.hideClanove = true;
            } else {
                $scope.hideClanove = false;
            }

        }

        PropisService.getPropisi(function(result) {
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

        $scope.dodajReferencuNaOvajClan = function(clan, glava, deo, propis) {
        	if($scope.sadrzaj_tekst){
	            $scope.sadrzaj_tekst = $scope.sadrzaj_tekst + ' Prema clanu:' +
	                //	' <jump xlink:href="localhost:8000/#/clan/' + clan.naziv + '/' + propis.id + '" xlink:type="simple" xlink:show="replace" >' + clan.naziv + ' </jump>' ;
	                ' "' + clan.naziv + '", propisa:' + ' "' + propis.naziv + '"';
        	}
        	else
        		{
        		 $scope.sadrzaj_tekst = ' Prema clanu:' +
	                //	' <jump xlink:href="localhost:8000/#/clan/' + clan.naziv + '/' + propis.id + '" xlink:type="simple" xlink:show="replace" >' + clan.naziv + ' </jump>' ;
	                ' "' + clan.naziv + '", propisa:' + ' "' + propis.naziv + '"';
        		
        		}
        }

        $scope.informacijeOClanu = function(clan, glava, deo, propis) {
            var pr = propis.id;
            switch (user.vrsta) {
                case 'odbornik':
                    $state.go('odbornik.clan', {
                        id: clan.naziv,
                        propis: pr
                    });
                    break;
                case 'predsednik':
                    $state.go('predsednik.clan', {
                        id: clan.naziv,
                        propis: pr
                    });
                    break;
                default:
            }
        }
    };

    var addClanController = function($scope, PropisService, $state, AuthService) {

        $scope.hideClanove = true;
        var user = {};
        AuthService.getLoggedUser(function(userOnSession) {
            user = userOnSession;
        })
        $scope.insertClan = function() {
            var clan = {
                naziv: $scope.clan_naziv,
                opis: $scope.clan_opis,
                sadrzaj: {
                   /* stav: {

                        tekst: $scope.stav_tekst
                    },*/
                    tekst: $scope.sadrzaj_tekst
                }
            }

            PropisService.insertClan(clan, function() {
                switch (user.vrsta) {
                    case 'odbornik':
                        $state.go('odbornik.addToPropis');
                        break;
                    case 'predsednik':
                        $state.go('predsednik.addToPropis');
                        break;
                    default:

                }
            });
        }


        $scope.sakrijClanove = function() {
            if ($scope.hideClanove === false) {
                $scope.hideClanove = true;
            } else {
                $scope.hideClanove = false;
            }

        }

        PropisService.getPropisi(function(result) {
            $scope.propisi = result.data.propis;
        });
        $scope.getPropisById = function(id) {
            switch (user.vrsta) {
                case 'gradjanin':
                    $state.go('gradjanin.propis', {
                        id: id
                    });
                    break;
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

        $scope.dodajReferencuNaOvajClan = function(clan, glava, deo, propis) {
        	if($scope.sadrzaj_tekst){
	            $scope.sadrzaj_tekst = $scope.sadrzaj_tekst + ' Prema clanu:' +
	                //	' <jump xlink:href="localhost:8000/#/clan/' + clan.naziv + '/' + propis.id + '" xlink:type="simple" xlink:show="replace" >' + clan.naziv + ' </jump>' ;
	                ' "' + clan.naziv + '", propisa:' + ' "' + propis.naziv + '"';
        	}
        	else
        		{
        		 $scope.sadrzaj_tekst = ' Prema clanu:' +
	                //	' <jump xlink:href="localhost:8000/#/clan/' + clan.naziv + '/' + propis.id + '" xlink:type="simple" xlink:show="replace" >' + clan.naziv + ' </jump>' ;
	                ' "' + clan.naziv + '", propisa:' + ' "' + propis.naziv + '"';
        		
        		}
        }

        $scope.informacijeOClanu = function(clan, glava, deo, propis) {
            var pr = propis.id;
            switch (user.vrsta) {
                case 'gradjanin':
                    $state.go('gradjanin.clan', {
                        id: clan.naziv,
                        propis: pr
                    });
                    break;
                case 'odbornik':
                    $state.go('odbornik.clan', {
                        id: clan.naziv,
                        propis: pr
                    });
                    break;
                case 'predsednik':
                    $state.go('predsednik.clan', {
                        id: clan.naziv,
                        propis: pr
                    });
                    break;
                default:
            }
        }
    };

    var addStavController = function($rootScope, $scope, PropisService, $state, AuthService) {

        var user = {};
        AuthService.getLoggedUser(function(userOnSession) {
            user = userOnSession;
        })


        $scope.insertStav = function() {
            PropisService.getBrojStava(function(result) {
                var redniBrojStava = result.data;
                console.log('REDNI BROJ');
                console.log(redniBrojStava);
                var stav = {
                    redniBroj: redniBrojStava,
                    tekst: $scope.stav_tekst
                }


                PropisService.insertStav(stav, function() {
                    switch (user.vrsta) {
                        case 'odbornik':
                            $state.go('odbornik.addToPropis');
                            break;
                        case 'predsednik':
                            $state.go('predsednik.addToPropis');
                            break;
                        default:

                    }
                });

            })

        }
    }

    var propisiController = function($rootScope, $scope, PropisService, $state, AuthService) {
        var user = {};
        AuthService.getLoggedUser(function(userOnSession) {
            user = userOnSession;
        })
        PropisService.getPropisi(function(result) {
            $scope.propisi = result.data.propis;
        });
        $scope.getPropisById = function(id) {
            switch (user.vrsta) {
                case 'gradjanin':
                    $state.go('gradjanin.propis', {
                        id: id
                    })
                    break;
                case 'predsednik':
                    $state.go('predsednik.propis', {
                        id: id
                    })
                    break;
                case 'odbornik':
                    $state.go('odbornik.propis', {
                        id: id
                    })
                    break;
                default:

            }
        }

    }

    var propisiUProceduriController = function($rootScope, $scope, PropisService, $state, AuthService) {

        var user = {};
        AuthService.getLoggedUser(function(userOnSession) {
            user = userOnSession;
        })

        PropisService.getPropisiUProceduri(function(result) {
            $scope.propisiUProceduri = result.data.propis;
        });


        /*   $scope.usvojiPropis = function(propis){

        	 PropisService.usvojiPropis(propis.id, function(result){
        		 console.log(" Usvojio propis");

	        		switch (user.vrsta) {
	                 case 'odbornik':
	                     $state.go('odbornik.propisiProcedura');
	                     break;
	                 case 'predsednik':
	                     $state.go('predsednik.propisiProcedura');
	                     break;
	        		 }
        	 });
         }*/

        $scope.usvojiPropisUNacelu = function(propis) {

            PropisService.usvojiPropisUNacelu(propis.id, function(result) {
                console.log(" Usvojio propis u nacelu");
                switch (user.vrsta) {
                    case 'odbornik':
                        $state.go('odbornik.propisiUsvojeniUNacelu');
                        break;
                    case 'predsednik':
                        $state.go('predsednik.propisiUsvojeniUNacelu');
                        break;
                }

            });
        }

        $scope.odbijPropis = function(propis) {
            PropisService.odbijPropis(propis.id, function(result) {
                console.log(" odbio propis");

                switch (user.vrsta) {
                    case 'odbornik':
                        $state.go('odbornik.propisiProcedura');
                        break;
                    case 'predsednik':
                        $state.go('predsednik.propisiProcedura');
                        break;
                }
            });

        }


    }

    var propisiUsvojeniUNaceluController = function($rootScope, $scope, PropisService, $state, AuthService, ArhivService) {

        var user = {};
        AuthService.getLoggedUser(function(userOnSession) {
            user = userOnSession;
        });

        PropisService.getPropisiUsvojeniUNacelu(function(result) {
            console.log(result.data.propis);
            $scope.propisiUsvojeniUNacelu = result.data.propis;
        });

        $scope.usvojiPropisUCelini = function(propis) {

            //NAPOMENA: Gde god pise 'usvojenPropis', misli se na 'usvojenPropisUCelini'
            //prvo sam ovako uradio, pa da ne bih menjao na 10 mesta, onda sam ovako ostavio da pise
            PropisService.usvojiPropis(propis.id, function(result) {
                console.log(" Usvojio propis u celini");
                $scope.propisiUsvojeniUCelini = result.data.propis;
                /*ArhivService.arhiviraj(propis, function(data) {
                  console.log('Arhiviran');
                  console.log(data);
                })*/
                switch (user.vrsta) {
                    case 'odbornik':
                        $state.go('odbornik.propisiUsvojeni');
                        break;
                    case 'predsednik':
                        $state.go('predsednik.propisiUsvojeni');
                        break;
                }
            });

        }

    }

    


    var propisiUsvojeniController = function($rootScope, $scope, PropisService, $state, AuthService){
    	PropisService.getPropisiUsvojeni(function(result) {
	         console.log(result.data.propis);
	         $scope.propisiUsvojeniUCelini = result.data.propis;
     });


    	$scope.kreirajPDFFile = function(propisId){

    		PropisService.kreirajPDFFilePropis(propisId, function(){
    			console.log('KREIRAO FAJL');
    		});

    	}
}





    var propisController = function($rootScope, $scope, PropisService, $state, $stateParams) {

        PropisService.getPropisById($stateParams.id, function(result) {
            //$scope.propis = result.data;
            $scope.htmlXsl = result.data;
        });
    }

    var clanController = function($scope, PropisService, $state, $stateParams) {

        var propisId = $stateParams.propis;
        var nazivClana = $stateParams.id;

        PropisService.getPropisById(propisId, function(result) {

            for (var i = 0; i < result.data.deo.length; i++) {
                for (var j = 0; j < result.data.deo[i].glava.length; j++) {
                    for (var a = 0; a < result.data.deo[i].glava[j].clan.length; a++) {
                        if (result.data.deo[i].glava[j].clan[a].naziv == nazivClana) {
                            $scope.clan = result.data.deo[i].glava[j].clan[a];
                            break;
                        }

                    }
                }
            }
        })

    }

    var app = angular.module('app');

    app.controller('newPropisContoller', newPropisContoller)
        .controller('addStuffToPropisController', addStuffToPropisController)
        .controller('addGlavaController', addGlavaController)
        .controller('addDeoController', addDeoController)
        .controller('addClanController', addClanController)
        .controller('propisiController', propisiController)
        .controller('propisController', propisController)
        .controller('clanController', clanController)
        .controller('addStavController', addStavController)
        .controller('propisiUProceduriController', propisiUProceduriController)
        .controller('propisiUsvojeniController', propisiUsvojeniController)
        .controller('propisiUsvojeniUNaceluController', propisiUsvojeniUNaceluController);

    app.config(function($stateProvider) {
        $stateProvider
            .state('odbornik.newPropis', {
                url: '/newPropis',
                templateUrl: '/Templates/odbornik/newPropis.html',
                controller: 'newPropisContoller'
            })
            .state('predsednik.newPropis', {
                url: '/newPropis',
                templateUrl: '/Templates/predsednik/newPropis.html',
                controller: 'newPropisContoller'
            })
            .state('odbornik.addToPropis', {
                url: '/addToPropis',
                templateUrl: '/Templates/odbornik/addToPropis.html',
                contoller: 'addStuffToPropisController'
            })
            .state('predsednik.addToPropis', {
                url: '/addToPropis',
                templateUrl: '/Templates/predsednik/addToPropis.html',
                contoller: 'addStuffToPropisController'
            })
            .state('odbornik.addGlava', {
                url: '/addGlava',
                templateUrl: 'Templates/odbornik/addGlava.html',
                contoller: 'addGlavaController'
            })
            .state('predsednik.addGlava', {
                url: '/addGlava',
                templateUrl: 'Templates/predsednik/addGlava.html',
                contoller: 'addGlavaController'
            })
            .state('odbornik.addDeo', {
                url: '/addDeo',
                templateUrl: 'Templates/odbornik/addDeo.html',
                controller: 'addDeoController'
            })
            .state('predsednik.addDeo', {
                url: '/addDeo',
                templateUrl: 'Templates/predsednik/addDeo.html',
                controller: 'addDeoController'
            })
            .state('odbornik.addClan', {
                url: '/addClan',
                templateUrl: 'Templates/odbornik/addClan.html',
                controller: 'addClanController'
            })
            .state('predsednik.addClan', {
                url: '/addClan',
                templateUrl: 'Templates/predsednik/addClan.html',
                controller: 'addClanController'
            })
            .state('odbornik.addStav', {
                url: '/addStav',
                templateUrl: 'Templates/odbornik/addStav.html',
                controller: 'addStavController'
            })
            .state('predsednik.addStav', {
                url: '/addStav',
                templateUrl: 'Templates/predsednik/addStav.html',
                controller: 'addStavController'
            })
            .state('odbornik.propisi', {
                url: '/propisi',
                templateUrl: 'Templates/odbornik/propisi.html',
                controller: 'propisiController'
            })
            .state('predsednik.propisi', {
                url: '/propisi',
                templateUrl: 'Templates/predsednik/propisi.html',
                controller: 'propisiController'
            })
            .state('gradjanin.propisi', {
                url: '/propisi',
                templateUrl: 'Templates/gradjanin/propisi.html',
                controller: 'propisiController'
            })
            .state('odbornik.propis', {
                url: '/propis/:id',
                templateUrl: 'Templates/odbornik/propisXsl.html',
                controller: 'propisController'
            })
            .state('predsednik.propis', {
                url: '/propis/:id',
                templateUrl: 'Templates/predsednik/propisXsl.html',
                controller: 'propisController'
            })
            .state('gradjanin.propis', {
                url: '/propis/:id',
                templateUrl: 'Templates/gradjanin/propisXsl.html',
                controller: 'propisController'
            })
            .state('odbornik.clan', {
                url: '/clan/:id/:propis',
                templateUrl: 'Templates/odbornik/clan.html',
                controller: 'clanController'
            })
            .state('predsednik.clan', {
                url: '/clan/:id/:propis',
                templateUrl: 'Templates/predsednik/clan.html',
                controller: 'clanController'
            })

        .state('odbornik.propisiProcedura', {
                url: '/propisiProcedura',
                templateUrl: 'Templates/odbornik/propisiProcedura.html',
                controller: 'propisiUProceduriController'
            })
            .state('predsednik.propisiProcedura', {
                url: '/propisiProcedura',
                templateUrl: 'Templates/predsednik/propisiProcedura.html',
                controller: 'propisiUProceduriController'
            })
            .state('gradjanin.propisiProcedura', {
                url: '/propisiProcedura',
                templateUrl: 'Templates/gradjanin/propisiProcedura.html',
                controller: 'propisiUProceduriController'
            })

        .state('odbornik.propisiUsvojeni', {
                url: '/propisiUsvojeni',
                templateUrl: 'Templates/odbornik/propisiUsvojeniUCelini.html',
                controller: 'propisiUsvojeniController'
            })
            .state('predsednik.propisiUsvojeni', {
                url: '/propisiUsvojeni',
                templateUrl: 'Templates/predsednik/propisiUsvojeniUCelini.html',
                controller: 'propisiUsvojeniController'
            })
            .state('gradjanin.propisiUsvojeni', {
                url: '/propisiUsvojeni',
                templateUrl: 'Templates/gradjanin/propisiUsvojeniUCelini.html',
                controller: 'propisiUsvojeniController'
            })

        .state('predsednik.propisiUsvojeniUNacelu', {
                url: '/propisiUsvojeniUNacelu',
                templateUrl: 'Templates/predsednik/propisiUsvojeniUNacelu.html',
                controller: 'propisiUsvojeniUNaceluController'
            })
            .state('gradjanin.propisiUsvojeniUNacelu', {
                url: '/propisiUsvojeniUNacelu',
                templateUrl: 'Templates/gradjanin/propisiUsvojeniUNacelu.html',
                controller: 'propisiUsvojeniUNaceluController'
            })
            .state('odbornik.propisiUsvojeniUNacelu', {
                url: '/propisiUsvojeniUNacelu',
                templateUrl: 'Templates/odbornik/propisiUsvojeniUNacelu.html',
                controller: 'propisiUsvojeniUNaceluController'
            })


    })
})(angular);
