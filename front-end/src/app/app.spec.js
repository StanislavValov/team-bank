xdescribe("logout behavior", function () {

    var scope, http, controller, _windowService;

    beforeEach(module('team-bank'));


    beforeEach(inject(function ($rootScope, $http, $controller, $injector) {

        scope = $rootScope.$new();

        http = $http;

        _windowService = $injector.get('windowService');

//        spyOn(_windowService, 'redirect');

        controller = $controller('LogoutCtrl', {
            '$scope': scope,
            '$http': http,
            'windowService': _windowService
        });
    }));

    it("should be redirected to login page", function () {

        scope.logout();

        scope.$apply();

        expect(_windowService.redirect).toHaveBeenCalled();
    });
});