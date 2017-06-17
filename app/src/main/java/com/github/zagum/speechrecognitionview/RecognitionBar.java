package com.github.zagum.speechrecognitionview;

import android.graphics.RectF;

public class RecognitionBar {
    private int height;
    private final int maxHeight;
    private int radius;
    private final RectF rect;
    private final int startX;
    private final int startY;
    private int x;
    private int y;

    public RecognitionBar(int x, int y, int height, int maxHeight, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.startX = x;
        this.startY = y;
        this.height = height;
        this.maxHeight = maxHeight;
        this.rect = new RectF((float) (x - radius), (float) (y - (height / 2)), (float) (x + radius), (float) ((height / 2) + y));
    }

    public void update() {
        this.rect.set((float) (this.x - this.radius), (float) (this.y - (this.height / 2)), (float) (this.x + this.radius), (float) (this.y + (this.height / 2)));
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMaxHeight() {
        return this.maxHeight;
    }

    public int getStartX() {
        return this.startX;
    }

    public int getStartY() {
        return this.startY;
    }

    public RectF getRect() {
        return this.rect;
    }

    public int getRadius() {
        return this.radius;
    }
}
