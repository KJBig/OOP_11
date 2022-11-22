package com.example.projectOOP.fragment

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.createScaledBitmap
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.projectOOP.*
import com.example.projectOOP.databinding.FragmentGameBinding
import kotlinx.coroutines.Runnable
import java.util.*
import kotlin.collections.ArrayList

class GameFragment : Fragment() {

    var binding : FragmentGameBinding? = null
    var health = 0
    var damage = 0
    var playerImage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            health = it.getInt("health")  // 레디 프래그먼트에서 보낸 체력 값
            damage = it.getInt("damage")
            playerImage = it.getInt("playerImage")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {



            private val bgAry = arrayOf<Bitmap>(BitmapFactory.decodeResource(resources, R.drawable.bg_spring),
                BitmapFactory.decodeResource(resources, R.drawable.bg_sea),
                BitmapFactory.decodeResource(resources, R.drawable.bg_fall),
                BitmapFactory.decodeResource(resources, R.drawable.bg_winter),
                BitmapFactory.decodeResource(resources, R.drawable.bg_high),
                BitmapFactory.decodeResource(resources, R.drawable.bg_desert))


            private var backgroundImage1: BackgroundImage
            private var backgroundImage2: BackgroundImage
            private var count1 =0
            private var count2 =0
            private var bgNum = 0
            private var Bg1 =  bgAry[0]
            private var Bg2 =  bgAry[bgNum]


            var player: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.player1)

            var rectBackground: Rect
            var dWidth = 0
            var dHeight = 0
            var TEXT_SIZE = 120f


            val thread: MyThread
            val threadShot: MyThreadShot


            var textPaint = Paint()
            var healthPaint = Paint()

            var points = 0
            var life = health
            var playerX: Float
            var playerY: Float
            var oldX = 0f
            var oldPlayerX = 0f
            var mobs: ArrayList<Mob>
            var explosions: ArrayList<Explosion>

            var missiles: ArrayList<Missile>
            var missileReload:Long

            lateinit var bundle : Bundle


            init {


                    backgroundImage1 = BackgroundImage(dHeight)
                    backgroundImage2 = BackgroundImage(dHeight)

                    backgroundImage2.y = backgroundImage1.y - Bg1.height

                    val display = (getContext() as Activity).windowManager.defaultDisplay
                    val size = Point()
                    display.getSize(size)

                    dWidth = size.x
                    dHeight = size.y
                    rectBackground = Rect(0, 0, dWidth, dHeight)

                    val holder = holder
                    holder.addCallback(this)
                    thread = MyThread(holder)
                    threadShot = MyThreadShot()

                    textPaint.color = Color.rgb(255, 165, 0)
                    textPaint.textSize = TEXT_SIZE
                    textPaint.textAlign = Paint.Align.LEFT
                    healthPaint.color = Color.GREEN

                    playerX = (dWidth / 2 - player.width / 2).toFloat()
                    playerY = (dHeight - player.height).toFloat()

                    when(playerImage){
                        1 -> player = BitmapFactory.decodeResource(resources, R.drawable.player1)
                        2 -> player = BitmapFactory.decodeResource(resources, R.drawable.player2)
                        3 -> player = BitmapFactory.decodeResource(resources, R.drawable.player3)
                    }

                    explosions = ArrayList()
                    mobs = ArrayList()
                    for (i in 0..7) {
                        val mob = Mob(context, dWidth)
                        mobs.add(mob)
                    }

                    missileReload = 250
                    missiles = ArrayList()
                    val missile = Missile(context, playerX, playerY)
                    missiles.add(missile)

            }

            fun rand(from: Int, to: Int): Int {
                val random = Random()
                return random.nextInt(to - from) + from
            }
            var cnt = intArrayOf(1, 1, 1, 1, 1, 1, 1, 1) //방향 판단용 배열

            //mob 400, 400 기준
            //오른쪽벽 = mobs[i].mobX + mobs[i].mobWidth >= dWidth + 150
            //왼쪽벽 = mobs[i].mobX - mobs[i].mobWidth <= dWidth - 1600
            fun circleAnim(j: Int): Int {
                val a = IntArray(100)
                for(i in 0..49) {
                    a[i] = rand(10, 30)
                }
                for(i in 50..99) {
                    a[i] = rand(-30, -10)
                }
                return a[j]
            }

            fun animation(i: Int) {
                if( i == 0 ) {
                    if(cnt[i] != 0) { //오른쪽으로
                        mobs[i].mobY += mobs[i].mobVelocity - 10
                        mobs[i].mobX += mobs[i].mobVelocity
                    }
                    if(mobs[i].mobX + mobs[i].mobWidth >= dWidth + 150 || cnt[i] == 0) { //왼쪽으로
                        mobs[i].mobX -= mobs[i].mobVelocity
                        mobs[i].mobY += mobs[i].mobVelocity - 10
                        cnt[i] = 0
                    }
                    if(mobs[i].mobX - mobs[i].mobWidth <= dWidth - 1600) { //왼쪽 벽 닿으면
                        cnt[i] = 1
                    }
                }//벽 닿을때마다 방향전환
                else if ( i == 1 ) {
                    if(cnt[i] != 0) { //오른쪽으로
                        mobs[i].mobY += mobs[i].mobVelocity - 10
                        mobs[i].mobX += mobs[i].mobVelocity
                    }
                    if(mobs[i].mobX + mobs[i].mobWidth >= dWidth + 150 || cnt[i] == 0) { //왼쪽으로
                        mobs[i].mobX -= mobs[i].mobVelocity
                        mobs[i].mobY += mobs[i].mobVelocity - 10
                        cnt[i] = 0
                    }
                    if(mobs[i].mobX - mobs[i].mobWidth <= dWidth - 1600) { //왼쪽 벽 닿으면
                        cnt[i] = 1
                    }
                }
                else if( i == 2 ) {
                    if(cnt[i] != 0) {
                        mobs[i].mobY += mobs[i].mobVelocity - 10
                        mobs[i].mobX -= mobs[i].mobVelocity
                    }
                    if(mobs[i].mobX - mobs[i].mobWidth <= dWidth - 1600 || cnt[i] == 0) {
                        mobs[i].mobX += mobs[i].mobVelocity
                        mobs[i].mobY += mobs[i].mobVelocity - 10
                        cnt[i]= 0
                    }
                    if(mobs[i].mobX + mobs[i].mobWidth >= dWidth + 150) {
                        cnt[i] = 1
                    }
                }
                else if( i == 3 ) {
                    if(cnt[i] != 0) {
                        mobs[i].mobY += mobs[i].mobVelocity - 10
                        mobs[i].mobX -= mobs[i].mobVelocity
                    }
                    if(mobs[i].mobX - mobs[i].mobWidth <= dWidth - 1600 || cnt[i] == 0) {
                        mobs[i].mobX += mobs[i].mobVelocity
                        mobs[i].mobY += mobs[i].mobVelocity - 10
                        cnt[i]= 0
                    }
                    if(mobs[i].mobX + mobs[i].mobWidth >= dWidth + 150) {
                        cnt[i] = 1
                    }
                }
                else if( i == 4 ) {
                    mobs[i].mobY += mobs[i].mobVelocity
                }
                else if( i == 5 ) {
                    mobs[i].mobY += mobs[i].mobVelocity
                }
                else if( i == 6 ){
                    mobs[i].mobY += mobs[i].mobVelocity
                }
                else {
                    mobs[i].mobY += mobs[i].mobVelocity
                }
            }

            override fun surfaceCreated(p0: SurfaceHolder) {
                thread.setRunning(true)     // 게임 그리기 쓰레드 실행
                thread.start()

                threadShot.setRunning(true)  // 총알 발사 간격 쓰레드 실행
                threadShot.start()
            }

            override fun surfaceDestroyed(p0: SurfaceHolder) {
                var retry = true
                thread.setRunning(false)
                threadShot.setRunning(false)
                while (retry) {
                    try {
                        thread.interrupt()
                        threadShot.interrupt()
                        retry = false

                        bundle = bundleOf("score" to points, "health" to health)
                        requireActivity().runOnUiThread {
                            findNavController().navigate(R.id.action_gameFragment_to_gameoverFragment, bundle)
                        }
                    } catch (e: java.lang.IllegalArgumentException) {
                        println("쓰레드 꺼지면서 경고 메시지") // 오류메시지는 나오는데 실행에 문제는 없음
                    }
                }
            }

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
            }

            inner class MyThread(private val mSurfaceHolder: SurfaceHolder) : Thread() {
                private var mRun = false

                override fun run() {
                    while (mRun) {
                        var c : Canvas? = null
                        try {
                            c = mSurfaceHolder.lockCanvas(null) // 캔버스 잠금 후, 내부 버퍼에 그린다
                            c.drawColor(Color.BLACK)            // 잔상 지우기
                            synchronized(mSurfaceHolder) {      // sync 안에 있는 코드가 전부 실행되기 전까지 다른 쓰레드 간섭불가
                                drawGame(c)
                                drawMissile(c)
                            }
                        } finally {
                            if (c != null) {
                                mSurfaceHolder.unlockCanvasAndPost(c)  // 캔버스의 잠금을 풀고, view에 옮긴다
                            }
                        }
                        try {
                            sleep(20)
                        } catch (e : InterruptedException) {
                            println("쓰레드 꺼짐") // 쓰레드 끄고나서도 한번 실행돼서 오류 메시지 (일단은 try로 막아놓음)
                        }
                    }
                }

                fun setRunning(b: Boolean) {
                    mRun = b
                }
            }

            inner class MyThreadShot() : Thread() {
                private var mRun = false

                override fun run() {
                    while (mRun) {
                        try {
                            missiles.add(Missile(context, playerX, playerY))
                            sleep(missileReload)
                        } catch (e : InterruptedException) {
                            println("쓰레드 꺼짐")
                        }
                    }
                }
                fun setRunning(b: Boolean) {
                    mRun = b
                }
            }

            fun drawMissile(canvas: Canvas) {
                val missileImg = BitmapFactory.decodeResource(context.resources, R.drawable.missile)
                val modifyMobImg = createScaledBitmap(missileImg, 300, 300, false)

                for (i in missiles.indices.reversed()) {
                    canvas.drawBitmap(
                        modifyMobImg,
                        missiles[i].missileX,
                        missiles[i].missileY,
                        null
                    )

                    missiles[i].missileY -= missiles[i].missileSpeed
                }

                for (i in missiles.indices.reversed()) {       // 미사일이 화면 위쪽으로 사라지면 없애기
                    if (missiles[i].missileY < 0) {
                        missiles.removeAt(i)
                    }
                }

                for (i in mobs.indices.reversed()) {        // 총알과 적 충돌 계산
                    for (j in missiles.indices.reversed()) {
                        if (
                            mobs[i].mobX + mobs[i].mobWidth/2  >= missiles[j].missileX - 50 &&
                            mobs[i].mobX - mobs[i].mobWidth/2  <= missiles[j].missileX - 50 &&
                            mobs[i].mobY + mobs[i].mobHeight/2 >= missiles[j].missileY) {

                            val explosion = Explosion(context)
                            explosion.explosionX = mobs[i].mobX + 70
                            explosion.explosionY = mobs[i].mobY
                            explosions.add(explosion)

                            mobs[i].resetPosition()
                            missiles.removeAt(j)
                            points += 10
                        }
                    }
                }
            }

            fun drawGame(canvas: Canvas) {
                backgroundImage1.y = backgroundImage1.y + backgroundImage1.velocity
                backgroundImage2.y = backgroundImage2.y + backgroundImage2.velocity

                if (backgroundImage1.y > dHeight) {
                    backgroundImage1.y = -Bg1.height
                    count1 += 1
                    if(count1 == 1){
                        count1 = 0
                        bgNum += 1
                        if(bgNum == bgAry.size){
                            bgNum=0
                        }
                        Bg1 = bgAry[bgNum]
                    }
                }
                if (backgroundImage2.y > dHeight) {
                    backgroundImage2.y = -Bg2.height
                    count2 += 1
                    if(count2 == 1){
                        count2 = 0
                        Bg2 = bgAry[bgNum]
                    }
                }

                canvas.drawBitmap(
                    Bg1,
                    backgroundImage1.x.toFloat(),
                    backgroundImage1.y.toFloat(),
                    null
                )
                canvas.drawBitmap(
                    Bg2,
                    backgroundImage2.x.toFloat(),
                    backgroundImage2.y.toFloat(),
                    null
                )

                canvas.drawBitmap(player, playerX, playerY+100, null)

                for (i in mobs.indices) {
                    mobs[i].getMob(mobs[i].mobFrame)?.let {
                        canvas.drawBitmap(
                            it,
                            mobs[i].mobX.toFloat(),
                            mobs[i].mobY.toFloat(),
                            null
                        )
                    }
                    mobs[i].mobFrame++
                    if (mobs[i].mobFrame > 2) {
                        mobs[i].mobFrame = 0
                    }

                    animation(i)  //여기서 내려오는 애니메이션

                    if (mobs[i].mobY + mobs[i].mobHeight >= dHeight + 200) {
                        mobs[i].resetPosition()
                        cnt[i] = 1 //사라지면 방향판단배열 초기화
                    }
                }

                for (i in mobs.indices) {
                    if (mobs[i].mobX + mobs[i].mobWidth >= playerX +270   // +할수록 왼쪽 판정 좋음
                        && mobs[i].mobX <= playerX + player.width -120     // -할수록 오른쪽 판정 좋음
                        && mobs[i].mobY + mobs[i].mobWidth >= playerY + 250) {
                        life--
                        mobs[i].resetPosition()
                        if (life == 0) {
                            surfaceDestroyed(holder)
                        }
                    }
                }
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
                    } catch (e : java.lang.IndexOutOfBoundsException) {
                        e.stackTrace
                    }
                }
                if (life < health / 3 * 2 && life >= health / 3) {
                    healthPaint.color = Color.YELLOW
                } else if (life < health / 3) {
                    healthPaint.color = Color.RED
                }

                canvas.drawRect(
                    (dWidth - 300).toFloat(),
                    30f,
                    ((dWidth - 300) + 200 * life.toFloat() / health.toFloat()),
                    80f,
                    healthPaint
                )
                canvas.drawText("" + points, 20f, TEXT_SIZE, textPaint)
            }

            override fun onTouchEvent(event: MotionEvent): Boolean {
                val touchX = event.x
                val touchY = event.y
                if (touchY >= playerY) {
                    val action = event.action
                    if (action == MotionEvent.ACTION_DOWN) {
                        oldX = event.x
                        oldPlayerX = playerX
                    }
                    if (action == MotionEvent.ACTION_MOVE) {
                        val shift = oldX - touchX
                        val newPlayerX = oldPlayerX - shift
                        playerX = if (newPlayerX <= -150f) {
                            -150f
                        } else if (newPlayerX >= dWidth - player.width +100) {
                            (dWidth - player.width + 100).toFloat()
                        } else newPlayerX
                    }
                }
                return true
            }
        }
        return context?.let { GameView(it) }  // GameView 시작
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }
}