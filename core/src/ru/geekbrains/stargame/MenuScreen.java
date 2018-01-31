package ru.geekbrains.stargame;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.engine.Base2DScreen;

public class MenuScreen extends Base2DScreen {

    private SpriteBatch batch;
    private Texture background;
    private Texture img;
    private Vector2 position;
    private Vector2 targetPosition;
    float vx;
    float tuchX;
    float tuchY;
    float x;
    float y;

    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        batch = new SpriteBatch();
        background = new Texture("space3.png");
        img = new Texture("Human-Fighter.png");
        position = new Vector2((Gdx.graphics.getWidth() / 2), (Gdx.graphics.getWidth() / 4));
//        vx = 30.0f;
    }

    @Override
    public void render(float delta) {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        super.render(delta);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background, 0, 0);
        batch.draw(img, x, y);
        batch.end();
    }

    public void update(float dt) {
        x = position.x;
        y = position.y;
        if (tuchX != 0 && tuchY !=0) {
            x = targetPosition.x;
            y = targetPosition.y;
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        super.dispose();

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        tuchX = screenX;
        tuchY = Gdx.graphics.getHeight() - screenY;
        targetPosition = new Vector2(tuchX, tuchY);
        return super.touchUp(screenX, screenY, pointer, button);
    }
}
