import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.voidplus.leapmotion.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Leap_motion_test extends PApplet {

 //<>// //<>// //<>//



LeapMotion leap;

public void setup() {
        
        background(255);
        // ...

        leap = new LeapMotion(this);
        rain();
}

final int PLAY_MODE_HAND = 1;
final int PLAY_MODE_MOUSE = 2;
int play_mode = PLAY_MODE_MOUSE;



float defaultSize = 10;
rect[] rainDrops = {
        new rect()
};
int score = 0;
public void displayScore(){
        String scoreString = "Score: " + score;
        textSize(20);
        textAlign(RIGHT,TOP);
        text(scoreString,width,0);
        fill(0);
}
public void pause(){
        textSize( 60);
        textAlign(CENTER,CENTER);
        rectMode(CENTER);
        text("Place your hand in the tracking area to play", width/2, height/2, 600, 1000);
}

//todo
public void rain() {
        float acceleration = 9.81f/frameRate;
        for (rect i : rainDrops) {
                if (i.cords == null) {
                        i.cords = new PVector(random(0, width), random(-200, -10));
                        i.acceleration = 0;
                        print("null");
                } else {
                        if (i.cords.y > height) {
                                i.cords.set(random(0, 800), random(-300, -10), 0);
                                i.acceleration = 0;
                        }
                }


                if (play_mode == PLAY_MODE_HAND) {
                        //if there is a hand and the pointer in the playspace
                        if (handCount > 0 && pointer.cords.x > 0 && pointer.cords.x < width && pointer.cords.y > 0 && pointer.cords.y < height) {
                                i.acceleration += acceleration;
                                i.cords.y += i.acceleration;
                        } else {
                                pause();
                        }
                }
                if (play_mode == PLAY_MODE_MOUSE) {
                    
                        i.acceleration += acceleration;
                        i.cords.y += i.acceleration;


                }
        }
}
public void drawRain() {
        for (rect i : rainDrops) {
                if (i.collision(pointer)) {
                        println(i.cords);
                        i.cords.set(random(0, width), random(-300, -10), 0);
                        i.acceleration = 0;
                        score += 1;
                };
                i.draw();
        }
}
ellipse pointer = new ellipse(new PVector(55, 55));

public void drawFingertip(PVector fingerTip) {
        pointer.cords = fingerTip;
        pointer.draw();
}

boolean play;
int handCount = 0;
public void draw() {
        background(255);
        leapstuff();

        if (play_mode == PLAY_MODE_MOUSE) {
                pointer.cords = new PVector(pmouseX, pmouseY);
        }

        rain();
        drawRain();
        displayScore();
        pointer.draw();
}
// ======================================================
// 1. Callbacks
// ======================================================
// Table of Contents:
// ├─ 1. Callbacks
// ├─ 2. Hand
// ├─ 3. Arms
// ├─ 4. Fingers
// ├─ 5. Bones
// ├─ 6. Tools
// └─ 7. Devices
// ======================================================


public void leapOnInit() {
        // println("Leap Motion Init");
}
public void leapOnConnect() {
        // println("Leap Motion Connect");
}
public void leapOnFrame() {
        // println("Leap Motion Frame");
}
public void leapOnDisconnect() {
        // println("Leap Motion Disconnect");
}
public void leapOnExit() {
        // println("Leap Motion Exit");
}

// ...
public void leapstuff(){

        int fps = leap.getFrameRate();
        handCount = 0;
        for (Hand hand : leap.getHands ()) {


                // ==================================================
                // 2. Hand

                int handId             = hand.getId();
                PVector handPosition       = hand.getPosition();
                PVector handStabilized     = hand.getStabilizedPosition();
                PVector handDirection      = hand.getDirection();
                PVector handDynamics       = hand.getDynamics();
                float handRoll           = hand.getRoll();
                float handPitch          = hand.getPitch();
                float handYaw            = hand.getYaw();
                boolean handIsLeft         = hand.isLeft();
                boolean handIsRight        = hand.isRight();
                float handGrab           = hand.getGrabStrength();
                float handPinch          = hand.getPinchStrength();
                float handTime           = hand.getTimeVisible();
                PVector spherePosition     = hand.getSpherePosition();
                float sphereRadius       = hand.getSphereRadius();

                // --------------------------------------------------
                // Drawing
                hand.draw();


                // ==================================================
                // 3. Arm

                if (hand.hasArm()) {
                        Arm arm              = hand.getArm();
                        float armWidth         = arm.getWidth();
                        PVector armWristPos      = arm.getWristPosition();
                        PVector armElbowPos      = arm.getElbowPosition();
                }


                // ==================================================
                // 4. Finger

                Finger fingerThumb        = hand.getThumb();
                // or                        hand.getFinger("thumb");
                // or                        hand.getFinger(0);


                Finger fingerIndex        = hand.getIndexFinger();
                // or                        hand.getFinger("index");
                // or                        hand.getFinger(1);

                handCount += 1;


                Finger fingerMiddle       = hand.getMiddleFinger();
                // or                        hand.getFinger("middle");
                // or                        hand.getFinger(2);

                Finger fingerRing         = hand.getRingFinger();
                // or                        hand.getFinger("ring");
                // or                        hand.getFinger(3);

                Finger fingerPink         = hand.getPinkyFinger();
                // or                        hand.getFinger("pinky");
                // or                        hand.getFinger(4);

                if (play_mode == PLAY_MODE_HAND) {
                        pointer.cords = fingerIndex.getPositionOfJointTip();
                        pointer.cords.z = 0;
                }

                //pointer.draw();


                for (Finger finger : hand.getFingers()) {
                        // or              hand.getOutstretchedFingers();
                        // or              hand.getOutstretchedFingersByAngle();

                        int fingerId         = finger.getId();
                        PVector fingerPosition   = finger.getPosition();
                        PVector fingerStabilized = finger.getStabilizedPosition();
                        PVector fingerVelocity   = finger.getVelocity();
                        PVector fingerDirection  = finger.getDirection();
                        float fingerTime       = finger.getTimeVisible();

                        // ------------------------------------------------
                        // Drawing

                        // Drawing:
                        //finger.draw();  // Executes drawBones() and drawJoints()
                        //finger.drawBones();
                        //finger.drawJoints();

                        // ------------------------------------------------
                        // Selection

                        switch(finger.getType()) {
                        case 0:
                                // System.out.println("thumb");
                                break;
                        case 1:
                                // System.out.println("index");
                                break;
                        case 2:
                                // System.out.println("middle");
                                break;
                        case 3:
                                // System.out.println("ring");
                                break;
                        case 4:
                                // System.out.println("pinky");
                                break;
                        }


                        // ================================================
                        // 5. Bones
                        // --------
                        // https://developer.leapmotion.com/documentation/java/devguide/Leap_Overview.html#Layer_1

                        Bone boneDistal       = finger.getDistalBone();
                        // or                      finger.get("distal");
                        // or                      finger.getBone(0);

                        Bone boneIntermediate = finger.getIntermediateBone();
                        // or                      finger.get("intermediate");
                        // or                      finger.getBone(1);

                        Bone boneProximal     = finger.getProximalBone();
                        // or                      finger.get("proximal");
                        // or                      finger.getBone(2);

                        Bone boneMetacarpal   = finger.getMetacarpalBone();
                        // or                      finger.get("metacarpal");
                        // or                      finger.getBone(3);

                        // ------------------------------------------------
                        // Touch emulation

                        int touchZone        = finger.getTouchZone();
                        float touchDistance    = finger.getTouchDistance();

                        switch(touchZone) {
                        case -1: // None
                                break;
                        case 0: // Hovering
                                // println("Hovering (#" + fingerId + "): " + touchDistance);
                                break;
                        case 1: // Touching
                                // println("Touching (#" + fingerId + ")");
                                break;
                        }
                }


                // ==================================================
                // 6. Tools

                for (Tool tool : hand.getTools()) {
                        int toolId           = tool.getId();
                        PVector toolPosition     = tool.getPosition();
                        PVector toolStabilized   = tool.getStabilizedPosition();
                        PVector toolVelocity     = tool.getVelocity();
                        PVector toolDirection    = tool.getDirection();
                        float toolTime         = tool.getTimeVisible();

                        // ------------------------------------------------
                        // Drawing:
                        // tool.draw();
                        //drawFingertip(fingerIndex.getPositionOfJointTip());


                        // ------------------------------------------------
                        // Touch emulation

                        int touchZone        = tool.getTouchZone();
                        float touchDistance    = tool.getTouchDistance();

                        switch(touchZone) {
                        case -1: // None
                                break;
                        case 0: // Hovering
                                // println("Hovering (#" + toolId + "): " + touchDistance);
                                break;
                        case 1: // Touching
                                // println("Touching (#" + toolId + ")");
                                break;
                        }
                }
        }


        // ====================================================
        // 7. Devices

        for (Device device : leap.getDevices()) {
                float deviceHorizontalViewAngle = device.getHorizontalViewAngle();
                float deviceVericalViewAngle = device.getVerticalViewAngle();
                float deviceRange = device.getRange();
        }
}
public boolean ellipseWithEllipse(PVector cord, PVector size, PVector otherCord, PVector otherSize) {
  if (PVector.dist(cord, otherCord) < size.x+otherSize.x) {
    println("collided");
    return true;
  }
  return false;
}

public boolean rectWithRect(PVector cord, PVector size, PVector otherCord, PVector otherSize) {
  PVector[] corners = findCorners(cord, size);
  PVector[] other_corners = findCorners(otherCord, otherSize);
  for (PVector i : other_corners) {
    if (i.x < corners[0].x && i.x > corners[3].x) {
      //println("collidedx");
      if (i.y < corners[0].y && i.y > corners[1].y) {
        println("collided");
        return true;
      }
    }
  }
  return false;
}

public boolean rectWithEllipse(PVector boxCord, PVector boxSize, PVector ellipseCord, PVector ellipseSize) { //<>//
  //define bounds of this box
  PVector[] corners = findCorners(boxCord, boxSize);
  //check if any point is inside the radius
  //remember in this case, we only have circles so far, so x or y would be the same and both would be radius
  for (PVector i : corners) {
    float distance = i.dist(ellipseCord);
    //if the point's distance from the center is smaller then the radius then there is a collision
    if (distance < ellipseSize.x/2) {
      println("collided");
      return true;
    }
  }
  return false;
}



//try maybe forgoing the findcorners
PVector[] corners;

public PVector[] findCorners(PVector cords, PVector size) {
  PVector[] corners = {
    //top right corner
    new PVector(cords.x + size.x/2, cords.y + size.y/2), 
    //bottom left corner
    new PVector(cords.x - size.x/2, cords.y - size.y/2), 
    //bottom right corner
    new PVector(cords.x + size.x/2, cords.y - size.y/2), 
    //top left corner
    new PVector(cords.x - size.x/2, cords.y + size.y/2)

  };
  return corners;
}

interface object {
  final int TYPE_RECT = 1;
  final int TYPE_ELLIPSE = 2;

  public PVector getCords();
  public PVector getSize();
  public int getType();

  public boolean collision(object other);
  public void move(float x, float y);
  public void move(PVector delta);
  public void moveTo(float x, float y);
  public void moveTo(PVector delta);
  public void draw();
}


class rect implements object {
  PVector size;
  PVector cords;
  float acceleration;
  int type = TYPE_RECT;
  boolean collided;


  //constructor
  rect(){
    this.size = new PVector(defaultSize, defaultSize);
  }
  
  rect(PVector size) {
    this.size = size;
  }
  rect(float size){
    this.size = new PVector(size, size); 
  }
  rect(float x,float y){
    this.size = new PVector(x, y); 
  }

  public boolean collision(object other) {
    switch (other.getType()) {
    case TYPE_RECT:
      return rectWithRect(cords, size, other.getCords(), other.getSize());
    case TYPE_ELLIPSE:
      return rectWithEllipse(cords, size, other.getCords(), other.getSize());
    }
    return false;
  }

  public void move(float x, float y) {
    cords.x += x;
    cords.y += y;
  }
  public void move(PVector delta) {
    cords.add(delta);
  }
  
  public void moveTo(float x, float y){
    cords.x = x;
    cords.y = y;
  }
  public void moveTo(PVector cord){
   cords = cord; 
  }

  public void draw() {
    rect(cords.x, cords.y, size.x, size.y);
  }

  public int getType() {
    return type;
  }
  public PVector getCords() {
    return cords;
  }
  public PVector getSize() {
    return size;
  }
}


class ellipse implements object {
  PVector size;
  PVector cords = new PVector (50,50);
  float acceleration;
  int type = TYPE_ELLIPSE;
  boolean collided;


  //constructor
  ellipse(PVector size) {
    this.size = size;
  }
  ellipse(){
    this.size = new PVector(defaultSize, defaultSize); 
  }

  public boolean collision(object other) {
    switch (other.getType()) {
    case TYPE_RECT:
      return rectWithEllipse(other.getCords(), other.getSize(), cords, size);
    case TYPE_ELLIPSE:
      return collided = rectWithEllipse(cords, size, other.getCords(), other.getSize());
    }
    return false;
  }

  public void move(float x, float y) {
    cords.x += x;
    cords.y += y;
  }
  public void move(PVector delta) {
    cords.add(delta);
  }
    public void moveTo(float x, float y){
    cords.x = x;
    cords.y = y;
  }
  public void moveTo(PVector cord){
   cords = cord; 
  }

  public void draw() {
    ellipse(cords.x, cords.y, size.x, size.y);
  }

  public int getType() {
    return type;
  }
  public PVector getCords() {
    return cords;
  }
  public PVector getSize() {
    return size;
  }
}
  public void settings() {  size(1000, 1080); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Leap_motion_test" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
