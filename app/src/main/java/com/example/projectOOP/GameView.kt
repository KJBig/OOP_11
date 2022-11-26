package com.example.projectOOP

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.os.bundleOf

import com.example.projectOOP.thread.GameThread
import com.example.projectOOP.thread.MissileThread

//class GameView(context: Context, _health: Int, _damage: Int, _missileReload: Int, _playerImage: Int, private val controller: GameFragment) : SurfaceView(context), SurfaceHolder.Callback {
//class GameView(context: Context, _health: Int, _damage: Int, _missileReload: Int, _playerImage: Int, private val controller: GameOverController) : SurfaceView(context), SurfaceHolder.Callback {
class GameView(context: Context, _health: Int, _damage: Int, _missileReload: Int, _playerImage: Int, private val controller: GameOverController) : SurfaceView(context), SurfaceHolder.Callback {


    private var gameLogic = GameLogic(context,this)
//    // 배경화면 array
//    private val bgAry = arrayOf<Bitmap>(
//        BitmapFactory.decodeResource(resources, R.drawable.bg_spring),
//        BitmapFactory.decodeResource(resources, R.drawable.bg_sea),
//        BitmapFactory.decodeResource(resources, R.drawable.bg_fall),
//        BitmapFactory.decodeResource(resources, R.drawable.bg_winter),
//        BitmapFactory.decodeResource(resources, R.drawable.bg_high),
//        BitmapFactory.decodeResource(resources, R.drawable.bg_desert))
//
//    // 배경화면 설정
//    private var backgroundImage1: BackgroundImage
//    private var backgroundImage2: BackgroundImage
//    private var backgroundCount1 =0
//    private var backgroundCount2 =0
//    private var bgNum = 0
//    private var Bg1 =  bgAry[0]
//    private var Bg2 =  bgAry[bgNum]
//
//    // Player 설정
//    var player: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.player1)
    var playerImage = _playerImage
//    var damage = _damage
//    var playerX: Float
//    var playerY: Float
//    var oldX = 0f
//    var oldPlayerX = 0f
//
//    // itme 설정
//    var item: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.hpitem)
//
    //화면 좌표 설정
    var rectBackground: Rect
    var dWidth = 0
    var dHeight = 0
//
//    // TEXT 설정
//    var TEXT_SIZE = 120f
//    var textPaint = Paint()
//
//    // 목숨 설정
//    var healthPaint = Paint()
//    var health = _health
//    var life = health
//    var hpItems: ArrayList<HpItem> = ArrayList()

    // Thread 설정
    val thread: GameThread
    val threadShot: MissileThread

//    // 미사일 설정
//    var missileReload = _missileReload
//    var missiles: ArrayList<Missile> = ArrayList()
//
//    // 몬스터 설정
//    var mobs: ArrayList<Mob> = ArrayList()
//
//    // 폭발 이펙트 설정
//    var explosions: ArrayList<Explosion> = ArrayList()
//
//    // 점수 설정
    var points = 0
//
    // 번들
    lateinit var bundle : Bundle

    init {
        //화면 좌표 초기화
        val display = (getContext() as Activity).windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        dWidth = size.x
        dHeight = size.y
        rectBackground = Rect(0, 0, dWidth, dHeight)

//        gameLogic.gameViewHeight = dHeight
//        gameLogic.gameViewWidth = dWidth
        gameLogic.health = _health
        gameLogic.damage = _damage
        gameLogic.missileReload = _missileReload
        gameLogic.playerImage = _playerImage

//        // 배경화면 초기화
//        backgroundImage1 = BackgroundImage(dHeight)
//        backgroundImage2 = BackgroundImage(dHeight)
//
//        backgroundImage2.y = backgroundImage1.y - Bg1.height
//
        // Thread 초기화
        val holder = holder
        holder.addCallback(this)
        thread = GameThread(holder, gameLogic)
        threadShot = MissileThread(gameLogic)

        gameLogic = GameLogic(context,this)

//        gameLogic = GameLogic(context, _health, _damage, _missileReload, _playerImage, this)
//
//        // TEXT 초기화
//        textPaint.color = Color.rgb(255, 165, 0)
//        textPaint.textSize = TEXT_SIZE
//        textPaint.textAlign = Paint.Align.LEFT
//
//        // 목숨 초기화
//        healthPaint.color = Color.GREEN
//
//        // Player 초기화
//            // Player 위치 초기화
//        playerX = (dWidth / 2 - player.width / 2).toFloat()
//        playerY = (dHeight - player.height).toFloat()
//            // Player 이미지 초기화
//        when(playerImage){
//            1 -> player = BitmapFactory.decodeResource(resources, R.drawable.player1)
//            2 -> player = BitmapFactory.decodeResource(resources, R.drawable.player2)
//            3 -> player = BitmapFactory.decodeResource(resources, R.drawable.player3)
//        }
//
//        // 몬스터 배열 초기화
//        for (i in 0..7) {
//            val mob = Mob(context, dWidth)
//            mobs.add(mob)
//        }
    }

//    fun rand(from: Int, to: Int): Int {
//        val random = Random()
//        return random.nextInt(to - from) + from
//    }
//    var cnt = intArrayOf(1, 1, 1, 1, 1, 1, 1, 1) //방향 판단용 배열

    //mob 400, 400 기준
    //오른쪽벽 = mobs[i].mobX + mobs[i].mobWidth >= dWidth + 150
    //왼쪽벽 = mobs[i].mobX - mobs[i].mobWidth <= dWidth - 1600
//    fun circleAnim(j: Int): Int {
//        val a = IntArray(100)
//        for(i in 0..49) {
//            a[i] = rand(10, 30)
//        }
//        for(i in 50..99) {
//            a[i] = rand(-30, -10)
//        }
//        return a[j]
//    }

//    // 몬스터 움직임
//    fun animation(i: Int) {
//        if( i == 0 ) {
//            if(cnt[i] != 0) { //오른쪽으로
//                mobs[i].mobY += mobs[i].mobVelocity - 10
//                mobs[i].mobX += mobs[i].mobVelocity
//            }
//            if(mobs[i].mobX + mobs[i].mobWidth >= dWidth + 150 || cnt[i] == 0) { //왼쪽으로
//                mobs[i].mobX -= mobs[i].mobVelocity
//                mobs[i].mobY += mobs[i].mobVelocity - 10
//                cnt[i] = 0
//            }
//            if(mobs[i].mobX - mobs[i].mobWidth <= dWidth - 1600) { //왼쪽 벽 닿으면
//                cnt[i] = 1
//            }
//        }//벽 닿을때마다 방향전환
//        else if ( i == 1 ) {
//            if(cnt[i] != 0) { //오른쪽으로
//                mobs[i].mobY += mobs[i].mobVelocity - 10
//                mobs[i].mobX += mobs[i].mobVelocity
//            }
//            if(mobs[i].mobX + mobs[i].mobWidth >= dWidth + 150 || cnt[i] == 0) { //왼쪽으로
//                mobs[i].mobX -= mobs[i].mobVelocity
//                mobs[i].mobY += mobs[i].mobVelocity - 10
//                cnt[i] = 0
//            }
//            if(mobs[i].mobX - mobs[i].mobWidth <= dWidth - 1600) { //왼쪽 벽 닿으면
//                cnt[i] = 1
//            }
//        }
//        else if( i == 2 ) {
//            if(cnt[i] != 0) {
//                mobs[i].mobY += mobs[i].mobVelocity - 10
//                mobs[i].mobX -= mobs[i].mobVelocity
//            }
//            if(mobs[i].mobX - mobs[i].mobWidth <= dWidth - 1600 || cnt[i] == 0) {
//                mobs[i].mobX += mobs[i].mobVelocity
//                mobs[i].mobY += mobs[i].mobVelocity - 10
//                cnt[i]= 0
//            }
//            if(mobs[i].mobX + mobs[i].mobWidth >= dWidth + 150) {
//                cnt[i] = 1
//            }
//        }
//        else if( i == 3 ) {
//            if(cnt[i] != 0) {
//                mobs[i].mobY += mobs[i].mobVelocity - 10
//                mobs[i].mobX -= mobs[i].mobVelocity
//            }
//            if(mobs[i].mobX - mobs[i].mobWidth <= dWidth - 1600 || cnt[i] == 0) {
//                mobs[i].mobX += mobs[i].mobVelocity
//                mobs[i].mobY += mobs[i].mobVelocity - 10
//                cnt[i]= 0
//            }
//            if(mobs[i].mobX + mobs[i].mobWidth >= dWidth + 150) {
//                cnt[i] = 1
//            }
//        }
//        else if( i == 4 ) {
//            mobs[i].mobY += mobs[i].mobVelocity
//        }
//        else if( i == 5 ) {
//            mobs[i].mobY += mobs[i].mobVelocity
//        }
//        else if( i == 6 ){
//            mobs[i].mobY += mobs[i].mobVelocity
//        }
//        else {
//            mobs[i].mobY += mobs[i].mobVelocity
//        }
//    }

    // Thread 실행
    override fun surfaceCreated(p0: SurfaceHolder) {
        // 게임 그리기 쓰레드 실행
        thread.setRunning(true)
        thread.start()

        // 총알 발사 간격 쓰레드 실행
        threadShot.setRunning(true)
        threadShot.start()
    }

    // 게임 종료 후 GameoverFragment 넘어갈 작업
    override fun surfaceDestroyed(p0: SurfaceHolder) {
        var retry = true
        thread.setRunning(false)
        threadShot.setRunning(false)
        while (retry) {
            try {
                thread.interrupt()
                threadShot.interrupt()
                retry = false

//                bundle = bundleOf("score" to points, "health" to health, "playerImage" to playerImage)
                bundle = bundleOf("score" to points, "playerImage" to playerImage)
                controller.gameOver(bundle)

            } catch (e: java.lang.IllegalArgumentException) {
                println("쓰레드 꺼지면서 경고 메시지") // 오류메시지는 나오는데 실행에 문제는 없음
            }
        }
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
    }

//    // 미사일 그리기
//    fun drawMissile(canvas: Canvas) {
//        val missileImg = BitmapFactory.decodeResource(context.resources, R.drawable.missile)
//        val modifyMissileImg = Bitmap.createScaledBitmap(missileImg, 300, 300, false)
//
//        for (i in missiles.indices.reversed()) {
//            canvas.drawBitmap(
//                modifyMissileImg,
//                missiles[i].missileX,
//                missiles[i].missileY,
//                null
//            )
//
//            missiles[i].missileY -= missiles[i].missileSpeed
//        }
//
//        for (i in missiles.indices.reversed()) {       // 미사일이 화면 위쪽으로 사라지면 없애기
//            if (missiles[i].missileY < 0) {
//                missiles.removeAt(i)
//            }
//        }
//
//        for (i in mobs.indices.reversed()) {        // 총알과 적 충돌 계산
//            for (j in missiles.indices.reversed()) {
//                if (
//                    mobs[i].mobX + mobs[i].mobWidth/2  >= missiles[j].missileX - 50 &&
//                    mobs[i].mobX - mobs[i].mobWidth/2  <= missiles[j].missileX - 50 &&
//                    mobs[i].mobY + mobs[i].mobHeight/2 >= missiles[j].missileY) {
//
//                    mobs[i].mobHp -= damage
//                    missiles.removeAt(j)
//
//                    if (mobs[i].mobHp <= 0) {
//                        val explosion = Explosion(context)
//                        explosion.explosionX = mobs[i].mobX + 70
//                        explosion.explosionY = mobs[i].mobY
//                        explosions.add(explosion)
//
//                        var range = (1..5)      // 아이템 드랍 확률
//                        if (range.random() == 1) {
//                            hpItems.add(HpItem(context, explosion.explosionX, explosion.explosionY))
//                        }
//
//                        mobs[i].resetPosition()
//                        points += 10
//                    }
//                }
//            }
//        }
//    }

//    // 게임 그리기
//    fun drawGame(canvas: Canvas) {
//        drawBackground(canvas)
//
//        canvas.drawBitmap(player, playerX, playerY+100, null)
//
//        drawItems(canvas)
//
//        for (i in mobs.indices) {
//            mobs[i].getMob(mobs[i].mobFrame)?.let {
//                canvas.drawBitmap(
//                    it,
//                    mobs[i].mobX.toFloat(),
//                    mobs[i].mobY.toFloat(),
//                    null
//                )
//            }
//            mobs[i].mobFrame++
//            if (mobs[i].mobFrame > 2) {
//                mobs[i].mobFrame = 0
//            }
//
//            animation(i)  //여기서 내려오는 애니메이션
//
//            if (mobs[i].mobY + mobs[i].mobHeight >= dHeight + 200) {
//                mobs[i].resetPosition()
//                cnt[i] = 1 //사라지면 방향판단배열 초기화
//            }
//        }
//
//        for (i in mobs.indices) {
//            if (mobs[i].mobX + mobs[i].mobWidth >= playerX +270   // +할수록 왼쪽 판정 좋음
//                && mobs[i].mobX <= playerX + player.width -200     // -할수록 오른쪽 판정 좋음
//                && mobs[i].mobY + mobs[i].mobWidth >= playerY + 250) {
//                life -= 1
//                mobs[i].resetPosition()
//                if (life == 0) {
//                    surfaceDestroyed(holder)
//                }
//            }
//        }
//
//
//        drawExplosion(canvas)
//        if (life < health / 3 * 2 && life >= health / 3) {
//            healthPaint.color = Color.YELLOW
//        } else if (life < health / 3) {
//            healthPaint.color = Color.RED
//        }
//
//        canvas.drawRect(
//            (dWidth - 300).toFloat(),
//            30f,
//            ((dWidth - 300) + 200 * life.toFloat() / health.toFloat()),
//            80f,
//            healthPaint
//        )
//        canvas.drawText("" + points, 20f, TEXT_SIZE, textPaint)
//    }

//    // 폭발 효과 그리기
//    private fun drawExplosion(canvas: Canvas) {
//        for (i in explosions.indices) {
//            try {
//                explosions[i].getExplosion(explosions[i].explosionFrame)?.let {
//                    canvas.drawBitmap(
//                        it,
//                        explosions[i].explosionX.toFloat(),
//                        explosions[i].explosionY.toFloat(),
//                        null
//                    )
//                }
//                explosions[i].explosionFrame++
//                if (explosions[i].explosionFrame > 2) {
//                    explosions.removeAt(i)
//                }
//            } catch (e: IndexOutOfBoundsException) {
//                e.stackTrace
//            }
//        }
//    }
//
//    // 아이템 그리기
//    private fun drawItems(canvas: Canvas) {
//        for (i in hpItems.indices) {
//            hpItems[i].let {
//                canvas.drawBitmap(
//                    item,
//                    hpItems[i].itemX.toFloat() + 30,
//                    hpItems[i].itemY.toFloat(),
//                    null
//                )
//            }
//        }
//
//        // 아이템 움직임
//        for (i in hpItems.indices) {
//            hpItems[i].itemY += hpItems[i].itemSpeed
//        }
//
//        for (i in hpItems.indices.reversed()) {                          // 플레이어와 아이템 충돌
//            if (hpItems[i].itemX + hpItems[i].itemWidth - 100 >= playerX
//                && hpItems[i].itemX + 100 <= playerX + player.width
//                && hpItems[i].itemY + hpItems[i].itemHeight >= playerY + 100 ) {
//                if (life < health)
//                    life += 1
//                hpItems.removeAt(i)
//            }
//        }
//    }

//    // 배경화면 그리기
//    private fun drawBackground(canvas: Canvas) {
//        backgroundImage1.y = backgroundImage1.y + backgroundImage1.velocity
//        backgroundImage2.y = backgroundImage2.y + backgroundImage2.velocity
//
//        if (backgroundImage1.y > dHeight) {
//            backgroundImage1.y = -Bg1.height
//            backgroundCount1 += 1
//            if (backgroundCount1 == 1) {
//                backgroundCount1 = 0
//                bgNum += 1
//                if (bgNum == bgAry.size) {
//                    bgNum = 0
//                }
//                Bg1 = bgAry[bgNum]
//            }
//        }
//        if (backgroundImage2.y > dHeight) {
//            backgroundImage2.y = -Bg2.height
//            backgroundCount2 += 1
//            if (backgroundCount2 == 1) {
//                backgroundCount2 = 0
//                Bg2 = bgAry[bgNum]
//            }
//        }
//
//        canvas.drawBitmap(
//            Bg1,
//            backgroundImage1.x.toFloat(),
//            backgroundImage1.y.toFloat(),
//            null
//        )
//        canvas.drawBitmap(
//            Bg2,
//            backgroundImage2.x.toFloat(),
//            backgroundImage2.y.toFloat(),
//            null
//        )
//    }


//  [  // Player 터치로 이동
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        val touchX = event.x
//        val touchY = event.y
//        if (touchY >= playerY) {
//            val action = event.action
//            if (action == MotionEvent.ACTION_DOWN) {
//                oldX = event.x
//                oldPlayerX = playerX
//            }
//            if (action == MotionEvent.ACTION_MOVE) {
//                val shift = oldX - touchX
//                val newPlayerX = oldPlayerX - shift
//                playerX = if (newPlayerX <= -150f) {
//                    -150f
//                } else if (newPlayerX >= dWidth - player.width +100) {
//                    (dWidth - player.width + 100).toFloat()
//                } else newPlayerX
//            }
//        }
//        return true
//    }]



}