package com.example.denguecheckerapp

import android.content.Context
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
        CREATE TABLE pacientes (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nome VARCHAR(100) NOT NULL,
            idade INTEGER NOT NULL,
            regiao VARCHAR(50) NOT NULL,
            sintomas TEXT NOT NULL,
            diagnostico VARCHAR(200) NOT NULL
        )
        """.trimIndent())

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Atualização de tabelas
    }

    fun inserirPaciente(nome: String, idade: Int, regiao: String, sintomas: String, diagnostico: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nome", nome)
            put("idade", idade)
            put("regiao", regiao)
            put("sintomas", sintomas)
            put("diagnostico", diagnostico)
        }
        return db.insert("pacientes", null, values)
    }

    fun listarPacientes(): List<Map<String, String>> {
        val db = readableDatabase
        val cursor = db.query("pacientes", null, null, null, null, null, null)
        val pacientes = mutableListOf<Map<String, String>>()

        with(cursor) {
            while (moveToNext()) {
                val paciente = mutableMapOf<String, String>()
                paciente["id"] = getString(getColumnIndexOrThrow("id"))
                paciente["nome"] = getString(getColumnIndexOrThrow("nome"))
                paciente["idade"] = getString(getColumnIndexOrThrow("idade"))
                paciente["regiao"] = getString(getColumnIndexOrThrow("regiao"))
                paciente["sintomas"] = getString(getColumnIndexOrThrow("sintomas"))
                paciente["diagnostico"] = getString(getColumnIndexOrThrow("diagnostico"))
                pacientes.add(paciente)
            }
        }
        cursor.close()
        return pacientes
    }

    fun excluirPaciente(id: String) {
        val db = writableDatabase
        println(arrayOf(id))
        db.delete("pacientes", "id = ?", arrayOf(id))
    }

    fun atualizarPaciente(id: Int, nome: String, idade: Int, estado: String, sintomas: String, diagnostico: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("nome", nome)
        contentValues.put("idade", idade)
        contentValues.put("regiao", estado)
        contentValues.put("sintomas", sintomas)
        contentValues.put("diagnostico", diagnostico)
        db.update("pacientes", contentValues, "id=?", arrayOf(id.toString()))
        db.close()
    }

    fun obterPaciente(id: Int): Map<String, String> {
        val db = this.readableDatabase
        val query = "SELECT * FROM pacientes WHERE id = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString()))
        cursor.moveToFirst()

        val paciente = mapOf(
            "nome" to cursor.getString(cursor.getColumnIndexOrThrow("nome")),
            "idade" to cursor.getInt(cursor.getColumnIndexOrThrow("idade")).toString(),
            "estado" to cursor.getString(cursor.getColumnIndexOrThrow("regiao")),
            "sintomas" to cursor.getString(cursor.getColumnIndexOrThrow("sintomas")),
            "diagnostico" to cursor.getString(cursor.getColumnIndexOrThrow("diagnostico"))
        )

        cursor.close()
        db.close()
        return paciente
    }


    companion object {
        private const val DATABASE_NAME = "database.db"
        private const val DATABASE_VERSION = 1
    }


}