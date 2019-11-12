package cn.csu.software.wechat.adapter.holder;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

import cn.csu.software.wechat.adapter.MediaPlayerAdapter;
import cn.csu.software.wechat.util.LogUtil;

/**
 * 功能描述
 *
 * @author
 * @since
 */
public class MediaPlayerHolder implements MediaPlayerAdapter {
    private Context mContext;

    private static MediaPlayer sMediaPlayer;

    private static PlaybackInfoListener sPlaybackInfoListener;

    private static MediaPlayerHolder sMediaPlayerHolder;

    public static MediaPlayerHolder getMediaPlayerHolder(Context context) {
        if (sMediaPlayerHolder == null) {
            return new MediaPlayerHolder(context);
        }
        return sMediaPlayerHolder;
    }

    private MediaPlayerHolder(Context context) {
        mContext = context;
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

    public interface PlaybackInfoListener {
        void onPlaybackCompleted();

        void onPlaybackInterrupt();
    }
}
