<?php
	$con = mysqli_connect("localhost","root","","gab");
	$uid = $_POST["uid"];
	$rid = $_POST["rid"];
	$review = $_POST["review"];	
		
	
	

	  $sql = "INSERT INTO review (uid, rid, review) VALUES ('$uid', '$rid', '$review')";
  if ($con->query($sql) === TRUE) {
    echo "New record created successfully";
    
	
} else {
    echo "error";
}
  mysqli_close($con);
?>
?>