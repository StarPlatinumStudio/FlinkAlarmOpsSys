import config from 'app/core/config';
//import getall from './getall';
//import './g_apitest';
import './keywords.css!'
import './PARALLAX PIXEL STARS.css!'
export class KeyWordsCtrl {

    constructor() {
      this.name = config.bootData.user.name;
      
    }
  }
  KeyWordsCtrl.templateUrl = 'components/keywords.html';

  var allJobsUrl="http://localhost:8080/api/jobs/alljobs" 
  var allRulesUrl="http://localhost:8080/api/rules/allrules"
  var deleteRuleUrl="http://localhost:8080/api/rules/deleterule/"
  var updateJobUrl="http://localhost:8080/api/jobs/updatejob/"
  var updateRuleUrl="http://localhost:8080/api/rules/updaterule/"
  var newRuleUrl="http://localhost:8080/api/rules/newrule"
  var setalarmUrl="http://localhost:8080/nacosconfig/alarm"
  var getalarmUrl="http://localhost:8080/nacosconfig/getalarm?id="
  var appendHtml=""//Container+Model添加的总HTML
  var rulesMap=new Object()//键值对映射表
  var rulesArrayList=new Object()//全局对象
  var rulesObj=new Object()
  var initfail=false
  var reinitfail=false
  init()

  function init(){
  $.get(allRulesUrl,function(rulesdata){
  /**
   * @summary:API GetAll:rules表,遍历构建键值对
   */
      $.each(rulesdata,function(id,rule){
  rulesMap[rule.ruleid]=rule.content
      });

  /**
   * @summary:API GetAll:jobs表,遍历组装成HTML
   * @return:JSON:[{ruleid:,content:}]
   */
      $.get(allJobsUrl,function(data){
           //初始化[jobs表]全局变量↓
           rulesObj=data
          $.each(data,function(index,obj)
          　　{
      //HTML拼接↓
      appendHtml="<div class='container' ><div class='containerinfo'>"
          +"<div class='containertitle'>信息详情</div><div class='jobtype'>Log关键字过滤报警形任务</div>"
          +"<table style='width:95%'>"
          +"<tr><th>ID</th><td>"+obj.jobId+"</td> </tr>"
          +"<tr><th>名称</th><td>"+obj.jobName+"</td>"
          +"  </tr> <tr><th>关键字条数</th><td>"+rulesObj[index].rules.split(",").length+"</td>"
          +"   </tr><tr><th>报警阈值</th><td id='alarmval_"+index+"'></td></tr></table></div>"
          +"  <div class='containerbtns'>"
          +"<button id='showbtn' class='gbutton' "
          +"onclick=\"$('#myModal_"+obj.jobId+"').css('display','block');"
          +"$('#close_"+obj.jobId+"').click(function(){"
              +"$('#myModal_"+obj.jobId+"').css('display','none');});\">配置过滤关键字</button>"

              +"<button id='showbtn' class='gbutton' "
              +"onclick=\"$('#alarmModal_"+index+"').css('display','block');"
              +"$('#alarmModalclose_"+index+"').click(function(){"
                  +"$('#alarmModal_"+index+"').css('display','none');});\">配置报警阈值</button>"
              +"<input class='gbutton' type='button' value='修改任务名称' />"
              +"<input class='gbutton' type='button' value='Flink集群监控' /></div></div>"
  
              +"<div id='myModal_"+obj.jobId+"' class='modal' style='padding-top:50px'><span id='close_"+obj.jobId+"'><button type='button' class='dialogclose'>关闭</button></span><div class='containertitle'>配置过滤关键字</div><div class='jobtype'>"+obj.jobName+"</div>"+modelInit(rulesObj[index].rules,index)+"<div id='caption_"+obj.jobId+"'></div></div>"
              +dialogInit(rulesObj[index].rules,index)+adddialogInit(index)+alarmModelInit(index);

      $('#containers').append(appendHtml)   
      $('#loadingdialog').css('display','none');
      $('.wait').css('display','none');
      $("#add"+index).click(function(){
        $('#dialog'+index).css('display','block')
      }) 
  /**
   *@summary:在插入HTML之后对所有【删除】和【修改】按钮的指定id进行click事件绑定
   */
      $.each(rulesObj[index].rules.split(','),function(id,ruleid){
        //【删除】按钮的click事件
       $("#del"+index+ruleid).click(function (){
        $('#loadingdialog').css('display','block');
        $('#loadbox').css('display','block');
        $("#dialogclose").css('display','none');
         var ruleArray=rulesObj[index].rules.split(',')
          var itemindex = ruleArray.indexOf(ruleid);
          if (itemindex > -1){ruleArray.splice(itemindex, 1);}
          rulesObj[index].rules=toString(ruleArray)
          $.ajax({
              type:"PUT",
              url:updateJobUrl+rulesObj[index].jobId,
              data:JSON.stringify(rulesObj[index]),
              dataType:"json",
              contentType:"application/json",
              success : function(result) {
                  console.log(result);
                  if(result.ret==0){//返回数据ret:0
                      $("#li"+index+ruleid).remove()
                      $('#loadingdialog').css('display','none');
                      $('#deldialog'+index+ruleid).css('display','none');
                  }
              },
              error : function(e){
                  console.log(e.status);
                  console.log(e.responseText);
                    alert("操作失败，错误信息："+e.responseText)
                      $('#loadbox').css('display','none')
                    $('#operatefailbox').css('display','block')
                    $('#dialogok').css('display','block')
              }
          })
      });
      //【修改】按钮的click事件
      $('#update'+index+ruleid).click(function (){
        var content=$('#updateinput'+index+ruleid).val()
        if(content==""){
          alert("输入不可为空")
          return false;}
          var samerule=false
          var changeJobOnly=false
          var ruleArray=rulesObj[index].rules.split(',')
          $.each(ruleArray,function(id,rule){//job内对内容排重
            if(content==rulesMap[rule]){
            $('#loadingdialog').css('display','block')
             $('#addfailbox').css('display','block')
             $('#dialogclose').css('display','block')
             $('#loadbox').css('display','none')
            $('#connectfailbox').css('display','none')
              samerule=true
             return false;
            }});
        if(!samerule)
        {
          $.each(rulesMap,function(id,rule){//遍历规则表
            if(content==rule)//rules表是否已经存在ID
            {
              var oldid = ruleArray.indexOf(ruleid);
              if (oldid > -1){
                ruleArray.splice(oldid, 1);}//splice 老Rule ID
              ruleArray.push(id)//push新Rule ID
              rulesObj[index].rules=toString(ruleArray)//更新全局类 
              updateJob(index)//确定仅更新Job
            changeJobOnly=true
           return false//结束当前循环
            }});
        if(!changeJobOnly)
        {
        $('#loadingdialog').css('display','block');
        $('#loadbox').css('display','block');
        $("#dialogclose").css('display','none');
        var JsonObj = {"ruleid":null,"content":content}//新增rule对
        $.ajax({
          type:"POST",
          url:newRuleUrl,
          data:JSON.stringify(JsonObj) ,
          dataType:"json",
          contentType:"application/json",
          success : function(result) {
              console.log(result);
              if(result.content==content){//返回数据job类,成功
                var oldid = ruleArray.indexOf(ruleid);
              if (oldid > -1){
                ruleArray.splice(oldid, 1);}//splice 老Rule ID
                  ruleArray.push(result.ruleid)
                  rulesObj[index].rules=toString(ruleArray)//更新全局类 
                  updateJob(index)
              }
          },
          error : function(e){
              console.log(e.status);
              console.log(e.responseText);
                alert("操作失败，错误信息："+e.responseText)
                $('#loadbox').css('display','none')
                $('#operatefailbox').css('display','block')
                $('#dialogok').css('display','block')
          }
      })
        // var JsonObj = {"ruleid":parseInt(ruleid),"content":content}
        //   $.ajax({
        //       type:"PUT",
        //       url:updateRuleUrl+ruleid,
        //       data:JSON.stringify(JsonObj),
        //       dataType:"json",
        //       contentType:"application/json",
        //       success : function(result) {
        //           console.log(result);
        //           if(!result.ret){//返回数据ret:0,
        //             rulesMap[ruleid]=content//修改全局变量
        //               $('#loadingdialog').css('display','none');
        //               $('#updatedialog'+index+ruleid).css('display','none');
        //               $(".content"+index+ruleid).html(content)//执行成功的DOM操作
        //           }
        //       },
        //       error : function(e){
        //           console.log(e.status);
        //           console.log(e.responseText);
        //             alert("操作失败，错误信息："+e.responseText)
        //               $('#loadbox').css('display','none')
        //             $('#operatefailbox').css('display','block')
        //             $('#dialogok').css('display','block')
        //       }
        //   })
        }
        }
      });
       });
       //对所有【添加规则】按钮的指定id进行click事件绑定
$('#addok'+index).click(function(){
        if($('#input'+index).val()==""){
          alert("输入不可为空")
          return false;}
         var ready=true
         var updateRule=false
         var ruleArray=rulesObj[index].rules.split(',')
$.each(ruleArray,function(id,rule){//job内排重
  if($('#input'+index).val()==rulesMap[rule]){
ready=false
  $('#loadingdialog').css('display','block')
   $('#addfailbox').css('display','block')
   $('#dialogclose').css('display','block')
   $('#loadbox').css('display','none')
  $('#connectfailbox').css('display','none')
   return false;//结束全局循环
  }
});
if(ready){
$.each(rulesMap,function(id,rule){//遍历规则表,确定是否更新规则表
 if($('#input'+index).val()==rule)
 {
updateRule=true//规则表已存在
ruleArray.push(id)
rulesObj[index].rules=toString(ruleArray)//更新全局类
updateJob(index)
return false//结束当前循环
 }
})
if(!updateRule){
  var JsonObj = {"ruleid":null,"content":$('#input'+index).val()}//新增rule对
  $('#loadingdialog').css('display','block');
  $('#loadbox').css('display','block');
  $("#dialogclose").css('display','none');
  $.ajax({
    type:"POST",
    url:newRuleUrl,
    data:JSON.stringify(JsonObj) ,
    dataType:"json",
    contentType:"application/json",
    success : function(result) {
        console.log(result);
        if(result.content==$('#input'+index).val()){//返回数据job类,成功
            ruleArray.push(result.ruleid)
            rulesObj[index].rules=toString(ruleArray)//更新全局类 

            updateJob(index)
        }
    },
    error : function(e){
        console.log(e.status);
        console.log(e.responseText);
          alert("操作失败，错误信息："+e.responseText)
          $('#loadbox').css('display','none')
          $('#operatefailbox').css('display','block')
          $('#dialogok').css('display','block')
    }
})
}
//更新Job类
}
       });
//对所有【报警阀值】配置按钮的指定id进行click事件绑定
$("#alarmbtn_"+index).click(function(){
  var choose=$("input[name='"+index+"']:checked").val()
  var minute
  var num
  switch(choose){
case '1':minute=1
num=10
break;
case '2':minute=10
num=100
break;
case 'custom':
  if(($("#inpA"+index).val()=="")&&($("#inpB"+index).val()==""))
  {
    alert("自定义输入不可为空")
    return false
  }
  minute=$("#inpA"+index).val()
  num=$("#inpB"+index).val()
  break;
  }
  var content = "{\"minute\":"+minute+",\"num\":"+num+"}"
  var postParam={"dataId":"FlinkJob."+rulesObj[index].jobId+".properties.alarm","group":"BaseService","content":content}
  $('#loadingdialog').css('display','block');
  $('#loadbox').css('display','block');
  $("#dialogclose").css('display','none');
  $.ajax({
    type:"POST",
    url:setalarmUrl,
    data:JSON.stringify(postParam) ,
    dataType:"json",
    contentType:"application/json",
    success : function(result) {
        console.log(result);
        if(result.ret==0){//返回数据job类,成功
          $('#loadingdialog').css('display','none');
          $('#loadbox').css('display','none');
          $("#alarmModal_"+index).css("display","none")
          $("#alarmval_"+index).html(minute+"分钟"+num+"次")
        }
    },
    error : function(e){
        console.log(e.status);
        console.log(e.responseText);
          alert("操作失败，错误信息："+e.responseText)
          $('#loadbox').css('display','none')
          $('#operatefailbox').css('display','block')
          $('#dialogok').css('display','block')
    }
})
})

//初始时读取预警阀值
var alarmurl=getalarmUrl+rulesObj[index].jobId
$.ajax({
  type:"GET",
  url:alarmurl,
  dataType:"json",
  contentType:"application/json",
  success:function(result){
    console.log(result)
    $("#alarmval_"+index).html(result.minute+"分钟"+result.num+"次")
  }
})
      });
          　　});
          }).fail(function(){ //连接失败-重连的机制
            //连接失败-重连的机制
          if((!initfail)&&(!reinitfail)){
          $('#dialogbtns').append("<button id='reinit' class='gbutton' style='background-color: cornflowerblue;width: 100%'>重新加载</button>")
          $('#reinit').click(function (){
            $('#reinit').css('display','none')
            $('#loadbox').css('display','block')
            $('#connectfailbox').css('display','none')
          init()});
          initfail=true
          $('#loadbox').css('display','none')
          $('#connectfailbox').css('display','block')
            }
       else{
        $('#loadbox').css('display','none')
        $('#connectfailbox').css('display','block')
        $('#reinit').css('display','block')
       }
        }); 
  }
  
  /**
   * @param:给定Rules ID字符串
   * @summary：分割字符串为数组，遍历Map表从中取字段插入到Html中
   * @returns: 组装生成的HTML
   */
  function dialogInit(rulesStr,index)
  {
      var htmlStr=""
      var ruleArray=rulesStr.split(',')
        $.each(ruleArray,function(id,ruleid){
         htmlStr+="<div id='deldialog"+index+ruleid+"' class='dialog'>"
         +"<div class='dialoginner'>"
         +"<span class='dialogTitle'>请确认本次操作删除的内容</span>"
         +" <ul class='dialogul'>"
         +"     <li>▮ 将要删除的字段：<span class='content"+index+ruleid+"'>"+rulesMap[ruleid]+"</span></li>"
         +"     <li>▮ 字段所属任务ID: "+rulesObj[index].jobId+"</li>"
         +"     <li>▮ 确认删除吗？</li>"
         +"  </ul> "
         +"<div class='dialogbtns'><input id='del"+index+ruleid+"' class='gbutton' type='button' value='确认' style='background-color: orangered;' /></li><input id='closedelDialog"+index+ruleid+"' class='gbutton' type='button' style='color: black;background-color: gray;' value='取消' /></li></div>"
         +"</div>"
         +"</div>"
         +"<div id='updatedialog"+index+ruleid+"' class='loading dialog' style='display:none'>"
         +"<div class='dialoginner'>"
         +"<span class='dialogTitle'>请输入本次操作修改的内容</span>"
         +" <ul class='dialogul'>"
         +"     <li>▮ 将要修改的字段：<span class='content"+index+ruleid+"'>"+rulesMap[ruleid]+"</span></li>"
         +"     <li>▮ 字段所属任务ID: "+rulesObj[index].jobId+"</li>"
         +"     <li>▮ 修改后字段：<input id='updateinput"+index+ruleid+"' style='font-size: large;width: 100%;background: #243238;' type='text'/></li>"
         +"  </ul> "
         +"<div class='dialogbtns'><input id='update"+index+ruleid+"' class='gbutton' type='button' value='确认' style='background-color: #9ccadc;color: #000;' /></li><input id='closeupdateDialog"+index+ruleid+"' class='gbutton' type='button' style='color: black;background-color: gray;' value='取消' /></li></div>"
         +"</div>"
         +"</div>";
      });
      return htmlStr
  }
  function adddialogInit(index){
    var htmlStr=""
       htmlStr+="<div id='dialog"+index+"' class='loading dialog' style='display:none'>"
       +"<div class='dialoginner'>"
       +"<span class='dialogTitle'>请输入需要添加的字段</span>"
       +" <ul class='dialogul' style='padding: 35px;'>"
       +"     <li><input id='input"+index+"' style='font-size: large;width: 100%;background: #243238;' type='text'/></li>"
       +" </ul> "
       +"<div class='dialogbtns'><input id='addok"+index+"' class='gbutton' type='button' value='确认' style='background-color: #9ccadc;color: #000;' />"
       +"<input id='closeDialog"+index+"' onClick=\"$('#dialog"+index+"').css('display','none')\" class='gbutton' type='button' style='color: black;background-color: gray;' value='取消' /></li></div>"
       +"</div>"
       +"</div>";
    return htmlStr
  }
  function modelInit(rulesStr,index){
      var htmlStr="<table style='width:95%;margin: 15px 0 100px 15px;'><tr><th>规则ID</th> <th>规则内容</th> <th class='dialogtd'></th> <th class='dialogtd'></th></tr>"
      var ruleArray=rulesStr.split(',')
        $.each(ruleArray,function(id,ruleid){
         htmlStr+="<tr id='li"+index+ruleid+"' style='background-color: #9ccadcf2'><td ><p style='width: 30px;'>"+ruleid+"</p></td> <td class='dialogtdrule' style='background-color: #9ccadcf2;'><p class='content"+index+ruleid+"' style='width: 300px;'>"+rulesMap[ruleid]+"</p></td>"
         +"<th class='dialogtd'><button  onclick=\"$('#updatedialog"+index+ruleid+"').css('display','block');"
         +"$('#closeupdateDialog"+index+ruleid+"').click(function(){"
         +"$('#updatedialog"+index+ruleid+"').css('display','none')"
         +"});\" class='gbutton dialogBtn'>修改</button></th>"

         +"<th class='dialogtd'><button  onclick=\"$('#deldialog"+index+ruleid+"').css('display','block');"
         +"$('#closedelDialog"+index+ruleid+"').click(function(){"
         +"$('#deldialog"+index+ruleid+"').css('display','none')"
         +"});\" class='gbutton dialogBtn'>删除</button></th></tr>"
      });
      htmlStr+="<tr><th>END</th> <th></th> <th class='dialogtd'></th> <th class='dialogtd'></th></tr></table><button id='add"+index+"' class='gbutton addbtn' >添加规则</button>"
      return htmlStr
  }
  function alarmModelInit(index){
var htmlStr=""
htmlStr+="<div id='alarmModal_"+index+"' class='modal' style='display: none;padding-top:50px'>"
+"<span id='alarmModalclose_"+index+"'><button type='button' class='dialogclose'>关闭</button></span>"
+" <div class='containertitle'>配置报警阀值</div><div class='jobtype'>"+rulesObj[index].jobName+"</div>"
+" <table style='width:95%;margin: 15px 0 100px 15px;'>"
+"   <tr><th style='width: 1%'>选择</th> <th><p style='margin-left: 20%;'>规则内容</p></th></tr>"
+"   <tr><td><label><input type='radio' name='"+index+"' id='r1_"+index+"' class='a-radio' value='1'><span class='b-radio'></span></label></td> "
+"     <td class='dialogtdrule' ><p style='margin-left: 20%;'>1分钟10次</p></td></tr>"

+"   <tr><td > <label><input type='radio' name='"+index+"' id='r2_"+index+"' class='a-radio' value='2'><span class='b-radio'></span> </label></td>"
+"     <td class='dialogtdrule' ><p style='margin-left: 20%;'>10分钟100次</p></td></tr>"
+"    </table>"

+"    <table style='width:95%;margin: 15px 0 100px 15px;'>"
+"        <tr  style='background-color: #9ccadcf2'>"
+"           <th style='width: 1%'>选择</th><th><p style='margin-left: 50%;'>自定义</p></th> <th></th></tr>"
             
+"        <tr><tr><td><label><input type='radio' name='"+index+"' id='rc_"+index+"' class='a-radio' value='custom'><span class='b-radio'></span></label></td> "
+"           <td ><label for='inpA"+index+"' class='inp'>"
+"              <input type='text' id='inpA"+index+"' onclick=\"$('#rc_"+index+"').click()\" onkeyup=\"this.value=this.value.replace(/[^0-9]/g,'')\" onafterpaste=\"this.value=this.value.replace(/[^0-9]/g,'')\" placeholder='&nbsp;'>"
+"              <span class='label'>分钟数</span>"
+"              <span class='border'></span>"
+"            </label></td>"
+"            <td><label for='inpB"+index+"' class='inp'>"
+"                 <input type='text' id='inpB"+index+"' onclick=\"$('#rc_"+index+"').click()\" onkeyup=\"this.value=this.value.replace(/[^0-9]/g,'')\" onafterpaste=\"this.value=this.value.replace(/[^0-9]/g,'')\" placeholder='&nbsp;'>"
+"                 <span class='label'>触发次数</span>"
+"                  <span class='border'></span>"
+"               </label></td></tr>"
+"           </table>"
+"           <button id='alarmbtn_"+index+"' class='gbutton addbtn' >确认更改</button>"
+"  </div>"
return htmlStr
  }
  /**
   * toString:数组转逗号分隔字符串，形如：（"1,2,3"）
   * @param {Array[]} ruleArray 
   * @returns {String} rStr
   */
  function toString(ruleArray){
    var rStr=""
          $.each(ruleArray,function(id,rule){
              rStr+=rule
              if(id!=ruleArray.length-1)
              rStr+=','
          });
          return rStr;
  }

  function updateJob(index){ 
  $.ajax({
    type:"PUT",
    url:updateJobUrl+rulesObj[index].jobId,
    data:JSON.stringify(rulesObj[index]),
    dataType:"json",
    contentType:"application/json",
    success : function(result) {
        console.log(result);
        if(result.ret==0){//返回数据ret:0,成功
          location.reload()
        }
    },
    error : function(e){
        console.log(e.status);
        console.log(e.responseText);
          alert("操作失败，错误信息："+e.responseText)
            $('#loadbox').css('display','none')
          $('#operatefailbox').css('display','block')
          $('#dialogok').css('display','block')
    }
})}
