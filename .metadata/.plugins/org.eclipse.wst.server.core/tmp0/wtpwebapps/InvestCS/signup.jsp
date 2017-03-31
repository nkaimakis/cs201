<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Invest CS</title>
</head>
<script>
	function validate() {
		var xhttp = new XMLHttpRequest();
		xhttp.open("GET",
				"${pageContext.request.contextPath}/CreateUserServlet?firstname="
						+ document.getElementById("firstname").value
						+ "&lastname="
						+ document.getElementById("lastname").value
						+ "&username="
						+ document.getElementById("username").value
						+ "&password="
						+ document.getElementById("password").value
						+ "&email=" + document.getElementById("email").value,
				false);
		xhttp.send();
		if (xhttp.responseText.trim().length > 0) {
			document.getElementById("formerror").innerHTML = xhttp.responseText;
			return false;
		}
		return true;
	};
</script>
<body>
	<h1>Create your new account!</h1>

	<form name="myform" method="GET" action="login.jsp"
		onsubmit="return validate();">
		<div id="formerror"></div>
		<div>First Name</div>
		<input type="text" id="firstname" placeholder="First Name" /> <br>
		<div>Last Name</div>
		<input type="text" id="lastname" placeholder="Last Name" />
		<div>Email</div>
		<input type="text" id="email" placeholder="Email" /> <br>
		<div>Username</div>
		<input type="text" id="username" placeholder="Username" /> <br>
		<div>Password</div>
		<input type="text" id="password" placeholder="Password" />
		<br>
		<button type="submit" id="createAccount">Submit</button>
	</form>
	

</body>
</html>