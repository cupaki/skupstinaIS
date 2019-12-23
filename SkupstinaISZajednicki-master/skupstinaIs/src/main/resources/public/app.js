(function(angular) {

    'use strict'
    var app = angular.module('app', ['ui.router', 'ngResource', 'ngSanitize', 'ngMaterial']);

    app.run(['$rootScope', '$state', 'AuthService', function($rootScope, $state, AuthService) {
        $rootScope.$on('$stateChangeStart', function(e, to, toParams, from) {
            var state = to.name;
            AuthService.getLoggedUser(function(user) {
                if (state === 'login' && user.username) { //ako pokusava da se loguje a vec je logovan
                    if (user.vrsta === 'predsednik') {
                        $state.go('predsednik')
                    } else if (user.vrsta === 'odbornik') {
                        $state.go('odbornik')
                    } else if (user.vrsta === 'gradjanin') {
                        $state.go('gradjanin')
                    }
                } else if (state === 'register' && user.username) {
                    if (user.vrsta === 'predsednik') {
                        $state.go('predsednik')
                    } else if (user.vrsta === 'odbornik') {
                        $state.go('odbornik')
                    } else if (user.vrsta === 'gradjanin') {
                        $state.go('gradjanin')
                    }
                } else if (to.data && to.data.gradjanin && user.vrsta !== 'gradjanin') {
                    if (!user.username) {
                        e.preventDefault();
                        $state.go('login');
                    } else {
                        e.preventDefault();
                        $state.go('error');
                    }
                } else if (to.data && to.data.odbornik && user.vrsta !== 'odbornik') {
                    if (!user.username) {
                        e.preventDefault();
                        $state.go('login');
                    } else {
                        e.preventDefault();
                        $state.go('error');
                    }
                } else if (to.data && to.data.predsednik && user.vrsta !== 'predsednik') {
                    if (!user.username) {
                        e.preventDefault();
                        $state.go('login');
                    } else {
                        e.preventDefault();
                        $state.go('error');
                    }
                } else if (state === 'login' && !user.username) {
                    $state.go('login');
                } else if (state === 'register' && !user.username) {
                    $state.go('register');
                } else if (to.data && to.data.gradjanin && user.vrsta !== 'gradjanin') {
                    switch (user.vrsta) {
                        case 'odbornik':
                            $state.go('odbornik.propisi')
                            break;
                        case 'predsednik':
                            $state.go('predsednik.propisi')
                            break;
                        default:

                    }
                } else if (to.data && to.data.odbornik && user.vrsta !== 'odbornik') {
                    switch (user.vrsta) {
                        case 'gradjanin':
                            $state.go('gradjanin.propisi')
                            break;
                        case 'predsednik':
                            $state.go('predsednik.propisi')
                            break;
                        default:

                    }
                } else if (to.data && to.data.predsednik && user.vrsta !== 'predsednik') {
                    switch (user.vrsta) {
                        case 'gradjanin':
                            $state.go('gradjanin.propisi')
                            break;
                        case 'odbornik':
                            $state.go('odbornik.propisi')
                            break;
                        default:

                    }
                }
            });


        });
    }]);

    app.config(function($stateProvider) {
        $stateProvider
            .state('register', {
                url: '/register',
                templateUrl: '/Templates/register.html',
                controller: 'registerController'
            })
            .state('dashboard', {
                url: '/dashboard',
                templateUrl: '/Templates/dashboard.html'
            })
            .state('gradjanin', {
                url: '/gradjanin',
                templateUrl: '/Templates/gradjanin/dashboard.html',
                data: {
                    gradjanin: true
                }
            })
            .state('odbornik', {
                url: '/odbornik',
                templateUrl: '/Templates/odbornik/dashboard.html',
                data: {
                    odbornik: true
                }
            })
            .state('predsednik', {
                url: '/predsednik',
                templateUrl: '/Templates/predsednik/dashboard.html',
                data: {
                    predsednik: true
                }
            })
            .state('error', {
                url: '/error',
                templateUrl: '/Templates/error.html'
            })
    })



})(angular)
