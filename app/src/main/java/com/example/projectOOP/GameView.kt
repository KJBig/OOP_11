package com.example.projectOOP

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
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
        //화면 좌표 초기화
        val display = (getContext() as Activity).windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        dWidth = size.x
        dHeight = size.y
        rectBackground = Rect(0, 0, dWidth, dHeight)

        //GameLogic 초기화
        gameLogic = GameLogic(context, player, this)

        // Thread 초기화
        val holder = holder
        holder.addCallback(this)
        thread = GameThread(holder, gameLogic)
        threadShot = MissileThread(gameLogic)

    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        // 게임 쓰레드 실행
        thread.setRunning(true)
        thread.start()

        // 미사일 쓰레드 실행
        threadShot.setRunning(true)
        threadShot.start()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        // ***이 작업을 왜 하는건가?***
        var retry = true
        thread.setRunning(false)
        threadShot.setRunning(false)
        while (retry) {
            try {
                thread.interrupt()
                threadShot.interrupt()
                retry = false

                // 게임 결과를 Bundle로 GameOverFragment에 전달
                bundle = bundleOf("score" to gameLogic.points, "playerImage" to player.playerImage)
                // 인터페이스를 통해 GameFragment에 gameOver() 호출
                controller.gameOver(bundle)

            } catch (e: java.lang.IllegalArgumentException) {
                println("쓰레드 꺼지면서 경고 메시지") // 오류메시지는 나오는데 실행에 문제는 없음
            }
        }
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
    }

    // Player 터치로 이동
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        if (touchY >= gameLogic.playerY) {//
            val action = event.action
            if (action == MotionEvent.ACTION_DOWN) {
                oldX = event.x
                oldPlayerX = gameLogic.playerX //
            }
            if (action == MotionEvent.ACTION_MOVE) {
                val shift = oldX - touchX
                val newPlayerX = oldPlayerX - shift
                gameLogic.playerX = if (newPlayerX <= -150f) {
                    -150f
                } else if (newPlayerX >= dWidth - gameLogic.playerImage.width +100) {
                    (dWidth - gameLogic.playerImage.width + 100).toFloat()
                } else newPlayerX
            }
        }
        return true
    }

}