package com.baishakhee.biometricathounticationdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import android.widget.Toast
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    lateinit var editTextPin: EditText
    lateinit var btnPinVerify: Button
    private lateinit var btnBiometric: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextPin = findViewById(R.id.editTextPin)
        btnPinVerify = findViewById(R.id.btnPinVerify)
        btnBiometric = findViewById(R.id.btnAuthenticate)

        // Handle PIN verification
        btnPinVerify.setOnClickListener {
            val enteredPin = editTextPin.text.toString()
            if (enteredPin == "123456") { // Replace with actual PIN verification logic
                Toast.makeText(this, "PIN Verified", Toast.LENGTH_SHORT).show()
                // Proceed to next screen or functionality
            } else {
                Toast.makeText(this, "Incorrect PIN", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle biometric authentication (supports both fingerprint and face)
        btnBiometric.setOnClickListener {
            checkBiometricAvailability()
        }
    }

    fun checkBiometricAvailability() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                showBiometricPrompt()
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(this, "No biometric hardware available", Toast.LENGTH_SHORT).show()
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(this, "Biometric hardware is currently unavailable", Toast.LENGTH_SHORT).show()
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(this, "No biometric data enrolled. Please enroll fingerprint or face data.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun showBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)

        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext, "Authentication succeeded!", Toast.LENGTH_SHORT).show()
                    // Proceed to the next activity/screen
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Authenticate using fingerprint or face recognition")
            .setDescription("Scan your fingerprint or face to proceed")
            .setNegativeButtonText("Cancel")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    fun onAuthenticationSucceeded(authResult: BiometricPrompt.AuthenticationResult?) {
        // Handle the successful authentication result
        Toast.makeText(this, "Authentication succeeded!", Toast.LENGTH_SHORT).show()

        // You can also extract details from authResult if needed
        // Proceed to the next screen or functionality
        // For example, navigate to the home screen or unlock sensitive features
    }

    fun onAuthenticationFailed() {
        // Handle the failed authentication
        Toast.makeText(this, "Authentication failed. Please try again.", Toast.LENGTH_SHORT).show()

        // Optionally, you could track the number of failed attempts or provide additional feedback
    }

}

