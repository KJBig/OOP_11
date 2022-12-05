package com.example.projectOOP.thread

import android.util.Log
import com.example.projectOOP.GameLogic
import com.example.projectOOP.viewobject.Missile

class MissileThread(private val gameLogic: GameLogic) : Thread() {
    private var runCondition = false

    override fun run() {
            while (runCondition) {
                try {
                    // 플레이어의 좌표값을 넘겨서 그 위치값을 갖는 미사일을 배열에 추가한다.
                    // GameThread와 달리 그림을 그릴 필요가 없기에 surfaceHolder를 받지 않는다.
                    gameLogic.missiles.add(Missile(gameLogic.context, gameLogic.playerX, gameLogic.playerY))
                    sleep(gameLogic.missileReload.toLong())     // 플레이어 클래스로부터 얻어온 연사력 값만큼 쓰레드를 멈췄다가 실행한다.
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