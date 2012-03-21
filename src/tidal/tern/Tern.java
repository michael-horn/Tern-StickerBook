/*
 * @(#) Tern.java
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

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;
import android.graphics.Bitmap;
import android.content.Intent;
import android.app.Activity;
import android.util.Log;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;

import java.io.File;
import com.lego.minddroid.DeviceListActivity;


public class Tern extends Activity {
   
   public static final String TAG = "Tern";
   
   public static final int CONNECT_DEVICE  = 1000;
   public static final int CAMERA_REQUEST  = 2000;
   public static final int GALLERY_REQUEST = 3000;
   
   
   /** Main view for the app */
   protected ProgramView view;
   
   /** Storage file for captured bitmaps */
   protected File temp;
   


//----------------------------------------------------------------   
// onCreate
//----------------------------------------------------------------   
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      // Hide the window title
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
      
      // Set layout 
      setContentView(R.layout.main);

      // Initialize view
      this.view = (ProgramView)findViewById(R.id.ProgramView);
      this.view.init(getApplicationContext(), this);

      // Storage for captured bitmaps      
      this.temp = new File(
         Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES),
         "capture.jpg");
      
   }
   
   
//----------------------------------------------------------------   
// onStart
//----------------------------------------------------------------   
   protected void onStart() {
      super.onStart();
   }
   
   
//----------------------------------------------------------------   
// onResume
//----------------------------------------------------------------   
   protected void onResume() {
      super.onResume();
   }
   
   
//----------------------------------------------------------------   
// onPause
//----------------------------------------------------------------   
   protected void onPause() {
      super.onPause();
   }
   
   
//----------------------------------------------------------------   
// onDestroy
//----------------------------------------------------------------   
   protected void onDestroy() {
      super.onDestroy();
      this.view.destroy();
   }
   
   
   protected void captureBitmap() {
      Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temp));
      startActivityForResult(intent, CAMERA_REQUEST);
   }
   
   
   protected void selectBitmap() {
      Intent intent = new Intent();
      intent.setType("image/*");
      intent.setAction(Intent.ACTION_GET_CONTENT);
      startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            GALLERY_REQUEST);
   }
   
   
   public void selectBluetoothDevice() {
      Intent serverIntent = new Intent(this, DeviceListActivity.class);
      startActivityForResult(serverIntent, CONNECT_DEVICE);
   }
   
   
    
//----------------------------------------------------------------
// onActivityResult -- Called by the ImageCapture intent
//----------------------------------------------------------------
   protected void onActivityResult(int request, int result, Intent data) {
      Bitmap bitmap = null;
      
      switch (request) {
         case CAMERA_REQUEST:
            if (result == RESULT_OK) {
               try {
                  bitmap = Media.getBitmap(getContentResolver(), Uri.fromFile(temp) );
                  view.loadBitmap(bitmap);
               } catch (Exception x) {
                  Log.e(TAG, "Image load error", x);
               }
            }
            break;
         
         case GALLERY_REQUEST:
            if (result == RESULT_OK) {
               try {
                  bitmap = Media.getBitmap(getContentResolver(), data.getData());
                  view.loadBitmap(bitmap);
               } catch (Exception x) {
                  Log.e(TAG, "Image load error", x);
               }
            }
            break;

         case CONNECT_DEVICE:
            if (result == RESULT_OK) {
               String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
               view.setBluetoothDevice(address);
            }
            break;
      }
   }
}
