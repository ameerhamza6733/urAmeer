package com.github.zagum.speechrecognitionview.animators;

import com.github.zagum.speechrecognitionview.RecognitionBar;
import java.util.ArrayList;
import java.util.List;

public class RmsAnimator implements BarParamsAnimator {
    private final List<BarRmsAnimator> barAnimators = new ArrayList();

    public RmsAnimator(List<RecognitionBar> recognitionBars) {
        for (RecognitionBar bar : recognitionBars) {
            this.barAnimators.add(new BarRmsAnimator(bar));
        }
    }

    public void start() {
        for (BarRmsAnimator barAnimator : this.barAnimators) {
            barAnimator.start();
        }
    }

    public void stop() {
        for (BarRmsAnimator barAnimator : this.barAnimators) {
            barAnimator.stop();
        }
    }

    public void animate() {
        for (BarRmsAnimator barAnimator : this.barAnimators) {
            barAnimator.animate();
        }
    }

    public void onRmsChanged(float rmsDB) {
        for (BarRmsAnimator barAnimator : this.barAnimators) {
            barAnimator.onRmsChanged(rmsDB);
        }
    }
}
