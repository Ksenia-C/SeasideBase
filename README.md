# SeasideBase
Web application. Its main task is to work with repeated events and their participators. 
It can be useful for organizers who need statistics.

The project can be run in Eclipse IDE with Tomcat, MySQL and MySQL Connector for Java.

To add people to the basa you need to create CSV file with information about them, one person - one string. 
Main characteristics are Name, Surname, Birthday, City (and maybe Patronymic). 
Optional parameters also can be written in the table, the app will add all listed parameters even if some people have empty cells.

To add event you may fill fields with its title, date of beginning, duration, places where it is held and add optional parameters. 
You also may save table where participators are mentioned and you may ask to find them in the base.

People and events can be searched by some parameters, main or optional ones. 
The results are shown as a table. 
Each string has a reference to the page where all the item information is collected. 
Then it may be changed or deleted.

Some problems with operations may exist, if the request was strange, for instance, enumerate the columns of the table starting with zero, or do not follow patterns. 
Check that database is created.

Pages can look like:

![insert_page](https://github.com/Ksenia-C/SeasideBase/blob/master/Turkey/WebContent/img/show1.png?raw=true)

![request_page](https://github.com/Ksenia-C/SeasideBase/blob/master/Turkey/WebContent/img/show2.png?raw=true)
