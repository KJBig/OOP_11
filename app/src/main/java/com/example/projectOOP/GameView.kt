package com.example.projectOOP

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager
import android.view.WindowMetrics
import androidx.core.os.bundleOf
import com.example.projectOOP.player.Player

import com.example.projectOOP.thread.GameThread
import com.example.projectOOP.thread.MissileThread

class GameView(context: Context, val player: Player, private val controller: GameOverController)
                                                : SurfaceView(context), SurfaceHolder.Callback {

    // 플레이어의 위치 초기화
    var oldX = 0f
    var oldPlayerX = 0f

    //화면 좌표 설정
    private var rectBackground: Rect
    var dWidth = 0
    var dHeight = 0

    // Thread 설정
    private val thread: GameThread
    private val threadShot: MissileThread

    // 게임 로직 설정
    private var gameLogic: GameLogic

    private lateinit var bundle : Bundle

    init {
        // 화면 좌표 초기화
        // Activity를 기준으로 화면의 크기를 구해온다.
        val display = (getContext() as Activity).applicationContext.resources.displayMetrics
        val deviceWidth = display.widthPixels
        val deviceHeight = display.heightPixels

        dWidth = deviceWidth
        dHeight = deviceHeight
        rectBackground = Rect(0, 0, dWidth, dHeight)

        //GameLogic 초기화
        gameLogic = GameLogic(context, player, this)

        // holder는 곧 SurfaceView 클래스에 있는 메소드 getHolder를 의미한다.
        val holder = holder     // 홀더를 만들고
        holder.addCallback(this)    // 홀더안에 addCallback을 호출하여 SurfaceHolder.Callback으로부터 이벤트를 수신하도록 해야한다.

        // Thread 초기화
        thread = GameThread(holder, gameLogic)  // addCallback없이 그냥 holder를 넣으면 오류 발생
        threadShot = MissileThread(gameLogic)   // MissileThread는 뷰에 그려넣는 기능없이 일정시간마다 총알배열에 추가하는 기능만 하므로 SurfaceHolder가 필요없다
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        // 게임 쓰레드 실행
        thread.setRunning(true)     // GameThread의 while문 조건을 true로 바꿔준다
        thread.start()              // GameThread의 run() 메소드 실행

        // 미사일 쓰레드 실행
        threadShot.setRunning(true)
        threadShot.start()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        thread.setRunning(false)        // GameThread의 while문 조건을 false로 바꿔준다
        threadShot.setRunning(false)    // while문을 벗어나서 코드부터 멈추고 나서, 밑에서 쓰레드를 끈다

        // interrupt()는 Thread가 sleep() 메소드를 만나서 일시정지 된 순간 interruptedException을 발생시키며 while문을 빠져나와 run()메소드를 정상 종료하게 해준다.
        thread.interrupt()
        threadShot.interrupt()

        // 게임 결과를 Bundle로 GameOverFragment에 전달
        bundle = bundleOf("score" to gameLogic.points, "playerImage" to player.playerImage)

        // 인터페이스를 통해 GameFragment에 gameOver() 호출
        controller.gameOver(bundle)
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
    }

    // Player 터치로 이동
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        if (touchY >= gameLogic.playerY) {              // 터치한 y좌표가 플레이어의 y좌표보다 아래에 있을 경우
            val action = event.action                   // MotionEvent의 action은 DOWN, MOVE, UP 등의 구분자역할을 한다
            if (action == MotionEvent.ACTION_DOWN) {    // ACTION_DOWN은 누른 순간을 말한다.
                oldX = event.x                          // oldX에 지금 누른 x좌표 값을 넣어준다
                oldPlayerX = gameLogic.playerX          // oldPlayerX에 누른 순간 현재 플레이어 위치를 넣어준다
            }
            if (action == MotionEvent.ACTION_MOVE) {    // ACTION_MOVE는 누른 채로 움직였을 떄를 말한다
                val shift = oldX - touchX               // 누른 순간의 x좌표와 누른 채로 움직이고 있는 현재의 x좌표를 빼준다
                val newPlayerX = oldPlayerX - shift     // 누른채로 움직인 거리만큼을 이전 플레이어 값에서 변화시켜 새 플레이어 좌표를 지정해준다.
                gameLogic.playerX =                     // 만약 화면 왼쪽과 오른쪽 끝을 벗어나면 값이 넘어가더라도 그 한계치 이하로 계속 갱신시킨다
                if (newPlayerX <= -150f) {
                    -150f
                } else if (newPlayerX >= dWidth - 280f) {
                    dWidth - 280f
                } else newPlayerX
            }
        }
        return true
    }
}