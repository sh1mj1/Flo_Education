package com.example.flo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.example.flo.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignupTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.btnLoginTv.setOnClickListener { login()
            startMainActivity()
        }


    }

    private fun login(){
        if (binding.loginIdEt.text.toString().isEmpty() || binding.loginMailEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.loginPasswordEt.text.toString().isEmpty()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        val email: String =
            binding.loginIdEt.text.toString() + "@" + binding.loginMailEt.text.toString()
        val pwd: String = binding.loginPasswordEt.text.toString()
        // 해당 email과 pwd를 가진 user의 data를 DB에서 가져오기
        val songDB = SongDatabase.getInstance(this)!!
        val user = songDB.userDao().getUser(email, pwd)

        // user 정보가 없을 때만 실행. let문은 :
        user?.let{
            Log.d("LOGIN/GET_USER","userId: ${user.id},     $user")
            // 발급받은 jwt를 저장해주는 함수
            saveJwt(user.id)
        }
        Toast.makeText(this,"회원정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
    }

    // 로그인 완료시
    private fun startMainActivity(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
    // User jwt(USERIDX)를 SharedPreferences 에 저장해주는 함수
    private fun saveJwt(jwt: Int){
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putInt("jwt",jwt)
        editor.apply()
    }
}