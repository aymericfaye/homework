function SearchController($scope, $http) {
    $scope.results = [];

    $scope.init = function(initial_search) {
        $scope.text = initial_search
        $scope.submit()
    }

    $scope.submit = function() {
        $scope.results = [];
        $scope.search_input = $scope.text;

        $http.get("/search/" + $scope.text).success(function(data) {
            $scope.results = data;
        }).error(function(data) {
            $scope.results = [ "No project found." ];
        });
    };
}

function StatsController($scope, $http) {
    $scope.stats = []
    $scope.stats.users = [];
    $scope.stats.timeline = [];

    $scope.get_stats = function(request) {
        $scope.stats.users = [];
        $scope.stats.timeline = [];

        $http.get("/api/users/" + request).success(function(data) {
            $scope.stats.users = data;
        });

        $http.get("/api/commits/" + request).success(function(data) {
            $scope.stats.timeline = data;
        });
    };
}
