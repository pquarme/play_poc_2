if (window.console) {
	console.log("Welcome to your Play application's JavaScript!");
}

var app = angular.module('sito2', [ 'ngRoute', 'ngAnimate' ]);

app.config(function($routeProvider, $locationProvider) {
	$routeProvider.when('/', {
		templateUrl : '/assets/html/index.html',
		controller : 'AppController'
	}).when('/view/json', {
		templateUrl : '/assets/html/jsonRequest.html',
		controller : 'JsonController'
	}).when('/view/users', {
		templateUrl : '/assets/html/userInfo.html',
		controller : 'UserController'
	}).when('/view/metar', {
		templateUrl : '/assets/html/metarRequest.html',
		controller : 'MetarController'
	}).otherwise({
		redirectTo : '/'
	});

	// configure html5 to get links working
	// If you don't do this, you URLs will be base.com/#/home rather than
	// base.com/home
	$locationProvider.html5Mode(true);
});

app.controller('AppController', function($scope, $location) {
	$scope.message = "message!";

	$scope.usersAjax = [];
	$scope.searchQuery = "";
	
	$scope.getUserList = function() {
		$location.path('/view/users');	

	};	
	
	$scope.getJson = function(){
		//console.log($scope.searchQuery);		
		$location.search('query', $scope.searchQuery);
		$location.path('/view/json');			
	};
	
	$scope.getMetar = function(){
		//console.log($scope.searchQuery);	
		$location.search('metar', $scope.locQuery);
		$location.path('/view/metar');			
	};
	
	$scope.getActorList = function(){

		getRoute = jsRoutes.controllers.Application.getMySqlDb();
		$scope.actorsList = {};
		
		getRoute.ajax( ).done(function(res) { 
			console.log(res);
			$scope.actorsList = res;
			//console.log($scope.jsonData);
			/*makes angular notice any changes 
			 * readmore@http://jimhoskins.com/2012/12/17/angularjs-and-apply.html */		 
			$scope.$apply(); 
			//alert("hit");
		}).fail(function(res) { 
			//console.log(res.responseText);
			alert("error"); });
		//console.log($scope.searchQuery);		
		//$location.path('/view/actors');	
		$scope.showTable = true;
	};

});


app.controller('UserController', function($scope) {
	//$scope.userData = new Array();
	var route = jsRoutes.controllers.Application.getUsers();
	
	route.ajax( ).done(function(res) { 
		//console.log(res.md5);
		//$scope.userData.fName = res.fName;
		//$scope.userData.lName = res.lName; 	
		//$scope.userData.occupation = res.occupation; 
		//var t = eval("("+res+")");
		
		//console.log("eval:  " + t);
		//console.log(res);
		//$scope.userData.push(res);
		//angular.forEach(t., function(u) {
		//$scope.userData.push(u);
		//});
		$scope.userData = res;
		console.log($scope.userData);
		
		/*makes angular notice any changes 
		 * readmore@http://jimhoskins.com/2012/12/17/angularjs-and-apply.html */
		$scope.$apply(); 
		//alert("hit");
	}).fail(function(res) { 
		//console.log(res.responseText);
		alert("error"); });
	
});

app.controller('JsonController', function($scope, $routeParams) {
	$scope.query = $routeParams.query;
	var getRoute = jsRoutes.controllers.Application.getJson($scope.query);
	$scope.jsonData = {};
	
	getRoute.ajax( ).done(function(res) { 
		//console.log(res.md5);
		$scope.jsonData.md5 = res.md5;
		$scope.jsonData.original = res.original; 	
		console.log($scope.jsonData);
		/*makes angular notice any changes 
		 * readmore@http://jimhoskins.com/2012/12/17/angularjs-and-apply.html */		 
		$scope.$apply(); 
		//alert("hit");
	}).fail(function(res) { 
		//console.log(res.responseText);
		alert("error"); });
	
});

app.controller('MetarController', function($scope, $routeParams) {
	$scope.locQuery  = $routeParams.metar;
	var getRoute = jsRoutes.controllers.Application.getMetar($scope.locQuery);
	console.log(getRoute);
	$scope.metar = {};
	
	getRoute.ajax( ).done(function(res) { 
		//console.log(res);
		$scope.metar.station = res.station;
		$scope.metar.temp = res.temp;
		$scope.metar.windSpeed = res.windSpeed; 	
		$scope.metar.windDir = res.windDir;
		$scope.metar.elevation = res.elevation;
		//console.log($scope.jsonData);
		/*makes angular notice any changes 
		 * readmore@http://jimhoskins.com/2012/12/17/angularjs-and-apply.html */		 
		$scope.$apply(); 
		//alert("hit");
	}).fail(function(res) { 
		//console.log(res.responseText);
		alert("error"); });
	
});