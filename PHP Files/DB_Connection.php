<?php

$db_name = "3475064_transactionsdatabase";
$mysql_username = "3475064_transactionsdatabase";
$mysql_password = "dbpassword123";
$server_name = "fdb24.awardspace.net";


$connection = mysqli_connect($server_name, $mysql_username, $mysql_password, $db_name);


if ($connection->connect_error) {
	die ("Failed to connect to the database : " . $connection->connect_error);
}


?>