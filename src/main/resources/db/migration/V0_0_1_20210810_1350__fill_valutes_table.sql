insert into valutes (code, name)
values ('AUD', 'Австралийский доллар'),
       ('AZN', 'Азербайджанский манат'),
       ('GBP', 'Фунт стерлингов Соединенного королевства'),
       ('AMD', 'Армянский драм'),
       ('BYN', 'Белорусский рубль'),
       ('BGN', 'Болгарский лев'),
       ('BRL', 'Бразильский реал'),
       ('HUF', 'Венгерский форинт'),
       ('HKD', 'Гонконгский доллар'),
       ('DKK', 'Датская крона'),
       ('USD', 'Доллар США'),
       ('EUR', 'Евро'),
       ('INR', 'Индийская рупия'),
       ('KZT', 'Казахстанский тенге'),
       ('CAD', 'Канадский доллар'),
       ('KGS', 'Киргизский сом'),
       ('CNY', 'Китайский юань'),
       ('MDL', 'Молдавский лей'),
       ('NOK', 'Норвежская крона'),
       ('PLN', 'Польский злотый'),
       ('RON', 'Румынский лей'),
       ('XDR', 'СДР'),
       ('SGD', 'Сингапурский доллар'),
       ('TJS', 'Таджикский сомони'),
       ('TRY', 'Турецкая лира'),
       ('TMT', 'Новый туркменский манат'),
       ('UZS', 'Узбекский сум'),
       ('UAH', 'Украинская гривна'),
       ('CZK', 'Чешская крона'),
       ('SEK', 'Шведская крона'),
       ('CHF', 'Швейцарский франк'),
       ('ZAR', 'Южноафриканский рэнд'),
       ('KRW', 'Вон Республики Корея'),
       ('JPY', 'Японская иена');



insert into intervals (valute_id, interval)
values ((select id from valutes where code = 'USD'), 60),
       ((select id from valutes where code = 'BYN'), 180);


