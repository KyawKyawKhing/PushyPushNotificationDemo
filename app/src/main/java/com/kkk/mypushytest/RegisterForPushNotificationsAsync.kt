package com.kkk.mypushytest

import android.app.Activity
import android.os.AsyncTask
import android.util.Log
import me.pushy.sdk.Pushy
import java.net.URL

class RegisterForPushNotificationsAsync(activity: Activity) : AsyncTask<Void, Void, Any>() {
    var activity: Activity = activity;

    override fun doInBackground(vararg params: Void): Any {
        try {
            // Register the device for notifications
            val deviceToken = Pushy.register(activity)
            Pushy.subscribe("news", activity)

            // Registration succeeded, log token to logcat
            Log.d("Pushy", "Pushy device token: " + deviceToken)

            // Send the token to your backend server via an HTTP GET request
            URL("https://{YOUR_API_HOSTNAME}/register/device?token=" + deviceToken).openConnection()

            // Provide token to onPostExecute()
            return deviceToken
        } catch (exc: Exception) {
            // Registration failed, provide exception to onPostExecute()
            return exc
        }
    }

    override fun onPostExecute(result: Any) {
        var message: String

        // Registration failed?
        if (result is Exception) {
            // Log to console
            Log.e("Pushy", result.message)

            // Display error in alert
            message = result.message.toString()
        }
        else {
            // Registration success, result is device token
            message = "Pushy device token: " + result.toString() + "\n\n(copy from logcat)"
        }

        // Display dialog
        android.app.AlertDialog.Builder(activity)
            .setTitle("Pushy")
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }
}