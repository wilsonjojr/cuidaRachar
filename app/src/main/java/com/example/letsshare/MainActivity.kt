package com.example.letsshare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import java.text.DecimalFormat
import java.util.Locale


class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var inputMoney: EditText
    private lateinit var inputPeoples: EditText
    private lateinit var textViewResult:  TextView
    private lateinit var btnListen: Button
    private lateinit var btnShare: Button
    private lateinit var textSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputMoney = findViewById(R.id.etMoney)
        inputPeoples = findViewById(R.id.etPeoples)
        textViewResult = findViewById(R.id.tvResult)

        textSpeech = TextToSpeech(this, this)
        btnListen = findViewById(R.id.btnListen)
        btnShare = findViewById(R.id.btnShare)

        inputMoney.addTextChangedListener(TextChangeListener())
        inputPeoples.addTextChangedListener(TextChangeListener())

        // share
        btnShare.setOnClickListener(View.OnClickListener {
            val moneyStr = inputMoney.text.toString()
            val peoplesStr = inputPeoples.text.toString()

            if (moneyStr.isNotEmpty() && peoplesStr.isNotEmpty()) {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Ficou ${textViewResult.text} para você :3")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        })

        // speaker
        btnListen.setOnClickListener(View.OnClickListener {
            val moneyStr = inputMoney.text.toString()
            val peoplesStr = inputPeoples.text.toString()

            if (peoplesStr.isNotEmpty() && moneyStr.isNotEmpty()) {
                speakResult("Ficou ${textViewResult.text} para cada um... Cuida em pagar logo!")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        textSpeech.shutdown()
        textSpeech.stop()
    }

    private fun speakResult(text: String) {
        textSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val locale = Locale.getDefault()
            val result = textSpeech.setLanguage(locale)
        }
    }

    inner class TextChangeListener: TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            val moneyStr = inputMoney.text.toString()
            val peoplesStr = inputPeoples.text.toString()
            val df = DecimalFormat("#.00")

            if (moneyStr.isNotEmpty() && peoplesStr.isNotEmpty()) {
                val money = moneyStr.toDouble()
                val peoples = peoplesStr.toDouble()
                val resultText = df.format(money / peoples)

                Log.d("Text", "money: $money, qtdPeople: $peoples")
                textViewResult.text = "R$$resultText"
            }

        }
    }

    private fun showAlert(message: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Aviso")
        alertDialogBuilder.setMessage(message)

        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }



}