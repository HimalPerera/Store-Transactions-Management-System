<?php

require "DB_Connection.php";

$transaction_ID = $_POST["transaction_ID"];
$itemCode = $_POST["itemCode"];
$amount = $_POST["amount"];
$totalPrice = $_POST["totalPrice"];


$mysql_query = "INSERT INTO purchases (transaction_ID, itemCode, amount, totalPrice) VALUES ('$transaction_ID', '$itemCode', '$amount', '$totalPrice')";


if ($connection->query($mysql_query) === TRUE) {
	echo "Successfully inserted in to the database";
} else {
	echo "Failed to insert in to the database" . "\n" . "Error : " . $connection->connect_error; 
}
$connection->close();


?>