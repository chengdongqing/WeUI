package top.chengdongqing.weui.ui.views.hardware

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.form.WeButton

@Composable
fun FingerprintPage() {
    WePage(title = "Fingerprint", description = "指纹认证") {
        val context = LocalContext.current

        WeButton(text = "指纹认证") {
            context.startActivity(BiometricActivity.newIntent(context))
        }
    }
}

class BiometricActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("指纹认证")
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .setNegativeButtonText("取消认证")
            .build()
        createBiometricPrompt(this).authenticate(promptInfo)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, BiometricActivity::class.java)
    }
}

private fun createBiometricPrompt(activity: FragmentActivity): BiometricPrompt {
    val callback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            activity.finish()
            Toast.makeText(activity, "此设备不支持", Toast.LENGTH_SHORT).show()
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            Toast.makeText(activity, "认证失败", Toast.LENGTH_SHORT).show()
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            activity.setResult(Activity.RESULT_OK)
            activity.finish()
            Toast.makeText(activity, "认证成功", Toast.LENGTH_SHORT).show()
        }
    }
    return BiometricPrompt(activity, callback)
}