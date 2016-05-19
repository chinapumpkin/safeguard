# safeguard[中文版](https://github.com/chinapumpkin/safeguard/blob/master/%E8%AF%B4%E6%98%8E.md)

Mobile Safeguard application for my bachelor thesis
## introduction
The development of this system with the design pattern of MVC structure, using SQLite as the background database, Apache as the server processing user requests. The final realization of nine function modules, communication, mobile phone anti-theft guard software management, process management, traffic statistics, mobile phone antivirus, system optimization. The paper analyzes the basic situation of mobile phone, guardian of the target market and the development of the status. Then a detailed description of the design idea, function modules and implementation technology of mobile phone guardian.After testing, the system basically completes the design function. In line with the actual needs, so it has certain practicability

## SQLite database table
1. 号码归属地查询
|属性|数据类型|说明|
|---|---|---|
|id|integer|自动新增的主键|
|cardtype|text|从属城市和运营商|
|city|text|城市|
|area|text|城市区号如0791|
该表格中的数据时从网络上找到的，可能没有实时更新。可是经过实验，大多数号码都是可以判断的。

2.号码黄页
|属性|数据类型|说明|
|---|---|---|
|id|integer|自动新增主键|
|number|text|电话号码|
|name|text|商家名称|

3. virus databse

|属性|数据类型|说明|
|---|---|---|
|id|integer|自动增加主键|
|Md5|text|病毒特征值|
|type|numeric|病毒类型|
|name|text|病毒名称|
|desc|text|后果|





