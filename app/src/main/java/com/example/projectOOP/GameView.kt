package com.example.projectOOP

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.projectOOP.rank.Rank

import com.example.projectOOP.thread.GameThread
import com.example.projectOOP.thread.MissileThread


// Thread 구동 및 화면 좌표 생성.
class GameView(context: Context, _health: Int, _damage: Int, _missileReload: Int, _playerImage: Int, private val controller: GameOverController) : SurfaceView(context), SurfaceHolder.Callback {


    private var gameLogic: GameLogic

//    var playerImage = _playerImage

    private val _playerXY = MutableLiveData<List<Float>>()
    val playerXY: LiveData<List<Float>> get() = _playerXY

    // 점수 설정
    private var playerX: Float
    private var playerY: Float
    var oldX = 0f
    var oldPlayerX = 0f

    //화면 좌표 설정
    private var rectBackground: Rect
    var dWidth = 0
    var dHeight = 0

    // Thread 설정
    private val thread: GameThread
    private val threadShot: MissileThread

    // 번들
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
        gameLogic = GameLogic(context, _health, _damage, _missileReload, _playerImage, this)

        // Thread 초기화
        val holder = holder
        holder.addCallback(this)
        thread = GameThread(holder, gameLogic)
        threadShot = MissileThread(gameLogic)

        playerX = (dWidth / 2 - gameLogic.player.width / 2).toFloat()
        playerY = (dHeight - gameLogic.player.height).toFloat()

        _playerXY.value = arrayListOf(playerX, playerY)

    }

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

//                bundle = bundleOf("score" to points, "playerImage" to playerImage)
                bundle = bundleOf("score" to gameLogic.points, "playerImage" to gameLogic.playerImage)
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
        gameLogic.playerX = playerX
        gameLogic.playerY = playerY

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
                } else if (newPlayerX >= dWidth - gameLogic.player.width +100) {
                    (dWidth - gameLogic.player.width + 100).toFloat()
                } else newPlayerX
            }
        }
        return true
    }

}