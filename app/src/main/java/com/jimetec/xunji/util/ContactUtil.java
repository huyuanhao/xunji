package com.jimetec.xunji.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.text.TextUtils;

import com.jimetec.xunji.bean.ContactBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者:zh
 * 时间:1/16/19 11:42 AM
 * 描述:
 */
public class ContactUtil {


    public static List<ContactBean> getContactList(Context context) {
        // 获得通讯录信息 ，URI是ContactsContract.Contacts.CONTENT_URI
        List<ContactBean> datas = new ArrayList<>();
           long curTime = System.currentTimeMillis();

        ContactBean contactBean = null;
        try {
            String mimetype = "";
            int oldrid = -1;
            int contactId = -1;
            Cursor cursor = context.getContentResolver().query(Data.CONTENT_URI, null, null, null, Data.RAW_CONTACT_ID);
            while (cursor.moveToNext()) {
                contactId = cursor.getInt(cursor.getColumnIndex(Data.RAW_CONTACT_ID));
                if (oldrid != contactId) {
                    contactBean = new ContactBean();
                    datas.add(contactBean);
                    oldrid = contactId;
                }

                // 取得mimetype类型
                mimetype = cursor.getString(cursor.getColumnIndex(Data.MIMETYPE));
                // 获得通讯录中每个联系人的ID
                // 获得通讯录中联系人的名字
                if (StructuredName.CONTENT_ITEM_TYPE.equals(mimetype)) {
                    String displayName = cursor.getString(cursor.getColumnIndex(StructuredName.DISPLAY_NAME));
                    contactBean.emergencyName = displayName;
                }
                // 获取电话信息
                if (Phone.CONTENT_ITEM_TYPE.equals(mimetype)) {
                    // 取出电话类型
                    int phoneType = cursor.getInt(cursor.getColumnIndex(Phone.TYPE));
                    // 手机
                    if (phoneType == Phone.TYPE_MOBILE) {
                        String mobile = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                        if (!TextUtils.isEmpty(mobile)) {
                            mobile = mobile.replace(" ", "");
                        }
                        contactBean.emergencyPhone = mobile;
                    }
                }
//
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }


//    /**
//     * 利用系统CallLog获取通话历史记录
//     * @param num  要读取记录的数量
//     * @return
//     */
//    public void getCallHistoryList(Context context, int num) {
//        Cursor cs;
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG)
//                != PackageManager.PERMISSION_GRANTED) {
////            ActivityCompat.requestPermissions(context,
////                    new String[]{Manifest.permission.READ_CALL_LOG}, 1000);
//
//        }
//        cs = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, //系统方式获取通讯录存储地址
//                new String[]{
//                        CallLog.Calls.CACHED_NAME,  //姓名
//                        CallLog.Calls.NUMBER,    //号码
//                        CallLog.Calls.TYPE,  //呼入/呼出(2)/未接
//                        CallLog.Calls.DATE,  //拨打时间
//                        CallLog.Calls.DURATION,   //通话时长
//                }, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
//        int i = 0;
//        if (cs != null && cs.getCount() > 0) {
//            Date date = new Date(System.currentTimeMillis());
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            String date_today = simpleDateFormat.format(date);
//            for (cs.moveToFirst(); (!cs.isAfterLast()) && i < num; cs.moveToNext(), i++) {
//                String callName = cs.getString(0);  //名称
//                String callNumber = cs.getString(1);  //号码
//                //如果名字为空，在通讯录查询一次有没有对应联系人
//                if (callName == null || callName.equals("")){
//                    String[] cols = {ContactsContract.PhoneLookup.DISPLAY_NAME};
//                    //设置查询条件
//                    String selection = ContactsContract.CommonDataKinds.Phone.NUMBER + "='"+callNumber+"'";
//                    Cursor cursor =context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                            cols, selection, null, null);
//                    int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
//                    if (cursor.getCount()>0){
//                        cursor.moveToFirst();
//                        callName = cursor.getString(nameFieldColumnIndex);
//                    }
//                    cursor.close();
//                }
//                //通话类型
//                int callType = Integer.parseInt(cs.getString(2));
//                String callTypeStr = "";
//                switch (callType) {
//                    case CallLog.Calls.INCOMING_TYPE:
//                        callTypeStr = 1+"";
//                        break;
//                    case CallLog.Calls.OUTGOING_TYPE:
//                        callTypeStr = 2+"";
//                        break;
//                    case CallLog.Calls.MISSED_TYPE:
//                        callTypeStr = 3+"";
//                        break;
//                    default:
//                        //其他类型的，例如新增号码等记录不算进通话记录里，直接跳过
//                        LogUtil.e("ssss",""+callType);
//                        i--;
//                        continue;
//                }
//                //拨打时间
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                Date callDate = new Date(Long.parseLong(cs.getString(3)));
//                String callDateStr = sdf.format(callDate);
////                if (callDateStr.equals(date_today)) { //判断是否为今天
////                    sdf = new SimpleDateForsmat("HH:mm");
////                    callDateStr = sdf.format(callDate);
////                } else if (date_today.contains(callDateStr.substring(0, 7))) { //判断是否为当月
////                    sdf = new SimpleDateFormat("dd");
////                    int callDay = Integer.valueOf(sdf.format(callDate));
////
////                    int day = Integer.valueOf(sdf.format(date));
////                    if (day - callDay == 1) {
////                        callDateStr = "昨天";
////                    } else {
////                        sdf = new SimpleDateFormat("MM-dd");
////                        callDateStr = sdf.format(callDate);
////                    }
////                } else if (date_today.contains(callDateStr.substring(0, 4))) { //判断是否为当年
////                    sdf = new SimpleDateFormat("MM-dd");
////                    callDateStr = sdf.format(callDate);
////                }
//
//                //通话时长
//                int callDuration = Integer.parseInt(cs.getString(4));
//                int min = callDuration / 60;
//                int sec = callDuration % 60;
//                String callDurationStr = "";
//                if (sec > 0) {
//                    if (min > 0) {
//                        callDurationStr = min + "分" + sec + "秒";
//                    } else {
//                        callDurationStr = sec + "秒";
//                    }
//                }
//
//                /**
//                 * callName 名字
//                 * callNumber 号码
//                 * callTypeStr 通话类型
//                 * callDateStr 通话日期
//                 * callDurationStr 通话时长
//                 * 请在此处执行相关UI或存储操作，之后会查询下一条通话记录
//                 */
//                LogUtil.e("Msg","callName"+callName);
//                LogUtil.e("Msg","callNumber"+callNumber);
//                LogUtil.e("Msg","callTypeStr"+callTypeStr);
//                LogUtil.e("Msg","callDateStr"+callDateStr);
//                LogUtil.e("Msg","callDurationStr"+callDurationStr);
//            }
//        }
//    }
//
//
//
//





/*
    public static String getContactInfo(Context context) throws JSONException {
        // 获得通讯录信息 ，URI是ContactsContract.Contacts.CONTENT_URI
        JSONObject jsonObject = null;
        JSONObject contactData = new JSONObject();
        String mimetype = "";
        int oldrid = -1;
        int contactId = -1;
        Cursor cursor = context.getContentResolver().query(Data.CONTENT_URI, null, null, null, Data.RAW_CONTACT_ID);
        int numm = 0;
        while (cursor.moveToNext()) {
            contactId = cursor.getInt(cursor.getColumnIndex(Data.RAW_CONTACT_ID));
            if (oldrid != contactId) {
                jsonObject = new JSONObject();
                contactData.put("contact" + numm, jsonObject);
                numm++;
                oldrid = contactId;
            }

            // 取得mimetype类型
            mimetype = cursor.getString(cursor.getColumnIndex(Data.MIMETYPE));
            // 获得通讯录中每个联系人的ID
            // 获得通讯录中联系人的名字
            if (StructuredName.CONTENT_ITEM_TYPE.equals(mimetype)) {
                //            String display_name = cursor.getString(cursor.getColumnIndex(StructuredName.DISPLAY_NAME));
                String prefix = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PREFIX));
                jsonObject.put("prefix", prefix);
                String firstName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                jsonObject.put("firstName", firstName);
                String middleName = cursor.getString(cursor.getColumnIndex(StructuredName.MIDDLE_NAME));
                jsonObject.put("middleName", middleName);
                String lastname = cursor.getString(cursor.getColumnIndex(StructuredName.GIVEN_NAME));
                jsonObject.put("lastname", lastname);
                String suffix = cursor.getString(cursor.getColumnIndex(StructuredName.SUFFIX));
                jsonObject.put("suffix", suffix);
                String phoneticFirstName = cursor.getString(cursor.getColumnIndex(StructuredName.PHONETIC_FAMILY_NAME));
                jsonObject.put("phoneticFirstName", phoneticFirstName);
                String phoneticMiddleName = cursor.getString(cursor.getColumnIndex(StructuredName.PHONETIC_MIDDLE_NAME));
                jsonObject.put("phoneticMiddleName", phoneticMiddleName);
                String phoneticLastName = cursor.getString(cursor.getColumnIndex(StructuredName.PHONETIC_GIVEN_NAME));
                jsonObject.put("phoneticLastName", phoneticLastName);
            }
            // 获取电话信息
            if (Phone.CONTENT_ITEM_TYPE.equals(mimetype)) {
                // 取出电话类型
                int phoneType = cursor.getInt(cursor.getColumnIndex(Phone.TYPE));
                // 手机
                if (phoneType == Phone.TYPE_MOBILE) {
                    String mobile = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("mobile", mobile);
                }
                // 住宅电话
                if (phoneType == Phone.TYPE_HOME) {
                    String homeNum = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("homeNum", homeNum);
                }
                // 单位电话
                if (phoneType == Phone.TYPE_WORK) {
                    String jobNum = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("jobNum", jobNum);
                }
                // 单位传真
                if (phoneType == Phone.TYPE_FAX_WORK) {
                    String workFax = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("workFax", workFax);
                }
                // 住宅传真
                if (phoneType == Phone.TYPE_FAX_HOME) {
                    String homeFax = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("homeFax", homeFax);
                }
                // 寻呼机
                if (phoneType == Phone.TYPE_PAGER) {
                    String pager = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("pager", pager);
                }
                // 回拨号码
                if (phoneType == Phone.TYPE_CALLBACK) {
                    String quickNum = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("quickNum", quickNum);
                }
                // 公司总机
                if (phoneType == Phone.TYPE_COMPANY_MAIN) {
                    String jobTel = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("jobTel", jobTel);
                }
                // 车载电话
                if (phoneType == Phone.TYPE_CAR) {
                    String carNum = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("carNum", carNum);
                }
                // ISDN
                if (phoneType == Phone.TYPE_ISDN) {
                    String isdn = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("isdn", isdn);
                }
                // 总机
                if (phoneType == Phone.TYPE_MAIN) {
                    String tel = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("tel", tel);
                }
                // 无线装置
                if (phoneType == Phone.TYPE_RADIO) {
                    String wirelessDev = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("wirelessDev", wirelessDev);
                }
                // 电报
                if (phoneType == Phone.TYPE_TELEX) {
                    String telegram = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("telegram", telegram);
                }
                // TTY_TDD
                if (phoneType == Phone.TYPE_TTY_TDD) {
                    String tty_tdd = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("tty_tdd", tty_tdd);
                }
                // 单位手机
                if (phoneType == Phone.TYPE_WORK_MOBILE) {
                    String jobMobile = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("jobMobile", jobMobile);
                }
                // 单位寻呼机
                if (phoneType == Phone.TYPE_WORK_PAGER) {
                    String jobPager = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("jobPager", jobPager);
                }
                // 助理
                if (phoneType == Phone.TYPE_ASSISTANT) {
                    String assistantNum = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("assistantNum", assistantNum);
                }
                // 彩信
                if (phoneType == Phone.TYPE_MMS) {
                    String mms = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    jsonObject.put("mms", mms);
                }
            }
            // }
            // 查找email地址
            if (Email.CONTENT_ITEM_TYPE.equals(mimetype)) {
                // 取出邮件类型
                int emailType = cursor.getInt(cursor.getColumnIndex(Email.TYPE));

                // 住宅邮件地址
                if (emailType == Email.TYPE_CUSTOM) {
                    String homeEmail = cursor.getString(cursor.getColumnIndex(Email.DATA));
                    jsonObject.put("homeEmail", homeEmail);
                }

                // 住宅邮件地址
                else if (emailType == Email.TYPE_HOME) {
                    String homeEmail = cursor.getString(cursor.getColumnIndex(Email.DATA));
                    jsonObject.put("homeEmail", homeEmail);
                }
                // 单位邮件地址
                if (emailType == Email.TYPE_CUSTOM) {
                    String jobEmail = cursor.getString(cursor.getColumnIndex(Email.DATA));
                    jsonObject.put("jobEmail", jobEmail);
                }

                // 单位邮件地址
                else if (emailType == Email.TYPE_WORK) {
                    String jobEmail = cursor.getString(cursor.getColumnIndex(Email.DATA));
                    jsonObject.put("jobEmail", jobEmail);
                }
                // 手机邮件地址
                if (emailType == Email.TYPE_CUSTOM) {
                    String mobileEmail = cursor.getString(cursor.getColumnIndex(Email.DATA));
                    jsonObject.put("mobileEmail", mobileEmail);
                }

                // 手机邮件地址
                else if (emailType == Email.TYPE_MOBILE) {
                    String mobileEmail = cursor.getString(cursor.getColumnIndex(Email.DATA));
                    jsonObject.put("mobileEmail", mobileEmail);
                }
            }
            // 查找event地址
            if (ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE.equals(mimetype)) {
                // 取出时间类型
                int eventType = cursor.getInt(cursor.getColumnIndex(Event.TYPE));
                // 生日
                if (eventType == Event.TYPE_BIRTHDAY) {
                    String birthday = cursor.getString(cursor.getColumnIndex(Event.START_DATE));
                    jsonObject.put("birthday", birthday);
                }
                // 周年纪念日
                if (eventType == Event.TYPE_ANNIVERSARY) {
                    String anniversary = cursor.getString(cursor.getColumnIndex(Event.START_DATE));
                    jsonObject.put("anniversary", anniversary);
                }
            }
            // 即时消息
            if (Im.CONTENT_ITEM_TYPE.equals(mimetype)) {
                // 取出即时消息类型
                int protocal = cursor.getInt(cursor.getColumnIndex(Im.PROTOCOL));
                if (Im.TYPE_CUSTOM == protocal) {
                    String workMsg = cursor.getString(cursor.getColumnIndex(Im.DATA));
                    jsonObject.put("workMsg", workMsg);
                } else if (Im.PROTOCOL_MSN == protocal) {
                    String workMsg = cursor.getString(cursor.getColumnIndex(Im.DATA));
                    jsonObject.put("workMsg", workMsg);
                }
                if (Im.PROTOCOL_QQ == protocal) {
                    String instantsMsg = cursor.getString(cursor.getColumnIndex(Im.DATA));
                    jsonObject.put("instantsMsg", instantsMsg);
                }
            }
            // 获取备注信息
            if (Note.CONTENT_ITEM_TYPE.equals(mimetype)) {
                String remark = cursor.getString(cursor.getColumnIndex(Note.NOTE));
                jsonObject.put("remark", remark);
            }
            // 获取昵称信息
            if (Nickname.CONTENT_ITEM_TYPE.equals(mimetype)) {
                String nickName = cursor.getString(cursor.getColumnIndex(Nickname.NAME));
                jsonObject.put("nickName", nickName);
            }
            // 获取组织信息
            if (Organization.CONTENT_ITEM_TYPE.equals(mimetype)) {
                // 取出组织类型
                int orgType = cursor.getInt(cursor.getColumnIndex(Organization.TYPE));
                // 单位
                if (orgType == Organization.TYPE_CUSTOM) {
                    //             if (orgType == Organization.TYPE_WORK) {
                    String company = cursor.getString(cursor.getColumnIndex(Organization.COMPANY));
                    jsonObject.put("company", company);
                    String jobTitle = cursor.getString(cursor.getColumnIndex(Organization.TITLE));
                    jsonObject.put("jobTitle", jobTitle);
                    String department = cursor.getString(cursor.getColumnIndex(Organization.DEPARTMENT));
                    jsonObject.put("department", department);
                }
            }
            // 获取网站信息
            if (Website.CONTENT_ITEM_TYPE.equals(mimetype)) {
                // 取出组织类型
                int webType = cursor.getInt(cursor.getColumnIndex(Website.TYPE));
                // 主页
                if (webType == Website.TYPE_CUSTOM) {
                    String home = cursor.getString(cursor.getColumnIndex(Website.URL));
                    jsonObject.put("home", home);
                }
                // 主页
                else if (webType == Website.TYPE_HOME) {
                    String home = cursor.getString(cursor.getColumnIndex(Website.URL));
                    jsonObject.put("home", home);
                }

                // 个人主页
                if (webType == Website.TYPE_HOMEPAGE) {
                    String homePage = cursor.getString(cursor.getColumnIndex(Website.URL));
                    jsonObject.put("homePage", homePage);
                }
                // 工作主页
                if (webType == Website.TYPE_WORK) {
                    String workPage = cursor.getString(cursor.getColumnIndex(Website.URL));
                    jsonObject.put("workPage", workPage);
                }
            }
            // 查找通讯地址
            if (StructuredPostal.CONTENT_ITEM_TYPE.equals(mimetype)) {
                // 取出邮件类型
                int postalType = cursor.getInt(cursor.getColumnIndex(StructuredPostal.TYPE));
                // 单位通讯地址
                if (postalType == StructuredPostal.TYPE_WORK) {
                    String street = cursor.getString(cursor.getColumnIndex(StructuredPostal.STREET));
                    jsonObject.put("street", street);
                    String ciry = cursor.getString(cursor.getColumnIndex(StructuredPostal.CITY));
                    jsonObject.put("ciry", ciry);
//                    String box = cursor.getString(cursor.getColumnIndex(StructuredPostal.POBOX));
                    jsonObject.put("box", box);
                    String area = cursor.getString(cursor.getColumnIndex(StructuredPostal.NEIGHBORHOOD));
                    jsonObject.put("area", area);
                    String state = cursor.getString(cursor.getColumnIndex(StructuredPostal.REGION));
                    jsonObject.put("state", state);
                    String zip = cursor.getString(cursor.getColumnIndex(StructuredPostal.POSTCODE));
                    jsonObject.put("zip", zip);
                    String country = cursor.getString(cursor.getColumnIndex(StructuredPostal.COUNTRY));
                    jsonObject.put("country", country);
                }
                // 住宅通讯地址
                if (postalType == StructuredPostal.TYPE_HOME) {
                    String homeStreet = cursor.getString(cursor.getColumnIndex(StructuredPostal.STREET));
                    jsonObject.put("homeStreet", homeStreet);
                    String homeCity = cursor.getString(cursor.getColumnIndex(StructuredPostal.CITY));
                    jsonObject.put("homeCity", homeCity);
                    String homeBox = cursor.getString(cursor.getColumnIndex(StructuredPostal.POBOX));
                    jsonObject.put("homeBox", homeBox);
                    String homeArea = cursor.getString(cursor.getColumnIndex(StructuredPostal.NEIGHBORHOOD));
                    jsonObject.put("homeArea", homeArea);
                    String homeState = cursor.getString(cursor.getColumnIndex(StructuredPostal.REGION));
                    jsonObject.put("homeState", homeState);
                    String homeZip = cursor.getString(cursor.getColumnIndex(StructuredPostal.POSTCODE));
                    jsonObject.put("homeZip", homeZip);
                    String homeCountry = cursor.getString(cursor.getColumnIndex(StructuredPostal.COUNTRY));
                    jsonObject.put("homeCountry", homeCountry);
                }
                // 其他通讯地址
                if (postalType == StructuredPostal.TYPE_OTHER) {
                    String otherStreet = cursor.getString(cursor.getColumnIndex(StructuredPostal.STREET));
                    jsonObject.put("otherStreet", otherStreet);
                    String otherCity = cursor.getString(cursor.getColumnIndex(StructuredPostal.CITY));
                    jsonObject.put("otherCity", otherCity);
                    String otherBox = cursor.getString(cursor.getColumnIndex(StructuredPostal.POBOX));
                    jsonObject.put("otherBox", otherBox);
                    String otherArea = cursor.getString(cursor.getColumnIndex(StructuredPostal.NEIGHBORHOOD));
                    jsonObject.put("otherArea", otherArea);
                    String otherState = cursor.getString(cursor.getColumnIndex(StructuredPostal.REGION));
                    jsonObject.put("otherState", otherState);
                    String otherZip = cursor.getString(cursor.getColumnIndex(StructuredPostal.POSTCODE));
                    jsonObject.put("otherZip", otherZip);
                    String otherCountry = cursor.getString(cursor.getColumnIndex(StructuredPostal.COUNTRY));
                    jsonObject.put("otherCountry", otherCountry);
                }
            }
        }
        cursor.close();
        LogUtil.e("contactData", contactData.toString());
        return contactData.toString();
    }*/


}
