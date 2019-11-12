/*
 * Copyright (c) 2019-2019 cn.csu.software. All rights reserved.
 */

package cn.csu.software.wechat.adapter;

import java.io.IOException;

/**
 * MediaPlayer adapter 接口
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-19
 */
public interface MediaPlayerAdapter {
    void loadMedia(String path) throws IOException;

    void release();

    boolean isPlaying();

    void play();

    void reset();

    void pause();
}
