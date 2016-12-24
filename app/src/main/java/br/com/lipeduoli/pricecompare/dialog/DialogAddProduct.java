package br.com.lipeduoli.pricecompare.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;

import java.math.BigDecimal;

import br.com.lipeduoli.pricecompare.R;
import br.com.lipeduoli.pricecompare.model.Product;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liped on 06/07/2016.
 */
public class DialogAddProduct extends DialogFragment {

    ProductDialogListener mListener;
    @BindView(R.id.dialog_edittext_preco)
    CurrencyEditText mPreco;
    @BindView(R.id.dialog_edittext_peso)
    EditText mPeso;
    @BindView(R.id.dialog_radiogroup_tipo)
    RadioGroup mTipo;
    @BindView(R.id.dialog_edittext_nome)
    EditText mNome;

    private View view;

    public interface ProductDialogListener {
        public void onDialogPositiveClick(Product product);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_add_product, null);
        ButterKnife.bind(this, view);
        builder.setTitle("Adicionar Produto");
//        mPreco.addTextChangedListener(new CurrencyTextWatcher());
        builder.setView(view)
                .setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DialogAddProduct.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    private Product montaProduto() {
        String nome = mNome.getText().toString();

        BigDecimal preco = BigDecimal.valueOf(mPreco.getRawValue(), 2);
        int peso = Integer.valueOf(mPeso.getText().toString());

        int checkedRadioButtonId = mTipo.getCheckedRadioButtonId();
        RadioButton tipoMarcado = (RadioButton) view.findViewById(checkedRadioButtonId);

        return new Product(nome, preco, peso, tipoMarcado.getText().toString());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ProductDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ProductDialogListener");
        }
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEmpty(mPeso) || isEmpty(mPreco) || mTipo.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getContext(), "Preencha os campos com *", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    d.dismiss();
                    mListener.onDialogPositiveClick(montaProduto());
                }
            });
        }
    }

}
