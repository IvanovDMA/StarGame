package ru.geekbrains.stargame.ship;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.bullet.Bullet;
import ru.geekbrains.stargame.bullet.BulletPool;
import ru.geekbrains.stargame.engine.Sprite;
import ru.geekbrains.stargame.engine.math.Rect;

public class Ship extends Sprite {

    protected final Vector2 v = new Vector2(); //скорость коробля
    protected Rect worldBounds; //границы мира

    protected BulletPool bulletPool;
    protected TextureRegion bulletRegion;

    protected final Vector2 bulletV = new Vector2(); //скорость пули
    protected float bulletHeight; //высота пуди
    protected int bulletDamage; //урон от пули
    protected float reloadInterval; //время перезарядки
    protected float reloadTimer; //таймер для стрельбы

    protected Sound sound;

    public Ship(TextureRegion region, int rows, int cols, int frames, Sound sound) {
        super(region, rows, cols, frames);
        this.sound = sound;
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    protected void shoot() {
        sound.play();
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, pos, bulletV, bulletHeight, worldBounds, bulletDamage);
    }
}
