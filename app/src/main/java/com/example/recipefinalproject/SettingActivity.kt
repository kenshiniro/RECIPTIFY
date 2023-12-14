package com.example.recipefinalproject

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.recipefinalproject.databinding.ActivitySettingBinding
import com.google.firebase.auth.FirebaseAuth

/**
 * SettingActivity class
 * Our Design for the Setting Activity page is in activity_setting.xml
 * It's ready now, let's go to the next step
 * Need to add java code to this class
 * let's test our code
 * Works pretty well
 * In our next Video We will add Favorties Functionality
 * Thank you for watching this video
 * Happy Coding
 */
class SettingActivity : AppCompatActivity() {
    var binding: ActivitySettingBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        binding!!.linearLayoutShare.setOnClickListener { view: View? -> shareApp() }
        binding!!.linearLayoutRate.setOnClickListener { view: View? -> rateApp() }
        binding!!.linearLayoutFeedback.setOnClickListener { view: View? -> sendFeedback() }
        binding!!.linearLayoutApps.setOnClickListener { view: View? -> moreApps() }
        binding!!.linearLayoutPrivacy.setOnClickListener { view: View? -> privacyPolicy() }
        binding!!.btnSignout.setOnClickListener { view: View? -> signOut() }
    }

    private fun signOut() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            return
        }
        AlertDialog.Builder(this)
            .setTitle("Sign Out")
            .setMessage("Are you sure you want to sign out?")
            .setPositiveButton("Sign Out") { dialogInterface: DialogInterface?, i: Int ->
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@SettingActivity, LoginActivity::class.java))
                finishAffinity()
            }
            .setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
            .show()
    }

    private fun privacyPolicy() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://www.google.com")
        startActivity(intent)
    }

    private fun sendFeedback() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.developer_email)))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for " + getString(R.string.app_name))
        intent.putExtra(Intent.EXTRA_TEXT, "Hi " + getString(R.string.developer_name) + ",")
        startActivity(Intent.createChooser(intent, "Send Feedback"))
    }

    private fun moreApps() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data =
            Uri.parse("https://play.google.com/store/apps/developer?id=" + getString(R.string.developer_id))
        startActivity(intent)
    }

    private fun rateApp() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data =
            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        startActivity(intent)
    }

    private fun shareApp() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Wallcraft")
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Get " + getString(R.string.app_name) + " to get the best wallpapers for your phone: https://play.google.com/store/apps/details?id=" + packageName
        )
        startActivity(Intent.createChooser(intent, "Share App"))
    }
}