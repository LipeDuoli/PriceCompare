package br.com.lipeduoli.pricecompare;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import br.com.lipeduoli.pricecompare.adapter.ProductAdapter;
import br.com.lipeduoli.pricecompare.dialog.DialogAddProduct;
import br.com.lipeduoli.pricecompare.model.Product;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements DialogAddProduct.ProductDialogListener {

    private FirebaseAnalytics mFirebaseAnalytics;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.activity_main_recyclerview_product_list)
    RecyclerView mProductListView;

    private ProductAdapter mProductAdapter;
    private List<Product> mProductList;
    private Product mProdutoMaisBarato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mProductList = new ArrayList<>();
        mProductAdapter = new ProductAdapter(this, mProductList);
        mProductListView.setHasFixedSize(true);
        mProductListView.setLayoutManager(new LinearLayoutManager(this));
        mProductListView.setAdapter(mProductAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @OnClick(R.id.fab)
    public void onClick(View view) {
        DialogAddProduct dialog = new DialogAddProduct();
        dialog.show(getSupportFragmentManager(), "productDialog");

    }

    @Override
    public void onDialogPositiveClick(Product product) {
        Product produtoVerificado = verificaMenorValorProduto(product);
        mProductAdapter.addItem(produtoVerificado);
    }

    private Product verificaMenorValorProduto(Product product) {
        if (mProdutoMaisBarato == null){
            mProdutoMaisBarato = product;
        } else {
            if (product.getValorPorPeso().compareTo(mProdutoMaisBarato.getValorPorPeso()) == -1){
                mProductList.get(mProductList.indexOf(mProdutoMaisBarato)).setMenorValor(false);
                product.setMenorValor(true);
                mProdutoMaisBarato = product;
                return product;
            }
        }
        product.setMenorValor(false);
        return product;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clean:
                mProductList.clear();
                mProductAdapter.notifyDataSetChanged();
                mProdutoMaisBarato = null;
                Toast.makeText(this, "Lista Limpa", Toast.LENGTH_SHORT).show();
                return true;
        }

        return false;
    }
}
