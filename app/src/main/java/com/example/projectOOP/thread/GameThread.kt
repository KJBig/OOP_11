package com.example.projectOOP.thread

import android.graphics.Canvas
import android.graphics.Color
import android.view.SurfaceHolder
import com.example.projectOOP.GameLogic

class GameThread(private val mSurfaceHolder: SurfaceHolder, private val gameLogic: GameLogic) : Thread() {
    private var runCondition = false

    override fun run() {
        while (runCondition) {
            var threadCanvas : Canvas? = null
            try {
                threadCanvas = mSurfaceHolder.lockCanvas(null) // 캔버스 잠금 후, 내부 버퍼에 그린다
                threadCanvas.drawColor(Color.BLACK)            // 잔상 지우기
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
                println("쓰레드 꺼짐") // 쓰레드 끄고나서도 한번 실행돼서 오류 메시지 (일단은 try로 막아놓음)
            }
        }
    }

    fun setRunning(state: Boolean) {
        runCondition = state
    }
}