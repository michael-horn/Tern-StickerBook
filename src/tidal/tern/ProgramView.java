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
import android.graphics.Bitmap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.content.res.Resources;
import android.view.MotionEvent;
import android.util.AttributeSet;


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
         dw = bitmap.getWidth();
         dh = bitmap.getHeight();
         ds = 1.4f * w / dw;
         dw *= ds;
         dh *= ds;
         RectF bounds = new RectF(w/2 - dw/2, h/2 - dh/2, w/2 + dw/2, h/2 + dh/2);
         canvas.drawBitmap(bitmap, null, bounds, null);
      }
      
      // draw button
      dw = button.getIntrinsicWidth();
      dh = button.getIntrinsicHeight();
      int dx = w - dw - 10;
      int dy = h - dh - 10;
      button.setBounds(dx, dy, dx + dw, dy + dh);
      button.draw(canvas);
      
      // draw program status
      Paint font = new Paint(Paint.ANTI_ALIAS_FLAG);
      font.setColor(Color.WHITE);
      font.setStyle(Style.FILL);
      font.setTextSize(40);
      font.setTextAlign(Paint.Align.CENTER);
      canvas.drawText(tern.getProgramStatus(), 15, h/2, font);
   }
}