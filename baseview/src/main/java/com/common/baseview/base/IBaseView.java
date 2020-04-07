package com.common.baseview.base;


/**
 * @作者 zh
 * @时间 2018/8/6 下午2:58
 * @描述
 */
public  interface IBaseView {

      void loadingNetData();
     /**
      * 显示加载中的页面
      */
      void showLoadingPage();

     /**
      * 显示空布局的页面
      */
     void showEmptyPage();

     /**
      * 显示成功的页面
      */
     void showSuccessPage();

     /**
      * @param msg
      * 弹出错误信息
      */
     void showErrorMsg(String code, String msg);

     /**
      * @param code
      * @param msg
      * 页面显示错误信息
      */
     void showErrorPage(String code, String msg);

     /**
      * 网络加载数据结束   隐藏  以及状态更改
      */
     void onFinish();





}
