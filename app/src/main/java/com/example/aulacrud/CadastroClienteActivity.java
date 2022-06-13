package com.example.aulacrud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CadastroClienteActivity extends AppCompatActivity {

    private EditText nome,cpf,telefone;
    private ClienteDAO dao;
    private Cliente cliente = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cliente);

        nome = findViewById(R.id.editNome);
        cpf = findViewById(R.id.editCPF);
        telefone = findViewById(R.id.editTelefone);
        dao = new ClienteDAO(this);

        Intent it = getIntent();
        if(it.hasExtra("cliente")) {
            cliente = (Cliente) it.getSerializableExtra("cliente");
            nome.setText(cliente.getNome());
            cpf.setText(cliente.getCpf());
            telefone.setText(cliente.getTelefone());
        }
    }

    public void salvar(View v) {

        if (cliente == null) {
            Cliente c = new Cliente();
            c.setNome(nome.getText().toString());
            c.setCpf(cpf.getText().toString());
            c.setTelefone(telefone.getText().toString());
            long id = dao.inserir(c);
            Toast.makeText(this, "Cliente inserido com o id: " + id, Toast.LENGTH_LONG).show();
        } else {
            cliente.setNome(nome.getText().toString());
            cliente.setCpf(cpf.getText().toString());
            cliente.setTelefone(telefone.getText().toString());
            dao.atualizar(cliente);
            Toast.makeText(this, "Cliente atualizado com sucesso!", Toast.LENGTH_LONG).show();
        }
    }
}