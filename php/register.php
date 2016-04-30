<?php
	$con = mysqli_connect("localhost","root","","gab");
	$username = $_POST["username"];
	$email = $_POST["email"];
	$password = $_POST["password"];	
	$name= $_POST["name"];	
	
	

	  $sql = "INSERT INTO register (username, name, password, email) VALUES ('$username', '$name', '$password', '$email')";
  if ($con->query($sql) === TRUE) {
    echo "New record created successfully";
    
	mail($email, $subject, $mail_body, $headers);*/
} else {
    echo "Error: " . $sql . "<br>" . $con->error;
}
  mysqli_close($con);
?>
?>