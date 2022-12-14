package com.example.projectOOP
import android.content.Context
import android.graphics.*
import android.view.SurfaceView
import com.example.projectOOP.item.HpItem
import com.example.projectOOP.player.Player
import com.example.projectOOP.viewobject.BackgroundImage
import com.example.projectOOP.viewobject.Explosion
import com.example.projectOOP.viewobject.Missile
import com.example.projectOOP.viewobject.Mob
import java.lang.IndexOutOfBoundsException

class GameLogic(context: Context, val player: Player, private val gameView: GameView): SurfaceView(context) {
    // 배경화면 설정
    var backGrounds: ArrayList<BackgroundImage> = ArrayList()

    // 플레이어 이미지 초기화
    var playerImage: Bitmap = BitmapFactory.decodeResource(resources, player.image)

    // 플레이어 데미지 설정
    var damage = player.damage
    // 플레이어 총 체력 설정
    var health = player.health

    // 플레이어의 좌표
    var playerX: Float
    var playerY: Float

    // 게임 텍스트 설정
    private var TEXT_SIZE = 120f

    // 색 설정
    private var textPaint = Paint()
    var healthPaint = Paint()

    // 플레이어의 총 체력을 기반으로 목숨 설정
    var life = health

    // 체력 아이템 배열
    var hpItems: ArrayList<HpItem> = ArrayList()

    // 미사일 설정
    var missileReload = player.reload
    var missiles: ArrayList<Missile> = ArrayList()

    // 몬스터 배열
    private var mobs: ArrayList<Mob> = ArrayList()

    // 폭발 객체 배열
    var explosions: ArrayList<Explosion> = ArrayList()

    // 점수 설정
    var points = 0

    init {
        backGrounds.add(BackgroundImage(context, gameView.dHeight))
        backGrounds.add(BackgroundImage(context, gameView.dHeight))

        // 배경화면 초기화
        backGrounds[1].y = backGrounds[0].y - backGrounds[0].nowImage.height

        // TEXT 초기화
        textPaint.color = Color.YELLOW
        textPaint.textSize = TEXT_SIZE
        textPaint.textAlign = Paint.Align.LEFT

        // 목숨 이미지 설정
        healthPaint.color = Color.GREEN

        // Player 초기화
        playerX = (gameView.dWidth / 2 - playerImage.width / 2).toFloat()
        playerY = (gameView.dHeight - playerImage.height).toFloat()

        // 몬스터 배열 초기화
        for (i in 0..7) {
            val mob = Mob(context, gameView.dWidth)
            mobs.add(mob)
        }
    }

    // 게임 그리기
    // GameThread에 의해 지속적으로 호출됨.
    fun drawGame(canvas: Canvas) {
        drawBackground(canvas)
        drawItems(canvas)
        drawExplosion(canvas)
        canvas.drawBitmap(playerImage, playerX, playerY+100, null)
        // 배열의 몬스터들을 draw
        for (i in mobs.indices) {
            mobs[i].getMob(mobs[i].mobFrame)?.let {
                canvas.drawBitmap(
                    it,
                    mobs[i].mobX.toFloat(),
                    mobs[i].mobY.toFloat(),
                    null
                )
            }
            // 움직일 때 마다 몬스터의 이미지를 변경
            mobs[i].mobFrame++

            // 배열의 이미지들을 순환
            if (mobs[i].mobFrame > 7) {
                mobs[i].mobFrame = 0
            }

            //몬스터의 움직임 제어
            mobs[i].movement()
            checkWallCrash(mobs[i])

            // 화면 밖으로 사라질 때 처리
            if (mobs[i].mobY + mobs[i].mobHeight >= gameView.dHeight + 200) {
                mobs[i].reGen()
            }
        }

        // 몬스터와 플레이어가 부딪혔을 때 처리
        checkMobHit(mobs, playerX, playerY, playerImage)

        // 목숨에 따른 목숨 상태 색 변경
        if (life <= health / 3) {
            healthPaint.color = Color.RED
        } else if (life <= health / 3 * 2 ) {
            healthPaint.color = Color.YELLOW
        } else
            healthPaint.color = Color.GREEN

        // 업데이트 된 목숨 draw
        canvas.drawRect(
            (gameView.dWidth - 300).toFloat(),
            30f,
            ((gameView.dWidth - 300) + 200 * life.toFloat() / health.toFloat()),
            80f,
            healthPaint
        )
        // 업데이트 된 점수 draw
        canvas.drawText("" + points, 20f, TEXT_SIZE, textPaint)
    }

    // 배경화면 그리기
    private fun drawBackground(canvas: Canvas) {
        // 해당 메소드가 호출 될 때마다 이미지의 y가 증가하므로 이미지가 하강.
        for(i in 0..1) {
            backGrounds[i].move()
        }

        // 배경화면 화면 끝 체크
        checkBackgroundBottom(backGrounds, gameView.dHeight)

        for(i in 0..1) {
            // 해당 메소드가 호출될 때마다 수정된 y값을 기준으로 draw
            canvas.drawBitmap(
                backGrounds[i].nowImage,
                backGrounds[i].x.toFloat(),
                backGrounds[i].y.toFloat(),
                null
            )
        }
    }

    // 아이템 그리기
    private fun drawItems(canvas: Canvas) {
        for (i in hpItems.indices) {
            hpItems[i].let {
                canvas.drawBitmap(
                    hpItems[i].itemImg,
                    hpItems[i].itemX.toFloat() + 30,
                    hpItems[i].itemY.toFloat(),
                    null
                )
            }
        }

        // 아이템 움직임
        for (i in hpItems.indices) {
            hpItems[i].move()
        }

        checkItemHit(hpItems, playerX, playerY, playerImage)    // 플레이어와 아이템 충돌체크
    }
    // 미사일 그리기
    fun drawMissile(canvas: Canvas) {
        for (i in missiles.indices.reversed()) {
            canvas.drawBitmap(
                missiles[i].missileImg,
                missiles[i].missileX,
                missiles[i].missileY,
                null
            )
            missiles[i].move()      // 미사일 움직임
        }

        for (i in missiles.indices.reversed()) {
            if (missiles[i].missileY < 0) {     // 미사일이 화면 위쪽으로 사라지면 배열에서 삭제
                missiles.removeAt(i)
            }
        }
        checkMissileHit(mobs, missiles)     // 미사일과 몬스터 충돌체크
    }
    // 폭발 효과 그리기
    private fun drawExplosion(canvas: Canvas) {
        // explosions 배열에 객체가 있다면 폭발 그림.
        // 객체가 없다면 폭발이 없음.
        for (i in explosions.indices) {
            try {
                explosions[i].getExplosion(explosions[i].explosionFrame)?.let {
                    canvas.drawBitmap(
                        it,
                        explosions[i].explosionX.toFloat(),
                        explosions[i].explosionY.toFloat(),
                        null
                    )
                }
                explosions[i].explosionFrame++
                if (explosions[i].explosionFrame > 2) {
                    explosions.removeAt(i)
                }
            } catch (e: IndexOutOfBoundsException) {
                e.stackTrace
            }
        }
    }
    // 벽 충돌 체크
    private fun checkWallCrash(mob: Mob){
        if(mob.mobX + mob.mobWidth >= gameView.dWidth + 150) { // 오른쪽 벽에 닿았을 때
            mob.direction = 0
        }else if(mob.mobX - mob.mobWidth <= gameView.dWidth - 1600){
            mob.direction = 1
        }
    }
    // 플레이어 몬스터 충돌 체크
    private fun checkMobHit(_mobs: ArrayList<Mob>, _playerX: Float, _playerY: Float, _playerImage: Bitmap) {
        for (i in _mobs.indices) {
            if (
                (((_mobs[i].mobX <= _playerX) && (_mobs[i].mobX + _mobs[i].mobWidth >= _playerX + 270)) ||// 몬스터가 왼쪽에 있을 때
                        ((_mobs[i].mobX + _mobs[i].mobWidth >= _playerX + _playerImage.width) && (_mobs[i].mobX <= _playerX + _playerImage.width - 200))) // 몬스터가 오른족에 있을 때
                && (_mobs[i].mobY + _mobs[i].mobHeight >= _playerY + 250) // 몬스터와 플레이어의 높이가 겹칠때
            ) {

                // 목숨 1 감소 후 몬스터 다시만듬
                life -= 1
                // 몬스터 리젠
                _mobs[i].reGen()

                // 목숨을 모두 소모 시
                // gameView 파괴. 즉, 게임 종료
                if (life == 0) {
                    gameView.surfaceDestroyed(holder)
                }
            }
        }
    }
    // 미사일과 적 충돌 체크
    private fun checkMissileHit(_mobs: ArrayList<Mob>, _missiles: ArrayList<Missile>) {
        for (i in _mobs.indices.reversed()) {        // 총알과 적 충돌 계산
            for (j in _missiles.indices.reversed()) {
                if (
                    _mobs[i].mobX + _mobs[i].mobWidth / 2 >= _missiles[j].missileX - 50 &&
                    _mobs[i].mobX - _mobs[i].mobWidth / 2 <= _missiles[j].missileX - 50 &&
                    _mobs[i].mobY + _mobs[i].mobHeight / 2 >= _missiles[j].missileY
                ) {

                    _mobs[i].mobHp -= damage
                    _missiles.removeAt(j)

                    if (_mobs[i].mobHp <= 0) {
                        val explosion = Explosion(context)
                        explosion.explosionX = _mobs[i].mobX + 70
                        explosion.explosionY = _mobs[i].mobY
                        explosions.add(explosion)

                        if (_mobs[i].itemDrop == 0) {    // 몬스터가 갖고 있는 0~9 값 중 0에 해당하면 아이템 생성
                            hpItems.add(
                                HpItem(
                                    context,
                                    explosion.explosionX,
                                    explosion.explosionY
                                )
                            )    // 폭발이 발생한 위치 좌표에 따라 아이템을 생성한다
                        }
                        _mobs[i].reGen()
                        points += 10
                    }
                }
            }
        }
    }
    // 플레이어와 아이템 충돌 체크
    private fun checkItemHit(_hpItems: ArrayList<HpItem>, _playerX: Float, _playerY: Float, _playerImage: Bitmap) {
        for (i in _hpItems.indices.reversed()) {   // 플레이어와 아이템 충돌
            if (_hpItems[i].itemX + _hpItems[i].itemWidth - 100 >= _playerX
                && _hpItems[i].itemX + 100 <= _playerX + _playerImage.width
                && _hpItems[i].itemY + _hpItems[i].itemHeight >= _playerY + 100
            ) {
                if (life < health)      // 체력이 최대체력 적은 경우
                    life += 1           // 체력이 1만큼 증가시킨다
                _hpItems.removeAt(i)     // 충돌 시 배열에서 삭제
            }
        }
    }
    // 배경화면 화면 끝 체크
    private fun checkBackgroundBottom(_backGrounds: ArrayList<BackgroundImage>, _height: Int) {
        for (i in 0..1) {
            // 배경 이미지의 y 좌표가 화면의 하단보다 낮으면
            if (_backGrounds[i].y > _height) {
                // 상단으로 이미지의 위치를 변경
                _backGrounds[i].reGen(_backGrounds[1 - i].y - _backGrounds[1 - i].nowImage.height)
                // 이미지 변경을 위한 count up
                _backGrounds[i].count += 1

                // 조건 만족 시 배경화면 이미지를 다음 이미지로 변경
                if (_backGrounds[i].count == 1) {
                    // 이미지 변경을 위한 count는 초기화
                    _backGrounds[i].count = 0
                    _backGrounds[i].changeImage()
                }
            }
        }
    }
}