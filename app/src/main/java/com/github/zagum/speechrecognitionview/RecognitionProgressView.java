package com.github.zagum.speechrecognitionview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.AttributeSet;
import android.view.View;
import com.github.zagum.speechrecognitionview.animators.BarParamsAnimator;
import com.github.zagum.speechrecognitionview.animators.IdleAnimator;
import com.github.zagum.speechrecognitionview.animators.RmsAnimator;
import com.github.zagum.speechrecognitionview.animators.RotatingAnimator;
import com.github.zagum.speechrecognitionview.animators.TransformAnimator;
import com.github.zagum.speechrecognitionview.animators.TransformAnimator.OnInterpolationFinishedListener;
import java.util.ArrayList;
import java.util.List;

public class RecognitionProgressView extends View implements RecognitionListener {
    public static final int BARS_COUNT = 5;
    private static final int CIRCLE_RADIUS_DP = 5;
    private static final int CIRCLE_SPACING_DP = 11;
    private static final int[] DEFAULT_BARS_HEIGHT_DP = new int[]{60, 46, 70, 54, 64};
    private static final int IDLE_FLOATING_AMPLITUDE_DP = 3;
    private static final float MDPI_DENSITY = 1.5f;
    private static final int ROTATION_RADIUS_DP = 25;
    private int amplitude;
    private boolean animating;
    private BarParamsAnimator animator;
    private int barColor = -1;
    private int[] barColors;
    private int[] barMaxHeights;
    private float density;
    private boolean isSpeaking;
    private Paint paint;
    private int radius;
    private final List<RecognitionBar> recognitionBars = new ArrayList();
    private RecognitionListener recognitionListener;
    private int rotationRadius;
    private int spacing;
    private SpeechRecognizer speechRecognizer;

    public RecognitionProgressView(Context context) {
        super(context);
        init();
    }

    public RecognitionProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecognitionProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setSpeechRecognizer(SpeechRecognizer recognizer) {
        this.speechRecognizer = recognizer;
        this.speechRecognizer.setRecognitionListener(this);
    }

    public void setRecognitionListener(RecognitionListener listener) {
        this.recognitionListener = listener;
    }

    public void play() {
        startIdleInterpolation();
        this.animating = true;
    }

    public void stop() {
        if (this.animator != null) {
            this.animator.stop();
            this.animator = null;
        }
        this.animating = false;
        resetBars();
    }

    public void setSingleColor(int color) {
        this.barColor = color;
    }

    public void setColors(int[] colors) {
        if (colors != null) {
            this.barColors = new int[CIRCLE_RADIUS_DP];
            if (colors.length < CIRCLE_RADIUS_DP) {
                System.arraycopy(colors, 0, this.barColors, 0, colors.length);
                for (int i = colors.length; i < CIRCLE_RADIUS_DP; i++) {
                    this.barColors[i] = colors[0];
                }
                return;
            }
            System.arraycopy(colors, 0, this.barColors, 0, CIRCLE_RADIUS_DP);
        }
    }

    public void setBarMaxHeightsInDp(int[] heights) {
        if (heights != null) {
            this.barMaxHeights = new int[CIRCLE_RADIUS_DP];
            if (heights.length < CIRCLE_RADIUS_DP) {
                System.arraycopy(heights, 0, this.barMaxHeights, 0, heights.length);
                for (int i = heights.length; i < CIRCLE_RADIUS_DP; i++) {
                    this.barMaxHeights[i] = heights[0];
                }
                return;
            }
            System.arraycopy(heights, 0, this.barMaxHeights, 0, CIRCLE_RADIUS_DP);
        }
    }

    private void init() {
        this.paint = new Paint();
        this.paint.setFlags(1);
        this.paint.setColor(-7829368);
        this.density = getResources().getDisplayMetrics().density;
        this.radius = (int) (5.0f * this.density);
        this.spacing = (int) (11.0f * this.density);
        this.rotationRadius = (int) (25.0f * this.density);
        this.amplitude = (int) (3.0f * this.density);
        if (this.density <= MDPI_DENSITY) {
            this.amplitude *= 2;
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.recognitionBars.isEmpty()) {
            initBars();
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!this.recognitionBars.isEmpty()) {
            if (this.animating) {
                this.animator.animate();
            }
            for (int i = 0; i < this.recognitionBars.size(); i++) {
                RecognitionBar bar = (RecognitionBar) this.recognitionBars.get(i);
                if (this.barColors != null) {
                    this.paint.setColor(this.barColors[i]);
                } else if (this.barColor != -1) {
                    this.paint.setColor(this.barColor);
                }
                canvas.drawRoundRect(bar.getRect(), (float) this.radius, (float) this.radius, this.paint);
            }
            if (this.animating) {
                invalidate();
            }
        }
    }

    private void initBars() {
        List<Integer> heights = initBarHeights();
        int firstCirclePosition = ((getMeasuredWidth() / 2) - (this.spacing * 2)) - (this.radius * 4);
        for (int i = 0; i < CIRCLE_RADIUS_DP; i++) {
            this.recognitionBars.add(new RecognitionBar(firstCirclePosition + (((this.radius * 2) + this.spacing) * i), getMeasuredHeight() / 2, this.radius * 2, ((Integer) heights.get(i)).intValue(), this.radius));
        }
    }

    private List<Integer> initBarHeights() {
        List<Integer> barHeights = new ArrayList();
        int i;
        if (this.barMaxHeights == null) {
            for (i = 0; i < CIRCLE_RADIUS_DP; i++) {
                barHeights.add(Integer.valueOf((int) (((float) DEFAULT_BARS_HEIGHT_DP[i]) * this.density)));
            }
        } else {
            for (i = 0; i < CIRCLE_RADIUS_DP; i++) {
                barHeights.add(Integer.valueOf((int) (((float) this.barMaxHeights[i]) * this.density)));
            }
        }
        return barHeights;
    }

    private void resetBars() {
        for (RecognitionBar bar : this.recognitionBars) {
            bar.setX(bar.getStartX());
            bar.setY(bar.getStartY());
            bar.setHeight(this.radius * 2);
            bar.update();
        }
    }

    private void startIdleInterpolation() {
        this.animator = new IdleAnimator(this.recognitionBars, this.amplitude);
        this.animator.start();
    }

    private void startRmsInterpolation() {
        resetBars();
        this.animator = new RmsAnimator(this.recognitionBars);
        this.animator.start();
    }

    private void startTransformInterpolation() {
        resetBars();
        this.animator = new TransformAnimator(this.recognitionBars, getWidth() / 2, getHeight() / 2, this.rotationRadius);
        this.animator.start();
        ((TransformAnimator) this.animator).setOnInterpolationFinishedListener(new OnInterpolationFinishedListener() {
            public void onFinished() {
                RecognitionProgressView.this.startRotateInterpolation();
            }
        });
    }

    private void startRotateInterpolation() {
        this.animator = new RotatingAnimator(this.recognitionBars, getWidth() / 2, getHeight() / 2);
        this.animator.start();
    }

    public void onReadyForSpeech(Bundle params) {
        if (this.recognitionListener != null) {
            this.recognitionListener.onReadyForSpeech(params);
        }
    }

    public void onBeginningOfSpeech() {
        if (this.recognitionListener != null) {
            this.recognitionListener.onBeginningOfSpeech();
        }
        this.isSpeaking = true;
    }

    public void onRmsChanged(float rmsdB) {
        if (this.recognitionListener != null) {
            this.recognitionListener.onRmsChanged(rmsdB);
        }
        if (this.animator != null && rmsdB >= 1.0f) {
            if (!(this.animator instanceof RmsAnimator) && this.isSpeaking) {
                startRmsInterpolation();
            }
            if (this.animator instanceof RmsAnimator) {
                ((RmsAnimator) this.animator).onRmsChanged(rmsdB);
            }
        }
    }

    public void onBufferReceived(byte[] buffer) {
        if (this.recognitionListener != null) {
            this.recognitionListener.onBufferReceived(buffer);
        }
    }

    public void onEndOfSpeech() {
        if (this.recognitionListener != null) {
            this.recognitionListener.onEndOfSpeech();
        }
        this.isSpeaking = false;
        startTransformInterpolation();
    }

    public void onError(int error) {
        if (this.recognitionListener != null) {
            this.recognitionListener.onError(error);
        }
    }

    public void onResults(Bundle results) {
        if (this.recognitionListener != null) {
            this.recognitionListener.onResults(results);
        }
    }

    public void onPartialResults(Bundle partialResults) {
        if (this.recognitionListener != null) {
            this.recognitionListener.onPartialResults(partialResults);
        }
    }

    public void onEvent(int eventType, Bundle params) {
        if (this.recognitionListener != null) {
            this.recognitionListener.onEvent(eventType, params);
        }
    }
}
