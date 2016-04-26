<?php
    $con=mysqli_connect("localhost","root","","gab");
      
	$table ="cafeteria";// $_POST["table"];
    
    
    
    $statement = mysqli_prepare($con,"SELECT * FROM {$table} ");
    //mysqli_stmt_bind_param($statement, "ss", $rest_name);
    mysqli_stmt_execute($statement);
    
    //$con->query($sql);
     mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $dish_name, $dish_price);
    
    $budget_result = array();
    
    while(mysqli_stmt_fetch($statement)){
        $budget_result[] = array('dish_name' => $dish_name , 'dish_price'=> $dish_price);
        
        
    }
    echo json_encode($budget_result);
    
    mysqli_close($con);
 
?>