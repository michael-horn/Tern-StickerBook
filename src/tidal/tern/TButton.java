/*
 * @(#) TButton.java
 * 
 * Tern Tangible Programming Language
 * Copyright (c) 2011 Michael S. Horn
 * 
 *           Michael S. Horn (michael.horn@tufts.edu)
 *           Northwestern University
 *           2120 Campus Drive
 *           Evanston, IL 60613
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

import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.content.res.Resources;
import android.view.MotionEvent;
import android.os.Handler;



/**
 * Lightweight button implementation
 */
public class TButton {

   protected Drawable btn_up;
   protected Drawable btn_dn;
   protected Drawable btn_da;
   protected boolean down;
   protected int width;
   protected int height;
   protected int x;
   protected int y;
   
   /** Touch id currently captured by this button */
   protected int touch_id;
   protected boolean over;
   protected boolean enabled;
   protected Handler handler;

   
   public TButton(Resources res, int up, int dn, int da, Handler handler) {
      this.btn_up = res.getDrawable(up);
      this.btn_dn = res.getDrawable(dn);
      this.btn_da = res.getDrawable(da);
      this.down = false;
      this.enabled = true;
      this.width = btn_up.getIntrinsicWidth();
      this.height = btn_up.getIntrinsicHeight();
      this.x = 0;
      this.y = 0;
      this.touch_id = -1;
      this.over = false;
      this.handler = handler;
   }
   
   
   public void setDownImage(Resources res, int dn) {
      this.btn_dn = res.getDrawable(dn);
   }
   
   
   public void setUpImage(Resources res, int up) {
      this.btn_up = res.getDrawable(up);
   }
   
   
   public void setDisabledImage(Resources res, int da) {
      this.btn_da = res.getDrawable(da);
   }
   
   
   public void setCenter(int centerX, int centerY) {
      this.x = centerX - width/2;
      this.y = centerY - height/2;
   }
   
   
   public void setLocation(int x, int y) {
      this.x = x;
      this.y = y;
   }
   
   
   public int getWidth() {
      return this.width;
   }
   
   
   public int getHeight() {
      return this.height;
   }
   
   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }
   
   
   public void enable() {
      this.enabled = true;
   }
   
   
   public void disable() {
      this.enabled = false;
   }
   
   
   public boolean contains(float touchX, float touchY) {
      return (touchX >= x &&
              touchY >= y &&
              touchX <= x + width &&
              touchY <= y + height);
   }

      
   public void draw(Canvas canvas) {
      btn_up.setBounds(x, y, x + width, y + height);
      btn_dn.setBounds(x, y, x + width, y + height);
      btn_da.setBounds(x, y, x + width, y + height);
      
      if (!enabled) {
         btn_da.draw(canvas);
      }
      else if (touch_id >= 0 && over) {
         btn_dn.draw(canvas);
      } else {
         btn_up.draw(canvas);
      }
   }
   
   
   public void action() {
      this.handler.sendEmptyMessage(0);
   }
   
   
   public boolean touchEvent(MotionEvent event) {
      if (!enabled) return false;
      int action = event.getAction();
      int id = event.getPointerId(0);
      float x, y;
      
      switch (action & MotionEvent.ACTION_MASK) {
         
         case MotionEvent.ACTION_DOWN:
            if (contains(event.getX(), event.getY())) {
               touch_id = id;
               over = true;
               return true;
            }
            break;
         
         case MotionEvent.ACTION_UP:
            if (touch_id >= 0) {
               touch_id = -1;
               if (over) action();
               over = false;
               return true;
            }
            break;
         
         case MotionEvent.ACTION_MOVE:
            if (touch_id >= 0) {
               int index = event.findPointerIndex(touch_id);
               over = contains(event.getX(index), event.getY(index));
               return true;
            }
            break;
         
         case MotionEvent.ACTION_CANCEL:
            over = false;
            if (touch_id >= 0) {
               touch_id = -1;
               return true;
            }
            break;
      }
      return false;
   }
}