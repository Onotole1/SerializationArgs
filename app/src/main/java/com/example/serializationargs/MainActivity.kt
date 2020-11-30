package com.example.serializationargs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.example.serializationargs.TestFragment.Companion.arg

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.container, TestFragment().apply {
                    arguments = Bundle().apply {
                        arg = FeedArguments(
                            0L,
                            "testContent"
                        )
                    }
                })
            }
        }
    }
}
