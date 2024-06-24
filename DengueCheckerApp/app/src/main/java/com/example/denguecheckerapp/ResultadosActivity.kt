package com.example.dengueapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.denguecheckerapp.R

class ResultadosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultados)

        // Recebendo dados do Intent
        val nomePaciente = intent.getStringExtra("nome")
        val idadePaciente = intent.getStringExtra("idade")
        val estadoPaciente = intent.getStringExtra("estado")
        val diagnosticadoComDengue = intent.getBooleanExtra("diagnosticoDengue", false)

        // Atualizando informações do paciente no layout
        val infoPaciente = findViewById<TextView>(R.id.infoPaciente)
        infoPaciente.text = "$nomePaciente, $idadePaciente anos, reside em $estadoPaciente"

        // Atualizando diagnóstico com base no resultado
        val infoDiagnostico = findViewById<TextView>(R.id.infoDiagnostico)
        infoDiagnostico.text = if (diagnosticadoComDengue) {
            "Positivo para dengue. Para amenizar sintomas como dor e febre, pode-se usar medicamentos como paracetamol e dipirona. Mantenha-se hidratado."
        } else {
            "Negativo para dengue. Cuide-se."
        }

        // Botão "Análise"
        val botaoAnalise: Button = findViewById(R.id.botaoAnalise)
        botaoAnalise.setOnClickListener {
            // Iniciar a atividade AnaliseActivity ao clicar no botão
            val intent = Intent(this, AnaliseActivity::class.java)
            startActivity(intent)
        }
    }
}
