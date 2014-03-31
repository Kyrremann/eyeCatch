package no.minimon.eyecatch.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class SemiCircle {

	private Path circle;
	private Paint color;
	private float px, py, radio, angleI, width, offset;
	private int r, g, b;
	private int resolution;
	private float pointX[], pointY[];

	public SemiCircle(int r, int g, int b, int resolution) {
		this.offset = 0;
		this.r = r;
		this.g = g;
		this.b = b;
		this.color = new Paint();
		this.color.setColor(Color.rgb(this.r, this.g, this.b));
		this.color.setAntiAlias(true);
		this.circle = new Path();
		this.resolution = resolution;
		this.pointX = new float[this.resolution];
		this.pointY = new float[this.resolution];
		this.angleI = 0;
		this.width = 1;

	}

	public void SetOffset(float off) {
		this.offset = off;
	}

	public void SetColor(int r1, int g1, int b1) {
		this.r = r1;
		this.g = g1;
		this.b = b1;
		this.color.setColor(Color.rgb(r, g, b));
	}

	public void calculatePercentagePoints(float px, float py, float percentage,
			float radio) {
		this.angleI = 0 + this.offset;
		this.px = px;
		this.py = py;
		this.radio = radio;
		this.angleI = 0;
		this.width = percentage / 100 * 360;

		this.calculatePoints(this.px, this.py, angleI, width, this.radio);
	}

	public void calculatePoints(float px1, float py1, float angleI,
			float width, float radio) {
		this.angleI = angleI + this.offset;
		this.width = width;
		this.px = px1;
		this.py = py1;
		this.radio = radio;

		float angle = 360 - this.angleI - this.width;

		for (int i = 0; i < resolution; i++) {
			this.pointX[i] = this.px - (float) Math.sin(Math.toRadians(angle))
					* this.radio;
			this.pointY[i] = this.py - (float) Math.cos(Math.toRadians(angle))
					* this.radio;
			angle = (360 - this.angleI - this.width)
					+ ((this.width / (float) (this.resolution)) * (i + 2));
		}

		this.circle.reset();

		this.circle.moveTo(this.px, this.py);
		for (int i = 0; i < resolution; i++) {
			this.circle.lineTo(this.pointX[i], this.pointY[i]);
		}

	}

	public void drawSemiCircle(Canvas canvas) {
		canvas.drawPath(this.circle, this.color);

	}
}