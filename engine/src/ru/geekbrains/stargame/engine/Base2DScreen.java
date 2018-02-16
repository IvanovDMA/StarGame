package ru.geekbrains.stargame.engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.engine.math.MatrixUtils;
import ru.geekbrains.stargame.engine.math.Rect;

public class Base2DScreen implements Screen, InputProcessor {

    protected Game game;

    private Rect screenBounds; //границы области рисования в пикселях
    protected Rect worldBounds; //границы проекции мировых координат
    private Rect glBounds; //дефолтные границы проекции в gl

    protected Matrix4 worldToGl;
    protected Matrix3 screenToWorld;
    private final Vector2 touch = new Vector2();
    protected SpriteBatch batch;

    public Base2DScreen(Game game) {
        this.game = game;

    }

    @Override
    public void show() {
        System.out.println("show");
        Gdx.input.setInputProcessor(this);
        this.screenBounds = new Rect();
        this.worldBounds = new Rect();
        this.glBounds = new Rect(0, 0, 1.0f, 1.0f);
        this.worldToGl = new Matrix4();
        this.screenToWorld = new Matrix3();
        if (batch != null) {
            throw new RuntimeException("batch != null повторная установка screen без dispose");
        }
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        System.out.println("resize width: " + width + " height: " + height);
        screenBounds.setSize(width, height);
        screenBounds.setLeft(0);
        screenBounds.setBottom(0);

        float aspect = width / (float) height;
        worldBounds.setHeight(1.0f);
        worldBounds.setWidth(1.0f * aspect);
        MatrixUtils.calcTransitionMatrix(worldToGl, worldBounds, glBounds);
        batch.setProjectionMatrix(worldToGl);
        MatrixUtils.calcTransitionMatrix(screenToWorld, screenBounds, worldBounds);
        resize(worldBounds);
    }

    public void resize(Rect worldBounds) {

    }

    @Override
    public void pause() {
        System.out.println("pause");
    }

    @Override
    public void resume() {
        System.out.println("resume");
    }

    @Override
    public void hide() {
        System.out.println("hide");
    }

    @Override
    public void dispose() {
        batch.dispose();
        System.out.println("dispose");
    }



    @Override
    public boolean keyDown(int keycode) {
        System.out.println("keyDown keycode: " + keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        System.out.println("keyUp keycode: " + keycode);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        System.out.println("keyTyped character: " + character);
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, (screenBounds.getHeight() - screenY)).mul(screenToWorld);
        System.out.println("touchDown X: " + touch.x + " Y: " + touch.y + " pointer: " + pointer + " button: " + button);
        touchDown(touch, pointer);
        return false;
    }

    protected void touchDown(Vector2 touch, int pointer) {

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, (screenBounds.getHeight() - screenY)).mul(screenToWorld);
        System.out.println("touchUp X: " + touch.x + " Y: " + touch.y + " pointer: " + pointer + " button: " + button);
        touchUp(touch, pointer);
        return false;
    }

    protected void touchUp(Vector2 touch, int pointer) {

    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        touch.set(screenX, (screenBounds.getHeight() - screenY)).mul(screenToWorld);
        System.out.println("touchDragged X: " +touch.x + " Y: " + touch.y + " pointer: " + pointer);
        touchDragged(touch, pointer);
        return false;
    }

    protected void touchDragged(Vector2 touch, int pointer) {

    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
