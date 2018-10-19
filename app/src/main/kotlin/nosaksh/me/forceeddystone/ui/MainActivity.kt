package nosaksh.me.forceeddystone.ui

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.activity_main.*
import nosaksh.me.forceeddystone.R
import nosaksh.me.forceeddystone.domain.Configuration
import nosaksh.me.forceeddystone.service.EddystoneCentralService
import nosaksh.me.forceeddystone.service.EddystonePeripheralService

class MainActivity : AppCompatActivity() {

    companion object {

        private const val MAX_BYTE_LEN_PHYSICAL_WEB_URL = 18

        private const val REQUEST_ENABLE_BLUETOOTH = 0x001

        private const val REQUEST_PERMISSIONS = 0x002

        fun intent(context: Context) = Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        eddystoneCentralSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startService(EddystoneCentralService.intent(this))
            } else {
                stopService(EddystoneCentralService.intent(this))
            }
            Configuration.saveBootEddystoneCentral(this@MainActivity, isChecked)
        }
        eddystoneCentralSwitch.isChecked = EddystoneCentralService.isRunning

        eddystoneCentralInputUrlEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length = s?.toString()?.toByteArray()?.size ?: 0
                eddystoneCentralinputUrlTextInputLayout.hint = s
                if (length > MAX_BYTE_LEN_PHYSICAL_WEB_URL) {
                    eddystoneCentralInputUrlEditText.error = getString(R.string.label_main_eddystone_central_url_max_error)
                } else {
                    eddystoneCentralInputUrlEditText.error = null
                }
            }
        })
        eddystoneCentralInputUrlEditText.setOnEditorActionListener { view, actionId, _ ->
            if (view.error != null) return@setOnEditorActionListener false
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    Configuration.savePhysicalWebUrl(this@MainActivity, view.text.toString())
                    if (eddystoneCentralSwitch.isChecked) {
                        startService(EddystoneCentralService.intent(this))
                    }
                }
                else -> Unit
            }
            return@setOnEditorActionListener false
        }
        eddystoneCentralInputUrlEditText.setText(Configuration.getPhysicalWebUrl(this))


        eddystonePeripheralSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startService(EddystonePeripheralService.intent(this))
            } else {
                stopService(EddystonePeripheralService.intent(this))
            }
            Configuration.saveBootEddystonePeripheral(this@MainActivity, isChecked)
        }
        eddystonePeripheralSwitch.isChecked = EddystonePeripheralService.isRunning

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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.contains(PackageManager.PERMISSION_DENIED)) {
            finish()
        }
    }

    private fun configureBluetooth() {
        if (BluetoothAdapter.getDefaultAdapter()?.isEnabled != true) {
            startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BLUETOOTH)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_PERMISSIONS)
        }
    }
}
