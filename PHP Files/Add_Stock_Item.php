<?php

require "DB_Connection.php";

$itemCode = $_POST["itemCode"];
$itemName = $_POST["itemName"];
$unitPrice = $_POST["unitPrice"];
$discountRate = $_POST["discountRate"];
$discountLevel = $_POST["discountLevel"];
$currentStock = $_POST["currentStock"];


$mysql_query = "INSERT INTO stock (itemCode, itemName, unitPrice, discountRate, discountLevel, currentStock) VALUES ('$itemCode', '$itemName', '$unitPrice', '$discountRate', '$discountLevel', '$currentStock')";


if ($connection->query($mysql_query) === TRUE) {
	echo "Successfully inserted in to the database";
} else {
	echo "Failed to insert in to the database" . "\n" . "Error : " . $connection->connect_error; 
}
$connection->close();


?>