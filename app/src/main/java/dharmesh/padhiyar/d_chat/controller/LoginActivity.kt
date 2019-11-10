package dharmesh.padhiyar.d_chat.controller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dharmesh.padhiyar.d_chat.R
import dharmesh.padhiyar.d_chat.service.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginSpinner.visibility = View.GONE
    }

    fun onLoginClicked(view: View) {
        val email = loginEmail.text.toString()
        val password = loginPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            enableSpinner(true)
            hideKeyboard()
            AuthService.loginUser(this, email, password) { isLoggedIn ->
                if (isLoggedIn) {
                    AuthService.findUserByEmail(this) { isUserFound ->
                        if (isUserFound) {
                            finish()
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    errorToast()
                }
            }
        } else {
            Toast.makeText(this, "Some field is missing!", Toast.LENGTH_SHORT).show()
        }
    }

    fun onCreateUserClicked(view: View) {
        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }

    fun errorToast() {
        Toast.makeText(this, "Something went wrong, try again.", Toast.LENGTH_SHORT).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean) {
        if (enable) {
            loginSpinner.visibility = View.VISIBLE
        } else {
            loginSpinner.visibility = View.GONE
        }

        loginLoginButton.isEnabled = !enable
        loginCreateUser.isEnabled = !enable
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }
}
