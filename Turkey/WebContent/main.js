var max = function(x, y) {
    if (x > y) {
        return x;
    }
    return y;
}

document.onscroll = function (event) {
	var elem = document.getElementById("menu");    
    elem.style.top =  max(window.pageYOffset, 100) + 'px';
};



function putMenu(NotA) {	
	var str = '<ul class = "topmenu">'+
	'<a href = "index.html"><li>Главная</li></a>'+
	'<li>добавить таблицу  <i class="arrow down"></i>'+
	      '<ul class="submenu" style = "width: 200px ">'+
	       ' <a href = "set_peo.html"><li>с людьми</li></a>'+
	        '<a href = "set_act.html"><li>с результатами</li></a>'+
'	      </ul>'+
'	</li>'+
'	<li>сделать запрос  <i class="arrow down"></i>'+
'	 <ul class="submenu" style = "width: 200px ">'+
'	        <a href = "ask_peo.html"><li>о людях</li></a>'+
'	        <a href = "ask_act.html"><li>о мероприятиях</li></a>'+
'	      </ul>'+
'	</li>'+
'	<a href = "actdatabase.html">'+
'	<li>действия с базой данных'+	 
	'</li></a>'+
	'</ul>';
	document.write(str);
}

function putFoot(){
	var str = "связаться по почте - kseniapetrenko6@gmail.com";
	document.write(str);
}
