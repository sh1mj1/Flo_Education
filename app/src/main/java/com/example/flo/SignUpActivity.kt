package com.example.flo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.flo.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignupCompleteTv.setOnClickListener { signUp()
        finish()}
    }

    // 사용자가 입력한 값을 가져오는 함수
    private fun getUser(): User {
        val email: String =
            binding.loginIdEt.text.toString() + "@" + binding.loginMailEt.text.toString()
        val pwd: String = binding.loginPasswordEt.text.toString()

        return User(email, pwd)
    }

    private fun signUp() {
        // 모든 칸에 입력되어 있어야, 비번, 비번확인란이 같아야 : Validation 처리
        if (binding.loginIdEt.text.toString().isEmpty() || binding.loginMailEt.text.toString().isEmpty()
        ) {
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.loginPasswordEt.text.toString() != binding.loginPasswordCheckEt.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        // 위 if문을 통과하면 DB에 email과 pwd 저장
        val userDB = SongDatabase.getInstance(this)!!
        userDB.userDao().insert(getUser())
        val users = userDB.userDao().getUsers()

        Log.d("SIGNUP ACTIVITY", users.toString())
    }
}