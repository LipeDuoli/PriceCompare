package br.com.duoli.pricecompare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;

import java.math.BigDecimal;

import br.com.duoli.pricecompare.R;
import br.com.duoli.pricecompare.model.Product;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddProductActivity extends AppCompatActivity {

    @BindView(R.id.activity_product_edittext_nome)
    EditText mNome;
    @BindView(R.id.activity_product_edittext_preco)
    CurrencyEditText mPreco;
    @BindView(R.id.activity_product_edittext_peso)
    EditText mPeso;
    @BindView(R.id.activity_product_checkBox_pack)
    CheckBox mPack;
    @BindView(R.id.activity_product_radiogroup_tipo)
    RadioGroup mTipo;
    @BindView(R.id.activity_product_editText_qtunits)
    EditText mQtUnits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_product, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                if (!allFieldsFilled()) {
                    Toast.makeText(this, getString(R.string.dialog_toast_campo_vazio), Toast.LENGTH_SHORT).show();
                    return false;
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra("produto", montaProduto());
                setResult(RESULT_OK, resultIntent);
                finish();
                return true;
        }
        return false;
    }

    private boolean allFieldsFilled() {
        if (isEmpty(mPeso) || isEmpty(mPreco) || mTipo.getCheckedRadioButtonId() == -1){
            return false;
        }
        if (mPack.isChecked()){
            if(isEmpty(mQtUnits)){
                return false;
            }
        }
        return true;
    }

    private Product montaProduto() {
        String nome = mNome.getText().toString();

        BigDecimal preco = BigDecimal.valueOf(mPreco.getRawValue(), 2);
        double peso = Double.valueOf(mPeso.getText().toString());
        if(mPack.isChecked()){
            peso *= Integer.valueOf(mQtUnits.getText().toString());
        }
        int checkedRadioButtonId = mTipo.getCheckedRadioButtonId();
        RadioButton tipoMarcado = (RadioButton) findViewById(checkedRadioButtonId);

        return new Product(nome, preco, peso, tipoMarcado.getText().toString());
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    public void onCheckboxClicked(View view){
        if(((CheckBox) view).isChecked()) {
            mQtUnits.setVisibility(View.VISIBLE);
        } else {
            mQtUnits.setVisibility(View.GONE);
        }
    }
}
