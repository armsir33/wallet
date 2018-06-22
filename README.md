# wallet

run application by "./gradlew run"

APIs:

POST http://localhost:8080/api/account/update 
{
    "transactionId": "1529619538965",
    "orderId": "1529619538965",
    "accountId": "A1000",
    "transactionDate": "2018-06-22T00:18:58.965",
    "transactionType": "debit",
    "amount": 100,
    "currency": "SEK"
}

GET http://localhost:8080/api/account/{accountId}/balance

GET http://localhost:8080/example

GET http://localhost:8080/api/accounts

GET http://localhost:8080/status
