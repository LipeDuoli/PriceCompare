package br.com.lipeduoli.pricecompare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

import br.com.lipeduoli.pricecompare.R;
import br.com.lipeduoli.pricecompare.model.Product;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements Serializable {

    private Context mContext;
    private List<Product> mProductList;
    private Product mProdutoMaisBarato;

    public ProductAdapter(Context context, List<Product> products) {
        this.mContext = context;
        this.mProductList = products;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product, null, false);
        return new ViewHolder(mContext, v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = mProductList.get(position);

        if (!product.getNome().isEmpty()){
            holder.mProduto.setText(product.getNome());
        } else {
            String nomeProduto = "Produto " + (position + 1);
            holder.mProduto.setText(nomeProduto);
        }

        holder.mPeso.setText(String.format(mContext.getString(R.string.list_peso), Integer.toString(product.getPeso()), product.getTipo()));
        holder.mPreco.setText(String.format(mContext.getString(R.string.list_preco), doubleToCurrency(product.getPreco(), 2)));

        holder.mValorProduto.setText(String.format(mContext.getString(R.string.list_valor_produto), product.getTipo(), doubleToCurrency(product.getValorPorPeso(), 3)));

        if (product.isMenorValor()){
            holder.mMaisBarato.setVisibility(View.VISIBLE);
        } else {
            holder.mMaisBarato.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }


    public void addItem(Product product) {
        Product produtoVerificado = verificaMenorValorProduto(product);
        mProductList.add(produtoVerificado);
        notifyDataSetChanged();
    }

    private String doubleToCurrency(BigDecimal value, int numberOfDigits) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMinimumFractionDigits(numberOfDigits);
        return format.format(value);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.list_product_textview_produto)
        TextView mProduto;
        @BindView(R.id.list_product_textview_peso)
        TextView mPeso;
        @BindView(R.id.list_product_textview_preco)
        TextView mPreco;
        @BindView(R.id.list_produto_textview_valor_produto)
        TextView mValorProduto;
        @BindView(R.id.list_product_imageview_mais_barato)
        ImageView mMaisBarato;

        public ViewHolder(final Context context, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition(); // gets item position
                    Product product = mProductList.get(position);
                    if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                        // We can access the data within the views
                        Toast.makeText(context, "Produto Deletado", Toast.LENGTH_SHORT).show();
                        deleteProduct(position);
                    }
                    return true;
                }
            });
        }

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

    private void deleteProduct(int position) {
        mProductList.remove(position);
        recalculaProdutoMaisBarato();
        notifyDataSetChanged();
    }

    private void recalculaProdutoMaisBarato() {
        mProdutoMaisBarato = null;
        for (Product p : mProductList) {
            if (mProdutoMaisBarato == null){
                p.setMenorValor(true);
                mProdutoMaisBarato = p;
            } else {
                if (p.getValorPorPeso().compareTo(mProdutoMaisBarato.getValorPorPeso()) == -1){
                    mProductList.get(mProductList.indexOf(mProdutoMaisBarato)).setMenorValor(false);
                    p.setMenorValor(true);
                    mProdutoMaisBarato = p;
                }
            }
        }
    }

    public void clearList(){
        mProductList.clear();
        notifyDataSetChanged();
        mProdutoMaisBarato = null;
    }
}
