package ru.geekbrains.stargame.ship;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.stargame.bullet.BulletPool;
import ru.geekbrains.stargame.engine.math.Rect;


public class MainShip extends Ship {

    private static final float SHIP_HEIGHT = 0.15f;
    private static final float BOTTOM_MARGIN = 0.05f;
    private static final int INVALID_POINTER = -1;

    private final Vector2 v0 = new Vector2(0.5f, 0.0f);
//    private final Vector2 v1 = new Vector2(0.0f, 0.5f);

    private boolean pressedLeft;
    private boolean pressedRight;

    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;
    private Sound sound;

    public MainShip(TextureAtlas atlas, BulletPool bulletPool, Sound sound) {
        super(atlas.findRegion("main_ship"), 1, 2, 2, sound);
        setHeightProportion(SHIP_HEIGHT);
        this.bulletPool = bulletPool;
        this.bulletRegion = atlas.findRegion("bulletMainShip");
        this.bulletHeight = 0.01f;
        this.bulletV.set(0, 0.5f);
        this.bulletDamage = 1;
        this.reloadInterval = 0.2f;
        this.sound = sound;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        reloadTimer += delta;
        if (reloadTimer > reloadInterval) {
            reloadTimer = 0f;
            shoot();
        }
        if (getRight() > worldBounds.getRight()) {
        setRight(worldBounds.getRight());
        stop();
        }
        if (getLeft() < worldBounds.getLeft()) {
        setLeft(worldBounds.getLeft());
        stop();
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setBottom(worldBounds.getBottom() + BOTTOM_MARGIN);
    }

    public void keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                moveLeft();
                break;

            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = true;
                moveRight();
                break;

//            case Input.Keys.W:
//            case Input.Keys.UP:
//                moveUp();
//                break;
//
//            case Input.Keys.S:
//            case Input.Keys.DOWN:
//                moveDown();
//                break;
        }
    }

    public void keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if (pressedRight) {
                    moveRight();
                } else {
                    stop();
                }
                break;

            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
                if (pressedLeft) {
                    moveLeft();
                } else {
                    stop();
                }
//            case Input.Keys.W:
//            case Input.Keys.UP:
//            case Input.Keys.S:
//            case Input.Keys.DOWN:
                break;
            case Input.Keys.UP:
            case Input.Keys.W:
                shoot();
                break;
        }
    }

    @Override
    public void touchDown(Vector2 touch, int pointer) {
        if (worldBounds.pos.x > touch.x) {
            if (leftPointer != INVALID_POINTER) return;
            leftPointer = pointer;
            moveLeft();
        } else{
            if (rightPointer != INVALID_POINTER) return;
            rightPointer = pointer;
            moveRight();
        }
    }

    @Override
    public void touchUp(Vector2 touch, int pointer) {
        if (pointer == leftPointer) {
            leftPointer = INVALID_POINTER;
            if (rightPointer != INVALID_POINTER) moveRight(); else stop();
        } else if (pointer == rightPointer) {
            rightPointer = INVALID_POINTER;
        if (leftPointer != INVALID_POINTER) moveLeft(); else stop();
        }
    }

//    private void moveUp() {
//        v.set(v1);
//    }
//
//    private void moveDown() {
//        v.set(v1).rotate(180);
//    }

    private void moveRight() {
        v.set(v0);
    }

    private void moveLeft() {
        v.set(v0).rotate(180);
    }

    private void stop() {
        v.setZero();
    }

    public Vector2 getV() {
        return v;
    }
}
