angular.module('team-bank', [
    'templates-app',
    'templates-common',
    'ui.router',
    'transaction'
])

    .config(['$urlRouterProvider', function ($urlRouterProvider) {

        $urlRouterProvider.otherwise('transaction');

    }])

    .controller("LogoutCtrl", ["$scope", '$http', 'windowService', function ($scope, $http, windowService) {

        $scope.logout = function () {
            $http.post("/logout")
                .success(function () {
                    windowService.redirect();
                });
        };
    }]);
    //TODO: need to implement service and move http in that service