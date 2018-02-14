package ru.geekbrains.stargame.ship;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.stargame.bullet.BulletPool;
import ru.geekbrains.stargame.engine.pool.SpritesPool;

public class EnemyShipPool extends SpritesPool<EnemyShip> {

    private final TextureAtlas atlas;
    private Sound sound;
    private BulletPool bulletPool;

    public EnemyShipPool(TextureAtlas atlas, BulletPool bulletPool, Sound sound) {
        this.atlas = atlas;
        this.bulletPool = bulletPool;
        this.sound = sound;
    }

    @Override
    protected EnemyShip newObject() {
        return new EnemyShip(atlas, bulletPool, sound);
    }
}
