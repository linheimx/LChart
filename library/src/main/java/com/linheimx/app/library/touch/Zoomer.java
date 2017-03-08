package com.linheimx.app.library.touch;

import android.os.SystemClock;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by lijian on 2017/1/26.
 */

public class Zoomer {

    long _startTime;
    int _duration = 1 * 500;// 动画的持续时间
    Interpolator _Interpolator;//校对机

    float _endZoom;
    float _currentLevel;

    boolean _isFinish = true;


    public Zoomer() {
        _Interpolator = new LinearInterpolator();
    }

    public void startZoom(float endZoom) {
        _startTime = SystemClock.elapsedRealtime();
        _endZoom = endZoom;
        _currentLevel = 1;
        _isFinish = false;
    }

    public float getCurrentZoom() {
        return _currentLevel;
    }


    /**
     * 计算当前的缩放级别
     *
     * @return true：需要计算，false：不需要计算
     */
    public boolean computeZoom() {
        if (_isFinish) {
            return false;
        }

        long d = SystemClock.elapsedRealtime() - _startTime;
        if (d > _duration) {
            _currentLevel = _endZoom;
            _isFinish = true;
            return false;
        }

        float t = d * 1f / _duration;
        _currentLevel = 1 - (1 - _endZoom) * _Interpolator.getInterpolation(t);
        return true;
    }

    public void stop() {
        _isFinish = true;
    }


    public long get_duration() {
        return _duration;
    }

    public void set_duration(int _duration) {
        this._duration = _duration;
    }
}
