package cn.csu.software.wechat.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.csu.software.wechat.R;
import cn.csu.software.wechat.constant.ConstantData;
import cn.csu.software.wechat.util.LogUtil;

import java.io.File;
import java.io.IOException;

/**
 * 录音按钮
 *
 * @author 来自网络
 * @since 2019-11-12
 */
public class RecordButton extends android.support.v7.widget.AppCompatButton {
    private static final String TAG = RecordButton.class.getSimpleName();

    /**
     * 最短录音时间
     **/
    private static final int MIN_INTERVAL_TIME = 1000;

    /**
     * 最长录音时间
     **/
    private static final int MAX_INTERVAL_TIME = 1000 * 60;

    private Context mContext;

    private View view;

    private String mFile;

    private OnFinishedRecordListener finishedListener;

    private TextView mStateTv;

    private ImageView mStateIv;

    private MediaRecorder mRecorder;

    private ObtainDecibelThread mThread;

    private Handler volumeHandler;

    private float dy;

    private AnimationDrawable anim;

    private long startTime;

    private Dialog recordDialog;

    private int[] res = {R.mipmap.ic_volume_0, R.mipmap.ic_volume_1, R.mipmap.ic_volume_2,
        R.mipmap.ic_volume_3, R.mipmap.ic_volume_4, R.mipmap.ic_volume_5, R.mipmap.ic_volume_6,
        R.mipmap.ic_volume_7, R.mipmap.ic_volume_8};

    private DialogInterface.OnDismissListener onDismiss = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            stopRecording();
        }
    };

    /**
     * 构造函数
     *
     * @param context Context
     */
    public RecordButton(Context context) {
        super(context);
        mContext = context;
        init();
    }

    /**
     * 构造函数
     *
     * @param context Context
     * @param attrs AttributeSet
     */
    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }
    /**
     * 构造函数
     *
     * @param context Context
     * @param attrs AttributeSet
     * @param defStyle int
     */
    public RecordButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }


    public void setOnFinishedRecordListener(OnFinishedRecordListener listener) {
        finishedListener = listener;
    }

    @SuppressLint("HandlerLeak")
    private void init() {
        volumeHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == -100) {
                    stopRecording();
                    recordDialog.dismiss();
                } else if (msg.what != -1) {
                    mStateIv.setImageResource(res[msg.what]);
                }
            }
        };
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        dy = event.getY();
        if (mStateTv != null && mStateIv != null && dy < 0) {
            mStateTv.setText("松开手指,取消发送");
            mStateIv.setImageResource(R.mipmap.ic_volume_cancel);
        } else if (mStateTv != null) {
            mStateTv.setText("手指上滑,取消发送");
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setText("松开发送");
                initDialogAndStartRecord();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                this.setText("按住录音");
                if (dy >= 0 && (System.currentTimeMillis() - startTime <= MAX_INTERVAL_TIME)) {
                    LogUtil.d(TAG, "结束录音:");
                    finishRecord();
                } else if (dy < 0) { // 当手指向上滑，会cancel
                    cancelRecord();
                }
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 初始化录音对话框 并 开始录音
     */
    private void initDialogAndStartRecord() {
        startTime = System.currentTimeMillis();
        mFile = mContext.getFilesDir().getPath() + File.separator + ConstantData.VOICE_DIRECTORY
            + File.separator + startTime + ConstantData.EXTENSION_NAME_MP3;
        recordDialog = new Dialog(getContext(), R.style.like_toast_dialog_style);
        view = View.inflate(getContext(), R.layout.dialog_record, null);
        mStateIv = view.findViewById(R.id.rc_audio_state_image);
        mStateTv = view.findViewById(R.id.rc_audio_state_text);
        mStateIv.setImageResource(R.drawable.anim_mic);
        anim = (AnimationDrawable) mStateIv.getDrawable();
        anim.start();
        mStateIv.setVisibility(View.VISIBLE);
        mStateTv.setVisibility(View.VISIBLE);
        mStateTv.setText("手指上滑,取消发送");
        recordDialog.setContentView(view, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT));
        recordDialog.setOnDismissListener(onDismiss);
        WindowManager.LayoutParams lp = recordDialog.getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        startRecording();
        recordDialog.show();
    }

    /**
     * 放开手指，结束录音处理
     */
    private void finishRecord() {
        long intervalTime = System.currentTimeMillis() - startTime;
        if (intervalTime < MIN_INTERVAL_TIME) {
            LogUtil.d(TAG, "录音时间太短");
            volumeHandler.sendEmptyMessageDelayed(-100, 500);
            mStateIv.setImageResource(R.mipmap.ic_volume_wraning);
            mStateTv.setText("录音时间太短");
            anim.stop();
            File file = new File(mFile);
            file.delete();
            return;
        } else {
            stopRecording();
            recordDialog.dismiss();
        }
        LogUtil.d(TAG, "录音完成的路径:" + mFile);
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(mFile);
            mediaPlayer.prepare();
            mediaPlayer.getDuration();
            LogUtil.d(TAG, "获取到的时长:" + mediaPlayer.getDuration() / 1000);
        } catch (IOException e) {
            LogUtil.i(TAG, "IOException");
        }

        if (finishedListener != null) {
            finishedListener.onFinishedRecord(mFile, mediaPlayer.getDuration() / 1000);
        }
    }

    /**
     * 取消录音对话框和停止录音
     */
    public void cancelRecord() {
        stopRecording();
        recordDialog.dismiss();
        File file = new File(mFile);
        file.delete();
    }

    /**
     * 执行录音操作
     */
    private void startRecording() {
        if (mRecorder != null) {
            mRecorder.reset();
        } else {
            mRecorder = new MediaRecorder();
        }
        mThread = new ObtainDecibelThread();
        mThread.start();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        File file = new File(mFile);
        LogUtil.d(TAG, "创建文件的路径:" + mFile);
        LogUtil.d(TAG, "文件创建成功:" + file.exists());
        mRecorder.setOutputFile(mFile);
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            LogUtil.d(TAG, "prepare start异常,重新开始录音:" + e.toString());
            mRecorder.release();
            mRecorder = null;
            startRecording();
        }
    }

    private void stopRecording() {
        if (mThread != null) {
            mThread.exit();
            mThread = null;
        }
        if (mRecorder != null) {
            mRecorder.stop(); // 停止时没有prepare，就会报stop failed
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
        }
    }

    /**
     * 通过音量大小调节图标
     *
     * @author 来自网络
     * @since 2019-11-12
     */
    private class ObtainDecibelThread extends Thread {
        private volatile boolean running = true;

        public void exit() {
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                if (mRecorder == null || !running) {
                    break;
                }
                // 振幅
                int db = mRecorder.getMaxAmplitude() / 600;
                if (db != 0 && dy >= 0) {
                    int f = (int) (db / 5);
                    if (f == 0)
                        volumeHandler.sendEmptyMessage(0);
                    else if (f == 1)
                        volumeHandler.sendEmptyMessage(1);
                    else if (f == 2)
                        volumeHandler.sendEmptyMessage(2);
                    else if (f == 3)
                        volumeHandler.sendEmptyMessage(3);
                    else if (f == 4)
                        volumeHandler.sendEmptyMessage(4);
                    else if (f == 5)
                        volumeHandler.sendEmptyMessage(5);
                    else if (f == 6)
                        volumeHandler.sendEmptyMessage(6);
                    else
                        volumeHandler.sendEmptyMessage(7);
                }

                volumeHandler.sendEmptyMessage(-1);
                if (System.currentTimeMillis() - startTime > 20000) {
                    finishRecord();
                }

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    LogUtil.i(TAG, "InterruptedException");
                }
            }
        }
    }

    /**
     * 完成录音回调接口
     *
     * @author 来自网络
     * @since 2019-11-12
     */
    public interface OnFinishedRecordListener {
        /**
         * 回调函数
         *
         * @param audioPath String
         * @param time int
         */
        void onFinishedRecord(String audioPath, int time);
    }
}
