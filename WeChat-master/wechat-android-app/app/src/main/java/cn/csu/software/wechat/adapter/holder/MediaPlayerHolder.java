package cn.csu.software.wechat.adapter.holder;

import android.content.Context;
import android.media.MediaPlayer;

import cn.csu.software.wechat.adapter.MediaPlayerAdapter;

import java.io.IOException;

/**
 * MediaPlayerHolder
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-11-14
 */
public class MediaPlayerHolder implements MediaPlayerAdapter {
    private static MediaPlayer sMediaPlayer;

    private static PlaybackInfoListener sPlaybackInfoListener;

    private static MediaPlayerHolder sMediaPlayerHolder;

    private Context mContext;

    private MediaPlayerHolder(Context context) {
        mContext = context;
    }

    /**
     * 静态获取实例
     *
     * @param context 上下文
     * @return MediaPlayerHolder
     */
    public static MediaPlayerHolder getMediaPlayerHolder(Context context) {
        if (sMediaPlayerHolder == null) {
            return new MediaPlayerHolder(context);
        }
        return sMediaPlayerHolder;
    }

    private void initializeMediaPlayer() {
        if (sMediaPlayer == null) {
            sMediaPlayer = new MediaPlayer();
            sMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (sPlaybackInfoListener != null) {
                        sPlaybackInfoListener.onPlaybackCompleted();
                    }
                }
            });
        }
    }

    /**
     * 设置PlaybackInfoListener
     *
     * @param playbackInfoListener PlaybackInfoListener
     */
    public void setPlaybackInfoListener(PlaybackInfoListener playbackInfoListener) {
        if (sPlaybackInfoListener == null) {
            sPlaybackInfoListener = playbackInfoListener;
        }
    }

    @Override
    public void loadMedia(String path) throws IOException {
        initializeMediaPlayer();
        sMediaPlayer.setDataSource(path);
        sMediaPlayer.prepare();
    }

    @Override
    public void release() {
        if (sMediaPlayer != null) {
            sMediaPlayer.release();
            sMediaPlayer = null;
        }
    }

    @Override
    public boolean isPlaying() {
        if (sMediaPlayer != null) {
            return sMediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public void play() {
        if (sMediaPlayer != null && !sMediaPlayer.isPlaying()) {
            sMediaPlayer.start();
        }
    }

    @Override
    public void reset() {
        if (sMediaPlayer != null) {
            if (isPlaying()) {
                sPlaybackInfoListener.onPlaybackInterrupt();
            }
            sMediaPlayer.reset();
        }
    }

    @Override
    public void pause() {
        if (sMediaPlayer != null && sMediaPlayer.isPlaying()) {
            sMediaPlayer.pause();
        }
    }

    /**
     * 功能描述
     *
     * @author huangjishun 874904407@qq.com
     * @since 2019-11-14
     */
    public interface PlaybackInfoListener {
        /**
         * 播放完成回调函数
         */
        void onPlaybackCompleted();

        /**
         * 播放中止回调函数
         */
        void onPlaybackInterrupt();
    }
}
