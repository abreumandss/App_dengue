package com.example.dengueapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.denguecheckerapp.DatabaseHelper
import com.example.denguecheckerapp.R

class AnaliseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analise)

        // Referências para os componentes do layout
        val totalDengue: TextView = findViewById(R.id.totalDengue)
        val regiaoDengue: LinearLayout = findViewById(R.id.regiaoDengue)

        // Inicializar mapa para contabilizar casos por estado
        val casosPorEstado = mutableMapOf<String, Int>()

        // Obter dados dos pacientes do banco de dados
        val dbHelper = DatabaseHelper(this)
        val pacientes = dbHelper.listarPacientes()

        // Verificar e contabilizar diagnósticos positivos para dengue
        for (paciente in pacientes) {
            if (paciente["diagnostico"] == "Dengue positivo") {
                val estado = paciente["regiao"]
                if (estado != null) {
                    val casosAtuais = casosPorEstado.getOrDefault(estado, 0)
                    casosPorEstado[estado] = casosAtuais + 1
                }
            }
        }

        // Atualizar a UI com os dados
        var totalCasos = 0
        for ((estado, casos) in casosPorEstado) {
            totalCasos += casos
        }

        totalDengue.text = "Total de pacientes diagnosticados com dengue: $totalCasos"

        // Limpar o conteúdo anterior e preencher dinamicamente as regiões com casos de dengue
        regiaoDengue.removeAllViews()
        for ((estado, casos) in casosPorEstado) {
            val textView = TextView(this)
            textView.text = "Região: $estado, Casos de dengue: $casos"
            textView.setTextColor(resources.getColor(android.R.color.black))
            textView.textSize = 18f
            textView.setPadding(0, 10, 0, 10)
            regiaoDengue.addView(textView)
        }

        val botaoVoltar: Button = findViewById(R.id.botaoVoltar)
        botaoVoltar.setOnClickListener {
            // Iniciar a atividade MainActivity ao clicar no botão
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
