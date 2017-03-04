package br.com.lipeduoli.pricecompare;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import br.com.lipeduoli.pricecompare.adapter.ProductAdapter;
import br.com.lipeduoli.pricecompare.dialog.DialogAddProduct;
import br.com.lipeduoli.pricecompare.model.Product;
import br.com.lipeduoli.pricecompare.tools.DividerItemDecoration;
import br.com.lipeduoli.pricecompare.tools.LeftSwipeDelete;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mProductAdapter = new ProductAdapter(this, new ArrayList<Product>());
        mProductListView.setHasFixedSize(true);
        mProductListView.setLayoutManager(new LinearLayoutManager(this));
        mProductListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new LeftSwipeDelete(mProductAdapter));
        itemTouchHelper.attachToRecyclerView(mProductListView);

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
        mProductAdapter.addItem(product);
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
                mProductAdapter.clearList();
                Toast.makeText(this, "Lista Limpa", Toast.LENGTH_SHORT).show();
                return true;
        }

        return false;
    }
}
