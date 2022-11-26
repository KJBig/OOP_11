package com.example.projectOOP.thread

import com.example.projectOOP.GameLogic
import com.example.projectOOP.GameView
import com.example.projectOOP.viewobject.Missile


class MissileThread(private val gameLogic: GameLogic) : Thread() {
    private var runCondition = false

    override fun run() {
//            while (runCondition) {
//                try {
//                   gameLogic.missiles.add(Missile(gameView.context, gameView.playerX, gameView.playerY))
//                    sleep(gameView.missileReload.toLong())
//                } catch (e : InterruptedException) {
//                    println("쓰레드 꺼짐")
//                }
//            }
    }
    fun setRunning(b: Boolean) {
        runCondition = b
    }
}