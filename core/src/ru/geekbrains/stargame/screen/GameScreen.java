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
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.geekbrains.stargame.Background;
import ru.geekbrains.stargame.bullet.Bullet;
import ru.geekbrains.stargame.bullet.BulletPool;
import ru.geekbrains.stargame.engine.ActionListener;
import ru.geekbrains.stargame.engine.Base2DScreen;
import ru.geekbrains.stargame.engine.font.Font;
import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.math.Rnd;
import ru.geekbrains.stargame.explosion.ExplosionPool;
import ru.geekbrains.stargame.other.Medkit;
import ru.geekbrains.stargame.other.MedkitEmmiter;
import ru.geekbrains.stargame.other.MedkitPool;
import ru.geekbrains.stargame.ship.EnemyEmmiter;
import ru.geekbrains.stargame.ship.EnemyShip;
import ru.geekbrains.stargame.ship.EnemyPool;
import ru.geekbrains.stargame.ship.MainShip;
import ru.geekbrains.stargame.star.TrackingStar;
import ru.geekbrains.stargame.ui.ButtonNewGame;
import ru.geekbrains.stargame.ui.MessageGameOver;

public class GameScreen extends Base2DScreen implements ActionListener {

    private enum State { PLAYING, GAME_OVER };
    private State state;
    private MessageGameOver messageGameOver;
    private ButtonNewGame buttonNewGame;



    private static final int STAR_COUNT = 50; //количество звезд
    private static final float STAR_HEIGHT = 0.01f; //высота звезды
    private static final float FONT_SIZE = 0.025f; //размер шрифта

    private TextureAtlas atlas;
    private TextureAtlas atlasMedkit; //++++
    private Texture backgroundTexture;
    private Background background;
    private TrackingStar[] stars;
    private MainShip mainShip;

    private MedkitPool medkitPool; //++++
    private MedkitEmmiter medkitEmmiter; //++++
    private final BulletPool bulletPool = new BulletPool();
    private ExplosionPool explosionPool;
    private EnemyPool enemyPool;
    private EnemyEmmiter enemyEmmiter;

    private Sound soundMedkit; //++++
    private Sound soundExplosion;
    private Sound soundLaser;
    private Sound soundBullet;
    private Music music;
    private int frags;
    private Font font;
    private StringBuilder sbFrags = new StringBuilder();
    private StringBuilder sbHP = new StringBuilder();
    private StringBuilder sbStage = new StringBuilder();


    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();
        soundLaser = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        soundBullet = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        soundExplosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        soundMedkit = Gdx.audio.newSound(Gdx.files.internal("sounds/medkit_pickup.wav")); //++++

        backgroundTexture = new Texture("textures/bg.png");
        background = new Background(new TextureRegion(backgroundTexture));
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        atlasMedkit = new TextureAtlas("textures/med.tpack");

        this.medkitPool = new MedkitPool(worldBounds, soundMedkit); //++++
        this.medkitEmmiter = new MedkitEmmiter(medkitPool, worldBounds, atlasMedkit); //++++
        this.explosionPool = new ExplosionPool(atlas, soundExplosion);
        mainShip = new MainShip(atlas, bulletPool, explosionPool, worldBounds, soundLaser);
        this.enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds,mainShip,soundBullet);
        this.enemyEmmiter = new EnemyEmmiter(enemyPool, worldBounds, atlas);
        stars = new TrackingStar[STAR_COUNT];
        for (int i = 0; i < stars.length ; i++) {
            stars[i] = new TrackingStar(atlas, Rnd.nextFloat(-0.005f, 0.005f), Rnd.nextFloat(-0.5f, -0.1f), STAR_HEIGHT, mainShip.getV());
        }
        this.messageGameOver = new MessageGameOver(atlas);
        this.buttonNewGame = new ButtonNewGame(atlas, this);
        this.font = new Font("font/font.fnt", "font/font.png");
        this.font.setWorldSize(FONT_SIZE);
        startNewGame();
    }

    public void printInfo() {
        sbFrags.setLength(0);
        sbHP.setLength(0);
        sbStage.setLength(0);
        font.draw(batch, sbFrags.append("Frags: ").append(frags), worldBounds.getLeft(), worldBounds.getTop());
        font.draw(batch, sbHP.append("HP: ").append(mainShip.getHp()), worldBounds.pos.x, worldBounds.getTop(), Align.center);
        font.draw(batch, sbStage.append("Stage: ").append(enemyEmmiter.getStage()), worldBounds.getRight(), worldBounds.getTop(), Align.right);

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (state == State.PLAYING) {
            checkColisions();
        }
        deleteAlldestroyed();
        update(delta);
        draw();
    }

    public void checkColisions() {
        //столкновение кораблей
        List<EnemyShip> enemyShipList = enemyPool.getActiveObjects();
        for (EnemyShip enemy : enemyShipList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            float minDist = enemy.getHalfWidth() + mainShip.getHalfWidth();
            if (enemy.pos.dst2(mainShip.pos) < minDist * minDist) {
                enemy.setDestroyed(true);
                enemy.boom();
                mainShip.damage(mainShip.getHp());
                return;
            }
        }

        //столкновение игрового корабля с аптечкой
        List<Medkit> MedkitList = medkitPool.getActiveObjects();
        for (Medkit medkit : MedkitList) {
            if (medkit.isDestroyed()) {
                continue;
            }
            float minDist = medkit.getHalfWidth() + mainShip.getHalfWidth();
            if (medkit.pos.dst2(mainShip.pos) < minDist * minDist) {
                medkit.setDestroyed(true);
                mainShip.addDamage(medkit.getMedkitHp());
                soundMedkit.play();
                return;
            }
        }

        //нанесение урона вражескому кораблю
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (EnemyShip enemy : enemyShipList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            for (Bullet bullet : bulletList) {
                if (bullet.getOwner() != mainShip || bullet.isDestroyed()) {
                    continue;
                }
                if (enemy.isBulletCollision(bullet)) {
                    enemy.damage(bullet.getDamage());
                    bullet.setDestroyed(true);
                    if (enemy.isDestroyed()) {
                        frags++;
                        break;
                    }
                }
            }
        }

        //нанесение урона игровому кораблю
        for (Bullet bullet : bulletList) {
            if (bullet.isDestroyed() || bullet.getOwner() == mainShip) {
                continue;
            }
            if (mainShip.isBulletCollision(bullet)) {
                mainShip.damage(bullet.getDamage());
                bullet.setDestroyed(true);
            }
        }
    }

    public void deleteAlldestroyed() {
        bulletPool.freeAllDestroedActiveObjects();
        explosionPool.freeAllDestroedActiveObjects();
        enemyPool.freeAllDestroedActiveObjects();
        medkitPool.freeAllDestroedActiveObjects();

    }

    public void update(float delta) {
        for (int i = 0; i < stars.length ; i++) {
            stars[i].update(delta);
        }
        explosionPool.updateActiveObjects(delta);
        switch (state) {
            case PLAYING:
                bulletPool.updateActiveObjects(delta);
                medkitPool.updateActiveObjects(delta);//++++
                medkitEmmiter.generateMedkit(delta);
                enemyPool.updateActiveObjects(delta);
                mainShip.update(delta);
                enemyEmmiter.generateEnemy(delta, frags);
                if (mainShip.isDestroyed()) {
                    state = State.GAME_OVER;
                }
                break;
            case GAME_OVER:
                break;
        }
    }

    public void draw() {
        Gdx.gl.glClearColor(0.7f, 0.3f, 0.7f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (int i = 0; i < stars.length ; i++) {
            stars[i].draw(batch);
        }
        explosionPool.drawActiveObjects(batch);
        printInfo();
        if (state == State.GAME_OVER){
            messageGameOver.draw(batch);
            buttonNewGame.draw(batch);
        } else {
            bulletPool.drawActiveObjects(batch);
            enemyPool.drawActiveObjects(batch);
            mainShip.draw(batch);
            medkitPool.drawActiveObjects(batch);//++++
        }
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
        enemyPool.dispose();
        medkitPool.dispose();//++++
        soundLaser.dispose();
        soundBullet.dispose();
        soundExplosion.dispose();
        soundMedkit.dispose();
        music.dispose();
        font.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyUp(keycode);
        }
        return false;
    }

    @Override
    protected void touchDown(Vector2 touch, int pointer) {
        if (state == State.PLAYING) {
            mainShip.touchDown(touch, pointer);
        } else {
            buttonNewGame.touchUp(touch, pointer);
        }
    }

    @Override
    protected void touchUp(Vector2 touch, int pointer) {
        if (state == State.PLAYING) {
            mainShip.touchUp(touch, pointer);
        } else {
            buttonNewGame.touchUp(touch, pointer);
        }
    }

    private void startNewGame() {
        enemyEmmiter.setToNewGame();
        state = State.PLAYING;
        frags = 0;
        mainShip.setToNewGame();
        bulletPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
        medkitPool.freeAllActiveObjects();
    }

    @Override
    public void actionPerformed(Object src) {
        if (src == buttonNewGame) {
            startNewGame();
        } else {
            throw new RuntimeException("Unknown src " + src);
        }
    }
}
