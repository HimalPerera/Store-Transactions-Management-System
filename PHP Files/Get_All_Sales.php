<?php

require "DB_Connection.php";

$allSales = array ();

$mysql_query = "SELECT * FROM sales;";

$preparedStatement = $connection->prepare($mysql_query);
$preparedStatement->execute();

$preparedStatement->bind_result($transaction_ID, $itemCode, $amount, $totalPrice, $givenDiscount);

while ($preparedStatement->fetch()) {

	$jsonArray_aSale = [
		'transaction_ID' => $transaction_ID,
		'itemCode' => $itemCode,
		'amount' => $amount,
		'totalPrice' => $totalPrice,
		'givenDiscount' => $givenDiscount
	];

	array_push($allSales, $jsonArray_aSale);
}

echo json_encode($allSales);

?>