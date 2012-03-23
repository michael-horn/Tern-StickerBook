/*
 * @(#) NXTRobot.java
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

import android.util.Log;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.lego.minddroid.BTConnectable;
import com.lego.minddroid.BTCommunicator;

import tidal.tern.rt.Robot;

/**
 * NXT implementation of Robot
 */
public class NXTRobot implements Robot, BTConnectable {
   
   public static final String TAG = "NXT Robot";
   
   protected ProgramView view;
   
   protected boolean connected = false;
   
   protected boolean pairing = false;
   
   protected String address = "00:16:53:10:AC:54";
   
   protected BTCommunicator nxt = null;
   
   private Handler btcHandler;
   
   
   
   public NXTRobot(ProgramView view) {
      this.view = view;
   }
   
   
   public void setAddress(String address) {
      this.address = address;
   }
   
   
   public void openConnection() {
      if (address == null) return;
      this.connected = false;
      //this.view.showProgressDialog("Connecting...");
      if (this.nxt != null) {
         try {
            this.nxt.destroyNXTconnection();
         } catch (java.io.IOException iox) { }
      }
      this.nxt = new BTCommunicator(this, myHandler, BluetoothAdapter.getDefaultAdapter(), view.getResources());
      btcHandler = this.nxt.getHandler();
      this.nxt.setMACAddress(address);
      this.nxt.start();
   }
   
   
   public void closeConnection() {
      if (this.nxt != null) {
         sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.DISCONNECT, 0, 0);
      }
      this.nxt = null;
      this.connected = false;
   }
   
   
   public boolean isConnected() {
      return this.connected;
   }
   
   
   public boolean isPairing() {
      return pairing;
   }
   
   
   public int getIcon() {
      return isConnected() ? R.drawable.nxt : R.drawable.nxt_fade;
   }
   
   
   public void allStop() {
      sendBTCmessage(BTCommunicator.NO_DELAY, 0, 0, 0);
      sendBTCmessage(BTCommunicator.NO_DELAY, 0, 0, 0);
      sendBTCmessage(BTCommunicator.NO_DELAY, 0, 0, 0);
   }
   
   
   public int doStartMotor(int [] args) {
      int motor = args[0];
      int power = args[1];
      sendBTCmessage(BTCommunicator.NO_DELAY, motor, power, 0);
      return 0;
   }
   
   
   public int doStopMotor(int [] args) {
      int motor = args[0];
      sendBTCmessage(BTCommunicator.NO_DELAY, motor, 0, 0);
      return 0;
   }
   
   
   public int doBeep(int [] args) {
      sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.DO_BEEP, 494, 100);
      sendBTCmessage(200, BTCommunicator.DO_BEEP, 440, 100);
      return 0;
   }
   
   
   public int doSing(int [] args) {
      sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.DO_BEEP, 392, 100);
      sendBTCmessage(200, BTCommunicator.DO_BEEP, 440, 100);
      sendBTCmessage(400, BTCommunicator.DO_BEEP, 494, 100);
      sendBTCmessage(600, BTCommunicator.DO_BEEP, 523, 100);
      sendBTCmessage(800, BTCommunicator.DO_BEEP, 587, 300);
      sendBTCmessage(1200, BTCommunicator.DO_BEEP, 523, 300);
      sendBTCmessage(1600, BTCommunicator.DO_BEEP, 494, 300);
      return 0;
   }
   
   
   public int doGrowl(int [] args) {
      return 0;
   }

   
   /**
    * Sends the message via the BTCommuncator to the robot.
    * @param delay time to wait before sending the message.
    * @param message the message type (as defined in BTCommucator)
    * @param value1 first parameter
    * @param value2 second parameter
    */   
   void sendBTCmessage(int delay, int message, int value1, int value2) {
      if (!connected) return;
      Bundle myBundle = new Bundle();
      myBundle.putInt("message", message);
      myBundle.putInt("value1", value1);
      myBundle.putInt("value2", value2);
      Message myMessage = myHandler.obtainMessage();
      myMessage.setData(myBundle);

      if (delay == 0)
         btcHandler.sendMessage(myMessage);
      else
         btcHandler.sendMessageDelayed(myMessage, delay);
   }


   /**
    * Sends the message via the BTCommuncator to the robot.
    * @param delay time to wait before sending the message.
    * @param message the message type (as defined in BTCommucator)
    * @param String a String parameter
    */       
   void sendBTCmessage(int delay, int message, String name) {
      if (!connected) return;
      Bundle myBundle = new Bundle();
      myBundle.putInt("message", message);
      myBundle.putString("name", name);
      Message myMessage = myHandler.obtainMessage();
      myMessage.setData(myBundle);
  
      if (delay == 0)
         btcHandler.sendMessage(myMessage);
      else
         btcHandler.sendMessageDelayed(myMessage, delay);
   }

   
   final Handler myHandler = new Handler() {
      public void handleMessage(Message message) {
         switch (message.getData().getInt("message")) {
            
            case BTCommunicator.STATE_CONNECTED:
               connected = true;
               //view.hideProgressDialog();
               sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.GET_FIRMWARE_VERSION, 0, 0);
               Log.i(TAG, "Connected to NXT!");
               view.repaint();
               break;
            
            case BTCommunicator.MOTOR_STATE:
               break;
            
            case BTCommunicator.STATE_CONNECTERROR_PAIRING:
               //view.hideProgressDialog();
               closeConnection();
               break;
            
            case BTCommunicator.STATE_CONNECTERROR:
            case BTCommunicator.STATE_RECEIVEERROR:
            case BTCommunicator.STATE_SENDERROR:
               //view.hideProgressDialog();
               closeConnection();
               Log.e(TAG, "Connection error " + message.getData());
               break;
         }
      }
   };
}