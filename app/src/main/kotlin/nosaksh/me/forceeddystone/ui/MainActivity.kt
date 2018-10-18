package nosaksh.me.forceeddystone.ui

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.activity_main.*
import nosaksh.me.forceeddystone.R
import nosaksh.me.forceeddystone.domain.Configuration
import nosaksh.me.forceeddystone.service.EddystoneCentralService

class MainActivity : AppCompatActivity() {

    companion object {

        private const val MAX_BYTE_LEN_PHYSICAL_WEB_URL = 18

        private const val REQUEST_ENABLE_BLUETOOTH = 0x001

        fun intent(context: Context) = Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        eddystoneSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startService(EddystoneCentralService.intent(this))
            } else {
                stopService(EddystoneCentralService.intent(this))
            }
            Configuration.saveBootEddystone(this@MainActivity, isChecked)
        }
        eddystoneSwitch.isChecked = EddystoneCentralService.isRunning

        inputUrlEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length = s?.toString()?.toByteArray()?.size ?: 0
                inputUrlTextInputLayout.hint = s
                if (length > MAX_BYTE_LEN_PHYSICAL_WEB_URL) {
                    inputUrlEditText.error = getString(R.string.label_main_physical_web_url_max_error)
                } else {
                    inputUrlEditText.error = null
                }
            }
        })
        inputUrlEditText.setOnEditorActionListener { view, actionId, _ ->
            if (view.error != null) return@setOnEditorActionListener false
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    Configuration.savePhysicalWebUrl(this@MainActivity, view.text.toString())
                    if (eddystoneSwitch.isChecked) {
                        startService(EddystoneCentralService.intent(this))
                    }
                }
                else -> Unit
            }
            return@setOnEditorActionListener false
        }
        inputUrlEditText.setText(Configuration.getPhysicalWebUrl(this))

        configureBluetooth()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_ENABLE_BLUETOOTH -> {
                if (resultCode != Activity.RESULT_OK) {
                    finish()
                }
            }
            else -> Unit
        }
    }

    private fun configureBluetooth() {
        if (BluetoothAdapter.getDefaultAdapter()?.isEnabled != true) {
            startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BLUETOOTH)
        }
    }
}
