package com.example.dengueapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.denguecheckerapp.DatabaseHelper
import com.example.denguecheckerapp.R

class MainActivity : AppCompatActivity() {

    private lateinit var editTextNome: EditText
    private lateinit var editTextIdade: EditText
    private lateinit var spinnerEstado: Spinner
    private lateinit var checkBoxFebreAlta: CheckBox
    private lateinit var checkBoxDorCabeca: CheckBox
    private lateinit var checkBoxDorOlhos: CheckBox
    private lateinit var checkBoxManchasPele: CheckBox
    private lateinit var checkBoxDoresCorpo: CheckBox
    private lateinit var checkBoxDoresMusculos: CheckBox
    private lateinit var checkBoxNausea: CheckBox
    private lateinit var checkBoxVomito: CheckBox
    private lateinit var buttonEnviar: Button
    private lateinit var botaoAnalise: Button
    private lateinit var botaoPacientes: Button

    private var pacienteId: Int? = null // variável para armazenar o ID do paciente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)

        // Referências para os componentes
        editTextNome = findViewById(R.id.editTextNome)
        editTextIdade = findViewById(R.id.editTextIdade)
        spinnerEstado = findViewById(R.id.spinnerEstado)
        checkBoxFebreAlta = findViewById(R.id.checkBoxFebreAlta)
        checkBoxDorCabeca = findViewById(R.id.checkBoxDorCabeca)
        checkBoxDorOlhos = findViewById(R.id.checkBoxDorOlhos)
        checkBoxManchasPele = findViewById(R.id.checkBoxManchasPele)
        checkBoxDoresCorpo = findViewById(R.id.checkBoxDoresCorpo)
        checkBoxDoresMusculos = findViewById(R.id.checkBoxDoresMusculos)
        checkBoxNausea = findViewById(R.id.checkBoxNausea)
        checkBoxVomito = findViewById(R.id.checkBoxVomito)
        buttonEnviar = findViewById(R.id.buttonEnviar)
        botaoAnalise = findViewById(R.id.botaoAnalise)
        botaoPacientes = findViewById(R.id.botaoPacientes)

        // Configurar o Spinner com os estados
        val adapter = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.array_estados,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEstado.adapter = adapter

        // Verificar se estamos editando um paciente
        pacienteId = intent.getIntExtra("pacienteId", -1)
        if (pacienteId != -1) {
            preencherFormularioComDadosDoPaciente(pacienteId!!)
        }

        // Botão Enviar
        buttonEnviar.setOnClickListener {
            val nome = editTextNome.text.toString()
            val idade = editTextIdade.text.toString().toInt()
            val estado = spinnerEstado.selectedItem.toString()
            val sintomas = arrayListOf<String>()

            if (checkBoxFebreAlta.isChecked) sintomas.add(getString(R.string.febre))
            if (checkBoxDorCabeca.isChecked) sintomas.add(getString(R.string.dor_de_cabeca))
            if (checkBoxDorOlhos.isChecked) sintomas.add(getString(R.string.dor_atras_dos_olhos))
            if (checkBoxManchasPele.isChecked) sintomas.add(getString(R.string.manchas_na_pele))
            if (checkBoxDoresCorpo.isChecked) sintomas.add(getString(R.string.dores_no_corpo))
            if (checkBoxDoresMusculos.isChecked) sintomas.add(getString(R.string.dores_musculares))
            if (checkBoxNausea.isChecked) sintomas.add(getString(R.string.nausea))
            if (checkBoxVomito.isChecked) sintomas.add(getString(R.string.vomito))

            val sintomasConcatenados = sintomas.joinToString(", ")

            // Verificar se o diagnóstico é positivo para dengue
            val diagnosticadoComDengue = verificarDiagnostico(sintomas)
            val diagnosticoString = if (diagnosticadoComDengue) "Dengue positivo" else "Dengue negativo"

            // Salvar ou atualizar dados no banco de dados
            val dbHelper = DatabaseHelper(this)
            if (pacienteId != -1) {
                dbHelper.atualizarPaciente(pacienteId!!, nome, idade, estado, sintomasConcatenados, diagnosticoString)
            } else {
                dbHelper.inserirPaciente(nome, idade, estado, sintomasConcatenados, diagnosticoString)
            }

            // Enviar dados para a ResultadosActivity
            val intent = Intent(this, ResultadosActivity::class.java).apply {
                putExtra("nome", nome)
                putExtra("idade", idade.toString())
                putExtra("estado", estado)
                putExtra("diagnosticoDengue", diagnosticadoComDengue)
            }
            startActivity(intent)
        }

        // Botão Análise
        botaoAnalise.setOnClickListener {
            // Iniciar a atividade AnaliseActivity ao clicar no botão
            val intent = Intent(this, AnaliseActivity::class.java)
            startActivity(intent)
        }

        // Botão Pacientes
        botaoPacientes.setOnClickListener {
            // Iniciar a atividade ListaPacientesActivity ao clicar no botão
            val intent = Intent(this, ListaPacientesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun preencherFormularioComDadosDoPaciente(id: Int) {
        val dbHelper = DatabaseHelper(this)
        val paciente = dbHelper.obterPaciente(id)
        editTextNome.setText(paciente["nome"])
        editTextIdade.setText(paciente["idade"])
        val estadoPos = resources.getStringArray(R.array.array_estados).indexOf(paciente["estado"])
        spinnerEstado.setSelection(estadoPos)

        val sintomas = paciente["sintomas"]?.split(", ") ?: emptyList()
        checkBoxFebreAlta.isChecked = sintomas.contains(getString(R.string.febre))
        checkBoxDorCabeca.isChecked = sintomas.contains(getString(R.string.dor_de_cabeca))
        checkBoxDorOlhos.isChecked = sintomas.contains(getString(R.string.dor_atras_dos_olhos))
        checkBoxManchasPele.isChecked = sintomas.contains(getString(R.string.manchas_na_pele))
        checkBoxDoresCorpo.isChecked = sintomas.contains(getString(R.string.dores_no_corpo))
        checkBoxDoresMusculos.isChecked = sintomas.contains(getString(R.string.dores_musculares))
        checkBoxNausea.isChecked = sintomas.contains(getString(R.string.nausea))
        checkBoxVomito.isChecked = sintomas.contains(getString(R.string.vomito))
    }

    private fun verificarDiagnostico(sintomas: ArrayList<String>): Boolean {
        // Lógica para verificar se os sintomas indicam dengue
        return sintomas.size >= 3
    }
}
