package com.jimetec.basin;

import java.util.Map;

/**
 * 作者:zh
 * 时间:2019-05-22 11:57
 * 描述:
 */
public interface LoanHelp {

    /**
     * @return  更新app
     */
    boolean isUpdateApp();

    /**
     * @return  强制更新
     */
    boolean isMustUpdate();

    /**
     * @return 下载路径
     */
    String getUpgradeUrl();

    /**
     * @return 贷超h5
     */
    String geth5HomePageUrl();

    /**
     * @return 多久上报一次 已安装列表
     */
    long getApplistReportTime();

    /**
     * @return 多久弹窗
     */
    long getonfidePopFrameTime();

    /**
     * @param json 提交json点击事件
     */
    void  submitGson(String json);

    /**
     * @param json 提交json点击事件
     */
    void  submitScreenshot(String json);


    /**
     * @param json 提交json点击事件
     */
    void  submitUserAppList(String json);

    /**
     * @param json 停留时间
     */
    void  submitProdStayTime(String json);

    /**
     * @param map  上报弹窗信息
     */
    void  feed(Map<String ,String> map);


}
