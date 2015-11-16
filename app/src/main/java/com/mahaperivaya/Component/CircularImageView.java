package com.mahaperivaya.Component;

/**
 * Created by m84098 on 9/3/15.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

public class CircularImageView extends ImageView {

  private int borderWidth = 5;
  private int viewWidth;
  private int viewHeight;
  private Bitmap image;
  private Paint paint;
  private Paint paintBorder;
  private BitmapShader shader;
  Context mContext;

  public CircularImageView(Context context) {
    super(context);
    this.mContext = context;
    setup();
  }

  public CircularImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.mContext = context;
    setup();

  }

  public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

  }

  private int getImgWidth() {
    int width = 0;
    WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    if (Build.VERSION.SDK_INT > 12) {
      Point size = new Point();
      display.getSize(size);
      width = size.x;
    } else {
      width = display.getWidth();  // Deprecated
    }
    Log.d("CircularImageView", "Width::" + width);
    return width;
  }

  private void setup() {
    // init paint
    paint = new Paint();
    paint.setAntiAlias(true);

    paintBorder = new Paint();
    setBorderColor(Color.WHITE);
    paintBorder.setAntiAlias(true);

  }

  public void setBorderWidth(int borderWidth) {
    this.borderWidth = borderWidth;
    this.invalidate();
  }

  public void setBorderColor(int borderColor) {
    if (paintBorder != null)
      paintBorder.setColor(borderColor);

    this.invalidate();
  }

  private void loadBitmap() {
    BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getDrawable();

    if (bitmapDrawable != null)
      image = bitmapDrawable.getBitmap();
  }

  @SuppressLint("DrawAllocation")
  @Override
  public void onDraw(Canvas canvas) {
    //load the bitmap
    loadBitmap();

    // init shader
    if (image != null) {
      shader = new BitmapShader(Bitmap.createScaledBitmap(image, canvas.getWidth(), canvas.getHeight(), false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
      paint.setShader(shader);
      int circleCenter = viewWidth / 2;

      // circleCenter is the x or y of the view's center
      // radius is the radius in pixels of the cirle to be drawn
      // paint contains the shader that will texture the shape
      canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter + borderWidth, paintBorder);
      canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter, paint);
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int width = measureWidth(widthMeasureSpec);
    int height = measureHeight(heightMeasureSpec, widthMeasureSpec);

      /*  viewWidth = getImgWidth() - (borderWidth *2);
        viewHeight = getImgWidth() - (borderWidth*2);
*/

    viewWidth = width - (borderWidth * 2);
    viewHeight = height - (borderWidth * 2);
    setMeasuredDimension(width, height);

  }

  private int measureWidth(int measureSpec) {
    int result = 0;
    int specMode = MeasureSpec.getMode(measureSpec);
    int specSize = MeasureSpec.getSize(measureSpec);

    if (specMode == MeasureSpec.EXACTLY) {
      // We were told how big to be
      result = specSize;
    } else {
      // Measure the text
      result = viewWidth;

    }

    return result;
  }

  private int measureHeight(int measureSpecHeight, int measureSpecWidth) {
    int result = 0;
    int specMode = MeasureSpec.getMode(measureSpecHeight);
    int specSize = MeasureSpec.getSize(measureSpecHeight);

    if (specMode == MeasureSpec.EXACTLY) {
      // We were told how big to be
      result = specSize;
    } else {
      // Measure the text (beware: ascent is a negative number)
      result = viewHeight;
    }
    return result;
  }

  public static float dipToPixels(Context context, float dipValue) {
    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
  }
}
