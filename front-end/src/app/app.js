/**
 * Created by emil on 14-10-21.
 */
angular.module('team-bank', [
    'templates-app',
    'templates-common',
    'ui.router',
    'transaction'
])

    .config(['$urlRouterProvider', function ($urlRouterProvider) {

        $urlRouterProvider.otherwise('transaction');

    }])

    .service('logoutService', ['requestService', function (requestService) {

        return{
            logout: function () {
                return requestService.sendRequest('POST', '/logout');
            }
        };
    }])

    .controller("LogoutCtrl", ["$scope", 'windowService','logoutService', function ($scope, windowService, logoutService) {

        $scope.logout = function () {
            logoutService.logout().then(function () {
                windowService.redirect();
            });
        };
    }]);