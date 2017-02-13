#  考卷模块
1.初始化：127.0.0.1:29001/ospaper/question/paper
场景一
## 1. 创建试卷
    127.0.0.1:29001/ospaper/question/stem/P1215111152578
	subject:
	   header
	jsonStr:
	  {"name":"中级考试","comment":"好好考试","duration":120}
## 2. 创建卷页
    127.0.0.1:29001/ospaper/question/stem/P1215111152578
    subject:
       page
    jsonStr:
     {"dPageStyleSetting":{"subjectOrder":1,"optionOrder":1},
      "dBlankStyleSetting":{"height":10.0,"width":5.0,"limit":200},
      "dOptionStyleSetting":{"optionSetting":1}}
## 3. 创建说明
     127.0.0.1:29001/ospaper/question/stem/P1215111152578
     subject:
       instruction
     jsonStr:
       {"comment":"说明"}
## 4. 创建选择题
     127.0.0.1:29001/ospaper/question/stem/P1215111152578
     subject:
        option
     jsonStr:
       {"question":"选择题?",
       "options":[{"label":"选项123"},{"label":"选项2"},{"label":"选项3"}],
       "type":1}
## 5. 创建填空题
     127.0.0.1:29001/ospaper/question/stem/P1215111152578
     subject:
         blank
     jsonStr:
        {"question":"填空题问题"}
## 6. 创建上传题
     127.0.0.1:29001/ospaper/question/stem/P1215111152578
     subject:
         attachment
     jsonStr:
         {"question":"上传题问题"}
## 7. 设置选择题分数
     127.0.0.1:29001/ospaper/question/score/P1215111152578-option-5
    subject:
    	option
    jsonStr:
## 8. 设置填空题分数
     127.0.0.1:29001/ospaper/question/score/P1215111152578
     subject:
        blank
     jsonStr:
## 9. 设置上传题分数
     127.0.0.1:29001/ospaper/question/score/P1215111152578
     subject:
        attachent
     jsonStr:
## 10. 设置试卷结束语
     127.0.0.1:29001/ospaper/question/stem/P1215111152578
     subject:
        remark
     jsonStr:
## 11. 完成
     127.0.0.1:29001/ospaper/question/stem/question/compose
      jsonStr:

## 12. 查询卷子
     127.0.0.1:29001/ospaper/question/paper/P1215111152578