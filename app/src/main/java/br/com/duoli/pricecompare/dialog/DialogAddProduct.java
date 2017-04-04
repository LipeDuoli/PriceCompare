package br.com.duoli.pricecompare.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import br.com.duoli.pricecompare.R;
import br.com.duoli.pricecompare.model.Product;
import butterknife.BindView;
import butterknife.ButterKnife;

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
        void onDialogPositiveClick(Product product);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_add_product, null);
        ButterKnife.bind(this, view);
        builder.setTitle(getString(R.string.dialog_title));
        builder.setView(view)
                .setPositiveButton(getString(R.string.dialog_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(getString(R.string.dialog_negative_button), new DialogInterface.OnClickListener() {
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
        double peso = Double.valueOf(mPeso.getText().toString());

        int checkedRadioButtonId = mTipo.getCheckedRadioButtonId();
        RadioButton tipoMarcado = (RadioButton) view.findViewById(checkedRadioButtonId);

        return new Product(nome, preco, peso, tipoMarcado.getText().toString());
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEmpty(mPeso) || isEmpty(mPreco) || mTipo.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getContext(), getString(R.string.dialog_toast_campo_vazio), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    d.dismiss();
                    mListener.onDialogPositiveClick(montaProduto());
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (ProductDialogListener) getActivity();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }

    }
}
