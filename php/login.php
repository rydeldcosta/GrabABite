<?php
    $con=mysqli_connect("localhost","root","","gab");
      
    $username = $_POST["username"];
    $password = $_POST["password"];
    
    $statement = mysqli_prepare($con,"SELECT * FROM register WHERE username = ? AND password = ?");
    mysqli_stmt_bind_param($statement, "ss", $username, $password);
    mysqli_stmt_execute($statement);
    
    //$con->query($sql);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $id, $name, $username, $password,$email );
    
    $user = array();
    
    while(mysqli_stmt_fetch($statement)){
        $user["id"] = $id;
        $user["username"] = $username;
        $user["name"] = $name;
        $user["password"] = $password;
        $user["email"] = $email;
    }
    
    echo json_encode($user);
    mysqli_close($con);
?>