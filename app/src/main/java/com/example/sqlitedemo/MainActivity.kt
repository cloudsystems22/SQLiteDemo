package com.example.sqlitedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var edName:EditText
    private lateinit var edEmail:EditText
    private lateinit var btnAdd:Button
    private lateinit var btnView:Button
    private lateinit var btnUpdate:Button

    private lateinit var sqLiteHelper: SQLiteHelper
    private lateinit var recyclerView: RecyclerView

    private var adapter: StudentAdapter? = null
    private var std:StudentModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initview()
        initRecyclerView()
        sqLiteHelper = SQLiteHelper(this)

        btnAdd.setOnClickListener{
            addStudent()
        }

        btnView.setOnClickListener {
            getStudent()
        }

        btnUpdate.setOnClickListener{
            updateStudent()
        }

        adapter?.setOnClickItem {
            Toast.makeText(this, it.Name, Toast.LENGTH_SHORT).show()
            edName.setText(it.Name)
            edEmail.setText(it.Email)

            std = it

        }

        adapter?.setOnClickDeleteItem {
            deleteStudent(it.Id)
        }
    }

    private fun getStudent(){
        val stdList = sqLiteHelper.getAllStudent()
        Log.e("pppp", "${stdList.size}")

        adapter?.addItems(stdList)
    }

    private fun addStudent(){
        val name = edName.text.toString()
        val email = edEmail.text.toString()

        if(name.isEmpty() || email.isEmpty()){
            Toast.makeText(this,"Entre com nome", Toast.LENGTH_SHORT).show()
        } else {
            val std = StudentModel(Name = name, Email = email)
            val status = sqLiteHelper.insertStudent(std)

            if(status >- 1){
                Toast.makeText(this, "Estudante adicionado!", Toast.LENGTH_SHORT).show()
                clearEditText()
                getStudent()
            } else {
                Toast.makeText(this, "Erro ao salvar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateStudent(){
        val name = edName.text.toString()
        val email = edEmail.text.toString()

        if(name == std?.Name && email == std?.Email){
            Toast.makeText(this, "Não atualizado!", Toast.LENGTH_SHORT).show()
            return
        }

        if(std == null) return

        val std = StudentModel(std!!.Id, name, email)
        val status = sqLiteHelper.updateStudent(std)

        if(status > -1){
            clearEditText()
            getStudent()
        } else {
            Toast.makeText(this, "Erro ao atualizar!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun deleteStudent(id:Int){
        if(id == null) return

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Tem certeza que quer excluir esse registro?")
        builder.setCancelable(true)
        builder.setPositiveButton("Sim"){ dialog, _->
            sqLiteHelper.deleteStudent(id)
            getStudent()
            dialog.dismiss()
        }
        builder.setNegativeButton("Não"){ dialog, _->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }

    private fun clearEditText(){
        edName.setText("")
        edEmail.setText("")
        edName.requestFocus()
    }

    private fun initRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter()
        recyclerView.adapter = adapter
    }

    private fun initview(){
        edName = findViewById(R.id.edName)
        edEmail = findViewById(R.id.edEmail)
        btnAdd = findViewById(R.id.btnAdd)
        btnView = findViewById(R.id.btnView)
        btnUpdate = findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerView)
    }

}