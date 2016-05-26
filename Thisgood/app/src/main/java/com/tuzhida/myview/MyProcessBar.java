package com.tuzhida.myview;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tuzhida.thisgood.R;

/**
 * Created by Paul-Sartre on 2015/12/5.
 */
public class MyProcessBar extends RelativeLayout {
    protected static final int STOP = 0x108;
    protected static final int NEXT = 0x109;

    private int m_max = 100;
    private int m_process = 0;
    private ImageView mImageView = null;
    private LayoutParams params;
    private Handler mHandler;
    private boolean isModified = false;
    private Thread mThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                mThread.sleep(20);
//                m_process++;
//                setProgress(m_process);
                reflashPorcess(m_process);// 界面的修改，交由线程来处理
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    public MyProcessBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyProcessBar(Context context) {
        super(context);
        init();
    }

    private void init() {
        mHandler = new Handler(getContext().getMainLooper());
        params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mThread.start();
    }

    public void setMax(int max) {
        m_max = max;
    }

    public int getMax() {
        return m_max;
    }

    public void setProgress(int process) {
        if (process <= m_max) {
            m_process = process;
            mHandler.post(mThread);
        }else {
            m_process = m_max;
            mHandler.post(mThread);
        }
    }

    public int getProgress() {
        return m_process;
    }

    private int getCountLength() {
        return (getHeight()) * m_process / m_max;
    }

    private void reflashPorcess(int process) {
        if (mImageView != null)
            removeView(mImageView);
        mImageView = null;
        mImageView = new ImageView(getContext());
        mImageView.setAdjustViewBounds(true);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mImageView.setImageResource(R.drawable.widget_battery_bg1);
        params.height = getCountLength();
        addView(mImageView, params);
    }
}