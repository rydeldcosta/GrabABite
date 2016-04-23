<?php
    $con=mysqli_connect("localhost","root","","gab");
      
    $rest_name = $_POST["rest_name"];
    
    
    $statement = mysqli_prepare($con,"SELECT * FROM testrest WHERE r_name = '$rest_name' ");
    //mysqli_stmt_bind_param($statement, "ss", $rest_name);
    mysqli_stmt_execute($statement);
    
    //$con->query($sql);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $rid, $r_name, $r_tablename, $contact,$delivery );
    
    $restaurant = array();
    
    while(mysqli_stmt_fetch($statement)){
        $restaurant["r_id"] = $rid;
        $restaurant["r_name"] = $r_name;
        $restaurant["r_tablename"] = $r_tablename;
        $restaurant["contact"] = $contact;
        $restaurant["delivery"] = $delivery;
    }
    
    echo json_encode($restaurant);
    mysqli_close($con);
?>