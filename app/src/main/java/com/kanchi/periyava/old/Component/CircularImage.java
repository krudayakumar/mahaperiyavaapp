package com.kanchi.periyava.old.Component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by m84098 on 11/15/15.
 */
public class CircularImage extends ImageView {

  public CircularImage(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
    final Drawable d = this.getDrawable();

    if (d != null) {
      // ceil not round - avoid thin vertical gaps along the left/right edges
      final int width = MeasureSpec.getSize(widthMeasureSpec);
      final int height = (int) Math.ceil(width * (float) d.getIntrinsicHeight() / d.getIntrinsicWidth());
      this.setMeasuredDimension(width, height);
    } else {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
  }

  public String TAG = "CircularImage";
  Context mContext;
  Bitmap bitmap;
  int resid;

  /*public CircularImage(  Context mContext, int resid) {
    this.mContext=mContext;
    this.resid= resid;
  }*/

/*

  public   int getWidth() {
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
    Log.d(TAG,"Width::"+width);
    return width+300;
  }

  public   int getHeight() {
    int height = 0;
    WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    if (Build.VERSION.SDK_INT > 12) {
      Point size = new Point();
      display.getSize(size);
      height = size.y;
    } else {
      height = display.getHeight();  // Deprecated
    }
    return height;
  }
*/

  public Bitmap getCircleBitmap() {
    bitmap = BitmapFactory.decodeResource(mContext.getResources(), resid);
    final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
        bitmap.getHeight(), Bitmap.Config.ARGB_8888);
    final Canvas canvas = new Canvas(output);

    final int color = Color.RED;
    final Paint paint = new Paint();
    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getWidth());
    final RectF rectF = new RectF(rect);

    paint.setAntiAlias(true);
    canvas.drawARGB(0, 0, 0, 0);
    paint.setColor(color);
    canvas.drawOval(rectF, paint);

    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    canvas.drawBitmap(bitmap, rect, rect, paint);

    bitmap.recycle();

    return output;
  }
}
