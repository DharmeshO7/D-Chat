package dharmesh.padhiyar.d_chat.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.ui.AppBarConfiguration
import dharmesh.padhiyar.d_chat.R
import dharmesh.padhiyar.d_chat.service.AuthService
import dharmesh.padhiyar.d_chat.service.UserDataService
import dharmesh.padhiyar.d_chat.utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.nav_app_bar_open_drawer_description,
            R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        LocalBroadcastManager.getInstance(this).registerReceiver(
            userDataChangeReceiver, IntentFilter(
                BROADCAST_USER_DATA_CHANGE
            )
        )
    }

    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (AuthService.isLoggedIn) {
                userName.text = UserDataService.name
                userEmail.text = UserDataService.email
                val resId =
                    resources.getIdentifier(UserDataService.avatarName, "drawable", packageName)
                userImage.setImageResource(resId)
                userImage.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))
                loginLoginButton.text = "Logout"
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    fun onLoginClicked(view: View) {
        if (AuthService.isLoggedIn) {
            //logout
            UserDataService.logout()
            userName.text = ""
            userEmail.text = ""
            userImage.setImageResource(R.drawable.profiledefault)
            userImage.setBackgroundColor(Color.TRANSPARENT)
            loginLoginButton.text = "Login"
        } else {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    fun onAddChannelClicked(view: View) {

    }

    fun onSendMessageClicked(view: View) {

    }
}
