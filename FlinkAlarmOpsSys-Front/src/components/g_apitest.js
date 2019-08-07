var testapiurl="http://47.107.254.69:8083/api/Config/GetAll"
var v="" 
$.get(testapiurl,function(datas){
$.each(datas,function(idx,objs)
　　{
v = "<tr><td>" + objs.id + "</td><td>" + objs.name + "</td><td>" + objs.foods + "</td><td>"+ objs.hotal + "</td><td>"+ objs.events + "</td><td>"+ objs.place + "</td><td>"+ objs.shop+ "</td><td>"+ objs.trans+ "</td><td>"+ objs.date+"</td></tr>";
$("#tab").append(v);                   
　　});
}); 