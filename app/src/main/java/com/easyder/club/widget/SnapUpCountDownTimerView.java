package com.easyder.club.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easyder.club.R;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("HandlerLeak")
public class SnapUpCountDownTimerView extends LinearLayout {

    private TextView tv_hour_decade;
    private TextView tv_hour_unit;
    private TextView tv_min_decade;
    private TextView tv_min_unit;
    private TextView tv_sec_decade;
    private TextView tv_sec_unit;

    private Context context;

    private int hour_decade;
    private int hour_unit;
    private int min_decade;
    private int min_unit;
    private int sec_decade;
    private int sec_unit;

    private Timer timer;

    public SnapUpCountDownTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_countdowntimer, this);

        tv_hour_decade = view.findViewById(R.id.tv_hour_decade);
        tv_hour_unit = view.findViewById(R.id.tv_hour_unit);
        tv_min_decade = view.findViewById(R.id.tv_min_decade);
        tv_min_unit = view.findViewById(R.id.tv_min_unit);
        tv_sec_decade = view.findViewById(R.id.tv_sec_decade);
        tv_sec_unit = view.findViewById(R.id.tv_sec_unit);

    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    /**
     * ==========================================================================================
     **/

    private long mTime = 0;
    private boolean isRefresh;//改字段用来

    public void startTime(long nowTime, long endTime) {
        long mTime = endTime > nowTime ? endTime - nowTime : 0;
        startTime(mTime);
    }

    public void startTime(long time) {
        mTime = time;
        isRefresh = mTime > 0 ? true : false;
        setTime(getHour(mTime), getMinute(mTime), getSecond(mTime));
    }

    /**
     * 设置时间显示
     *
     * @param hour
     * @param min
     * @param sec
     */

    public void setTime(int hour, int min, int sec) {
        //处理异常情况
//        hour = hour > 99 ? 99 : hour;
        min = min >= 60 ? 59 : min;
        sec = sec >= 60 ? 59 : sec;

        //时
        hour_decade = hour / 10;
        hour_unit = hour % 10;
        //分
        min_decade = min / 10;
        min_unit = min % 10;
        //秒
        sec_decade = sec / 10;
        sec_unit = sec % 10;

        tv_hour_decade.setText(hour_decade + "");
        tv_hour_unit.setText(hour_unit + "");
        tv_min_decade.setText(min_decade + "");
        tv_min_unit.setText(min_unit + "");
        tv_sec_decade.setText(sec_decade + "");
        tv_sec_unit.setText(sec_unit + "");
        //时间自动到期回调刷新
        if (hour <= 0 && min <= 0 && sec <= 0 && isRefresh) {
            if (outOfTimeListener != null) {
                outOfTimeListener.outOfTime();
            }
        }
    }

    public void start() {
        if (timer != null) {
            stop();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 0, 1000);

    }

    /**
     * 进行--操作
     */
    private void countReduce() {
        if (mTime > 0) {
            mTime--;
            setTime(getHour(mTime), getMinute(mTime), getSecond(mTime));
        } else {
            //重置
            stop();
            setTime(0, 0, 0);
        }
    }

    /**
     * 根据秒值获取小时
     *
     * @param time
     * @return 除3600
     */
    private int getHour(long time) {
        return (int) (time / 60 / 60);
    }

    /**
     * 根据秒值获取分钟
     *
     * @param time
     * @return 除60然后对60求余
     */
    private int getMinute(long time) {
        return (int) (time / 60 % 60);
    }

    /**
     * @param time
     * @return 对60求余数
     */
    private int getSecond(long time) {
        return (int) (time % 60);
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            countReduce();
        }
    };

    public interface OutOfTimeListener {
        void outOfTime();
    }

    public OutOfTimeListener outOfTimeListener;

    public void setOutOfTimeListener(OutOfTimeListener outOfTimeListener) {
        this.outOfTimeListener = outOfTimeListener;
    }
}
