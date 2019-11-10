package dharmesh.padhiyar.d_chat.controller

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dharmesh.padhiyar.d_chat.R
import dharmesh.padhiyar.d_chat.service.AuthService
import dharmesh.padhiyar.d_chat.utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        createSpinner.visibility = View.GONE
    }

    fun onUserAvatarClicked(view: View) {
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)

        if (color == 0) {
            userAvatar = "light$avatar"
        } else {
            userAvatar = "dark$avatar"
        }

        val resId = resources.getIdentifier(userAvatar, "drawable", packageName)
        createAvatar.setImageResource(resId)
    }

    fun onGenerateBackgroundClicked(view: View) {
        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)

        createAvatar.setBackgroundColor(Color.rgb(r, g, b))

        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255

        avatarColor = "[$savedR, $savedG, $savedB, 1]"
    }

    fun onCreateUserClicked(view: View) {

        val username = createUserName.text.toString()
        val email = createEmail.text.toString()
        val password = createPassword.text.toString()

        if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
            enableSpinner(true)

            AuthService.registerUser(this, email, password) { isRegistered ->
                if (isRegistered) {
                    AuthService.loginUser(this, email, password) { isLoggedIn ->
                        if (isLoggedIn) {
                            AuthService.createUser(
                                this,
                                username,
                                email,
                                userAvatar,
                                avatarColor
                            ) { isUserCreated ->
                                if (isUserCreated) {
                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this)
                                        .sendBroadcast(userDataChange)
                                    enableSpinner(false)
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
                    errorToast()
                }
            }
        } else {
            Toast.makeText(this, "Some field is missing!", Toast.LENGTH_SHORT).show()
        }
    }

    fun errorToast() {
        Toast.makeText(this, "Something went wrong, try again.", Toast.LENGTH_SHORT).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean) {
        if (enable) {
            createSpinner.visibility = View.VISIBLE
        } else {
            createSpinner.visibility = View.GONE
        }

        createCreateUser.isEnabled = !enable
        createAvatar.isEnabled = !enable
        createGenerateButton.isEnabled = !enable
    }
}
