# Bank

## Описание.
В проекте реализованы операции пополнения, снятия и перевод средств с одного счёта на другой.
Так же реализовано сквозное логирование с использованием AspektJ для методов сервиса.
Но так как запуск программы идёт через Tomcat, файл лога нужно искать в папке apache-tomcat\bin\
Или же смотреть вывод в консоль.
### запуск: через docker-compose.yml
Зайдите в корень проекта где находится файл docker-compose.yml,
в ней же откройте командную строку и введите: ( docker compose up -d ) -
для загрузки контейнера postgres, (-d) - в фоновом режиме.
#### gradle Update  
Эту команду запустить из корня проекта через командную строку, для создания таблиц в базе
и заполнения данными.

Tomcat - лежит в корне проекта.

1. Для получение информации о клиенте в Postman в ввидите имя клиента. Выбрав Get запрос.
Пример: http://localhost:8080/v1?name=Vasya

Query result name: Vasya

["clientId-1 clientname-Vasya accountId-35 account-35 balance-1000 bankName-Clever-Bank"
"clientId-1 clientname-Vasya accountId-36 account-36 balance-1000 bankName-Alfa-Bank"
"clientId-1 clientname-Vasya accountId-2 account-2 balance-503 bankName-Alfa-Bank"
"clientId-1 clientname-Vasya accountId-1 account-1 balance-855 bankName-Clever-Bank"]

2. Для пополнения счёта клиента выберите Put запрос с параметром znak=plus.
Пример: http://localhost:8080/v1?znak=plus&account=1&sum=7
Ответ:
   {
   "id": 1,
   "account": 1,
   "clientId": 1,
   "bankId": 1,
   "balance": 669
   }

3. Для снятия со счёта клиента выберите Put запросс параметром znak=minus.
Пример: http://localhost:8080/v1?znak=minus&account=1&sum=7
Ответ:
   {
   "id": 1,
   "account": 1,
   "clientId": 1,
   "bankId": 1,
   "balance": 662
   }

4. Для перевода средств выберите Put запрос аклаунт 1, аккаунт 2 и сумму (первому перевод от второга).
Пример: http://localhost:8080/v1/perevod?account1=1&account2=2&sum=10
Ответ:
   Query result: 1
   Query result: 2


[
{
"id": 1,
"account": 1,
"clientId": 1,
"bankId": 1,
"balance": 692
},
{
"id": 2,
"account": 2,
"clientId": 1,
"bankId": 2,
"balance": 690
}
]