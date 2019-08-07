System.register([], function (_export, _context) {
  "use strict";

  var allJobsUrl, allRulesUrl, deleteRuleUrl, updateJobUrl, appendHtml, rulesMap, modelContent, rulesArrayList, rulesObj;

  //init()
  function init() {
    $.get(allRulesUrl, function (rulesdata) {
      /**
       * @summary:API GetAll:rules表,遍历构建键值对
       */
      $.each(rulesdata, function (id, rule) {
        rulesMap[rule.ruleid] = rule.content;
      });
      /**
       * @summary:API GetAll:jobs表,遍历组装成HTML
       * @return:JSON:[{ruleid:,content:}]
       */

      $.get(allJobsUrl, function (data) {
        //初始化[jobs表]全局变量↓
        rulesObj = data;
        $.each(data, function (index, obj) {
          //HTML拼接↓
          appendHtml = "<div class='container' ><div class='containerinfo'>" + "<div class='containertitle'>信息详情</div><div class='jobtype'>Log关键字过滤报警形任务</div>" + "<table style='width:95%'>" + "<tr><th>ID</th><td>" + obj.jobId + "</td> </tr>" + "<tr><th>名称</th><td>" + obj.jobName + "</td>" + "  </tr> <tr><th>关键字条数</th><td>" + rulesObj[index].rules.split(",").length + "</td>" + "   </tr><tr><th>报警阈值</th><td>NULL</td></tr></table></div>" + "  <div class='containerbtns'>" + "<button id='showbtn' class='gbutton' " + "onclick=\"$('#myModal_" + obj.jobId + "').css('display','block');" + "$('#close_" + obj.jobId + "').click(function(){" + "$('#myModal_" + obj.jobId + "').css('display','none');});\">配置过滤关键字</button>" + "<button class='gbutton' type='button'>配置报警阈值</button>" + "<input class='gbutton' type='button' value='修改任务名称' />" + "<input class='gbutton' type='button' value='Flink集群监控' /></div></div>" + "<div id='myModal_" + obj.jobId + "' class='modal' style='padding-top:50px'><span id='close_" + obj.jobId + "'><button type='button' class='dialogclose'>关闭</button></span><div class='containertitle'>配置过滤关键字</div><div class='jobtype'>" + obj.jobName + "</div>" + modelInit(rulesObj[index].rules, index) + "<div id='caption_" + obj.jobId + "'></div></div>" + dialogInit(rulesObj[index].rules, index);
          $('#containers').append(appendHtml);
          $('#loadingdialog').css('display', 'none');
          $('.wait').css('display', 'none');
          $("#add" + index).click(function () {
            alert("请输入新增的规则名称,index:" + index);
          });
          /**
           *@summary:在插入HTML之后对所有【删除】按钮的指定id进行click事件绑定
           */

          $.each(rulesObj[index].rules.split(','), function (id, ruleid) {
            $("#del" + index + ruleid).click(function () {
              var ruleArray = rulesObj[index].rules.split(',');
              var itemindex = ruleArray.indexOf(ruleid);

              if (itemindex > -1) {
                ruleArray.splice(itemindex, 1);
              }

              var rStr = "";
              $.each(ruleArray, function (id, rule) {
                rStr += rule;

                if (id != ruleArray.length - 1) {
                  rStr += ',';
                }
              });
              rulesObj[index].rules = rStr;
              $.ajax({
                type: "PUT",
                url: updateJobUrl + rulesObj[index].jobId,
                data: JSON.stringify(rulesObj[index]),
                dataType: "application/json",
                contentType: "application/json",
                success: function (result) {
                  console.log(result);
                },
                error: function (e) {
                  console.log(e.status);
                  console.log(e.responseText);
                  $('#dialog' + index + ruleid).css('display', 'none');
                  var resultObj = JSON.parse(e.responseText);

                  if (!resultObj.ret) {
                    $("#li" + index + ruleid).remove();
                  }
                }
              });
            });
          });
        });
        modelContent = "";
      });
    });
  }
  /**
   * @param:给定Rules ID字符串
   * @summary：分割字符串为数组，遍历Map表从中取字段插入到Html中
   * @returns: 组装生成的HTML
  }
  $('#reinit').click(function(){
      init();
  });
   * 
   *  */


  function dialogInit(rulesStr, index) {
    var htmlStr = "";
    var ruleArray = rulesStr.split(',');
    $.each(ruleArray, function (id, ruleid) {
      htmlStr += "<div id='dialog" + index + ruleid + "' class='dialog'>" + "<div class='dialoginner'>" + "<span class='dialogTitle'>请确认本次操作删除的内容</span>" + " <ul class='dialogul'>" + "     <li>▮ 将要删除的字段：" + rulesMap[ruleid] + "</li>" + "     <li>▮ 字段所属任务ID: " + rulesObj[index].jobId + "</li>" + "     <li>▮ 确认删除吗？</li>" + "  </ul> " + "<div class='dialogbtns'><input id='del" + index + ruleid + "' class='gbutton' type='button' value='确认' style='background-color: orangered;' /></li><input id='closeDialog" + index + ruleid + "' class='gbutton' type='button' style='color: black;background-color: gray;' value='取消' /></li></div>" + "</div>" + "</div>";
    });
    return htmlStr;
  }

  function modelInit(rulesStr, index) {
    var htmlStr = "<table style='width:95%;margin: 15px 0 100px 15px;'><tr><th>ID</th> <th>规则内容</th> <th class='dialogtd'></th> <th class='dialogtd'></th></tr>";
    var ruleArray = rulesStr.split(',');
    $.each(ruleArray, function (id, ruleid) {
      htmlStr += "<tr id='li" + index + ruleid + "' style='background-color: #9ccadcf2;'><td ><p style='width: 30px;'>" + id + "</p></td> <td class='dialogtdrule' style='background-color: #9ccadcf2;'><p style='width: 300px;'>" + rulesMap[ruleid] + "</p></td><th class='dialogtd'><button type='input' class='gbutton dialogBtn'>修改</button></th>" + "<th class='dialogtd'><button  onclick=\"$('#dialog" + index + ruleid + "').css('display','block');" + "$('#closeDialog" + index + ruleid + "').click(function(){" + "$('#dialog" + index + ruleid + "').css('display','none')" + "});\" class='gbutton dialogBtn'>删除</button></th></tr>";
    });
    htmlStr += "<tr><th>END</th> <th></th> <th class='dialogtd'></th> <th class='dialogtd'></th></tr></table><button id='add" + index + "' class='gbutton addbtn' >添加规则</button>";
    return htmlStr;
  }

  function deleteRuleById(id) {
    alert("cl!");
    var urlstr = deleteRuleUrl + id;
    $.ajax({
      type: "delete",
      url: urlstr,
      success: function (data) {
        alert("Delete Successfuly");
        $("#li_" + id).remove;
      }
    });
  } //    +"onclick=\""
  //       +"$.ajax({"
  //       +"type: 'delete',"
  //       +"url: 'http://localhost:8080/api/rules/deleterule/"+ruleid+"',"
  //     +" success: function (data) {if(data.deleted){$('#li_"+id+"_"+ruleid+"').remove()};}});
  //老插入代码
  // appendHtml="<div class='container' ><ul style='list-style-type:none'><li>"
  // +"<h2>ID:"+obj.jobId+"</h2></li><li><h2>Name:"+obj.jobName+"<input class='gbutton' type='button' value='修改' onload='init()' /></h2></li></ul>"
  // +"<button id='showbtn_"+obj.jobId+"' class='gbutton'style='width: 100%;height: 60px;' "
  // +"onclick=\"$('#myModal_"+obj.jobId+"').css('display','block');$('#close_"+obj.jobId+"').click(function(){$('#myModal_"+obj.jobId+"').css('display','none');});\">配置过滤关键字</button></div>"
  // +"<div id='myModal_"+obj.jobId+"' class='modal'>This is the Model DIV_+"+modelInit(rulesObj[index].rules,index)+"<span id='close_"+obj.jobId+"'class='close'><button type='button' class='btn btn-info'>关闭</button></span><div id='caption_"+obj.jobId+"'></div></div>"
  // +dialogInit(rulesObj[index].rules,index);


  return {
    setters: [],
    execute: function () {
      allJobsUrl = "http://localhost:8080/api/jobs/alljobs";
      allRulesUrl = "http://localhost:8080/api/rules/allrules";
      deleteRuleUrl = "http://localhost:8080/api/rules/deleterule/";
      updateJobUrl = "http://localhost:8080/api/jobs/updatejob/";
      appendHtml = "";
      rulesMap = new Object();
      modelContent = "";
      rulesArrayList = new Object();
      rulesObj = new Object();
    }
  };
});
//# sourceMappingURL=getall.js.map
