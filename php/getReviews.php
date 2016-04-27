<?php
    $con=mysqli_connect("localhost","root","","gab");
      
    $rest_name = $_POST["table"];
    
    
    $statement = mysqli_prepare($con,"SELECT name,review FROM review,register,restaurant where review.uid = register.id and restaurant.r_tablename='$rest_name' and restaurant.r_id = review.rid ");
    //mysqli_stmt_bind_param($statement, "ss", $rest_name);
    mysqli_stmt_execute($statement);
    
    //$con->query($sql);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $name, $review );
    
    $search_result = array();
    
    while(mysqli_stmt_fetch($statement)){
        $search_result[] =  array('name' => $name , 'review'=> $review);
        
         
    }
    echo json_encode($search_result);
    
    
    mysqli_close($con);
?>