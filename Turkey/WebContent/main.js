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
	'<a href = "index.html"><li>Main</li></a>'+
	'<li>Add table  <i class="arrow down"></i>'+
	      '<ul class="submenu" style = "width: 200px ">'+
	       ' <a href = "set_peo.html"><li>with people</li></a>'+
	        '<a href = "set_act.html"><li>with event</li></a>'+
'	      </ul>'+
'	</li>'+
'	<li> Make request <i class="arrow down"></i>'+
'	 <ul class="submenu" style = "width: 200px ">'+
'	        <a href = "ask_peo.html"><li>about people</li></a>'+
'	        <a href = "ask_act.html"><li>about events</li></a>'+
'	      </ul>'+
'	</li>'+
'	<a href = "actdatabase.html">'+
'	<li>Operations with database'+	 
	'</li></a>'+
	'</ul>';
	document.write(str);
}

function putFoot(){
	var str = "contact email - kseniapetrenko6@gmail.com";
	document.write(str);
}
