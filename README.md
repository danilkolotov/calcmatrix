# calcmatrix
## Описание
Простое API, позволяющее делать операции с матрицами.

В запрос входит квадратная матрица, ее размер, и операция: степень или определитель. Также показатель степени, если нужен.

База этих "запросов" сохраняется.

Реализованные методы:
1. `/get/{id}`
   
   Get по id.
   Пример возврата:
   ```js
     {
       "id": 1,
       "matrix": [1.0, 2.0, 3.0, 4.0],
       "n": 2,
       "operation": "DET",
       "power": null,
       "powerResult": [],
       "result":-2.0
     }
   ```
2. `/`

   Вернется список всех запросов
3. `/new`

    Post для создания нового запроса. В теле должна быть матрица, размер, операция, иначе вернется `Bad Request`. В противном случае вернет response с location созданного запроса, у которого будет вычислен результат.

## Тесты
Для методов выше написаны некоторые [тесты](/src/test/java/calcmatrix/CalcmatrixApplicationTests.java). Также написаны [тесты](/src/test/java/calcmatrix/MatrixMathTest.java) для проверки выполнения вычислений с матрицами.

## Web
Также написана простая главная [страничка](/src/main/resources/templates/index.html), где можно делать запросы через форму, выводится история. Иллюстрация (до и после нажатия кнопки = добавления запроса в базу)
<div style="display: flex;">
  <img src="https://github.com/danilkolotov/calcmatrix/assets/67107781/780d95e6-4454-4eec-bb7e-9831ba5790d5" width=500/>
  <img src="https://github.com/danilkolotov/calcmatrix/assets/67107781/49810cfa-bf14-4147-b1f2-98b73e3086b5" width=500/>
</div>
