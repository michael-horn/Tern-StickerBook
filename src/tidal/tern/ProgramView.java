/*
 * @(#) ProgramView.java
 * 
 * Copyright (c) 2011 Michael S. Horn
 * 
 *           Michael S. Horn (michael.horn@tufts.edu)
 *           Northwestern University 
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (version 2) as
 * published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package tidal.tern;

import android.widget.ImageView;
import android.view.View;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Bitmap;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.content.res.Resources;
import android.view.MotionEvent;
import android.util.Log;
import android.util.AttributeSet;
import android.os.Handler;
import android.os.Message;



public class ProgramView extends View {
   
   protected Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
   protected Bitmap bitmap = null;
   protected Tern tern = null;

   
   public ProgramView(Context context) {
      super(context);
   }
      
      
   public ProgramView(Context context, AttributeSet attribs) {
      super(context, attribs);
   }
   
   
/**
 * Thread-safe invalidate function
 */
   public void repaint() {
      repaintHandler.sendEmptyMessage(0);
   }
   
   
   private Handler repaintHandler = new Handler() {
      @Override
      public void handleMessage(Message msg) {
         invalidate();
      }
   };
   
   
   public void setTern(Tern tern) {
      this.tern = tern;
   }
   
   
   public boolean onTouchEvent(MotionEvent event) {
      paint.setColor(Color.WHITE);
      int action = event.getAction();
      paint.setColor(Color.RED);
      if (action == MotionEvent.ACTION_DOWN) {
         paint.setColor(Color.BLUE);
      } else if (action == MotionEvent.ACTION_UP) {
         paint.setColor(Color.WHITE);
         
         int w = getWidth();
         int h = getHeight();
         
         if (tern != null) {
            if (event.getX() > w/2 && event.getY() > h/2) {
               //tern.selectNXT();
               tern.onClick(this);
            } else if (event.getY() > h/2) {
               tern.onClick(this);
            }
         }
      }
      invalidate();
      return true;
   }
   
   
   public void setBitmap(Bitmap bitmap) {
      this.bitmap = bitmap;
   }

   
   protected void onDraw(Canvas canvas) {
      int w = getWidth();
      int h = getHeight();
      int dw, dh;
      float ds;
      
      Resources res = getResources();
      Drawable logo = res.getDrawable(R.drawable.logo);
      Drawable button = res.getDrawable(R.drawable.play_button_up);

      // clear background      
      canvas.drawRGB(210, 210, 210);
      
      // draw logo
      dw = logo.getIntrinsicWidth();
      dh = logo.getIntrinsicHeight();
      ds = Math.min(0.8f, 0.8f * w / dw);
      dw *= ds;
      dh *= ds;
      logo.setBounds(w/2 - dw/2, 70, w/2 + dw/2, 70 + dh);
      logo.draw(canvas);
      
      //canvas.drawCircle(50, 50, 15, paint);

      // draw bitmap
      if (this.bitmap != null) {
         if (tern.getProgram() != null) {
            Matrix m = focus(tern.getProgram().getBounds());
            canvas.drawBitmap(bitmap, m, null);
         } else {
            dw = bitmap.getWidth();
            dh = bitmap.getHeight();
            ds = 1.4f * w / dw;
            dw *= ds;
            dh *= ds;
            RectF dest = new RectF(w/2 - dw/2, h/2 - dh/2, w/2 + dw/2, h/2 + dh/2);
            canvas.drawBitmap(bitmap, null, dest, null);
         }
      }
      
      // draw button
      dw = button.getIntrinsicWidth();
      dh = button.getIntrinsicHeight();
      int dx = w - dw - 10;
      int dy = h - dh - 10;
      button.setBounds(dx, dy, dx + dw, dy + dh);
      button.draw(canvas);
      
      // draw robot picture
      if (picture > 0) {
         Drawable d = res.getDrawable(picture);
         if (d != null) {
            Log.i("ProgramView", "drawing picture");
            dw = d.getIntrinsicWidth() / 3;
            dh = d.getIntrinsicHeight() / 3;
            dx = w/2 - dw/2;
            dy = h/2 - dh/2;
            d.setBounds(dx, dy, dx + dw, dy + dh);
            d.draw(canvas);
         }
      }
      
      /*
      Paint font = new Paint(Paint.ANTI_ALIAS_FLAG);
      font.setColor(Color.WHITE);
      font.setStyle(Style.FILL);
      font.setTextSize(40);
      font.setTextAlign(Paint.Align.CENTER);
      canvas.drawText(msg, w/2, h/2, font);
      */
   }
   
   
   private String msg = "";
   public void setMessage(String msg) {
      this.msg = msg;
   }
   
   private int picture = -1;
   public void setPicture(int id) {
      Resources res = getResources();
      this.picture = id;
      Log.i("ProgramView", "picture set to " + picture);
   }
   
   
	public Matrix focus(RectF bounds) {
		float pw = bounds.width();
		float ph = bounds.height();
		float px = bounds.left;
		float py = bounds.top;
		float par = (pw / ph);
		
		float sw = getWidth();
		float sh = getHeight();
		float sar = (sw / sh);
		
		float cx = sw / 2;
		float cy = sh / 2;
		float pcx = (bounds.left + bounds.right) / 2;
		float pcy = ph / 2 + py;
      
      Matrix m = new Matrix();

		float z = 1;
		
		if (par > sar) {
			z = sw / pw;
		} else {
			z = sh / ph;
		}
		
		// Limit zoom in factor so we don't get too close
		if (z > 1.5f) z = 1.5f;
      z *= 0.95f;
      
      m.preScale(z, z);
      m.preTranslate(cx-pcx, cy-pcy);
      return m;
	}
   
}