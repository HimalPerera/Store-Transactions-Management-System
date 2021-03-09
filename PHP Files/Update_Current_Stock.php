<?php

include "DB_Connection.php";

$itemCode = $_POST["itemCode"];
$new_currentStock = $_POST["new_currentStock"];

$mysql_query = "UPDATE stock SET currentStock = '$new_currentStock' WHERE itemCode = '$itemCode'";

if (mysqli_query($connection,$mysql_query)) {
	echo "Successfully updated the current stock in the stock database";
} else {
	echo "Something went wrong during the update of stock database";
}

mysqli_close($connection);

?>