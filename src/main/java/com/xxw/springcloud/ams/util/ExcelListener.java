package com.xxw.springcloud.ams.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

/* 解析监听器，
* 每解析一行会回调invoke()方法。
* 整个excel解析结束会执行doAfterAllAnalysed()方法
*
* 下面只是我写的一个样例而已，可以根据自己的逻辑修改该类。
* @author jipengfei
* @date 2017/03/14
*/
public class ExcelListener extends AnalysisEventListener<Object> {

   //自定义用于暂时存储data。
   //可以通过实例获取该值
   private List<Object> datas = new ArrayList<Object>();
   public void invoke(Object object, AnalysisContext context) {
	   
	   
	   
	   
	   
	   
	   
	   
	   String sheetName = context.getCurrentSheet().getSheetName();
       System.out.println(sheetName+",当前行："+context.getCurrentRowNum()+"==>"+object);
   }
   private void doSomething(Object object) {
       //1、入库调用接口
   }
   public void doAfterAllAnalysed(AnalysisContext context) {
      // datas.clear();//解析结束销毁不用的资源
   }
   public List<Object> getDatas() {
       return datas;
   }
   public void setDatas(List<Object> datas) {
       this.datas = datas;
   }
}
