package com.example.hamburgueriaz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private int quantidade = 1;
    private final int precoBase = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Referências às views
        TextView txtQuantidade = findViewById(R.id.txtQuantidade);
        TextView txtResumeValue = findViewById(R.id.txtResumeValue);
        Button btnAdicionar = findViewById(R.id.btnAdicionar);
        Button btnSubtrair = findViewById(R.id.btnSubtrair);
        Button btnEnviarPedido = findViewById(R.id.btnEnviarPedido);
        CheckBox checkBacon = findViewById(R.id.checkBacon);
        CheckBox checkQueijo = findViewById(R.id.checkQueijo);
        CheckBox checkOnion = findViewById(R.id.checkOnion);

        atualizarQuantidade(txtQuantidade, txtResumeValue);
        calcularPrecoTotal();


        CompoundButton.OnCheckedChangeListener listener = (buttonView, isChecked) -> {
            atualizarQuantidade(txtQuantidade, txtResumeValue);
            calcularPrecoTotal();
        };

        checkBacon.setOnCheckedChangeListener(listener);
        checkQueijo.setOnCheckedChangeListener(listener);
        checkOnion.setOnCheckedChangeListener(listener);


        btnAdicionar.setOnClickListener(v -> {
            quantidade++;
            atualizarQuantidade(txtQuantidade, txtResumeValue);
        });

        btnSubtrair.setOnClickListener(v -> {
            if (quantidade > 1) {
                quantidade--;
                atualizarQuantidade(txtQuantidade, txtResumeValue);
            } else {
                Toast.makeText(this, "Quantidade mínima é 1", Toast.LENGTH_SHORT).show();
            }
        });

        btnEnviarPedido.setOnClickListener(this::enviarPedido);


    }

    private void atualizarQuantidade(TextView txtQuantidade, TextView txtPrecoTotal) {
        txtQuantidade.setText(String.valueOf(quantidade));
        int precoTotal = calcularPrecoTotal();
        txtPrecoTotal.setText("R$ " + precoTotal);
    }

    private int calcularPrecoTotal() {
        CheckBox checkBacon = findViewById(R.id.checkBacon);
        CheckBox checkQueijo = findViewById(R.id.checkQueijo);
        CheckBox checkOnion = findViewById(R.id.checkOnion);

        int adicionais = 0;
        if (checkBacon.isChecked()) adicionais += 2;
        if (checkQueijo.isChecked()) adicionais += 2;
        if (checkOnion.isChecked()) adicionais += 3;

        return (precoBase + adicionais) * quantidade;
    }

    private void enviarPedido(View view) {
        EditText nomeCliente = findViewById(R.id.nome);
        CheckBox checkBacon = findViewById(R.id.checkBacon);
        CheckBox checkQueijo = findViewById(R.id.checkQueijo);
        CheckBox checkOnion = findViewById(R.id.checkOnion);
        TextView txtPrecoTotal = findViewById(R.id.txtResumeValue);

        String nome = nomeCliente.getText().toString();
        if (nome.isEmpty()) {
            Toast.makeText(this, "Por favor, insira o nome do cliente", Toast.LENGTH_SHORT).show();
            return;
        }

        String resumoPedido = "Nome do cliente: " + nome + "\n" +
                "Tem Bacon? " + (checkBacon.isChecked() ? "Sim" : "Não") + "\n" +
                "Tem Queijo? " + (checkQueijo.isChecked() ? "Sim" : "Não") + "\n" +
                "Tem Onion Rings? " + (checkOnion.isChecked() ? "Sim" : "Não") + "\n" +
                "Quantidade: " + quantidade + "\n" +
                "Preço Final: " + txtPrecoTotal.getText().toString();


        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Pedido de " + nome);
        intent.putExtra(Intent.EXTRA_TEXT, resumoPedido);

        try {
            startActivity(Intent.createChooser(intent, "Escolha o aplicativo de e-mail"));
        } catch (Exception e) {
            Toast.makeText(this, "Nenhum aplicativo de e-mail encontrado", Toast.LENGTH_SHORT).show();
        }
    }
}