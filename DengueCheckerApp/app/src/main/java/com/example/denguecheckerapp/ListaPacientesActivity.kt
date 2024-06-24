package com.example.dengueapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.denguecheckerapp.DatabaseHelper
import com.example.denguecheckerapp.R

class ListaPacientesActivity : AppCompatActivity() {

    private lateinit var tablePacientes: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_pacientes)

        tablePacientes = findViewById(R.id.tablePacientes)

        carregarPacientes()

        // Configurar botão para voltar à MainActivity
        val botaoVoltar: Button = findViewById(R.id.botaoVoltar)
        botaoVoltar.setOnClickListener {
            finish()
        }
    }

    private fun carregarPacientes() {
        // Limpar a tabela antes de adicionar novos dados
        tablePacientes.removeViews(1, tablePacientes.childCount - 1)

        // Carregar dados dos pacientes do banco de dados
        val dbHelper = DatabaseHelper(this)
        val pacientes = dbHelper.listarPacientes()

        // Mapa de siglas de estados
        val estadoSiglas = mapOf(
            "Acre" to "AC",
            "Alagoas" to "AL",
            "Amapá" to "AP",
            "Amazonas" to "AM",
            "Bahia" to "BA",
            "Ceará" to "CE",
            "Distrito Federal" to "DF",
            "Espírito Santo" to "ES",
            "Goiás" to "GO",
            "Maranhão" to "MA",
            "Mato Grosso" to "MT",
            "Mato Grosso do Sul" to "MS",
            "Minas Gerais" to "MG",
            "Pará" to "PA",
            "Paraíba" to "PB",
            "Paraná" to "PR",
            "Pernambuco" to "PE",
            "Piauí" to "PI",
            "Rio de Janeiro" to "RJ",
            "Rio Grande do Norte" to "RN",
            "Rio Grande do Sul" to "RS",
            "Rondônia" to "RO",
            "Roraima" to "RR",
            "Santa Catarina" to "SC",
            "São Paulo" to "SP",
            "Sergipe" to "SE",
            "Tocantins" to "TO"
        )

        // Preencher a tabela com os dados dos pacientes
        for (paciente in pacientes) {
            val tableRow = TableRow(this)

            val textViewNome = TextView(this)
            textViewNome.text = paciente["nome"]
            textViewNome.setPadding(5, 5, 5, 5)
            tableRow.addView(textViewNome)

            val textViewEstado = TextView(this)
            val estadoCompleto = paciente["regiao"]
            textViewEstado.text = estadoSiglas[estadoCompleto] ?: estadoCompleto // Usa a sigla ou o nome completo se a sigla não for encontrada

            textViewEstado.setPadding(5, 5, 5, 5)
            tableRow.addView(textViewEstado)

            val textViewDiagnostico = TextView(this)
            textViewDiagnostico.text = paciente["diagnostico"]
            textViewDiagnostico.setPadding(5, 5, 5, 5)
            tableRow.addView(textViewDiagnostico)

            val buttonEdit = ImageButton(this)
            buttonEdit.setImageResource(R.drawable.ic_edit)
            buttonEdit.setBackgroundColor(android.graphics.Color.TRANSPARENT)
            buttonEdit.setPadding(5, 5, 5, 5)
            buttonEdit.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("pacienteId", paciente["id"]?.toInt())
                }
                startActivity(intent)
            }
            tableRow.addView(buttonEdit)

            // Botão de excluir
            val buttonDelete = ImageButton(this)
            buttonDelete.setImageResource(R.drawable.ic_delete) // Substitua com o nome do seu ícone de exclusão
            buttonDelete.setBackgroundColor(android.graphics.Color.TRANSPARENT)
            buttonDelete.setPadding(5, 5, 5, 5)
            tableRow.addView(buttonDelete)

            tablePacientes.addView(tableRow)

            // Implementar a funcionalidade do botão de excluir
            buttonDelete.setOnClickListener {
                dbHelper.excluirPaciente(paciente["id"]!!)
                carregarPacientes()
            }
        }
    }
}
