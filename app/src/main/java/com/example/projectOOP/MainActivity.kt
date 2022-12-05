package com.example.projectOOP

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setFullScreen()   // 전체화면용
    }

    private fun setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {   // 안드로이드 11, API 30 이상
            supportActionBar?.hide()    // 화면 상단에 제목같은 걸 나타내는 toolbar 숨기기

            window.setDecorFitsSystemWindows(false)     // 최상단 레이아웃을 전체 화면으로 설정

            // API 30 이후로는 systemUIVisibility 대신 windowInsetsController를 만들어서 사용해야 한다
            val controller = window.insetsController
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() or   // status Bar, Navigation Bar 숨기기
                WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE  // 스와이프 해야 시스템바가 나타나도록
            }
        }
    }
}