package com.fimtrus.rule;

import android.graphics.Canvas;
import android.view.SurfaceHolder;


public class RuleThread extends Thread {

	private SurfaceHolder sh;
	private RuleSurfaceView view;
	private boolean run;
	public RuleThread(SurfaceHolder sh, RuleSurfaceView view) {
		this.sh = sh;
		this.view = view;
		run = false;
	}
	public void setRunning(boolean run) {
		this.run = run;
	}

	public void run() {
		Canvas canvas;
		while(run) {
			canvas = null;
			try {
				canvas = sh.lockCanvas(null);
				synchronized(sh) {
					view.onDraw(canvas);
				}
			} finally {
				if(canvas != null)
					sh.unlockCanvasAndPost(canvas);
			}
		}
	}
}

