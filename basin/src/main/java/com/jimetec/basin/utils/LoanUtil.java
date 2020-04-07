package com.jimetec.basin.utils;

import com.common.lib.utils.LogUtils;
import com.jimetec.basin.DefaultLoanHelp;
import com.jimetec.basin.LoanHelp;


/**
 * 作者:zh
 * 时间:2019-05-22 14:23
 * 描述:
 */
public class LoanUtil {
    static  LoanHelp sLoanHelp;
    public static boolean  isDebug(){
        return LogUtils.getConfig().isLog2ConsoleSwitch();
    }
    public static LoanHelp getLoanHelp()  {
        if (sLoanHelp ==null) {
            sLoanHelp=new DefaultLoanHelp();
        }
        return sLoanHelp;
    }

    public static void setLoanHelp(LoanHelp loanHelp) {
        sLoanHelp = loanHelp;
    }



}
