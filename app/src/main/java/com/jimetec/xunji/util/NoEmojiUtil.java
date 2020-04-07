package com.jimetec.xunji.util;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者:capTain
 * 时间:2019-09-16 18:22
 * 描述:
 */
public class NoEmojiUtil {

    private static InputFilter emojiFilter = new InputFilter() {


        Pattern emoji = Pattern.compile(


                "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",


                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);


        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dstart,


                                   int dend) {


            Matcher emojiMatcher = emoji.matcher(source);


            if (emojiMatcher.find()) {


                return "";


            }
            return null;


        }
    };

    public static InputFilter[] emojiFilters = {emojiFilter};

    public static void setNoEmoji(EditText et) {
        et.setFilters(emojiFilters);

    }


}
