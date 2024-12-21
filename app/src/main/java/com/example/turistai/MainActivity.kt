package com.example.turistai

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Variáveis de Trabalho
        var frase: String = ""
        var pais: String = ""

        //Variéveis de local:
        var etLocal: EditText
        etLocal = findViewById(R.id.etLocal)
        var local: String   //armazenar o local digitado
//=======================Tratamento da lista dePaíses
        //Variáveis de listaPaises:
        var paises = arrayOf("Brasil", "Argentina", "Chile", "Uruguai", "Paraguai")
        var listaPaises: Spinner
        listaPaises = findViewById(R.id.spinner)
        //O spinner precisa receber um adapter para que ele possa exibir os dados
        var adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paises)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        listaPaises.adapter = adapter
        listaPaises.setAdapter(adapter)
        listaPaises.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //retorna a posição do item selecionado
                pais = paises[position]
                Toast.makeText(this@MainActivity, "Pais Selecionado: $pais", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        //======================Tratamento da data inicial e final
        var etDataIni: EditText
        var etDataFim: EditText
        var dataini: String
        var datafim: String
 //       var dataAux: String
        var cal: Calendar
  //      var datePickerDialog: DatePickerDialog
        etDataIni = findViewById(R.id.etDataIni)    //etDataIni é o id do objeto Data Inicial
        etDataFim = findViewById(R.id.etDataFim)    //etDataFim é o id do objeto Data Final
        cal = Calendar.getInstance()                 //cal é o objeto Calendar
        etDataIni.setOnClickListener {

            showDatePickerDialog(this, etDataIni, cal)
        }
        etDataFim.setOnClickListener {
            showDatePickerDialog(this, etDataFim, cal)
        }


        var txtSaida: TextView
        txtSaida = findViewById(R.id.txtRetorno)

//======================Tratamento do botão Pesquisar
        var btOk: Button
        btOk = findViewById(R.id.btOk)
        btOk.setOnClickListener {
/*            local = etLocal.text.toString()
            frase="Favor elaborar um roteiro turístico para o local $local , no país $pais"
            Toast.makeText(this, frase, Toast.LENGTH_SHORT).show()*/
            dataini = etDataIni.text.toString()
            datafim = etDataFim.text.toString()
            local = etLocal.text.toString()
            frase =
                "Favor elaborar um roteiro turístico para o local $local, no país $pais entre $dataini e $datafim"
            Toast.makeText(this, frase, Toast.LENGTH_SHORT).show()
            modelcall(txtSaida, frase)
        }
        var fabLixo: View
        fabLixo = findViewById(R.id.fabLixo)
        fabLixo.setOnClickListener {
            etLocal.setText("")
            etDataIni.setText("")
            etDataFim.setText("")
            txtSaida.setText("")
        }
    }

    fun showDatePickerDialog(context: Context, editText: EditText, cal: Calendar) {
        val ano = cal.get(Calendar.YEAR)
        val mes = cal.get(Calendar.MONTH)
        val dia = cal.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            context,
            { view, ano, mes, dia ->
                val dataSelecionada = "$dia/${mes + 1}/$ano"
                editText.setText(dataSelecionada)
                Toast.makeText(context, dataSelecionada, Toast.LENGTH_SHORT).show()
            }, ano, mes, dia
        )
        datePickerDialog.show()

    }

    fun modelcall(txtSaida: TextView, frase: String) {
        var generativeModel =
            GenerativeModel(
                // Specify a Gemini model appropriate for your use case
                modelName = "gemini-1.5-flash",
                // Access your API key as a Build Configuration variable (see "Set up your API key" above)
                apiKey = "AIzaSyB3te0Ggf33T8wq6pMRc8GmVPAGZB8ul4w"
            )
        val prompt = frase
        MainScope().launch {
            val response = generativeModel.generateContent(prompt)
            txtSaida.setText(response.text)
        }
    }
}
