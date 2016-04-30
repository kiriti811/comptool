var app = angular.module('blog',['ngRoute','ui.bootstrap']);

app.config(function($routeProvider){
	$routeProvider
	.when('/login', {
	templateUrl: 'Partials/login.html',
	controller: 'loginController'
      })
     .when('/adminHome', {
	templateUrl: 'Partials/AdminHome.html',
	controller: 'courseController'
      })
      .when('/facultyhome', {
	templateUrl: 'Partials/FacultyHome.html'
	//controller: 'evaluationController'
      })
      .when('/studenthome', {
	templateUrl: 'Partials/StudentHome.html',
	controller: 'practiceTestController'
      })
       .when('/superAdmin', {
	templateUrl: 'Partials/superAdminHome.html',
	controller: 'universityController'
      })
      .when('/message', {
	templateUrl: 'Partials/message.html'})

});
//Login Controller
app.controller('loginController',function($scope,$http,$location){
	$scope.login = function(){
		$http({
			method: 'POST',
			url:'/ucmCompTool/loginForm',
			headers:{'Content-Type':'application/json'},
			data:$scope.user
		}).success(function(data){
			$scope.message =data;
			if($scope.message.isValid==true){
				if($scope.message.role=="Admin")
					$location.path( "/adminHome" );
				else if($scope.message.role=="Faculty")
					$location.path( "/facultyhome" );
				else if($scope.message.role=="Student")
					$location.path( "/studenthome" );
				else if($scope.message.role=="SuperAdmin")
					$location.path( "/superAdmin" );
				
			}
		});
	}
});
//Course Controller
app.controller('courseController',function($scope,$http,$location){
	 var url = '/ucmCompTool/addCourses';
	 $http.get(url).success(function(result) {  
	        $scope.instructors = result.instructors;
	        console.log('val'+$scope.instructors);
	        
	    })  
	    
	    $scope.clicked = function(){
		 
		 console.log("inside clicked");
	 }
	    
	$scope.addCourse = function(){
		$http({
			method: 'POST',
			url:'/ucmCompTool/addCourses',
			headers:{'Content-Type':'application/json'},
			data:$scope.course
		}).success(function(data){
			$scope.message=data;
			console.log($scope.message.isValid);
			if($scope.message.isValid==true){
				console.log("success");				
			}
		});
	}
});
//Register Controller
app.controller('registerController',function($scope,$http,$location){
	
	$scope.register = function(){
		$http({
			method: 'POST',
			url:'/ucmCompTool/registerForm',
			headers:{'Content-Type':'application/json'},
			data:$scope.user
		}).success(function(data){
			$scope.message=data;
			console.log("data : "+data);
			console.log($scope.message.isValid);
			if($scope.message.isValid==true){
				$scope.status.isValid=true;
				console.log("success");
				$location.path( "/adminHome" );
			}
		});
	}
});
//Department Controller
app.controller('deptController',function($scope,$http,$location){
	$scope.addDept = function(){
		$http({
			method: 'POST',
			url:'/ucmCompTool/addDepartment',
			headers:{'Content-Type':'application/json'},
			data:$scope.dept
		}).success(function(data){
			$scope.message=data;
			console.log("data : "+data);
			console.log($scope.message.isValid);
			if($scope.message.isValid==true){
				console.log("success");				
			}
		});
	}
});

//Evaluation Controller
app.controller('evaluationController', function($scope,$http,$location,$route) {
	
	$scope.reload = function(){
		$route.reload();
		
	}
	 var url = '/ucmCompTool/evaluationForm';  
	 $scope.courses;
	    $http.get(url).success(function(result) {  
	        $scope.courses = result.courses;
	        $scope.reports = result.reports;
	        console.log('val'+$scope.reports);
	    })  
	    $scope.getOutcome = function(selectedCourse){
	    	console.log("selectedCourse"+selectedCourse);
	    	$http({
				method: 'GET',
				url:'/ucmCompTool/evaluationForm',
				headers:{'Content-Type':'application/json'},
				params:{course:selectedCourse}
			}).success(function(data){
				$scope.message=data;
				$scope.courseOutcomes =$scope.message.outcomes;
				console.log($scope.courseOutcomes);
				if($scope.message.isValid==true){
					console.log("success");				
				}
			});
			 $scope.showOutcome=true;
		 }
	    $scope.addEvaluation = function(){
			$http({
				method: 'POST',
				url:'/ucmCompTool/postQuestion',
				headers:{'Content-Type':'application/json'},
				data:$scope.evaluation
			}).success(function(data){
				$scope.message=data;
				console.log($scope.message.isValid);
				if($scope.message.isValid==true){
					console.log("success");
					$location.path("/message")
									
				}
			});
	    }
	
});

app.controller('universityController',function($scope,$http){
	var url = '/ucmCompTool/universityReport';  
	   $http.get(url).success(function(result) {  
	        $scope.reports = result.reports;
	        console.log('val'+$scope.reports);
	    }) 
});

app.controller('practiceTestController', function($scope,$http,$location) {	 
	var url =    '/ucmCompTool/getQuestions'
		$scope.message=null;
	$http.get(url).success(function(result) {  
	        $scope.courses = result.courses;
	        console.log('val'+$scope.courses);
	    })  
	$scope.selected_ids = [];
	$scope.getQuestions = function(selectedCourse){
		console.log("selectedCourse"+selectedCourse);
		 $http({
				method: 'GET',
				url:'/ucmCompTool/getQuestions',
				headers:{'Content-Type':'application/json'},
				params:{course:selectedCourse}
			}).success(function(result) { 
				console.log("data"+result)
				$scope.questionPaper = result;
		        $scope.options = result.questions.options;
		        $scope.question = result.questions.question1;
		        console.log("options"+$scope.options);
		        console.log("question"+$scope.question)
		    });
	}
	
	$scope.submitPracticeTest=function(){
		
		angular.forEach($scope.questionPaper.questions, function(question) {
		      $scope.selected_ids.push(question.selected_id);
		});
		 $http({
				method: 'POST',
				url:'/ucmCompTool/getQuestions',
				headers:{'Content-Type':'application/json'},
				data:{options:$scope.selected_ids,course:$scope.selectedCourse}
			}).success(function(result) { 
				console.log("data"+result)
				$scope.count = result.count;
				console.log("count"+$scope.count);
				$scope.message = "Total questions correct : "+$scope.count;
				$scope.questionPaper=null;
		    });
		
		
	}      
});

app.controller('takeTestController', function($scope,$http,$location) {	 
	var url =    '/ucmCompTool/TakeTests'
		$scope.message=null;
	$scope.selected_ids = [];
	$scope.getQuestions = function(){
		 $http({
				method: 'GET',
				url:'/ucmCompTool/TakeTests',
				headers:{'Content-Type':'application/json'},
			}).success(function(result) { 
				console.log("data"+result)
				$scope.questionPaper = result;
		        $scope.options = result.questions.options;
		        $scope.question = result.questions.question1;
		        console.log("options"+$scope.options);
		        console.log("question"+$scope.question)
		    });
	}
	
	$scope.submitPracticeTest=function(){
		
		angular.forEach($scope.questionPaper.questions, function(question) {
		      $scope.selected_ids.push(question.selected_id);
		});
		 $http({
				method: 'POST',
				url:'/ucmCompTool/TakeTests',
				headers:{'Content-Type':'application/json'},
				data:{options:$scope.selected_ids}
			}).success(function(result) { 
				console.log("data"+result)
				$scope.count = result.count;
				console.log("count"+$scope.count);
				$scope.message = "Total questions correct : "+$scope.count;
				$scope.questionPaper=null;
		    });
		
		
	}      
});






/*app.controller('DatepickerDemoCtrl', function($scope,$filter) {
  $scope.today = function() {
    $scope.dt = new Date();
    $scope.out = new Date();

  };
  $scope.today();

  $scope.clear = function () {
    $scope.dt = null;
  };

  // Disable weekend selection
  $scope.disabled = function(date, mode) {
  	var sysdate = new Date();
  	var today = $filter('date')(sysdate,'MM/dd/yyyy');
  	var selectedDate = $filter('date')(date,'MM/dd/yyyy');

if(selectedDate<today)
{
	return (mode==='day')
}
  	 $scope.diff =  yearDiff(sysdate,date);
  	 if($scope.diff>90){
    return ( mode === 'day');
    }
  };

  $scope.toggleMin = function() {
    $scope.minDate = $scope.minDate ? null : new Date();
  };
  $scope.toggleMin();

  $scope.open = function($event) {
    $event.preventDefault();
    $event.stopPropagation();

    $scope.opened = true;
  };

  $scope.dateOptions = {
    formatYear: 'yy',
    startingDay: 1
  };

  $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
  $scope.format = $scope.formats[0];

   
   function yearDiff(first, second) {
  if(second && first){
    return Math.floor((second-first)/(86400000))-1;
  }
  else
  {
    return 0;

  }
}
});*/