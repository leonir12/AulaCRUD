package com.example.aulacrud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListarClientesActivity extends AppCompatActivity {

    private ListView listView;
    private ClienteDAO dao;
    private List<Cliente> clientes;
    private List<Cliente> clientesFiltrados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_clientes);

        listView = findViewById(R.id.lista_clientes);
        dao = new ClienteDAO(this);
        clientes = dao.obterTodos();
        clientesFiltrados.addAll(clientes);
        ArrayAdapter<Cliente> adaptador = new ArrayAdapter<Cliente>(this, android.R.layout.simple_list_item_1,clientesFiltrados);
        listView.setAdapter(adaptador);
        registerForContextMenu(listView);
    }

    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_principal,menu);

        SearchView sv = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                procuraCliente(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_contexto, menu);
    }

    public void procuraCliente(String nome) {
        clientesFiltrados.clear();
        for (Cliente c : clientes) {
            if (c.getNome().toLowerCase().contains(nome.toLowerCase())) {
                clientesFiltrados.add(c);
            }
        }
        listView.invalidateViews();
    }

    public void excluir(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Cliente clienteExcluir = clientesFiltrados.get(menuInfo.position);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Atenção")
                .setMessage("Você deseja excluir o cliente?")
                .setNegativeButton("NÃO",null)
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clientesFiltrados.remove(clienteExcluir);
                        clientes.remove(clienteExcluir);
                        dao.excluir(clienteExcluir);
                        listView.invalidateViews();
                    }
                }).create();
        dialog.show();
    }

    public void cadastrar (MenuItem item) {
        Intent it = new Intent(this,CadastroClienteActivity.class);
        startActivity(it);
    }

    public void atualizar (MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Cliente clienteAtualizar = clientesFiltrados.get(menuInfo.position);
        Intent it = new Intent(this,CadastroClienteActivity.class);
        it.putExtra("cliente", clienteAtualizar);
        startActivity(it);

    }

    @Override
    public void onResume() {
        super.onResume();
        clientes = dao.obterTodos();
        clientesFiltrados.clear();
        clientesFiltrados.addAll(clientes);
        listView.invalidateViews();
    }
}