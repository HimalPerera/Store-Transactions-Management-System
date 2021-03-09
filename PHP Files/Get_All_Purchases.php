<?php

require "DB_Connection.php";

$allPurchases = array ();

$mysql_query = "SELECT * FROM purchases;";

$preparedStatement = $connection->prepare($mysql_query);
$preparedStatement->execute();

$preparedStatement->bind_result($transaction_ID, $itemCode, $amount, $totalPrice);

while ($preparedStatement->fetch()) {

	$jsonArray_aPurchase = [
		'transaction_ID' => $transaction_ID,
		'itemCode' => $itemCode,
		'amount' => $amount,
		'totalPrice' => $totalPrice
	];

	array_push($allPurchases, $jsonArray_aPurchase);
}

echo json_encode($allPurchases);

?>