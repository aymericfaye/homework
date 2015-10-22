function SearchController($scope, $http) {
    $scope.results = [];

    $scope.init = function(initial_search) {
        $scope.text = initial_search
        $scope.submit()
    }

    $scope.submit = function() {
        $scope.results = [];
        $scope.search_input = $scope.text;
        var request = $http({
            method : 'GET',
            url : "/search/" + $scope.text,
        }).success(function(data, status) {
            $scope.results = data;
        }).error(function(arg) {
            $scope.results = [ "No project found."];
        });
    };
}
