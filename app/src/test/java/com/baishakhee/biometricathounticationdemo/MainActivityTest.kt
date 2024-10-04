package com.baishakhee.biometricathounticationdemo

import android.widget.EditText
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class MainActivityTest {

    @Mock
    private lateinit var mainActivity: MainActivity

    @Mock
    private lateinit var biometricManager: BiometricManager

    @Mock
    private lateinit var biometricPrompt: BiometricPrompt

    @Mock
    private lateinit var editTextPin: EditText

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mainActivity = mock(MainActivity::class.java)
        biometricManager = mock(BiometricManager::class.java)
        biometricPrompt = mock(BiometricPrompt::class.java)
        editTextPin = mock(EditText::class.java)
    }

    @Test
    fun testPinVerification_CorrectPin() {
        // Mock the EditText to return a valid PIN
        `when`(editTextPin.text.toString()).thenReturn("123456")

        // Simulate button click
        mainActivity.editTextPin = editTextPin
        mainActivity.btnPinVerify.performClick()

        // Verify that the PIN verification succeeds
        verify(mainActivity, times(1)).showToast("PIN Verified")
    }

    @Test
    fun testPinVerification_IncorrectPin() {
        // Mock the EditText to return an invalid PIN
        `when`(editTextPin.text.toString()).thenReturn("654321")

        // Simulate button click
        mainActivity.editTextPin = editTextPin
        mainActivity.btnPinVerify.performClick()

        // Verify that the PIN verification fails
        verify(mainActivity, times(1)).showToast("Incorrect PIN")
    }

    @Test
    fun testBiometricAvailability_BiometricSuccess() {
        // Mock BiometricManager to return BIOMETRIC_SUCCESS
        `when`(biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG))
            .thenReturn(BiometricManager.BIOMETRIC_SUCCESS)

        // Simulate biometric availability check
        mainActivity.checkBiometricAvailability()

        // Verify that biometric prompt is shown
        verify(mainActivity, times(1)).showBiometricPrompt()
    }

    @Test
    fun testBiometricAvailability_NoHardware() {
        // Mock BiometricManager to return BIOMETRIC_ERROR_NO_HARDWARE
        `when`(biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG))
            .thenReturn(BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE)

        // Simulate biometric availability check
        mainActivity.checkBiometricAvailability()

        // Verify that a toast is shown for no hardware
        verify(mainActivity, times(1)).showToast("No biometric hardware available")
    }

    @Test
    fun testBiometricAvailability_HardwareUnavailable() {
        // Mock BiometricManager to return BIOMETRIC_ERROR_HW_UNAVAILABLE
        `when`(biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG))
            .thenReturn(BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE)

        // Simulate biometric availability check
        mainActivity.checkBiometricAvailability()

        // Verify that a toast is shown for hardware being unavailable
        verify(mainActivity, times(1)).showToast("Biometric hardware is currently unavailable")
    }

    @Test
    fun testBiometricAuthentication_Success() {
        // Simulate a successful biometric authentication
        val authResult = mock(BiometricPrompt.AuthenticationResult::class.java)

        mainActivity.showBiometricPrompt()
        mainActivity.onAuthenticationSucceeded(authResult)

        // Verify that the success toast is shown
        verify(mainActivity, times(1)).showToast("Authentication succeeded!")
    }

    @Test
    fun testBiometricAuthentication_Failure() {
        // Simulate a failed biometric authentication
        mainActivity.showBiometricPrompt()
        mainActivity.onAuthenticationFailed()

        // Verify that the failure toast is shown
        verify(mainActivity, times(1)).showToast("Authentication failed")
    }

    // Helper method to mock Toast showing
    private fun MainActivity.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
