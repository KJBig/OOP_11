package com.example.projectOOP.thread

import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import android.view.SurfaceHolder
import com.example.projectOOP.GameLogic

class GameThread(private val mSurfaceHolder: SurfaceHolder, private val gameLogic: GameLogic) : Thread() {
    private var runCondition = false

    override fun run() {
        while (runCondition) {
            var threadCanvas : Canvas? = null
            try {
                threadCanvas = mSurfaceHolder.lockCanvas(null) // 캔버스 잠금 후, 내부 버퍼 Surface에 게임화면을 그린다
                gameLogic.drawGame(threadCanvas)
                gameLogic.drawMissile(threadCanvas)

            } finally {
                if (threadCanvas != null) {
                    mSurfaceHolder.unlockCanvasAndPost(threadCanvas)  // 캔버스의 잠금을 풀고, view에 옮긴다
                }
            }
            try {
                sleep(20)
            } catch (e : InterruptedException) {
                // GameView의 interrupt 메소드 실행 시 호출되는 InterruptedException을 처리해준다
                Log.d("thread", "쓰레드 꺼짐")
            }
        }
    }

    fun setRunning(state: Boolean) {
        runCondition = state
    }
}