package com.zhongan.demo.hxin.lib;

final class OnItemSelectedRunnable implements Runnable {
    final WheelView loopView;

    OnItemSelectedRunnable(WheelView loopview) {
        loopView = loopview;
    }

    @Override
    public final void run() {
        loopView.OnItemSelectedListener.onItemSelected(loopView.getCurrentItem());
    }
}
