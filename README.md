# SeasideBase
Web application. Its main task is to work with repeated events and theirs participators. It can be useful for organizators who need staticstics.

The project can be run in Eclipse IDE with tomcat, MySQL and MySQL Connector for Java.

To add people to the basa you need create csv file with information about them, one person - one string.
Main characteristics are Name, Surname, Birthday, City (and maybe Patronymic).
Optional parameters also can be written in the table, the app will add all listed parametres even if some people have empty ceils.

To add event you may fill fields with its title, date of beginning, duraction, places where it takes part and add optional parameters.
You also may save table where participators are mentioned and you may ask to find them in the base.

People and events can be searched by some parameters, main or optional ones.
Each string has a reference to the page where all the information about item is collected. 
Then it may be changed or deleted.

Some problems with operations may exists, if request was strange, for instance, enumarate the columns of table by zero, or do not follow partens.
Check that database is created.

Pages can look like:
![alt text](https://github.com/Ksenia-C/SeasideBase/blob/origin/Turkey/WebContent/img/show1.jpg?raw=true)
![alt text](https://github.com/Ksenia-C/SeasideBase/blob/origin/Turkey/WebContent/img/show2.jpg?raw=true)
