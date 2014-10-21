describe("logout behavior", function () {

    beforeEach(module('team-bank'));


    describe('logoutService', function () {

        var _logoutService, _requestService;

        beforeEach(function () {
            _requestService = {sendRequest: jasmine.createSpy()};

            module(function ($provide) {
                $provide.value('requestService', _requestService);
            });

            inject(function ($injector) {
                _logoutService = $injector.get('logoutService');
            });

        });


        it('should redirect to login', function () {
            _logoutService.logout();

            expect(_requestService.sendRequest).toHaveBeenCalledWith("POST", "/logout");
        });
    });
});