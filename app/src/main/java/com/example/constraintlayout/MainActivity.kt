package com.example.constraintlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import java.text.DecimalFormat
import java.util.*

class MainActivity : AppCompatActivity(), TextWatcher, TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var edtConta: EditText
    private lateinit var edtPessoas: EditText
    private var ttsSuccess: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtConta = findViewById(R.id.edtConta)
        edtConta.addTextChangedListener(this)
        edtConta.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                calculateResult()
            }
        }

        edtPessoas = findViewById(R.id.editPessoas)
        edtPessoas.addTextChangedListener(this)
        edtPessoas.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                calculateResult()
            }
        }

        // Initialize TTS engine
        tts = TextToSpeech(this, this)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        Log.d("PDM24", "Antes de mudar")
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d("PDM24", "Mudando")
    }

    override fun afterTextChanged(s: Editable?) {
        Log.d("PDM24", "Depois de mudar")
        // Calculation is now handled in onFocusChange listeners
    }

    private fun calculateResult() {
        val valor: Double
        val divisor: Double
        if (edtConta.text.toString().isNotEmpty() && edtPessoas.text.toString().isNotEmpty()) {
            if (edtConta.text.toString() == "0" || edtPessoas.text.toString() == "0") {
                tts.speak(
                    "O número de pessoas ou o valor da conta está nulo, por favor altere",
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    null
                )
            } else {
                valor = edtConta.text.toString().toDouble()
                divisor = edtPessoas.text.toString().toDouble()
                val result = valor / divisor
                val decimalFormat = DecimalFormat("#.##")
                val roundedResult = decimalFormat.format(result)
                tts.speak("O valor para cada ficou: $roundedResult", TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }

    fun clickFalar(v: View) {
        if (tts.isSpeaking) {
            tts.stop()
        }
        if (ttsSuccess) {
            Log.d("PDM23", tts.language.toString())
            tts.speak("Oi Sumido", TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onDestroy() {
        // Release TTS engine resources
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // TTS engine is initialized successfully
            tts.language = Locale.getDefault()
            ttsSuccess = true
            Log.d("PDM23", "Sucesso na Inicialização")
        } else {
            // TTS engine failed to initialize
            Log.e("PDM23", "Failed to initialize TTS engine.")
            ttsSuccess = false
        }
    }
}
