package com.example.projectOOP.fragment

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.projectOOP.*
import com.example.projectOOP.databinding.FragmentGameBinding
import kotlinx.coroutines.Runnable
import java.util.*

class GameFragment : Fragment() {
    var binding : FragmentGameBinding? = null
    var health = 0

    companion object {
        @JvmField
        var dWidth: Int = 0
        var dHeight: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            health = it.getInt("health")  // 레디 프래그먼트에서 보낸 체력 값
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


            private val backgroundImage1: BackgroundImage = BackgroundImage()
            private val backgroundImage2: BackgroundImage = BackgroundImage()
            private var count1 =0
            private var count2 =0
            private var bgNum = 0
            private var Bg1 =  bgAry[0]
            private var Bg2 =  bgAry[bgNum]


            var background: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.background)
            var ground: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ground)
            var player: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.player0)

            var rectBackground: Rect
            var rectGround: Rect

            var TEXT_SIZE = 120f

            var runnable: java.lang.Runnable
            var random: Random
            val thread: MyThread

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

            lateinit var bundle : Bundle

            init {
                backgroundImage2.y = backgroundImage1.y - Bg1.height

                val display = (getContext() as Activity).windowManager.defaultDisplay
                val size = Point()
                display.getSize(size)

                dWidth = size.x
                dHeight = size.y
                rectBackground = Rect(0, 0, dWidth, dHeight)
                rectGround = Rect(0, dHeight - ground.height, dWidth, dHeight)

                runnable = Runnable { invalidate() }

                val holder = holder
                holder.addCallback(this)
                thread = MyThread(holder)

                textPaint.color = Color.rgb(255, 165, 0)
                textPaint.textSize = TEXT_SIZE
                textPaint.textAlign = Paint.Align.LEFT
                healthPaint.color = Color.GREEN

                random = Random()

                playerX = (dWidth / 2 - player.width / 2).toFloat()
                playerY = (dHeight - ground.height - player.height).toFloat()

                explosions = ArrayList()
                mobs = ArrayList()
                for (i in 0..2) {
                    val mob = Mob(context)
                    mobs.add(mob)
                }
            }

            override fun surfaceCreated(p0: SurfaceHolder) {
                thread.setRunning(true)
                thread.start()
            }

            override fun surfaceDestroyed(p0: SurfaceHolder) {
                var retry = true
                thread.setRunning(false)
                while (retry) {
                    try {
                        thread.interrupt()
                        retry = false

                        bundle = bundleOf("score" to points, "health" to health)
                        requireActivity().runOnUiThread {
                            findNavController().navigate(R.id.action_gameFragment_to_gameoverFragment, bundle)
                        }
                    } catch (e: java.lang.IllegalArgumentException) {
                        println("여기 오류 왜 나옴??") // 오류메시지는 나오는데 실행에 문제는 없음
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
                            synchronized(mSurfaceHolder) {
                                drawGame(c)
                            }
                        } finally {
                            if (c != null) {
                                mSurfaceHolder.unlockCanvasAndPost(c)  // 캔버스의 잠금을 풀고, view에 옮긴다
                            }
                        }
                        try {
                            sleep(20)  // 게임 속도조절인데 쓰레드 끄고나서도 한번 실행돼서 오류 발생 (일단은 try로 막아놓음)
                            // delay 써볼래? (철기교수님)
                        } catch (e : InterruptedException) {
                            println("쓰레드 꺼짐")
                        }
                    }
                }

                fun setRunning(b: Boolean) {
                    mRun = b
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


//                canvas.drawBitmap(background, null, rectBackground, null)
//                canvas.drawBitmap(ground, null, rectGround, null)
                canvas.drawBitmap(player, playerX+50, playerY+100, null)

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
                    mobs[i].mobY += mobs[i].mobVelocity
                    if (mobs[i].mobY + mobs[i].mobHeight >= dHeight + 200) {
                        points += 10
                        val explosion = Explosion(context)
                        explosion.explosionX = mobs[i].mobX + 70
                        explosion.explosionY = mobs[i].mobY
                        explosions.add(explosion)
                        mobs[i].resetPosition()
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
                for (i in explosions.indices) {  // 배열 범위 초과로 튕김현상 발생
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
                        if (explosions[i].explosionFrame > 3) {
                            explosions.removeAt(i)
                        }
                    } catch (e : java.lang.IndexOutOfBoundsException) {
                        e.stackTrace
                    }
                }
                if (life == 2) {
                    healthPaint.color = Color.YELLOW
                } else if (life == 1) {
                    healthPaint.color = Color.RED
                }
                canvas.drawRect(
                    (dWidth - 200).toFloat(),
                    30f,
                    (dWidth - 200 + 60 * life).toFloat(),
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
                        } else if (newPlayerX >= dWidth - player.width + 50) {
                            (dWidth - player.width + 50).toFloat()
                        } else newPlayerX
                    }
                }
                return true
            }
        }
        return context?.let { GameView(it) } // GameView 시작
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }
}