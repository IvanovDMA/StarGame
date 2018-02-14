package ru.geekbrains.stargame.ship;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.bullet.BulletPool;
import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.math.Rnd;

public class EnemyShip extends Ship {

    private static final float SHIP_HEIGHT = 0.15f;
    private Vector2 pos0;
    protected float startInterval; //время старта вражеского коробля
    protected float reloadInterval; //время перезарядки
    protected float reloadTimer; //таймер для стрельбы

    private final Vector2 v0 = new Vector2(0.0f,-0.5f);


    public EnemyShip(TextureAtlas atlas, BulletPool bulletPool, Sound sound) {
        super(atlas.findRegion("enemy0"), 1, 2, 2, sound);
        setHeightProportion(SHIP_HEIGHT);
        this.bulletPool = bulletPool;
        this.bulletRegion = atlas.findRegion("bulletEnemy");
        this.bulletHeight = 0.01f;
        this.bulletV.set(0, 0.5f);
        this.bulletDamage = 1;
        this.reloadInterval = 0.2f;
        this.startInterval = 0.1f;
        this.sound = sound;
    }

    @Override
    public void update(float delta) {
    pos.mulAdd(v0, delta);
//        reloadTimer += delta;
//        if (reloadTimer > reloadInterval) {
//            reloadTimer = 0f;
//            shoot();
//        }
//        if (getBottom() < worldBounds.getBottom()) {
//            setDestroyed(true);
//        }
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
//        float posX = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
//        float posY = worldBounds.getHalfHeight();
//        pos.set(posX, posY);
    }
}
