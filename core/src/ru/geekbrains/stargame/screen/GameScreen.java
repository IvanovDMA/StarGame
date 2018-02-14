package ru.geekbrains.stargame.screen;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.Background;
import ru.geekbrains.stargame.bullet.BulletPool;
import ru.geekbrains.stargame.engine.ActionListener;
import ru.geekbrains.stargame.engine.Base2DScreen;
import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.math.Rnd;
import ru.geekbrains.stargame.explosion.Explosion;
import ru.geekbrains.stargame.explosion.ExplosionPool;
import ru.geekbrains.stargame.ship.EnemyShip;
import ru.geekbrains.stargame.ship.EnemyShipPool;
import ru.geekbrains.stargame.ship.MainShip;
import ru.geekbrains.stargame.star.TrackingStar;

public class GameScreen extends Base2DScreen implements ActionListener {

    private static final int STAR_COUNT = 50;
    private static final float STAR_HEIGHT = 0.01f;

    private float startEnemyTimer;
    private final float startEnemyInterval = 10f;

    private TextureAtlas atlas;
    private Texture backgroundTexture;
    private Background background;
    private TrackingStar[] stars;
    private MainShip mainShip;

    private final BulletPool bulletPool = new BulletPool();
    private ExplosionPool explosionPool;
    private EnemyShipPool enemyShipPool;
    private Sound soundExplosion;
    private Sound soundMainShip;
    private Music music;


    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();
        soundMainShip = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        soundExplosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        backgroundTexture = new Texture("textures/bg.png");
        background = new Background(new TextureRegion(backgroundTexture));
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        mainShip = new MainShip(atlas, bulletPool, soundMainShip);
        stars = new TrackingStar[STAR_COUNT];
        for (int i = 0; i < stars.length ; i++) {
            stars[i] = new TrackingStar(atlas, Rnd.nextFloat(-0.005f, 0.005f), Rnd.nextFloat(-0.5f, -0.1f), STAR_HEIGHT, mainShip.getV());
        }
        this.explosionPool = new ExplosionPool(atlas, soundExplosion);
        this.enemyShipPool = new EnemyShipPool(atlas, bulletPool, soundMainShip);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        deleteAlldestroyed();
        update(delta);
        draw();
    }

    public void deleteAlldestroyed() {
        bulletPool.freeAllDestroedActiveObjects();
        explosionPool.freeAllDestroedActiveObjects();
        enemyShipPool.freeAllDestroedActiveObjects();
    }

    public void update(float delta) {
        for (int i = 0; i < stars.length ; i++) {
            stars[i].update(delta);
        }
        bulletPool.updateActiveObjects(delta);
        start(delta);
        explosionPool.updateActiveObjects(delta);
        enemyShipPool.updateActiveObjects(delta);
        mainShip.update(delta);
        EnemyShip enemyShip = enemyShipPool.obtain();
        enemyShip.update(delta);

    }

    public void draw() {
        Gdx.gl.glClearColor(0.7f, 0.3f, 0.7f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (int i = 0; i < stars.length ; i++) {
            stars[i].draw(batch);
        }
        mainShip.draw(batch);
        bulletPool.drawActiveObjects(batch);
        explosionPool.drawActiveObjects(batch);
        enemyShipPool.drawActiveObjects(batch);
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (int i = 0; i < stars.length ; i++) {
            stars[i].resize(worldBounds);
        }
        mainShip.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
        atlas.dispose();
        bulletPool.dispose();
        explosionPool.dispose();
        enemyShipPool.dispose();
    }

    public void start(float delta) {
        startEnemyTimer += delta;
        if (startEnemyTimer > startEnemyInterval) {
            EnemyShip enemyShip = enemyShipPool.obtain();
            System.out.println("Вражеский корабль создан");
        }
    }

    @Override
    public void actionPerformed(Object src) {

    }

    @Override
    public boolean keyDown(int keycode) {
        mainShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShip.keyUp(keycode);
        return false;
    }

    @Override
    protected void touchDown(Vector2 touch, int pointer) {
        mainShip.touchDown(touch, pointer);
        Explosion explosion = explosionPool.obtain();
        explosion.set(0.1f, touch);
    }

    @Override
    protected void touchUp(Vector2 touch, int pointer) {
        mainShip.touchUp(touch, pointer);
    }
}
