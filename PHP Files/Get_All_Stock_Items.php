<?php

require "DB_Connection.php";

$stock_Items = array ();

$mysql_query = "SELECT * FROM stock;";

$preparedStatement = $connection->prepare($mysql_query);
$preparedStatement->execute();

$preparedStatement->bind_result($itemCode, $itemName, $unitPrice, $discountRate, $discountLevel, $currentStock);

while ($preparedStatement->fetch()) {

	$jsonArray_StockItem = [
		'itemCode' => $itemCode,
		'itemName' => $itemName,
		'unitPrice' => $unitPrice,
		'discountRate' => $discountRate,
		'discountLevel' => $discountLevel,
		'currentStock' => $currentStock
	];

	array_push($stock_Items, $jsonArray_StockItem);
}

echo json_encode($stock_Items);

?>