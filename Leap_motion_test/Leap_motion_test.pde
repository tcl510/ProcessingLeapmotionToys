import de.voidplus.leapmotion.*; //<>// //<>// //<>//



LeapMotion leap;

void setup() {
        size(1000, 1080);
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
void displayScore(){
        String scoreString = "Score: " + score;
        textSize(20);
        textAlign(RIGHT,TOP);
        text(scoreString,width,0);
        fill(0);
}
void pause(){
        textSize( 60);
        textAlign(CENTER,CENTER);
        rectMode(CENTER);
        text("Place your hand in the tracking area to play", width/2, height/2, 600, 1000);
}

//todo
void rain() {
        float acceleration = 9.81/frameRate;
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
void drawRain() {
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

void drawFingertip(PVector fingerTip) {
        pointer.cords = fingerTip;
        pointer.draw();
}

boolean play;
int handCount = 0;
void draw() {
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
