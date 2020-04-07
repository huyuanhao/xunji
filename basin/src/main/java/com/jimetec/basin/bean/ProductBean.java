package com.jimetec.basin.bean;


import java.io.Serializable;
import java.util.List;

/**
 * 作者:zh
 * 时间:2018/9/4 下午2:04
 * 描述:
 */
public class ProductBean  implements Serializable{
    /**
     * prodId : 3
     * prodTypeId : 13
     * prodName : 小钱贷666
     * prodHighlight : gggg123
     * prodTitle : title333
     * prodUrl : url
     * prodIcon : /icon
     * totalApply : 1000
     * lendingTime : 5小时
     * dayInterestRate :
     * amountLowerLimit : 1000
     * amountUpperLimit : 2000000
     * amountLimitStr : null
     * termLowerLimit : 2
     * termUpperLimit : 36
     * loanTermType : 1
     * termLimitStr : null
     * applyQualification : xxxxxxxxxxxxxx
     * applyCationinformation : 33333333333333
     * prodIntroduction : ddddddddd
     * applyProcess : null
     * isSkipUrl : 1
     */

    public int prodId;
    /**
     *
     */
    public int prodTypeId;
    /**
     *
     */
    public String prodName;
    /**
     *
     */
    public List<String> prodHighlights;

    /**
     *
     */
    public String prodTitle;
    public String prodUrl;
    public String prodIcon;
    public String totalApply;
    public String lendingTime;
    public String dayInterestRate;
    public int amountLowerLimit;
    public int amountUpperLimit;
    public String amountLimitStr;
    public int termLowerLimit;
    public int termUpperLimit;
    public int loanTermType;
    public String termLimitStr;
    public String applyQualification;
    public String applyCationinformation;
    public String prodIntroduction;
    public List<String> listApplyProcess;

//    public List<String> applyProcess;
//    public Object applyProcess;
    public int isSkipUrl;
    public int creditCard;

    @Override
    public String toString() {
        return "ProductBean{" +
                "prodId=" + prodId +
                ", prodTypeId=" + prodTypeId +
                ", prodName='" + prodName + '\'' +
                ", prodHighlights=" + prodHighlights +
                ", prodTitle='" + prodTitle + '\'' +
                ", prodUrl='" + prodUrl + '\'' +
                ", prodIcon='" + prodIcon + '\'' +
                ", totalApply='" + totalApply + '\'' +
                ", lendingTime='" + lendingTime + '\'' +
                ", dayInterestRate='" + dayInterestRate + '\'' +
                ", amountLowerLimit=" + amountLowerLimit +
                ", amountUpperLimit=" + amountUpperLimit +
                ", amountLimitStr='" + amountLimitStr + '\'' +
                ", termLowerLimit=" + termLowerLimit +
                ", termUpperLimit=" + termUpperLimit +
                ", loanTermType=" + loanTermType +
                ", termLimitStr='" + termLimitStr + '\'' +
                ", applyQualification='" + applyQualification + '\'' +
                ", applyCationinformation='" + applyCationinformation + '\'' +
                ", prodIntroduction='" + prodIntroduction + '\'' +
                ", listApplyProcess=" + listApplyProcess +
                ", isSkipUrl=" + isSkipUrl +
                ", creditCard=" + creditCard +
                '}';
    }

   /*
    *
    *
  `prod_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '产品id',
  `prod_name` varchar(50) NOT NULL COMMENT '产品名称',
  `prod_highlight` varchar(255) DEFAULT NULL COMMENT '产品高亮词',
  `prod_title` varchar(255) DEFAULT NULL COMMENT '产品标题',
  `prod_url` varchar(255) DEFAULT NULL COMMENT '产品url',
  `prod_icon` varchar(255) DEFAULT NULL COMMENT '产品logo',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `prod_state` smallint(1) DEFAULT '1' COMMENT '产品状态 1有效 2失效',
  `credit_card` smallint(6) DEFAULT NULL COMMENT '是否有信用卡 1 有 2无',
  `total_apply` varchar(32) DEFAULT NULL COMMENT '申请人数',
  `lending_time` varchar(32) DEFAULT NULL COMMENT '放款时间',
  `day_interest_rate` varchar(32) DEFAULT NULL COMMENT '日利率',
  `amount_lower_limit` int(11) DEFAULT NULL COMMENT '金额下限',
  `amount_upper_limit` int(11) DEFAULT NULL COMMENT '金额上限',
  `term_lower_limit` int(6) DEFAULT NULL COMMENT '最低期限',
  `term_upper_limit` int(6) DEFAULT NULL COMMENT '最高期限',
  `loan_term_type` smallint(6) DEFAULT NULL COMMENT '借款期限方式 1按天计算 2按月计算',
  `apply_qualification` varchar(1024) DEFAULT NULL COMMENT '申请资格',
  `apply_cationInformation` varchar(2048) DEFAULT NULL COMMENT '申请资料',
  `prod_introduction` varchar(1024) DEFAULT NULL COMMENT '产品介绍',
  `apply_process` varchar(125) DEFAULT NULL COMMENT '申请过程',

    *
    *
    * */
    /**
     * amountLimitStr : 1000~2000000
     * amountLowerLimit : 1000
     * amountUpperLimit : 2000000
     * applyCationinformation : 33333333333333
     * applyQualification : xxxxxxxxxxxxxx
     * isSkipUrl : 1
     * lendingTime : 5小时
     * loanTermType : 1
     * prodHighlight : gggg
     * prodIcon : /icon
     * prodId : 1
     * prodIntroduction : ddddddddd
     * prodName : 小钱贷
     * prodTitle : title
     * prodTypeId : 10
     * prodUrl : url
     * termLimitStr : 2-36天
     * termLowerLimit : 2
     * termUpperLimit : 36
     * totalApply : 1000
     */


//    public long id;
//    public String amountLimitStr;
//    public int amountLowerLimit;
//    public int amountUpperLimit;
//    public String applyCationinformation;
//    public String applyQualification;
//    public int isSkipUrl;
//    public String lendingTime;
//    public int creditCard;
//    public String dayInterestRate;
//    public int loanTermType;
//    public String prodHighlight;
//    public String prodIcon;
//    public int prodId;
//    public String prodIntroduction;
//    public String prodName;
//    public String prodTitle;
//    public int prodTypeId;
//    public String prodUrl;
//    public String termLimitStr;
//    public int termLowerLimit;
//    public int termUpperLimit;
//    public String totalApply;
//    public maxTermUpperLimit;







}
