package com.theironyard.superkoalio;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SuperKoalio extends ApplicationAdapter {
    SpriteBatch batch;
    TextureRegion stand, jump;
    Animation walk;

    static final int WIDTH = 18;
    static final int HEIGHT = 26;
    static final int DRAW_WIDTH = WIDTH * 3;
    static final int DRAW_HEIGHT = HEIGHT * 3;
    static final float MAX_VELOCITY = 500;
    static final float MAX_JUMP_VELOCITY = 1500;
    static final int GRAVITY = -50;

    float x, y, xv, yv, time;
    boolean canJump;
    boolean faceRight = true;



    @Override
    public void create () {
        batch = new SpriteBatch();
        Texture sheet = new Texture("koalio.png");
        TextureRegion[][] tiles = TextureRegion.split(sheet, WIDTH, HEIGHT);
        stand = tiles[0][0];
        jump = tiles[0][1];
        walk = new Animation(0.1f, tiles[0][2], tiles[0][3], tiles[0][4]);  //this is an animation for him walking 0.1 of a second
    }

    @Override
    public void render () {
        time += Gdx.graphics.getDeltaTime();  //this keeps track of time since start of game
        move();

        TextureRegion img;
        if (y > 0) {
            img = jump;  //jump image if he is not at y=0
        } else if (xv != 0) {
            img = walk.getKeyFrame(time, true);  //call the walking animation if there is acceleration
        } else {
            img = stand;  //otherwise dude is just standing there
        }

        Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        if (faceRight) {
            batch.draw(img, x, y, DRAW_WIDTH, DRAW_HEIGHT);
        } else {
            batch.draw(img, x + DRAW_WIDTH, y, DRAW_WIDTH * -1, DRAW_HEIGHT);
        }
        batch.end();
    }

    public float decelerate(float velocity, float deceleration ) {

        velocity *= deceleration;
        if (Math.abs(velocity) < 1) {
            velocity = 0;
        }
        return velocity;
    }

    void move() {
        //grabbing a keystroke
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && canJump) {
            yv = MAX_JUMP_VELOCITY;
            canJump = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            yv = MAX_VELOCITY * -1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xv = MAX_VELOCITY;
            faceRight = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xv = MAX_VELOCITY * -1;
            faceRight = false;
        }

        yv += GRAVITY;

        y += yv * Gdx.graphics.getDeltaTime();
        x += xv * Gdx.graphics.getDeltaTime();

        if (y < 0) {
            y = 0;
            canJump = true;
        }

        yv = decelerate(yv, 0.9f);
        xv = decelerate(xv, 0.6f);

    }
}
