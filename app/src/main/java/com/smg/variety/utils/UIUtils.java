package com.smg.variety.utils;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.smg.variety.base.BaseApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class UIUtils {
    public static Context getContext(){
        return BaseApplication.getContext();
    }
    public static Resources getResources(){
        return getContext().getResources();
    }
    public static int getColor(int id){
        return getResources().getColor(id);
    }
    public static Handler getMainHandler(){
        return BaseApplication.getHandler();
    }
    public static void postDelayed(Runnable runnable, long delayMillis ){
        getMainHandler().postDelayed(runnable, delayMillis);
    }

    public static void removeTask(Runnable task) {
        getMainHandler().removeCallbacks(task);
    }

    /**
     * 倒计时 x分x秒
     * @param second
     * @return
     */
    public static String  getTime(int second) {
        if (second < 0) {
            return "0秒";
        }
        if (second < 60) {
            return second + "秒";
        }

        int minute = second / 60;
        second = second - minute * 60;
        return minute + "分" + second + "秒";
    }
    /**
     * 倒计时 x分x秒
     * @param second
     * @return
     */
    public static String  getTime3(int second) {
        if (second < 60) {
            return "0分"+second+"秒";
        }

        if (second < 3600) {
            int minute = second / 60;
            second = second - minute * 60;
            return minute+"分"+second+"秒";
        }

        int hour = second / 3600;
        int minute = (second - hour * 3600) / 60;
        second = second - hour * 3600 - minute * 60;
        return hour+"小时"+minute+"分"+second+"秒";
    }

    /**
     *
     * @param second
     * @return
     */
    public static String  getTime2(int second) {
        if (second < 10) {
            return "00:0" + second;
        }
        if (second < 60) {
            return "00:" + second;
        }

        if (second < 3600) {
            int minute = second / 60;
            second = second - minute * 60;
            if (minute < 10) {
                if (second < 10) {
                    return "0" + minute + ":0" + second;
                }
                return "0" + minute + ":" + second;
            }

            if (second < 10) {
                return minute + ":0" + second;
            }
            return minute + ":" + second;
        }

        int hour = second / 3600;
        int minute = (second - hour * 3600) / 60;
        second = second - hour * 3600 - minute * 60;

        if (hour < 10) {
            if (minute < 10) {
                if (second < 10) {
                    return "0" + hour + ":0" + minute + ":0" + second;
                }
                return "0" + hour + ":0" + minute + ":" + second;
            }
            if (second < 10) {
                return "0" + hour + ":"+minute + ":0" + second;
            }
            return "0" + hour +":"+ minute + ":" + second;
        }

        if (minute < 10) {
            if (second < 10) {
                return hour + ":0" + minute + ":0" + second;
            }
            return hour + ":0" + minute + ":" + second;
        }

        if (second < 10) {
            return hour + ":" + minute + ":0" + second;
        }
        return hour+ ":" + minute + ":" + second;
    }
    /**
     *
     * @param second
     * @return
     */
    public static String  getTimeHMS(int second) {
        if (second < 10) {
            return "00:00:0" + second;
        }
        if (second < 60) {
            return "00:00:" + second;
        }

        if (second < 3600) {
            int minute = second / 60;
            second = second - minute * 60;
            if (minute < 10) {
                if (second < 10) {
                    return "00:0" + minute + ":0" + second;
                }
                return "00:0" + minute + ":" + second;
            }

            if (second < 10) {
                return "00:"+ minute + ":0" + second;
            }
            return "00:"+minute + ":" + second;
        }

        int hour = second / 3600;
        int minute = (second - hour * 3600) / 60;
        second = second - hour * 3600 - minute * 60;

        if (hour < 10) {
            if (minute < 10) {
                if (second < 10) {
                    return "0" + hour + ":0" + minute + ":0" + second;
                }
                return "0" + hour + ":0" + minute + ":" + second;
            }
            if (second < 10) {
                return "0" + hour + ":"+minute + ":0" + second;
            }
            return "0" + hour +":"+ minute + ":" + second;
        }

        if (minute < 10) {
            if (second < 10) {
                return hour + ":0" + minute + ":0" + second;
            }
            return hour + ":0" + minute + ":" + second;
        }

        if (second < 10) {
            return hour + ":" + minute + ":0" + second;
        }
        return hour+ ":" + minute + ":" + second;
    }

    /**
     *
     * @param textView
     * @param redId
     * @param orientation
     *          方向 0左 1上 2右 3下
     */
    public static void changeTextViewIcon(TextView textView, int redId, int orientation){
        Drawable drawable = getContext().getResources().getDrawable(redId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        if (orientation == 0) {
            textView.setCompoundDrawables(drawable, null, null, null);
        } else if (orientation == 1) {
            textView.setCompoundDrawables(null, drawable, null, null);
        }else if (orientation == 2) {
            textView.setCompoundDrawables(null, null, drawable, null);
        }else if (orientation == 3) {
            textView.setCompoundDrawables(null, null, null, drawable);
        }
    }

    public static void finishLoadData(int mPage, SmartRefreshLayout mRefreshLayout) {
        if(mPage<0 || mRefreshLayout == null){
            return;
        }
        if (mPage <= 1) {
            mRefreshLayout.finishRefresh();

        } else {

            mRefreshLayout.setEnableRefresh(true);
        }
    }

    public static int getLearnTime(int leaningTime) {
        if (leaningTime <= 0) {
            return 0;
        }
        return ((leaningTime % 60) == 0 ? (leaningTime / 60) : (leaningTime / 60) + 1);
    }

    public static String stringFilter(String str) throws PatternSyntaxException {
        //只允许汉字
        String regEx = "[^\u4E00-\u9FA5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static String getVideoDuration(Integer videoDuration) {
        if(videoDuration == null){
            return "";
        }
        return getTimeHMS(videoDuration);
    }
}

