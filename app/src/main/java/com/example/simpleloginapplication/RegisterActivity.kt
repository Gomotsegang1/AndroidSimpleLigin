package com.example.simpleloginapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

private lateinit var etUserName: EditText
private lateinit var etPassword: EditText
private lateinit var btnReg: Button

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etUserName = findViewById(R.id.etRUserName)
        etPassword = findViewById(R.id.etRPassword)
        btnReg = findViewById(R.id.btnRegister)

        this.findViewById<TextView>(R.id.tvLoginLink).setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

        btnReg.setOnClickListener {
            //Toast.makeText(this, "successful ", Toast.LENGTH_SHORT).show()
            registerUser()
        }
    }
    private fun registerUser()
    {
        val userName: String = etUserName.getText().toString().trim()
        val password: String = etPassword.getText().toString().trim()

        if (userName.isEmpty()) {
            etUserName.setError("Username is required")
            etUserName.requestFocus()
            return
        }
        if (password.isEmpty()) {
            etPassword.setError("Password is required")
            etPassword.requestFocus()
            return
        }

        val call: Call<ResponseBody> = RetrofitClient
            .getInstance()
            .api
            .createUser(User(userName, password))
        call.enqueue(object : Callback<ResponseBody?>
        {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                var s = ""
                try {

                    s = response.body()!!.string()

                    if (s == "SUCCESS") {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Successfully registered. Please login",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    } else {
                        Toast.makeText(this@RegisterActivity, "User already exists!", Toast.LENGTH_LONG)
                            .show()
                    }


            } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}