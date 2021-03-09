<?php

include "DB_Connection.php";

$itemCode = $_POST["itemCode"];

$mysql_query = "DELETE FROM stock WHERE itemCode = '$itemCode'";

if (mysqli_query($connection,$mysql_query)) {
	echo "Successfully deleted the record from database";
} else {
	echo "Something went wrong during the deletion of record from database";
}

mysqli_close($connection);

?>