<?php
	$con = mysqli_connect("localhost","root","","gab");
	$username = $_POST["username"];
	$email = $_POST["email"];
	$password = $_POST["password"];	
	$name= $_POST["name"];	
	
	
/*$statement = mysqli_prepare($con, "INSERT INTO register (username, name, password, email) VALUES ( $username, $name, $password, $email)") or die(mysqli_error($con));
	//mysqli_stmt_bind_param($statement, "ssssssssssssssssssssssssss", $username, $name, $password, $email);
	
mysqli_stmt_execute($statement);
	mysqli_stmt_close($statement);
	mysqli_close($con);*/
	  $sql = "INSERT INTO register (username, name, password, email) VALUES ('$username', '$name', '$password', '$email')";
  if ($con->query($sql) === TRUE) {
    echo "New record created successfully";
    //$to = "app@novahro.com";
  	/*$subject = "NEW USER REGISTERED";	
    $mail_body = " Thank you for choosing GrabABite! You have successfully been registered";
	$headers  = "From:DONOTREPLY@grababite.com\n";
	$headers .= "Content-type: text\n";
	mail($email, $subject, $mail_body, $headers);*/
} else {
    echo "Error: " . $sql . "<br>" . $con->error;
}
  mysqli_close($con);
?>
?>