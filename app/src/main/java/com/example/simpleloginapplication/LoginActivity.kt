package com.example.simpleloginapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.PasswordAuthentication

private lateinit var txtRegisterActivity: TextView
private lateinit var etUser: TextView
private lateinit var etPassword: TextView
private lateinit var btnLogin: Button

private lateinit var user:String
private lateinit var password:String
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin = findViewById(R.id.btnLogin)
        etUser = findViewById(R.id.etUserName)
        etPassword = findViewById(R.id.etPassword)

        btnLogin.setOnClickListener {
            user = etUser.text.toString()
            password = etPassword.text.toString()
            login(etUser as EditText, etPassword as EditText);
            Toast.makeText(this, "successful", Toast.LENGTH_SHORT).show()
        }

        this.findViewById<TextView>(R.id.tvRegisterLink).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }

    fun login(Name:EditText,Pass:EditText) {
        val userName: String = Name.getText().toString().trim()
        val password: String = Pass.getText().toString().trim()

        val call: Call<ResponseBody> = RetrofitClient
            .getInstance()
            .api
            .checkUser(User(userName, password))

        if (userName.isEmpty()) {
            Name.setError("Username is required")
            Name.requestFocus()
            return
        } else if (password.isEmpty()) {
            Pass.setError("Password is required")
            Pass.requestFocus()
            return
        }

        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>) {
                var s = ""
                try {
                    s = response.body()!!.string()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (s == userName) {
                    val intent = Intent(this@LoginActivity,DashboardActivity::class.java)
                    intent.putExtra("Username",s)
                    Toast.makeText(

                        this@LoginActivity,
                        "Successfully login",
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(intent)
                } else {
                    Toast.makeText(this@LoginActivity, "User does not exists!", Toast.LENGTH_LONG)
                        .show()
                }


            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_LONG).show()
            }


        })
    }
}